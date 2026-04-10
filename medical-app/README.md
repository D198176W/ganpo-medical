# Medical App - 医疗健康管理应用

一个基于 Vue.js 的综合性医疗健康管理移动应用，提供健康记录、紧急救助、医疗咨询等功能。

## 🚀 功能特性

### 🏠 首页 (Home)
- 个性化推荐卡片
- 快速访问常用功能

### 🏥 健康管理 (HealthManagement)
- **健康记录** (HealthRecord)
  - 个人资料管理 (EditProfileModal)
  - 健康指标卡片 (HealthMetricsCard)
  - 医疗记录管理 (MedicalRecordsCard)
- **健康提醒** (HealthReminder)
  - 日历视图 (Calendar)
  - 检查提醒 (CheckupModal)
  - 健康日志 (HealthLog)

### 🚑 紧急服务 (EmergencyGuidelines)
- 紧急呼叫功能 (EmergencyCallModal)
- 急救指导 (EmergencyGuidance)
- 紧急服务点地图 (EmergencyServicePoints)
- 医疗机构详情 (FacilityDetailModal)

### 🤒 常见疾病 (CommonIllnesses)
- 疾病信息卡片 (IllnessCard)
- 详细症状指导 (IllnessDetailModal)
- 医院推荐 (HospitalModal)

### 💬 医疗咨询 (MedicalConsult)
- 在线聊天窗口 (ChatWindow)
- 语音识别功能 (VoiceButton)
- 消息气泡组件 (MessageBubble)

### 📊 医疗报告 (MedicalReports)
- 健康数据图表 (ReportChart)
- 报告分析功能

### 🗓️ 我的预约 (MyAppointments)
- 预约管理功能

### 🏪 病理服务 (PathologyService)
- 病理检测服务

### 👤 个人中心 (PersonalCenter)
- 用户设置 (Settings)
- 帮助中心 (HelpCenter)

## 🛠️ 技术栈

- **前端框架**: Vue 3
- **构建工具**: Vite
- **路由管理**: Vue Router
- **状态管理**: Vuex/Pinia
- **样式**: CSS3
- **语音识别**: Web Speech API
- **本地存储**: Web Storage API

## 📦 项目结构


src/
├── components/ # 组件目录
│ ├── chat/ # 聊天组件
│ ├── emergency/ # 紧急服务组件
│ ├── health/ # 健康管理组件
│ └── common/ # 通用组件
├── views/ # 页面视图
├── composables/ # Vue 组合式函数
├── router/ # 路由配置
├── store/ # 状态管理
├── styles/ # 全局样式
└── utils/ # 工具函数


## 🚀 快速开始

### 环境要求
- Node.js 16+ 
- npm 或 yarn

### 安装依赖
```bash
npm install

🔧 配置说明
vite.config.js - Vite 构建配置

jsconfig.json - JavaScript 路径映射配置

支持 @/ 路径别名指向 src/ 目录

📱 主要技术特性
响应式设计，支持移动端

模块化组件架构

语音交互功能

本地数据持久化

地图集成服务

实时聊天功能

🎯 目标用户
需要日常健康管理的个人用户

需要紧急医疗指导的用户

需要医疗咨询服务的患者

关注健康数据的用户

📄 许可证
MIT License