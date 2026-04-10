<template>
  <div class="health-reminder-container">
    <!-- 顶部导航 -->
    <div class="header">
      <div class="nav-left">
        <button class="back-btn" @click="goBack">
          <i class="fa fa-arrow-left"></i>
        </button>
        <h1 class="page-title">健康提醒</h1>
      </div>
      <button class="add-btn">
        <i class="fa fa-plus"></i> 添加提醒
      </button>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-cards">
      <div class="stat-card">
        <div class="stat-number">5</div>
        <div class="stat-label">总提醒</div>
      </div>
      <div class="stat-card">
        <div class="stat-number">3</div>
        <div class="stat-label">今日提醒</div>
      </div>
      <div class="stat-card">
        <div class="stat-number">2</div>
        <div class="stat-label">即将到期</div>
      </div>
    </div>

    <!-- 分类导航 -->
    <div class="category-nav">
      <div class="nav-item active">全部提醒</div>
      <div class="nav-item">用药提醒</div>
      <div class="nav-item">预约提醒</div>
      <div class="nav-item">疫苗接种</div>
      <div class="nav-item">体检提醒</div>
    </div>

    <!-- 今日提醒 -->
    <div class="section-title">今日提醒</div>
    <div class="reminders-list">
      <div class="reminder-card">
        <div class="card-header">
          <div class="card-icon">
            <i class="fa fa-calendar"></i>
          </div>
          <div class="card-content">
            <div class="card-title">开始复诊</div>
            <div class="card-desc">南昌大学第一附属医院</div>
          </div>
          <div class="card-badge">预约提醒</div>
        </div>
        <div class="card-footer">
          <div class="time-info">
            <i class="fa fa-clock-o"></i>
            <span class="time">今天 14:00</span>
            <span class="countdown">19小时后</span>
          </div>
          <div class="card-actions">
            <button class="action-btn secondary">导航</button>
            <button class="action-btn primary">联系医院</button>
          </div>
        </div>
      </div>

      <div class="reminder-card">
        <div class="card-header">
          <div class="card-icon">
            <i class="fa fa-stethoscope"></i>
          </div>
          <div class="card-content">
            <div class="card-title">年度体检</div>
            <div class="card-desc">空腹前往，携带身份证</div>
          </div>
          <div class="card-badge">体检提醒</div>
        </div>
        <div class="card-footer">
          <div class="time-info">
            <i class="fa fa-clock-o"></i>
            <span class="time">今天 08:30</span>
            <span class="countdown">7小时后</span>
          </div>
          <div class="card-actions">
            <button class="action-btn secondary">检查项</button>
            <button class="action-btn primary">已完成</button>
          </div>
        </div>
      </div>
    </div>

    <!-- 即将到期 -->
    <div class="section-title">即将到期</div>
    <div class="reminders-list">
      <div class="reminder-card">
        <div class="card-header">
          <div class="card-icon">
            <i class="fa fa-syringe"></i>
          </div>
          <div class="card-content">
            <div class="card-title">HPV疫苗第二剂</div>
            <div class="card-desc">南昌市疾控中心</div>
          </div>
          <div class="card-badge">疫苗接种</div>
        </div>
        <div class="card-footer">
          <div class="time-info">
            <i class="fa fa-clock-o"></i>
            <span class="time">明天 09:00</span>
            <span class="countdown">1天后</span>
          </div>
          <div class="card-actions">
            <button class="action-btn secondary">调整时间</button>
            <button class="action-btn primary">确认提醒</button>
          </div>
        </div>
      </div>

      <div class="reminder-card">
        <div class="card-header">
          <div class="card-icon">
            <i class="fa fa-medkit"></i>
          </div>
          <div class="card-content">
            <div class="card-title">感冒药服用</div>
            <div class="card-desc">每日两次，早晚各一次</div>
          </div>
          <div class="card-badge">用药提醒</div>
        </div>
        <div class="card-footer">
          <div class="time-info">
            <i class="fa fa-clock-o"></i>
            <span class="time">明天 08:00</span>
            <span class="countdown">1天后</span>
          </div>
          <div class="card-actions">
            <button class="action-btn secondary">删除</button>
            <button class="action-btn primary">编辑</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script >
import router from '@/router';

export default {
  name: 'HealthReminder',
  data() {
    return {
      // 这里可以添加响应式数据
    }
  },
  methods:{
    goBack(){
      router.push('/')
    }
  }
}
</script>

<style scoped>
.health-reminder-container {
  padding: 16px;
  background-color: #f5f5f5;
  min-height: 100vh;
}

/* 头部导航 */
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.nav-left {
  display: flex;
  align-items: center;
}

.back-btn {
  background: none;
  border: none;
  font-size: 18px;
  margin-right: 12px;
  color: #333;
  cursor: pointer;
}

.page-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.add-btn {
  background-color: #4a90e2;
  color: white;
  border: none;
  border-radius: 20px;
  padding: 8px 16px;
  font-size: 14px;
  cursor: pointer;
}

/* 统计卡片 */
.stats-cards {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.stat-card {
  flex: 1;
  background-color: white;
  border-radius: 12px;
  padding: 16px;
  text-align: center;
  margin: 0 6px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.stat-number {
  font-size: 24px;
  font-weight: 600;
  color: #333;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 12px;
  color: #999;
}

/* 分类导航 */
.category-nav {
  display: flex;
  overflow-x: auto;
  margin-bottom: 20px;
  padding-bottom: 8px;
}

.nav-item {
  white-space: nowrap;
  padding: 8px 16px;
  margin-right: 8px;
  border-radius: 16px;
  background-color: #f0f0f0;
  color: #666;
  font-size: 14px;
  cursor: pointer;
}

.nav-item.active {
  background-color: #e3f2fd;
  color: #1976d2;
}

/* 部分标题 */
.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin: 16px 0 12px;
}

/* 提醒卡片 */
.reminders-list {
  margin-bottom: 20px;
}

.reminder-card {
  background-color: white;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.card-header {
  display: flex;
  align-items: flex-start;
  margin-bottom: 12px;
}

.card-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: #e3f2fd;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  color: #1976d2;
}

.card-content {
  flex: 1;
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 4px;
}

.card-desc {
  font-size: 14px;
  color: #666;
}

.card-badge {
  font-size: 12px;
  background-color: #e3f2fd;
  color: #1976d2;
  padding: 4px 8px;
  border-radius: 12px;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 12px;
  border-top: 1px solid #f0f0f0;
}

.time-info {
  display: flex;
  align-items: center;
  font-size: 14px;
  color: #666;
}

.time-info i {
  margin-right: 6px;
}

.time {
  margin-right: 8px;
}

.countdown {
  color: #ff9800;
  font-size: 12px;
}

.card-actions {
  display: flex;
}

.action-btn {
  padding: 6px 12px;
  border-radius: 16px;
  font-size: 12px;
  cursor: pointer;
  margin-left: 8px;
}

.action-btn.primary {
  background-color: #4a90e2;
  color: white;
  border: none;
}

.action-btn.secondary {
  background-color: #f5f5f5;
  color: #666;
  border: 1px solid #e0e0e0;
}
</style>