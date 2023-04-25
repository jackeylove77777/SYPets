<template>
  <div>
    <el-card>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input placeholder="输入搜索内容" v-model="queryInfo.query" clearable @clear="getData">
            <el-button slot="append" icon="el-icon-search" @click="getData"></el-button>
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
        <th class="text-center">描述</th>
        <th class="text-center">发布时间</th>
        <th class="text-center">走丢时间</th>
        <th class="text-center">图片</th>
        <th class="text-center">地点</th>
        <th class="text-center">联系方式</th>
        <th class="text-center">是否解决</th>
        <th class="text-center">状态</th>
        <th class="text-center">操作</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="item in list" :key="item.id">
        <td class="tableUint">{{item.id}}</td>
        <td class="tableUint text-primary" style="cursor: pointer" @click="link(item.username)">{{item.username}}</td>
        <td class="tableUint">{{item.description}}</td>
        <td class="tableUint">{{item.publishTime}}</td>
        <td class="tableUint">{{item.lostTime}}</td>
        <td class="tableUint">
          <a :href="item.image" target="_blank">{{item.image}}</a>
        </td>
        <td class="tableUint">{{item.address}}</td>
        <td class="tableUint">{{item.contaction}}</td>
        <td class="tableUint">{{item.solve===1?'已找到':'未找到'}}</td>
        <td class="tableUint">{{item.status===1?'正常':'异常'}}</td>
        <td class="tableUint">
          <div class="op-col">
            <span @click="changeStatus(item)" class="op-text">修改状态</span>
            <span @click="deleteById(item.id)" class="delete-text">删除</span>
          </div>
        </td>
      </tr>
      </tbody>
    </table>
    <div v-if="list===undefined||list.length===0">
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
  name: "AdminFindPet",
  data(){
    return {
      queryInfo:{
        //当前页数
        pagenum: 1,
        // 每页显示多少数据
        pagesize: 10,
        query:''
      },
      list:[],
      total:0,
    }
  },
  methods:{
    getData(){
      this.$http.get("/findpet/all",{params:this.queryInfo}).then(res=>{
        this.list=res.data.data.list;
        this.total=res.data.data.total;
      })
    },
    changeStatus(item){
      this.$http.post("/findpet/admin/changeStatus/"+item.id).then(res=>{
        if(res.data.status==200){
          if(item.status===1)
            item.status=0
          else
            item.status=1
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
      this.getData()
    },
    // 监听页码值改变
    handleCurrentChange (newNum) {
      this.queryInfo.pagenum = newNum
      this.getData()
    },
    deleteById(id){
      this.$confirm('确定删除该信息吗')
          .then(_ => {
            this.$http.delete('/findpet/admin/'+id).then(res=>{
              this.$message.info(res.data.message)
              this.list = this.list.filter(x=>x.id!==id)
            })
          })
          .catch(_ => {});
    },
    link(data){
      this.$router.push('/profile/'+data)
    }
  },
  mounted() {
    this.getData()
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
