<template>
  <div class="health-container">
    <!-- 背景图片 -->
    <div class="bg-container">
      <img src="@/assets/images/health-bg.png" alt="健康管理背景" class="bg-img">
    </div>

    <!-- 顶部导航栏 -->
    <NavBar title="健康管理" :show-user="true" />

    <!-- 健康概览区域 -->
    <div class="health-overview">
      <div class="overview-cards">
        <div class="overview-card" @click="navigateTo('/health/data')">
          <div class="card-icon">
            <i class="fas fa-heartbeat"></i>
          </div>
          <div class="card-info">
            <span class="card-value">72</span>
            <span class="card-label">心率(bpm)</span>
          </div>
          <div class="card-status normal">正常</div>
        </div>
        
        <div class="overview-card" @click="navigateTo('/health/foot')">
          <div class="card-icon">
            <i class="fas fa-walking"></i>
          </div>
          <div class="card-info">
            <span class="card-value">8,542</span>
            <span class="card-label">今日步数</span>
          </div>
          <div class="card-status good">达标</div>
        </div>
        
        <div class="overview-card" @click="navigateTo('/health/sleep')">
          <div class="card-icon">
            <i class="fas fa-bed"></i>
          </div>
          <div class="card-info">
            <span class="card-value">7.2</span>
            <span class="card-label">睡眠(小时)</span>
          </div>
          <div class="card-status warning">不足</div>
        </div>
      </div>
    </div>

    <!-- 功能导航区 -->
    <div class="function-nav">
      <span class="nav-label">健康工具</span>
      <div class="nav-grid">
        <div class="nav-item" v-for="item in healthTools" :key="item.id" @click="navigateTo(item.route)">
          <div class="icon-wrapper" :class="item.colorClass">
            <i :class="item.icon"></i>
          </div>
          <span class="nav-text">{{ item.text }}</span>
        </div>
      </div>
    </div>

    <!-- 健康记录区 -->
    <div class="health-records">
      <div class="section-header">
        <span class="section-title">健康记录</span>
        <span class="see-all" @click="navigateTo('/health/records')">查看全部 ></span>
      </div>
      
      <div class="records-list">
        <div class="record-item" v-for="record in recentRecords" :key="record.id">
          <div class="record-icon">
            <i :class="record.icon"></i>
          </div>
          <div class="record-details">
            <span class="record-type">{{ record.type }}</span>
            <span class="record-time">{{ record.time }}</span>
            <span class="record-value">{{ record.value }}</span>
          </div>
          <div class="record-status" :class="record.statusClass">
            {{ record.status }}
          </div>
        </div>
      </div>
    </div>

    <!-- 健康计划区 -->
    <div class="health-plans">
      <div class="section-header">
        <span class="section-title">我的健康计划</span>
        <span class="see-all" @click="navigateTo('/health/plan')">管理计划 ></span>
      </div>
      
      <div class="plans-grid">
        <div class="plan-card" v-for="plan in healthPlans" :key="plan.id" 
             :class="plan.priority" @click="navigateTo(plan.route)">
          <div class="plan-header">
            <div class="plan-icon">
              <i :class="plan.icon"></i>
            </div>
            <div class="plan-title">{{ plan.title }}</div>
          </div>
          <div class="plan-progress">
            <div class="progress-bar">
              <div class="progress-fill" :style="{ width: plan.progress + '%' }"></div>
            </div>
            <span class="progress-text">{{ plan.progress }}% 完成</span>
          </div>
          <div class="plan-desc">{{ plan.desc }}</div>
        </div>
      </div>
    </div>

    <!-- 健康提醒区 -->
    <div class="health-reminders">
      <div class="section-header">
        <span class="section-title">今日提醒</span>
        <span class="see-all" @click="navigateTo('/health/reminders')">全部提醒 ></span>
      </div>
      
      <div class="reminders-list">
        <div class="reminder-item" v-for="reminder in todayReminders" :key="reminder.id"
             :class="{ urgent: reminder.urgent }">
          <div class="reminder-time">{{ reminder.time }}</div>
          <div class="reminder-content">
            <div class="reminder-title">{{ reminder.title }}</div>
            <div class="reminder-desc">{{ reminder.desc }}</div>
          </div>
          <div class="reminder-actions">
            <button class="action-btn complete" @click.stop="completeReminder(reminder.id)">完成</button>
            <button class="action-btn postpone" @click.stop="postponeReminder(reminder.id)">稍后</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import NavBar from '@/components/common/NavBar.vue'
import Toast from '@/components/common/Toast.vue'

const router = useRouter()

// Toast 相关
const showToast = ref(false)
const toastMessage = ref('')
const toastType = ref('info')

const showtoast = (message, type = 'info') => {
  toastMessage.value = message
  toastType.value = type
  showToast.value = true
  setTimeout(() => {
    showToast.value = false
  }, 2000)
}

const healthTools = ref([
  {
    id: 1,
    text: '体征记录',
    route: '/health/vitals',
    icon: 'fas fa-notes-medical',
    colorClass: 'blue-bg'
  },
  {
    id: 2,
    text: '用药提醒',
    route: '/health/medication',
    icon: 'fas fa-pills',
    colorClass: 'green-bg'
  },
  {
    id: 3,
    text: '饮食记录',
    route: '/health/diet',
    icon: 'fas fa-utensils',
    colorClass: 'orange-bg'
  },
  {
    id: 4,
    text: '运动计划',
    route: '/health/exercise',
    icon: 'fas fa-dumbbell',
    colorClass: 'purple-bg'
  }
])

const recentRecords = ref([
  {
    id: 1,
    type: '血压测量',
    time: '今天 08:30',
    value: '120/80 mmHg',
    status: '正常',
    statusClass: 'normal',
    icon: 'fas fa-heartbeat'
  },
  {
    id: 2,
    type: '血糖检测',
    time: '今天 07:45',
    value: '5.6 mmol/L',
    status: '良好',
    statusClass: 'good',
    icon: 'fas fa-tint'
  },
  {
    id: 3,
    type: '体重记录',
    time: '昨天 20:00',
    value: '65.2 kg',
    status: '偏重',
    statusClass: 'warning',
    icon: 'fas fa-weight-scale'
  }
])

const healthPlans = ref([
  {
    id: 1,
    title: '减重计划',
    desc: '目标：3个月减重5kg',
    progress: 65,
    priority: 'high',
    route: '/health/plan/weight-loss',
    icon: 'fas fa-weight-scale'
  },
  {
    id: 2,
    title: '跑步训练',
    desc: '每周跑步3次，每次30分钟',
    progress: 80,
    priority: 'medium',
    route: '/health/plan/running',
    icon: 'fas fa-running'
  },
  {
    id: 3,
    title: '睡眠改善',
    desc: '保证每晚7-8小时睡眠',
    progress: 45,
    priority: 'low',
    route: '/health/plan/sleep',
    icon: 'fas fa-moon'
  }
])

const todayReminders = ref([
  {
    id: 1,
    time: '09:00',
    title: '服用维生素',
    desc: '每日维生素补充',
    urgent: false
  },
  {
    id: 2,
    time: '12:30',
    title: '午间散步',
    desc: '步行15分钟促进消化',
    urgent: false
  },
  {
    id: 3,
    time: '20:00',
    title: '血压测量',
    desc: '每日血压监测',
    urgent: true
  }
])

const navigateTo = (route) => {
  router.push(route).catch((error) => {
    // ignore duplicate navigation; log only unexpected failures
    if (error && error.name !== 'NavigationDuplicated') {
      console.warn('导航失败:', error)
    }
  })
}

const completeReminder = (id) => {
  const index = todayReminders.value.findIndex(item => item.id === id)
  if (index !== -1) {
    todayReminders.value.splice(index, 1)
  }
}

const postponeReminder = (id) => {
  // 实现稍后提醒逻辑
  console.log('稍后提醒:', id)
}
</script>

<style scoped>
/* 其他样式保持不变，只修改图标相关样式 */

/* 健康概览卡片图标 */
.card-icon {
  width: 40px;
  height: 40px;
  margin-bottom: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.card-icon i {
  font-size: 24px;
  color: #1E88E5;
}

/* 功能导航图标 */
.icon-wrapper {
  width: 72px;
  height: 72px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 10px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.icon-wrapper i {
  font-size: 28px;
  color: #333;
}

/* 记录列表图标 */
.record-icon {
  width: 40px;
  height: 40px;
  margin-right: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.record-icon i {
  font-size: 20px;
  color: #1E88E5;
}

/* 计划卡片图标 */
.plan-icon {
  width: 32px;
  height: 32px;
  margin-right: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.plan-icon i {
  font-size: 18px;
  color: #1E88E5;
}

/* 其他样式保持不变 */
.health-container {
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

/* 健康概览区域 */
.health-overview {
  padding: 20px;
}

.overview-cards {
  display: flex;
  gap: 12px;
  overflow-x: auto;
  padding-bottom: 10px;
}

.overview-card {
  flex: 1;
  min-width: 120px;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 16px;
  padding: 16px;
  display: flex;
  flex-direction: column;
  align-items: center;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  cursor: pointer;
  transition: transform 0.2s;
}

.overview-card:hover {
  transform: translateY(-2px);
}

.card-icon {
  width: 40px;
  height: 40px;
  margin-bottom: 8px;
}

.card-icon img {
  width: 100%;
  height: 100%;
}

.card-info {
  text-align: center;
  margin-bottom: 8px;
}

.card-value {
  display: block;
  font-size: 20px;
  font-weight: 700;
  color: #333;
  margin-bottom: 4px;
}

.card-label {
  font-size: 12px;
  color: #666;
}

.card-status {
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 10px;
  font-weight: 500;
}

.card-status.normal {
  background: #E8F5E8;
  color: #4CAF50;
}

.card-status.good {
  background: #E3F2FD;
  color: #2196F3;
}

.card-status.warning {
  background: #FFF3E0;
  color: #FF9800;
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

.icon-wrapper img {
  width: 36px;
  height: 36px;
}

.nav-text {
  font-size: 14px;
  color: #333333;
  font-weight: 500;
}

/* 通用区域样式 */
.health-records,
.health-plans,
.health-reminders {
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

/* 健康记录列表 */
.records-list {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.record-item {
  display: flex;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: background-color 0.2s;
}

.record-item:last-child {
  border-bottom: none;
}

.record-item:hover {
  background-color: #f8f9fa;
}

.record-icon {
  width: 40px;
  height: 40px;
  margin-right: 12px;
}

.record-icon img {
  width: 100%;
  height: 100%;
}

.record-details {
  flex: 1;
}

.record-type {
  display: block;
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 4px;
}

.record-time {
  font-size: 12px;
  color: #999;
  margin-right: 8px;
}

.record-value {
  font-size: 14px;
  color: #666;
}

.record-status {
  padding: 6px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

/* 健康计划网格 */
.plans-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 16px;
}

.plan-card {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 16px;
  padding: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  cursor: pointer;
  transition: transform 0.2s;
}

.plan-card:hover {
  transform: translateY(-2px);
}

.plan-card.high {
  border-left: 4px solid #E53935;
}

.plan-card.medium {
  border-left: 4px solid #FF9800;
}

.plan-card.low {
  border-left: 4px solid #4CAF50;
}

.plan-header {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}

.plan-icon {
  width: 32px;
  height: 32px;
  margin-right: 8px;
}

.plan-icon img {
  width: 100%;
  height: 100%;
}

.plan-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.plan-progress {
  margin-bottom: 8px;
}

.progress-bar {
  width: 100%;
  height: 6px;
  background-color: #f0f0f0;
  border-radius: 3px;
  overflow: hidden;
  margin-bottom: 4px;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #1E88E5, #64B5F6);
  border-radius: 3px;
  transition: width 0.3s ease;
}

.progress-text {
  font-size: 12px;
  color: #666;
}

.plan-desc {
  font-size: 14px;
  color: #666;
}

/* 健康提醒列表 */
.reminders-list {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.reminder-item {
  display: flex;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid #f0f0f0;
}

.reminder-item:last-child {
  border-bottom: none;
}

.reminder-item.urgent {
  background: linear-gradient(90deg, rgba(255, 235, 238, 0.6), transparent);
}

.reminder-time {
  width: 60px;
  font-size: 14px;
  font-weight: 600;
  color: #333;
}

.reminder-content {
  flex: 1;
  margin: 0 12px;
}

.reminder-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 4px;
}

.reminder-desc {
  font-size: 14px;
  color: #666;
}

.reminder-actions {
  display: flex;
  gap: 8px;
}

.action-btn {
  padding: 6px 12px;
  border: none;
  border-radius: 8px;
  font-size: 12px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.action-btn.complete {
  background: #4CAF50;
  color: white;
}

.action-btn.complete:hover {
  background: #45a049;
}

.action-btn.postpone {
  background: #f0f0f0;
  color: #666;
}

.action-btn.postpone:hover {
  background: #e0e0e0;
}
</style>

