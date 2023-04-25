<template>
   <div>
     <el-card>
       <el-row :gutter="20">
         <el-col :span="6">
           <el-input placeholder="输入搜索内容" v-model="queryInfo.query" clearable @clear="getUserList">
             <el-button slot="append" icon="el-icon-search" @click="getUserList"></el-button>
           </el-input>
         </el-col>
       </el-row>
     </el-card>
<!--     表格-->
    <table class="table table-striped table-bordered">
      <thead>
        <tr>
          <th class="text-center">Id</th>
          <th class="text-center">用户名</th>
          <th class="text-center">性别</th>
          <th class="text-center">邮箱地址</th>
          <th class="text-center">个人简介</th>
          <th class="text-center">头像</th>
          <th class="text-center">状态</th>
          <th class="text-center">操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="user in userlist" :key="user.id">
          <td class="tableUint">{{user.id}}</td>
          <td class="tableUint" style="cursor: pointer" @click="link(user.username)">{{user.username}}</td>
          <td class="tableUint">{{user.sex===0?'未知':(user.sex===1?'男':'女')}}</td>
          <td class="tableUint">{{user.email}}</td>
          <td class="tableUint">{{user.privateInfo}}</td>
          <td class="tableUint">
            <a :href="user.avatar" target="_blank">{{user.avatar}}</a>
          </td>
          <td style="width: 100px" :class="user.status===1?'text-success':'text-danger'">{{user.status===1?'正常':'异常'}}</td>
          <td class="text-center">
            <div class="op-col">
              <span @click="userStateChanged(user)" class="op-text">修改状态</span>
              <span @click="deleteById(user.id)" class="delete-text">删除</span>
            </div>
          </td>
        </tr>
      </tbody>
    </table>

     <div v-if="userlist===undefined||userlist.length===0">
       <el-empty description="暂无内容"></el-empty>
     </div>
<!--     分页-->
     <el-pagination
         @size-change="handleSizeChange"
         @current-change="handleCurrentChange"
         :current-page="queryInfo.pagenum"
         :page-sizes="[ 10, 15,30]"
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
        pagesize: 10,
        query:''
      },
      userlist:[],
      total:0,
    }
  },

  methods:{
    getUserList(){
      this.$http.get("/admin/getUser",{params:this.queryInfo}).then(res=>{
        this.userlist=res.data.data.list
        this.total=res.data.data.total
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
        // else{
        //   this.$message.error(res.data.message)
        // }
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
    deleteById(id){
      this.$confirm('确定删除该用户吗')
          .then(_ => {
            this.$http.delete('/admin/user/'+id).then(res=>{
              this.$message.info(res.data.message)
              this.userlist = this.userlist.filter(x=>x.id!==id)
            })
          })
          .catch(_ => {});
    },
    link(data){
      this.$router.push('/profile/'+data)
    }
  },
  created() {
    this.getUserList()
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
