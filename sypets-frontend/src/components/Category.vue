<template>
  <div class="category-box rounded">
    <el-menu>
      <el-menu-item index="2" v-for="type in typeList" :key="type.id" @click="clickItem(type.name)">
        <span slot="title">{{type.name +':'+type.type_num}}</span>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script>
export default {
  name: "Category",
  data(){
    return{
      typeList:[],
      color:['success','info','warning','danger','','']
    }
  },
  methods:{
    clickItem(name){
      this.$router.push('/category/'+name)
    },
    getData(){
      this.$http.get('/type/postTypeNum').then(res=>{
        if(res.data.status==200){
          this.typeList = res.data.data.typeList
        }
      })
    },
  },
  created(){
    this.getData()
  },
  mounted(){
    this.$bus.$on('updateList',()=>{
      this.getData()
    })
  },
  beforeDestroy(){
    this.$bus.$off('updateList')
  }
}
</script>

<style scoped>
.category{
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
}
.category-box{
  height: auto;
  background-color: white;
  padding: 8px;
}
</style>