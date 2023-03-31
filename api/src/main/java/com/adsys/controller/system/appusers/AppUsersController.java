package com.adsys.controller.system.appusers;

import java.io.File;
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
import org.springframework.web.servlet.support.RequestContextUtils;

import com.adsys.common.enums.permissionEnum;
import com.adsys.controller.base.BaseController;
import com.adsys.entity.Page;
import com.adsys.service.adEditor.resource.impl.AdResourceService;
import com.adsys.service.system.appuser.impl.AppUserService;
import com.adsys.service.system.customer.CustomerManager;
import com.adsys.service.system.device.DeviceManager;
import com.adsys.service.system.group.GroupManager;
import com.adsys.service.system.role.impl.RoleService;
import com.adsys.service.system.schedule.impl.ScheduleService;
import com.adsys.util.AESUtil;
import com.adsys.util.AppUtil;
import com.adsys.util.AuthorityUtil;
import com.adsys.util.Const;
import com.adsys.util.Constants;
import com.adsys.util.DateUtil;
import com.adsys.util.MD5;
import com.adsys.util.PageData;
import com.adsys.util.Tools;
import com.adsys.util.json.JsonResponse;


@Controller
@RequestMapping(value="/appusers")
public class AppUsersController extends BaseController {
	
	@Resource(name="appuserService")
	private AppUserService appuserservice;

	@Resource(name="customerService")
	private CustomerManager customerService;
	
	@Resource(name="roleService")
	private RoleService roleservice;
	
	@Resource(name="adResourceService")
	private AdResourceService adResourceService;

	@Resource(name="groupService")
	private GroupManager groupsservice;
	
	/**
	 * 新增成员
	 * @return
	 */
	@RequestMapping(value = "/member" ,method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse userMember(){
		try{
			
			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.App_User_WRITE);
			if (auth == false)
				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
			
			PageData pd =this.getPageData();
			if(null!=pd&&pd.size()>0){
				JsonResponse checkResult = userParamsCheck(pd);
				if(checkResult.isSuccess() == false){
					return checkResult;
				}
				
				pd.put("uid",this.get32UUID());
				pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
				pd.put("usertype", "appuser");
				pd.put("status", "activity");
				pd.put("adddate", DateUtil.getTime());
				pd.put("logindate", DateUtil.getTime());
				pd.put("password", new SimpleHash("SHA-1", pd.getString("username"), pd.getString("password")).toString());	//密码加密
				if(null == appuserservice.findByUsername(pd)){	//判断用户名是否存在
					appuserservice.saveU(pd);
					return ajaxSuccess(Constants.REQUEST_01,Constants.REQUEST_OK);
				}
				else
					return ajaxFailure(Constants.REQUEST_03,"用户已存在");
			}else{
				return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
			}

		}catch (Exception ex){
			logger.info("add user error !!!");
			logger.info(ex.getMessage());
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}
	
	/**
	 * 注册接口
	 * @return
	 */
	@RequestMapping(value = "/reg" ,method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse userReg(){
		try{
			PageData pd =this.getPageData();
			if(null!=pd&&pd.size()>0){
				JsonResponse checkResult = userRegParamsCheck(pd);
				if(checkResult.isSuccess() == false){
					return checkResult;
				}
				
				PageData customer = new PageData();
				customer.put("uuid", get32UUID());
				customer.put("cname", pd.getString("cname"));
				customer.put("tel", pd.getString("tel"));
				customer.put("caddr", pd.getString("caddr"));
				customer.put("cstatus", "activity");
				customer.put("installnum", AESUtil.encrypt("0", Const.AES_KEY));
				customer.put("limitnum", AESUtil.encrypt("3", Const.AES_KEY));
				customer.put("adddate", DateUtil.getTime());
				customerService.save(customer);
				
				PageData role = new PageData();
				role.put("id",this.get32UUID());
				role.put("uuid", customer.getString("uuid"));
				role.put("rolename", "管理员");
				role.put("roletype", "appuser");
				role.put("permission", "100,103,104,105,106,300,301,302,400,401,500,501,600,601,700,701,703,702,800,801,802");
				role.put("permissionname", "用户角色管理,角色写权限,角色读权限,用户写权限,用户读权限,设备管理,设备查看,设备编辑删除,群组管理,群组编辑删除,文件管理,节目管理,节目审核,节目发布,节目编辑删除,系统设置,远程升级");
				roleservice.save(role);
				
				pd.put("uid",this.get32UUID());
				pd.put("uuid", customer.getString("uuid"));
				pd.put("roleid", role.getString("id"));
				pd.put("usertype", "appuser");
				pd.put("status", "activity");
				pd.put("adddate", DateUtil.getTime());
				pd.put("logindate", DateUtil.getTime());
				pd.put("password", new SimpleHash("SHA-1", pd.getString("username"), pd.getString("password")).toString());	//密码加密
				if(null == appuserservice.findByUsername(pd)){	//判断用户名是否存在
					try{
						appuserservice.saveU(pd);
						
						PageData resource = new PageData();
						resource.put("uuid", customer.getString("uuid"));
						resource.put("id", this.get32UUID());
						resource.put("pid", "-1");
						resource.put("resName", "根目录");
						resource.put("des", "根目录");
						resource.put("resType", 0);
						resource.put("resPath", "upload/" + customer.getString("uuid"));
						adResourceService.save(resource);
						
						String rootPath = RequestContextUtils.getWebApplicationContext(this.getRequest()).getServletContext().getRealPath("/");
						File dir = new File(rootPath+"/"+resource.getString("resPath"));
						if(!dir.exists()) {
							dir.mkdirs();
						}
						
						PageData group = new PageData();
						group.put("uuid", customer.getString("uuid"));
						group.put("gpid", 0);
						group.put("gname", "全部");
						group.put("gpath", "/");
						groupsservice.save(group);
					}catch (Exception ex){
						customerService.delete(customer);
						return ajaxFailure(Constants.REQUEST_05,"创建用户失败，可能存在重名。");
					}
					return ajaxSuccess(Constants.REQUEST_01,Constants.REQUEST_OK);
				}
				else{
					customerService.delete(customer);
					return ajaxFailure(Constants.REQUEST_03,"用户已存在");
				}	
			}else{
				return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
			}

		}catch (Exception ex){
			logger.info("add app user error !!!");
			logger.info(ex.getMessage());
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}
	
	/**
	 * 查询列表
	 */
	@RequestMapping(value = "/appusers",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse toList(){
		try {
			
			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.App_User_READ);
			if (auth == false)
				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
			
			PageData pd = this.getPageData();
			pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
			if (pd.getString("keywords") != null && !"".equals(pd.getString("keywords")))
				pd.put("keywords", pd.getString("keywords"));
			Page page = new Page();
			page.setPd(pd);			page.setOffset(pd.getString("offset")==null?0:Integer.valueOf(pd.getString("offset")));
			List<PageData> result = appuserservice.list(page);
			return ajaxSuccessPage("appuser", result, page, Constants.REQUEST_01,Constants.REQUEST_OK);
		}catch (Exception ex){
			logger.info(ex.getMessage());
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}

	
	/**
	 * 查询成员列表
	 */
	@RequestMapping(value = "/members",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse toMemberList(){
		try {
			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.App_User_READ);
			if (auth == false)
				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
			
			PageData pd = this.getPageData();
			pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
			pd.put("keywords", pd.getString("keywords"));
			Page page = new Page();
			page.setPd(pd);
			page.setOffset(pd.getString("offset")==null?0:Integer.valueOf(pd.getString("offset")));
			List<PageData> result = appuserservice.list(page);
			return ajaxSuccessPage("member", result, page, Constants.REQUEST_01,Constants.REQUEST_OK);
		}catch (Exception ex){
			logger.info(ex.getMessage());
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}
	
	
	/**
	 * 修改信息
	 */
	@RequestMapping(value = "/appuser/{id}",method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse toupdate(@PathVariable("id") String id){
		try {
			
			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.App_User_WRITE);
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
			pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
			pd.put("uid",id);
			if(pd.getString("password") != null && !"".equals(pd.getString("password"))){
				pd.put("password", new SimpleHash("SHA-1", pd.getString("username"), pd.getString("password")).toString());	//密码加密
			}
			appuserservice.editU(pd);
			return ajaxSuccess(Constants.REQUEST_01,Constants.REQUEST_OK);
		}catch (Exception ex){
			logger.info(ex.getMessage());
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}
	
	/**
	 * 修改信息
	 */
	@RequestMapping(value = "/member/{id}",method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse toupdateMember(@PathVariable("id") String id){
		try {
			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.App_User_WRITE);
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
			pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
			pd.put("usertype", "appuser");
			if(pd.getString("password") != null && !"".equals(pd.getString("password"))){
				pd.put("password", new SimpleHash("SHA-1", pd.getString("username"), pd.getString("password")).toString());	//密码加密
			}
			appuserservice.editU(pd);
			return ajaxSuccess(Constants.REQUEST_01,Constants.REQUEST_OK);
		}catch (Exception ex){
			logger.info(ex.getMessage());
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
			pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
			pd.put("uid", AuthorityUtil.getRequesterUid(getRequest()));
			PageData rst = appuserservice.findById(pd);
			
			pd.clear();
			pd.put("uid", AuthorityUtil.getRequesterUid(getRequest()));
			pd.put("username", rst.getString("username"));
			pd.put("name", rst.getString("name"));
			pd.put("tel", rst.getString("tel"));
			pd.put("email", rst.getString("email"));
			return ajaxSuccess(pd,Constants.REQUEST_01,Constants.REQUEST_OK);
		}catch (Exception ex){
			logger.info(ex.getMessage());
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
				return ajaxFailure(Constants.REQUEST_03, "请输入手机号码");
			}
			
			pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
			pd.put("uid", AuthorityUtil.getRequesterUid(getRequest()));
			
			if(pd.getString("password") != null && !"".equals(pd.getString("password"))){
				pd.put("password", new SimpleHash("SHA-1", AuthorityUtil.getRequesterUserName(getRequest()), pd.getString("password")).toString());	//密码加密
			}
			appuserservice.editProfile(pd);
			return ajaxSuccess(Constants.REQUEST_01,Constants.REQUEST_OK);
		}catch (Exception ex){
			logger.info(ex.getMessage());
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}
	
	
	
	/**
	 * 查询详情
	 */
	@RequestMapping(value = "/appuser/{id}",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse findById(@PathVariable(value ="id" ) String id){
		try {
			
			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.App_User_READ);
			if (auth == false)
				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
			
			if(null==id){
				return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
			}
			PageData pd = new PageData();
			pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
			pd.put("uid",id);
			PageData pageData = appuserservice.findById(pd);
			pageData.remove("password");//把密码屏蔽
			return ajaxSuccess(pageData,Constants.REQUEST_01,Constants.REQUEST_OK);
		}catch (Exception ex){
			logger.info(ex.getMessage());
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}

	/**
	 * 查询详情
	 */
	@RequestMapping(value = "/member/{id}",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse findMemberById(@PathVariable(value ="id" ) String id){
		try {
			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.App_User_READ);
			if (auth == false)
				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
			
			if(null==id){
				return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
			}
			PageData pd = new PageData();
			pd.put("uid",id);
			pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
			PageData pageData = appuserservice.findById(pd);
			pageData.put("user_type", pageData.getString("role_id").substring(pageData.getString("role_id").indexOf("_")+1, pageData.getString("role_id").length()-1));
			pageData.remove("password");//把密码屏蔽
			return ajaxSuccess(pageData,Constants.REQUEST_01,Constants.REQUEST_OK);
		}catch (Exception ex){
			logger.info(ex.getMessage());
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}
	
	/**
	 * 删除
	 */
	@RequestMapping(value = "/appuser/{id}",method = RequestMethod.DELETE,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse deleteById(@PathVariable("id") String id){
		try {
			
			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.App_User_WRITE);
			if (auth == false)
				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
			
			if(null==id){
				return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
			}
			PageData pd =new PageData();
			pd.put("uid",id);
			pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
			appuserservice.deleteU(pd);
			return ajaxSuccess(Constants.REQUEST_01,Constants.REQUEST_OK);
		}catch (Exception ex){
			logger.info(ex.getMessage());
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}
	
	/**
	 * 删除
	 */
	@RequestMapping(value = "/member/{id}",method = RequestMethod.DELETE,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse deleteMemberById(@PathVariable("id") String id){
		try {
			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.App_User_WRITE);
			if (auth == false)
				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
			
			if(null==id){
				return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
			}
			PageData pd =new PageData();
			pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
			pd.put("uid",id);
			appuserservice.deleteU(pd);
			return ajaxSuccess(Constants.REQUEST_01,Constants.REQUEST_OK);
		}catch (Exception ex){
			logger.info(ex.getMessage());
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
		
		if(null == params.getString("roleid") || "".equals(params.getString("roleid"))){
			return ajaxFailure(Constants.REQUEST_03, "请输入角色ID");
		}
		
		
		return ajaxSuccess(Constants.REQUEST_01,Constants.REQUEST_OK);
	}
	
	private JsonResponse userRegParamsCheck(PageData params)
	{
		if(null == params.getString("cname") || "".equals(params.getString("cname"))){
			return ajaxFailure(Constants.REQUEST_03, "请输入客户名称");
		}
		
		if(null == params.getString("caddr") || "".equals(params.getString("caddr"))){
			return ajaxFailure(Constants.REQUEST_03, "请输入客户地址");
		}
		
		if(null == params.getString("username") || "".equals(params.getString("username"))){
			return ajaxFailure(Constants.REQUEST_03, "请输入用户名");
		}
		
		if(null == params.getString("name") || "".equals(params.getString("name"))){
			return ajaxFailure(Constants.REQUEST_03, "请输入姓名");
		}
		
		if(null == params.getString("tel") || "".equals(params.getString("tel"))){
			return ajaxFailure(Constants.REQUEST_03, "请输入手机号码");
		}
		
		if(null == params.getString("password") || "".equals(params.getString("password"))){
			return ajaxFailure(Constants.REQUEST_03, "请输入密码");
		}
		
		if(null == params.getString("password2") || "".equals(params.getString("password2"))){
			return ajaxFailure(Constants.REQUEST_03, "请重复输入一次密码");
		}
		
		if (!params.getString("password").equals(params.getString("password2"))){
			return ajaxFailure(Constants.REQUEST_03, "两次输入密码不对应");
		}
		
		return ajaxSuccess(Constants.REQUEST_01,Constants.REQUEST_OK);
	}
}
