package com.adsys.service.system.device;

import java.util.List;

import com.adsys.entity.Page;
import com.adsys.util.PageData;


/**
 * 说明：设备管理接口
 * 创建时间：2018-04-18
 * @version
 */
public interface DeviceManager{

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

	void updateErrStatus(PageData pd)throws Exception;

	/**更新状态
	 * @param pd
	 * @throws Exception
	 */
	public void updateState(PageData pd)throws Exception;

	public void updateStates(PageData pd)throws Exception;

	/**更新心跳时间
	 * @param pd
	 * @throws Exception
	 */
	public void updateBeat(PageData pd)throws Exception;

	/**更新定位坐标
	 * @param pd
	 * @throws Exception
	 */
	public void updateLocation(PageData pd)throws Exception;

	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception;

	public List<PageData> devices() throws Exception;

	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception;

	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;

	/**通过gid获取数据
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> findDeviceByGid(Integer gid)throws Exception;

	public List<String> findDeviceNameByDid(List<String> failDids )throws Exception;

	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(PageData pd)throws Exception;

	/**获取定位列表
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> devicesLocation(PageData pd) throws Exception;

}

