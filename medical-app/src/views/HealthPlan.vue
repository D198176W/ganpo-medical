<template>
  <div class="health-plan-container">
    <!-- 顶部导航栏 -->
    <div class="nav-bar">
      <div class="nav-left">
        <div class="back-btn" @click="navigateBack">
          <i class="fas fa-arrow-left"></i>
        </div>
        <div class="title">我的健康计划</div>
      </div>
    </div>

    <!-- 内容区域 -->
    <div class="content">
      <!-- 计划卡片 -->
      <div class="plan-cards">
        <!-- 减重计划 -->
        <div class="plan-card weight-loss" @click="navigateToPlan('weight-loss')">
          <div class="plan-icon">
            <img src="@/assets/images/weight-loss.png" alt="减重计划">
          </div>
          <div class="plan-info">
            <h3>减重计划</h3>
            <div class="progress-info">
              <div class="progress-bar">
                <div class="progress-fill" :style="{ width: weightLossProgress + '%' }"></div>
              </div>
              <span class="progress-text">{{ weightLossProgress }}% 完成</span>
            </div>
            <p class="plan-goal">目标：3个月减重5kg</p>
          </div>
        </div>

        <!-- 跑步训练 -->
        <div class="plan-card running" @click="navigateToPlan('running')">
          <div class="plan-icon">
            <img src="@/assets/images/running.png" alt="跑步训练">
          </div>
          <div class="plan-info">
            <h3>跑步训练</h3>
            <div class="progress-info">
              <div class="progress-bar">
                <div class="progress-fill" :style="{ width: runningProgress + '%' }"></div>
              </div>
              <span class="progress-text">{{ runningProgress }}% 完成</span>
            </div>
            <p class="plan-goal">每周跑步3次，每次30分钟</p>
          </div>
        </div>

        <!-- 睡眠改善 -->
        <div class="plan-card sleep" @click="navigateToPlan('sleep')">
          <div class="plan-icon">
            <i class="fas fa-moon"></i>
          </div>
          <div class="plan-info">
            <h3>睡眠改善</h3>
            <div class="progress-info">
              <div class="progress-bar">
                <div class="progress-fill" :style="{ width: sleepProgress + '%' }"></div>
              </div>
              <span class="progress-text">{{ sleepProgress }}% 完成</span>
            </div>
            <p class="plan-goal">保证每晚7-8小时睡眠</p>
          </div>
        </div>
      </div>

      <!-- 今日提醒 -->
      <div class="today-reminders">
        <h3>今日提醒</h3>
        <div class="reminder-list">
          <div class="reminder-item" v-for="(reminder, index) in todayReminders" :key="index">
            <div class="reminder-time">{{ reminder.time }}</div>
            <div class="reminder-content">{{ reminder.content }}</div>
            <div class="reminder-action">
              <button class="complete-btn" @click="markAsCompleted(index)">
                {{ reminder.completed ? '已完成' : '完成' }}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'HealthPlan',
  data() {
    return {
      weightLossProgress: 60,
      runningProgress: 20,
      sleepProgress: 45,
      todayReminders: [
        {
          time: '08:00',
          content: '晨间慢跑30分钟',
          completed: false
        },
        {
          time: '12:00',
          content: '午餐控制热量摄入',
          completed: false
        },
        {
          time: '22:00',
          content: '准备睡觉，保证充足睡眠',
          completed: false
        }
      ]
    }
  },
  methods: {
    navigateBack() {
      this.$router.back()
    },
    navigateToPlan(planType) {
      this.$router.push(`/health/plan/${planType}`)
    },
    markAsCompleted(index) {
      this.todayReminders[index].completed = true
      // 这里可以添加保存逻辑
    }
  }
}
</script>

<style scoped>
.health-plan-container {
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

.plan-cards {
  display: flex;
  flex-direction: column;
  gap: 16px;
  margin-bottom: 30px;
}

.plan-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: transform 0.3s ease;
}

.plan-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.plan-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: white;
}

.plan-card.weight-loss .plan-icon {
  background-color: #52c41a;
}

.plan-card.running .plan-icon {
  background-color: #1890ff;
}

.plan-card.sleep .plan-icon {
  background-color: #722ed1;
}

.plan-icon img {
  width: 40px;
  height: 40px;
  object-fit: contain;
}

.plan-info {
  flex: 1;
}

.plan-info h3 {
  margin: 0 0 12px 0;
  font-size: 16px;
  font-weight: bold;
}

.progress-info {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;
}

.progress-bar {
  flex: 1;
  height: 8px;
  background-color: #e8e8e8;
  border-radius: 4px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  border-radius: 4px;
  transition: width 0.3s ease;
}

.plan-card.weight-loss .progress-fill {
  background-color: #52c41a;
}

.plan-card.running .progress-fill {
  background-color: #1890ff;
}

.plan-card.sleep .progress-fill {
  background-color: #722ed1;
}

.progress-text {
  font-size: 12px;
  color: #666;
  min-width: 80px;
}

.plan-goal {
  margin: 0;
  font-size: 12px;
  color: #999;
}

.today-reminders {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.today-reminders h3 {
  margin: 0 0 16px 0;
  font-size: 16px;
  font-weight: bold;
}

.reminder-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.reminder-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 12px;
  background-color: #fafafa;
  border-radius: 8px;
}

.reminder-time {
  font-size: 14px;
  font-weight: bold;
  min-width: 60px;
  color: #666;
}

.reminder-content {
  flex: 1;
  font-size: 14px;
}

.complete-btn {
  padding: 6px 12px;
  border: 1px solid #1890ff;
  border-radius: 4px;
  background: white;
  color: #1890ff;
  cursor: pointer;
  font-size: 12px;
  transition: all 0.3s ease;
}

.complete-btn:hover {
  background-color: #1890ff;
  color: white;
}

.reminder-item .complete-btn {
  background-color: #52c41a;
  border-color: #52c41a;
  color: white;
}
</style>