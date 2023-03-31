package com.adsys.service.system.device.timepref;

import java.util.List;

import com.adsys.entity.Page;
import com.adsys.util.PageData;


/** 
 * 说明：设备定时接口
 * 创建时间：2018-04-18
 * @version
 */
public interface DeviceTimePrefManager{

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception;
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception;
	
	public void deleteByDid(PageData pd)throws Exception;
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception;
	
	public long getWeekCount(PageData pd)throws Exception;
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception;
	
	
}

