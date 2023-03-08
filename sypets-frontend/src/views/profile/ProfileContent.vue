<template>
  <div>
    <el-tabs v-model="activeName" type="border-card" @tab-click="handleClick">
      <el-tab-pane label="动态" name="first" lazy>
        <div v-show="activeName==='first'">
          <First  :IsMe="IsMe" :user="user"/>
        </div>
      </el-tab-pane>
      <el-tab-pane label="收藏" name="second" lazy>
        <div v-show="activeName==='second'" >
          <Second  :IsMe="IsMe" :user="user"/>
        </div>
      </el-tab-pane>
      <el-tab-pane label="粉丝" name="third" lazy>
        <div v-show="activeName==='third'">
          <Third :IsMe="IsMe"  :user="user"/>
        </div>
      </el-tab-pane>
      <el-tab-pane label="关注" name="fourth" lazy>
        <div v-show="activeName==='fourth'">
          <Fourth :IsMe="IsMe"  :user="user"/>
        </div>
      </el-tab-pane>
      <el-tab-pane label="被提到的" name="fifth" lazy>
        <div v-show="activeName==='fifth'">
          <Fifth :IsMe="IsMe" :user="user"/>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import First from "./First.vue";
import Second from "./Second.vue";
import Third from "./Third.vue";
import Fourth from "./Fourth.vue";
import Fifth from "./Fifth.vue";
export default {
  name: "ProfileContent",
  components:{Fifth, First,Second,Third,Fourth},
  data(){
    return{
      activeName:''
    }
  },
  props:{
    IsMe:false,
    user:{
      profile:{}
    }
  },
  methods:{
    handleClick(tab,event){
      this.activeName = tab.name
    }
  },
  mounted() {
    this.$bus.$on("open",index=>{
      if(index===1){
        this.activeName="third"
      }
      else if(index===2){
        this.activeName="fourth"
      }
      else if(index===3){
        this.activeName='second'
      }
    })
  },
  beforeDestroy(){
    this.$bus.$off("open")
  }
}
</script>

<style scoped>

</style>