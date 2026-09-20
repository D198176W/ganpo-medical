<template>
  <div class="health-reminder-container">
    <!-- 顶部导航 -->
    <div class="header">
      <div class="nav-left">
        <button class="back-btn" @click="goBack">
          <i class="fa fa-arrow-left"></i>
        </button>
        <div class="header-info">
          <h1 class="page-title">健康提醒</h1>
          <p class="subtitle">{{ getCurrentDate() }} · 星期{{ getWeekDay() }}</p>
        </div>
      </div>
      <button class="add-btn" @click="showAddModal">
        <i class="fa fa-plus"></i>
      </button>
      <button class="menu-btn" @click="showMenu">
        <i class="fa fa-ellipsis-v"></i>
      </button>
    </div>

    <!-- 康复进度卡片 -->
    <div class="recovery-progress">
      <div class="progress-header">
        <div class="progress-title">
          <i class="fa fa-heartbeat"></i>
          <span>康复进度</span>
        </div>
        <span class="progress-days">已坚持 {{ recoveryDays }} 天</span>
      </div>
      <div class="progress-bar-container">
        <div class="progress-bar" :style="{ width: progressPercentage + '%' }"></div>
      </div>
      <div class="progress-stats">
        <div class="stat-item">
          <div class="stat-value">{{ completedToday }}/{{ totalToday }}</div>
          <div class="stat-label">今日完成</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">{{ completionRate }}%</div>
          <div class="stat-label">完成率</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">{{ streakDays }}</div>
          <div class="stat-label">连续打卡</div>
        </div>
      </div>
    </div>

    <!-- 健康小贴士 -->
    <div class="health-tip-card" @click="viewHealthTips">
      <div class="tip-header">
        <div class="tip-icon">
          <i class="fa fa-lightbulb-o"></i>
        </div>
        <div class="tip-content">
          <div class="tip-title">健康小贴士</div>
          <div class="tip-text">{{ currentTip.content }}</div>
        </div>
        <button class="tip-refresh" @click.stop="refreshTip">
          <i class="fa fa-refresh"></i>
        </button>
      </div>
    </div>

    <!-- 分类筛选 -->
    <div class="filter-section">
      <div class="filter-tabs">
        <button 
          class="filter-tab" 
          :class="{ active: filterType === 'all' }"
          @click="filterType = 'all'"
        >
          <i class="fa fa-th-large"></i>
          全部
        </button>
        <button 
          class="filter-tab" 
          :class="{ active: filterType === 'medication' }"
          @click="filterType = 'medication'"
        >
          <i class="fa fa-pills"></i>
          用药
        </button>
        <button 
          class="filter-tab" 
          :class="{ active: filterType === 'appointment' }"
          @click="filterType = 'appointment'"
        >
          <i class="fa fa-calendar-alt"></i>
          复诊
        </button>
        <button 
          class="filter-tab" 
          :class="{ active: filterType === 'exercise' }"
          @click="filterType = 'exercise'"
        >
          <i class="fa fa-running"></i>
          康复
        </button>
      </div>
      <div class="time-filter">
        <button 
          class="time-btn" 
          :class="{ active: timeFilter === 'today' }"
          @click="timeFilter = 'today'"
        >
          今天
        </button>
        <button 
          class="time-btn" 
          :class="{ active: timeFilter === 'tomorrow' }"
          @click="timeFilter = 'tomorrow'"
        >
          明天
        </button>
        <button 
          class="time-btn" 
          :class="{ active: timeFilter === 'week' }"
          @click="timeFilter = 'week'"
        >
          本周
        </button>
      </div>
    </div>

    <!-- 提醒列表 -->
    <div class="reminders-section">
      <!-- 进行中提醒 -->
      <div class="section">
        <div class="section-header">
          <div class="section-title">
            <i class="fa fa-clock-o"></i>
            进行中
            <span class="badge">{{ ongoingReminders.length }}</span>
          </div>
          <button class="section-action" @click="completeAllOngoing" v-if="ongoingReminders.length > 0">
            <i class="fa fa-check-double"></i>
            全部完成
          </button>
        </div>

        <div class="reminders-grid">
          <div 
            class="reminder-card" 
            v-for="reminder in ongoingReminders" 
            :key="reminder.id"
            :class="['type-' + reminder.type, { urgent: reminder.isUrgent }]"
            @click="viewReminderDetail(reminder)"
          >
            <div class="card-top">
              <div class="card-icon" :class="getTypeIconClass(reminder.type)">
                <i :class="getTypeIcon(reminder.type)"></i>
              </div>
              <div class="card-tags">
                <span class="tag urgent-tag" v-if="reminder.isUrgent">
                  <i class="fa fa-exclamation-circle"></i>
                  紧急
                </span>
                <span class="tag type-tag" :class="'tag-' + reminder.type">
                  {{ reminder.typeName }}
                </span>
              </div>
            </div>
            
            <div class="card-body">
              <div class="card-title">{{ reminder.title }}</div>
              <div class="card-desc">{{ reminder.description }}</div>
              
              <div class="card-details" v-if="reminder.details">
                <div class="detail-item" v-if="reminder.details.dosage">
                  <i class="fa fa-info-circle"></i>
                  <span>{{ reminder.details.dosage }}</span>
                </div>
                <div class="detail-item" v-if="reminder.details.hospital">
                  <i class="fa fa-hospital"></i>
                  <span>{{ reminder.details.hospital }}</span>
                </div>
                <div class="detail-item" v-if="reminder.details.department">
                  <i class="fa fa-user-md"></i>
                  <span>{{ reminder.details.department }}</span>
                </div>
              </div>
            </div>

            <div class="card-bottom">
              <div class="time-info">
                <i class="fa fa-clock-o"></i>
                <span class="time">{{ reminder.time }}</span>
                <span class="countdown" :class="getUrgencyClass(reminder)">
                  {{ reminder.countdown }}
                </span>
              </div>
              <div class="card-actions" @click.stop>
                <button class="action-btn icon-btn" @click="handleAction(reminder, 'location')" v-if="reminder.showLocation">
                  <i class="fa fa-location-arrow"></i>
                </button>
                <button class="action-btn secondary" @click="handleAction(reminder, 'snooze')">
                  <i class="fa fa-clock"></i>
                </button>
                <button class="action-btn primary" @click="handleAction(reminder, 'complete')">
                  <i class="fa fa-check"></i>
                </button>
              </div>
            </div>
          </div>

          <div class="empty-state" v-if="ongoingReminders.length === 0">
            <i class="fa fa-check-circle"></i>
            <p>所有提醒已完成</p>
            <span>继续保持健康习惯！</span>
          </div>
        </div>
      </div>

      <!-- 已完成 -->
      <div class="section">
        <div class="section-header">
          <div class="section-title">
            <i class="fa fa-check-circle"></i>
            已完成
            <span class="badge completed">{{ completedReminders.length }}</span>
          </div>
          <button class="section-action" @click="clearCompleted" v-if="completedReminders.length > 0">
            <i class="fa fa-trash"></i>
            清空
          </button>
        </div>

        <div class="completed-list">
          <div 
            class="completed-item" 
            v-for="reminder in completedReminders" 
            :key="reminder.id"
            @click="viewReminderDetail(reminder)"
          >
            <div class="completed-icon">
              <i class="fa fa-check"></i>
            </div>
            <div class="completed-content">
              <div class="completed-title">{{ reminder.title }}</div>
              <div class="completed-meta">
                <span class="completed-time">
                  <i class="fa fa-check-circle"></i>
                  {{ reminder.completedTime }}
                </span>
                <span class="completed-type">{{ reminder.typeName }}</span>
              </div>
            </div>
            <button class="completed-restore" @click.stop="handleAction(reminder, 'restore')">
              <i class="fa fa-undo"></i>
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 底部快捷操作 -->
    <div class="quick-add-bar">
      <div class="quick-add-item" @click="quickAdd('medication')">
        <i class="fa fa-pills"></i>
        <span>用药</span>
      </div>
      <div class="quick-add-item" @click="quickAdd('appointment')">
        <i class="fa fa-calendar-alt"></i>
        <span>复诊</span>
      </div>
      <div class="quick-add-item" @click="quickAdd('exercise')">
        <i class="fa fa-running"></i>
        <span>康复</span>
      </div>
      <div class="quick-add-item" @click="quickAdd('diet')">
        <i class="fa fa-cutlery"></i>
        <span>饮食</span>
      </div>
    </div>

    <!-- Toast 提示 -->
    <transition name="toast">
      <div v-if="showToast" class="toast" :class="toastType">
        <i :class="toastIcon"></i>
        <span>{{ toastMessage }}</span>
      </div>
    </transition>

    <!-- 添加提醒模态框 -->
    <transition name="modal">
      <div class="modal-overlay" v-if="showModal" @click="closeModal">
        <div class="modal" @click.stop>
          <div class="modal-header">
            <h3>添加健康提醒</h3>
            <button class="modal-close" @click="closeModal">
              <i class="fa fa-times"></i>
            </button>
          </div>
          
          <div class="modal-body">
            <!-- 类型选择 -->
            <div class="form-section">
              <label class="form-label">提醒类型</label>
              <div class="type-selector">
                <button 
                  class="type-option" 
                  :class="{ active: newReminder.type === 'medication' }"
                  @click="selectType('medication')"
                >
                  <i class="fa fa-pills"></i>
                  <span>用药提醒</span>
                </button>
                <button 
                  class="type-option" 
                  :class="{ active: newReminder.type === 'appointment' }"
                  @click="selectType('appointment')"
                >
                  <i class="fa fa-calendar-alt"></i>
                  <span>复诊预约</span>
                </button>
                <button 
                  class="type-option" 
                  :class="{ active: newReminder.type === 'exercise' }"
                  @click="selectType('exercise')"
                >
                  <i class="fa fa-running"></i>
                  <span>康复训练</span>
                </button>
                <button 
                  class="type-option" 
                  :class="{ active: newReminder.type === 'diet' }"
                  @click="selectType('diet')"
                >
                  <i class="fa fa-cutlery"></i>
                  <span>饮食建议</span>
                </button>
              </div>
            </div>

            <!-- 基本信息 -->
            <div class="form-section">
              <div class="form-group">
                <label class="form-label">提醒标题 <span class="required">*</span></label>
                <input 
                  type="text" 
                  v-model="newReminder.title"
                  :placeholder="getTypePlaceholder(newReminder.type)"
                  class="form-input"
                >
              </div>
              
              <div class="form-group">
                <label class="form-label">详细描述</label>
                <textarea 
                  v-model="newReminder.description"
                  placeholder="详细描述提醒内容..."
                  class="form-textarea"
                  rows="3"
                ></textarea>
              </div>
            </div>

            <!-- 时间设置 -->
            <div class="form-section">
              <div class="form-row">
                <div class="form-group">
                  <label class="form-label">提醒时间 <span class="required">*</span></label>
                  <input 
                    type="time" 
                    v-model="newReminder.time"
                    class="form-input"
                  >
                </div>
                <div class="form-group">
                  <label class="form-label">提醒日期</label>
                  <input 
                    type="date" 
                    v-model="newReminder.date"
                    class="form-input"
                  >
                </div>
              </div>
              
              <div class="form-group">
                <label class="form-label">重复设置</label>
                <div class="repeat-options">
                  <label class="radio-option">
                    <input type="radio" v-model="newReminder.repeat" value="once">
                    <span>仅一次</span>
                  </label>
                  <label class="radio-option">
                    <input type="radio" v-model="newReminder.repeat" value="daily">
                    <span>每天</span>
                  </label>
                  <label class="radio-option">
                    <input type="radio" v-model="newReminder.repeat" value="weekly">
                    <span>每周</span>
                  </label>
                </div>
              </div>
            </div>

            <!-- 用药详情 -->
            <div class="form-section" v-if="newReminder.type === 'medication'">
              <div class="form-group">
                <label class="form-label">药品名称</label>
                <input 
                  type="text" 
                  v-model="newReminder.details.medicationName"
                  placeholder="例如：阿莫西林胶囊"
                  class="form-input"
                >
              </div>
              <div class="form-group">
                <label class="form-label">用药剂量</label>
                <input 
                  type="text" 
                  v-model="newReminder.details.dosage"
                  placeholder="例如：每次 1 片，每日 2 次"
                  class="form-input"
                >
              </div>
            </div>

            <!-- 预约详情 -->
            <div class="form-section" v-if="newReminder.type === 'appointment'">
              <div class="form-group">
                <label class="form-label">医院名称</label>
                <input 
                  type="text" 
                  v-model="newReminder.details.hospital"
                  placeholder="例如：南昌大学第一附属医院"
                  class="form-input"
                >
              </div>
              <div class="form-group">
                <label class="form-label">科室</label>
                <input 
                  type="text" 
                  v-model="newReminder.details.department"
                  placeholder="例如：骨科/康复科"
                  class="form-input"
                >
              </div>
              <div class="form-group">
                <label class="form-label">医生姓名</label>
                <input 
                  type="text" 
                  v-model="newReminder.details.doctor"
                  placeholder="例如：张医生"
                  class="form-input"
                >
              </div>
            </div>
          </div>

          <div class="modal-footer">
            <button class="btn-cancel" @click="closeModal">取消</button>
            <button class="btn-confirm" @click="addReminder">
              <i class="fa fa-plus"></i>
              添加提醒
            </button>
          </div>
        </div>
      </div>
    </transition>

    <!-- 菜单弹窗 -->
    <transition name="dropdown">
      <div class="dropdown-menu" v-if="showMenuFlag" @click="showMenuFlag = false">
        <div class="menu-items" @click.stop>
          <div class="menu-item" @click="viewStatistics">
            <i class="fa fa-bar-chart"></i>
            <span>健康统计</span>
          </div>
          <div class="menu-item" @click="exportReminders">
            <i class="fa fa-download"></i>
            <span>导出记录</span>
          </div>
          <div class="menu-item" @click="showSettings">
            <i class="fa fa-cog"></i>
            <span>提醒设置</span>
          </div>
          <div class="menu-divider"></div>
          <div class="menu-item danger" @click="clearAllData">
            <i class="fa fa-trash"></i>
            <span>清空所有数据</span>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
export default {
  name: 'HealthReminder',
  data() {
    return {
      // UI 状态
      showToast: false,
      toastMessage: '',
      toastType: 'info',
      showModal: false,
      showMenuFlag: false,
      
      // 筛选状态
      filterType: 'all',
      timeFilter: 'today',
      
      // 康复进度
      recoveryDays: 15,
      targetDays: 30,
      
      // 新增提醒表单
      newReminder: {
        type: 'medication',
        title: '',
        description: '',
        time: '',
        date: '',
        repeat: 'once',
        details: {
          medicationName: '',
          dosage: '',
          hospital: '',
          department: '',
          doctor: ''
        }
      },
      
      // 提醒数据
      reminders: [
        {
          id: 1,
          type: 'medication',
          typeName: '用药',
          title: '服用降压药',
          description: '硝苯地平控释片',
          time: '08:00',
          date: new Date().toISOString().split('T')[0],
          countdown: '已过期 2 小时',
          isUrgent: true,
          status: 'pending',
          details: {
            medicationName: '硝苯地平控释片',
            dosage: '每次 1 片，每日 1 次'
          },
          showLocation: false
        },
        {
          id: 2,
          type: 'appointment',
          typeName: '复诊',
          title: '骨科复诊',
          description: '术后复查，拆线',
          time: '14:30',
          date: new Date().toISOString().split('T')[0],
          countdown: '2 小时后',
          isUrgent: false,
          status: 'pending',
          details: {
            hospital: '南昌大学第一附属医院',
            department: '骨科',
            doctor: '张医生'
          },
          showLocation: true
        },
        {
          id: 3,
          type: 'exercise',
          typeName: '康复',
          title: '膝关节康复训练',
          description: '屈伸练习、肌肉力量训练',
          time: '16:00',
          date: new Date().toISOString().split('T')[0],
          countdown: '3 小时后',
          isUrgent: false,
          status: 'pending',
          details: {},
          showLocation: false
        },
        {
          id: 4,
          type: 'medication',
          typeName: '用药',
          title: '服用维生素 D',
          description: '促进钙吸收',
          time: '07:30',
          date: new Date().toISOString().split('T')[0],
          countdown: '已完成',
          isUrgent: false,
          status: 'completed',
          completedTime: '07:35',
          details: {
            medicationName: '维生素 D 胶囊',
            dosage: '每次 1 粒，每日 1 次'
          },
          showLocation: false
        },
        {
          id: 5,
          type: 'diet',
          typeName: '饮食',
          title: '早餐',
          description: '高蛋白、低盐饮食',
          time: '07:00',
          date: new Date().toISOString().split('T')[0],
          countdown: '已完成',
          isUrgent: false,
          status: 'completed',
          completedTime: '07:15',
          details: {},
          showLocation: false
        }
      ],
      
      // 健康小贴士
      healthTips: [
        { content: '术后保持伤口干燥，避免感染' },
        { content: '适量运动有助于康复，但不要过度劳累' },
        { content: '保持良好心态，积极面对康复过程' },
        { content: '按时服药，不要随意更改剂量' },
        { content: '定期复查，及时了解康复情况' },
        { content: '充足睡眠有助于身体恢复' },
        { content: '均衡饮食，多摄入蛋白质和维生素' }
      ],
      currentTipIndex: 0
    }
  },
  computed: {
    // 进行中的提醒
    ongoingReminders() {
      let filtered = this.reminders.filter(r => r.status === 'pending')
      
      // 按类型筛选
      if (this.filterType !== 'all') {
        filtered = filtered.filter(r => r.type === this.filterType)
      }
      
      // 按时间筛选
      const today = new Date().toISOString().split('T')[0]
      if (this.timeFilter === 'today') {
        filtered = filtered.filter(r => r.date === today)
      } else if (this.timeFilter === 'tomorrow') {
        const tomorrow = new Date()
        tomorrow.setDate(tomorrow.getDate() + 1)
        const tomorrowStr = tomorrow.toISOString().split('T')[0]
        filtered = filtered.filter(r => r.date === tomorrowStr)
      }
      
      return filtered
    },
    
    // 已完成的提醒
    completedReminders() {
      return this.reminders.filter(r => r.status === 'completed')
    },
    
    // 今日总任务数
    totalToday() {
      const today = new Date().toISOString().split('T')[0]
      return this.reminders.filter(r => r.date === today).length
    },
    
    // 今日已完成数
    completedToday() {
      const today = new Date().toISOString().split('T')[0]
      return this.reminders.filter(r => r.date === today && r.status === 'completed').length
    },
    
    // 完成率
    completionRate() {
      if (this.totalToday === 0) return 0
      return Math.round((this.completedToday / this.totalToday) * 100)
    },
    
    // 进度百分比
    progressPercentage() {
      return Math.round((this.recoveryDays / this.targetDays) * 100)
    },
    
    // 连续打卡天数
    streakDays() {
      return this.recoveryDays
    },
    
    // 当前小贴士
    currentTip() {
      return this.healthTips[this.currentTipIndex]
    },
    
    // Toast 图标
    toastIcon() {
      const icons = {
        info: 'fa fa-info-circle',
        success: 'fa fa-check-circle',
        warning: 'fa fa-exclamation-triangle',
        error: 'fa fa-times-circle'
      }
      return icons[this.toastType]
    }
  },
  mounted() {
    this.updateAllCountdowns()
    // 每分钟更新一次倒计时
    setInterval(() => {
      this.updateAllCountdowns()
    }, 60000)
  },
  methods: {
    // 获取当前日期
    getCurrentDate() {
      const now = new Date()
      const month = now.getMonth() + 1
      const day = now.getDate()
      return `${month}月${day}日`
    },
    
    // 获取星期
    getWeekDay() {
      const weekdays = ['日', '一', '二', '三', '四', '五', '六']
      return weekdays[new Date().getDay()]
    },
    
    // 更新所有倒计时
    updateAllCountdowns() {
      const now = new Date()
      const today = now.toISOString().split('T')[0]
      const nowTime = now.getHours() * 60 + now.getMinutes()
      
      this.reminders.forEach(reminder => {
        if (reminder.status === 'completed' || reminder.date !== today) return
        
        const [hours, minutes] = reminder.time.split(':').map(Number)
        const reminderTime = hours * 60 + minutes
        const diff = reminderTime - nowTime
        
        if (diff < -120) {
          reminder.countdown = `已过期 ${Math.abs(diff + 120)} 分钟`
          reminder.isUrgent = true
        } else if (diff < 0) {
          reminder.countdown = `已过期 ${Math.abs(diff)} 分钟`
          reminder.isUrgent = true
        } else if (diff === 0) {
          reminder.countdown = '立即执行'
          reminder.isUrgent = true
        } else if (diff < 60) {
          reminder.countdown = `${diff} 分钟后`
          reminder.isUrgent = true
        } else if (diff < 180) {
          reminder.countdown = `${Math.floor(diff / 60)} 小时后`
          reminder.isUrgent = false
        } else {
          reminder.countdown = `${Math.floor(diff / 60)} 小时后`
          reminder.isUrgent = false
        }
      })
    },
    
    // 获取类型图标
    getTypeIcon(type) {
      const icons = {
        medication: 'fa fa-pills',
        appointment: 'fa fa-calendar-alt',
        exercise: 'fa fa-running',
        diet: 'fa fa-cutlery'
      }
      return icons[type]
    },
    
    // 获取类型图标样式
    getTypeIconClass(type) {
      return type
    },
    
    // 获取紧急程度样式
    getUrgencyClass(reminder) {
      if (!reminder.isUrgent) return 'normal'
      if (reminder.countdown.includes('已过期')) return 'expired'
      return 'urgent'
    },
    
    // 显示 Toast
    showtoast(message, type = 'info') {
      this.toastMessage = message
      this.toastType = type
      this.showToast = true
      setTimeout(() => {
        this.showToast = false
      }, 2000)
    },
    
    // 返回
    goBack() {
      this.showtoast('返回上一页', 'info')
      this.$router.back()
    },
    
    // 显示菜单
    showMenu() {
      this.showMenuFlag = true
    },
    
    // 查看健康小贴士
    viewHealthTips() {
      this.showtoast('查看更多健康建议...', 'info')
    },
    
    // 刷新小贴士
    refreshTip() {
      this.currentTipIndex = (this.currentTipIndex + 1) % this.healthTips.length
      this.showtoast('已更新健康小贴士', 'success')
    },
    
    // 快速添加
    quickAdd(type) {
      this.newReminder.type = type
      this.showModal = true
      this.showtoast(`添加${this.getTypeName(type)}提醒`, 'info')
    },
    
    // 获取类型名称
    getTypeName(type) {
      const names = {
        medication: '用药',
        appointment: '复诊',
        exercise: '康复',
        diet: '饮食'
      }
      return names[type]
    },
    
    // 选择类型
    selectType(type) {
      this.newReminder.type = type
    },
    
    // 获取占位符
    getTypePlaceholder(type) {
      const placeholders = {
        medication: '例如：服用阿莫西林',
        appointment: '例如：骨科复诊',
        exercise: '例如：膝关节康复训练',
        diet: '例如：早餐'
      }
      return placeholders[type]
    },
    
    // 显示添加模态框
    showAddModal() {
      this.showModal = true
    },
    
    // 关闭模态框
    closeModal() {
      this.showModal = false
      this.newReminder = {
        type: 'medication',
        title: '',
        description: '',
        time: '',
        date: '',
        repeat: 'once',
        details: {
          medicationName: '',
          dosage: '',
          hospital: '',
          department: '',
          doctor: ''
        }
      }
    },
    
    // 添加提醒
    addReminder() {
      if (!this.newReminder.title || !this.newReminder.time) {
        this.showtoast('请填写完整的提醒信息', 'warning')
        return
      }
      
      const newId = Math.max(...this.reminders.map(r => r.id), 0) + 1
      const today = new Date().toISOString().split('T')[0]
      
      const newReminder = {
        id: newId,
        type: this.newReminder.type,
        typeName: this.getTypeName(this.newReminder.type),
        title: this.newReminder.title,
        description: this.newReminder.description,
        time: this.newReminder.time,
        date: this.newReminder.date || today,
        countdown: '刚刚添加',
        isUrgent: false,
        status: 'pending',
        details: { ...this.newReminder.details },
        showLocation: this.newReminder.type === 'appointment'
      }
      
      this.reminders.push(newReminder)
      this.closeModal()
      this.showtoast(`已添加${newReminder.typeName}提醒`, 'success')
    },
    
    // 查看提醒详情
    viewReminderDetail(reminder) {
      const statusText = reminder.status === 'completed' ? '查看已完成' : '查看'
      this.showtoast(`${statusText}：${reminder.title}`, 'info')
    },
    
    // 处理操作
    handleAction(reminder, action) {
      const now = new Date().toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
      
      switch(action) {
        case 'complete':
          reminder.status = 'completed'
          reminder.completedTime = now
          this.showtoast(`已完成：${reminder.title}`, 'success')
          break
        case 'snooze':
          this.showtoast(`已延迟 30 分钟`, 'info')
          break
        case 'location':
          this.showtoast('正在打开导航...', 'info')
          break
        case 'restore':
          reminder.status = 'pending'
          delete reminder.completedTime
          this.showtoast('已恢复提醒', 'success')
          break
      }
    },
    
    // 完成所有进行中
    completeAllOngoing() {
      this.ongoingReminders.forEach(reminder => {
        reminder.status = 'completed'
        reminder.completedTime = new Date().toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
      })
      this.showtoast('已全部完成！', 'success')
    },
    
    // 清空已完成
    clearCompleted() {
      if (confirm('确定要清空所有已完成的提醒吗？')) {
        this.completedReminders.forEach(r => {
          const index = this.reminders.indexOf(r)
          if (index > -1) this.reminders.splice(index, 1)
        })
        this.showtoast('已清空已完成记录', 'success')
      }
    },
    
    // 查看统计
    viewStatistics() {
      this.showtoast('健康统计功能开发中...', 'info')
    },
    
    // 导出记录
    exportReminders() {
      this.showtoast('导出功能开发中...', 'info')
    },
    
    // 设置
    showSettings() {
      this.showtoast('提醒设置功能开发中...', 'info')
    },
    
    // 清空所有数据
    clearAllData() {
      if (confirm('确定要清空所有数据吗？此操作不可恢复！')) {
        this.reminders = []
        this.showtoast('已清空所有数据', 'success')
      }
    }
  }
}
</script>

<style scoped>
/* 动画 */
.toast-enter-active, .toast-leave-active {
  transition: all 0.3s ease;
}
.toast-enter, .toast-leave-to {
  opacity: 0;
  transform: translateY(-20px);
}

.modal-enter-active, .modal-leave-active {
  transition: all 0.3s ease;
}
.modal-enter, .modal-leave-to {
  opacity: 0;
}
.modal-overlay .modal {
  transform: scale(1);
}
.modal-enter .modal, .modal-leave-to .modal {
  transform: scale(0.9);
}

.dropdown-enter-active, .dropdown-leave-active {
  transition: all 0.2s ease;
}
.dropdown-enter, .dropdown-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

/* 容器 */
.health-reminder-container {
  padding: 16px;
  padding-bottom: 100px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  min-height: 100vh;
}

/* 头部 */
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  color: white;
}

.nav-left {
  display: flex;
  align-items: center;
  flex: 1;
}

.back-btn {
  background: none;
  border: none;
  font-size: 18px;
  color: white;
  cursor: pointer;
  margin-right: 12px;
  padding: 8px;
  transition: all 0.3s ease;
}

.back-btn:hover {
  transform: translateX(-3px);
}

.header-info {
  display: flex;
  flex-direction: column;
}

.page-title {
  font-size: 20px;
  font-weight: 600;
  margin: 0;
}

.subtitle {
  font-size: 12px;
  opacity: 0.9;
  margin: 2px 0 0 0;
}

.add-btn, .menu-btn {
  background: rgba(255, 255, 255, 0.2);
  border: none;
  color: white;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  font-size: 16px;
  cursor: pointer;
  margin-left: 8px;
  transition: all 0.3s ease;
}

.add-btn:hover, .menu-btn:hover {
  background: rgba(255, 255, 255, 0.3);
  transform: scale(1.1);
}

/* 康复进度 */
.recovery-progress {
  background: white;
  border-radius: 16px;
  padding: 20px;
  margin-bottom: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.progress-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.progress-title {
  display: flex;
  align-items: center;
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.progress-title i {
  margin-right: 8px;
  color: #ff6b6b;
}

.progress-days {
  font-size: 14px;
  color: #666;
}

.progress-bar-container {
  height: 8px;
  background: #f0f0f0;
  border-radius: 4px;
  overflow: hidden;
  margin-bottom: 16px;
}

.progress-bar {
  height: 100%;
  background: linear-gradient(90deg, #667eea, #764ba2);
  border-radius: 4px;
  transition: width 0.5s ease;
}

.progress-stats {
  display: flex;
  justify-content: space-around;
}

.stat-item {
  text-align: center;
}

.stat-value {
  font-size: 20px;
  font-weight: 600;
  color: #667eea;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 12px;
  color: #999;
}

/* 健康小贴士 */
.health-tip-card {
  background: white;
  border-radius: 16px;
  padding: 16px;
  margin-bottom: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: all 0.3s ease;
}

.health-tip-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.15);
}

.tip-header {
  display: flex;
  align-items: center;
}

.tip-icon {
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, #ffd700, #ffed4e);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  flex-shrink: 0;
}

.tip-icon i {
  color: #fff;
  font-size: 18px;
}

.tip-content {
  flex: 1;
}

.tip-title {
  font-size: 14px;
  font-weight: 600;
  color: #333;
  margin-bottom: 4px;
}

.tip-text {
  font-size: 13px;
  color: #666;
  line-height: 1.4;
}

.tip-refresh {
  background: none;
  border: none;
  color: #999;
  font-size: 16px;
  cursor: pointer;
  padding: 8px;
  transition: all 0.3s ease;
}

.tip-refresh:hover {
  color: #667eea;
  transform: rotate(90deg);
}

/* 筛选区域 */
.filter-section {
  background: white;
  border-radius: 16px;
  padding: 16px;
  margin-bottom: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.filter-tabs {
  display: flex;
  margin-bottom: 12px;
}

.filter-tab {
  flex: 1;
  background: #f5f5f5;
  border: none;
  padding: 10px;
  font-size: 13px;
  color: #666;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
}

.filter-tab:first-child {
  border-radius: 8px 0 0 8px;
}

.filter-tab:last-child {
  border-radius: 0 8px 8px 0;
}

.filter-tab.active {
  background: #667eea;
  color: white;
}

.time-filter {
  display: flex;
  gap: 8px;
}

.time-btn {
  flex: 1;
  background: #f5f5f5;
  border: none;
  padding: 8px;
  font-size: 12px;
  color: #666;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.time-btn.active {
  background: #667eea;
  color: white;
}

/* 提醒列表 */
.reminders-section {
  padding-bottom: 16px;
}

.section {
  margin-bottom: 20px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
  color: white;
}

.section-title {
  display: flex;
  align-items: center;
  font-size: 16px;
  font-weight: 600;
}

.section-title i {
  margin-right: 8px;
}

.section-title .badge {
  background: rgba(255, 255, 255, 0.3);
  padding: 2px 10px;
  border-radius: 12px;
  font-size: 12px;
  margin-left: 8px;
  font-weight: normal;
}

.section-title .badge.completed {
  background: rgba(82, 196, 26, 0.3);
}

.section-action {
  background: rgba(255, 255, 255, 0.2);
  border: none;
  color: white;
  padding: 6px 12px;
  border-radius: 16px;
  font-size: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 4px;
}

.section-action:hover {
  background: rgba(255, 255, 255, 0.3);
}

/* 提醒卡片网格 */
.reminders-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 12px;
}

/* 提醒卡片 */
.reminder-card {
  background: white;
  border-radius: 12px;
  padding: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: all 0.3s ease;
  border-left: 4px solid transparent;
}

.reminder-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.reminder-card.type-medication {
  border-left-color: #ff9800;
}

.reminder-card.type-appointment {
  border-left-color: #1976d2;
}

.reminder-card.type-exercise {
  border-left-color: #4caf50;
}

.reminder-card.type-diet {
  border-left-color: #e91e63;
}

.reminder-card.urgent {
  box-shadow: 0 4px 12px rgba(255, 152, 0, 0.2);
}

.card-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.card-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
}

.card-icon.medication {
  background: #fff3e0;
  color: #ff9800;
}

.card-icon.appointment {
  background: #e3f2fd;
  color: #1976d2;
}

.card-icon.exercise {
  background: #e8f5e9;
  color: #4caf50;
}

.card-icon.diet {
  background: #fce4ec;
  color: #e91e63;
}

.card-tags {
  display: flex;
  gap: 6px;
}

.tag {
  padding: 3px 8px;
  border-radius: 8px;
  font-size: 11px;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 4px;
}

.urgent-tag {
  background: #ffebee;
  color: #f44336;
}

.type-tag {
  background: #f5f5f5;
  color: #666;
}

.card-body {
  margin-bottom: 12px;
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 6px;
}

.card-desc {
  font-size: 13px;
  color: #666;
  margin-bottom: 10px;
  line-height: 1.4;
}

.card-details {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.detail-item {
  display: flex;
  align-items: center;
  font-size: 12px;
  color: #999;
  gap: 6px;
}

.detail-item i {
  font-size: 11px;
}

.card-bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 12px;
  border-top: 1px solid #f0f0f0;
}

.time-info {
  display: flex;
  align-items: center;
  font-size: 13px;
  color: #666;
  gap: 6px;
}

.time-info i {
  font-size: 12px;
}

.countdown {
  font-size: 12px;
  font-weight: 500;
}

.countdown.urgent {
  color: #f44336;
}

.countdown.expired {
  color: #ff9800;
}

.countdown.normal {
  color: #4caf50;
}

.card-actions {
  display: flex;
  gap: 6px;
}

.action-btn {
  padding: 6px 10px;
  border-radius: 8px;
  font-size: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
}

.action-btn.icon-btn {
  background: #f5f5f5;
  color: #666;
  width: 32px;
  height: 32px;
  padding: 0;
}

.action-btn.icon-btn:hover {
  background: #e0e0e0;
}

.action-btn.secondary {
  background: #f5f5f5;
  color: #666;
}

.action-btn.secondary:hover {
  background: #e0e0e0;
}

.action-btn.primary {
  background: #667eea;
  color: white;
}

.action-btn.primary:hover {
  background: #5568d3;
}

/* 空状态 */
.empty-state {
  text-align: center;
  padding: 40px 20px;
  color: white;
}

.empty-state i {
  font-size: 48px;
  margin-bottom: 12px;
  opacity: 0.8;
}

.empty-state p {
  font-size: 16px;
  margin-bottom: 4px;
}

.empty-state span {
  font-size: 13px;
  opacity: 0.9;
}

/* 已完成列表 */
.completed-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.completed-item {
  background: white;
  border-radius: 12px;
  padding: 12px 16px;
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.completed-item:hover {
  background: rgba(255, 255, 255, 0.95);
  transform: translateX(4px);
}

.completed-icon {
  width: 36px;
  height: 36px;
  background: #e8f5e9;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #4caf50;
  flex-shrink: 0;
}

.completed-content {
  flex: 1;
}

.completed-title {
  font-size: 14px;
  font-weight: 500;
  color: #333;
  margin-bottom: 4px;
}

.completed-meta {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 12px;
  color: #999;
}

.completed-time {
  display: flex;
  align-items: center;
  gap: 4px;
}

.completed-type {
  background: #f5f5f5;
  padding: 2px 8px;
  border-radius: 8px;
}

.completed-restore {
  background: none;
  border: none;
  color: #999;
  font-size: 16px;
  cursor: pointer;
  padding: 8px;
  transition: all 0.3s ease;
}

.completed-restore:hover {
  color: #667eea;
}

/* 底部快捷操作栏 */
.quick-add-bar {
  position: fixed;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  background: white;
  border-radius: 24px;
  padding: 12px 24px;
  display: flex;
  gap: 24px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
  z-index: 100;
}

.quick-add-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
  cursor: pointer;
  transition: all 0.3s ease;
  color: #666;
}

.quick-add-item:hover {
  color: #667eea;
  transform: scale(1.1);
}

.quick-add-item i {
  font-size: 20px;
}

.quick-add-item span {
  font-size: 11px;
}

/* Toast */
.toast {
  position: fixed;
  top: 80px;
  left: 50%;
  transform: translateX(-50%);
  background: rgba(0, 0, 0, 0.8);
  color: white;
  padding: 12px 24px;
  border-radius: 24px;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 8px;
  z-index: 1001;
}

.toast i {
  font-size: 16px;
}

/* 模态框 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  padding: 20px;
}

.modal {
  background: white;
  border-radius: 16px;
  width: 100%;
  max-width: 500px;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.3);
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
  font-size: 18px;
  color: #333;
}

.modal-close {
  background: none;
  border: none;
  font-size: 20px;
  color: #999;
  cursor: pointer;
  padding: 4px;
}

.modal-body {
  padding: 20px;
}

.form-section {
  margin-bottom: 20px;
}

.form-label {
  display: block;
  font-size: 14px;
  font-weight: 500;
  color: #333;
  margin-bottom: 8px;
}

.form-label .required {
  color: #f44336;
}

.form-input, .form-textarea {
  width: 100%;
  padding: 12px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  font-size: 14px;
  transition: all 0.3s ease;
  box-sizing: border-box;
}

.form-input:focus, .form-textarea:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.form-textarea {
  resize: vertical;
  font-family: inherit;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
}

.type-selector {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
}

.type-option {
  background: #f5f5f5;
  border: 2px solid transparent;
  border-radius: 12px;
  padding: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.type-option i {
  font-size: 24px;
  color: #999;
}

.type-option span {
  font-size: 13px;
  color: #666;
}

.type-option.active {
  background: #e8eaf6;
  border-color: #667eea;
}

.type-option.active i {
  color: #667eea;
}

.repeat-options {
  display: flex;
  gap: 16px;
}

.radio-option {
  display: flex;
  align-items: center;
  gap: 6px;
  cursor: pointer;
  font-size: 13px;
  color: #666;
}

.radio-option input {
  accent-color: #667eea;
}

.modal-footer {
  display: flex;
  gap: 12px;
  padding: 20px;
  border-top: 1px solid #f0f0f0;
}

.btn-cancel, .btn-confirm {
  flex: 1;
  padding: 12px;
  border-radius: 12px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  border: none;
}

.btn-cancel {
  background: #f5f5f5;
  color: #666;
}

.btn-cancel:hover {
  background: #e0e0e0;
}

.btn-confirm {
  background: #667eea;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.btn-confirm:hover {
  background: #5568d3;
}

/* 下拉菜单 */
.dropdown-menu {
  position: absolute;
  top: 60px;
  right: 20px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  z-index: 100;
  overflow: hidden;
}

.menu-items {
  min-width: 180px;
  padding: 8px 0;
}

.menu-item {
  padding: 12px 20px;
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  color: #333;
}

.menu-item:hover {
  background: #f5f5f5;
}

.menu-item i {
  font-size: 16px;
  color: #666;
}

.menu-item.danger {
  color: #f44336;
}

.menu-item.danger i {
  color: #f44336;
}

.menu-divider {
  height: 1px;
  background: #e0e0e0;
  margin: 8px 0;
}
</style>
