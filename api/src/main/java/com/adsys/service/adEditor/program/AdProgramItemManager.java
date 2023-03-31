package com.adsys.service.adEditor.program;

import java.util.List;

import com.adsys.util.PageData;

/**	节目明细接口类
 */
public interface AdProgramItemManager {
	
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
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception;
	
	/**列表(全部) by pid
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAllByPid(PageData pd)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(PageData pd)throws Exception;
	
	public List<PageData> findByPid(PageData pd)throws Exception;
	
	public void deleteByPid(PageData pd)throws Exception;

}
