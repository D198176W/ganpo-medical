<template>
  <div class="sleep-plan-container">
    <!-- 顶部导航栏 -->
    <div class="nav-bar">
      <div class="nav-left">
        <div class="back-btn" @click="navigateBack">
          <i class="fas fa-arrow-left"></i>
        </div>
        <div class="title">睡眠改善</div>
      </div>
    </div>

    <!-- 内容区域 -->
    <div class="content">
      <!-- 睡眠概览 -->
      <div class="sleep-overview">
        <div class="overview-card">
          <div class="overview-item">
            <span class="label">平均睡眠时长</span>
            <span class="value">{{ averageSleepHours }} 小时</span>
          </div>
          <div class="overview-item">
            <span class="label">本周达标</span>
            <span class="value">{{ weeklyGoalMet }}/{{ weeklyGoal }} 天</span>
          </div>
          <div class="overview-item">
            <span class="label">睡眠质量</span>
            <span class="value">{{ sleepQuality }}</span>
          </div>
        </div>

        <!-- 睡眠目标 -->
        <div class="goal-section">
          <h3>睡眠目标</h3>
          <div class="goal-items">
            <div class="goal-item">
              <span class="goal-label">每晚睡眠</span>
              <span class="goal-value">{{ targetSleepHours }}-{{ targetSleepHours + 1 }} 小时</span>
            </div>
            <div class="goal-item">
              <span class="goal-label">入睡时间</span>
              <span class="goal-value">{{ targetBedtime }}</span>
            </div>
            <div class="goal-item">
              <span class="goal-label">起床时间</span>
              <span class="goal-value">{{ targetWakeTime }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 睡眠记录 -->
      <div class="sleep-records">
        <h3>睡眠记录</h3>
        <div class="record-list">
          <div class="record-item" v-for="(record, index) in sleepRecords" :key="index">
            <div class="record-date">{{ record.date }}</div>
            <div class="record-info">
              <div class="record-detail">
                <span class="label">入睡时间</span>
                <span class="value">{{ record.bedtime }}</span>
              </div>
              <div class="record-detail">
                <span class="label">起床时间</span>
                <span class="value">{{ record.wakeTime }}</span>
              </div>
              <div class="record-detail">
                <span class="label">睡眠时长</span>
                <span class="value">{{ record.duration }} 小时</span>
              </div>
              <div class="record-detail">
                <span class="label">质量</span>
                <span class="value" :class="record.quality">{{ record.quality }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 睡眠建议 -->
      <div class="sleep-tips">
        <h3>睡眠建议</h3>
        <div class="tip-list">
          <div class="tip-item" v-for="(tip, index) in sleepTips" :key="index">
            <div class="tip-icon">{{ tip.icon }}</div>
            <div class="tip-content">
              <h4>{{ tip.title }}</h4>
              <p>{{ tip.description }}</p>
            </div>
          </div>
        </div>
      </div>

      <!-- 睡眠习惯 -->
      <div class="sleep-habits">
        <h3>睡眠习惯</h3>
        <div class="habit-list">
          <div class="habit-item" v-for="(habit, index) in sleepHabits" :key="index">
            <div class="habit-info">
              <h4>{{ habit.title }}</h4>
              <p>{{ habit.description }}</p>
            </div>
            <div class="habit-status">
              <input type="checkbox" :checked="habit.followed" @change="toggleHabit(index)">
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'HealthPlanSleep',
  data() {
    return {
      averageSleepHours: 6.5,
      weeklyGoal: 7,
      weeklyGoalMet: 3,
      sleepQuality: '良好',
      targetSleepHours: 7,
      targetBedtime: '22:30',
      targetWakeTime: '06:30',
      sleepRecords: [
        {
          date: '2026-03-24',
          bedtime: '23:00',
          wakeTime: '06:30',
          duration: 7.5,
          quality: '良好'
        },
        {
          date: '2026-03-23',
          bedtime: '22:30',
          wakeTime: '06:00',
          duration: 7.5,
          quality: '良好'
        },
        {
          date: '2026-03-22',
          bedtime: '00:00',
          wakeTime: '06:30',
          duration: 6.5,
          quality: '一般'
        },
        {
          date: '2026-03-21',
          bedtime: '23:30',
          wakeTime: '07:00',
          duration: 7.5,
          quality: '良好'
        }
      ],
      sleepTips: [
        {
          icon: '🌙',
          title: '保持规律的作息时间',
          description: '每天固定时间上床睡觉和起床，包括周末'
        },
        {
          icon: '📵',
          title: '睡前避免屏幕时间',
          description: '睡前1小时避免使用手机、电脑等电子设备'
        },
        {
          icon: '🧘',
          title: '睡前放松活动',
          description: '可以进行冥想、深呼吸或温水浴来放松身心'
        },
        {
          icon: '🌡️',
          title: '创造舒适的睡眠环境',
          description: '保持房间温度适宜，光线柔和，噪音最小化'
        },
        {
          icon: '☕',
          title: '限制咖啡因摄入',
          description: '下午后避免饮用咖啡、茶和能量饮料'
        }
      ],
      sleepHabits: [
        {
          title: '睡前避免剧烈运动',
          description: '睡前2小时内避免剧烈运动',
          followed: true
        },
        {
          title: '保持卧室安静舒适',
          description: '使用窗帘、耳塞等辅助工具',
          followed: true
        },
        {
          title: '睡前避免大餐',
          description: '睡前3小时内避免进食过多',
          followed: false
        },
        {
          title: '建立睡前仪式',
          description: '如阅读、听轻音乐等',
          followed: true
        }
      ]
    }
  },
  methods: {
    navigateBack() {
      this.$router.back()
    },
    toggleHabit(index) {
      this.sleepHabits[index].followed = !this.sleepHabits[index].followed
      // 这里可以添加保存逻辑
    }
  }
}
</script>

<style scoped>
.sleep-plan-container {
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

.sleep-overview {
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
  color: #722ed1;
}

.goal-section {
  margin-top: 20px;
}

.goal-section h3 {
  margin: 0 0 12px 0;
  font-size: 14px;
  font-weight: bold;
  color: #333;
}

.goal-items {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.goal-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  background-color: #fafafa;
  border-radius: 6px;
}

.goal-label {
  font-size: 14px;
  color: #666;
}

.goal-value {
  font-size: 14px;
  font-weight: bold;
  color: #722ed1;
}

.sleep-records {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.sleep-records h3 {
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
  padding: 16px;
  background-color: #fafafa;
  border-radius: 8px;
}

.record-date {
  font-size: 14px;
  font-weight: bold;
  margin-bottom: 12px;
  color: #666;
}

.record-info {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
}

.record-detail {
  text-align: center;
}

.record-detail .label {
  display: block;
  font-size: 12px;
  color: #999;
  margin-bottom: 4px;
}

.record-detail .value {
  display: block;
  font-size: 14px;
  font-weight: bold;
  color: #333;
}

.record-detail .value.良好 {
  color: #52c41a;
}

.record-detail .value.一般 {
  color: #faad14;
}

.record-detail .value.较差 {
  color: #ff4d4f;
}

.sleep-tips {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.sleep-tips h3 {
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
  gap: 16px;
  padding: 12px;
  background-color: #fafafa;
  border-radius: 8px;
}

.tip-icon {
  font-size: 24px;
  min-width: 40px;
  text-align: center;
  margin-top: 4px;
}

.tip-content {
  flex: 1;
}

.tip-content h4 {
  margin: 0 0 4px 0;
  font-size: 14px;
  font-weight: bold;
}

.tip-content p {
  margin: 0;
  font-size: 12px;
  color: #666;
  line-height: 1.4;
}

.sleep-habits {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.sleep-habits h3 {
  margin: 0 0 16px 0;
  font-size: 16px;
  font-weight: bold;
}

.habit-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.habit-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px;
  background-color: #fafafa;
  border-radius: 8px;
}

.habit-info {
  flex: 1;
  margin-right: 16px;
}

.habit-info h4 {
  margin: 0 0 4px 0;
  font-size: 14px;
  font-weight: bold;
}

.habit-info p {
  margin: 0;
  font-size: 12px;
  color: #666;
}

.habit-status input[type="checkbox"] {
  width: 18px;
  height: 18px;
  cursor: pointer;
}
</style>