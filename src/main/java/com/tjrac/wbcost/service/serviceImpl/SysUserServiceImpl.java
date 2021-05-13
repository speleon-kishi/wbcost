package com.tjrac.wbcost.service.serviceImpl;

import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tjrac.wbcost.configs.Const;
import com.tjrac.wbcost.configs.shiro.JWTUtils;
import com.tjrac.wbcost.entity.LoginResp;
import com.tjrac.wbcost.entity.SysUser;
import com.tjrac.wbcost.mapper.SysUserMapper;
import com.tjrac.wbcost.service.SysUserService;
import com.tjrac.wbcost.utils.PasswordUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    private Logger logger = LoggerFactory.getLogger(SysUserServiceImpl.class);
    @Override
    public R registUser(SysUser sysUser) {
        if (sysUser.getUsername() == null || sysUser.getUsername().trim().equals("")) {
            return R.failed("用户名不能为空！");
        } else if (sysUser.getPassword() == null || sysUser.getPassword().trim().equals("")) {
            return R.failed("密码不能为空！");
        } else {
            sysUser.setPassword(new PasswordUtil(sysUser.getPassword()).getEncryptedPwd());
            try {
                sysUser.setCreateTime(new Date());
                this.save(sysUser);
                return R.ok(sysUser);
            } catch (Exception e) {
                e.printStackTrace();
                if (e.toString().contains("Duplicate entry") && e.toString().contains("for key 'username'")) {
                    return R.failed("用户名已经存在！");
                } else return R.failed("未知错误！");
            }
        }
    }

    @Override
    public LoginResp login(SysUser sysUser) {
        if (sysUser.getUsername() == null || sysUser.getUsername().trim().equals("")
                || sysUser.getPassword() == null || sysUser.getPassword().trim().equals("")) {
            return null;
        } else {
            Subject subject = SecurityUtils.getSubject();
            try {
                UsernamePasswordToken token = new UsernamePasswordToken(sysUser.getUsername(), sysUser.getPassword());
                subject.login(token);
                SysUser user = (SysUser) subject.getPrincipal();
                String newToken = JWTUtils.sign(user.getUsername(), Const.MD5_PWD_SALT, 3600);
                user.setToken(newToken);
                user.setLastlogin(new Date());
                this.updateById(user);
                return new LoginResp("OK", 200, newToken, user.getPermission(),user);
            } catch (AuthenticationException e) {
                logger.error("User {} login fail, Reason:{}", sysUser.getUsername(), e.getMessage());
                return new LoginResp("UNAUTHORIZED",401,null,null,null);
            } catch (Exception e) {
                return new LoginResp("INTERNAL_SERVER_ERROR",500,null,null,null);
            }
        }
    }

    @Override
    public LoginResp loginByUser(SysUser sysUser) {
        if (sysUser.getUsername() == null || sysUser.getUsername().trim().equals("")
                || sysUser.getPassword() == null || sysUser.getPassword().trim().equals("")) {
            return null;
        } else {
            Subject subject = SecurityUtils.getSubject();
            try {
                UsernamePasswordToken token = new UsernamePasswordToken(sysUser.getUsername(), sysUser.getPassword());
                subject.login(token);
                SysUser user = (SysUser) subject.getPrincipal();
                String newToken = JWTUtils.sign(user.getUsername(), Const.MD5_PWD_SALT, 3600);
                user.setToken(newToken);
                this.updateById(user);
                return new LoginResp("OK", 200, newToken, user.getPermission(),user);
            } catch (AuthenticationException e) {
                logger.error("User {} login fail, Reason:{}", sysUser.getUsername(), e.getMessage());
                return new LoginResp("UNAUTHORIZED",401,null,null,null);
            } catch (Exception e) {
                return new LoginResp("INTERNAL_SERVER_ERROR",500,null,null,null);
            }
        }
    }
}
