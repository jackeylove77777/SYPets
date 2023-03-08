<template>
  <el-card >
    <el-timeline-item  :timestamp="item.createTime" placement="top">
      <div class="user" >
        <el-avatar :src="item.avatar?item.avatar:user.avatar"></el-avatar>
        <span>{{item.username?item.username:user.username}}</span>
      </div>
      <h4>标题:{{item.title}}</h4>
      <p>描述:{{item.description}}</p>
      <el-row :gutter="10" class="row">
        <el-col :span="4">
          <router-link :to="{path:'/detail/'+item.id}" >
            <el-link type="info">详情</el-link>
          </router-link>
        </el-col>
        <el-col :span="5">
          <i class="el-icon-star-on">{{'点赞数量:'+item.starts}}</i>
        </el-col>
        <el-col :span="5">
          <i class="el-icon-star-off">{{'收藏数量:'+item.collects}}</i>
        </el-col>
        <el-col :span="4" v-if="IsMe&&!item.username&&item.author==1"><el-button type="primary" @click="edit(item.id)" size="mini" icon="el-icon-edit" circle></el-button></el-col>
        <el-col :span="4" v-if="IsMe&&!item.username"><el-button type="danger"  @click="deleted(item.id)" size="mini" icon="el-icon-delete" circle></el-button></el-col>
        <el-col :span="4" v-show="IsMe&&item.username">
          <el-tooltip class="item" effect="dark" content="点击取消收藏" placement="top">
            <el-button type="danger"  @click="unCollect(item.id)" size="mini" icon="el-icon-delete" circle></el-button>
          </el-tooltip>
        </el-col>
      </el-row>
    </el-timeline-item>

    <span slot="footer" class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
    </span>
  </el-card>
</template>

<script>
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
    IsMe:false
  },
  methods:{
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
.row{
  display: flex;

}
</style>