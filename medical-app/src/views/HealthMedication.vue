<template>
  <div class="medication-page">
    <header class="page-header">
      <button class="back-btn" @click="goBack" aria-label="返回">
        <i class="fas fa-arrow-left"></i>
      </button>
      <h1 class="title">用药提醒</h1>
      <span class="placeholder"></span>
    </header>

    <main class="content">
      <section class="today-card">
        <h2>今日用药计划</h2>
        <p class="desc">按时用药有助于提高治疗效果，请按提醒执行。</p>
        <div class="summary">
          <span>今日共 {{ medications.length }} 次提醒</span>
          <span>已完成 {{ completedCount }} 次</span>
        </div>
      </section>

      <section class="list-card">
        <div
          v-for="item in medications"
          :key="item.id"
          class="med-item"
          :class="{ done: item.done }"
        >
          <div class="left">
            <p class="name">{{ item.name }}</p>
            <p class="meta">{{ item.dose }} · {{ item.time }}</p>
            <p class="note">{{ item.note }}</p>
          </div>
          <button class="action-btn" @click="toggleDone(item.id)">
            {{ item.done ? '已服用' : '标记服用' }}
          </button>
        </div>
      </section>

      <section class="add-card">
        <h3>新增提醒</h3>
        <div class="form-grid">
          <label>
            药品名称
            <input v-model.trim="form.name" type="text" placeholder="如：阿莫西林" />
          </label>
          <label>
            剂量
            <input v-model.trim="form.dose" type="text" placeholder="如：0.5g/次" />
          </label>
          <label>
            时间
            <input v-model.trim="form.time" type="time" />
          </label>
          <label>
            备注
            <input v-model.trim="form.note" type="text" placeholder="饭后服用" />
          </label>
        </div>
        <button class="save-btn" @click="addMedication">添加提醒</button>
      </section>
    </main>
  </div>
</template>

<script setup>
import { computed, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const medications = ref([
  { id: 1, name: '维生素C', dose: '1片', time: '08:00', note: '早餐后服用', done: true },
  { id: 2, name: '阿莫西林', dose: '0.5g', time: '13:00', note: '午餐后服用', done: false },
  { id: 3, name: '复合维生素', dose: '1片', time: '20:00', note: '晚餐后服用', done: false }
])

const form = reactive({
  name: '',
  dose: '',
  time: '',
  note: ''
})

const completedCount = computed(() => medications.value.filter(item => item.done).length)

const toggleDone = (id) => {
  const item = medications.value.find(m => m.id === id)
  if (item) item.done = !item.done
}

const addMedication = () => {
  if (!form.name || !form.dose || !form.time) return
  medications.value.push({
    id: Date.now(),
    name: form.name,
    dose: form.dose,
    time: form.time,
    note: form.note || '按医嘱服用',
    done: false
  })
  form.name = ''
  form.dose = ''
  form.time = ''
  form.note = ''
}

const goBack = () => router.back()
</script>

<style scoped>
.medication-page { min-height: 100vh; background: #f4f6fa; color: #1f2937; }
.page-header { position: sticky; top: 0; z-index: 10; background: #2d89d8; color: #fff; display: flex; align-items: center; justify-content: space-between; padding: 12px 16px; }
.back-btn { width: 36px; height: 36px; border: none; border-radius: 18px; background: rgba(255,255,255,.2); color: #fff; cursor: pointer; }
.title { font-size: 22px; margin: 0; }
.placeholder { width: 36px; }
.content { max-width: 920px; margin: 0 auto; padding: 16px; display: grid; gap: 14px; }
.today-card,.list-card,.add-card { background: #fff; border-radius: 14px; padding: 16px; box-shadow: 0 2px 8px rgba(0,0,0,.06); }
.today-card h2,.add-card h3 { margin: 0 0 8px; }
.desc { margin: 0; color: #6b7280; }
.summary { margin-top: 10px; display: flex; justify-content: space-between; font-size: 14px; color: #374151; }
.med-item { display: flex; justify-content: space-between; gap: 10px; align-items: center; padding: 12px; border: 1px solid #e5e7eb; border-radius: 10px; margin-bottom: 10px; }
.med-item.done { background: #f0fdf4; border-color: #bbf7d0; }
.name { margin: 0; font-weight: 600; }
.meta,.note { margin: 4px 0 0; font-size: 13px; color: #6b7280; }
.action-btn { border: none; background: #2563eb; color: #fff; border-radius: 8px; padding: 8px 12px; cursor: pointer; min-width: 90px; }
.med-item.done .action-btn { background: #16a34a; }
.form-grid { display: grid; grid-template-columns: repeat(2, minmax(0,1fr)); gap: 10px; }
label { display: grid; gap: 6px; font-size: 13px; color: #6b7280; }
input { height: 38px; border: 1px solid #d1d5db; border-radius: 8px; padding: 0 10px; font-size: 14px; }
.save-btn { margin-top: 12px; width: 100%; height: 40px; border: none; border-radius: 8px; background: #2563eb; color: #fff; font-weight: 600; cursor: pointer; }
@media (max-width: 640px) { .form-grid { grid-template-columns: 1fr; } .summary { flex-direction: column; gap: 6px; } }
</style>
