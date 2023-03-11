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
     <div v-if="userlist.length===0" class="h-50 text-center text-muted fs-4 fw-bold ">
       空空如也
     </div>
<!--     表格-->
    <table class="table table-striped table-bordered">
      <thead>
        <tr>
          <th class="text-center">用户名</th>
          <th class="text-center">性别</th>
          <th class="text-center">邮箱地址</th>
          <th class="text-center">头像</th>
          <th class="text-center">状态</th>
          <th class="text-center">操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="user in userlist" :key="user.id">
          <td class="tableUint lh-lg text-center" style="cursor: pointer" @click="link(user.username)">{{user.username}}</td>
          <td class="tableUint lh-lg text-truncate text-center">{{user.sex===0?'未知':(user.sex===1?'男':'女')}}</td>
          <td class="lh-lg text-center" style="width: 200px">{{user.email}}</td>
          <td style="width: 200px" class="text-center">
            <a :href="user.avatar" target="_blank">
              <img :src="user.avatar" alt="..." style="width: 100px;height: 100px" class="object-fit-cover rounded-1">
            </a>
          </td>
          <td style="width: 100px" :class="user.status===1?'text-success':'text-danger'">{{user.status===1?'正常':'异常'}}</td>
          <td class="text-center">
              <el-button type="warning" class="my-2" @click="userStateChanged(user)">修改状态</el-button>
              <el-button type="danger" @click="deleteById(user.id)">删除用户</el-button>
          </td>
        </tr>
      </tbody>
    </table>
<!--     分页-->
     <el-pagination
         @size-change="handleSizeChange"
         @current-change="handleCurrentChange"
         :current-page="queryInfo.pagenum"
         :page-sizes="[5, 10, 15]"
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
  computed:{
    sex(val){
      if(val===1){
        return "男"
      }
      else if(val===2){
        return "女"
      }
      return "未知"
    },

    status(val){
      if(val===1){
        return '正常'
      }else{
        return '异常'
      }
    },
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
    deleteById(id){
      this.$http.delete('/admin/user/'+id).then(res=>{
        this.$message.info(res.data.message)
      })
      this.userlist = this.userlist.filter(x=>x.id!==id)
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
.mytable{
  min-height: 800px;
}
.tableUint{
  width: 150px;
}
.unitAvatar{
  width: 400px;
}

</style>
