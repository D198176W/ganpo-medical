<template>
  <div class="personal-container">
    <!-- 背景图片 -->
    <div class="bg-container">
      <img src="@/assets/images/personal-bg.png" alt="个人中心背景" class="bg-img">
    </div>

    <!-- 顶部导航栏 -->
    <NavBar title="个人中心" :show-user="false" />

    <!-- 用户信息卡片 -->
    <div class="user-info-card">
      <div class="user-header">
        <div class="user-avatar">
          <img :src="userInfo.avatar" alt="用户头像">
          <div class="avatar-edit" @click="editAvatar">
            <img src="@/assets/images/camera-icon.png" alt="编辑头像">
          </div>
        </div>
        <div class="user-details">
          <span class="user-name">{{ userInfo.name }}</span>
          <span class="user-id">账号: {{ userInfo.account }}</span>
          <div class="user-tags">
            <span class="user-tag" v-for="tag in userInfo.tags" :key="tag">{{ tag }}</span>
          </div>
        </div>
        <div class="edit-profile" @click="editProfile">
          <img src="@/assets/images/edit-icon.png" alt="编辑资料">
        </div>
        
      </div>
      
      <div class="user-stats">
        <div class="stat-item" @click="navigateTo('/personal/health')">
          <span class="stat-value">{{ userStats.healthScore }}</span>
          <span class="stat-label">健康评分</span>
        </div>
        <div class="stat-item" @click="navigateTo('/personal/records')">
          <span class="stat-value">{{ userStats.records }}</span>
          <span class="stat-label">健康记录</span>
        </div>
        <div class="stat-item" @click="navigateTo('/personal/plans')">
          <span class="stat-value">{{ userStats.plans }}</span>
          <span class="stat-label">进行中计划</span>
        </div>
      </div>
    </div>

    <!-- 功能导航区 -->
    <div class="function-nav">
      <span class="nav-label">我的服务</span>
      <div class="nav-grid">
        <div class="nav-item" v-for="item in personalServices" :key="item.id" @click="navigateTo(item.route)">
          <div class="icon-wrapper" :class="item.colorClass">
            <img :src="item.icon" :alt="item.text">
            <div v-if="item.badge" class="service-badge">{{ item.badge }}</div>
          </div>
          <span class="nav-text">{{ item.text }}</span>
        </div>
      </div>
    </div>

    <!-- 健康档案 -->
    <div class="health-records">
      <div class="section-header">
        <span class="section-title">我的健康档案</span>
        <span class="see-all" @click="navigateTo('/personal/health-records')">查看全部 ></span>
      </div>
      
      <div class="records-grid">
        <div class="record-card" v-for="record in healthRecords" :key="record.id" @click="navigateTo(record.route)">
          <div class="record-icon" :class="record.bgClass">
            <img :src="record.icon" :alt="record.title">
          </div>
          <div class="record-info">
            <span class="record-title">{{ record.title }}</span>
            <span class="record-desc">{{ record.desc }}</span>
          </div>
          <div class="record-arrow">
            <img src="@/assets/images/arrow-right.png" alt="右箭头">
          </div>
        </div>
      </div>
    </div>

    <!-- 我的计划 -->
    <div class="my-plans">
      <div class="section-header">
        <span class="section-title">我的健康计划</span>
        <span class="see-all" @click="navigateTo('/personal/plans')">管理计划 ></span>
      </div>
      
      <div class="plans-list">
        <div class="plan-item" v-for="plan in activePlans" :key="plan.id" @click="navigateTo(plan.route)">
          <div class="plan-icon">
            <img :src="plan.icon" :alt="plan.title">
          </div>
          <div class="plan-details">
            <span class="plan-title">{{ plan.title }}</span>
            <span class="plan-progress-text">完成 {{ plan.progress }}%</span>
            <div class="progress-bar">
              <div class="progress-fill" :style="{ width: plan.progress + '%' }"></div>
            </div>
          </div>
          <div class="plan-status" :class="plan.status">
            {{ plan.statusText }}
          </div>
        </div>
      </div>
    </div>

    <!-- 系统设置 -->
    <div class="system-settings">
      <div class="section-header">
        <span class="section-title">系统设置</span>
      </div>
      
      <div class="settings-list">
        <div class="setting-item" v-for="setting in systemSettings" :key="setting.id" @click="handleSetting(setting)">
          <div class="setting-left">
            <div class="setting-icon" :class="setting.colorClass">
              <img :src="setting.icon" :alt="setting.title">
            </div>
            <span class="setting-title">{{ setting.title }}</span>
          </div>
          <div class="setting-right">
            <span class="setting-value" v-if="setting.value">{{ setting.value }}</span>
            <img src="@/assets/images/arrow-right.png" alt="右箭头" class="setting-arrow">
          </div>
        </div>
      </div>
    </div>

    <!-- 退出登录 -->
    <div class="logout-section">
      <button class="logout-btn" @click="handleLogout">退出登录</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import NavBar from '@/components/common/NavBar.vue'


const router = useRouter()

// 用户信息
const userInfo = ref({
  avatar: '@/assets/images/default-avatar.png',
  name: '张同学',
  account: '2024123456',
  tags: ['大学生医保', '健康监测']
})

// 用户统计
const userStats = ref({
  healthScore: 86,
  records: 24,
  plans: 3
})

// 个人服务
const personalServices = ref([
  {
    id: 1,
    text: '我的收藏',
    route: '/personal/favorites',
    icon: '@/assets/images/favorite-icon.png',
    colorClass: 'pink-bg',
    badge: '12'
  },
  {
    id: 2,
    text: '我的报告',
    route: '/personal/reports',
    icon: '@/assets/images/report-icon.png',
    colorClass: 'blue-bg',
    badge: '5'
  },
  {
    id: 3,
    text: '用药记录',
    route: '/personal/medication',
    icon: '@/assets/images/medication-icon.png',
    colorClass: 'green-bg'
  },
  {
    id: 4,
    text: '健康评估',
    route: '/personal/assessment',
    icon: '@/assets/images/assessment-icon.png',
    colorClass: 'orange-bg'
  }
])

// 健康档案
const healthRecords = ref([
  {
    id: 1,
    title: '体检报告',
    desc: '最近一次体检结果',
    route: '/personal/checkup',
    icon: '@/assets/images/checkup-icon.png',
    bgClass: 'blue-bg'
  },
  {
    id: 2,
    title: '病历档案',
    desc: '历史就诊记录',
    route: '/personal/medical-history',
    icon: '@/assets/images/history-icon.png',
    bgClass: 'green-bg'
  },
  {
    id: 3,
    title: '用药记录',
    desc: '药品使用情况',
    route: '/personal/medication-records',
    icon: '@/assets/images/medication-record.png',
    bgClass: 'orange-bg'
  },
  {
    id: 4,
    title: '过敏信息',
    desc: '过敏原记录',
    route: '/personal/allergy',
    icon: '@/assets/images/allergy-icon.png',
    bgClass: 'purple-bg'
  }
])

// 活跃计划
const activePlans = ref([
  {
    id: 1,
    title: '减重计划',
    progress: 65,
    status: 'active',
    statusText: '进行中',
    route: '/health/plan/weight-loss',
    icon: '@/assets/images/weight-loss.png'
  },
  {
    id: 2,
    title: '跑步训练',
    progress: 80,
    status: 'active',
    statusText: '进行中',
    route: '/health/plan/running',
    icon: '@/assets/images/running.png'
  }
])

// 系统设置
const systemSettings = ref([
  {
    id: 1,
    title: '账号与安全',
    route: '/settings/account',
    icon: '@/assets/images/security-icon.png',
    colorClass: 'blue-bg'
  },
  {
    id: 2,
    title: '消息通知',
    route: '/settings/notification',
    icon: '@/assets/images/notification-icon.png',
    colorClass: 'green-bg',
    value: '开启'
  },
  {
    id: 3,
    title: '隐私设置',
    route: '/settings/privacy',
    icon: '@/assets/images/privacy-icon.png',
    colorClass: 'orange-bg'
  },
  {
    id: 4,
    title: '关于我们',
    route: '/settings/about',
    icon: '@/assets/images/about-icon.png',
    colorClass: 'purple-bg',
    value: 'v1.0.0'
  },
  {
    id: 5,
    title: '帮助与反馈',
    route: '/settings/help',
    icon: '@/assets/images/help-icon.png',
    colorClass: 'pink-bg'
  }
])

// 方法
const navigateTo = (route) => {
  router.push(route)
}

const editAvatar = () => {
  // 实现编辑头像逻辑
  console.log('编辑头像')
}

const editProfile = () => {
  navigateTo('/personal/edit-profile')
}

const handleSetting = (setting) => {
  navigateTo(setting.route)
}

const handleLogout = () => {
  if (confirm('确定要退出登录吗？')) {
    // 实现退出登录逻辑
    console.log('退出登录')
    router.push('/login')
  }
}
</script>

<style scoped>
.personal-container {
  width: 100%;
  min-height: 100vh;
  background-color: transparent;
  padding-bottom: 20px;
  margin: 0;
  padding-top: 50px;
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
  opacity: 0.3;
}

/* 用户信息卡片 */
.user-info-card {
  background: rgba(255, 255, 255, 0.9);
  margin: 20px;
  border-radius: 20px;
  padding: 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.user-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.user-avatar {
  position: relative;
  width: 80px;
  height: 80px;
  margin-right: 16px;
}

.user-avatar img {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  object-fit: cover;
}

.avatar-edit {
  position: absolute;
  bottom: 0;
  right: 0;
  width: 28px;
  height: 28px;
  background: #1E88E5;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  border: 2px solid white;
}

.avatar-edit img {
  width: 14px;
  height: 14px;
}

.user-details {
  flex: 1;
}

.user-name {
  display: block;
  font-size: 20px;
  font-weight: 700;
  color: #333;
  margin-bottom: 4px;
}

.user-id {
  display: block;
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
}

.user-tags {
  display: flex;
  gap: 6px;
}

.user-tag {
  padding: 4px 8px;
  background: #E3F2FD;
  color: #1976D2;
  border-radius: 12px;
  font-size: 10px;
  font-weight: 500;
}

.edit-profile {
  width: 36px;
  height: 36px;
  background: #f5f5f5;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

.edit-profile img {
  width: 18px;
  height: 18px;
}

.user-stats {
  display: flex;
  justify-content: space-around;
  border-top: 1px solid #f0f0f0;
  padding-top: 16px;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
}

.stat-value {
  font-size: 24px;
  font-weight: 700;
  color: #1E88E5;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 12px;
  color: #666;
}

/* 功能导航区 */
.function-nav {
  padding: 20px;
}

.nav-label {
  display: block;
  font-size: 14px;
  color: #757575;
  margin-bottom: 12px;
}

.nav-grid {
  display: flex;
  justify-content: space-between;
  gap: 15px;
}

.nav-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

.icon-wrapper {
  position: relative;
  width: 72px;
  height: 72px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 10px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.blue-bg {
  background: linear-gradient(135deg, #E3F2FD, #BBDEFB);
}

.green-bg {
  background: linear-gradient(135deg, #E8F5E8, #C8E6C9);
}

.orange-bg {
  background: linear-gradient(135deg, #FFF3E0, #FFE0B2);
}

.purple-bg {
  background: linear-gradient(135deg, #F3E5F5, #E1BEE7);
}

.pink-bg {
  background: linear-gradient(135deg, #FCE4EC, #F8BBD0);
}

.icon-wrapper img {
  width: 36px;
  height: 36px;
}

.service-badge {
  position: absolute;
  top: -6px;
  right: -6px;
  background: #E53935;
  color: white;
  border-radius: 10px;
  padding: 2px 6px;
  font-size: 10px;
  font-weight: 600;
}

.nav-text {
  font-size: 14px;
  color: #333333;
  font-weight: 500;
}

/* 通用区域样式 */
.health-records,
.my-plans,
.system-settings {
  padding: 0 20px 20px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.section-title {
  font-size: 18px;
  font-weight: 700;
  color: #333333;
  position: relative;
  padding-left: 8px;
}

.section-title::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 4px;
  height: 16px;
  background-color: #1E88E5;
  border-radius: 2px;
}

.see-all {
  font-size: 14px;
  color: #666;
  cursor: pointer;
}

/* 健康档案 */
.records-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
}

.record-card {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 16px;
  padding: 16px;
  display: flex;
  align-items: center;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  cursor: pointer;
  transition: transform 0.2s;
}

.record-card:hover {
  transform: translateY(-2px);
}

.record-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
}

.record-icon img {
  width: 24px;
  height: 24px;
}

.record-info {
  flex: 1;
}

.record-title {
  display: block;
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 4px;
}

.record-desc {
  display: block;
  font-size: 12px;
  color: #666;
}

.record-arrow {
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.record-arrow img {
  width: 16px;
  height: 16px;
}

/* 我的计划 */
.plans-list {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.plan-item {
  display: flex;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: background-color 0.2s;
}

.plan-item:last-child {
  border-bottom: none;
}

.plan-item:hover {
  background-color: #f8f9fa;
}

.plan-icon {
  width: 40px;
  height: 40px;
  margin-right: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #F0F7FF;
  border-radius: 10px;
}

.plan-icon img {
  width: 24px;
  height: 24px;
}

.plan-details {
  flex: 1;
}

.plan-title {
  display: block;
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 4px;
}

.plan-progress-text {
  display: block;
  font-size: 12px;
  color: #666;
  margin-bottom: 6px;
}

.progress-bar {
  width: 100%;
  height: 4px;
  background-color: #f0f0f0;
  border-radius: 2px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #1E88E5, #64B5F6);
  border-radius: 2px;
  transition: width 0.3s ease;
}

.plan-status {
  padding: 6px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.plan-status.active {
  background: #E8F5E8;
  color: #4CAF50;
}

/* 系统设置 */
.settings-list {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.setting-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: background-color 0.2s;
}

.setting-item:last-child {
  border-bottom: none;
}

.setting-item:hover {
  background-color: #f8f9fa;
}

.setting-left {
  display: flex;
  align-items: center;
}

.setting-icon {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
}

.setting-icon img {
  width: 20px;
  height: 20px;
}

.setting-title {
  font-size: 16px;
  color: #333;
  font-weight: 500;
}

.setting-right {
  display: flex;
  align-items: center;
  gap: 8px;
}

.setting-value {
  font-size: 14px;
  color: #666;
}

.setting-arrow {
  width: 16px;
  height: 16px;
}

/* 退出登录 */
.logout-section {
  padding: 0 20px 30px;
}

.logout-btn {
  width: 100%;
  height: 48px;
  background: #E53935;
  color: white;
  border: none;
  border-radius: 24px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: background-color 0.2s;
}

.logout-btn:hover {
  background: #D32F2F;
}
</style>