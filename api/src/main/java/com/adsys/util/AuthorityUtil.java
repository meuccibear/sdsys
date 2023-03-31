package com.adsys.util;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.adsys.common.enums.permissionEnum;
import com.adsys.service.system.role.impl.RoleService;

public class AuthorityUtil {
	
	protected static Logger logger = Logger.getLogger(AuthorityUtil.class);
	
	
	public static String getRequesterUid(HttpServletRequest request)
	{
		return (String)request.getAttribute("userid");
	}

	public static String getRequesterUserName(HttpServletRequest request)
	{
		return (String)request.getAttribute("username");
	}
	
	public static String getRequesterName(HttpServletRequest request)
	{
		return (String)request.getAttribute("name");
	}
	
	public static String getRequesterUserType(HttpServletRequest request)
	{
		return (String)request.getAttribute("usertype");
	}
	
	public static String getRequesterUUID(HttpServletRequest request)
	{
		if ("appuser".equals((String)request.getAttribute("usertype")))
			return (String)request.getAttribute("uuid");
		else
			return "";
	}
	
	public static String getRequesterRoleID(HttpServletRequest request)
	{
		return (String)request.getAttribute("roleid");
	}
	
	public static boolean checkPermissionByRole(HttpServletRequest request, permissionEnum permission)
	{
		String perm = (String)request.getAttribute("permission");
		if (perm.indexOf(permission.getCode()) == -1)
			return false;
		else
			return true;
	
	}
}
