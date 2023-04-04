<template>
  <div >
    <PageHeader>
      <el-page-header @back="goBack" content="文章详情">
      </el-page-header>
    </PageHeader>
    <el-row class="mt-4">
      <span class="title">{{data.title}}</span>
    </el-row>
    <div @click="visitUser(data.username)" class="user">
      <el-image
          style="width: 60px; height: 60px"
          :src="data.avatar"></el-image>
      <div class="user-content">
        <span style="color: black;font-size: 20px">{{data.username}}</span>
        <span style="color:#8a919f;">{{data.createTime}}</span>
      </div>
    </div>
    <div class="mt-2">
      <div>
        <span class="edit" @click="edit">编辑</span>
        <span class="toType" @click="toType">{{'类型:'+data.name}}</span>
      </div>
    </div>
    <div  class="top">
      <el-row :gutter="5" style="margin-top: 15px">
        <el-col :span="4" v-if="data.start" >
          <el-button @click="unStart" type="success" plain><i class="el-icon-star-on">{{data.starts}}取消点赞</i></el-button>
        </el-col>
        <el-col :span="4" v-else >
          <el-button @click="onStart" type="success" plain><i class="el-icon-star-off">{{data.starts}}点赞</i></el-button>
        </el-col>
        <el-col :span="4" v-if="data.collect" >
          <el-button @click="unCollect" type="primary" plain><i class="el-icon-star-on">{{data.collects}}取消收藏</i></el-button>
        </el-col>
        <el-col :span="4" v-else >
          <el-button @click="onCollect" type="primary" plain><i class="el-icon-star-off">{{data.collects}}收藏</i></el-button>
        </el-col>
      </el-row>
    </div>
    <el-divider class="my-2"/>
    <div class="markdown-body" v-html="data.content"></div>
    <el-divider class="my-2"/>
    <Comment />
  </div>
</template>

<script>
import "github-markdown-css"
import PageHeader from "../../components/PageHeader.vue";
import Comment from "./Comment.vue";
import {marked} from "marked";
export default {
  name: "DetailPost",
  components:{Comment,PageHeader},
  data(){
    return {
      data:{},
      avatar:''
    }
  },
  methods:{
    edit(){
      this.$router.push('/update/'+this.data.id)
    },
    toType(){
      this.$router.push('/category/'+this.data.name)
    },
    goBack(){
      this.$router.back()
    },
    visitUser(username){
      this.$router.push('/profile/'+username)
    },
    //取消点赞
    unStart(){
      let _this=this
      this.$http.get("/unStart/"+this.data.id).then(res=>{
        if(res.data.status===200){
          _this.data.start=false
          _this.data.starts-=1
          _this.$message.success(res.data.message)
        }
        else{
          _this.$message.error(res.data.message)
        }
      })
    },
    //点赞
    onStart(){
      let _this=this
      this.$http.get("/onStart/"+this.data.id).then(res=>{
        if(res.data.status===200){
          _this.data.start=true
          _this.data.starts+=1
          _this.$message.success(res.data.message)
          // _this.$bus.$emit("collect")
        }
        else{
          _this.$message.error(res.data.message)
        }
      })
    },
    //取消收藏
    unCollect(){
      let _this=this
      this.$http.get("/unCollect/"+this.data.id).then(res=>{
        if(res.data.status===200){
          _this.data.collect=false
          _this.data.collects-=1
          _this.$message.success(res.data.message)
          _this.$bus.$emit("unCollect")
        }
        else{
          _this.$message.error(res.data.message)
        }
      })
    },
    //收藏
    onCollect(){
      let _this=this
      this.$http.get("/onCollect/"+this.data.id).then(res=>{
        if(res.data.status===200){
          _this.data.collect=true
          _this.data.collects+=1
          _this.$message.success(res.data.message)
          _this.$bus.$emit("collect")
        }
        else{
          _this.$message.error(res.data.message)
        }
      })
    },
    getData(){
      let id=this.$route.params.postId
      const _this=this
      this.$http.get("/post/findPostById/"+id).then(res=>{
        if(res.status===404){
          this.$router.push('/notfound')
        }
        if(res.data.status===200) {
          _this.data = res.data.data.data
          _this.data.content = marked(res.data.data.data.content)
        }
        else{
          _this.$message.error("文章不存在或者已被删除")
          this.$router.push('/notfound')
        }
      })
    },
  },
  // computed:{
  //   markedContent(){
  //     return marked(this.data.content)
  //   }
  // },
  created() {
    this.getData()
  },
}
</script>

<style lang="less" scoped>
.title{
  font-size: 36px;
  font-weight: 600;
}
.edit{
  color: #0086b3;
}
.toType{
  margin-left: 15px;
  color: #0086b3;
}
.edit:hover{
  color: blue;
  cursor: pointer;
}
.toType:hover{
  color: blue;
  cursor: pointer;
}
.user{
  display: flex;
  flex-direction: row;
  margin-top: 20px;
}
.user-content{
  display: flex;
  flex-direction: column;
  justify-content: center;
  margin-left: 10px;
}
.markdown-body{
  border-radius: 30px;
  box-sizing: border-box;
  min-width: 200px;
  max-width: 100%;
  margin: 0 auto;
  padding: 5px;
  background: white;
  //响应式
  @media (max-width: 767px) {
    .markdown-body {
      padding: 15px;
    }
  }
}
</style>