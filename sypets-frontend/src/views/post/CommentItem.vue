<template>
  <div >
    <el-tooltip class="item" effect="dark" content="点击回复" placement="top">

      <div @click="open()">
        <el-link @click="visitUser(item.uname)">
          <el-avatar :src="item.avatar" />
          <span>{{item.uname}}</span>
        </el-link>
        <span style="margin-left: 3%">{{item.content}}</span>
      </div>
    </el-tooltip>
    <!-- 分隔符 -->
    <el-divider></el-divider>
    <!-- 回复列表 -->
    <Answer style="margin-left: 20%" :list="item.answerList"></Answer>
  </div>
</template>
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
