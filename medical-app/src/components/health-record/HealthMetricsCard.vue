<template>
  <div class="health-card">
    <div class="card-header">
      <h3 class="card-title">健康指标</h3>
      <div class="card-actions">
        <button class="action-btn btn-outline" @click="$emit('add-record')">
          <i class="fas fa-plus"></i> 添加记录
        </button>
        <button class="action-btn btn-primary" @click="$emit('view-trend')">
          <i class="fas fa-chart-line"></i> 查看趋势
        </button>
      </div>
    </div>
    <div class="health-grid">
      <div 
        v-for="metric in metrics" 
        :key="metric.label"
        class="health-item"
      >
        <div class="health-value">{{ metric.value }}</div>
        <div class="health-label">{{ metric.label }}</div>
        <div class="health-status" :class="`status-${metric.status}`">
          {{ metric.statusText }}
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'HealthMetricsCard',
  props: {
    metrics: {
      type: Array,
      required: true
    }
  },
  emits: ['add-record', 'view-trend']
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

.card-actions {
  display: flex;
  gap: 10px;
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

.btn-primary {
  background: var(--primary);
  color: white;
}

.btn-primary:hover {
  background: var(--primary-dark);
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

.health-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 15px;
}

.health-item {
  background: var(--light);
  border-radius: 8px;
  padding: 15px;
  text-align: center;
}

.health-value {
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 5px;
  color: var(--primary);
}

.health-label {
  font-size: 12px;
  color: var(--gray);
}

.health-status {
  font-size: 12px;
  margin-top: 5px;
  padding: 2px 8px;
  border-radius: 10px;
  display: inline-block;
}

.status-normal {
  background: #D1ECF1;
  color: #0C5460;
}

.status-warning {
  background: #FFF3CD;
  color: #856404;
}

.status-danger {
  background: #F8D7DA;
  color: #721C24;
}

@media (max-width: 600px) {
  .health-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .card-actions {
    flex-direction: column;
  }
}
</style>