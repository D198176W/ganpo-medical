<template>
  <div class="personal-medication">
    <div class="bg-container">
      <img src="/src/assets/images/personal-bg.png" alt="背景" class="bg-img">
    </div>
    <NavBar title="用药记录" :show-user="false" />
    
    <div class="content">
      <div class="medication-stats">
        <div class="stat-item">
          <span class="stat-value">{{ activeMedications }}</span>
          <span class="stat-label">进行中</span>
        </div>
        <div class="stat-item">
          <span class="stat-value today">{{ todayDoses }}</span>
          <span class="stat-label">今日剂量</span>
        </div>
        <div class="stat-item">
          <span class="stat-value completed">{{ completedDoses }}</span>
          <span class="stat-label">已完成</span>
        </div>
      </div>

      <div class="medications-section">
        <div class="section-header">
          <span class="section-title">当前用药</span>
        </div>

        <div class="medications-list">
          <div 
            v-for="med in medications" 
            :key="med.id"
            class="medication-card"
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
              <span class="med-schedule">{{ med.schedule }}</span>
              <div class="med-actions">
                <button class="action-btn take" @click="takeMedication(med.id)">
                  <i class="fas fa-check"></i> 服药
                </button>
                <button class="action-btn skip">
                  <i class="fas fa-times"></i> 跳过
                </button>
              </div>
            </div>
          </div>

          <div v-if="medications.length === 0" class="empty-state">
            <div class="empty-icon">💊</div>
            <div>暂无用药记录</div>
            <div class="empty-hint">添加您的用药计划开始记录</div>
          </div>
        </div>
      </div>

      <div class="history-section" v-if="historyMedications.length > 0">
        <div class="section-header">
          <span class="section-title">用药历史</span>
        </div>
        <div class="history-list">
          <div v-for="med in historyMedications" :key="med.id" class="history-item">
            <div class="history-icon">
              <i class="fas fa-check-circle"></i>
            </div>
            <div class="history-info">
              <span class="history-name">{{ med.name }}</span>
              <span class="history-date">{{ med.date }}</span>
            </div>
          </div>
        </div>
      </div>

      <div class="add-section">
        <button class="add-btn" @click="addMedication">
          <i class="fas fa-plus"></i>
          添加用药
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import NavBar from '@/components/common/NavBar.vue'

const medications = ref([
  {
    id: 1,
    name: '维生素C',
    dosage: '每次1片',
    schedule: '每天3次 饭后',
    status: '待服药',
    statusClass: 'pending',
    icon: 'fas fa-pills',
    bgClass: 'bg-orange'
  },
  {
    id: 2,
    name: '钙补充剂',
    dosage: '每次2片',
    schedule: '每天1次 睡前',
    status: '已服用',
    statusClass: 'taken',
    icon: 'fas fa-capsules',
    bgClass: 'bg-green'
  }
])

const historyMedications = ref([
  { id: 3, name: '感冒灵颗粒', date: '2024-03-10 完成' },
  { id: 4, name: '阿莫西林', date: '2024-02-28 完成' }
])

const activeMedications = medications.value.filter(m => m.status !== '已完成').length
const todayDoses = 6
const completedDoses = 4

const takeMedication = (id) => {
  const med = medications.value.find(m => m.id === id)
  if (med) {
    med.status = '已服用'
    med.statusClass = 'taken'
  }
}

const addMedication = () => {
  console.log('添加用药')
}
</script>

<style scoped>
.personal-medication {
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

.stat-value.today {
  color: #FF9800;
}

.stat-value.completed {
  color: #4CAF50;
}

.stat-label {
  font-size: 14px;
  color: #666;
}

.medications-section,
.history-section {
  margin-bottom: 20px;
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

.medications-list,
.history-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.medication-card {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 16px;
  padding: 16px;
  display: flex;
  align-items: flex-start;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.med-icon {
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

.med-icon.bg-orange {
  background: linear-gradient(135deg, #FF9800, #FFE0B2);
}

.med-icon.bg-green {
  background: linear-gradient(135deg, #4CAF50, #A5D6A7);
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

.med-status.pending {
  background: #FFF3E0;
  color: #FF9800;
}

.med-status.taken {
  background: #E8F5E8;
  color: #4CAF50;
}

.med-dosage,
.med-schedule {
  display: block;
  font-size: 14px;
  color: #666;
  margin-bottom: 4px;
}

.med-actions {
  display: flex;
  gap: 8px;
  margin-top: 12px;
}

.action-btn {
  flex: 1;
  height: 36px;
  border: none;
  border-radius: 18px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
  transition: all 0.3s ease;
}

.action-btn.take {
  background: linear-gradient(135deg, #4CAF50, #A5D6A7);
  color: white;
}

.action-btn.skip {
  background: #F5F5F5;
  color: #666;
}

.history-item {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 12px;
  padding: 12px 16px;
  display: flex;
  align-items: center;
  opacity: 0.7;
}

.history-icon {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: #E8F5E8;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  color: #4CAF50;
  font-size: 14px;
}

.history-info {
  flex: 1;
}

.history-name {
  display: block;
  font-size: 14px;
  color: #333;
  margin-bottom: 2px;
}

.history-date {
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

.add-section {
  margin-top: 20px;
}

.add-btn {
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

.add-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(30, 136, 229, 0.4);
}

.add-btn i {
  font-size: 18px;
}
</style>
