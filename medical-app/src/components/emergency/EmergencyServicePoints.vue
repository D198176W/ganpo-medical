<template>
  <div class="bg-white rounded-xl shadow-md p-5">
    <h2 class="text-lg font-semibold mb-4 flex items-center">
      <i class="fa fa-ambulance text-danger mr-2"></i> 最近应急服务点
    </h2>
    <div class="space-y-4">
      <div 
        v-for="service in emergencyServices"
        :key="service.id"
        class="flex items-start gap-4 p-3 hover:bg-gray-50 rounded-lg transition-colors"
      >
        <div class="w-10 h-10 rounded-full flex items-center justify-center flex-shrink-0" :class="service.iconBg">
          <i :class="service.icon"></i>
        </div>
        <div class="flex-1">
          <div class="flex justify-between">
            <h3 class="font-medium">{{ service.name }}</h3>
            <span class="text-sm text-gray-500">距离 {{ service.distance }}</span>
          </div>
          <p class="text-sm text-gray-600 mt-1">{{ service.description }}</p>
          <div class="flex gap-2 mt-2">
            <button 
              class="btn btn-primary text-sm px-3 py-1"
              @click="$emit('call', service.phone, service.name)"
            >
              <i class="fa fa-phone mr-1"></i> 呼叫
            </button>
            <button 
              class="btn btn-secondary text-sm px-3 py-1"
              @click="$emit('navigate', service.name)"
            >
              <i class="fa fa-map-marker mr-1"></i> 导航
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'

export default {
  name: 'EmergencyServicePoints',
  emits: ['call', 'navigate'],
  setup() {
    const emergencyServices = ref([
      {
        id: 1,
        name: '园区医务室',
        distance: '200米',
        description: '1号楼一层东侧，24小时值班',
        phone: '0791-88669120',
        icon: 'fa fa-hospital-o',
        iconBg: 'bg-red-100 text-danger'
      },
      {
        id: 2,
        name: '园区保安亭',
        distance: '150米',
        description: '园区主入口处，24小时值班',
        phone: '0791-88669999',
        icon: 'fa fa-shield',
        iconBg: 'bg-blue-100 text-primary'
      },
      {
        id: 3,
        name: '附近医院',
        distance: '1.2公里',
        description: '南昌市第一人民医院，急诊科24小时开放',
        phone: '0791-88881120',
        icon: 'fa fa-ambulance',
        iconBg: 'bg-green-100 text-secondary'
      }
    ])

    return {
      emergencyServices
    }
  }
}
</script>

<!-- <style scoped>
.btn {
  @apply px-4 py-2 rounded-lg font-medium transition-all duration-200;
}
.btn-primary {
  @apply bg-primary text-white hover:bg-primary/90 active:scale-95;
}
.btn-secondary {
  @apply bg-light text-dark hover:bg-gray-200 active:scale-95;
}
</style> -->

<style scoped>
.btn {
  padding: 0.5rem 1rem;
  border-radius: 0.5rem;
  font-weight: 500;
  transition: all 0.2s ease;
}

.btn-primary {
  background-color: #007bff;
  color: white;
}

.btn-primary:hover {
  background-color: #0069d9;
  transform: scale(0.95);
}

.btn-secondary {
  background-color: #f8f9fa;
  color: #212529;
}

.btn-secondary:hover {
  background-color: #e2e6ea;
  transform: scale(0.95);
}
</style>