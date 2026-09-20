<template>
  <div class="settings-notification">
    <div class="bg-container">
      <img src="/src/assets/images/personal-bg.png" alt="背景" class="bg-img">
    </div>
    <NavBar title="消息通知" :show-user="false" />
    
    <div class="content">
      <!-- 横幅 -->
      <div class="notification-banner">
        <div class="banner-content">
          <h2>消息通知设置</h2>
          <p>自定义您想接收的通知类型和方式</p>
        </div>
        <div class="banner-icon">
          <i class="fas fa-bell"></i>
        </div>
      </div>

      <!-- 通知统计 -->
      <div class="notification-stats">
        <div class="stat-item">
          <span class="stat-value unread">{{ unreadCount }}</span>
          <span class="stat-label">未读消息</span>
        </div>
        <div class="stat-item">
          <span class="stat-value today">{{ todayCount }}</span>
          <span class="stat-label">今日消息</span>
        </div>
        <div class="stat-item">
          <span class="stat-value total">{{ totalCount }}</span>
          <span class="stat-label">总消息数</span>
        </div>
      </div>

      <!-- 快捷操作 -->
      <div class="quick-actions">
        <button class="action-btn" @click="markAllAsRead">
          <i class="fas fa-check-double"></i>
          全部已读
        </button>
        <button class="action-btn" @click="clearAllNotifications">
          <i class="fas fa-trash-alt"></i>
          清空消息
        </button>
      </div>

      <!-- 通知分类设置 -->
      <div class="notification-categories">
        <div class="section-header">
          <span class="section-title">通知分类设置</span>
        </div>

        <div class="category-list">
          <!-- 问诊通知 -->
          <div class="category-item">
            <div class="category-header" @click="toggleCategory('consultation')">
              <div class="category-icon consultation">
                <i class="fas fa-user-md"></i>
              </div>
              <div class="category-info">
                <span class="category-name">问诊通知</span>
                <span class="category-desc">医生回复、复诊提醒等</span>
              </div>
              <div class="category-toggle">
                <div class="toggle-switch" :class="{ active: categories.consultation }" @click.stop="toggleCategory('consultation')">
                  <div class="toggle-handle"></div>
                </div>
              </div>
            </div>
            <div class="category-detail" v-show="categories.consultation">
              <div class="detail-option">
                <span>医生回复消息</span>
                <div class="toggle-switch small" :class="{ active: subCategories.consultationReply }" @click="subCategories.consultationReply = !subCategories.consultationReply"></div>
              </div>
              <div class="detail-option">
                <span>复诊提醒</span>
                <div class="toggle-switch small" :class="{ active: subCategories.followupReminder }" @click="subCategories.followupReminder = !subCategories.followupReminder"></div>
              </div>
              <div class="detail-option">
                <span>处方审核通知</span>
                <div class="toggle-switch small" :class="{ active: subCategories.prescriptionAudit }" @click="subCategories.prescriptionAudit = !subCategories.prescriptionAudit"></div>
              </div>
            </div>
          </div>

          <!-- 用药提醒 -->
          <div class="category-item">
            <div class="category-header" @click="toggleCategory('medication')">
              <div class="category-icon medication">
                <i class="fas fa-pills"></i>
              </div>
              <div class="category-info">
                <span class="category-name">用药提醒</span>
                <span class="category-desc">服药时间、药品库存等</span>
              </div>
              <div class="category-toggle">
                <div class="toggle-switch" :class="{ active: categories.medication }" @click.stop="toggleCategory('medication')">
                  <div class="toggle-handle"></div>
                </div>
              </div>
            </div>
            <div class="category-detail" v-show="categories.medication">
              <div class="detail-option">
                <span>定时服药提醒</span>
                <div class="toggle-switch small" :class="{ active: subCategories.medicationTime }" @click="subCategories.medicationTime = !subCategories.medicationTime"></div>
              </div>
              <div class="detail-option">
                <span>药品库存预警</span>
                <div class="toggle-switch small" :class="{ active: subCategories.medicationStock }" @click="subCategories.medicationStock = !subCategories.medicationStock"></div>
              </div>
              <div class="detail-option">
                <span>用药注意事项</span>
                <div class="toggle-switch small" :class="{ active: subCategories.medicationNotice }" @click="subCategories.medicationNotice = !subCategories.medicationNotice"></div>
              </div>
            </div>
          </div>

          <!-- 健康提醒 -->
          <div class="category-item">
            <div class="category-header" @click="toggleCategory('health')">
              <div class="category-icon health">
                <i class="fas fa-heartbeat"></i>
              </div>
              <div class="category-info">
                <span class="category-name">健康提醒</span>
                <span class="category-desc">健康指标、体检预约等</span>
              </div>
              <div class="category-toggle">
                <div class="toggle-switch" :class="{ active: categories.health }" @click.stop="toggleCategory('health')">
                  <div class="toggle-handle"></div>
                </div>
              </div>
            </div>
            <div class="category-detail" v-show="categories.health">
              <div class="detail-option">
                <span>健康指标异常</span>
                <div class="toggle-switch small" :class="{ active: subCategories.healthAlert }" @click="subCategories.healthAlert = !subCategories.healthAlert"></div>
              </div>
              <div class="detail-option">
                <span>体检预约提醒</span>
                <div class="toggle-switch small" :class="{ active: subCategories.checkupReminder }" @click="subCategories.checkupReminder = !subCategories.checkupReminder"></div>
              </div>
              <div class="detail-option">
                <span>健康建议推送</span>
                <div class="toggle-switch small" :class="{ active: subCategories.healthTips }" @click="subCategories.healthTips = !subCategories.healthTips"></div>
              </div>
            </div>
          </div>

          <!-- 系统通知 -->
          <div class="category-item">
            <div class="category-header" @click="toggleCategory('system')">
              <div class="category-icon system">
                <i class="fas fa-cog"></i>
              </div>
              <div class="category-info">
                <span class="category-name">系统通知</span>
                <span class="category-desc">系统更新、活动通知等</span>
              </div>
              <div class="category-toggle">
                <div class="toggle-switch" :class="{ active: categories.system }" @click.stop="toggleCategory('system')">
                  <div class="toggle-handle"></div>
                </div>
              </div>
            </div>
            <div class="category-detail" v-show="categories.system">
              <div class="detail-option">
                <span>系统更新通知</span>
                <div class="toggle-switch small" :class="{ active: subCategories.systemUpdate }" @click="subCategories.systemUpdate = !subCategories.systemUpdate"></div>
              </div>
              <div class="detail-option">
                <span>平台活动通知</span>
                <div class="toggle-switch small" :class="{ active: subCategories.activityNotice }" @click="subCategories.activityNotice = !subCategories.activityNotice"></div>
              </div>
              <div class="detail-option">
                <span>服务变更通知</span>
                <div class="toggle-switch small" :class="{ active: subCategories.serviceChange }" @click="subCategories.serviceChange = !subCategories.serviceChange"></div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 通知方式设置 -->
      <div class="notification-methods">
        <div class="section-header">
          <span class="section-title">通知方式</span>
        </div>

        <div class="method-list">
          <div class="method-item">
            <div class="method-info">
              <div class="method-icon phone">
                <i class="fas fa-mobile-alt"></i>
              </div>
              <div class="method-content">
                <span class="method-name">手机短信</span>
                <span class="method-desc">重要通知将通过短信发送</span>
              </div>
            </div>
            <div class="toggle-switch" :class="{ active: methods.sms }" @click="methods.sms = !methods.sms"></div>
          </div>

          <div class="method-item">
            <div class="method-info">
              <div class="method-icon push">
                <i class="fas fa-comment-alt"></i>
              </div>
              <div class="method-content">
                <span class="method-name">推送通知</span>
                <span class="method-desc">在手机通知栏显示消息</span>
              </div>
            </div>
            <div class="toggle-switch" :class="{ active: methods.push }" @click="methods.push = !methods.push"></div>
          </div>

          <div class="method-item">
            <div class="method-info">
              <div class="method-icon email">
                <i class="fas fa-envelope"></i>
              </div>
              <div class="method-content">
                <span class="method-name">邮件通知</span>
                <span class="method-desc">发送通知到您的邮箱</span>
              </div>
            </div>
            <div class="toggle-switch" :class="{ active: methods.email }" @click="methods.email = !methods.email"></div>
          </div>

          <div class="method-item">
            <div class="method-info">
              <div class="method-icon voice">
                <i class="fas fa-phone-alt"></i>
              </div>
              <div class="method-content">
                <span class="method-name">语音电话</span>
                <span class="method-desc">紧急情况下通过语音电话通知</span>
              </div>
            </div>
            <div class="toggle-switch" :class="{ active: methods.voice }" @click="methods.voice = !methods.voice"></div>
          </div>
        </div>
      </div>

      <!-- 免打扰设置 -->
      <div class="dnd-section">
        <div class="section-header">
          <span class="section-title">免打扰模式</span>
        </div>

        <div class="dnd-card">
          <div class="dnd-header">
            <div class="dnd-info">
              <span class="dnd-name">开启免打扰</span>
              <span class="dnd-desc">设定时间段内不接收通知</span>
            </div>
            <div class="toggle-switch large" :class="{ active: dndEnabled }" @click="dndEnabled = !dndEnabled"></div>
          </div>
          
          <div class="dnd-content" v-show="dndEnabled">
            <div class="time-range">
              <div class="time-input">
                <label>开始时间</label>
                <input type="time" v-model="dndStartTime" class="time-picker">
              </div>
              <div class="time-separator">至</div>
              <div class="time-input">
                <label>结束时间</label>
                <input type="time" v-model="dndEndTime" class="time-picker">
              </div>
            </div>
            <div class="dnd-tips">
              <i class="fas fa-info-circle"></i>
              <span>免打扰期间，重要紧急通知仍会发送</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import NavBar from '@/components/common/NavBar.vue'

// 通知统计
const unreadCount = ref(5)
const todayCount = ref(12)
const totalCount = ref(156)

// 通知分类
const categories = ref({
  consultation: true,
  medication: true,
  health: true,
  system: false
})

// 子分类
const subCategories = ref({
  consultationReply: true,
  followupReminder: true,
  prescriptionAudit: true,
  medicationTime: true,
  medicationStock: true,
  medicationNotice: true,
  healthAlert: true,
  checkupReminder: true,
  healthTips: false,
  systemUpdate: false,
  activityNotice: false,
  serviceChange: true
})

// 通知方式
const methods = ref({
  sms: true,
  push: true,
  email: false,
  voice: false
})

// 免打扰设置
const dndEnabled = ref(false)
const dndStartTime = ref('22:00')
const dndEndTime = ref('08:00')

// 方法
const toggleCategory = (category) => {
  categories.value[category] = !categories.value[category]
}

const markAllAsRead = () => {
  unreadCount.value = 0
  alert('已将全部消息标记为已读')
}

const clearAllNotifications = () => {
  if (confirm('确定要清空所有消息吗？此操作不可恢复！')) {
    totalCount.value = 0
    todayCount.value = 0
    unreadCount.value = 0
    alert('已清空所有消息')
  }
}
</script>

<style scoped>
.settings-notification {
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
.notification-banner {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  border-radius: 20px;
  padding: 24px;
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: relative;
  overflow: hidden;
  box-shadow: 0 8px 24px rgba(240, 147, 251, 0.4);
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

/* 通知统计 */
.notification-stats {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
  margin-bottom: 20px;
}

.stat-item {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  padding: 16px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  backdrop-filter: blur(10px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #667eea;
}

.stat-value.unread {
  color: #f5576c;
}

.stat-value.today {
  color: #4facfe;
}

.stat-value.total {
  color: #43e97b;
}

.stat-label {
  font-size: 13px;
  color: #666;
}

/* 快捷操作 */
.quick-actions {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
  margin-bottom: 20px;
}

.action-btn {
  background: rgba(255, 255, 255, 0.95);
  color: #667eea;
  border: 2px solid #667eea;
  padding: 14px 20px;
  border-radius: 16px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  transition: all 0.3s ease;
  backdrop-filter: blur(10px);
}

.action-btn:hover {
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
  border-color: transparent;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

/* 通知分类 */
.notification-categories {
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

.category-list {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  overflow: hidden;
  backdrop-filter: blur(10px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.category-item {
  border-bottom: 1px solid #f0f0f0;
}

.category-item:last-child {
  border-bottom: none;
}

.category-header {
  display: flex;
  align-items: center;
  padding: 16px;
  cursor: pointer;
  transition: background 0.3s ease;
}

.category-header:hover {
  background: rgba(102, 126, 234, 0.05);
}

.category-icon {
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

.category-icon.consultation {
  background: linear-gradient(135deg, #667eea, #764ba2);
}

.category-icon.medication {
  background: linear-gradient(135deg, #f093fb, #f5576c);
}

.category-icon.health {
  background: linear-gradient(135deg, #4facfe, #00f2fe);
}

.category-icon.system {
  background: linear-gradient(135deg, #43e97b, #38f9d7);
}

.category-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.category-name {
  font-size: 15px;
  font-weight: 600;
  color: #333;
}

.category-desc {
  font-size: 13px;
  color: #999;
}

.category-toggle {
  flex-shrink: 0;
}

.category-detail {
  padding: 12px 16px 16px 72px;
  background: rgba(102, 126, 234, 0.03);
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.detail-option {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.detail-option span {
  font-size: 14px;
  color: #666;
}

/* 开关样式 */
.toggle-switch {
  width: 50px;
  height: 28px;
  background: #e0e0e0;
  border-radius: 14px;
  position: relative;
  cursor: pointer;
  transition: background 0.3s ease;
}

.toggle-switch.active {
  background: linear-gradient(135deg, #667eea, #764ba2);
}

.toggle-handle {
  width: 24px;
  height: 24px;
  background: white;
  border-radius: 50%;
  position: absolute;
  top: 2px;
  left: 2px;
  transition: transform 0.3s ease;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.toggle-switch.active .toggle-handle {
  transform: translateX(22px);
}

.toggle-switch.small {
  width: 40px;
  height: 22px;
  border-radius: 11px;
}

.toggle-switch.small .toggle-handle {
  width: 18px;
  height: 18px;
  top: 2px;
  left: 2px;
}

.toggle-switch.small.active .toggle-handle {
  transform: translateX(18px);
}

.toggle-switch.large {
  width: 60px;
  height: 32px;
  border-radius: 16px;
}

.toggle-switch.large .toggle-handle {
  width: 28px;
  height: 28px;
  top: 2px;
  left: 2px;
}

.toggle-switch.large.active .toggle-handle {
  transform: translateX(28px);
}

/* 通知方式 */
.notification-methods {
  margin-bottom: 20px;
}

.method-list {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  overflow: hidden;
  backdrop-filter: blur(10px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.method-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid #f0f0f0;
}

.method-item:last-child {
  border-bottom: none;
}

.method-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.method-icon {
  width: 44px;
  height: 44px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  color: white;
  flex-shrink: 0;
}

.method-icon.phone {
  background: linear-gradient(135deg, #f093fb, #f5576c);
}

.method-icon.push {
  background: linear-gradient(135deg, #667eea, #764ba2);
}

.method-icon.email {
  background: linear-gradient(135deg, #4facfe, #00f2fe);
}

.method-icon.voice {
  background: linear-gradient(135deg, #fa709a, #fee140);
}

.method-content {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.method-name {
  font-size: 15px;
  font-weight: 600;
  color: #333;
}

.method-desc {
  font-size: 13px;
  color: #999;
}

/* 免打扰设置 */
.dnd-section {
  margin-bottom: 20px;
}

.dnd-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  padding: 20px;
  backdrop-filter: blur(10px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.dnd-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.dnd-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.dnd-name {
  font-size: 16px;
  font-weight: 700;
  color: #333;
}

.dnd-desc {
  font-size: 13px;
  color: #999;
}

.dnd-content {
  padding-top: 16px;
  border-top: 1px solid #f0f0f0;
}

.time-range {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 12px;
}

.time-input {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.time-input label {
  font-size: 13px;
  color: #666;
  font-weight: 600;
}

.time-picker {
  width: 100%;
  padding: 12px;
  border: 2px solid #e0e0e0;
  border-radius: 12px;
  font-size: 16px;
  color: #333;
  background: white;
  cursor: pointer;
  transition: border-color 0.3s ease;
}

.time-picker:focus {
  outline: none;
  border-color: #667eea;
}

.time-separator {
  font-size: 16px;
  color: #999;
  font-weight: 600;
}

.dnd-tips {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px;
  background: rgba(102, 126, 234, 0.05);
  border-radius: 12px;
}

.dnd-tips i {
  font-size: 16px;
  color: #667eea;
}

.dnd-tips span {
  font-size: 13px;
  color: #666;
}
</style>
