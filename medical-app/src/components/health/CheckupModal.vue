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
        <h3 class="text-xl font-semibold">体检检查项</h3>
        <button @click="close" class="text-gray-400 hover:text-gray-600 transition-colors">
          <i class="fa fa-times text-xl"></i>
        </button>
      </div>
      <div class="space-y-3">
        <div 
          v-for="item in checkupItems" 
          :key="item.id"
          class="flex items-center"
        >
          <input 
            type="checkbox" 
            :id="'checkup' + item.id" 
            v-model="item.checked"
            class="w-4 h-4 text-primary"
          >
          <label :for="'checkup' + item.id" class="ml-2 text-sm">{{ item.name }}</label>
        </div>
      </div>
      <div class="mt-6">
        <button @click="close" class="w-full btn btn-primary">完成</button>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, watch } from 'vue'

export default {
  name: 'CheckupModal',
  props: {
    show: {
      type: Boolean,
      required: true
    }
  },
  emits: ['close'],
  setup(props, { emit }) {
    const checkupItems = ref([
      { id: 1, name: '身高体重测量', checked: false },
      { id: 2, name: '血常规检查', checked: false },
      { id: 3, name: '肝功能检查', checked: false },
      { id: 4, name: '肾功能检查', checked: false },
      { id: 5, name: '胸部X光', checked: false },
      { id: 6, name: '心电图', checked: false }
    ])

    const close = () => {
      emit('close')
    }

    const handleBackdropClick = (event) => {
      if (event.target === event.currentTarget) {
        close()
      }
    }

    watch(() => props.show, (newVal) => {
      if (!newVal) {
        // 重置复选框状态
        checkupItems.value.forEach(item => {
          item.checked = false
        })
      }
    })

    return {
      checkupItems,
      close,
      handleBackdropClick
    }
  }
}
</script>