<template>
  <div class="bg-white rounded-xl shadow-md p-5 mb-8">
    <h2 class="text-lg font-semibold mb-4 flex items-center">
      <i class="fa fa-map-marker text-primary mr-2"></i> 应急设施分布
    </h2>
    <div class="relative bg-gray-100 rounded-lg overflow-hidden h-64 md:h-80 mb-4">
      <!-- 简化的园区地图 -->
      <img src="https://picsum.photos/id/1019/800/400" alt="创业园区地图" class="w-full h-full object-cover opacity-60">
      
      <!-- 地图标记点 -->
      <div 
        v-for="marker in mapMarkers" 
        :key="marker.id"
        class="map-marker" 
        :style="marker.style"
        @click="handleMarkerClick(marker.id)"
      >
        <i :class="marker.icon"></i>
      </div>
    </div>
    <div class="grid grid-cols-2 md:grid-cols-5 gap-2 text-sm">
      <div class="flex items-center gap-1">
        <i class="fa fa-fire-extinguisher text-danger"></i>
        <span>灭火器</span>
      </div>
      <div class="flex items-center gap-1">
        <i class="fa fa-medkit text-secondary"></i>
        <span>急救点</span>
      </div>
      <div class="flex items-center gap-1">
        <i class="fa fa-shield text-warning"></i>
        <span>保安室</span>
      </div>
      <div class="flex items-center gap-1">
        <i class="fa fa-sign-out text-primary"></i>
        <span>疏散出口</span>
      </div>
      <div class="flex items-center gap-1">
        <i class="fa fa-phone text-purple-600"></i>
        <span>紧急电话</span>
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'

export default {
  name: 'EmergencyFacilityMap',
  emits: ['marker-click'],
  setup(props, { emit }) {
    const mapMarkers = ref([
      {
        id: 'fire-extinguisher-1',
        style: { top: '30%', left: '60%' },
        icon: 'fa fa-fire-extinguisher text-xs'
      },
      {
        id: 'fire-extinguisher-2',
        style: { top: '60%', left: '70%' },
        icon: 'fa fa-fire-extinguisher text-xs'
      },
      {
        id: 'first-aid',
        style: { top: '45%', left: '50%' },
        icon: 'fa fa-medkit text-xs'
      },
      {
        id: 'security-room',
        style: { top: '20%', left: '80%' },
        icon: 'fa fa-shield text-xs'
      },
      {
        id: 'evacuation-exit',
        style: { top: '70%', left: '30%' },
        icon: 'fa fa-sign-out text-xs'
      }
    ])

    const handleMarkerClick = (facilityId) => {
      emit('marker-click', facilityId)
    }

    return {
      mapMarkers,
      handleMarkerClick
    }
  }
}
</script>

<!-- <style scoped>
.map-marker {
  @apply absolute w-6 h-6 bg-danger text-white rounded-full flex items-center justify-center cursor-pointer transform -translate-x-1/2 -translate-y-1/2 hover:scale-110 transition-transform;
}
</style> -->

<style scoped>
.map-marker {
  position: absolute;
  width: 1.5rem;
  height: 1.5rem;
  background-color: #ef4444; /* tailwind 的 danger 色 */
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transform: translate(-50%, -50%);
  transition: transform 0.2s ease;
}
.map-marker:hover {
  transform: translate(-50%, -50%) scale(1.1);
}
</style>