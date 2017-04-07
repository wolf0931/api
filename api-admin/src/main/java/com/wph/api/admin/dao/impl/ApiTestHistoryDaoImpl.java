package com.wph.api.admin.dao.impl;

import com.wph.api.admin.core.model.ApiTestHistory;
import com.wph.api.admin.dao.lApiTestHistoryDao;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by xuxueli on 2017-04-04 18:40:34
 */
@Repository
public class ApiTestHistoryDaoImpl implements lApiTestHistoryDao {

    @Resource
    public SqlSessionTemplate sqlSessionTemplate;

    @Override
    public int add(ApiTestHistory apiTestHistory) {
        return sqlSessionTemplate.insert("XxlApiTestHistoryMapper.add", apiTestHistory);
    }

    @Override
    public int update(ApiTestHistory apiTestHistory) {
        return sqlSessionTemplate.update("XxlApiTestHistoryMapper.update", apiTestHistory);
    }

    @Override
    public int delete(int id) {
        return sqlSessionTemplate.update("XxlApiTestHistoryMapper.delete", id);
    }

    @Override
    public ApiTestHistory load(int id) {
        return sqlSessionTemplate.selectOne("XxlApiTestHistoryMapper.load", id);
    }

    @Override
    public List<ApiTestHistory> loadByDocumentId(int documentId) {
        return sqlSessionTemplate.selectList("XxlApiTestHistoryMapper.loadByDocumentId", documentId);
    }


}
