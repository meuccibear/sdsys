package com.adsys.service.system.dashboard.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.adsys.dao.DaoSupport;
import com.adsys.service.system.dashboard.DashboardManager;
import com.adsys.util.ComputerMonitorUtil;
import com.adsys.util.PageData;



@Service("dashboardService")
public class DashboardService implements DashboardManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**控制板信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData getDashboardInfo(PageData pd)throws Exception{
		PageData date = new PageData();
		date.putAll((PageData) dao.findForObject("DashboardMapper.getDeviceCount", pd));
		date.put("ProgramCount", dao.findForObject("DashboardMapper.getProgramCount", pd));
//		date.put("DeviceStatusNum", dao.findForList("DashboardMapper.getDeviceStatusNum", null));
		date.put("DeployDeviceNumOfDay", dao.findForList("DashboardMapper.getDeviceNumGroupByDay", pd));
		date.put("cupUsage", ComputerMonitorUtil.getCpuUsage());
		date.put("memoryUsage", ComputerMonitorUtil.getMemUsage());
		return date;
	}
	
}
