package com.adsys.controller.system.login;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.util.URIUtil;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.taglibs.standard.tag.common.core.Util;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.adsys.controller.base.BaseController;
import com.adsys.service.system.appuser.AppUserManager;
import com.adsys.service.system.sysuser.SysUserManager;
import com.adsys.util.AppUtil;
import com.adsys.util.Const;
import com.adsys.util.Constants;
import com.adsys.util.DateUtil;
import com.adsys.util.JwtUtil;
import com.adsys.util.PageData;
import com.adsys.util.json.JsonResponse;
import com.adsys.weixin.commons.WeixinCommon;

/**
 * 总入口
 * 修改日期：2015/11/2
 */
/**
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value="/login")
public class LoginController extends BaseController  {

	@Resource(name="sysuserService")
	private SysUserManager sysuserService;

	@Resource(name="appuserService")
	private AppUserManager appuserService;

	private JwtUtil jwt;


	/**请求登录，验证用户
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/syslogin" ,produces="application/json;charset=UTF-8")
	@ResponseBody
	public JsonResponse login()throws Exception{
		PageData result = new PageData();
		PageData pd = new PageData();
		String token="";
		pd = this.getPageData();
		String USERNAME =  pd.getString("username");	//登录过来的用户名
		String PASSWORD  = pd.getString("password");	//登录过来的密码
		if(null != USERNAME && !USERNAME.equals("") && PASSWORD != null && !PASSWORD.equals("")){

			pd.put("username", USERNAME);
			String passwd = new SimpleHash("SHA-1", USERNAME, PASSWORD).toString();	//密码加密
			pd.put("password", passwd);
			pd = sysuserService.getUserByNameAndPwd(pd);	//根据用户名和密码去读取用户信息
			if(pd != null){
				sysuserService.updateLastLogin(pd);
				JSONObject obj = new JSONObject();
				obj.put("userid", pd.getString("uid"));
				obj.put("username", pd.getString("username"));
				obj.put("name", pd.getString("name"));
				obj.put("user_type", pd.getString("usertype"));
				obj.put("role_id", pd.getString("roleid"));
				jwt = new JwtUtil();
				token = jwt.createJWT(Const.JWT_ID,obj.toString(),Const.JWT_TTL);
				result.put("token", token);
				result.put("user_type", pd.getString("usertype"));
				result.put("name", pd.getString("name"));
				return ajaxSuccess(result,Constants.REQUEST_01,Constants.REQUEST_OK);
			}else{
				return ajaxFailure(Constants.REQUEST_03, "密码错误");
//				logBefore(logger, USERNAME+"登录系统密码或用户名错误");

			}
		}else{
			return ajaxFailure(Constants.REQUEST_03, "用户不存在");
		}
	}

	/**会员请求登录，验证用户
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/applogin" ,produces="application/json;charset=UTF-8")
	@ResponseBody
	public JsonResponse applogin()throws Exception{
		PageData result = new PageData();
		PageData pd = new PageData();
		String token="";
		pd = this.getPageData();
		String USERNAME =  pd.getString("username");	//登录过来的用户名
		String PASSWORD  = pd.getString("password");	//登录过来的密码
		if(null != USERNAME && !USERNAME.equals("") && PASSWORD != null && !PASSWORD.equals("")){

			pd.put("username", USERNAME);
			String passwd = new SimpleHash("SHA-1", USERNAME, PASSWORD).toString();	//密码加密
			pd.put("password", passwd);
			pd = appuserService.getUserByNameAndPwd(pd);	//根据用户名和密码去读取用户信息
			if(pd != null){
				appuserService.updateLastLogin(pd);
				JSONObject obj = new JSONObject();
				obj.put("userid", pd.getString("uid"));
				obj.put("username", pd.getString("username"));
				obj.put("name", pd.getString("name"));
				obj.put("user_type", pd.getString("usertype"));
				obj.put("role_id", pd.getString("roleid"));
				obj.put("uuid", pd.getString("uuid"));
				jwt = new JwtUtil();
				token = jwt.createJWT(Const.JWT_ID,obj.toString(),Const.JWT_TTL);
				result.put("token", token);
				result.put("user_type", pd.getString("usertype"));
				result.put("name", pd.getString("name"));
				return ajaxSuccess(result,Constants.REQUEST_01,Constants.REQUEST_OK);
			}else{
				return ajaxFailure(Constants.REQUEST_03, "密码错误");
//				logBefore(logger, USERNAME+"登录系统密码或用户名错误");

			}
		}else{
			return ajaxFailure(Constants.REQUEST_03, "用户不存在");
		}
	}


	public static void main(String[] args) {
		String passwd = new SimpleHash("SHA-1", "admin", "123456").toString();
		System.out.println(passwd);
	}
}
