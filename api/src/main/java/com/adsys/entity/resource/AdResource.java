package com.adsys.entity.resource;

public class AdResource {
	
	private String id ;
	private String pid;
	private String resName;
	private int resType;//0:文件夹   1：文件
	private String des;//资源描述
	private String resPath;//资源路径(相对)
	private String fileType;//文件类型，pic,video,music,sub,calendar,clock
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getResName() {
		return resName;
	}
	public void setResName(String resName) {
		this.resName = resName;
	}
	public int getResType() {
		return resType;
	}
	public void setResType(int resType) {
		this.resType = resType;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public String getResPath() {
		return resPath;
	}
	public void setResPath(String resPath) {
		this.resPath = resPath;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	
	
	
}


