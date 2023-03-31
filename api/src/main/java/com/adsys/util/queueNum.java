package com.adsys.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.adsys.weixin.commons.WeixinCommon;

public class queueNum {

	private static Map locks = new HashMap();
	private static List lockKeys = new ArrayList();
	private static Map<Object, Long> queuePool = new HashMap<Object, Long>();
	private static String curDate = "";
	private Logger logger = Logger.getLogger(queueNum.class);
	
	public void init()
	{
		for(int num = 1; num < 10000; num++) {  
			Object lockKey = new Object();  
			lockKeys.add(lockKey);  
		    locks.put(lockKey, new Object());  
		}  
		queuePool.clear();
		curDate = DateUtil.getDay();
		logger.info("init()");
	}
	
	public long getNumber(String uid) {
		try{
			if (!curDate.equals(DateUtil.getDay()))
			{
				init();
			}
			Object lockKey = lockKeys.get(Math.abs(uid.hashCode()) % lockKeys.size());  
			Object lock = locks.get(lockKey);  
			synchronized(lock) {  
				long num = 0;
				Object numObj = (Object)queuePool.get(lock);
				if (numObj == null)
				{
					queuePool.put(lock, (long)2);
					num = 1;
				}
				else
				{
					num = Long.parseLong(numObj.toString());
					queuePool.put(lock, num+1);
				}
				return num;
			}  
		}catch (Exception ex){
			ex.printStackTrace();
			return 0;
		}
	}  
	
	public static void main(String args[]){
		queueNum qn = new queueNum();
		qn.init();
		System.out.println(qn.getNumber("abcde"));
		System.out.println(qn.getNumber("abcde"));
		System.out.println(qn.getNumber("abcde"));
		System.out.println(qn.getNumber("ddddd"));
	}
}
