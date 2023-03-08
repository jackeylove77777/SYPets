<template>
  <div>
    <div v-for="item in list" :key="item.id">
      <UserItem :item="item" />
    </div>
    <div v-show="list.length===0" style="text-align: center;margin-top: 10%;margin-bottom: 10%;">你来到了数据的荒漠</div>
  </div>
</template>

<script>
import UserItem from "../profile/UserItem.vue";
export default {
  name: "UserList",
  components:{UserItem},
  data(){
    return{
      list:[]
    }
  },
  methods:{
    getData(){
      this.$http.get("/following/findFollowings").then(res=>{
        if(res.data.status===200) {
          this.list = res.data.data.followers
        }
      })
    }
  },
  mounted() {
    this.getData()
  }
}
</script>
