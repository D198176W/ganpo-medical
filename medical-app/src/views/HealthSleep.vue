<template>
  <div class="sleep-page">
    <header class="page-header">
      <button class="back-btn" @click="goBack" aria-label="返回">
        <i class="fas fa-arrow-left"></i>
      </button>
      <h1 class="title">睡眠小时</h1>
      <span class="placeholder"></span>
    </header>

    <main class="content">
      <section class="summary-card">
        <p class="label">昨晚睡眠时长</p>
        <p class="value">{{ sleepHours.toFixed(1) }} <span>小时</span></p>
        <p class="target">建议睡眠 {{ targetHours }}-{{ targetMaxHours }} 小时</p>
        <div class="progress-track">
          <div class="progress-fill" :style="{ width: `${progressPercent}%` }"></div>
        </div>
        <p class="status" :class="sleepStatus.className">{{ sleepStatus.text }}</p>
      </section>

      <section class="stat-grid">
        <div class="stat-item">
          <p class="stat-label">入睡时间</p>
          <p class="stat-value">{{ sleepStart }}</p>
        </div>
        <div class="stat-item">
          <p class="stat-label">起床时间</p>
          <p class="stat-value">{{ sleepEnd }}</p>
        </div>
        <div class="stat-item">
          <p class="stat-label">深睡占比</p>
          <p class="stat-value">{{ deepSleepPercent }}%</p>
        </div>
      </section>

      <section class="trend-card">
        <div class="trend-head">
          <h2>近 7 天睡眠趋势</h2>
          <span>单位：小时</span>
        </div>
        <div class="bars">
          <div v-for="item in weeklySleep" :key="item.day" class="bar-col">
            <div class="bar-wrap">
              <div class="bar" :style="{ height: `${barHeight(item.hours)}%` }"></div>
            </div>
            <span class="time">{{ item.day }}</span>
            <span class="num">{{ item.hours }}</span>
          </div>
        </div>
      </section>

      <section class="tips-card">
        <h3>睡眠建议</h3>
        <ul>
          <li v-if="sleepHours < targetHours">建议今晚提前 30 分钟上床，减少睡前刷手机时间。</li>
          <li>下午 4 点后避免摄入过多咖啡因。</li>
          <li>保持卧室安静和适宜温度，有助于提升深睡比例。</li>
        </ul>
      </section>
    </main>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const targetHours = 7
const targetMaxHours = 9
const sleepStart = '23:35'
const sleepEnd = '06:48'
const deepSleepPercent = 26

const weeklySleep = ref([
  { day: '周一', hours: 6.8 },
  { day: '周二', hours: 7.2 },
  { day: '周三', hours: 6.5 },
  { day: '周四', hours: 7.6 },
  { day: '周五', hours: 7.1 },
  { day: '周六', hours: 8.0 },
  { day: '周日', hours: 7.2 }
])

const sleepHours = computed(() => {
  const last = weeklySleep.value[weeklySleep.value.length - 1]
  return last ? last.hours : 0
})

const progressPercent = computed(() => {
  const ratio = (sleepHours.value / targetHours) * 100
  return Math.max(0, Math.min(100, Math.round(ratio)))
})

const sleepStatus = computed(() => {
  if (sleepHours.value < targetHours) return { text: '睡眠不足', className: 'low' }
  if (sleepHours.value > targetMaxHours) return { text: '略长', className: 'high' }
  return { text: '睡眠达标', className: 'normal' }
})

const barHeight = (hours) => {
  const min = 4
  const max = 10
  const safe = Math.min(Math.max(hours, min), max)
  return ((safe - min) / (max - min)) * 100
}

const goBack = () => router.back()
</script>

<style scoped>
.sleep-page { min-height: 100vh; background: #f4f6fa; color: #1f2937; }
.page-header { position: sticky; top: 0; z-index: 10; background: #2d89d8; color: #fff; display: flex; align-items: center; justify-content: space-between; padding: 12px 16px; }
.back-btn { width: 36px; height: 36px; border: none; border-radius: 18px; background: rgba(255,255,255,.2); color: #fff; cursor: pointer; }
.title { font-size: 22px; margin: 0; }
.placeholder { width: 36px; }
.content { max-width: 900px; margin: 0 auto; padding: 16px; display: grid; gap: 14px; }
.summary-card,.trend-card,.tips-card { background: #fff; border-radius: 14px; padding: 16px; box-shadow: 0 2px 8px rgba(0,0,0,.06); }
.label { margin: 0; color: #6b7280; }
.value { margin: 8px 0 6px; font-size: 34px; font-weight: 700; }
.value span { font-size: 16px; font-weight: 500; }
.target { margin: 4px 0; font-size: 14px; color: #6b7280; }
.progress-track { margin-top: 10px; width: 100%; height: 10px; border-radius: 999px; background: #e5e7eb; overflow: hidden; }
.progress-fill { height: 100%; background: linear-gradient(90deg, #38bdf8 0%, #2563eb 100%); }
.status { margin-top: 10px; display: inline-block; padding: 4px 10px; border-radius: 999px; font-size: 14px; }
.status.normal { background: #e7f8ec; color: #1f9d4a; }
.status.low { background: #fff1e6; color: #ea580c; }
.status.high { background: #ecebff; color: #6d28d9; }
.stat-grid { display: grid; grid-template-columns: repeat(3, minmax(0, 1fr)); gap: 12px; }
.stat-item { background: #fff; border-radius: 12px; padding: 12px; text-align: center; }
.stat-label { margin: 0; color: #6b7280; font-size: 13px; }
.stat-value { margin: 6px 0 0; font-size: 20px; font-weight: 600; }
.trend-head { display: flex; justify-content: space-between; align-items: baseline; margin-bottom: 10px; }
.trend-head h2 { margin: 0; font-size: 17px; }
.trend-head span { font-size: 12px; color: #6b7280; }
.bars { display: grid; grid-template-columns: repeat(7, minmax(0, 1fr)); gap: 10px; align-items: end; height: 220px; }
.bar-col { display: flex; flex-direction: column; align-items: center; gap: 4px; }
.bar-wrap { width: 100%; height: 140px; display: flex; align-items: end; }
.bar { width: 100%; min-height: 6px; border-radius: 8px 8px 4px 4px; background: linear-gradient(180deg, #93c5fd 0%, #2563eb 100%); }
.time { font-size: 11px; color: #6b7280; }
.num { font-size: 12px; font-weight: 600; }
.tips-card h3 { margin-top: 0; }
.tips-card ul { margin: 8px 0 0; padding-left: 18px; }
.tips-card li { margin-bottom: 6px; }
@media (max-width: 640px) { .stat-grid { grid-template-columns: 1fr; } }
</style>
