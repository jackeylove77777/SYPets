<template>

  <div class="aside rounded">
    <div  class="item">
      <el-link type="primary" @click="to(user.username,1)"><h4>{{isNowUser?'我':sex}}{{'的粉丝:'+user.profile.followers}}</h4></el-link>
    </div>
    <el-divider/>
    <div  class="item">
      <el-link type="primary"  @click="to(user.username,2)"><h4>{{isNowUser?'我':sex}}{{'的关注:'+user.profile.followings}}</h4></el-link>
    </div>
    <el-divider/>
    <div  class="item">
      <el-link type="primary" @click="to(user.username,3)"><h4>{{isNowUser?'我':sex}}{{'的收藏:'+user.profile.collects}}</h4></el-link>
    </div>
  </div>
</template>

<script>
export default {
  name: "Aside",
  data(){
    return {
      isNowUser:true,
      user:{
        profile:{}
      },
      url:''
    }
  },
  computed:{
    sex(){
      if(this.user.sex===0)return this.user.username
      else if(this.user.sex===1)return '他'
      return '她'
    }
  },

  methods:{
    getData(){
      this.$http.get(this.url).then(res=>{
        if(res.data.status===200){
          this.user = res.data.data.user
        }
      })
    },
    getUserInfo(){
      this.user = this.$store.getters.getUser
    },
    //跳转主页方法
    to(username,index){
      this.$router.push("/profile/"+username)
      setTimeout(()=>{
        this.$bus.$emit("open",index)
      },1000)
    },
  },
  created() {
    this.$http.get("/findUserDetails/"+this.$store.getters.getUser.username).then(res=>{
      this.$store.commit("SET_USERINFO",res.data.data.user)
      this.getUserInfo()
    })
    this.$bus.$on('updateAside',(username)=>{
      this.url = "/findUserDetails/"+username
      this.isNowUser = false;
      this.getData()
    })
    this.$bus.$on('NoupdateAside',()=>{
      this.isNowUser=true
      this.getUserInfo()

    })
    //关注
    this.$bus.$on('follow',()=>{
      let userInfo=this.$store.getters.getUser
      userInfo.profile.followings+=1
      this.$store.commit("SET_USERINFO",userInfo)
      this.user.profile.followers+=1
    })
    //取消关注
    this.$bus.$on('unFollow',()=>{
      let userInfo=this.$store.getters.getUser
      userInfo.profile.followings-=1
      this.$store.commit("SET_USERINFO",userInfo)
      this.user.profile.followers-=1
    })
    //收藏事件
    this.$bus.$on("collect",()=>{
      let userInfo=this.$store.getters.getUser
      userInfo.profile.collects+=1
      this.$store.commit("SET_USERINFO",userInfo)
      this.userInfo.profile.collects+=1

    })
    //取消收藏事件
    this.$bus.$on("unCollect",()=>{
      let userInfo=this.$store.getters.getUser
      userInfo.profile.collects-=1
      this.$store.commit("SET_USERINFO",userInfo)
      this.userInfo.profile.collects-=1

    })
  },
  beforeDestroy(){
    this.$bus.$off("updateAside")
    this.$bus.$off("NoupdateAside")
    this.$bus.$off("follow")
    this.$bus.$off("unFollow")
    this.$bus.$off("collect")
    this.$bus.$off("unCollect")
  }
}
</script>

<style scoped>
.aside{
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: white;
}
.item{
  height: 50px;
  width: 200px%;
  color: blue;
  text-align: center;
  margin-top: 1px;
}

</style>