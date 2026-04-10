<template>
  <div class="reports-page">
    <div class="container">
      <!-- 页面头部 -->
      <div class="header">
        <BackButton @click="goBack" />
        <h1 class="page-title">我的报告</h1>
      </div>
      
      <!-- 筛选区域 -->
      <div class="filter-section">
        <h3 class="filter-title">筛选报告</h3>
        <div class="filter-options">
          <button 
            class="filter-btn" 
            :class="{ active: currentFilter === 'all' }"
            @click="setFilter('all')"
          >全部</button>
          <button 
            class="filter-btn" 
            :class="{ active: currentFilter === 'completed' }"
            @click="setFilter('completed')"
          >已出报告</button>
          <button 
            class="filter-btn" 
            :class="{ active: currentFilter === 'pending' }"
            @click="setFilter('pending')"
          >待审核</button>
          <button 
            class="filter-btn" 
            :class="{ active: currentFilter === 'abnormal' }"
            @click="setFilter('abnormal')"
          >异常报告</button>
        </div>
      </div>
      
      <!-- 报告列表 -->
      <div id="reportList">
        <div 
          v-for="report in filteredReports" 
          :key="report.id" 
          class="report-card"
        >
          <div class="card-header">
            <div class="report-info">
              <div class="report-icon">
                <i :class="report.icon"></i>
              </div>
              <div class="report-details">
                <h3>{{ report.type }}</h3>
                <p>检查日期：{{ report.date }} | 医生：{{ report.doctor }}</p>
              </div>
            </div>
            <div class="status-badge" :class="getStatusClass(report)">
              {{ getStatusText(report) }}
            </div>
          </div>
          <div class="report-summary">
            <div class="summary-item">
              <div class="summary-icon">
                <i class="fas fa-file-medical-alt"></i>
              </div>
              <div class="summary-text">
                报告摘要
                <strong>{{ report.summary }}</strong>
              </div>
            </div>
          </div>
          <div class="card-actions">
            <button class="action-btn btn-outline" @click="viewDetails(report)">
              <i class="fas fa-eye"></i> 查看详情
            </button>
            <button 
              v-if="report.status === 'completed'" 
              class="action-btn btn-success" 
              @click="downloadReport(report)"
            >
              <i class="fas fa-download"></i> 下载报告
            </button>
          </div>
        </div>
      </div>
      
      <!-- 空状态 -->
      <div v-if="filteredReports.length === 0" class="empty-state">
        <div class="empty-icon">
          <i class="fas fa-file-medical-alt"></i>
        </div>
        <h3 class="empty-text">暂无检查报告</h3>
        <a href="#" class="empty-btn">预约检查</a>
      </div>
    </div>
    
    <!-- 报告详情模态框 -->
    <Modal 
      :show="showDetailModal" 
      @close="closeDetailModal"
      :title="detailModalTitle"
    >
      <div class="modal-body">
        <div class="report-detail-content">
          <div class="report-detail-section">
            <h4 class="detail-section-title">基本信息</h4>
            <div class="detail-item">
              <div class="detail-label">检查类型</div>
              <div class="detail-value">{{ currentReport?.type }}</div>
            </div>
            <div class="detail-item">
              <div class="detail-label">检查日期</div>
              <div class="detail-value">{{ currentReport?.date }}</div>
            </div>
            <div class="detail-item">
              <div class="detail-label">负责医生</div>
              <div class="detail-value">{{ currentReport?.doctor }}</div>
            </div>
            <div class="detail-item">
              <div class="detail-label">报告状态</div>
              <div class="detail-value" :class="currentReport?.abnormal ? 'abnormal-value' : 'normal-value'">
                {{ currentReport?.abnormal ? '异常报告' : '正常报告' }}
              </div>
            </div>
          </div>
          
          <div class="report-detail-section">
            <h4 class="detail-section-title">报告摘要</h4>
            <p>{{ currentReport?.summary }}</p>
          </div>
          
          <!-- 指标表格 -->
          <div v-if="currentReport?.indicators && currentReport.indicators.length > 0" class="report-detail-section">
            <h4 class="detail-section-title">检查指标</h4>
            <table class="indicator-table">
              <thead>
                <tr>
                  <th>指标名称</th>
                  <th>检查结果</th>
                  <th>参考范围</th>
                  <th>状态</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="indicator in currentReport.indicators" :key="indicator.name">
                  <td>{{ indicator.name }}</td>
                  <td>{{ indicator.value }} {{ indicator.unit }}</td>
                  <td>{{ indicator.normalRange }}</td>
                  <td :class="indicator.status">{{ indicator.status === 'normal' ? '正常' : '异常' }}</td>
                </tr>
              </tbody>
            </table>
          </div>
          
          <!-- 医生建议 -->
          <div class="report-detail-section">
            <h4 class="detail-section-title">医生建议</h4>
            <p>{{ currentReport?.abnormal ? 
              '建议您定期复查，注意生活习惯，如有不适请及时就医。' : 
              '检查结果正常，请继续保持良好的生活习惯，定期体检。'
            }}</p>
          </div>
        </div>
      </div>
      <template #footer>
        <button class="action-btn btn-outline" @click="shareReport">
          <i class="fas fa-share-alt"></i> 分享报告
        </button>
        <button class="action-btn btn-primary" @click="downloadReport(currentReport)">
          <i class="fas fa-download"></i> 下载报告
        </button>
      </template>
    </Modal>
  </div>
</template>

<script>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import Modal from '@/components/common/Modal.vue'
import BackButton from '@/components/common/BackButton.vue'

export default {
  name: 'MedicalReports',
  components: {
    Modal,
    BackButton
  },
  setup() {
    const router = useRouter()
    
    // 模拟报告数据
    const reports = ref([
      {
        id: 1,
        type: "血常规检查",
        icon: "fas fa-tint",
        date: "2023-11-15",
        doctor: "张医生",
        status: "completed",
        summary: "检查结果基本正常，部分指标轻微异常",
        abnormal: true,
        indicators: [
          { name: "白细胞计数", value: "6.2", unit: "×10^9/L", normalRange: "4.0-10.0", status: "normal" },
          { name: "红细胞计数", value: "4.8", unit: "×10^12/L", normalRange: "4.3-5.8", status: "normal" },
          { name: "血红蛋白", value: "145", unit: "g/L", normalRange: "130-175", status: "normal" },
          { name: "血小板计数", value: "320", unit: "×10^9/L", normalRange: "100-300", status: "abnormal" }
        ]
      },
      {
        id: 2,
        type: "眼科检查",
        icon: "fas fa-eye",
        date: "2023-11-10",
        doctor: "王医生",
        status: "completed",
        summary: "视力正常，眼底检查无异常",
        abnormal: false,
        indicators: [
          { name: "右眼视力", value: "1.0", unit: "", normalRange: "≥0.8", status: "normal" },
          { name: "左眼视力", value: "1.0", unit: "", normalRange: "≥0.8", status: "normal" },
          { name: "眼压", value: "16", unit: "mmHg", normalRange: "10-21", status: "normal" }
        ]
      },
      {
        id: 3,
        type: "肝功能检查",
        icon: "fas fa-liver",
        date: "2023-11-05",
        doctor: "李医生",
        status: "completed",
        summary: "肝功能指标正常，无明显异常",
        abnormal: false,
        indicators: [
          { name: "谷丙转氨酶", value: "28", unit: "U/L", normalRange: "0-40", status: "normal" },
          { name: "谷草转氨酶", value: "25", unit: "U/L", normalRange: "0-40", status: "normal" },
          { name: "总胆红素", value: "12.5", unit: "μmol/L", normalRange: "3.4-20.5", status: "normal" }
        ]
      },
      {
        id: 4,
        type: "心电图检查",
        icon: "fas fa-heartbeat",
        date: "2023-11-25",
        doctor: "赵医生",
        status: "pending",
        summary: "检查已完成，等待医生审核",
        abnormal: false
      },
      {
        id: 5,
        type: "胸部X光检查",
        icon: "fas fa-x-ray",
        date: "2023-11-20",
        doctor: "陈医生",
        status: "completed",
        summary: "肺部纹理增粗，建议随访观察",
        abnormal: true
      }
    ])

    const currentFilter = ref('all')
    const showDetailModal = ref(false)
    const currentReport = ref(null)

    // 计算属性：筛选后的报告列表
    const filteredReports = computed(() => {
      if (currentFilter.value === 'all') {
        return reports.value
      } else if (currentFilter.value === 'abnormal') {
        return reports.value.filter(report => report.abnormal)
      } else {
        return reports.value.filter(report => report.status === currentFilter.value)
      }
    })

    // 计算属性：详情模态框标题
    const detailModalTitle = computed(() => {
      return currentReport.value ? `${currentReport.value.type} - 报告详情` : '报告详情'
    })

    // 设置筛选条件
    const setFilter = (filter) => {
      currentFilter.value = filter
    }

    // 获取状态文本
    const getStatusText = (report) => {
      if (report.status === 'completed') {
        return report.abnormal ? '异常报告' : '已出报告'
      } else if (report.status === 'pending') {
        return '待审核'
      }
      return ''
    }

    // 获取状态类名
    const getStatusClass = (report) => {
      if (report.status === 'completed') {
        return report.abnormal ? 'status-abnormal' : 'status-completed'
      } else if (report.status === 'pending') {
        return 'status-pending'
      }
      return ''
    }

    // 查看详情
    const viewDetails = (report) => {
      currentReport.value = report
      showDetailModal.value = true
    }

    // 关闭详情模态框
    const closeDetailModal = () => {
      showDetailModal.value = false
      currentReport.value = null
    }

    // 下载报告
    const downloadReport = (report) => {
      // 在实际应用中，这里应该发送请求到服务器下载报告文件
      console.log('下载报告:', report)
      // 这里可以添加提示消息
    }

    // 分享报告
    const shareReport = () => {
      // 在实际应用中，这里应该实现分享功能
      console.log('分享报告')
      // 这里可以添加提示消息
    }

    // 返回上一页
    const goBack = () => {
      router.back()
    }

    return {
      reports,
      currentFilter,
      showDetailModal,
      currentReport,
      filteredReports,
      detailModalTitle,
      setFilter,
      getStatusText,
      getStatusClass,
      viewDetails,
      closeDetailModal,
      downloadReport,
      shareReport,
      goBack
    }
  }
}
</script>

<style scoped>
/* 复制原有的CSS样式，但移除模态框相关的样式，因为Modal组件已经独立 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;
}

:root {
  --primary: #1E88E5;
  --primary-dark: #1565C0;
  --secondary: #00BCD4;
  --success: #4CAF50;
  --warning: #FF9800;
  --danger: #E53935;
  --light: #f5f7fa;
  --dark: #333;
  --gray: #666;
  --border: #e0e0e0;
  --card: #fff;
}

body {
  background-color: var(--light);
  color: var(--dark);
  line-height: 1.6;
}

.container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

/* 头部样式 */
.header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid var(--border);
}

.page-title {
  font-size: 22px;
  font-weight: 600;
  color: var(--dark);
}

/* 筛选区域 */
.filter-section {
  background: var(--card);
  border-radius: 12px;
  padding: 15px;
  margin-bottom: 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.filter-title {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 12px;
  color: var(--dark);
}

.filter-options {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.filter-btn {
  padding: 8px 16px;
  border-radius: 20px;
  background: var(--light);
  border: 1px solid var(--border);
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s;
}

.filter-btn.active {
  background: var(--primary);
  color: white;
  border-color: var(--primary);
}

.filter-btn:hover:not(.active) {
  background: #e9ecef;
}

/* 报告卡片 */
.report-card {
  background: var(--card);
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 15px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  transition: all 0.3s;
}

.report-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 15px;
}

.report-info {
  display: flex;
  align-items: center;
}

.report-icon {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--primary), var(--secondary));
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 20px;
  margin-right: 12px;
}

.report-details h3 {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 5px;
}

.report-details p {
  font-size: 14px;
  color: var(--gray);
}

.status-badge {
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
}

.status-completed {
  background: #D1ECF1;
  color: #0C5460;
}

.status-pending {
  background: #FFF3CD;
  color: #856404;
}

.status-abnormal {
  background: #F8D7DA;
  color: #721C24;
}

.report-summary {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 15px;
  margin-bottom: 15px;
}

.summary-item {
  display: flex;
  align-items: center;
}

.summary-icon {
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--light);
  border-radius: 50%;
  margin-right: 10px;
  color: var(--primary);
}

.summary-text {
  font-size: 14px;
  color: var(--gray);
}

.summary-text strong {
  color: var(--dark);
  display: block;
  font-size: 15px;
}

.card-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
}

.action-btn {
  padding: 8px 16px;
  border-radius: 6px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s;
  border: none;
  display: flex;
  align-items: center;
  gap: 5px;
}

.btn-primary {
  background: var(--primary);
  color: white;
}

.btn-primary:hover {
  background: var(--primary-dark);
}

.btn-outline {
  background: transparent;
  border: 1px solid var(--primary);
  color: var(--primary);
}

.btn-outline:hover {
  background: var(--primary);
  color: white;
}

.btn-success {
  background: var(--success);
  color: white;
}

.btn-success:hover {
  background: #388E3C;
}

/* 空状态 */
.empty-state {
  text-align: center;
  padding: 40px 20px;
  color: var(--gray);
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 15px;
  color: #bdbdbd;
}

.empty-text {
  font-size: 16px;
  margin-bottom: 20px;
}

.empty-btn {
  display: inline-block;
  padding: 10px 20px;
  background: var(--primary);
  color: white;
  border-radius: 6px;
  text-decoration: none;
  font-weight: 500;
  transition: all 0.3s;
}

.empty-btn:hover {
  background: var(--primary-dark);
}

/* 报告详情样式 */
.report-detail-section {
  margin-bottom: 20px;
}

.detail-section-title {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 10px;
  padding-bottom: 8px;
  border-bottom: 1px solid var(--border);
}

.detail-item {
  display: flex;
  margin-bottom: 8px;
}

.detail-label {
  width: 120px;
  color: var(--gray);
  font-size: 14px;
}

.detail-value {
  flex: 1;
  font-size: 14px;
}

.normal-value {
  color: var(--success);
}

.abnormal-value {
  color: var(--danger);
}

/* 指标表格 */
.indicator-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 10px;
}

.indicator-table th,
.indicator-table td {
  padding: 10px;
  text-align: left;
  border-bottom: 1px solid var(--border);
}

.indicator-table th {
  background: var(--light);
  font-weight: 600;
}

.indicator-table tr:last-child td {
  border-bottom: none;
}

.normal {
  color: var(--success);
}

.abnormal {
  color: var(--danger);
}

/* 响应式设计 */
@media (max-width: 600px) {
  .container {
    padding: 15px;
  }
  
  .report-summary {
    grid-template-columns: 1fr;
  }
  
  .card-actions {
    flex-direction: column;
  }
  
  .action-btn {
    width: 100%;
    justify-content: center;
  }
  
  .detail-item {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .detail-label {
    width: 100%;
    margin-bottom: 4px;
  }
}
</style>