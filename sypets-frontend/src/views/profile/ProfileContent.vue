<template>
  <div>
    <el-tabs v-model="activeName" type="border-card" @tab-click="handleClick">
      <el-tab-pane label="动态" name="first" lazy>
        <div if="activeName==='first'">
          <First  :IsMe="IsMe" :user="user"/>
        </div>
      </el-tab-pane>
      <el-tab-pane label="收藏" name="second" lazy>
        <div v-if="activeName==='second'" >
          <Second  :IsMe="IsMe" :user="user"/>
        </div>
      </el-tab-pane>
      <el-tab-pane label="粉丝" name="third" lazy>
        <div v-if="activeName==='third'">
          <Third :IsMe="IsMe"  :user="user"/>
        </div>
      </el-tab-pane>
      <el-tab-pane label="关注" name="fourth" lazy>
        <div v-if="activeName==='fourth'">
          <Fourth :IsMe="IsMe"  :user="user"/>
        </div>
      </el-tab-pane>
      <el-tab-pane label="寻宠" name="findpet" lazy>
        <div v-if="activeName==='findpet'">
          <UserFindPet :IsMe="IsMe"  :user="user"/>
        </div>
      </el-tab-pane>
      <el-tab-pane label="寄养" name="foster" lazy>
        <div v-if="activeName==='foster'">
          <UserFoster :IsMe="IsMe" :user="user"/>
        </div>
      </el-tab-pane>
      <el-tab-pane label="流浪" name="stray" lazy>
        <div v-if="activeName==='stray'">
          <UserStray :IsMe="IsMe"  :user="user"/>
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
import UserFindPet from "./UserFindPet.vue";
import UserFoster from "./UserFoster.vue";
import UserStray from "./UserStray.vue";
export default {
  name: "ProfileContent",
  components:{First,Second,Third,Fourth,UserFindPet,UserFoster,UserStray},
  data(){
    return{
      activeName:'first'
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