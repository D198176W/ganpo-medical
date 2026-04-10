<template>
  <div class="health-log-section">
    <h3 class="section-title">健康日志</h3>
    
    <!-- 日历部分 -->
    <div class="calendar-header">
      <span class="current-month">{{ displayMonth }}</span>
      <div class="calendar-actions">
        <button class="month-btn" @click="prevMonth">上月</button>
        <button class="month-btn" @click="nextMonth">下月</button>
      </div>
    </div>
    
    <div class="week-days">
      <span v-for="day in weekDays" :key="day">{{ day }}</span>
    </div>
    
    <div class="calendar-grid">
      <div
        v-for="(day, index) in calendarDays"
        :key="index"
        class="calendar-day"
        :class="{
          'today': day.isToday,
          'selected': day.dateStr === selectedDate,
          'has-data': day.hasData,
          'current-month': day.isCurrentMonth
        }"
        @click="selectDate(day.dateStr)"
      >
        <span class="day-number">{{ day.day }}</span>
        <div v-if="day.hasData" class="data-marker"></div>
      </div>
    </div>

    <!-- 日志编辑器 -->
    <div class="log-editor">
      <span class="editor-title">记录 {{ selectedDate }} 的健康情况</span>
      
      <!-- 作息记录 -->
      <div class="log-item">
        <span class="log-label">作息：</span>
        <div class="quick-options">
          <div
            v-for="option in sleepOptions"
            :key="option"
            class="quick-option"
            :class="{ 'selected': currentLog.sleep === option }"
            @click="selectQuickOption('sleep', option)"
          >
            {{ option }}
          </div>
        </div>
      </div>

      <!-- 运动记录 -->
      <div class="log-item">
        <span class="log-label">运动：</span>
        <div class="quick-options">
          <div
            v-for="option in exerciseOptions"
            :key="option"
            class="quick-option"
            :class="{ 'selected': currentLog.exercise === option }"
            @click="selectQuickOption('exercise', option)"
          >
            {{ option }}
          </div>
        </div>
      </div>

      <!-- 用药记录 -->
      <div class="log-item">
        <span class="log-label">用药：</span>
        <div class="quick-options">
          <div
            v-for="option in medicationOptions"
            :key="option"
            class="quick-option"
            :class="{ 'selected': currentLog.medication === option }"
            @click="selectQuickOption('medication', option)"
          >
            {{ option }}
          </div>
        </div>
      </div>

      <!-- 备注 -->
      <div class="log-item notes-item">
        <span class="log-label">备注：</span>
        <textarea
          class="notes-input"
          placeholder="添加备注信息..."
          :value="currentLog.notes"
          @input="updateNotes"
        ></textarea>
      </div>

      <button class="save-btn" @click="saveHealthLog">保存记录</button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'

// Props
const props = defineProps({
  healthLogs: {
    type: Object,
    default: () => ({})
  }
})

// Emits
const emit = defineEmits(['log-saved', 'update:healthLogs'])

// 响应式数据
const currentYear = ref(new Date().getFullYear())
const currentMonth = ref(new Date().getMonth() + 1)
const selectedDate = ref('')
const currentLog = ref({
  sleep: '',
  exercise: '',
  medication: '',
  notes: ''
})

// 常量数据
const weekDays = ['日', '一', '二', '三', '四', '五', '六']
const sleepOptions = ['早睡(22:00前)', '正常(23:00前)', '熬夜(00:00后)', '失眠']
const exerciseOptions = ['散步30分钟', '跑步30分钟', '健身1小时', '瑜伽40分钟', '无运动']
const medicationOptions = ['按时服药', '忘记服药', '无需服药']

// 计算属性
const displayMonth = computed(() => {
  return `${currentYear.value}年${currentMonth.value}月`
})

const calendarDays = computed(() => {
  const days = []
  const firstDay = new Date(currentYear.value, currentMonth.value - 1, 1).getDay()
  const daysInMonth = new Date(currentYear.value, currentMonth.value, 0).getDate()
  
  const today = new Date()
  const todayStr = formatDate(today)
  
  // 添加上个月的天数
  const prevMonthDays = new Date(currentYear.value, currentMonth.value - 1, 0).getDate()
  for (let i = firstDay - 1; i >= 0; i--) {
    const day = prevMonthDays - i
    const date = new Date(currentYear.value, currentMonth.value - 2, day)
    days.push({
      day,
      dateStr: formatDate(date),
      isToday: false,
      hasData: false,
      isCurrentMonth: false
    })
  }
  
  // 添加当前月的天数
  for (let day = 1; day <= daysInMonth; day++) {
    const dateStr = `${currentYear.value}-${String(currentMonth.value).padStart(2, '0')}-${String(day).padStart(2, '0')}`
    days.push({
      day,
      dateStr,
      isToday: dateStr === todayStr,
      hasData: !!props.healthLogs[dateStr],
      isCurrentMonth: true
    })
  }
  
  // 添加下个月的天数（补全42个格子）
  const totalCells = 42
  const nextMonthStartDay = 1
  for (let i = days.length; i < totalCells; i++) {
    const day = nextMonthStartDay + (i - days.length)
    const date = new Date(currentYear.value, currentMonth.value, day)
    days.push({
      day,
      dateStr: formatDate(date),
      isToday: false,
      hasData: false,
      isCurrentMonth: false
    })
  }
  
  return days
})

// 方法
const formatDate = (date) => {
  const y = date.getFullYear()
  const m = String(date.getMonth() + 1).padStart(2, '0')
  const d = String(date.getDate()).padStart(2, '0')
  return `${y}-${m}-${d}`
}

const prevMonth = () => {
  currentMonth.value--
  if (currentMonth.value < 1) {
    currentMonth.value = 12
    currentYear.value--
  }
}

const nextMonth = () => {
  currentMonth.value++
  if (currentMonth.value > 12) {
    currentMonth.value = 1
    currentYear.value++
  }
}

const selectDate = (dateStr) => {
  if (!dateStr) return
  
  selectedDate.value = dateStr
  
  // 加载选中日期的日志
  loadSelectedDateLog()
}

const selectQuickOption = (type, value) => {
  currentLog.value[type] = currentLog.value[type] === value ? '' : value
}

const updateNotes = (event) => {
  currentLog.value.notes = event.target.value
}

const loadSelectedDateLog = () => {
  const log = props.healthLogs[selectedDate.value] || {
    sleep: '',
    exercise: '',
    medication: '',
    notes: ''
  }
  currentLog.value = { ...log }
}

const saveHealthLog = () => {
  if (!selectedDate.value) {
    alert('请先选择日期')
    return
  }
  
  // 触发保存事件
  emit('log-saved', {
    date: selectedDate.value,
    log: { ...currentLog.value }
  })
  
  // 更新本地存储（模拟原HTML功能）
  const updatedLogs = {
    ...props.healthLogs,
    [selectedDate.value]: { ...currentLog.value }
  }
  localStorage.setItem('healthLogs', JSON.stringify(updatedLogs))
  
  // 通知父组件更新
  emit('update:healthLogs', updatedLogs)
  
  // 显示成功提示
  alert('保存成功')
}

const loadFromLocalStorage = () => {
  try {
    const logs = JSON.parse(localStorage.getItem('healthLogs') || '{}')
    emit('update:healthLogs', logs)
  } catch (error) {
    console.error('加载健康日志失败:', error)
  }
}

// 生命周期
onMounted(() => {
  // 初始化选中今天
  const today = new Date()
  selectedDate.value = formatDate(today)
  
  // 从localStorage加载数据
  loadFromLocalStorage()
  
  // 加载选中日期的日志
  loadSelectedDateLog()
})

// 监听健康日志变化
watch(
  () => props.healthLogs,
  () => {
    if (selectedDate.value) {
      loadSelectedDateLog()
    }
  },
  { deep: true }
)

// 监听月份变化，确保选中日期在当前月
watch(
  [currentYear, currentMonth],
  () => {
    const currentDate = new Date(selectedDate.value)
    const currentYearNum = currentDate.getFullYear()
    const currentMonthNum = currentDate.getMonth() + 1
    
    if (currentYearNum !== currentYear.value || currentMonthNum !== currentMonth.value) {
      // 如果选中日期不在当前月，选择今天
      const today = new Date()
      selectedDate.value = formatDate(today)
      loadSelectedDateLog()
    }
  }
)
</script>

<style scoped>
.health-log-section {
  margin: 12px;
  background: #fff;
  border-radius: 12px;
  padding: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.section-title {
  font-size: 18px;
  font-weight: 500;
  color: #333;
  margin-bottom: 16px;
  padding-left: 8px;
  position: relative;
}

.section-title::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 4px;
  height: 16px;
  background: #1E88E5;
  border-radius: 2px;
}

/* 日历头部 */
.calendar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.current-month {
  font-size: 16px;
  color: #333;
  font-weight: 500;
}

.calendar-actions {
  display: flex;
  gap: 8px;
}

.month-btn {
  height: 28px;
  padding: 0 12px;
  font-size: 12px;
  border: none;
  background: #f5f5f5;
  color: #666;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.25s;
}

.month-btn:hover {
  background: #e3e3e3;
  transform: scale(1.05);
}

/* 星期标题 */
.week-days {
  display: flex;
  margin-bottom: 8px;
}

.week-days span {
  flex: 1;
  text-align: center;
  font-size: 12px;
  color: #999;
  height: 24px;
  line-height: 24px;
}

/* 日历网格 */
.calendar-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 2px;
}

.calendar-day {
  width: calc(100% / 7);
  aspect-ratio: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  position: relative;
  cursor: pointer;
  border-radius: 8px;
  transition: background 0.25s, transform 0.25s;
}

.calendar-day:hover {
  background: #f1f1f1;
  transform: scale(1.08);
}

.calendar-day.today {
  background: #e3f2fd;
  color: #1E88E5;
  font-weight: 500;
}

.calendar-day.selected {
  background: #1E88E5;
  color: #fff;
}

.calendar-day.has-data .data-marker {
  position: absolute;
  bottom: 6px;
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: #E53935;
}

.calendar-day:not(.current-month) {
  color: #ccc;
  cursor: not-allowed;
}

.calendar-day:not(.current-month):hover {
  background: transparent;
  transform: none;
}

.day-number {
  font-size: 14px;
  font-weight: 400;
}

/* 日志编辑器 */
.log-editor {
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid #f1f1f1;
}

.editor-title {
  font-size: 14px;
  color: #666;
  margin-bottom: 12px;
  display: block;
}

.log-item {
  margin-bottom: 16px;
  display: flex;
  align-items: flex-start;
}

.log-label {
  width: 60px;
  font-size: 14px;
  color: #333;
  line-height: 32px;
}

.quick-options {
  flex: 1;
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.quick-option {
  padding: 0 12px;
  height: 32px;
  line-height: 32px;
  background: #f5f5f5;
  color: #666;
  font-size: 14px;
  border-radius: 16px;
  cursor: pointer;
  transition: all 0.25s;
}

.quick-option:hover {
  transform: scale(1.05);
}

.quick-option.selected {
  background: #1E88E5;
  color: #fff;
}

.notes-input {
  flex: 1;
  min-height: 80px;
  padding: 8px;
  background: #f5f5f5;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  color: #333;
  resize: none;
  font-family: inherit;
}

.notes-input:focus {
  outline: none;
  background: #f0f0f0;
}

.save-btn {
  width: 100%;
  height: 44px;
  background: #1E88E5;
  color: #fff;
  font-size: 16px;
  border: none;
  border-radius: 8px;
  margin-top: 12px;
  cursor: pointer;
  transition: all 0.25s;
}

.save-btn:hover {
  transform: scale(1.02);
  box-shadow: 0 4px 12px rgba(30, 136, 229, 0.3);
}

.save-btn:active {
  transform: scale(0.98);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .health-log-section {
    padding: 12px;
    margin: 0 12px 12px;
  }
  
  .calendar-day {
    font-size: 12px;
  }
  
  .quick-options {
    gap: 6px;
  }
  
  .quick-option {
    padding: 0 10px;
    height: 28px;
    line-height: 28px;
    font-size: 12px;
  }
  
  .log-label {
    width: 50px;
    font-size: 13px;
  }
}

@media (max-width: 480px) {
  .calendar-header {
    flex-direction: column;
    gap: 8px;
    align-items: stretch;
  }
  
  .calendar-actions {
    justify-content: center;
  }
  
  .month-btn {
    flex: 1;
  }
  
  .quick-options {
    flex-direction: column;
    gap: 6px;
  }
  
  .quick-option {
    text-align: center;
  }
  
  .log-item {
    flex-direction: column;
    gap: 8px;
  }
  
  .log-label {
    width: 100%;
    line-height: 1.4;
  }
  
  .notes-item {
    flex-direction: column;
  }
}
</style>