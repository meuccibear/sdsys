package com.adsys.service.system.customer.impl;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import com.adsys.dao.DaoSupport;
import com.adsys.entity.Page;
import com.adsys.service.system.customer.CustomerManager;
import com.adsys.util.AESUtil;
import com.adsys.util.Const;
import com.adsys.util.PageData;

/** 
 * 说明： 客户管理
 * 创建时间：2018-04-18
 * @version
 */
@Service("customerService")
public class CustomerService implements CustomerManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("CustomerMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("CustomerMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("CustomerMapper.edit", pd);
	}
	
	/**修改授权
	 * @param pd
	 * @throws Exception
	 */
	public void editLisence(PageData pd)throws Exception{
		dao.update("CustomerMapper.editLisence", pd);
	}
	
	/**增加安装数
	 * @param pd
	 * @throws Exception
	 */
	public void addInstallNum(PageData pd)throws Exception{
		//String installnum = AESUtil.decrypt(pd.getString("installnum"), Const.AES_KEY);
		int installnum_i = Integer.valueOf(pd.getString("installnum")) + 1;
		pd.put("installnum", AESUtil.encrypt(String.valueOf(installnum_i), Const.AES_KEY));
		dao.update("CustomerMapper.UpdateInstallNum", pd);
	}
	
	/**减少安装数
	 * @param pd
	 * @throws Exception
	 */
	public void minusInstallNum(PageData pd)throws Exception{
		//String installnum = AESUtil.decrypt(pd.getString("installnum"), Const.AES_KEY);
		int installnum_i = Integer.valueOf(pd.getString("installnum")) - 1;
		if (installnum_i <= 0)
			installnum_i = 0;
		pd.put("installnum", AESUtil.encrypt(String.valueOf(installnum_i), Const.AES_KEY));
		dao.update("CustomerMapper.UpdateInstallNum", pd);
	}
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		List<PageData> result = new ArrayList<PageData>();

		Number pagenum = (Number) dao.findForObject("CustomerMapper.findPageCount", page);
		if(pagenum == null || pagenum .longValue()<=0){
			return result;
		}
		page.setTotalResult(pagenum.intValue());
		RowBounds rowbounds = new RowBounds(page.getOffset(), page.getShowCount());
		return (List<PageData>)dao.finPageForList("CustomerMapper.datalistPage", page,rowbounds);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("CustomerMapper.listAll", pd);
	}
	
	/**客户总数
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public int count()throws Exception{
		Page page = new Page();
		Number countnum = (Number) dao.findForObject("CustomerMapper.findPageCount", page);
		if(countnum == null || countnum .longValue()<=0){
			return 0;
		}
		return countnum.intValue();
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		PageData pageData =  (PageData)dao.findForObject("CustomerMapper.findById", pd);
		String installnum = AESUtil.decrypt(pageData.getString("installnum"), Const.AES_KEY);
		String limitnum = AESUtil.decrypt(pageData.getString("limitnum"), Const.AES_KEY);
		String exprie = "";
		if (pageData.getString("expriedate") != null && pageData.getString("expriedate") != "")
			exprie = AESUtil.decrypt(pageData.getString("expriedate"), Const.AES_KEY);
		pageData.put("installnum", installnum);
		pageData.put("limitnum", limitnum);
		pageData.put("expriedate", exprie);
		return pageData;
	}
	
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("CustomerMapper.deleteAll", ArrayDATA_IDS);
	}
	
}

