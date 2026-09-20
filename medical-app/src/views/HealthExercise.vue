<template>
  <div class="exercise-page">
    <header class="page-header">
      <button class="back-btn" @click="goBack" aria-label="返回">
        <i class="fas fa-arrow-left"></i>
      </button>
      <h1 class="title">运动计划</h1>
      <span class="placeholder"></span>
    </header>

    <main class="content">
      <section class="summary-card">
        <h2>本周进度</h2>
        <p class="desc">坚持规律运动，提升心肺功能和体能状态。</p>
        <div class="progress-wrap">
          <div class="progress-track">
            <div class="progress-fill" :style="{ width: `${weekProgress}%` }"></div>
          </div>
          <span>{{ weekProgress }}%</span>
        </div>
        <div class="summary-grid">
          <div class="item">
            <p class="label">计划次数</p>
            <p class="value">{{ weeklyTarget }} 次</p>
          </div>
          <div class="item">
            <p class="label">已完成</p>
            <p class="value">{{ completedCount }} 次</p>
          </div>
          <div class="item">
            <p class="label">总时长</p>
            <p class="value">{{ totalMinutes }} 分钟</p>
          </div>
        </div>
      </section>

      <section class="plan-card">
        <div class="head">
          <h3>本周训练计划</h3>
          <span>{{ plans.length }} 项</span>
        </div>
        <div v-for="plan in plans" :key="plan.id" class="plan-item" :class="{ done: plan.done }">
          <div class="left">
            <p class="name">{{ plan.name }}</p>
            <p class="meta">{{ plan.day }} · {{ plan.duration }} 分钟 · {{ plan.intensity }}</p>
          </div>
          <button class="action-btn" @click="toggleDone(plan.id)">
            {{ plan.done ? '已完成' : '标记完成' }}
          </button>
        </div>
      </section>

      <section class="add-card">
        <h3>新增训练任务</h3>
        <div class="form-grid">
          <label>
            项目名称
            <input v-model.trim="form.name" type="text" placeholder="如：慢跑训练" />
          </label>
          <label>
            训练日
            <select v-model="form.day">
              <option value="周一">周一</option>
              <option value="周二">周二</option>
              <option value="周三">周三</option>
              <option value="周四">周四</option>
              <option value="周五">周五</option>
              <option value="周六">周六</option>
              <option value="周日">周日</option>
            </select>
          </label>
          <label>
            时长(分钟)
            <input v-model.number="form.duration" type="number" min="5" />
          </label>
          <label>
            强度
            <select v-model="form.intensity">
              <option value="低强度">低强度</option>
              <option value="中强度">中强度</option>
              <option value="高强度">高强度</option>
            </select>
          </label>
        </div>
        <button class="save-btn" @click="addPlan">添加计划</button>
      </section>
    </main>
  </div>
</template>

<script setup>
import { computed, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const weeklyTarget = 5

const plans = ref([
  { id: 1, name: '晨间慢跑', day: '周一', duration: 30, intensity: '中强度', done: true },
  { id: 2, name: '核心训练', day: '周三', duration: 25, intensity: '中强度', done: false },
  { id: 3, name: '力量循环', day: '周五', duration: 40, intensity: '高强度', done: false },
  { id: 4, name: '拉伸恢复', day: '周六', duration: 20, intensity: '低强度', done: true }
])

const form = reactive({
  name: '',
  day: '周一',
  duration: 30,
  intensity: '中强度'
})

const completedCount = computed(() => plans.value.filter(p => p.done).length)
const totalMinutes = computed(() => plans.value.filter(p => p.done).reduce((sum, p) => sum + p.duration, 0))
const weekProgress = computed(() => Math.min(100, Math.round((completedCount.value / weeklyTarget) * 100)))

const toggleDone = (id) => {
  const target = plans.value.find(p => p.id === id)
  if (target) target.done = !target.done
}

const addPlan = () => {
  if (!form.name || !form.duration) return
  plans.value.push({
    id: Date.now(),
    name: form.name,
    day: form.day,
    duration: Number(form.duration),
    intensity: form.intensity,
    done: false
  })
  form.name = ''
  form.duration = 30
  form.intensity = '中强度'
}

const goBack = () => router.back()
</script>

<style scoped>
.exercise-page { min-height: 100vh; background: #f4f6fa; color: #1f2937; }
.page-header { position: sticky; top: 0; z-index: 10; background: #2d89d8; color: #fff; display: flex; align-items: center; justify-content: space-between; padding: 12px 16px; }
.back-btn { width: 36px; height: 36px; border: none; border-radius: 18px; background: rgba(255,255,255,.2); color: #fff; cursor: pointer; }
.title { font-size: 22px; margin: 0; }
.placeholder { width: 36px; }
.content { max-width: 920px; margin: 0 auto; padding: 16px; display: grid; gap: 14px; }
.summary-card,.plan-card,.add-card { background: #fff; border-radius: 14px; padding: 16px; box-shadow: 0 2px 8px rgba(0,0,0,.06); }
.summary-card h2,.add-card h3 { margin: 0 0 8px; }
.desc { margin: 0 0 10px; color: #6b7280; }
.progress-wrap { display: flex; align-items: center; gap: 8px; margin-bottom: 10px; }
.progress-track { flex: 1; height: 10px; border-radius: 999px; background: #e5e7eb; overflow: hidden; }
.progress-fill { height: 100%; background: linear-gradient(90deg, #a78bfa 0%, #7c3aed 100%); }
.summary-grid { display: grid; grid-template-columns: repeat(3, minmax(0,1fr)); gap: 10px; }
.item { background: #f8fafc; border-radius: 10px; padding: 10px; text-align: center; }
.label { margin: 0; color: #6b7280; font-size: 13px; }
.value { margin: 6px 0 0; font-size: 20px; font-weight: 600; }
.head { display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px; color: #6b7280; font-size: 13px; }
.plan-item { display: flex; justify-content: space-between; gap: 12px; align-items: center; border: 1px solid #e5e7eb; border-radius: 10px; padding: 10px 12px; margin-bottom: 10px; }
.plan-item.done { background: #f5f3ff; border-color: #ddd6fe; }
.name { margin: 0; font-weight: 600; }
.meta { margin: 4px 0 0; color: #6b7280; font-size: 13px; }
.action-btn { border: none; background: #7c3aed; color: #fff; border-radius: 8px; padding: 8px 12px; cursor: pointer; min-width: 94px; }
.plan-item.done .action-btn { background: #16a34a; }
.form-grid { display: grid; grid-template-columns: repeat(2, minmax(0,1fr)); gap: 10px; }
label { display: grid; gap: 6px; font-size: 13px; color: #6b7280; }
input,select { height: 38px; border: 1px solid #d1d5db; border-radius: 8px; padding: 0 10px; font-size: 14px; }
.save-btn { margin-top: 12px; width: 100%; height: 40px; border: none; border-radius: 8px; background: #7c3aed; color: #fff; font-size: 14px; font-weight: 600; cursor: pointer; }
@media (max-width: 640px) { .summary-grid,.form-grid { grid-template-columns: 1fr; } }
</style>
