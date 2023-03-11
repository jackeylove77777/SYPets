<template>
  <div class="container">
    <div class="text-center mt-5">
      <img src="../assets/404.png" class="h-50 w-50">
    </div>
    <div class="text-center">
      <el-button @click="goHome" class="mt-5 p-1 mx-auto fs-3" type="primary">返回首页</el-button>
    </div>
  </div>
</template>

<script>
export default {
  name:"NotFound",
  methods:{
    goHome(){
      this.$router.push('/')
    }
  }
}
</script>