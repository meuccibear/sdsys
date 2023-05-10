package com.adsys.controller.system.reset;

import java.sql.Timestamp;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.adsys.controller.base.BaseController;
import com.adsys.entity.system.UserResetPassword;
import com.adsys.service.system.appuser.impl.AppUserService;
import com.adsys.util.AuthorityUtil;
import com.adsys.util.Constants;
import com.adsys.util.DateUtil;
import com.adsys.util.PageData;
import com.adsys.util.Sendsms;
import com.adsys.util.json.JsonResponse;




/**
 * 找回密码
 * @author FY
 *
 */
@Controller
@RequestMapping(value="/reset")
public class ResetPasswordController extends BaseController{

	@Resource(name="appuserService")
	private AppUserService appuserservice;

	//重置密码
	@RequestMapping(value = "/passwd",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse findPwd(UserResetPassword user) {
		if(user.getUsername().equals("")) {
			return ajaxFailure(Constants.REQUEST_05,"用户名为空");
		}
		PageData pd = new PageData();
		try{
			pd.put("username", user.getUsername());
			PageData userpd = appuserservice.findByUsername(pd);
			if (userpd == null){
				return ajaxFailure(Constants.REQUEST_05,"用户不存在");
			}

			if(!user.getTel().equals(userpd.getString("tel"))) {
				return ajaxFailure(Constants.REQUEST_05,"与系统记录的手机号码不符");
			}

			if(!user.getCheck().equals(userpd.getString("checkcode"))) {
				return ajaxFailure(Constants.REQUEST_05,"验证码不正确");
			}

			if (DateUtil.getDiffMin(DateUtil.formatSdfTimes((Timestamp)userpd.get("checkdate")), DateUtil.getTime()) > 3 * (1000 * 60))
				return ajaxFailure(Constants.REQUEST_05,"验证码已过期");

			userpd.put("password", new SimpleHash("SHA-1", user.getUsername(), user.getPassword()).toString());
			appuserservice.editU(userpd);

			return ajaxSuccess(Constants.REQUEST_01,Constants.REQUEST_OK);
		}catch (Exception ex){
			logger.error(ex);
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}

	//获取验证码
	@RequestMapping(value = "/checkcode",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse sendCheck(@RequestParam("tel") String tel, @RequestParam("username") String username) {
		StringBuilder codes = new StringBuilder();
		Random random = new Random();
		// 生成6位验证码
		for (int i = 0; i < 6; i++) {
			codes.append(String.valueOf(random.nextInt(10)));
		}
		try{
			PageData pd = new PageData();
			pd.put("username", username);
			pd.put("tel", tel);
			pd.put("checkcode", codes.toString());
			appuserservice.updateCheckCode(pd);
		}catch (Exception ex){
			logger.error(ex);
			return ajaxFailure(Constants.REQUEST_05,"用户名或手机号码不存在");
		}
		Sendsms.sendsms(codes, tel);

		return ajaxSuccess(Constants.REQUEST_01,Constants.REQUEST_OK);
	}
}
