package com.wph.api.admin.dao;


import com.wph.api.admin.core.model.ApiProject;

import java.util.List;

/**
 * Created by xuxueli on 17/3/30.
 */
public interface lApiProjectDao {

    public int add(ApiProject apiProject);
    public int update(ApiProject apiProject);
    public int delete(int id);

    public ApiProject load(int id);
    public List<ApiProject> pageList(int offset, int pagesize, String name);
    public int pageListCount(int offset, int pagesize, String name);

}
