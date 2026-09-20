<template>
  <div class="running-plan-container">
    <!-- 顶部导航栏 -->
    <div class="nav-bar">
      <div class="nav-left">
        <div class="back-btn" @click="navigateBack">
          <i class="fas fa-arrow-left"></i>
        </div>
        <div class="title">跑步训练</div>
      </div>
    </div>

    <!-- 内容区域 -->
    <div class="content">
      <!-- 计划概览 -->
      <div class="plan-overview">
        <div class="overview-card">
          <div class="overview-item">
            <span class="label">本周目标</span>
            <span class="value">{{ weeklyGoal }} 次</span>
          </div>
          <div class="overview-item">
            <span class="label">已完成</span>
            <span class="value">{{ completedThisWeek }} 次</span>
          </div>
          <div class="overview-item">
            <span class="label">总距离</span>
            <span class="value">{{ totalDistance }} km</span>
          </div>
        </div>

        <!-- 进度条 -->
        <div class="progress-section">
          <div class="progress-header">
            <span>周进度</span>
            <span>{{ weeklyProgress }}%</span>
          </div>
          <div class="progress-bar">
            <div class="progress-fill" :style="{ width: weeklyProgress + '%' }"></div>
          </div>
          <div class="progress-details">
            <span>本周剩余: {{ remainingDays }} 天</span>
            <span>还需: {{ remainingRuns }} 次</span>
          </div>
        </div>
      </div>

      <!-- 训练计划 -->
      <div class="training-plan">
        <h3>本周训练计划</h3>
        <div class="plan-list">
          <div class="plan-item" v-for="(plan, index) in weeklyPlans" :key="index">
            <div class="plan-date">{{ plan.date }}</div>
            <div class="plan-info">
              <h4>{{ plan.type }}</h4>
              <p>{{ plan.duration }}分钟 / {{ plan.distance }}km</p>
            </div>
            <div class="plan-status">
              <button class="status-btn" :class="plan.status" @click="toggleStatus(index)">
                {{ plan.status === 'completed' ? '已完成' : '标记完成' }}
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- 跑步记录 -->
      <div class="running-records">
        <h3>跑步记录</h3>
        <div class="record-list">
          <div class="record-item" v-for="(record, index) in runningRecords" :key="index">
            <div class="record-date">{{ record.date }}</div>
            <div class="record-info">
              <div class="record-detail">
                <span class="label">距离</span>
                <span class="value">{{ record.distance }} km</span>
              </div>
              <div class="record-detail">
                <span class="label">时长</span>
                <span class="value">{{ record.duration }} 分钟</span>
              </div>
              <div class="record-detail">
                <span class="label">配速</span>
                <span class="value">{{ record.pace }} min/km</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 跑步技巧 -->
      <div class="running-tips">
        <h3>跑步技巧</h3>
        <div class="tip-list">
          <div class="tip-item" v-for="(tip, index) in runningTips" :key="index">
            <div class="tip-icon">{{ tip.icon }}</div>
            <div class="tip-content">
              <h4>{{ tip.title }}</h4>
              <p>{{ tip.description }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'HealthPlanRunning',
  data() {
    return {
      weeklyGoal: 3,
      completedThisWeek: 1,
      totalDistance: 8.5,
      weeklyProgress: 20,
      remainingDays: 5,
      remainingRuns: 2,
      weeklyPlans: [
        {
          date: '周一',
          type: '轻松跑',
          duration: 30,
          distance: 3,
          status: 'completed'
        },
        {
          date: '周三',
          type: '间歇跑',
          duration: 40,
          distance: 4,
          status: 'pending'
        },
        {
          date: '周六',
          type: '长距离跑',
          duration: 60,
          distance: 6,
          status: 'pending'
        }
      ],
      runningRecords: [
        {
          date: '2026-03-24',
          distance: 3.5,
          duration: 32,
          pace: '9:09'
        },
        {
          date: '2026-03-21',
          distance: 5.0,
          duration: 45,
          pace: '9:00'
        }
      ],
      runningTips: [
        {
          icon: '👟',
          title: '选择合适的跑鞋',
          description: '选择适合自己脚型和跑步风格的专业跑鞋，减少受伤风险'
        },
        {
          icon: '🏃',
          title: '正确的跑步姿势',
          description: '保持身体正直，手臂自然摆动，步伐轻盈，着地时膝盖微屈'
        },
        {
          icon: '💧',
          title: '合理补水',
          description: '跑步前、中、后都要适当补水，保持身体水分平衡'
        },
        {
          icon: '🌡️',
          title: '逐渐增加强度',
          description: '不要急于增加跑量和强度，给身体足够的适应时间'
        }
      ]
    }
  },
  methods: {
    navigateBack() {
      this.$router.back()
    },
    toggleStatus(index) {
      const plan = this.weeklyPlans[index]
      if (plan.status === 'completed') {
        plan.status = 'pending'
        this.completedThisWeek--
      } else {
        plan.status = 'completed'
        this.completedThisWeek++
      }
      this.weeklyProgress = Math.round((this.completedThisWeek / this.weeklyGoal) * 100)
      this.remainingRuns = this.weeklyGoal - this.completedThisWeek
    }
  }
}
</script>

<style scoped>
.running-plan-container {
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
  color: #1890ff;
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
  background-color: #1890ff;
  border-radius: 6px;
  transition: width 0.3s ease;
}

.progress-details {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #999;
}

.training-plan {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.training-plan h3 {
  margin: 0 0 16px 0;
  font-size: 16px;
  font-weight: bold;
}

.plan-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.plan-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px;
  background-color: #fafafa;
  border-radius: 8px;
}

.plan-date {
  font-size: 14px;
  font-weight: bold;
  min-width: 60px;
  color: #666;
}

.plan-info {
  flex: 1;
}

.plan-info h4 {
  margin: 0 0 4px 0;
  font-size: 14px;
  font-weight: bold;
}

.plan-info p {
  margin: 0;
  font-size: 12px;
  color: #999;
}

.status-btn {
  padding: 6px 12px;
  border: 1px solid #1890ff;
  border-radius: 4px;
  background: white;
  color: #1890ff;
  cursor: pointer;
  font-size: 12px;
  transition: all 0.3s ease;
}

.status-btn:hover {
  background-color: #1890ff;
  color: white;
}

.status-btn.completed {
  background-color: #52c41a;
  border-color: #52c41a;
  color: white;
}

.running-records {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.running-records h3 {
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
  grid-template-columns: repeat(3, 1fr);
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

.running-tips {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.running-tips h3 {
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
</style>