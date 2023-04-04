<template>
  <div>
    <!--    顶部-->
    <div class="header">
      <!--      搜索栏-->
      <div class="search">
        <el-input clearable v-model="queryInfo.query" maxlength="15"
                  show-word-limit style="width: 40%"  placeholder="请输入搜索内容"></el-input>
        <el-button type="primary" @click="getList" style="height: 40px;margin-left: 5px" icon="el-icon-search"></el-button>
      </div>
      <!--      新增按钮-->
      <el-button type="primary" @click="dialogFormVisible=true" style="height: 40px;margin-left: 5px"  icon="el-icon-circle-plus-outline">发布流浪动物信息</el-button>
    </div>

    <!--    内容主体-->
    <div class="mx-lg-2 mx-md-1 mt-4">
      <StrayItem v-for="item in list" :item="item" :key="item.id"></StrayItem>
    </div>
    <div v-show="total===0" style="text-align: center">
      <el-empty description="暂无内容"></el-empty>
    </div>
    <!--    分页-->
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryInfo.pagenum"
        :page-sizes="[ 5,10,20]"
        :page-size="queryInfo.pagesize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        class="el-pagination mt-4"/>

    <!--点击新增按钮弹出的对话框-->
    <el-dialog title="发布流浪动物信息"  width="30%" :visible.sync="dialogFormVisible" class="rounded-4">
      <el-form  :rules="rule" ref="formRef" :model="form" label-width="0">
        <el-form-item prop="description">
          <el-input  type="textarea" placeholder="描述" v-model.trim="form.description" ></el-input>
        </el-form-item>
        <el-form-item prop="address">
          <el-input  prefix-icon="el-icon-location" placeholder="发现地点" v-model="form.address" ></el-input>
        </el-form-item>

        <el-upload
            class="avatar-uploader"
            action=""
            list-type="picture-card"
            :show-file-list="false"
            :http-request="upAvatar"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload">
          <img v-if="form.image" :src="form.image" class="avatar">
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
        <div class="form-btn">
          <el-button class="mx-2" type="primary" @click="addNew">确定</el-button>
          <el-button class="mx-2" @click="cancel">取消</el-button>
        </div>
      </el-form>
    </el-dialog>

  </div>
</template>

<script>
import StrayItem from "./StrayItem.vue";
export default {
  name: "StrayList",
  components:{
    StrayItem
  },
  data(){
    return{
      list:[],
      queryInfo:{
        query:'',
        pagenum:1,
        pagesize:5
      },
      total:0,
      dialogFormVisible:false,
      form:{
        description:'',
        address:'',
        image:''
      },
      img_file:[],
      rule:{
        description: [
          {required: true, message: '请输入描述', trigger: 'blur'},
          {min: 3, max: 100, message: '长度在 3 到 100 个字符', trigger: 'blur'}
        ],
        address: [
          {required: true, message: '请输入联系方式', trigger: 'blur'},
          {min: 3, max: 50, message: '长度在 3 到 50 个字符', trigger: 'blur'}
        ],
      },
    }
  },
  methods:{
    getList(){
      this.$http.get('/stray/all',{params:this.queryInfo}).then(res=>{
        if(res.data.status==200){
          this.list=res.data.data.list
          this.total=res.data.data.total
        }
        else {
          this.$message.error(res.data.message)
          // setTimeout(()=>{
          //   this.$router.push('/')
          // },3000)
        }
      })
    },
    handleSizeChange(newSize){
      this.queryInfo.pagesize=newSize
      this.getList()
    },
    handleCurrentChange(newPage){
      this.queryInfo.pagenum=newPage
      this.getList()
    },
    handleAvatarSuccess() {
      this.$message.success('success')
    },
    //上传头像之前的验证
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg';
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 格式!');
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
      }
      return isJPG && isLt2M;
    },
    upAvatar(item){
      let formdata = new FormData();
      formdata.append('file', item.file);
      this.img_file[0] = item.file;
      this.$http({
        url: '/qiniu/upload',
        method: 'post',
        data: formdata,
        headers: { 'Content-Type': 'multipart/form-data' },
      }).then((res) => {
        //删除成功后，删掉旧的图片
        if(res.data.status===200) {
          let oldKey=this.form.image
          this.form.image = res.data.data.key
          if(oldKey!=''){
            oldKey=oldKey.substring(36)
            this.$http.get("/qiniu/delete/"+oldKey)
          }
        }
        else{
          this.$message.error(res.data.message)
        }
      })
    },
    clearForm(){
      //首先删除图片
      let oldKey=this.form.image
      if(oldKey!=''){
        oldKey=oldKey.substring(36)
        this.$http.get("/qiniu/delete/"+oldKey)
      }
      this.form = {
        description:'',
        address:'',
        image:''
      }
    },
    cancel(){
      this.dialogFormVisible=false
      this.clearForm()
    },
    addNew(){
      this.$refs['formRef'].validate(async (valid) => {
        if (valid) {
          const {data:res}=await this.$http.post('/stray',this.form)

          if(res.status!==200){
            this.$message({
              showClose: true,
              message: res.message,
              type: 'error'
            });
          }else{
            this.$message({
              showClose: true,
              message: res.message,
              type: 'success'
            });
          }
        } else {
          this.$message({
            showClose: true,
            message: '输入不符合要求',
            type: 'error'
          });
        }
      });
      this.dialogFormVisible=false
      this.form = {
        description:'',
        address:'',
        image:''
      }
      this.getList()
    }
  },
  mounted() {
    this.getList()
  }
}
</script>

<style scoped>
.header{
  display: flex;
  justify-content: space-between;
  width: 90%;
  margin: auto;
}
.search{
  display: flex;
  margin-top: 5px;
  margin-left: 5px;
  flex: 3;
}
.el-pagination{
  text-align: center;
  margin: 20px auto auto;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 20px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
.form-btn{
  display: flex;
  flex-direction: row-reverse;
}
</style>