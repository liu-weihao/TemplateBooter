package com.yoogurt.taxi.licences.service;


import com.yoogurt.taxi.licences.dal.model.AuthorityModel;
import com.yoogurt.taxi.licences.dal.model.ResponseObj;

import java.util.List;

public interface RoleAuthorityService {

    List<AuthorityModel> getAuthoritiesByRoleId(Long roleId);

    List<AuthorityModel> getAuthoritiesByUserId(String userId);

    ResponseObj saveRoleAuthorityInfo(Long roleId, List<Long> authorityIdList);

    ResponseObj removeRoleAuthority(Long id);
}
