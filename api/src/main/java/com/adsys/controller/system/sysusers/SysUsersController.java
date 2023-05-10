package com.adsys.controller.system.sysusers;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.adsys.common.enums.permissionEnum;
import com.adsys.controller.base.BaseController;
import com.adsys.entity.Page;
import com.adsys.service.system.device.DeviceManager;
import com.adsys.service.system.schedule.impl.ScheduleService;
import com.adsys.service.system.sysuser.impl.SysUserService;
import com.adsys.util.AppUtil;
import com.adsys.util.AuthorityUtil;
import com.adsys.util.Constants;
import com.adsys.util.DateUtil;
import com.adsys.util.MD5;
import com.adsys.util.PageData;
import com.adsys.util.Tools;
import com.adsys.util.json.JsonResponse;


@Controller
@RequestMapping(value="/sysusers")
public class SysUsersController extends BaseController {
	
	@Resource(name="sysuserService")
	private SysUserService sysuserservice;

	
	/**
	 * 新增
	 * @return
	 */
	@RequestMapping(value = "/sysuser" ,method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse userAdd(){
		try{
			
			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Sys_User_WRITE);
			if (auth == false)
				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
			
			PageData pd =this.getPageData();
			if(null!=pd&&pd.size()>0){
				JsonResponse checkResult = userParamsCheck(pd);
				if(checkResult.isSuccess() == false){
					return checkResult;
				}
				
				pd.put("uid",this.get32UUID());
				pd.put("status", "activity");
				pd.put("usertype", "admin");
				pd.put("roleid", "admingobl");
				pd.put("adddate", DateUtil.getTime());
				pd.put("logindate", DateUtil.getTime());
				pd.put("password", new SimpleHash("SHA-1", pd.getString("username"), pd.getString("password")).toString());	//密码加密
				if(null == sysuserservice.findByUsername(pd)){	//判断用户名是否存在
					sysuserservice.saveU(pd);
					return ajaxSuccess(Constants.REQUEST_01,Constants.REQUEST_OK);
				}
				else
					return ajaxFailure(Constants.REQUEST_03,"用户已存在");
			}else{
				return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
			}

		}catch (Exception ex){
			logger.info("add user error !!!");
			logger.error(ex);
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}
	
	/**
	 * 查询列表
	 */
	@RequestMapping(value = "/sysusers",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse toList(){
		try {
			
			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Sys_User_READ);
			if (auth == false)
				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
			
			PageData pd = this.getPageData();
			if (pd.getString("keywords") != null && !"".equals(pd.getString("keywords")))
				pd.put("keywords", pd.getString("keywords"));
			Page page = new Page();
			page.setPd(pd);
			page.setOffset(pd.getString("offset")==null?0:Integer.valueOf(pd.getString("offset")));
			List<PageData> result = sysuserservice.list(page);
			return ajaxSuccessPage("sysuser", result, page, Constants.REQUEST_01,Constants.REQUEST_OK);
		}catch (Exception ex){
			logger.error(ex);
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}

	/**
	 * 修改信息
	 */
	@RequestMapping(value = "/sysuser/{id}",method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse toupdate(@PathVariable("id") String id){
		try {
			
			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Sys_User_WRITE);
			if (auth == false)
				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
			
			PageData pd =this.getPageData();
			if(null==id){
				return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
			}
			JsonResponse checkResult = userParamsCheck(pd);
			if(checkResult.isSuccess() == false){
				return checkResult;
			}
			
			pd.put("uid",id);
			pd.put("usertype", "admin");
			pd.put("roleid", "admingobl");
			if(pd.getString("password") != null && !"".equals(pd.getString("password"))){
				pd.put("password", new SimpleHash("SHA-1", pd.getString("username"), pd.getString("password")).toString());	//密码加密
			}
			sysuserservice.editU(pd);
			return ajaxSuccess(Constants.REQUEST_01,Constants.REQUEST_OK);
		}catch (Exception ex){
			logger.error(ex);
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}
	/**
	 * 查询详情
	 */
	@RequestMapping(value = "/sysuser/{id}",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse findById(@PathVariable(value ="id" ) String id){
		try {
			
			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Sys_User_READ);
			if (auth == false)
				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
			
			if(null==id){
				return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
			}
			PageData pd = new PageData();
			pd.put("uid",id);
			PageData pageData = sysuserservice.findById(pd);
			pageData.remove("password");//把密码屏蔽
			return ajaxSuccess(pageData,Constants.REQUEST_01,Constants.REQUEST_OK);
		}catch (Exception ex){
			logger.error(ex);
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}

	/**
	 * 查询profile
	 */
	@RequestMapping(value = "/profile",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse getProfile(){
		try {
			PageData pd = new PageData();
			pd.put("uid", AuthorityUtil.getRequesterUid(getRequest()));
			PageData rst = sysuserservice.findById(pd);
			
			pd.clear();
			pd.put("username", rst.getString("username"));
			pd.put("name", rst.getString("name"));
			pd.put("tel", rst.getString("tel"));
			pd.put("email", rst.getString("email"));
			return ajaxSuccess(pd,Constants.REQUEST_01,Constants.REQUEST_OK);
		}catch (Exception ex){
			logger.error(ex);
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}
	
	/**
	 * 修改基础信息
	 */
	@RequestMapping(value = "/profile",method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse toUpdateProfile(){
		try {
			PageData pd = this.getPageData();
			
			if(null == pd.getString("name") || "".equals(pd.getString("name"))){
				return ajaxFailure(Constants.REQUEST_03, "请输入姓名");
			}
			
			if(null == pd.getString("tel") || "".equals(pd.getString("tel"))){
				return ajaxFailure(Constants.REQUEST_03, "请输入电话");
			}
			
			
			pd.put("uid", AuthorityUtil.getRequesterUid(getRequest()));
			
			if(pd.getString("password") != null && !"".equals(pd.getString("password"))){
				pd.put("password", new SimpleHash("SHA-1", AuthorityUtil.getRequesterUserName(getRequest()), pd.getString("password")).toString());	//密码加密
			}
			sysuserservice.editProfile(pd);
			return ajaxSuccess(Constants.REQUEST_01,Constants.REQUEST_OK);
		}catch (Exception ex){
			logger.error(ex);
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}
	
	/**
	 * 删除
	 */
	@RequestMapping(value = "/sysuser/{id}",method = RequestMethod.DELETE,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse deleteById(@PathVariable("id") String id){
		try {
			
			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Sys_User_WRITE);
			if (auth == false)
				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
			
			if(null==id){
				return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
			}
			PageData pd =new PageData();
			pd.put("uid",id);
			sysuserservice.deleteU(pd);
			return ajaxSuccess(Constants.REQUEST_01,Constants.REQUEST_OK);
		}catch (Exception ex){
			logger.error(ex);
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}

	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private JsonResponse userParamsCheck(PageData params)
	{
		if(null == params.getString("username") || "".equals(params.getString("username"))){
			return ajaxFailure(Constants.REQUEST_03, "请输入用户名");
		}
		
		if(null == params.getString("name") || "".equals(params.getString("name"))){
			return ajaxFailure(Constants.REQUEST_03, "请输入姓名");
		}
		
		if(null == params.getString("tel") || "".equals(params.getString("tel"))){
			return ajaxFailure(Constants.REQUEST_03, "请输入电话");
		}
		
		if(null == params.getString("email") || "".equals(params.getString("email"))){
			return ajaxFailure(Constants.REQUEST_03, "请输入邮件地址");
		}
		
		return ajaxSuccess(Constants.REQUEST_01,Constants.REQUEST_OK);
	}
}
