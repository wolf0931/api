package com.wph.api.admin.dao.impl;

import com.wph.api.admin.core.model.ApiDocument;
import com.wph.api.admin.dao.lApiDocumentDao;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuxueli on 17/3/31.
 */
@Repository
public class ApiDocumentDaoImpl implements lApiDocumentDao {

    @Resource
    public SqlSessionTemplate sqlSessionTemplate;

    @Override
    public int add(ApiDocument apiDocument) {
        return sqlSessionTemplate.insert("ApiDocumentMapper.add", apiDocument);
    }

    @Override
    public int update(ApiDocument apiDocument) {
        return sqlSessionTemplate.update("ApiDocumentMapper.update", apiDocument);
    }

    @Override
    public int delete(int id) {
        return sqlSessionTemplate.update("ApiDocumentMapper.delete", id);
    }

    @Override
    public ApiDocument load(int id) {
        return sqlSessionTemplate.selectOne("ApiDocumentMapper.load", id);
    }

    @Override
    public List<ApiDocument> loadAll(int productId, int groupId) {
        Map<String ,Integer> params = new HashMap<String, Integer>();
        params.put("productId", productId);
        params.put("groupId", groupId);

        return sqlSessionTemplate.selectList("ApiDocumentMapper.loadAll", params);
    }

    @Override
    public List<ApiDocument> loadByGroupId(int groupId) {
        return sqlSessionTemplate.selectList("ApiDocumentMapper.loadByGroupId", groupId);
    }

}
