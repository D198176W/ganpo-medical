<template>
  <div class="settings-account">
    <div class="bg-container">
      <img src="/src/assets/images/personal-bg.png" alt="背景" class="bg-img">
    </div>
    <NavBar title="账号与安全" :show-user="false" />
    
    <div class="content">
      <!-- 横幅 -->
      <div class="account-banner">
        <div class="banner-content">
          <h2>账号安全管理</h2>
          <p>保护您的账号安全，设置登录密码和验证方式</p>
        </div>
        <div class="banner-icon">
          <i class="fas fa-shield-alt"></i>
        </div>
      </div>

      <!-- 账号信息卡片 -->
      <div class="account-info-card">
        <div class="info-header">
          <span class="info-title">账号信息</span>
          <button class="edit-btn" @click="editAccount">
            <i class="fas fa-edit"></i>
            编辑
          </button>
        </div>
        <div class="info-content">
          <div class="info-row">
            <div class="info-label">
              <i class="fas fa-user"></i>
              <span>用户名</span>
            </div>
            <div class="info-value">{{ accountInfo.username }}</div>
          </div>
          <div class="info-row">
            <div class="info-label">
              <i class="fas fa-phone"></i>
              <span>手机号</span>
            </div>
            <div class="info-value">
              {{ accountInfo.phone }}
              <span class="verified-badge" v-if="accountInfo.phoneVerified">
                <i class="fas fa-check-circle"></i>
                已验证
              </span>
            </div>
          </div>
          <div class="info-row">
            <div class="info-label">
              <i class="fas fa-envelope"></i>
              <span>邮箱</span>
            </div>
            <div class="info-value">
              {{ accountInfo.email }}
              <span class="verified-badge" v-if="accountInfo.emailVerified">
                <i class="fas fa-check-circle"></i>
                已验证
              </span>
            </div>
          </div>
          <div class="info-row">
            <div class="info-label">
              <i class="fas fa-calendar-alt"></i>
              <span>注册时间</span>
            </div>
            <div class="info-value">{{ accountInfo.registerDate }}</div>
          </div>
        </div>
      </div>

      <!-- 安全设置 -->
      <div class="security-section">
        <div class="section-header">
          <span class="section-title">安全设置</span>
        </div>

        <div class="security-list">
          <!-- 登录密码 -->
          <div class="security-item" @click="changePassword">
            <div class="security-icon password">
              <i class="fas fa-key"></i>
            </div>
            <div class="security-info">
              <div class="security-header">
                <span class="security-name">登录密码</span>
                <span class="security-status" :class="{ weak: passwordStrength === '弱', medium: passwordStrength === '中', strong: passwordStrength === '强' }">
                  {{ passwordStrength }}强度
                </span>
              </div>
              <span class="security-desc">定期修改密码可提高账号安全性</span>
              <span class="security-update">上次修改：{{ lastPasswordChange }}</span>
            </div>
            <div class="security-action">
              <i class="fas fa-chevron-right"></i>
            </div>
          </div>

          <!-- 手机验证 -->
          <div class="security-item" @click="verifyPhone">
            <div class="security-icon phone">
              <i class="fas fa-mobile-alt"></i>
            </div>
            <div class="security-info">
              <div class="security-header">
                <span class="security-name">手机验证</span>
                <span class="security-status verified" v-if="accountInfo.phoneVerified">已绑定</span>
                <span class="security-status unverified" v-else>未绑定</span>
              </div>
              <span class="security-desc">可用于登录验证、密码找回</span>
              <span class="security-phone">{{ accountInfo.phone }}</span>
            </div>
            <div class="security-action">
              <i class="fas fa-chevron-right"></i>
            </div>
          </div>

          <!-- 邮箱验证 -->
          <div class="security-item" @click="verifyEmail">
            <div class="security-icon email">
              <i class="fas fa-envelope"></i>
            </div>
            <div class="security-info">
              <div class="security-header">
                <span class="security-name">邮箱验证</span>
                <span class="security-status verified" v-if="accountInfo.emailVerified">已绑定</span>
                <span class="security-status unverified" v-else>未绑定</span>
              </div>
              <span class="security-desc">可用于接收通知、密码找回</span>
              <span class="security-email">{{ accountInfo.email }}</span>
            </div>
            <div class="security-action">
              <i class="fas fa-chevron-right"></i>
            </div>
          </div>

          <!-- 登录设备 -->
          <div class="security-item" @click="manageDevices">
            <div class="security-icon device">
              <i class="fas fa-laptop"></i>
            </div>
            <div class="security-info">
              <div class="security-header">
                <span class="security-name">登录设备管理</span>
                <span class="device-count">{{ loginDevices.length }} 台设备</span>
              </div>
              <span class="security-desc">管理已登录的设备，移除可疑设备</span>
              <span class="security-devices">当前设备：{{ currentDevice }}</span>
            </div>
            <div class="security-action">
              <i class="fas fa-chevron-right"></i>
            </div>
          </div>

          <!-- 登录日志 -->
          <div class="security-item" @click="viewLoginHistory">
            <div class="security-icon history">
              <i class="fas fa-history"></i>
            </div>
            <div class="security-info">
              <div class="security-header">
                <span class="security-name">登录日志</span>
              </div>
              <span class="security-desc">查看最近的登录记录</span>
              <span class="security-last-login">最近登录：{{ lastLoginTime }}</span>
            </div>
            <div class="security-action">
              <i class="fas fa-chevron-right"></i>
            </div>
          </div>
        </div>
      </div>

      <!-- 账号安全等级 -->
      <div class="security-level-section">
        <div class="section-header">
          <span class="section-title">账号安全等级</span>
        </div>
        <div class="security-level-card">
          <div class="level-header">
            <div class="level-info">
              <span class="level-label">当前安全等级</span>
              <span class="level-value" :class="securityLevelClass">{{ securityLevelText }}</span>
            </div>
            <div class="level-score">
              <span class="score-value">{{ securityScore }}</span>
              <span class="score-label">分</span>
            </div>
          </div>
          <div class="level-progress">
            <div class="progress-bar">
              <div class="progress-fill" :style="{ width: securityScore + '%' }" :class="securityLevelClass"></div>
            </div>
          </div>
          <div class="level-tips">
            <div class="tip-item" v-for="(tip, index) in securityTips" :key="index">
              <i :class="[tip.completed ? 'fas fa-check-circle' : 'far fa-circle', tip.completed ? 'completed' : 'pending']"></i>
              <span :class="{ completed: tip.completed }">{{ tip.text }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 危险操作 -->
      <div class="danger-section">
        <div class="section-header">
          <span class="section-title">危险操作</span>
        </div>
        <div class="danger-list">
          <div class="danger-item" @click="confirmCancelAccount">
            <div class="danger-icon">
              <i class="fas fa-user-slash"></i>
            </div>
            <div class="danger-info">
              <span class="danger-name">注销账号</span>
              <span class="danger-desc">注销后所有数据将被删除且无法恢复</span>
            </div>
            <div class="danger-action">
              <i class="fas fa-chevron-right"></i>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import NavBar from '@/components/common/NavBar.vue'

// 账号信息
const accountInfo = ref({
  username: 'zhangsan',
  phone: '138****5678',
  email: 'zhang****@gmail.com',
  phoneVerified: true,
  emailVerified: true,
  registerDate: '2024-01-15'
})

// 密码信息
const passwordStrength = ref('强')
const lastPasswordChange = ref('2024-03-20')

// 设备信息
const loginDevices = ref([
  { id: 1, name: 'iPhone 14 Pro', location: '北京', lastLogin: '2024-04-12 10:30' },
  { id: 2, name: 'MacBook Pro', location: '北京', lastLogin: '2024-04-11 18:45' }
])
const currentDevice = ref('iPhone 14 Pro')
const lastLoginTime = ref('2024-04-12 10:30:25')

// 安全等级
const securityScore = ref(85)

// 安全提示
const securityTips = ref([
  { text: '设置登录密码', completed: true },
  { text: '绑定手机号', completed: true },
  { text: '绑定邮箱', completed: true },
  { text: '开启双重验证', completed: false }
])

// 计算属性
const securityLevelClass = computed(() => {
  if (securityScore.value >= 80) return 'high'
  if (securityScore.value >= 60) return 'medium'
  return 'low'
})

const securityLevelText = computed(() => {
  if (securityScore.value >= 80) return '高'
  if (securityScore.value >= 60) return '中'
  return '低'
})

// 方法
const editAccount = () => {
  alert('编辑账号信息功能开发中...')
}

const changePassword = () => {
  alert('修改密码功能开发中...')
}

const verifyPhone = () => {
  if (accountInfo.value.phoneVerified) {
    alert('手机号已验证')
  } else {
    alert('手机验证功能开发中...')
  }
}

const verifyEmail = () => {
  if (accountInfo.value.emailVerified) {
    alert('邮箱已验证')
  } else {
    alert('邮箱验证功能开发中...')
  }
}

const manageDevices = () => {
  alert('设备管理功能开发中...')
}

const viewLoginHistory = () => {
  alert('登录日志功能开发中...')
}

const confirmCancelAccount = () => {
  if (confirm('确定要注销账号吗？此操作不可恢复，所有数据将被删除！')) {
    alert('账号注销功能开发中...')
  }
}
</script>

<style scoped>
.settings-account {
  min-height: 100vh;
  padding-bottom: 80px;
  position: relative;
}

.bg-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: -1;
  overflow: hidden;
}

.bg-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.content {
  padding: 16px;
  position: relative;
  z-index: 1;
}

/* 横幅 */
.account-banner {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 20px;
  padding: 24px;
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: relative;
  overflow: hidden;
  box-shadow: 0 8px 24px rgba(102, 126, 234, 0.4);
}

.banner-content {
  flex: 1;
  z-index: 1;
}

.banner-content h2 {
  font-size: 22px;
  font-weight: 700;
  color: white;
  margin: 0 0 8px 0;
}

.banner-content p {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.9);
  margin: 0;
}

.banner-icon {
  width: 64px;
  height: 64px;
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32px;
  color: white;
  flex-shrink: 0;
}

/* 账号信息卡片 */
.account-info-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  padding: 20px;
  margin-bottom: 20px;
  backdrop-filter: blur(10px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.info-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid #eee;
}

.info-title {
  font-size: 18px;
  font-weight: 700;
  color: #333;
}

.edit-btn {
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 6px;
  transition: all 0.3s ease;
}

.edit-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

.info-content {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.info-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.info-label {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: #666;
}

.info-label i {
  font-size: 16px;
  color: #667eea;
  width: 20px;
}

.info-value {
  font-size: 14px;
  color: #333;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 8px;
}

.verified-badge {
  font-size: 12px;
  color: #4CAF50;
  display: flex;
  align-items: center;
  gap: 4px;
  font-weight: 500;
}

.verified-badge i {
  font-size: 14px;
}

/* 安全设置区域 */
.security-section {
  margin-bottom: 20px;
}

.section-header {
  margin-bottom: 16px;
}

.section-title {
  font-size: 18px;
  font-weight: 700;
  color: #333;
  display: block;
}

.security-list {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  overflow: hidden;
  backdrop-filter: blur(10px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.security-item {
  display: flex;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: background 0.3s ease;
}

.security-item:last-child {
  border-bottom: none;
}

.security-item:hover {
  background: rgba(102, 126, 234, 0.05);
}

.security-icon {
  width: 44px;
  height: 44px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  color: white;
  flex-shrink: 0;
  margin-right: 12px;
}

.security-icon.password {
  background: linear-gradient(135deg, #667eea, #764ba2);
}

.security-icon.phone {
  background: linear-gradient(135deg, #f093fb, #f5576c);
}

.security-icon.email {
  background: linear-gradient(135deg, #4facfe, #00f2fe);
}

.security-icon.device {
  background: linear-gradient(135deg, #43e97b, #38f9d7);
}

.security-icon.history {
  background: linear-gradient(135deg, #fa709a, #fee140);
}

.security-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.security-header {
  display: flex;
  align-items: center;
  gap: 8px;
}

.security-name {
  font-size: 15px;
  font-weight: 600;
  color: #333;
}

.security-status {
  font-size: 12px;
  padding: 2px 8px;
  border-radius: 10px;
  font-weight: 600;
}

.security-status.weak {
  background: #FFEBEE;
  color: #D32F2F;
}

.security-status.medium {
  background: #FFF3E0;
  color: #F57C00;
}

.security-status.strong {
  background: #E8F5E9;
  color: #388E3C;
}

.security-status.verified {
  background: #E8F5E9;
  color: #388E3C;
}

.security-status.unverified {
  background: #FFEBEE;
  color: #D32F2F;
}

.device-count {
  font-size: 12px;
  color: #667eea;
  background: rgba(102, 126, 234, 0.1);
  padding: 2px 8px;
  border-radius: 10px;
  font-weight: 600;
}

.security-desc {
  font-size: 13px;
  color: #999;
}

.security-update,
.security-phone,
.security-email,
.security-devices,
.security-last-login {
  font-size: 12px;
  color: #666;
}

.security-action {
  color: #ccc;
  font-size: 14px;
}

/* 安全等级区域 */
.security-level-section {
  margin-bottom: 20px;
}

.security-level-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  padding: 20px;
  backdrop-filter: blur(10px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.level-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.level-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.level-label {
  font-size: 14px;
  color: #666;
}

.level-value {
  font-size: 24px;
  font-weight: 700;
}

.level-value.high {
  color: #4CAF50;
}

.level-value.medium {
  color: #FF9800;
}

.level-value.low {
  color: #F44336;
}

.level-score {
  display: flex;
  align-items: baseline;
  gap: 4px;
}

.score-value {
  font-size: 36px;
  font-weight: 700;
  color: #667eea;
}

.score-label {
  font-size: 16px;
  color: #666;
}

.level-progress {
  margin-bottom: 16px;
}

.progress-bar {
  height: 8px;
  background: #f0f0f0;
  border-radius: 4px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  border-radius: 4px;
  transition: width 0.3s ease;
}

.progress-fill.high {
  background: linear-gradient(90deg, #4CAF50, #8BC34A);
}

.progress-fill.medium {
  background: linear-gradient(90deg, #FF9800, #FFC107);
}

.progress-fill.low {
  background: linear-gradient(90deg, #F44336, #FF5722);
}

.level-tips {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.tip-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.tip-item i {
  font-size: 16px;
}

.tip-item i.completed {
  color: #4CAF50;
}

.tip-item i.pending {
  color: #ccc;
}

.tip-item span {
  font-size: 14px;
  color: #666;
}

.tip-item span.completed {
  color: #333;
  font-weight: 600;
}

/* 危险操作区域 */
.danger-section {
  margin-bottom: 20px;
}

.danger-list {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  overflow: hidden;
  backdrop-filter: blur(10px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.danger-item {
  display: flex;
  align-items: center;
  padding: 16px;
  cursor: pointer;
  transition: background 0.3s ease;
}

.danger-item:hover {
  background: rgba(244, 67, 54, 0.05);
}

.danger-icon {
  width: 44px;
  height: 44px;
  border-radius: 12px;
  background: linear-gradient(135deg, #F44336, #FF5722);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  color: white;
  flex-shrink: 0;
  margin-right: 12px;
}

.danger-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.danger-name {
  font-size: 15px;
  font-weight: 600;
  color: #F44336;
}

.danger-desc {
  font-size: 13px;
  color: #999;
}

.danger-action {
  color: #ccc;
  font-size: 14px;
}
</style>
