package com.adsys.service.system.schedule.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import com.adsys.dao.DaoSupport;
import com.adsys.entity.Page;
import com.adsys.service.system.schedule.ScheduleManager;
import com.adsys.util.PageData;

/** 
 * 说明： 发布调度管理
 * 创建时间：2018-04-18
 * @version
 */
@Service("scheduleService")
public class ScheduleService implements ScheduleManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("ScheduleMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("ScheduleMapper.delete", pd);
	}
	
	
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		List<PageData> result = new ArrayList<PageData>();

		Number pagenum = (Number) dao.findForObject("ScheduleMapper.findPageCount", page);
		if(pagenum == null || pagenum .longValue()<=0){
			return result;
		}
		page.setTotalResult(pagenum.intValue());
		RowBounds rowbounds = new RowBounds(page.getOffset(), page.getShowCount());
		return (List<PageData>)dao.finPageForList("ScheduleMapper.datalistPage", page,rowbounds);
	}
	
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("ScheduleMapper.listAll", pd);
	}
	
	/**项目列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> itemListAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("ScheduleMapper.itemListAll", pd);
	}
	
	
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("ScheduleMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("ScheduleMapper.deleteAll", ArrayDATA_IDS);
	}

	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void deleteItem(PageData pd)throws Exception{
		dao.delete("ScheduleMapper.deleteItem", pd);
	}
	
	public void reView(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		dao.update("ScheduleMapper.review",pd); 
	}
	
	/**新增ITEM
	 * @param pd
	 * @throws Exception
	 */
	public void saveItem(PageData pd)throws Exception{
		dao.save("ScheduleMapper.saveItem", pd);
	}
	
	/**修改ITEM
	 * @param pd
	 * @throws Exception
	 */
	public void editItem(PageData pd)throws Exception{
		dao.update("ScheduleMapper.editItem", pd);
	}
	
	/**列表(按DID查询)
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listByDid(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("ScheduleMapper.listAllByDid", pd);
	}

	/**被替换列表(按DID查询)
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAllForOlder(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("ScheduleMapper.listAllForOlder", pd);
	}
	
	/**修改发布单状态
	 * @param pd

	 * @throws Exception

	 */
	public void editScheduleListState(PageData pd)throws Exception{
		dao.update("ScheduleMapper.editScheduleListState", pd);
	}
	
	/**通过id获取ITEM数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findItemById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("ScheduleMapper.findItemById", pd);
	}
	/////////////////////////////////////////////

	/**新增PUSH ITEM
	 * @param pd

	 * @throws Exception
	 */
	public void savePushItem(PageData pd)throws Exception{
		dao.save("ScheduleMapper.savePushItem", pd);
	}
	
	/**修改PUSH STATE
	 * @param pd

	 * @throws Exception

	 */
	public void editPushState(PageData pd)throws Exception{
		dao.update("ScheduleMapper.editResourceState", pd);
	}
	
	/**通过id获取PUSH数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findPushItemById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("ScheduleMapper.findPushItemById", pd);
	}
	
	/**删除PUSH ITEM

	 * @param pd
	 * @throws Exception

	 */
	public void deletePushItem(PageData pd)throws Exception{
		dao.delete("ScheduleMapper.deletePushItem", pd);
	}

	/**PUSH列表
	 * @param page

	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> pushItemList(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("ScheduleMapper.pushItemListAll", pd);
	}
	
	/**PUSH ITEM数量
	 * @param page

	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public long pushItemCount(PageData pd)throws Exception{
		Number pagenum = (Number) dao.findForObject("ScheduleMapper.pushItemCount", pd);
		if(pagenum == null || pagenum.longValue()<=0){
			return 0;
		}
		return pagenum.longValue();
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAllPushItem(PageData pd)throws Exception{
		dao.delete("ScheduleMapper.deleteAllPushItem", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAllList(PageData pd)throws Exception{
		dao.delete("ScheduleMapper.deleteAllList", pd);
	}
	
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAllSche(PageData pd)throws Exception{
		dao.delete("ScheduleMapper.deleteAllSche", pd);
	}
}

