package com.adsys.controller.adEditor.resource;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.adsys.common.enums.permissionEnum;
import com.adsys.controller.base.BaseController;
import com.adsys.service.adEditor.resource.impl.AdResourceService;
import com.adsys.service.system.role.impl.RoleService;
import com.adsys.util.AuthorityUtil;
import com.adsys.util.Constants;
import com.adsys.util.FileUtil;
import com.adsys.util.MD5;
import com.adsys.util.PageData;
import com.adsys.util.json.JsonResponse;

import net.sf.json.JSONObject;

/** 
 * 说明：
 * 创建时间：2018年4月18日
 */
@Controller 
@RequestMapping(value="/adEditor/resources")
public class AdResourcesController extends BaseController {
	
	@Resource(name="roleService")
	private RoleService roleservice;
	
	@Resource(name="adResourceService")
	private AdResourceService adResourceService;

	
	/**
	 * 新增
	 * @return
	 */
	@RequestMapping(value = "/forlder/add" ,method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse add(){
		try{
			
			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Program_EDIT);
			if (auth == false)
				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
			
			String pid = this.getRequest().getParameter("pid");
			String id = this.getRequest().getParameter("id");
			String resName = this.getRequest().getParameter("resName");
			String uuid = AuthorityUtil.getRequesterUUID(getRequest());
			PageData ppd = new PageData();
			ppd.put("uuid", uuid);
			ppd.put("id", pid);
			PageData presource = adResourceService.findById(ppd);
			if ((int)presource.get("resType") == 1){
				return ajaxFailure(Constants.REQUEST_05,"新建失败，上级目录不是文件夹");
			}
			if(StringUtils.isBlank(id)) {//add
				PageData resource = new PageData();
				resource.put("uuid", uuid);
				resource.put("id", this.get32UUID());
				resource.put("pid", pid);
				resource.put("resName", resName);
				resource.put("resType", 0);
				resource.put("resPath", presource.get("resPath")+"/"+resName);
				adResourceService.save(resource);
			}else {//update
				PageData pd = new PageData();
				pd.put("id", id);
				pd.put("uuid", uuid);
				PageData resource = adResourceService.findById(pd);
				resource.put("resName", resName);
				resource.put("resPath", presource.get("resPath")+"/"+resName);
				adResourceService.edit(resource);
			}
			
			return ajaxSuccess(Constants.REQUEST_01,Constants.REQUEST_OK);

		}catch (Exception ex){
			logger.info("add role error !!!");
			logger.error(ex);
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}
	
	/**
	 * 删除文件夹
	 * @return
	 */
	@RequestMapping(value = "/forlder/delete" ,method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse deleteForlder(){
		try{
			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Program_EDIT);
			if (auth == false)
				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
			
			PageData pd = new PageData();
			String uuid = AuthorityUtil.getRequesterUUID(getRequest());
			pd.put("uuid", uuid);
			pd.put("id", this.getRequest().getParameter("id"));
			adResourceService.delete(pd);
			return ajaxSuccess(Constants.REQUEST_01,Constants.REQUEST_OK);

		}catch (Exception ex){
			logger.info("add role error !!!");
			logger.error(ex);
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}
	
	/**
	 * 上传素材
	 * @return
	 */
	@RequestMapping(value = "/save" ,method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public void upload(@RequestParam("file") MultipartFile file ,@RequestParam("pid") String pid){
		this.getResponse().setContentType("text/html; charset=utf-8");
		PrintWriter out = null;
		try{
			
			out = this.getResponse().getWriter();
			String resName = file.getOriginalFilename();//picPath.substring(picPath.lastIndexOf("."));
			int len = resName.length();
			String indName = resName.replaceAll("\\(|\\)", "");//替换英文括号
			int len2 = indName.length();
			if(len == len2){
				indName = resName.replaceAll("（","").replaceAll("）","");//替换中文括号
			}
			resName = indName;
			
			PageData pd = new PageData();
			String uuid = AuthorityUtil.getRequesterUUID(getRequest());
			System.out.println("pid=" + pid + " uuid=" + uuid);
			pd.put("uuid", uuid);
			pd.put("id", pid);
			PageData presource = adResourceService.findById(pd);
			String rootPath = RequestContextUtils.getWebApplicationContext(this.getRequest()).getServletContext().getRealPath("/");
			File dir = new File(rootPath+"/"+presource.get("resPath").toString());
			if(!dir.exists()) {
				dir.mkdirs();
			}
			
			File newFile = new File(rootPath+"/"+presource.get("resPath")+"/"+resName);
			if(newFile.exists()) {
				JsonResponse json = ajaxFailure(Constants.REQUEST_05,"文件已存在");
				JSONObject obj = JSONObject.fromObject(json);
				out.write(JSONObject.fromObject(json).toString());
				out.flush();
				out.close();
				return;
			}
			file.transferTo(newFile);
			
			PageData resource = new PageData();
			resource.put("uuid", uuid);
			resource.put("id", MD5.md5(presource.get("resPath")+"/"+resName));
			resource.put("pid", pid);
			resource.put("resName", resName);
			resource.put("resType", 1);//文件
			resource.put("resPath", presource.get("resPath")+"/"+resName);
			resource.put("fileType", FileUtil.fileType(resName));
			adResourceService.save(resource);
			
			
		}catch (Exception ex){
			logger.info("add role error !!!");
			logger.error(ex);
			JsonResponse json = ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
			JSONObject obj = JSONObject.fromObject(json);
			out.write(JSONObject.fromObject(json).toString());
			out.flush();
			out.close();
			return;
		}
//		try {
//			this.getResponse().getWriter().write(ajaxSuccess(Constants.REQUEST_01,Constants.REQUEST_OK).toString());
//			this.getResponse().getWriter().flush();
//			this.getResponse().getWriter().close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		JsonResponse json = ajaxSuccess(Constants.REQUEST_01,Constants.REQUEST_OK);
		JSONObject obj = JSONObject.fromObject(json);
		out.write(JSONObject.fromObject(json).toString());
		out.flush();
		out.close();
		return;
	}
	
	/**
	 * 查询列表:树形
	 */
	@RequestMapping(value = "/list",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse toList(){
		try {
			
			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Program_EDIT);
			if (auth == false)
				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
			
			String testpath = RequestContextUtils.getWebApplicationContext(this.getRequest()).getServletContext().getRealPath("/");
			System.out.println("======================"+testpath+"======================");
			PageData pd = this.getPageData();
			pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
			List<PageData> result = adResourceService.listAll(pd);
			return ajaxSuccess(result,Constants.REQUEST_01,Constants.REQUEST_OK);
		}catch (Exception ex){
			logger.error(ex);
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}

	/**
	 * 下载文件
	 */
	@RequestMapping(value = "/file/{id}",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String download(@PathVariable("id") String id){
		try {
			
//			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Sys_Role_WRITE);
//			if (auth == false)
//				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
			
			PageData pd =this.getPageData();
			if(null==id){
				id="none";
			}

			//pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
			pd.put("id",id);
			PageData file = adResourceService.findById(pd);
			if (file != null){
				getRequest().getRequestDispatcher("/" + file.getString("resPath")).forward(getRequest(), getResponse());
				return null;
			}

		}catch (Exception ex){
			logger.error(ex);
		}
		
		try {
			HttpServletResponse rps = getResponse();
			rps.setStatus(404);
			getRequest().getRequestDispatcher("/upload/" + id).forward(getRequest(), rps);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
}
