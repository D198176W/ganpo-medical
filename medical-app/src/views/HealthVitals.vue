<template>
  <div class="vitals-page">
    <header class="page-header">
      <button class="back-btn" @click="goBack" aria-label="返回">
        <i class="fas fa-arrow-left"></i>
      </button>
      <h1 class="title">体征记录</h1>
      <span class="placeholder"></span>
    </header>

    <main class="content">
      <section class="today-card">
        <h2>今日体征概览</h2>
        <div class="today-grid">
          <div class="today-item">
            <p class="label">血压</p>
            <p class="value">{{ bloodPressure }}</p>
          </div>
          <div class="today-item">
            <p class="label">心率</p>
            <p class="value">{{ heartRate }} bpm</p>
          </div>
          <div class="today-item">
            <p class="label">体温</p>
            <p class="value">{{ temperature }}°C</p>
          </div>
          <div class="today-item">
            <p class="label">血氧</p>
            <p class="value">{{ spo2 }}%</p>
          </div>
        </div>
      </section>

      <section class="record-card">
        <h3>快速记录</h3>
        <div class="form-grid">
          <label>
            收缩压(mmHg)
            <input v-model.number="form.systolic" type="number" min="60" max="220" />
          </label>
          <label>
            舒张压(mmHg)
            <input v-model.number="form.diastolic" type="number" min="40" max="140" />
          </label>
          <label>
            心率(bpm)
            <input v-model.number="form.pulse" type="number" min="30" max="220" />
          </label>
          <label>
            体温(°C)
            <input v-model.number="form.temp" type="number" min="34" max="42" step="0.1" />
          </label>
        </div>
        <button class="save-btn" @click="saveRecord">保存记录</button>
      </section>

      <section class="history-card">
        <div class="history-head">
          <h3>最近记录</h3>
          <span>共 {{ records.length }} 条</span>
        </div>
        <div class="table-wrap">
          <table>
            <thead>
              <tr>
                <th>时间</th>
                <th>血压</th>
                <th>心率</th>
                <th>体温</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in records" :key="item.id">
                <td>{{ item.time }}</td>
                <td>{{ item.bp }}</td>
                <td>{{ item.pulse }}</td>
                <td>{{ item.temp }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </section>
    </main>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const bloodPressure = '120/78'
const heartRate = 72
const temperature = 36.6
const spo2 = 98

const form = reactive({
  systolic: 120,
  diastolic: 80,
  pulse: 72,
  temp: 36.6
})

const records = ref([
  { id: 1, time: '今天 08:20', bp: '120/78', pulse: '72 bpm', temp: '36.6°C' },
  { id: 2, time: '昨天 21:10', bp: '118/76', pulse: '70 bpm', temp: '36.5°C' },
  { id: 3, time: '昨天 08:05', bp: '122/80', pulse: '74 bpm', temp: '36.7°C' }
])

const saveRecord = () => {
  const bp = `${form.systolic}/${form.diastolic}`
  records.value.unshift({
    id: Date.now(),
    time: '刚刚',
    bp,
    pulse: `${form.pulse} bpm`,
    temp: `${form.temp.toFixed(1)}°C`
  })
}

const goBack = () => router.back()
</script>

<style scoped>
.vitals-page { min-height: 100vh; background: #f4f6fa; color: #1f2937; }
.page-header { position: sticky; top: 0; z-index: 10; background: #2d89d8; color: #fff; display: flex; align-items: center; justify-content: space-between; padding: 12px 16px; }
.back-btn { width: 36px; height: 36px; border: none; border-radius: 18px; background: rgba(255,255,255,.2); color: #fff; cursor: pointer; }
.title { font-size: 22px; margin: 0; }
.placeholder { width: 36px; }
.content { max-width: 920px; margin: 0 auto; padding: 16px; display: grid; gap: 14px; }
.today-card,.record-card,.history-card { background: #fff; border-radius: 14px; padding: 16px; box-shadow: 0 2px 8px rgba(0,0,0,.06); }
.today-card h2,.record-card h3,.history-card h3 { margin: 0 0 12px; }
.today-grid { display: grid; grid-template-columns: repeat(4, minmax(0,1fr)); gap: 10px; }
.today-item { background: #f8fafc; border-radius: 10px; padding: 12px; text-align: center; }
.label { margin: 0; color: #6b7280; font-size: 13px; }
.value { margin: 6px 0 0; font-size: 20px; font-weight: 600; }
.form-grid { display: grid; grid-template-columns: repeat(2, minmax(0,1fr)); gap: 12px; }
label { display: grid; gap: 6px; font-size: 13px; color: #6b7280; }
input { height: 38px; border: 1px solid #d1d5db; border-radius: 8px; padding: 0 10px; font-size: 14px; }
.save-btn { margin-top: 12px; width: 100%; height: 40px; border: none; border-radius: 8px; background: #2563eb; color: #fff; font-size: 14px; font-weight: 600; cursor: pointer; }
.history-head { display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px; color: #6b7280; font-size: 13px; }
.table-wrap { overflow-x: auto; }
table { width: 100%; border-collapse: collapse; }
th,td { padding: 10px 8px; border-bottom: 1px solid #e5e7eb; text-align: left; font-size: 13px; }
th { color: #6b7280; font-weight: 500; }
@media (max-width: 640px) { .today-grid { grid-template-columns: repeat(2, minmax(0,1fr)); } .form-grid { grid-template-columns: 1fr; } }
</style>
