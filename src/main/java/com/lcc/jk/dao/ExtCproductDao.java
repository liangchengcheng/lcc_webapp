package com.lcc.jk.dao;

import java.io.Serializable;

import com.lcc.jk.domain.ExtCproduct;

public interface ExtCproductDao extends BaseDao<ExtCproduct> {
	
	public void deleteByContractProductById(Serializable[] ids);
	public void deleteByContractId(Serializable[] contractIds);
}

