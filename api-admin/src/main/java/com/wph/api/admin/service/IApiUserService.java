package com.wph.api.admin.service;

import com.wph.api.admin.core.model.ReturnT;
import com.wph.api.admin.core.model.ApiUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by xuxueli on 17/3/30.
 */
public interface IApiUserService {

     ReturnT<String> login(HttpServletRequest request, HttpServletResponse response, boolean ifRemember, String userName, String password);

     ReturnT<String> logout(HttpServletRequest request, HttpServletResponse response);

     ApiUser ifLogin(HttpServletRequest request);

}
