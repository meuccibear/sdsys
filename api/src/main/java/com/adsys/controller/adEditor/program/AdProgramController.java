package com.adsys.controller.adEditor.program;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.adsys.common.enums.permissionEnum;
import com.adsys.controller.adEditor.util.CompressedFileUtil;
import com.adsys.controller.adEditor.util.FileUtils;
import com.adsys.controller.base.BaseController;
import com.adsys.entity.Page;
import com.adsys.entity.resource.AdProgramItem;
import com.adsys.service.adEditor.program.impl.AdProgramItemService;
import com.adsys.service.adEditor.program.impl.AdProgramService;
import com.adsys.service.system.role.impl.RoleService;
import com.adsys.service.system.template.impl.TemplateService;
import com.adsys.util.AuthorityUtil;
import com.adsys.util.Constants;
import com.adsys.util.FileUtil;
import com.adsys.util.MD5;
import com.adsys.util.PageData;
import com.adsys.util.UuidUtil;
import com.adsys.util.json.JsonResponse;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/** 
 * 说明：
 * 创建时间：2018年4月18日
 */
@Controller
@RequestMapping(value="/adEditor/programs")
public class AdProgramController extends BaseController {
	
	@Resource(name="roleService")
	private RoleService roleservice;
	
	@Resource(name="adProgramService")
	private AdProgramService adProgramService;
	
	@Resource(name="adProgramItemService")
	private AdProgramItemService adProgramItemService;

	@Resource(name="templateService")
	private TemplateService templateservice;
	
	/**
	 * 新增节目
	 * @return
	 */
	@RequestMapping(value = "/add" ,method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse add(){
		try{
			
			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Program_EDIT);
			if (auth == false)
				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
			
			PageData pds =this.getPageData();
			
			JsonResponse checkResult = programParamsCheck(pds);
			if(checkResult.isSuccess() == false){
				return checkResult;
			}
			
			String uuid = AuthorityUtil.getRequesterUUID(getRequest());
			String id = pds.getString("id");
			String name = pds.getString("name");
			String wd = pds.getString("wd");
			String hd = pds.getString("hd");
			String dp = wd + "x" + hd;
			String tid = pds.getString("tid");
			String ischeck = pds.getString("ischeck");
			
			PageData pcheck = new PageData();
			pcheck.put("uuid", uuid);
			pcheck.put("name", name);
			PageData gprogram = adProgramService.findByName(pcheck);
			if (gprogram != null)
				return ajaxFailure(Constants.REQUEST_05, "节目名重复，请重命名");
			
			if(StringUtils.isBlank(id)) {//add
				String nid = this.get32UUID();
				if(!ischeck.equals("1")) {
					PageData program = new PageData();
					program.put("uuid", uuid);
					program.put("id", nid);
					program.put("name", name);
					program.put("ptype", 0);
					program.put("dp", dp);
					adProgramService.save(program);
				}else {//通过模板新建
					PageData program = new PageData();
					program.put("uuid", uuid);
					program.put("id", nid);
					program.put("name", name);
					program.put("ptype", 0);
					program.put("dp", dp);
					adProgramService.save(program);
					
					PageData pd = new PageData();
					pd.put("uuid", uuid);
					pd.put("tid", tid);
					List<PageData> items = templateservice.listAllByPid(pd);
					for(PageData item : items) {
						PageData addPd = new PageData();
						addPd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
						addPd.put("id",  UuidUtil.getOrderNo().substring(0,5));
						addPd.put("name", item.getString("name"));
						addPd.put("type", item.getString("type"));
						addPd.put("pid", nid);
						addPd.put("res", "");
						addPd.put("targetpath", "");
						
						if (!"voiceControl".equals(item.getString("type")) && !"program".equals(item.getString("type")) && !"music".equals(item.getString("type")))
						{
							JSONArray settings = JSONObject.parseObject(item.getString("setting")).getJSONArray("Rows") ;
							JSONObject ljson = settings.getJSONObject(2);
							ljson.put("pvalue", (int)(ljson.getFloat("pvalue")*Integer.parseInt(wd)));
							settings.set(2, ljson);
							
							JSONObject tjson = settings.getJSONObject(3);
							tjson.put("pvalue", (int)(tjson.getFloat("pvalue")*Integer.parseInt(hd)));
							settings.set(3, tjson);
							
							JSONObject wjson = settings.getJSONObject(4);
							wjson.put("pvalue", (int)(wjson.getFloat("pvalue")*Integer.parseInt(wd)));
							settings.set(4, wjson);
							
							JSONObject hjson = settings.getJSONObject(5);
							hjson.put("pvalue", (int)(hjson.getFloat("pvalue")*Integer.parseInt(hd)));
							settings.set(5, hjson);
							
							JSONObject newobj = new JSONObject();
							newobj.put("Rows", settings);
							addPd.put("setting", newobj.toJSONString());
						}
						else
							addPd.put("setting", item.getString("setting"));
						
						adProgramItemService.save(addPd);
					}
					
				}
				JSONObject retObj = new JSONObject();
				retObj.put("nid", nid);
				retObj.put("width", wd);
				retObj.put("height", hd);
				retObj.put("name", name);
				return ajaxSuccess(retObj,Constants.REQUEST_01,Constants.REQUEST_OK);
			}else {//update
				PageData ppd = new PageData();
				ppd.put("uuid", uuid);
				ppd.put("id", id);
				PageData pprogram = adProgramService.findById(ppd);
				pprogram.put("resName", name);
				pprogram.put("dp", dp);
				adProgramService.edit(pprogram);
			}
			
			return ajaxSuccess(Constants.REQUEST_01,Constants.REQUEST_OK);

		}catch (Exception ex){
			logger.info("add role error !!!");
			logger.error(ex);
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}
	
	/**
	 * 复制节目
	 * @return
	 */
	@RequestMapping(value = "/copy" ,method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse copy(){
		try{
			
			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Program_EDIT);
			if (auth == false)
				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
			
			PageData pds =this.getPageData();
			
			String uuid = AuthorityUtil.getRequesterUUID(getRequest());
			String id = pds.getString("id");
			String name = pds.getString("name");
			
			PageData pcheck = new PageData();
			pcheck.put("uuid", uuid);
			pcheck.put("name", name);
			PageData gprogram = adProgramService.findByName(pcheck);
			if (gprogram != null)
				return ajaxFailure(Constants.REQUEST_05, "节目名重复，请重命名");
			
			pcheck.clear();
			pcheck.put("uuid", uuid);
			pcheck.put("id", id);
			
			gprogram = adProgramService.findById(pcheck);
			if (gprogram != null){
				String nid = this.get32UUID();
				PageData program = new PageData();
				program.put("uuid", uuid);
				program.put("id", nid);
				program.put("name", name);
				program.put("ptype", 0);
				program.put("dp", gprogram.getString("dp"));
				adProgramService.save(program);
				
				pcheck.clear();
				pcheck.put("uuid", uuid);
				pcheck.put("pid", id);
				List<PageData> pitems = adProgramItemService.findByPid(pcheck);
				for(PageData item : pitems) {
					item.put("pid", nid);
					item.put("id", UuidUtil.getOrderNo().substring(0,5));
					adProgramItemService.save(item);
				}
			}
			else
				return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
			
			return ajaxSuccess(Constants.REQUEST_01,Constants.REQUEST_OK);

		}catch (Exception ex){
			logger.info("copy program error !!!");
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
			PageData file = adProgramService.findById(pd);
			if (file != null){
				getRequest().getRequestDispatcher("/" + file.getString("zippath")).forward(getRequest(), getResponse());
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
	
	private String objectTypeChange(String name){
		if(name.equals("pic")) {
			return "1";
		}else if(name.equals("video")) {
			return "2";
		}else if(name.equals("music")) {
			return "5";
		}else if(name.equals("sub")) {
			return "4";
		}else if(name.equals("calendar")) {
			return "6";
		}else if(name.equals("clock")) {
			return "7";
		}else if(name.equals("weather")) {
			return "8";
		}else if(name.equals("web")) {
			return "9";
		}else if(name.equals("txt")) {
			return "3";
		}else if(name.equals("voiceControl")) {
			return "10";
		}
		return "no";
	}
	/**
	 * 生成节目包
	 * 生成下述文件，uuid/id.zip
	 * uuid/id/con/id/contant.xml
	 * uuid/id/con/id/1/model-id.xml
	 * uuid/id/con/id/1/Object.xml
	 * @return
	 */
//	@RequestMapping(value = "/exportxml" ,method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
//	@ResponseBody
	private JsonResponse exportxml(String uuid, String id){
		PrintWriter out = null;
		try{
			
//			this.getResponse().setContentType("text/xml");
			Map<String, Integer> objectNum = new HashMap<String, Integer>();
			//String id = this.getRequest().getParameter("id");
			boolean hasVideo = false;
			String runtime = "60";
			String bgpicpath = "";
			String userId = uuid;
			String zipDir = "zipDir";
			String rootPath = RequestContextUtils.getWebApplicationContext(this.getRequest()).getServletContext().getRealPath("/");
//			rootPath = "d:\\test2\\ad\\";
			File rootFile = new File(rootPath);
			rootFile.setReadable(true);
			rootFile.setWritable(true);
			rootFile.setExecutable(true);
			if(!rootFile.exists()) {
				rootFile.mkdirs();
			}
			String filePath = rootPath+zipDir+File.separator+userId+File.separator+id+File.separator;
			FileUtils.delAllFile(filePath);//删除旧文件
			this.handlerDir(new String[] {rootPath,zipDir,userId,id,"cont",id,"page","1"});
			
			//==============生成contant.xml==============================
			PageData proPd = new PageData();
			proPd.put("uuid", uuid);
			proPd.put("id", id);
			PageData programPd = adProgramService.findById(proPd);
			String programName = programPd.getString("name");
			
			PageData pd = new PageData();
			pd.put("uuid", uuid);
			pd.put("pid", id);
			List<PageData> items = adProgramItemService.findByPid(pd);
			for(PageData item : items) {
				if ("video".equals(item.getString("type")))
					hasVideo = true;
				else if ("program".equals(item.getString("type"))){
					JSONArray p_setting = JSONObject.parseObject(item.getString("setting")).getJSONArray("Rows") ;
					runtime = p_setting.getJSONObject(2).getString("pvalue");//ptime
					String bgpic = p_setting.getJSONObject(3).getString("pvalue");
					if (bgpic.indexOf("upload") >= 0){
						String fileid = MD5.md5(bgpic.substring(bgpic.indexOf("upload")));
						bgpicpath = "sdcard/sour/" + fileid + bgpic.substring(bgpic.lastIndexOf("."));
					}
				}
					
			}
			Document contantXmlDoc = DocumentHelper.createDocument();
			Element contantRoot = contantXmlDoc.addElement("contant");
			contantRoot.addAttribute("type", "0").addAttribute("name", programName);
			Element pageEle = contantRoot.addElement("page");
			pageEle.addAttribute("contant", id).addAttribute("id", "1").addAttribute("name", programName).addAttribute("type", "0").addAttribute("daymode", "").addAttribute("starttime", "").addAttribute("endtime", "").addAttribute("engross", "0").addAttribute("seqid", "0");
			String modelId = UuidUtil.getOrderNo().substring(0, 5);
			Element modelEle = pageEle.addElement("model");
			modelEle.addAttribute("id", modelId).addAttribute("name", programName);
			if (hasVideo)
				modelEle.addAttribute("Condition", "video").addAttribute("times", "1");
			else
				modelEle.addAttribute("Condition", "timestamp").addAttribute("times", runtime);
			modelEle.addAttribute("targetobject", "Video1").addAttribute("Effect", "slide").addAttribute("path", FileUtils.swrapFilePath(new String[] {"cont",id,"page","1"}));
			File contantXmlFile = new File(filePath+"cont"+File.separator+id+File.separator+"contant.xml");
			//File pageDir = new File(filePath+"cont"+File.separator+id+File.separator+"page");
			//if(!pageDir.exists()) {
			//	pageDir.mkdirs();
			//}
			if(!contantXmlFile.exists()) {
				contantXmlFile.createNewFile();	
			}
			FileUtils.writeXml(contantXmlFile, contantXmlDoc);

			//=================生成con/id/1/model-id.xml=======================================
			String dp = programPd.getString("dp");
			
			Document modelXmlDoc = DocumentHelper.createDocument();
			Element modelRoot = modelXmlDoc.addElement("Template");
			modelRoot.addAttribute("name", "containerbox").addAttribute("width", dp.split("x")[0]).addAttribute("height", dp.split("x")[1]);
			modelRoot.addAttribute("border", "0px solid #ff0000");
			modelRoot.addAttribute("img", bgpicpath);
			JSONArray settings = null;
			
			String objType = "";
			for(PageData item : items) {
				if ("program".equals(item.getString("type")))
					continue;
				
				Document objectXmlDoc = DocumentHelper.createDocument();
				Element objectRoot = objectXmlDoc.addElement("resourse");
				objType = item.getString("type");
				String objectName = "";
				String objectEffect = "1";
				String objectRandmon = "false,1,0,0";
				if (objectNum.get(objType) == null)
				{
					objectName = objType + "1";
					objectNum.put(objType, 2);
				}
				else
				{
					Integer num = objectNum.get(objType);
					objectName = objType + num;
					objectNum.put(objType, num+1);
				}
				if (objType.equals("voiceControl")){
					if (item.getString("setting").equals(""))
						return ajaxFailure(Constants.REQUEST_05,"至少需要添加一项声控事件");
					settings = JSONObject.parseArray(item.getString("setting"));
				}
				else
					settings = JSONObject.parseObject(item.getString("setting")).getJSONArray("Rows") ;
				
				if(objType.equals("pic")) {
					objectEffect = "50";
					int changetime = Integer.parseInt(settings.getJSONObject(6).getString("pvalue"));//图片切换间隔
					if (changetime <= 0 || changetime >= 3600)
						changetime = 10;
					objectRandmon = "false," + changetime;
				}else if(objType.equals("video")) {
					objectEffect = settings.getJSONObject(6).getString("pvalue");//volume
					objectRandmon = "false";
				}else if(objType.equals("music")) {
					objectEffect = settings.getJSONObject(6).getString("pvalue");//volume
				}else if(objType.equals("sub")) {
					objectEffect = settings.getJSONObject(7).getString("pvalue") + ","+settings.getJSONObject(8).getString("pvalue")+",transparent,defaultfont,255";
					objectRandmon = "left,1";
				}else if(objType.equals("txt")) {
					objectEffect = settings.getJSONObject(7).getString("pvalue") + ","+settings.getJSONObject(8).getString("pvalue")+",transparent,defaultfont,255";
				}else if(objType.equals("calendar")) {
					
				}else if(objType.equals("clock")) {
					objectEffect = "#C4B16F,50,transparent,defaultfont,255";
					objectRandmon = "Digital7";
				}else if(objType.equals("weather")) {
					
				}else if(objType.equals("web")) {
					
				}
					
				
				Element objEle = modelRoot.addElement("Object");
				objEle.addAttribute("name", objectName).addAttribute("type", objectTypeChange(item.getString("type")));
				if (objType.equals("voiceControl")){
					objEle.addAttribute("top", "0");
					objEle.addAttribute("left", "0");
					objEle.addAttribute("width", "100");
					objEle.addAttribute("height", "100");
				}
				else
				{
					objEle.addAttribute("top", settings.getJSONObject(3).getString("pvalue"));
					objEle.addAttribute("left", settings.getJSONObject(2).getString("pvalue"));
					objEle.addAttribute("width", settings.getJSONObject(4).getString("pvalue"));
					objEle.addAttribute("height", settings.getJSONObject(5).getString("pvalue"));
				}
				objEle.addAttribute("border", "0px solid #ff0000").addAttribute("zindex", "10");
				objEle.addAttribute("effect", objectEffect);
				 		
				objEle.addAttribute("random", objectRandmon);
				
				objEle.addAttribute("res", "1").addAttribute("onclick", "0");
				
				if (objType.equals("weather"))
					objEle.addAttribute("memo", settings.getJSONObject(1).getString("pvalue"));
				else
					objEle.addAttribute("memo", "");
					
				if(objType.equals("pic") || objType.equals("video") || objType.equals("music") || objType.equals("sub") || objType.equals("web") || objType.equals("voiceControl") || objType.equals("txt")) {
					if(objType.equals("web") || objType.equals("sub") || objType.equals("txt")) {
						Element fileEle = objectRoot.addElement("file");
						fileEle.addAttribute("id", item.getString("id")).addAttribute("type", objectTypeChange(item.getString("type")));
						if (objType.endsWith("web")){
							fileEle.addAttribute("name", settings.getJSONObject(1).getString("pvalue"));
						}
						else if (objType.endsWith("sub")){
							fileEle.addAttribute("name", settings.getJSONObject(6).getString("pvalue"));
						}
						else if (objType.endsWith("txt")){
							fileEle.addAttribute("name", settings.getJSONObject(6).getString("pvalue"));
						}
						fileEle.addAttribute("daymode", "11111110").addAttribute("starttime", "00:00:00;23:59:59").addAttribute("link", "").addAttribute("attribute", "5");
					}else if(objType.equals("voiceControl")){
						for(int k = 0; k < settings.size(); k++)
				        {
							String []voicectr = settings.getJSONObject(k).getString("event").split(",");
							if (voicectr.length == 3){
								Element fileEle = objectRoot.addElement("file");
								fileEle.addAttribute("id", item.getString("id") + k);
								fileEle.addAttribute("type", voicectr[0]);
								fileEle.addAttribute("name", settings.getJSONObject(k).getString("keyname")+","+voicectr[1]);
								fileEle.addAttribute("daymode", "11111110").addAttribute("starttime", "00:00:00;23:59:59").addAttribute("link", "").addAttribute("attribute", "5");
							}
				        }
					}else if(StringUtils.isNotBlank(item.getString("res"))) {
						String []targetres = item.getString("targetpath").split(";");
						String []res = item.getString("res").split(";");           
				        for(int r = 0; r < res.length; r++)
				        {
							Element fileEle = objectRoot.addElement("file");
							fileEle.addAttribute("id", item.getString("id") + r).addAttribute("type", objectTypeChange(item.getString("type")));
							fileEle.addAttribute("name", targetres[r]);
							fileEle.addAttribute("daymode", "11111110").addAttribute("starttime", "00:00:00;23:59:59").addAttribute("link", "").addAttribute("attribute", "5");
				        }
					}

					//======================cont/id/1/Object.xml=====================================================
					File objectXmlFile = new File(filePath+"cont"+File.separator+id+File.separator+"page"+File.separator+"1"+File.separator+objectName+".xml");
					if(!objectXmlFile.exists()) {
						objectXmlFile.createNewFile();	
					}
					FileUtils.writeXml(objectXmlFile, objectXmlDoc);
				}
			}
			File modelXmlFile = new File(FileUtils.swrapFilePath(new String[] {filePath,"cont",id,"page","1",modelId+".xml"}));
//			File modelXmlFileDir = new File(filePath+"con"+File.separator+id+File.separator+"1"+File.separator);
//			if(!modelXmlFileDir.exists()) {
//				modelXmlFileDir.mkdirs();
//			}
			if(!modelXmlFile.exists()) {
				modelXmlFile.createNewFile();	
			}
			
			FileUtils.writeXml(modelXmlFile, modelXmlDoc);
			
			try{
				CompressedFileUtil.compressedFile(rootPath+zipDir+File.separator+userId+File.separator+id+File.separator, rootPath+zipDir+File.separator+userId, id);
				PageData param = new PageData();
				param.put("uuid", uuid);
				param.put("id", id);
				param.put("zippath", zipDir+File.separator+userId+File.separator+id+".zip");
				adProgramService.updateZipPath(param);
			}catch (Exception ex){
				logger.info("Compressed File fail!!!");
				logger.error(ex);
				return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
			}
			return ajaxSuccess(Constants.REQUEST_01,Constants.REQUEST_OK);
		}catch (Exception ex){
			logger.info("add role error !!!");
			ex.printStackTrace();
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}finally {
//			if(writer != null) {
//				try {
//					writer.flush();
//					writer.close();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
		}
	}
	
	private void handlerDir(String[] ds) {
		String path = ds[0];
		for(int i=1;i<ds.length;i++) {
			path += File.separator+ds[i];
			File file = new File(path);
			if(!file.exists()) {
				file.mkdir();
			}
		}
	}
	
	
	
	/**
	 * 新增
	 * @return
	 */
	@RequestMapping(value = "/additem" ,method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse additem(@RequestBody AdProgramItem[] ps){
		try{
			
			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Program_EDIT);
			if (auth == false)
				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
			
			String pid = ps[0].getPid();
			PageData delPd = new PageData();
			delPd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
			delPd.put("pid", pid);
			adProgramItemService.deleteByPid(delPd);
			for(int i=0;i<ps.length;i++) {
				String respath = "";
				if (ps[i].getType().endsWith("pic") || ps[i].getType().endsWith("video") || ps[i].getType().endsWith("music"))
				{
					respath = ps[i].getRes();
					if (respath == null || "".equals(respath)){
						String msg = "图片控件的内容不能为空";
						if (ps[i].getType().endsWith("video"))
							msg = "视频控件的内容不能为空";
						else if (ps[i].getType().endsWith("music"))
							msg = "音频控件的内容不能为空";
						return ajaxFailure(Constants.REQUEST_05,msg);
					}
				}
				else
				{
					respath = "";
				}
				PageData addPd = new PageData();
				addPd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
				addPd.put("id",  UuidUtil.getOrderNo().substring(0,5));
				addPd.put("name", ps[i].getName());
				addPd.put("type", ps[i].getType());
				addPd.put("setting", ps[i].getSetting());
				addPd.put("pid", ps[i].getPid());
				addPd.put("res", respath);
				if (!"".equals(respath)){
					String targetres = "";
					String []res = respath.split(";");           
			        for(int r = 0; r < res.length; r++)
			        {
			        	String fileid = MD5.md5(res[r].substring(res[r].indexOf("upload")));
			        	targetres += "sdcard/sour/" + fileid + res[r].substring(res[r].lastIndexOf("."));
			        	if (r+1 < res.length){
			        		targetres += ";";
			        	}
			        }
					addPd.put("targetpath", targetres);
				}else if (ps[i].getType().equals("program")){
					JSONArray p_setting = JSONObject.parseObject(ps[i].getSetting()).getJSONArray("Rows") ;
					String bgpic = p_setting.getJSONObject(3).getString("pvalue");//ppic
					if (bgpic.indexOf("upload") >= 0){
						addPd.put("res", bgpic);
						String fileid = MD5.md5(bgpic.substring(bgpic.indexOf("upload")));
			        	String targetpath = "sdcard/sour/" + fileid + bgpic.substring(bgpic.lastIndexOf("."));
			        	addPd.put("targetpath", targetpath);
					}
				}else
					addPd.put("targetpath", "");
				
				adProgramItemService.save(addPd);
			}
			
			return exportxml(AuthorityUtil.getRequesterUUID(getRequest()), pid);
			//return ajaxSuccess(Constants.REQUEST_01,Constants.REQUEST_OK);

		}catch (Exception ex){
			logger.info("additem error !!!");
			ex.printStackTrace();
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}
	
	/**
	 * 删除
	 * @return
	 */
	@RequestMapping(value = "/delete" ,method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse deleteProgram(){
		try{
			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Program_EDIT);
			if (auth == false)
				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
			
			PageData pd = new PageData();
			pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
			pd.put("pid", this.getRequest().getParameter("id"));
			adProgramItemService.deleteByPid(pd);	
			pd.put("id", this.getRequest().getParameter("id"));
			adProgramService.delete(pd);
			
			return ajaxSuccess(Constants.REQUEST_01,Constants.REQUEST_OK);

		}catch (Exception ex){
			logger.info("add role error !!!");
			logger.error(ex);
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}
	
	/**
	 * 查询节目明细列表
	 */
	@RequestMapping(value = "/items",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse toItems(){
		try {
			
			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Program_EDIT);
			if (auth == false)
				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
			
			
			PageData pd = this.getPageData();
			pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
			pd.put("pid", this.getRequest().getParameter("pid"));
			List<PageData> result = adProgramItemService.findByPid(pd);
			return ajaxSuccess(result,Constants.REQUEST_01,Constants.REQUEST_OK);
		}catch (Exception ex){
			logger.error(ex);
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}
	
	
	/**
	 * 查询列表:树形
	 */
	@RequestMapping(value = "/listTemplate",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse toTemplateList(){
		try {
			
			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Program_EDIT);
			if (auth == false)
				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
			
			PageData pd = this.getPageData();
			pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
			List<PageData> templates = templateservice.listAll(pd);
			return ajaxSuccess(templates,Constants.REQUEST_01,Constants.REQUEST_OK);
		}catch (Exception ex){
			logger.error(ex);
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
			
			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Program_EDIT);
			if (auth == false)
				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
			
			List<PageData> result = new ArrayList<>();
			PageData root = new PageData();
			root.put("id", "0");
			root.put("pId", -1);
			root.put("name", "所有节目");
			root.put("isParent", true);
			result.add(root);
			
			PageData pd = this.getPageData();
			pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
			List<PageData> programs = adProgramService.listAll(pd);
			for(PageData p : programs) {
				PageData node = new PageData();
				node.put("id", p.get("id"));
				node.put("pId", "0");
				node.put("name", p.get("name")+"("+p.getString("dp")+")");
				node.put("isParent", true);
				node.put("dpw", p.getString("dp").split("x")[0]);
				node.put("dph", p.getString("dp").split("x")[1]);
				result.add(node);
				/*
				if(p.containsKey("pics") && !p.get("pics").toString().equals("0")) {
					PageData node1 = new PageData();
					node1.put("id", p.get("id")+"_pic");
					node1.put("pId", p.get("id"));
					node1.put("name", "图片");
					node1.put("isParent", true);
//					node1.put("iconClose", "../../img/editor/fun/pic.png");
					result.add(node1);
				}
				if(p.containsKey("videos") && !p.get("videos").toString().equals("0")) {
					PageData node1 = new PageData();
					node1.put("id", p.get("id")+"_video");
					node1.put("pId", p.get("id"));
					node1.put("name", "视频");
					node1.put("isParent", true);
//					node1.put("iconOpen", "../../img/editor/fun/video.png");
					result.add(node1);
				}
				if(p.containsKey("musics") && !p.get("musics").toString().equals("0")) {
					PageData node1 = new PageData();
					node1.put("id", p.get("id")+"_music");
					node1.put("pId", p.get("id"));
					node1.put("name", "音频");
					node1.put("isParent", true);
					result.add(node1);
				}
				if(p.containsKey("subs") && !p.get("subs").toString().equals("0")) {
					PageData node1 = new PageData();
					node1.put("id", p.get("id")+"_sub");
					node1.put("pId", p.get("id"));
					node1.put("name", "字幕");
					node1.put("isParent", true);
					result.add(node1);
				}
				if(p.containsKey("calendars") && !p.get("calendars").toString().equals("0")) {
					PageData node1 = new PageData();
					node1.put("id", p.get("id")+"_calendar");
					node1.put("pId", p.get("id"));
					node1.put("name", "日期");
					node1.put("isParent", true);
					result.add(node1);
				}
				if(p.containsKey("clocks") && !p.get("clocks").toString().equals("0")) {
					PageData node1 = new PageData();
					node1.put("id", p.get("id")+"_clock");
					node1.put("pId", p.get("id"));
					node1.put("name", "时钟");
					node1.put("isParent", true);
					result.add(node1);
				}
				if(p.containsKey("weathers") && !p.get("weathers").toString().equals("0")) {
					PageData node1 = new PageData();
					node1.put("id", p.get("id")+"_weather");
					node1.put("pId", p.get("id"));
					node1.put("name", "天气");
					node1.put("isParent", true);
					result.add(node1);
				}
				if(p.containsKey("webs") && !p.get("webs").toString().equals("0")) {
					PageData node1 = new PageData();
					node1.put("id", p.get("id")+"_web");
					node1.put("pId", p.get("id"));
					node1.put("name", "网页");
					node1.put("isParent", true);
					result.add(node1);
				}
				if(p.containsKey("buttons") && !p.get("buttons").toString().equals("0")) {
					PageData node1 = new PageData();
					node1.put("id", p.get("id")+"_button");
					node1.put("pId", p.get("id"));
					node1.put("name", "按钮");
					node1.put("isParent", true);
					result.add(node1);
				}
				if(p.containsKey("voiceControls") && !p.get("voiceControls").toString().equals("0")) {
					PageData node1 = new PageData();
					node1.put("id", p.get("id")+"_voiceControl");
					node1.put("pId", p.get("id"));
					node1.put("name", "声控");
					node1.put("isParent", true);
					result.add(node1);
				}*/
			}
			List<PageData> items = adProgramItemService.listAll(pd);
			String res = null;
			for(PageData item : items) {
				PageData node1 = new PageData();
				String nid = item.get("id").toString();
				node1.put("id", nid);
				node1.put("pId", item.get("pid").toString());
				node1.put("name", item.getString("name"));
				node1.put("isComp", 1);
				boolean isParent = false;
				//处理资源节点，当前只处理图片和视频组件
				res = item.getString("res").toString();
				if(StringUtils.isNotBlank(res)) {
					isParent = true;
					String[] rs = res.split(";");
					for(int i=0;i<rs.length;i++) {
						PageData resNode = new PageData();
						resNode.put("id", this.get32UUID());
						resNode.put("pId", nid);
						resNode.put("name", rs[i].substring(rs[i].lastIndexOf("/")+1));
						resNode.put("res", rs[i]);
						resNode.put("isParent", false);
						resNode.put("isContent", 1);
						result.add(resNode);
					}
				}
				node1.put("isParent", isParent);
				result.add(node1);
			}
			return ajaxSuccess(result,Constants.REQUEST_01,Constants.REQUEST_OK);
		}catch (Exception ex){
			logger.error(ex);
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
			
			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Program_EDIT);
			if (auth == false)
				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
			
			PageData pd = this.getPageData();
			pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
			List<PageData> result = adProgramService.listProgram(pd);
			return ajaxSuccess(result, Constants.REQUEST_01,Constants.REQUEST_OK);
		}catch (Exception ex){
			logger.error(ex);
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}
	


/**
	 * 查询列表
	 */
	@RequestMapping(value = "/pagelist",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
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
			List<PageData> result = adProgramService.pageList(page);
			return ajaxSuccessPage("program", result, page, Constants.REQUEST_01,Constants.REQUEST_OK);
		}catch (Exception ex){
			logger.error(ex);
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}
	

	
	/**
	 * 保存为模板 kk
	 */
	@RequestMapping(value = "/tempetes/add/{id}",method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse toTemplete(@PathVariable("id") String id){
		try {
			String name = this.getRequest().getParameter("name");
			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Program_EDIT);
			if (auth == false)
				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
			
			PageData pd =this.getPageData();
			if(null==id){
				return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
			}

			pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
			pd.put("id",id);
			PageData pr = adProgramService.findById(pd);
			int wp =Integer.parseInt(pr.getString("dp").split("x")[0]);
			int hp =Integer.parseInt(pr.getString("dp").split("x")[1]);
			
			pd.clear();
			pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
			pd.put("pid", id);
			List<PageData> items = adProgramItemService.findByPid(pd);
			String tid = this.get32UUID();
			for(PageData item : items) {
				PageData newTempl = new PageData();
				newTempl.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
				newTempl.put("tid", tid);
				newTempl.put("tempname", name);
				newTempl.put("name", item.getString("name"));
				newTempl.put("type", item.getString("type"));
				newTempl.put("property", "private");
				newTempl.put("previewImg", "none");
				
				if (!"voiceControl".equals(item.getString("type")) && !"program".equals(item.getString("type")) && !"music".equals(item.getString("type")))
				{
					JSONArray settings = JSONObject.parseObject(item.getString("setting")).getJSONArray("Rows") ;
					JSONObject ljson = settings.getJSONObject(2);
					ljson.put("pvalue", (float)ljson.getInteger("pvalue")/wp);
					settings.set(2, ljson);
					
					JSONObject tjson = settings.getJSONObject(3);
					tjson.put("pvalue", (float)tjson.getInteger("pvalue")/hp);
					settings.set(3, tjson);
					
					JSONObject wjson = settings.getJSONObject(4);
					wjson.put("pvalue", (float)wjson.getInteger("pvalue")/wp);
					settings.set(4, wjson);
					
					JSONObject hjson = settings.getJSONObject(5);
					hjson.put("pvalue", (float)hjson.getInteger("pvalue")/hp);
					settings.set(5, hjson);
					
					JSONObject newobj = new JSONObject();
					newobj.put("Rows", settings);
					newTempl.put("setting", newobj.toJSONString());
				}
				else
					newTempl.put("setting", item.getString("setting"));
				templateservice.save(newTempl);
			}
			
			return ajaxSuccess(Constants.REQUEST_01,Constants.REQUEST_OK);
		}catch (Exception ex){
			logger.error(ex);
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}
	
	private JsonResponse programParamsCheck(PageData params)
	{

		if(null == params.getString("name") || "".equals(params.getString("name"))){
			return ajaxFailure(Constants.REQUEST_03, "请输入节目名称");
		}
		
		if(null == params.getString("dp") || "".equals(params.getString("dp"))){
			return ajaxFailure(Constants.REQUEST_03, "请输入节目分辨率");
		}
		
		
		
		return ajaxSuccess(Constants.REQUEST_01,Constants.REQUEST_OK);
	}
}
