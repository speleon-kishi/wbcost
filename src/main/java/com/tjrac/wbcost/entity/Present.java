package com.tjrac.wbcost.entity;

import lombok.Data;

@Data
public class Present {
    private int id;
    /**
     * 礼品名
     */
    private String presentName;
    /**
     * 礼品描述
     */
    private String presentDescrip;
    /**
     * 所需积分
     */
    private int presentCosts;
    /**
     * 图片地址
     */
    private String picUrl = "https://i.loli.net/2020/03/19/2SmpCHVYr4jownB.png";
    /**
     * 状态 0可用 1禁用
     */
    private int status;
}
