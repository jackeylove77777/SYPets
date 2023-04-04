<template>
  <div>
    <div class="header" >
      <el-badge v-for="item in headerData"  :value="item.value" class="item">
        <el-button @click="click(item.id)" size="small">{{item.name}}</el-button>
      </el-badge>
      <el-divider/>
    </div>
    <router-view/>
  </div>
</template>

<script>
export default {
  name: "Messages",
  data(){
    return {
      headerData:[],
      isFixed:false
    }
  },
  computed:{
    total(){
      let x = 0;
      for(let i = 0;i<this.headerData.length;i++){
        x+=this.headerData[i].value
      }
      return x
    }
  },
  methods:{
    click(id){
      console.log(id)
      this.$router.push('/messages/'+id)
    },

  },
  mounted() {

    this.$http.get("/message/findMessageTypeNum").then(res=>{
          this.headerData=res.data.data.data
        }
    )
    this.$bus.$on("change",id=>{
      if(this.headerData[id-1].value>0){
        this.headerData[id-1].value=this.headerData[id-1].value-1
      }
      if(this.total===0){
        this.$bus.$emit("noMessage")
      }

    })
  },
  beforeDestroy(){
    this.$bus.$off("change")
  },
}
</script>

<style scoped>
.item {
  margin-top: 10px;
  margin-right: 40px;
}
</style>