package com.wph.api.admin.dao.impl;

import com.wph.api.admin.core.model.ApiProject;
import com.wph.api.admin.dao.lApiProjectDao;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuxueli on 17/3/30.
 */
@Repository
public class ApiProjectDaoImpl implements lApiProjectDao {

    @Resource
    public SqlSessionTemplate sqlSessionTemplate;

    @Override
    public int add(ApiProject apiProject) {
        return sqlSessionTemplate.insert("ApiProjectMapper.add", apiProject);
    }

    @Override
    public int update(ApiProject apiProject) {
        return sqlSessionTemplate.update("ApiProjectMapper.update", apiProject);
    }

    @Override
    public int delete(int id) {
        return sqlSessionTemplate.update("ApiProjectMapper.delete", id);
    }

    @Override
    public ApiProject load(int id) {
        return sqlSessionTemplate.selectOne("ApiProjectMapper.load", id);
    }

    @Override
    public List<ApiProject> pageList(int offset, int pagesize, String name) {
        Map<String, Object> params = new HashMap();
        params.put("offset", offset);
        params.put("pagesize", pagesize);
        params.put("name", name);

        return sqlSessionTemplate.selectList("ApiProjectMapper.pageList", params);
    }

    @Override
    public int pageListCount(int offset, int pagesize, String name) {
        Map<String, Object> params = new HashMap();
        params.put("offset", offset);
        params.put("pagesize", pagesize);
        params.put("name", name);

        return sqlSessionTemplate.selectOne("ApiProjectMapper.pageListCount", params);
    }

}
