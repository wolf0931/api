package com.wph.api.admin.controller;

import com.wph.api.admin.controller.annotation.PermessionLimit;
import com.wph.api.admin.core.model.ApiUser;
import com.wph.api.admin.core.model.ReturnT;
import com.wph.api.admin.core.util.JacksonUtil;
import com.wph.api.admin.dao.lApiUserDao;
import com.wph.api.admin.service.IApiUserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.wph.api.admin.service.impl.ApiUserServiceImpl.LOGIN_IDENTITY_KEY;

/**
 * index controller
 * @author xuxueli 2015-12-19 16:13:16
 */
@Controller
@RequestMapping("/user")
public class ApiUserController {

	@Resource
	private lApiUserDao xxlApiUserDao;
	@Resource
	private IApiUserService xxlApiUserService;

	@RequestMapping
    @PermessionLimit(superUser = true)
	public String index(Model model, HttpServletRequest request) {

		// permission
		ApiUser loginUser = (request.getAttribute(LOGIN_IDENTITY_KEY)!=null)? (ApiUser) request.getAttribute(LOGIN_IDENTITY_KEY) :null;
		if (loginUser.getType()!=1) {
			throw new RuntimeException("权限拦截.");
		}

		List<ApiUser> userList = xxlApiUserDao.loadAll();
		if (CollectionUtils.isEmpty(userList)) {
			userList = new ArrayList<>();
		} else {
			for (ApiUser user: userList) {
				user.setPassword("***");
			}
		}
		model.addAttribute("userList", JacksonUtil.writeValueAsString(userList));

		return "user/user.list";
	}

	@RequestMapping("/add")
	@ResponseBody
    @PermessionLimit(superUser = true)
	public ReturnT<String> add(ApiUser apiUser) {
		// valid
		if (StringUtils.isBlank(apiUser.getUserName())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入“登录账号”");
		}
		if (StringUtils.isBlank(apiUser.getPassword())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入“登录密码”");
		}

		// valid
		ApiUser existUser = xxlApiUserDao.findByUserName(apiUser.getUserName());
		if (existUser != null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "“登录账号”重复，请更换");
		}

		int ret = xxlApiUserDao.add(apiUser);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	@RequestMapping("/update")
	@ResponseBody
    @PermessionLimit(superUser = true)
	public ReturnT<String> update(ApiUser apiUser) {

		// exist
		ApiUser existUser = xxlApiUserDao.findByUserName(apiUser.getUserName());
		if (existUser == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "更新失败，登录账号非法");
		}

		// update param
		if (StringUtils.isNotBlank(apiUser.getPassword())) {
			existUser.setPassword(apiUser.getPassword());
		}
		existUser.setType(apiUser.getType());
		existUser.setRealName(apiUser.getRealName());

		int ret = xxlApiUserDao.update(existUser);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	@RequestMapping("/delete")
	@ResponseBody
	@PermessionLimit(superUser = true)
	public ReturnT<String> delete(HttpServletRequest request, int id) {

		// valid user
		ApiUser delUser = xxlApiUserDao.findById(id);
		if (delUser == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "拒绝删除，用户ID非法");
		}

		ApiUser loginUser = xxlApiUserService.ifLogin(request);
		if (loginUser == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "拒绝删除，系统登录异常");
		}

		if (delUser.getUserName().equals(loginUser.getUserName())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "拒绝删除，不可以删除自己的用户信息");
		}

		// must leave one user
		List<ApiUser> allUser = xxlApiUserDao.loadAll();
		if (allUser==null || allUser.size()==1) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "拒绝删除，系统至少保留一个登录用户");
		}

		int ret = xxlApiUserDao.delete(id);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

}
