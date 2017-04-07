package com.wph.api.admin.controller;

import com.wph.api.admin.core.model.ApiDocument;
import com.wph.api.admin.core.model.ApiGroup;
import com.wph.api.admin.core.model.ApiProject;
import com.wph.api.admin.core.model.ReturnT;
import com.wph.api.admin.dao.lApiDocumentDao;
import com.wph.api.admin.dao.lApiGroupDao;
import com.wph.api.admin.dao.lApiProjectDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xuxueli 2017-03-31 18:10:37
 */
@Controller
@RequestMapping("/group")
public class ApiGroupController {

	@Resource
	private lApiProjectDao xxlApiProjectDao;
	@Resource
	private lApiGroupDao xxlApiGroupDao;
	@Resource
	private lApiDocumentDao xxlApiDocumentDao;

	@RequestMapping
	public String index(Model model, int productId, @RequestParam(required = false, defaultValue = "-1")  int groupId) {

		// 项目
		ApiProject apiProject = xxlApiProjectDao.load(productId);
		if (apiProject == null) {
			throw new RuntimeException("系统异常，项目ID非法");
		}
		model.addAttribute("productId", productId);
		model.addAttribute("project", apiProject);

		// 分组列表
		List<ApiGroup> groupList = xxlApiGroupDao.loadAll(productId);
		model.addAttribute("groupList", groupList);

		// 选中分组
		ApiGroup groupInfo = null;
		if (CollectionUtils.isNotEmpty(groupList)) {
			for (ApiGroup groupItem: groupList) {
				if (groupId == groupItem.getId()) {
					groupInfo = groupItem;
				}
			}
		}
		if (groupInfo == null && !(groupId==-1 || groupId==0)) {
			groupId = -1;	// 合法性校验：全部(-1) | 默认分组(0) | 指定分组(匹配到数据)
		}
		model.addAttribute("groupId", groupId);
		model.addAttribute("groupInfo", groupInfo);

		// 分组下的，接口列表
		List<ApiDocument> documentList = xxlApiDocumentDao.loadAll(productId, groupId);
		model.addAttribute("documentList", documentList);

		return "group/group.list";
	}

	@RequestMapping("/add")
	@ResponseBody
	public ReturnT<String> add(ApiGroup apiGroup) {
		// valid
		if (StringUtils.isBlank(apiGroup.getName())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入“分组名称”");
		}

		int ret = xxlApiGroupDao.add(apiGroup);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	@RequestMapping("/update")
	@ResponseBody
	public ReturnT<String> update(ApiGroup apiGroup) {
		// exist
		ApiGroup existGroup = xxlApiGroupDao.load(apiGroup.getId());
		if (existGroup == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "更新失败，分组ID非法");
		}

		// valid
		if (StringUtils.isBlank(apiGroup.getName())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入“分组名称”");
		}

		int ret = xxlApiGroupDao.update(apiGroup);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	@RequestMapping("/delete")
	@ResponseBody
	public ReturnT<String> delete(int id) {

		// 分组下是否存在接口
		List<ApiDocument> documentList = xxlApiDocumentDao.loadByGroupId(id);
		if (CollectionUtils.isNotEmpty(documentList)) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "拒绝删除，分组下存在接口，不允许强制删除");
		}

		int ret = xxlApiGroupDao.delete(id);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

}
