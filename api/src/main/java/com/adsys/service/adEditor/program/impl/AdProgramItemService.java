package com.adsys.service.adEditor.program.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.adsys.dao.DaoSupport;
import com.adsys.service.adEditor.program.AdProgramItemManager;
import com.adsys.util.PageData;


/**	节目
 */
@Service("adProgramItemService")
public class AdProgramItemService implements AdProgramItemManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao; 
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("AdProgramItemMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("AdProgramItemMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("AdProgramItemMapper.edit", pd);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("AdProgramItemMapper.listAll", pd);
	}
	
	/**列表(全部) by pid
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAllByPid(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("AdProgramItemMapper.listAllByPid", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("AdProgramItemMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(PageData pd)throws Exception{
		dao.delete("AdProgramItemMapper.deleteAll", pd);
	}

	@Override
	public List<PageData> findByPid(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return (List<PageData>)dao.findForList("AdProgramItemMapper.findByPid", pd);
	}

	@Override
	public void deleteByPid(PageData pd) throws Exception {
		dao.delete("AdProgramItemMapper.deleteByPid", pd);
	}

}
