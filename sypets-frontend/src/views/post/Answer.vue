<template>
  <div>
    <el-timeline v-for="item in list" :key="item.id" style="margin-top: 2%">

      <el-timeline-item :timestamp="item.time" placement="top">
        <el-link @click="visitUser(item.uname)">
          <el-avatar :src="item.avatar" />
          <span>{{item.uname}}</span>
        </el-link>

        <div>{{item.content}}</div>
      </el-timeline-item>
    </el-timeline>
  </div>
</template>

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