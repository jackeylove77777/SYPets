import Vue from "vue";
import Home from "../views/Home.vue";
import About from "../views/About.vue";
import VueRouter from 'vue-router'
import {createWebHistory} from "vue-router";

Vue.use(VueRouter)

const routes = [
    {
        path:'/',
        name:'Home',
        component:Home
    },
    {
        path: '/about',
        name:'About',
        component: About
    }
]

const router = new VueRouter.createRouter({
    routes:routes,
    history:createWebHistory()
})

export default router