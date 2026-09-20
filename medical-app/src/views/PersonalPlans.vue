<template>
  <div class="personal-plans">
    <div class="bg-container">
      <img src="/src/assets/images/personal-bg.png" alt="背景" class="bg-img">
    </div>
    <NavBar title="进行中计划" :show-user="false" />
    
    <div class="content">
      <div class="plan-stats">
        <div class="stat-item">
          <span class="stat-value">{{ activePlansCount }}</span>
          <span class="stat-label">进行中</span>
        </div>
        <div class="stat-item">
          <span class="stat-value completed">{{ completedPlansCount }}</span>
          <span class="stat-label">已完成</span>
        </div>
        <div class="stat-item">
          <span class="stat-value total">{{ totalPlansCount }}</span>
          <span class="stat-label">总计划</span>
        </div>
      </div>

      <div class="plans-section">
        <div class="section-header">
          <span class="section-title">进行中的计划</span>
        </div>
        <div class="plans-list">
          <div 
            v-for="plan in activePlans" 
            :key="plan.id"
            class="plan-card"
            @click="viewPlan(plan)"
          >
            <div class="plan-icon" :class="plan.bgClass">
              <i :class="plan.icon"></i>
            </div>
            <div class="plan-info">
              <div class="plan-header">
                <span class="plan-title">{{ plan.title }}</span>
                <span class="plan-status" :class="plan.statusClass">{{ plan.statusText }}</span>
              </div>
              <span class="plan-desc">{{ plan.desc }}</span>
              <div class="plan-progress">
                <span class="progress-text">完成 {{ plan.progress }}%</span>
                <div class="progress-bar">
                  <div class="progress-fill" :style="{ width: plan.progress + '%' }"></div>
                </div>
              </div>
            </div>
          </div>

          <div v-if="activePlans.length === 0" class="empty-state">
            <div class="empty-icon">📋</div>
            <div>暂无进行中的计划</div>
            <div class="empty-hint">点击下方按钮添加新计划</div>
          </div>
        </div>
      </div>

      <div class="completed-section" v-if="completedPlans.length > 0">
        <div class="section-header">
          <span class="section-title">已完成的计划</span>
        </div>
        <div class="plans-list">
          <div 
            v-for="plan in completedPlans" 
            :key="plan.id"
            class="plan-card completed"
            @click="viewPlan(plan)"
          >
            <div class="plan-icon bg-gray">
              <i class="fas fa-check"></i>
            </div>
            <div class="plan-info">
              <div class="plan-header">
                <span class="plan-title">{{ plan.title }}</span>
                <span class="plan-status completed-status">已完成</span>
              </div>
              <span class="plan-desc">{{ plan.desc }}</span>
              <span class="completed-date">完成于 {{ plan.completedDate }}</span>
            </div>
          </div>
        </div>
      </div>

      <div class="add-plan-btn">
        <button @click="addPlan">
          <i class="fas fa-plus"></i>
          添加新计划
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import NavBar from '@/components/common/NavBar.vue'

const activePlans = ref([
  {
    id: 1,
    title: '减重计划',
    desc: '目标：减重5kg，每天运动30分钟',
    progress: 65,
    status: 'active',
    statusText: '进行中',
    statusClass: 'active-status',
    icon: 'fas fa-weight',
    bgClass: 'bg-blue'
  },
  {
    id: 2,
    title: '跑步训练',
    desc: '每周跑步4次，每次5公里',
    progress: 80,
    status: 'active',
    statusText: '进行中',
    statusClass: 'active-status',
    icon: 'fas fa-running',
    bgClass: 'bg-green'
  }
])

const completedPlans = ref([
  {
    id: 3,
    title: '早睡计划',
    desc: '每天23点前入睡，坚持30天',
    completedDate: '2024-03-10',
    status: 'completed'
  }
])

const activePlansCount = computed(() => activePlans.value.length)
const completedPlansCount = computed(() => completedPlans.value.length)
const totalPlansCount = computed(() => activePlans.value.length + completedPlans.value.length)

const viewPlan = (plan) => {
  console.log('查看计划:', plan)
}

const addPlan = () => {
  console.log('添加新计划')
}
</script>

<style scoped>
.personal-plans {
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

.plan-stats {
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

.stat-value.completed {
  color: #4CAF50;
}

.stat-value.total {
  color: #9C27B0;
}

.stat-label {
  font-size: 14px;
  color: #666;
}

.section-header {
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

.plans-section,
.completed-section {
  margin-bottom: 20px;
}

.plans-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.plan-card {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 16px;
  padding: 16px;
  display: flex;
  align-items: flex-start;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  cursor: pointer;
  transition: transform 0.2s;
}

.plan-card:hover {
  transform: translateY(-2px);
}

.plan-card.completed {
  opacity: 0.7;
}

.plan-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  font-size: 20px;
  color: white;
  flex-shrink: 0;
}

.plan-icon.bg-blue {
  background: linear-gradient(135deg, #1E88E5, #64B5F6);
}

.plan-icon.bg-green {
  background: linear-gradient(135deg, #4CAF50, #A5D6A7);
}

.plan-icon.bg-gray {
  background: linear-gradient(135deg, #9E9E9E, #E0E0E0);
}

.plan-info {
  flex: 1;
  overflow: hidden;
}

.plan-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 4px;
}

.plan-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.plan-status {
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.plan-status.active-status {
  background: #E8F5E8;
  color: #4CAF50;
}

.plan-status.completed-status {
  background: #F5F5F5;
  color: #9E9E9E;
}

.plan-desc {
  display: block;
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
}

.plan-progress {
  display: flex;
  align-items: center;
  gap: 12px;
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
  background: linear-gradient(90deg, #1E88E5, #64B5F6);
  border-radius: 3px;
  transition: width 0.3s ease;
}

.completed-date {
  display: block;
  font-size: 12px;
  color: #999;
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

.add-plan-btn {
  margin-top: 20px;
}

.add-plan-btn button {
  width: 100%;
  height: 48px;
  background: linear-gradient(135deg, #1E88E5, #64B5F6);
  color: white;
  border: none;
  border-radius: 24px;
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

.add-plan-btn button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(30, 136, 229, 0.4);
}

.add-plan-btn i {
  font-size: 18px;
}
</style>
