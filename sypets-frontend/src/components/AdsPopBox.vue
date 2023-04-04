<template>
  <el-card :body-style="{ padding: '0px' }" v-show="showAd" class="ad-dialog">
    <div style="padding: 14px;">
      <div class="bottom clearfix">
        <span class="time">{{ advertisement.description}}</span>
        <el-button type="text" class="button" @click="showAd=false">关闭</el-button>
      </div>
    </div>
    <a :href="advertisement.address" target="_blank">
      <img :src="advertisement.avatar" class="image">
    </a>
  </el-card>
</template>
<script>
export default {
  name: "AdsPopBox",
  data(){
    return{
      showAd:false,
      advertisement:{}
    };
  },
  mounted() {  //vue加载到浏览器时就调用一次
    this.$http.get("/ad/random").then(res=>{
      this.advertisement = res.data.data.ad
    })
    setTimeout(()=>{
      this.showAd=true;
    },1000);
  },
}
</script>

<style scoped>
.ad-dialog{
  position: fixed;
  bottom: 10px;
  left: 10px;
  z-index: 1000;
  background-color: white;
  width: 250px;
  height: 250px;
}

.close-icon{
  position: absolute;
  top: 5px;
  right: 5px;
}
.time {
  font-size: 13px;
  color: #999;
}

.bottom {

}

.button {
  padding: 0;
  float: right;
}

.image {
  width: 100%;
  display: block;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both
}
</style>