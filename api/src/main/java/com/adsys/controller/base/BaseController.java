package com.adsys.controller.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.adsys.entity.Page;
import com.adsys.util.Logger;
import com.adsys.util.PageData;
import com.adsys.util.UuidUtil;
import com.adsys.util.json.JsonResponse;

/**
 * BaseConntroller封装处理
 */
public abstract class BaseController {
	
	protected Logger logger = Logger.getLogger(this.getClass());

	private static final long serialVersionUID = 6357869213649815390L;
	
	/** new PageData对象
	 * @return
	 */
	public PageData getPageData(){
		return new PageData(this.getRequest());
	}
	
	/**得到ModelAndView
	 * @return
	 */
	public ModelAndView getModelAndView(){
		return new ModelAndView();
	}
	
	/**得到request对象
	 * @return
	 */
	public HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		return request;
	}

	/**得到response对象
	 * @return
	 */
	public HttpServletResponse getResponse() {
		HttpServletResponse rep = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
		return rep;
	}
	
	/**得到32位的uuid
	 * @return
	 */
	public String get32UUID(){
		return UuidUtil.get32UUID();
	}
	
	/**得到分页列表的信息
	 * @return
	 */
	public Page getPage(){
		return new Page();
	}
	
	public static void logBefore(Logger logger, String interfaceName){
		logger.info("");
		logger.info("start");
		logger.info(interfaceName);
	}
	
	public static void logAfter(Logger logger){
		logger.info("end");
		logger.info("");
	}
	protected JsonResponse ajaxSuccess() {
		return ajaxSuccess(null);
	}

	protected JsonResponse ajaxSuccess(Object data) {
		JsonResponse response = new JsonResponse();
		response.setSuccess(true);
		response.setData(data);

		return response;
	}

	protected JsonResponse ajaxSuccess(Object data, String msg) {
		JsonResponse response = new JsonResponse();
		response.setSuccess(true);
		response.setData(data);
		response.setMsg(msg);
		return response;
	}

	protected JsonResponse ajaxFailure() {
		JsonResponse response = new JsonResponse();
		response.setSuccess(false);

		return response;
	}

	protected JsonResponse ajaxFailure(String msg) {
		JsonResponse response = new JsonResponse();
		response.setSuccess(false);
		response.setMsg(msg);

		return response;
	}
	protected JsonResponse ajaxFailure(Integer status,String msg) {
		JsonResponse response = new JsonResponse();
		response.setSuccess(false);
		response.setMsg(msg);
		response.setStatus(status);
		return response;
	}
	protected JsonResponse ajaxSuccess(Integer status,String msg) {
		JsonResponse response = new JsonResponse();
		response.setSuccess(true);
		response.setMsg(msg);
		response.setStatus(status);
		return response;
	}

	protected JsonResponse ajaxSuccess(Object data,Integer status,String msg) {
		JsonResponse response = new JsonResponse();
		response.setSuccess(true);
		response.setData(data);
		response.setMsg(msg);
		response.setStatus(status);
		return response;
	}

	/**
     *  分页返回json标准，
	 * @param dataType data 集合名称，如user_items
     * @param data 返回数据库集合结果
     * @param page 分页信息
     * @param status 请求状态码
     * @param msg 请求返回消息
     * @return
     */
	protected JsonResponse ajaxSuccessPage(String dataType,Object data,Page page,Integer status,String msg) {
		PageData pageData = new PageData();
		pageData.put("pagesize",page.getShowCount());//每页数据数
		pageData.put("total",page.getTotalResult());//总数量
		pageData.put("offset",page.getOffset());//---偏移
		pageData.put(dataType+"_items",data);
		JsonResponse response = new JsonResponse();
		response.setSuccess(true);
		response.setData(pageData);
		response.setMsg(msg);
		response.setStatus(status);
		return response;
	}
}
