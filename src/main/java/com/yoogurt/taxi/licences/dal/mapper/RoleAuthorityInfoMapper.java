package com.yoogurt.taxi.licences.dal.mapper;

import com.yoogurt.taxi.licences.dal.beans.RoleAuthorityInfo;
import com.yoogurt.taxi.licences.dal.model.AuthorityModel;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface RoleAuthorityInfoMapper extends Mapper<RoleAuthorityInfo> {

    List<AuthorityModel> getAuthoritiesByRoleId(Long roleId);

    List<Long> getAuthorityIdListByRoleId(Long roleId);
}