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
            @click="selectDepartment(dept.id)"
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
                <span class="hospital-distance">{{ hospital.distance.toFixed(1) }}km</span>
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
      <!-- 医保报销计算器 -->
      <div class="insurance-calculator">
        <span class="calculator-title">大学生医保报销计算器</span>
        <div class="calculator-input">
          <span>医疗总费用：</span>
          <input 
            type="number" 
            placeholder="请输入金额（元）" 
            v-model="medicalCost"
          />
        </div>
        <div class="expense-type">
          <span>费用类型：</span>
          <select class="expense-type-select" v-model="expenseTypeIndex">
            <option value="0">门诊费用</option>
            <option value="1">住院费用</option>
            <option value="2">检查费用</option>
            <option value="3">药品费用</option>
          </select>
        </div>
        <button 
          class="calculate-btn" 
          @click="calculateReimbursement"
          :disabled="loadingReimbursement"
        >
          <span v-if="loadingReimbursement" class="loading small"></span>
          {{ loadingReimbursement ? '计算中...' : '计算报销金额' }}
        </button>
        
        <div class="calculation-result" :class="{ show: showResult }">
          <span class="result-title">报销结果：</span>
          <div class="result-details">
            <span>医疗总费用：{{ parseFloat(medicalCost || 0).toLocaleString() }}元</span>
            <span>报销比例：{{ reimbursementRate }}%</span>
            <span class="result-amount">可报销金额：{{ parseFloat(reimbursementAmount || 0).toLocaleString() }}元</span>
          </div>
        </div>
        
        <div class="rule-info">
          <strong>报销规则说明：</strong><br>
          具体报销比例以医保政策为准，本计算结果仅供参考
        </div>
      </div>

      <!-- 定点药店地图 -->
      <div class="pharmacy-map">
        <span class="map-title">定点药店地图</span>
        <div class="map-container">
          <div 
            class="map-placeholder" 
            @click="loadPharmacyMap" 
            :style="mapStyle"
          >
            <div v-if="!mapLoaded && !loadingPharmacies">点击加载地图</div>
            <div v-else>
              <div class="loading" v-if="loadingPharmacies"></div>
              <div>{{ loadingPharmacies ? '地图加载中...' : '南昌市定点药店分布图' }}</div>
              <div class="map-desc" v-if="mapLoaded && !loadingPharmacies">红色标记为定点药店位置</div>
            </div>
            <div class="reload-text">重新加载</div>
            
            <!-- 药店标记 -->
            <div 
              v-for="(pharmacy, index) in nearbyPharmacies" 
              :key="pharmacy.id"
              class="map-marker"
              :style="getMarkerStyle(index)"
              @click.stop="showPharmacyInfo(pharmacy)"
            ></div>
          </div>
          
          <!-- 药店列表 -->
          <div class="pharmacy-list" v-if="mapLoaded && !loadingPharmacies">
            <div 
              v-for="pharmacy in nearbyPharmacies" 
              :key="pharmacy.id"
              class="pharmacy-item"
              @click="showPharmacyInfo(pharmacy)"
            >
              <span class="pharmacy-name">{{ pharmacy.name }}</span>
              <span class="pharmacy-address">{{ pharmacy.address }}</span>
              <span class="pharmacy-contact">{{ pharmacy.phone }}</span>
              <span class="pharmacy-distance">距离 {{ pharmacy.distance.toFixed(1) }}km</span>
              <button class="navigate-btn" @click.stop="navigateToPharmacy(pharmacy)">
                导航
              </button>
            </div>
          </div>
          
          <div class="error-text" v-if="errorPharmacies">
            获取药店信息失败，请重试
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
      // 响应式数据
      activeTab: 0, // 0: 医院查询, 1: 医保服务
      selectedDepartment: 'all',
      sortType: 'distance',
      medicalCost: '',
      expenseTypeIndex: 0,
      showResult: false,
      reimbursementRate: 0,
      reimbursementAmount: 0,
      mapLoaded: false,
      loadingMap: false,

      // 分页相关
      currentPage: 1,
      pageSize: 10,
      totalHospitals: 0,
      
      // 数据容器
      departments: [],
      hospitals: [],
      nearbyPharmacies: [],
      
      // 加载状态
      loadingDepartments: false,
      loadingHospitals: false,
      loadingPharmacies: false,
      loadingReimbursement: false,
      
      // 错误状态
      errorDepartments: false,
      errorPharmacies: false,

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
    // 地图样式
    mapStyle() {
      if (this.mapLoaded && !this.loadingPharmacies) {
        return {
          backgroundImage: 'linear-gradient(135deg, #a8edea 0%, #fed6e3 100%)',
          backgroundSize: 'cover',
          color: '#333',
          justifyContent: 'flex-start',
          padding: '20px',
          alignItems: 'flex-start',
          cursor: 'default'
        }
      }
      return {}
    }
  },
  methods: {
    switchTab(tab) {
      this.activeTab = tab
    },
    selectDepartment(departmentId) {
      this.selectedDepartment = departmentId
      this.currentPage = 1 // 切换科室重置页码
    },
    setSortType(type) {
      this.sortType = type
      this.currentPage = 1 // 切换排序重置页码
    },
    handlePageChange(page) {
      if (page < 1) return
      this.currentPage = page
    },
    async viewHospitalDetail(hospitalId) {
      try {
        this.loadingHospitals = true
        const res = await request.get(`/hospitals/${hospitalId}`)
        if (res.data.code === 0) {
          const detail = res.data.data
          // 这里可以跳转到详情页或弹窗展示
          window.open(detail.detailUrl, '_blank')
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
        const res = await request.get(`/hospitals/${hospitalId}`)
        if (res.data.code === 0) {
          const detail = res.data.data
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
    async calculateReimbursement() {
      const cost = parseFloat(this.medicalCost)
      
      if (isNaN(cost) || cost <= 0) {
        alert('请输入有效的医疗费用')
        return
      }
      
      try {
        this.loadingReimbursement = true
        const res = request.axios.post('/reimbursement/calculate', {
          userType: 'student',
          totalAmount: cost,
          breakdown: [{ 
            type: this.getExpenseType(), 
            amount: cost 
          }]
        })
        
        if (res.data.code === 0) {
          this.showResult = true
          this.reimbursementRate = res.data.data.details[0].ratio
          this.reimbursementAmount = res.data.data.reimbursableAmount
        }
      } catch (err) {
        console.error('计算报销金额失败', err)
        alert('计算失败，请重试')
      } finally {
        this.loadingReimbursement = false
      }
    },
    getExpenseType() {
      const types = ['outpatient', 'inpatient', 'exam', 'drug']
      return types[this.expenseTypeIndex]
    },
    async loadPharmacyMap() {
      this.errorPharmacies = false
      this.loadingPharmacies = true
      
      try {
        // 1. 获取用户位置（经纬度）
        const position = await new Promise((resolve, reject) => {
          navigator.geolocation.getCurrentPosition(resolve, reject)
        })
        
        const { latitude: lat, longitude: lng } = position.coords
        
        // 2. 调用药店接口
        const res = await request.get('/pharmacies', {
          params: { page: 1, size: 10, lat, lng, radius: 5 } // 5公里内
        })
        
        if (res.data.code === 0) {
          this.nearbyPharmacies = res.data.data.records
          this.mapLoaded = true
        }
      } catch (err) {
        console.error('获取药店信息失败', err)
        this.errorPharmacies = true
        if (err.code === 1) {
          alert('请允许获取地理位置权限，否则无法获取附近药店')
        }
      } finally {
        this.loadingPharmacies = false
      }
    },
    getMarkerStyle(index) {
      // 实际项目中应根据经纬度计算位置，这里仅做示例
      const left = 20 + (index * 25)
      const top = 50 + (index % 2 * 30)
      return {
        left: `${left}%`,
        top: `${top}%`
      }
    },
    showPharmacyInfo(pharmacy) {
      alert(`${pharmacy.name}\n地址：${pharmacy.address}\n电话：${pharmacy.phone}`)
    },
    navigateToPharmacy(pharmacy) {
      const confirmed = confirm(`是否要导航到：${pharmacy.name}？\n地址：${pharmacy.address}`)
      if (confirmed) {
        // 实际应用中使用地图API导航
        alert(`正在启动导航...\n目的地：${pharmacy.name}`)
        // 示例：window.open(`https://maps.baidu.com/search/${pharmacy.lat},${pharmacy.lng}`)
      }
    },
    async fetchHospitals() {
      this.loadingHospitals = true
      try {
        // 获取用户位置（经纬度）
        let lat, lng
        try {
          const position = await new Promise((resolve) => {
            navigator.geolocation.getCurrentPosition(resolve)
          })
          lat = position.coords.latitude
          lng = position.coords.longitude
        } catch (err) {
          console.warn('无法获取位置，使用默认位置', err)
          lat = 28.683569 // 南昌默认纬度
          lng = 115.858225 // 南昌默认经度
        }

        // 构造查询参数
        const params = {
          page: this.currentPage,
          size: this.pageSize,
          departmentId: this.selectedDepartment === 'all' ? '' : this.selectedDepartment,
          sort: this.sortType,
          lat,
          lng
        }
        
        const res = await request.get('/hospitals', { params })
        if (res.data.code === 0) {
          this.hospitals = res.data.data.records
          this.totalHospitals = res.data.data.total
        }
      } catch (err) {
        console.error('获取医院列表失败', err)
        alert('获取医院信息失败，请重试')
      } finally {
        this.loadingHospitals = false
      }
    }
  },
  watch: {
    // 筛选条件变化时重新查询
    selectedDepartment() { this.fetchHospitals() },
    sortType() { this.fetchHospitals() },
    currentPage() { this.fetchHospitals() }
  },
  async mounted() {
    // 加载科室数据
    try {
      this.loadingDepartments = true
      const res = await request.get('/departments')
      if (res.data.code === 0) {
        //  prepend 'all' option
        this.departments = [{ id: 'all', name: '全部科室' }, ...res.data.data]
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

/* 顶部导航栏 */
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

/* 选项卡样式 */
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
  position: relative;
  cursor: pointer;
  transition: all 0.3s ease;
}

.tab-item.active {
  color: #1E88E5;
  font-weight: 600;
}

.tab-item.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 60%;
  height: 3px;
  background-color: #1E88E5;
  border-radius: 3px 3px 0 0;
}

/* 医院查询模块样式 */
.hospital-module {
  padding: 16px;
  display: none;
}

.hospital-module.active {
  display: block;
}

/* 科室筛选 */
.department-filter {
  margin-bottom: 16px;
  background-color: #fff;
  padding: 16px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.filter-title {
  display: block;
  margin-bottom: 12px;
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.department-scroll {
  white-space: nowrap;
  padding-bottom: 8px;
  overflow-x: auto;
  display: flex;
  padding: 8px 0;
}

.department-scroll::-webkit-scrollbar {
  height: 4px;
}

.department-scroll::-webkit-scrollbar-thumb {
  background: #ccc;
  border-radius: 2px;
}

.department-item {
  display: inline-block;
  padding: 8px 16px;
  margin-right: 8px;
  background-color: #f5f7fa;
  border: 1px solid #e0e0e0;
  border-radius: 20px;
  font-size: 14px;
  color: #555;
  cursor: pointer;
  transition: all 0.2s ease;
  flex-shrink: 0;
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

/* 医院列表（卡片式设计） */
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
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.hospital-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
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
  background-color: #f5f7fa;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #1E88E5, #1565C0);
  color: white;
  font-weight: bold;
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
  line-height: 1.4;
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
  display: flex;
  align-items: center;
}

.hospital-distance::before {
  content: '';
  display: inline-block;
  width: 14px;
  height: 14px;
  background-image: url("data:image/svg+xml;utf8,<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='%23666'><path d='M12 2C8.13 2 5 5.13 5 9c0 5.25 7 13 7 13s7-7.75 7-13c0-3.87-3.13-7-7-7zm0 9.5c-1.38 0-2.5-1.12-2.5-2.5s1.12-2.5 2.5-2.5 2.5 1.12 2.5 2.5-1.12 2.5-2.5 2.5z'/></svg>");
  background-repeat: no-repeat;
  background-size: contain;
  margin-right: 4px;
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
  display: flex;
  align-items: flex-start;
  margin-bottom: 16px;
  font-size: 14px;
  color: #666;
  line-height: 1.4;
}

.hospital-address::before {
  content: '';
  display: inline-block;
  width: 16px;
  height: 16px;
  background-image: url("data:image/svg+xml;utf8,<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='%23666'><path d='M12 2C8.13 2 5 5.13 5 9c0 5.25 7 13 7 13s7-7.75 7-13c0-3.87-3.13-7-7-7zm0 9.5c-1.38 0-2.5-1.12-2.5-2.5s1.12-2.5 2.5-2.5 2.5 1.12 2.5 2.5-1.12 2.5-2.5 2.5z'/></svg>");
  background-repeat: no-repeat;
  background-size: contain;
  margin-right: 8px;
  flex-shrink: 0;
  margin-top: 2px;
}

/* 操作按钮区域 */
.hospital-actions {
  display: flex;
  gap: 12px;
}

.register-btn, .detail-btn {
  flex: 1;
  height: 40px;
  line-height: 40px;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  text-align: center;
}

.register-btn {
  background-color: #E53935;
  color: #fff;
}

.register-btn:hover:not(.disabled) {
  background-color: #c62828;
  box-shadow: 0 2px 6px rgba(229, 57, 53, 0.3);
}

.register-btn.disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.detail-btn {
  background-color: #f5f7fa;
  color: #666;
  border: 1px solid #e0e0e0;
}

.detail-btn:hover {
  background-color: #e8eaed;
  color: #333;
}

/* 医保服务模块样式 */
.insurance-module {
  padding: 16px;
  display: none;
  flex-direction: column;
  gap: 16px;
}

.insurance-module.active {
  display: flex;
}

/* 医保报销计算器 */
.insurance-calculator {
  background-color: #fff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.calculator-title {
  display: block;
  font-size: 18px;
  font-weight: 700;
  color: #333;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid #f0f0f0;
}

.calculator-input, .expense-type {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.calculator-input span, .expense-type span {
  width: 100px;
  font-size: 14px;
  color: #333;
  font-weight: 500;
}

.calculator-input input {
  flex: 1;
  height: 44px;
  padding: 0 16px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 14px;
  transition: border-color 0.2s ease;
}

.calculator-input input:focus {
  border-color: #1E88E5;
  outline: none;
  box-shadow: 0 0 0 2px rgba(30, 136, 229, 0.1);
}

.expense-type-select {
  flex: 1;
  height: 44px;
  line-height: 44px;
  padding: 0 16px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 14px;
  color: #333;
  cursor: pointer;
  position: relative;
  background-color: #fff;
  appearance: none;
  background-image: url("data:image/svg+xml;utf8,<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='%23666'><path d='M7 10l5 5 5-5z'/></svg>");
  background-repeat: no-repeat;
  background-position: right 16px center;
  background-size: 16px;
}

.calculate-btn {
  width: 100%;
  height: 44px;
  line-height: 44px;
  background: linear-gradient(135deg, #1E88E5, #1565C0);
  color: #fff;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 20px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.calculate-btn:hover {
  box-shadow: 0 4px 12px rgba(30, 136, 229, 0.3);
  transform: translateY(-1px);
}

.calculation-result {
  padding: 16px;
  background: linear-gradient(135deg, #f5f7fa, #e8eaed);
  border-radius: 8px;
  display: none;
  border-left: 4px solid #1E88E5;
}

.calculation-result.show {
  display: block;
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

.result-title {
  display: block;
  font-size: 16px;
  font-weight: 700;
  color: #333;
  margin-bottom: 12px;
}

.result-details {
  display: flex;
  flex-direction: column;
  gap: 8px;
  font-size: 14px;
}

.result-amount {
  color: #E53935;
  font-weight: 700;
  font-size: 18px;
  margin-top: 4px;
}

.rule-info {
  margin-top: 16px;
  padding: 12px;
  background-color: #e3f2fd;
  border-radius: 6px;
  font-size: 12px;
  color: #1565C0;
  line-height: 1.5;
}

/* 定点药店地图 */
.pharmacy-map {
  background-color: #fff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.map-title {
  display: block;
  font-size: 18px;
  font-weight: 700;
  color: #333;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid #f0f0f0;
}

.map-container {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

/* 地图组件样式 */
.map-placeholder {
  width: 100%;
  height: 200px;
  border-radius: 8px;
  background-color: #e8eaed;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #666;
  position: relative;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.2s ease;
}

.map-placeholder:hover {
  background-color: #e0e0e0;
}

.map-placeholder .reload-text {
  margin-top: 8px;
  font-size: 14px;
  color: #1E88E5;
}

.map-desc {
  font-size: 12px;
  color: #666;
  margin-top: 5px;
}

.map-marker {
  position: absolute;
  width: 24px;
  height: 24px;
  background-color: #E53935;
  border-radius: 50% 50% 50% 0;
  transform: rotate(-45deg);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
  cursor: pointer;
}

.map-marker::after {
  content: '';
  position: absolute;
  width: 12px;
  height: 12px;
  background-color: #fff;
  border-radius: 50%;
  top: 6px;
  left: 6px;
}

/* 药店列表 */
.pharmacy-list {
  max-height: 300px;
  overflow-y: auto;
  border-radius: 8px;
  border: 1px solid #f0f0f0;
}

.pharmacy-item {
  padding: 16px;
  border-bottom: 1px solid #f0f0f0;
  position: relative;
  transition: background-color 0.2s ease;
  cursor: pointer;
}

.pharmacy-item:hover {
  background-color: #f9f9f9;
}

.pharmacy-item:last-child {
  border-bottom: none;
}

.pharmacy-name {
  display: block;
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 6px;
}

.pharmacy-address, .pharmacy-contact {
  display: block;
  font-size: 14px;
  color: #666;
  margin-bottom: 4px;
  line-height: 1.4;
}

.pharmacy-distance {
  display: inline-block;
  font-size: 12px;
  color: #1E88E5;
  background-color: rgba(30, 136, 229, 0.1);
  padding: 2px 8px;
  border-radius: 10px;
  margin-top: 4px;
}

.navigate-btn {
  position: absolute;
  top: 50%;
  right: 16px;
  transform: translateY(-50%);
  height: 36px;
  line-height: 36px;
  padding: 0 16px;
  background-color: #1E88E5;
  color: #fff;
  border: none;
  border-radius: 18px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
}

.navigate-btn:hover {
  background-color: #1565C0;
  box-shadow: 0 2px 6px rgba(30, 136, 229, 0.3);
}

/* 无数据提示 */
.no-data {
  background-color: #fff;
  border-radius: 12px;
  padding: 40px 20px;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.no-data-text {
  font-size: 16px;
  color: #666;
  margin-bottom: 8px;
}

.no-data-desc {
  font-size: 14px;
  color: #999;
}

/* 加载动画 */
.loading {
  display: inline-block;
  width: 20px;
  height: 20px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  border-top-color: #fff;
  animation: spin 1s ease-in-out infinite;
  margin-right: 8px;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

/* 原有样式保持不变，新增以下样式 */
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 16px;
  padding: 20px 0;
  font-size: 14px;
}

.pagination button {
  padding: 6px 12px;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  background-color: #fff;
  cursor: pointer;
  transition: all 0.2s ease;
}

.pagination button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.pagination button:hover:not(:disabled) {
  background-color: #f5f7fa;
  border-color: #1E88E5;
  color: #1E88E5;
}

.error-text {
  color: #F44336;
  font-size: 14px;
  text-align: center;
  padding: 16px;
}

.loading.small {
  width: 16px;
  height: 16px;
  margin-right: 8px;
  vertical-align: middle;
}


</style>