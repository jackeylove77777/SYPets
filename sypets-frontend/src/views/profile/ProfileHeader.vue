<template>
  <div class="profile">
<!--    自己主页-->
    <div v-if="IsMe">
      <div class="user-avatar" style="margin-bottom: 0">
        <div><div class="block" ><el-avatar shape="square" :size="120"  :src="user.avatar"></el-avatar></div></div>
        <div class="user-info">
          <span class="mx-lg-4 mx-md-2 username">{{user.username}}</span>
          <span class="mx-lg-4 mx-md-2 " style="color: #8A919F;">{{'邮箱: '+user.email}}</span>
          <span class="mx-lg-4 mx-md-2 " style="color: #8A919F;">简介:{{user.privateInfo}}</span>
        </div>
      </div>
      <el-row>
        <el-col class="update" :span="4"><span type="success" @click="editProfile"><i class="el-icon-edit">编辑个人资料</i></span></el-col>
        <el-col :span="4"  v-show="user.username==='admin'"><span class="admin-text" @click="toAdmin">进入后台</span></el-col>
      </el-row>
    </div>
<!--    他人主页-->
    <div v-else>
      <div class="user-avatar" style="margin-bottom: 0">
        <div><div class="block" ><el-avatar shape="square" :size="120"  :src="user.avatar"></el-avatar></div></div>
        <div class="user-info">
          <span class="mx-lg-4 mx-md-2 username">{{user.username}}</span>
          <span class="mx-lg-4 mx-md-2 " style="color: #8A919F;">{{'邮箱: '+user.email}}</span>
          <span class="mx-lg-4 mx-md-2 " style="color: #8A919F;">简介:{{user.privateInfo}}</span>
        </div>
      </div>
      <el-row>
        <el-col class="update" :span="4">
          <el-button v-if="isfollow" @click="unFollow" type="primary">
            <i class="el-icon-star-off" >取消关注</i>
          </el-button>
          <el-button v-else type="primary" @click="follow" >
            <i class="el-icon-star-off" >关注</i>
          </el-button>
        </el-col>
        <el-col :span="3">
          <el-button type="primary" @click="privateChat" >
            <i class="el-icon-chat-dot-round" >私信</i>
          </el-button>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
export default {
  name: "ProfileHeader",
  props:{
    IsMe:false,
    user:{
      profile:{}
    },
    isfollow:false
  },
  computed:{
    sex(){
      if(this.user.sex===0)
        return '未知'
      else if(this.user.sex===1)
        return '男'
      return '女'
    },
  },
  methods:{
    privateChat(){
      let args = {
        toId:this.user.id,
        toUsername:this.user.username
      }
      this.$bus.$emit("sendMessage",args)
    },
    editProfile(){
      this.$router.push("/editProfile")
    },
    toAdmin(){
      this.$router.push("/admin")
    },
    //取消关注
    unFollow(){
      this.$http.get("/profile/unFollow/"+this.user.id).then(res=>{
        if(res.data.status===200){
          this.isfollow=false
          this.$bus.$emit("unFollow")
          this.$message.success(res.data.message)
          this.$bus.$emit("followAction")
        }
        else{
          this.$message.error(res.data.message)
        }
      })
    },
    //关注
    follow(){
      this.$http.get("/profile/follow/"+this.user.id).then(res=>{

        if(res.data.status===200){
          this.isfollow=true
          this.$bus.$emit("follow")
          this.$message.success(res.data.message)
          this.$bus.$emit("followAction")
        }
        else{
          this.$message.error(res.data.message)
        }
      })
    },
  }

}
</script>

<style scoped>
.user-info{
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.admin-text{
  color: #0086b3;
  font-size: 16px;
}
.admin-text:hover{
  color: blue;
  cursor: pointer;
}
.username{
  font-size: 36px;
}
.user-avatar{
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-items: center;
}
.update:hover{
  color: blue;
  cursor: pointer;
}
.update{
  margin-left: 10px;
  font-size: 16px;
  color: #0086b3;
}
</style>