<template>
  <div class="settings-help">
    <div class="bg-container">
      <img src="/src/assets/images/personal-bg.png" alt="背景" class="bg-img">
    </div>
    <NavBar title="帮助与反馈" :show-user="false" />
    
    <div class="content">
      <!-- 横幅 -->
      <div class="help-banner">
        <div class="banner-content">
          <h2>帮助中心</h2>
          <p>常见问题解答，快速找到您需要的帮助</p>
        </div>
        <div class="banner-icon">
          <i class="fas fa-headset"></i>
        </div>
      </div>

      <!-- 搜索框 -->
      <div class="search-section">
        <div class="search-box">
          <i class="fas fa-search"></i>
          <input type="text" v-model="searchQuery" placeholder="搜索帮助文档..." class="search-input">
          <button class="search-btn" @click="searchHelp">搜索</button>
        </div>
      </div>

      <!-- 快捷入口 -->
      <div class="quick-links">
        <div class="quick-link-item" @click="contactOnlineService">
          <div class="link-icon online">
            <i class="fas fa-comments"></i>
          </div>
          <span class="link-label">在线客服</span>
        </div>
        <div class="quick-link-item" @click="submitFeedback">
          <div class="link-icon feedback">
            <i class="fas fa-edit"></i>
          </div>
          <span class="link-label">意见反馈</span>
        </div>
        <div class="quick-link-item" @click="callService">
          <div class="link-icon phone">
            <i class="fas fa-phone-alt"></i>
          </div>
          <span class="link-label">电话咨询</span>
        </div>
        <div class="quick-link-item" @click="rateApp">
          <div class="link-icon rate">
            <i class="fas fa-star"></i>
          </div>
          <span class="link-label">给个好评</span>
        </div>
      </div>

      <!-- 常见问题 -->
      <div class="faq-section">
        <div class="section-header">
          <span class="section-title">常见问题</span>
        </div>

        <div class="faq-list">
          <!-- 账号相关 -->
          <div class="faq-category">
            <div class="category-title">
              <i class="fas fa-user-circle"></i>
              <span>账号问题</span>
            </div>
            <div class="faq-items">
              <div class="faq-item" v-for="(item, index) in faqAccount" :key="index" @click="toggleFaq(index, 'account')">
                <div class="faq-question">
                  <span>{{ item.question }}</span>
                  <i class="fas" :class="item.expanded ? 'fa-chevron-up' : 'fa-chevron-down'"></i>
                </div>
                <div class="faq-answer" v-show="item.expanded">
                  {{ item.answer }}
                </div>
              </div>
            </div>
          </div>

          <!-- 问诊相关 -->
          <div class="faq-category">
            <div class="category-title">
              <i class="fas fa-user-md"></i>
              <span>在线问诊</span>
            </div>
            <div class="faq-items">
              <div class="faq-item" v-for="(item, index) in faqConsultation" :key="index" @click="toggleFaq(index, 'consultation')">
                <div class="faq-question">
                  <span>{{ item.question }}</span>
                  <i class="fas" :class="item.expanded ? 'fa-chevron-up' : 'fa-chevron-down'"></i>
                </div>
                <div class="faq-answer" v-show="item.expanded">
                  {{ item.answer }}
                </div>
              </div>
            </div>
          </div>

          <!-- 药品相关 -->
          <div class="faq-category">
            <div class="category-title">
              <i class="fas fa-pills"></i>
              <span>药品服务</span>
            </div>
            <div class="faq-items">
              <div class="faq-item" v-for="(item, index) in faqMedicine" :key="index" @click="toggleFaq(index, 'medicine')">
                <div class="faq-question">
                  <span>{{ item.question }}</span>
                  <i class="fas" :class="item.expanded ? 'fa-chevron-up' : 'fa-chevron-down'"></i>
                </div>
                <div class="faq-answer" v-show="item.expanded">
                  {{ item.answer }}
                </div>
              </div>
            </div>
          </div>

          <!-- 支付相关 -->
          <div class="faq-category">
            <div class="category-title">
              <i class="fas fa-credit-card"></i>
              <span>支付问题</span>
            </div>
            <div class="faq-items">
              <div class="faq-item" v-for="(item, index) in faqPayment" :key="index" @click="toggleFaq(index, 'payment')">
                <div class="faq-question">
                  <span>{{ item.question }}</span>
                  <i class="fas" :class="item.expanded ? 'fa-chevron-up' : 'fa-chevron-down'"></i>
                </div>
                <div class="faq-answer" v-show="item.expanded">
                  {{ item.answer }}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 意见反馈 -->
      <div class="feedback-section">
        <div class="section-header">
          <span class="section-title">意见反馈</span>
        </div>

        <div class="feedback-card">
          <div class="feedback-type">
            <span class="type-label">反馈类型</span>
            <select class="type-select" v-model="feedbackType">
              <option value="bug">功能异常</option>
              <option value="suggestion">功能建议</option>
              <option value="content">内容问题</option>
              <option value="other">其他问题</option>
            </select>
          </div>

          <div class="feedback-content">
            <textarea 
              v-model="feedbackContent" 
              placeholder="请详细描述您遇到的问题或建议，我们会认真倾听..." 
              class="feedback-textarea"
              rows="5"
            ></textarea>
          </div>

          <div class="feedback-contact">
            <input 
              type="text" 
              v-model="feedbackContact" 
              placeholder="您的联系方式（选填，方便我们回复您）" 
              class="contact-input"
            >
          </div>

          <button class="submit-btn" @click="submitFeedbackForm">
            <i class="fas fa-paper-plane"></i>
            提交反馈
          </button>
        </div>
      </div>

      <!-- 联系方式 -->
      <div class="contact-section">
        <div class="section-header">
          <span class="section-title">联系我们</span>
        </div>

        <div class="contact-list">
          <div class="contact-item">
            <div class="contact-icon phone">
              <i class="fas fa-phone-alt"></i>
            </div>
            <div class="contact-info">
              <span class="contact-label">客服热线</span>
              <span class="contact-value">400-888-8888</span>
              <span class="contact-time">服务时间：9:00-21:00</span>
            </div>
            <button class="contact-btn call" @click="callService">
              <i class="fas fa-phone"></i>
            </button>
          </div>

          <div class="contact-item">
            <div class="contact-icon email">
              <i class="fas fa-envelope"></i>
            </div>
            <div class="contact-info">
              <span class="contact-label">官方邮箱</span>
              <span class="contact-value">support@example.com</span>
              <span class="contact-time">工作日 24 小时内回复</span>
            </div>
            <button class="contact-btn email" @click="sendEmail">
              <i class="fas fa-envelope"></i>
            </button>
          </div>

          <div class="contact-item">
            <div class="contact-icon wechat">
              <i class="fab fa-weixin"></i>
            </div>
            <div class="contact-info">
              <span class="contact-label">微信公众号</span>
              <span class="contact-value">智慧医疗服务平台</span>
              <span class="contact-time">关注获取更多服务</span>
            </div>
            <button class="contact-btn wechat" @click="followWechat">
              <i class="fas fa-qrcode"></i>
            </button>
          </div>

          <div class="contact-item">
            <div class="contact-icon address">
              <i class="fas fa-map-marker-alt"></i>
            </div>
            <div class="contact-info">
              <span class="contact-label">公司地址</span>
              <span class="contact-value">北京市朝阳区某某大厦</span>
              <span class="contact-time">欢迎来访（需预约）</span>
            </div>
            <button class="contact-btn map" @click="viewMap">
              <i class="fas fa-map"></i>
            </button>
          </div>
        </div>
      </div>

      <!-- 服务时间说明 -->
      <div class="service-hours">
        <div class="hours-card">
          <div class="hours-header">
            <i class="fas fa-clock"></i>
            <span>服务时间说明</span>
          </div>
          <div class="hours-content">
            <p>在线客服：7×24 小时</p>
            <p>电话客服：每天 9:00-21:00</p>
            <p>邮件咨询：工作日 24 小时内回复</p>
            <p>节假日服务时间可能有所调整，敬请谅解</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import NavBar from '@/components/common/NavBar.vue'

// 搜索关键词
const searchQuery = ref('')

// 反馈相关
const feedbackType = ref('bug')
const feedbackContent = ref('')
const feedbackContact = ref('')

// 常见问题数据
const faqAccount = ref([
  { 
    question: '如何注册账号？', 
    answer: '您可以通过手机号注册，点击登录页面的"注册"按钮，输入手机号并获取验证码，设置密码后即可成功注册。',
    expanded: false 
  },
  { 
    question: '忘记密码怎么办？', 
    answer: '在登录页面点击"忘记密码"，通过手机验证码验证身份后即可重置密码。',
    expanded: false 
  },
  { 
    question: '如何修改个人信息？', 
    answer: '进入个人中心页面，点击"编辑资料"，即可修改您的昵称、头像等个人信息。',
    expanded: false 
  }
])

const faqConsultation = ref([
  { 
    question: '如何发起在线问诊？', 
    answer: '在首页点击"在线问诊"，选择科室和医生，填写病情描述后即可发起问诊。',
    expanded: false 
  },
  { 
    question: '问诊响应时间是多久？', 
    answer: '一般情况下，医生会在 30 分钟内响应。如超过 1 小时未响应，系统会自动为您分配其他医生。',
    expanded: false 
  },
  { 
    question: '可以指定医生问诊吗？', 
    answer: '可以的，您可以在医生列表中选择您信任的医生进行问诊。',
    expanded: false 
  }
])

const faqMedicine = ref([
  { 
    question: '如何购买药品？', 
    answer: '在问诊结束后，医生会开具电子处方，您可以在处方页面直接购买药品。',
    expanded: false 
  },
  { 
    question: '药品多久能送到？', 
    answer: '一般情况下，药品会在 1-3 个工作日内送达。偏远地区可能需要更长时间。',
    expanded: false 
  },
  { 
    question: '支持退换货吗？', 
    answer: '药品属于特殊商品，非质量问题不支持退换货。如收到药品有质量问题，请及时联系客服处理。',
    expanded: false 
  }
])

const faqPayment = ref([
  { 
    question: '支持哪些支付方式？', 
    answer: '目前支持微信支付、支付宝、银联卡等多种支付方式。',
    expanded: false 
  },
  { 
    question: '支付失败怎么办？', 
    answer: '支付失败可能是网络原因或余额不足，建议您检查网络状态或更换支付方式。如问题持续，请联系客服。',
    expanded: false 
  },
  { 
    question: '如何申请退款？', 
    answer: '在订单页面找到对应订单，点击"申请退款"，填写退款原因后即可提交。审核通过后，款项将原路返回。',
    expanded: false 
  }
])

// 方法
const searchHelp = () => {
  if (!searchQuery.value.trim()) {
    alert('请输入搜索关键词')
    return
  }
  alert(`正在搜索"${searchQuery.value}"...`)
}

const toggleFaq = (index, category) => {
  const map = {
    'account': faqAccount,
    'consultation': faqConsultation,
    'medicine': faqMedicine,
    'payment': faqPayment
  }
  map[category].value[index].expanded = !map[category].value[index].expanded
}

const contactOnlineService = () => {
  alert('正在连接在线客服...')
}

const submitFeedback = () => {
  alert('意见反馈页面开发中...')
}

const callService = () => {
  alert('正在拨打客服热线：400-888-8888')
}

const rateApp = () => {
  alert('感谢支持！评分页面开发中...')
}

const submitFeedbackForm = () => {
  if (!feedbackContent.value.trim()) {
    alert('请填写反馈内容')
    return
  }
  alert('感谢您的反馈！我们会尽快处理并回复您。')
  feedbackContent.value = ''
  feedbackContact.value = ''
}

const sendEmail = () => {
  alert('正在打开邮件客户端...')
}

const followWechat = () => {
  alert('微信公众号：智慧医疗服务平台')
}

const viewMap = () => {
  alert('地图页面开发中...')
}
</script>

<style scoped>
.settings-help {
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
.help-banner {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  border-radius: 20px;
  padding: 24px;
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: relative;
  overflow: hidden;
  box-shadow: 0 8px 24px rgba(79, 172, 254, 0.4);
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

/* 搜索框 */
.search-section {
  margin-bottom: 20px;
}

.search-box {
  display: flex;
  align-items: center;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  padding: 8px 16px;
  backdrop-filter: blur(10px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.search-box i {
  font-size: 18px;
  color: #999;
  margin-right: 12px;
}

.search-input {
  flex: 1;
  border: none;
  outline: none;
  font-size: 15px;
  color: #333;
  background: transparent;
}

.search-input::placeholder {
  color: #ccc;
}

.search-btn {
  background: linear-gradient(135deg, #4facfe, #00f2fe);
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 12px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.search-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(79, 172, 254, 0.4);
}

/* 快捷入口 */
.quick-links {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  margin-bottom: 20px;
}

.quick-link-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  transition: transform 0.3s ease;
}

.quick-link-item:hover {
  transform: translateY(-4px);
}

.link-icon {
  width: 56px;
  height: 56px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: white;
}

.link-icon.online {
  background: linear-gradient(135deg, #667eea, #764ba2);
}

.link-icon.feedback {
  background: linear-gradient(135deg, #f093fb, #f5576c);
}

.link-icon.phone {
  background: linear-gradient(135deg, #4facfe, #00f2fe);
}

.link-icon.rate {
  background: linear-gradient(135deg, #43e97b, #38f9d7);
}

.link-label {
  font-size: 13px;
  color: #666;
  font-weight: 600;
}

/* 常见问题 */
.faq-section {
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

.faq-list {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  padding: 16px;
  backdrop-filter: blur(10px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.faq-category {
  margin-bottom: 20px;
}

.faq-category:last-child {
  margin-bottom: 0;
}

.category-title {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
  padding-bottom: 12px;
  border-bottom: 2px solid #f0f0f0;
}

.category-title i {
  font-size: 18px;
  color: #4facfe;
}

.category-title span {
  font-size: 16px;
  font-weight: 700;
  color: #333;
}

.faq-items {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.faq-item {
  background: rgba(79, 172, 254, 0.05);
  border-radius: 12px;
  overflow: hidden;
  cursor: pointer;
  transition: background 0.3s ease;
}

.faq-item:hover {
  background: rgba(79, 172, 254, 0.1);
}

.faq-question {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 14px 16px;
}

.faq-question span {
  font-size: 15px;
  color: #333;
  font-weight: 600;
}

.faq-question i {
  font-size: 14px;
  color: #999;
  transition: transform 0.3s ease;
}

.faq-answer {
  padding: 0 16px 16px 16px;
  font-size: 14px;
  line-height: 1.8;
  color: #666;
  border-top: 1px solid rgba(79, 172, 254, 0.1);
  margin-top: 8px;
  padding-top: 12px;
}

/* 意见反馈 */
.feedback-section {
  margin-bottom: 20px;
}

.feedback-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  padding: 20px;
  backdrop-filter: blur(10px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.feedback-type {
  margin-bottom: 16px;
}

.type-label {
  display: block;
  font-size: 14px;
  font-weight: 600;
  color: #666;
  margin-bottom: 8px;
}

.type-select {
  width: 100%;
  padding: 12px 16px;
  border: 2px solid #e0e0e0;
  border-radius: 12px;
  font-size: 15px;
  color: #333;
  background: white;
  cursor: pointer;
  transition: border-color 0.3s ease;
  outline: none;
}

.type-select:focus {
  border-color: #4facfe;
}

.feedback-content {
  margin-bottom: 16px;
}

.feedback-textarea {
  width: 100%;
  padding: 12px 16px;
  border: 2px solid #e0e0e0;
  border-radius: 12px;
  font-size: 15px;
  color: #333;
  background: white;
  resize: none;
  transition: border-color 0.3s ease;
  outline: none;
  font-family: inherit;
}

.feedback-textarea:focus {
  border-color: #4facfe;
}

.feedback-textarea::placeholder {
  color: #ccc;
}

.feedback-contact {
  margin-bottom: 20px;
}

.contact-input {
  width: 100%;
  padding: 12px 16px;
  border: 2px solid #e0e0e0;
  border-radius: 12px;
  font-size: 15px;
  color: #333;
  background: white;
  transition: border-color 0.3s ease;
  outline: none;
}

.contact-input:focus {
  border-color: #4facfe;
}

.contact-input::placeholder {
  color: #ccc;
}

.submit-btn {
  width: 100%;
  height: 48px;
  background: linear-gradient(135deg, #4facfe, #00f2fe);
  color: white;
  border: none;
  border-radius: 24px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(79, 172, 254, 0.3);
}

.submit-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(79, 172, 254, 0.4);
}

/* 联系方式 */
.contact-section {
  margin-bottom: 20px;
}

.contact-list {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  overflow: hidden;
  backdrop-filter: blur(10px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.contact-item {
  display: flex;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid #f0f0f0;
}

.contact-item:last-child {
  border-bottom: none;
}

.contact-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  color: white;
  flex-shrink: 0;
  margin-right: 12px;
}

.contact-icon.phone {
  background: linear-gradient(135deg, #667eea, #764ba2);
}

.contact-icon.email {
  background: linear-gradient(135deg, #4facfe, #00f2fe);
}

.contact-icon.wechat {
  background: linear-gradient(135deg, #07c160, #06ad56);
}

.contact-icon.address {
  background: linear-gradient(135deg, #fa709a, #fee140);
}

.contact-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.contact-label {
  font-size: 15px;
  font-weight: 600;
  color: #333;
}

.contact-value {
  font-size: 14px;
  color: #4facfe;
  font-weight: 600;
}

.contact-time {
  font-size: 12px;
  color: #999;
}

.contact-btn {
  width: 40px;
  height: 40px;
  border-radius: 12px;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  color: white;
  cursor: pointer;
  transition: all 0.3s ease;
  flex-shrink: 0;
  margin-left: 12px;
}

.contact-btn.call {
  background: linear-gradient(135deg, #43e97b, #38f9d7);
}

.contact-btn.email {
  background: linear-gradient(135deg, #4facfe, #00f2fe);
}

.contact-btn.wechat {
  background: linear-gradient(135deg, #07c160, #06ad56);
}

.contact-btn.map {
  background: linear-gradient(135deg, #fa709a, #fee140);
}

.contact-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

/* 服务时间说明 */
.service-hours {
  margin-bottom: 20px;
}

.hours-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  padding: 20px;
  backdrop-filter: blur(10px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.hours-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 2px solid #f0f0f0;
}

.hours-header i {
  font-size: 20px;
  color: #4facfe;
}

.hours-header span {
  font-size: 16px;
  font-weight: 700;
  color: #333;
}

.hours-content {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.hours-content p {
  font-size: 14px;
  color: #666;
  line-height: 1.8;
  margin: 0;
}
</style>
