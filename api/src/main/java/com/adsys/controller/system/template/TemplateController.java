package com.adsys.controller.system.template;

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
import com.adsys.service.system.group.impl.GroupService;
import com.adsys.service.system.schedule.impl.ScheduleService;
import com.adsys.service.system.template.impl.TemplateService;
import com.adsys.util.AppUtil;
import com.adsys.util.AuthorityUtil;
import com.adsys.util.Const;
import com.adsys.util.Constants;
import com.adsys.util.DateUtil;
import com.adsys.util.JwtUtil;
import com.adsys.util.PageData;
import com.adsys.util.Tools;
import com.adsys.util.json.JsonResponse;

import net.sf.json.JSONObject;


@Controller
@RequestMapping(value="/templates")
public class TemplateController extends BaseController {
	
	@Resource(name="templateService")
	private TemplateService templateservice;


	
	/**
	 * 新增接口
	 * @return
	 */
	@RequestMapping(value = "/template" ,method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse addEvaluate(){
		try{
			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Program_EDIT);
			if (auth == false)
				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
			
			PageData pd = this.getPageData();
			if(null!=pd&&pd.size()>0){
				JsonResponse checkResult = templateParamsCheck(pd);
				if(checkResult.isSuccess() == false){
					return checkResult;
				}

				pd.put("uuid", AuthorityUtil.getRequesterUid(getRequest()));
				pd.put("tid", this.get32UUID());
				pd.put("tstatus", "new");
				pd.put("adddate", DateUtil.getTime());
				pd.put("editdate", DateUtil.getTime());
				templateservice.save(pd);
				return ajaxSuccess(Constants.REQUEST_01,Constants.REQUEST_OK);
			}else{
				return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
			}

		}catch (Exception ex){
			logger.info("add template error !!!");
			logger.error(ex);
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}

	/**
	 * 查询列表
	 */
	@RequestMapping(value = "/template",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse productList(){
		try {
			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Program_EDIT);
			if (auth == false)
				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
			
			PageData pd = this.getPageData();
			Page page = new Page();
			pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
			if (pd.getString("keywords") != null && !"".equals(pd.getString("keywords")))
				pd.put("keywords", pd.getString("keywords"));
			page.setPd(pd);
			page.setOffset(pd.getString("offset")==null?0:Integer.valueOf(pd.getString("offset")));
			List<PageData> result = templateservice.list(page);
			return ajaxSuccessPage("template", result, page, Constants.REQUEST_01,Constants.REQUEST_OK);
		}catch (Exception ex){
			logger.error(ex);
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}


	/**
	 * 查询详情
	 */
	@RequestMapping(value = "/template/{id}",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse findTemplateById(@PathVariable("id") String id){
		try {
			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Program_EDIT);
			if (auth == false)
				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
			
			PageData pd = this.getPageData();
			
			pd.put("tid", id);
			pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
			PageData pageData = templateservice.findById(pd);
			return ajaxSuccess(pageData,Constants.REQUEST_01,Constants.REQUEST_OK);
		}catch (Exception ex){
			logger.error(ex);
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}
	
	
	/**
	 * 删除产品
	 */
	@RequestMapping(value = "/template/{id}",method = RequestMethod.DELETE,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse deleteById(@PathVariable("id") String id){
		try {
			
			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Program_EDIT);
			if (auth == false)
				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
			
			if(null==id){
				return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
			}
			
			PageData pd =new PageData();
			pd.put("tid",id);
			pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
			templateservice.delete(pd);
			return ajaxSuccess(Constants.REQUEST_01,Constants.REQUEST_OK);
		}catch (Exception ex){
			logger.error(ex);
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}

	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private JsonResponse templateParamsCheck(PageData params)
	{
		
		if(null == params.getString("setting") || "".equals(params.getString("setting"))){
			return ajaxFailure(Constants.REQUEST_03, "请输入模板内容");
		}
		
		if(null == params.getString("name") || "".equals(params.getString("name"))){
			return ajaxFailure(Constants.REQUEST_03, "请输入模板名称");
		}
		
				
		return ajaxSuccess(Constants.REQUEST_01,Constants.REQUEST_OK);
	}
	
}
