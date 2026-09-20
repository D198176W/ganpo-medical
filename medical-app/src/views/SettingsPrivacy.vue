<template>
  <div class="settings-privacy">
    <div class="bg-container">
      <img src="/src/assets/images/personal-bg.png" alt="背景" class="bg-img">
    </div>
    <NavBar title="隐私设置" :show-user="false" />
    
    <div class="content">
      <!-- 横幅 -->
      <div class="privacy-banner">
        <div class="banner-content">
          <h2>隐私保护设置</h2>
          <p>控制您的个人信息和数据的可见性</p>
        </div>
        <div class="banner-icon">
          <i class="fas fa-user-shield"></i>
        </div>
      </div>

      <!-- 隐私概览 -->
      <div class="privacy-overview">
        <div class="overview-card">
          <div class="overview-header">
            <div class="overview-icon">
              <i class="fas fa-shield-alt"></i>
            </div>
            <div class="overview-info">
              <span class="overview-title">隐私保护等级</span>
              <span class="overview-level high">高级保护</span>
            </div>
          </div>
          <div class="overview-progress">
            <div class="progress-bar">
              <div class="progress-fill high" style="width: 85%"></div>
            </div>
            <span class="progress-text">85 分</span>
          </div>
          <div class="overview-tips">
            <div class="tip-item completed">
              <i class="fas fa-check-circle"></i>
              <span>已开启隐私保护</span>
            </div>
            <div class="tip-item completed">
              <i class="fas fa-check-circle"></i>
              <span>已隐藏个人敏感信息</span>
            </div>
            <div class="tip-item pending">
              <i class="far fa-circle"></i>
              <span>建议开启匿名浏览模式</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 信息可见性 -->
      <div class="visibility-section">
        <div class="section-header">
          <span class="section-title">信息可见性</span>
        </div>

        <div class="visibility-list">
          <!-- 个人资料 -->
          <div class="visibility-item">
            <div class="item-header">
              <div class="item-icon profile">
                <i class="fas fa-user-circle"></i>
              </div>
              <div class="item-info">
                <span class="item-name">个人资料</span>
                <span class="item-desc">头像、昵称、性别等基本信息</span>
              </div>
            </div>
            <div class="item-control">
              <select class="visibility-select" v-model="visibility.profile">
                <option value="public">所有人可见</option>
                <option value="friends">仅好友可见</option>
                <option value="private">仅自己可见</option>
              </select>
            </div>
          </div>

          <!-- 健康档案 -->
          <div class="visibility-item">
            <div class="item-header">
              <div class="item-icon health">
                <i class="fas fa-heartbeat"></i>
              </div>
              <div class="item-info">
                <span class="item-name">健康档案</span>
                <span class="item-desc">健康记录、体检报告等</span>
              </div>
            </div>
            <div class="item-control">
              <select class="visibility-select" v-model="visibility.health">
                <option value="public">所有人可见</option>
                <option value="friends">仅好友可见</option>
                <option value="private" selected>仅自己可见</option>
              </select>
            </div>
          </div>

          <!-- 问诊记录 -->
          <div class="visibility-item">
            <div class="item-header">
              <div class="item-icon consult">
                <i class="fas fa-user-md"></i>
              </div>
              <div class="item-info">
                <span class="item-name">问诊记录</span>
                <span class="item-desc">在线问诊、咨询记录</span>
              </div>
            </div>
            <div class="item-control">
              <select class="visibility-select" v-model="visibility.consultation">
                <option value="public">所有人可见</option>
                <option value="friends">仅好友可见</option>
                <option value="private" selected>仅自己可见</option>
              </select>
            </div>
          </div>

          <!-- 用药记录 -->
          <div class="visibility-item">
            <div class="item-header">
              <div class="item-icon medication">
                <i class="fas fa-pills"></i>
              </div>
              <div class="item-info">
                <span class="item-name">用药记录</span>
                <span class="item-desc">药品购买、服药记录</span>
              </div>
            </div>
            <div class="item-control">
              <select class="visibility-select" v-model="visibility.medication">
                <option value="public">所有人可见</option>
                <option value="friends">仅好友可见</option>
                <option value="private" selected>仅自己可见</option>
              </select>
            </div>
          </div>

          <!-- 动态分享 -->
          <div class="visibility-item">
            <div class="item-header">
              <div class="item-icon activity">
                <i class="fas fa-share-alt"></i>
              </div>
              <div class="item-info">
                <span class="item-name">动态分享</span>
                <span class="item-desc">发布的健康动态、心得分享</span>
              </div>
            </div>
            <div class="item-control">
              <select class="visibility-select" v-model="visibility.activity">
                <option value="public">所有人可见</option>
                <option value="friends" selected>仅好友可见</option>
                <option value="private">仅自己可见</option>
              </select>
            </div>
          </div>
        </div>
      </div>

      <!-- 隐私权限 -->
      <div class="permission-section">
        <div class="section-header">
          <span class="section-title">隐私权限</span>
        </div>

        <div class="permission-list">
          <!-- 好友申请 -->
          <div class="permission-item">
            <div class="permission-info">
              <div class="permission-icon friend">
                <i class="fas fa-user-plus"></i>
              </div>
              <div class="permission-content">
                <span class="permission-name">好友申请</span>
                <span class="permission-desc">允许其他人添加我为好友</span>
              </div>
            </div>
            <div class="toggle-switch" :class="{ active: permissions.friendRequest }" @click="permissions.friendRequest = !permissions.friendRequest"></div>
          </div>

          <!-- 推荐好友 -->
          <div class="permission-item">
            <div class="permission-info">
              <div class="permission-icon recommend">
                <i class="fas fa-user-friends"></i>
              </div>
              <div class="permission-content">
                <span class="permission-name">把我推荐给好友</span>
                <span class="permission-desc">通过手机号搜索到我</span>
              </div>
            </div>
            <div class="toggle-switch" :class="{ active: permissions.recommend }" @click="permissions.recommend = !permissions.recommend"></div>
          </div>

          <!-- 在线状态 -->
          <div class="permission-item">
            <div class="permission-info">
              <div class="permission-icon online">
                <i class="fas fa-wifi"></i>
              </div>
              <div class="permission-content">
                <span class="permission-name">显示在线状态</span>
                <span class="permission-desc">向他人显示我的在线状态</span>
              </div>
            </div>
            <div class="toggle-switch" :class="{ active: permissions.onlineStatus }" @click="permissions.onlineStatus = !permissions.onlineStatus"></div>
          </div>

          <!-- 已读状态 -->
          <div class="permission-item">
            <div class="permission-info">
              <div class="permission-icon read">
                <i class="fas fa-eye"></i>
              </div>
              <div class="permission-content">
                <span class="permission-name">显示已读状态</span>
                <span class="permission-desc">聊天时显示消息已读状态</span>
              </div>
            </div>
            <div class="toggle-switch" :class="{ active: permissions.readStatus }" @click="permissions.readStatus = !permissions.readStatus"></div>
          </div>

          <!-- 个性化推荐 -->
          <div class="permission-item">
            <div class="permission-info">
              <div class="permission-icon ads">
                <i class="fas fa-ad"></i>
              </div>
              <div class="permission-content">
                <span class="permission-name">个性化内容推荐</span>
                <span class="permission-desc">根据浏览历史推荐相关内容</span>
              </div>
            </div>
            <div class="toggle-switch" :class="{ active: permissions.personalizedAds }" @click="permissions.personalizedAds = !permissions.personalizedAds"></div>
          </div>
        </div>
      </div>

      <!-- 数据与隐私 -->
      <div class="data-section">
        <div class="section-header">
          <span class="section-title">数据与隐私</span>
        </div>

        <div class="data-list">
          <div class="data-item" @click="exportData">
            <div class="data-icon export">
              <i class="fas fa-download"></i>
            </div>
            <div class="data-info">
              <span class="data-name">导出个人数据</span>
              <span class="data-desc">下载您的个人信息和数据副本</span>
            </div>
            <div class="data-action">
              <i class="fas fa-chevron-right"></i>
            </div>
          </div>

          <div class="data-item" @click="viewDataUsage">
            <div class="data-icon usage">
              <i class="fas fa-chart-bar"></i>
            </div>
            <div class="data-info">
              <span class="data-name">查看数据使用</span>
              <span class="data-desc">了解我们如何收集和使用您的数据</span>
            </div>
            <div class="data-action">
              <i class="fas fa-chevron-right"></i>
            </div>
          </div>

          <div class="data-item" @click="clearCache">
            <div class="data-icon cache">
              <i class="fas fa-broom"></i>
            </div>
            <div class="data-info">
              <span class="data-name">清除缓存数据</span>
              <span class="data-desc">清理本地缓存的应用数据</span>
            </div>
            <div class="data-action">
              <span class="cache-size">{{ cacheSize }}</span>
              <i class="fas fa-chevron-right"></i>
            </div>
          </div>

          <div class="data-item danger" @click="deleteAccount">
            <div class="data-icon delete">
              <i class="fas fa-user-slash"></i>
            </div>
            <div class="data-info">
              <span class="data-name">注销账号</span>
              <span class="data-desc">永久删除账号及所有数据</span>
            </div>
            <div class="data-action">
              <i class="fas fa-chevron-right"></i>
            </div>
          </div>
        </div>
      </div>

      <!-- 隐私政策 -->
      <div class="policy-section">
        <div class="policy-list">
          <div class="policy-item" @click="viewPrivacyPolicy">
            <span class="policy-text"><i class="fas fa-file-contract"></i> 隐私政策</span>
            <i class="fas fa-chevron-right"></i>
          </div>
          <div class="policy-item" @click="viewTerms">
            <span class="policy-text"><i class="fas fa-book"></i> 用户协议</span>
            <i class="fas fa-chevron-right"></i>
          </div>
          <div class="policy-item" @click="contactSupport">
            <span class="policy-text"><i class="fas fa-headset"></i> 隐私问题反馈</span>
            <i class="fas fa-chevron-right"></i>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import NavBar from '@/components/common/NavBar.vue'

// 可见性设置
const visibility = ref({
  profile: 'friends',
  health: 'private',
  consultation: 'private',
  medication: 'private',
  activity: 'friends'
})

// 权限设置
const permissions = ref({
  friendRequest: true,
  recommend: false,
  onlineStatus: true,
  readStatus: true,
  personalizedAds: false
})

// 缓存大小
const cacheSize = ref('23.5 MB')

// 方法
const exportData = () => {
  alert('数据导出功能开发中...')
}

const viewDataUsage = () => {
  alert('数据使用说明开发中...')
}

const clearCache = () => {
  if (confirm('确定要清除缓存数据吗？这可能会影响应用的加载速度。')) {
    cacheSize.value = '0 MB'
    alert('缓存已清除')
  }
}

const deleteAccount = () => {
  if (confirm('警告：此操作将永久删除您的账号及所有数据，且无法恢复！确定要继续吗？')) {
    alert('账号注销功能开发中...')
  }
}

const viewPrivacyPolicy = () => {
  alert('隐私政策页面开发中...')
}

const viewTerms = () => {
  alert('用户协议页面开发中...')
}

const contactSupport = () => {
  alert('隐私问题反馈功能开发中...')
}
</script>

<style scoped>
.settings-privacy {
  min-height: 100vh;
  padding-bottom: 80px;
  position: relative;
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
}

.content {
  padding: 16px;
  position: relative;
  z-index: 1;
}

/* 横幅 */
.privacy-banner {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 20px;
  padding: 24px;
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: relative;
  overflow: hidden;
  box-shadow: 0 8px 24px rgba(102, 126, 234, 0.4);
}

.banner-content {
  flex: 1;
  z-index: 1;
}

.banner-content h2 {
  font-size: 22px;
  font-weight: 700;
  color: white;
  margin: 0 0 8px 0;
}

.banner-content p {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.9);
  margin: 0;
}

.banner-icon {
  width: 64px;
  height: 64px;
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32px;
  color: white;
  flex-shrink: 0;
}

/* 隐私概览 */
.privacy-overview {
  margin-bottom: 20px;
}

.overview-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  padding: 20px;
  backdrop-filter: blur(10px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.overview-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.overview-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: white;
}

.overview-info {
  flex: 1;
  margin-left: 12px;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.overview-title {
  font-size: 16px;
  color: #666;
}

.overview-level {
  font-size: 20px;
  font-weight: 700;
}

.overview-level.high {
  color: #4CAF50;
}

.overview-level.medium {
  color: #FF9800;
}

.overview-level.low {
  color: #F44336;
}

.overview-progress {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.progress-bar {
  flex: 1;
  height: 8px;
  background: #f0f0f0;
  border-radius: 4px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  border-radius: 4px;
  transition: width 0.3s ease;
}

.progress-fill.high {
  background: linear-gradient(90deg, #4CAF50, #8BC34A);
}

.progress-fill.medium {
  background: linear-gradient(90deg, #FF9800, #FFC107);
}

.progress-fill.low {
  background: linear-gradient(90deg, #F44336, #FF5722);
}

.progress-text {
  font-size: 16px;
  font-weight: 700;
  color: #667eea;
  min-width: 50px;
}

.overview-tips {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.tip-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
}

.tip-item i {
  font-size: 16px;
}

.tip-item.completed i {
  color: #4CAF50;
}

.tip-item.pending i {
  color: #ccc;
}

.tip-item.completed span {
  color: #333;
}

.tip-item.pending span {
  color: #999;
}

/* 可见性设置 */
.visibility-section {
  margin-bottom: 20px;
}

.section-header {
  margin-bottom: 16px;
}

.section-title {
  font-size: 18px;
  font-weight: 700;
  color: #333;
  display: block;
}

.visibility-list {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  overflow: hidden;
  backdrop-filter: blur(10px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.visibility-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid #f0f0f0;
}

.visibility-item:last-child {
  border-bottom: none;
}

.item-header {
  display: flex;
  align-items: center;
  gap: 12px;
}

.item-icon {
  width: 44px;
  height: 44px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  color: white;
  flex-shrink: 0;
}

.item-icon.profile {
  background: linear-gradient(135deg, #667eea, #764ba2);
}

.item-icon.health {
  background: linear-gradient(135deg, #f093fb, #f5576c);
}

.item-icon.consult {
  background: linear-gradient(135deg, #4facfe, #00f2fe);
}

.item-icon.medication {
  background: linear-gradient(135deg, #43e97b, #38f9d7);
}

.item-icon.activity {
  background: linear-gradient(135deg, #fa709a, #fee140);
}

.item-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.item-name {
  font-size: 15px;
  font-weight: 600;
  color: #333;
}

.item-desc {
  font-size: 13px;
  color: #999;
}

.visibility-select {
  padding: 8px 16px;
  border: 2px solid #e0e0e0;
  border-radius: 12px;
  font-size: 14px;
  color: #333;
  background: white;
  cursor: pointer;
  transition: border-color 0.3s ease;
  outline: none;
}

.visibility-select:focus {
  border-color: #667eea;
}

/* 权限设置 */
.permission-section {
  margin-bottom: 20px;
}

.permission-list {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  overflow: hidden;
  backdrop-filter: blur(10px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.permission-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid #f0f0f0;
}

.permission-item:last-child {
  border-bottom: none;
}

.permission-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.permission-icon {
  width: 44px;
  height: 44px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  color: white;
  flex-shrink: 0;
}

.permission-icon.friend {
  background: linear-gradient(135deg, #667eea, #764ba2);
}

.permission-icon.recommend {
  background: linear-gradient(135deg, #f093fb, #f5576c);
}

.permission-icon.online {
  background: linear-gradient(135deg, #4facfe, #00f2fe);
}

.permission-icon.read {
  background: linear-gradient(135deg, #43e97b, #38f9d7);
}

.permission-icon.ads {
  background: linear-gradient(135deg, #fa709a, #fee140);
}

.permission-content {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.permission-name {
  font-size: 15px;
  font-weight: 600;
  color: #333;
}

.permission-desc {
  font-size: 13px;
  color: #999;
}

/* 开关样式 */
.toggle-switch {
  width: 50px;
  height: 28px;
  background: #e0e0e0;
  border-radius: 14px;
  position: relative;
  cursor: pointer;
  transition: background 0.3s ease;
}

.toggle-switch.active {
  background: linear-gradient(135deg, #667eea, #764ba2);
}

.toggle-handle {
  width: 24px;
  height: 24px;
  background: white;
  border-radius: 50%;
  position: absolute;
  top: 2px;
  left: 2px;
  transition: transform 0.3s ease;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.toggle-switch.active .toggle-handle {
  transform: translateX(22px);
}

/* 数据与隐私 */
.data-section {
  margin-bottom: 20px;
}

.data-list {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  overflow: hidden;
  backdrop-filter: blur(10px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.data-item {
  display: flex;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: background 0.3s ease;
}

.data-item:last-child {
  border-bottom: none;
}

.data-item:hover {
  background: rgba(102, 126, 234, 0.05);
}

.data-item.danger:hover {
  background: rgba(244, 67, 54, 0.05);
}

.data-icon {
  width: 44px;
  height: 44px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  color: white;
  flex-shrink: 0;
  margin-right: 12px;
}

.data-icon.export {
  background: linear-gradient(135deg, #667eea, #764ba2);
}

.data-icon.usage {
  background: linear-gradient(135deg, #f093fb, #f5576c);
}

.data-icon.cache {
  background: linear-gradient(135deg, #4facfe, #00f2fe);
}

.data-icon.delete {
  background: linear-gradient(135deg, #F44336, #FF5722);
}

.data-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.data-name {
  font-size: 15px;
  font-weight: 600;
  color: #333;
}

.data-desc {
  font-size: 13px;
  color: #999;
}

.data-action {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #ccc;
  font-size: 14px;
}

.data-action .cache-size {
  font-size: 13px;
  color: #667eea;
  font-weight: 600;
}

.data-item.danger .data-name {
  color: #F44336;
}

/* 隐私政策 */
.policy-section {
  margin-bottom: 20px;
}

.policy-list {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  overflow: hidden;
  backdrop-filter: blur(10px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.policy-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: background 0.3s ease;
}

.policy-item:last-child {
  border-bottom: none;
}

.policy-item:hover {
  background: rgba(102, 126, 234, 0.05);
}

.policy-text {
  font-size: 15px;
  color: #333;
  display: flex;
  align-items: center;
  gap: 12px;
}

.policy-text i {
  font-size: 18px;
  color: #667eea;
}

.policy-item:hover .policy-text i {
  color: #764ba2;
}
</style>
