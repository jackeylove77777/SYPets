<template>
  <div class="header">
<!--    左-->
    <div class="title">
      <img src="../assets/pcat.png" width="60px" height="50px" alt="">
      <span>思远宠物论坛</span>
    </div>
<!--    中-->
    <el-menu  :default-active="$route.path"  class="el-menu-demo" mode="horizontal" router  >
      <el-menu-item  :index="'/'" ><i class="el-icon-s-home"></i>首页</el-menu-item>
      <el-menu-item :index="'/following'" ><i class="el-icon-user-solid"></i>关注</el-menu-item>
      <el-menu-item :index="'/search'" ><i class="el-icon-search"></i>发现</el-menu-item>
      <el-menu-item  :index="'/messages'" ><i class="el-icon-message-solid"></i>消息 <el-badge v-show="message" value="new" /></el-menu-item>
      <el-menu-item  :index="url" ><i class="el-icon-user"></i>个人主页</el-menu-item>
    </el-menu>

    <div class="create">
      <el-button type="primary" @click="edit" round style="margin-left: 50px"><i class="el-icon-edit">此刻的想法</i></el-button>
    </div>
<!--    头像-->
    <div class="user">
      <span >{{userInfo.username}}</span>
      <el-dropdown @command="handleCommand" class="mt-2">
                <span class="el-dropdown-link">
                    <i class="el-icon-arrow-down el-icon--right"><el-avatar :src="userInfo.avatar"></el-avatar></i>
                </span>
        <el-dropdown-menu slot="dropdown" >
          <el-dropdown-item command="profile"><i class="el-icon-user">个人主页</i></el-dropdown-item>
          <el-dropdown-item command="logout" divided><i class="el-icon-warning-outline">退出</i></el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
export default {
  name: "Header",
  data(){
    return {
      activeIndex:"/",
      userInfo:{},
      message: false,
      search:'',
      path:'',
      socket:""
    }
  },
  computed:{
    url(){
      return '/profile/'+this.userInfo.username
    }
  },
  methods:{
    edit(){
      this.$router.push("/edit")
    },
    logout(){
      this.$store.commit("REMOVE_INFO")
      this.$router.push('/login')
    },
    profile(){
      this.$router.push('/profile/'+this.userInfo.username)
    },
    handleCommand(command){
      if(command==="logout"){
        this.logout()
      }
      else if(command==="profile"){
        this.profile()
      }
    },
    exists(){
      this.$http.get("/message/exists").then(res=>{
        this.message=res.data.data.exists
      })
    },
    init(){
      if(typeof(WebSocket) === "undefined"){
        alert("您的浏览器不支持socket")
      }else{
        // 实例化socket
        this.path="ws://localhost:8001/api/websocket"+'?token='+this.$store.getters.getToken
        this.socket = new WebSocket(this.path)
        // 监听socket连接
        this.socket.onopen = this.open
        // 监听socket错误信息
        this.socket.onerror = this.error
        // 监听socket消息
        this.socket.onmessage = this.getMessage
      }
    },
    open(){
      console.log('socket连接')
    },
    error(){
      console.log('连接错误')
    },
    getMessage(msg){
      this.message=true
    }
  },
  created() {
    this.userInfo=this.$store.getters.getUser
    //默认头像
    if(!this.userInfo.avatar){
      this.userInfo.avatar="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
    }
    this.exists()
    this.$bus.$on('noMessage',()=>{
      this.message = false
    })
  },
  mounted() {
    this.init()
  },
  beforeDestroy() {
    this.$bus.$off('noMessage')
  },
  destroyed() {
    this.socket.onclose = this.close
  }
}
</script>

<style scoped>
.header{
  height: 100%;
  display: flex;
  color: #fff;
  font-size: 25px;
}
.title{
  margin-left: 5%;
}
.user{
  margin-left: 5%;
  color: #3FC1C9;
  display: flex;
  justify-content: center;
  align-items: center;
}
.el-menu-demo{
  position: absolute;
  margin-left: 19%;
  background-color: #364F6B;
}

.create{
  margin-left: 45%;
  margin-top: 5px;
}

</style>