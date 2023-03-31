package com.adsys.service.system.device.ext;

import java.util.List;

import com.adsys.entity.Page;
import com.adsys.util.PageData;


/** 
 * 说明：设备管理扩展信息接口
 * 创建时间：2018-04-18
 * @version
 */
public interface DeviceExtManager{

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
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception;
	
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	
	
}

