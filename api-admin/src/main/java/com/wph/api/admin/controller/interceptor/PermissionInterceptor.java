package com.wph.api.admin.controller.interceptor;

import com.wph.api.admin.controller.annotation.PermessionLimit;
import com.wph.api.admin.core.model.ApiUser;
import com.wph.api.admin.service.IApiUserService;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.wph.api.admin.service.impl.ApiUserServiceImpl.LOGIN_IDENTITY_KEY;

/**
 * 权限拦截
 * @author xuxueli 2015-12-12 18:09:04
 */
public class PermissionInterceptor extends HandlerInterceptorAdapter {

	@Resource
	private IApiUserService xxlApiUserService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		if (!(handler instanceof HandlerMethod)) {
			return super.preHandle(request, response, handler);
		}

		// if limit
		HandlerMethod method = (HandlerMethod)handler;
		PermessionLimit permission = method.getMethodAnnotation(PermessionLimit.class);
		boolean limit = (permission != null)?permission.limit():true;
		boolean superUser = (permission != null)?permission.superUser():false;

		// login user
		ApiUser loginUser = xxlApiUserService.ifLogin(request);
		request.setAttribute(LOGIN_IDENTITY_KEY, loginUser);

		// if pass
		boolean ifPass = false;
		if (limit) {
			if (loginUser == null) {
				ifPass = false;
			} else {
				if (superUser) {
					// 0-普通用户、1-超级管理员
					if (loginUser.getType() == 1) {
						ifPass = true;
					} else {
						ifPass = false;
					}
				} else {
					ifPass = true;
				}
			}
		} else {
			ifPass = true;
		}

		if (!ifPass) {
			response.sendRedirect("/toLogin");	//request.getRequestDispatcher("/toLogin").forward(request, response);
			return false;
		}
		
		return super.preHandle(request, response, handler);
	}
	
}
