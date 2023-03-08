<template>
  <div class="block">
    <PageHeader v-show="header1">
      <el-page-header @back="goBack" :content="header2">
      </el-page-header>
    </PageHeader>

    <el-timeline>
      <ListItem v-for="item in list" :item="item"></ListItem>
    </el-timeline>
    <div v-show="total===0" style="text-align: center">
      暂无内容
    </div>

    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryInfo.pagenum"
        :page-sizes="[1,3, 5]"
        :page-size="queryInfo.pagesize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        class="el-pagination"
    >
    </el-pagination>

  </div>
</template>

<script>
import ListItem from "./ListItem.vue";
import PageHeader from "../../components/PageHeader.vue";
export default {
  name: "List",
  components:{ListItem,PageHeader},
  data(){
    return{
      list:[],
      queryInfo:{
        query:'',
        pagenum:1,
        pagesize:5
      },
      total:0,
      asycUrl:''
    }
  },
  props:{
    url:'',
    header1:'',
    header2:''
  },
  methods:{
    goBack(){
      this.$router.back()
    },
    getList(){
      this.$http.get(this.asycUrl===''?this.url:this.asycUrl,{params:this.queryInfo}).then(res=>{
        if(res.data.status==200){
          this.list=res.data.data.postList.list
          this.total=res.data.data.postList.total
        }
        else {
          this.$message.error(res.data.message)
          setTimeout(()=>{
            this.goBack()
          },3000)

        }
      })
    },
    handleSizeChange(newSize){
      this.queryInfo.pagesize=newSize
      this.getList()
    },
    handleCurrentChange(newPage){
      this.queryInfo.pagenum=newPage
      this.getList()
    },
  },
  mounted() {
    this.getList()
  }
}
</script>

<style scoped>

.el-pagination{
  margin: auto;
  text-align: center;
}

</style>