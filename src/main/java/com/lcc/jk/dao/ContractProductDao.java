package com.lcc.jk.dao;

import java.io.Serializable;

import com.lcc.jk.domain.ContractProduct;

public interface ContractProductDao extends BaseDao<ContractProduct>{

	public void deleteByContractProductById(Serializable[] ids);
	
	public void deleteByContractId(Serializable[] ids);
}
