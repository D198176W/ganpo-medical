<template>
  <Modal 
    :show="show"
    @close="$emit('close')"
    title="紧急联系电话"
  >
    <template #body>
      <div class="space-y-4">
        <div 
          v-for="contact in emergencyContacts"
          :key="contact.name"
          :class="`flex items-center justify-between p-3 ${contact.bgColor} rounded-lg`"
        >
          <div>
            <h4 class="font-medium text-gray-800">{{ contact.name }}</h4>
            <p class="text-gray-600 text-sm">{{ contact.description }}</p>
          </div>
          <button 
            @click="$emit('call', contact.number)"
            :class="`${contact.textColor} text-lg font-bold`"
          >
            {{ contact.number }}
          </button>
        </div>
      </div>
    </template>
    <template #footer>
      <button 
        @click="$emit('close')"
        class="w-full btn btn-secondary"
      >
        <i class="fa fa-times mr-1"></i> 关闭
      </button>
    </template>
  </Modal>
</template>

<script setup>
import { useEmergencyData } from '@/composables/useEmergencyData'
import Modal from '@/components/common/Modal.vue'

const { emergencyContacts } = useEmergencyData()

defineProps({
  show: {
    type: Boolean,
    default: false
  }
})

defineEmits(['close', 'call'])
</script>