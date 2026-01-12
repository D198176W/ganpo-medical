<template>
  <div class="health-report-section">
    <h3 class="section-title">健康报告</h3>
    
    <!-- 周期切换 -->
    <div class="period-switch">
      <button
        v-for="period in periodOptions"
        :key="period.value"
        class="period-btn"
        :class="{ 'active': currentPeriod === period.value }"
        @click="switchPeriod(period.value)"
      >
        {{ period.label }}
      </button>
    </div>

    <!-- 睡眠分析 -->
    <div class="report-card">
      <span class="report-title">睡眠分析</span>
      <div class="report-chart">
        <div class="sleep-chart">
          <div class="chart-axis-y">
            <span v-for="label in sleepYAxisLabels" :key="label">{{ label }}</span>
          </div>
          <div class="chart-bars">
            <div
              v-for="(item, index) in chartData.sleepData"
              :key="index"
              class="chart-bar-item"
            >
              <div
                class="chart-bar"
                :style="{ height: `${calculateSleepBarHeight(item.value)}px` }"
              ></div>
              <span class="bar-value">{{ item.value }}h</span>
            </div>
          </div>
          <div class="chart-axis-x">
            <span v-for="day in weekDays" :key="day">{{ day }}</span>
          </div>
        </div>
      </div>
      <div class="report-analysis">
        <span>平均睡眠：{{ averageSleep }}小时 | 达标天数：{{ sleepTargetDays }}天</span>
        <span class="suggestion">{{ sleepSuggestion }}</span>
      </div>
    </div>

    <!-- 运动分析 -->
    <div class="report-card">
      <span class="report-title">运动分析</span>
      <div class="report-chart">
        <div class="exercise-chart">
          <div class="chart-axis-y">
            <span v-for="label in exerciseYAxisLabels" :key="label">{{ label }}</span>
          </div>
          <div class="chart-bars">
            <div
              v-for="(item, index) in chartData.exerciseData"
              :key="index"
              class="chart-bar-item"
            >
              <div
                class="chart-bar"
                :style="{ height: `${calculateExerciseBarHeight(item.value)}px` }"
              ></div>
              <span class="bar-value">{{ item.value }}m</span>
            </div>
          </div>
          <div class="chart-axis-x">
            <span v-for="day in weekDays" :key="day">{{ day }}</span>
          </div>
        </div>
      </div>
      <div class="report-analysis">
        <span>运动天数：{{ exerciseDays }}天 | 总时长：{{ totalExerciseMinutes }}分钟</span>
        <span class="suggestion">{{ exerciseSuggestion }}</span>
      </div>
    </div>

    <!-- 用药分析 -->
    <div class="report-card">
      <span class="report-title">用药分析</span>
      <div class="report-analysis medication-analysis">
        <span class="suggestion">{{ medicationSuggestion }}</span>
      </div>
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
  },
  period: {
    type: String,
    default: 'week',
    validator: (value) => ['week', 'month'].includes(value)
  }
})

// Emits
const emit = defineEmits(['period-changed'])

// 响应式数据
const currentPeriod = ref(props.period)
const chartData = ref({
  sleepData: [],
  exerciseData: []
})

// 常量数据
const weekDays = ['一', '二', '三', '四', '五', '六', '日']
const periodOptions = [
  { value: 'week', label: '周报告' },
  { value: 'month', label: '月报告' }
]
const sleepYAxisLabels = ['8h', '6h', '4h', '2h', '0h']
const exerciseYAxisLabels = ['120m', '90m', '60m', '30m', '0m']

// 计算属性
const averageSleep = computed(() => {
  if (chartData.value.sleepData.length === 0) return 0
  const total = chartData.value.sleepData.reduce((sum, item) => sum + item.value, 0)
  return (total / chartData.value.sleepData.length).toFixed(1)
})

const sleepTargetDays = computed(() => {
  return chartData.value.sleepData.filter(item => item.value >= 7).length
})

const exerciseDays = computed(() => {
  return chartData.value.exerciseData.filter(item => item.value > 0).length
})

const totalExerciseMinutes = computed(() => {
  return chartData.value.exerciseData.reduce((sum, item) => sum + item.value, 0)
})

const sleepSuggestion = computed(() => {
  const avgSleep = parseFloat(averageSleep.value)
  if (avgSleep >= 7) {
    return '本周睡眠质量良好，继续保持规律作息！'
  } else if (avgSleep >= 6) {
    return '睡眠时间略少，建议增加30分钟睡眠时间'
  } else {
    return '睡眠不足，请注意保证充足休息，避免熬夜'
  }
})

const exerciseSuggestion = computed(() => {
  const days = exerciseDays.value
  const total = totalExerciseMinutes.value
  
  if (days >= 5 && total >= 150) {
    return '本周运动表现优秀，继续保持！建议增加力量训练'
  } else if (days >= 3 && total >= 90) {
    return '本周运动达标，建议保持并适当增加运动频率'
  } else {
    return '运动量不足，建议每天至少运动30分钟'
  }
})

const medicationSuggestion = computed(() => {
  // 这里可以根据实际的用药记录数据生成建议
  return '用药记录规律，请注意按时服药'
})

// 方法
const switchPeriod = (period) => {
  currentPeriod.value = period
  emit('period-changed', period)
  generateChartData()
}

const calculateSleepBarHeight = (value) => {
  // 睡眠图表最大高度为120px，对应8小时
  const maxHeight = 120
  const maxValue = 8
  return (value / maxValue) * maxHeight
}

const calculateExerciseBarHeight = (value) => {
  // 运动图表最大高度为120px，对应120分钟
  const maxHeight = 120
  const maxValue = 120
  return (value / maxValue) * maxHeight
}

const generateChartData = () => {
  if (currentPeriod.value === 'week') {
    // 生成周数据（模拟数据，实际应该根据healthLogs计算）
    chartData.value.sleepData = [
      { label: '周一', value: 7 },
      { label: '周二', value: 6.5 },
      { label: '周三', value: 5 },
      { label: '周四', value: 7.5 },
      { label: '周五', value: 6 },
      { label: '周六', value: 8 },
      { label: '周日', value: 7 }
    ]
    
    chartData.value.exerciseData = [
      { label: '周一', value: 30 },
      { label: '周二', value: 0 },
      { label: '周三', value: 45 },
      { label: '周四', value: 0 },
      { label: '周五', value: 60 },
      { label: '周六', value: 45 },
      { label: '周日', value: 0 }
    ]
  } else {
    // 生成月数据（简化版，实际应该根据healthLogs计算）
    chartData.value.sleepData = Array.from({ length: 7 }, (_, i) => ({
      label: `第${i + 1}周`,
      value: 6 + Math.random() * 2
    }))
    
    chartData.value.exerciseData = Array.from({ length: 7 }, (_, i) => ({
      label: `第${i + 1}周`,
      value: Math.floor(Math.random() * 120)
    }))
  }
}

const analyzeHealthLogs = () => {
  // 这里可以根据实际的healthLogs数据进行分析
  // 目前使用模拟数据
  generateChartData()
}

// 生命周期
onMounted(() => {
  analyzeHealthLogs()
})

// 监听健康日志变化
watch(
  () => props.healthLogs,
  () => {
    analyzeHealthLogs()
  },
  { deep: true }
)

// 监听周期变化
watch(
  () => props.period,
  (newPeriod) => {
    currentPeriod.value = newPeriod
    analyzeHealthLogs()
  }
)
</script>

<style scoped>
.health-report-section {
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

/* 周期切换 */
.period-switch {
  display: flex;
  gap: 8px;
  margin-bottom: 16px;
}

.period-btn {
  flex: 1;
  height: 36px;
  font-size: 14px;
  border: none;
  background: #f5f5f5;
  color: #666;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.25s;
}

.period-btn:hover {
  transform: scale(1.05);
}

.period-btn.active {
  background: #1E88E5;
  color: #fff;
}

/* 报告卡片 */
.report-card {
  margin-bottom: 16px;
  padding: 12px;
  background: #f9f9f9;
  border-radius: 8px;
}

.report-title {
  font-size: 16px;
  color: #333;
  font-weight: 500;
  margin-bottom: 12px;
  display: block;
}

.report-chart {
  height: 120px;
  position: relative;
  margin-bottom: 12px;
}

/* 睡眠图表 */
.sleep-chart,
.exercise-chart {
  display: flex;
  align-items: flex-end;
  height: 100%;
  padding-left: 32px;
  box-sizing: border-box;
}

.chart-axis-y {
  position: absolute;
  left: 0;
  top: 0;
  bottom: 20px;
  width: 28px;
  display: flex;
  flex-direction: column-reverse;
  justify-content: space-between;
  font-size: 10px;
  color: #999;
}

.chart-bars {
  flex: 1;
  display: flex;
  align-items: flex-end;
  justify-content: space-around;
  height: calc(100% - 20px);
  padding-bottom: 20px;
}

.chart-bar-item {
  width: 10%;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.chart-bar {
  width: 100%;
  border-radius: 4px 4px 0 0;
  background: #4CAF50;
  transition: height 0.3s ease;
}

.exercise-chart .chart-bar {
  background: #1E88E5;
}

.bar-value {
  font-size: 10px;
  color: #666;
  margin-top: 4px;
}

.chart-axis-x {
  position: absolute;
  left: 32px;
  right: 0;
  bottom: 0;
  height: 20px;
  display: flex;
  justify-content: space-around;
  font-size: 10px;
  color: #999;
}

/* 分析文本 */
.report-analysis {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
}

.suggestion {
  color: #E53935;
  display: block;
  margin-top: 4px;
}

.medication-analysis {
  padding: 8px 0;
}

/* 图表动画 */
.chart-bar {
  animation: chartBarGrow 0.8s ease-out;
}

@keyframes chartBarGrow {
  from {
    height: 0;
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .health-report-section {
    padding: 12px;
    margin: 0 12px 12px;
  }
  
  .report-chart {
    height: 100px;
  }
  
  .chart-axis-y {
    font-size: 9px;
    width: 24px;
  }
  
  .chart-axis-x {
    font-size: 9px;
  }
  
  .bar-value {
    font-size: 9px;
  }
  
  .report-analysis {
    font-size: 13px;
  }
}

@media (max-width: 480px) {
  .period-switch {
    flex-direction: column;
    gap: 6px;
  }
  
  .period-btn {
    height: 32px;
    font-size: 13px;
  }
  
  .report-chart {
    height: 80px;
  }
  
  .sleep-chart,
  .exercise-chart {
    padding-left: 28px;
  }
  
  .chart-axis-y {
    width: 22px;
    font-size: 8px;
  }
  
  .chart-axis-x {
    font-size: 8px;
    left: 28px;
  }
  
  .bar-value {
    font-size: 8px;
  }
  
  .chart-bar-item {
    width: 12%;
  }
}

/* 深色模式支持 */
@media (prefers-color-scheme: dark) {
  .health-report-section {
    background: #2d2d2d;
    color: #e0e0e0;
  }
  
  .section-title {
    color: #e0e0e0;
  }
  
  .report-card {
    background: #3d3d3d;
  }
  
  .report-title {
    color: #e0e0e0;
  }
  
  .period-btn {
    background: #4d4d4d;
    color: #ccc;
  }
  
  .period-btn.active {
    background: #1E88E5;
    color: #fff;
  }
  
  .report-analysis {
    color: #ccc;
  }
  
  .chart-axis-y,
  .chart-axis-x,
  .bar-value {
    color: #999;
  }
}

/* 打印样式 */
@media print {
  .health-report-section {
    box-shadow: none;
    border: 1px solid #ddd;
  }
  
  .period-switch {
    display: none;
  }
  
  .report-card {
    break-inside: avoid;
  }
}
</style>