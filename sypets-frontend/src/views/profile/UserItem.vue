<template>
  <div>
    <el-divider class="divider"></el-divider>
    <div class="user-item">
      <div>
        <el-image
            style="width: 70px; height: 70px" class="user-avatar" @click="toUser"
            :src="item.avatar"></el-image>
      </div>
      <div class="user-info">
        <span style="font-size: 20px">{{item.username}}</span>
        <span style="color: #8A919F">{{item.privateInfo}}</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "UserItem",
  props:{
    item:{}
  },
  methods:{
    toUser(username){
      this.$router.push("/profile/"+this.item.username)
    }
  }
}
</script>

<style scoped>
.user-item{
  display: flex;
  flex-direction: row;
  padding: 10px 20px;
}
.user-avatar{
  border-radius: 50%;
}
.divider{
  width: 90%;
  margin: 0 auto 5px;
}
.user-info{
  margin-left: 10px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

</style>