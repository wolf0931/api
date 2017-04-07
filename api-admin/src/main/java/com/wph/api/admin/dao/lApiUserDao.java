package com.wph.api.admin.dao;


import com.wph.api.admin.core.model.ApiUser;

import java.util.List;

/**
 * Created by xuxueli on 17/3/29.
 */
 public interface lApiUserDao {

     int add(ApiUser apiUser);

     int update(ApiUser apiUser);

     int delete(int id);

     ApiUser findByUserName(String userName);

     ApiUser findById(int id);

     List<ApiUser> loadAll();

}
