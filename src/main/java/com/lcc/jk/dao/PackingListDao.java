package com.lcc.jk.dao;

import java.util.Map;

import com.lcc.jk.domain.PackingList;

public interface PackingListDao extends BaseDao<PackingList> {
	public void updateState(Map map);			//修改状态
}

