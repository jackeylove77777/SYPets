<template>
  <div>
    <div class="comment-body" v-for="item in list" :key="item.id">
      <div @click="visitUser(item.uname)" class="user-info">
        <el-avatar :src="item.avatar" style="width: 24px;height: 24px;"/>
      </div>
      <div class="comment-text">
        <div class="user-info">
          <span style="font-size: 15px">{{item.uname}}</span>
          <span class="mx-1" style="color: #8a919f; font-size: 14px">{{item.createTime}}</span>
        </div>
        <span class="comment">{{item.content}}</span>
      </div>
    </div>
  </div>
</template>
<style scoped>
.comment-body{
  display: flex;
  flex-direction: row;
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
export default {
  name: "Answer",
  //list是回复数组
  props:{
    list:[]
  },
  methods:{
    visitUser(username) {
      this.$router.push("/profile/" + username)
    },
  }
}
</script>

<style scoped>

</style>