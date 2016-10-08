package com.lcc.jk.dao.impl;

import org.springframework.stereotype.Repository;
import com.lcc.jk.dao.ExportProductDao;
import com.lcc.jk.domain.ExportProduct;

@Repository
public class ExportProductDaoImpl extends BaseDaoImpl<ExportProduct> implements ExportProductDao{
	public ExportProductDaoImpl() {
		//设置命名空间
		super.setNs("com.lcc.jk.mapper.ExportProductMapper");
	}
}

