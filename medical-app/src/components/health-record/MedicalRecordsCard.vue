<template>
  <div class="health-card">
    <div class="card-header">
      <h3 class="card-title">最近医疗记录</h3>
      <button class="action-btn btn-outline" @click="$emit('view-all')">
        <i class="fas fa-list"></i> 查看全部
      </button>
    </div>
    <div class="record-list">
      <div 
        v-for="record in records" 
        :key="record.id"
        class="record-item"
      >
        <div class="record-info">
          <h4>{{ record.title }}</h4>
          <p>{{ record.description }}</p>
        </div>
        <div class="record-date">{{ record.date }}</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'MedicalRecordsCard',
  props: {
    records: {
      type: Array,
      required: true
    }
  },
  emits: ['view-all']
}
</script>

<style scoped>
.health-card {
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

.record-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.record-item {
  background: var(--light);
  border-radius: 8px;
  padding: 15px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.record-info h4 {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 5px;
}

.record-info p {
  font-size: 14px;
  color: var(--gray);
}

.record-date {
  font-size: 12px;
  color: var(--gray);
}
</style>