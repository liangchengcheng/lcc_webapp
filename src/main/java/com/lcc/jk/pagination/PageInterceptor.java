package com.lcc.jk.pagination;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;

@Intercepts({ @Signature(method = "prepare", type = StatementHandler.class, args = { Connection.class }) })
public class PageInterceptor implements Interceptor {

	// 数据库类型，不同的数据库不同的分页方式
	private String databaseType;

	/**
	 * 拦截之后要执行的方法
	 */
	public Object intercept(Invocation invocation) throws Throwable {
		// 对于StatementHandler其实只有两个实现类，一个是RoutingStatementHandler，
		// 另一个是抽象类BaseStatementHandler，
		// BaseStatementHandler有三个子类，分别是SimpleStatementHandler，
		// PreparedStatementHandler和CallableStatementHandler，

		// SimpleStatementHandler是用于处理Statement的，PreparedStatementHandler是处理PreparedStatement的，
		// 而CallableStatementHandler是处理CallableStatement的。

		// Mybatis在进行Sql语句处理的时候都是建立的 RoutingStatementHandler，
		// 而在RoutingStatementHandler里面拥有一个StatementHandler类型的 delegate 属性，

		// RoutingStatementHandler会依据Statement的不同建立对应的BaseStatementHandler，
		// 即SimpleStatementHandler、
		// PreparedStatementHandler或CallableStatementHandler，

		// 在RoutingStatementHandler里面所有StatementHandler接口方法的实现都是调用的delegate对应的方法。
		// 我们在PageInterceptor类上已经用@Signature标记了该Interceptor只拦截StatementHandler接口的
		// prepare 方法，

		// 又因为Mybatis只有在建立RoutingStatementHandler的时候是通过Interceptor的plugin方法进行包裹的，
		// 所以我们这里拦截到的目标对象肯定是RoutingStatementHandler对象。

		RoutingStatementHandler handler = (RoutingStatementHandler) invocation.getTarget();
		// 通过反射获取到当前RoutingStatementHandler对象的delegate属性
		StatementHandler delegate = (StatementHandler) ReflectUtil.getFieldValue(handler, "delegate");

		// 获取到当前的statmentHandler的boundsql，这里不管是调用handler的getBoundsql还是直接调用
		// delegate 的getBoundsql的结果都是一样的
		// 因为之前已经说过了
		// RoutingStatmentHandler实现所有的StatmentHandler接口方法里面都是调用delegate里面的方法
		BoundSql boundSql = delegate.getBoundSql();

		// 拿到当前绑定的sql的参数对象，就是我们在调用对应的Mapper映射语句时候传入的参数
		Object obj = boundSql.getParameterObject();

		// 只拦截Page对象的其他放过
		if (obj instanceof Page<?>) {
			Page<?> page = (Page<?>) obj;
			// 通过反射获取delegate父类的BaseStatementHandler的mappedStatment属性
			MappedStatement mappedStatement = (MappedStatement) ReflectUtil.getFieldValue(delegate, "mappedStatement");
			// 拦截到的prepare方法参数是一个connection对象
			Connection connection = (Connection) invocation.getArgs()[0];
			// 获取当前要执行的sql语句，也就是我们直接在Mapper映射语句里面写的sql
			String sql = boundSql.getSql();
			// 给当前的page参数对象设置总记录数
			this.setTotalRecord(page, mappedStatement, connection);
			// 获取分页的sql的语句
			String pageSql = this.getPageSql(page, sql);
			// 利用反射设置当前的BoundSql对应的sql属性为我们建立好的分页sql语句
			ReflectUtil.setFieldValue(boundSql, "sql", pageSql);
		}
		return invocation.proceed();
	}

	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	public void setProperties(Properties properties) {
		this.databaseType = properties.getProperty("databaseType");
	}

	/**
	 * 根据page对象获取对应的分页查询Sql语句，这里只做了两种数据库类型，Mysql和Oracle 其它的数据库都 没有进行分页
	 * 
	 * @param page
	 *            分页对象
	 * @param sql
	 *            原sql语句
	 * @return
	 */
	private String getPageSql(Page<?> page, String sql) {
		StringBuffer sqlBuffer = new StringBuffer(sql);
		if ("mysql".equalsIgnoreCase(databaseType)) {
			return getMysqlPageSql(page, sqlBuffer);
		} else if ("oracle".equalsIgnoreCase(databaseType)) {
			return getOraclePageSql(page, sqlBuffer);
		}

		return sqlBuffer.toString();
	}

	/**
	 * 获取Mysql数据库的分页查询语句
	 * 
	 * @param page
	 *            分页对象
	 * @param sqlBuffer
	 *            包含原sql语句的StringBuffer对象
	 * @return Mysql数据库分页语句
	 */
	private String getMysqlPageSql(Page<?> page, StringBuffer sqlBuffer) {
		// 计算第一条记录的位置，Mysql中记录的位置是从0开始的。
		int offset = (page.getPageNo() - 1) * page.getPageSize();
		sqlBuffer.append(" limit ").append(offset).append(",").append(page.getPageSize());
		return sqlBuffer.toString();
	}

	/**
	 * 获取Oracle数据库的分页查询语句
	 * 
	 * @param page
	 *            分页对象
	 * @param sqlBuffer
	 *            包含原sql语句的StringBuffer对象
	 * @return Oracle数据库的分页查询语句
	 */
	private String getOraclePageSql(Page<?> page, StringBuffer sqlBuffer) {
		// 计算第一条记录的位置，Oracle分页是通过rownum进行的，而rownum是从1开始的
		int offset = (page.getPageNo() - 1) * page.getPageSize() + 1;
		sqlBuffer.insert(0, "select u.*, rownum r from (").append(") u where rownum < ")
				.append(offset + page.getPageSize());
		sqlBuffer.insert(0, "select * from (").append(") where r >= ").append(offset);
		// 上面的Sql语句拼接之后大概是这个样子：
		// select * from (select u.*, rownum r from (select * from t_user) u
		// where rownum < 31) where r >= 10
		return sqlBuffer.toString();
	}

	private void setTotalRecord(Page<?> page, MappedStatement mappedStatement, Connection connection) {
		// 获取对应的boundsql，这个boundsql其实跟我们利用statementhandler获取的boundsql是一个对象
		// delegate里面的boundsql也是通过mappedstatment.getBoundSql(ParamObj)方法取到的
		BoundSql boundSql = mappedStatement.getBoundSql(page);
		// 获取到我们自己卸载Mapper映射语句中对应的sql语句
		String sql = boundSql.getSql();
		// 通过查询sql语句获取到对应的计算总记录数sql语句
		String countSql = this.getCountSql(sql);
		// 通过BoundSql获取对应的参数映射
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		// 利用Configuration . 查询记录数的sql语句的 countsql 参数映射关系paramenterMappings
		
		// 参数对象page建立查询记录数对应的BoundSql对象。
		BoundSql countBoundSql = new BoundSql(mappedStatement.getConfiguration(), countSql, parameterMappings, page);
		// 通过mappedStatement、参数对象page和BoundSql对象countBoundSql建立一个用于设定参数的ParameterHandler对象
		ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, page, countBoundSql);
		// 通过connection建立一个countsql对应的PreparedStatment对象
		PreparedStatement mstmt = null;
		ResultSet rSet = null;
		try {
			mstmt = connection.prepareStatement(countSql);
			// 通过parameterHandler给PreparedStatement对象设置参数
			parameterHandler.setParameters(mstmt);
			// 之后就是执行获取总的记录数的sql语句和获取结果了
			rSet = mstmt.executeQuery();
			if (rSet.next()) {
				int totalRecord = rSet.getInt(1);
				// 给当前的参数page对象设置总的记录数
				page.setTotalRecord(totalRecord);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rSet != null) {
					rSet.close();
				}

				if (mstmt != null) {
					mstmt.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	/**
	 * 根据原来的sql语句获取对应的查询总的记录数的sql语句
	 * 
	 * @param sql
	 * @return
	 */
	private String getCountSql(String sql) {
		int index = sql.indexOf("from");
		return "select count(*)" + sql.substring(index);
	}

	private static class ReflectUtil {

		/**
		 * 利用反射获取指定对象的指定属性
		 * 
		 * @param obj
		 *            目标对象
		 * @param fieldName
		 *            目标属性
		 * @return 目标属性的值
		 */
		public static Object getFieldValue(Object obj, String fieldName) {
			Object result = null;
			Field field = ReflectUtil.getField(obj, fieldName);
			if (field != null) {
				field.setAccessible(true);
				try {
					result = field.get(obj);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return result;
		}

		/**
		 * 利用反射获取指定对象里面的指定属性
		 * 
		 * @param obj
		 *            目标对象
		 * @param fieldName
		 *            目标属性
		 * @return 目标字段
		 */
		private static Field getField(Object obj, String fieldName) {
			Field field = null;
			for (Class<?> clazz = obj.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
				try {
					field = clazz.getDeclaredField(fieldName);
					break;
				} catch (NoSuchFieldException e) {
					// 这里不用做处理，子类没有该字段可能对应的父类有，都没有就返回null。
				}
			}
			return field;
		}

		/**
		 * 利用反射设置指定对象的指定属性为指定的值
		 * 
		 * @param obj
		 *            目标对象
		 * @param fieldName
		 *            目标属性
		 * @param fieldValue
		 *            目标值
		 */
		public static void setFieldValue(Object obj, String fieldName, String fieldValue) {
			Field field = ReflectUtil.getField(obj, fieldName);
			if (field != null) {
				try {
					field.setAccessible(true);
					field.set(obj, fieldValue);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
