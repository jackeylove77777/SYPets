import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from "../views/Login.vue";
import Register from "../views/Register.vue";
import Home from "../views/Home.vue";
import IndexList from "../views/IndexList.vue";
import EditPost from "../views/post/EditPost.vue";
import DetailPost from "../views/post/DetailPost.vue";
import CategoryList from "../views/CategoryList.vue";
import Update from "../views/post/Update.vue";
import Profile from "../views/Profile.vue";
import Following from "../views/Following.vue";
import Messages from "../views/Messages.vue";
import MessageList from "../views/messages/MessageList.vue";
import Search from "../views/Search.vue";
import Admin from "../components/admin/Admin.vue";
import User from "../views/admin/User.vue";
import Post from "../views/admin/Post.vue";
import EditProfile from "../views/EditProfile.vue";

Vue.use(VueRouter)
const originalPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(location) {
    return originalPush.call(this, location).catch(err => err)
};
const routes = [
    {
        path:'/',
        component:Home,
        children:[
            {
                path:'/',
                name:'IndexList',
                component:IndexList,
                meta:{
                    title:'首页'
                }
            },
            {
                path: '/edit',
                name:'EditPost',
                component: EditPost,
                meta:{
                    title: '写文章'
                }
            },
            {
                path: '/detail/:postId',
                name:'DetailPost',
                component: DetailPost
            },
            {
                path: '/category/:name',
                name:'CategoryList',
                component: CategoryList,
                meta: {
                    title: '分类'
                }
            },
            {
                path: 'update/:id',
                component: Update,
                meta: {
                    title: '编辑文章'
                }
            },
            {
                path: '/profile/:username',
                component: Profile,
                meta:{
                    title: '个人主页'
                }
            },
            // {
            //     path: '/mention/:id',
            //     component: ItemDetail,
            //     meta:{
            //         title:'个人主页'
            //     }
            // },
            {
                path: '/following',
                component: Following,
                meta: {
                    title: '关注'
                }
            },
            {
                path: '/messages',
                component: Messages,
                children:[
                    {
                        path: '/messages/:id',
                        component: MessageList
                    }
                ],
                meta:{
                    title: '消息'
                }
            },
            {
                path: '/search',
                component: Search,
                meta: {
                    title: '搜索'
                }
            }
        ]
    },
    {
        path: '/admin',
        name: 'Admin',
        component: Admin,
        meta:{
            title:"后台管理"
        },
        children: [
            {
                path: '/user',
                component:User
            },
            {
                path: '/post',
                component: Post,
            }
        ]
    },
    {
        path: '/home',
        redirect: '/'
    },
    {
        path: '/login',
        component: Login,
        meta:{
            title: '登录'
        }
    },
    {
        path: '/register',
        component: Register,
        meta: {
            title: '注册'
        }
    },
    {
        path:'/editProfile',
        component: EditProfile,
        meta:{
            title: '编辑个人信息'
        }
    }
]

const router = new VueRouter({
    mode: 'history',
    routes
})

router.beforeEach((to,form,next)=>{
    if (to.meta.title) {
        document.title = to.meta.title
    }
    if(to.path==='/login')return next()
    if(to.path==='/register')return next()
    const tokenStr=window.sessionStorage.getItem('token')

    if(!tokenStr)return next('/login')
    if(to.path==='/admin'){
        const user=JSON.parse(window.sessionStorage.getItem('userInfo'))
        if(user.username!='admin')
            return next("/")
    }
    next()
})


export default router
