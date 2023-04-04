<template>
  <div>
    <div class="el-card mt-3" @click="showDialog=true">
      <div class="info-body" >
        <div class="info-row">
          <div class="info-header">
            <div>
              <el-button type="primary" plain disabled class="btn-label">发布用户</el-button>
              <span class="mx-2 username" @click="visitUser(item.username)">{{item.username}}</span>
            </div>
            <div>
              <span class="username mx-2 delete-text" v-if="IsMe&&homepage" @click.stop="deleted">删除寻宠信息</span>
            </div>
          </div>
        </div>
        <div class="info-row">
          <el-button type="primary"  plain disabled class="btn-label">&nbsp;&nbsp;&nbsp;&nbsp;描述&nbsp;&nbsp;&nbsp;</el-button>
          <span class="mx-2 info" >{{item.description}}</span>
        </div>
        <div class="info-row">
          <el-button type="primary"  plain disabled class="btn-label">发布时间</el-button>
          <span class="mx-2 info">{{item.publishTime}}</span>
        </div>
        <div class="info-row">
          <el-button type="primary"  plain disabled class="btn-label">走丢时间</el-button>
          <span class="mx-2 info">{{item.lostTime.substring(0,10)}}</span>
        </div>
        <div class="info-row">
          <el-button type="primary"  plain disabled class="btn-label">&nbsp;&nbsp;&nbsp;&nbsp;地址&nbsp;&nbsp;&nbsp;</el-button>
          <span class="mx-2 info">{{item.address}}</span>
        </div>
        <div class="info-row">
          <el-button type="primary"  plain disabled class="btn-label">联系方式</el-button>
          <span class="mx-2 info">{{item.contaction}}</span>
        </div>
      </div>
      <div class="image-box">
        <img :src="item.image" class="image">
      </div>

    </div>

    <el-dialog
        :visible.sync="showDialog"
        width="30%">
      <h3>描述</h3>
      <p>{{item.description}}</p>
      <span slot="footer" class="dialog-footer">
        <el-button @click="showDialog = false">取 消</el-button>
        <el-button type="primary" @click="showDialog = false">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<style scoped>
.delete-text{
  color: orangered;
}
.info-header{
  display: flex;
  justify-content: space-between;
}
.btn-label{
  margin-top: 1px;
  font-size: 14px;
  padding: 7px;
}
.info-row{
  display: flex;
  flex-direction: row;
  align-items: center;
}
.info{
  font-size: 18px;
  display: -webkit-box;
  overflow: hidden;
  text-overflow: ellipsis;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 1;
}
.username{
  cursor: pointer;
  font-size: 18px;
}
.info-body{
  padding: 10px;
  width: 70%;
}
.el-card:hover{
  box-shadow: 0 17px 17px -5px rgba(38, 154, 232, 0.25), 12px 8px 8px -8px rgba(213, 228, 72, 0.3);
  transform:translate(0,-10px)
}
.el-card{
  width: 90%;
  height: 200px;
  margin: auto;
  background-color: #F2F3F5;
  display: flex;
  justify-content: space-between;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1)
}
.image-box{
  overflow: hidden;
  height: 200px;
  width: 30%;
}
.image{
  height: 250px;
  width: 330px;
  object-fit: cover;
  transition: all 1s;
}
.image:hover{
  transform: scale(1.25);
}

</style>
<script>
export default {
  name: "FosterItem",
  data(){
    return{
      showDialog:false,
    }
  },
  props:{
    item:{},
    homepage:false,
    user:{},
    IsMe:false
  },
  methods:{
    visitUser(username){
      this.$router.push('/profile/'+username)
    },
    deleted(id){
      this.dialogVisible=true
      this.handleClose(id,'确认删除？')
    },
    handleClose(id,msg) {
      this.$confirm(msg)
          .then(_ => {
            this.$http.delete('/findpet/'+this.item.id).then(res=>{
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
