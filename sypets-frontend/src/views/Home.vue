<template>
  <el-container>
    <el-container>
      <div class="left-el-aside"><Category></Category></div>
      <el-header :class="isFixed=== true?'isFixed':''">
        <Header></Header>
      </el-header>
      <el-main>
        <keep-alive exclude="DetailPost,Update,EditPost,CategoryList,ItemDetail,Message,DetailPost" ><router-view/></keep-alive>
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
  data(){
    return{
      isFixed:false
    }
  },
  methods: {
    handleScroll() {
      this.$nextTick(() => {
        let scrollTop = document.documentElement.scrollTop || document.body.scrollTop;

        this.isFixed = scrollTop > 10;
      })
    },
    toTop() {
      let top = document.documentElement.scrollTop || document.body.scrollTop;
      // 实现滚动效果
      const timeTop = setInterval(() => {
        document.body.scrollTop = document.documentElement.scrollTop = top -= 50;
        if (top <= 0) {
          clearInterval(timeTop);
        }
      }, 10);
    },

  },
  mounted(){
    window.addEventListener('scroll', this.handleScroll);
    this.$bus.$on('backTop',()=>{
      this.toTop()
    })
  },
  beforeDestroy(){
    this.$bus.$off("backTop")
  },
  destroyed(){
    window.removeEventListener('scroll', this.handleScroll);
  },

}
</script>

<style lang="less" scoped>
.el-header{
  width: 100%;
  box-shadow:0 0 6px rgba(0, 0, 0, 0.03);
  background-color: #373D41;
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
  margin-top: 5px;
  margin-left: 20%;
  width: 52%;
  height: 92%;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.5)
}
.left-el-aside{
  position: fixed;
  left: 10%;
  top: 20%;
  height: 350px;
  width: 9%;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.5)
}

</style>