package com.yoogurt.taxi.licences.common.shiro;

import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * Description:
 * 基于shiro的用户鉴权凭证
 *
 * @Author Eric Lau
 * @Date 2017/9/5.
 */
@Setter
@Getter
public class UserAuthenticationToken extends UsernamePasswordToken {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 登录成功后，获取到的授权码。
     */
    private String grantCode;

    /**
     * 系统颁发的token
     */
    private String token;

    /**
     * 由于服务重启后，导致
     * <pre>Subject.isAuthenticated()</pre>
     * 返回false，
     * 会触发executeLogin操作，重新调用
     * <pre>Subject.login(token)</pre>进行登录，
     * 传入的token中，loginAgain赋值为true。
     */
    private boolean loginAgain = false;

    /**
     * 用户类型
     */
    private Integer userType;

    public UserAuthenticationToken() {
    }

    public UserAuthenticationToken(String userId, String username) {
        this.userId = userId;
        super.setUsername(username);
    }

    /**
     * Constructs a new UsernamePasswordToken encapsulating the username and password submitted
     * during an authentication attempt, with a <tt>null</tt> {@link #getHost() host} and a
     * <tt>rememberMe</tt> default of <tt>false</tt>.
     *
     * @param username the username submitted for authentication
     * @param password the password character array submitted for authentication
     */
    public UserAuthenticationToken(String username, char[] password) {
        super(username, password);
    }

}
