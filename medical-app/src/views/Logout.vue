<template>
  <div class="logout-page">
    <div class="container">
      <!-- 页面头部 -->
      <div class="header">
        <BackButton @click="goBack" />
        <h1 class="page-title">退出登录</h1>
      </div>
      
      <!-- 退出确认卡片 -->
      <div class="logout-card">
        <div class="logout-icon" :class="{ success: logoutSuccess }">
          <i class="fas" :class="logoutIcon"></i>
        </div>
        <h2 class="logout-title">{{ logoutTitle }}</h2>
        <p class="logout-description">{{ logoutDescription }}</p>
        
        <div class="user-info" v-if="userInfo">
          <div class="user-avatar">{{ userAvatar }}</div>
          <div class="user-details">
            <h3>{{ userInfo.name || userInfo.nickName || '用户' }}</h3>
            <p>{{ userInfo.college || '未知学校' }}</p>
          </div>
        </div>
        
        <div class="logout-actions" v-if="!logoutSuccess">
          <button class="action-btn btn-outline" @click="cancelLogout">
            <i class="fas fa-times"></i> 取消
          </button>
          <button class="action-btn btn-danger" @click="confirmLogout" :disabled="isLoggingOut">
            <i class="fas fa-sign-out-alt"></i> {{ isLoggingOut ? '退出中...' : '确认退出' }}
          </button>
        </div>
      </div>
      
      <!-- 安全提示 -->
      <div class="security-tips">
        <h3 class="tips-title">
          <i class="fas fa-shield-alt"></i> 安全提示
        </h3>
        <ul class="tips-list">
          <li class="tip-item">
            <i class="fas fa-check-circle tip-icon"></i>
            <span>退出登录会清除您在本设备上的登录信息</span>
          </li>
          <li class="tip-item">
            <i class="fas fa-check-circle tip-icon"></i>
            <span>您的个人数据将保留在服务器，下次登录可恢复</span>
          </li>
          <li class="tip-item">
            <i class="fas fa-check-circle tip-icon"></i>
            <span>建议在公共设备上使用后及时退出登录</span>
          </li>
        </ul>
      </div>
    </div>
    
    <!-- 底部导航 -->
    <NavBar active-tab="profile" />
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useToast } from '@/composables/useToast.js'
import { getStorage, removeStorage, clearStorage } from '@/utils/storage.js'
import BackButton from '@/components/common/BackButton.vue'
import NavBar from '@/components/common/NavBar.vue'

const router = useRouter()
const { showToast } = useToast()

// 响应式数据
const isLoggingOut = ref(false)
const logoutSuccess = ref(false)
const userInfo = ref(null)

// 计算属性
const logoutIcon = computed(() => {
  if (isLoggingOut.value) return 'fa-spinner fa-spin'
  if (logoutSuccess.value) return 'fa-check-circle'
  return 'fa-sign-out-alt'
})

const logoutTitle = computed(() => {
  if (isLoggingOut.value) return '正在退出...'
  if (logoutSuccess.value) return '退出成功'
  return '确认退出登录？'
})

const logoutDescription = computed(() => {
  if (isLoggingOut.value) return '请稍候，正在安全退出您的账号'
  if (logoutSuccess.value) return '您已安全退出账号，即将跳转到登录页面'
  return '退出后需要重新登录才能使用应用功能'
})

const userAvatar = computed(() => {
  if (!userInfo.value) return '用'
  const name = userInfo.value.name || userInfo.value.nickName || '用户'
  return name.charAt(0)
})

// 方法
const goBack = () => {
  router.back()
}

const cancelLogout = () => {
  router.back()
}

const confirmLogout = async () => {
  isLoggingOut.value = true
  
  try {
    // 模拟退出过程
    await new Promise(resolve => setTimeout(resolve, 1500))
    
    // 清除用户数据
    removeStorage('userInfo')
    removeStorage('userToken')
    removeStorage('loginState')
    
    // 可以清除所有存储（可选）
    // clearStorage()
    
    logoutSuccess.value = true
    showToast('退出登录成功', 'success')
    
    // 跳转到登录页面
    setTimeout(() => {
      router.push('/login')
    }, 1500)
  } catch (error) {
    console.error('退出登录失败:', error)
    showToast('退出登录失败，请重试', 'error')
    isLoggingOut.value = false
  }
}

// 初始化用户信息
const initUserInfo = () => {
  const storedUserInfo = getStorage('userInfo')
  if (storedUserInfo) {
    userInfo.value = storedUserInfo
  } else {
    // 如果没有用户信息，直接跳转到登录页
    router.push('/login')
  }
}

onMounted(() => {
  initUserInfo()
})
</script>

<style scoped>
.logout-page {
  background-color: var(--light);
  color: var(--dark);
  line-height: 1.6;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.container {
  max-width: 500px;
  margin: 0 auto;
  padding: 20px;
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

/* 头部样式 */
.header {
  display: flex;
  align-items: center;
  margin-bottom: 40px;
}

.page-title {
  font-size: 22px;
  font-weight: 600;
  color: var(--dark);
}

/* 退出确认卡片 */
.logout-card {
  background: var(--card);
  border-radius: 16px;
  padding: 30px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  text-align: center;
  margin-bottom: 30px;
  animation: fadeIn 0.5s ease-out;
}

.logout-icon {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--danger), #F44336);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 32px;
  margin: 0 auto 20px;
  transition: all 0.3s ease;
}

.logout-icon.success {
  background: linear-gradient(135deg, var(--success), #4CAF50);
}

.logout-title {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 10px;
  color: var(--dark);
  transition: all 0.3s ease;
}

.logout-description {
  font-size: 15px;
  color: var(--gray);
  margin-bottom: 25px;
  transition: all 0.3s ease;
}

.user-info {
  background: var(--light);
  border-radius: 10px;
  padding: 15px;
  margin-bottom: 25px;
  display: flex;
  align-items: center;
  text-align: left;
}

.user-avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--primary), var(--secondary));
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: bold;
  font-size: 18px;
  margin-right: 15px;
}

.user-details h3 {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 5px;
}

.user-details p {
  font-size: 14px;
  color: var(--gray);
}

.logout-actions {
  display: flex;
  gap: 15px;
}

.action-btn {
  flex: 1;
  padding: 14px 20px;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.action-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.btn-outline {
  background: transparent;
  border: 1px solid var(--primary);
  color: var(--primary);
}

.btn-outline:hover:not(:disabled) {
  background: var(--primary);
  color: white;
}

.btn-danger {
  background: var(--danger);
  color: white;
}

.btn-danger:hover:not(:disabled) {
  background: #c62828;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(229, 57, 53, 0.3);
}

/* 安全提示 */
.security-tips {
  background: var(--card);
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  animation: fadeIn 0.5s ease-out 0.2s both;
}

.tips-title {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 12px;
  color: var(--dark);
  display: flex;
  align-items: center;
  gap: 8px;
}

.tips-list {
  list-style: none;
}

.tip-item {
  display: flex;
  align-items: flex-start;
  margin-bottom: 10px;
  font-size: 14px;
  color: var(--gray);
}

.tip-item:last-child {
  margin-bottom: 0;
}

.tip-icon {
  color: var(--primary);
  margin-right: 10px;
  margin-top: 2px;
}

/* 动画效果 */
@keyframes fadeIn {
  from { 
    opacity: 0; 
    transform: translateY(20px); 
  }
  to { 
    opacity: 1; 
    transform: translateY(0); 
  }
}

/* 响应式设计 */
@media (max-width: 600px) {
  .container {
    padding: 15px;
  }
  
  .logout-actions {
    flex-direction: column;
  }
  
  .logout-card {
    padding: 20px;
  }
}

/* CSS变量定义 */
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
</style>