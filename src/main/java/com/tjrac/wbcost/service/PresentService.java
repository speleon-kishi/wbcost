package com.tjrac.wbcost.service;

import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tjrac.wbcost.entity.Present;

import java.util.List;

public interface PresentService extends IService<Present> {
    /**
     * 查询所有礼品并排序
     * @return
     */
    List<Present> listPresent();

    /**
     * 编辑礼品
     * @param Present
     * @return
     */
    R editPresent(Present Present);

    /**
     * 删除礼品
     * @param Present
     * @return
     */
    R deletePresent(Present Present);

    /**
     * 添加礼品
     * @param Present
     * @return
     */
    R addPresent(Present Present);

    /**
     * 更改礼品上下架状态
     * @param Present
     * @return
     */
    R changePresentStatus(Present Present);
}
