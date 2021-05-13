package com.tjrac.wbcost.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class Goods {
    /**
     * id
     */
    private int id;
    /**
     * 商品名
     */
    private String itemName;
    /**
     * 商品描述
     */
    private String itemDescription;
    /**
     * 价格
     */
    private Double price;
    /*数量*/
    @TableField(exist = false)
    private Integer num;
    /*总金额*/
    @TableField(exist = false)
    private Double totalprice;
    /**
     * 状态 0可用 1已下架
     */
    private Integer status;
    /**
     * 图片地址
     */
    private String picUrl = "https://i.loli.net/2020/03/19/2SmpCHVYr4jownB.png";
}
