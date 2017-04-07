package com.wph.api.admin.dao.impl;

import com.wph.api.admin.core.model.ApiMock;
import com.wph.api.admin.dao.lApiMockDao;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by xuxueli on 17/4/1.
 */
@Repository
public class ApiMockDaoImpl implements lApiMockDao {

    @Resource
    public SqlSessionTemplate sqlSessionTemplate;

    @Override
    public int add(ApiMock apiMock) {
        return sqlSessionTemplate.insert("ApiMockMapper.add", apiMock);
    }

    @Override
    public int update(ApiMock apiMock) {
        return sqlSessionTemplate.update("ApiMockMapper.update", apiMock);
    }

    @Override
    public int delete(int id) {
        return sqlSessionTemplate.update("ApiMockMapper.delete", id);
    }

    @Override
    public List<ApiMock> loadAll(int documentId) {
        return sqlSessionTemplate.selectList("ApiMockMapper.loadAll", documentId);
    }

    @Override
    public ApiMock load(int id) {
        return sqlSessionTemplate.selectOne("ApiMockMapper.load", id);
    }

    @Override
    public ApiMock loadByUuid(String uuid) {
        return sqlSessionTemplate.selectOne("ApiMockMapper.loadByUuid", uuid);
    }
}
