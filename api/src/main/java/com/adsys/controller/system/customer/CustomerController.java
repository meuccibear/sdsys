package com.adsys.controller.system.customer;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.adsys.common.enums.permissionEnum;
import com.adsys.controller.adEditor.util.FileUtils;
import com.adsys.controller.base.BaseController;
import com.adsys.entity.Page;
import com.adsys.service.adEditor.program.impl.AdProgramItemService;
import com.adsys.service.adEditor.program.impl.AdProgramService;
import com.adsys.service.adEditor.resource.impl.AdResourceService;
import com.adsys.service.system.appuser.impl.AppUserService;
import com.adsys.service.system.customer.CustomerManager;
import com.adsys.service.system.device.DeviceManager;
import com.adsys.service.system.device.ext.impl.DeviceExtService;
import com.adsys.service.system.device.impl.DeviceService;
import com.adsys.service.system.group.GroupManager;
import com.adsys.service.system.log.impl.LogService;
import com.adsys.service.system.role.impl.RoleService;
import com.adsys.service.system.schedule.impl.ScheduleService;
import com.adsys.util.AESUtil;
import com.adsys.util.AuthorityUtil;
import com.adsys.util.Const;
import com.adsys.util.Constants;
import com.adsys.util.DateUtil;
import com.adsys.util.JwtUtil;
import com.adsys.util.PageData;
import com.adsys.util.Tools;
import com.adsys.util.json.JsonResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import net.sf.json.JSONObject;


@Controller
@RequestMapping(value="/customer")
public class CustomerController extends BaseController {

	@Resource(name="customerService")
	private CustomerManager customerService;

	@Resource(name="groupService")
	private GroupManager groupsservice;
	
	@Resource(name="roleService")
	private RoleService roleservice;

	@Resource(name="appuserService")
	private AppUserService appuserservice;

	@Resource(name="adResourceService")
	private AdResourceService adResourceService;
	
	@Resource(name="adProgramService")
	private AdProgramService adProgramService;
	
	@Resource(name="adProgramItemService")
	private AdProgramItemService adProgramItemService;
	
	@Resource(name="deviceService")
	private DeviceService deviceservice;

	@Resource(name="deviceExtService")
	private DeviceExtService deviceextservice;

	@Resource(name="logService")
	private LogService logservice;
	
	@Resource(name="scheduleService")
	private ScheduleService scheduleService;
	
	/**
	 * 查询列表
	 */
	@RequestMapping(value = "/customer",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
		public JsonResponse customerList(){
		try {
			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Customer_READ);
			if (auth == false)
				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
			
			PageData pd = this.getPageData();
			if (pd.getString("keywords") != null && !"".equals(pd.getString("keywords")))
				pd.put("keywords", pd.getString("keywords"));
			Page page = new Page();
			page.setPd(pd);
			page.setOffset(pd.getString("offset")==null?0:Integer.valueOf(pd.getString("offset")));
			List<PageData> result = customerService.list(page);
			for(int i = 0; i < result.size(); i++)
			{
				PageData pp = result.get(i);
				String installnum = AESUtil.decrypt(pp.getString("installnum"), Const.AES_KEY);
				String limitnum = AESUtil.decrypt(pp.getString("limitnum"), Const.AES_KEY);
				String exprie = "";
				if (pp.getString("expriedate") != null && pp.getString("expriedate") != "")
					exprie = AESUtil.decrypt(pp.getString("expriedate"), Const.AES_KEY);
				pp.put("installnum", installnum);
				pp.put("limitnum", limitnum);
				pp.put("expriedate", exprie);
				result.set(i, pp);
			}
			return ajaxSuccessPage("customer", result, page, Constants.REQUEST_01,Constants.REQUEST_OK);
		}catch (Exception ex){
			logger.info(ex.getMessage());
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}

	/**
	 * 查询列表:树形
	 */
	@RequestMapping(value = "/list",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse toList(){
		try {
			
			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Customer_READ);
			if (auth == false)
				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
			
			PageData pd = this.getPageData();
			List<PageData> result = customerService.listAll(pd);
			for(int i = 0; i < result.size(); i++)
			{
				PageData pp = result.get(i);
				String installnum = AESUtil.decrypt(pp.getString("installnum"), Const.AES_KEY);
				String limitnum = AESUtil.decrypt(pp.getString("limitnum"), Const.AES_KEY);
				String exprie = "";
				if (pp.getString("expriedate") != null && pp.getString("expriedate") != "")
					exprie = AESUtil.decrypt(pp.getString("expriedate"), Const.AES_KEY);
				pp.put("installnum", installnum);
				pp.put("limitnum", limitnum);
				pp.put("expriedate", exprie);
				result.set(i, pp);
			}
			return ajaxSuccess(result,Constants.REQUEST_01,Constants.REQUEST_OK);
		}catch (Exception ex){
			logger.info(ex.getMessage());
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}
	
	/**
	 * 修改客户信息
	 */
	@RequestMapping(value = "/customer/{id}",method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse updateCustomer(@PathVariable("id") String id){
		try {
			
			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Customer_EDIT);
			if (auth == false)
				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
			
			PageData pd =this.getPageData();
			if(null==id){
				return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
			}
			
			JsonResponse checkResult = customerParamsCheck(pd);
			if(checkResult.isSuccess() == false){
				return checkResult;
			}
			
			pd.put("uuid", id);
			customerService.edit(pd);
			return ajaxSuccess(Constants.REQUEST_01,Constants.REQUEST_OK);
		}catch (Exception ex){
			logger.info(ex.getMessage());
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}
	
	/**
	 * 更新授权
	 */
	@RequestMapping(value = "/license",method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse updateLisence(){
		try {
			
			PageData pd =this.getPageData();
			
			if(null != pd.getString("license") && !"".equals(pd.getString("license"))){
				JwtUtil jwt = new JwtUtil();
				try{
					Claims claims=jwt.parseJWT(pd.getString("license"));
					String body = claims.getSubject();
					JSONObject obj = new JSONObject();
					obj = JSONObject.fromObject(body);
					String curuuid = AuthorityUtil.getRequesterUUID(getRequest());
					String uuid = obj.opt("uuid").toString();
					int exprie = (int)obj.opt("exprie");
					int limitNum = (int)obj.opt("limit");
					System.out.println("uuid=" + uuid + " exprie=" +exprie + " limit=" + limitNum);
					
					if (!uuid.equals(curuuid)){
						return ajaxFailure(Constants.REQUEST_05, "请联系管理员获取正确的授权码");
					}
					
					PageData lpd = new PageData();
					lpd.put("uuid", uuid);
					lpd.put("limitnum", AESUtil.encrypt(String.valueOf(limitNum), Const.AES_KEY));
					if (exprie > 0)
						lpd.put("expriedate", AESUtil.encrypt(DateUtil.getTimeFromDate(DateUtil.currDateAddDay(exprie)), Const.AES_KEY));
					customerService.editLisence(lpd);
					return ajaxSuccess(Constants.REQUEST_01,Constants.REQUEST_OK);
				}catch(SignatureException  e){
					e.printStackTrace();
					return ajaxFailure(Constants.REQUEST_05, "授权无效");
				}catch (ExpiredJwtException  e1) {
					return ajaxFailure(Constants.REQUEST_05, "授权已过期");
				}
			}else{
				return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
			}

		}catch (Exception ex){
			logger.info(ex.getMessage());
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}
	/**
	 * 查询客户详情
	 */
	@RequestMapping(value = "/customer/{id}",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse findCustomerById(@PathVariable(value ="id" ) String id){
		try {
			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Customer_READ);
			if (auth == false)
				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
			
			if(null==id){
				return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
			}
			PageData pd = new PageData();
			pd.put("uuid",id);
			PageData pageData = customerService.findById(pd);
			return ajaxSuccess(pageData,Constants.REQUEST_01,Constants.REQUEST_OK);
		}catch (Exception ex){
			logger.info(ex.getMessage());
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}
	
	/**
	 * 查询license
	 */
	@RequestMapping(value = "/license",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse findLicenseById(){
		try {

			PageData pd = new PageData();
			pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
			PageData pageData = customerService.findById(pd);
			if (pageData != null){
				logger.info("hermanlin=" + pageData.getString("limitnum"));
				String limitnum = pageData.getString("limitnum");
				String result = "";
				if (Integer.valueOf(limitnum) != 0){
					result += "授权设备数：" + limitnum;
				}
				if (pageData.getString("expriedate") != null && pageData.getString("expriedate") != "")
					result += " 到期时间：" + pageData.getString("expriedate");
				
				return ajaxSuccess(result,Constants.REQUEST_01,Constants.REQUEST_OK);
			}
			else
				return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
			
		}catch (Exception ex){
			logger.info(ex.getMessage());
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}
	
	/**
	 * 删除
	 */
	@RequestMapping(value = "/customer/{id}",method = RequestMethod.DELETE,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse deleteById(@PathVariable("id") String id){
		try {
			String rootPath = RequestContextUtils.getWebApplicationContext(this.getRequest()).getServletContext().getRealPath("/");

			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Customer_EDIT);
			if (auth == false)
				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
			
			if(null==id){
				return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
			}
			PageData pd =new PageData();
			pd.put("uuid",id);
			groupsservice.delete(pd);
			roleservice.deleteAll(pd);
			appuserservice.deleteAllU(pd);
			deviceservice.deleteAll(pd);
			logservice.deleteAll(pd);
			adResourceService.deleteAll(pd);
			adProgramItemService.deleteAll(pd);
			adProgramService.deleteAll(pd);
			scheduleService.deleteAllPushItem(pd);
			scheduleService.deleteAllList(pd);
			scheduleService.deleteAllSche(pd);
			customerService.delete(pd);
			
			String userDir = rootPath+"zipDir"+File.separator+id+File.separator;
			FileUtils.delAllFile(userDir);//删除所有用户文件
			return ajaxSuccess(Constants.REQUEST_01,Constants.REQUEST_OK);
		}catch (Exception ex){
			logger.info(ex.getMessage());
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}


	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private JsonResponse customerParamsCheck(PageData params)
	{
		
		if(null == params.getString("cname") || "".equals(params.getString("cname"))){
			return ajaxFailure(Constants.REQUEST_03, "请输入客户名称");
		}
		
		if(null == params.getString("tel") || "".equals(params.getString("tel"))){
			return ajaxFailure(Constants.REQUEST_03, "请输入客户电话");
		}
		
		if(null == params.getString("caddr") || "".equals(params.getString("caddr"))){
			return ajaxFailure(Constants.REQUEST_03, "请输入客户地址");
		}
		
		return ajaxSuccess(Constants.REQUEST_01,Constants.REQUEST_OK);
	}
}
