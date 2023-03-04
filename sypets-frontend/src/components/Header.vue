<template>
  <div class="header">

    <div class="title">
      <img src="../assets/logo.png" width="50px" height="50px" alt="">
      <span>思远论坛</span>
    </div>

    <el-menu  :default-active="$route.path"  class="el-menu-demo" mode="horizontal" router  >
      <el-menu-item  :index="'/'" ><i class="el-icon-s-home"></i>首页</el-menu-item>
      <el-menu-item :index="'/following'"><i class="el-icon-user-solid"></i>关注</el-menu-item>
      <el-menu-item :index="'/serach'"><i class="el-icon-search"></i>发现</el-menu-item>
      <el-menu-item  :index="'/messages'"><i class="el-icon-message-solid"></i>消息 <el-badge v-show="message" value="new" /></el-menu-item>
      <el-menu-item   :index="url"><i class="el-icon-user"></i>个人主页</el-menu-item>

    </el-menu>

    <div class="create">
      <el-button type="primary" @click="edit" round style="margin-left: 50px"><i class="el-icon-edit">此刻的想法</i></el-button>
    </div>

    <div class="user">
      <span>{{userInfo.username}}</span>
      <el-dropdown @command="handleCommand">
        <span class="el-dropdown-link">
          <i class="el-icon-arrow-down el-icon--right"><el-avatar :src="userInfo.avatar"></el-avatar></i>
        </span>
        <el-dropdown-menu slot="dropdown"  >
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
    return{
      activeIndex:"/",
      userInfo: {},
      message:false,
      serach:'',
      path:"",
      socket:""
    }
  },
  computed:{
    url(){
      return '/profile/'+this.userInfo.username+''
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
    handleCommand(command) {
      if(command==="logout"){
        this.logout()
      }
      else if(command==="profile"){
        this.profile()
      }
    },
    handleScroll () {
      this.$nextTick(() => {
        let scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
        if (scrollTop > 10) {
          this.isFixed = true;
        } else {
          this.isFixed = false;
        }
      })
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
    open() {
      console.log("socket连接成功")
    },
    error() {
      console.log("连接错误")
    },
    getMessage(msg) {
      console.log(msg)
      this.message=true

    },
    close() {
      console.log("socket已经关闭")
    }

  },
  created() {
    // this.handleSelect(this.$route.name)
    this.userInfo=this.$store.getters.getUser

    if(!this.userInfo.avatar){
      //默认的头像
      this.userInfo.avatar="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
    }
    this.exists()
    // window.setInterval(()=>{
    //     this.exists()
    // },1000*30)
    this.$bus.$on("noMessage",()=>{
      this.message=false
    })
  },
  mounted() {
    this.init()
  },
  beforeDestroy(){
    this.$bus.$off("noMessage")
  },
  destroyed() {
    window.clearInterval()
    this.socket.onclose = this.close
  }
}
</script>

<style lang="less" scoped>
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
  color: #0086b3;
}
.el-menu-demo{
  position: absolute;
  margin-left: 19%;
}
.create{
  margin-left: 45%;
  margin-top: 5px;
}

</style>