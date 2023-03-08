<template>
  <div>
    <el-card>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input placeholder="输入文章ID" v-model="query" clearable @clear="getPostList">
            <el-button slot="append" icon="el-icon-search" @click="findOne"></el-button>
          </el-input>
        </el-col>
      </el-row>
    </el-card>
    <el-table :data="postlist" border stripe>
      <el-table-column type="index" label="#"></el-table-column>
      <el-table-column prop="id" label="id"></el-table-column>
      <el-table-column prop="title" label="标题">
        <template slot-scope="scope">
          <span v-html="linkPost(scope.row.title,scope.row.id)"></span>
        </template>
      </el-table-column>
      <el-table-column prop="descr" label="描叙"></el-table-column>
      <el-table-column prop="create_time" label="发布时间"></el-table-column>
      <el-table-column prop="username" label="作者">
        <template slot-scope="scope">
          <span v-html="link(scope.row.username)"></span>
        </template>
      </el-table-column>
      <el-table-column prop="type" label="文章类型">
        <template slot-scope="scope">
          <span>{{types[scope.row.type_id]}}</span>
        </template>
      </el-table-column>
      <el-table-column prop="author" label="原创"></el-table-column>
      <el-table-column label="状态">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.status===1" @change="postStateChanged(scope.row)"></el-switch>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryInfo.pagenum"
        :page-sizes="[2, 5, 10, 15]"
        :page-size="queryInfo.pagesize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
    ></el-pagination>
  </div>
</template>

<script>
export default {
  name: "Post",
  data(){
    return{
      queryInfo:{
        // 当前页数
        pagenum: 1,
        // 每页显示多少数据
        pagesize: 5
      },
      postlist:[{
        status:0
      }],
      total: 0,
      query: '',
      types:['','学习','生活','美食','旅游','想法','其他']
    }
  },
  methods:{
    getPostList(){
      this.$http.get("/admin/getPost",{params:this.queryInfo}).then(res=>{
        console.log(res)
        this.postlist=res.data.data.postlist.list
        this.total=res.data.data.postlist.total
      })
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
    findOne(){
      if(this.query===''||this.query.length===0){
        this.$message.error("请输入搜索内容")
      }
      else{
        let num=parseInt(this.query,10)
        if(num>0){
          this.$http.get("/admin/findOne/"+num).then(res=>{
            this.postlist=res.data.data.postlist
            this.total=this.postlist.length
            this.queryInfo.pagenum=1
            this.queryInfo.pagesize=5
          })
        }
        else {
          this.$message.error("搜索内容无效")
        }

      }
    },
    link(data){
      return "<a href='/profile/"+data+"'><el-link type='primary'>"+data+"</el-link></a>"
    },
    linkPost(title,id){
      return "<a href='/detail/"+id+"'>"+title+"</a>"
    }
  },
  created(){
    this.getPostList()
  }

}
</script>