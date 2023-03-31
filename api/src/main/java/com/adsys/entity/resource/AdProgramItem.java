package com.adsys.entity.resource;

public class AdProgramItem {
	private String id;
	private String name;
	/**
	 * 类型
	 * 图片：pic   视频：video    音频：music 字幕：sub    日期：calendar 
	 * 时钟：clock 天气：weather  网页：web   按钮：button 声控：voiceControl
	 */
	private String type;
	private String setting;
	private String pid;
	private String res;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSetting() {
		return setting;
	}
	public void setSetting(String setting) {
		this.setting = setting;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getRes() {
		return res;
	}
	public void setRes(String res) {
		this.res = res;
	}
	
	
}
