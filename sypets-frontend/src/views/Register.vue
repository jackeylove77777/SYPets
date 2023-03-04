<template>
  <div class="login_container">
    <div style="text-align: center;color: #67C23A;font-size: 30px;">用户注册</div>
    <div class="login_box">
      <el-form ref="loginFormRef" label-width="0" class="login_form" :model="loginForm" :rules="rules">
        <el-form-item prop="username" >
          <el-input prefix-icon="iconfont icon-user" placeholder="用户名" v-model="loginForm.username" ></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input type="password" show-password prefix-icon="iconfont icon-3702mima" placeholder="密码" v-model="loginForm.password"></el-input>
        </el-form-item>
        <el-form-item prop="secondPassword">
          <el-input type="password" show-password prefix-icon="iconfont icon-3702mima" placeholder="确认密码" v-model="loginForm.secondPassword"></el-input>
        </el-form-item>

        <el-form-item prop="email">
          <el-input prefix-icon="el-icon-phone" placeholder="请输入邮箱" @click="getCode" v-model="loginForm.email"></el-input>
        </el-form-item>
        <el-form-item prop="code">
          <el-row :gutter="100">
            <el-col :span="10">
              <el-input style="width: 200px" maxlength="6" show-word-limit v-model="loginForm.code"  placeholder="请输入验证码" ></el-input>
            </el-col>
            <el-col :span="10">
              <div v-show="show">
                <el-button @click="validateEmail('loginFormRef')" :disabled="disable" type="primary">发送验证码</el-button>
              </div>
              <div v-show="!show">
                <span>{{count+'s后再获取'}}</span>
              </div>
            </el-col>
          </el-row>
        </el-form-item>

        <el-form-item class="btns">
          <el-button type="primary" @click="login('loginFormRef')">注册</el-button>
          <el-button type="info" @click="resetLoginFrom('loginFormRef')">重置</el-button>
        </el-form-item>
        <el-form-item>
          <router-link to="/login">
            <el-link type="primary" style="margin-left: 350px">去登陆</el-link>
          </router-link>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  name: "Login",
  data(){
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.loginForm.password) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };

    var userEmail = (rule, value, callback) => {
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
      loginForm:{
        username:'',
        password:'',
        secondPassword: '',
        email:'',
        code:'',
      },
      count:0,
      show:true,
      timer: null,
      disable:false,
      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
          {min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 6, max: 15, message: '长度在 6 到 15 个字符', trigger: 'blur'}
        ],
        secondPassword:[
          { validator: validatePass2, trigger: 'blur' },
        ],
        email: [
          {validator:userEmail}
        ],
        code:[
          {required: true, message: '请输入验证码', trigger: 'blur'},
          {min:6,max:6,message: "长度为6",trigger: 'blur'}
        ]
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
          console.log(_this.loginForm)
          const {data:res}=await _this.$http.post('/register',_this.loginForm)
          if(res.status!==200){
            this.$message({
              showClose: true,
              message: res.message,
              type: 'error'
            });
          }else{
            this.$message({
              showClose: true,
              message: "注册成功",
              type: 'success'
            });
            setTimeout(()=>{
              this.loginForm={
                username:'',
                password:'',
                secondPassword: '',
                email:'',
                code:'',
              }
              this.$router.push("/login")
            },3000)
          }
        } else {
          return false;
        }
      });
    },
    validateEmail(formName) {
      this.$refs[formName].validateField('email', emailError => {
        if (!emailError) {
          this.getCode()
        } else {
          this.$message.error("邮箱格式错误")
          return false;
        }
      });

    },
    getCode(){
      this.disable=true
      this.$http.post("/sendEmail",{"email":this.loginForm.email}).then(res=>{
        console.log(res)
        if(res.data.status===200) {
          this.$message.success(res.data.message)
        }
        else {
          this.disable=false
          this.$message.error(res.data.message)
          return false
        }
        this.disable=false
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
  height: 500px;
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