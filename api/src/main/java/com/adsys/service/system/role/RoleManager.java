package com.adsys.service.system.role;

import java.util.List;

import com.adsys.entity.Page;
import com.adsys.entity.system.Role;
import com.adsys.util.PageData;

/**	角色接口类
 * @author FHadmin QQ313596790
 * 修改日期：2015.11.6
 */
public interface RoleManager {
	
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
	
	/**批量删除
	 * @param pd
	 * @throws Exception
	 */
	public void deleteAll(PageData pd)throws Exception;

}
