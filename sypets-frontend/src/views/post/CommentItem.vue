<template>
  <div class="comment-body">
      <div @click="visitUser(item.uname)" class="user-info">
        <el-avatar :src="item.avatar" />
      </div>
      <div class="comment-text">
        <div class="user-info">
          <span style="font-size: 15px">{{item.uname}}</span>
          <span class="mx-1" style="color: #8a919f; font-size: 14px">{{item.createTime}}</span>
        </div>
        <span @click="open()" class="comment">{{item.content}}</span>
        <!-- 回复列表 -->
        <div class="answer-list" v-show="item.answerList.length>0">
          <Answer :list="item.answerList"></Answer>
        </div>
      </div>
  </div>
</template>
<style scoped>
.answer-list{
  padding: 20px 12px 12px;
  background-color: #f9fafb;
}
.comment-body{
  display: flex;
  flex-direction: row;
  margin-top: 20px;
}
.comment-text{
  display: flex;
  flex-direction: column;
  margin-left: 10px;
  width: 100%;
}
.comment{
  font-weight: 400;
  font-size: 14px;
  line-height: 2rem;
  color: #515767;
  margin-top: 8px;
}
.user-info{
  display: flex;
  justify-content: space-between;
}
</style>
<script>
import Answer from "./Answer.vue";
export default {
  name: "CommentItem",
  components:{Answer},
  props:{
    item:{
      answerList:[]
    }
  },
  methods:{
    visitUser(username){
      this.$router.push("/profile/"+username)
    },
    open(){
      //$prompt是一个弹窗
      this.$prompt('回复'+this.item.uname,'提示',{
        confirmButtonText:'确定',
        cancelButtonText:'取消',
        inputValidator:(value)=>{
          return value.length>=3&&value.length<=50
        },
        inputErrorMessage:'长度3-50'
      }).then(({value})=>{
        //回复这个评论，并在回复数组首部添加新的回复
        this.$http.post("/answer/"+this.item.id,{'content':value}).then(res=>{
          if(res.data.status===200){
            this.item.answerList.splice(0,0,res.data.data.answer)

            this.$message({
              type: 'success',
              message: '回复成功'
            });
          }
        })
      })
    },
  }
}
</script>
