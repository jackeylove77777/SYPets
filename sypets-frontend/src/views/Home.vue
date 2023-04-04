<template>
<div>
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
    <Aside class="aside" />
  </el-container>
  <AdsPopBox></AdsPopBox>
</div>
</template>

<script>
import Header from "../components/Header.vue";
import Aside from "../components/Aside.vue";
import Category from "../components/Category.vue";
import AdsPopBox from "../components/AdsPopBox.vue";
export default {
  name: "Home",
  components:{Category, Header,Aside,AdsPopBox },
}
</script>

<style scoped>
.my-container{
  background-color: #F2F3F5;
  min-height: 101vh;
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
.aside{
  position: fixed;
  top:9%;
  left: 78%;
  width: 160px;
  height: 220px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.5)
}
.el-main{
  margin-top: 60px;
  margin-left: 20%;
  width: 57%;
  height: 92%;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.5);
}
.left-el-aside{
  position: fixed;
  left: 10%;
  top: 8%;
  height: auto;
  width: 9%;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.5);
  border-radius: 8px;
}

</style>