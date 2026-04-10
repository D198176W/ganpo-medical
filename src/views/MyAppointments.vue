<template>
  <div class="appointment-page">
    <div class="container">
      <!-- 页面头部 -->
      <div class="header">
        <div class="back-btn" @click="goBack">
          <i class="fas fa-arrow-left"></i>
        </div>
        <h1 class="page-title">我的挂号</h1>
      </div>
      
      <!-- 筛选区域 -->
      <div class="filter-section">
        <h3 class="filter-title">筛选状态</h3>
        <div class="filter-options">
          <button 
            class="filter-btn" 
            :class="{ active: currentFilter === 'all' }"
            @click="setFilter('all')"
          >全部</button>
          <button 
            class="filter-btn" 
            :class="{ active: currentFilter === 'pending' }"
            @click="setFilter('pending')"
          >待就诊</button>
          <button 
            class="filter-btn" 
            :class="{ active: currentFilter === 'completed' }"
            @click="setFilter('completed')"
          >已就诊</button>
          <button 
            class="filter-btn" 
            :class="{ active: currentFilter === 'cancelled' }"
            @click="setFilter('cancelled')"
          >已取消</button>
        </div>
      </div>
      
      <!-- 挂号列表 -->
      <div id="appointmentList">
        <div 
          v-for="appointment in filteredAppointments" 
          :key="appointment.id" 
          class="appointment-card"
        >
          <div class="card-header">
            <div class="doctor-info">
              <div class="doctor-avatar">{{ appointment.avatar }}</div>
              <div class="doctor-details">
                <h3>{{ appointment.doctorName }}</h3>
                <p>{{ appointment.department }}</p>
              </div>
            </div>
            <div class="status-badge" :class="getStatusClass(appointment.status)">
              {{ getStatusText(appointment.status) }}
            </div>
          </div>
          <div class="appointment-details">
            <div class="detail-item">
              <div class="detail-icon">
                <i class="far fa-calendar"></i>
              </div>
              <div class="detail-text">
                就诊时间
                <strong>{{ appointment.date }}</strong>
              </div>
            </div>
            <div class="detail-item">
              <div class="detail-icon">
                <i class="fas fa-map-marker-alt"></i>
              </div>
              <div class="detail-text">
                就诊地点
                <strong>{{ appointment.location }}</strong>
              </div>
            </div>
          </div>
          <div class="card-actions">
            <button class="action-btn btn-outline" @click="viewDetails(appointment)">查看详情</button>
            <button 
              v-if="appointment.status === 'pending'" 
              class="action-btn btn-danger" 
              @click="showCancelModal(appointment)"
            >取消挂号</button>
            <button 
              v-else-if="appointment.status === 'completed'" 
              class="action-btn btn-primary" 
              @click="rebook(appointment)"
            >重新挂号</button>
          </div>
        </div>
      </div>
      
      <!-- 空状态 -->
      <div v-if="filteredAppointments.length === 0" class="empty-state">
        <div class="empty-icon">
          <i class="far fa-calendar-times"></i>
        </div>
        <h3 class="empty-text">暂无挂号记录</h3>
        <a href="#" class="empty-btn">立即预约</a>
      </div>
    </div>
    
    <!-- 取消挂号确认模态框 -->
    <Modal 
      :show="showCancelModalFlag" 
      @close="closeCancelModal"
      title="取消挂号"
    >
      <div class="modal-body">
        <p>确定要取消<span class="cancel-doctor-name">{{ currentAppointment?.doctorName }}</span>的挂号吗？</p>
        <p class="cancel-warning">
          <i class="fas fa-exclamation-circle"></i> 取消后如需就诊需要重新预约
        </p>
      </div>
      <template #footer>
        <button class="action-btn btn-outline" @click="closeCancelModal">再想想</button>
        <button class="action-btn btn-danger" @click="confirmCancel">确定取消</button>
      </template>
    </Modal>
    
    <!-- 挂号详情模态框 -->
    <Modal 
      :show="showDetailModal" 
      @close="closeDetailModal"
      title="挂号详情"
    >
      <div class="modal-body">
        <div class="appointment-card">
          <div class="card-header">
            <div class="doctor-info">
              <div class="doctor-avatar">{{ currentAppointment?.avatar }}</div>
              <div class="doctor-details">
                <h3>{{ currentAppointment?.doctorName }}</h3>
                <p>{{ currentAppointment?.department }}</p>
              </div>
            </div>
            <div class="status-badge" :class="getStatusClass(currentAppointment?.status)">
              {{ getStatusText(currentAppointment?.status) }}
            </div>
          </div>
          <div class="appointment-details">
            <div class="detail-item">
              <div class="detail-icon">
                <i class="far fa-calendar"></i>
              </div>
              <div class="detail-text">
                就诊时间
                <strong>{{ currentAppointment?.date }}</strong>
              </div>
            </div>
            <div class="detail-item">
              <div class="detail-icon">
                <i class="fas fa-map-marker-alt"></i>
              </div>
              <div class="detail-text">
                就诊地点
                <strong>{{ currentAppointment?.location }}</strong>
              </div>
            </div>
            <div class="detail-item">
              <div class="detail-icon">
                <i class="fas fa-user-md"></i>
              </div>
              <div class="detail-text">
                挂号费用
                <strong>{{ currentAppointment?.fee }}</strong>
              </div>
            </div>
            <div class="detail-item">
              <div class="detail-icon">
                <i class="fas fa-file-medical"></i>
              </div>
              <div class="detail-text">
                挂号单号
                <strong>{{ currentAppointment?.orderNo }}</strong>
              </div>
            </div>
          </div>
        </div>
      </div>
      <template #footer>
        <button class="action-btn btn-primary" @click="closeDetailModal">确定</button>
      </template>
    </Modal>
  </div>
</template>

<script>
import { ref, computed } from 'vue'
import Modal from '@/components/common/Modal.vue'

export default {
  name: 'MyAppointments',
  components: {
    Modal
  },
  setup() {
    // 模拟挂号数据
    const appointments = ref([
      {
        id: 1,
        doctorName: "张医生",
        department: "消化内科",
        avatar: "张",
        date: "2023-11-20 09:30",
        location: "门诊楼3层",
        fee: "¥25",
        orderNo: "GH20231120001",
        status: "pending"
      },
      {
        id: 2,
        doctorName: "王医生",
        department: "眼科",
        avatar: "王",
        date: "2023-11-15 14:00",
        location: "门诊楼2层",
        fee: "¥20",
        orderNo: "GH20231115002",
        status: "completed"
      },
      {
        id: 3,
        doctorName: "李医生",
        department: "皮肤科",
        avatar: "李",
        date: "2023-11-10 10:15",
        location: "门诊楼4层",
        fee: "¥30",
        orderNo: "GH20231110003",
        status: "completed"
      },
      {
        id: 4,
        doctorName: "赵医生",
        department: "骨科",
        avatar: "赵",
        date: "2023-11-25 15:30",
        location: "门诊楼1层",
        fee: "¥35",
        orderNo: "GH20231125004",
        status: "cancelled"
      }
    ])

    const currentFilter = ref('all')
    const showCancelModalFlag = ref(false)
    const showDetailModal = ref(false)
    const currentAppointment = ref(null)

    // 计算属性：筛选后的挂号列表
    const filteredAppointments = computed(() => {
      if (currentFilter.value === 'all') {
        return appointments.value
      }
      return appointments.value.filter(app => app.status === currentFilter.value)
    })

    // 设置筛选条件
    const setFilter = (filter) => {
      currentFilter.value = filter
    }

    // 获取状态文本
    const getStatusText = (status) => {
      const statusMap = {
        'pending': '待就诊',
        'completed': '已就诊',
        'cancelled': '已取消'
      }
      return statusMap[status] || ''
    }

    // 获取状态类名
    const getStatusClass = (status) => {
      const classMap = {
        'pending': 'status-pending',
        'completed': 'status-completed',
        'cancelled': 'status-cancelled'
      }
      return classMap[status] || ''
    }

    // 查看详情
    const viewDetails = (appointment) => {
      currentAppointment.value = appointment
      showDetailModal.value = true
    }

    // 显示取消挂号模态框
    const showCancelModal = (appointment) => {
      currentAppointment.value = appointment
      showCancelModalFlag.value = true
    }

    // 关闭取消挂号模态框
    const closeCancelModal = () => {
      showCancelModalFlag.value = false
      currentAppointment.value = null
    }

    // 确认取消挂号
    const confirmCancel = () => {
      if (currentAppointment.value) {
        const appointmentIndex = appointments.value.findIndex(app => app.id === currentAppointment.value.id)
        if (appointmentIndex !== -1) {
          appointments.value[appointmentIndex].status = 'cancelled'
          closeCancelModal()
          // 这里可以添加提示消息
        }
      }
    }

    // 关闭详情模态框
    const closeDetailModal = () => {
      showDetailModal.value = false
      currentAppointment.value = null
    }

    // 重新挂号
    const rebook = (appointment) => {
      // 在实际应用中，这里应该跳转到挂号页面
      console.log('重新挂号:', appointment)
    }

    // 返回上一页
    const goBack = () => {
      window.history.back()
    }

    return {
      appointments,
      currentFilter,
      showCancelModalFlag,
      showDetailModal,
      currentAppointment,
      filteredAppointments,
      setFilter,
      getStatusText,
      getStatusClass,
      viewDetails,
      showCancelModal,
      closeCancelModal,
      confirmCancel,
      closeDetailModal,
      rebook,
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

.back-btn {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  background: var(--primary);
  color: white;
  margin-right: 15px;
  cursor: pointer;
  transition: all 0.3s;
}

.back-btn:hover {
  background: var(--primary-dark);
  transform: translateX(-3px);
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

/* 挂号卡片 */
.appointment-card {
  background: var(--card);
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 15px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  transition: all 0.3s;
}

.appointment-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 15px;
}

.doctor-info {
  display: flex;
  align-items: center;
}

.doctor-avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--primary), var(--secondary));
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: bold;
  margin-right: 12px;
}

.doctor-details h3 {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 5px;
}

.doctor-details p {
  font-size: 14px;
  color: var(--gray);
}

.status-badge {
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
}

.status-pending {
  background: #FFF3CD;
  color: #856404;
}

.status-completed {
  background: #D1ECF1;
  color: #0C5460;
}

.status-cancelled {
  background: #F8D7DA;
  color: #721C24;
}

.appointment-details {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 15px;
  margin-bottom: 15px;
}

.detail-item {
  display: flex;
  align-items: center;
}

.detail-icon {
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

.detail-text {
  font-size: 14px;
  color: var(--gray);
}

.detail-text strong {
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

.btn-danger {
  background: var(--danger);
  color: white;
}

.btn-danger:hover {
  background: #c62828;
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

/* 模态框内容样式 */
.cancel-warning {
  margin-top: 10px;
  color: var(--danger);
  font-size: 14px;
}

.cancel-doctor-name {
  font-weight: bold;
  color: var(--primary);
  margin: 0 4px;
}

/* 响应式设计 */
@media (max-width: 600px) {
  .container {
    padding: 15px;
  }
  
  .appointment-details {
    grid-template-columns: 1fr;
  }
  
  .card-actions {
    flex-direction: column;
  }
  
  .action-btn {
    width: 100%;
  }
}
</style>