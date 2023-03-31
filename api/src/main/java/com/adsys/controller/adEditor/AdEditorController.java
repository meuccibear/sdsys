package com.adsys.controller.adEditor;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.adsys.controller.base.BaseController;
import com.adsys.service.system.role.impl.RoleService;

/** 
 * 说明：
 * 创建时间：2018年4月18日
 */
@Controller
@RequestMapping(value="/ad/editor")
public class AdEditorController extends BaseController {
	
	@Resource(name="roleService")
	private RoleService roleservice; 

	
	/**
	 * 跳转设计器界面
	 * @return
	 */
	@RequestMapping(value = "/" )
	public String viewEditor(){
		return "adEditor/editor";
	}
	
	/**
	 * 跳转预览界面
	 * @return
	 */
	@RequestMapping(value = "/preview" )
	public String viewPreview(Model model){
		model.addAttribute("pid", this.getRequest().getParameter("pid"));
		model.addAttribute("width", this.getRequest().getParameter("width"));
		model.addAttribute("height", this.getRequest().getParameter("height"));
		model.addAttribute("orgwidth", this.getRequest().getParameter("orgwidth"));
		model.addAttribute("orgheight", this.getRequest().getParameter("orgheight"));
		model.addAttribute("wwidth", this.getRequest().getParameter("wwidth"));
		model.addAttribute("wheight", this.getRequest().getParameter("wheight"));
		return "adEditor/preview";
	}
	
	/**
	 * 跳转发送界面
	 * @return
	 */
	@RequestMapping(value = "/sent" )
	public String viewSent(Model model){
		model.addAttribute("id", this.getRequest().getParameter("id"));
		model.addAttribute("pname", this.getRequest().getParameter("pname"));
		return "adEditor/sent";
	}
	
	/**
	 * 跳转到编辑文件夹界面
	 * @return
	 */
	@RequestMapping(value = "/editFolder" )
	public String viewFolder(Model model){
		model.addAttribute("pid", this.getRequest().getParameter("pid"));
		model.addAttribute("id", this.getRequest().getParameter("id"));
		model.addAttribute("pResName", this.getRequest().getParameter("pResName"));
		return "adEditor/folder";
	}
	
	/**
	 * 跳转到上传素材界面
	 * @return
	 */
	@RequestMapping(value = "/upload" )
	public String viewUpload(Model model){
		model.addAttribute("pid", this.getRequest().getParameter("pid"));
		return "adEditor/upload";
	}
	
	/**
	 * 跳转到上传列表界面
	 * @return
	 */
	@RequestMapping(value = "/uploadList" )
	public String viewUploadList(){
		return "adEditor/upload-list";
	}
	
	/**
	 * 跳转到新增节目界面
	 * @return
	 */
	@RequestMapping(value = "/editProgram" )
	public String viewEditProgram(){
		return "adEditor/program";
	}
	
	
	
}
