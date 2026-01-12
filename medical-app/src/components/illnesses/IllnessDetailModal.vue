<template>
  <Modal 
    :show="show"
    @close="$emit('close')"
    :title="illness?.title || '疾病详情'"
  >
    <template #body>
      <div v-if="illness" class="space-y-5">
        <div>
          <h4 class="text-base font-semibold text-gray-800 mb-2">疾病概述</h4>
          <p class="text-gray-600">{{ illness.overview }}</p>
        </div>
        
        <div>
          <h4 class="text-base font-semibold text-gray-800 mb-2">典型症状</h4>
          <ul class="space-y-1 text-gray-600">
            <li 
              v-for="symptom in illness.symptomsDetail"
              :key="symptom"
              class="flex items-start"
            >
              <i class="fa fa-circle text-xs text-primary mt-1.5 mr-2"></i>
              <span>{{ symptom }}</span>
            </li>
          </ul>
        </div>
        
        <div>
          <h4 class="text-base font-semibold text-gray-800 mb-2">处理建议</h4>
          <ul class="space-y-1 text-gray-600">
            <li 
              v-for="treatment in illness.treatment"
              :key="treatment"
              class="flex items-start"
            >
              <i class="fa fa-check-circle text-xs text-secondary mt-1.5 mr-2"></i>
              <span>{{ treatment }}</span>
            </li>
          </ul>
        </div>
        
        <div>
          <h4 class="text-base font-semibold text-gray-800 mb-2">预防措施</h4>
          <ul class="space-y-1 text-gray-600">
            <li 
              v-for="prevention in illness.prevention"
              :key="prevention"
              class="flex items-start"
            >
              <i class="fa fa-shield text-xs text-accent mt-1.5 mr-2"></i>
              <span>{{ prevention }}</span>
            </li>
          </ul>
        </div>
        
        <div class="bg-blue-50 p-4 rounded-lg">
          <h4 class="text-base font-semibold text-primary mb-2">校园小贴士</h4>
          <p class="text-gray-700">{{ illness.campusTips }}</p>
        </div>
      </div>
    </template>
    <template #footer>
      <button 
        @click="$emit('close')"
        class="w-full btn btn-primary"
      >
        <i class="fa fa-check mr-1"></i> 我知道了
      </button>
    </template>
  </Modal>
</template>

<script setup>
import Modal from '@/components/common/Modal.vue'

defineProps({
  show: {
    type: Boolean,
    default: false
  },
  illness: {
    type: Object,
    default: null
  }
})

defineEmits(['close'])
</script>