<template>
  <div class="login_container">
    <div class="login_box">
      <div class="avatar_box">
        <img src="../assets/login-cat.jpg" alt="">
      </div>
      <el-form ref="loginFormRef" label-width="0" class="login_form" :model="loginForm" :rules="rules">
        <el-form-item prop="username" >
          <el-input prefix-icon="iconfont icon-user" placeholder="用户名" v-model.trim="loginForm.username" ></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input type="password" show-password prefix-icon="iconfont icon-3702mima" placeholder="密码" v-model.trim="loginForm.password"></el-input>
        </el-form-item>
        <el-form-item class="btns">
          <el-button type="primary" @click="login('loginFormRef')">登录</el-button>
          <el-button type="info" @click="resetLoginFrom('loginFormRef')">重置</el-button>
        </el-form-item>
        <div style="display: flex;justify-content: space-between">
          <el-link type="primary" @click="dialogFormVisible=true">找回密码</el-link>
          <el-link type="primary" @click="toRegister()">立即注册</el-link>
        </div>
      </el-form>
    </div>



    <el-dialog title="找回密码"  width="25%" :visible.sync="dialogFormVisible">
      <el-form  :rules="rule" ref="formRef" :model="form" label-width="0">
        <el-form-item prop="username">
          <el-input  prefix-icon="iconfont icon-user" placeholder="用户名" v-model.trim="form.username" ></el-input>
        </el-form-item>
        <el-form-item prop="email">
          <el-input prefix-icon="el-icon-phone" placeholder="请输入邮箱" @click="getCode" v-model="form.email"></el-input>
        </el-form-item>
        <el-form-item >
          <div v-show="show">
            <el-button :disabled="disable" @click="validateEmail('formRef')" type="primary">找回密码</el-button>
          </div>
          <div v-show="!show">
            <span>{{count+'s后再获取'}}</span>
          </div>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Login",
  data(){
    let userEmail = (rule, value, callback) => {
      //邮箱验证正则表达式
      const mailReg = /[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?/
      if (!value) {
        return callback(new Error('邮箱不能为空'))
      }
      setTimeout(() => {
        if (mailReg.test(value)) {
          callback()
        } else {
          callback(new Error('请输入正确的邮箱'))
        }
      }, 1000)
    };
    return{
      dialogFormVisible:false,
      show:true,
      timer: null,
      count:0,
      disable:false,
      form:{
        username:'',
        email:'',
      },
      rule:{
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
          {min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur'}
        ],

        email: [
          {validator:userEmail}
        ],

      },
      loginForm:{
        username:'',
        password:''
      },
      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
          {min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 6, max: 15, message: '长度在 6 到 15 个字符', trigger: 'blur'}
        ],
      }
    }
  },
  methods:{
    resetLoginFrom(loginFormRef){
      this.$refs[loginFormRef].resetFields()
    },
    login(formName){
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          const _this=this
          _this.loginForm.username=_this.loginForm.username.trim()
          _this.loginForm.password=_this.loginForm.password.trim()
          const {data:res}=await _this.$http.post('login',_this.loginForm)

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
            this.$store.commit("SET_USERINFO",res.data.user)
            this.$store.commit("SET_TOKEN",res.data.token)
            if(res.data.user==='admin'){

            }
            else
              this.$router.push("/home")
          }

        } else {
          return false;
        }
      });
    },
    toRegister(){
      this.$router.push('/register')
    },
    validateEmail(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.getCode()
        } else {
          this.$message.error('请填写完再提交!!');
          return false;
        }
      });
    },

    getCode(){
      this.disable=true
      this.$http.post("/findPassword",this.form).then(res=>{
        this.disable=false
        if(res.data.status===200){
          this.$message.success(res.data.message)
          this.dialogFormVisible=false
          this.form={}
        }
        else{

          this.$message.error(res.data.message)
          return false
        }
        const TIME_COUNT = 60;
        if (!this.timer) {
          this.count = TIME_COUNT;
          this.show = false;
          this.timer = setInterval(() => {
            if (this.count > 0 && this.count <= TIME_COUNT) {
              this.count--;
            } else {
              this.show = true;
              clearInterval(this.timer);
              this.timer = null;
            }
          }, 1000)
        }
      })


    }
  }
}
</script>

<style lang="less" scoped>
.login_container{
  background: #2b4b6b;
  height: 100%;
  background-image: url("../assets/bg-cat.jpg");
  background-size: cover;
}
.login_box{
  width: 450px;
  height: 350px;
  background-color: #fff;
  border-radius: 3px;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%,-50%);
}
.avatar_box{
  height: 130px;
  width: 130px;
  border: 1px solid #eee;
  border-radius: 50%;
  padding: 10px;
  box-shadow: 0 0 10px;
  position: absolute;
  left: 50%;
  transform: translate(-50%,-50%);
  background-color: #fff;
  img{
    width: 100%;
    height: 100%;
    border-radius: 50%;
    background-color: #eee;
  }
}
.btns{
  display: flex;
  justify-content: flex-end;
}
.login_form{
  position: absolute;
  bottom: 0;
  width: 100%;
  padding: 0 20px;
  box-sizing: border-box;
}
</style>