<template>
  <el-container>
    <el-container class="my-container">
      <div class="left-el-aside"><Category></Category></div>
      <el-header class="fixed-header">
        <Header></Header>
      </el-header>
      <el-main style="background-color: white">
        <router-view/>
      </el-main>
    </el-container>
    <el-aside><Aside/></el-aside>

  </el-container>
</template>

<script>
import Header from "../components/Header.vue";
import Aside from "../components/Aside.vue";
import Category from "../components/Category.vue";

export default {
  name: "Home",
  components:{Category, Header,Aside},
}
</script>

<style scoped>
.my-container{
  background-color: #F2F3F5;
  min-height: calc(100vh - 60px);
}
.fixed-header{
  position: fixed;
  top: 0;
  z-index: 100;
}
.el-header{
  width: 100%;
  box-shadow:0 0 6px rgba(0, 0, 0, 0.03);
  background-color: #364F6B;
}
.el-aside{
  position: fixed;
  top:20%;
  left: 73%;
  width: 30%;
  height: 350px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.5)
}
.el-main{
  margin-top: 60px;
  margin-left: 20%;
  width: 52%;
  height: 92%;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.5);
}
.left-el-aside{
  position: fixed;
  left: 200px;
  top: 8%;
  height: auto;
  width: 9%;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.5);
  border-radius: 8px;
}

</style>