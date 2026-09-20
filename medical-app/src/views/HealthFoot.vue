<template>
  <div class="steps-page">
    <header class="page-header">
      <button class="back-btn" @click="goBack" aria-label="返回">
        <i class="fas fa-arrow-left"></i>
      </button>
      <h1 class="title">今日步数</h1>
      <span class="placeholder"></span>
    </header>

    <main class="content">
      <section class="summary-card">
        <p class="label">当前步数</p>
        <p class="value">{{ todaySteps.toLocaleString() }} <span>步</span></p>
        <p class="target">目标 {{ targetSteps.toLocaleString() }} 步</p>
        <div class="progress-track">
          <div class="progress-fill" :style="{ width: `${progressPercent}%` }"></div>
        </div>
        <p class="percent">完成度 {{ progressPercent }}%</p>
      </section>

      <section class="stat-grid">
        <div class="stat-item">
          <p class="stat-label">运动距离</p>
          <p class="stat-value">{{ distanceKm }} km</p>
        </div>
        <div class="stat-item">
          <p class="stat-label">消耗热量</p>
          <p class="stat-value">{{ calories }} kcal</p>
        </div>
        <div class="stat-item">
          <p class="stat-label">活跃时长</p>
          <p class="stat-value">{{ activeMinutes }} min</p>
        </div>
      </section>

      <section class="trend-card">
        <div class="trend-head">
          <h2>分时步数趋势</h2>
          <span>单位：步</span>
        </div>
        <div class="bars">
          <div v-for="item in stepTrend" :key="item.time" class="bar-col">
            <div class="bar-wrap">
              <div class="bar" :style="{ height: `${barHeight(item.steps)}%` }"></div>
            </div>
            <span class="time">{{ item.time }}</span>
            <span class="num">{{ item.steps }}</span>
          </div>
        </div>
      </section>

      <section class="tips-card">
        <h3>今日建议</h3>
        <ul>
          <li v-if="todaySteps < targetSteps">距离目标还差 {{ (targetSteps - todaySteps).toLocaleString() }} 步，建议晚饭后快走 20 分钟。</li>
          <li>每连续久坐 1 小时，起身活动 3-5 分钟。</li>
          <li>保持步频稳定，避免突然高强度冲刺。</li>
        </ul>
      </section>
    </main>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const targetSteps = 10000
const stepTrend = ref([
  { time: '08:00', steps: 420 },
  { time: '09:00', steps: 860 },
  { time: '10:00', steps: 520 },
  { time: '11:00', steps: 740 },
  { time: '12:00', steps: 980 },
  { time: '13:00', steps: 430 },
  { time: '14:00', steps: 660 },
  { time: '15:00', steps: 710 },
  { time: '16:00', steps: 920 },
  { time: '17:00', steps: 1120 },
  { time: '18:00', steps: 780 },
  { time: '19:00', steps: 400 }
])

const todaySteps = computed(() => stepTrend.value.reduce((sum, item) => sum + item.steps, 0))
const progressPercent = computed(() => Math.min(100, Math.round((todaySteps.value / targetSteps) * 100)))
const distanceKm = computed(() => (todaySteps.value * 0.0007).toFixed(2))
const calories = computed(() => Math.round(todaySteps.value * 0.04))
const activeMinutes = computed(() => Math.round(todaySteps.value / 100))

const barHeight = (steps) => {
  const max = 1300
  return Math.max(8, Math.min(100, (steps / max) * 100))
}

const goBack = () => router.back()
</script>

<style scoped>
.steps-page { min-height: 100vh; background: #f4f6fa; color: #1f2937; }
.page-header { position: sticky; top: 0; z-index: 10; background: #2d89d8; color: #fff; display: flex; align-items: center; justify-content: space-between; padding: 12px 16px; }
.back-btn { width: 36px; height: 36px; border: none; border-radius: 18px; background: rgba(255,255,255,.2); color: #fff; cursor: pointer; }
.title { font-size: 22px; margin: 0; }
.placeholder { width: 36px; }
.content { max-width: 900px; margin: 0 auto; padding: 16px; display: grid; gap: 14px; }
.summary-card,.trend-card,.tips-card { background: #fff; border-radius: 14px; padding: 16px; box-shadow: 0 2px 8px rgba(0,0,0,.06); }
.label { margin: 0; color: #6b7280; }
.value { margin: 8px 0 6px; font-size: 34px; font-weight: 700; }
.value span { font-size: 16px; font-weight: 500; }
.target,.percent { margin: 4px 0; font-size: 14px; color: #6b7280; }
.progress-track { margin-top: 10px; width: 100%; height: 10px; border-radius: 999px; background: #e5e7eb; overflow: hidden; }
.progress-fill { height: 100%; background: linear-gradient(90deg, #22c55e 0%, #16a34a 100%); }
.stat-grid { display: grid; grid-template-columns: repeat(3, minmax(0, 1fr)); gap: 12px; }
.stat-item { background: #fff; border-radius: 12px; padding: 12px; text-align: center; }
.stat-label { margin: 0; color: #6b7280; font-size: 13px; }
.stat-value { margin: 6px 0 0; font-size: 20px; font-weight: 600; }
.trend-head { display: flex; justify-content: space-between; align-items: baseline; margin-bottom: 10px; }
.trend-head h2 { margin: 0; font-size: 17px; }
.trend-head span { font-size: 12px; color: #6b7280; }
.bars { display: grid; grid-template-columns: repeat(12, minmax(0, 1fr)); gap: 8px; align-items: end; height: 230px; }
.bar-col { display: flex; flex-direction: column; align-items: center; gap: 4px; }
.bar-wrap { width: 100%; height: 150px; display: flex; align-items: end; }
.bar { width: 100%; min-height: 6px; border-radius: 8px 8px 4px 4px; background: linear-gradient(180deg, #60a5fa 0%, #2563eb 100%); }
.time { font-size: 11px; color: #6b7280; }
.num { font-size: 12px; font-weight: 600; }
.tips-card h3 { margin-top: 0; }
.tips-card ul { margin: 8px 0 0; padding-left: 18px; }
.tips-card li { margin-bottom: 6px; }
@media (max-width: 640px) { .stat-grid { grid-template-columns: 1fr; } .bars { overflow-x: auto; grid-template-columns: repeat(12, 48px); } }
</style>
