package com.wph.api.admin.dao;


import com.wph.api.admin.core.model.ApiTestHistory;

import java.util.List;

/**
 * Created by xuxueli on 2017-04-04 18:40:11
 */
public interface lApiTestHistoryDao {

    public int add(ApiTestHistory apiTestHistory);
    public int update(ApiTestHistory apiTestHistory);
    public int delete(int id);

    public ApiTestHistory load(int id);
    public List<ApiTestHistory> loadByDocumentId(int documentId);
}
