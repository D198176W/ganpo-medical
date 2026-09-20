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
import { getStorage } from '@/utils/storage.js'

const routes = [
  {
    path: '/',
    redirect: '/login'  // 根路径默认重定向到登录页
  },
  {
    path: '/home',  // 首页使用 /home 路径
    name: 'Home',
    component: () => import('@/views/Home.vue'),
    meta: {
      requiresAuth: true // 首页需要登录才能访问
    }
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
    path: '/health/data',
    name: 'HealthData',
    component: () => import('@/views/HealthData.vue')
  },
  {
    path: '/health/foot',
    name: 'HealthFoot',
    component: () => import('@/views/HealthFoot.vue')
  },
  {
    path: '/health/sleep',
    name: 'HealthSleep',
    component: () => import('@/views/HealthSleep.vue')
  },
  {
    path: '/health/vitals',
    name: 'HealthVitals',
    component: () => import('@/views/HealthVitals.vue')
  },
  {
    path: '/health/medication',
    name: 'HealthMedication',
    component: () => import('@/views/HealthMedication.vue')
  },
  {
    path: '/health/diet',
    name: 'HealthDiet',
    component: () => import('@/views/HealthDiet.vue')
  },
  {
    path: '/health/exercise',
    name: 'HealthExercise',
    component: () => import('@/views/HealthExercise.vue')
  },
  {
    path: '/health/records',
    name: 'HealthRecords',
    component: () => import('@/views/HealthRecords.vue')
  },
  {
    path: '/health/plan',
    name: 'HealthPlan',
    component: () => import('@/views/HealthPlan.vue')
  },
  {
    path: '/health/plan/weight-loss',
    name: 'HealthPlanWeightLoss',
    component: () => import('@/views/HealthPlanWeightLoss.vue')
  },
  {
    path: '/health/plan/running',
    name: 'HealthPlanRunning',
    component: () => import('@/views/HealthPlanRunning.vue')
  },
  {
    path: '/health/plan/sleep',
    name: 'HealthPlanSleep',
    component: () => import('@/views/HealthPlanSleep.vue')
  },
  {
    path: '/health/reminders',
    name: 'HealthReminders',
    component: () => import('@/views/HealthReminders.vue')
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
    path: '/personal/health',
    name: 'PersonalHealth',
    component: () => import('@/views/PersonalHealth.vue')
  },
  {
    path: '/personal/records',
    name: 'PersonalRecords',
    component: () => import('@/views/PersonalRecords.vue')
  },
  {
    path: '/personal/plans',
    name: 'PersonalPlans',
    component: () => import('@/views/PersonalPlans.vue')
  },
  {
    path: '/personal/favorites',
    name: 'PersonalFavorites',
    component: () => import('@/views/PersonalFavorites.vue')
  },
  {
    path: '/personal/reports',
    name: 'PersonalReports',
    component: () => import('@/views/PersonalReports.vue')
  },
  {
    path: '/personal/medication',
    name: 'PersonalMedication',
    component: () => import('@/views/PersonalMedication.vue')
  },
  {
    path: '/personal/assessment',
    name: 'PersonalAssessment',
    component: () => import('@/views/PersonalAssessment.vue')
  },
  {
    path: '/personal/health-records',
    name: 'PersonalHealthRecords',
    component: () => import('@/views/PersonalHealthRecords.vue')
  },
  {
    path: '/personal/checkup',
    name: 'PersonalCheckup',
    component: () => import('@/views/PersonalCheckup.vue')
  },
  {
    path: '/personal/medical-history',
    name: 'PersonalMedicalHistory',
    component: () => import('@/views/PersonalMedicalHistory.vue')
  },
  {
    path: '/personal/medication-records',
    name: 'PersonalMedicationRecords',
    component: () => import('@/views/PersonalMedicationRecords.vue')
  },
  {
    path: '/personal/allergy',
    name: 'PersonalAllergy',
    component: () => import('@/views/PersonalAllergy.vue')
  },
  {
    path: '/personal/edit-profile',
    name: 'PersonalEditProfile',
    component: () => import('@/views/PersonalEditProfile.vue')
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
  },
  {
    path: '/settings/account',
    name: 'SettingsAccount',
    component: () => import('@/views/SettingsAccount.vue')
  },
  {
    path: '/settings/notification',
    name: 'SettingsNotification',
    component: () => import('@/views/SettingsNotification.vue')
  },
  {
    path: '/settings/privacy',
    name: 'SettingsPrivacy',
    component: () => import('@/views/SettingsPrivacy.vue')
  },
  {
    path: '/settings/about',
    name: 'SettingsAbout',
    component: () => import('@/views/SettingsAbout.vue')
  },
  {
    path: '/settings/help',
    name: 'SettingsHelp',
    component: () => import('@/views/SettingsHelp.vue')
  }



]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫 - 统一处理登录验证
router.beforeEach(async (to, from, next) => {
  // 清除式检查登录状态
  const userInfo = getStorage('userInfo', null)
  const hasLogin = Boolean(userInfo && userInfo.hasLoginBefore)
  
  console.log('[路由守卫] 访问路径:', to.path)
  console.log('[路由守卫] 登录状态:', hasLogin)
  console.log('[路由守卫] userInfo:', userInfo)
  
  // 登录页访问策略：已登录用户不能访问登录页
  if (to.path === '/login') {
    if (hasLogin) {
      console.log('[路由守卫] 已登录，从登录页重定向到首页')
      next('/home')
      return
    }
  }
  
  // 需要登录的页面检查
  if (to.meta.requiresAuth && !hasLogin) {
    console.log('[路由守卫] 未登录访问受限页面，重定向到登录页')
    next('/login')
    return
  }
  
  console.log('[路由守卫] 允许访问')
  next()
})

export default router