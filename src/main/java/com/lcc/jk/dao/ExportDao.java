package com.lcc.jk.dao;

import java.util.Map;
import com.lcc.jk.domain.Export;

public interface ExportDao extends BaseDao<Export>{

	public void updateState(Map map);//修改状态
}
