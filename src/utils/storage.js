 import { constants } from './constants.js'

/**
 * 本地存储工具类
 * 提供对 localStorage 的封装，支持数据类型自动转换和错误处理
 */
const storage = {
  /**
   * 获取存储项
   * @param {string} key - 存储键名
   * @param {*} defaultValue - 默认值
   * @returns {*} 存储的值或默认值
   */
  get(key, defaultValue = null) {
    try {
      const item = localStorage.getItem(key)
      if (item === null) {
        return defaultValue
      }

      // 尝试解析 JSON
      try {
        return JSON.parse(item)
      } catch {
        // 如果不是 JSON 格式，返回原始字符串
        return item
      }
    } catch (error) {
      console.error(`获取存储项失败 [${key}]:`, error)
      return defaultValue
    }
  },

  /**
   * 设置存储项
   * @param {string} key - 存储键名
   * @param {*} value - 存储的值
   * @returns {boolean} 是否设置成功
   */
  set(key, value) {
    try {
      // 处理 undefined 值
      if (value === undefined) {
        this.remove(key)
        return true
      }

      const serializedValue = JSON.stringify(value)
      localStorage.setItem(key, serializedValue)
      return true
    } catch (error) {
      console.error(`设置存储项失败 [${key}]:`, error)
      return false
    }
  },

  /**
   * 删除存储项
   * @param {string} key - 存储键名
   * @returns {boolean} 是否删除成功
   */
  remove(key) {
    try {
      localStorage.removeItem(key)
      return true
    } catch (error) {
      console.error(`删除存储项失败 [${key}]:`, error)
      return false
    }
  },

  /**
   * 清空所有存储项
   * @returns {boolean} 是否清空成功
   */
  clear() {
    try {
      localStorage.clear()
      return true
    } catch (error) {
      console.error('清空存储失败:', error)
      return false
    }
  },

  /**
   * 检查存储项是否存在
   * @param {string} key - 存储键名
   * @returns {boolean} 是否存在
   */
  has(key) {
    try {
      return localStorage.getItem(key) !== null
    } catch (error) {
      console.error(`检查存储项失败 [${key}]:`, error)
      return false
    }
  },

  /**
   * 获取所有存储键名
   * @returns {string[]} 键名数组
   */
  keys() {
    try {
      return Object.keys(localStorage)
    } catch (error) {
      console.error('获取存储键名失败:', error)
      return []
    }
  },

  /**
   * 获取存储大小（字节）
   * @param {string} key - 存储键名，不传则计算总大小
   * @returns {number} 存储大小
   */
  size(key = null) {
    try {
      if (key) {
        const value = localStorage.getItem(key)
        return value ? new Blob([value]).size : 0
      } else {
        return Object.keys(localStorage).reduce((total, k) => {
          const value = localStorage.getItem(k)
          return total + (value ? new Blob([value]).size : 0)
        }, 0)
      }
    } catch (error) {
      console.error('计算存储大小失败:', error)
      return 0
    }
  },

  /**
   * 获取存储使用情况
   * @returns {Object} 存储使用情况
   */
  getUsage() {
    const totalSize = this.size()
    const itemCount = this.keys().length
    
    return {
      totalSize,
      itemCount,
      readableSize: this.formatBytes(totalSize)
    }
  },

  /**
   * 格式化字节大小
   * @param {number} bytes - 字节数
   * @param {number} decimals - 小数位数
   * @returns {string} 格式化后的字符串
   */
  formatBytes(bytes, decimals = 2) {
    if (bytes === 0) return '0 Bytes'

    const k = 1024
    const dm = decimals < 0 ? 0 : decimals
    const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB']

    const i = Math.floor(Math.log(bytes) / Math.log(k))

    return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i]
  },

  /**
   * 批量获取存储项
   * @param {string[]} keys - 键名数组
   * @returns {Object} 键值对对象
   */
  getMultiple(keys) {
    return keys.reduce((result, key) => {
      result[key] = this.get(key)
      return result
    }, {})
  },

  /**
   * 批量设置存储项
   * @param {Object} items - 键值对对象
   * @returns {boolean} 是否全部设置成功
   */
  setMultiple(items) {
    let allSuccess = true
    
    Object.keys(items).forEach(key => {
      const success = this.set(key, items[key])
      if (!success) {
        allSuccess = false
      }
    })
    
    return allSuccess
  },

  /**
   * 批量删除存储项
   * @param {string[]} keys - 键名数组
   * @returns {boolean} 是否全部删除成功
   */
  removeMultiple(keys) {
    let allSuccess = true
    
    keys.forEach(key => {
      const success = this.remove(key)
      if (!success) {
        allSuccess = false
      }
    })
    
    return allSuccess
  },

  /**
   * 监听存储变化
   * @param {Function} callback - 回调函数
   * @returns {Function} 取消监听函数
   */
  watch(callback) {
    const handler = (event) => {
      if (event.storageArea === localStorage) {
        callback({
          key: event.key,
          oldValue: event.oldValue ? JSON.parse(event.oldValue) : null,
          newValue: event.newValue ? JSON.parse(event.newValue) : null,
          url: event.url,
          storageArea: event.storageArea
        })
      }
    }

    window.addEventListener('storage', handler)
    
    // 返回取消监听函数
    return () => {
      window.removeEventListener('storage', handler)
    }
  },

  /**
   * 获取健康日志数据
   * @returns {Array} 健康日志数组
   */
  getHealthLogs() {
    return this.get(constants.STORAGE_KEYS.HEALTH_LOGS, [])
  },

  /**
   * 保存健康日志数据
   * @param {Array} logs - 健康日志数组
   * @returns {boolean} 是否保存成功
   */
  setHealthLogs(logs) {
    return this.set(constants.STORAGE_KEYS.HEALTH_LOGS, logs)
  },

  /**
   * 获取用户配置
   * @returns {Object} 用户配置对象
   */
  getUserProfile() {
    return this.get(constants.STORAGE_KEYS.USER_PROFILE, {})
  },

  /**
   * 保存用户配置
   * @param {Object} profile - 用户配置对象
   * @returns {boolean} 是否保存成功
   */
  setUserProfile(profile) {
    return this.set(constants.STORAGE_KEYS.USER_PROFILE, profile)
  },

  /**
   * 获取应用设置
   * @returns {Object} 应用设置对象
   */
  getAppSettings() {
    const defaultSettings = {
      theme: 'light',
      language: constants.I18N.DEFAULT_LANGUAGE,
      notifications: true,
      autoSave: true,
      voiceEnabled: false
    }
    
    return this.get(constants.STORAGE_KEYS.APP_SETTINGS, defaultSettings)
  },

  /**
   * 保存应用设置
   * @param {Object} settings - 应用设置对象
   * @returns {boolean} 是否保存成功
   */
  setAppSettings(settings) {
    return this.set(constants.STORAGE_KEYS.APP_SETTINGS, settings)
  },

  /**
   * 获取紧急联系人
   * @returns {Array} 紧急联系人数组
   */
  getEmergencyContacts() {
    const defaultContacts = [
      {
        id: 1,
        name: '园区急救中心',
        phone: '120',
        relationship: '急救服务'
      },
      {
        id: 2,
        name: '园区安保',
        phone: '110',
        relationship: '安全服务'
      }
    ]
    
    return this.get(constants.STORAGE_KEYS.EMERGENCY_CONTACTS, defaultContacts)
  },

  /**
   * 保存紧急联系人
   * @param {Array} contacts - 紧急联系人数组
   * @returns {boolean} 是否保存成功
   */
  setEmergencyContacts(contacts) {
    return this.set(constants.STORAGE_KEYS.EMERGENCY_CONTACTS, contacts)
  },

  /**
   * 导出所有数据
   * @returns {Object} 所有存储数据
   */
  exportAll() {
    const allData = {}
    
    Object.keys(constants.STORAGE_KEYS).forEach(key => {
      const storageKey = constants.STORAGE_KEYS[key]
      allData[storageKey] = this.get(storageKey)
    })
    
    return {
      ...allData,
      _meta: {
        exportTime: new Date().toISOString(),
        version: constants.APP.VERSION,
        itemCount: this.keys().length,
        totalSize: this.getUsage().readableSize
      }
    }
  },

  /**
   * 导入数据
   * @param {Object} data - 要导入的数据
   * @returns {Object} 导入结果
   */
  importData(data) {
    const results = {
      success: [],
      failed: [],
      total: 0
    }
    
    Object.keys(data).forEach(key => {
      // 跳过元数据
      if (key.startsWith('_')) return
      
      results.total++
      const success = this.set(key, data[key])
      
      if (success) {
        results.success.push(key)
      } else {
        results.failed.push(key)
      }
    })
    
    return results
  },

  /**
   * 检查存储空间是否充足
   * @param {number} requiredSize - 需要的空间大小（字节）
   * @returns {Object} 检查结果
   */
  checkStorageSpace(requiredSize = 0) {
    // 大多数浏览器限制为 5MB
    const MAX_STORAGE = 5 * 1024 * 1024
    const currentUsage = this.size()
    const availableSpace = MAX_STORAGE - currentUsage
    
    return {
      hasEnoughSpace: availableSpace >= requiredSize,
      availableSpace,
      requiredSize,
      currentUsage,
      maxStorage: MAX_STORAGE,
      usagePercentage: ((currentUsage / MAX_STORAGE) * 100).toFixed(2)
    }
  },

  /**
   * 清理过期数据
   * @param {number} maxAge - 最大保存时间（毫秒），默认30天
   * @returns {Object} 清理结果
   */
  cleanup(maxAge = 30 * 24 * 60 * 60 * 1000) {
    const now = Date.now()
    const results = {
      cleaned: [],
      kept: [],
      totalCleanedSize: 0
    }
    
    this.keys().forEach(key => {
      // 跳过系统关键数据
      if (Object.values(constants.STORAGE_KEYS).includes(key)) {
        results.kept.push(key)
        return
      }
      
      const item = localStorage.getItem(key)
      if (!item) return
      
      try {
        const data = JSON.parse(item)
        // 检查是否有时间戳字段
        if (data.timestamp && (now - data.timestamp > maxAge)) {
          const itemSize = new Blob([item]).size
          this.remove(key)
          results.cleaned.push(key)
          results.totalCleanedSize += itemSize
        } else {
          results.kept.push(key)
        }
      } catch {
        // 如果不是 JSON 格式，跳过
        results.kept.push(key)
      }
    })
    
    results.totalCleanedSizeReadable = this.formatBytes(results.totalCleanedSize)
    
    return results
  }
}

export const setStorage = (key, value) => storage.set(key, value);
export const getStorage = (key) => storage.get(key);
export const removeStorage = (key) => storage.remove(key);
export const clearStorage = () => storage.clear();

export default storage