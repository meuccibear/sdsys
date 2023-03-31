package com.adsys.service.system.log;

import java.util.List;

import com.adsys.entity.Page;
import com.adsys.util.PageData;


/** 
 * 说明： 操作日志管理
 * 创建时间：2018-04-18
 * @version
 */
public interface LogManager{

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(String type, String msg, String user,String uuid, String did) throws Exception;
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception;
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void deleteAll(PageData pd)throws Exception;
	
	
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
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	
}

