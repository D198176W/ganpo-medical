import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import './assets/styles/global.css'

// import { createRouter, createWebHistory } from 'vue-router'

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)
app.use(router)
app.mount('#app')

// 开发环境下的错误处理
if (import.meta.env.DEV) {
  app.config.errorHandler = (err, vm, info) => {
    console.error('Vue错误:', err)
    console.info('错误信息:', info)
  }
}


