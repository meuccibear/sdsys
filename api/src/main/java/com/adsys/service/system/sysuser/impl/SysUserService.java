package com.adsys.service.system.sysuser.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import com.adsys.dao.DaoSupport;
import com.adsys.entity.Page;
import com.adsys.entity.system.User;
import com.adsys.service.system.sysuser.SysUserManager;
import com.adsys.util.PageData;



@Service("sysuserService")
public class SysUserService implements SysUserManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**登录判断
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData getUserByNameAndPwd(PageData pd)throws Exception{
		return (PageData)dao.findForObject("SysUserMapper.getUserInfo", pd);
	}
	
	/**更新登录时间
	 * @param pd
	 * @throws Exception
	 */
	public void updateLastLogin(PageData pd)throws Exception{
		dao.update("SysUserMapper.updateLastLogin", pd);
	}
	
	/**通过用户ID获取用户信息和角色信息
	 * @param uid
	 * @return
	 * @throws Exception
	 */
	public PageData getUserAndRoleById(String uid) throws Exception {
		return (PageData) dao.findForObject("SysUserMapper.getUserAndRoleById", uid);
	}
	
	/**通过USERNAEME获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findByUsername(PageData pd)throws Exception{
		return (PageData)dao.findForObject("SysUserMapper.findByUsername", pd);
	}
	
	/**列出某角色下的所有用户
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAllUserByRoldId(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("SysUserMapper.listAllUserByRoldId", pd);
		
	}
	

	/**通过邮箱获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findByUE(PageData pd)throws Exception{
		return (PageData)dao.findForObject("SysUserMapper.findByUE", pd);
	}
	
	/**通过电话获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findByTel(PageData pd)throws Exception{
		return (PageData)dao.findForObject("SysUserMapper.findByTel", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("SysUserMapper.findById", pd);
	}
	
	/**保存用户
	 * @param pd
	 * @throws Exception
	 */
	public void saveU(PageData pd)throws Exception{
		dao.save("SysUserMapper.saveU", pd);
	}
	 
	/**修改用户
	 * @param pd
	 * @throws Exception
	 */
	public void editU(PageData pd)throws Exception{
		dao.update("SysUserMapper.editU", pd);
	}
	
	/**修改基础信息
	 * @param pd
	 * @throws Exception
	 */
	public void editProfile(PageData pd)throws Exception{
		dao.update("SysUserMapper.editProfile", pd);
	}
	
	/**删除用户
	 * @param pd
	 * @throws Exception
	 */
	public void deleteU(PageData pd)throws Exception{
		dao.delete("SysUserMapper.deleteU", pd);
	}
	
	/**批量删除用户
	 * @param uids
	 * @throws Exception
	 */
	public void deleteAllU(String[] uid)throws Exception{
		dao.delete("SysUserMapper.deleteAllU", uid);
	}
	
	/**用户列表(全部)
	 * @param USER_IDS
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAllUser(PageData pd)throws Exception{
		return (List<PageData>) dao.findForList("SysUserMapper.listAllUser", pd);
	}
	
	/**获取总数
	 * @param pd
	 * @throws Exception
	 */
	public PageData getUserCount(String value)throws Exception{
		return (PageData)dao.findForObject("SysUserMapper.getUserCount", value);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		List<PageData> result = new ArrayList<PageData>();

		Number pagenum = (Number) dao.findForObject("SysUserMapper.findPageCount", page);
		if(pagenum == null || pagenum .longValue()<=0){
			return result;
		}
		page.setTotalResult(pagenum.intValue());
		RowBounds rowbounds = new RowBounds(page.getOffset(), page.getShowCount());
		return (List<PageData>)dao.finPageForList("SysUserMapper.userlistPage", page,rowbounds);
	}
}
