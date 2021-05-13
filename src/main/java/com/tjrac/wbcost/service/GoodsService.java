package com.tjrac.wbcost.service;

import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tjrac.wbcost.entity.Goods;

import java.util.List;

public interface GoodsService extends IService<Goods> {
    /**
     * 查询所有商品并排序
     * @return
     */
    List<Goods> listGoods();

    /**
     * 编辑商品
     * @param goods
     * @return
     */
    R editGoods(Goods goods);

    /**
     * 删除商品
     * @param goods
     * @return
     */
    R deleteGoods(Goods goods);

    /**
     * 添加商品
     * @param goods
     * @return
     */
    R addGoods(Goods goods);

    /**
     * 更改商品上下架状态
     * @param goods
     * @return
     */
    R changeGoodsStatus(Goods goods);


}

