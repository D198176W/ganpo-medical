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
      localStorage.setItem('userInfo', JSON.stringify(this.userInfo))
    },
    loadUserInfo() {
      const saved = localStorage.getItem('userInfo')
      if (saved) {
        this.userInfo = JSON.parse(saved)
      }
    },
    saveHealthLog(date, log) {
      this.healthLogs[date] = log
      localStorage.setItem('healthLogs', JSON.stringify(this.healthLogs))
    },
    loadHealthLogs() {
      const saved = localStorage.getItem('healthLogs')
      if (saved) {
        this.healthLogs = JSON.parse(saved)
      }
    }
  }
})