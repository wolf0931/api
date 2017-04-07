package com.wph.api.admin.controller;

import com.wph.api.admin.controller.annotation.PermessionLimit;
import com.wph.api.admin.core.consistant.RequestConfig;
import com.wph.api.admin.core.model.ApiDocument;
import com.wph.api.admin.core.model.ApiMock;
import com.wph.api.admin.core.model.ReturnT;
import com.wph.api.admin.dao.lApiDocumentDao;
import com.wph.api.admin.dao.lApiMockDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.UUID;

/**
 * @author xuxueli 2017-03-31 18:10:37
 */
@Controller
@RequestMapping("/mock")
public class ApiMockController {
	private static Logger logger = LoggerFactory.getLogger(ApiMockController.class);

	@Resource
	private lApiMockDao xxlApiMockDao;
	@Resource
	private lApiDocumentDao xxlApiDocumentDao;

	/**
	 * 保存Mock数据
	 * @param apiMock
	 * @return
	 */
	@RequestMapping("/add")
	@ResponseBody
	public ReturnT<String> add(ApiMock apiMock) {

		ApiDocument document = xxlApiDocumentDao.load(apiMock.getDocumentId());
		if (document == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "保存Mock数据失败，接口ID非法");
		}
		String uuid = UUID.randomUUID().toString();

		apiMock.setUuid(uuid);
		int ret = xxlApiMockDao.add(apiMock);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	@RequestMapping("/delete")
	@ResponseBody
	public ReturnT<String> delete(int id) {
		int ret = xxlApiMockDao.delete(id);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	@RequestMapping("/update")
	@ResponseBody
	public ReturnT<String> update(ApiMock apiMock) {
		int ret = xxlApiMockDao.update(apiMock);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	@RequestMapping("/run/{uuid}")
	@PermessionLimit(limit=false)
	public void run(@PathVariable("uuid") String uuid, HttpServletRequest request, HttpServletResponse response) {
		ApiMock apiMock = xxlApiMockDao.loadByUuid(uuid);
		if (apiMock == null) {
			throw new RuntimeException("Mock数据ID非法");
		}

		RequestConfig.ResponseContentType contentType = RequestConfig.ResponseContentType.match(apiMock.getRespType());
		if (contentType == null) {
			throw new RuntimeException("Mock数据响应数据类型(MIME)非法");
		}

		// write response
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType(contentType.type);

			PrintWriter writer = response.getWriter();
			writer.write(apiMock.getRespExample());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

}
