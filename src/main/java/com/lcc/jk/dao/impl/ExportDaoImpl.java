package com.lcc.jk.dao.impl;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lcc.jk.dao.ExportDao;
import com.lcc.jk.domain.Export;

@Repository
public class ExportDaoImpl extends BaseDaoImpl<Export> implements ExportDao{

	public ExportDaoImpl() {
		//设置命名空间
		super.setNs("com.lcc.jk.mapper.ExportMapper");
	}
	
	public void updateState(Map map) {
		super.getSqlSession().update(super.getNs()+".updateState",map);
	}

}
