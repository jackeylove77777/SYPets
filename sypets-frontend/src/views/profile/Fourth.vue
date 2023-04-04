<template>
  <div>
    <h3>{{sex+'的关注'}}</h3>
    <div v-for="item in list">
      <UserItem :item="item" />
    </div>
    <div v-show="list.length===0" style="text-align: center;margin-top: 10%;margin-bottom: 10%;">
      <el-empty description="暂无内容"></el-empty>
    </div>
  </div>
</template>

<script>
import UserItem from "./UserItem.vue";
export default {
  name: "Fourth",
  components:{
    UserItem
  },
  data(){
    return{
      list:[]
    }
  },
  props:{
    IsMe:false,
    user:{}
  },
  watch:{
    user:{
      handler(){
        this.getData()
      },
      deep:true
    }
  },
  computed:{
    sex(){
      if(this.IsMe){
        return '我'
      }
      if(this.user.sex===0)
        return this.user.username
      else if(this.user.sex===1)
        return '他'
      return '她'
    }
  },
  methods:{
    getData(){
      this.$http.get('/profile/findFollowings/'+this.user.id).then(res=>{
        console.log(res)
        if(res.data.status===200) {
          this.list = res.data.data.followers
        }
      })
    }
  },
  mounted() {
    this.getData()
    this.$bus.$on("followAction",()=>{

      this.getData()
    })
  },
  beforeDestroy(){
    this.$bus.$off("followAction")
  }
}
</script>
