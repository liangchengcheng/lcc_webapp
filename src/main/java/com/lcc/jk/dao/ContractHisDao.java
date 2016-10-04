package com.lcc.jk.dao;

import com.lcc.jk.domain.Contract;
import com.lcc.jk.vo.ContractVO;

public interface ContractHisDao extends BaseDao<Contract>{

	public ContractVO view(String contractId);//查询某个合同
}
