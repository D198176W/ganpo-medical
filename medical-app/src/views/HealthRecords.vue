<template>
  <div class="records-page">
    <header class="page-header">
      <button class="back-btn" @click="goBack" aria-label="返回">
        <i class="fas fa-arrow-left"></i>
      </button>
      <h1 class="title">健康记录</h1>
      <span class="placeholder"></span>
    </header>

    <main class="content">
      <section class="summary-card">
        <h2>本周概览</h2>
        <div class="summary-grid">
          <div class="item">
            <p class="label">血压记录</p>
            <p class="value">{{ bloodPressureRecords.length }} 条</p>
          </div>
          <div class="item">
            <p class="label">血糖记录</p>
            <p class="value">{{ bloodSugarRecords.length }} 条</p>
          </div>
          <div class="item">
            <p class="label">体重记录</p>
            <p class="value">{{ weightRecords.length }} 条</p>
          </div>
        </div>
      </section>

      <section class="tabs-card">
        <button
          v-for="tab in tabs"
          :key="tab.id"
          class="tab-btn"
          :class="{ active: activeTab === tab.id }"
          @click="activeTab = tab.id"
        >
          {{ tab.name }}
        </button>
      </section>

      <section class="list-card" v-if="activeTab === 'bp'">
        <div class="head">
          <h3>血压测量</h3>
          <span>{{ bloodPressureRecords.length }} 条</span>
        </div>
        <div class="record-item" v-for="item in bloodPressureRecords" :key="item.id">
          <div class="left">
            <p class="name">{{ item.value }} mmHg</p>
            <p class="meta">{{ item.time }}</p>
          </div>
          <span class="tag" :class="item.level">{{ item.label }}</span>
        </div>
      </section>

      <section class="list-card" v-if="activeTab === 'sugar'">
        <div class="head">
          <h3>血糖检测</h3>
          <span>{{ bloodSugarRecords.length }} 条</span>
        </div>
        <div class="record-item" v-for="item in bloodSugarRecords" :key="item.id">
          <div class="left">
            <p class="name">{{ item.value }} mmol/L</p>
            <p class="meta">{{ item.time }}</p>
          </div>
          <span class="tag" :class="item.level">{{ item.label }}</span>
        </div>
      </section>

      <section class="list-card" v-if="activeTab === 'weight'">
        <div class="head">
          <h3>体重记录</h3>
          <span>{{ weightRecords.length }} 条</span>
        </div>
        <div class="record-item" v-for="item in weightRecords" :key="item.id">
          <div class="left">
            <p class="name">{{ item.value }} kg</p>
            <p class="meta">{{ item.time }}</p>
          </div>
          <span class="tag normal">记录</span>
        </div>
      </section>

      <section class="form-card">
        <h3>新增记录</h3>
        <div class="form-grid">
          <label>
            记录类型
            <select v-model="form.type">
              <option value="bp">血压测量</option>
              <option value="sugar">血糖检测</option>
              <option value="weight">体重记录</option>
            </select>
          </label>

          <label v-if="form.type === 'bp'">
            血压值(mmHg)
            <input v-model.trim="form.bpValue" type="text" placeholder="如：120/80" />
          </label>

          <label v-if="form.type === 'sugar'">
            血糖值(mmol/L)
            <input v-model.number="form.sugarValue" type="number" step="0.1" min="0" />
          </label>

          <label v-if="form.type === 'weight'">
            体重值(kg)
            <input v-model.number="form.weightValue" type="number" step="0.1" min="0" />
          </label>

          <label>
            记录时间
            <input v-model.trim="form.time" type="text" placeholder="如：今天 20:00" />
          </label>
        </div>
        <button class="save-btn" @click="addRecord">保存记录</button>
      </section>
    </main>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const tabs = [
  { id: 'bp', name: '血压' },
  { id: 'sugar', name: '血糖' },
  { id: 'weight', name: '体重' }
]
const activeTab = ref('bp')

const bloodPressureRecords = ref([
  { id: 1, value: '120/80', time: '今天 08:30', label: '正常', level: 'normal' },
  { id: 2, value: '136/86', time: '昨天 21:00', label: '偏高', level: 'warn' }
])

const bloodSugarRecords = ref([
  { id: 1, value: 5.6, time: '今天 07:45', label: '良好', level: 'normal' },
  { id: 2, value: 7.2, time: '昨天 20:30', label: '偏高', level: 'warn' }
])

const weightRecords = ref([
  { id: 1, value: 65.2, time: '今天 20:00' },
  { id: 2, value: 65.5, time: '昨天 20:00' }
])

const form = reactive({
  type: 'bp',
  bpValue: '',
  sugarValue: 0,
  weightValue: 0,
  time: '今天'
})

const addRecord = () => {
  const time = form.time || '刚刚'

  if (form.type === 'bp') {
    if (!form.bpValue) return
    bloodPressureRecords.value.unshift({
      id: Date.now(),
      value: form.bpValue,
      time,
      label: '新记录',
      level: 'normal'
    })
    form.bpValue = ''
    activeTab.value = 'bp'
    return
  }

  if (form.type === 'sugar') {
    if (!form.sugarValue) return
    bloodSugarRecords.value.unshift({
      id: Date.now(),
      value: Number(form.sugarValue),
      time,
      label: '新记录',
      level: 'normal'
    })
    form.sugarValue = 0
    activeTab.value = 'sugar'
    return
  }

  if (!form.weightValue) return
  weightRecords.value.unshift({
    id: Date.now(),
    value: Number(form.weightValue),
    time
  })
  form.weightValue = 0
  activeTab.value = 'weight'
}

const goBack = () => router.back()
</script>

<style scoped>
.records-page { min-height: 100vh; background: #f4f6fa; color: #1f2937; }
.page-header { position: sticky; top: 0; z-index: 10; background: #2d89d8; color: #fff; display: flex; align-items: center; justify-content: space-between; padding: 12px 16px; }
.back-btn { width: 36px; height: 36px; border: none; border-radius: 18px; background: rgba(255,255,255,.2); color: #fff; cursor: pointer; }
.title { font-size: 22px; margin: 0; }
.placeholder { width: 36px; }
.content { max-width: 920px; margin: 0 auto; padding: 16px; display: grid; gap: 14px; }
.summary-card,.tabs-card,.list-card,.form-card { background: #fff; border-radius: 14px; padding: 16px; box-shadow: 0 2px 8px rgba(0,0,0,.06); }
.summary-card h2,.form-card h3 { margin: 0 0 10px; }
.summary-grid { display: grid; grid-template-columns: repeat(3, minmax(0,1fr)); gap: 10px; }
.item { background: #f8fafc; border-radius: 10px; padding: 10px; text-align: center; }
.label { margin: 0; color: #6b7280; font-size: 13px; }
.value { margin: 6px 0 0; font-size: 20px; font-weight: 600; }
.tabs-card { display: grid; grid-template-columns: repeat(3, minmax(0,1fr)); gap: 10px; }
.tab-btn { height: 38px; border: 1px solid #d1d5db; border-radius: 8px; background: #fff; cursor: pointer; }
.tab-btn.active { background: #2563eb; color: #fff; border-color: #2563eb; }
.head { display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px; color: #6b7280; font-size: 13px; }
.record-item { display: flex; justify-content: space-between; gap: 12px; align-items: center; border: 1px solid #e5e7eb; border-radius: 10px; padding: 10px 12px; margin-bottom: 10px; }
.name { margin: 0; font-weight: 600; }
.meta { margin: 4px 0 0; color: #6b7280; font-size: 13px; }
.tag { font-size: 12px; border-radius: 12px; padding: 4px 10px; }
.tag.normal { background: #e7f8ec; color: #1f9d4a; }
.tag.warn { background: #fff1e6; color: #ea580c; }
.form-grid { display: grid; grid-template-columns: repeat(2, minmax(0,1fr)); gap: 10px; }
label { display: grid; gap: 6px; font-size: 13px; color: #6b7280; }
input,select { height: 38px; border: 1px solid #d1d5db; border-radius: 8px; padding: 0 10px; font-size: 14px; }
.save-btn { margin-top: 12px; width: 100%; height: 40px; border: none; border-radius: 8px; background: #2563eb; color: #fff; font-size: 14px; font-weight: 600; cursor: pointer; }
@media (max-width: 640px) { .summary-grid,.tabs-card,.form-grid { grid-template-columns: 1fr; } }
</style>
