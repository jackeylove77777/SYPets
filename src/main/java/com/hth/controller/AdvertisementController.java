package com.hth.controller;





import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hth.dto.FindpetDetail;
import com.hth.entity.Advertisement;
import com.hth.entity.Findpet;
import com.hth.entity.Msg;
import com.hth.entity.Stray;
import com.hth.log.TestSuccess;
import com.hth.service.AdvertisementService;
import io.swagger.models.auth.In;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 友链(Advertisement)表控制层
 *
 * @author makejava
 * @since 2023-03-08 11:07:19
 */
@RestController
@RequestMapping("ad")
@TestSuccess
public class AdvertisementController{
    @Autowired
    AdvertisementService advertisementService;

    @DeleteMapping("/{id}")
    @RequiresRoles("admin")
    public Msg delete(@PathVariable Integer id){
        advertisementService.removeById(id);
        return Msg.success("删除成功");
    }

    @TestSuccess
    @PostMapping
    @RequiresRoles("admin")
    public Msg add(@RequestBody Advertisement ad){
        System.out.println(ad);
        advertisementService.add(ad);
        return Msg.success();
    }
    @TestSuccess
    @GetMapping("/all")
    @RequiresRoles("admin")
    public Msg getPage(@RequestParam(name = "pagenum",defaultValue = "1")Integer page,
                       @RequestParam(name ="pagesize",defaultValue = "5")Integer size,
                       @RequestParam(name = "query",defaultValue = "")String query){

        LambdaQueryWrapper<Advertisement> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Advertisement::getDescription,query);
        Page<Advertisement> strayPage = new Page<>(page, size);
        Page<Advertisement> page1 = advertisementService.getBaseMapper().selectPage(strayPage,queryWrapper);

        return Msg.success().add("list",page1.getRecords()).add("total",page1.getTotal());
    }
    //随机获取一个广告
    @GetMapping("/random")
    public Msg getRandom(){
        List<Advertisement> all = advertisementService.getAll();
        Random random = new Random();
        int i = random.nextInt(all.size());
        return Msg.success().add("ad",all.get(i));
    }

}

