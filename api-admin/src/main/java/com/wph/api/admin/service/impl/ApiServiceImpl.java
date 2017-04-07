package com.wph.api.admin.service.impl;


import com.wph.api.admin.core.model.ReturnT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class ApiServiceImpl implements com.wph.api.admin.service.IApiService {
	private static Logger logger = LoggerFactory.getLogger(ApiServiceImpl.class);

	@Override
	public ReturnT<String> add() {
		return ReturnT.SUCCESS;
	}

}
