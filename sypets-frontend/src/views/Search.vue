<template>
  <div>
<!--    搜索框-->
    <div class="search">
      <el-input clearable v-model="search" maxlength="15"
                show-word-limit style="width: 40%"  placeholder="请输入搜索内容"></el-input>
      <el-button type="primary" @click="getData" style="height: 40px;margin-left: 5px" size="mini" icon="el-icon-search"></el-button>
    </div>
<!--    文章和内容显示-->
    <div class="content">
      <el-tabs v-model="activeName" type="card"  @tab-click="handleClick">
<!--        文章-->
        <el-tab-pane label="文章" name="1">
          <div v-for="item in postList">
            <ListItem :article="item"></ListItem>
          </div>
          <div v-show="postList.length===0" style="text-align: center;margin-top: 10%;margin-bottom: 10%;">
            <el-empty description="暂无内容"></el-empty>
          </div>
        </el-tab-pane>
<!--        用户-->
        <el-tab-pane label="用户" name="2">
          <div v-for="item in userList">
            <UserItem :item="item"></UserItem>
          </div>
          <div v-show="userList.length===0" style="text-align: center;margin-top: 10%;margin-bottom: 10%;">
            <el-empty description="暂无内容"></el-empty>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import ListItem from "./post/ListItem.vue";
import UserItem from "./profile/UserItem.vue";
export default {
  name: "Search",
  components:{ListItem,UserItem},
  data(){
    return{
      search:'',
      userList:[],
      postList:[],
      activeName: '1',
    }
  },
  methods:{
    handleClick(tab, event) {
      this.activeName=tab.name
    },
    getData(){
      if(this.search.length===0){
        this.$message.error("输入内容不能为空")
        return false
      }
      this.$http.get("/search?content="+this.search).then(res=>{
        //后端返回的是map 有post  users
        if(res.data.status===200){
          this.postList=res.data.data.data.post
          this.userList=res.data.data.data.users
        }
      })
    }
  }
}
</script>

<style scoped>
.search{
  display: flex;
  margin-top: 5px;
  margin-left: 5px;
}
.content{
  box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);

  margin-top: 10px;
}
</style>