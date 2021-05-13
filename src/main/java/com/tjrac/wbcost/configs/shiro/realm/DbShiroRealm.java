package com.tjrac.wbcost.configs.shiro.realm;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tjrac.wbcost.configs.Const;
import com.tjrac.wbcost.entity.SysUser;
import com.tjrac.wbcost.service.SysUserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

public class DbShiroRealm extends AuthorizingRealm {

    private Logger logger = LoggerFactory.getLogger(DbShiroRealm.class);

    @Autowired
    private SysUserService iUserService;

    public DbShiroRealm(SysUserService userService) {
        this.iUserService = userService;
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        // 散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashAlgorithmName(Md5Hash.ALGORITHM_NAME);
        logger.info("User {} login DBQuery", Md5Hash.ALGORITHM_NAME);
        //因为数据库中的密码做了散列，所以使用shiro的散列Matcher
        this.setCredentialsMatcher(hashedCredentialsMatcher);
    }

    /**
     * 找它的原因是这个方法返回true
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    /**
     * 这一步我们根据token给的用户名，去数据库查出加密过用户密码，然后把加密后的密码和盐值一起发给shiro，让它做比对
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken userpasswordToken = (UsernamePasswordToken) token;
        // 获取用户的输入的账号
        String username = userpasswordToken.getUsername();
        //查询用户信息
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        SysUser user = iUserService.getOne(queryWrapper);
        logger.info("User {} login DBQuery", user.getUsername());
        if (user == null) {
            throw new AuthenticationException("用户名或者密码错误");
        }
        return new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(Const.MD5_PWD_SALT), "dbRealm");
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        SysUser user = (SysUser) principals.getPrimaryPrincipal();
        String role = String.valueOf(user.getPermission());
        try {
            simpleAuthorizationInfo.addRoles(Collections.singleton(role));
        } catch (Exception roleException) {
            throw roleException;
        }
        return simpleAuthorizationInfo;
    }
}
