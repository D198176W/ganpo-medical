<template>
  <div class="personal-favorites">
    <div class="bg-container">
      <img src="/src/assets/images/personal-bg.png" alt="背景" class="bg-img">
    </div>
    <NavBar title="我的收藏" :show-user="false" />
    
    <div class="content">
      <div class="favorites-tabs">
        <div 
          v-for="(tab, index) in favoriteTabs" 
          :key="index"
          class="tab-item"
          :class="{ active: activeTab === tab.id }"
          @click="activeTab = tab.id"
        >
          {{ tab.name }}
        </div>
      </div>

      <div class="favorites-list">
        <div v-if="favorites.length === 0" class="empty-state">
          <div class="empty-icon">❤️</div>
          <div>暂无收藏内容</div>
          <div class="empty-hint">浏览内容时点击收藏按钮</div>
        </div>

        <div v-else class="favorites-grid">
          <div v-for="item in favorites" :key="item.id" class="favorite-card" @click="viewItem(item)">
            <div class="favorite-icon" :class="item.bgClass">
              <i :class="item.icon"></i>
            </div>
            <div class="favorite-info">
              <span class="favorite-title">{{ item.title }}</span>
              <span class="favorite-desc">{{ item.desc }}</span>
              <span class="favorite-time">{{ item.time }}</span>
            </div>
            <button class="remove-btn" @click.stop="removeFavorite(item.id)">
              <i class="fas fa-trash-alt"></i>
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import NavBar from '@/components/common/NavBar.vue'

const activeTab = ref('all')

const favoriteTabs = ref([
  { id: 'all', name: '全部' },
  { id: 'illness', name: '疾病' },
  { id: 'resource', name: '资源' },
  { id: 'article', name: '文章' }
])

const favorites = ref([
  {
    id: 1,
    title: '普通感冒',
    desc: '常见呼吸道疾病',
    time: '2024-03-10',
    icon: 'fas fa-virus',
    bgClass: 'bg-blue',
    category: 'illness'
  },
  {
    id: 2,
    title: '南昌大学一附院',
    desc: '三级甲等医院',
    time: '2024-03-08',
    icon: 'fas fa-hospital',
    bgClass: 'bg-green',
    category: 'resource'
  }
])

const viewItem = (item) => {
  console.log('查看收藏:', item)
}

const removeFavorite = (id) => {
  favorites.value = favorites.value.filter(item => item.id !== id)
}
</script>

<style scoped>
.personal-favorites {
  width: 100%;
  min-height: 100vh;
  background-color: transparent;
  padding-bottom: 20px;
  margin: 0;
  padding-top: 50px;
}

.bg-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: -1;
  overflow: hidden;
}

.bg-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  opacity: 0.3;
}

.content {
  padding: 20px;
}

.favorites-tabs {
  display: flex;
  gap: 8px;
  margin-bottom: 20px;
  overflow-x: auto;
  padding-bottom: 4px;
}

.tab-item {
  padding: 8px 16px;
  background: rgba(255, 255, 255, 0.6);
  border-radius: 20px;
  font-size: 14px;
  color: #666;
  cursor: pointer;
  white-space: nowrap;
  transition: all 0.3s ease;
}

.tab-item:hover {
  background: rgba(255, 255, 255, 0.8);
}

.tab-item.active {
  background: linear-gradient(135deg, #1E88E5, #64B5F6);
  color: white;
  font-weight: 600;
}

.favorites-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.favorites-grid {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.favorite-card {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 16px;
  padding: 16px;
  display: flex;
  align-items: center;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  cursor: pointer;
  position: relative;
  transition: transform 0.2s;
}

.favorite-card:hover {
  transform: translateY(-2px);
}

.favorite-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  font-size: 20px;
  color: white;
  flex-shrink: 0;
}

.favorite-icon.bg-blue {
  background: linear-gradient(135deg, #1E88E5, #64B5F6);
}

.favorite-icon.bg-green {
  background: linear-gradient(135deg, #4CAF50, #A5D6A7);
}

.favorite-info {
  flex: 1;
  overflow: hidden;
}

.favorite-title {
  display: block;
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 4px;
}

.favorite-desc {
  display: block;
  font-size: 14px;
  color: #666;
  margin-bottom: 4px;
}

.favorite-time {
  display: block;
  font-size: 12px;
  color: #999;
}

.remove-btn {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: #F5F5F5;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  color: #999;
  font-size: 14px;
  flex-shrink: 0;
  transition: all 0.3s ease;
}

.remove-btn:hover {
  background: #E53935;
  color: white;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: #999;
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 16px;
}

.empty-hint {
  font-size: 14px;
  margin-top: 8px;
}
</style>
