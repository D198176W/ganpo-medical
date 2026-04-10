<template>
  <div class="bg-white rounded-xl shadow-md p-5 mb-6 border-l-4 border-danger">
    <h2 class="text-lg font-semibold mb-3 flex items-center">
      <i class="fa fa-ambulance text-danger mr-2"></i> 紧急就医指引
    </h2>
    <p class="text-gray-600 mb-4">如出现以下情况，请立即前往校医院或附近医院就诊：</p>
    <ul class="space-y-2 text-gray-600 mb-4">
      <li 
        v-for="(warning, index) in emergencyWarnings"
        :key="index"
        class="flex items-start"
      >
        <i class="fa fa-exclamation-circle text-danger mt-1 mr-2"></i>
        <span>{{ warning }}</span>
      </li>
    </ul>
    <div class="flex gap-3">
      <button 
        @click="$emit('show-hospital')"
        class="flex-1 btn btn-primary"
      >
        <i class="fa fa-hospital-o mr-1"></i> 校医院信息
      </button>
      <button 
        @click="$emit('show-emergency')"
        class="flex-1 btn btn-danger"
      >
        <i class="fa fa-phone mr-1"></i> 紧急电话
      </button>
    </div>
  </div>
</template>

<script setup>
import { useEmergencyData } from '@/composables/useEmergencyData'

const { emergencyWarnings } = useEmergencyData()

defineEmits(['show-hospital', 'show-emergency'])
</script>