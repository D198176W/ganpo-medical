<template>
  <div class="personal-health">
    <div class="bg-container">
      <img src="/src/assets/images/personal-bg.png" alt="背景" class="bg-img">
    </div>
    <NavBar title="健康评分" :show-user="false" />
    
    <div class="content">
      <div class="health-score-card">
        <div class="score-circle">
          <div class="score-value">{{ healthScore }}</div>
          <div class="score-label">健康评分</div>
        </div>
        <div class="score-desc">
          <div class="score-status" :class="scoreStatus.class">
            {{ scoreStatus.text }}
          </div>
          <p class="score-tip">{{ scoreTip }}</p>
        </div>
      </div>

      <div class="health-metrics">
        <div class="section-header">
          <span class="section-title">健康指标</span>
        </div>
        <div class="metrics-grid">
          <div class="metric-card" v-for="metric in healthMetrics" :key="metric.id">
            <div class="metric-icon" :class="metric.bgClass">
              <i :class="metric.icon"></i>
            </div>
            <div class="metric-info">
              <span class="metric-title">{{ metric.title }}</span>
              <span class="metric-value">{{ metric.value }}<span class="metric-unit">{{ metric.unit }}</span></span>
              <div class="metric-bar">
                <div class="metric-fill" :style="{ width: metric.progress + '%', backgroundColor: metric.color }"></div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="health-suggestions">
        <div class="section-header">
          <span class="section-title">健康建议</span>
        </div>
        <div class="suggestions-list">
          <div class="suggestion-item" v-for="(suggestion, index) in healthSuggestions" :key="index">
            <div class="suggestion-icon">
              <i :class="suggestion.icon"></i>
            </div>
            <div class="suggestion-content">
              <h4>{{ suggestion.title }}</h4>
              <p>{{ suggestion.desc }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import NavBar from '@/components/common/NavBar.vue'

const healthScore = ref(86)

const scoreStatus = computed(() => {
  if (healthScore.value >= 90) {
    return { class: 'excellent', text: '优秀' }
  } else if (healthScore.value >= 75) {
    return { class: 'good', text: '良好' }
  } else if (healthScore.value >= 60) {
    return { class: 'fair', text: '一般' }
  } else {
    return { class: 'poor', text: '需关注' }
  }
})

const scoreTip = computed(() => {
  if (healthScore.value >= 90) {
    return '继续保持当前的健康生活方式！'
  } else if (healthScore.value >= 75) {
    return '整体状况良好，可以进一步优化。'
  } else if (healthScore.value >= 60) {
    return '需要关注健康状况，建议增加运动。'
  } else {
    return '健康状况需要改善，请咨询医生建议。'
  }
})

const healthMetrics = ref([
  {
    id: 1,
    title: '体重',
    value: 65,
    unit: 'kg',
    icon: 'fas fa-weight',
    bgClass: 'bg-blue',
    color: '#1E88E5',
    progress: 75
  },
  {
    id: 2,
    title: '睡眠',
    value: 7.5,
    unit: 'h',
    icon: 'fas fa-moon',
    bgClass: 'bg-purple',
    color: '#9C27B0',
    progress: 85
  },
  {
    id: 3,
    title: '运动',
    value: 45,
    unit: 'min',
    icon: 'fas fa-running',
    bgClass: 'bg-green',
    color: '#4CAF50',
    progress: 60
  },
  {
    id: 4,
    title: '心率',
    value: 72,
    unit: 'bpm',
    icon: 'fas fa-heartbeat',
    bgClass: 'bg-red',
    color: '#E53935',
    progress: 90
  }
])

const healthSuggestions = ref([
  {
    title: '增加有氧运动',
    desc: '建议每周进行3-4次有氧运动，每次30分钟，可以选择慢跑、游泳或骑行。',
    icon: 'fas fa-heart'
  },
  {
    title: '保持规律作息',
    desc: '每天在相同时间入睡和起床，保证7-8小时睡眠，有助于提高睡眠质量。',
    icon: 'fas fa-clock'
  },
  {
    title: '均衡饮食',
    desc: '增加蔬菜水果摄入，减少高脂肪食物，保持饮食多样化。',
    icon: 'fas fa-apple-alt'
  }
])
</script>

<style scoped>
.personal-health {
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

.health-score-card {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 20px;
  padding: 30px 20px;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.score-circle {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  background: linear-gradient(135deg, #1E88E5, #64B5F6);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  margin-right: 20px;
  box-shadow: 0 4px 15px rgba(30, 136, 229, 0.3);
}

.score-value {
  font-size: 36px;
  font-weight: 700;
  color: white;
  line-height: 1;
}

.score-label {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.9);
  margin-top: 4px;
}

.score-desc {
  flex: 1;
}

.score-status {
  font-size: 20px;
  font-weight: 700;
  margin-bottom: 8px;
}

.score-status.excellent {
  color: #4CAF50;
}

.score-status.good {
  color: #1E88E5;
}

.score-status.fair {
  color: #FF9800;
}

.score-status.poor {
  color: #E53935;
}

.score-tip {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  margin: 0;
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

.health-metrics,
.health-suggestions {
  margin-bottom: 20px;
}

.metrics-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
}

.metric-card {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 16px;
  padding: 16px;
  display: flex;
  align-items: center;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.metric-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  font-size: 20px;
  color: white;
}

.metric-icon.bg-blue {
  background: linear-gradient(135deg, #1E88E5, #64B5F6);
}

.metric-icon.bg-purple {
  background: linear-gradient(135deg, #9C27B0, #CE93D8);
}

.metric-icon.bg-green {
  background: linear-gradient(135deg, #4CAF50, #A5D6A7);
}

.metric-icon.bg-red {
  background: linear-gradient(135deg, #E53935, #EF9A9A);
}

.metric-info {
  flex: 1;
}

.metric-title {
  display: block;
  font-size: 14px;
  color: #666;
  margin-bottom: 4px;
}

.metric-value {
  display: block;
  font-size: 20px;
  font-weight: 700;
  color: #333;
  margin-bottom: 6px;
}

.metric-unit {
  font-size: 12px;
  font-weight: 400;
  color: #666;
  margin-left: 2px;
}

.metric-bar {
  width: 100%;
  height: 4px;
  background-color: #f0f0f0;
  border-radius: 2px;
  overflow: hidden;
}

.metric-fill {
  height: 100%;
  border-radius: 2px;
  transition: width 0.3s ease;
}

.suggestions-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.suggestion-item {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 16px;
  padding: 16px;
  display: flex;
  align-items: flex-start;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.suggestion-icon {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  background: linear-gradient(135deg, #1E88E5, #64B5F6);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  font-size: 18px;
  color: white;
  flex-shrink: 0;
}

.suggestion-content {
  flex: 1;
}

.suggestion-content h4 {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin: 0 0 6px 0;
}

.suggestion-content p {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  margin: 0;
}
</style>
