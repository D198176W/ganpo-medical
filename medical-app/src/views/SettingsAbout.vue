<template>
  <div class="settings-about">
    <div class="bg-container">
      <img src="/src/assets/images/personal-bg.png" alt="背景" class="bg-img">
    </div>
    <NavBar title="关于我们" :show-user="false" />
    
    <div class="content">
      <!-- 应用信息横幅 -->
      <div class="about-banner">
        <div class="app-logo">
          <i class="fas fa-heartbeat"></i>
        </div>
        <div class="app-info">
          <h2 class="app-name">智慧医疗服务平台</h2>
          <p class="app-version">版本：v2.5.0</p>
          <p class="app-slogan">您的健康，我们守护</p>
        </div>
        <button class="check-update" @click="checkUpdate">
          <i class="fas fa-sync-alt"></i>
          检查更新
        </button>
      </div>

      <!-- 应用简介 -->
      <div class="intro-section">
        <div class="section-header">
          <span class="section-title">应用简介</span>
        </div>
        <div class="intro-card">
          <p class="intro-text">
            智慧医疗服务平台是一款专业的医疗健康服务应用，致力于为用户提供便捷、高效、专业的医疗健康服务。
            我们整合了优质的医疗资源，通过互联网技术让用户享受更便捷的就医体验。
          </p>
          <div class="intro-features">
            <div class="feature-item">
              <div class="feature-icon online">
                <i class="fas fa-user-md"></i>
              </div>
              <span class="feature-name">在线问诊</span>
              <span class="feature-desc">专业医生在线解答</span>
            </div>
            <div class="feature-item">
              <div class="feature-icon health">
                <i class="fas fa-heartbeat"></i>
              </div>
              <span class="feature-name">健康管理</span>
              <span class="feature-desc">全方位健康档案</span>
            </div>
            <div class="feature-item">
              <div class="feature-icon medicine">
                <i class="fas fa-pills"></i>
              </div>
              <span class="feature-name">用药提醒</span>
              <span class="feature-desc">智能服药提醒</span>
            </div>
            <div class="feature-item">
              <div class="feature-icon report">
                <i class="fas fa-file-medical"></i>
              </div>
              <span class="feature-name">体检报告</span>
              <span class="feature-desc">电子报告随时查</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 公司信息 -->
      <div class="company-section">
        <div class="section-header">
          <span class="section-title">公司信息</span>
        </div>
        <div class="company-list">
          <div class="company-item">
            <div class="company-icon">
              <i class="fas fa-building"></i>
            </div>
            <div class="company-info">
              <span class="company-label">公司名称</span>
              <span class="company-value">某某医疗科技有限公司</span>
            </div>
          </div>
          <div class="company-item">
            <div class="company-icon">
              <i class="fas fa-map-marker-alt"></i>
            </div>
            <div class="company-info">
              <span class="company-label">公司地址</span>
              <span class="company-value">北京市朝阳区某某大厦</span>
            </div>
          </div>
          <div class="company-item">
            <div class="company-icon">
              <i class="fas fa-phone-alt"></i>
            </div>
            <div class="company-info">
              <span class="company-label">联系电话</span>
              <span class="company-value">400-888-8888</span>
            </div>
          </div>
          <div class="company-item">
            <div class="company-icon">
              <i class="fas fa-envelope"></i>
            </div>
            <div class="company-info">
              <span class="company-label">官方邮箱</span>
              <span class="company-value">support@example.com</span>
            </div>
          </div>
          <div class="company-item">
            <div class="company-icon">
              <i class="fas fa-globe"></i>
            </div>
            <div class="company-info">
              <span class="company-label">官方网站</span>
              <span class="company-value">www.example.com</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 团队成员 -->
      <div class="team-section">
        <div class="section-header">
          <span class="section-title">核心团队</span>
        </div>
        <div class="team-list">
          <div class="team-member">
            <div class="member-avatar ceo">
              <i class="fas fa-user"></i>
            </div>
            <div class="member-info">
              <span class="member-name">张三</span>
              <span class="member-role">创始人 & CEO</span>
              <span class="member-desc">15 年医疗健康行业经验</span>
            </div>
          </div>
          <div class="team-member">
            <div class="member-avatar cto">
              <i class="fas fa-user"></i>
            </div>
            <div class="member-info">
              <span class="member-name">李四</span>
              <span class="member-role">联合创始人 & CTO</span>
              <span class="member-desc">前知名互联网公司技术总监</span>
            </div>
          </div>
          <div class="team-member">
            <div class="member-avatar cmo">
              <i class="fas fa-user"></i>
            </div>
            <div class="member-info">
              <span class="member-name">王五</span>
              <span class="member-role">首席医疗官</span>
              <span class="member-desc">三甲医院主任医师，医学博士</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 荣誉资质 -->
      <div class="honors-section">
        <div class="section-header">
          <span class="section-title">荣誉资质</span>
        </div>
        <div class="honors-list">
          <div class="honor-item">
            <div class="honor-icon">
              <i class="fas fa-award"></i>
            </div>
            <div class="honor-info">
              <span class="honor-name">国家高新技术企业</span>
              <span class="honor-date">2023 年</span>
            </div>
          </div>
          <div class="honor-item">
            <div class="honor-icon">
              <i class="fas fa-medal"></i>
            </div>
            <div class="honor-info">
              <span class="honor-name">互联网医疗示范企业</span>
              <span class="honor-date">2022 年</span>
            </div>
          </div>
          <div class="honor-item">
            <div class="honor-icon">
              <i class="fas fa-certificate"></i>
            </div>
            <div class="honor-info">
              <span class="honor-name">医疗器械经营许可证</span>
              <span class="honor-date">2021 年</span>
            </div>
          </div>
          <div class="honor-item">
            <div class="honor-icon">
              <i class="fas fa-shield-alt"></i>
            </div>
            <div class="honor-info">
              <span class="honor-name">信息安全等级保护三级</span>
              <span class="honor-date">2023 年</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 相关链接 -->
      <div class="links-section">
        <div class="section-header">
          <span class="section-title">相关链接</span>
        </div>
        <div class="links-list">
          <div class="link-item" @click="viewPrivacyPolicy">
            <div class="link-icon">
              <i class="fas fa-file-contract"></i>
            </div>
            <span class="link-text">隐私政策</span>
            <i class="fas fa-chevron-right link-arrow"></i>
          </div>
          <div class="link-item" @click="viewTerms">
            <div class="link-icon">
              <i class="fas fa-book"></i>
            </div>
            <span class="link-text">用户协议</span>
            <i class="fas fa-chevron-right link-arrow"></i>
          </div>
          <div class="link-item" @click="viewDisclaimer">
            <div class="link-icon">
              <i class="fas fa-exclamation-triangle"></i>
            </div>
            <span class="link-text">免责声明</span>
            <i class="fas fa-chevron-right link-arrow"></i>
          </div>
          <div class="link-item" @click="viewLicense">
            <div class="link-icon">
              <i class="fas fa-certificate"></i>
            </div>
            <span class="link-text">营业执照</span>
            <i class="fas fa-chevron-right link-arrow"></i>
          </div>
        </div>
      </div>

      <!-- 关注我们 -->
      <div class="social-section">
        <div class="section-header">
          <span class="section-title">关注我们</span>
        </div>
        <div class="social-list">
          <div class="social-item" @click="followWechat">
            <div class="social-icon wechat">
              <i class="fab fa-weixin"></i>
            </div>
            <span class="social-label">微信公众号</span>
          </div>
          <div class="social-item" @click="followWeibo">
            <div class="social-icon weibo">
              <i class="fab fa-weibo"></i>
            </div>
            <span class="social-label">官方微博</span>
          </div>
          <div class="social-item" @click="followDouyin">
            <div class="social-icon douyin">
              <i class="fab fa-tiktok"></i>
            </div>
            <span class="social-label">抖音号</span>
          </div>
          <div class="social-item" @click="downloadApp">
            <div class="social-icon app">
              <i class="fas fa-mobile-alt"></i>
            </div>
            <span class="social-label">下载 APP</span>
          </div>
        </div>
      </div>

      <!-- 版权信息 -->
      <div class="copyright-section">
        <p class="copyright-text">
          Copyright © 2021-2024 某某医疗科技有限公司 版权所有
        </p>
        <p class="copyright-text">
          京 ICP 备 12345678 号 -1 | 京网文〔2023〕1234-567 号
        </p>
        <p class="copyright-tip">
          <i class="fas fa-info-circle"></i>
          本应用仅用于健康咨询，不能替代专业医疗诊断
        </p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import NavBar from '@/components/common/NavBar.vue'

const currentVersion = ref('2.5.0')

// 检查更新
const checkUpdate = () => {
  alert('正在检查更新...\n当前已是最新版本 v2.5.0')
}

// 查看隐私政策
const viewPrivacyPolicy = () => {
  alert('隐私政策页面开发中...')
}

// 查看用户协议
const viewTerms = () => {
  alert('用户协议页面开发中...')
}

// 查看免责声明
const viewDisclaimer = () => {
  alert('免责声明页面开发中...')
}

// 查看营业执照
const viewLicense = () => {
  alert('营业执照页面开发中...')
}

// 关注微信
const followWechat = () => {
  alert('微信公众号：智慧医疗服务平台')
}

// 关注微博
const followWeibo = () => {
  alert('官方微博：@智慧医疗服务平台')
}

// 关注抖音
const followDouyin = () => {
  alert('抖音号：zhihuimedical')
}

// 下载 APP
const downloadApp = () => {
  alert('APP 下载页面开发中...')
}
</script>

<style scoped>
.settings-about {
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

/* 应用信息横幅 */
.about-banner {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 20px;
  padding: 24px;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
  position: relative;
  overflow: hidden;
  box-shadow: 0 8px 24px rgba(102, 126, 234, 0.4);
}

.app-logo {
  width: 72px;
  height: 72px;
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 36px;
  color: white;
  flex-shrink: 0;
}

.app-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.app-name {
  font-size: 20px;
  font-weight: 700;
  color: white;
  margin: 0;
}

.app-version {
  font-size: 13px;
  color: rgba(255, 255, 255, 0.8);
  margin: 0;
}

.app-slogan {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.9);
  margin: 0;
  font-style: italic;
}

.check-update {
  background: rgba(255, 255, 255, 0.2);
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.5);
  padding: 10px 16px;
  border-radius: 20px;
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 6px;
  transition: all 0.3s ease;
  flex-shrink: 0;
}

.check-update:hover {
  background: rgba(255, 255, 255, 0.3);
  transform: translateY(-2px);
}

.check-update i {
  font-size: 14px;
}

/* 通用区域样式 */
.section-header {
  margin-bottom: 16px;
}

.section-title {
  font-size: 18px;
  font-weight: 700;
  color: #333;
  display: block;
}

/* 应用简介 */
.intro-section {
  margin-bottom: 20px;
}

.intro-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  padding: 20px;
  backdrop-filter: blur(10px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.intro-text {
  font-size: 15px;
  line-height: 1.8;
  color: #666;
  margin: 0 0 20px 0;
}

.intro-features {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

.feature-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.feature-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: white;
}

.feature-icon.online {
  background: linear-gradient(135deg, #667eea, #764ba2);
}

.feature-icon.health {
  background: linear-gradient(135deg, #f093fb, #f5576c);
}

.feature-icon.medicine {
  background: linear-gradient(135deg, #4facfe, #00f2fe);
}

.feature-icon.report {
  background: linear-gradient(135deg, #43e97b, #38f9d7);
}

.feature-name {
  font-size: 14px;
  font-weight: 600;
  color: #333;
}

.feature-desc {
  font-size: 12px;
  color: #999;
}

/* 公司信息 */
.company-section {
  margin-bottom: 20px;
}

.company-list {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  overflow: hidden;
  backdrop-filter: blur(10px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.company-item {
  display: flex;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid #f0f0f0;
}

.company-item:last-child {
  border-bottom: none;
}

.company-icon {
  width: 44px;
  height: 44px;
  border-radius: 12px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  color: white;
  flex-shrink: 0;
  margin-right: 12px;
}

.company-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.company-label {
  font-size: 13px;
  color: #999;
}

.company-value {
  font-size: 15px;
  font-weight: 600;
  color: #333;
}

/* 团队成员 */
.team-section {
  margin-bottom: 20px;
}

.team-list {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  overflow: hidden;
  backdrop-filter: blur(10px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.team-member {
  display: flex;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid #f0f0f0;
}

.team-member:last-child {
  border-bottom: none;
}

.member-avatar {
  width: 56px;
  height: 56px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  color: white;
  flex-shrink: 0;
  margin-right: 16px;
}

.member-avatar.ceo {
  background: linear-gradient(135deg, #667eea, #764ba2);
}

.member-avatar.cto {
  background: linear-gradient(135deg, #f093fb, #f5576c);
}

.member-avatar.cmo {
  background: linear-gradient(135deg, #4facfe, #00f2fe);
}

.member-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.member-name {
  font-size: 16px;
  font-weight: 700;
  color: #333;
}

.member-role {
  font-size: 14px;
  font-weight: 600;
  color: #667eea;
}

.member-desc {
  font-size: 13px;
  color: #999;
}

/* 荣誉资质 */
.honors-section {
  margin-bottom: 20px;
}

.honors-list {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  overflow: hidden;
  backdrop-filter: blur(10px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.honor-item {
  display: flex;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid #f0f0f0;
}

.honor-item:last-child {
  border-bottom: none;
}

.honor-icon {
  width: 44px;
  height: 44px;
  border-radius: 12px;
  background: linear-gradient(135deg, #fa709a, #fee140);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  color: white;
  flex-shrink: 0;
  margin-right: 12px;
}

.honor-info {
  flex: 1;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.honor-name {
  font-size: 15px;
  font-weight: 600;
  color: #333;
}

.honor-date {
  font-size: 13px;
  color: #999;
}

/* 相关链接 */
.links-section {
  margin-bottom: 20px;
}

.links-list {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  overflow: hidden;
  backdrop-filter: blur(10px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.link-item {
  display: flex;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: background 0.3s ease;
}

.link-item:last-child {
  border-bottom: none;
}

.link-item:hover {
  background: rgba(102, 126, 234, 0.05);
}

.link-icon {
  width: 44px;
  height: 44px;
  border-radius: 12px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  color: white;
  flex-shrink: 0;
  margin-right: 12px;
}

.link-text {
  flex: 1;
  font-size: 15px;
  color: #333;
  font-weight: 600;
}

.link-arrow {
  color: #ccc;
  font-size: 14px;
}

/* 关注我们 */
.social-section {
  margin-bottom: 20px;
}

.social-list {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  padding: 20px;
  backdrop-filter: blur(10px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

.social-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  transition: transform 0.3s ease;
}

.social-item:hover {
  transform: translateY(-4px);
}

.social-icon {
  width: 56px;
  height: 56px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  color: white;
}

.social-icon.wechat {
  background: linear-gradient(135deg, #07c160, #06ad56);
}

.social-icon.weibo {
  background: linear-gradient(135deg, #e6162d, #d11426);
}

.social-icon.douyin {
  background: linear-gradient(135deg, #000000, #333333);
}

.social-icon.app {
  background: linear-gradient(135deg, #667eea, #764ba2);
}

.social-label {
  font-size: 13px;
  color: #666;
  font-weight: 600;
}

/* 版权信息 */
.copyright-section {
  padding: 20px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  backdrop-filter: blur(10px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  text-align: center;
}

.copyright-text {
  font-size: 13px;
  color: #999;
  margin: 8px 0;
  line-height: 1.6;
}

.copyright-tip {
  margin-top: 16px;
  padding: 12px;
  background: rgba(102, 126, 234, 0.05);
  border-radius: 12px;
  font-size: 13px;
  color: #666;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.copyright-tip i {
  font-size: 16px;
  color: #667eea;
}
</style>
