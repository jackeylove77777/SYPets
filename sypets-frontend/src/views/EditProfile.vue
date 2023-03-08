<template>
  <div>
    <h2 style="text-align: center">个人资料</h2>
    <div class="edit-profile">
      <el-upload
          class="avatar-uploader"
          action=""

          :show-file-list="false"
          :http-request="upAvatar"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload">
        <img v-if="user.avatar" :src="user.avatar" class="avatar">
        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
      </el-upload>

      <p>用户名:
        <span >{{user.username}}</span>
        <el-button style="margin-left: 5%" @click="editUserName(user.username)" type="primary" icon="el-icon-edit" size="mini" circle></el-button>
        <router-link style="margin-left: 50%" :to="'/profile/'+user.username"><el-link type="success">返回个人主页</el-link></router-link></p>
      <el-divider/>
      <p>性别:<span >{{sex}}</span> <el-button  @click="open()" style="margin-left: 5%" type="primary" icon="el-icon-edit" size="mini" circle></el-button></p>
      <el-divider/>
      <p>简介:<span >{{info}}</span> <el-button @click="editInfo(user.private_info)" style="margin-left: 5%" type="primary" icon="el-icon-edit" size="mini" circle></el-button></p>
      <el-divider/>
      <p>邮箱:<span >{{email}}</span></p>
      <el-divider/>
      <p>密码:<el-button style="margin-left: 10%" @click="diaPassword=true" type="primary" icon="el-icon-edit" size="mini" circle></el-button></p>


      <el-dialog title="修改性别" :visible.sync="dialogFormVisible">
        <el-radio v-model="userSex" label="1">男</el-radio>
        <el-radio v-model="userSex" label="2">女</el-radio>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="subSex()">保存</el-button>
        </div>
      </el-dialog>
<!--      修改密码弹窗-->
      <el-dialog title="修改密码" :visible.sync="diaPassword">
        <el-form :rules="rules" ref="passwordForm" :model="passwordForm">
          <el-form-item label="旧密码" prop="lastPassword" >
            <el-input type="password" show-password  v-model.trim="passwordForm.lastPassword" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="新密码"  prop="newPassword">
            <el-input type="password" show-password v-model.trim="passwordForm.newPassword" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="确认密码" prop="sencondPassword" >
            <el-input type="password" show-password v-model.trim="passwordForm.secondPassword" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item class="btns">
            <el-button type="primary" @click="subEditPassword('passwordForm')">修改</el-button>
            <el-button @click="diaPassword=false">取消</el-button>
          </el-form-item>
        </el-form>

      </el-dialog>
    </div>

  </div>
</template>

<script>
export default {
  name: "EditProfile",
  data(){
    let validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.passwordForm.newPassword) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    return {
      user:{},
      dialogFormVisible:false,
      userSex:0,
      passwordForm:{
        lastPassword:'',
        newPassword:'',
        secondPassword:''
      },
      img_file:[],
      rules:{
        lastPassword: [
          { required: true, message: '请输入旧密码', trigger: 'blur' },
          { min: 6, max: 15, message: '长度在 6 到 15 个字符', trigger: 'blur' }
        ],
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          { min: 6, max: 15, message: '长度在 6 到 15 个字符', trigger: 'blur' }
        ],
        secondPassword: [
          { validator: validatePass2, trigger: 'blur' },
        ],
      },
      diaPassword:false
    }
  },
  methods:{
    editUserName(userName){
      this.$prompt('输入修改的用户名', '修改用户名', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputValue:userName,
        inputValidator:value => {
          let x=value.trim()
          // return x.length===value.length
          return value.length>=3&&value.length<=20
        },
        inputErrorMessage: '用户名长度为3-20且前后不能带空格'
      }).then(({ value }) => {
        if(value===userName){
          this.$message.error('没有修改')
          return false
        }
        this.$http.get("/updateUserName?userName="+value).then(res=>{

          if(res.data.status===200){

            this.user.username=value
            this.$store.commit("SET_USERINFO",this.user)
            this.$message({
              type: 'success',
              message: res.data.message
            });

          }
          else{
            this.$message.error(res.data.message)
          }
        })
      })
    },
    open(){
      this.dialogFormVisible=true
      this.userSex=''+this.user.sex
    },
    //修改性别
    subSex(){
      this.$http.get("/updateSex?sex="+this.userSex).then(res=>{
        if(res.data.status===200){
          this.user.sex=parseInt(this.userSex)
          this.$store.commit("SET_USERINFO",this.user)
          this.$message.success(res.data.message)
          this.dialogFormVisible=false
        }
        else{
          this.$message.error(res.data.message)

        }
      })
    },
    //修改个人简介
    editInfo(info){
      this.$prompt('输入修改的简介', '修改个人简介', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputValue:info,
        inputValidator:value => {
          return value.length<=50
        },
        inputErrorMessage: '简介长度不超过50'
      }).then(({ value }) => {
        if(value===info){
          this.$message.error('没有修改')
          return false
        }
        this.$http.get("/updateInfo?info="+value).then(res=>{
          if(res.data.status===200){
            this.user.private_info=value
            this.$store.commit("SET_USERINFO",this.user)
            this.$message({
              type: 'success',
              message: '修改成功'
            });
          }
          else this.$message.error("修改失败")
        })

      })
    },
    subEditPassword(formName){
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$http.post("/updatePassword",this.passwordForm).then(res=>{

            if(res.data.status===200){
              this.$message.success("修改成功")
              this.$store.commit("REMOVE_INFO")
              this.diaPassword=false
              this.$router.push('/login')
            }
            else{
              this.$message.error(res.data.message)
            }
          })
        } else {
          console.log('error submit!!');
          return false;
        }
      });
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
        url: '/qiniu/upAvatar',
        method: 'post',
        data: formdata,
        headers: { 'Content-Type': 'multipart/form-data' },
      }).then((res) => {

        if(res.data.status===200) {
          let oldKey=this.user.avatar
          this.user.avatar = res.data.data.fileName
          this.$store.commit("SET_USERINFO", this.user)
          oldKey=oldKey.substring(36)
          this.$http.get("/qiniu/delete/"+oldKey)
        }
        else{
          this.$message.error(res.data.message)
        }
      })
    },
  },
  computed:{
    sex(){
      if(this.user.sex===1){
        return "男"
      }
      else if(this.user.sex===2){
        return "女"
      }
      return "未知"
    },
    info(){
      if(this.user.privateInfo){
        return this.user.privateInfo
      }
      return '暂无'
    },
    email(){
      if(this.user.email){
        return this.user.email
      }
      return '暂无'
    }
  },
  created() {
    this.user=this.$store.getters.getUser
    this.userSex=''+this.user.sex
  }
}
</script>

<style scoped>
.edit-profile{
  position: absolute;

  width: 80%;
  height: 100%;
  margin-left: 10%;
  box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .1)
}
p{
  margin-left: 200px;
}
p>span{
  margin-left: 10%
}
.btns{
  display: flex;
  justify-content: flex-end;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
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
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>