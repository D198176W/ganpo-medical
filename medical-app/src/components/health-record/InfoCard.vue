<template>
  <div class="info-card">
    <div class="card-header">
      <h3 class="card-title">{{ title }}</h3>
      <button class="action-btn btn-outline" @click="$emit('edit')">
        <i class="fas fa-edit"></i> 编辑
      </button>
    </div>
    <div class="info-content">
      <p>{{ content }}</p>
      <div class="info-tags">
        <span 
          v-for="tag in tags" 
          :key="tag"
          class="info-tag"
        >
          {{ tag }}
        </span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'InfoCard',
  props: {
    title: {
      type: String,
      required: true
    },
    content: {
      type: String,
      required: true
    },
    tags: {
      type: Array,
      default: () => []
    }
  },
  emits: ['edit']
}
</script>

<style scoped>
.info-card {
  background: var(--card);
  border-radius: 12px;
  padding: 20px;
  margin: 0 20px 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.card-title {
  font-size: 18px;
  font-weight: 600;
  color: var(--dark);
}

.action-btn {
  padding: 6px 12px;
  border-radius: 6px;
  font-size: 12px;
  cursor: pointer;
  transition: all 0.3s;
  border: none;
  display: flex;
  align-items: center;
  gap: 5px;
}

.btn-outline {
  background: transparent;
  border: 1px solid var(--primary);
  color: var(--primary);
}

.btn-outline:hover {
  background: var(--primary);
  color: white;
}

.info-content {
  font-size: 14px;
  line-height: 1.6;
}

.info-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 10px;
}

.info-tag {
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 12px;
  background: var(--light);
  color: var(--gray);
}
</style>