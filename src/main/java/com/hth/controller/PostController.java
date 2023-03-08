package com.hth.controller;

import com.github.pagehelper.PageInfo;
import com.hth.entity.Msg;
import com.hth.entity.Post;
import com.hth.entity.PostDetail;
import com.hth.log.Logweb;
import com.hth.log.TestSuccess;
import com.hth.service.PostService;
import com.hth.service.TypeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/post")
@RestController
@TestSuccess
public class PostController {
    @Autowired
    PostService postService;
    @Autowired
    TypeService typeService;

    //添加文章
    @TestSuccess
    @ApiOperation(value = "添加文章")
    @PostMapping("/add")
    public Msg add(@RequestBody Post post){
        postService.addPost(post);
        return Msg.success("添加成功").add("id",post.getId());
    }

    //分页查出所有的文章
    @TestSuccess
    @GetMapping("/findAll")
    public Msg findAll(@RequestParam(name = "pagenum",defaultValue = "1")Integer page,
                       @RequestParam(name ="pagesize",defaultValue = "5")Integer size){
        PageInfo p =  postService.findAll(page, size);
        System.out.println(p);
        return Msg.success().add("postList",p);
    }

    //通过文章id查找所有文章内容
    @TestSuccess
    @GetMapping("/findPostById/{id}")
    public Msg findPostById(@PathVariable Integer id){
        PostDetail postDetail=postService.findPostById(id);
        if(postDetail==null)
            return Msg.fail("文章不存在或删除");
        return Msg.success().add("data",postDetail);
    }
    //修改的时候查找文章，跟上面差不多
    @GetMapping("/findPostByIds/{id}")
    public Msg findPostByIds(@PathVariable Integer id){
        PostDetail postDetail=postService.findPostByIds(id);
        return Msg.success().add("data",postDetail);
    }
    //查找一个类型的文章
    @GetMapping("/findType/{name}")
    @TestSuccess
    public Msg findTypeById(@PathVariable String name,
                            @RequestParam(name = "pagenum",defaultValue = "1")Integer page,
                            @RequestParam(name ="pagesize",defaultValue = "5")Integer size){
        Integer id=typeService.findTypeIdByName(name);
        if(id==null) return Msg.fail("不存在这个类型");
        return Msg.success().add("postList",postService.findTypeById(id,page, size));
    }
    //修改文章
    @PutMapping("/update")
    @TestSuccess
    public Msg update(@RequestBody Post post){
        postService.update(post);
        return Msg.success("修改成功");
    }
}
