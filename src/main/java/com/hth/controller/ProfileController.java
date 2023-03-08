package com.hth.controller;

import com.hth.entity.Msg;
import com.hth.entity.User;
import com.hth.log.TestSuccess;
import com.hth.service.PostService;
import com.hth.service.ProfileService;
import com.hth.service.UserService;
import com.hth.util.JWTUtil;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/profile")
@RestController
@Slf4j
@TestSuccess
public class ProfileController {
    @Autowired
    ProfileService profileService;
    @Autowired
    UserService userService;
    @Autowired
    PostService postService;

    //个人主页的接口，返回这个用户的相关信息
    @GetMapping("/findUserProfile/{username}")
    @TestSuccess
    public Msg findUserProfile(@PathVariable String username){
        User user = userService.findUserDetails(username);
        if(user==null){
            return Msg.fail("用户名不存在");
        }
        boolean isfollow = profileService.isFollower(user.getId(), JWTUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal()));
        return Msg.success().add("user",user).add("isfollower",isfollow);
    }
    //关注
    @GetMapping("/follow/{userId}")
    @TestSuccess
    public Msg follow(@PathVariable Integer userId){
        boolean result = profileService.follow(userId);
        if(!result)return Msg.fail("关注失败");
        return Msg.success("关注成功");
    }
    //取消关注
    @GetMapping("/unFollow/{userId}")
    @TestSuccess
    public Msg unfollow(@PathVariable Integer userId){
        boolean result = profileService.unFollow(userId);
        if(!result)return Msg.fail("取消关注失败");
        return Msg.success("取消关注成功");
    }
    //个人主页，查出这个人所有的文章
    @GetMapping("/findPostById/{id}")
    @TestSuccess
    public Msg findPostByUserId(@PathVariable Integer id){
        return Msg.success().add("postList",postService.findPostByUserId(id));
    }
    //删除文章，就是删除自己主页的文章
    @PutMapping("/delete")
    @TestSuccess
    public Msg deletePost(@RequestBody Map map){
        boolean result = postService.delete((Integer) map.get("id"));//this.$http.put('/profile/delete/',{'id':id})
        if(result)return Msg.success("删除成功");
        return Msg.fail("删除失败");
    }

    //个人主页收藏的文章
    @GetMapping("/findCollect/{id}")
    @TestSuccess
    public Msg findCollect(@PathVariable Integer id){
        return Msg.success(profileService.findCollectById(id));
    }

    //个人主页找出用户粉丝
    @GetMapping("/findFollowers/{id}")
    @TestSuccess
    public Msg findFollowers(@PathVariable Integer id){
        List<User> followers = profileService.findFollowers(id);
        return Msg.success().add("followers",followers);
    }

    //个人主页,查出这个用户的关注
    @GetMapping("/findFollowings/{id}")
    @TestSuccess
    public Msg findFollowings(@PathVariable Integer id){
        List<User> followings = profileService.findFollowings(id);
        return Msg.success().add("followers",followings);
    }


}
