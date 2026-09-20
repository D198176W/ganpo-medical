<template>
  <div class="personal-medical-history">
    <div class="bg-container">
      <img src="/src/assets/images/personal-bg.png" alt="背景" class="bg-img">
    </div>
    <NavBar title="病历档案" :show-user="false" />
    
    <div class="content">
      <div class="history-banner">
        <div class="banner-content">
          <h2>我的病历档案</h2>
          <p>记录就诊历史，关注健康变化</p>
        </div>
        <div class="banner-icon">
          <i class="fas fa-notes-medical"></i>
        </div>
      </div>

      <div class="history-stats">
        <div class="stat-item">
          <span class="stat-value">{{ totalVisits }}</span>
          <span class="stat-label">总就诊</span>
        </div>
        <div class="stat-item">
          <span class="stat-value hospital">{{ visitedHospitals }}</span>
          <span class="stat-label">就诊医院</span>
        </div>
        <div class="stat-item">
          <span class="stat-value department">{{ visitedDepartments }}</span>
          <span class="stat-label">科室</span>
        </div>
      </div>

      <div class="history-tabs">
        <div 
          v-for="tab in historyTabs" 
          :key="tab.id"
          class="tab-item"
          :class="{ active: activeTab === tab.id }"
          @click="activeTab = tab.id"
        >
          {{ tab.name }}
        </div>
      </div>

      <div class="visits-section">
        <div class="section-header">
          <span class="section-title">就诊记录</span>
        </div>

        <div class="visits-timeline">
          <div 
            v-for="visit in filteredVisits" 
            :key="visit.id"
            class="visit-card"
            @click="viewVisit(visit)"
          >
            <div class="visit-date">
              <span class="day">{{ visit.day }}</span>
              <span class="month-year">{{ visit.monthYear }}</span>
              <span class="weekday">{{ visit.weekday }}</span>
            </div>
            <div class="visit-content">
              <div class="visit-header">
                <span class="visit-hospital">{{ visit.hospital }}</span>
                <span class="visit-department">{{ visit.department }}</span>
              </div>
              <span class="visit-diagnosis">诊断：{{ visit.diagnosis }}</span>
              <div class="visit-tags">
                <span 
                  v-for="tag in visit.tags" 
                  :key="tag"
                  class="tag"
                >
                  {{ tag }}
                </span>
              </div>
              <div class="visit-actions">
                <button class="action-btn view" @click.stop="viewDetails(visit)">
                  <i class="fas fa-eye"></i>
                  查看详情
                </button>
                <button class="action-btn download" @click.stop="downloadRecord(visit)">
                  <i class="fas fa-download"></i>
                  下载病历
                </button>
              </div>
            </div>
          </div>

          <div v-if="filteredVisits.length === 0" class="empty-state">
            <div class="empty-icon">🏥</div>
            <div>暂无就诊记录</div>
            <div class="empty-hint">就诊后会自动同步病历信息</div>
          </div>
        </div>
      </div>

      <div class="chronic-conditions">
        <div class="section-header">
          <span class="section-title">慢性病史</span>
        </div>
        <div class="conditions-list">
          <div 
            v-for="condition in chronicConditions" 
            :key="condition.id"
            class="condition-card"
            :class="condition.statusClass"
          >
            <div class="condition-icon">
              <i :class="condition.icon"></i>
            </div>
            <div class="condition-info">
              <div class="condition-header">
                <span class="condition-name">{{ condition.name }}</span>
                <span class="condition-status" :class="condition.statusClass">{{ condition.status }}</span>
              </div>
              <span class="condition-diagnosed">确诊时间：{{ condition.diagnosedDate }}</span>
              <span class="condition-note">{{ condition.note }}</span>
            </div>
          </div>
        </div>
      </div>

      <div class="surgeries-section">
        <div class="section-header">
          <span class="section-title">手术史</span>
        </div>
        <div class="surgeries-list">
          <div v-for="surgery in surgeries" :key="surgery.id" class="surgery-card">
            <div class="surgery-icon">
              <i class="fas fa-procedures"></i>
            </div>
            <div class="surgery-info">
              <span class="surgery-name">{{ surgery.name }}</span>
              <span class="surgery-date">{{ surgery.date }}</span>
              <span class="surgery-hospital">{{ surgery.hospital }}</span>
            </div>
          </div>
          <div v-if="surgeries.length === 0" class="empty-hint-small">
            无手术史记录
          </div>
        </div>
      </div>

      <div class="allergies-section">
        <div class="section-header">
          <span class="section-title">过敏史</span>
          <span class="section-more" @click="viewAllAllergies">查看全部 ></span>
        </div>
        <div class="allergies-list">
          <div 
            v-for="allergy in allergies" 
            :key="allergy.id"
            class="allergy-card"
            :class="allergy.severityClass"
          >
            <div class="allergy-icon">
              <i :class="allergy.icon"></i>
            </div>
            <div class="allergy-info">
              <span class="allergy-name">{{ allergy.name }}</span>
              <span class="allergy-reaction">反应：{{ allergy.reaction }}</span>
            </div>
            <div class="allergy-severity" :class="allergy.severityClass">
              {{ allergy.severity }}
            </div>
          </div>
        </div>
      </div>

      <div class="add-record-btn">
        <button @click="addManualRecord">
          <i class="fas fa-plus"></i>
          手动添加记录
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import NavBar from '@/components/common/NavBar.vue'

const activeTab = ref('all')

const historyTabs = ref([
  { id: 'all', name: '全部' },
  { id: 'recent', name: '最近半年' },
  { id: '2024', name: '2024 年' },
  { id: 'older', name: '更早' }
])

const visits = ref([
  {
    id: 1,
    day: '15',
    monthYear: '2024 年 3 月',
    weekday: '星期五',
    hospital: '南昌大学第一附属医院',
    department: '呼吸内科',
    diagnosis: '急性上呼吸道感染',
    tags: ['初诊', '药物治疗'],
    status: 'completed'
  },
  {
    id: 2,
    day: '20',
    monthYear: '2024 年 2 月',
    weekday: '星期二',
    hospital: '江西省人民医院',
    department: '消化内科',
    diagnosis: '慢性胃炎',
    tags: ['复诊', '胃镜检查'],
    status: 'completed'
  },
  {
    id: 3,
    day: '10',
    monthYear: '2024 年 1 月',
    weekday: '星期三',
    hospital: '南昌大学第二附属医院',
    department: '骨科',
    diagnosis: '软组织挫伤',
    tags: ['急诊', '外用药'],
    status: 'completed'
  }
])

const chronicConditions = ref([
  {
    id: 1,
    name: '慢性胃炎',
    status: '稳定期',
    statusClass: 'status-stable',
    icon: 'fas fa-stomach',
    diagnosedDate: '2023-06-15',
    note: '定期复查，注意饮食规律'
  }
])

const surgeries = ref([
  {
    id: 1,
    name: '阑尾切除手术',
    date: '2020-08-12',
    hospital: '南昌大学第一附属医院'
  }
])

const allergies = ref([
  {
    id: 1,
    name: '青霉素',
    reaction: '皮疹、呼吸困难',
    severity: '严重',
    severityClass: 'severity-high',
    icon: 'fas fa-pills'
  },
  {
    id: 2,
    name: '海鲜',
    reaction: '皮肤瘙痒',
    severity: '中度',
    severityClass: 'severity-medium',
    icon: 'fas fa-fish'
  }
])

const totalVisits = visits.value.length
const visitedHospitals = new Set(visits.value.map(v => v.hospital)).size
const visitedDepartments = new Set(visits.value.map(v => v.department)).size

const filteredVisits = computed(() => {
  if (activeTab.value === 'all') {
    return visits.value
  }
  return visits.value
})

const viewVisit = (visit) => {
  console.log('查看就诊:', visit)
}

const viewDetails = (visit) => {
  console.log('查看详情:', visit)
}

const downloadRecord = (visit) => {
  console.log('下载病历:', visit)
}

const viewAllAllergies = () => {
  console.log('查看全部过敏史')
}

const addManualRecord = () => {
  console.log('手动添加记录')
}
</script>

<style scoped>
.personal-medical-history {
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

.history-banner {
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

.history-stats {
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

.stat-value.hospital {
  color: #4CAF50;
}

.stat-value.department {
  color: #9C27B0;
}

.stat-label {
  font-size: 14px;
  color: #666;
}

.history-tabs {
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

.visits-section {
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

.section-more {
  font-size: 14px;
  color: #1E88E5;
  cursor: pointer;
}

.visits-timeline {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.visit-card {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 16px;
  padding: 16px;
  display: flex;
  gap: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  cursor: pointer;
  transition: transform 0.2s;
}

.visit-card:hover {
  transform: translateY(-2px);
}

.visit-date {
  display: flex;
  flex-direction: column;
  align-items: center;
  min-width: 80px;
  padding-right: 16px;
  border-right: 2px solid #f0f0f0;
}

.visit-date .day {
  font-size: 32px;
  font-weight: 700;
  color: #1E88E5;
  line-height: 1;
}

.visit-date .month-year {
  font-size: 12px;
  color: #666;
  margin-top: 4px;
}

.visit-date .weekday {
  font-size: 12px;
  color: #999;
  margin-top: 2px;
}

.visit-content {
  flex: 1;
}

.visit-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.visit-hospital {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.visit-department {
  padding: 4px 10px;
  background: #E3F2FD;
  color: #1E88E5;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.visit-diagnosis {
  display: block;
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
}

.visit-tags {
  display: flex;
  gap: 6px;
  margin-bottom: 12px;
  flex-wrap: wrap;
}

.tag {
  padding: 3px 8px;
  font-size: 11px;
  border-radius: 10px;
  background: #F5F5F5;
  color: #666;
}

.visit-actions {
  display: flex;
  gap: 8px;
}

.action-btn {
  flex: 1;
  height: 36px;
  border: none;
  border-radius: 18px;
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
  transition: all 0.3s ease;
}

.action-btn.view {
  background: linear-gradient(135deg, #1E88E5, #64B5F6);
  color: white;
}

.action-btn.download {
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

.chronic-conditions,
.surgeries-section,
.allergies-section {
  margin-bottom: 20px;
}

.conditions-list,
.surgeries-list,
.allergies-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.condition-card,
.surgery-card,
.allergy-card {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 16px;
  padding: 16px;
  display: flex;
  align-items: center;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.condition-icon,
.surgery-icon,
.allergy-icon {
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

.condition-icon {
  background: linear-gradient(135deg, #4CAF50, #A5D6A7);
}

.surgery-icon {
  background: linear-gradient(135deg, #1E88E5, #64B5F6);
}

.allergy-icon {
  background: linear-gradient(135deg, #E53935, #EF9A9A);
}

.condition-info,
.surgery-info,
.allergy-info {
  flex: 1;
}

.condition-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 4px;
}

.condition-name,
.surgery-name,
.allergy-name {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.condition-status {
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.status-stable {
  background: #E8F5E8;
  color: #4CAF50;
}

.condition-diagnosed,
.surgery-date,
.allergy-reaction {
  display: block;
  font-size: 13px;
  color: #666;
  margin-bottom: 4px;
}

.condition-note,
.surgery-hospital {
  display: block;
  font-size: 13px;
  color: #1E88E5;
}

.allergy-severity {
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.severity-high {
  background: #FFEBEE;
  color: #E53935;
}

.severity-medium {
  background: #FFF3E0;
  color: #FF9800;
}

.add-record-btn {
  margin-top: 20px;
}

.add-record-btn button {
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

.add-record-btn button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(30, 136, 229, 0.4);
}

.add-record-btn i {
  font-size: 18px;
}
</style>
