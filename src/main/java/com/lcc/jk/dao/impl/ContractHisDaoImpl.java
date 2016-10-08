package com.lcc.jk.dao.impl;

import com.lcc.jk.dao.ContractHisDao;
import com.lcc.jk.domain.Contract;
import com.lcc.jk.vo.ContractVO;

public class ContractHisDaoImpl extends BaseDaoImpl<Contract> implements ContractHisDao {

	public ContractHisDaoImpl(){
		super.setNs("com.lcc.jk.mapper.ContractHisMapper");
	}
	
	public ContractVO view(String contractId) {
		return super.getSqlSession().selectOne(super.getNs()+".view",contractId);
	}

}
