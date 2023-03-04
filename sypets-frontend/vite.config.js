import { createVuePlugin } from "vite-plugin-vue2";
import {resolve} from 'path'
export default {
  plugins: [createVuePlugin()],//引入vue2插件
  resolve:{
    alias:{
      'bst': resolve(__dirname,'node_modules/bootstrap')
    }
  }
};
