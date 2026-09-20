<template>
  <div class="personal-allergy">
    <div class="bg-container">
      <img src="/src/assets/images/personal-bg.png" alt="背景" class="bg-img">
    </div>
    <NavBar title="过敏信息" :show-user="false" />
    
    <div class="content">
      <div class="allergy-banner">
        <div class="banner-content">
          <h2>过敏信息管理</h2>
          <p>记录过敏原，预防过敏反应</p>
        </div>
        <div class="banner-icon">
          <i class="fas fa-shield-virus"></i>
        </div>
      </div>

      <div class="allergy-stats">
        <div class="stat-item">
          <span class="stat-value">{{ totalAllergies }}</span>
          <span class="stat-label">过敏原总数</span>
        </div>
        <div class="stat-item">
          <span class="stat-value severe">{{ severeCount }}</span>
          <span class="stat-label">严重过敏</span>
        </div>
        <div class="stat-item">
          <span class="stat-value mild">{{ mildCount }}</span>
          <span class="stat-label">轻度过敏</span>
        </div>
      </div>

      <div class="allergy-tabs">
        <div 
          v-for="tab in allergyTabs" 
          :key="tab.id"
          class="tab-item"
          :class="{ active: activeTab === tab.id }"
          @click="activeTab = tab.id"
        >
          {{ tab.name }}
        </div>
      </div>

      <div class="allergies-section">
        <div class="section-header">
          <span class="section-title">过敏原列表</span>
        </div>

        <div class="allergies-list">
          <div 
            v-for="allergy in filteredAllergies" 
            :key="allergy.id"
            class="allergy-card"
            @click="viewAllergy(allergy)"
          >
            <div class="allergy-icon" :class="allergy.severityClass">
              <i :class="allergy.icon"></i>
            </div>
            <div class="allergy-info">
              <div class="allergy-header">
                <span class="allergy-name">{{ allergy.name }}</span>
                <span class="allergy-severity" :class="allergy.severityClass">{{ allergy.severity }}</span>
              </div>
              <span class="allergy-category">类别：{{ allergy.category }}</span>
              <span class="allergy-reaction">反应：{{ allergy.reaction }}</span>
              <div class="allergy-tags">
                <span class="tag discovered">发现于 {{ allergy.discoveredDate }}</span>
                <span v-if="allergy.lastReaction" class="tag last-reaction">
                  最近发作 {{ allergy.lastReaction }}
                </span>
              </div>
            </div>
            <div class="allergy-actions">
              <button class="action-btn edit" @click.stop="editAllergy(allergy.id)">
                <i class="fas fa-edit"></i>
              </button>
              <button class="action-btn delete" @click.stop="deleteAllergy(allergy.id)">
                <i class="fas fa-trash-alt"></i>
              </button>
            </div>
          </div>

          <div v-if="filteredAllergies.length === 0" class="empty-state">
            <div class="empty-icon">🚫</div>
            <div>暂无过敏记录</div>
            <div class="empty-hint">如有过敏史请添加记录</div>
          </div>
        </div>
      </div>

      <div class="precautions-section">
        <div class="section-header">
          <span class="section-title">注意事项</span>
        </div>
        <div class="precautions-list">
          <div 
            v-for="item in precautions" 
            :key="item.id"
            class="precaution-card"
          >
            <div class="precaution-icon">
              <i :class="item.icon"></i>
            </div>
            <div class="precaution-content">
              <h4>{{ item.title }}</h4>
              <p>{{ item.desc }}</p>
            </div>
          </div>
        </div>
      </div>

      <div class="emergency-info">
        <div class="section-header">
          <span class="section-title">紧急联系</span>
        </div>
        <div class="emergency-card">
          <div class="emergency-icon">
            <i class="fas fa-ambulance"></i>
          </div>
          <div class="emergency-content">
            <h4>如发生严重过敏反应，请立即：</h4>
            <ol class="emergency-steps">
              <li>立即拨打 120 急救电话</li>
              <li>如有肾上腺素自动注射器，立即使用</li>
              <li>保持平躺，抬高双腿</li>
              <li>保持呼吸道通畅</li>
            </ol>
          </div>
        </div>
      </div>

      <div class="add-allergy-btn">
        <button @click="addAllergy">
          <i class="fas fa-plus"></i>
          添加过敏原
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import NavBar from '@/components/common/NavBar.vue'

const activeTab = ref('all')

const allergyTabs = ref([
  { id: 'all', name: '全部' },
  { id: 'drug', name: '药物' },
  { id: 'food', name: '食物' },
  { id: 'other', name: '其他' }
])

const allergies = ref([
  {
    id: 1,
    name: '青霉素',
    category: '药物',
    severity: '严重',
    severityClass: 'severity-high',
    icon: 'fas fa-pills',
    reaction: '皮疹、呼吸困难、过敏性休克',
    discoveredDate: '2018-05-12',
    lastReaction: '2 年前'
  },
  {
    id: 2,
    name: '海鲜',
    category: '食物',
    severity: '中度',
    severityClass: 'severity-medium',
    icon: 'fas fa-fish',
    reaction: '皮肤瘙痒、红肿',
    discoveredDate: '2015-08-20',
    lastReaction: '6 个月前'
  },
  {
    id: 3,
    name: '花粉',
    category: '环境',
    severity: '轻度',
    severityClass: 'severity-low',
    icon: 'fas fa-seedling',
    reaction: '打喷嚏、流鼻涕、眼睛发痒',
    discoveredDate: '2020-03-15',
    lastReaction: '1 个月前'
  },
  {
    id: 4,
    name: '芒果',
    category: '食物',
    severity: '轻度',
    severityClass: 'severity-low',
    icon: 'fas fa-apple-alt',
    reaction: '口腔瘙痒、嘴唇红肿',
    discoveredDate: '2019-07-10',
    lastReaction: '1 年前'
  }
])

const precautions = ref([
  {
    title: '就医时主动告知',
    desc: '就诊时务必主动告知医生您的过敏史，避免使用过敏药物。',
    icon: 'fas fa-user-md'
  },
  {
    title: '仔细阅读标签',
    desc: '购买食品和药品时，仔细阅读成分表，避免接触过敏原。',
    icon: 'fas fa-clipboard-list'
  },
  {
    title: '随身携带药物',
    desc: '严重过敏者应随身携带抗过敏药物和肾上腺素自动注射器。',
    icon: 'fas fa-first-aid'
  }
])

const totalAllergies = allergies.value.length
const severeCount = allergies.value.filter(a => a.severity === '严重').length
const mildCount = allergies.value.filter(a => a.severity === '轻度').length

const filteredAllergies = computed(() => {
  if (activeTab.value === 'all') {
    return allergies.value
  }
  const categoryMap = {
    drug: '药物',
    food: '食物',
    other: '其他'
  }
  return allergies.value.filter(a => a.category === categoryMap[activeTab.value])
})

const viewAllergy = (allergy) => {
  console.log('查看过敏原:', allergy)
}

const editAllergy = (id) => {
  console.log('编辑过敏原:', id)
}

const deleteAllergy = (id) => {
  console.log('删除过敏原:', id)
}

const addAllergy = () => {
  console.log('添加过敏原')
}
</script>

<style scoped>
.personal-allergy {
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

.allergy-banner {
  background: linear-gradient(135deg, #E53935, #EF5350);
  border-radius: 20px;
  padding: 24px;
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 4px 20px rgba(229, 57, 53, 0.3);
}

.banner-content h2 {
  font-size: 22px;
  color: white;
  margin: 0 0 8px 0;
  font-weight: 700;
}

.banner-content p {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.9);
  margin: 0;
}

.banner-icon {
  width: 64px;
  height: 64px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  color: white;
}

.allergy-stats {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 20px;
  padding: 20px;
  display: flex;
  justify-content: space-around;
  margin-bottom: 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #E53935;
  margin-bottom: 4px;
}

.stat-value.severe {
  color: #D32F2F;
}

.stat-value.mild {
  color: #FF9800;
}

.stat-label {
  font-size: 14px;
  color: #666;
}

.allergy-tabs {
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
  background: linear-gradient(135deg, #E53935, #EF5350);
  color: white;
  font-weight: 600;
}

.allergies-section {
  margin-bottom: 20px;
}

.section-header {
  margin-bottom: 16px;
}

.section-title {
  font-size: 18px;
  font-weight: 700;
  color: #333333;
  position: relative;
  padding-left: 8px;
}

.section-title::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 4px;
  height: 16px;
  background-color: #E53935;
  border-radius: 2px;
}

.allergies-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.allergy-card {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 16px;
  padding: 16px;
  display: flex;
  align-items: center;
  gap: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  cursor: pointer;
  transition: transform 0.2s;
}

.allergy-card:hover {
  transform: translateY(-2px);
}

.allergy-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  font-size: 20px;
  color: white;
}

.severity-high .allergy-icon {
  background: linear-gradient(135deg, #D32F2F, #EF5350);
}

.severity-medium .allergy-icon {
  background: linear-gradient(135deg, #FF9800, #FFB74D);
}

.severity-low .allergy-icon {
  background: linear-gradient(135deg, #FFC107, #FFE082);
}

.allergy-info {
  flex: 1;
}

.allergy-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 4px;
}

.allergy-name {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.allergy-severity {
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.severity-high .allergy-severity {
  background: #FFEBEE;
  color: #D32F2F;
}

.severity-medium .allergy-severity {
  background: #FFF3E0;
  color: #FF9800;
}

.severity-low .allergy-severity {
  background: #FFF8E1;
  color: #FFC107;
}

.allergy-category,
.allergy-reaction {
  display: block;
  font-size: 13px;
  color: #666;
  margin-bottom: 4px;
}

.allergy-tags {
  display: flex;
  gap: 6px;
  margin-top: 8px;
  flex-wrap: wrap;
}

.tag {
  padding: 3px 8px;
  font-size: 11px;
  border-radius: 10px;
  background: #F5F5F5;
  color: #666;
}

.tag.discovered {
  background: #E3F2FD;
  color: #1E88E5;
}

.tag.last-reaction {
  background: #FFF3E0;
  color: #FF9800;
}

.allergy-actions {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.action-btn {
  width: 32px;
  height: 32px;
  border: none;
  border-radius: 50%;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  font-size: 14px;
}

.action-btn.edit {
  background: #E3F2FD;
  color: #1E88E5;
}

.action-btn.delete {
  background: #FFEBEE;
  color: #D32F2F;
}

.action-btn:hover {
  transform: scale(1.1);
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

.precautions-section,
.emergency-info {
  margin-bottom: 20px;
}

.precautions-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.precaution-card {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 16px;
  padding: 16px;
  display: flex;
  align-items: flex-start;
  gap: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.precaution-icon {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  background: linear-gradient(135deg, #1E88E5, #64B5F6);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  font-size: 18px;
  color: white;
}

.precaution-content {
  flex: 1;
}

.precaution-content h4 {
  font-size: 15px;
  font-weight: 600;
  color: #333;
  margin: 0 0 6px 0;
}

.precaution-content p {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  margin: 0;
}

.emergency-card {
  background: linear-gradient(135deg, #FFEBEE, #FFCDD2);
  border-radius: 16px;
  padding: 20px;
  display: flex;
  gap: 16px;
  border: 2px solid #EF5350;
}

.emergency-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  background: linear-gradient(135deg, #D32F2F, #EF5350);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  font-size: 24px;
  color: white;
}

.emergency-content {
  flex: 1;
}

.emergency-content h4 {
  font-size: 16px;
  font-weight: 700;
  color: #D32F2F;
  margin: 0 0 12px 0;
}

.emergency-steps {
  margin: 0;
  padding-left: 20px;
  font-size: 14px;
  color: #333;
  line-height: 1.8;
}

.emergency-steps li {
  margin-bottom: 4px;
}

.add-allergy-btn {
  margin-top: 20px;
}

.add-allergy-btn button {
  width: 100%;
  height: 52px;
  background: linear-gradient(135deg, #E53935, #EF5350);
  color: white;
  border: none;
  border-radius: 26px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  box-shadow: 0 4px 12px rgba(229, 57, 53, 0.3);
  transition: all 0.3s ease;
}

.add-allergy-btn button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(229, 57, 53, 0.4);
}

.add-allergy-btn i {
  font-size: 18px;
}
</style>
