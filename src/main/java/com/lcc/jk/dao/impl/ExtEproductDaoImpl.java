package com.lcc.jk.dao.impl;

import org.springframework.stereotype.Repository;

import com.lcc.jk.dao.ExtEproductDao;
import com.lcc.jk.domain.ExtEproduct;

@Repository
public class ExtEproductDaoImpl extends BaseDaoImpl<ExtEproduct> implements ExtEproductDao{
	public ExtEproductDaoImpl() {
		//设置命名空间
		super.setNs("com.lcc.jk.mapper.ExtEproductMapper");
	}
}
