<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import NavBar from '@/components/common/NavBar.vue'
import RecommendCard from '@/components/home/RecommendCard.vue'

// 1. 静态导入所有图标（关键修复）
import icon1 from '@/assets/images/icon1.png'
import icon2 from '@/assets/images/icon2.png'
import icon3 from '@/assets/images/icon3.png'
import voiceIcon from '@/assets/images/voice-icon.png'
import lockIcon from '@/assets/images/lock-icon.png'
import insuranceIcon from '@/assets/images/insurance-icon.png'
import hospitalIcon from '@/assets/images/hospital-icon.png'
import arrowRight from '@/assets/images/arrow-right.png'
import weatherIcon from '@/assets/images/weather-icon.png'
import vaccineIcon from '@/assets/images/vaccine-icon.png'
import nanchangBg from '@/assets/images/nanchang-bg.png'
// 新增图标导入
import illnessIcon from '@/assets/images/icon1.png'
import emergencyIcon from '@/assets/images/emergency-icon.png'
// 为校园常见病和应急指引添加项目图标
import coldIcon from '@/assets/images/cold-icon.png'
import stomachacheIcon from '@/assets/images/stomachache-icon.png'
import fatigueIcon from '@/assets/images/fatigue-icon.png'
import firstaidIcon from '@/assets/images/first-aid-icon.png'
import fireIcon from '@/assets/images/fire-icon.png'

const router = useRouter()

// 2. 使用导入的图标变量替换字符串路径
const navItems = ref([
  {
    id: 1,
    text: '智能问诊',
    route: '/consult',
    icon: icon1, // 使用导入的变量
    badge: { class: 'voice-badge', icon: voiceIcon } // 使用导入的变量
  },
  {
    id: 2,
    text: '健康管理',
    route: '/health',
    icon: icon2,
    badge: { class: 'tag-badge', text: '5折' }
  },
  {
    id: 3,
    text: '病理问答',
    route: '/pathology',
    icon: icon3,
    badge: { class: 'lock-badge', icon: lockIcon } // 使用导入的变量
  }
])

const localServices = ref([
  {
    id: 1,
    title: '大学生医保服务',
    desc: '医保查询、报销指南、参保登记',
    route: '/medical-resources',
    icon: insuranceIcon, // 使用导入的变量
    isInsurance: true
  },
  {
    id: 2,
    title: '南昌医院挂号',
    desc: '省人民医院、大学一附院等',
    route: '/medical-resources',
    icon: hospitalIcon // 使用导入的变量
  }
])

const recommendCards = ref([
  {
    id: 1,
    title: '健康提醒',
    route: '/health-reminder',
    type: 'tips',
    items: [
      { icon: weatherIcon, text: '明日降温，注意添衣保暖，预防感冒' },
      { icon: vaccineIcon, text: '流感季节来临，建议及时接种流感疫苗' }
    ]
  },
  // 修改：校园常见病卡片 - 使用与健康提醒相同的样式
  {
    id: 2,
    title: '校园常见病',
    route: '/common-illnesses',
    type: 'illness',
    items: [
      { icon: coldIcon, text: '普通感冒：发热、咳嗽、流鼻涕等症状处理' },
      { icon: stomachacheIcon, text: '急性胃炎：腹痛、恶心、呕吐等应对措施' },
      { icon: fatigueIcon, text: '神经衰弱：失眠、焦虑、注意力不集中调理' }
    ],
    icon: illnessIcon
  },
  // 修改：创业园区应急指引卡片 - 使用与健康提醒相同的样式
  {
    id: 3,
    title: '创业园区应急指引',
    route: '/emergency-guidelines',
    type: 'emergency',
    items: [
      { icon: firstaidIcon, text: '园区医务室位置及24小时急救电话' },
      { icon: fireIcon, text: '消防通道分布及应急疏散路线' }
    ],
    icon: emergencyIcon
  }
])

const navigateTo = (route) => {
  router.push(route)
}

</script>

<template>
  <div class="index-container">
    <!-- 背景图片使用导入的变量 -->
    <div class="bg-container">
      <img :src="nanchangBg" alt="南昌背景" class="bg-img"> 
    </div>

    <!-- 顶部导航栏 -->
    <NavBar title="首页" :show-user="true" />

    <!-- 功能导航区 -->
    <div class="function-nav">
      <span class="nav-label">常用服务</span>
      <div class="nav-grid">
        <div class="nav-item" v-for="item in navItems" :key="item.id" @click="navigateTo(item.route)">
          <div class="icon-wrapper">
            <!-- 使用绑定:src正确 -->
            <img :src="item.icon" :alt="item.text">
            <div v-if="item.badge" class="badge" :class="item.badge.class">
              <!-- 图标使用绑定:src正确 -->
              <img v-if="item.badge.icon" :src="item.badge.icon" :alt="item.badge.text">
              <span v-else>{{ item.badge.text }}</span>
            </div>
          </div>
          <span class="nav-text">{{ item.text }}</span>
        </div>
      </div>
    </div>

    <!-- 本地化服务区 -->
    <div class="local-service">
      <span class="section-title">南昌本地医疗资源</span>
      <div class="service-cards">
        <div v-for="service in localServices" :key="service.id" class="service-card" @click="navigateTo(service.route)">
          <div class="card-content">
            <div class="card-icon" :class="{ 'insurance-icon': service.isInsurance }">
              <img :src="service.icon" :alt="service.title">
            </div>
            <div class="card-info">
              <span class="card-title">{{ service.title }}</span>
              <span class="card-desc">{{ service.desc }}</span>
            </div>
            <div class="card-arrow">
              <!-- 使用导入的变量 -->
              <img :src="arrowRight" alt="右箭头">
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 个性化推荐区 -->
    <div class="recommendation">
      <span class="section-title">为你推荐</span>
      <div class="recommend-content">
        <RecommendCard v-for="card in recommendCards" :key="card.id" :card="card" />
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 保持原有的CSS样式 */
.index-container {
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

/* 其他原有样式保持不变 */
.function-nav {
  padding: 20px 20px 30px;
}

.nav-label {
  display: block;
  font-size: 14px;
  color: #757575;
  margin-bottom: 12px;
}

.nav-grid {
  display: flex;
  justify-content: space-between;
  gap: 15px;
}

.nav-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

.icon-wrapper {
  width: 72px;
  height: 72px;
  background-color: rgba(255, 255, 255, 0.8);
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 10px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  position: relative;
}

.icon-wrapper img {
  width: 48px;
  height: 48px;
}

.nav-text {
  font-size: 16px;
  color: #333333;
  font-weight: 500;
}

.badge {
  position: absolute;
  right: 6px;
  top: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.voice-badge {
  width: 20px;
  height: 20px;
  background-color: #4CAF50;
  border-radius: 50%;
}

.voice-badge img {
  width: 12px;
  height: 12px;
}

.tag-badge {
  background-color: #E53935;
  border-radius: 10px;
  padding: 0 6px;
  height: 20px;
}

.tag-badge span {
  font-size: 10px;
  color: #FFFFFF;
  line-height: 20px;
}

.lock-badge {
  width: 20px;
  height: 20px;
  background-color: #FFC107;
  border-radius: 50%;
}

.lock-badge img {
  width: 12px;
  height: 12px;
}

.local-service {
  padding: 0 20px 30px;
}

.section-title {
  font-size: 18px;
  font-weight: 700;
  color: #333333;
  margin-bottom: 16px;
  display: block;
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

.service-cards {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.service-card {
  width: 100%;
  height: 100px;
  border-radius: 16px;
  overflow: hidden;
  position: relative;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  cursor: pointer;
}

.card-content {
  position: relative;
  z-index: 2;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  padding: 0 20px;
  box-sizing: border-box;
  background-color: rgba(255, 255, 255, 0.8);
}

.card-icon {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  background-color: #F0F7FF;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
}

.card-icon img {
  width: 32px;
  height: 32px;
}

.insurance-icon {
  background-color: #FFF0F0;
}

.card-info {
  flex: 1;
}

.card-title {
  font-size: 17px;
  font-weight: 600;
  color: #333333;
  margin-bottom: 4px;
  display: block;
}

.card-desc {
  font-size: 13px;
  color: #757575;
}

.card-arrow {
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.card-arrow img {
  width: 16px;
  height: 16px;
  color: #CCCCCC;
}

.recommendation {
  padding: 0 20px;
}

.recommend-content {
  display: flex;
  flex-direction: column;
  gap: 16px;
}
</style>