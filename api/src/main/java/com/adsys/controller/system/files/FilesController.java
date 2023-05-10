package com.adsys.controller.system.files;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.axis.transport.java.Handler;
import java.net.HttpURLConnection;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.adsys.common.enums.permissionEnum;
import com.adsys.controller.base.BaseController;
import com.adsys.entity.Page;
import com.adsys.service.files.FilesManager;
import com.adsys.util.AuthorityUtil;
import com.adsys.util.Const;
import com.adsys.util.Constants;
import com.adsys.util.DateUtil;
import com.adsys.util.FileUpload;
import com.adsys.util.FileUtil;
import com.adsys.util.MD5;
import com.adsys.util.PageData;
import com.adsys.util.PathUtil;
import com.adsys.util.Tools;
import com.adsys.util.json.JsonResponse;



@Controller
@RequestMapping(value="/files")
public class FilesController extends BaseController {

	@Resource(name="filesService")
	private FilesManager filesService;

	/**列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/files",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse list() throws Exception{
		boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.File_MANAGE);
		if (auth == false)
			return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);

		Page page = new Page();
		PageData pd = this.getPageData();
		pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));

		page.setOffset(pd.getString("offset")==null?0:Integer.valueOf(pd.getString("offset")));
		page.setPd(pd);
		List<PageData>	varList = filesService.list(page);
		return ajaxSuccessPage("file", varList, page, Constants.REQUEST_01,Constants.REQUEST_OK);
	}


	/**新增
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/save" ,method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse save(
			@RequestParam("file") MultipartFile file
			) throws Exception{
		/*
		Map<String,String> map = new HashMap<String,String>();
		String  ffile = DateUtil.getDays(), fileName = "";
		PageData pd = new PageData();

		if (null != file && !file.isEmpty()) {
			String filePath = PathUtil.getClasspath() + Const.FILEPATHIMG + ffile;		//文件上传路径
			fileName = FileUpload.fileUp(file, filePath, this.get32UUID());				//执行上传
		}else{
			System.out.println("上传失败");
		}
		pd.put("fname", fileName);
		pd.put("ftype", FileUtil.getFilesize(fileName));
		pd.put("fsize", FileUtil.getFilesize(Const.FILEPATHIMG  + ffile + "/" + fileName));
		pd.put("fpath", Const.FILEPATHIMG  + ffile + "/" + fileName);
		pd.put("adddate", DateUtil.getTime());
		pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
		pd.put("uploaderuid", AuthorityUtil.getRequesterUid(getRequest()));
		Watermark.setWatemark(PathUtil.getClasspath() + Const.FILEPATHIMG + ffile + "/" + fileName);//加水印
		filesService.save(pd);
*/
		 try {
			if (null != file && !file.isEmpty()) {
				String BOUNDARY = "---------7d4a6d158c9"; // 定义数据分隔线
	            String fileName = file.getOriginalFilename();
	            String contentType = file.getContentType();
	            long sizeInBytes = file.getSize();
	            //上传到远程服务器
	            InputStream uploadedStream = file.getInputStream();

	            URL url = new URL(Const.REMOTE_FILE_SERVER_ADDR + "/file/upload");
	            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            	// 发送POST请求必须设置如下两行
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setUseCaches(false);
                conn.setReadTimeout(50000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("connection", "Keep-Alive");
                conn.setRequestProperty("user-agent",
                        "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
                conn.setRequestProperty("Charsert", "UTF-8");
                conn.setRequestProperty("Content-Type",
                        "multipart/form-data; boundary=" + BOUNDARY);

                OutputStream out = new DataOutputStream(conn.getOutputStream());
                byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();// 定义最后数据分隔线
                StringBuilder sb = new StringBuilder();
                sb.append("--");
                sb.append(BOUNDARY);
                sb.append("\r\n");
                sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + fileName + "\"\r\n");
                sb.append("Content-Type:application/octet-stream\r\n\r\n");

                byte[] data = sb.toString().getBytes();
                out.write(data);
                System.out.println(sb.toString());
                DataInputStream in = new DataInputStream(uploadedStream);
                int bytes = 0;
                byte[] bufferOut = new byte[1024];
                while ((bytes = in.read(bufferOut)) != -1) {
                    out.write(bufferOut, 0, bytes);
                }
                out.write("\r\n".getBytes()); // 多个文件时，二个文件之间加入这个
                in.close();
                out.write(end_data);
                out.flush();
                out.close();
                // 定义BufferedReader输入流来读取URL的响应
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        conn.getInputStream(), "UTF-8"));
                String line = reader.readLine();
                uploadedStream.close();
                if (line.indexOf("result") >= 0 && line.indexOf("OK") >= 0)
                {
                	String urlpath = Const.REMOTE_FILE_SERVER_ADDR + "/"  + fileName + ".torrent";
					PageData pd = new PageData();
					pd.put("fid", MD5.md5(urlpath));
		            pd.put("fname", fileName);
		    		pd.put("ftype", contentType);
		    		pd.put("fsize", Double.valueOf(sizeInBytes)/1000.00);
		    		pd.put("fpath", urlpath);
		    		pd.put("adddate", DateUtil.getTime());
		    		pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
		    		pd.put("uploaderuid", AuthorityUtil.getRequesterUid(getRequest()));
		    		filesService.save(pd);
		    		return ajaxSuccess(pd, Constants.REQUEST_01,Constants.REQUEST_OK);
                }else
                {
                	return ajaxFailure(Constants.REQUEST_404, "上传失败");
                }
			}
		}catch (Exception e) {
	            System.out.println("请求出现异常！" + e);
	            e.printStackTrace();
	    }
		return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
	}

	/**
	 * 查询列表:树形
	 */
	@RequestMapping(value = "/list",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse toList(){
		try {

			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.File_MANAGE);
			if (auth == false)
				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);


			PageData pd = this.getPageData();
			pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
			List<PageData> result = filesService.listAll(pd);
			return ajaxSuccess(result,Constants.REQUEST_01,Constants.REQUEST_OK);
		}catch (Exception ex){
			logger.error(ex);
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}

	/**
	 * 修改文件信息
	 */
	@RequestMapping(value = "/file/{id}",method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse updateFile(@PathVariable("id") String id){
		try {

			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.File_MANAGE);
			if (auth == false)
				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);

			PageData pd =this.getPageData();
			if(null==id){
				return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
			}

			pd.put("did",id);
			pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
			filesService.edit(pd);
			return ajaxSuccess(Constants.REQUEST_01,Constants.REQUEST_OK);
		}catch (Exception ex){
			logger.error(ex);
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/file/{id}",method = RequestMethod.DELETE,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public JsonResponse deleteById(@PathVariable("id") String id){
		try {
			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.File_MANAGE);
			if (auth == false)
				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);

			if(null==id){
				return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
			}
			PageData pd =new PageData();
			pd.put("fid",id);
			pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
			filesService.delete(pd);
			return ajaxSuccess(Constants.REQUEST_01,Constants.REQUEST_OK);
		}catch (Exception ex){
			logger.error(ex);
			return ajaxFailure(Constants.REQUEST_05,Constants.REQUEST_FAILL);
		}
	}


}
