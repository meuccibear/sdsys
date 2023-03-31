package com.adsys.controller.system.roles;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.adsys.common.enums.permissionEnum;
import com.adsys.controller.base.BaseController;
import com.adsys.entity.Page;
import com.adsys.service.system.device.DeviceManager;
import com.adsys.service.system.role.impl.RoleService;
import com.adsys.service.system.schedule.impl.ScheduleService;
import com.adsys.util.AppUtil;
import com.adsys.util.AuthorityUtil;
import com.adsys.util.Constants;
import com.adsys.util.PageData;
import com.adsys.util.Tools;
import com.adsys.util.json.JsonResponse;

/** 
 * 说明：
 * 创建时间：2018年4月18日
 */
@Controller
@RequestMapping(value="/roles")
public class RolesController extends BaseController {
	
	@Resource(name="roleService")
	private RoleService roleservice;

	
	/**
	 * 新增
	 * @return
	 */
	@RequestMapping(value = "/role" ,method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse roleAdd(){
		try{
			
			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Sys_Role_WRITE);
			if (auth == false)
				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
			
			PageData pd =this.getPageData();
			if(null!=pd&&pd.size()>0){
				JsonResponse checkResult = roleParamsCheck(pd);
				if(checkResult.isSuccess() == false){
					return checkResult;
				}
				pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
				pd.put("id",this.get32UUID());
				roleservice.save(pd);
				return ajaxSuccess(Constants.REQUEST_01,Constants.REQUEST_OK);
			}else{
				return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
			}

		}catch (Exception ex){
			logger.info("add role error !!!");
			logger.info(ex.getMessage());
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}
	
	/**
	 * 查询列表
	 */
	@RequestMapping(value = "/roles",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse toList(){
		try {
			
			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Sys_Role_READ);
			if (auth == false)
				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
			
			PageData pd = this.getPageData();
			pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
			
			if (pd.getString("keywords") != null && !"".equals(pd.getString("keywords")))
				pd.put("keywords", pd.getString("keywords"));
			
			Page page = new Page();
			page.setPd(pd);			page.setOffset(Integer.parseInt(pd.getString("offset")));
			List<PageData> result = roleservice.list(page);
			return ajaxSuccessPage("role", result, page, Constants.REQUEST_01,Constants.REQUEST_OK);
		}catch (Exception ex){
			logger.info(ex.getMessage());
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}

	/**
	 * 查询列表
	 */
	@RequestMapping(value = "/alllist",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse allList(){
		try {
			
			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Sys_Role_READ);
			if (auth == false)
				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
			
			PageData pd = this.getPageData();
			pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
			
			List<PageData> result = roleservice.listAll(pd);
			return ajaxSuccess(result, Constants.REQUEST_01,Constants.REQUEST_OK);
		}catch (Exception ex){
			logger.info(ex.getMessage());
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}
	
	/**
	 * 修改信息
	 */
	@RequestMapping(value = "/role/{id}",method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse toupdate(@PathVariable("id") String id){
		try {
			
			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Sys_Role_WRITE);
			if (auth == false)
				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
			
			PageData pd =this.getPageData();
			if(null==id){
				return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
			}
			JsonResponse checkResult = roleParamsCheck(pd);
			if(checkResult.isSuccess() == false){
				return checkResult;
			}
			pd.put("id",id);
			pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
			roleservice.edit(pd);
			return ajaxSuccess(Constants.REQUEST_01,Constants.REQUEST_OK);
		}catch (Exception ex){
			logger.info(ex.getMessage());
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}
	/**
	 * 查询详情
	 */
	@RequestMapping(value = "/role/{id}",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse findById(@PathVariable(value ="id" ) String id){
		try {
			
			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Sys_Role_READ);
			if (auth == false)
				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
			
			if(null==id){
				return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
			}
			PageData pd = new PageData();
			pd.put("id",id);
			pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
			PageData pageData = roleservice.findById(pd);
			return ajaxSuccess(pageData,Constants.REQUEST_01,Constants.REQUEST_OK);
		}catch (Exception ex){
			logger.info(ex.getMessage());
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/role/{id}",method = RequestMethod.DELETE,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse deleteById(@PathVariable("id") String id){
		try {
			
			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Sys_Role_WRITE);
			if (auth == false)
				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
			
			if(null==id){
				return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
			}
			
			if (id.indexOf("default") > -1){
				return ajaxFailure(Constants.REQUEST_03, "不能删除系统固定角色");
			}
			
			PageData pd =new PageData();
			pd.put("id",id);
			pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
			roleservice.delete(pd);
			return ajaxSuccess(Constants.REQUEST_01,Constants.REQUEST_OK);
		}catch (Exception ex){
			logger.info(ex.getMessage());
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}

	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private JsonResponse roleParamsCheck(PageData params)
	{
		
		if(null == params.getString("rolename") || "".equals(params.getString("rolename"))){
			return ajaxFailure(Constants.REQUEST_03, "请输入角色名");
		}
		
		if(null == params.getString("permission") || "".equals(params.getString("permission"))){
			return ajaxFailure(Constants.REQUEST_03, "请输入角色权限");
		}
		
		if(null == params.getString("permissionname") || "".equals(params.getString("permissionname"))){
			return ajaxFailure(Constants.REQUEST_03, "请输入角色权限详情");
		}
		
		if(null == params.getString("roletype") || "".equals(params.getString("roletype"))){
			return ajaxFailure(Constants.REQUEST_03, "请输入角色类型");
		}
		
		return ajaxSuccess(Constants.REQUEST_01,Constants.REQUEST_OK);
	}
}
