/**
 * 后端 API 服务器示例（Node.js + Express）
 * 
 * 使用前请安装依赖：
 * npm install express cors body-parser node-cache
 * 
 * 启动服务器：
 * node server.js
 */

const express = require('express')
const cors = require('cors')
const bodyParser = require('body-parser')
const NodeCache = require('node-cache')

const app = express()
const PORT = 3000

// 中间件
app.use(cors())
app.use(bodyParser.json())

// 创建缓存实例（存储验证码和用户数据）
const cache = new NodeCache({ stdTTL: 300 }) // 5 分钟过期

// 模拟数据库
const users = new Map()

// 生成 6 位数字验证码
const generateCode = () => {
  return Math.floor(100000 + Math.random() * 900000).toString()
}

/**
 * 发送验证码 API
 * POST /api/auth/send-code
 */
app.post('/api/auth/send-code', (req, res) => {
  const { phone, type } = req.body
  
  if (!phone) {
    return res.status(400).json({ message: '请输入手机号' })
  }
  
  // 生成验证码
  const code = generateCode()
  
  // 存储验证码到缓存
  cache.set(`code:${type}:${phone}`, code, 300) // 5 分钟有效
  
  console.log(`【验证码】${type === 'register' ? '注册' : '登录'}验证码：${code}（手机号：${phone}）`)
  
  // 实际项目中应该调用短信服务商 API 发送短信
  // 这里只是在控制台输出验证码用于测试
  
  res.json({
    success: true,
    message: '验证码已发送',
    data: {
      expires: 300 // 验证码有效期（秒）
    }
  })
})

/**
 * 用户注册 API
 * POST /api/auth/register
 */
app.post('/api/auth/register', (req, res) => {
  const { studentId, phone, code, college } = req.body
  
  // 验证必填字段
  if (!studentId || !phone || !code) {
    return res.status(400).json({ message: '请填写完整的注册信息' })
  }
  
  // 验证验证码
  const savedCode = cache.get(`code:register:${phone}`)
  if (!savedCode) {
    return res.status(400).json({ message: '验证码已过期，请重新获取' })
  }
  
  if (savedCode !== code) {
    return res.status(400).json({ message: '验证码错误' })
  }
  
  // 检查手机号是否已注册
  if (users.has(phone)) {
    return res.status(400).json({ message: '该手机号已注册' })
  }
  
  // 检查学号是否已注册
  for (const [key, user] of users.entries()) {
    if (user.studentId === studentId) {
      return res.status(400).json({ message: '该学号已注册' })
    }
  }
  
  // 创建用户
  const user = {
    id: Date.now().toString(),
    studentId,
    phone,
    college,
    userTag: 'student',
    createdAt: new Date().toISOString(),
    nickName: `用户${phone.substr(7)}`,
    avatarUrl: ''
  }
  
  // 保存用户
  users.set(phone, user)
  
  // 删除验证码
  cache.del(`code:register:${phone}`)
  
  console.log(`【注册成功】用户：${phone}, 学号：${studentId}, 学校：${college}`)
  
  res.json({
    success: true,
    message: '注册成功',
    data: user
  })
})

/**
 * 用户登录 API
 * POST /api/auth/login
 */
app.post('/api/auth/login', (req, res) => {
  const { account, code } = req.body
  
  if (!account || !code) {
    return res.status(400).json({ message: '请输入账号和验证码' })
  }
  
  // 验证验证码
  const savedCode = cache.get(`code:login:${account}`)
  if (!savedCode) {
    return res.status(400).json({ message: '验证码已过期，请重新获取' })
  }
  
  if (savedCode !== code) {
    return res.status(400).json({ message: '验证码错误' })
  }
  
  // 查找用户
  let user = users.get(account)
  
  // 如果是学号登录，查找对应的用户
  if (!user && /^\d{10}$/.test(account)) {
    for (const [key, value] of users.entries()) {
      if (value.studentId === account) {
        user = value
        break
      }
    }
  }
  
  // 如果是新用户（未注册但验证码正确），自动注册
  if (!user) {
    user = {
      id: Date.now().toString(),
      account,
      phone: account,
      userTag: /^\d{10}$/.test(account) ? 'student' : 'other',
      createdAt: new Date().toISOString(),
      nickName: `用户${account.substr(account.length - 4)}`,
      avatarUrl: ''
    }
    users.set(account, user)
  }
  
  // 删除验证码
  cache.del(`code:login:${account}`)
  
  console.log(`【登录成功】用户：${account}`)
  
  res.json({
    success: true,
    message: '登录成功',
    data: user
  })
})

/**
 * 微信登录 API（示例）
 * POST /api/auth/wechat-login
 */
app.post('/api/auth/wechat-login', (req, res) => {
  const { code } = req.body
  
  if (!code) {
    return res.status(400).json({ message: '缺少微信授权码' })
  }
  
  // 实际项目中需要：
  // 1. 使用 code 换取 openid 和 access_token
  // 2. 根据 openid 查找或创建用户
  // 3. 返回用户信息
  
  // 这里模拟微信登录
  const user = {
    id: Date.now().toString(),
    userTag: 'wechat',
    nickName: '微信用户',
    avatarUrl: '',
    createdAt: new Date().toISOString()
  }
  
  console.log(`【微信登录成功】用户：${user.nickName}`)
  
  res.json({
    success: true,
    message: '微信登录成功',
    data: user
  })
})

/**
 * 退出登录 API
 * POST /api/auth/logout
 */
app.post('/api/auth/logout', (req, res) => {
  // 实际项目中需要使 token 失效
  // 这里只是简单返回成功
  
  res.json({
    success: true,
    message: '退出登录成功'
  })
})

/**
 * 检查账号是否存在 API
 * POST /api/auth/check-account
 */
app.post('/api/auth/check-account', (req, res) => {
  const { account } = req.body
  
  if (!account) {
    return res.status(400).json({ message: '请输入账号' })
  }
  
  const user = users.get(account)
  const exists = !!user
  
  res.json({
    success: true,
    data: {
      exists,
      registered: exists
    }
  })
})

/**
 * 重置密码 API
 * POST /api/auth/reset-password
 */
app.post('/api/auth/reset-password', (req, res) => {
  const { phone, code, newPassword } = req.body
  
  if (!phone || !code || !newPassword) {
    return res.status(400).json({ message: '请填写完整信息' })
  }
  
  // 验证验证码
  const savedCode = cache.get(`code:reset:${phone}`)
  if (!savedCode || savedCode !== code) {
    return res.status(400).json({ message: '验证码错误' })
  }
  
  // 查找用户
  const user = users.get(phone)
  if (!user) {
    return res.status(400).json({ message: '该手机号未注册' })
  }
  
  // 更新密码（实际项目中应该加密存储）
  // user.password = bcrypt.hashSync(newPassword, 10)
  
  // 删除验证码
  cache.del(`code:reset:${phone}`)
  
  console.log(`【密码重置成功】用户：${phone}`)
  
  res.json({
    success: true,
    message: '密码重置成功'
  })
})

// 启动服务器
app.listen(PORT, () => {
  console.log(`===========================================`)
  console.log(`🚀 服务器已启动`)
  console.log(`📍 地址：http://localhost:${PORT}`)
  console.log(`📝 API 文档：http://localhost:${PORT}/api`)
  console.log(`===========================================`)
  console.log(`\n【提示】`)
  console.log(`1. 验证码会在控制台输出，请查看控制台获取`)
  console.log(`2. 用户数据存储在内存中，重启服务器会清空`)
  console.log(`3. 生产环境请使用数据库和短信服务\n`)
})
