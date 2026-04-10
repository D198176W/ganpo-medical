<template>
  <div v-if="show" class="fixed inset-0 bg-black/50 z-50 flex items-center justify-center opacity-100 transition-opacity duration-300" @click.self="$emit('close')">
    <div class="bg-white rounded-xl shadow-lg w-full max-w-md p-5 transform transition-all scale-100 opacity-100">
      <div class="text-center mb-6">
        <div class="w-16 h-16 bg-danger/10 rounded-full flex items-center justify-center text-danger mx-auto mb-4">
          <i class="fa fa-phone text-3xl"></i>
        </div>
        <h3 class="text-xl font-semibold mb-2">紧急呼叫</h3>
        <p class="text-gray-600">请选择需要呼叫的紧急服务</p>
      </div>
      <div class="space-y-3 mb-6">
        <button 
          v-for="service in emergencyServices"
          :key="service.number"
          class="w-full flex items-center justify-between p-4 rounded-lg text-left transition-colors"
          :class="service.bgColor"
          @click="handleCall(service.number, service.name)"
        >
          <div>
            <div class="font-medium">{{ service.name }}</div>
            <div class="text-sm text-gray-600">{{ service.description }}</div>
          </div>
          <div :class="service.numberColor">{{ service.displayNumber }}</div>
        </button>
      </div>
      <button class="w-full btn btn-secondary" @click="$emit('close')">
        取消
      </button>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'

export default {
  name: 'EmergencyCallModal',
  props: {
    show: {
      type: Boolean,
      default: false
    }
  },
  emits: ['close', 'call'],
  setup(props, { emit }) {
    const emergencyServices = ref([
      {
        name: '消防救援',
        number: '119',
        displayNumber: '119',
        description: '火灾、爆炸等紧急情况',
        bgColor: 'bg-red-50 hover:bg-red-100',
        numberColor: 'text-danger text-xl font-bold'
      },
      {
        name: '医疗急救',
        number: '120',
        displayNumber: '120',
        description: '重伤、急病等医疗紧急情况',
        bgColor: 'bg-blue-50 hover:bg-blue-100',
        numberColor: 'text-primary text-xl font-bold'
      },
      {
        name: '报警电话',
        number: '110',
        displayNumber: '110',
        description: '盗窃、滋扰等治安事件',
        bgColor: 'bg-gray-50 hover:bg-gray-100',
        numberColor: 'text-dark text-xl font-bold'
      },
      {
        name: '园区安保',
        number: '0791-88669999',
        displayNumber: '0791-88669999',
        description: '园区内安全问题',
        bgColor: 'bg-yellow-50 hover:bg-yellow-100',
        numberColor: 'text-warning text-lg font-bold'
      }
    ])

    const handleCall = (number, serviceName) => {
      emit('call', number, serviceName)
    }

    return {
      emergencyServices,
      handleCall
    }
  }
}
</script>

<style scoped>
.btn-secondary {
  background-color: #f5f5f5; /* light */
  color: #333; /* dark */
  padding: 0.5rem 1rem;
  border-radius: 0.5rem;
  font-weight: 500;
  transition: all 0.2s ease-in-out;
}

.btn-secondary:hover {
  background-color: #e5e5e5; /* gray-200 */
}

.btn-secondary:active {
  transform: scale(0.95);
}
</style>