package com.yoogurt.taxi.licences.controller;

import com.yoogurt.taxi.licences.common.helper.RedisHelper;
import com.yoogurt.taxi.licences.common.helper.ServletHelper;
import com.yoogurt.taxi.licences.common.helper.TokenHelper;
import com.yoogurt.taxi.licences.common.utils.CacheKey;
import com.yoogurt.taxi.licences.dal.enums.StatusCode;
import com.yoogurt.taxi.licences.dal.model.ResponseObj;
import com.yoogurt.taxi.licences.dal.model.SessionUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * 公共Controller，所有Controller继承于此
 *
 * @Author Eric Lau
 * @Date 2017/8/28.
 */
@Slf4j
@ControllerAdvice
public class BaseController {

    @Autowired
    private TokenHelper tokenHelper;

    @Autowired
    private RedisHelper redisHelper;

    /**
     * 从请求中获取UserID
     *
     * @return userId
     */
    public String getUserId() {
        return tokenHelper.getUserId(ServletHelper.getRequest());
    }

    /**
     * 从请求中获取userName
     *
     * @return username
     */
    public String getUserName() {
        return tokenHelper.getUserName(ServletHelper.getRequest());
    }

    /**
     * 从请求中获取用户类型
     *
     * @return 用户类型
     */
    public Integer getUserType() {
        HttpServletRequest request = ServletHelper.getRequest();
        return tokenHelper.getUserType(request);
    }

    /**
     * 从缓存中获取{@link SessionUser} SessionUser
     *
     * @return SessionUser
     */
    public SessionUser getUser() {
        String userId = getUserId();
        if (StringUtils.isBlank(userId)) {
            return null;
        }
        Object obj = redisHelper.getObject(CacheKey.SESSION_USER_KEY + userId);
        return obj != null ? (SessionUser) obj : null;
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseObj exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception {
        Map<String, Object> extras = new HashMap<String, Object>(4) {{
            put("error", e.toString());
            put("uri", request.getRequestURI());
            put("params", request.getParameterMap());
            put("userName", getUserName());
        }};
        log.error("error: " + request.getRequestURI());
        log.error("捕获到异常：", e);
        return ResponseObj.fail(StatusCode.SYS_ERROR, StatusCode.SYS_ERROR.getDetail(), extras);
    }
}
