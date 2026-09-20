<template>
  <div class="heart-rate-page">
    <header class="page-header">
      <button class="back-btn" @click="goBack" aria-label="返回">
        <i class="fas fa-arrow-left"></i>
      </button>
      <h1 class="title">心率数据</h1>
      <span class="placeholder"></span>
    </header>

    <main class="content">
      <section class="current-card">
        <p class="label">当前心率</p>
        <p class="value">{{ currentHeartRate }} <span>bpm</span></p>
        <p class="status" :class="currentStatus.className">{{ currentStatus.text }}</p>
      </section>

      <section class="stats-grid">
        <div class="stat-item">
          <p class="stat-label">今日最低</p>
          <p class="stat-value">{{ minHeartRate }} bpm</p>
        </div>
        <div class="stat-item">
          <p class="stat-label">今日最高</p>
          <p class="stat-value">{{ maxHeartRate }} bpm</p>
        </div>
        <div class="stat-item">
          <p class="stat-label">今日平均</p>
          <p class="stat-value">{{ averageHeartRate }} bpm</p>
        </div>
      </section>

      <section class="trend-card">
        <div class="trend-header">
          <h2>近 12 小时趋势</h2>
          <span class="unit">单位：bpm</span>
        </div>
        <div class="chart">
          <div
            v-for="point in trendData"
            :key="point.time"
            class="bar-group"
          >
            <div class="bar-wrapper">
              <div class="bar" :style="{ height: `${calcBarHeight(point.value)}%` }"></div>
            </div>
            <span class="time">{{ point.time }}</span>
            <span class="num">{{ point.value }}</span>
          </div>
        </div>
      </section>

      <section class="tips-card">
        <h3>健康提示</h3>
        <ul>
          <li>静息心率长期超过 100 bpm，建议就医检查。</li>
          <li>运动后心率升高属于正常，休息后应逐步回落。</li>
          <li>保证睡眠和补水，有助于维持稳定心率。</li>
        </ul>
      </section>
    </main>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const trendData = ref([
  { time: '08:00', value: 68 },
  { time: '09:00', value: 72 },
  { time: '10:00', value: 75 },
  { time: '11:00', value: 70 },
  { time: '12:00', value: 78 },
  { time: '13:00', value: 80 },
  { time: '14:00', value: 76 },
  { time: '15:00', value: 74 },
  { time: '16:00', value: 71 },
  { time: '17:00', value: 73 },
  { time: '18:00', value: 77 },
  { time: '19:00', value: 72 }
])

const currentHeartRate = computed(() => {
  const last = trendData.value[trendData.value.length - 1]
  return last ? last.value : 0
})

const minHeartRate = computed(() => Math.min(...trendData.value.map(item => item.value)))
const maxHeartRate = computed(() => Math.max(...trendData.value.map(item => item.value)))
const averageHeartRate = computed(() => {
  const total = trendData.value.reduce((sum, item) => sum + item.value, 0)
  return Math.round(total / trendData.value.length)
})

const currentStatus = computed(() => {
  const rate = currentHeartRate.value
  if (rate < 60) return { text: '偏低', className: 'low' }
  if (rate > 100) return { text: '偏高', className: 'high' }
  return { text: '正常', className: 'normal' }
})

const calcBarHeight = (value) => {
  const min = 50
  const max = 130
  const safeValue = Math.min(Math.max(value, min), max)
  return ((safeValue - min) / (max - min)) * 100
}

const goBack = () => {
  router.back()
}
</script>

<style scoped>
.heart-rate-page {
  min-height: 100vh;
  background: #f4f6fa;
  color: #1f2937;
}

.page-header {
  position: sticky;
  top: 0;
  z-index: 10;
  background: #2d89d8;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
}

.back-btn {
  width: 36px;
  height: 36px;
  border: none;
  border-radius: 18px;
  background: rgba(255, 255, 255, 0.2);
  color: #fff;
  cursor: pointer;
}

.title {
  font-size: 22px;
  margin: 0;
}

.placeholder {
  width: 36px;
}

.content {
  max-width: 900px;
  margin: 0 auto;
  padding: 16px;
  display: grid;
  gap: 14px;
}

.current-card,
.trend-card,
.tips-card {
  background: #fff;
  border-radius: 14px;
  padding: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.label {
  margin: 0;
  color: #6b7280;
}

.value {
  margin: 8px 0;
  font-size: 36px;
  font-weight: 700;
}

.value span {
  font-size: 16px;
  font-weight: 500;
}

.status {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 999px;
  font-size: 14px;
}

.status.normal { background: #e7f8ec; color: #1f9d4a; }
.status.low { background: #e8f1ff; color: #2563eb; }
.status.high { background: #ffe8e8; color: #dc2626; }

.stats-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 12px;
}

.stat-item {
  background: #fff;
  border-radius: 12px;
  padding: 12px;
  text-align: center;
}

.stat-label {
  margin: 0;
  color: #6b7280;
  font-size: 13px;
}

.stat-value {
  margin: 6px 0 0;
  font-size: 20px;
  font-weight: 600;
}

.trend-header {
  display: flex;
  justify-content: space-between;
  align-items: baseline;
  margin-bottom: 10px;
}

.trend-header h2 {
  margin: 0;
  font-size: 17px;
}

.unit {
  color: #6b7280;
  font-size: 12px;
}

.chart {
  display: grid;
  grid-template-columns: repeat(12, minmax(0, 1fr));
  gap: 8px;
  align-items: end;
  height: 230px;
}

.bar-group {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
}

.bar-wrapper {
  width: 100%;
  height: 150px;
  display: flex;
  align-items: end;
}

.bar {
  width: 100%;
  min-height: 6px;
  border-radius: 8px 8px 4px 4px;
  background: linear-gradient(180deg, #5cb3ff 0%, #2d89d8 100%);
}

.time {
  font-size: 11px;
  color: #6b7280;
}

.num {
  font-size: 12px;
  font-weight: 600;
}

.tips-card h3 {
  margin-top: 0;
}

.tips-card ul {
  margin: 8px 0 0;
  padding-left: 18px;
}

.tips-card li {
  margin-bottom: 6px;
}

@media (max-width: 640px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }
  .chart {
    overflow-x: auto;
    grid-template-columns: repeat(12, 48px);
  }
}
</style>
