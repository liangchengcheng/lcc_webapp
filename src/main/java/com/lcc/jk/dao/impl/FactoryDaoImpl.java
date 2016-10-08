package com.lcc.jk.dao.impl;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lcc.jk.dao.FactoryDao;
import com.lcc.jk.domain.Factory;

@Repository
public class FactoryDaoImpl extends BaseDaoImpl<Factory> implements FactoryDao{
	public FactoryDaoImpl() {
		//设置命名空间
		super.setNs("com.lcc.jk.mapper.FactoryMapper");
	}

	public void updateState(Map map) {
		super.getSqlSession().update(super.getNs()+".updateState", map);
	}
}
