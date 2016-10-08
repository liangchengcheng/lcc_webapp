package com.lcc.jk.dao.impl;

import java.util.Map;
import org.springframework.stereotype.Repository;
import com.lcc.jk.dao.ContractDao;
import com.lcc.jk.domain.Contract;
import com.lcc.jk.vo.ContractVO;

@Repository
public class ContractDaoImpl extends BaseDaoImpl<Contract> implements ContractDao{

	public ContractDaoImpl(){
		super.setNs("com.lcc.jk.mapper.Con");
	}
	
	public void updateState(Map map) {
		super.getSqlSession().update(super.getNs()+".updateState",map);
	}

	public ContractVO view(String contractId) {
		return super.getSqlSession().selectOne(super.getNs()+".view",contractId);
	}

}
