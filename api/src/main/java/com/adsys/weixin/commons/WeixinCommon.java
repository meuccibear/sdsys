package com.adsys.weixin.commons;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.adsys.util.MD5;
import com.adsys.util.PageData;
import com.adsys.util.Tools;
import com.adsys.weixin.config.WeixinConfig;

import net.sf.json.JSONObject;



public class WeixinCommon {
	private Logger logger = Logger.getLogger(WeixinCommon.class);
	private String code;
	private String prepay_id;
	public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?" + "grant_type=client_credential&appid=APPID&secret=APPSECRET";
	public final static String jsapi_ticket_url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
	
	public static void main(String args[]){
		
		WeixinCommon wxCom = new WeixinCommon();
		String token_access = wxCom.getAccess_token(WeixinConfig.APPID, WeixinConfig.APPSECRET);
		System.out.println(token_access);
	}
	
	/**
	 * 写日志
	 */
	public void logger(String msg){
		logger.info(msg);
	}
	
	

	/**
	 * 获取access_token
	 * 
	 * @param appid
	 * @param appsecret
	 * @return
	 */
	public String getAccess_token(String appid, String appsecret) {
		try {
			String requestUrl = access_token_url.replace("APPID", appid).replace("APPSECRET", appsecret);
			JSONObject jsonObject = httpRequst(requestUrl, "GET", null);
			return jsonObject.getString("access_token");
		} catch (Exception e) {
			e.printStackTrace();
			return "errer";
		}
	}
	
	/**
	 * 获取jsapi_ticket
	 * 
	 * @param appid
	 * @param appsecret
	 * @return
	 */
	public String getJsapiTicket(String accessToken) {
		try {
			String requestUrl = jsapi_ticket_url.replace("ACCESS_TOKEN", accessToken);
			JSONObject jsonObject = httpRequst(requestUrl, "GET", null);
			return jsonObject.getString("ticket");
		} catch (Exception e) {
			e.printStackTrace();
			return "errer";
		}
	}
	
	public JSONObject httpRequst(String requestUrl, String requetMethod,
			String outputStr) {
		JSONObject jsonobject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLContext对象，并使用我们指定的新人管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslcontext = SSLContext.getInstance("SSL", "SunJSSE");
			sslcontext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocktFactory对象
			SSLSocketFactory ssf = sslcontext.getSocketFactory();
			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url
					.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requetMethod);
			if ("GET".equalsIgnoreCase(requetMethod))
				httpUrlConn.connect();
			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}
			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();

			jsonobject = JSONObject.fromObject(buffer.toString());
		} catch (ConnectException ce) {
			ce.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return jsonobject;
	}

	
	/**
	 * 	作用：通过http向微信提交code，以获取openid
	 *   1、发送http请求
	 *   2、格式化json数据
	 *   3、获取openid
	 */
	public PageData getOpenidAndToken(){
		String url = createOauthUrlForOpenid();
		PageData result = new PageData();
		ObjectMapper mapper = new ObjectMapper();
		try {
			JSONObject jsonObject = httpRequst(url, "GET", null);
			if (jsonObject != null)
			{
				result.put("openid", jsonObject.getString("openid"));
				result.put("access_token", jsonObject.getString("access_token"));
				return result;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 	作用：通过http向微信提交code，以获取用户信息
	 *   
	 */
	public JSONObject getWxUserInfo(String code, String openid){
		SortedMap<String,Object> url_map = new TreeMap<String,Object>();
		url_map.put("access_token", code);
		url_map.put("openid", openid);
		String bizString = formatBizQueryParaMap(url_map);
		String url =  "https://api.weixin.qq.com/sns/userinfo?"+bizString;
		ObjectMapper mapper = new ObjectMapper();
		try {
			JSONObject jsonObject = httpRequst(url, "GET", null);
			if (jsonObject != null)
			{
				return jsonObject;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String httpGetRequest(String url) {
		HttpClient client = new HttpClient();
		String response_msg = "";
		GetMethod get = new GetMethod(url);
		client.getHttpConnectionManager().getParams().setConnectionTimeout(WeixinConfig.POST_TIMEOUT);
		//get.setRequestHeader("Content-Type", "text/html; charset=utf-8");
		try {
			int success = client.executeMethod(get);
			if(success > 0){
				response_msg = get.getResponseBodyAsString();
				logger("httpGetRequest method response_msg:"+response_msg);
			}
			
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(get != null){
				get.releaseConnection();
			}
		}
		
		
		return response_msg;
	}
	
	public String httpPostRequest(String content,String url) {
		HttpClient client = new HttpClient();
		String response_msg = "";
		PostMethod post = new PostMethod(url);
		//post.setQueryString(content);
		//post.setRequestBody(content);
		RequestEntity requestEntity = new StringRequestEntity(content);
		post.setRequestEntity(requestEntity);
		post.setRequestHeader("Content-type", "text/xml; charset=utf-8");
		client.getHttpConnectionManager().getParams().setConnectionTimeout(WeixinConfig.POST_TIMEOUT);  
		try {
			int success = client.executeMethod(post);
			if(success > 0){
				response_msg = post.getResponseBodyAsString();
				logger("httpPostRequest method response_msg:"+response_msg);
			}
			
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(post != null){
				post.releaseConnection();
			}
		}
		
		return response_msg;
	}
	
	public String wxISOtoUTF(String str)
	{
		String strc = str;
		try {
			strc = new String(str.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return strc;
	}
	
	/**
	 * 	作用：生成可以获得openid的url
	 */
	public String createOauthUrlForOpenid()
	{
		SortedMap<String,Object> url_map = new TreeMap<String,Object>();
		url_map.put("appid", WeixinConfig.APPID);
		url_map.put("secret", WeixinConfig.APPSECRET);
		url_map.put("code", this.code);
		url_map.put("grant_type", "authorization_code");
		String bizString = formatBizQueryParaMap(url_map);
		return "https://api.weixin.qq.com/sns/oauth2/access_token?"+bizString;
	}
	/**
	 * 	作用：生成可以获得code的url
	 */
	public String createOauthUrlForCode(){
		SortedMap<String,Object> url_map = new TreeMap<String,Object>();
		url_map.put("appid", WeixinConfig.APPID);
		url_map.put("redirect_uri", WeixinConfig.WX_LOGIN_CALL_URL);
		url_map.put("response_type", "code");
		//url_map.put("scope", "snsapi_base");
		url_map.put("scope", "snsapi_userinfo");
		url_map.put("state", "STATE"+"#wechat_redirect");
		String biz_string = formatBizQueryParaMap(url_map);
		String code = "https://open.weixin.qq.com/connect/oauth2/authorize?"+biz_string;
		logger("request code is :"+code);
		return code;
	}
	
	/**
	 * 	作用：格式化参数，签名过程需要使用
	 *  将参数按照ASCII字母顺序升序排序
	 */
	private String formatBizQueryParaMap(SortedMap<String,Object> url_map){
		StringBuilder builder = new StringBuilder();
		String str = "";
		for(Map.Entry<String, Object> entry : url_map.entrySet()){
			builder.append("&"+entry.getKey()+"="+entry.getValue());
		}
		if(builder.length() > 0){
			str = builder.toString().substring(1);
		}
		return str;
	}
	////////////////////////////////////////////////////////////////////////////////////////////////
	///
	///  for weixin pay
	///
	////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 获取预支付prepayId
	 * @return
	 */
	public String getPrepayId(SortedMap<String,Object> sorted_map)
	{
		String response = postXml(sorted_map);
		Map<String,Object> result = xmlToArray(response);
		String prepay_id = String.valueOf(result.get("prepay_id"));
		try {
			logger.info("getPrepayId msg=" + Tools.decodeUTF8(String.valueOf(result.get("return_msg"))));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return prepay_id;
	}
	/**
	 * 	作用：将xml转为array
	 */
	public Map<String,Object> xmlToArray(String xml)
	{		
		Map<String,Object> map_value = new HashMap<String,Object>();
		Document document;
		try {
			document = DocumentHelper.parseText(xml);
			Element root_element = document.getRootElement();  
			List<Element> child_element = root_element.elements();
			for(Element ele : child_element){
				map_value.put(ele.getName(), ele.getText());
			}
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map_value;
	}
	

	
	/**
	 * 	作用：post请求xml 调用统一下单接口
	 */
	private String postXml(SortedMap<String,Object> sorted_map)
	{
	    String xml = arrayToXml(sorted_map);
		return httpPostRequest(xml,WeixinConfig.UNIFIED_ORDER);
	}
	

	/**
	 * 	作用：产生随机字符串，不长于32位
	 */
	public String createNoncestr() 
	{
		String chars = "abcdefghijklmnopqrstuvwxyz0123456789";  
		String str ="";
		int current_str_length = 0;
		Random random = new Random();
		for ( int i = 0; i < 32; i++ )  {
			current_str_length = random.nextInt(chars.length()-1);
			str+=  chars.substring(current_str_length,current_str_length+1);
		}
		return str;
	}
	
	/**
	 * 	作用：生成签名
	 */
	public String getSign(SortedMap<String,Object> value_map)
	{
		//将值按照key=value的形式格式化为字符串
		String str= formatBizQueryParaMap(value_map);
		//签名步骤二：在string后加入KEY
		str = str+"&key="+WeixinConfig.KEY;
		logger.info("getSign=" + str);
		//签名步骤三：MD5加密
		str = MD5.md5(str);
		//签名步骤四：所有字符转为大写
		return str.toUpperCase();
	}
	/**
	 * 	作用：array转xml
	 */
	public String arrayToXml(SortedMap<String,Object> map_value)
    {
        String xml = "<xml>";
        
        for(Map.Entry<String, Object> entry : map_value.entrySet()){
        	if(entry.getValue() instanceof Integer){
        		xml+="<"+entry.getKey()+">"+entry.getValue()+"</"+entry.getKey()+">"; 
        	}else{
        		xml+="<"+entry.getKey()+"><![CDATA["+entry.getValue()+"]]></"+entry.getKey()+">";  
        	}
        }
        
        xml +="</xml>";
        return xml; 
    }
	
	/**
	 * 	作用：设置jsapi的参数
	 */
	public SortedMap<String,Object> getParameters()
	{
		SortedMap<String,Object> order_map = new TreeMap<String,Object>();
		order_map.put("appId", WeixinConfig.APPID);
		order_map.put("timeStamp", String.valueOf(System.currentTimeMillis()/1000));
		order_map.put("nonceStr", createNoncestr());
		order_map.put("package", "prepay_id="+prepay_id);
		order_map.put("signType", "MD5");
		order_map.put("paySign", getSign(order_map));

		return order_map;
	}
	
	public void  setCode(String code)
	{
		this.code = code;
	}
	public void setPrepay_id(String prepay_id) {
		this.prepay_id = prepay_id;
	}
	
	
}

class MyX509TrustManager implements X509TrustManager {

	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		// TODO Auto-generated method stub

	}

	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		// TODO Auto-generated method stub

	}

	public X509Certificate[] getAcceptedIssuers() {
		// TODO Auto-generated method stub
		return null;
	}
}