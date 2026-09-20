<template>
  <div class="health-reminders-container">
    <!-- 顶部导航栏 -->
    <div class="nav-bar">
      <div class="nav-left">
        <div class="back-btn" @click="navigateBack">
          <i class="fas fa-arrow-left"></i>
        </div>
        <div class="title">今日提醒</div>
      </div>
      <div class="nav-right">
        <button class="add-btn" @click="showAddReminderModal = true">
          <i class="fas fa-plus"></i>
        </button>
      </div>
    </div>

    <!-- 内容区域 -->
    <div class="content">
      <!-- 日期显示 -->
      <div class="date-section">
        <h2>{{ todayDate }}</h2>
        <p>{{ todayDay }}</p>
      </div>

      <!-- 提醒统计 -->
      <div class="stats-section">
        <div class="stat-item">
          <span class="stat-value">{{ totalReminders }}</span>
          <span class="stat-label">总提醒</span>
        </div>
        <div class="stat-item">
          <span class="stat-value completed">{{ completedReminders }}</span>
          <span class="stat-label">已完成</span>
        </div>
        <div class="stat-item">
          <span class="stat-value pending">{{ pendingReminders }}</span>
          <span class="stat-label">待完成</span>
        </div>
      </div>

      <!-- 提醒列表 -->
      <div class="reminders-section">
        <h3>今日提醒</h3>
        <div class="reminder-list">
          <div 
            v-for="reminder in sortedReminders" 
            :key="reminder.id"
            class="reminder-item"
            :class="{
              'completed': reminder.status === 'completed',
              'overdue': reminder.status === 'pending' && isOverdue(reminder.time)
            }"
          >
            <div class="reminder-time">{{ reminder.time }}</div>
            <div class="reminder-content">
              <h4>{{ reminder.title }}</h4>
              <p>{{ reminder.description }}</p>
              <div class="reminder-tags">
                <span 
                  v-for="tag in reminder.tags" 
                  :key="tag"
                  class="tag"
                  :class="tag"
                >
                  {{ getTagLabel(tag) }}
                </span>
              </div>
            </div>
            <div class="reminder-actions">
              <button 
                v-if="reminder.status === 'pending'"
                class="complete-btn"
                @click="markAsCompleted(reminder.id)"
              >
                完成
              </button>
              <button 
                v-else
                class="completed-btn"
                disabled
              >
                已完成
              </button>
              <button 
                class="later-btn"
                @click="snoozeReminder(reminder.id)"
              >
                稍后
              </button>
            </div>
          </div>
          
          <div v-if="sortedReminders.length === 0" class="empty-state">
            <div class="empty-icon">🔔</div>
            <div>暂无今日提醒</div>
            <div class="empty-hint">点击右上角添加新提醒</div>
          </div>
        </div>
      </div>

      <!-- 即将到来的提醒 -->
      <div class="upcoming-section" v-if="upcomingReminders.length > 0">
        <h3>即将到来</h3>
        <div class="reminder-list">
          <div 
            v-for="reminder in upcomingReminders" 
            :key="reminder.id"
            class="reminder-item upcoming"
          >
            <div class="reminder-time">{{ reminder.time }}</div>
            <div class="reminder-content">
              <h4>{{ reminder.title }}</h4>
              <p>{{ reminder.description }}</p>
            </div>
            <div class="reminder-actions">
              <button class="remind-btn" @click="remindNow(reminder.id)">
                立即提醒
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Toast 提示 -->
    <div v-if="showToast" class="toast" :class="toastType">
      {{ toastMessage }}
    </div>

    <!-- 添加提醒模态框 -->
    <div v-if="showAddReminderModal" class="modal-overlay">
      <div class="modal-content">
        <div class="modal-header">
          <h3>添加新提醒</h3>
          <button class="close-btn" @click="showAddReminderModal = false">
            <i class="fas fa-times"></i>
          </button>
        </div>
        <div class="modal-body">
          <div class="form-group">
            <label>标题</label>
            <input 
              type="text" 
              v-model="newReminder.title"
              placeholder="请输入提醒标题"
            >
          </div>
          <div class="form-group">
            <label>描述</label>
            <textarea 
              v-model="newReminder.description"
              placeholder="请输入提醒描述"
              rows="3"
            ></textarea>
          </div>
          <div class="form-group">
            <label>时间</label>
            <input 
              type="time" 
              v-model="newReminder.time"
            >
          </div>
          <div class="form-group">
            <label>标签</label>
            <div class="tag-selector">
              <label 
                v-for="tag in availableTags" 
                :key="tag.value"
                class="tag-option"
              >
                <input 
                  type="checkbox" 
                  :value="tag.value"
                  v-model="newReminder.tags"
                >
                <span>{{ tag.label }}</span>
              </label>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button class="cancel-btn" @click="showAddReminderModal = false">
            取消
          </button>
          <button 
            class="confirm-btn"
            @click="addReminder"
            :disabled="!newReminder.title || !newReminder.time"
          >
            添加
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import storage from '../utils/storage.js'

export default {
  name: 'HealthReminders',
  data() {
    return {
      showToast: false,
      toastMessage: '',
      toastType: 'info',
      showAddReminderModal: false,
      newReminder: {
        title: '',
        description: '',
        time: '',
        tags: []
      },
      availableTags: [
        { value: 'medication', label: '用药' },
        { value: 'exercise', label: '运动' },
        { value: 'health', label: '健康检测' },
        { value: 'diet', label: '饮食' },
        { value: 'other', label: '其他' }
      ],
      reminders: []
    }
  },
  mounted() {
    this.loadReminders()
  },
  computed: {
    todayDate() {
      const date = new Date()
      return date.toLocaleDateString('zh-CN', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
      })
    },
    todayDay() {
      const date = new Date()
      return date.toLocaleDateString('zh-CN', {
        weekday: 'long'
      })
    },
    totalReminders() {
      return this.reminders.length
    },
    completedReminders() {
      return this.reminders.filter(r => r.status === 'completed').length
    },
    pendingReminders() {
      return this.reminders.filter(r => r.status === 'pending').length
    },
    sortedReminders() {
      return [...this.reminders].sort((a, b) => {
        return a.time.localeCompare(b.time)
      })
    },
    upcomingReminders() {
      const now = new Date()
      const currentTime = now.getHours() * 60 + now.getMinutes()
      
      return this.reminders.filter(reminder => {
        const [hours, minutes] = reminder.time.split(':').map(Number)
        const reminderTime = hours * 60 + minutes
        return reminder.status === 'pending' && reminderTime > currentTime
      }).sort((a, b) => a.time.localeCompare(b.time))
    }
  },
  methods: {
    showtoast(message, type = 'info') {
      this.toastMessage = message
      this.toastType = type
      this.showToast = true
      setTimeout(() => {
        this.showToast = false
      }, 2000)
    },
    navigateBack() {
      this.$router.back()
      this.showtoast('返回上一页', 'info')
    },
    loadReminders() {
      try {
        const savedReminders = storage.get('health_reminders')
        if (savedReminders && Array.isArray(savedReminders)) {
          this.reminders = savedReminders
        } else {
          // 初始数据
          this.reminders = [
            {
              id: 1,
              title: '服用维生素',
              description: '每日维生素补充',
              time: '09:30',
              status: 'pending',
              tags: ['medication']
            },
            {
              id: 2,
              title: '午间散步',
              description: '步行15分钟促进消化',
              time: '12:30',
              status: 'pending',
              tags: ['exercise']
            },
            {
              id: 3,
              title: '血压测量',
              description: '每日血压监测',
              time: '20:00',
              status: 'pending',
              tags: ['health']
            },
            {
              id: 4,
              title: '晚餐准备',
              description: '准备健康晚餐',
              time: '18:00',
              status: 'pending',
              tags: ['diet']
            },
            {
              id: 5,
              title: '睡前放松',
              description: '睡前冥想10分钟',
              time: '22:00',
              status: 'pending',
              tags: ['health', 'other']
            }
          ]
          this.saveReminders()
        }
      } catch (error) {
        console.error('加载提醒失败:', error)
        // 使用默认数据
        this.reminders = [
          {
            id: 1,
            title: '服用维生素',
            description: '每日维生素补充',
            time: '09:30',
            status: 'pending',
            tags: ['medication']
          },
          {
            id: 2,
            title: '午间散步',
            description: '步行15分钟促进消化',
            time: '12:30',
            status: 'pending',
            tags: ['exercise']
          },
          {
            id: 3,
            title: '血压测量',
            description: '每日血压监测',
            time: '20:00',
            status: 'pending',
            tags: ['health']
          },
          {
            id: 4,
            title: '晚餐准备',
            description: '准备健康晚餐',
            time: '18:00',
            status: 'pending',
            tags: ['diet']
          },
          {
            id: 5,
            title: '睡前放松',
            description: '睡前冥想10分钟',
            time: '22:00',
            status: 'pending',
            tags: ['health', 'other']
          }
        ]
      }
    },
    saveReminders() {
      try {
        storage.set('health_reminders', this.reminders)
      } catch (error) {
        console.error('保存提醒失败:', error)
        // 存储失败时不影响功能，只是数据不会持久化
      }
    },
    markAsCompleted(id) {
      const reminder = this.reminders.find(r => r.id === id)
      if (reminder) {
        reminder.status = 'completed'
        this.saveReminders()
        this.showtoast(`已完成：${reminder.title}`, 'success')
      }
    },
    snoozeReminder(id) {
      const reminder = this.reminders.find(r => r.id === id)
      if (reminder) {
        // 实现稍后提醒逻辑，比如延迟 30 分钟
        const [hours, minutes] = reminder.time.split(':').map(Number)
        let newMinutes = minutes + 30
        let newHours = hours
        if (newMinutes >= 60) {
          newHours += Math.floor(newMinutes / 60)
          newMinutes = newMinutes % 60
        }
        if (newHours >= 24) {
          newHours = newHours % 24
        }
        reminder.time = `${String(newHours).padStart(2, '0')}:${String(newMinutes).padStart(2, '0')}`
        this.saveReminders()
        this.showtoast(`已延迟至 ${reminder.time}`, 'info')
      }
    },
    remindNow(id) {
      const reminder = this.reminders.find(r => r.id === id)
      if (reminder) {
        // 实现立即提醒逻辑
        this.showtoast(`提醒：${reminder.title}`, 'warning')
        // 可以添加通知或声音提醒
      }
    },
    isOverdue(time) {
      const now = new Date()
      const currentTime = now.getHours() * 60 + now.getMinutes()
      const [hours, minutes] = time.split(':').map(Number)
      const reminderTime = hours * 60 + minutes
      return reminderTime < currentTime
    },
    getTagLabel(tag) {
      const tagInfo = this.availableTags.find(t => t.value === tag)
      return tagInfo ? tagInfo.label : tag
    },
    addReminder() {
      if (this.newReminder.title && this.newReminder.time) {
        const newId = Math.max(...this.reminders.map(r => r.id), 0) + 1
        const newReminder = {
          id: newId,
          title: this.newReminder.title,
          description: this.newReminder.description,
          time: this.newReminder.time,
          status: 'pending',
          tags: this.newReminder.tags
        }
        this.reminders.push(newReminder)
        // 重置表单
        this.newReminder = {
          title: '',
          description: '',
          time: '',
          tags: []
        }
        this.showAddReminderModal = false
        this.saveReminders()
        this.showtoast(`已添加提醒：${newReminder.title}`, 'success')
      } else {
        this.showtoast('请填写完整的提醒信息', 'warning')
      }
    }
  }
}
</script>

<style scoped>
/* Toast 提示样式 */
.toast {
  position: fixed;
  top: 80px;
  left: 50%;
  transform: translateX(-50%);
  padding: 12px 24px;
  border-radius: 8px;
  color: white;
  font-size: 14px;
  font-weight: 500;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  z-index: 1001;
  animation: slideDown 0.3s ease;
}

.toast.info {
  background-color: #1890ff;
}

.toast.success {
  background-color: #52c41a;
}

.toast.warning {
  background-color: #faad14;
}

.toast.error {
  background-color: #ff4d4f;
}

@keyframes slideDown {
  from {
    top: 60px;
    opacity: 0;
  }
  to {
    top: 80px;
    opacity: 1;
  }
}

.health-reminders-container {
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

.nav-right .add-btn {
  background: rgba(255, 255, 255, 0.2);
  border: none;
  border-radius: 50%;
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  cursor: pointer;
  font-size: 16px;
  transition: all 0.3s ease;
}

.nav-right .add-btn:hover {
  background: rgba(255, 255, 255, 0.3);
}

.content {
  padding: 20px;
}

.date-section {
  text-align: center;
  margin-bottom: 20px;
}

.date-section h2 {
  margin: 0 0 8px 0;
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.date-section p {
  margin: 0;
  font-size: 14px;
  color: #666;
}

.stats-section {
  display: flex;
  justify-content: space-around;
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.stat-item {
  text-align: center;
}

.stat-value {
  display: block;
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 4px;
  color: #333;
}

.stat-value.completed {
  color: #52c41a;
}

.stat-value.pending {
  color: #faad14;
}

.stat-label {
  font-size: 12px;
  color: #666;
}

.reminders-section {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.reminders-section h3 {
  margin: 0 0 16px 0;
  font-size: 16px;
  font-weight: bold;
  color: #333;
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
  padding: 16px;
  background-color: #fafafa;
  border-radius: 8px;
  border-left: 4px solid #1890ff;
  transition: all 0.3s ease;
  cursor: pointer;
  user-select: none;
}

.reminder-item:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transform: translateY(-2px);
}

.reminder-item:active {
  transform: translateY(0);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.reminder-item.completed {
  background-color: #f6ffed;
  border-left-color: #52c41a;
  opacity: 0.8;
}

.reminder-item.overdue {
  background-color: #fff2f0;
  border-left-color: #ff4d4f;
}

.reminder-item.upcoming {
  border-left-color: #faad14;
}

.reminder-time {
  font-size: 14px;
  font-weight: bold;
  min-width: 60px;
  color: #666;
}

.reminder-content {
  flex: 1;
}

.reminder-content h4 {
  margin: 0 0 4px 0;
  font-size: 14px;
  font-weight: bold;
  color: #333;
}

.reminder-content p {
  margin: 0 0 8px 0;
  font-size: 12px;
  color: #666;
}

.reminder-tags {
  display: flex;
  gap: 6px;
  flex-wrap: wrap;
}

.tag {
  padding: 2px 8px;
  border-radius: 12px;
  font-size: 10px;
  font-weight: bold;
  color: white;
}

.tag.medication {
  background-color: #1890ff;
}

.tag.exercise {
  background-color: #52c41a;
}

.tag.health {
  background-color: #faad14;
}

.tag.diet {
  background-color: #722ed1;
}

.tag.other {
  background-color: #999;
}

.reminder-actions {
  display: flex;
  gap: 8px;
  flex-shrink: 0;
}

.complete-btn {
  padding: 6px 16px;
  background-color: #52c41a;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 2px 4px rgba(82, 196, 26, 0.2);
}

.complete-btn:hover {
  background-color: #389e0d;
  transform: translateY(-1px);
  box-shadow: 0 4px 8px rgba(82, 196, 26, 0.3);
}

.complete-btn:active {
  transform: translateY(0);
}

.completed-btn {
  padding: 6px 16px;
  background-color: #f0f0f0;
  color: #999;
  border: none;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
  cursor: not-allowed;
}

.later-btn {
  padding: 6px 16px;
  background-color: white;
  color: #1890ff;
  border: 1px solid #1890ff;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.later-btn:hover {
  background-color: #e6f7ff;
  transform: translateY(-1px);
  box-shadow: 0 2px 4px rgba(24, 144, 255, 0.2);
}

.later-btn:active {
  transform: translateY(0);
}

.remind-btn {
  padding: 6px 16px;
  background-color: #faad14;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 2px 4px rgba(250, 173, 20, 0.2);
}

.remind-btn:hover {
  background-color: #d48806;
  transform: translateY(-1px);
  box-shadow: 0 4px 8px rgba(250, 173, 20, 0.3);
}

.remind-btn:active {
  transform: translateY(0);
}

.empty-state {
  text-align: center;
  padding: 40px 20px;
  color: #999;
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 16px;
}

.empty-hint {
  font-size: 12px;
  margin-top: 8px;
  color: #999;
}

.upcoming-section {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.upcoming-section h3 {
  margin: 0 0 16px 0;
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

/* 模态框样式 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  border-radius: 12px;
  width: 90%;
  max-width: 500px;
  max-height: 80vh;
  overflow-y: auto;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.modal-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: bold;
}

.close-btn {
  background: none;
  border: none;
  font-size: 18px;
  cursor: pointer;
  color: #999;
}

.modal-body {
  padding: 20px;
}

.form-group {
  margin-bottom: 16px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-size: 14px;
  font-weight: bold;
  color: #333;
}

.form-group input,
.form-group textarea {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  font-size: 14px;
  box-sizing: border-box;
}

.form-group textarea {
  resize: vertical;
}

.tag-selector {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.tag-option {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  cursor: pointer;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 20px;
  border-top: 1px solid #f0f0f0;
}

.cancel-btn {
  padding: 8px 16px;
  background: white;
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.cancel-btn:hover {
  border-color: #1890ff;
  color: #1890ff;
}

.confirm-btn {
  padding: 8px 16px;
  background-color: #1890ff;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.confirm-btn:hover {
  background-color: #40a9ff;
}

.confirm-btn:disabled {
  background-color: #d9d9d9;
  cursor: not-allowed;
}
</style>