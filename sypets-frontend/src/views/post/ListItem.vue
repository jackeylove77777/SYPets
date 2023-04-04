<template>
  <div class="article-body" @click="toDetail()">
    <el-divider class="divider"></el-divider>
    <div class="article-header">
      <span class="username" @click="visitUser(article.username)"><i>{{article.username}}</i></span>
      <el-divider direction="vertical"></el-divider>
      <span>{{article.createTime}}</span>
      <el-divider direction="vertical"></el-divider>
      <span>{{article.name}}</span>
    </div>
    <div class="article-content">
      <div class="article-text">
        <div class="title">{{article.title}}</div>
        <div class="description">{{article.description}}</div>
        <div class="article-starts">
          <span class="iconfont">&#xe605;{{' '+article.starts}}</span>
          <el-divider direction="vertical"></el-divider>
          <span class="iconfont">&#xe601;{{' '+article.collects}}</span>
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
  name: "ListItem",
  props:{article:{}},
  methods:{
    visitUser(username){
      this.$router.push('/profile/'+username)
    },
    toDetail(){
      this.$router.push('/detail/'+this.article.id)
    }
  },
  computed:{
    firstImg(){
      // 将Markdown转换为HTML
      const htmlText = marked(this.article.content);
      // 从HTML中获取第一张图片的链接
      const htmlDoc = new DOMParser().parseFromString(htmlText, 'text/html');
      const firstImgElement = htmlDoc.querySelector('img');
      return firstImgElement ? firstImgElement.src : 'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg';
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
}
.username{
  color: #252933;
}
.username:hover{
  color: #0086b3;
  cursor: pointer;
}
</style>