package com.tjrac.wbcost.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.tjrac.wbcost.entity.LoginResp;
import com.tjrac.wbcost.entity.SysUser;
import com.tjrac.wbcost.service.SysUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;

    @PostMapping("/doRegister")
    @Transactional(rollbackFor = Exception.class)
    public R doRegister(@RequestBody SysUser sysUser) {
        return sysUserService.registUser(sysUser);
    }

    @PostMapping("/doLogin")
    public LoginResp doLogin(@RequestBody SysUser sysUser) {
        return sysUserService.login(sysUser);
    }

    @PostMapping("/loginByUser")
    public LoginResp loginByUser(@RequestBody SysUser sysUser) {
        return sysUserService.loginByUser(sysUser);
    }

    @GetMapping(value = "/logout")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.getPrincipals() != null) {
            SysUser user = (SysUser) subject.getPrincipals().getPrimaryPrincipal();
            user.setToken("");
            sysUserService.updateById(user);
        }
        SecurityUtils.getSubject().logout();
        response.setHeader("x-auth-token", "");
        return ResponseEntity.ok().build();
    }
}
