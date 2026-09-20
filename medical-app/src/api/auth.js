/**
 * 认证相关 API
 */
import axios from 'axios'

// 创建 axios 实例
const api = axios.create({
  baseURL: 'http://localhost:3000/api', // 后端 API 地址
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
api.interceptors.request.use(
  config => {
    // 可以在这里添加 token 等信息
    return config
  },
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    console.error('响应错误:', error)
    return Promise.reject(error)
  }
)

/**
 * 发送验证码
 * @param {string} phone - 手机号
 * @param {string} type - 类型：'register' | 'login' | 'reset'
 */
export const sendCode = async (phone, type = 'register') => {
  try {
    const response = await api.post('/auth/send-code', {
      phone,
      type
    })
    return response
  } catch (error) {
    throw error
  }
}

/**
 * 用户注册
 * @param {Object} userData - 用户信息
 * @param {string} userData.studentId - 学号
 * @param {string} userData.phone - 手机号
 * @param {string} userData.code - 验证码
 * @param {string} userData.college - 学校
 * @param {string} userData.password - 密码（可选）
 */
export const register = async (userData) => {
  try {
    const response = await api.post('/auth/register', userData)
    return response
  } catch (error) {
    throw error
  }
}

/**
 * 用户登录
 * @param {string} account - 账号（手机号或学号）
 * @param {string} code - 验证码
 */
export const login = async (account, code) => {
  try {
    const response = await api.post('/auth/login', {
      account,
      code
    })
    return response
  } catch (error) {
    throw error
  }
}

/**
 * 微信登录
 * @param {string} code - 微信授权 code
 */
export const wechatLogin = async (code) => {
  try {
    const response = await api.post('/auth/wechat-login', {
      code
    })
    return response
  } catch (error) {
    throw error
  }
}

/**
 * 退出登录
 */
export const logout = async () => {
  try {
    const response = await api.post('/auth/logout')
    return response
  } catch (error) {
    throw error
  }
}

/**
 * 检查账号是否存在
 * @param {string} account - 账号
 */
export const checkAccount = async (account) => {
  try {
    const response = await api.post('/auth/check-account', {
      account
    })
    return response
  } catch (error) {
    throw error
  }
}

/**
 * 重置密码
 * @param {string} phone - 手机号
 * @param {string} code - 验证码
 * @param {string} newPassword - 新密码
 */
export const resetPassword = async (phone, code, newPassword) => {
  try {
    const response = await api.post('/auth/reset-password', {
      phone,
      code,
      newPassword
    })
    return response
  } catch (error) {
    throw error
  }
}

export default api
