package com.yoogurt.taxi.licences.service.impl;

import com.yoogurt.taxi.licences.common.helper.RedisHelper;
import com.yoogurt.taxi.licences.common.helper.TokenHelper;
import com.yoogurt.taxi.licences.common.shiro.UserAuthenticationToken;
import com.yoogurt.taxi.licences.common.utils.CacheKey;
import com.yoogurt.taxi.licences.dal.beans.UserInfo;
import com.yoogurt.taxi.licences.dal.mapper.UserInfoMapper;
import com.yoogurt.taxi.licences.dal.model.SessionUser;
import com.yoogurt.taxi.licences.form.LoginForm;
import com.yoogurt.taxi.licences.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private RedisHelper redisHelper;

    @Autowired
    private TokenHelper tokenHelper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public SessionUser login(LoginForm form) {
        UserInfo user = getUserByUsername(form.getUsername());
        if (user == null) return null;
        // 密码不匹配
        if (!form.getPassword().equals(user.getLoginPassword())) {
            return null;
        }
        return generateSessionUser(user);
    }

    @Override
    public List<UserInfo> getUserList(String username, Integer type, Integer status) {
        Example example = new Example(UserInfo.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(username)) {
            criteria.andEqualTo("username", username);
        }
        if (type != null) {
            criteria.andEqualTo("type", type);
        }
        if (status != null) {
            criteria.andEqualTo("status", status);
        }
        return userInfoMapper.selectByExample(example);
    }

    @Override
    public UserInfo getUserByUsername(String username) {
        Example example = new Example(UserInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", username);
        List<UserInfo> list = userInfoMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 生成SessionUser，并缓存之
     *
     * @param userInfo 登录成功的用户信息
     * @return SessionUser
     */
    private SessionUser generateSessionUser(UserInfo userInfo) {
        String userId = userInfo.getUserId();
        String username = userInfo.getUsername();
        String authToken = tokenHelper.createToken(userId, username);
        SessionUser sessionUser = new SessionUser(userId, username);
        sessionUser.setStatus(userInfo.getStatus());
        sessionUser.setType(userInfo.getType());
        sessionUser.setName(userInfo.getName());
        sessionUser.setToken(authToken);
        //缓存SessionUser，不需要设置过期时间，以JWT的过期时间为准
        redisHelper.setObject(CacheKey.SESSION_USER_KEY + userId, sessionUser);
        doAuth(userId, username, userInfo.getType(), authToken);
        return sessionUser;
    }

    private boolean doAuth(String userId, String username, Integer userType, String authToken) {
        try {
            //shiro认证
            UserAuthenticationToken token = new UserAuthenticationToken();
            token.setUserId(userId);
            token.setUsername(username);
            token.setUserType(userType);
            token.setToken(authToken);
            token.setRememberMe(true);
            token.setLoginAgain(false);

            //shiro登录
            Subject subject = SecurityUtils.getSubject();
            subject.login(token);
            return true;
        } catch (Exception e) {
            log.error("授权失败, {}", e);
            return false;
        }
    }
}
