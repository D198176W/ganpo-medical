<template>
  <div v-if="show" class="fixed inset-0 bg-black/50 z-50 flex items-center justify-center opacity-100 transition-opacity duration-300" @click.self="$emit('close')">
    <div class="bg-white rounded-xl shadow-lg w-full max-w-2xl max-h-[90vh] overflow-y-auto transform transition-all scale-100 opacity-100">
      <div class="sticky top-0 bg-white z-10 p-5 border-b flex justify-between items-center">
        <h3 class="text-xl font-semibold">{{ emergencyData?.title || '应急处理详情' }}</h3>
        <button @click="$emit('close')" class="text-gray-400 hover:text-gray-600 transition-colors p-2" aria-label="关闭弹窗">
          <i class="fa fa-times text-xl"></i>
        </button>
      </div>
      <div class="p-5">
        <div v-if="emergencyData">
          <!-- 详情内容 -->
          <div class="mb-6">
            <h4 class="text-base font-semibold text-gray-800 mb-2">情况概述</h4>
            <p class="text-gray-600">{{ emergencyData.overview }}</p>
          </div>
          
          <div class="mb-6">
            <h4 class="text-base font-semibold text-gray-800 mb-2">应急处理步骤</h4>
            <div class="space-y-3">
              <div v-for="(step, index) in emergencyData.steps" :key="index" class="step-item">
                <div class="step-number">{{ index + 1 }}</div>
                <div class="text-gray-600">{{ step }}</div>
              </div>
            </div>
          </div>

          <!-- 根据类型显示额外内容 -->
          <div v-if="emergencyType === 'fire'" class="mb-6">
            <h4 class="text-base font-semibold text-gray-800 mb-2">预防措施</h4>
            <ul class="space-y-2 text-gray-600">
              <li v-for="(item, index) in emergencyData.precautions" :key="index" class="flex items-start">
                <i class="fa fa-check-circle text-secondary mt-1 mr-2"></i>
                <span>{{ item }}</span>
              </li>
            </ul>
          </div>

          <!-- 其他类型的额外内容... -->
        </div>
      </div>
      <div class="sticky bottom-0 bg-white z-10 p-5 border-t">
        <button @click="$emit('close')" class="w-full btn btn-primary">
          <i class="fa fa-check mr-1"></i> 我知道了
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import { computed } from 'vue'
import { emergencyData } from '@/utils/emergency-data'

export default {
  name: 'EmergencyDetailModal',
  props: {
    show: {
      type: Boolean,
      default: false
    },
    emergencyType: {
      type: String,
      default: ''
    }
  },
  emits: ['close'],
  setup(props) {
    const emergencyData = computed(() => {
      return props.emergencyType ? emergencyData[props.emergencyType] : null
    })

    return {
      emergencyData
    }
  }
}
</script>

<!-- <style scoped>
.step-item {
  @apply flex items-start mb-4 last:mb-0;
}
.step-number {
  @apply w-6 h-6 rounded-full bg-primary text-white flex items-center justify-center text-sm font-bold mr-3 mt-0.5 flex-shrink-0;
}
.btn {
  @apply px-4 py-2 rounded-lg font-medium transition-all duration-200;
}
.btn-primary {
  @apply bg-primary text-white hover:bg-primary/90 active:scale-95;
}
</style> -->


<style scoped>
.step-item {
  display: flex;
  align-items: flex-start;
  margin-bottom: 1rem;
}

.step-item:last-child {
  margin-bottom: 0;
}

.step-number {
  width: 1.5rem;
  height: 1.5rem;
  border-radius: 50%;
  background-color: #3b82f6; /* primary color */
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.875rem;
  font-weight: bold;
  margin-right: 0.75rem;
  margin-top: 0.125rem;
  flex-shrink: 0;
}

.btn {
  padding: 0.5rem 1rem;
  border-radius: 0.5rem;
  font-weight: 500;
  transition: all 0.2s;
}

.btn-primary {
  background-color: #3b82f6; /* primary */
  color: white;
}

.btn-primary:hover {
  background-color: #2563eb; /* primary/90 */
}

.btn-primary:active {
  transform: scale(0.95);
}
</style>