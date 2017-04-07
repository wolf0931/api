package com.wph.api.admin.service.impl;

import com.wph.api.admin.core.model.ApiUser;
import com.wph.api.admin.core.model.ReturnT;
import com.wph.api.admin.core.util.CookieUtil;
import com.wph.api.admin.dao.lApiUserDao;
import com.wph.api.admin.service.IApiUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;

/**
 * Created by xuxueli on 17/3/30.
 */
@Service
public class ApiUserServiceImpl implements IApiUserService {

    public static final String LOGIN_IDENTITY_KEY = "XXL_API_LOGIN_IDENTITY";
    private static String makeToken (ApiUser apiUser) {
        String tokenStr = apiUser.getUserName() + "_" + apiUser.getPassword() + "_" + apiUser.getType();
        String token = new BigInteger(1, tokenStr.getBytes()).toString(16);
        return token;
    }
    private static ApiUser parseToken(HttpServletRequest request){
        String token = CookieUtil.getValue(request, LOGIN_IDENTITY_KEY);
        if (token != null) {
            String tokenStr = new String(new BigInteger(token, 16).toByteArray());
            String[] tokenArr = tokenStr.split("_");
            if (tokenArr!=null && tokenArr.length==3) {
                ApiUser apiUser = new ApiUser();
                apiUser.setUserName(tokenArr[0]);
                apiUser.setPassword(tokenArr[1]);
                apiUser.setType(Integer.valueOf(tokenArr[2]));
                return apiUser;
            }
        }

        return null;
    }

    @Resource
    private lApiUserDao xxlApiUserDao;

    @Override
    public ReturnT<String> login(HttpServletRequest request, HttpServletResponse response, boolean ifRemember, String userName, String password) {

        ApiUser loginUser = ifLogin(request);
        if (loginUser != null) {
            return ReturnT.SUCCESS;
        }

        ApiUser apiUser = xxlApiUserDao.findByUserName(userName);
        if (apiUser == null) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "登录用户不存在");
        }
        if (!apiUser.getPassword().equals(password)) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "登录密码错误");
        }

        String token = makeToken(apiUser);
        CookieUtil.set(response, LOGIN_IDENTITY_KEY, token, ifRemember);
        return ReturnT.SUCCESS;
    }

    @Override
    public ReturnT<String> logout(HttpServletRequest request, HttpServletResponse response) {

        ApiUser loginUser = ifLogin(request);
        if (loginUser == null) {
            return ReturnT.SUCCESS;

        }

        CookieUtil.remove(request, response, LOGIN_IDENTITY_KEY);
        return ReturnT.SUCCESS;
    }

    @Override
    public ApiUser ifLogin(HttpServletRequest request) {
        ApiUser loginUser = parseToken(request);
        return loginUser;
    }
}
