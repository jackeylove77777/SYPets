import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)
//存jwt token和用户信息
export default new Vuex.Store({
    state: {
        token:window.sessionStorage.getItem("token"),
        userInfo:JSON.parse( window.sessionStorage.getItem("userInfo")),
    },
    mutations: {
        SET_TOKEN: (state,token)=>{
            state.token=token
            window.sessionStorage.setItem("token",token)
        },
        SET_USERINFO: (state,userInfo)=>{
            state.userInfo=userInfo
            window.sessionStorage.setItem("userInfo", JSON.stringify(userInfo))
        },
        REMOVE_INFO:state=>{
            state.token=''
            state.userInfo={}
            window.sessionStorage.removeItem('token')
            window.sessionStorage.removeItem("userInfo")
        },

    },
    getters:{
        getUser:state => {
            return state.userInfo
        },
        getToken:state => {
            return state.token
        },

    },
    actions: {
    },
    modules: {
    }
})
