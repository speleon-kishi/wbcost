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
import com.tjrac.wbcost.utils.XmlToDocx;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/public")
public class PublicController {
    @Autowired
    SysUserService sysUserService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private PresentService presentService;

    /**
     * 查询在线人数接口
     *
     * @return
     */
    @GetMapping("/listAllOnline")
    public R listAllOnline() {
        try {
            return R.ok(sysUserService.count(new QueryWrapper<SysUser>()
                    .eq("status", 0)
                    .eq("online", 1)
            ));
        } catch (Exception e) {
            return R.failed("失败");
        }
    }

    /**
     * 查询当前客户是否在线接口
     *
     * @return
     */
    @GetMapping("/selectIsOnline")
    public R selectIsOnline(@RequestParam String id) {
        try {
            Map<String, String> resMap = new HashMap<>();
            SysUser sysUser = sysUserService.getById(id);
            resMap.put("online", String.valueOf(sysUser.getOnline()));
            resMap.put("balance", String.valueOf(sysUser.getBalance()));
            resMap.put("point", String.valueOf(sysUser.getUserPoint()));
            return R.ok(resMap);
        } catch (Exception e) {
            return R.failed("失败");
        }
    }

    /**
     * 查询商品接口
     */
    @GetMapping("/listGoods")
    public R listGoods() {
        try {
            List<Goods> goods = goodsService.listGoods();
            return R.ok(goods);
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed("失败！");
        }
    }

    /**
     * 查询礼品接口
     */
    @GetMapping("/listPresent")
    public R listPresent() {
        try {
            List<Present> presents = presentService.listPresent();
            return R.ok(presents);
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed("失败！");
        }
    }

    /**
     * 兑换礼品接口
     */
    @PostMapping("/rewardItem")
    @Transactional(rollbackFor = Exception.class)
    public R rewardItem(@RequestBody RewardPresentReq rewardPresentReq) {
        SysUser sysUser = sysUserService.getById(rewardPresentReq.getUid());
        if (sysUser != null) {
            int currentPoint = sysUser.getUserPoint();
            int costPoint = rewardPresentReq.getPresentCosts();
            if (currentPoint - costPoint >= 0) {
                sysUser.setUserPoint(currentPoint - costPoint);
                sysUserService.updateById(sysUser);
                return R.ok("兑换成功！");
            } else return R.failed("积分不足，兑换失败！");
        } else return R.failed("兑换失败！");
    }

    /**
     * 购物接口
     */
    @PostMapping("/buyGoods")
    @Transactional(rollbackFor = Exception.class)
    public R buyGoods(@RequestBody BuyGoodsReq buyGoodsReq) {
        SysUser sysUser = sysUserService.getById(buyGoodsReq.getUid());
        if (sysUser != null) {
            sysUser.setBuyGoodsCost(sysUser.getBuyGoodsCost() + buyGoodsReq.getPrice());
            sysUserService.updateById(sysUser);
            return R.ok("购买成功！");
        } else return R.failed("购买失败！");
    }

    /**
     * 导出word账单
     *
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping("/exportBills")
    public String downloadFile(HttpServletResponse response, @ModelAttribute BillInfo billInfo) throws IOException {
        String[] tmpBaseInfo = new String[billInfo.getBaseInfo().length];
        for (int i = 0; i < tmpBaseInfo.length; i++) {
            tmpBaseInfo[i] = billInfo.getBaseInfo()[i].replaceAll("\"", "");
        }
        String[] tmpFoodInfo = new String[billInfo.getFoodInfo().length];
        for (int i = 0; i < tmpFoodInfo.length; i++) {
            tmpFoodInfo[i] = billInfo.getFoodInfo()[i].replaceAll("\"", "");
        }
        String xmls = DocumentDefine.generateWord(tmpBaseInfo, tmpFoodInfo);
        File temp = File.createTempFile("temp-" + UUID.randomUUID().toString().replaceAll("-", ""), ".xml");
        temp.deleteOnExit();
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(temp));
        bufferedWriter.write(xmls);
        bufferedWriter.close();
        XmlToDocx xmlToDocx = new XmlToDocx();
        File tempDocx = File.createTempFile("temp-" + UUID.randomUUID().toString(), ".docx");
        xmlToDocx.outDocx(temp, tempDocx.getAbsolutePath());
        String downloadFilePath = tempDocx.getPath();
        String fileName = tempDocx.getName();
        File file = new File(downloadFilePath);
        if (file.exists()) {
            response.setContentType("application/force-download");
            response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream outputStream = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    outputStream.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                return "下载成功！";
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return "下载失败！";
    }

    /**
     * 测试
     */
    @GetMapping("/testdoc")
    public void testdoc() throws Exception {

    }
}

@Data
class BillInfo {
    private String[] baseInfo;
    private String[] foodInfo;
}

@Data
class RewardPresentReq extends Present {
    private String uid;
}

@Data
class BuyGoodsReq extends Goods {
    private String uid;
}
