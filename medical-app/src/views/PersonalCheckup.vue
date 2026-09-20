<template>
  <div class="personal-checkup">
    <div class="bg-container">
      <img src="/src/assets/images/personal-bg.png" alt="背景" class="bg-img">
    </div>
    <NavBar title="体检报告" :show-user="false" />
    
    <div class="content">
      <div class="report-banner">
        <div class="banner-content">
          <h2>我的体检报告</h2>
          <p>记录健康状况，关注身体变化</p>
        </div>
        <div class="banner-icon">
          <i class="fas fa-file-medical-alt"></i>
        </div>
      </div>

      <div class="report-stats">
        <div class="stat-item">
          <span class="stat-value">{{ totalReports }}</span>
          <span class="stat-label">总报告</span>
        </div>
        <div class="stat-item">
          <span class="stat-value normal">{{ normalCount }}</span>
          <span class="stat-label">正常</span>
        </div>
        <div class="stat-item">
          <span class="stat-value warning">{{ attentionCount }}</span>
          <span class="stat-label">需关注</span>
        </div>
      </div>

      <div class="report-tabs">
        <div 
          v-for="tab in reportTabs" 
          :key="tab.id"
          class="tab-item"
          :class="{ active: activeTab === tab.id }"
          @click="activeTab = tab.id"
        >
          {{ tab.name }}
        </div>
      </div>

      <div class="reports-section">
        <div class="section-header">
          <span class="section-title">报告列表</span>
        </div>

        <div class="reports-list">
          <div 
            v-for="report in filteredReports" 
            :key="report.id"
            class="report-card"
            @click="viewReport(report)"
          >
            <div class="report-icon" :class="report.bgClass">
              <i :class="report.icon"></i>
            </div>
            <div class="report-info">
              <div class="report-header">
                <span class="report-title">{{ report.title }}</span>
                <span class="report-status" :class="report.statusClass">{{ report.status }}</span>
              </div>
              <span class="report-hospital">{{ report.hospital }}</span>
              <span class="report-date">{{ report.date }}</span>
              <div class="report-tags">
                <span 
                  v-for="tag in report.tags" 
                  :key="tag"
                  class="tag"
                  :class="getTagClass(tag)"
                >
                  {{ tag }}
                </span>
              </div>
            </div>
            <div class="report-arrow">
              <img src="/src/assets/images/arrow-right.png" alt="右箭头">
            </div>
          </div>

          <div v-if="filteredReports.length === 0" class="empty-state">
            <div class="empty-icon">📄</div>
            <div>暂无体检报告</div>
            <div class="empty-hint">点击右上角上传新的体检报告</div>
          </div>
        </div>
      </div>

      <div class="health-indicators">
        <div class="section-header">
          <span class="section-title">关键指标趋势</span>
        </div>
        <div class="indicators-grid">
          <div 
            v-for="indicator in healthIndicators" 
            :key="indicator.id"
            class="indicator-card"
          >
            <div class="indicator-header">
              <span class="indicator-title">{{ indicator.name }}</span>
              <span class="indicator-status" :class="indicator.statusClass">{{ indicator.status }}</span>
            </div>
            <div class="indicator-value">
              {{ indicator.value }}<span class="indicator-unit">{{ indicator.unit }}</span>
            </div>
            <div class="indicator-trend">
              <span :class="indicator.trendClass">
                <i :class="indicator.trendIcon"></i>
                {{ indicator.trend }}
              </span>
              <span class="trend-text">较上次</span>
            </div>
          </div>
        </div>
      </div>

      <div class="abnormal-items">
        <div class="section-header">
          <span class="section-title">异常项提醒</span>
        </div>
        <div class="abnormal-list">
          <div 
            v-for="item in abnormalItems" 
            :key="item.id"
            class="abnormal-card"
            :class="item.severityClass"
          >
            <div class="abnormal-icon">
              <i :class="item.icon"></i>
            </div>
            <div class="abnormal-content">
              <div class="abnormal-header">
                <span class="abnormal-title">{{ item.title }}</span>
                <span class="abnormal-level" :class="item.levelClass">{{ item.level }}</span>
              </div>
              <p class="abnormal-desc">{{ item.desc }}</p>
              <p class="abnormal-suggestion">{{ item.suggestion }}</p>
            </div>
          </div>
        </div>
      </div>

      <div class="upload-section">
        <button class="upload-btn" @click="uploadReport">
          <i class="fas fa-cloud-upload-alt"></i>
          上传新报告
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import NavBar from '@/components/common/NavBar.vue'

const activeTab = ref('all')

const reportTabs = ref([
  { id: 'all', name: '全部' },
  { id: '2024', name: '2024 年' },
  { id: '2023', name: '2023 年' },
  { id: 'older', name: '更早' }
])

const reports = ref([
  {
    id: 1,
    title: '年度全面体检',
    hospital: '南昌大学第一附属医院',
    date: '2024-03-15',
    status: '正常',
    statusClass: 'normal',
    icon: 'fas fa-file-medical-alt',
    bgClass: 'bg-green',
    year: '2024',
    tags: ['血常规', '生化检查']
  },
  {
    id: 2,
    title: '入职体检',
    hospital: '江西省人民医院',
    date: '2024-02-20',
    status: '需关注',
    statusClass: 'warning',
    icon: 'fas fa-clipboard-check',
    bgClass: 'bg-orange',
    year: '2024',
    tags: ['肝功能', '肾功能']
  },
  {
    id: 3,
    title: '常规体检',
    hospital: '南昌大学第二附属医院',
    date: '2023-12-10',
    status: '正常',
    statusClass: 'normal',
    icon: 'fas fa-file-medical',
    bgClass: 'bg-blue',
    year: '2023',
    tags: ['基础检查']
  }
])

const healthIndicators = ref([
  {
    id: 1,
    name: '白细胞',
    value: 6.8,
    unit: '×10⁹/L',
    status: '正常',
    statusClass: 'normal',
    trend: '5%',
    trendIcon: 'fas fa-arrow-up',
    trendClass: 'trend-up'
  },
  {
    id: 2,
    name: '红细胞',
    value: 4.5,
    unit: '×10¹²/L',
    status: '正常',
    statusClass: 'normal',
    trend: '2%',
    trendIcon: 'fas fa-arrow-down',
    trendClass: 'trend-down'
  },
  {
    id: 3,
    name: '血糖',
    value: 5.2,
    unit: 'mmol/L',
    status: '正常',
    statusClass: 'normal',
    trend: '平稳',
    trendIcon: 'fas fa-minus',
    trendClass: 'trend-stable'
  },
  {
    id: 4,
    name: '胆固醇',
    value: 5.8,
    unit: 'mmol/L',
    status: '偏高',
    statusClass: 'warning',
    trend: '8%',
    trendIcon: 'fas fa-arrow-up',
    trendClass: 'trend-up-warning'
  }
])

const abnormalItems = ref([
  {
    id: 1,
    title: '总胆固醇偏高',
    level: '轻度',
    levelClass: 'level-mild',
    severity: 'mild',
    severityClass: 'card-mild',
    icon: 'fas fa-exclamation-triangle',
    desc: '检测值 5.8mmol/L，略高于正常范围（3.1-5.7mmol/L）',
    suggestion: '建议控制饮食，减少高脂肪食物摄入，适量运动'
  },
  {
    id: 2,
    title: '尿酸偏高',
    level: '轻度',
    levelClass: 'level-mild',
    severity: 'mild',
    severityClass: 'card-mild',
    icon: 'fas fa-flask',
    desc: '检测值 428μmol/L，略高于正常范围（208-428μmol/L）',
    suggestion: '建议多饮水，减少海鲜、动物内脏等高嘌呤食物'
  }
])

const totalReports = reports.value.length
const normalCount = reports.value.filter(r => r.status === '正常').length
const attentionCount = reports.value.filter(r => r.status === '需关注').length

const filteredReports = computed(() => {
  if (activeTab.value === 'all') {
    return reports.value
  }
  return reports.value.filter(report => report.year === activeTab.value)
})

const getTagClass = (tag) => {
  return 'tag-default'
}

const viewReport = (report) => {
  console.log('查看报告:', report)
}

const uploadReport = () => {
  console.log('上传报告')
}
</script>

<style scoped>
.personal-checkup {
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

.report-banner {
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

.report-stats {
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

.stat-value.normal {
  color: #4CAF50;
}

.stat-value.warning {
  color: #FF9800;
}

.stat-label {
  font-size: 14px;
  color: #666;
}

.report-tabs {
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

.reports-section {
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

.reports-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.report-card {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 16px;
  padding: 16px;
  display: flex;
  align-items: center;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  cursor: pointer;
  transition: transform 0.2s;
}

.report-card:hover {
  transform: translateY(-2px);
}

.report-icon {
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

.report-icon.bg-green {
  background: linear-gradient(135deg, #4CAF50, #A5D6A7);
}

.report-icon.bg-orange {
  background: linear-gradient(135deg, #FF9800, #FFE0B2);
}

.report-icon.bg-blue {
  background: linear-gradient(135deg, #1E88E5, #64B5F6);
}

.report-info {
  flex: 1;
  overflow: hidden;
}

.report-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 4px;
}

.report-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.report-status {
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.report-status.normal {
  background: #E8F5E8;
  color: #4CAF50;
}

.report-status.warning {
  background: #FFF3E0;
  color: #FF9800;
}

.report-hospital {
  display: block;
  font-size: 14px;
  color: #666;
  margin-bottom: 4px;
}

.report-date {
  display: block;
  font-size: 12px;
  color: #999;
  margin-bottom: 8px;
}

.report-tags {
  display: flex;
  gap: 6px;
  flex-wrap: wrap;
}

.tag {
  padding: 3px 8px;
  font-size: 11px;
  border-radius: 10px;
  background: #F5F5F5;
  color: #666;
}

.report-arrow {
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.report-arrow img {
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

.health-indicators {
  margin-bottom: 20px;
}

.indicators-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}

.indicator-card {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 16px;
  padding: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.indicator-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.indicator-title {
  font-size: 14px;
  color: #666;
}

.indicator-status {
  font-size: 12px;
  padding: 2px 8px;
  border-radius: 10px;
}

.indicator-status.normal {
  background: #E8F5E8;
  color: #4CAF50;
}

.indicator-status.warning {
  background: #FFF3E0;
  color: #FF9800;
}

.indicator-value {
  font-size: 24px;
  font-weight: 700;
  color: #1E88E5;
  margin-bottom: 8px;
}

.indicator-unit {
  font-size: 12px;
  color: #999;
  margin-left: 2px;
}

.indicator-trend {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
}

.trend-up {
  color: #E53935;
}

.trend-down {
  color: #4CAF50;
}

.trend-stable {
  color: #1E88E5;
}

.trend-up-warning {
  color: #FF9800;
}

.trend-text {
  color: #999;
}

.abnormal-items {
  margin-bottom: 20px;
}

.abnormal-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.abnormal-card {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 16px;
  padding: 16px;
  display: flex;
  align-items: flex-start;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  border-left: 4px solid;
}

.abnormal-card.card-mild {
  border-left-color: #FF9800;
}

.abnormal-card.card-moderate {
  border-left-color: #FF5722;
}

.abnormal-card.card-severe {
  border-left-color: #E53935;
}

.abnormal-icon {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  font-size: 18px;
  color: white;
  flex-shrink: 0;
}

.card-mild .abnormal-icon {
  background: linear-gradient(135deg, #FF9800, #FFE0B2);
}

.card-moderate .abnormal-icon {
  background: linear-gradient(135deg, #FF5722, #FFCCBC);
}

.card-severe .abnormal-icon {
  background: linear-gradient(135deg, #E53935, #EF9A9A);
}

.abnormal-content {
  flex: 1;
}

.abnormal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 6px;
}

.abnormal-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.abnormal-level {
  font-size: 12px;
  padding: 2px 8px;
  border-radius: 10px;
}

.level-mild {
  background: #FFF3E0;
  color: #FF9800;
}

.level-moderate {
  background: #FFEBEE;
  color: #FF5722;
}

.level-severe {
  background: #FFEBEE;
  color: #E53935;
}

.abnormal-desc {
  font-size: 14px;
  color: #666;
  margin: 0 0 6px 0;
  line-height: 1.5;
}

.abnormal-suggestion {
  font-size: 13px;
  color: #1E88E5;
  margin: 0;
  line-height: 1.5;
}

.upload-section {
  margin-top: 20px;
}

.upload-btn {
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

.upload-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(30, 136, 229, 0.4);
}

.upload-btn i {
  font-size: 18px;
}
</style>
