<template>
  <div class="article-body" @click="toDetail()">
    <el-divider class="divider"></el-divider>
    <div class="article-header">
      <span class="username" @click="visitUser(item.username)"><i>{{item.username}}</i></span>
      <el-divider direction="vertical"></el-divider>
      <span>{{item.createTime}}</span>
      <el-divider direction="vertical"></el-divider>
      <span>{{item.name}}</span>
    </div>
    <div class="article-content">
      <div class="article-text">
        <div class="title">{{item.title}}</div>
        <div class="description">{{item.description}}</div>
        <div class="article-starts">
          <span class="iconfont">&#xe605;{{' '+item.starts}}</span>
          <el-divider direction="vertical"></el-divider>
          <span class="iconfont">&#xe601;{{' '+item.collects}}</span>
          <el-divider direction="vertical"></el-divider>
          <div v-if="show"><el-button type="primary" @click.prevent="edit(item.id)" size="mini" icon="el-icon-edit" circle></el-button></div>
          <el-divider direction="vertical"></el-divider>
          <div v-if="show"><el-button type="danger"  @click.stop="deleted(item.id)" size="mini" icon="el-icon-delete" circle></el-button></div>
          <el-divider direction="vertical"></el-divider>
          <div v-show="IsMe&&isCollect">
            <el-tooltip class="item" effect="dark" content="点击取消收藏" placement="top">
              <el-button type="info"  @click.stop="unCollect(item.id)" size="mini" icon="el-icon-star-off" circle></el-button>
            </el-tooltip>
          </div>
        </div>
      </div>
      <div>
        <el-image
            style="width: 120px; height: 80px"
            :src="firstImg"></el-image>
      </div>
    </div>
  </div>
</template>

<script>
import {marked} from "marked";

export default {
  name: "Item",
  data(){
    return{
      dialogVisible:false
    }
  },
  props:{
    user:{},
    item:{},
    isCollect:false,
    IsMe:false
  },
  computed:{
    firstImg(){
      // 将Markdown转换为HTML
      const htmlText = marked(this.item.content);
      // 从HTML中获取第一张图片的链接
      const htmlDoc = new DOMParser().parseFromString(htmlText, 'text/html');
      const firstImgElement = htmlDoc.querySelector('img');
      return firstImgElement ? firstImgElement.src : 'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg';
    },
    show(){
      return this.IsMe&&this.item.username===this.$store.getters.getUser.username
    }
  },
  methods:{
    visitUser(username){
      this.$router.push('/profile/'+username)
    },
    toDetail(){
      this.$router.push('/detail/'+this.item.id)
    },
    edit(id){
      this.$router.push('/update/'+id)
    },
    deleted(id){
      this.dialogVisible=true
      this.handleClose(id,'确认删除？')
    },
    unCollect(id){
      this.$http.get("/unCollect/"+id).then(res=>{
        if(res.data.status===200){
          this.item.collect=false
          this.item.collects-=1
          this.$message.success(res.data.message)
          this.$bus.$emit("unCollect")
          this.$emit('unCollects',id)
        }
        else{
          this.$message.error(res.data.message)
        }
      })
    },
    handleClose(id,msg) {
      this.$confirm(msg)
          .then(_ => {
            this.$http.put('/profile/delete/',{'id':id}).then(res=>{
              if(res.data.status===200){
                this.$message.success(res.data.message)
                this.$emit('delete',id)
              }
              else{
                this.$message.success(res.data.message)
              }
            })
          })
          .catch(_ => {});
    }
  }
}
</script>

<style scoped>
.article-body{
  padding: 12px 20px 0;
  width: 100%;
  height: 140px;
}
.article-body:hover{
  background-color: #F2F3F5;
}

.divider{
  margin-top: 0;
  margin-bottom: 5px;
}
.article-header{
  color: #8A919F;
  font-size: 14px;
}
.article-content{
  display: flex;
  flex-direction: row;
}
.article-text{
  flex: 4.5;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}
.title{
  color: #252933;
  font-size: 18px;
  margin-top: 5px;
  display: -webkit-box;
  overflow: hidden;
  text-overflow: ellipsis;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 1;
}
.description{
  color: #8A919F;
  font-size: 14px;
  margin-top: 5px;
  display: -webkit-box;
  overflow: hidden;
  text-overflow: ellipsis;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 1;
}
.article-starts{
  color: #8A919F;
  font-size: 14px;
  margin-top: 5px;
  display: flex;
  flex-direction: row;
}
.username{
  color: #252933;
}
.username:hover{
  color: #0086b3;
  cursor: pointer;
}
</style>