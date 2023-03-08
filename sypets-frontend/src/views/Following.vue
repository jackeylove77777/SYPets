<template>
  <el-tabs v-model="activeName" type="border-card" @tab-click="handleClick">
    <el-tab-pane label="文章" name="first" lazy>
      <List :url="'/following/findPost'" :header2="''"></List>
    </el-tab-pane>
    <el-tab-pane label="用户" name="second" lazy>
      <UserList/>
    </el-tab-pane>
  </el-tabs>
</template>

<script>
import UserList from "./following/UserList.vue";
import List from "./post/List.vue";
export default {
  name: "Following",
  components:{
    List,UserList
  },
  data(){
    return{
      activeName:'first',
      list:[]
    }
  },
  methods:{
    handleClick(tab, event) {
      this.activeName=tab.name
    }
  }
}
</script>

<style scoped>

</style>