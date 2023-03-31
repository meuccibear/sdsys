package com.adsys.controller.system.weixin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.adsys.common.enums.permissionEnum;
import com.adsys.controller.base.BaseController;
import com.adsys.dao.redis.RedisDao;
import com.adsys.entity.Page;
import com.adsys.service.system.appuser.AppUserManager;
import com.adsys.service.system.customer.CustomerManager;
import com.adsys.service.system.schedule.impl.ScheduleService;
import com.adsys.service.system.sysuser.SysUserManager;
import com.adsys.util.AppUtil;
import com.adsys.util.AuthorityUtil;
import com.adsys.util.Const;
import com.adsys.util.Constants;
import com.adsys.util.DateUtil;
import com.adsys.util.JwtUtil;
import com.adsys.util.PageData;
import com.adsys.util.Tools;
import com.adsys.util.json.JsonResponse;
import com.adsys.weixin.commons.WeixinCommon;
import com.adsys.weixin.commons.WeixinJsSdk;
import com.adsys.weixin.config.WeixinConfig;
import org.apache.commons.lang.StringUtils;
/**

 * 微信接口
 * 修改日期：2017/11/2
 */
/**
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value="/weixin")
public class weixinController extends BaseController  {

	@Resource(name = "redisDaoImpl")
	private RedisDao redisDaoImpl;
	
	/**
	 * jssdk sign
	 */
	@RequestMapping(value = "/jsSign",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
		public JsonResponse jsSdkSign(){
		try {
			PageData pd = this.getPageData();
			String token_access = null;
			WeixinCommon common = new WeixinCommon();
			
			if(null == pd.getString("url") || "".equals(pd.getString("url"))){
				return ajaxFailure(Constants.REQUEST_03, "请输入访问地址");
			}
			
			try {
				token_access = redisDaoImpl.get("token_access");
				if(StringUtils.isBlank(token_access)) {
					token_access = common.getAccess_token(WeixinConfig.APPID, WeixinConfig.APPSECRET);
					redisDaoImpl.addString("token_access", token_access);
					redisDaoImpl.setExpire("token_access", 60*100);
				}
			} catch (Exception e) {
				token_access = common.getAccess_token(WeixinConfig.APPID, WeixinConfig.APPSECRET);
				redisDaoImpl.addString("token_access", token_access);
				redisDaoImpl.setExpire("token_access", 60*100);
			}
			
			logger.info("token_access=" + token_access);
			String ticket = common.getJsapiTicket(token_access);
			logger.info("tocket=" + ticket);
			Map<String, String> rst = WeixinJsSdk.jsSign(ticket, pd.getString("url"));
			return ajaxSuccess(rst, Constants.REQUEST_01,Constants.REQUEST_OK);		
		}catch (Exception ex){
			logger.info(ex.getMessage());
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}

}
