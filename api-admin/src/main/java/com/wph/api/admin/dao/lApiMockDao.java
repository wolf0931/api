package com.wph.api.admin.dao;


import com.wph.api.admin.core.model.ApiMock;

import java.util.List;

/**
 * Created by xuxueli on 17/4/1.
 */
public interface lApiMockDao {

    public int add(ApiMock apiMock);
    public int update(ApiMock apiMock);
    public int delete(int id);

    public List<ApiMock> loadAll(int documentId);
    public ApiMock load(int id);
    public ApiMock loadByUuid(String uuid);

}
