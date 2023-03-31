package com.adsys.weixin.config;

public class WeixinConfig {
	//=======【基本信息设置】=====================================
		//微信公众号身份的唯一标识。审核通过后，在微信发送的邮件中查看
		public static final String APPID = "wx0ce859adc54f709d";
		//受理商ID，身份标识
		public static final String MCHID = "1493096962";
		//商户支付密钥Key。审核通过后，在微信发送的邮件中查看
		public static final String KEY = "4tywFStBfr5n2LEx4TWvtazoFGOqoAfO";
		//JSAPI接口中获取openid，审核后在公众平台开启开发模式后可查看
		public static final String APPSECRET = "1d4fb65a00ba4384fe3d98b8ca5820e1";
		
		//=======【JSAPI路径设置】===================================
		//获取access_token过程中的跳转uri，通过跳转将code传入jsapi支付页面
		public static final String JS_API_CALL_URL = "http://www.weixin.com/weixin/pay/index.jsp";
		public static final String WX_LOGIN_CALL_URL = "http://www.quqima.cn/mobile/login.html";
		//=======【证书路径设置】=====================================
		//证书路径,注意应该填写绝对路径
		public static final String SSLCERT_PATH = "/home/wwwroot/weixin/webApp/WxPayPubHelper/cacert/apiclient_cert.pem";
		public static final String SSLKEY_PATH = "/home/wwwroot/weixin/webApp/WxPayPubHelper/cacert/apiclient_key.pem";
		
		//=======【异步通知url设置】===================================
		//异步通知url，商户根据实际开发过程设定
		public static final String NOTIFY_URL = "http://www.quqima.cn/apis/YNTP/pay/pay_notify";

		public static final String SPBILL_CREATE_IP = "111.230.14.48";
		
		//=======【http超时设置】===================================
		//使用HTTP POST方法，此处可修改其超时时间，默认为30秒
		public static final int POST_TIMEOUT = 30000;
		//统一下单接口
		public static final String UNIFIED_ORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";
}
