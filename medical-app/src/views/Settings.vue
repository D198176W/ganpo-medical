<template>
  <div class="settings-page">
    <div class="container">
      <!-- 页面头部 -->
      <div class="header">
        <BackButton @click="goBack" />
        <h1 class="page-title">设置</h1>
      </div>
      
      <!-- 账号设置 -->
      <div class="settings-section">
        <div class="section-header">
          <h3 class="section-title">账号设置</h3>
        </div>
        <ul class="settings-list">
          <li class="setting-item" @click="editProfile">
            <div class="setting-icon">
              <i class="fas fa-user"></i>
            </div>
            <div class="setting-content">
              <div class="setting-title">个人信息</div>
              <div class="setting-description">管理您的个人资料信息</div>
            </div>
            <div class="setting-arrow">
              <i class="fas fa-chevron-right"></i>
            </div>
          </li>
          <li class="setting-item" @click="changePassword">
            <div class="setting-icon">
              <i class="fas fa-lock"></i>
            </div>
            <div class="setting-content">
              <div class="setting-title">修改密码</div>
              <div class="setting-description">定期更改密码保护账号安全</div>
            </div>
            <div class="setting-arrow">
              <i class="fas fa-chevron-right"></i>
            </div>
          </li>
          <li class="setting-item" @click="manageDevices">
            <div class="setting-icon">
              <i class="fas fa-mobile-alt"></i>
            </div>
            <div class="setting-content">
              <div class="setting-title">设备管理</div>
              <div class="setting-description">查看和管理已登录的设备</div>
            </div>
            <div class="setting-arrow">
              <i class="fas fa-chevron-right"></i>
            </div>
          </li>
        </ul>
      </div>
      
      <!-- 隐私设置 -->
      <div class="settings-section">
        <div class="section-header">
          <h3 class="section-title">隐私设置</h3>
        </div>
        <ul class="settings-list">
          <li class="setting-item">
            <div class="setting-icon">
              <i class="fas fa-eye"></i>
            </div>
            <div class="setting-content">
              <div class="setting-title">隐私保护</div>
              <div class="setting-description">控制谁可以看到您的健康信息</div>
            </div>
            <div class="setting-arrow">
              <i class="fas fa-chevron-right"></i>
            </div>
          </li>
          <li class="setting-item">
            <div class="setting-icon">
              <i class="fas fa-shield-alt"></i>
            </div>
            <div class="setting-content">
              <div class="setting-title">数据加密</div>
              <div class="setting-description">保护您的健康数据安全</div>
            </div>
            <label class="switch">
              <input type="checkbox" v-model="settings.dataEncryption">
              <span class="slider"></span>
            </label>
          </li>
          <li class="setting-item" @click="dataExport">
            <div class="setting-icon">
              <i class="fas fa-download"></i>
            </div>
            <div class="setting-content">
              <div class="setting-title">数据导出</div>
              <div class="setting-description">导出您的健康数据备份</div>
            </div>
            <div class="setting-arrow">
              <i class="fas fa-chevron-right"></i>
            </div>
          </li>
        </ul>
      </div>
      
      <!-- 通知设置 -->
      <div class="settings-section">
        <div class="section-header">
          <h3 class="section-title">通知设置</h3>
        </div>
        <ul class="settings-list">
          <li class="setting-item">
            <div class="setting-icon">
              <i class="fas fa-bell"></i>
            </div>
            <div class="setting-content">
              <div class="setting-title">推送通知</div>
              <div class="setting-description">接收应用推送消息</div>
            </div>
            <label class="switch">
              <input type="checkbox" v-model="settings.pushNotifications">
              <span class="slider"></span>
            </label>
          </li>
          <li class="setting-item">
            <div class="setting-icon">
              <i class="fas fa-calendar-check"></i>
            </div>
            <div class="setting-content">
              <div class="setting-title">预约提醒</div>
              <div class="setting-description">就诊前提醒</div>
            </div>
            <label class="switch">
              <input type="checkbox" v-model="settings.appointmentReminders">
              <span class="slider"></span>
            </label>
          </li>
          <li class="setting-item">
            <div class="setting-icon">
              <i class="fas fa-file-medical-alt"></i>
            </div>
            <div class="setting-content">
              <div class="setting-title">报告通知</div>
              <div class="setting-description">检查报告出具通知</div>
            </div>
            <label class="switch">
              <input type="checkbox" v-model="settings.reportNotifications">
              <span class="slider"></span>
            </label>
          </li>
          <li class="setting-item">
            <div class="setting-icon">
              <i class="fas fa-heartbeat"></i>
            </div>
            <div class="setting-content">
              <div class="setting-title">健康提醒</div>
              <div class="setting-description">定期健康检查提醒</div>
            </div>
            <label class="switch">
              <input type="checkbox" v-model="settings.healthReminders">
              <span class="slider"></span>
            </label>
          </li>
        </ul>
      </div>
      
      <!-- 通用设置 -->
      <div class="settings-section">
        <div class="section-header">
          <h3 class="section-title">通用设置</h3>
        </div>
        <ul class="settings-list">
          <li class="setting-item" @click="languageSettings">
            <div class="setting-icon">
              <i class="fas fa-language"></i>
            </div>
            <div class="setting-content">
              <div class="setting-title">语言设置</div>
              <div class="setting-description">选择应用显示语言</div>
            </div>
            <div class="setting-value">{{ settings.language }}</div>
            <div class="setting-arrow">
              <i class="fas fa-chevron-right"></i>
            </div>
          </li>
          <li class="setting-item" @click="themeSettings">
            <div class="setting-icon">
              <i class="fas fa-palette"></i>
            </div>
            <div class="setting-content">
              <div class="setting-title">主题设置</div>
              <div class="setting-description">选择应用主题颜色</div>
            </div>
            <div class="setting-value">{{ settings.theme }}</div>
            <div class="setting-arrow">
              <i class="fas fa-chevron-right"></i>
            </div>
          </li>
          <li class="setting-item">
            <div class="setting-icon">
              <i class="fas fa-wifi"></i>
            </div>
            <div class="setting-content">
              <div class="setting-title">仅WiFi下载</div>
              <div class="setting-description">仅在WiFi环境下下载报告</div>
            </div>
            <label class="switch">
              <input type="checkbox" v-model="settings.wifiOnlyDownload">
              <span class="slider"></span>
            </label>
          </li>
          <li class="setting-item" @click="clearCache">
            <div class="setting-icon">
              <i class="fas fa-trash-alt"></i>
            </div>
            <div class="setting-content">
              <div class="setting-title">清除缓存</div>
              <div class="setting-description">清理应用缓存数据</div>
            </div>
            <div class="setting-value">{{ cacheSize }}</div>
            <div class="setting-arrow">
              <i class="fas fa-chevron-right"></i>
            </div>
          </li>
        </ul>
      </div>
      
      <!-- 关于与帮助 -->
      <div class="settings-section">
        <div class="section-header">
          <h3 class="section-title">关于与帮助</h3>
        </div>
        <ul class="settings-list">
          <li class="setting-item" @click="aboutApp">
            <div class="setting-icon">
              <i class="fas fa-info-circle"></i>
            </div>
            <div class="setting-content">
              <div class="setting-title">关于应用</div>
              <div class="setting-description">版本信息和功能介绍</div>
            </div>
            <div class="setting-arrow">
              <i class="fas fa-chevron-right"></i>
            </div>
          </li>
          <li class="setting-item" @click="userAgreement">
            <div class="setting-icon">
              <i class="fas fa-file-contract"></i>
            </div>
            <div class="setting-content">
              <div class="setting-title">用户协议</div>
              <div class="setting-description">查看应用使用协议</div>
            </div>
            <div class="setting-arrow">
              <i class="fas fa-chevron-right"></i>
            </div>
          </li>
          <li class="setting-item" @click="privacyPolicy">
            <div class="setting-icon">
              <i class="fas fa-user-shield"></i>
            </div>
            <div class="setting-content">
              <div class="setting-title">隐私政策</div>
              <div class="setting-description">了解我们如何保护您的隐私</div>
            </div>
            <div class="setting-arrow">
              <i class="fas fa-chevron-right"></i>
            </div>
          </li>
          <li class="setting-item" @click="contactSupport">
            <div class="setting-icon">
              <i class="fas fa-headset"></i>
            </div>
            <div class="setting-content">
              <div class="setting-title">联系客服</div>
              <div class="setting-description">获取帮助和支持</div>
            </div>
            <div class="setting-arrow">
              <i class="fas fa-chevron-right"></i>
            </div>
          </li>
        </ul>
      </div>
      
      <!-- 退出登录 -->
      <div class="settings-section">
        <ul class="settings-list">
          <li class="setting-item setting-item-danger" @click="logout">
            <div class="setting-icon danger-icon">
              <i class="fas fa-sign-out-alt"></i>
            </div>
            <div class="setting-content">
              <div class="setting-title">退出登录</div>
              <div class="setting-description">安全退出当前账号</div>
            </div>
          </li>
        </ul>
      </div>
      
      <!-- 版本信息 -->
      <div class="version-info">
        健康服务平台 v2.1.0
      </div>
    </div>
    
    <!-- 修改密码模态框 -->
    <Modal 
      :show="showPasswordModal" 
      @close="closePasswordModal"
      title="修改密码"
    >
      <div class="modal-body">
        <form @submit.prevent="updatePassword">
          <div class="form-group">
            <label class="form-label">当前密码</label>
            <input type="password" class="form-input" v-model="passwordForm.currentPassword" required>
          </div>
          <div class="form-group">
            <label class="form-label">新密码</label>
            <input type="password" class="form-input" v-model="passwordForm.newPassword" required>
          </div>
          <div class="form-group">
            <label class="form-label">确认新密码</label>
            <input type="password" class="form-input" v-model="passwordForm.confirmPassword" required>
          </div>
        </form>
      </div>
      <template #footer>
        <button class="action-btn btn-outline" @click="closePasswordModal">取消</button>
        <button class="action-btn btn-primary" @click="updatePassword">确认修改</button>
      </template>
    </Modal>
    
    <!-- 清除缓存确认模态框 -->
    <Modal 
      :show="showClearCacheModal" 
      @close="closeClearCacheModal"
      title="清除缓存"
    >
      <div class="modal-body">
        <p>确定要清除应用缓存吗？这将删除临时文件，但不会影响您的个人数据。</p>
      </div>
      <template #footer>
        <button class="action-btn btn-outline" @click="closeClearCacheModal">取消</button>
        <button class="action-btn btn-danger" @click="confirmClearCache">清除缓存</button>
      </template>
    </Modal>
    
    <!-- 退出登录确认模态框 -->
    <Modal 
      :show="showLogoutModal" 
      @close="closeLogoutModal"
      title="退出登录"
    >
      <div class="modal-body">
        <p>确定要退出登录吗？退出后需要重新登录才能使用应用功能。</p>
      </div>
      <template #footer>
        <button class="action-btn btn-outline" @click="closeLogoutModal">取消</button>
        <button class="action-btn btn-danger" @click="confirmLogout">退出登录</button>
      </template>
    </Modal>
  </div>
</template>

<script>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import Modal from '@/components/common/Modal.vue'
import BackButton from '@/components/common/BackButton.vue'

export default {
  name: 'Settings',
  components: {
    Modal,
    BackButton
  },
  setup() {
    const router = useRouter()
    
    // 设置数据
    const settings = reactive({
      dataEncryption: true,
      pushNotifications: true,
      appointmentReminders: true,
      reportNotifications: true,
      healthReminders: false,
      wifiOnlyDownload: false,
      language: '简体中文',
      theme: '浅色'
    })
    
    // 模态框状态
    const showPasswordModal = ref(false)
    const showClearCacheModal = ref(false)
    const showLogoutModal = ref(false)
    
    // 表单数据
    const passwordForm = reactive({
      currentPassword: '',
      newPassword: '',
      confirmPassword: ''
    })
    
    // 缓存大小
    const cacheSize = ref('128 MB')
    
    // 编辑个人信息
    const editProfile = () => {
      // 在实际应用中，这里应该跳转到个人信息编辑页面
      console.log('编辑个人信息')
    }
    
    // 修改密码
    const changePassword = () => {
      showPasswordModal.value = true
    }
    
    // 关闭修改密码模态框
    const closePasswordModal = () => {
      showPasswordModal.value = false
      // 重置表单
      Object.keys(passwordForm).forEach(key => {
        passwordForm[key] = ''
      })
    }
    
    // 更新密码
    const updatePassword = () => {
      if (!passwordForm.currentPassword || !passwordForm.newPassword || !passwordForm.confirmPassword) {
        alert('请填写所有密码字段')
        return
      }
      
      if (passwordForm.newPassword !== passwordForm.confirmPassword) {
        alert('新密码和确认密码不一致')
        return
      }
      
      // 在实际应用中，这里应该发送请求到服务器更新密码
      alert('密码修改成功')
      closePasswordModal()
    }
    
    // 设备管理
    const manageDevices = () => {
      // 在实际应用中，这里应该跳转到设备管理页面
      console.log('设备管理')
    }
    
    // 数据导出
    const dataExport = () => {
      // 在实际应用中，这里应该实现数据导出功能
      alert('数据导出功能正在开发中')
    }
    
    // 语言设置
    const languageSettings = () => {
      // 在实际应用中，这里应该跳转到语言设置页面
      console.log('语言设置')
    }
    
    // 主题设置
    const themeSettings = () => {
      // 在实际应用中，这里应该跳转到主题设置页面
      console.log('主题设置')
    }
    
    // 清除缓存
    const clearCache = () => {
      showClearCacheModal.value = true
    }
    
    // 关闭清除缓存模态框
    const closeClearCacheModal = () => {
      showClearCacheModal.value = false
    }
    
    // 确认清除缓存
    const confirmClearCache = () => {
      // 在实际应用中，这里应该清除应用缓存
      cacheSize.value = '0 MB'
      alert('缓存已清除')
      closeClearCacheModal()
    }
    
    // 关于应用
    const aboutApp = () => {
      // 在实际应用中，这里应该跳转到关于应用页面
      console.log('关于应用')
    }
    
    // 用户协议
    const userAgreement = () => {
      // 在实际应用中，这里应该跳转到用户协议页面
      console.log('用户协议')
    }
    
    // 隐私政策
    const privacyPolicy = () => {
      // 在实际应用中，这里应该跳转到隐私政策页面
      console.log('隐私政策')
    }
    
    // 联系客服
    const contactSupport = () => {
      // 在实际应用中，这里应该跳转到联系客服页面
      console.log('联系客服')
    }
    
    // 退出登录
    const logout = () => {
      showLogoutModal.value = true
    }
    
    // 关闭退出登录模态框
    const closeLogoutModal = () => {
      showLogoutModal.value = false
    }
    
    // 确认退出登录
    const confirmLogout = () => {
      // 在实际应用中，这里应该清除登录状态并跳转到登录页面
      alert('已退出登录')
      closeLogoutModal()
      // 跳转到登录页面
      router.push('/login')
    }
    
    // 返回上一页
    const goBack = () => {
      router.back()
    }
    
    return {
      settings,
      showPasswordModal,
      showClearCacheModal,
      showLogoutModal,
      passwordForm,
      cacheSize,
      editProfile,
      changePassword,
      closePasswordModal,
      updatePassword,
      manageDevices,
      dataExport,
      languageSettings,
      themeSettings,
      clearCache,
      closeClearCacheModal,
      confirmClearCache,
      aboutApp,
      userAgreement,
      privacyPolicy,
      contactSupport,
      logout,
      closeLogoutModal,
      confirmLogout,
      goBack
    }
  }
}
</script>

<style scoped>
/* 复制原有的CSS样式，但移除模态框相关的样式，因为Modal组件已经独立 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;
}

:root {
  --primary: #1E88E5;
  --primary-dark: #1565C0;
  --secondary: #00BCD4;
  --success: #4CAF50;
  --warning: #FF9800;
  --danger: #E53935;
  --light: #f5f7fa;
  --dark: #333;
  --gray: #666;
  --border: #e0e0e0;
  --card: #fff;
}

body {
  background-color: var(--light);
  color: var(--dark);
  line-height: 1.6;
}

.container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

/* 头部样式 */
.header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid var(--border);
}

.page-title {
  font-size: 22px;
  font-weight: 600;
  color: var(--dark);
}

/* 设置分类卡片 */
.settings-section {
  background: var(--card);
  border-radius: 12px;
  padding: 0;
  margin-bottom: 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  overflow: hidden;
}

.section-header {
  padding: 16px 20px;
  border-bottom: 1px solid var(--border);
  background: var(--light);
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--dark);
}

.settings-list {
  list-style: none;
}

.setting-item {
  display: flex;
  align-items: center;
  padding: 16px 20px;
  border-bottom: 1px solid var(--border);
  transition: all 0.2s;
  cursor: pointer;
}

.setting-item:hover {
  background: var(--light);
}

.setting-item:last-child {
  border-bottom: none;
}

.setting-icon {
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--light);
  border-radius: 8px;
  margin-right: 15px;
  color: var(--primary);
}

.setting-content {
  flex: 1;
}

.setting-title {
  font-size: 15px;
  font-weight: 500;
  margin-bottom: 4px;
}

.setting-description {
  font-size: 13px;
  color: var(--gray);
}

.setting-value {
  font-size: 14px;
  color: var(--gray);
  margin-right: 10px;
}

.setting-arrow {
  color: var(--gray);
  font-size: 14px;
}

/* 开关样式 */
.switch {
  position: relative;
  display: inline-block;
  width: 50px;
  height: 26px;
}

.switch input {
  opacity: 0;
  width: 0;
  height: 0;
}

.slider {
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #ccc;
  transition: .4s;
  border-radius: 34px;
}

.slider:before {
  position: absolute;
  content: "";
  height: 18px;
  width: 18px;
  left: 4px;
  bottom: 4px;
  background-color: white;
  transition: .4s;
  border-radius: 50%;
}

input:checked + .slider {
  background-color: var(--primary);
}

input:checked + .slider:before {
  transform: translateX(24px);
}

/* 按钮样式 */
.action-btn {
  padding: 10px 16px;
  border-radius: 6px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s;
  border: none;
  display: flex;
  align-items: center;
  gap: 5px;
}

.btn-primary {
  background: var(--primary);
  color: white;
}

.btn-primary:hover {
  background: var(--primary-dark);
}

.btn-outline {
  background: transparent;
  border: 1px solid var(--primary);
  color: var(--primary);
}

.btn-outline:hover {
  background: var(--primary);
  color: white;
}

.btn-danger {
  background: var(--danger);
  color: white;
}

.btn-danger:hover {
  background: #c62828;
}

/* 表单样式 */
.form-group {
  margin-bottom: 15px;
}

.form-label {
  display: block;
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 5px;
}

.form-input {
  width: 100%;
  height: 40px;
  padding: 0 12px;
  border: 1px solid var(--border);
  border-radius: 6px;
  font-size: 14px;
}

/* 版本信息 */
.version-info {
  text-align: center;
  padding: 20px;
  color: var(--gray);
  font-size: 14px;
}

.setting-item-danger {
  color: var(--danger);
}

.setting-icon.danger-icon {
  color: var(--danger);
}

/* 响应式设计 */
@media (max-width: 600px) {
  .container {
    padding: 15px;
  }
}
</style>