package com.adsys.service.system.schedule;

import java.util.List;

import com.adsys.entity.Page;
import com.adsys.util.PageData;


/** 
 * 说明： 发布调度管理
 * 创建时间：2018-04-18
 * @version
 */
public interface ScheduleManager{

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
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception;
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception;
	
	/**项目列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> itemListAll(PageData pd)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	
	/**
	 * 审核
	 * @param pd
	 * @throws Exception
	 */
	public void reView(PageData pd)throws Exception;
	
	/**新增ITEM
	 * @param pd
	 * @throws Exception
	 */
	public void saveItem(PageData pd)throws Exception;
	
	/**修改ITEM
	 * @param pd
	 * @throws Exception
	 */
	public void editItem(PageData pd)throws Exception;
	
	/**列表(按DID查询)
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> listByDid(PageData pd)throws Exception;

	/**被替换列表(按DID查询)
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> listAllForOlder(PageData pd)throws Exception;
	
	/**修改发布单状态
	 * @param pd

	 * @throws Exception

	 */
	public void editScheduleListState(PageData pd)throws Exception;

	/**通过id获取ITEM数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findItemById(PageData pd)throws Exception;
	//////////////////////////////////////////////

	/**新增PUSH ITEM
	 * @param pd
	 * @throws Exception
	 */
	public void savePushItem(PageData pd)throws Exception;

	/**修改PUSH STATE
	 * @param pd
	 * @throws Exception
	 */
	public void editPushState(PageData pd)throws Exception;

	/**通过id获取PUSH数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findPushItemById(PageData pd)throws Exception;
	
	/**删除PUSH ITEM
	 * @param pd
	 * @throws Exception
	 */
	public void deletePushItem(PageData pd)throws Exception;

	/**PUSH列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> pushItemList(PageData pd)throws Exception;
	
	/**PUSH ITEM数量
	 * @param page

	 * @throws Exception
	 */
	public long pushItemCount(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAllPushItem(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAllList(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAllSche(PageData pd)throws Exception;
}

