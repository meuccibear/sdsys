package com.adsys.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class UuidUtil {

	public static String get32UUID() {
		String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
		return uuid;
	}
	
	public static  String getOrderNo() {
		String orderNo = "" ;
		String trandNo = String.valueOf((Math.random() * 9 + 1) * 1000000);
		String sdf = new SimpleDateFormat("yyyyMMddHHMMSS").format(new Date());
		orderNo = trandNo.toString().substring(0, 4);
		orderNo = orderNo + sdf ;
		return orderNo; 
	}

	public static void main(String[] args) {
		System.out.println(getOrderNo());
	}
}

