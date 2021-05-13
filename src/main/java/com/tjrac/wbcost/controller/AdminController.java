package com.tjrac.wbcost.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.tjrac.wbcost.billgen.DocumentDefine;
import com.tjrac.wbcost.entity.Goods;
import com.tjrac.wbcost.entity.Present;
import com.tjrac.wbcost.entity.SysUser;
import com.tjrac.wbcost.service.GoodsService;
import com.tjrac.wbcost.service.PresentService;
import com.tjrac.wbcost.service.SysUserService;
import com.tjrac.wbcost.utils.PasswordUtil;
import com.tjrac.wbcost.utils.XmlToDocx;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private PresentService presentService;

    /**
     * 上机接口
     *
     * @param req
     * @return
     */
    @PostMapping("/registerUser")
    @Transactional(rollbackFor = Exception.class)
    public R registerUser(@RequestBody RegisterUserReq req) {
        SysUser sysUser = new SysUser();
        sysUser.setUsername(req.getIdNum());
        try {
            //先判断是否已经有此用户
            SysUser tmpUser = sysUserService.getOne(new QueryWrapper<SysUser>()
                    .eq("username", req.getIdNum())
                    .eq("status", 0)
            );
            if (tmpUser != null) {
                //已有用户
                if (req.getPayAmount() != null && req.getPayAmount() != 0) {
                    //进行了充值
                    tmpUser.setBalance(req.getPayAmount() + tmpUser.getBalance());
                }
                //设置最后登录时间
                tmpUser.setLastlogin(new Date());
                //设置上机状态
                tmpUser.setOnline(1);
                sysUserService.updateById(tmpUser);
            } else {
                //无此用户则代为注册
                sysUser.setPermission(2);
                sysUser.setPassword(req.getIdNum());
                sysUser.setLastlogin(new Date());
                sysUser.setTrueName(req.getTrueName());
                sysUser.setOnline(1);
                //如果进行了充值
                if (req.getPayAmount() != null) {
                    sysUser.setBalance(req.getPayAmount());
                }
                sysUserService.registUser(sysUser);
            }
            return R.ok("成功");
        } catch (Exception e) {
            return R.failed("失败");
        }
    }

    /**
     * 列出在线用户列表
     *
     * @return
     */
    @GetMapping("/listUsers")
    public R listUsers() {
        try {
            return R.ok(sysUserService.list(new QueryWrapper<SysUser>()
                    .eq("status", 0)
                    .eq("online", 1)));
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed("失败");
        }
    }

    /**
     * 结账下机
     */
    @PostMapping("/payAction")
    @Transactional(rollbackFor = Exception.class)
    public R payAction(@RequestBody PayActionReq payActionReq) {
        SysUser sysUser = new SysUser();
        try {
            BeanUtils.copyProperties(payActionReq, sysUser);
            //修改储值卡余额
            if (payActionReq.getAfterPayBalance() < 0) {
                sysUser.setBalance((double) 0);
            } else {
                sysUser.setBalance(payActionReq.getAfterPayBalance());
            }
            //积分增加
            sysUser.setUserPoint(sysUser.getUserPoint() + payActionReq.getCosts());
            //购物花费清零
            sysUser.setBuyGoodsCost(0.0);
            //下线
            sysUser.setOnline(0);
            sysUserService.updateById(sysUser);
            return R.ok("成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed("失败");
        }
    }

    //商品操作

    /**
     * 添加商品接口
     *
     * @param goods
     * @return
     */
    @PostMapping("/addGoods")
    public R addGoods(@RequestBody Goods goods) {
        return goodsService.addGoods(goods);
    }

    /**
     * 商品上下架接口
     *
     * @param goods
     * @return
     */
    @PostMapping("/changeGoodsStatus")
    public R changeGoodsStatus(@RequestBody Goods goods) {
        return goodsService.changeGoodsStatus(goods);
    }

    /**
     * 商品删除接口
     *
     * @param goods
     * @return
     */
    @PostMapping("/deleteGoods")
    public R deleteGoods(@RequestBody Goods goods) {
        return goodsService.deleteGoods(goods);
    }

    /**
     * 商品信息修改接口
     *
     * @param goods
     * @return
     */
    @PostMapping("/editGoods")
    public R editGoods(@RequestBody Goods goods) {
        return goodsService.editGoods(goods);
    }

    //礼品操作

    /**
     * 添加礼品接口
     *
     * @param present
     * @return
     */
    @PostMapping("/addPresent")
    public R addPresent(@RequestBody Present present) {
        return presentService.addPresent(present);
    }

    /**
     * 礼品上下架接口
     *
     * @param present
     * @return
     */
    @PostMapping("/changePresentStatus")
    public R changePresentStatus(@RequestBody Present present) {
        return presentService.changePresentStatus(present);
    }

    /**
     * 礼品删除接口
     *
     * @param present
     * @return
     */
    @PostMapping("/deletePresent")
    public R deletePresent(@RequestBody Present present) {
        return presentService.deletePresent(present);
    }

    /**
     * 礼品信息修改接口
     *
     * @param present
     * @return
     */
    @PostMapping("/editPresent")
    public R editPresent(@RequestBody Present present) {
        return presentService.editPresent(present);
    }

    /**
     * 查询用户列表
     */
    @GetMapping("/getAllUsers")
    public R getAllUsers() {
        return R.ok(sysUserService.list());
    }

    /**
     * 修改密码
     */
    @PostMapping("/doChangePwd")
    @Transactional(rollbackFor = Exception.class)
    public R doChangePwd(@RequestBody ChangePasswordReq req) {
        SysUser sysUser = sysUserService.getById(req.getId());
        sysUser.setPassword(new PasswordUtil(req.getPassword()).getEncryptedPwd());
        sysUserService.updateById(sysUser);
        return R.ok("修改成功！");
    }

    /**
     * 删除用户
     */
    @PostMapping("/del")
    @Transactional(rollbackFor = Exception.class)
    public R del(Integer id) {
        try {
            boolean b = false;
            SysUser user = sysUserService.getById(id);
            if (!Objects.equals(user.getOnline(),1)){
                 b = sysUserService.removeById(id);
            }
            if (b){
                return R.ok("删除成功！");
            }else {
                return R.failed("删除失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed("删除失败请联系技术人员！原因："+e.getMessage());
        }

    }
}

@Data
class RegisterUserReq {
    //用户名
    private String idNum;
    //预付金额
    private Double payAmount;
    //真实姓名
    private String trueName;
}

@Data
class ChangePasswordReq {
    private int id;
    private String password;
}

@EqualsAndHashCode(callSuper = true)
@Data
class PayActionReq extends SysUser {
    //储值卡结账后余额
    private Double afterPayBalance;
    //新增积分
    private Integer costs;
}


