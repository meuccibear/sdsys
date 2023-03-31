package com.adsys.dao;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.adsys.entity.system.APK;

@Repository("apkDao")
public class APKDao {
	
	@Resource(name = "sqlSessionTemplate")
	private SqlSessionTemplate sqlSessionTemplate;
	
	/**
	 * 读取apk列表
	 */
	public List<APK> getAPKList(String str)throws Exception{
		return sqlSessionTemplate.selectList(str);
	}
	
	/**
	 * 上传apk
	 */
	public int saveAPK(String str,APK apk)throws Exception{
		int rows = sqlSessionTemplate.insert(str, apk);
		return rows;
	}
	
	/**
	 * 通过md5sum查询
	 */
	public APK findAPKByMd5sum(String str,String md5sum)throws Exception{
		return sqlSessionTemplate.selectOne(str, md5sum);
	}
	
	/**
	 * 删除
	 */
	public void deleteByVersion(String str,String version) throws Exception{
		sqlSessionTemplate.delete(str, version);
	}
}
