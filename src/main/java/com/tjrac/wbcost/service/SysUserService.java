package com.tjrac.wbcost.service;

import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tjrac.wbcost.entity.LoginResp;
import com.tjrac.wbcost.entity.SysUser;

public interface SysUserService extends IService<SysUser> {
    R registUser(SysUser sysUser);
    LoginResp login(SysUser sysUser);
    LoginResp loginByUser(SysUser sysUser);
}
