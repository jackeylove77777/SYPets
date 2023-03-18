package com.hth.controller;





import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hth.entity.Advertisement;
import com.hth.entity.Msg;
import com.hth.entity.Stray;
import com.hth.log.TestSuccess;
import com.hth.service.AdvertisementService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
                       @RequestParam(name ="pagesize",defaultValue = "5")Integer size){

        Page<Advertisement> strayPage = new Page<>(page, size);
        Page<Advertisement> page1 = advertisementService.page(strayPage);

        return Msg.success().add("ads",page1);
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

