package com.adsys.service.system.dashboard.impl;

import javax.annotation.Resource;
import com.adsys.util.Logger;
import org.springframework.stereotype.Service;
import com.adsys.dao.DaoSupport;
import com.adsys.service.system.dashboard.DashboardManager;
import com.adsys.util.ComputerMonitorUtil;
import com.adsys.util.PageData;



@Service("dashboardService")
public class DashboardService implements DashboardManager{

	protected Logger logger = Logger.getLogger(this.getClass());

	@Resource(name = "daoSupport")
	private DaoSupport dao;

	/**控制板信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData getDashboardInfo(PageData pd){
		PageData date = new PageData();
		try {
			date.putAll((PageData) dao.findForObject("DashboardMapper.getDeviceCount", pd));
		} catch (Exception e) {
			throw new RuntimeException("getDeviceCount异常", e);
		}
		try {
			date.put("ProgramCount", dao.findForObject("DashboardMapper.getProgramCount", pd));
		} catch (Exception e) {
			throw new RuntimeException("getProgramCount异常", e);
		}
//		date.put("DeviceStatusNum", dao.findForList("DashboardMapper.getDeviceStatusNum", null));
		try {
			date.put("DeployDeviceNumOfDay", dao.findForList("DashboardMapper.getDeviceNumGroupByDay", pd));
		} catch (Exception e) {
			throw new RuntimeException("getDeviceNumGroupByDay异常", e);
		}
		try{
			date.put("cupUsage", ComputerMonitorUtil.getCpuUsage());
		}catch (Exception e) {
			logger.error(e);
//			throw new RuntimeException("getCpuUsage 异常", e);
		}
		try{
			date.put("memoryUsage", ComputerMonitorUtil.getMemUsage());
		}catch (Exception e) {
//			e.printStackTrace();
			throw new RuntimeException("getMemUsage 异常", e);
		}
		return date;
	}

}
