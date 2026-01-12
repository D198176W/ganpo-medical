<template>
  <div class="emergency-card" :class="category.borderColor" @click="$emit('click')">
    <div class="flex justify-between items-start mb-3">
      <div class="flex items-center gap-3">
        <div class="w-12 h-12 rounded-full" :class="iconBgColor">
          <i class="fa text-xl" :class="[category.icon, textColor]"></i>
        </div>
        <div>
          <h3 class="font-medium text-lg">{{ category.title }}</h3>
          <p class="text-sm text-gray-500">{{ category.description }}</p>
        </div>
      </div>
      <i class="fa fa-chevron-right text-gray-400"></i>
    </div>
    <div class="text-sm text-gray-600">
      {{ category.detail }}
    </div>
  </div>
</template>

<script>
import { computed } from 'vue'

export default {
  name: 'EmergencyCategoryCard',
  props: {
    category: {
      type: Object,
      required: true
    }
  },
  emits: ['click'],
  setup(props) {
    const iconBgColor = computed(() => {
      const colorMap = {
        danger: 'bg-red-100',
        secondary: 'bg-green-100',
        warning: 'bg-yellow-100',
        primary: 'bg-blue-100'
      }
      return colorMap[props.category.color] || 'bg-gray-100'
    })

    const textColor = computed(() => {
      const colorMap = {
        danger: 'text-danger',
        secondary: 'text-secondary',
        warning: 'text-warning',
        primary: 'text-primary'
      }
      return colorMap[props.category.color] || 'text-gray-600'
    })

    return {
      iconBgColor,
      textColor
    }
  }
}
</script>

<style scoped>
.emergency-card {
  background-color: #fff;
  border-radius: 0.75rem;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
  padding: 1.25rem;
  transition: all 0.3s ease;
  cursor: pointer;
  border-left-width: 4px;
}

.emergency-card:hover {
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
  transform: translateY(-0.25rem);
}
</style>