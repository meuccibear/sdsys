package com.adsys.interceptor;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.adsys.service.system.role.impl.RoleService;
import com.adsys.util.Const;
import com.adsys.util.JwtUtil;
import com.adsys.util.Logger;
import com.adsys.util.PageData;
/**
 * 
* 类名称：登录过滤，权限验证
* 类描述： 
 */
public class LoginHandlerInterceptor extends HandlerInterceptorAdapter{
	
	protected Logger logger = Logger.getLogger(this.getClass());
	
	@Resource(name="roleService")
	private RoleService roleservice;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// TODO Auto-generated method stub
		response.setCharacterEncoding("UTF-8");  
		response.setHeader("Access-Control-Allow-Origin", "*"); //herman
		response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE,PUT");
		response.setHeader("Access-Control-Allow-Headers", "accept, X-Access-Token, content-type");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Content-Type", "text/html;charset=UTF-8");
		
		
		if ("OPTIONS".equals(request.getMethod()))
		{
			return true;
		}
		String token = request.getHeader("X-Access-Token");
		Claims claims = null;
		String path = request.getServletPath();
		if(path.matches(Const.NO_INTERCEPTOR_PATH)){
			return true;
		}else{
			if(token!=null && !token.equals("")){
				JwtUtil jwt = new JwtUtil();
				try{
					claims=jwt.parseJWT(token);
					String body = claims.getSubject();
					JSONObject obj = new JSONObject();
					obj = JSONObject.fromObject(body);
					request.setAttribute("userid", obj.opt("userid"));
					request.setAttribute("username", obj.opt("username"));
					request.setAttribute("usertype", obj.opt("user_type"));
					request.setAttribute("uuid", obj.opt("uuid"));
					request.setAttribute("roleid", obj.opt("role_id"));
					System.out.println("username=" + obj.opt("username") + " uuid=" + obj.opt("uuid"));
					PageData pd = new PageData();
					pd.put("id", (String)obj.opt("role_id"));
					PageData rst = roleservice.findById(pd);
					request.setAttribute("permission", rst.getString("permission"));
					return true;
				}catch(SignatureException  e){
//					e.printStackTrace();
					response.setStatus(HttpStatus.SC_FORBIDDEN);
					response.getWriter().append("验证失败，无效token!");
					return false;
				}catch (ExpiredJwtException  e1) {
					// TODO: handle exception
					response.setStatus(HttpStatus.SC_UNAUTHORIZED);
					response.getWriter().append("token已过期，请重新授权!");
					return false;
				}
			}else{
				response.setStatus(HttpStatus.SC_FORBIDDEN);
				response.getWriter().append("验证失败，无效token!");
				return false;
			}
		}
	}
	
}
