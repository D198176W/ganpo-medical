<template>
  <div 
    v-if="show"
    class="fixed inset-0 bg-black/50 z-50 flex items-center justify-center transition-opacity duration-300"
    :class="show ? 'opacity-100' : 'opacity-0'"
    @click="$emit('close')"
  >
    <div 
      class="bg-white rounded-xl shadow-lg w-full max-w-2xl max-h-[90vh] overflow-y-auto transform transition-all"
      :class="show ? 'scale-100 opacity-100' : 'scale-95 opacity-0'"
      @click.stop
    >
      <div class="sticky top-0 bg-white z-10 p-5 border-b flex justify-between items-center">
        <h3 class="text-xl font-semibold">{{ title }}</h3>
        <button 
          @click="$emit('close')"
          class="text-gray-400 hover:text-gray-600 transition-colors p-2"
          aria-label="关闭弹窗"
        >
          <i class="fa fa-times text-xl"></i>
        </button>
      </div>
      <div class="p-5">
        <slot name="body">
          <slot></slot>
        </slot>
      </div>
      <div v-if="$slots.footer" class="sticky bottom-0 bg-white z-10 p-5 border-t">
        <slot name="footer"></slot>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  show: {
    type: Boolean,
    default: false
  },
  title: {
    type: String,
    default: ''
  }
})

defineEmits(['close'])
</script>

<!-- <template>
  <div v-if="show" class="modal-overlay" @click.self="$emit('close')">
    <div class="modal-content">
      <div class="modal-header">
        <h3 class="modal-title">{{ title }}</h3>
        <button class="modal-close" @click="$emit('close')">&times;</button>
      </div>
      <div class="modal-body">
        <slot></slot>
      </div>
      <div v-if="$slots.footer" class="modal-footer">
        <slot name="footer"></slot>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Modal',
  props: {
    show: {
      type: Boolean,
      default: false
    },
    title: {
      type: String,
      default: ''
    }
  },
  emits: ['close']
}
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  border-radius: 12px;
  padding: 25px;
  width: 90%;
  max-width: 500px;
  max-height: 80vh;
  overflow-y: auto;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.modal-title {
  font-size: 18px;
  font-weight: 600;
}

.modal-close {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #666;
}

.modal-body {
  line-height: 1.6;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
}
</style> -->  