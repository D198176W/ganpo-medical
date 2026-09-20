<template>
  <div class="weight-loss-plan-container">
    <!-- 顶部导航栏 -->
    <div class="nav-bar">
      <div class="nav-left">
        <div class="back-btn" @click="navigateBack">
          <i class="fas fa-arrow-left"></i>
        </div>
        <div class="title">减重计划</div>
      </div>
    </div>

    <!-- 内容区域 -->
    <div class="content">
      <!-- 计划概览 -->
      <div class="plan-overview">
        <div class="overview-card">
          <div class="overview-item">
            <span class="label">目标体重</span>
            <span class="value">{{ targetWeight }} kg</span>
          </div>
          <div class="overview-item">
            <span class="label">当前体重</span>
            <span class="value">{{ currentWeight }} kg</span>
          </div>
          <div class="overview-item">
            <span class="label">已减体重</span>
            <span class="value">{{ weightLost }} kg</span>
          </div>
        </div>

        <!-- 进度条 -->
        <div class="progress-section">
          <div class="progress-header">
            <span>减重进度</span>
            <span>{{ progressPercentage }}%</span>
          </div>
          <div class="progress-bar">
            <div class="progress-fill" :style="{ width: progressPercentage + '%' }"></div>
          </div>
          <div class="progress-details">
            <span>开始日期: {{ startDate }}</span>
            <span>预计完成: {{ endDate }}</span>
          </div>
        </div>
      </div>

      <!-- 每日目标 -->
      <div class="daily-goals">
        <h3>每日目标</h3>
        <div class="goal-list">
          <div class="goal-item" v-for="(goal, index) in dailyGoals" :key="index">
            <div class="goal-icon">{{ goal.icon }}</div>
            <div class="goal-info">
              <h4>{{ goal.title }}</h4>
              <p>{{ goal.description }}</p>
            </div>
            <div class="goal-status">
              <input type="checkbox" :checked="goal.completed" @change="toggleGoal(index)">
            </div>
          </div>
        </div>
      </div>

      <!-- 饮食建议 -->
      <div class="diet-tips">
        <h3>饮食建议</h3>
        <div class="tip-list">
          <div class="tip-item" v-for="(tip, index) in dietTips" :key="index">
            <div class="tip-number">{{ index + 1 }}</div>
            <div class="tip-content">{{ tip }}</div>
          </div>
        </div>
      </div>

      <!-- 体重记录 -->
      <div class="weight-records">
        <h3>体重记录</h3>
        <div class="record-list">
          <div class="record-item" v-for="(record, index) in weightRecords" :key="index">
            <div class="record-date">{{ record.date }}</div>
            <div class="record-weight">{{ record.weight }} kg</div>
            <div class="record-change" :class="{ positive: record.change > 0, negative: record.change < 0 }">
              {{ record.change > 0 ? '+' : '' }}{{ record.change }} kg
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'HealthPlanWeightLoss',
  data() {
    return {
      targetWeight: 60,
      currentWeight: 65.2,
      weightLost: 2.8,
      progressPercentage: 60,
      startDate: '2026-03-01',
      endDate: '2026-06-01',
      dailyGoals: [
        {
          icon: '🍎',
          title: '控制饮食',
          description: '每日热量摄入不超过1800卡路里',
          completed: false
        },
        {
          icon: '🏃',
          title: '有氧运动',
          description: '每天至少30分钟有氧运动',
          completed: false
        },
        {
          icon: '💧',
          title: '多喝水',
          description: '每天喝够2000ml水',
          completed: false
        },
        {
          icon: '😴',
          title: '充足睡眠',
          description: '保证7-8小时睡眠',
          completed: false
        }
      ],
      dietTips: [
        '早餐要营养均衡，包含蛋白质、碳水和纤维',
        '午餐控制主食量，多吃蔬菜和瘦肉',
        '晚餐尽量清淡，避免油腻食物',
        '避免零食和含糖饮料',
        '细嚼慢咽，吃到7分饱',
        '增加膳食纤维摄入，促进肠道健康'
      ],
      weightRecords: [
        { date: '2026-03-01', weight: 68.0, change: 0 },
        { date: '2026-03-08', weight: 67.2, change: -0.8 },
        { date: '2026-03-15', weight: 66.5, change: -0.7 },
        { date: '2026-03-22', weight: 65.8, change: -0.7 },
        { date: '2026-03-29', weight: 65.2, change: -0.6 }
      ]
    }
  },
  methods: {
    navigateBack() {
      this.$router.back()
    },
    toggleGoal(index) {
      this.dailyGoals[index].completed = !this.dailyGoals[index].completed
      // 这里可以添加保存逻辑
    }
  }
}
</script>

<style scoped>
.weight-loss-plan-container {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.nav-bar {
  background-color: #1890ff;
  color: white;
  padding: 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.nav-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.back-btn {
  cursor: pointer;
  font-size: 18px;
}

.title {
  font-size: 18px;
  font-weight: bold;
}

.content {
  padding: 20px;
}

.plan-overview {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.overview-card {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
  margin-bottom: 20px;
}

.overview-item {
  text-align: center;
  padding: 12px;
  background-color: #fafafa;
  border-radius: 8px;
}

.overview-item .label {
  display: block;
  font-size: 12px;
  color: #666;
  margin-bottom: 4px;
}

.overview-item .value {
  display: block;
  font-size: 18px;
  font-weight: bold;
  color: #52c41a;
}

.progress-section {
  margin-top: 20px;
}

.progress-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
  font-size: 14px;
  font-weight: bold;
}

.progress-bar {
  height: 12px;
  background-color: #e8e8e8;
  border-radius: 6px;
  overflow: hidden;
  margin-bottom: 8px;
}

.progress-fill {
  height: 100%;
  background-color: #52c41a;
  border-radius: 6px;
  transition: width 0.3s ease;
}

.progress-details {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #999;
}

.daily-goals {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.daily-goals h3 {
  margin: 0 0 16px 0;
  font-size: 16px;
  font-weight: bold;
}

.goal-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.goal-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 12px;
  background-color: #fafafa;
  border-radius: 8px;
}

.goal-icon {
  font-size: 24px;
  min-width: 40px;
  text-align: center;
}

.goal-info {
  flex: 1;
}

.goal-info h4 {
  margin: 0 0 4px 0;
  font-size: 14px;
  font-weight: bold;
}

.goal-info p {
  margin: 0;
  font-size: 12px;
  color: #666;
}

.goal-status input[type="checkbox"] {
  width: 18px;
  height: 18px;
  cursor: pointer;
}

.diet-tips {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.diet-tips h3 {
  margin: 0 0 16px 0;
  font-size: 16px;
  font-weight: bold;
}

.tip-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.tip-item {
  display: flex;
  align-items: flex-start;
  gap: 12px;
}

.tip-number {
  width: 24px;
  height: 24px;
  background-color: #52c41a;
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: bold;
  flex-shrink: 0;
  margin-top: 2px;
}

.tip-content {
  flex: 1;
  font-size: 14px;
  line-height: 1.4;
  color: #333;
}

.weight-records {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.weight-records h3 {
  margin: 0 0 16px 0;
  font-size: 16px;
  font-weight: bold;
}

.record-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.record-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px;
  background-color: #fafafa;
  border-radius: 8px;
}

.record-date {
  font-size: 14px;
  color: #666;
}

.record-weight {
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.record-change {
  font-size: 14px;
  font-weight: bold;
}

.record-change.positive {
  color: #ff4d4f;
}

.record-change.negative {
  color: #52c41a;
}
</style>