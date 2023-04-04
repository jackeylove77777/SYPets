<template>
  <div>
    <el-card>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input placeholder="输入文章ID" v-model="queryInfo.query" clearable @clear="getPostList">
            <el-button slot="append" icon="el-icon-search" @click="getPostList"></el-button>
          </el-input>
        </el-col>
      </el-row>
    </el-card>
    <!--     表格-->
    <table class="table table-striped table-bordered">
      <thead>
      <tr>
        <th class="text-center">id</th>
        <th class="text-center">作者</th>
        <th class="text-center">分类</th>
        <th class="text-center">标题</th>
        <th class="text-center">描述</th>
        <th class="text-center">内容</th>
        <th class="text-center">状态</th>
        <th class="text-center">操作</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="post in postlist" :key="post.id">
        <td class="tableUint text-primary"><span @click="linkPost(post.id)" style="cursor: pointer" class="text-primary text-center">{{post.id}}</span></td>
        <td class="tableUint text-primary"><span @click="link(post.username)" style="cursor: pointer" class="text-primary text-center">{{post.username}}</span></td>
        <td class="tableUint">{{post.name}}</td>
        <td class="tableUint">{{post.title}}</td>
        <td class="tableUint">{{post.description}}</td>
        <td class="tableUint text-primary"><span @click="openContent(post.content)" style="cursor: pointer">查看文章内容</span></td>
        <td :class="post.status===1?'text-success text-center lh-lg fs-5':'text-danger text-center lh-lg fs-5'">{{post.status}}</td>
        <td class="tableUint">
          <div class="op-col">
            <span @click="postStateChanged(post)" class="op-text">修改状态</span>
            <span @click="deleteById(post.id)" class="delete-text">删除</span>
          </div>
        </td>
      </tr>
      </tbody>
    </table>

    <div v-if="postlist===undefined||postlist.length===0">
      <el-empty description="暂无内容"></el-empty>
    </div>
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryInfo.pagenum"
        :page-sizes="[ 10, 15,30]"
        :page-size="queryInfo.pagesize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
    ></el-pagination>

    <el-dialog
        title="文章内容"
        :visible.sync="showPostContent"
        width="50%"
        class="overflow-scroll"
        :before-close="closeDialog">
        <div v-html="htmlContent" class="bg-gradient overflow-scroll border p-2 bg-light" style="min-height: 400px;width: 100%"></div>
        <div class="d-flex flex-row-reverse px-1 mt-4"><el-button @click="closeDialog" type="primary">确定</el-button></div>
    </el-dialog>
  </div>
</template>

<script>
import {marked} from "marked";

export default {
  name: "Post",
  data(){
    return{
      queryInfo:{
        // 当前页数
        pagenum: 1,
        // 每页显示多少数据
        pagesize: 10,
        query: '',
      },
      postlist:[{
        status:0
      }],
      total: 0,
      types:['','学习','生活','美食','旅游','想法','其他'],
      showPostContent:false,
      htmlContent:null,
    }
  },
  methods:{
    getPostList(){
      this.$http.get("/admin/getPost",{params:this.queryInfo}).then(res=>{
        console.log(res)
        this.postlist=res.data.data.list
        this.total=res.data.data.total
      })
    },
    openContent(content){
      this.htmlContent = marked(content)
      this.showPostContent=true;
    },
    closeDialog(){
      this.showPostContent=false
      this.htmlContent=null
    },
    postStateChanged(post){
      this.$http.get("/admin/changePostStatus/"+post.id).then(res=>{
        if(res.data.status==200){
          if(post.status===1)
            post.status=0
          else
            post.status=1
          this.$message.success(res.data.message)
        }
        else{
          this.$message.error(res.data.message)
        }
      })
    },
    // 监听 pagesize改变的事件
    handleSizeChange (newSize) {
      // console.log(newSize)
      this.queryInfo.pagesize = newSize
      this.getPostList()
    },
    // 监听 页码值 改变事件
    handleCurrentChange (newSize) {
      // console.log(newSize)
      this.queryInfo.pagenum = newSize
      this.getPostList()
    },
    deleteById(id){
      this.$http.delete('/admin/post/'+id).then(res=>{
        this.$message.info(res.data.message)
      })
      this.postlist = this.postlist.filter(x=>x.id!==id)
    },
    link(data){
      this.$router.push('/profile/'+data)
    },
    linkPost(id){
      this.$router.push('/detail/'+id)
    }
  },
  created(){
    this.getPostList()
  }
}
</script>
<style scoped>
.op-text{
  color: #66b9fd;
  cursor: pointer;
}
.op-text:hover{
  color: blue;
  text-decoration: underline;
}
.delete-text{
  padding-left: 10px;
  color: orangered;
  cursor: pointer;
}
.delete-text:hover{
  color: red;
  text-decoration: underline;
}
.tableUint{
  text-align: center;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 150px;
  white-space: nowrap;
}
</style>