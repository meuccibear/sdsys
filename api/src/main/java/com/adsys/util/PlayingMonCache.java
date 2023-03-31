package com.adsys.util;

import java.util.HashMap;
import java.util.Map;

public class PlayingMonCache {
	private static Map<String, PageData> mCache = new  HashMap<String, PageData>();
	
	public static void setDevPlayMon(String did, PageData program){
		mCache.put(did, program);
	}
	
	public static PageData getDevPlayMon(String did){
		if (!mCache.isEmpty())
			return mCache.get(did);
		else
			return null;
	}
	
	public static void main(String[] args) {
		PageData d = PlayingMonCache.getDevPlayMon("dd");
		if (d == null)
			System.out.println("null");
		
		PageData dd = new PageData();
		dd.put("dd", "sss");
		
		PlayingMonCache.setDevPlayMon("dd", dd);
		
		d = PlayingMonCache.getDevPlayMon("dd");
		System.out.println(d.getString("dd"));
	}
}
