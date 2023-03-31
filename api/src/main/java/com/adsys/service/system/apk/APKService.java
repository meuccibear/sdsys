package com.adsys.service.system.apk;

import java.util.List;

import com.adsys.entity.system.APK;

public interface APKService {
	/**
	 * 查询
	 * @return
	 */
	List<APK> getAPKList()throws Exception;
	
	/**
	 *新增 
	 * @return 
	 */
	int saveAPK(APK apk)throws Exception;
	
	/**
	 * 查询
	 */
	APK findAPKByMd5sum(String md5sum)throws Exception;
	
	/**
	 * 删除
	 */
	void deleteByVersion(String version)throws Exception;
}
