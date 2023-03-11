package com.hth.controller;





import com.hth.entity.Advertisement;
import com.hth.entity.Msg;
import com.hth.log.TestSuccess;
import com.hth.service.AdvertisementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
    public Msg add(@RequestBody Advertisement ad){
        System.out.println(ad);
        advertisementService.add(ad);
        return Msg.success();
    }
    @TestSuccess
    @GetMapping("/all")
    public Msg getAll(){
        List<Advertisement> all = advertisementService.getAll();
        return Msg.success().add("data",all);
    }



}

