package com.wph.api.admin.controller;

import com.wph.api.admin.core.consistant.RequestConfig;
import com.wph.api.admin.core.model.ApiDocument;
import com.wph.api.admin.core.model.ApiGroup;
import com.wph.api.admin.core.model.ApiMock;
import com.wph.api.admin.core.model.ApiProject;
import com.wph.api.admin.core.model.ApiTestHistory;
import com.wph.api.admin.core.model.ReturnT;
import com.wph.api.admin.core.util.JacksonUtil;
import com.wph.api.admin.dao.lApiDocumentDao;
import com.wph.api.admin.dao.lApiGroupDao;
import com.wph.api.admin.dao.lApiMockDao;
import com.wph.api.admin.dao.lApiProjectDao;
import com.wph.api.admin.dao.lApiTestHistoryDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xuxueli 2017-03-31 18:10:37
 */
@Controller
@RequestMapping("/document")
public class ApiDocumentController {

	@Resource
	private lApiDocumentDao xxlApiDocumentDao;
	@Resource
	private lApiProjectDao xxlApiProjectDao;
	@Resource
	private lApiGroupDao xxlApiGroupDao;
	@Resource
	private lApiMockDao xxlApiMockDao;
	@Resource
	private lApiTestHistoryDao xxlApiTestHistoryDao;


	@RequestMapping("/markStar")
	@ResponseBody
	public ReturnT<String> markStar(int id, int starLevel) {

		ApiDocument document = xxlApiDocumentDao.load(id);
		if (document == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "操作失败，接口ID非法");
		}

		document.setStarLevel(starLevel);

		int ret = xxlApiDocumentDao.update(document);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	@RequestMapping("/delete")
	@ResponseBody
	public ReturnT<String> delete(int id) {

		// 存在Test记录，拒绝删除
		List<ApiTestHistory> historyList = xxlApiTestHistoryDao.loadByDocumentId(id);
		if (CollectionUtils.isNotEmpty(historyList)) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "拒绝删除，该接口下存在Test记录，不允许删除");
		}

		// 存在Mock记录，拒绝删除
		List<ApiMock> mockList = xxlApiMockDao.loadAll(id);
		if (CollectionUtils.isNotEmpty(mockList)) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "拒绝删除，该接口下存在Mock记录，不允许删除");
		}

		int ret = xxlApiDocumentDao.delete(id);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	/**
	 * 新增，API
	 *
	 * @param productId
	 * @return
	 */
	@RequestMapping("/addPage")
	public String addPage(Model model, int productId) {

		// project
		ApiProject project = xxlApiProjectDao.load(productId);
		if (project == null) {
			throw new RuntimeException("操作失败，项目ID非法");
		}
		model.addAttribute("productId", productId);

		// groupList
		List<ApiGroup> groupList = xxlApiGroupDao.loadAll(productId);
		model.addAttribute("groupList", groupList);

		// enum
		model.addAttribute("RequestMethodEnum", RequestConfig.RequestMethodEnum.values());
		model.addAttribute("requestHeadersEnum", RequestConfig.requestHeadersEnum);
		model.addAttribute("QueryParamTypeEnum", RequestConfig.QueryParamTypeEnum.values());
		model.addAttribute("ResponseContentType", RequestConfig.ResponseContentType.values());

		return "document/document.add";
	}
	@RequestMapping("/add")
	@ResponseBody
	public ReturnT<Integer> add(ApiDocument apiDocument) {
		int ret = xxlApiDocumentDao.add(apiDocument);
		return (ret>0)?new ReturnT<Integer>(apiDocument.getId()):new ReturnT<Integer>(ReturnT.FAIL_CODE, null);
	}

	/**
	 * 更新，API
	 * @return
	 */
	@RequestMapping("/updatePage")
	public String updatePage(Model model, int id) {

		// document
		ApiDocument apiDocument = xxlApiDocumentDao.load(id);
		if (apiDocument == null) {
			throw new RuntimeException("操作失败，接口ID非法");
		}
		model.addAttribute("document", apiDocument);
		model.addAttribute("requestHeadersList", (StringUtils.isNotBlank(apiDocument.getRequestHeaders()))? JacksonUtil.readValue(apiDocument.getRequestHeaders(), List.class):null );
		model.addAttribute("queryParamList", (StringUtils.isNotBlank(apiDocument.getQueryParams()))?JacksonUtil.readValue(apiDocument.getQueryParams(), List.class):null );
		model.addAttribute("responseParamList", (StringUtils.isNotBlank(apiDocument.getResponseParams()))?JacksonUtil.readValue(apiDocument.getResponseParams(), List.class):null );

		// project
		int projectId = apiDocument.getProjectId();
		model.addAttribute("productId", projectId);

		// groupList
		List<ApiGroup> groupList = xxlApiGroupDao.loadAll(projectId);
		model.addAttribute("groupList", groupList);

		// enum
		model.addAttribute("RequestMethodEnum", RequestConfig.RequestMethodEnum.values());
		model.addAttribute("requestHeadersEnum", RequestConfig.requestHeadersEnum);
		model.addAttribute("QueryParamTypeEnum", RequestConfig.QueryParamTypeEnum.values());
		model.addAttribute("ResponseContentType", RequestConfig.ResponseContentType.values());

		return "document/document.update";
	}
	@RequestMapping("/update")
	@ResponseBody
	public ReturnT<String> update(ApiDocument apiDocument) {

		// fill not-change val
		ApiDocument oldVo = xxlApiDocumentDao.load(apiDocument.getId());
		apiDocument.setProjectId(oldVo.getProjectId());
		apiDocument.setStarLevel(oldVo.getStarLevel());
		apiDocument.setAddTime(oldVo.getAddTime());

		int ret = xxlApiDocumentDao.update(apiDocument);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	/**
	 * 详情页，API
	 * @return
	 */
	@RequestMapping("/detailPage")
	public String detailPage(Model model, int id) {

		// document
		ApiDocument apiDocument = xxlApiDocumentDao.load(id);
		if (apiDocument == null) {
			throw new RuntimeException("操作失败，接口ID非法");
		}
		model.addAttribute("document", apiDocument);
		model.addAttribute("requestHeadersList", (StringUtils.isNotBlank(apiDocument.getRequestHeaders()))?JacksonUtil.readValue(apiDocument.getRequestHeaders(), List.class):null );
		model.addAttribute("queryParamList", (StringUtils.isNotBlank(apiDocument.getQueryParams()))?JacksonUtil.readValue(apiDocument.getQueryParams(), List.class):null );
		model.addAttribute("responseParamList", (StringUtils.isNotBlank(apiDocument.getResponseParams()))?JacksonUtil.readValue(apiDocument.getResponseParams(), List.class):null );

		// project
		int projectId = apiDocument.getProjectId();
		ApiProject project = xxlApiProjectDao.load(projectId);
		model.addAttribute("productId", projectId);
		model.addAttribute("project", project);

		// groupList
		List<ApiGroup> groupList = xxlApiGroupDao.loadAll(projectId);
		model.addAttribute("groupList", groupList);

		// mock list
		List<ApiMock> mockList = xxlApiMockDao.loadAll(id);
		model.addAttribute("mockList", mockList);

		// test list
		List<ApiTestHistory> testHistoryList = xxlApiTestHistoryDao.loadByDocumentId(id);
		model.addAttribute("testHistoryList", testHistoryList);

		// enum
		model.addAttribute("RequestMethodEnum", RequestConfig.RequestMethodEnum.values());
		model.addAttribute("requestHeadersEnum", RequestConfig.requestHeadersEnum);
		model.addAttribute("QueryParamTypeEnum", RequestConfig.QueryParamTypeEnum.values());
		model.addAttribute("ResponseContentType", RequestConfig.ResponseContentType.values());

		return "document/document.detail";
	}

}
