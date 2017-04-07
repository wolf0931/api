package com.wph.api.admin.dao;


import com.wph.api.admin.core.model.ApiGroup;

import java.util.List;

/**
 * Created by xuxueli on 17/3/30.
 */
public interface lApiGroupDao {

    public int add(ApiGroup apiGroup);
    public int update(ApiGroup apiGroup);
    public int delete(int id);

    public ApiGroup load(int id);
    public List<ApiGroup> loadAll(int productId);

}
