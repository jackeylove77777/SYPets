package com.hth.controller;

import com.hth.log.TestSuccess;
import com.hth.upload.QiniuService;
import com.qiniu.http.Response;
import com.hth.entity.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 文件上传的相关接口
 */
@RestController
@RequestMapping("/qiniu")
public class QiniuController {

    @Autowired
    private QiniuService qiniuService;

    /**
     * 以流的形式上传图片
     *
     * @param file
     * @return 返回访问路径
     * @throws IOException
     */
    @PostMapping("/upload")
    @TestSuccess
    public Msg uploadFile(@RequestParam(value = "file") MultipartFile file) throws IOException {
        String s = qiniuService.uploadFile(file.getInputStream());
        return Msg.success().add("key",s);
    }

    //上传头像成功
    @PostMapping("/upAvatar")
    @TestSuccess
    public Msg upAvatar(@RequestParam(value = "file") MultipartFile file)throws IOException{
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        if(suffix.equals("JPG")||suffix.equals("jpg")){
            String s = qiniuService.upAvatar(file.getInputStream());
            return Msg.success().add("fileName",s);
        }
        return Msg.fail("只支持jpg格式的文件");
    }


    /**
     * 删除文件
     *
     * @param key
     * @return
     * @throws IOException
     */
    @GetMapping("delete/{key}")
    @TestSuccess
    public Response deleteFile(@PathVariable String key) throws IOException {
        System.out.println(key);
        return qiniuService.delete(key);
    }
}