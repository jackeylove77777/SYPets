<template>
  <div class="mt-2">
    <div v-for="item in list" >
      <FosterItem  @delete="del" :item="item" :IsMe="IsMe" :homepage="true"></FosterItem>
    </div>
    <div v-show="list===undefined||list.length===0" style="text-align: center;margin-top: 10%;margin-bottom: 10%">
      <el-empty description="暂无内容"></el-empty>
    </div>
  </div>
</template>

<script>
import FosterItem from "../foster/FosterItem.vue";
export default {
  name: "UserFoster",
  components:{FosterItem},
  data(){
    return{
      list:[]
    }
  },
  props:{
    user:{},
    IsMe:false
  },
  methods:{
    getData(){
      this.$http.get('/foster/all/'+this.user.id).then(res=>{
        this.list=res.data.data.list
      })
    },
    del(id){
      for(let i=0;i<this.list.length;i++){
        if(this.list[i].id===id){
          this.list.splice(i,1)
          break
        }
      }
    }
  },
  mounted() {
    this.getData()
  },
}
</script>

<style scoped>

</style>