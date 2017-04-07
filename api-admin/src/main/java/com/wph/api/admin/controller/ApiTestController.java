package com.wph.api.admin.controller;

import com.wph.api.admin.core.consistant.RequestConfig;
import com.wph.api.admin.core.model.ApiDocument;
import com.wph.api.admin.core.model.ApiProject;
import com.wph.api.admin.core.model.ApiTestHistory;
import com.wph.api.admin.core.model.ReturnT;
import com.wph.api.admin.core.util.JacksonUtil;
import com.wph.api.admin.dao.lApiDocumentDao;
import com.wph.api.admin.dao.lApiProjectDao;
import com.wph.api.admin.dao.lApiTestHistoryDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xuxueli 2017-04-04 18:10:54
 */
@Controller
@RequestMapping("/test")
public class ApiTestController {
	private Logger logger = LoggerFactory.getLogger(ApiTestController.class);

	@Resource
	private lApiDocumentDao xxlApiDocumentDao;
	@Resource
	private lApiTestHistoryDao xxlApiTestHistoryDao;
	@Resource
	private lApiProjectDao xxlApiProjectDao;

	/**
	 * 接口测试，待完善
	 * @return
	 */
	@RequestMapping
	public String index(Model model,
			@RequestParam(required = false, defaultValue = "0") int documentId,
			@RequestParam(required = false, defaultValue = "0") int testId) {


		// params
		ApiProject project = null;
		ApiDocument document = null;
		List<Map<String, String>> requestHeaders = null;
		List<Map<String, String>> queryParams = null;

		if (testId > 0) {
			ApiTestHistory testHistory = xxlApiTestHistoryDao.load(testId);
			documentId = testHistory.getDocumentId();

			document = xxlApiDocumentDao.load(documentId);
			project = xxlApiProjectDao.load(document.getProjectId());

			requestHeaders = (StringUtils.isNotBlank(testHistory.getRequestHeaders()))? JacksonUtil.readValue(testHistory.getRequestHeaders(), List.class):null;
			queryParams = (StringUtils.isNotBlank(testHistory.getQueryParams()))? JacksonUtil.readValue(testHistory.getQueryParams(), List.class):null;
		} else {
			document = xxlApiDocumentDao.load(documentId);
			project = xxlApiProjectDao.load(document.getProjectId());

			requestHeaders = (StringUtils.isNotBlank(document.getRequestHeaders()))? JacksonUtil.readValue(document.getRequestHeaders(), List.class):null;
			queryParams = (StringUtils.isNotBlank(document.getQueryParams()))? JacksonUtil.readValue(document.getQueryParams(), List.class):null;
		}

		model.addAttribute("document", document);
		model.addAttribute("project", project);
		model.addAttribute("requestHeaders", requestHeaders);
		model.addAttribute("queryParams", queryParams);
        model.addAttribute("documentId", documentId);
        model.addAttribute("testId", testId);

		// enum
		model.addAttribute("RequestMethodEnum", RequestConfig.RequestMethodEnum.values());
		model.addAttribute("requestHeadersEnum", RequestConfig.requestHeadersEnum);
		model.addAttribute("QueryParamTypeEnum", RequestConfig.QueryParamTypeEnum.values());
		model.addAttribute("ResponseContentType", RequestConfig.ResponseContentType.values());

		return "test/test.index";
	}

	@RequestMapping("/add")
	@ResponseBody
	public ReturnT<Integer> add(ApiTestHistory apiTestHistory) {
		int ret = xxlApiTestHistoryDao.add(apiTestHistory);
		return ret>0?new ReturnT<Integer>(apiTestHistory.getId()):new ReturnT<Integer>(ReturnT.FAIL_CODE, null);
	}

	@RequestMapping("/update")
	@ResponseBody
	public ReturnT<String> update(ApiTestHistory apiTestHistory) {
		int ret = xxlApiTestHistoryDao.update(apiTestHistory);
		return ret>0?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	@RequestMapping("/delete")
	@ResponseBody
	public ReturnT<String> delete(int id) {
		int ret = xxlApiTestHistoryDao.delete(id);
		return ret>0?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	/**
	 * 测试Run
	 * @return
	 */
	@RequestMapping("/run")
	@ResponseBody
	public ReturnT<String> run(ApiTestHistory apiTestHistory, HttpServletRequest request, HttpServletResponse response) {

		// valid
		RequestConfig.ResponseContentType contentType = RequestConfig.ResponseContentType.match(apiTestHistory.getRespType());
		if (contentType == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "响应数据类型(MIME)非法");
		}

		if (StringUtils.isBlank(apiTestHistory.getRequestUrl())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入接口URL");
		}

		// request headers
		Map<String, String> requestHeaderMap = null;
		List<Map<String, String>> requestHeaders = (StringUtils.isNotBlank(apiTestHistory.getRequestHeaders()))? JacksonUtil.readValue(apiTestHistory.getRequestHeaders(), List.class):null;
		if (CollectionUtils.isNotEmpty(requestHeaders)) {
			requestHeaderMap = new HashMap<String, String>();
			for (Map<String, String> item: requestHeaders) {
				requestHeaderMap.put(item.get("key"), item.get("value"));
			}
		}

		// query param
		Map<String, String> queryParamMap = null;
		List<Map<String, String>> queryParams = (StringUtils.isNotBlank(apiTestHistory.getQueryParams()))? JacksonUtil.readValue(apiTestHistory.getQueryParams(), List.class):null;
		if (CollectionUtils.isNotEmpty(requestHeaders)) {
			queryParamMap = new HashMap<String, String>();
			for (Map<String, String> item: queryParams) {
				queryParamMap.put(item.get("key"), item.get("value"));
			}
		}

		// invoke 1/3
		HttpRequestBase remoteRequest = null;
		if (RequestConfig.RequestMethodEnum.POST.name().equals(apiTestHistory.getRequestMethod())) {
			HttpPost httpPost = new HttpPost(apiTestHistory.getRequestUrl());
			// query params
			if (queryParamMap != null && !queryParamMap.isEmpty()) {
				List<NameValuePair> formParams = new ArrayList<NameValuePair>();
				for(Map.Entry<String,String> entry : queryParamMap.entrySet()){
					formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
				try {
					httpPost.setEntity(new UrlEncodedFormEntity(formParams, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					logger.error(e.getMessage(), e);
				}
			}
			remoteRequest = httpPost;
		} else if (RequestConfig.RequestMethodEnum.GET.name().equals(apiTestHistory.getRequestMethod())) {
			remoteRequest = new HttpGet(markGetUrl(apiTestHistory.getRequestUrl(), queryParamMap));
		} else if (RequestConfig.RequestMethodEnum.PUT.name().equals(apiTestHistory.getRequestMethod())) {
			remoteRequest = new HttpPut(markGetUrl(apiTestHistory.getRequestUrl(), queryParamMap));
		} else if (RequestConfig.RequestMethodEnum.DELETE.name().equals(apiTestHistory.getRequestMethod())) {
			remoteRequest = new HttpDelete(markGetUrl(apiTestHistory.getRequestUrl(), queryParamMap));
		} else if (RequestConfig.RequestMethodEnum.HEAD.name().equals(apiTestHistory.getRequestMethod())) {
			remoteRequest = new HttpHead(markGetUrl(apiTestHistory.getRequestUrl(), queryParamMap));
		} else if (RequestConfig.RequestMethodEnum.OPTIONS.name().equals(apiTestHistory.getRequestMethod())) {
			remoteRequest = new HttpOptions(markGetUrl(apiTestHistory.getRequestUrl(), queryParamMap));
		} else if (RequestConfig.RequestMethodEnum.PATCH.name().equals(apiTestHistory.getRequestMethod())) {
			remoteRequest = new HttpPatch(markGetUrl(apiTestHistory.getRequestUrl(), queryParamMap));
		} else {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "请求方法非法");
		}

		// invoke 2/3
		if (requestHeaderMap!=null && !requestHeaderMap.isEmpty()) {
			for(Map.Entry<String,String> entry : requestHeaderMap.entrySet()){
				remoteRequest.setHeader(entry.getKey(), entry.getValue());
			}
		}

		// write response
		String responseContent = remoteCall(remoteRequest);
		return new ReturnT<String>(responseContent);
	}

	private String markGetUrl(String url, Map<String, String> queryParamMap){
		String finalUrl = url + "?";
		if (queryParamMap!=null && !queryParamMap.isEmpty()) {
			for(Map.Entry<String,String> entry : queryParamMap.entrySet()){
				finalUrl += entry.getKey() + "=" + entry.getValue() + "&";
			}
		}
		finalUrl = finalUrl.substring(0, finalUrl.length()-2);
		return finalUrl;
	}

	private String remoteCall(HttpRequestBase remoteRequest){
		// remote test
		String responseContent = null;

		CloseableHttpClient httpClient = null;
		try{
			org.apache.http.client.config.RequestConfig requestConfig = org.apache.http.client.config.RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
			remoteRequest.setConfig(requestConfig);

			httpClient = HttpClients.custom().disableAutomaticRetries().build();

			// parse response
			HttpResponse response = httpClient.execute(remoteRequest);
			HttpEntity entity = response.getEntity();
			if (null != entity) {
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode == 200) {
					responseContent = EntityUtils.toString(entity, "UTF-8");
				} else {
					responseContent = "请求状态异常：" + response.getStatusLine().getStatusCode();
					if (statusCode == 302) {
						responseContent += "；Redirect地址：" + response.getHeaders("Location");
					}

				}
				EntityUtils.consume(entity);
			}
			logger.info("http statusCode error, statusCode:" + response.getStatusLine().getStatusCode());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			responseContent = "请求异常：" + e.getMessage();
		} finally{
			if (remoteRequest!=null) {
				remoteRequest.releaseConnection();
			}
			if (httpClient!=null) {
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return responseContent;
	}

}
