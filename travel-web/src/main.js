import { createApp } from 'vue'
import App from './App.vue'
import router from './router' // 确保这里引入的是 src/router/index.js
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App)

// 注册所有图标，防止图标找不到导致组件崩溃
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.use(router) // 👈 这是最关键的一行，必须先 use router
app.use(ElementPlus)
app.mount('#app')