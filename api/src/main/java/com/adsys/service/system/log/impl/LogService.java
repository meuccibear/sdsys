package com.adsys.service.system.log.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import com.adsys.dao.DaoSupport;
import com.adsys.entity.Page;
import com.adsys.service.system.log.LogManager;
import com.adsys.util.DateUtil;
import com.adsys.util.PageData;
import com.adsys.util.UuidUtil;


/** 
 * 说明： 操作日志管理
 * 创建时间：2018-04-18
 * @version
 */
@Service("logService")
public class LogService implements LogManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(String type, String msg, String user,String uuid, String did)throws Exception{
		PageData pd = new PageData();
		pd.put("id", UuidUtil.get32UUID());
		pd.put("uuid", uuid);
		pd.put("ldid", did);
		pd.put("ltype", type);
		pd.put("laction", msg);
		pd.put("luser", user);
		pd.put("adddate", DateUtil.getTime());
		dao.save("LogMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("LogMapper.delete", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void deleteAll(PageData pd)throws Exception{
		dao.delete("LogMapper.deleteAll", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		List<PageData> result = new ArrayList<PageData>();

		Number pagenum = (Number) dao.findForObject("LogMapper.findPageCount", page);
		if(pagenum == null || pagenum .longValue()<=0){
			return result;
		}
		page.setTotalResult(pagenum.intValue());
		RowBounds rowbounds = new RowBounds(page.getOffset(), page.getShowCount());
		return (List<PageData>)dao.finPageForList("LogMapper.datalistPage", page,rowbounds);
	}
	
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("LogMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("LogMapper.findById", pd);
	}
	
	
}

