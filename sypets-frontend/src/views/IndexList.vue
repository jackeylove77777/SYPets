<template>
  <div>
    <List :url="'post/findAll'" :header1="'首页'" :header2="'文章列表'"></List>
  </div>
</template>

<script>
import List from "./post/List.vue";

export default {
  name: "IndexList",
  components: {List}
}
</script>

<style scoped>
.el-timeline{
  margin-top: 20px;
  margin-right: 50px;
}
</style>