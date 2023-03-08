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
        <el-select v-model="ruleForm.type_id" default-first-option placeholder="文章类型">
          <el-option v-for="item in typeList" :label="item.name" :value="item.id"></el-option>
        </el-select>
      </el-form-item>
<!--      富文本编辑器-->
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
  name: "EditPost",
  data(){
    return{
      typeList:[],
      img_file:[],
      ruleForm:{
        id:'',
        title:'',
        description:'',
        content:'',
        type_id:'',
      },
      rules: {
        title: [
          { required: true, message: '请输入标题', trigger: 'blur' },
          { min: 2, max: 25, message: '长度在 2 到 25 个字符', trigger: 'blur' }
        ],
        description: [
          { required: true, message: '请输入摘要', trigger: 'blur' }
        ],
        content: [
          { required: true, message: '请输入内容', trigger: 'blur' },

        ],
        type_id:[
          { required: true, message: '请选择类型', trigger: 'change' }
        ]
      }
    }
  },

  methods:{
    goBack() {
      this.$router.back()
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          const _this = this

          _this.$http.post("/post/add", this.ruleForm, {}).then(res => {
            if(res.data.status==200){
              _this.$bus.$emit('updateList')
              _this.$message.success(res.data.message)
              _this.$router.push("/detail/"+res.data.data.id)
            }
            else {
              _this.$message.error(res.data.message)
            }
          })
        } else {
          return false;
        }
      })
    },
    //上传图片
    ImgAdd(pos,_file){
      let formdata = new FormData();
      formdata.append('file',_file)
      this.img_file[pos] = _file
      this.$http({
        url:'/qiniu/upload',
        method:'post',
        data:formdata,
        headers: { 'Content-Type': 'multipart/form-data'}
      }).then(res=>{
        if(res.data.status===200)
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
    this.$http.get("/type/postTypeList").then(res=>{
      this.typeList=res.data.data.typeList
    })
  },
}
</script>

<style scoped>
.el-form{
  margin-top: 20px;
  margin-right: 50px;
}
</style>