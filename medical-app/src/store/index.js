import { defineStore } from 'pinia'

export const useAppStore = defineStore('app', {
  state: () => ({
    userInfo: {
      nickName: '',
      userTag: '',
      avatar: ''
    },
    healthLogs: {},
    chatHistory: []
  }),
  actions: {
    setUserInfo(info) {
      this.userInfo = { ...this.userInfo, ...info }
      try {
        localStorage.setItem('userInfo', JSON.stringify(this.userInfo))
      } catch (error) {
        console.warn('保存用户信息失败:', error)
      }
    },
    loadUserInfo() {
      try {
        const saved = localStorage.getItem('userInfo')
        if (saved) {
          this.userInfo = JSON.parse(saved)
        }
      } catch (error) {
        console.warn('加载用户信息失败:', error)
      }
    },
    saveHealthLog(date, log) {
      this.healthLogs[date] = log
      try {
        localStorage.setItem('healthLogs', JSON.stringify(this.healthLogs))
      } catch (error) {
        console.warn('保存健康日志失败:', error)
      }
    },
    loadHealthLogs() {
      try {
        const saved = localStorage.getItem('healthLogs')
        if (saved) {
          this.healthLogs = JSON.parse(saved)
        }
      } catch (error) {
        console.warn('加载健康日志失败:', error)
      }
    }
  }
})