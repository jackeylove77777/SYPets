<template>
  <div>
    <h3>{{sex+'的动态'}}</h3>
    <el-timeline>
      <div v-for="item in list" >
        <Item  @delete="del" :item="item" :user="user" :IsMe="IsMe"></Item>
      </div>
    </el-timeline>
    <div v-show="list.length===0" style="text-align: center;margin-top: 10%;margin-bottom: 10%">空空如也</div>
  </div>
</template>

<script>
import Item from "./Item.vue";
export default {
  name: "First",
  components:{Item},
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
      this.$http.get('/profile/findPostById/'+this.user.id).then(res=>{
        this.list=res.data.data.postList
      })
    },
    del(id){
      for(let i=0;i<this.list.length;i++){
        if(this.list[i].id===id){
          this.list.splice(i,1)
          break
        }
      }
      this.$bus.$emit("updateList")
    }
  },
  computed:{
    sex(){
      if(this.IsMe){
        return '我'
      }
      //1是男性
      if(this.user.sex===0)
        return this.user.username
      else if(this.user.sex===1)
        return '他'
      return '她'
    }
  },
  watch:{
    user:{
      handler(){
        this.getData()
      },
      deep:true
    }
  },
  mounted() {
    this.getData()
  },
}
</script>

<style scoped>

</style>