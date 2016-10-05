package com.lcc.jk.dao;

import java.util.Map;
import com.lcc.jk.domain.Factory;

public interface FactoryDao extends BaseDao<Factory> {
	public void updateState(Map map);			//修改状态
}

