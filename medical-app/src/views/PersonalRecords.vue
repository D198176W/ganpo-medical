<template>
  <div class="personal-records">
    <div class="bg-container">
      <img src="/src/assets/images/personal-bg.png" alt="背景" class="bg-img">
    </div>
    <NavBar title="健康记录" :show-user="false" />
    
    <div class="content">
      <div class="record-tabs">
        <div 
          v-for="(tab, index) in recordTabs" 
          :key="index"
          class="tab-item"
          :class="{ active: activeTab === tab.id }"
          @click="activeTab = tab.id"
        >
          {{ tab.name }}
        </div>
      </div>

      <div class="records-list">
        <div v-if="activeTab === 'all'" class="all-records">
          <div v-for="record in allRecords" :key="record.id" class="record-card" @click="viewRecord(record)">
            <div class="record-icon" :class="record.bgClass">
              <i :class="record.icon"></i>
            </div>
            <div class="record-info">
              <span class="record-title">{{ record.title }}</span>
              <span class="record-time">{{ record.time }}</span>
              <span class="record-desc">{{ record.desc }}</span>
            </div>
            <div class="record-arrow">
              <img src="/src/assets/images/arrow-right.png" alt="右箭头">
            </div>
          </div>
        </div>

        <div v-else class="category-records">
          <div v-for="record in getRecordsByCategory(activeTab)" :key="record.id" class="record-card" @click="viewRecord(record)">
            <div class="record-icon" :class="record.bgClass">
              <i :class="record.icon"></i>
            </div>
            <div class="record-info">
              <span class="record-title">{{ record.title }}</span>
              <span class="record-time">{{ record.time }}</span>
              <span class="record-desc">{{ record.desc }}</span>
            </div>
            <div class="record-arrow">
              <img src="/src/assets/images/arrow-right.png" alt="右箭头">
            </div>
          </div>
        </div>

        <div v-if="getRecordsByCategory(activeTab).length === 0" class="empty-state">
          <div class="empty-icon">📝</div>
          <div>暂无健康记录</div>
          <div class="empty-hint">点击右上角添加新记录</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import NavBar from '@/components/common/NavBar.vue'

const activeTab = ref('all')

const recordTabs = ref([
  { id: 'all', name: '全部' },
  { id: 'checkup', name: '体检' },
  { id: 'medical', name: '就诊' },
  { id: 'medication', name: '用药' },
  { id: 'exercise', name: '运动' }
])

const allRecords = ref([
  {
    id: 1,
    category: 'checkup',
    title: '年度体检报告',
    time: '2024-03-15',
    desc: '各项指标正常，体重略有上升',
    icon: 'fas fa-file-medical',
    bgClass: 'bg-blue'
  },
  {
    id: 2,
    category: 'medical',
    title: '感冒就诊',
    time: '2024-02-28',
    desc: '普通感冒，开了感冒药',
    icon: 'fas fa-clinic-medical',
    bgClass: 'bg-green'
  },
  {
    id: 3,
    category: 'medication',
    title: '维生素补充',
    time: '2024-02-20',
    desc: '每天服用维生素C和D',
    icon: 'fas fa-pills',
    bgClass: 'bg-orange'
  },
  {
    id: 4,
    category: 'exercise',
    title: '晨跑记录',
    time: '2024-03-10',
    desc: '慢跑30分钟，5公里',
    icon: 'fas fa-running',
    bgClass: 'bg-purple'
  },
  {
    id: 5,
    category: 'checkup',
    title: '血液检查',
    time: '2024-01-20',
    desc: '血常规检查，各项正常',
    icon: 'fas fa-vial',
    bgClass: 'bg-blue'
  }
])

const getRecordsByCategory = (category) => {
  if (category === 'all') {
    return allRecords.value
  }
  return allRecords.value.filter(record => record.category === category)
}

const viewRecord = (record) => {
  console.log('查看记录:', record)
}
</script>

<style scoped>
.personal-records {
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

.record-tabs {
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

.records-list {
  display: flex;
  flex-direction: column;
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
  font-size: 20px;
  color: white;
  flex-shrink: 0;
}

.record-icon.bg-blue {
  background: linear-gradient(135deg, #1E88E5, #64B5F6);
}

.record-icon.bg-green {
  background: linear-gradient(135deg, #4CAF50, #A5D6A7);
}

.record-icon.bg-orange {
  background: linear-gradient(135deg, #FF9800, #FFE0B2);
}

.record-icon.bg-purple {
  background: linear-gradient(135deg, #9C27B0, #CE93D8);
}

.record-info {
  flex: 1;
  overflow: hidden;
}

.record-title {
  display: block;
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 4px;
}

.record-time {
  display: block;
  font-size: 12px;
  color: #999;
  margin-bottom: 4px;
}

.record-desc {
  display: block;
  font-size: 14px;
  color: #666;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.record-arrow {
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.record-arrow img {
  width: 16px;
  height: 16px;
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
</style>
