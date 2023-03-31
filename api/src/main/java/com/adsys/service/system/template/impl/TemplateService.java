package com.adsys.service.system.template.impl;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import com.adsys.dao.DaoSupport;
import com.adsys.entity.Page;
import com.adsys.service.system.device.DeviceManager;
import com.adsys.service.system.template.TemplateManager;
import com.adsys.util.PageData;

/** 
 * 说明： 模板管理
 * 创建时间：2018-04-18
 * @version
 */
@Service("templateService")
public class TemplateService implements TemplateManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("TemplateMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("TemplateMapper.delete", pd);
	}
	
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		List<PageData> result = new ArrayList<PageData>();

		Number pagenum = (Number) dao.findForObject("TemplateMapper.findPageCount", page);
		if(pagenum == null || pagenum .longValue()<=0){
			return result;
		}
		page.setTotalResult(pagenum.intValue());
		RowBounds rowbounds = new RowBounds(page.getOffset(), page.getShowCount());
		return (List<PageData>)dao.finPageForList("TemplateMapper.datalistPage", page,rowbounds);
	}

	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("TemplateMapper.findById", pd);
	}
	
	/**列表(全部) by pid
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAllByPid(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("TemplateMapper.listAllByPid", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(PageData pd)throws Exception{
		dao.delete("TemplateMapper.deleteAll",pd);
	}

	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("TemplateMapper.listAll", pd);
	}
	
}

