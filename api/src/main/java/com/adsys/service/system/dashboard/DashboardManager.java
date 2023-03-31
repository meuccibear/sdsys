package com.adsys.service.system.dashboard;

import java.util.List;

import com.adsys.entity.Page;
import com.adsys.entity.system.User;
import com.adsys.util.PageData;



/** 
 * 修改时间：2018-6-17 
 */
public interface DashboardManager {
	
	/**
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData getDashboardInfo(PageData pd)throws Exception;
	
	
}
