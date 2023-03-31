package com.adsys.service.system.apk.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.adsys.dao.APKDao;
import com.adsys.entity.system.APK;
import com.adsys.service.system.apk.APKService;
@Service("apkService")
public class APKServiceImpl implements APKService {
	
	@Resource(name="apkDao")
	private APKDao apkDao;
	
	/**
	 * 查询
	 * @return
	 * @throws Exception 
	 */
	public List<APK> getAPKList() throws Exception {
		return apkDao.getAPKList("APKMapper.getAPKList");
	}
	
	/**
	 *新增 
	 * @throws Exception 
	 */
	public int saveAPK(APK apk) throws Exception {
		return apkDao.saveAPK("APKMapper.saveAPK", apk);
	}
	
	/**
	 * 查询
	 */
	public APK findAPKByMd5sum(String md5sum)throws Exception {
		return apkDao.findAPKByMd5sum("APKMapper.findAPKByMd5sum", md5sum);
	}
	
	/**
	 * 删除
	 */
	public void deleteByVersion(String version) throws Exception{
		apkDao.deleteByVersion("APKMapper.deleteByVersion",version);
	}
}
