<template>
  <div class="personal-reports">
    <div class="bg-container">
      <img src="/src/assets/images/personal-bg.png" alt="背景" class="bg-img">
    </div>
    <NavBar title="我的报告" :show-user="false" />
    
    <div class="content">
      <div class="report-stats">
        <div class="stat-item">
          <span class="stat-value">{{ totalReports }}</span>
          <span class="stat-label">总报告</span>
        </div>
        <div class="stat-item">
          <span class="stat-value normal">{{ normalReports }}</span>
          <span class="stat-label">正常</span>
        </div>
        <div class="stat-item">
          <span class="stat-value warning">{{ attentionReports }}</span>
          <span class="stat-label">需关注</span>
        </div>
      </div>

      <div class="reports-section">
        <div class="section-header">
          <span class="section-title">报告列表</span>
        </div>

        <div class="reports-list">
          <div 
            v-for="report in reports" 
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
            </div>
            <div class="report-arrow">
              <img src="/src/assets/images/arrow-right.png" alt="右箭头">
            </div>
          </div>

          <div v-if="reports.length === 0" class="empty-state">
            <div class="empty-icon">📄</div>
            <div>暂无报告</div>
            <div class="empty-hint">上传您的体检报告开始记录</div>
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
import { ref } from 'vue'
import NavBar from '@/components/common/NavBar.vue'

const reports = ref([
  {
    id: 1,
    title: '年度体检报告',
    hospital: '南昌大学第一附属医院',
    date: '2024-03-15',
    status: '正常',
    statusClass: 'normal',
    icon: 'fas fa-file-medical-alt',
    bgClass: 'bg-green'
  },
  {
    id: 2,
    title: '血常规检查',
    hospital: '江西省人民医院',
    date: '2024-02-20',
    status: '需关注',
    statusClass: 'warning',
    icon: 'fas fa-vial',
    bgClass: 'bg-orange'
  },
  {
    id: 3,
    title: '胸部X光检查',
    hospital: '南昌大学第二附属医院',
    date: '2024-01-10',
    status: '正常',
    statusClass: 'normal',
    icon: 'fas fa-x-ray',
    bgClass: 'bg-blue'
  }
])

const totalReports = reports.value.length
const normalReports = reports.value.filter(r => r.status === '正常').length
const attentionReports = reports.value.filter(r => r.status === '需关注').length

const viewReport = (report) => {
  console.log('查看报告:', report)
}

const uploadReport = () => {
  console.log('上传报告')
}
</script>

<style scoped>
.personal-reports {
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
