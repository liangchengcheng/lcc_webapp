package com.lcc.jk.service;

import java.util.List;
import java.util.Map;

import com.lcc.jk.domain.Contract;
import com.lcc.jk.pagination.Page;
import com.lcc.jk.vo.ContractVO;

/**
 * 
 * @author lcc
 *
 */
public interface ContractHisService {

	//分页查询
	public List<Contract> findPage(Page page);
	//带条件的查询，条件可以为null。没有条件，返回list对象的集合
	public List<Contract> find(Map paraMap);
	//关联对象的查询一个
	public ContractVO view(String contractId);
	//归档
	public void pigeinhole(String[] contractIds);
	//取消归档
	public void pigouthole(String[] contractIds);
}
	
