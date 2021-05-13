package com.tjrac.wbcost.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tjrac.wbcost.entity.Goods;
import com.tjrac.wbcost.mapper.GoodsMapper;
import com.tjrac.wbcost.service.GoodsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {
    private Logger logger = LoggerFactory.getLogger(GoodsServiceImpl.class);

    @Override
    public List<Goods> listGoods() {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<Goods>()
                .orderByAsc("status");
        List<Goods> goods =list(queryWrapper);
        for (Goods good:
             goods) {
            if (good.getNum()==null){
                good.setNum(0);
            }
            if (good.getTotalprice()==null){
                good.setTotalprice(0.0);
            }
        }
        return goods;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R editGoods(Goods goods) {
        try {
            this.updateById(goods);
            return R.ok("成功");
        }catch (Exception e){
            e.printStackTrace();
            return R.failed("失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R deleteGoods(Goods goods) {
        try {
            this.removeById(goods.getId());
            return R.ok("成功");
        }catch (Exception e){
            e.printStackTrace();
            return R.failed("失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R addGoods(Goods goods) {
        try {
            this.save(goods);
            return R.ok("成功");
        }catch (Exception e){
            e.printStackTrace();
            return R.failed("失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R changeGoodsStatus(Goods goods) {
        try {
            this.updateById(goods);
            return R.ok("成功");
        }catch (Exception e){
            e.printStackTrace();
            return R.failed("失败");
        }
    }
}
