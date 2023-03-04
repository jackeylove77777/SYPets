import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from "../views/Login.vue";
import Register from "../views/Register.vue";

Vue.use(VueRouter)
const originalPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(location) {
    return originalPush.call(this, location).catch(err => err)
};
const routes = [
    {
        path: '/',
        name: 'home',
        component: Home,
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
                    title:'写文章'
                }
            },
            {
                path: '/detail/:postId',
                name:'DetailPost',
                component: DetailPost
            },
            {
                path:'/category/:name',
                name:'CategoryList',
                component: CategoryList,
                meta:{
                    title:'分类'
                }

            },
            {
                path: '/update/:id',
                component: Update,
                meta:{
                    title:'编辑文章'
                }
            },

            {
                path:'/profile/:username',
                component: Profile,
                meta:{
                    title:'个人主页'
                }
            },
            {
                path: '/mention/:id',
                component: ItemDetail,
                meta:{
                    title:'个人主页'
                }
            },
            {
                path: '/following',
                component: Following,
                meta:{
                    title:'关注'
                }
            },
            {
                path: '/messages',
                component: Messages,
                children:[
                    {
                        path:'/messages/:id',
                        component:MessageList
                    }
                ],
                meta:{
                    title:'消息'
                }
            },
            {
                path: '/serach',
                component: Serach,
                meta:{
                    title:'搜索'
                }
            }
        ],

    },
    // {
    //     path:'/admin',
    //     name:'Admin',
    //     component:Admin,
    //     meta:{
    //         title:'后台管理'
    //     },
    //     children:[
    //         {
    //             path:'/user',
    //             component:User,
    //         },
    //         {
    //             path:'/post',
    //             component:Post,
    //         },
    //         {
    //             path:'/report',
    //             component:Report,
    //         },
    //     ]
    // },
    {
        path:'/home',
        redirect:'/'
    },
    // {
    //     path: '/editProfile',
    //     component: EditProfile,
    //     meta:{
    //         title:'编辑个人信息'
    //     }
    // },
    {
        path: '/login',
        component: Login,
        meta:{
            title:'登录'
        }
    },
    {
        path: '/register',
        component: Register,
        meta:{
            title:'注册'
        }
    }


]

const router = new VueRouter({
    mode: 'history',
    // base: process.env.BASE_URL,
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
