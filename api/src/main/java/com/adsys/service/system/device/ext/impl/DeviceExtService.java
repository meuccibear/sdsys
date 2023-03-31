package com.adsys.service.system.device.ext.impl;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import com.adsys.dao.DaoSupport;
import com.adsys.entity.Page;
import com.adsys.service.system.device.ext.DeviceExtManager;
import com.adsys.util.PageData;

/** 
 * 说明： 设备管理扩展信息
 * 创建时间：2018-04-18
 * @version
 */
@Service("deviceExtService")
public class DeviceExtService implements DeviceExtManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("DevicesExtMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("DevicesExtMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("DevicesExtMapper.edit", pd);
	}
	
	
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("DevicesExtMapper.findById", pd);
	}
	
}

