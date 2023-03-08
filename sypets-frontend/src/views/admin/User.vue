<template>
   <div>
     <el-card>
       <el-row :gutter="20">
         <el-col :span="6">
           <el-input placeholder="输入用户ID" v-model="query" clearable @clear="getUserList">
             <el-button slot="append" icon="el-icon-search" @click="findOne"></el-button>
           </el-input>
         </el-col>
       </el-row>
     </el-card>
     <el-table :data="userlist" border stripe>
       <el-table-column type="index" label="#"></el-table-column>
       <el-table-column prop="id" label="id"></el-table-column>
       <el-table-column prop="username" label="姓名">
         <template slot-scope="scope">
           <span v-html="link(scope.row.username)"></span>
         </template>
       </el-table-column>
       <el-table-column prop="email" label="邮箱"></el-table-column>
       <el-table-column prop="sex" label="性别"></el-table-column>
       <el-table-column prop="private_info" label="简介"></el-table-column>
       <el-table-column label="状态">
         <template slot-scope="scope">
           <el-switch v-model="scope.row.status===1" @change="userStateChanged(scope.row)"></el-switch>
         </template>
       </el-table-column>
     </el-table>
<!--     分页-->
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
  name: "User",
  data(){
    return {
      queryInfo:{
        //当前页数
        pagenum: 1,
        // 每页显示多少数据
        pagesize: 5
      },
      userlist:[],
      total:0,
      query:''
    }
  },
  methods:{
    getUserList(){
      this.$http.get("/admin/getUser",{params:this.queryInfo}).then(res=>{
        this.userlist=res.data.data.userlist.list
        this.total=res.data.data.userlist.total
      })
    },
    userStateChanged(user){
      this.$http.get("/admin/changeStatus/"+user.id).then(res=>{
        if(res.data.status==200){
          if(user.status===1)
            user.status=0
          else
            user.status=1
          this.$message.success(res.data.message)
        }
        else{
          this.$message.error(res.data.message)
        }
      })
    },
    // 监听 pagesize改变
    handleSizeChange (newSize) {
      this.queryInfo.pagesize = newSize
      this.getUserList()
    },
    // 监听页码值改变
    handleCurrentChange (newNum) {
      this.queryInfo.pagenum = newNum
      this.getUserList()
    },
    findOne(){
      if(this.query===''||this.query.length===0){
        this.$message.error("请输入搜索内容")
      }
      else{
        let num=parseInt(this.query,10)
        if(num>0){
          this.$http.get("/admin/getOne/"+num).then(res=>{
            this.userlist=res.data.data.userlist
            this.total=this.userlist.length
            this.queryInfo.pagenum=1
            this.queryInfo.pagesize=5
          })
        }
        else {
          this.$message.error("搜索内容无效")
        }
        console.log(num)
      }
    },
    link(data){

      return "<a href='/profile/"+data+"'><el-link type='primary'>"+data+"</el-link></a>"
    }
  },
  created() {
    this.getUserList()
  }
}
</script>
