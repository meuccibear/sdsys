package com.adsys.service.system.customer;

import java.util.List;

import com.adsys.entity.Page;
import com.adsys.util.PageData;


/** 
 * 说明： 客户管理接口
 * 创建时间：2018-04-18
 * @version
 */
public interface CustomerManager{

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
	
	/**修改授权
	 * @param pd
	 * @throws Exception
	 */
	public void editLisence(PageData pd)throws Exception;
	
	/**增加安装数
	 * @param pd
	 * @throws Exception
	 */
	public void addInstallNum(PageData pd)throws Exception;
	
	/**减少安装数
	 * @param pd
	 * @throws Exception
	 */
	public void minusInstallNum(PageData pd)throws Exception;
	
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
	
	/**景点总数
	 * @param page
	 * @throws Exception
	 */
	public int count()throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	
}

