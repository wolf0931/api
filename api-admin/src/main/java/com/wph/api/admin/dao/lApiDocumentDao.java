package com.wph.api.admin.dao;


import com.wph.api.admin.core.model.ApiDocument;

import java.util.List;

/**
 * Created by xuxueli on 17/3/31.
 */
 public interface lApiDocumentDao {

     int add(ApiDocument apiDocument);
     int update(ApiDocument apiDocument);
     int delete(int id);

     ApiDocument load(int id);
     List<ApiDocument> loadAll(int productId, int groupId);

     List<ApiDocument> loadByGroupId(int id);
}
