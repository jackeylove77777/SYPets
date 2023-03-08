<template>
  <div >
    <PageHeader>
      <el-page-header @back="goBack" content="文章详情">
      </el-page-header>
    </PageHeader>
    <div class="user" @click="visitUser(data.username)">
      <el-avatar :src="data.avatar"></el-avatar>
      <span>{{data.username}}</span>
    </div>
    <div style="margin-left: 20px;margin-top: 5px" v-show="data.uid===$store.getters.getUser.id&&data.author===1">
      <router-link :to="'/update/'+data.id"><el-link  type="warning">编辑</el-link></router-link>
    </div>
    <div  class="top">
      <el-row><router-link :to="'/category/'+data.name"><el-link type="primary">{{'类型:'+data.name}}</el-link></router-link></el-row>
      <el-row>
        <h3>{{'标题:'+data.title}}</h3>
      </el-row>
      <el-row>{{'描述:'+data.description}}</el-row>
<!--      <el-row style="color: #67C23A" v-if="data.author===0">-->
<!--        转发-->
<!--      </el-row>-->
<!--      <el-row style="color: #67C23A" v-else>-->
<!--        原创-->
<!--      </el-row>-->
      <el-row :gutter="20" style="margin-top: 15px">
        <el-col :span="5" v-if="data.start" >
          <el-button @click="unStart" type="primary"><i class="el-icon-star-on">{{data.starts}}取消点赞</i></el-button>
        </el-col>
        <el-col :span="5" v-else >
          <el-button @click="onStart" type="primary"><i class="el-icon-star-off">{{data.starts}}点赞</i></el-button>
        </el-col>
        <el-col :span="5" v-if="data.collect" >
          <el-button @click="unCollect" type="primary"><i class="el-icon-star-on">{{data.collects}}取消收藏</i></el-button>
        </el-col>
        <el-col :span="5" v-else >
          <el-button @click="onCollect" type="primary"><i class="el-icon-star-off">{{data.collects}}收藏</i></el-button>
        </el-col>
<!--        <el-col v-show="data.username!=$store.getters.getUser.username" :span="5">-->
<!--          <el-tooltip class="item" effect="dark" content="转发" placement="right">-->
<!--            <el-button  type="primary" @click="rePost(data.id)" icon="el-icon-share" circle></el-button>-->
<!--          </el-tooltip>-->
<!--        </el-col>-->
<!--        <el-col v-show="data.username!=$store.getters.getUser.username" :span="5">-->
<!--          <el-button type="danger" @click="open">举报</el-button>-->
<!--        </el-col>-->
      </el-row>
    </div>
    <el-divider/>
    <h2>文章内容:</h2>
    <div class="markdown-body" v-html="data.content"></div>
    <el-divider/>
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

        if(res.data.status===200) {
          _this.data = res.data.data.data
          _this.data.content = marked(res.data.data.data.content)
        }
        else{
          _this.$message.error("文章不存在或者已被删除")
          setTimeout(()=>{
            _this.$router.back()
          },2000)

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
.user{
  margin-left: 20px;
  margin-top: 20px;
}
.top{
  margin: 0 auto;
  text-align: center;
}
.markdown-body{
  border-radius: 30px;
  box-sizing: border-box;
  min-width: 200px;
  max-width: 500px;
  margin: 0 auto;
  padding: 45px;
  background: #969896;
  //响应式
  @media (max-width: 767px) {
    .markdown-body {
      padding: 15px;
  }
}

}
</style>