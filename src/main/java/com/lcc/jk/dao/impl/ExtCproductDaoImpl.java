package com.lcc.jk.dao.impl;

import java.io.Serializable;

import org.hamcrest.core.Is;
import org.springframework.stereotype.Repository;

import com.lcc.jk.dao.ExtCproductDao;
import com.lcc.jk.domain.ExtCproduct;

@Repository
public class ExtCproductDaoImpl extends BaseDaoImpl<ExtCproduct> implements ExtCproductDao{
	public ExtCproductDaoImpl() {
		//设置命名空间
		super.setNs("com.lcc.jk.mapper.ExtCproductMapper");
	}

	public void deleteByContractProductById(Serializable[] ids) {
		super.getSqlSession().delete(super.getNs()+"deleteByContractProductById",ids);
		
	}

	public void deleteByContractId(Serializable[] contractIds) {
		super.getSqlSession().delete(super.getNs()+".deleteByContractId",contractIds);
		
	}

}
