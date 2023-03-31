package com.adsys.service.adEditor.program;

import java.util.List;

import com.adsys.entity.Page;
import com.adsys.util.PageData;

/**	节目接口类
 */
public interface AdProgramManager {
	
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
	
	/**更新节目包路径
	 * @param pd
	 * @throws Exception
	 */
	public void updateZipPath(PageData pd)throws Exception;
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception;
	
	public List<PageData> listAllTemplate(PageData pd)throws Exception;
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listProgram(PageData pd)throws Exception;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> pageList(Page page)throws Exception;

	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	
	/**通过name获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByName(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(PageData pd)throws Exception;

	//////////////////////// for couter /////////////////////////////
	public void saveCouter(PageData pd)throws Exception;
	
	public PageData findCouterById(PageData pd)throws Exception;
	
	public void updateCouter(PageData pd)throws Exception;
	
	public List<PageData> couterPageList(Page page)throws Exception;
}
