package com.yoogurt.taxi.licences.common.shiro;

import com.yoogurt.taxi.licences.common.shiro.cache.AuthorizationCacheManager;
import com.yoogurt.taxi.licences.common.shiro.filter.UrlPrivilegeCtrlFilter;
import com.yoogurt.taxi.licences.common.shiro.filter.UserTokenFilter;
import com.yoogurt.taxi.licences.common.utils.CacheKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * shiro配置中心
 * @author Eric Lau
 * @Date 2017/9/5.
 */
@Slf4j
@Configuration
public class ShiroConfig {

    /**
     * shiro拦截器总配置。
     * 内置的Filter如下所示：
     -----------------------------------------------------------------
     ||                anon AnonymousFilter                         ||
     ||                auth FormAuthenticationFilter                ||
     ||                authcBasic BasicHttpAuthenticationFilter     ||
     ||                logout LogoutFilter                          ||
     ||                noSessionCreation NoSessionCreationFilter    ||
     ||                perms PermissionsAuthorizationFilter         ||
     ||                port PortFilter                              ||
     ||                rest HttpMethodPermissionFilter              ||
     ||                roles RolesAuthorizationFilter               ||
     ||                ssl SslFilter                                ||
     ||                user UserFilter                              ||
     ================================================================
     * @param securityManager securityManager
     * @return shiroFilterFactoryBean
     */
    @Bean("shiroFilterFactoryBean")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(SecurityManager securityManager) {

        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(securityManager);
        bean.setLoginUrl("/login");
        Map<String, Filter> filterMap = new HashMap<>();
        //自定义Filter
        //上一个Filter返回true将会进入下一个Filter，返回false则调用链条中断
        filterMap.put("userTokenFilter", getUserTokenFilter());
        filterMap.put("urlPrivilegeFilter", getUrlPrivilegeCtrlFilter());
        bean.setFilters(filterMap);


        Map<String, String> chains = new HashMap<>();

        // anon: 允许匿名访问
        chains.put("/**/**.js", "anon");
        chains.put("/**/**.ico", "anon");
        chains.put("/**/**.woff", "anon");
        chains.put("/**/**.css", "anon");
        chains.put("/**/**.html", "anon");
        chains.put("/resource/**", "anon");
        chains.put("/img/**", "anon");
        chains.put("/static/**", "anon");
        chains.put("/favicon.ico", "anon");
        chains.put("/login", "anon");
        chains.put("/login.html", "anon");
        chains.put("/info", "anon");
        chains.put("/env", "anon");
        chains.put("/beans", "anon");
        chains.put("/restart", "anon");
        chains.put("/health", "anon");
        chains.put("/call", "anon");
        chains.put("/hystrix", "anon");
        chains.put("/hystrix.stream", "anon");
        chains.put("/metrics", "anon");
        chains.put("/dump", "anon");
        chains.put("/configprops", "anon");
        chains.put("/loggers", "anon");
        chains.put("/mappings", "anon");
        chains.put("/trace", "anon");
        chains.put("/actuator", "anon");
        chains.put("/autoconfig", "anon");
        chains.put("/readness", "anon");
        //token颁发接口
        chains.put("/token/**", "anon");
        //刷新token接口
        chains.put("/refresh", "anon");
        //匿名接口
        chains.put("/**/i/**", "anon");

        // noSessionCreation: 要求shiro不创建session
        chains.put("/**.html", "noSessionCreation");
        chains.put("/common/**", "noSessionCreation,userTokenFilter");

        //拦截后台的uri
        // 1. userTokenFilter：判断token是否存在，是否过期
        // 2. urlPrivilegeFilter：主要用于对所访问的url进行鉴权
        chains.put("/web/**", "userTokenFilter,urlPrivilegeFilter");


        // <!-- 过滤链定义，从上向下顺序执行，一般将 /** 放在最下边 -->
        chains.put("/**", "user");

        bean.setFilterChainDefinitionMap(chains);

        return bean;
    }

    /**
     * 构造一个SecurityManager
     * @return SecurityManager
     */
    @Bean("securityManager")
    public SecurityManager getSecurityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager(getShiroRealm());
        //cookie功能加持
        securityManager.setRememberMeManager(getRememberMeManager());
        return securityManager;
    }

    /**
     * 构造一个AuthorizingRealm用于鉴权和授权
     * @return {@link ShiroRealm} ShiroRealm
     */
    @Bean("shiroRealm")
    public AuthorizingRealm getShiroRealm() {
        AuthorizingRealm realm = new ShiroRealm();
        realm.setCachingEnabled(true);
        realm.setPermissionResolver(PathPermission::new);
        realm.setAuthorizationCachingEnabled(true);
        realm.setAuthorizationCacheName(CacheKey.SHIRO_AUTHORITY_MAP);
        realm.setCacheManager(getRedisCacheManager());
        return realm;
    }

    /**
     * Token过期验证过滤器
     * @return UserTokenFilter
     */
    @Bean(name = "userTokenFilter")
    public UserTokenFilter getUserTokenFilter() {
        return new UserTokenFilter();
    }

    /**
     * web端过滤器，主要对登录状态的判断
     * @return UrlPrivilegeCtrlFilter
     */
    @Bean(name = "urlPrivilegeCtrlFilter")
    public UrlPrivilegeCtrlFilter getUrlPrivilegeCtrlFilter() {
        UrlPrivilegeCtrlFilter ctrlFilter = new UrlPrivilegeCtrlFilter();
        ctrlFilter.setLoginUrl("/login");
        return ctrlFilter;
    }


    /**
     * 因为需要开启rememberMe功能，需要注入一个RememberMeManager，
     * 通常情况，使用基于Cookie的实现方式。
     * @return RememberMeManager
     */
    @Bean
    public RememberMeManager getRememberMeManager() {
        CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();
        rememberMeManager.setCipherKey(Base64.decode("4AvVhmFLUs0KTA3Kprsdag=="));
        SimpleCookie cookie = new SimpleCookie();
        // 7天后过期
        cookie.setMaxAge(604800);
        cookie.setHttpOnly(true);
        cookie.setName("yoogurt-taxi");
        rememberMeManager.setCookie(cookie);
        return rememberMeManager;
    }

    @Bean(name = "redisCacheManager")
    public AuthorizationCacheManager getRedisCacheManager() {
        return new AuthorizationCacheManager();
    }
}
