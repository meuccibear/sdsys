package com.adsys.entity.system;

import java.util.Date;

public class APK {
	
	private String id;
	private String version;
	private String readme;
	private String md5sum;
	private String apkpath;
	private Date createdate;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getReadme() {
		return readme;
	}
	public void setReadme(String readme) {
		this.readme = readme;
	}
	public String getMd5sum() {
		return md5sum;
	}
	public void setMd5sum(String md5sum) {
		this.md5sum = md5sum;
	}
	public String getApkpath() {
		return apkpath;
	}
	public void setApkpath(String apkpath) {
		this.apkpath = apkpath;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	
}
