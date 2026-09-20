<template>
  <div class="diet-page">
    <header class="page-header">
      <button class="back-btn" @click="goBack" aria-label="返回">
        <i class="fas fa-arrow-left"></i>
      </button>
      <h1 class="title">饮食记录</h1>
      <span class="placeholder"></span>
    </header>

    <main class="content">
      <section class="summary-card">
        <h2>今日摄入概览</h2>
        <div class="summary-grid">
          <div class="item">
            <p class="label">总热量</p>
            <p class="value">{{ totalCalories }} kcal</p>
          </div>
          <div class="item">
            <p class="label">蛋白质</p>
            <p class="value">{{ totalProtein }} g</p>
          </div>
          <div class="item">
            <p class="label">碳水</p>
            <p class="value">{{ totalCarbs }} g</p>
          </div>
          <div class="item">
            <p class="label">脂肪</p>
            <p class="value">{{ totalFat }} g</p>
          </div>
        </div>
      </section>

      <section class="records-card">
        <div class="head">
          <h3>今日餐次记录</h3>
          <span>{{ records.length }} 条</span>
        </div>
        <div v-for="record in records" :key="record.id" class="record-item">
          <div class="left">
            <p class="meal">{{ record.meal }} · {{ record.time }}</p>
            <p class="food">{{ record.food }}</p>
          </div>
          <div class="right">
            <p>{{ record.calories }} kcal</p>
          </div>
        </div>
      </section>

      <section class="add-card">
        <h3>新增饮食记录</h3>
        <div class="form-grid">
          <label>
            餐次
            <select v-model="form.meal">
              <option value="早餐">早餐</option>
              <option value="午餐">午餐</option>
              <option value="晚餐">晚餐</option>
              <option value="加餐">加餐</option>
            </select>
          </label>
          <label>
            时间
            <input v-model="form.time" type="time" />
          </label>
          <label class="full">
            食物内容
            <input v-model.trim="form.food" type="text" placeholder="如：鸡胸肉沙拉 + 全麦面包" />
          </label>
          <label>
            热量(kcal)
            <input v-model.number="form.calories" type="number" min="0" />
          </label>
          <label>
            蛋白质(g)
            <input v-model.number="form.protein" type="number" min="0" />
          </label>
          <label>
            碳水(g)
            <input v-model.number="form.carbs" type="number" min="0" />
          </label>
          <label>
            脂肪(g)
            <input v-model.number="form.fat" type="number" min="0" />
          </label>
        </div>
        <button class="save-btn" @click="addRecord">保存记录</button>
      </section>
    </main>
  </div>
</template>

<script setup>
import { computed, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const records = ref([
  { id: 1, meal: '早餐', time: '08:10', food: '燕麦牛奶 + 水煮蛋', calories: 420, protein: 20, carbs: 48, fat: 14 },
  { id: 2, meal: '午餐', time: '12:20', food: '米饭 + 清炒西兰花 + 鸡胸肉', calories: 650, protein: 36, carbs: 72, fat: 18 },
  { id: 3, meal: '晚餐', time: '18:40', food: '番茄牛肉面', calories: 560, protein: 28, carbs: 68, fat: 17 }
])

const form = reactive({
  meal: '早餐',
  time: '08:00',
  food: '',
  calories: 0,
  protein: 0,
  carbs: 0,
  fat: 0
})

const totalCalories = computed(() => records.value.reduce((sum, item) => sum + item.calories, 0))
const totalProtein = computed(() => records.value.reduce((sum, item) => sum + item.protein, 0))
const totalCarbs = computed(() => records.value.reduce((sum, item) => sum + item.carbs, 0))
const totalFat = computed(() => records.value.reduce((sum, item) => sum + item.fat, 0))

const addRecord = () => {
  if (!form.food || !form.time) return
  records.value.push({
    id: Date.now(),
    meal: form.meal,
    time: form.time,
    food: form.food,
    calories: Number(form.calories || 0),
    protein: Number(form.protein || 0),
    carbs: Number(form.carbs || 0),
    fat: Number(form.fat || 0)
  })
  form.food = ''
  form.calories = 0
  form.protein = 0
  form.carbs = 0
  form.fat = 0
}

const goBack = () => router.back()
</script>

<style scoped>
.diet-page { min-height: 100vh; background: #f4f6fa; color: #1f2937; }
.page-header { position: sticky; top: 0; z-index: 10; background: #2d89d8; color: #fff; display: flex; align-items: center; justify-content: space-between; padding: 12px 16px; }
.back-btn { width: 36px; height: 36px; border: none; border-radius: 18px; background: rgba(255,255,255,.2); color: #fff; cursor: pointer; }
.title { font-size: 22px; margin: 0; }
.placeholder { width: 36px; }
.content { max-width: 920px; margin: 0 auto; padding: 16px; display: grid; gap: 14px; }
.summary-card,.records-card,.add-card { background: #fff; border-radius: 14px; padding: 16px; box-shadow: 0 2px 8px rgba(0,0,0,.06); }
.summary-card h2,.add-card h3 { margin: 0 0 10px; }
.summary-grid { display: grid; grid-template-columns: repeat(4, minmax(0,1fr)); gap: 10px; }
.item { background: #f8fafc; border-radius: 10px; padding: 12px; text-align: center; }
.label { margin: 0; color: #6b7280; font-size: 13px; }
.value { margin: 6px 0 0; font-size: 20px; font-weight: 600; }
.head { display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px; color: #6b7280; font-size: 13px; }
.record-item { display: flex; justify-content: space-between; gap: 12px; align-items: center; border: 1px solid #e5e7eb; border-radius: 10px; padding: 10px 12px; margin-bottom: 10px; }
.meal { margin: 0; font-weight: 600; }
.food { margin: 4px 0 0; color: #6b7280; font-size: 13px; }
.right p { margin: 0; font-weight: 600; color: #2563eb; }
.form-grid { display: grid; grid-template-columns: repeat(2, minmax(0,1fr)); gap: 10px; }
label { display: grid; gap: 6px; font-size: 13px; color: #6b7280; }
label.full { grid-column: 1 / -1; }
input,select { height: 38px; border: 1px solid #d1d5db; border-radius: 8px; padding: 0 10px; font-size: 14px; }
.save-btn { margin-top: 12px; width: 100%; height: 40px; border: none; border-radius: 8px; background: #2563eb; color: #fff; font-size: 14px; font-weight: 600; cursor: pointer; }
@media (max-width: 640px) { .summary-grid,.form-grid { grid-template-columns: 1fr; } label.full { grid-column: auto; } }
</style>
