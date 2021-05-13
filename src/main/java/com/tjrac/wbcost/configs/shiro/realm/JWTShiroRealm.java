package com.tjrac.wbcost.configs.shiro.realm;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tjrac.wbcost.configs.Const;
import com.tjrac.wbcost.configs.shiro.JWTCredentialsMatcher;
import com.tjrac.wbcost.configs.shiro.JWTToken;
import com.tjrac.wbcost.configs.shiro.JWTUtils;
import com.tjrac.wbcost.entity.SysUser;
import com.tjrac.wbcost.service.SysUserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 自定义身份认证
 * 基于HMAC（ 散列消息认证码）的控制域
 */

public class JWTShiroRealm extends AuthorizingRealm {
    private final Logger logger = LoggerFactory.getLogger(JWTShiroRealm.class);

    @Autowired
    protected SysUserService userService;

    public JWTShiroRealm(SysUserService userService) {
        this.userService = userService;
        this.setCredentialsMatcher(new JWTCredentialsMatcher());
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 认证信息.(身份验证) : Authentication 是用来验证用户身份
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        JWTToken jwtToken = (JWTToken) authcToken;
        String token = jwtToken.getToken();

        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", JWTUtils.getUsername(token));
        SysUser user = userService.getOne(queryWrapper);
        if (user == null) {
            throw new AuthenticationException("token失效，请重新登录");
        }
        return new SimpleAuthenticationInfo(user, Const.MD5_PWD_SALT, "jwtRealm");
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return new SimpleAuthorizationInfo();
    }
}
