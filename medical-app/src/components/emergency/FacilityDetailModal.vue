<template>
  <div v-if="show" class="fixed inset-0 bg-black/50 z-50 flex items-center justify-center opacity-100 transition-opacity duration-300" @click.self="$emit('close')">
    <div class="bg-white rounded-xl shadow-lg w-full max-w-md p-5 transform transition-all scale-100 opacity-100">
      <div class="flex justify-between items-center mb-4">
        <h3 class="text-xl font-semibold">{{ facilityData?.name || '设施详情' }}</h3>
        <button @click="$emit('close')" class="text-gray-400 hover:text-gray-600 transition-colors" aria-label="关闭弹窗">
          <i class="fa fa-times text-xl"></i>
        </button>
      </div>
      <div class="mb-5">
        <p class="text-gray-600 mb-3">
          <i class="fa fa-map-marker text-primary mr-1"></i> 位置：{{ facilityData?.location }}
        </p>
        
        <p v-if="facilityData?.type" class="text-gray-600 mb-3">
          <i class="fa fa-tag text-gray-500 mr-1"></i> 类型：{{ facilityData.type }}
        </p>
        
        <div v-if="facilityData?.equipment" class="mb-3">
          <p class="text-gray-700 font-medium mb-1">配备设备：</p>
          <ul class="text-gray-600">
            <li v-for="(item, index) in facilityData.equipment" :key="index" class="flex items-center">
              <i class="fa fa-check text-secondary mr-2"></i>{{ item }}
            </li>
          </ul>
        </div>
        
        <p v-if="facilityData?.description" class="text-gray-600 mb-3">{{ facilityData.description }}</p>
        
        <p v-if="facilityData?.checkDate" class="text-gray-600 mb-3">
          <i class="fa fa-calendar text-gray-500 mr-1"></i> 最近检查：{{ facilityData.checkDate }}
        </p>

        <p v-if="facilityData?.function" class="text-gray-600 mb-3">
          <i class="fa fa-cogs text-gray-500 mr-1"></i> 功能：{{ facilityData.function.join('，') }}
        </p>

        <p v-if="facilityData?.staff" class="text-gray-600 mb-3">
          <i class="fa fa-users text-gray-500 mr-1"></i> 人员配置：{{ facilityData.staff }}
        </p>

        <p v-if="facilityData?.width" class="text-gray-600 mb-3">
          <i class="fa fa-arrows-h text-gray-500 mr-1"></i> 宽度：{{ facilityData.width }}
        </p>

        <p v-if="facilityData?.capacity" class="text-gray-600 mb-3">
          <i class="fa fa-users text-gray-500 mr-1"></i> 容纳能力：{{ facilityData.capacity }}
        </p>

        <p v-if="facilityData?.direction" class="text-gray-600 mb-3">
          <i class="fa fa-sign-out text-gray-500 mr-1"></i> 方向：{{ facilityData.direction }}
        </p>

        <p v-if="facilityData?.notes" class="text-gray-600 mb-3">
          <i class="fa fa-info-circle text-gray-500 mr-1"></i> 备注：{{ facilityData.notes }}
        </p>
      </div>
      <button class="w-full btn btn-secondary" @click="handleNavigate">
        <i class="fa fa-map-marker mr-1"></i> 导航前往
      </button>
    </div>
  </div>
</template>

<script>
import { computed } from 'vue'
import { facilityData } from '@/utils/facility-data'

export default {
  name: 'FacilityDetailModal',
  props: {
    show: {
      type: Boolean,
      default: false
    },
    facilityId: {
      type: String,
      default: ''
    }
  },
  emits: ['close', 'navigate'],
  setup(props, { emit }) {
    const facilityData = computed(() => {
      return props.facilityId ? facilityData[props.facilityId] : null
    })

    const handleNavigate = () => {
      if (facilityData.value) {
        emit('navigate', facilityData.value.name)
      }
    }

    return {
      facilityData,
      handleNavigate
    }
  }
}
</script>

<style scoped>
.btn-secondary {
  background-color: #f3f4f6; /* bg-light */
  color: #1f2937; /* text-dark */
  padding: 0.5rem 1rem;
  border-radius: 0.5rem;
  font-weight: 500;
  transition: all 0.2s ease;
}

.btn-secondary:hover {
  background-color: #e5e7eb; /* hover:bg-gray-200 */
}

.btn-secondary:active {
  transform: scale(0.95); /* active:scale-95 */
}
</style>