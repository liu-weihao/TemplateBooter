package com.yoogurt.taxi.licences.service;

import com.yoogurt.taxi.licences.dal.beans.UserRoleInfo;
import com.yoogurt.taxi.licences.dal.model.ResponseObj;

import java.util.List;

public interface UserRoleService {

    UserRoleInfo getUserRoleInfo(String userId, Long roleId);

    ResponseObj saveUserRoleInfo(String userId, List<Long> roleIdList);

    ResponseObj removeUserRole(Long id);

    List<UserRoleInfo> getUserRoleInfo(Long roleId);
}
