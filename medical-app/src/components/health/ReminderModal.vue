<template>
  <div 
    v-if="show" 
    class="fixed inset-0 bg-black/50 z-50 flex items-center justify-center opacity-0 transition-opacity duration-300"
    :class="{ 'opacity-100': show }"
    @click="handleBackdropClick"
  >
    <div 
      class="bg-white rounded-xl shadow-lg w-full max-w-md p-6 transform transition-all scale-95 opacity-0"
      :class="{ 'scale-100 opacity-100': show }"
      @click.stop
    >
      <div class="flex justify-between items-center mb-4">
        <h3 class="text-xl font-semibold">{{ modalTitle }}</h3>
        <button @click="close" class="text-gray-400 hover:text-gray-600 transition-colors">
          <i class="fa fa-times text-xl"></i>
        </button>
      </div>
      <form @submit.prevent="handleSubmit">
        <input type="hidden" v-model="formData.id">
        <div class="mb-4">
          <label class="block text-sm font-medium text-gray-700 mb-1">提醒标题</label>
          <input 
            type="text" 
            v-model="formData.title"
            class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary" 
            placeholder="例如：服用降压药" 
            required
          >
        </div>
        <div class="mb-4">
          <label class="block text-sm font-medium text-gray-700 mb-1">提醒类型</label>
          <select 
            v-model="formData.category"
            class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary"
          >
            <option value="medication">用药提醒</option>
            <option value="appointment">预约提醒</option>
            <option value="vaccine">疫苗接种</option>
            <option value="checkup">体检提醒</option>
          </select>
        </div>
        <div class="mb-4">
          <label class="block text-sm font-medium text-gray-700 mb-1">提醒时间</label>
          <input 
            type="datetime-local" 
            v-model="formData.datetime"
            class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary" 
            required
          >
        </div>
        <div class="mb-6">
          <label class="block text-sm font-medium text-gray-700 mb-1">备注信息</label>
          <textarea 
            v-model="formData.description"
            class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary" 
            rows="3" 
            placeholder="添加备注信息，如服用剂量、注意事项等"
          ></textarea>
        </div>
        <div class="flex gap-3">
          <button type="button" @click="close" class="flex-1 btn btn-secondary">取消</button>
          <button type="submit" class="flex-1 btn btn-primary">保存提醒</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import { ref, watch, computed } from 'vue'

export default {
  name: 'ReminderModal',
  props: {
    show: {
      type: Boolean,
      required: true
    },
    reminder: {
      type: Object,
      default: null
    }
  },
  emits: ['close', 'save'],
  setup(props, { emit }) {
    const formData = ref({
      id: null,
      title: '',
      category: 'medication',
      datetime: '',
      description: ''
    })

    const modalTitle = computed(() => 
      props.reminder ? '编辑健康提醒' : '添加健康提醒'
    )

    watch(() => props.show, (newVal) => {
      if (newVal) {
        if (props.reminder) {
          // 编辑模式
          formData.value = { ...props.reminder }
          // 转换时间格式
          if (props.reminder.dateTime) {
            formData.value.datetime = formatDateTimeLocal(props.reminder.dateTime)
          }
        } else {
          // 添加模式
          resetForm()
          // 设置默认时间为当前时间
          const now = new Date()
          formData.value.datetime = formatDateTimeLocal(now)
        }
      }
    })

    const formatDateTimeLocal = (date) => {
      return date.toISOString().slice(0, 16)
    }

    const resetForm = () => {
      formData.value = {
        id: null,
        title: '',
        category: 'medication',
        datetime: '',
        description: ''
      }
    }

    const close = () => {
      emit('close')
      resetForm()
    }

    const handleBackdropClick = (event) => {
      if (event.target === event.currentTarget) {
        close()
      }
    }

    const handleSubmit = () => {
      emit('save', formData.value)
      resetForm()
    }

    return {
      formData,
      modalTitle,
      close,
      handleBackdropClick,
      handleSubmit
    }
  }
}
</script>