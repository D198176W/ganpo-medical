<template>
  <div class="medical-resources-container">
    <!-- 顶部导航栏 -->
    <div class="nav-bar">
      <div class="back-btn" @click="$router.back()">
        <div class="back-icon">←</div>
      </div>
      <div class="title">南昌本地医疗资源</div>
    </div>

    <!-- 功能选项卡 -->
    <div class="tabs">
      <div class="tab-item" :class="{ active: activeTab === 0 }" @click="switchTab(0)">
        医院查询
      </div>
      <div class="tab-item" :class="{ active: activeTab === 1 }" @click="switchTab(1)">
        医保服务
      </div>
    </div>

    <!-- 医院查询模块 -->
    <div class="hospital-module" :class="{ active: activeTab === 0 }">
     
      <!-- 科室筛选 -->
      <div class="department-filter">
        <span class="filter-title">科室筛选：</span>
        <div class="department-scroll" v-if="!loadingDepartments">
          <div 
            v-for="dept in departments" 
            :key="dept.id"
            class="department-item" 
            :class="{ selected: selectedDepartment === dept.id }"
            @click="selectDepartment(dept)"
          >
            {{ dept.name }}
          </div>
        </div>
        <div class="loading" v-if="loadingDepartments"></div>
        <div class="error-text" v-if="errorDepartments">
          获取科室失败，请重试
        </div>
      </div>

      <!-- 排序选项 -->
      <div class="sort-options">
        <div 
          v-for="sort in sortOptions" 
          :key="sort.type"
          class="sort-item" 
          :class="{ active: sortType === sort.type }"
          @click="setSortType(sort.type)"
        >
          {{ sort.name }}
        </div>
      </div>
      
       <!-- 医院列表 -->
      <div class="hospital-list">
        <div 
          class="loading" 
          v-if="loadingHospitals && hospitals.length === 0"
          style="margin: 40px auto;"
        ></div>
        
        <div 
          v-for="hospital in hospitals" 
          :key="hospital.id"
          class="hospital-card"
          v-if="!loadingHospitals"
        >
          <div class="hospital-header">
            <div class="hospital-logo">
              <div class="logo-placeholder">{{ hospital.name.substring(0, 2) }}</div>
            </div>
            <div class="hospital-info">
              <span class="hospital-name">{{ hospital.name }}</span>
              <div class="hospital-level-distance">
                <span class="hospital-level">{{ hospital.level }}</span>
                <span class="hospital-distance" v-if="hospital.distance !== null && hospital.distance !== undefined">{{ hospital.distance.toFixed(1) }}km</span>
                <span class="hospital-distance" v-else>--</span>
              </div>
              <div class="hospital-rating-availability">
                <span class="hospital-rating">★ {{ hospital.rating }}</span>
                <span 
                  class="hospital-availability" 
                  :class="{ available: hospital.hasOpenSlots }"
                >
                  {{ hospital.hasOpenSlots ? '有号源' : '无号源' }}
                </span>
              </div>
            </div>
          </div>
          
          <div class="hospital-departments">
            <span class="departments-title">核心科室：</span>
            <span class="departments-content">{{ hospital.coreDepartments.join('、') }}</span>
          </div>
          
          <div class="hospital-address">
            {{ hospital.address }}
          </div>
          
          <div class="hospital-actions">
            <button class="detail-btn" @click="viewHospitalDetail(hospital.id)">
              查看详情
            </button>
            <button 
              class="register-btn" 
              :disabled="!hospital.hasOpenSlots"
              :class="{ disabled: !hospital.hasOpenSlots }"
              @click="gotoRegistration(hospital.id)"
            >
              {{ hospital.hasOpenSlots ? '立即挂号' : '暂无号源' }}
            </button>
          </div>
        </div>

        <!-- 无数据提示 -->
        <div v-if="!loadingHospitals && hospitals.length === 0" class="no-data">
          <div class="no-data-text">暂无符合条件的医院</div>
          <div class="no-data-desc">请尝试选择其他科室或排序方式</div>
        </div>

        <!-- 分页控件 -->
        <div class="pagination" v-if="!loadingHospitals && totalHospitals > 0">
          <button 
            @click="handlePageChange(currentPage - 1)" 
            :disabled="currentPage === 1"
          >
            上一页
          </button>
          <span>
            第 {{ currentPage }} 页 / 共 {{ Math.ceil(totalHospitals / pageSize) }} 页
          </span>
          <button 
            @click="handlePageChange(currentPage + 1)" 
            :disabled="currentPage >= Math.ceil(totalHospitals / pageSize)"
          >
            下一页
          </button>
        </div>
      </div>

    </div>

    <!-- 医保服务模块 -->
    <div class="insurance-module" :class="{ active: activeTab === 1 }">
      <div class="insurance-container">
        <!-- 医保报销计算器 -->
        <div class="calculator-section">
          <div class="section-title">💰 大学生医保报销计算器</div>
          <div class="calculator-form">
            <div class="form-group">
              <label>医疗费用总额（元）：</label>
              <input 
                type="number" 
                v-model="medicalCost" 
                placeholder="请输入金额"
                @input="calculateReimbursement"
              />
            </div>
            <div class="form-group" v-if="medicalCost > 0">
              <label>费用类型：</label>
              <div class="type-selector">
                <div 
                  v-for="type in expenseTypes" 
                  :key="type.key"
                  class="type-option"
                  :class="{ selected: selectedType === type.key }"
                  @click="selectedType = type.key; calculateReimbursement()"
                >
                  {{ type.label }}
                </div>
              </div>
            </div>
            <div class="result-box" v-if="medicalCost > 0">
              <div class="result-title">报销结果</div>
              <div class="result-item">
                <span class="label">总费用：</span>
                <span class="value">¥{{ medicalCost.toFixed(2) }}</span>
              </div>
              <div class="result-item">
                <span class="label">报销比例：</span>
                <span class="value highlight">{{ currentRatio }}%</span>
              </div>
              <div class="result-item">
                <span class="label">可报销金额：</span>
                <span class="value success">¥{{ reimbursedAmount.toFixed(2) }}</span>
              </div>
              <div class="result-item">
                <span class="label">自付金额：</span>
                <span class="value warning">¥{{ selfPaidAmount.toFixed(2) }}</span>
              </div>
              <div class="result-note" v-if="reimbursedAmount >= annualLimit">
                ⚠️ 已达到年度报销封顶线（¥{{ annualLimit.toFixed(2) }}）
              </div>
            </div>
          </div>
        </div>

        <!-- 医保政策解读 -->
        <div class="policy-section">
          <div class="section-title">📋 大学生医保政策解读</div>
          <div class="policy-content" v-if="insuranceRules">
            <div class="policy-card">
              <div class="card-header">报销比例</div>
              <div class="card-body">
                <div class="ratio-item">
                  <span class="ratio-type">门诊费用</span>
                  <span class="ratio-value">{{ insuranceRules.outpatientRatio }}%</span>
                </div>
                <div class="ratio-item">
                  <span class="ratio-type">住院费用</span>
                  <span class="ratio-value">{{ insuranceRules.inpatientRatio }}%</span>
                </div>
                <div class="ratio-item">
                  <span class="ratio-type">检查费用</span>
                  <span class="ratio-value">{{ insuranceRules.examRatio }}%</span>
                </div>
                <div class="ratio-item">
                  <span class="ratio-type">药品费用</span>
                  <span class="ratio-value">{{ insuranceRules.drugRatio }}%</span>
                </div>
              </div>
            </div>
            <div class="policy-card">
              <div class="card-header">年度报销封顶线</div>
              <div class="card-body">
                <div class="limit-value">¥{{ insuranceRules.maxPerYear }}</div>
              </div>
            </div>
            <div class="policy-card">
              <div class="card-header">报销流程</div>
              <div class="card-body">
                <div class="process-step">
                  <div class="step-number">1</div>
                  <div class="step-content">
                    <div class="step-title">准备材料</div>
                    <div class="step-desc">门诊病历、发票原件、费用清单、身份证、学生证</div>
                  </div>
                </div>
                <div class="process-step">
                  <div class="step-number">2</div>
                  <div class="step-content">
                    <div class="step-title">提交申请</div>
                    <div class="step-desc">到学校医务室或社保局提交报销申请</div>
                  </div>
                </div>
                <div class="process-step">
                  <div class="step-number">3</div>
                  <div class="step-content">
                    <div class="step-title">审核审批</div>
                    <div class="step-desc">一般 5-10 个工作日完成审核</div>
                  </div>
                </div>
                <div class="process-step">
                  <div class="step-number">4</div>
                  <div class="step-content">
                    <div class="step-title">领取报销款</div>
                    <div class="step-desc">审核通过后，报销款打入学生银行卡</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="loading" v-if="!insuranceRules">加载中...</div>
        </div>

        <!-- 常见问题 -->
        <div class="faq-section">
          <div class="section-title">❓ 常见问题</div>
          <div class="faq-list">
            <div 
              v-for="(faq, index) in faqList" 
              :key="index"
              class="faq-item"
              :class="{ expanded: expandedFaq === index }"
            >
              <div class="faq-question" @click="toggleFaq(index)">
                <span>{{ faq.question }}</span>
                <span class="arrow">{{ expandedFaq === index ? '▼' : '▶' }}</span>
              </div>
              <div class="faq-answer" v-show="expandedFaq === index">
                {{ faq.answer }}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request'

export default {
  name: 'MedicalResources',
  data() {
    return {
      activeTab: 0,
      selectedDepartment: 'all',
      selectedDepartmentName: null,
      sortType: 'distance',
      
      // 医院列表相关
      hospitals: [],
      departments: [],
      loadingDepartments: false,
      loadingHospitals: false,
      errorDepartments: false,
      currentPage: 1,
      pageSize: 10,
      totalHospitals: 0,
      
      // 医保服务相关
      medicalCost: 0,
      selectedType: 'outpatient',
      expenseTypes: [
        { key: 'outpatient', label: '门诊' },
        { key: 'inpatient', label: '住院' },
        { key: 'exam', label: '检查' },
        { key: 'drug', label: '药品' }
      ],
      insuranceRules: null,
      expandedFaq: 0,
      faqList: [
        {
          question: '大学生如何参加医保？',
          answer: '大学生由学校统一组织参保，每年 9 月份集中办理。参保费用一般由学校代扣代缴，学生无需单独办理。'
        },
        {
          question: '异地就医可以报销吗？',
          answer: '可以。异地就医需先在参保地医保局办理异地就医备案手续，然后在异地定点医院就医，回参保地报销。'
        },
        {
          question: '哪些费用不能报销？',
          answer: '自费项目、美容整形、非医保目录内药品、因打架斗殴等违法行为导致的医疗费用等不在报销范围内。'
        },
        {
          question: '报销有起付线吗？',
          answer: '门诊报销没有起付线，住院报销有起付线，一般为 500-1000 元，起付线以下的费用由个人承担。'
        },
        {
          question: '报销周期是多久？',
          answer: '一般 5-10 个工作日完成审核，审核通过后报销款会在 3-5 个工作日内打入学生银行卡。'
        }
      ],
      
      // 排序选项
      sortOptions: [
        { type: 'distance', name: '距离排序' },
        { type: 'level', name: '等级排序' },
        { type: 'rating', name: '评分排序' },
        { type: 'availability', name: '号源排序' }
      ]
    }
  },
  computed: {
    // 根据选择的费用类型获取报销比例
    currentRatio() {
      if (!this.insuranceRules) return 0
      switch(this.selectedType) {
        case 'outpatient': return this.insuranceRules.outpatientRatio
        case 'inpatient': return this.insuranceRules.inpatientRatio
        case 'exam': return this.insuranceRules.examRatio
        case 'drug': return this.insuranceRules.drugRatio
        default: return 0
      }
    },
    // 可报销金额
    reimbursedAmount() {
      if (!this.insuranceRules || this.medicalCost <= 0) return 0
      const ratio = this.currentRatio / 100
      const amount = this.medicalCost * ratio
      return Math.min(amount, this.annualLimit)
    },
    // 自付金额
    selfPaidAmount() {
      return this.medicalCost - this.reimbursedAmount
    },
    // 年度报销封顶线
    annualLimit() {
      return this.insuranceRules ? this.insuranceRules.maxPerYear : 0
    }
  },
  methods: {
    switchTab(tab) {
      this.activeTab = tab
      // 切换到医保服务时加载数据
      if (tab === 1 && !this.insuranceRules) {
        this.loadInsuranceRules()
      }
    },
    // 医院查询相关方法
    selectDepartment(dept) {
      this.selectedDepartment = dept.id
      this.selectedDepartmentName = dept.name
      this.currentPage = 1
      this.fetchHospitals()
    },
    setSortType(type) {
      this.sortType = type
      this.currentPage = 1
      this.fetchHospitals()
    },
    handlePageChange(page) {
      const maxPage = Math.max(1, Math.ceil(this.totalHospitals / this.pageSize))
      if (page < 1 || page > maxPage) return
      this.currentPage = page
      this.fetchHospitals()
    },
    async viewHospitalDetail(hospitalId) {
      try {
        this.loadingHospitals = true
        const res = await request.get(`/api/hospitals/${hospitalId}`)
        if (res && res.data) {
          const detail = res.data
          if (detail.detailUrl) {
            window.open(detail.detailUrl, '_blank')
          } else {
            alert('该医院暂无详情页链接')
          }
        }
      } catch (err) {
        console.error('获取医院详情失败', err)
        alert('获取医院详情失败，请重试')
      } finally {
        this.loadingHospitals = false
      }
    },
    async gotoRegistration(hospitalId) {
      try {
        this.loadingHospitals = true
        const res = await request.get(`/api/hospitals/${hospitalId}`)
        if (res && res.data) {
          const detail = res.data
          if (detail.hasOpenSlots && detail.registrationUrl) {
            window.open(detail.registrationUrl, '_blank')
          } else if (!detail.hasOpenSlots) {
            alert('该医院暂无号源')
          } else {
            alert('暂未开通该医院挂号服务')
          }
        }
      } catch (err) {
        console.error('获取挂号信息失败', err)
        alert('获取挂号信息失败，请重试')
      } finally {
        this.loadingHospitals = false
      }
    },
    async fetchHospitals() {
      this.loadingHospitals = true
      try {
        let lat, lng
        try {
          const position = await new Promise((resolve, reject) => {
            navigator.geolocation.getCurrentPosition(resolve, reject, { timeout: 5000 })
          })
          lat = position.coords.latitude
          lng = position.coords.longitude
        } catch (err) {
          console.warn('无法获取位置，使用默认位置', err)
          lat = 28.683569
          lng = 115.858225
        }

        const params = {
          page: this.currentPage,
          size: this.pageSize,
          departmentId: this.selectedDepartment === 'all' ? '' : (this.selectedDepartmentName || ''),
          sort: this.sortType,
          lat,
          lng
        }
        
        const res = await request.get('/api/hospitals', { params })
        if (res && res.data) {
          this.hospitals = (res.data.records || []).map(item => ({
            ...item,
            distance: item.distance ? Number(item.distance) : null,
            rating: Number(item.rating || 0),
            coreDepartments: Array.isArray(item.coreDepartments) ? item.coreDepartments : []
          }))
          this.totalHospitals = Number(res.data.total || 0)
        }
      } catch (err) {
        console.error('获取医院列表失败', err)
        alert('获取医院信息失败，请重试')
      } finally {
        this.loadingHospitals = false
      }
    },
    
    // 医保服务相关方法
    async loadInsuranceRules() {
      try {
        const res = await request.get('/api/reimbursement/rules', {
          params: { crowd: 'student' }
        })
        if (res && res.data && res.data.length > 0) {
          this.insuranceRules = res.data[0]
        }
      } catch (err) {
        console.error('获取医保规则失败', err)
        // 使用默认规则
        this.insuranceRules = {
          outpatientRatio: 50,
          inpatientRatio: 80,
          examRatio: 70,
          drugRatio: 60,
          maxPerYear: 10000
        }
      }
    },
    calculateReimbursement() {
      // 计算逻辑在 computed 中已实现
    },
    toggleFaq(index) {
      this.expandedFaq = this.expandedFaq === index ? -1 : index
    }
  },
  async mounted() {
    // 加载科室数据
    try {
      this.loadingDepartments = true
      const res = await request.get('/api/departments')
      if (res && Array.isArray(res.data)) {
        this.departments = [{ id: 'all', name: '全部科室' }, ...res.data]
      }
    } catch (err) {
      console.error('获取科室失败', err)
      this.errorDepartments = true
    } finally {
      this.loadingDepartments = false
    }

    // 初始化加载医院数据
    this.fetchHospitals()
  }
}
</script>

<style scoped>
.medical-resources-container {
  width: 100%;
  min-height: 100vh;
  background-color: #f5f7fa;
  color: #333;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'PingFang SC', 'Microsoft YaHei', sans-serif;
}

.nav-bar {
  height: 48px;
  display: flex;
  align-items: center;
  padding: 0 16px;
  background: linear-gradient(135deg, #1E88E5, #1565C0);
  color: #fff;
  position: sticky;
  top: 0;
  z-index: 100;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.back-btn {
  width: 40px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

.back-icon {
  font-size: 20px;
  font-weight: bold;
}

.title {
  flex: 1;
  font-size: 18px;
  font-weight: 700;
  text-align: center;
  letter-spacing: 1px;
}

.tabs {
  display: flex;
  background-color: #fff;
  border-bottom: 1px solid #e0e0e0;
  position: sticky;
  top: 48px;
  z-index: 99;
}

.tab-item {
  flex: 1;
  height: 48px;
  line-height: 48px;
  text-align: center;
  font-size: 16px;
  color: #666;
  cursor: pointer;
  transition: all 0.3s ease;
}

.tab-item.active {
  color: #1E88E5;
  font-weight: 600;
  border-bottom: 3px solid #1E88E5;
}

.hospital-module, .insurance-module {
  display: none;
  padding: 16px;
}

.hospital-module.active, .insurance-module.active {
  display: block;
}

/* 科室筛选 */
.department-filter {
  margin-bottom: 16px;
}

.filter-title {
  font-size: 14px;
  font-weight: 600;
  color: #333;
  margin-bottom: 8px;
  display: block;
}

.department-scroll {
  display: flex;
  overflow-x: auto;
  gap: 8px;
  padding-bottom: 8px;
}

.department-item {
  flex-shrink: 0;
  padding: 8px 16px;
  background-color: #f5f7fa;
  border-radius: 20px;
  font-size: 14px;
  color: #666;
  cursor: pointer;
  transition: all 0.2s ease;
  border: 1px solid #e0e0e0;
}

.department-item.selected {
  background-color: #1E88E5;
  color: #fff;
  border-color: #1E88E5;
  box-shadow: 0 2px 4px rgba(30, 136, 229, 0.3);
}

/* 排序选项 */
.sort-options {
  display: flex;
  margin-bottom: 16px;
  background-color: #fff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.sort-item {
  flex: 1;
  height: 44px;
  line-height: 44px;
  text-align: center;
  background-color: #fff;
  font-size: 14px;
  color: #666;
  cursor: pointer;
  transition: all 0.2s ease;
  position: relative;
}

.sort-item:not(:last-child)::after {
  content: '';
  position: absolute;
  right: 0;
  top: 50%;
  transform: translateY(-50%);
  height: 20px;
  width: 1px;
  background-color: #e0e0e0;
}

.sort-item.active {
  background-color: #1E88E5;
  color: #fff;
  font-weight: 500;
}

/* 医院列表 */
.hospital-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.hospital-card {
  background-color: #fff;
  border-radius: 12px;
  padding: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.hospital-header {
  display: flex;
  margin-bottom: 12px;
}

.hospital-logo {
  width: 60px;
  height: 60px;
  margin-right: 12px;
  flex-shrink: 0;
  border-radius: 8px;
  overflow: hidden;
  background: linear-gradient(135deg, #1E88E5, #1565C0);
  color: white;
  font-weight: bold;
  display: flex;
  align-items: center;
  justify-content: center;
}

.logo-placeholder {
  font-size: 14px;
}

.hospital-info {
  flex: 1;
}

.hospital-name {
  font-size: 16px;
  font-weight: 700;
  color: #333;
  margin-bottom: 6px;
  display: block;
}

.hospital-level-distance {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 14px;
  flex-wrap: wrap;
}

.hospital-level {
  color: #E53935;
  background-color: rgba(229, 57, 53, 0.1);
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
}

.hospital-distance {
  color: #666;
}

.hospital-rating-availability {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-top: 4px;
  font-size: 12px;
}

.hospital-rating {
  color: #FF9800;
}

.hospital-availability {
  color: #F44336;
}

.hospital-availability.available {
  color: #4CAF50;
}

.hospital-departments {
  margin-bottom: 12px;
  font-size: 14px;
  line-height: 1.5;
}

.departments-title {
  color: #666;
  margin-right: 4px;
}

.departments-content {
  color: #333;
}

.hospital-address {
  font-size: 14px;
  color: #666;
  margin-bottom: 16px;
}

.hospital-actions {
  display: flex;
  gap: 12px;
}

.register-btn, .detail-btn {
  flex: 1;
  height: 40px;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
}

.register-btn {
  background-color: #E53935;
  color: #fff;
}

.register-btn.disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.detail-btn {
  background-color: #f5f7fa;
  color: #666;
}

.no-data {
  text-align: center;
  padding: 40px 20px;
  color: #999;
}

.no-data-text {
  font-size: 16px;
  margin-bottom: 8px;
}

.no-data-desc {
  font-size: 14px;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 16px;
  margin-top: 20px;
  padding: 16px;
}

.pagination button {
  padding: 8px 16px;
  border: 1px solid #e0e0e0;
  background-color: #fff;
  border-radius: 4px;
  cursor: pointer;
}

.pagination button:disabled {
  background-color: #f5f7fa;
  cursor: not-allowed;
}

.loading {
  text-align: center;
  padding: 20px;
  color: #999;
}

.error-text {
  text-align: center;
  color: #E53935;
  padding: 10px;
}

/* 医保服务样式 */
.insurance-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.section-title {
  font-size: 18px;
  font-weight: 700;
  color: #333;
  margin-bottom: 16px;
  padding-bottom: 8px;
  border-bottom: 2px solid #1E88E5;
}

/* 计算器部分 */
.calculator-section {
  background-color: #fff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.calculator-form {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-group label {
  font-size: 14px;
  font-weight: 600;
  color: #666;
}

.form-group input {
  padding: 12px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  font-size: 16px;
  outline: none;
  transition: border-color 0.2s;
}

.form-group input:focus {
  border-color: #1E88E5;
}

.type-selector {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.type-option {
  padding: 10px 20px;
  border: 1px solid #e0e0e0;
  border-radius: 20px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.2s;
}

.type-option.selected {
  background-color: #1E88E5;
  color: #fff;
  border-color: #1E88E5;
}

.result-box {
  background-color: #f5f7fa;
  border-radius: 8px;
  padding: 16px;
  margin-top: 8px;
}

.result-title {
  font-size: 16px;
  font-weight: 700;
  color: #333;
  margin-bottom: 12px;
}

.result-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
  border-bottom: 1px solid #e0e0e0;
}

.result-item:last-child {
  border-bottom: none;
}

.result-item .label {
  font-size: 14px;
  color: #666;
}

.result-item .value {
  font-size: 16px;
  font-weight: 700;
}

.result-item .value.highlight {
  color: #1E88E5;
}

.result-item .value.success {
  color: #4CAF50;
}

.result-item .value.warning {
  color: #F44336;
}

.result-note {
  margin-top: 12px;
  padding: 8px 12px;
  background-color: #FFF3E0;
  color: #F57C00;
  border-radius: 6px;
  font-size: 13px;
}

/* 政策解读部分 */
.policy-section {
  background-color: #fff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.policy-card {
  margin-bottom: 16px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  overflow: hidden;
}

.policy-card:last-child {
  margin-bottom: 0;
}

.card-header {
  background-color: #1E88E5;
  color: #fff;
  padding: 12px 16px;
  font-size: 16px;
  font-weight: 600;
}

.card-body {
  padding: 16px;
}

.ratio-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #f5f7fa;
}

.ratio-item:last-child {
  border-bottom: none;
}

.ratio-type {
  font-size: 14px;
  color: #666;
}

.ratio-value {
  font-size: 18px;
  font-weight: 700;
  color: #1E88E5;
}

.limit-value {
  font-size: 28px;
  font-weight: 700;
  color: #E53935;
  text-align: center;
}

.process-step {
  display: flex;
  gap: 12px;
  padding: 12px 0;
  border-bottom: 1px solid #f5f7fa;
}

.process-step:last-child {
  border-bottom: none;
}

.step-number {
  width: 32px;
  height: 32px;
  background-color: #1E88E5;
  color: #fff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  flex-shrink: 0;
}

.step-content {
  flex: 1;
}

.step-title {
  font-size: 14px;
  font-weight: 600;
  color: #333;
  margin-bottom: 4px;
}

.step-desc {
  font-size: 13px;
  color: #666;
}

/* 常见问题 */
.faq-section {
  background-color: #fff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.faq-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.faq-item {
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  overflow: hidden;
}

.faq-question {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 600;
  color: #333;
  transition: background-color 0.2s;
}

.faq-question:hover {
  background-color: #f5f7fa;
}

.arrow {
  font-size: 12px;
  color: #999;
}

.faq-answer {
  padding: 0 16px 16px;
  font-size: 13px;
  color: #666;
  line-height: 1.6;
}
</style>
