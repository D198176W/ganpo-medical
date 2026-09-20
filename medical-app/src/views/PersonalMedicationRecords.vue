<template>
  <div class="personal-medication-records">
    <div class="bg-container">
      <img src="/src/assets/images/personal-bg.png" alt="背景" class="bg-img">
    </div>
    <NavBar title="用药记录详情" :show-user="false" />
    
    <div class="content">
      <div class="medication-banner">
        <div class="banner-content">
          <h2>用药记录详情</h2>
          <p>追踪用药情况，确保用药安全</p>
        </div>
        <div class="banner-icon">
          <i class="fas fa-pills"></i>
        </div>
      </div>

      <div class="medication-stats">
        <div class="stat-item">
          <span class="stat-value">{{ totalMedications }}</span>
          <span class="stat-label">总药品</span>
        </div>
        <div class="stat-item">
          <span class="stat-value taking">{{ currentTaking }}</span>
          <span class="stat-label">进行中</span>
        </div>
        <div class="stat-item">
          <span class="stat-value completed">{{ completedCount }}</span>
          <span class="stat-label">已完成</span>
        </div>
      </div>

      <div class="medication-tabs">
        <div 
          v-for="tab in medicationTabs" 
          :key="tab.id"
          class="tab-item"
          :class="{ active: activeTab === tab.id }"
          @click="activeTab = tab.id"
        >
          {{ tab.name }}
        </div>
      </div>

      <div class="medications-section">
        <div class="section-header">
          <span class="section-title">用药列表</span>
        </div>

        <div class="medications-list">
          <div 
            v-for="med in filteredMedications" 
            :key="med.id"
            class="medication-card"
            @click="viewMedication(med)"
          >
            <div class="med-icon" :class="med.bgClass">
              <i :class="med.icon"></i>
            </div>
            <div class="med-info">
              <div class="med-header">
                <span class="med-name">{{ med.name }}</span>
                <span class="med-status" :class="med.statusClass">{{ med.status }}</span>
              </div>
              <span class="med-dosage">{{ med.dosage }}</span>
              <span class="med-frequency">{{ med.frequency }}</span>
              <div class="med-progress">
                <span class="progress-text">第 {{ med.currentDay }}/{{ med.totalDays }} 天</span>
                <div class="progress-bar">
                  <div 
                    class="progress-fill" 
                    :style="{ width: (med.currentDay / med.totalDays * 100) + '%', backgroundColor: med.progressColor }"
                  ></div>
                </div>
              </div>
            </div>
            <div class="med-actions">
              <button 
                v-if="med.status === '进行中'"
                class="action-btn take" 
                @click.stop="takeMedication(med.id)"
              >
                <i class="fas fa-check"></i>
                服药
              </button>
              <button class="action-btn detail" @click.stop="viewDetails(med)">
                <i class="fas fa-info-circle"></i>
                详情
              </button>
            </div>
          </div>

          <div v-if="filteredMedications.length === 0" class="empty-state">
            <div class="empty-icon">💊</div>
            <div>暂无用药记录</div>
            <div class="empty-hint">点击底部按钮添加用药计划</div>
          </div>
        </div>
      </div>

      <div class="today-schedule">
        <div class="section-header">
          <span class="section-title">今日服药计划</span>
          <span class="section-date">{{ todayDate }}</span>
        </div>
        <div class="schedule-list">
          <div 
            v-for="schedule in todaySchedule" 
            :key="schedule.id"
            class="schedule-item"
            :class="{ completed: schedule.completed }"
          >
            <div class="schedule-time">
              <i class="fas fa-clock"></i>
              <span>{{ schedule.time }}</span>
            </div>
            <div class="schedule-info">
              <span class="schedule-med">{{ schedule.medication }}</span>
              <span class="schedule-dosage">{{ schedule.dosage }}</span>
            </div>
            <div class="schedule-status">
              <span v-if="schedule.completed" class="status-taken">
                <i class="fas fa-check-circle"></i>
                已服用
              </span>
              <span v-else class="status-pending">
                未服用
              </span>
            </div>
          </div>
          <div v-if="todaySchedule.length === 0" class="empty-hint-small">
            今日暂无服药计划
          </div>
        </div>
      </div>

      <div class="medication-reminders">
        <div class="section-header">
          <span class="section-title">用药提醒</span>
        </div>
        <div class="reminders-list">
          <div 
            v-for="reminder in reminders" 
            :key="reminder.id"
            class="reminder-card"
            :class="reminder.typeClass"
          >
            <div class="reminder-icon">
              <i :class="reminder.icon"></i>
            </div>
            <div class="reminder-content">
              <div class="reminder-header">
                <span class="reminder-title">{{ reminder.title }}</span>
                <span class="reminder-time">{{ reminder.time }}</span>
              </div>
              <p class="reminder-desc">{{ reminder.desc }}</p>
            </div>
          </div>
          <div v-if="reminders.length === 0" class="empty-hint-small">
            暂无用药提醒
          </div>
        </div>
      </div>

      <div class="add-medication-btn">
        <button @click="addMedication">
          <i class="fas fa-plus"></i>
          添加用药
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import NavBar from '@/components/common/NavBar.vue'

const activeTab = ref('all')

const medicationTabs = ref([
  { id: 'all', name: '全部' },
  { id: 'taking', name: '进行中' },
  { id: 'completed', name: '已完成' }
])

const medications = ref([
  {
    id: 1,
    name: '阿莫西林胶囊',
    dosage: '每次 2 粒 (0.5g)',
    frequency: '每日 3 次，饭后服用',
    status: '进行中',
    statusClass: 'status-taking',
    icon: 'fas fa-capsules',
    bgClass: 'bg-blue',
    currentDay: 3,
    totalDays: 7,
    progressColor: '#1E88E5'
  },
  {
    id: 2,
    name: '布洛芬缓释胶囊',
    dosage: '每次 1 粒 (0.3g)',
    frequency: '每日 2 次，早晚服用',
    status: '进行中',
    statusClass: 'status-taking',
    icon: 'fas fa-pills',
    bgClass: 'bg-green',
    currentDay: 2,
    totalDays: 5,
    progressColor: '#4CAF50'
  },
  {
    id: 3,
    name: '维生素 C 片',
    dosage: '每次 2 片 (0.2g)',
    frequency: '每日 3 次',
    status: '已完成',
    statusClass: 'status-completed',
    icon: 'fas fa-tablets',
    bgClass: 'bg-orange',
    currentDay: 14,
    totalDays: 14,
    progressColor: '#FF9800'
  }
])

const todaySchedule = ref([
  {
    id: 1,
    time: '08:00',
    medication: '阿莫西林胶囊',
    dosage: '2 粒',
    completed: true
  },
  {
    id: 2,
    time: '12:00',
    medication: '阿莫西林胶囊',
    dosage: '2 粒',
    completed: false
  },
  {
    id: 3,
    time: '18:00',
    medication: '布洛芬缓释胶囊',
    dosage: '1 粒',
    completed: false
  },
  {
    id: 4,
    time: '20:00',
    medication: '阿莫西林胶囊',
    dosage: '2 粒',
    completed: false
  }
])

const reminders = ref([
  {
    id: 1,
    title: '记得服药',
    desc: '您有阿莫西林胶囊需要服用，请按时服药。',
    time: '12:00',
    type: 'reminder',
    typeClass: 'reminder-normal',
    icon: 'fas fa-bell'
  },
  {
    id: 2,
    title: '药品库存不足',
    desc: '阿莫西林胶囊剩余不足 3 天用量，请及时补充。',
    time: '今天',
    type: 'warning',
    typeClass: 'reminder-warning',
    icon: 'fas fa-exclamation-triangle'
  }
])

const totalMedications = medications.value.length
const currentTaking = medications.value.filter(m => m.status === '进行中').length
const completedCount = medications.value.filter(m => m.status === '已完成').length

const todayDate = ref(new Date().toLocaleDateString('zh-CN', { 
  year: 'numeric', 
  month: 'long', 
  day: 'numeric',
  weekday: 'long'
}))

const filteredMedications = computed(() => {
  if (activeTab.value === 'all') {
    return medications.value
  } else if (activeTab.value === 'taking') {
    return medications.value.filter(m => m.status === '进行中')
  } else {
    return medications.value.filter(m => m.status === '已完成')
  }
})

const viewMedication = (med) => {
  console.log('查看用药:', med)
}

const viewDetails = (med) => {
  console.log('查看详情:', med)
}

const takeMedication = (id) => {
  const med = medications.value.find(m => m.id === id)
  if (med) {
    console.log('标记服药:', med.name)
  }
}

const addMedication = () => {
  console.log('添加用药')
}
</script>

<style scoped>
.personal-medication-records {
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

.content {
  padding: 20px;
}

.medication-banner {
  background: linear-gradient(135deg, #1E88E5, #64B5F6);
  border-radius: 20px;
  padding: 24px;
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 4px 20px rgba(30, 136, 229, 0.3);
}

.banner-content h2 {
  font-size: 22px;
  color: white;
  margin: 0 0 8px 0;
  font-weight: 700;
}

.banner-content p {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.9);
  margin: 0;
}

.banner-icon {
  width: 64px;
  height: 64px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  color: white;
}

.medication-stats {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 20px;
  padding: 20px;
  display: flex;
  justify-content: space-around;
  margin-bottom: 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #1E88E5;
  margin-bottom: 4px;
}

.stat-value.taking {
  color: #4CAF50;
}

.stat-value.completed {
  color: #FF9800;
}

.stat-label {
  font-size: 14px;
  color: #666;
}

.medication-tabs {
  display: flex;
  gap: 8px;
  margin-bottom: 20px;
  overflow-x: auto;
  padding-bottom: 4px;
}

.tab-item {
  padding: 8px 16px;
  background: rgba(255, 255, 255, 0.6);
  border-radius: 20px;
  font-size: 14px;
  color: #666;
  cursor: pointer;
  white-space: nowrap;
  transition: all 0.3s ease;
}

.tab-item:hover {
  background: rgba(255, 255, 255, 0.8);
}

.tab-item.active {
  background: linear-gradient(135deg, #1E88E5, #64B5F6);
  color: white;
  font-weight: 600;
}

.medications-section {
  margin-bottom: 20px;
}

.section-header {
  margin-bottom: 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
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

.section-date {
  font-size: 14px;
  color: #666;
}

.medications-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.medication-card {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 16px;
  padding: 16px;
  display: flex;
  align-items: center;
  gap: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  cursor: pointer;
  transition: transform 0.2s;
}

.medication-card:hover {
  transform: translateY(-2px);
}

.med-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  font-size: 20px;
  color: white;
}

.med-icon.bg-blue {
  background: linear-gradient(135deg, #1E88E5, #64B5F6);
}

.med-icon.bg-green {
  background: linear-gradient(135deg, #4CAF50, #A5D6A7);
}

.med-icon.bg-orange {
  background: linear-gradient(135deg, #FF9800, #FFE0B2);
}

.med-info {
  flex: 1;
}

.med-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 4px;
}

.med-name {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.med-status {
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.status-taking {
  background: #E8F5E8;
  color: #4CAF50;
}

.status-completed {
  background: #F5F5F5;
  color: #9E9E9E;
}

.med-dosage,
.med-frequency {
  display: block;
  font-size: 13px;
  color: #666;
  margin-bottom: 4px;
}

.med-progress {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-top: 8px;
}

.progress-text {
  font-size: 12px;
  color: #1E88E5;
  font-weight: 600;
  white-space: nowrap;
}

.progress-bar {
  flex: 1;
  height: 6px;
  background-color: #f0f0f0;
  border-radius: 3px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  border-radius: 3px;
  transition: width 0.3s ease;
}

.med-actions {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.action-btn {
  height: 32px;
  padding: 0 12px;
  border: none;
  border-radius: 16px;
  font-size: 12px;
  font-weight: 500;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
  transition: all 0.3s ease;
  white-space: nowrap;
}

.action-btn.take {
  background: linear-gradient(135deg, #4CAF50, #A5D6A7);
  color: white;
}

.action-btn.detail {
  background: #F5F5F5;
  color: #666;
}

.action-btn:hover {
  transform: scale(1.05);
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: #999;
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 16px;
}

.empty-hint {
  font-size: 14px;
  margin-top: 8px;
}

.empty-hint-small {
  text-align: center;
  padding: 16px;
  color: #999;
  font-size: 14px;
}

.today-schedule {
  margin-bottom: 20px;
}

.schedule-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.schedule-item {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 16px;
  padding: 16px;
  display: flex;
  align-items: center;
  gap: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.schedule-item.completed {
  opacity: 0.6;
}

.schedule-time {
  display: flex;
  flex-direction: column;
  align-items: center;
  min-width: 60px;
}

.schedule-time i {
  font-size: 18px;
  color: #1E88E5;
  margin-bottom: 4px;
}

.schedule-time span {
  font-size: 14px;
  font-weight: 600;
  color: #333;
}

.schedule-info {
  flex: 1;
}

.schedule-med {
  display: block;
  font-size: 15px;
  font-weight: 600;
  color: #333;
  margin-bottom: 4px;
}

.schedule-dosage {
  display: block;
  font-size: 13px;
  color: #666;
}

.schedule-status {
  font-size: 13px;
  font-weight: 500;
}

.status-taken {
  color: #4CAF50;
}

.status-taken i {
  margin-right: 4px;
}

.status-pending {
  color: #FF9800;
  padding: 4px 10px;
  background: #FFF3E0;
  border-radius: 12px;
}

.medication-reminders {
  margin-bottom: 20px;
}

.reminders-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.reminder-card {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 16px;
  padding: 16px;
  display: flex;
  align-items: flex-start;
  gap: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  border-left: 4px solid;
}

.reminder-normal {
  border-left-color: #1E88E5;
}

.reminder-warning {
  border-left-color: #FF9800;
}

.reminder-icon {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  font-size: 18px;
  color: white;
}

.reminder-normal .reminder-icon {
  background: linear-gradient(135deg, #1E88E5, #64B5F6);
}

.reminder-warning .reminder-icon {
  background: linear-gradient(135deg, #FF9800, #FFE0B2);
}

.reminder-content {
  flex: 1;
}

.reminder-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 6px;
}

.reminder-title {
  font-size: 15px;
  font-weight: 600;
  color: #333;
}

.reminder-time {
  font-size: 12px;
  color: #999;
}

.reminder-desc {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  margin: 0;
}

.add-medication-btn {
  margin-top: 20px;
}

.add-medication-btn button {
  width: 100%;
  height: 52px;
  background: linear-gradient(135deg, #1E88E5, #64B5F6);
  color: white;
  border: none;
  border-radius: 26px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  box-shadow: 0 4px 12px rgba(30, 136, 229, 0.3);
  transition: all 0.3s ease;
}

.add-medication-btn button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(30, 136, 229, 0.4);
}

.add-medication-btn i {
  font-size: 18px;
}
</style>
