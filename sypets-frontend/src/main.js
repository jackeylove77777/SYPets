import Vue from 'vue'
import App from "./App.vue";
import router from './router/index.js'
import store from './store/index.js'
import axios from 'axios'
//引入bootstrap
import 'bootstrap/dist/css/bootstrap.css' //引用bootstrap的样式
import 'bootstrap/dist/js/bootstrap.min.js' //引用bootstrap的js
//饿了么UI
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
//图标
import './assets/css/global.css'
import './assets/font_bbint4nfd6/iconfont.css'
//富文本编辑
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
import showdown from 'showdown'
// import hljs from 'highlight.js/lib/highlight';
import 'highlight.js/styles/monokai-sublime.css';
import showdownHighlight from 'showdown-highlight'
import './api.js'

Vue.prototype.$http=axios
Vue.prototype.$bus = new Vue()


//在原型对象上挂在markdown转换器
Vue.prototype.md2html = (md)=> {
  let converter = new showdown.Converter({
    extensions:[showdownHighlight]
  })
  let text = md.toString()
  return converter.makeHtml(text)
}
Vue.use(ElementUI)
Vue.use(mavonEditor)
//关闭生产提示
Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
