<template>
  <div>
    <div class="main">
      <el-popover
          placement="bottom"
          width="200"
          :show="getData"
          trigger="click">
        <div class="content">
          <div v-for="item in list" @click="openChat(item)">
            <el-divider class="my-0"></el-divider>
            <div class="chat">
              <div class="chat-header">
                <span style=" color: #0086b3;cursor: pointer" @click.stop="visitUser(item.toUsername)">{{item.toUsername}}</span>
                <span style="color: #8A919F;">{{item.privateChat.lastTime?.substring(0,10)}}</span>
              </div>
              <span class="chat-content">{{item.privateChat.lastMessage}}</span>
            </div>
          </div>
          <span v-show="list.length===0">暂无私信</span>
        </div>
        <el-badge :value="all"  slot="reference" :hidden="all===0" :max="99" @click="getData">
          <i class="el-icon-chat-dot-round"></i>
        </el-badge>
      </el-popover>
    </div>

    <el-dialog
        :title="'与'+toUsername+'的聊天内容'"
        :visible.sync="showDialog"
        width="700px"
        class="dialog"
        :modal-append-to-body="false"
        :before-close="handleClose">
      <div class="messageBox">
        <div class="message-content"  id="msgBox">
          <div v-for="item in msgList" :key="item.id">
            <div style="color: #8A919F;" class="text-center">{{item.createTime}}</div>
            <div v-if="item.senderId === uid" class="message-right">
              <span> {{item.content}}</span>
            </div>
            <div v-else class="message-left">
              <span> {{item.content}}</span>
            </div>
          </div>
        </div>
        <el-divider></el-divider>
        <div class="message-control">
          <textarea v-model="msg" class="input" rows="5"></textarea>
          <el-button class="send-button" type="primary" @click="sendMsg">发送</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>
<style scoped>
.dialog{
  z-index: 10000;
}
.message-right{
  display: flex;
  flex-direction: row-reverse;
  padding: 10px;
  font-size: 18px;
}
.message-left{
  display: flex;
  flex-direction: row;
  padding: 10px;
  font-size: 18px;
}
.input{
  width: 600px;
}
.send-button{
  position: absolute;
  bottom: 3%;
  right: 3%;
}
.message-control{
  height: 150px;
  padding: 10px;
}
.message-content{
  height: 350px;
  overflow-y: scroll;
}

.messageBox{
  display: flex;
  flex-direction: column;
  max-height: 500px;
}
.chat-content{
  color: black;
  display: -webkit-box;
  overflow: hidden;
  text-overflow: ellipsis;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 1;
}
.chat-header{
  display: flex;
  justify-content: space-between;
}
.chat{
  padding: 2px;
  display: flex;
  flex-direction: column;
}
.chat:hover{
  background-color: #F2F3F5;
}
.main{
  width: 45px;
  height: 45px;
  margin-top: 10px;
}
</style>
<script>
export default {
  name: "Chat",
  data(){
    return{
      list:[],
      all:0,//all代表未读消息的总和,
      socket:{},
      showDialog:false,
      msgList:[],
      msg:"",
      uid:0,
      toId:0,
      chatId:0,
      toUsername:'',
    }
  },
  methods:{
    updateScroll(){
      let element = document.getElementById("msgBox");
      element.scrollTop = element.scrollHeight;
    },
    visitUser(username){
      this.$router.push('/profile/'+username)
    },
    handleClose(){
      this.showDialog=false
      this.toId = 0;
      this.toUsername = ''
      this.msgList = []
    },
    openChat(item){
      this.chatId=item.privateChat.id;
      if(item.privateChat.userOne!==this.uid){
        this.toId = item.privateChat.userOne
      }else{
        this.toId = item.privateChat.userTwo
      }
      this.toUsername = item.toUsername
      this.getChatDetail()
      this.showDialog=true
    },
    sendMsg(){
      this.$http.post("/chat/add/"+this.chatId+'/'+this.toId,{message:this.msg}).then(res=>{
        if(res.data.status===200){
          this.msgList[this.msgList.length] = {
            senderId:this.uid,
            content:JSON.parse (JSON.stringify (this.msg)),
          }
          this.msg = ""
          setTimeout(()=>{
            this.updateScroll()
          },100)
        }else{
          this.$message.error(res.data.message)
        }
      })
    },
    getData(){
      console.log('chat getData')
      this.$http.get('/chat/list').then(res=>{
        if(res.data.status===200){
          this.list=res.data.data.list;
          this.all = res.data.data.all;
        }
      })
    },
    init(){
      if(typeof(WebSocket) === "undefined"){
        alert("您的浏览器不支持socket")
      }else{
        // 实例化socket
        this.path="ws://localhost:8001/api/socket"+'?token='+this.$store.getters.getToken
        this.socket = new WebSocket(this.path)
        // 监听socket连接
        this.socket.onopen = this.open
        // 监听socket错误信息
        this.socket.onerror = this.error
        // 监听socket消息
        this.socket.onmessage = this.getMessage
      }
    },
    open(){
      console.log('私信Socket已连接')
    },
    error(){
      console.log('连接错误')
    },
    getMessage(msg){
      console.log(111)
      this.getData()
      if(this.toId!==0){
        this.getChatDetail()
      }
    },
    getChatDetail(){
      this.$http.get("/chat/messages/"+this.chatId+'/'+this.toId).then(res=>{
        if(res.data.status===200){
          this.msgList = res.data.data.list
        }else{
          this.$message.error(res.data.message)
        }
      })
    }
  },
  mounted() {
    this.uid = this.$store.getters.getUser.id;
    this.getData()
    this.init()
    this.$bus.$on('sendMessage',(args)=>{
      let toId = args.toId;
      this.toId = toId; //设置toId
      this.$http.get('/chat/exists/'+toId).then(res=>{
        if(res.data.status===200){
          let result = res.data.data.exist;
          //如果存在chat，那么就获取消息
          if(result){
            this.chatId = res.data.data.chatId
            this.$http.get("/chat/messages/"+this.chatId+'/'+toId).then(res=>{
              if(res.data.status===200){
                this.msgList = res.data.data.list
                this.toUsername = args.toUsername//设置对象用户名
                this.showDialog=true
              }else{
                this.$message.error(res.data.message)
              }
            })
          }else{
            this.$http.post('/chat/addChat/'+toId).then(res=>{
              if(res.data.status===200){
                this.chatId = res.data.data.chatId
                this.showDialog=true
              }else{
                this.$message.error("发生未知错误");
              }
            })
          }
        }else{
          this.$message.error(res.data.message)
        }
      })
    })
  }
}
</script>
