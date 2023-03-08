<template>
  <el-container>
    <!-- 头部 -->
    <el-header>
      <div>
        <img src="../../assets/logo.png" alt />
        <span>思远宠物论坛后台管理系统</span>
      </div>
      <el-button type="info" @click="logout">退出</el-button>
    </el-header>
    <!-- 主体 -->
    <el-container>
      <!-- 侧边栏 -->
      <el-aside :width="isCollapse ? '64px' : '200px'">

        <el-menu unique-opened  router :default-active="activePath" background-color="#333744" text-color="#fff" active-text-color="#409FFF">
          <!-- :unique-opened="true"->只允许展开一个菜单 -->
          <!-- :collapse-transition="false" -> 关闭动画 -->
          <!-- router -> 导航开启路由模式 -->
          <!-- 一级菜单  -->
          <el-menu-item @click="saveNavState('/' + item.path)" :index="'/'+item.path" v-for="item in menuList" :key="item.id" >
            <!-- 一级菜单的模板区域 -->
            <template slot="title">
              <i :class="iconObj[item.id]"></i>
              <span>{{ item.authName}}</span>
            </template>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <!-- 内容主体 -->
      <el-main>
        <router-view/>
      </el-main>
    </el-container>
  </el-container>
</template>
<script>
export default {
  name: "Admin",
  data () {
    return {
      // 左侧菜单数据
      menuList: [{'id':'125','authName':'用户管理',path:'user'},{'id':'103','authName':'文章管理',path:'post'},{'id':'101','authName':'举报处理',path:'report'}],
      iconObj: {
        '125': 'iconfont icon-user',
        '103': 'iconfont icon-baobiao',
        '101': 'iconfont icon-lock_fill',
      },
      // 默认不折叠
      isCollapse: false,
      // 被激活导航地址
      activePath: ''
    }
  },
  created () {
    this.activePath = window.sessionStorage.getItem('activePath')
  },
  methods: {
    logout () {
      this.$router.push('/')
    },
    // 保存连接的激活地址
    saveNavState (activePath) {
      window.sessionStorage.setItem('activePath', activePath)
    }
  }
}
</script>

<style lang="less" scoped>
.el-container {
  height: 100%;
}
.el-header {
  background-color: #373f41;
  display: flex;
  justify-content: space-between;
  padding-left: 0;
  align-items: center;
  color: #fff;
  font-size: 20px;
}
.el-header > div{
  display: flex;
  align-items: center;
}
.el-header > div img{
  height: 40px;
}
.el-header > div span{
  margin-left: 15px;
}
.el-aside {
  background-color: #333744;

  .el-menu {
    border: none;
  }
}
.el-main {
  background-color: #eaedf1;
}
.iconfont{
  margin-right: 10px;
}
.toggle-button {
  background-color: #4A5064;
  font-size: 10px;
  line-height: 24px;
  color: #fff;
  text-align: center;
  letter-spacing: 0.2em;
  // 鼠标放上去变成小手
  cursor: pointer;
}
</style>