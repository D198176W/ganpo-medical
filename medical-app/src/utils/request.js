

import axios from 'axios'

// 创建 axios 实例
const request = axios.create({
  baseURL: import.meta.env.VITE_APP_BASE_API,
  timeout: 100000, // 增加到100秒
  withCredentials: true // 允许携带cookies
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    config.headers = config.headers || {}
    try {
      const token = localStorage.getItem('token')
      if (token) {
        config.headers.Authorization = `Bearer ${token}`
      }
    } catch (error) {
      console.warn('读取token失败:', error)
    }
    return config
  },
  error => {
    console.error('请求拦截器错误：', error)
    return Promise.reject(error)
  }
)

// 响应拦截器 - 移除了 ElMessage
request.interceptors.response.use(
  response => {
    const res = response.data
    if (res && res.code !== undefined && res.code !== 0) {
      console.error('接口错误：', res.message)
      return Promise.reject(new Error(res.message || '接口请求失败'))
    }
    return res
  },
  error => {
    console.error('响应拦截器错误：', error)
    
    // 可以根据需要使用原生 alert 或其他通知方式
    // alert('请求失败: ' + error.message)
    
    return Promise.reject(error)
  }
)

export default request



