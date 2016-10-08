package com.lcc.jk.dao.impl;

import org.springframework.stereotype.Repository;

import com.lcc.jk.dao.SysCodeDao;
import com.lcc.jk.domain.SysCode;

@Repository
public class SysCodeDaoImpl extends BaseDaoImpl<SysCode> implements SysCodeDao{
	public SysCodeDaoImpl() {
		//设置命名空间
		super.setNs("com.lcc.jk.mapper.SysCodeMapper");
	}
}
