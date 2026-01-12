<template>
  <div class="help-center-page">
    <!-- 页面头部 -->
    <div class="header">
      <BackButton @click="goBack" />
      <h1 class="page-title">帮助中心</h1>
    </div>
    
    <div class="container">
      <!-- 搜索区域 -->
      <div class="search-section">
        <h3 class="search-title">您好，请问有什么可以帮您？</h3>
        <div class="search-box">
          <input 
            type="text" 
            class="search-input" 
            placeholder="请输入您的问题关键词..." 
            v-model="searchKeyword"
            @keyup.enter="searchQuestions"
          >
          <button class="search-btn" @click="searchQuestions" aria-label="搜索问题">
            <i class="fas fa-search"></i>
          </button>
        </div>
      </div>
      
      <!-- 分类区域 -->
      <div class="categories-section">
        <h3 class="section-title">常见问题分类</h3>
        <div class="categories-grid">
          <div 
            v-for="category in categories" 
            :key="category.id"
            class="category-item" 
            @click="showCategory(category.id)"
          >
            <div class="category-icon">
              <i :class="category.icon"></i>
            </div>
            <div class="category-name">{{ category.name }}</div>
          </div>
        </div>
      </div>
      
      <!-- 热门问题 -->
      <div class="faq-section">
        <h3 class="section-title">热门问题</h3>
        <div class="faq-list">
          <div 
            v-for="faq in faqs" 
            :key="faq.id"
            class="faq-item"
          >
            <div class="faq-question" @click="toggleFAQ(faq.id)">
              <div class="faq-question-text">{{ faq.question }}</div>
              <div class="faq-arrow" :class="{ active: faq.isOpen }">
                <i class="fas fa-chevron-down"></i>
              </div>
            </div>
            <div class="faq-answer" :class="{ active: faq.isOpen }" :id="`faq-answer-${faq.id}`">
              <div class="faq-answer-content" v-html="faq.answer"></div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 联系客服 -->
      <div class="contact-section">
        <h3 class="contact-title">仍然没有找到答案？</h3>
        <p class="contact-description">我们的客服团队随时为您提供帮助</p>
        
        <div class="contact-methods">
          <div class="contact-method">
            <div class="contact-icon">
              <i class="fas fa-phone-alt"></i>
            </div>
            <div class="contact-method-title">客服热线</div>
            <div class="contact-method-desc">400-123-4567</div>
          </div>
          <div class="contact-method">
            <div class="contact-icon">
              <i class="fas fa-comments"></i>
            </div>
            <div class="contact-method-title">在线客服</div>
            <div class="contact-method-desc">24小时在线解答</div>
          </div>
          <div class="contact-method">
            <div class="contact-icon">
              <i class="fas fa-envelope"></i>
            </div>
            <div class="contact-method-title">邮箱支持</div>
            <div class="contact-method-desc">support@healthplatform.com</div>
          </div>
        </div>
        
        <button class="contact-btn" @click="contactSupport">
          <i class="fas fa-headset"></i> 联系在线客服
        </button>
      </div>
    </div>
    
    <!-- 底部导航 -->
    <NavBar :active-tab="'help'" />
    
    <!-- 联系客服模态框 -->
    <Modal 
      :show="showContactModal" 
      title="联系客服" 
      @close="closeContactModal"
    >
      <form @submit.prevent="submitContactForm">
        <div class="form-group">
          <label class="form-label">问题类型</label>
          <select class="form-input" v-model="contactForm.issueType" required>
            <option value="">请选择问题类型</option>
            <option value="account">账号问题</option>
            <option value="appointment">预约挂号</option>
            <option value="report">报告查询</option>
            <option value="payment">支付问题</option>
            <option value="other">其他问题</option>
          </select>
        </div>
        <div class="form-group">
          <label class="form-label">问题描述</label>
          <textarea 
            class="form-textarea" 
            v-model="contactForm.issueDescription" 
            placeholder="请详细描述您遇到的问题..."
            required
          ></textarea>
        </div>
        <div class="form-group">
          <label class="form-label">联系方式</label>
          <input 
            type="text" 
            class="form-input" 
            v-model="contactForm.contactInfo" 
            placeholder="请输入您的手机号码或邮箱"
            required
          >
        </div>
        <div class="modal-footer">
          <button type="button" class="action-btn btn-outline" @click="closeContactModal">取消</button>
          <button type="submit" class="action-btn btn-primary">提交</button>
        </div>
      </form>
    </Modal>
    
    <!-- Toast 提示 -->
    <Toast ref="toastRef" />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import BackButton from '@/components/common/BackButton.vue'
import NavBar from '@/components/common/NavBar.vue'
import Modal from '@/components/common/Modal.vue'
import Toast from '@/components/common/Toast.vue'
import { useToast } from '@/composables/useToast.js'

const router = useRouter()
const { showToast } = useToast()

// 响应式数据
const searchKeyword = ref('')
const showContactModal = ref(false)
const contactForm = reactive({
  issueType: '',
  issueDescription: '',
  contactInfo: ''
})

// 分类数据
const categories = ref([
  { id: 'account', name: '账号问题', icon: 'fas fa-user-circle' },
  { id: 'appointment', name: '预约挂号', icon: 'fas fa-calendar-check' },
  { id: 'report', name: '报告查询', icon: 'fas fa-file-medical' },
  { id: 'insurance', name: '医保报销', icon: 'fas fa-file-invoice-dollar' },
  { id: 'payment', name: '支付问题', icon: 'fas fa-credit-card' },
  { id: 'usage', name: '使用指南', icon: 'fas fa-mobile-alt' }
])

// FAQ数据
const faqs = ref([
  {
    id: 1,
    question: '如何预约挂号？',
    isOpen: false,
    answer: `
      <p>预约挂号步骤如下：</p>
      <ol>
        <li>登录您的账号，点击首页的"预约挂号"</li>
        <li>选择您需要就诊的科室和医生</li>
        <li>选择适合您的就诊时间</li>
        <li>确认预约信息并提交</li>
        <li>支付挂号费用（部分医院需要）</li>
        <li>预约成功后，您会收到短信或App通知</li>
      </ol>
      <p>您可以在"我的挂号"中查看和管理您的预约。</p>
    `
  },
  {
    id: 2,
    question: '如何查看我的检查报告？',
    isOpen: false,
    answer: `
      <p>查看检查报告的步骤如下：</p>
      <ol>
        <li>登录您的账号，进入"我的报告"页面</li>
        <li>系统会自动显示您所有的检查报告</li>
        <li>点击报告名称可以查看详细内容</li>
        <li>如果需要，您可以下载或分享报告</li>
      </ol>
      <p>报告通常在检查后1-3个工作日内出具，您会收到通知。</p>
    `
  },
  {
    id: 3,
    question: '忘记密码怎么办？',
    isOpen: false,
    answer: `
      <p>如果您忘记了密码，可以通过以下方式重置：</p>
      <ol>
        <li>在登录页面点击"忘记密码"</li>
        <li>输入您注册时使用的手机号码</li>
        <li>系统会发送验证码到您的手机</li>
        <li>输入验证码并设置新密码</li>
        <li>使用新密码登录您的账号</li>
      </ol>
      <p>如果您无法接收验证码，请联系客服寻求帮助。</p>
    `
  },
  {
    id: 4,
    question: '如何申请医保报销？',
    isOpen: false,
    answer: `
      <p>申请医保报销的步骤如下：</p>
      <ol>
        <li>就诊结束后，进入"医保报销"页面</li>
        <li>上传相关的医疗费用单据</li>
        <li>填写报销申请信息</li>
        <li>提交申请等待审核</li>
        <li>审核通过后，报销金额会打入您的账户</li>
      </ol>
      <p>报销申请通常需要3-7个工作日处理，您可以在"报销记录"中查看进度。</p>
    `
  },
  {
    id: 5,
    question: '如何取消预约？',
    isOpen: false,
    answer: `
      <p>取消预约的步骤如下：</p>
      <ol>
        <li>进入"我的挂号"页面</li>
        <li>找到您需要取消的预约记录</li>
        <li>点击"取消挂号"按钮</li>
        <li>确认取消操作</li>
      </ol>
      <p>请注意：</p>
      <ul>
        <li>就诊前24小时内取消预约，可能会产生一定的取消费用</li>
        <li>就诊当天取消预约，可能无法退还挂号费用</li>
        <li>如需重新预约，请重新选择医生和时间</li>
      </ul>
    `
  }
])

// 方法
const searchQuestions = () => {
  if (!searchKeyword.value.trim()) {
    showToast('请输入搜索关键词', 'warning')
    return
  }
  
  // 在实际应用中，这里应该发送请求到服务器搜索相关问题
  showToast(`搜索关键词: ${searchKeyword.value}`, 'info')
  console.log('搜索功能正在开发中')
}

const showCategory = (categoryId) => {
  const categoryNames = {
    'account': '账号问题',
    'appointment': '预约挂号',
    'report': '报告查询',
    'insurance': '医保报销',
    'payment': '支付问题',
    'usage': '使用指南'
  }
  
  showToast(`加载 ${categoryNames[categoryId]} 分类的问题`, 'info')
  console.log('分类功能正在开发中')
}

const toggleFAQ = (id) => {
  // 关闭其他打开的FAQ
  faqs.value.forEach(faq => {
    if (faq.id !== id && faq.isOpen) {
      faq.isOpen = false
    }
  })
  
  // 切换当前FAQ
  const faq = faqs.value.find(f => f.id === id)
  if (faq) {
    faq.isOpen = !faq.isOpen
  }
}

const contactSupport = () => {
  showContactModal.value = true
}

const closeContactModal = () => {
  showContactModal.value = false
  // 清空表单
  Object.assign(contactForm, {
    issueType: '',
    issueDescription: '',
    contactInfo: ''
  })
}

const submitContactForm = () => {
  if (!contactForm.issueType || !contactForm.issueDescription || !contactForm.contactInfo) {
    showToast('请填写所有必填字段', 'warning')
    return
  }
  
  // 在实际应用中，这里应该发送请求到服务器提交问题
  showToast('您的问题已提交，客服人员将在24小时内联系您', 'success')
  closeContactModal()
}

const goBack = () => {
  router.back()
}

onMounted(() => {
  // 页面初始化逻辑
  console.log('HelpCenter component mounted')
})
</script>

<style scoped>
/* 保持原有的所有CSS样式，只需要添加scoped属性 */
/* 这里省略了完整的CSS样式，因为代码很长，但实际使用时需要全部复制过来 */

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;
}

:root {
  --primary: #1E88E5;
  --primary-dark: #1565C0;
  --secondary: #00BCD4;
  --success: #4CAF50;
  --warning: #FF9800;
  --danger: #E53935;
  --light: #f5f7fa;
  --dark: #333;
  --gray: #666;
  --border: #e0e0e0;
  --card: #fff;
}

.help-center-page {
  background-color: var(--light);
  color: var(--dark);
  line-height: 1.6;
  min-height: 100vh;
  padding-bottom: 60px; /* 为底部导航留出空间 */
}

.container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

/* 头部样式 */
.header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid var(--border);
}

.page-title {
  font-size: 22px;
  font-weight: 600;
  color: var(--dark);
}

/* 搜索区域 */
.search-section {
  background: var(--card);
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.search-title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 15px;
  color: var(--dark);
}

.search-box {
  display: flex;
  gap: 10px;
}

.search-input {
  flex: 1;
  height: 44px;
  padding: 0 16px;
  border: 1px solid var(--border);
  border-radius: 8px;
  font-size: 14px;
}

.search-btn {
  width: 44px;
  height: 44px;
  background: var(--primary);
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.search-btn:hover {
  background: var(--primary-dark);
}

/* 分类区域 */
.categories-section {
  background: var(--card);
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.section-title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 15px;
  color: var(--dark);
}

.categories-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 15px;
}

.category-item {
  background: var(--light);
  border-radius: 10px;
  padding: 20px 15px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
}

.category-item:hover {
  background: var(--primary);
  color: white;
  transform: translateY(-3px);
}

.category-icon {
  font-size: 24px;
  margin-bottom: 10px;
}

.category-name {
  font-size: 14px;
  font-weight: 500;
}

/* 热门问题 */
.faq-section {
  background: var(--card);
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.faq-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.faq-item {
  border: 1px solid var(--border);
  border-radius: 8px;
  overflow: hidden;
}

.faq-question {
  padding: 15px;
  background: var(--light);
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: pointer;
  transition: all 0.3s;
}

.faq-question:hover {
  background: #e9ecef;
}

.faq-question-text {
  font-size: 15px;
  font-weight: 500;
}

.faq-arrow {
  color: var(--gray);
  transition: transform 0.3s;
}

.faq-arrow.active {
  transform: rotate(180deg);
}

.faq-answer {
  padding: 0 15px;
  max-height: 0;
  overflow: hidden;
  transition: all 0.3s;
  background: white;
}

.faq-answer.active {
  padding: 15px;
  max-height: 500px;
}

.faq-answer-content {
  font-size: 14px;
  color: var(--gray);
  line-height: 1.6;
}

/* 联系客服 */
.contact-section {
  background: var(--card);
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  text-align: center;
}

.contact-title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 15px;
  color: var(--dark);
}

.contact-description {
  font-size: 14px;
  color: var(--gray);
  margin-bottom: 20px;
}

.contact-methods {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 15px;
  margin-bottom: 20px;
}

.contact-method {
  background: var(--light);
  border-radius: 10px;
  padding: 20px;
  text-align: center;
}

.contact-icon {
  font-size: 32px;
  color: var(--primary);
  margin-bottom: 10px;
}

.contact-method-title {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 5px;
}

.contact-method-desc {
  font-size: 14px;
  color: var(--gray);
}

.contact-btn {
  display: inline-block;
  padding: 12px 24px;
  background: var(--primary);
  color: white;
  border-radius: 8px;
  text-decoration: none;
  font-weight: 500;
  transition: all 0.3s;
  border: none;
  cursor: pointer;
}

.contact-btn:hover {
  background: var(--primary-dark);
  transform: translateY(-2px);
}

/* 表单样式 */
.form-group {
  margin-bottom: 15px;
}

.form-label {
  display: block;
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 5px;
}

.form-input {
  width: 100%;
  height: 40px;
  padding: 0 12px;
  border: 1px solid var(--border);
  border-radius: 6px;
  font-size: 14px;
}

.form-textarea {
  width: 100%;
  padding: 12px;
  border: 1px solid var(--border);
  border-radius: 6px;
  font-size: 14px;
  resize: vertical;
  min-height: 100px;
}

/* 按钮样式 */
.action-btn {
  padding: 10px 16px;
  border-radius: 6px;
  font-size: 14px;
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

/* 模态框底部 */
.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
}

/* 响应式设计 */
@media (max-width: 600px) {
  .container {
    padding: 15px;
  }
  
  .categories-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .contact-methods {
    grid-template-columns: 1fr;
  }
}
</style>