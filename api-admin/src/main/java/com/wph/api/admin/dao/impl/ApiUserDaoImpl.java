package com.wph.api.admin.dao.impl;

import com.wph.api.admin.core.model.ApiUser;
import com.wph.api.admin.dao.lApiUserDao;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by xuxueli on 17/3/29.
 */
@Repository
public class ApiUserDaoImpl implements lApiUserDao {

    @Resource
    public SqlSessionTemplate sqlSessionTemplate;

    @Override
    public int add(ApiUser apiUser) {
        return sqlSessionTemplate.insert("lApiUserMapper.add", apiUser);
    }

    @Override
    public int update(ApiUser apiUser) {
        return sqlSessionTemplate.update("lApiUserMapper.update", apiUser);
    }

    @Override
    public int delete(int id) {
        return sqlSessionTemplate.update("lApiUserMapper.delete", id);
    }

    @Override
    public ApiUser findByUserName(String userName) {
        return sqlSessionTemplate.selectOne("lApiUserMapper.findByUserName", userName);
    }

    @Override
    public ApiUser findById(int id) {
        return sqlSessionTemplate.selectOne("lApiUserMapper.findById", id);
    }

    @Override
    public List<ApiUser> loadAll() {
        return sqlSessionTemplate.selectList("lApiUserMapper.loadAll");
    }
}
