package com.adsys.service.system.device.impl;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import com.adsys.dao.DaoSupport;
import com.adsys.entity.Page;
import com.adsys.service.system.device.DeviceManager;
import com.adsys.util.PageData;

/** 
 * 说明： 设备管理
 * 创建时间：2018-04-18
 * @version
 */
@Service("deviceService")
public class DeviceService implements DeviceManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("DevicesMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("DevicesMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("DevicesMapper.edit", pd);
	}
	
	/**更新状态
	 * @param pd
	 * @throws Exception
	 */
	public void updateState(PageData pd)throws Exception{
		dao.update("DevicesMapper.updateState", pd);
	}
	
	/**更新心跳时间
	 * @param pd
	 * @throws Exception
	 */
	public void updateBeat(PageData pd)throws Exception{
		dao.update("DevicesMapper.updateBeat", pd);
	}
	
	/**更新定位坐标
	 * @param pd
	 * @throws Exception
	 */
	public void updateLocation(PageData pd)throws Exception{
		dao.update("DevicesMapper.updateLocation", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		List<PageData> result = new ArrayList<PageData>();

		Number pagenum = (Number) dao.findForObject("DevicesMapper.findPageCount", page);
		if(pagenum == null || pagenum .longValue()<=0){
			return result;
		}
		page.setTotalResult(pagenum.intValue());
		RowBounds rowbounds = new RowBounds(page.getOffset(), page.getShowCount());
		return (List<PageData>)dao.finPageForList("DevicesMapper.datalistPage", page,rowbounds);
	}
	
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("DevicesMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("DevicesMapper.findById", pd);
	}
	
	/**通过gid获取数据
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> findDeviceByGid(String gid)throws Exception{
		return (List<PageData>)dao.findForList("DevicesMapper.findDeviceByGid", gid);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(PageData pd)throws Exception{
		dao.delete("DevicesMapper.deleteAll", pd);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<PageData> devicesLocation(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return (List<PageData>)dao.findForList("DevicesMapper.listdevices4location", pd);
	}
}

