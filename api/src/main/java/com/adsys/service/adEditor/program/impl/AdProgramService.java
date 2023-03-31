package com.adsys.service.adEditor.program.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import com.adsys.dao.DaoSupport;
import com.adsys.entity.Page;
import com.adsys.service.adEditor.program.AdProgramManager;
import com.adsys.util.PageData;


/**	节目
 */
@Service("adProgramService")
public class AdProgramService implements AdProgramManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao; 
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("AdProgramMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("AdProgramMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("AdProgramMapper.edit", pd);
	}
	
	/**更新节目包路径
	 * @param pd
	 * @throws Exception
	 */
	public void updateZipPath(PageData pd)throws Exception{
		dao.update("AdProgramMapper.updateZipPath", pd);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("AdProgramMapper.listAll", pd);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listProgram(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("AdProgramMapper.listProgram", pd);
	}
	
	@SuppressWarnings("unchecked")
	public List<PageData> listAllTemplate(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("AdProgramMapper.listAllTemplate", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> pageList(Page page)throws Exception{
		List<PageData> result = new ArrayList<PageData>();

		Number pagenum = (Number) dao.findForObject("AdProgramMapper.findPageCount", page);
		if(pagenum == null || pagenum .longValue()<=0){
			return result;
		}
		page.setTotalResult(pagenum.intValue());
		RowBounds rowbounds = new RowBounds(page.getOffset(), page.getShowCount());
		return (List<PageData>)dao.finPageForList("AdProgramMapper.datalistPage", page,rowbounds);
	}

	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("AdProgramMapper.findById", pd);
	}
	
	/**通过name获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByName(PageData pd)throws Exception{
		return (PageData)dao.findForObject("AdProgramMapper.findByName", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(PageData pd)throws Exception{
		dao.delete("AdProgramMapper.deleteAll", pd);
	}

	////////////////////////// for couter //////////////////////////////////////
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void saveCouter(PageData pd)throws Exception{
		dao.save("AdProgramMapper.saveCouter", pd);
	}
	
	public void updateCouter(PageData pd)throws Exception{
		dao.update("AdProgramMapper.updateCouter", pd);
	}
	
	public PageData findCouterById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("AdProgramMapper.findCouterById", pd);
	}
	
	@SuppressWarnings("unchecked")
	public List<PageData> couterPageList(Page page)throws Exception{
		List<PageData> result = new ArrayList<PageData>();

		Number pagenum = (Number) dao.findForObject("AdProgramMapper.couterPageCount", page);
		if(pagenum == null || pagenum .longValue()<=0){
			return result;
		}
		page.setTotalResult(pagenum.intValue());
		RowBounds rowbounds = new RowBounds(page.getOffset(), page.getShowCount());
		return (List<PageData>)dao.finPageForList("AdProgramMapper.couterlistPage", page,rowbounds);
	}

}
