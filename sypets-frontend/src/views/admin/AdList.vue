<template>


<div>
  <el-card>
    <el-row :gutter="20">
      <el-col :span="6">
        <el-input placeholder="输入搜索内容" v-model="queryInfo.query" clearable @clear="getAdList">
          <el-button slot="append" icon="el-icon-search" @click="getAdList"></el-button>
        </el-input>
      </el-col>
      <el-col :span="6">
        <!-- 增加广告按钮 -->
        <el-button type="success" @click="dialogVisible = true">新增广告<i class="el-icon-upload el-icon--right"></i></el-button>
      </el-col>
    </el-row>
  </el-card>
  <el-dialog
      title="新增广告具体信息"
      :visible.sync="dialogVisible"
      width="40%"
      :before-close="handleClose">
    <div style="margin: 5px 0;"></div>
    <el-form :rules="rule" ref="formRef" :model="form" label-width="110px">
      <el-form-item label="广告名字：" prop="name">
        <el-input v-model="form.name" type="text" placeholder="请输入广告名字"></el-input>
      </el-form-item>
      <el-form-item label="广告描述：" prop="description">
        <el-input type="textarea" placeholder="请输入广告描述" v-model="form.description"></el-input>
      </el-form-item>
      <el-form-item label="广告地址：" prop="address">
        <el-input v-model="form.address" type="text" placeholder="请输入广告地址"></el-input>
      </el-form-item>
      <el-form-item label="广告图片：" prop="image">
        <el-upload
            class="avatar-uploader"
            action=""
            list-type="picture-card"
            :show-file-list="false"
            :http-request="upAvatar"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload">
          <img v-if="form.avatar" :src="form.avatar" class="avatar">
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="addNew">确 定</el-button>
    </span>
  </el-dialog>

  <!--     表格-->
  <table class="table table-striped table-bordered">
    <thead>
    <tr>
      <th class="text-center">广告ID</th>
      <th class="text-center">广告图片</th>
      <th class="text-center">广告描述</th>
      <th class="text-center">创建时间</th>
      <th class="text-center">链接地址</th>
      <th class="text-center">操作</th>
    </tr>
    </thead>
    <tbody>
    <tr v-for="ad in adlist" :key="ad.id">
      <td class="tableUint" style="cursor: pointer">{{ad.id}}</td>
      <td  class="tableUint">
        <a :href="ad.avatar" target="_blank">{{ad.avatar}}</a>
      </td>
      <td class="tableUint">{{ad.description}}</td>
      <td class="tableUint">{{ad.createTime}}</td>
      <td class="tableUint">
        <a :href="ad.address" target="_blank">{{ad.address}}</a>
      </td>
      <td class="tableUint">
        <div class="op-col">
          <span @click="deleteById(ad.id)" class="delete-text">删除</span>
        </div>
      </td>
    </tr>
    </tbody>
  </table>
<!-- 实现广告的分页查询-->
  <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="queryInfo.pagenum"
      :page-sizes="[5,10,15]"
      :page-size="queryInfo.pagesize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
  ></el-pagination>
</div>

</template>

<script>
export default {
  name: "AdList",
  data(){
    return{
      dialogVisible: false,
      queryInfo:{
        //当前页数
        pagenum: 1,
        pagesize: 5,           // 每页显示多少数据,
        query:'',
      },
      img_file:[],
      adlist:[],
      total:0,
      form:{
        name:'',
        description:'',
        address:'',
        avatar:""
      },
      rule:{
        name: [
          {required: true, message: '请输入广告名字', trigger: 'blur'},
          {min: 3, max: 100, message: '长度在 3 到 100 个字符', trigger: 'blur'}
        ],
        description: [
          {required: true, message: '请输入描述', trigger: 'blur'},
          {min: 3, max: 100, message: '长度在 3 到 100 个字符', trigger: 'blur'}
        ],
        address: [
          {required: true, message: '请输入地址', trigger: 'blur'},
          {min: 3, max: 50, message: '长度在 3 到 50 个字符', trigger: 'blur'}
        ]
      }
    }
  },
  methods:{
    deleteById(id){
      this.$confirm('确定删除该广告吗')
          .then(_ => {
            this.$http.delete('/ad/'+id).then(res=>{
              this.$message.info(res.data.message)
              this.adlist = this.adlist.filter(x=>x.id!==id)
            })
          })
          .catch(_ => {});
    },
    getAdList(){
      this.$http.get("/ad/all",{params:this.queryInfo}).then(res=>{
        this.adlist=res.data.data.list
        this.total=res.data.data.total
      })
    },
    handleClose(done) {
      this.$confirm('确认退出新增广告？')
          .then(_ => {
            this.form = {
              name:'',
              description:'',
              address:'',
              avatar:""
            }
            done()
          })
          .catch(_ => {});
    },
    handleAvatarSuccess() {
      this.$message.success('success')
    },
    //上传广告图片之前的验证
    beforeAvatarUpload(file) {
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
      }
      return isLt2M;
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

        if(res.data.status===200) {
          let oldKey=this.form.avatar
          this.form.avatar = res.data.data.key
          oldKey=oldKey.substring(36)
          this.$http.get("/qiniu/delete/"+oldKey)
        }
        else{
          this.$message.error(res.data.message)
        }
      })
    },
    // 监听 pagesize改变
    handleSizeChange (newSize) {
      this.queryInfo.pagesize = newSize
      this.getAdList()
    },
    // 监听页码值改变
    handleCurrentChange (newNum) {
      this.queryInfo.pagenum = newNum
      this.getAdList()
    },
    addNew(){
      this.$refs['formRef'].validate(async (valid) => {
        if (valid) {
          const {data:res}=await this.$http.post('/ad',this.form)

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
        name:'',
        description:'',
        address:'',
        avatar:""
      }
      this.getList()
    }
  },

  created(){
    this.getAdList()
  },

}
</script>

<style scoped>
.op-text{
  color: #66b9fd;
  cursor: pointer;
}
.op-text:hover{
  color: blue;
  text-decoration: underline;
}
.delete-text{
  padding-left: 10px;
  color: orangered;
  cursor: pointer;
}
.delete-text:hover{
  color: red;
  text-decoration: underline;
}
.tableUint{
  text-align: center;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 150px;
  white-space: nowrap;
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
</style>