package com.adsys.util;

import org.springframework.context.ApplicationContext;

public class Const {
	public static final String JWT_SECRET ="1@%ddh25sjkf8df46gsd4ds56f6sh26g/s;g[6d5";
	public static final String NO_INTERCEPTOR_PATH = ".*/((login)|(lib)|(upload)|(zipDir)|(apk)|(update/file)|(ad/editor)|(adEditor/resources/file)|(adEditor/programs/file)|(uploadFiles/uploadImgs)|(reset)).*";	//不对匹配该值的访问路径拦截（正则）如 .*/((login)|(logout)|(code)|(app)|(weixin)|(static)|(main)|(websocket)|(uploadImgs)).*
	public static final String PAGE="10";
	public static final int JWT_TTL = 7*24*60*60*1000;  //millisecond j_token 时长
	public static final String JWT_ID = "jwt";
	
	public static final String FWATERM = "admin/config/FWATERM.txt";		//文字水印配置路径
	public static final String IWATERM = "admin/config/IWATERM.txt";		//图片水印配置路径
	public static final String WEIXIN	= "admin/config/WEIXIN.txt";		//微信配置路径
	public static final String WEBSOCKET = "admin/config/WEBSOCKET.txt";	//WEBSOCKET配置路径
	public static final String FILEPATHIMG = "uploadFiles/uploadImgs/";		//图片上传路径
	public static final String FILEPATHTWODIMENSIONCODE = "uploadFiles/twoDimensionCode/"; //二维码存放路径
	
	public static final String WEBSOCKET_APPKEY = "123456"; 
	
	public static final String TXLIVE_URL_KEY = "c9edfbe2293808f6753d6ee63926e2e3"; 
	public static final String TXLIVE_BIZID = "37114"; 
	
	public static String REMOTE_FILE_SERVER_ADDR = "http://222.188.110.74:8080";
	public static long REMOTE_FILE_MAX_SIZE	= 104851000;
	
	public static String AES_KEY = "ad3926e2e36f6sh26gsys";
	/**
	 * APP Constants
	 */
	//系统用户注册接口_请求协议参数)
	public static final String[] SYSUSER_REGISTERED_PARAM_ARRAY = new String[]{"USERNAME","PASSWORD","NAME","EMAIL","rcode"};
	public static final String[] SYSUSER_REGISTERED_VALUE_ARRAY = new String[]{"用户名","密码","姓名","邮箱","验证码"};
	
	//app根据用户名获取会员信息接口_请求协议中的参数
	public static final String[] APP_GETAPPUSER_PARAM_ARRAY = new String[]{"USERNAME"};
	public static final String[] APP_GETAPPUSER_VALUE_ARRAY = new String[]{"用户名"};
	
}
