<template>
  <div class="content">

    <el-page-header @back="goBack" content="修改文章"/>

    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px"  class="demo-ruleForm">
      <el-form-item label="标题" prop="title">
        <el-input v-model="ruleForm.title"></el-input>
      </el-form-item>
      <el-form-item label="摘要" prop="description">
        <el-input type="textarea" v-model="ruleForm.description"></el-input>
      </el-form-item>
      <el-form-item label="分类" prop="type_id">
        <el-select v-model="ruleForm.type_id" default-first-option placeholder="文章类型" >
          <el-option v-for="item in typeList" :label="item.name" :value="item.id"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="内容"  prop="content">
        <mavon-editor  @imgAdd="ImgAdd"
                       @imgDel="ImgDel"
                       ref="md"
                       type="textarea"  v-model="ruleForm.content">
        </mavon-editor>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="submitForm('ruleForm')">修改</el-button>
      </el-form-item>
      <span></span>
    </el-form>
  </div>
</template>

<script>
export default {
  name: "Update",
  data(){
    return {
      typeList:[],
      img_file:[],
      ruleForm:{},
      rules: {
        title: [
          { required: true, message: '请输入标题', trigger: 'blur' },
          { min: 3, max: 25, message: '长度在 3 到 25 个字符', trigger: 'blur' }
        ],
        description: [
          { required: true, message: '请输入摘要', trigger: 'blur' }
        ],
        content: [
          { required: true, message: '请输入内容', trigger: 'blur' }
        ],
        type_id:[
          { required: true, message: '请选择类型', trigger: 'change' }
        ]
      }
    }
  },
  methods:{
    goBack(){
      this.$router.back()
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          const _this = this
          _this.$http.put("/post/update", this.ruleForm, {}).then(res => {
            if(res.data.status==200){
              _this.$message.success(res.data.message)
              _this.$bus.$emit('updateList')
              _this.$router.back()
            }
          })

        } else {
          return false;
        }
      })
    },
    ImgAdd(pos ,$file){
      let formdata = new FormData();
      formdata.append('file', $file);
      this.img_file[pos] = $file;
      this.$http({
        url: '/qiniu/upload',
        method: 'post',
        data: formdata,
        headers: { 'Content-Type': 'multipart/form-data' },
      }).then((res) => {
        this.$refs.md.$img2Url(pos,res.data.data.key)
      })
    },
    ImgDel(pos){
      let key=pos[0]
      delete this.img_file[pos];
      key=key.substring(36)
      this.$http.get("/qiniu/delete/"+key)
    }
  },

  created() {
    //获取路由的文章Id
    let id=this.$route.params.id
    let _this=this
    this.$http.get('/post/findPostByIds/'+id).then(res=>{
      if(res.data.status===200){
        if(res.data.data.data.uid===_this.$store.getters.getUser.id&&res.data.data.data.author===1){
          _this.ruleForm=res.data.data.data
        }
        else{
          _this.$message.error("无权限操作")
          setTimeout(()=>{
            _this.$router.back()
          },2000)
        }
      }else{

        _this.$message.error("文章不存在或者已被删除")
        setTimeout(()=>{
          _this.$router.back()
        },2000)

      }
    })
    this.$http.get("/type/postTypeList").then(res=>{
      this.typeList=res.data.data.typeList
    })
  }
}
</script>

<style scoped>
.content{
  height: 110%;
}
.el-form{
  margin-top: 20px;
  margin-right: 50px;
}
</style>