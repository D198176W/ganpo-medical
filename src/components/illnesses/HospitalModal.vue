<template>
  <Modal 
    :show="show"
    @close="$emit('close')"
    title="校医院信息"
  >
    <template #body>
      <div class="space-y-4">
        <div>
          <h4 class="font-medium text-gray-800 mb-1">地址</h4>
          <p class="text-gray-600">校园内东区，靠近第三食堂</p>
        </div>
        <div>
          <h4 class="font-medium text-gray-800 mb-1">门诊时间</h4>
          <p class="text-gray-600">周一至周五: 8:00-18:00</p>
          <p class="text-gray-600">周六至周日: 9:00-16:00</p>
        </div>
        <div>
          <h4 class="font-medium text-gray-800 mb-1">联系电话</h4>
          <p class="text-gray-600">0791-88776655</p>
        </div>
        <div>
          <h4 class="font-medium text-gray-800 mb-1">急诊电话</h4>
          <p class="text-gray-600">0791-88776699</p>
        </div>
      </div>
    </template>
    <template #footer>
      <button 
        @click="$emit('navigate')"
        class="w-full btn btn-primary"
      >
        <i class="fa fa-map-marker mr-1"></i> 导航前往
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
  }
})

defineEmits(['close', 'navigate'])
</script>