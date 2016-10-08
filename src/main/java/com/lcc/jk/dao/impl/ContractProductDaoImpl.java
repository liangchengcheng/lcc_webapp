package com.lcc.jk.dao.impl;

import java.io.Serializable;

import org.hamcrest.core.Is;
import org.springframework.stereotype.Repository;

import com.lcc.jk.dao.ContractProductDao;
import com.lcc.jk.domain.ContractProduct;

@Repository
public class ContractProductDaoImpl  extends BaseDaoImpl<ContractProduct> implements ContractProductDao{

	public ContractProductDaoImpl(){
		super.setNs("com.lcc.jk.mapper.ContractProductDaoImplMapper");
	}
	
	public void deleteByContractProductById(Serializable[] ids) {
		super.getSqlSession().delete(super.getNs()+".deleteByContractProductById",ids);
		
	}

	public void deleteByContractId(Serializable[] ids) {
		super.getSqlSession().delete(super.getNs()+".deleteByContractId",ids);
	}

}
