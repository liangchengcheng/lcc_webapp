package com.lcc.jk.dao.impl;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lcc.jk.dao.PackingListDao;
import com.lcc.jk.domain.PackingList;

@Repository
public class PackingListDaoImpl extends BaseDaoImpl<PackingList> implements PackingListDao{
	public PackingListDaoImpl() {
		//设置命名空间
		super.setNs("com.lcc.jk.mapper.PackingListMapper");
	}

	public void updateState(Map map) {
		super.getSqlSession().update(super.getNs()+".updateState", map);
	}
}
