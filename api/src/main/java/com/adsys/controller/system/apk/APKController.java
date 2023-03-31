package com.adsys.controller.system.apk;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.java_websocket.WebSocket;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.adsys.common.enums.permissionEnum;
import com.adsys.controller.base.BaseController;
import com.adsys.entity.system.APK;
import com.adsys.plugin.websocketOnline.OnlineDeviceServer;
import com.adsys.plugin.websocketOnline.OnlineDeviceServerPool;
import com.adsys.plugin.websocketOnline.OnlinePushCommand;
import com.adsys.service.system.apk.APKService;
import com.adsys.service.system.device.impl.DeviceService;
import com.adsys.util.AuthorityUtil;
import com.adsys.util.Constants;
import com.adsys.util.MD5;
import com.adsys.util.PageData;
import com.adsys.util.UuidUtil;
import com.adsys.util.json.JsonResponse;

@Controller
@RequestMapping(value="/update")
public class APKController extends BaseController {
	
	@Resource(name="apkService")
	private APKService apkService;
	
	@Resource(name="deviceService")
	private DeviceService deviceservice;
	
	/**
	 * 查询apk列表
	 */
	@RequestMapping("/getAPKList")
	@ResponseBody
	public JsonResponse getAPKList() {
		boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Setting_UPDATE);
		if (auth == false)
			return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
		
		try {
			List<APK> apkList = apkService.getAPKList();
			return ajaxSuccess(apkList, Constants.REQUEST_01,Constants.REQUEST_OK);
		} catch (Exception e) {
			e.printStackTrace();
			return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_FAILL);
		}
	}
	
	/**
	 * 新增
	 */
	@RequestMapping("/addAPK")
	@ResponseBody
	public JsonResponse saveAPK(@RequestParam("fileAPK") MultipartFile fileAPK ,@RequestParam("version") String version, @RequestParam("readme") String readme,@RequestParam("md5sum") String md5sum) {
		APK apk = new APK();
		try {
			String path = this.getRequest().getSession().getServletContext().getRealPath("apk");
			String name = "adsys";
			String ext = FilenameUtils.getExtension(fileAPK.getOriginalFilename());
			if(!ext.equals("apk")) {
				return ajaxFailure(Constants.REQUEST_03, "请上传正确文件类型");
			}
			 //上传文件名
            String filename = name+version+"."+ext;
            File filepath = new File(path,filename);
			//判断路径是否存在，如果不存在就创建一个
            if (!filepath.getParentFile().exists()) { 
            	filepath.getParentFile().mkdirs();
            }
            if(md5sum!=null&&md5sum!="") {
            	APK apkOld = apkService.findAPKByMd5sum(md5sum);
            	if(apkOld!=null) {
            		apkService.deleteByVersion(apkOld.getVersion());
            		new File(path + File.separator + filename).delete();
            	}
            	fileAPK.transferTo(new File(path + File.separator + filename));
            	
            	/*if(!MD5.md5sum(path + File.separator + filename).equals(md5sum)) {
            		new File(path + File.separator + filename).delete();
    				return ajaxFailure(Constants.REQUEST_03, "文件校验失败" + MD5.md5sum(path + File.separator + filename));
    			}*/
            	apk.setId(UuidUtil.get32UUID());
            	apk.setReadme(readme);
    			apk.setMd5sum(md5sum);
    			apk.setApkpath("apk/" + filename);
    			apk.setVersion(version);
    			apk.setCreatedate(new Date());
    			int rows = apkService.saveAPK(apk);
    			if(rows>0) {
    				return ajaxSuccess(Constants.REQUEST_01,Constants.REQUEST_OK);
    			}
            }
			
			return ajaxFailure(Constants.REQUEST_03, "文件上传失败");
		} catch (Exception e) {
			return ajaxFailure(Constants.REQUEST_03, "文件上传失败");
		}
		
	}
	
	@RequestMapping(value = "/pushUpdate/{id}",method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse pushUpdate(@PathVariable("id") String id){
		try {
			
			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Setting_UPDATE);
			if (auth == false)
				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
			
			if(null==id){
				return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
			}
			
			PageData pd = this.getPageData();
			
			APK apk = apkService.findAPKByMd5sum(id);
        	if(apk==null) {
        		return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
        	}
        	
			pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
			List<PageData> devlist = deviceservice.listAll(pd);
			for(int i = 0; i < devlist.size(); i++)
			{
				PageData dev = devlist.get(i);
				String did = dev.getString("did");
				WebSocket devsocket = OnlineDeviceServerPool.getWebSocketByUser(did);
				if(devsocket != null){
					String downloadurl = OnlineDeviceServer.getServerAddress() + "update/file/" + apk.getMd5sum();
					String data = downloadurl + ";" + apk.getVersion() + ";" + apk.getMd5sum();
					OnlinePushCommand.pushMessage(did, "updateapk", data);
				}
			}
			return ajaxSuccess(Constants.REQUEST_01,Constants.REQUEST_OK);
		}catch (Exception ex){
			logger.info(ex.getMessage());
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

			APK apk = apkService.findAPKByMd5sum(id);

			if (apk != null){
				getRequest().getRequestDispatcher("/" + apk.getApkpath()).forward(getRequest(), getResponse());
			}

		}catch (Exception ex){
			logger.info(ex.getMessage());
		}
		
		try {
			HttpServletResponse rps = getResponse();
			rps.setStatus(404);
			getRequest().getRequestDispatcher("/apk/" + id).forward(getRequest(), rps);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	/**
	 * 删除
	 * @return
	 */
	@RequestMapping(value = "/deleteByVersion/{id}" ,method = RequestMethod.DELETE,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse deleteApk(@PathVariable("id") String id){
		try{
			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Setting_APKUPLOAD);
			if (auth == false)
				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
			
			if(null==id){
				return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
			}
			
			apkService.deleteByVersion(id);
			
			return ajaxSuccess(Constants.REQUEST_01,Constants.REQUEST_OK);

		}catch (Exception ex){
			logger.info("add role error !!!");
			logger.info(ex.getMessage());
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}
}
