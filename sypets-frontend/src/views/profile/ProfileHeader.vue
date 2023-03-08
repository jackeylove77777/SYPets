<template>
  <div class="profile">
<!--    自己主页-->
    <div v-if="IsMe">
      <el-row style="margin-bottom: 0" :gutter="5">
        <el-col :span="6"><div class="block" ><el-avatar shape="square" :size="200"  :src="user.avatar"></el-avatar></div></el-col>
        <el-col class="user"  :span="6"><span>{{user.username}}</span></el-col>
      </el-row>
      <el-row>
        <el-col class="info" :span="5">简介:{{user.privateInfo}}</el-col>
        <el-col class="update" :span="4"><el-link type="success" @click="editProfile"><i class="el-icon-edit">编辑个人资料</i></el-link></el-col>
      </el-row>
      <el-row v-show="user.username==='admin'">
        <router-link to="/admin">进入后台</router-link>
      </el-row>
    </div>
<!--    他人主页-->
    <div v-else>
      <el-row style="margin-bottom: 0"  :gutter="5">
        <el-col :span="6"><div class="block" ><el-avatar shape="square" :size="200" :src="user.avatar"></el-avatar></div></el-col>
        <el-col class="user"  :span="7"><span>{{user.username}}</span></el-col>
      </el-row>
<!--      <el-button style="margin-left: 30%" @click="open" type="danger">举报</el-button>-->
      <el-row>
        <el-col class="info" :span="5">简介:{{user.private_info}}</el-col>
        <el-col class="update" :span="4">
          <el-button v-if="isfollow" @click="unFollow" type="primary">
            <i class="el-icon-star-off" >取消关注</i>
          </el-button>
          <el-button v-else type="primary" @click="follow" >
            <i class="el-icon-star-off" >关注</i>
          </el-button>
        </el-col >

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
    editProfile(){
      this.$router.push("/editProfile")
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
.user{
  margin-top: 120px;
  margin-left: 20px;
  color: #0086b3;
  font-size: 25px;
}
.info{
  margin-top: 130px;

}
.update{
  margin-left: 350px;
  margin-top: 150px;
}
</style>