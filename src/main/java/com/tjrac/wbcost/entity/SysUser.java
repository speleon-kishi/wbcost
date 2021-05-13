package com.tjrac.wbcost.entity;

import lombok.Data;

import java.util.Date;

@Data
public class SysUser {
    private int id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 状态 0可用 1禁用
     */
    private int status;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 0管理员 2普通用户（默认）
     */
    private int permission;
    /**
     * 最后登录时间
     */
    private Date lastlogin;
    /**
     * 登录令牌
     */
    private String token;
    /**
     * 余额
     */
    private Double balance;
    /**
     * 是否在线 0否 1是
     */
    private int online;
    /**
     * 用户积分
     */
    private int userPoint;
    /**
     * 购物花费
     */
    private Double buyGoodsCost;
    /**
     * 真实姓名
     */
    private String trueName;
}
