package com.tjrac.wbcost.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tjrac.wbcost.entity.Present;
import com.tjrac.wbcost.entity.SysUser;
import com.tjrac.wbcost.mapper.PresentMapper;
import com.tjrac.wbcost.service.PresentService;
import org.apache.ibatis.annotations.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class PresentServiceImpl extends ServiceImpl<PresentMapper, Present> implements PresentService {
    private Logger logger = LoggerFactory.getLogger(PresentServiceImpl.class);

    @Override
    public List<Present> listPresent() {
        QueryWrapper<Present> queryWrapper = new QueryWrapper<Present>()
                .orderByAsc("status");
        return this.list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R editPresent(Present Present) {
        try {
            this.updateById(Present);
            return R.ok("成功");
        }catch (Exception e){
            e.printStackTrace();
            return R.failed("失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R deletePresent(Present Present) {
        try {
            this.removeById(Present.getId());
            return R.ok("成功");
        }catch (Exception e){
            e.printStackTrace();
            return R.failed("失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R addPresent(Present Present) {
        try {
            this.save(Present);
            return R.ok("成功");
        }catch (Exception e){
            e.printStackTrace();
            return R.failed("失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R changePresentStatus(Present Present) {
        try {
            this.updateById(Present);
            return R.ok("成功");
        }catch (Exception e){
            e.printStackTrace();
            return R.failed("失败");
        }
    }
}
