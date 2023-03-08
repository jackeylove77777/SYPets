<template>
  <div>
    <i class="el-icon-menu" style="margin-left: 0;color: #409EFF">文章类型</i>
    <div class="category">
      <el-tooltip v-for="(item,index) in typeList" :content="'点击查看'+item.name+'类型的文章'" placement="right" effect="light">
        <el-tag  @click="clickItem(item.name)" :type="color[index]">{{item.name +' '+item.type_num}}</el-tag>
      </el-tooltip>
    </div>

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
.el-tag{
  margin-top: 8px;
  margin-bottom: 8px;
}
</style>