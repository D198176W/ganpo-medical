import { createRouter, createWebHistory } from 'vue-router'
import CommonIllnesses from '@/views/CommonIllnesses.vue'
import EmergencyGuidelines from '@/views/EmergencyGuidelines.vue'
import HealthRecord from '@/views/HealthRecord.vue'
import HealthReminder from '../components/health/HealthReminder.vue'
import HelpCenter from '@/views/HelpCenter.vue'
import Login from '@/views/Login.vue'
import Logout from '@/views/Logout.vue'
import MyAppointments from '@/views/MyAppointments.vue'
import MedicalReports from '@/views/MedicalReports.vue'
import Settings from '@/views/Settings.vue'
import Home from '@/views/Home.vue'
import MedicalResources from '@/views/MedicalResources.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/Home.vue')
  },
  {
    path: '/consult',
    name: 'MedicalConsult',
    component: () => import('@/views/MedicalConsult.vue')
  },
  {
    path: '/health',
    name: 'HealthManagement',
    component: () => import('@/views/HealthManagement.vue')
  },
  {
    path: '/medical-resources',
    name: 'MedicalResources',
    component: () => import('@/views/MedicalResources.vue')
  },
  {
    path: '/pathology',
    name: 'PathologyService',
    component: () => import('@/views/PathologyService.vue')
  },
  {
    path: '/personal',
    name: 'PersonalCenter',
    component: () => import('@/views/PersonalCenter.vue')
  },

  {
    path:'/common-illnesses',
    name:'CommonIllnesses',
    component:CommonIllnesses
  },

  {
    path:'/emergency-guidelines',
    name:'EmergencyGuidelines',
    component:EmergencyGuidelines
  },
  {
    path:'/health-record',
    name:'HealthRecord',
    component:HealthRecord
  },
  {
    path: '/health-reminder',
    name: 'HealthReminder',
    component: HealthReminder
  },
  {
    path: '/help-center',
    name: 'HelpCenter',
    component: HelpCenter,
    meta: {
      title: '帮助中心'
    }
  },

  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: {
      title: '登录 - 赣鄱医枢',
      requiresGuest: true // 需要游客状态才能访问
    }
  },
  
  {
    path: '/logout',
    name: 'Logout',
    component: Logout,
    meta: {
      title: '退出登录 - 赣鄱医枢',
      requiresAuth: true // 需要登录才能访问
    }
  },
  {
    path: '/my-appointments',
    name: 'MyAppointments',
    component: MyAppointments
  },
  {
    path: '/medical-reports',
    name: 'MedicalReports',
    component: MedicalReports
  },
  {
    path: '/settings',
    name: 'Settings',
    component: Settings
  }



]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫 - 检查登录状态
router.beforeEach((to, from, next) => {
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
  
  if (to.meta.requiresGuest && userInfo.hasLoginBefore) {
    // 如果已经登录，尝试访问登录页面，重定向到首页
    next('/')
  } else if (to.meta.requiresAuth && !userInfo.hasLoginBefore) {
    // 如果需要登录但未登录，重定向到登录页面
    next('/login')
  } else {
    next()
  }
})

export default router