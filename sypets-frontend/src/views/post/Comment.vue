<template>
  <div>
    <el-button style="margin-bottom: 10px" type="success" @click="open">我要评论</el-button>
<!--    评论列表-->
    <el-timeline v-for="item in list">
      <el-timeline-item :timestamp="item.time" placement="top">
        <el-card>
          <CommentItem :item="item"/>
        </el-card>
      </el-timeline-item>
    </el-timeline>
    <div v-show="list.length===0" style="text-align: center;margin-top: 10%;margin-bottom: 10%">暂无评论</div>
  </div>
</template>

<script>
import CommentItem from "./CommentItem.vue";
export default {
  name: "Comment",
  components:{CommentItem},
  data(){
    return{
      id:this.$route.params.postId,
      list:[]
    }
  },
  methods: {
    open() {
      this.$prompt('请输入评论内容', '提示', {
        confirmButtonText: '评论',
        cancelButtonText: '取消',
        inputValidator:value =>{
          return value.length>=3&&value.length<=50
        } ,
        inputErrorMessage: '长度为3-50'
      }).then(({ value }) => {
        this.$http.post('/comment/'+this.id,{'content':value}).then(res=>{
          if(res.data.status===200){
            this.list.splice(0,0, res.data.data.item)
            this.$message({
              type: 'success',
              message: '评论成功'
            });
          }

        })


      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消输入'
        });
      });
    },
    getData(){
      this.$http.get("/findCommentByPid/"+this.id).then(res=>{
        if(res.data.status==200){
          this.list=res.data.data.list
        }
      })
    }
  },
  mounted() {
    this.getData()
  }
}
</script>
