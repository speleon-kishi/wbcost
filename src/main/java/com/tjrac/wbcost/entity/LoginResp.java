package com.tjrac.wbcost.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResp {
    private String statusCode;
    private Integer statusCodeValue;
    private String x_auth_token;
    private Integer permission;
    private SysUser sysUser;
}
