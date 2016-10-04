package com.lcc.jk.dao;

import java.util.Map;

import com.lcc.jk.domain.Contract;
import com.lcc.jk.vo.ContractVO;

public interface ContractDao extends BaseDao<Contract> {

	public void updateState(Map map); // 修改状态

	public ContractVO view(String contractId); // 查询某个合同
}
