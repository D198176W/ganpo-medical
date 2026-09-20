<template>
  <div class="personal-assessment">
    <div class="bg-container">
      <img src="/src/assets/images/personal-bg.png" alt="背景" class="bg-img">
    </div>
    <NavBar title="健康评估" :show-user="false" />
    
    <div class="content">
      <div class="assessment-banner">
        <div class="banner-content">
          <h2>健康状况评估</h2>
          <p>了解您的健康状况，制定个性化健康管理计划</p>
        </div>
        <div class="banner-icon">
          <i class="fas fa-heartbeat"></i>
        </div>
      </div>

      <div class="quick-assessment">
        <div class="section-header">
          <span class="section-title">快速评估</span>
        </div>
        <div class="assessment-grid">
          <div 
            v-for="item in quickAssessments" 
            :key="item.id"
            class="assessment-card"
            :class="item.colorClass"
            @click="startAssessment(item)"
          >
            <div class="assessment-icon">
              <i :class="item.icon"></i>
            </div>
            <span class="assessment-title">{{ item.title }}</span>
            <span class="assessment-count">{{ item.count }}人参与</span>
          </div>
        </div>
      </div>

      <div class="health-scores">
        <div class="section-header">
          <span class="section-title">我的评估结果</span>
        </div>
        <div class="score-card">
          <div class="score-header">
            <span class="score-title">整体健康评分</span>
            <span class="score-date">更新于 {{ lastAssessmentDate }}</span>
          </div>
          <div class="score-display">
            <div class="score-circle">
              <svg viewBox="0 0 120 120" class="progress-svg">
                <circle cx="60" cy="60" r="52" class="progress-bg"></circle>
                <circle 
                  cx="60" 
                  cy="60" 
                  r="52" 
                  class="progress-bar"
                  :style="progressStyle"
                ></circle>
              </svg>
              <div class="score-value">{{ healthScore }}</div>
            </div>
            <div class="score-details">
              <div v-for="detail in scoreDetails" :key="detail.label" class="detail-item">
                <span class="detail-label">{{ detail.label }}</span>
                <span class="detail-value" :class="detail.class">{{ detail.value }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="assessment-history">
        <div class="section-header">
          <span class="section-title">评估历史</span>
        </div>
        <div class="history-list">
          <div 
            v-for="history in assessmentHistory" 
            :key="history.id"
            class="history-item"
            @click="viewHistory(history)"
          >
            <div class="history-icon" :class="history.bgClass">
              <i :class="history.icon"></i>
            </div>
            <div class="history-info">
              <span class="history-title">{{ history.title }}</span>
              <span class="history-date">{{ history.date }}</span>
            </div>
            <div class="history-score" :class="history.scoreClass">
              {{ history.score }}分
            </div>
            <div class="history-arrow">
              <img src="/src/assets/images/arrow-right.png" alt="右箭头">
            </div>
          </div>
        </div>
      </div>

      <div class="recommendations">
        <div class="section-header">
          <span class="section-title">健康建议</span>
        </div>
        <div class="recommendation-list">
          <div 
            v-for="rec in recommendations" 
            :key="rec.id"
            class="recommendation-item"
          >
            <div class="rec-icon" :class="rec.bgClass">
              <i :class="rec.icon"></i>
            </div>
            <div class="rec-content">
              <h4>{{ rec.title }}</h4>
              <p>{{ rec.desc }}</p>
            </div>
          </div>
        </div>
      </div>

      <div class="start-assessment-btn">
        <button @click="startFullAssessment">
          <i class="fas fa-clipboard-list"></i>
          开始全面评估
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import NavBar from '@/components/common/NavBar.vue'

const lastAssessmentDate = ref('2024-03-15')
const healthScore = ref(86)

const progressStyle = computed(() => {
  const circumference = 2 * Math.PI * 52
  const offset = circumference - (healthScore.value / 100) * circumference
  return {
    strokeDasharray: `${circumference} ${circumference}`,
    strokeDashoffset: offset
  }
})

const scoreDetails = ref([
  { label: '身体状况', value: '良好', class: 'good' },
  { label: '心理状态', value: '优秀', class: 'excellent' },
  { label: '生活习惯', value: '一般', class: 'fair' }
])

const quickAssessments = ref([
  {
    id: 1,
    title: '心理健康',
    icon: 'fas fa-brain',
    count: 12580,
    colorClass: 'card-purple'
  },
  {
    id: 2,
    title: '睡眠质量',
    icon: 'fas fa-moon',
    count: 8960,
    colorClass: 'card-blue'
  },
  {
    id: 3,
    title: '营养状况',
    icon: 'fas fa-apple-alt',
    count: 6420,
    colorClass: 'card-green'
  },
  {
    id: 4,
    title: '运动能力',
    icon: 'fas fa-running',
    count: 5180,
    colorClass: 'card-orange'
  }
])

const assessmentHistory = ref([
  {
    id: 1,
    title: '全面健康评估',
    date: '2024-03-15',
    score: 86,
    scoreClass: 'score-good',
    icon: 'fas fa-clipboard-check',
    bgClass: 'bg-green'
  },
  {
    id: 2,
    title: '心理健康测评',
    date: '2024-03-10',
    score: 92,
    scoreClass: 'score-excellent',
    icon: 'fas fa-heart',
    bgClass: 'bg-purple'
  },
  {
    id: 3,
    title: '睡眠质量评估',
    date: '2024-03-05',
    score: 78,
    scoreClass: 'score-fair',
    icon: 'fas fa-bed',
    bgClass: 'bg-blue'
  }
])

const recommendations = ref([
  {
    title: '增加有氧运动',
    desc: '建议每周进行 3-4 次有氧运动，每次 30 分钟以上，如慢跑、游泳等。',
    icon: 'fas fa-heartbeat',
    bgClass: 'bg-red'
  },
  {
    title: '保持规律作息',
    desc: '每天在相同时间入睡和起床，保证 7-8 小时睡眠，有助于提高睡眠质量。',
    icon: 'fas fa-clock',
    bgClass: 'bg-blue'
  },
  {
    title: '均衡饮食',
    desc: '增加蔬菜水果摄入，减少高脂肪食物，保持饮食多样化。',
    icon: 'fas fa-carrot',
    bgClass: 'bg-green'
  }
])

const startAssessment = (item) => {
  console.log('开始评估:', item.title)
}

const viewHistory = (history) => {
  console.log('查看历史:', history)
}

const startFullAssessment = () => {
  console.log('开始全面评估')
}
</script>

<style scoped>
.personal-assessment {
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

.assessment-banner {
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

.quick-assessment {
  margin-bottom: 20px;
}

.assessment-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}

.assessment-card {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 16px;
  padding: 20px 16px;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  cursor: pointer;
  transition: all 0.3s ease;
}

.assessment-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
}

.assessment-card.card-purple {
  border-top: 3px solid #9C27B0;
}

.assessment-card.card-blue {
  border-top: 3px solid #1E88E5;
}

.assessment-card.card-green {
  border-top: 3px solid #4CAF50;
}

.assessment-card.card-orange {
  border-top: 3px solid #FF9800;
}

.assessment-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 12px;
  font-size: 22px;
  color: white;
}

.card-purple .assessment-icon {
  background: linear-gradient(135deg, #9C27B0, #CE93D8);
}

.card-blue .assessment-icon {
  background: linear-gradient(135deg, #1E88E5, #64B5F6);
}

.card-green .assessment-icon {
  background: linear-gradient(135deg, #4CAF50, #A5D6A7);
}

.card-orange .assessment-icon {
  background: linear-gradient(135deg, #FF9800, #FFE0B2);
}

.assessment-title {
  font-size: 15px;
  font-weight: 600;
  color: #333;
  margin-bottom: 4px;
}

.assessment-count {
  font-size: 12px;
  color: #999;
}

.health-scores {
  margin-bottom: 20px;
}

.score-card {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 20px;
  padding: 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.score-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.score-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.score-date {
  font-size: 12px;
  color: #999;
}

.score-display {
  display: flex;
  align-items: center;
  gap: 20px;
}

.score-circle {
  width: 120px;
  height: 120px;
  position: relative;
  flex-shrink: 0;
}

.progress-svg {
  width: 100%;
  height: 100%;
  transform: rotate(-90deg);
}

.progress-bg {
  fill: none;
  stroke: #f0f0f0;
  stroke-width: 8;
}

.progress-bar {
  fill: none;
  stroke: url(#gradient);
  stroke-width: 8;
  stroke-linecap: round;
  transition: stroke-dashoffset 0.5s ease;
}

.score-value {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 32px;
  font-weight: 700;
  color: #1E88E5;
}

.score-details {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.detail-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  background: #f8f9fa;
  border-radius: 12px;
}

.detail-label {
  font-size: 14px;
  color: #666;
}

.detail-value {
  font-size: 14px;
  font-weight: 600;
  padding: 4px 12px;
  border-radius: 12px;
}

.detail-value.excellent {
  background: #E8F5E8;
  color: #4CAF50;
}

.detail-value.good {
  background: #E3F2FD;
  color: #1E88E5;
}

.detail-value.fair {
  background: #FFF3E0;
  color: #FF9800;
}

.assessment-history {
  margin-bottom: 20px;
}

.history-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.history-item {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 16px;
  padding: 16px;
  display: flex;
  align-items: center;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  cursor: pointer;
  transition: transform 0.2s;
}

.history-item:hover {
  transform: translateY(-2px);
}

.history-icon {
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

.history-icon.bg-green {
  background: linear-gradient(135deg, #4CAF50, #A5D6A7);
}

.history-icon.bg-purple {
  background: linear-gradient(135deg, #9C27B0, #CE93D8);
}

.history-icon.bg-blue {
  background: linear-gradient(135deg, #1E88E5, #64B5F6);
}

.history-info {
  flex: 1;
}

.history-title {
  display: block;
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 4px;
}

.history-date {
  display: block;
  font-size: 12px;
  color: #999;
}

.history-score {
  font-size: 20px;
  font-weight: 700;
  margin-right: 12px;
  padding: 4px 12px;
  border-radius: 12px;
}

.history-score.score-excellent {
  color: #4CAF50;
  background: #E8F5E8;
}

.history-score.score-good {
  color: #1E88E5;
  background: #E3F2FD;
}

.history-score.score-fair {
  color: #FF9800;
  background: #FFF3E0;
}

.history-arrow {
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.history-arrow img {
  width: 16px;
  height: 16px;
}

.recommendations {
  margin-bottom: 20px;
}

.recommendation-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.recommendation-item {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 16px;
  padding: 16px;
  display: flex;
  align-items: flex-start;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.rec-icon {
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

.rec-icon.bg-red {
  background: linear-gradient(135deg, #E53935, #EF9A9A);
}

.rec-icon.bg-blue {
  background: linear-gradient(135deg, #1E88E5, #64B5F6);
}

.rec-icon.bg-green {
  background: linear-gradient(135deg, #4CAF50, #A5D6A7);
}

.rec-content {
  flex: 1;
}

.rec-content h4 {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin: 0 0 6px 0;
}

.rec-content p {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  margin: 0;
}

.start-assessment-btn {
  margin-top: 20px;
}

.start-assessment-btn button {
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

.start-assessment-btn button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(30, 136, 229, 0.4);
}

.start-assessment-btn i {
  font-size: 18px;
}
</style>
