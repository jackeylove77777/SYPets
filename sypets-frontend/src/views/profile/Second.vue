<template>
  <div>
    <h3>{{sex+'的收藏'}}</h3>
    <el-timeline>
      <div v-for="(item,index) in list" >
        <el-timeline-item  :timestamp="'收藏:'+timeList[index]" placement="top">
          <Item  @unCollects="unCollects" :item="item" :user="user" :IsMe="IsMe"></Item>
        </el-timeline-item>
      </div>
    </el-timeline>
    <!-- 没有数据则空空如也 -->
    <div v-show="list.length===0" style="text-align: center;margin-top: 10%;margin-bottom: 10%">空空如也</div>
  </div>
</template>

<script>
import Item from "./Item.vue";
export default {
  name: "Second",
  components:{Item},
  data(){
    return{
      list:[],
      timeList:[]
    }
  },
  props:{
    user:{},
    IsMe:false
  },
  methods:{
    getData(){
      this.$http.get("/profile/findCollect/"+this.user.id).then(res=>{
        if(res.data.status===200) {
          this.list = res.data.data.postList
          this.timeList = res.data.data.collectTime
        }
      })
    },
    unCollects(id){
      for(let i=0;i<this.list.length;i++){
        if(this.list[i].id===id){
          this.list.splice(i,1)
          this.timeList.splice(i,1)
          break
        }
      }
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