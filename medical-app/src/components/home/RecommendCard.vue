<template>
  <div class="recommend-card" @click="navigateTo(card.route)">
    <span class="card-header">{{ card.title }}</span>
    
    <!-- 健康提醒类型 -->
    <div v-if="card.type === 'tips'" class="tips-list">
      <div v-for="(tip, index) in card.items" :key="index" class="tip-item">
        <img :src="tip.icon" :alt="tip.text">
        <span>{{ tip.text }}</span>
      </div>
    </div>
    
    <!-- 校园常见病类型 -->
    <div v-else-if="card.type === 'illness'" class="illness-list">
      <div v-for="(illness, index) in card.items" :key="index" class="illness-item">
        <img :src="illness.icon" :alt="illness.text">
        <span>{{ illness.text }}</span>
      </div>
    </div>
    
    <!-- 应急指引类型 -->
    <div v-else-if="card.type === 'emergency'" class="emergency-list">
      <div v-for="(item, index) in card.items" :key="index" class="emergency-item">
        <img :src="item.icon" :alt="item.text">
        <span>{{ item.text }}</span>
      </div>
    </div>
    
    <!-- 默认类型 -->
    <div v-else class="default-list">
      <div v-for="(item, index) in card.items" :key="index" class="default-item">
        <img :src="item.icon" :alt="item.text">
        <span>{{ item.text }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const props = defineProps({
  card: {
    type: Object,
    required: true
  }
})

const router = useRouter()

const navigateTo = (route) => {
  if (route) {
    router.push(route)
  }
}
</script>

<style scoped>
.recommend-card {
  width: 100%;
  background-color: rgba(255, 255, 255, 0.8);
  border-radius: 16px;
  padding: 18px 20px;
  box-sizing: border-box;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  cursor: pointer;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.recommend-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
}

.card-header {
  font-size: 16px;
  font-weight: 600;
  color: #333333;
  margin-bottom: 16px;
  display: block;
}

/* 健康提醒列表 */
.tips-list {
  display: flex;
  gap: 12px;
  overflow-x: auto;
  padding-bottom: 8px;
}

.tip-item {
  min-width: calc(50% - 6px);
  height: 100px;
  background-color: rgba(240, 247, 255, 0.9);
  border-radius: 12px;
  padding: 16px;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.tip-item img {
  width: 36px;
  height: 36px;
}

.tip-item span {
  font-size: 14px;
  color: #333333;
  line-height: 1.4;
}

/* 校园常见病列表 */
.illness-list {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.illness-item {
  width: calc(33.33% - 8px);
  height: 90px;
  background-color: rgba(240, 255, 244, 0.9);
  border-radius: 12px;
  padding: 12px;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
}

.illness-item img {
  width: 32px;
  height: 32px;
  margin-bottom: 8px;
}

.illness-item span {
  font-size: 13px;
  color: #333333;
}

/* 创业园区应急指引 */
.emergency-list {
  display: flex;
  gap: 12px;
}

.emergency-item {
  flex: 1;
  height: 100px;
  background-color: rgba(255, 248, 225, 0.9);
  border-radius: 12px;
  padding: 16px;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.emergency-item img {
  width: 36px;
  height: 36px;
}

.emergency-item span {
  font-size: 14px;
  color: #333333;
  line-height: 1.4;
}

/* 默认推荐列表 */
.default-list {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.default-item {
  width: calc(33.33% - 8px);
  height: 90px;
  background-color: rgba(245, 245, 245, 0.9);
  border-radius: 12px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
}

.default-item img {
  width: 32px;
  height: 32px;
  margin-bottom: 8px;
}

.default-item span {
  font-size: 13px;
  color: #333333;
}

/* 滚动条样式 */
.tips-list::-webkit-scrollbar {
  height: 4px;
}

.tips-list::-webkit-scrollbar-thumb {
  background: #ccc;
  border-radius: 2px;
}
</style>