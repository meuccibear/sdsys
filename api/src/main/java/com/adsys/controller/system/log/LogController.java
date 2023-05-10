package com.adsys.controller.system.log;

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
import com.adsys.service.system.customer.CustomerManager;
import com.adsys.service.system.log.LogManager;
import com.adsys.util.AppUtil;
import com.adsys.util.AuthorityUtil;
import com.adsys.util.Constants;
import com.adsys.util.DateUtil;
import com.adsys.util.PageData;
import com.adsys.util.Tools;
import com.adsys.util.json.JsonResponse;
import com.alibaba.fastjson.JSONArray;


@Controller
@RequestMapping(value="/loger")
public class LogController extends BaseController {
	
	@Resource(name="logService")
	private LogManager logService;


	/**
	 * 查询log列表
	 */
	@RequestMapping(value = "/log",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse productList(){
		try {
			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Device_READ);
			if (auth == false)
				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
			
			PageData pd = this.getPageData();
			Page page = new Page();
			pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
			if (pd.getString("keywords") != null && !"".equals(pd.getString("keywords"))){
				pd.put("keywords", pd.getString("keywords"));
			}
			page.setPd(pd);
			page.setOffset(pd.getString("offset")==null?0:Integer.valueOf(pd.getString("offset")));
			List<PageData> result = logService.list(page);
			return ajaxSuccessPage("log", result, page, Constants.REQUEST_01,Constants.REQUEST_OK);
		}catch (Exception ex){
			logger.error(ex);
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}

	
	/**
	 * 查询详情
	 */
	@RequestMapping(value = "/log/{id}",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse findGroupById(@PathVariable("id") String id){
		try {
			PageData pd = this.getPageData();
			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Setting_WRITE);
			if (auth == false)
			{
				String userType = AuthorityUtil.getRequesterUserType(getRequest());
				if (!"customer".equals(userType))
					return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
				else
					pd.put("uid", AuthorityUtil.getRequesterUid(getRequest()));
			}
			
			pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
			pd.put("id", id);
			PageData pageData = logService.findById(pd);

			return ajaxSuccess(pageData,Constants.REQUEST_01,Constants.REQUEST_OK);
		}catch (Exception ex){
			logger.error(ex);
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}
	
	/**
	 * 删除
	 */
	@RequestMapping(value = "/log/{id}",method = RequestMethod.DELETE,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse deleteById(@PathVariable("id") String id){
		try {
			
			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Setting_WRITE);
			if (auth == false)
				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
			
			if(null==id){
				return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
			}
			
			PageData pd =new PageData();
			pd.put("id",id);
			pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
			logService.delete(pd);
			return ajaxSuccess(Constants.REQUEST_01,Constants.REQUEST_OK);
		}catch (Exception ex){
			logger.error(ex);
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}

}
