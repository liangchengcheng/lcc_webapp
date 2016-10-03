package com.lcc.jk.service;

import java.util.List;

import com.lcc.jk.vo.OutProductVO;

public interface OutProductService {
	public List<OutProductVO> find(String inputDate);		//带条件查询，条件可以为null，既没有条件；返回list对象集合
}
