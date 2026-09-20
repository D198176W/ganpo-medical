<template>
  <div class="login-page">
    <!-- 全屏背景图片 -->
    <div class="fullscreen-bg">
      <img :src="backgroundImage" alt="登录页面背景图">
    </div>

    <div class="login-container">
      <!-- 登录表单卡片 -->
      <div class="login-card">
        <div class="logo">
          <img :src="logoImage" alt="Logo">
          <span class="logo-text">赣鄱医枢RAG增强大模型</span>
        </div>

        <div class="tab-bar">
          <button 
            class="tab-btn" 
            :class="{ active: currentTab === 0 }" 
            @click="switchTab(0)"
          >
            账号登录
          </button>
          <button 
            class="tab-btn" 
            :class="{ active: currentTab === 1 }" 
            @click="switchTab(1)"
          >
            学号注册
          </button>
        </div>

        <!-- 登录表单 -->
        <form v-if="currentTab === 0" class="login-form">
          <div class="form-step" :class="{ hidden: loginStep !== 1 }">
            <div class="step-title">
              <span>1. 输入账号</span>
              <span class="step-desc">支持手机号</span>
            </div>
            <div class="form-item">
              <input 
                type="text" 
                v-model="loginForm.account"
                placeholder="请输入手机号" 
              />
            </div>
            <button type="button" class="next-btn" @click="handleLoginNext">
              下一步
            </button>
          </div>

          <div class="form-step" :class="{ hidden: loginStep !== 2 }">
            <div class="step-title">
              <span>2. 验证身份</span>
              <span class="step-desc">{{ accountMaskedText }}</span>
            </div>
            <div class="form-item code-item">
              <input 
                type="number" 
                v-model="loginForm.code"
                placeholder="请输入6位验证码" 
              />
              <button 
                type="button" 
                class="get-code-btn" 
                :disabled="loginCodeDisabled"
                @click="getLoginCode"
              >
                {{ loginCodeText }}
              </button>
            </div>
            <button type="button" class="prev-btn" @click="loginStep = 1">
              ← 返回
            </button>
            <button type="button" class="submit-btn" @click="handleLoginSubmit">
              登录
            </button>
          </div>
        </form>

        <!-- 注册表单 -->
        <form v-else class="register-form">
          <div class="form-step" :class="{ hidden: registerStep !== 1 }">
            <div class="step-title">
              <span>1. 填写信息</span>
              <span class="step-desc">仅南昌高校学生可注册</span>
            </div>
            <div class="form-item">
              <input 
                type="text" 
                v-model="registerForm.studentId"
                placeholder="请输入学号（如：2023001001）" 
              />
            </div>

            <div class="form-item">
              <select v-model="registerForm.collegeIndex" class="college-select">
                <option value="-1">请选择南昌高校</option>
                <option v-for="(college, index) in colleges" :key="index" :value="index">
                  {{ college }}
                </option>
              </select>
            </div>
            <button type="button" class="next-btn" @click="handleRegisterNext">
              下一步
            </button>
          </div>

          <div class="form-step" :class="{ hidden: registerStep !== 2 }">
            <div class="step-title">
              <span>2. 验证手机</span>
              <span class="step-desc">用于账号安全与消息通知</span>
            </div>
            <div class="form-item">
              <input 
                type="number" 
                v-model="registerForm.phone"
                placeholder="请输入手机号" 
              />
            </div>
            <div class="form-item code-item">
              <input 
                type="number" 
                v-model="registerForm.code"
                placeholder="请输入6位验证码" 
              />
              <button 
                type="button" 
                class="get-code-btn" 
                :disabled="registerCodeDisabled"
                @click="getRegisterCode"
              >
                {{ registerCodeText }}
              </button>
            </div>
            
            <div class="agreement">
              <input 
                type="checkbox" 
                v-model="agreementChecked"
                id="agreement" 
              />
              <span>我已阅读并同意</span>
              <span class="highlight" @click="showUserAgreement">《用户协议》</span>
              <span>及</span>
              <span class="highlight" @click="showDisclaimer">《医疗建议免责声明》</span>
            </div>
            
            <button type="button" class="prev-btn" @click="registerStep = 1">
              ← 返回
            </button>
            <button 
              type="button" 
              class="submit-btn" 
              :disabled="!agreementChecked"
              @click="handleRegisterSubmit"
            >
              完成注册
            </button>
          </div>
        </form>

        <button class="wechat-login-btn" @click="handleWechatLogin">
          <img :src="wechatIcon" alt="微信图标">
          <span>微信快捷登录</span>
        </button>
      </div>

      <!-- 用户协议模态框 -->
      <Modal 
       :show="showUserAgreementModal"   
        title="用户协议"
        @close="showUserAgreementModal = false"
      >
        <div class="modal-content">
          <p class="disclaimer-bold">1. 服务条款</p>
          <p>1.1 本服务由赣鄱医枢 RAG 增强大模型提供，用户在使用本服务前应仔细阅读本协议。</p>
          <p>1.2 用户点击"注册"或"登录"按钮，即表示用户同意并接受本协议的全部条款。</p>
          
          <p class="disclaimer-bold">2. 用户信息</p>
          <p>2.1 用户承诺提供真实、准确、完整的个人信息，包括但不限于学号、手机号等。</p>
          <p>2.2 用户应妥善保管账号信息，对账号下的所有行为负责。</p>
        </div>
        <template #footer>
          <button class="modal-confirm" @click="showUserAgreementModal = false">
            我已阅读并理解
          </button>
        </template>
      </Modal>

      <!-- 免责声明弹窗 -->
      <Modal 
        :show="showDisclaimerModal" 
        title="医疗建议免责声明"
        @close="showDisclaimerModal = false"
      >
        <div class="modal-content">
          <p class="disclaimer-bold">1. 本系统提供的所有医疗建议仅供参考，不构成诊断或治疗方案。</p>
          <p>本系统基于人工智能技术提供健康咨询服务，其建议不能替代专业医师的诊断和治疗方案。</p>
          
          <p class="disclaimer-bold">2. 具体健康问题请务必咨询专业医疗机构及医生，遵医嘱进行诊疗。</p>
        </div>
        <template #footer>
          <button class="modal-confirm" @click="showDisclaimerModal = false">
            我已阅读并理解
          </button>
        </template>
      </Modal>

      <!-- Toast 提示 -->
      <Toast 
        :show="showToast" 
        :message="toastMessage" 
        @hide="showToast = false"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { setStorage, getStorage } from '@/utils/storage.js'
import Modal from '@/components/common/Modal.vue'
import Toast from '@/components/common/Toast.vue'
import { sendCode, register as apiRegister, login as apiLogin, wechatLogin as apiWechatLogin } from '@/api/auth.js'

const router = useRouter()

// Toast 相关
const showToast = ref(false)
const toastMessage = ref('')
const toastType = ref('info') // success, warning, error, info

const showtoast = (message, type = 'info') => {
  toastMessage.value = message
  toastType.value = type
  showToast.value = true
  setTimeout(() => {
    showToast.value = false
  }, 2000)
}

// 图片路径
import logo from '@/assets/images/logo.png' 
import wechat from '@/assets/images/wechat-icon.png'
const backgroundImage = logo
const logoImage = logo
const wechatIcon = wechat


// const backgroundImage = '@/assets/images/logo.png' 
// const logoImage = '@/assets/images/logo.png' 
// const wechatIcon = '@/assets/images/wechat-icon.png'
// 响应式数据
const currentTab = ref(0)
const loginStep = ref(1)
const registerStep = ref(1)
const loginCodeDisabled = ref(false)
const loginCodeText = ref('获取验证码')
const registerCodeDisabled = ref(false)
const registerCodeText = ref('获取验证码')
const agreementChecked = ref(false)
const showUserAgreementModal = ref(false)
const showDisclaimerModal = ref(false)

// 表单数据
const loginForm = reactive({
  account: '',
  code: ''
})

const registerForm = reactive({
  studentId: '',
  collegeIndex: -1,
  phone: '',
  code: ''
})

// 高校数据
const colleges = ref([
  "南昌大学", "江西师范大学", "南昌航空大学", "江西财经大学", 
  "华东交通大学", "南昌理工学院", "南昌工学院", "江西农业大学", 
  "江西科技师范大学"
])

// 计算属性
const accountMaskedText = computed(() => {
  if (!loginForm.account) return '验证码已发送至 '
  
  const isPhone = /^1[3-9]\d{9}$/.test(loginForm.account)
  const isStudentId = /^\d{10}$/.test(loginForm.account)
  
  if (isPhone) {
    return `验证码已发送至 ${loginForm.account.substr(0, 3)}****${loginForm.account.substr(7)}`
  } else if (isStudentId) {
    return `验证码已发送至 ${loginForm.account.substr(0, 4)}****${loginForm.account.substr(8)}`
  }
  
  return '验证码已发送至 '
})

// 方法
const switchTab = (tabIndex) => {
  currentTab.value = tabIndex
  resetFormStates()
}

const resetFormStates = () => {
  loginStep.value = 1
  registerStep.value = 1
  Object.assign(loginForm, { account: '', code: '' })
  Object.assign(registerForm, { studentId: '', collegeIndex: -1, phone: '', code: '' })
  agreementChecked.value = false
  resetCodeButtons()
}

const resetCodeButtons = () => {
  loginCodeDisabled.value = false
  loginCodeText.value = '获取验证码'
  registerCodeDisabled.value = false
  registerCodeText.value = '获取验证码'
}

const handleLoginNext = () => {
  if (!loginForm.account.trim()) {
    showtoast('请输入手机号或学号', 'warning')
    return
  }
  
  const isPhone = /^1[3-9]\d{9}$/.test(loginForm.account)
  const isStudentId = /^\d{10}$/.test(loginForm.account)
  
  if (!isPhone && !isStudentId) {
    showtoast('请输入正确的手机号或 10 位学号', 'warning')
    return
  }
  
  loginStep.value = 2
  getLoginCode()
}

const getLoginCode = async () => {
  try {
    // 调用发送验证码 API
    await sendCode(loginForm.account, 'login')
    
    let countdown = 60
    loginCodeDisabled.value = true
    loginCodeText.value = `${countdown}秒后重发`
    
    const timer = setInterval(() => {
      countdown--
      if (countdown <= 0) {
        clearInterval(timer)
        loginCodeDisabled.value = false
        loginCodeText.value = '获取验证码'
      } else {
        loginCodeText.value = `${countdown}秒后重发`
      }
    }, 1000)
    
    showtoast('验证码已发送', 'success')
  } catch (error) {
    console.error('发送验证码失败:', error)
    showtoast(error.response?.data?.message || '发送验证码失败，请稍后重试', 'error')
    loginCodeDisabled.value = false
    loginCodeText.value = '获取验证码'
  }
}

const handleLoginSubmit = async () => {
  if (!loginForm.code || loginForm.code.length !== 6) {
    showtoast('请输入 6 位验证码', 'warning')
    return
  }
  
  try {
    // 调用登录 API
    const result = await apiLogin(loginForm.account, loginForm.code)
    
    // 保存用户信息到本地存储
    const userTag = /^\d{10}$/.test(loginForm.account) ? "student" : "other"
    const userInfo = {
      account: loginForm.account,
      userTag,
      college: userTag === "student" ? guessCollege(loginForm.account) : "",
      hasLoginBefore: true,
      ...result.data // 合并后端返回的用户信息
    }
    
    setStorage('userInfo', userInfo)
    
    showToast('登录成功，即将跳转到首页', 'success')
    
    // 跳转到首页
    setTimeout(async () => {
      try {
        await router.push('/')
      } catch (error) {
        console.error('登录跳转失败：', error)
        showtoast('跳转失败，请手动进入首页', 'error')
      }
    }, 1000)
  } catch (error) {
    console.error('登录失败:', error)
    showtoast(error.response?.data?.message || '登录失败，请检查验证码是否正确', 'error')
  }
}

const handleRegisterNext = () => {
  if (!registerForm.studentId.trim()) {
    showtoast('请输入学号', 'warning')
    return
  }
  
  if (registerForm.collegeIndex === -1) {
    showtoast('请选择所属高校', 'warning')
    return
  }
  
  if (!/^\d{10}$/.test(registerForm.studentId)) {
    showtoast('请输入 10 位数字学号', 'warning')
    return
  }
  
  registerStep.value = 2
}

const getRegisterCode = async () => {
  if (!registerForm.phone || registerForm.phone.length !== 11) {
    showtoast('请先输入正确的手机号', 'warning')
    return
  }
  
  try {
    // 调用发送验证码 API
    await sendCode(registerForm.phone, 'register')
    
    let countdown = 60
    registerCodeDisabled.value = true
    registerCodeText.value = `${countdown}秒后重发`
    
    const timer = setInterval(() => {
      countdown--
      if (countdown <= 0) {
        clearInterval(timer)
        registerCodeDisabled.value = false
        registerCodeText.value = '获取验证码'
      } else {
        registerCodeText.value = `${countdown}秒后重发`
      }
    }, 1000)
    
    showtoast('验证码已发送', 'success')
  } catch (error) {
    console.error('发送验证码失败:', error)
    showtoast(error.response?.data?.message || '发送验证码失败，请稍后重试', 'error')
    registerCodeDisabled.value = false
    registerCodeText.value = '获取验证码'
  }
}

const handleRegisterSubmit = async () => {
  if (!registerForm.phone || registerForm.phone.length !== 11) {
    showtoast('请输入正确的手机号', 'warning')
    return
  }
  
  if (!registerForm.code || registerForm.code.length !== 6) {
    showtoast('请输入 6 位验证码', 'warning')
    return
  }
  
  if (!agreementChecked.value) {
    showtoast('请阅读并同意用户协议和免责声明', 'warning')
    return
  }
  
  try {
    // 调用注册 API
    const result = await apiRegister({
      studentId: registerForm.studentId,
      phone: registerForm.phone,
      code: registerForm.code,
      college: colleges.value[registerForm.collegeIndex]
    })
    
    // 保存用户信息到本地存储
    const userInfo = {
      studentId: registerForm.studentId,
      phone: registerForm.phone,
      college: colleges.value[registerForm.collegeIndex],
      userTag: "student",
      hasLoginBefore: true,
      ...result.data // 合并后端返回的用户信息
    }
    
    setStorage('userInfo', userInfo)
    
    showtoast('注册成功，即将跳转到首页', 'success')
    
    // 跳转到首页
    setTimeout(async () => {
      try {
        await router.push('/')
      } catch (error) {
        console.error('注册跳转失败：', error)
        showtoast('跳转失败，请手动进入首页', 'error')
      }
    }, 1000)
  } catch (error) {
    console.error('注册失败:', error)
    showtoast(error.response?.data?.message || '注册失败，请稍后重试', 'error')
  }
}

const handleWechatLogin = async () => {
  // 这里需要后端实现微信 OAuth 流程
  // 简化处理：模拟微信登录
  if (confirm("是否允许微信授权登录？")) {
    try {
      // 调用微信登录 API（需要后端实现）
      // const result = await apiWechatLogin('mock-wechat-code')
      
      // 保存用户信息到本地存储
      const userInfo = {
        nickName: "微信用户",
        avatarUrl: wechatIcon,
        userTag: "other",
        hasLoginBefore: true
      }
      
      setStorage('userInfo', userInfo)
      
      showtoast('微信登录成功，即将跳转到首页', 'success')
      
      // 跳转到首页
      setTimeout(async () => {
        try {
          await router.push('/')
        } catch (error) {
          console.error('微信登录跳转失败：', error)
          showtoast('跳转失败，请手动进入首页', 'error')
        }
      }, 1000)
    } catch (error) {
      console.error('微信登录失败:', error)
      showtoast('微信登录失败，请稍后重试', 'error')
    }
  } else {
    showtoast('您已取消微信授权', 'info')
  }
}

const showUserAgreement = () => {
  showUserAgreementModal.value = true
}

const showDisclaimer = () => {
  showDisclaimerModal.value = true
}

// 辅助函数：通过学号猜测高校
const guessCollege = (studentId) => {
  const prefix = studentId.substr(0, 2)
  const collegeMap = {
    "20": "南昌大学",
    "21": "江西师范大学",
    "22": "南昌航空大学"
  }
  return collegeMap[prefix] || "未知高校"
}

onMounted(() => {
  // 检查是否已经登录
  const userInfo = getStorage('userInfo')
  if (userInfo && userInfo.hasLoginBefore) {
    router.push('/')
  }
})
</script>


<style scoped>
.login-page {
  min-height: 100vh;
  font-family: '思源黑体', sans-serif;
  margin: 0;
  padding: 0;
  position: relative;
}

/* 全屏背景图片样式 */
.fullscreen-bg {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: -1;
}

.fullscreen-bg img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: center;
}

/* 登录容器定位到指定位置 */
.login-container {
  position: absolute;
  top: 50%;
  left: 80%;
  transform: translate(-50%, -50%);
  width: 100%;
  max-width: 400px;
  padding: 20px;
  box-sizing: border-box;
}

/* 登录表单卡片样式 */
.login-card {
  width: 100%;
  max-width: 400px;
  background-color: rgba(255, 255, 255, 0.95);
  padding: 30px;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.logo {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 30px;
  width: 100%;
}

.logo img {
  width: 120px;
  height: 120px;
  margin-bottom: 16px;
}

.logo-text {
  font-family: '思源黑体', sans-serif;
  font-size: 22px;
  font-weight: 700;
  color: #333333;
  text-align: center;
}

.tab-bar {
  display: flex;
  justify-content: center;
  margin-bottom: 30px;
  width: 100%;
}

.tab-btn {
  width: 140px;
  height: 44px;
  line-height: 44px;
  background-color: #F5F5F5;
  color: #757575;
  font-size: 16px;
  font-family: '思源黑体', sans-serif;
  border: none;
  border-radius: 22px 0 0 22px;
  margin: 0;
  padding: 0;
  cursor: pointer;
}


:deep(.modal-container ){
  z-index: 9999 !important;
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: rgba(0, 0, 0, 0.5);
}

.tab-btn:last-child {
  border-radius: 0 22px 22px 0;
}

.tab-btn.active {
  background-color: #1E88E5;
  color: #FFFFFF;
}

.form-step {
  width: 100%;
  transition: all 0.3s ease;
}

.form-step.hidden {
  display: none;
}

.step-title {
  margin-bottom: 24px;
}

.step-title span:first-child {
  font-size: 16px;
  font-weight: 700;
  color: #333333;
  margin-right: 8px;
}

.step-title span:last-child {
  font-size: 14px;
  color: #757575;
}

.form-item {
  width: 100%;
  height: 52px;
  margin-bottom: 20px;
  position: relative;
}

.form-item input, .form-item select {
  width: 100%;
  height: 100%;
  padding: 0 16px;
  border: 1px solid #E0E0E0;
  border-radius: 8px;
  font-size: 16px;
  color: #333333;
  font-family: '思源黑体', sans-serif;
  box-sizing: border-box;
}

::placeholder {
  color: #BDBDBD;
}

.code-item {
  display: flex;
  align-items: center;
}

.get-code-btn {
  position: absolute;
  right: 8px;
  top: 50%;
  transform: translateY(-50%);
  width: 110px;
  height: 36px;
  line-height: 36px;
  background-color: #F5F5F5;
  color: #1E88E5;
  font-size: 14px;
  border: none;
  border-radius: 4px;
  margin: 0;
  padding: 0;
  cursor: pointer;
}

.get-code-btn:disabled {
  color: #BDBDBD;
  cursor: not-allowed;
}

.next-btn, .prev-btn, .submit-btn {
  width: 100%;
  height: 52px;
  line-height: 52px;
  font-size: 16px;
  font-family: '思源黑体', sans-serif;
  border-radius: 8px;
  margin-bottom: 12px;
  padding: 0;
  border: none;
  cursor: pointer;
}

.next-btn, .submit-btn {
  background-color: #1E88E5;
  color: #FFFFFF;
}

.prev-btn {
  background-color: #F5F5F5;
  color: #333333;
}

.submit-btn:disabled {
  background-color: #BDBDBD;
  cursor: not-allowed;
}

.wechat-login-btn {
  width: 100%;
  height: 52px;
  line-height: 52px;
  background-color: #FFFFFF;
  color: #1E88E5;
  border: 1px solid #1E88E5;
  border-radius: 8px;
  margin: 20px 0 0;
  padding: 0;
  font-size: 16px;
  font-family: '思源黑体', sans-serif;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

.wechat-login-btn img {
  width: 24px;
  height: 24px;
  margin-right: 8px;
}

.agreement {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  font-size: 12px;
  color: #757575;
  margin: 12px 0 20px;
  line-height: 1.5;
}

.agreement input[type="checkbox"] {
  margin-right: 8px;
  transform: scale(0.8);
}

.highlight {
  color: #1E88E5;
  text-decoration: underline;
  margin: 0 2px;
  cursor: pointer;
}

.modal-content {
  max-height: 400px;
  overflow-y: auto;
  font-size: 14px;
  color: #333333;
  line-height: 1.8;
  margin-bottom: 24px;
}

.disclaimer-bold {
  font-weight: 700;
  display: block;
  margin: 12px 0 4px;
}

.modal-confirm {
  width: 100%;
  height: 48px;
  background-color: #1E88E5;
  color: #FFFFFF;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  cursor: pointer;
}

/* 响应式调整 */
@media (max-width: 480px) {
  .login-card {
    padding: 20px;
  }
  
  .logo img {
    width: 100px;
    height: 100px;
  }
  
  .login-container {
    left: 50%;
    transform: translate(-50%, -50%);
  }
}
</style>