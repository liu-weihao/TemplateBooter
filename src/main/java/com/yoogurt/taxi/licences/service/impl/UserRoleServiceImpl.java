package com.yoogurt.taxi.licences.service.impl;

import com.yoogurt.taxi.licences.dal.beans.UserRoleInfo;
import com.yoogurt.taxi.licences.dal.mapper.UserRoleInfoMapper;
import com.yoogurt.taxi.licences.dal.model.ResponseObj;
import com.yoogurt.taxi.licences.service.UserRoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleInfoMapper mapper;

    @Override
    public UserRoleInfo getUserRoleInfo(String userId, Long roleId) {

        boolean b = StringUtils.isBlank(userId) && (roleId == null || roleId <= 0);
        if (b) {
            return null;
        }
        UserRoleInfo info = new UserRoleInfo();

        if (StringUtils.isNotBlank(userId)) {
            info.setUserId(userId);
        }
        if (roleId != null) {
            info.setRoleId(roleId);
        }
        return mapper.selectOne(info);
    }

    @Override
    public ResponseObj saveUserRoleInfo(String userId, List<Long> roleIdList) {
        for (Long roleId : roleIdList) {
            UserRoleInfo roleInfo = new UserRoleInfo();
            roleInfo.setRoleId(roleId);
            roleInfo.setUserId(userId);
            mapper.insert(roleInfo);
        }
        return ResponseObj.success();
    }

    @Override
    public ResponseObj removeUserRole(Long id) {
        UserRoleInfo userRoleInfo = mapper.selectByPrimaryKey(id);
        if (userRoleInfo == null) {
            return ResponseObj.success();
        }
        userRoleInfo.setIsDeleted(Boolean.TRUE);
        mapper.updateByPrimaryKey(userRoleInfo);
        return ResponseObj.success();
    }

    @Override
    public List<UserRoleInfo> getUserRoleInfo(Long roleId) {
        UserRoleInfo userRoleInfo = new UserRoleInfo();
        userRoleInfo.setRoleId(roleId);
        return mapper.select(userRoleInfo);
    }
}
