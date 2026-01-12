<template>
  <div class="illness-card" @click="$emit('click')">
    <div class="flex justify-between items-start mb-3">
      <div class="flex items-center gap-3">
        <div :class="`w-12 h-12 rounded-full ${illness.iconBg} flex items-center justify-center ${illness.iconColor}`">
          <i :class="`${illness.icon} text-xl`"></i>
        </div>
        <div>
          <h3 class="font-medium text-lg">{{ illness.title }}</h3>
          <p class="text-sm text-gray-500">{{ illness.subtitle }}</p>
        </div>
      </div>
      <span :class="`text-xs ${illness.tagBg} ${illness.tagColor} px-2 py-1 rounded-full`">
        {{ illness.tag }}
      </span>
    </div>
    <div class="mb-3">
      <div class="text-sm text-gray-600 mb-1">典型症状：</div>
      <div>
        <span 
          v-for="symptom in illness.symptoms" 
          :key="symptom"
          class="symptom-tag"
        >
          {{ symptom }}
        </span>
      </div>
    </div>
    <button 
      @click.stop="$emit('click')"
      class="w-full btn btn-primary mt-2"
    >
      <i class="fa fa-info-circle mr-1"></i> 查看详情
    </button>
  </div>
</template>

<script setup>
defineProps({
  illness: {
    type: Object,
    required: true
  }
})

defineEmits(['click'])
</script>

<style scoped>
.illness-card {
  background-color: white;
  border-radius: 0.75rem;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
  padding: 1.25rem;
  transition: all 0.3s;
  cursor: pointer;
}

.illness-card:hover {
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
  transform: translateY(-0.25rem);
}

.symptom-tag {
  display: inline-block;
  padding-left: 0.5rem;
  padding-right: 0.5rem;
  padding-top: 0.25rem;
  padding-bottom: 0.25rem;
  background-color: #DBEAFE;
  color: #3B82F6;
  font-size: 0.75rem;
  border-radius: 9999px;
  margin-right: 0.5rem;
  margin-bottom: 0.5rem;
}
</style>