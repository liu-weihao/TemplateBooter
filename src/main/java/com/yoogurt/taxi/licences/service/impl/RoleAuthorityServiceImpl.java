package com.yoogurt.taxi.licences.service.impl;

import com.yoogurt.taxi.licences.common.helper.RedisHelper;
import com.yoogurt.taxi.licences.common.utils.CacheKey;
import com.yoogurt.taxi.licences.dal.beans.RoleAuthorityInfo;
import com.yoogurt.taxi.licences.dal.beans.UserRoleInfo;
import com.yoogurt.taxi.licences.dal.mapper.RoleAuthorityInfoMapper;
import com.yoogurt.taxi.licences.dal.model.AuthorityModel;
import com.yoogurt.taxi.licences.dal.model.ResponseObj;
import com.yoogurt.taxi.licences.service.RoleAuthorityService;
import com.yoogurt.taxi.licences.service.UserRoleService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleAuthorityServiceImpl implements RoleAuthorityService {

    @Autowired
    private RoleAuthorityInfoMapper roleAuthorityMapper;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RedisHelper redisHelper;

    @Override
    public List<AuthorityModel> getAuthoritiesByRoleId(Long roleId) {

        return roleAuthorityMapper.getAuthoritiesByRoleId(roleId);
    }

    @Override
    public List<AuthorityModel> getAuthoritiesByUserId(String userId) {
        List<AuthorityModel> authorities = new ArrayList<>();
        UserRoleInfo userRoleInfo = userRoleService.getUserRoleInfo(userId, null);
        if (userRoleInfo != null) {
            authorities = getAuthoritiesByRoleId(userRoleInfo.getRoleId());
        }
        return authorities;
    }

    @Override
    public ResponseObj saveRoleAuthorityInfo(Long roleId, List<Long> authorityIdList) {
        List<Long> authoritys = roleAuthorityMapper.getAuthorityIdListByRoleId(roleId);
        List<Long> retainList = new ArrayList<>();
        retainList.addAll(authorityIdList);
        retainList.retainAll(authoritys);
        List<Long> removeList = new ArrayList<>();
        removeList.addAll(authoritys);
        removeList.removeAll(retainList);
        List<Long> newList = new ArrayList<>();
        newList.addAll(authorityIdList);
        newList.removeAll(authoritys);
        for (Long authorityId : newList) {
            RoleAuthorityInfo roleAuthorityInfo = new RoleAuthorityInfo();
            roleAuthorityInfo.setAuthorityId(authorityId);
            roleAuthorityInfo.setRoleId(roleId);
            roleAuthorityMapper.insert(roleAuthorityInfo);
        }
        for (Long authorityId : removeList) {
            Example example = new Example(RoleAuthorityInfo.class);
            example.createCriteria()
                    .andEqualTo("roleId", roleId)
                    .andEqualTo("authorityId", authorityId);
            roleAuthorityMapper.deleteByExample(example);
        }
        List<UserRoleInfo> userRoleInfoList = userRoleService.getUserRoleInfo(roleId);
        List<String> userIds = userRoleInfoList.stream().map(UserRoleInfo::getUserId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(userIds)) {
            redisHelper.deleteMap(CacheKey.SHIRO_AUTHORITY_MAP, userIds.toArray(new String[userIds.size()]));
        }
        return ResponseObj.success();
    }

    @Override
    public ResponseObj removeRoleAuthority(Long id) {
        RoleAuthorityInfo roleAuthorityInfo = roleAuthorityMapper.selectByPrimaryKey(id);
        if (roleAuthorityInfo == null) {
            return ResponseObj.success();
        }
        roleAuthorityInfo.setIsDeleted(Boolean.TRUE);
        roleAuthorityMapper.updateByPrimaryKey(roleAuthorityInfo);
        return ResponseObj.success();
    }
}
