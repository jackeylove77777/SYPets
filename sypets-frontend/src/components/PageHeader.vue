<template>
  <div :class="isFixed== true?'isFixed':''" :style="isFixed==true?'margin-top: 50px':''">
    <slot/>
  </div>
</template>

<script>
export default {
  name: "PageHeader",
  data(){
    return{
      isFixed:false
    }
  },
  methods:{
    handleScroll(){
      this.$nextTick(()=>{
        //获取距离顶部的距离
        let scrollTop = document.documentElement.scrollTop || document.body.scrollTop
        this.isFixed = scrollTop>30
      })
    }
  },
  mounted() {
    window.addEventListener('scroll',this.handleScroll)
  },
  destroyed() {
    window.removeEventListener('scroll',this.handleScroll)
  }
}
</script>

<style scoped>
.isFixed{
  position: fixed;
  margin-top: 0;
  z-index: 4;
  width: 100%;
}
</style>