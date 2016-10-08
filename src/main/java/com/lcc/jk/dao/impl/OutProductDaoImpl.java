package com.lcc.jk.dao.impl;

import com.lcc.jk.dao.OutProductDao;
import com.lcc.jk.vo.OutProductVO;

public class OutProductDaoImpl extends BaseDaoImpl<OutProductVO> implements OutProductDao{
	public OutProductDaoImpl() {
		//设置命名空间
		super.setNs("com.lcc.jk.mapper.OutProductMapper");
	}
}

