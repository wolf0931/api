package com.wph.api.admin.dao.impl;

import com.wph.api.admin.core.model.ApiGroup;
import com.wph.api.admin.dao.lApiGroupDao;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by xuxueli on 17/3/30.
 */
@Repository
public class ApiGroupDaoImpl implements lApiGroupDao {

    @Resource
    public SqlSessionTemplate sqlSessionTemplate;

    @Override
    public int add(ApiGroup apiGroup) {
        return sqlSessionTemplate.insert("ApiGroupMapper.add", apiGroup);
    }

    @Override
    public int update(ApiGroup apiGroup) {
        return sqlSessionTemplate.update("ApiGroupMapper.update", apiGroup);
    }

    @Override
    public int delete(int id) {
        return sqlSessionTemplate.update("ApiGroupMapper.delete", id);
    }

    @Override
    public ApiGroup load(int id) {
        return sqlSessionTemplate.selectOne("ApiGroupMapper.load", id);
    }

    @Override
    public List<ApiGroup> loadAll(int productId) {
        return sqlSessionTemplate.selectList("ApiGroupMapper.loadAll", productId);
    }

}
