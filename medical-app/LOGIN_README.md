# 登录注册功能说明

## 一、功能概述

本项目已实现完整的登录注册功能，包括：

1. **账号登录**：支持手机号/学号 + 验证码登录
2. **学号注册**：支持南昌高校学生通过学号注册
3. **微信快捷登录**：支持微信授权登录（需后端实现）

## 二、启动项目自动跳转到登录页

### 修改内容

已修改路由配置，现在启动项目时会自动跳转到登录页面：

1. **首页添加登录验证**：首页现在需要登录才能访问
2. **路由守卫**：未登录用户访问需要认证的页面会自动跳转到登录页
3. **根路径处理**：访问根路径 `/` 时，如果未登录会跳转到 `/login`

### 验证方式

```bash
# 启动前端项目
npm run dev

# 访问 http://localhost:5173
# 会自动跳转到 http://localhost:5173/login
```

## 三、后端 API 服务

### 1. 快速启动（开发环境）

项目已包含一个 Node.js 后端示例服务器，用于开发和测试。

**安装依赖：**

```bash
cd medical-app
npm install express cors body-parser node-cache
```

**启动服务器：**

```bash
node server.js
```

服务器会在 http://localhost:3000 启动

**验证码说明：**
- 验证码会在**后端服务器控制台**输出
- 开发环境下不会真正发送短信
- 查看终端获取验证码

### 2. API 接口说明

#### 发送验证码
```http
POST /api/auth/send-code
Content-Type: application/json

{
  "phone": "13800138000",
  "type": "register"  // 或 "login"
}

Response:
{
  "success": true,
  "message": "验证码已发送",
  "data": {
    "expires": 300
  }
}
```

#### 用户注册
```http
POST /api/auth/register
Content-Type: application/json

{
  "studentId": "2023001001",
  "phone": "13800138000",
  "code": "123456",
  "college": "南昌大学"
}

Response:
{
  "success": true,
  "message": "注册成功",
  "data": {
    "id": "1234567890",
    "studentId": "2023001001",
    "phone": "13800138000",
    "college": "南昌大学",
    "userTag": "student",
    "nickName": "用户 3800",
    "avatarUrl": ""
  }
}
```

#### 用户登录
```http
POST /api/auth/login
Content-Type: application/json

{
  "account": "13800138000",
  "code": "123456"
}

Response:
{
  "success": true,
  "message": "登录成功",
  "data": {
    "id": "1234567890",
    "phone": "13800138000",
    "userTag": "other",
    "nickName": "用户 3800"
  }
}
```

#### 微信登录
```http
POST /api/auth/wechat-login
Content-Type: application/json

{
  "code": "wechat-auth-code"
}

Response:
{
  "success": true,
  "message": "微信登录成功",
  "data": {
    "id": "1234567890",
    "userTag": "wechat",
    "nickName": "微信用户"
  }
}
```

## 四、前端调用方式

前端已集成 API 调用，无需修改代码。

### API 文件位置
```
src/api/auth.js  // 认证相关 API
```

### 已实现的功能

1. **发送验证码**：调用 `/api/auth/send-code`
2. **注册**：调用 `/api/auth/register`
3. **登录**：调用 `/api/auth/login`
4. **微信登录**：调用 `/api/auth/wechat-login`

## 五、生产环境部署

### 1. 后端部署

生产环境需要：

1. **数据库**：MySQL/MongoDB 等
2. **短信服务**：阿里云短信、腾讯云短信等
3. **微信 OAuth**：微信开放平台配置
4. **Token 认证**：JWT 或其他认证方式

### 2. 修改 API 地址

修改 `src/api/auth.js` 中的 `baseURL`：

```javascript
const api = axios.create({
  baseURL: 'https://your-api-domain.com/api', // 生产环境地址
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})
```

### 3. 环境变量（推荐）

创建 `.env` 文件：

```env
VITE_API_BASE_URL=https://your-api-domain.com/api
```

然后在 `src/api/auth.js` 中使用：

```javascript
const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:3000/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})
```

## 六、测试流程

### 注册流程测试

1. 启动后端服务器：`node server.js`
2. 启动前端项目：`npm run dev`
3. 访问登录页面
4. 切换到"学号注册"标签
5. 输入学号（10 位数字，如：2023001001）
6. 选择高校
7. 点击"下一步"
8. 输入手机号（11 位）
9. 点击"获取验证码"
10. **在后端控制台查看验证码**
11. 输入验证码
12. 勾选同意协议
13. 点击"完成注册"
14. 自动跳转到首页

### 登录流程测试

1. 启动后端服务器：`node server.js`
2. 启动前端项目：`npm run dev`
3. 访问登录页面
4. 使用"账号登录"标签
5. 输入已注册的手机号或学号
6. 点击"下一步"
7. 点击"获取验证码"
8. **在后端控制台查看验证码**
9. 输入验证码
10. 点击"登录"
11. 自动跳转到首页

## 七、数据存储说明

### 开发环境
- 用户数据存储在**内存**中
- 服务器重启后数据会**清空**
- 验证码有效期：5 分钟

### 生产环境
需要使用数据库持久化存储：
- 用户表（users）
- 验证码表（verification_codes）
- 登录日志表（login_logs）

## 八、安全建议

1. **验证码**：
   - 限制发送频率（每 60 秒一次）
   - 设置有效期（5 分钟）
   - 限制尝试次数（防暴力破解）

2. **密码**：
   - 使用 bcrypt 等加密存储
   - 要求密码强度（长度、复杂度）

3. **Token**：
   - 使用 JWT 进行认证
   - 设置合理的过期时间
   - 实现 Token 刷新机制

4. **短信**：
   - 接入正规短信服务商
   - 设置每日发送上限
   - 防刷机制（IP 限制、图形验证码）

## 九、常见问题

### Q1: 为什么收不到验证码？
**A**: 开发环境下，验证码在后端服务器控制台输出，不会真正发送短信。请查看启动 `server.js` 的终端窗口。

### Q2: 注册后提示"该手机号已注册"？
**A**: 每个手机号只能注册一次。如需重新注册，请重启后端服务器清空数据，或更换手机号。

### Q3: 微信登录无法使用？
**A**: 微信登录需要：
- 微信开放平台账号
- 配置 OAuth 回调地址
- 后端实现微信 OAuth 流程
目前前端代码已准备好，但需要后端配合实现。

### Q4: 如何清空用户数据？
**A**: 重启后端服务器（`server.js`）即可清空内存中的所有数据。

## 十、技术栈

### 前端
- Vue 3 + Composition API
- Vue Router 4
- Axios
- Vite

### 后端（示例）
- Node.js
- Express
- node-cache（缓存验证码）
- CORS（跨域支持）

## 十一、联系支持

如有问题，请联系开发团队或查看相关文档。
