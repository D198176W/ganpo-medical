<template>
  <div class="health-record">
    <!-- 页面头部 -->
    <div class="header">
      <div class="header-left">
        <div class="back-btn" @click="goBack">
          <i class="fas fa-arrow-left"></i>
        </div>
        <h1 class="page-title">健康档案</h1>
      </div>
      <button class="edit-btn" @click="editProfile">
        <i class="fas fa-edit"></i> 编辑档案
      </button>
    </div>
    
    <!-- 个人信息卡片 -->
    <ProfileCard 
      :profile="profileData"
      @edit="editProfile"
    />
    
    <!-- 健康指标卡片 -->
    <HealthMetricsCard 
      :metrics="healthMetrics"
      @add-record="addHealthRecord"
      @view-trend="viewHealthHistory"
    />
    
    <!-- 过敏史卡片 -->
    <InfoCard 
      title="过敏史"
      :content="allergyInfo.content"
      :tags="allergyInfo.tags"
      @edit="editAllergy"
    />
    
    <!-- 既往病史卡片 -->
    <InfoCard 
      title="既往病史"
      :content="medicalHistory.content"
      :tags="medicalHistory.tags"
      @edit="editMedicalHistory"
    />
    
    <!-- 家族病史卡片 -->
    <InfoCard 
      title="家族病史"
      :content="familyHistory.content"
      :tags="familyHistory.tags"
      @edit="editFamilyHistory"
    />
    
    <!-- 最近医疗记录 -->
    <MedicalRecordsCard 
      :records="recentRecords"
      @view-all="viewAllRecords"
    />
    
    <!-- 编辑档案模态框 -->
    <EditProfileModal 
      :show="showEditModal"
      :profile="profileData"
      @close="showEditModal = false"
      @save="saveProfile"
    />
  </div>
</template>

<script>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import ProfileCard from '@/components/health-record/ProfileCard.vue'
import HealthMetricsCard from '@/components/health-record/HealthMetricsCard.vue'
import InfoCard from '@/components/health-record/InfoCard.vue'
import MedicalRecordsCard from '@/components/health-record/MedicalRecordsCard.vue'
import EditProfileModal from '@/components/health-record/EditProfileModal.vue'

export default {
  name: 'HealthRecord',
  components: {
    ProfileCard,
    HealthMetricsCard,
    InfoCard,
    MedicalRecordsCard,
    EditProfileModal
  },
  setup() {
    const router = useRouter()
    
    const showEditModal = ref(false)
    
    // 个人信息数据
    const profileData = reactive({
      name: '李同学',
      school: '江西农业大学软件学院',
      gender: '男',
      age: '21岁',
      birthday: '2002年5月15日',
      phone: '138****1234',
      bloodType: 'O型',
      avatar: '李'
    })
    
    // 健康指标数据
    const healthMetrics = ref([
      { value: '175cm', label: '身高', status: 'normal', statusText: '正常' },
      { value: '68kg', label: '体重', status: 'normal', statusText: '正常' },
      { value: '22.2', label: 'BMI', status: 'normal', statusText: '正常' },
      { value: '120/80', label: '血压(mmHg)', status: 'normal', statusText: '正常' },
      { value: '72', label: '心率(bpm)', status: 'normal', statusText: '正常' },
      { value: '5.2', label: '血糖(mmol/L)', status: 'warning', statusText: '偏高' }
    ])
    
    // 过敏史数据
    const allergyInfo = reactive({
      content: '无药物过敏史，有轻微花粉过敏（春季易发）',
      tags: ['花粉过敏', '春季易发']
    })
    
    // 既往病史数据
    const medicalHistory = reactive({
      content: '2021年因急性阑尾炎行阑尾切除术，术后恢复良好。无其他重大疾病史。',
      tags: ['阑尾切除术', '2021年']
    })
    
    // 家族病史数据
    const familyHistory = reactive({
      content: '父亲有高血压病史，母亲有轻度糖尿病史。无其他家族遗传病史。',
      tags: ['高血压', '糖尿病']
    })
    
    // 最近医疗记录
    const recentRecords = ref([
      {
        id: 1,
        title: '血常规检查',
        description: '检查结果基本正常，血小板计数略高',
        date: '2023-11-15'
      },
      {
        id: 2,
        title: '眼科检查',
        description: '视力正常，眼底检查无异常',
        date: '2023-11-10'
      },
      {
        id: 3,
        title: '年度体检',
        description: '全面体检，各项指标基本正常',
        date: '2023-10-28'
      }
    ])
    
    // 方法
    const goBack = () => {
      router.back()
    }
    
    const editProfile = () => {
      showEditModal.value = true
    }
    
    const saveProfile = (updatedProfile) => {
      Object.assign(profileData, updatedProfile)
      showEditModal.value = false
      // 在实际应用中，这里应该发送请求到服务器保存数据
    }
    
    const editAllergy = () => {
      // 在实际应用中，这里应该打开编辑过敏史的模态框
      alert('编辑过敏史功能正在开发中')
    }
    
    const editMedicalHistory = () => {
      // 在实际应用中，这里应该打开编辑既往病史的模态框
      alert('编辑既往病史功能正在开发中')
    }
    
    const editFamilyHistory = () => {
      // 在实际应用中，这里应该打开编辑家族病史的模态框
      alert('编辑家族病史功能正在开发中')
    }
    
    const addHealthRecord = () => {
      // 在实际应用中，这里应该打开添加健康记录的模态框
      alert('添加健康记录功能正在开发中')
    }
    
    const viewHealthHistory = () => {
      // 在实际应用中，这里应该跳转到健康趋势页面
      alert('查看健康趋势功能正在开发中')
    }
    
    const viewAllRecords = () => {
      // 在实际应用中，这里应该跳转到全部医疗记录页面
      alert('查看全部医疗记录功能正在开发中')
    }
    
    return {
      showEditModal,
      profileData,
      healthMetrics,
      allergyInfo,
      medicalHistory,
      familyHistory,
      recentRecords,
      goBack,
      editProfile,
      saveProfile,
      editAllergy,
      editMedicalHistory,
      editFamilyHistory,
      addHealthRecord,
      viewHealthHistory,
      viewAllRecords
    }
  }
}
</script>

<style scoped>
.health-record {
  background-color: var(--light);
  min-height: 100vh;
  padding-bottom: 80px; /* 为底部导航留出空间 */
}

.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
  padding: 20px 20px 15px;
  border-bottom: 1px solid var(--border);
  background: white;
}

.header-left {
  display: flex;
  align-items: center;
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

.edit-btn {
  padding: 8px 16px;
  border-radius: 6px;
  background: var(--primary);
  color: white;
  border: none;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  gap: 5px;
}

.edit-btn:hover {
  background: var(--primary-dark);
}
</style>