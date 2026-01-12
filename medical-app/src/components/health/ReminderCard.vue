<template>
  <div :class="['reminder-card', { completed: reminder.completed }]" :data-category="reminder.category">
    <div class="flex justify-between items-start mb-3">
      <div class="flex items-center gap-3">
        <div :class="iconClass">
          <i :class="categoryIcon"></i>
        </div>
        <div>
          <h3 class="font-medium">{{ reminder.title }}</h3>
          <p class="text-sm text-gray-500">{{ reminder.description }}</p>
        </div>
      </div>
      <span :class="badgeClass">{{ categoryName }}</span>
    </div>
    <div class="flex justify-between items-center pt-3 border-t border-gray-100">
      <div class="flex items-center gap-2">
        <i class="fa fa-clock-o text-gray-400"></i>
        <span class="text-sm">{{ reminder.time }}</span>
        <span :class="countdownClass">{{ countdownText }}</span>
      </div>
      <div class="flex gap-2">
        <button 
          v-for="action in actions" 
          :key="action.name"
          :class="['btn', action.primary ? 'btn-primary' : 'btn-secondary', 'px-3 py-1 text-sm']"
          @click="handleAction(action.name)"
        >
          <i :class="[action.icon, 'mr-1']"></i> {{ action.label }}
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import { computed } from 'vue'

export default {
  name: 'ReminderCard',
  props: {
    reminder: {
      type: Object,
      required: true
    }
  },
  emits: ['complete', 'action'],
  setup(props, { emit }) {
    const categoryConfig = {
      medication: {
        name: '用药提醒',
        icon: 'fa fa-medkit',
        color: 'danger'
      },
      appointment: {
        name: '预约提醒',
        icon: 'fa fa-calendar',
        color: 'primary'
      },
      vaccine: {
        name: '疫苗接种',
        icon: 'fa fa-syringe',
        color: 'warning'
      },
      checkup: {
        name: '体检提醒',
        icon: 'fa fa-stethoscope',
        color: 'secondary'
      }
    }

    const categoryInfo = computed(() => categoryConfig[props.reminder.category] || categoryConfig.medication)

    const categoryName = computed(() => 
      props.reminder.completed ? '已完成' : categoryInfo.value.name
    )

    const categoryIcon = computed(() => categoryInfo.value.icon)

    const iconClass = computed(() => 
      `w-10 h-10 rounded-full bg-${categoryInfo.value.color}/10 flex items-center justify-center text-${categoryInfo.value.color}`
    )

    const badgeClass = computed(() => 
      props.reminder.completed 
        ? 'badge bg-secondary/10 text-secondary'
        : `badge bg-${categoryInfo.value.color}/10 text-${categoryInfo.value.color}`
    )

    const countdownClass = computed(() => 
      `countdown ml-2 text-xs text-${categoryInfo.value.color}`
    )

    const countdownText = computed(() => {
      // 这里可以计算倒计时文本
      return ''
    })

    const actions = computed(() => {
      const baseActions = {
        medication: [
          { name: 'complete', label: '完成', icon: 'fa fa-check', primary: false },
          { name: 'later', label: '稍后提醒', icon: 'fa fa-bell-o', primary: true }
        ],
        appointment: [
          { name: 'navigate', label: '导航', icon: 'fa fa-map-marker', primary: false },
          { name: 'call', label: '联系医院', icon: 'fa fa-phone', primary: true }
        ],
        checkup: [
          { name: 'items', label: '检查项', icon: 'fa fa-list', primary: false },
          { name: 'complete', label: '已完成', icon: 'fa fa-check', primary: true }
        ],
        vaccine: [
          { name: 'adjust', label: '调整时间', icon: 'fa fa-calendar', primary: false },
          { name: 'confirm', label: '确认提醒', icon: 'fa fa-bell-o', primary: true }
        ]
      }

      // 如果是已完成状态，显示编辑和删除
      if (props.reminder.completed) {
        return [
          { name: 'delete', label: '删除', icon: 'fa fa-trash', primary: false },
          { name: 'edit', label: '编辑', icon: 'fa fa-pencil', primary: true }
        ]
      }

      // 如果是即将到期的用药提醒
      if (props.reminder.section === 'upcoming' && props.reminder.category === 'medication') {
        return [
          { name: 'delete', label: '删除', icon: 'fa fa-trash', primary: false },
          { name: 'edit', label: '编辑', icon: 'fa fa-pencil', primary: true }
        ]
      }

      return baseActions[props.reminder.category] || baseActions.medication
    })

    const handleAction = (actionName) => {
      if (actionName === 'complete') {
        emit('complete', props.reminder.id)
      } else {
        emit('action', actionName, props.reminder)
      }
    }

    return {
      categoryName,
      categoryIcon,
      iconClass,
      badgeClass,
      countdownClass,
      countdownText,
      actions,
      handleAction
    }
  }
}
</script>