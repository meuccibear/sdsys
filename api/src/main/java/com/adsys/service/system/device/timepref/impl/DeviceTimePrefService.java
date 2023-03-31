package com.adsys.service.system.device.timepref.impl;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import com.adsys.dao.DaoSupport;
import com.adsys.entity.Page;
import com.adsys.service.system.device.timepref.DeviceTimePrefManager;
import com.adsys.util.PageData;

/** 
 * 说明： 设备定时信息
 * 创建时间：2018-04-18
 * @version
 */
@Service("deviceTimePrefService")
public class DeviceTimePrefService implements DeviceTimePrefManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("DevicesTimeprefMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("DevicesTimeprefMapper.delete", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void deleteByDid(PageData pd)throws Exception{
		dao.delete("DevicesTimeprefMapper.deleteByDid", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("DevicesTimeprefMapper.edit", pd);
	}
	
	
	public long getWeekCount(PageData pd)throws Exception{
		Number num = (Number) dao.findForObject("DevicesTimeprefMapper.findWeekCount", pd);
		if(num == null || num .longValue()<=0){
			return 0;
		}
		return num.longValue();
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("DevicesTimeprefMapper.listAll", pd);
	}
	
}

