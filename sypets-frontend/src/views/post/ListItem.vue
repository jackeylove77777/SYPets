<template>
  <div>
    <el-timeline-item  :timestamp="item.createTime" placement="top">
      <el-card>
        <div class="user" @click="visitUser(item.username)">
          <el-link>
            <el-avatar :src="item.avatar"></el-avatar>
            <span>{{item.username}}</span>
          </el-link>
        </div>
        <h4>标题:{{item.title}}</h4>
        <p>描述:{{item.description}}</p>

        <el-row :gutter="10">
          <el-col :span="5">
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
        </el-row>
      </el-card>
    </el-timeline-item>
  </div>

</template>

<script>
export default {
  name: "ListItem",
  props:{item:{}},
  methods:{
    visitUser(username){
      this.$router.push('/profile/'+username)
    }
  }
}
</script>

<style scoped>

</style>