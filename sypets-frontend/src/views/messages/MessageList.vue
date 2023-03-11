<template>
  <el-timeline style="margin-top: 20px">
    <el-timeline-item v-for="item in list" :timestamp="item.time" placement="top">
      <el-card>
        <el-badge v-show="item.status===1" value="new" />
        <p v-html="item.message"></p>
        <div class="btns">
          <el-button v-if="item.status===1" @click="read(item.id)" type="primary" plain>已读</el-button>
          <el-button type="danger"  @click="deleted(item.id)" icon="el-icon-delete"></el-button>
        </div>
      </el-card>
    </el-timeline-item>

  </el-timeline>
</template>

<script>
export default {
  name: "MessageList",
  data(){
    return {
      list:[]
    }
  },
  methods:{
    getData(){
      this.$http.get("/message/findMessageById/"+this.$route.params.id).then(res=>{
        this.list=res.data.data.list
      })
    },
    read(id){
      this.$http.get('/message/read/'+id).then(res=>{
        if(res.data.status===200){
          this.getData()
          this.$message.success('success')
        }else{
          this.$message.error('error')
        }
      })
    },
    deleted(id){
      this.$confirm('确定删除吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.get("/message/delete/"+id).then(res=>{

          if(res.data.status===200) {
            for(let i=0;i<this.list.length;i++){
              if(this.list[i].id===id){
                //删除掉这个元素
                this.list.splice(i,1)
                break
              }
            }
            this.$message({
              type: 'success',
              message: '删除成功!'
            });
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    }
  },
  mounted() {
    this.getData()
  },
  beforeRouteUpdate(to,from,next){
    next()
    this.getData()
  },
}
</script>

<style scoped>
.btns{
  display: flex;
  justify-content: flex-end;
}

</style>