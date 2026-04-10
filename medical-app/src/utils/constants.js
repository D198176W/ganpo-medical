export const constants = {
  // 应用信息
  APP: {
    NAME: '健康管理系统',
    VERSION: '1.0.0',
    DESCRIPTION: '园区健康管理与应急服务'
  },

  // 存储键名
  STORAGE_KEYS: {
    HEALTH_LOGS: 'health_logs',
    USER_PROFILE: 'user_profile',
    APP_SETTINGS: 'app_settings',
    EMERGENCY_CONTACTS: 'emergency_contacts'
  },

  // 健康相关常量
  HEALTH: {
    // 睡眠相关
    SLEEP: {
      IDEAL_HOURS: 7,
      MIN_HOURS: 6,
      MAX_HOURS: 9,
      TARGET_BEDTIME: '22:00'
    },
    
    // 运动相关
    EXERCISE: {
      WEEKLY_TARGET_MINUTES: 150,
      DAILY_TARGET_MINUTES: 30,
      TYPES: ['散步', '跑步', '健身', '瑜伽', '游泳', '骑行']
    },
    
    // 用药相关
    MEDICATION: {
      STATUS: {
        TAKEN: 'taken',
        MISSED: 'missed',
        NOT_NEEDED: 'not_needed'
      },
      REMINDER_INTERVALS: [1, 2, 3, 4, 6, 8, 12, 24]
    }
  },

  // 时间常量
  TIME: {
    // 时间段
    PERIODS: {
      WEEK: 'week',
      MONTH: 'month',
      YEAR: 'year'
    },
    
    // 星期
    WEEK_DAYS: ['日', '一', '二', '三', '四', '五', '六'],
    WEEK_DAYS_FULL: ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'],
    
    // 月份
    MONTHS: [
      '一月', '二月', '三月', '四月', '五月', '六月',
      '七月', '八月', '九月', '十月', '十一月', '十二月'
    ],
    
    // 时间格式
    FORMAT: {
      DATE: 'YYYY-MM-DD',
      TIME: 'HH:mm',
      DATETIME: 'YYYY-MM-DD HH:mm',
      DISPLAY_DATE: 'YYYY年MM月DD日'
    }
  },

  // 颜色常量
  COLORS: {
    // 主色调
    PRIMARY: '#1E88E5',
    PRIMARY_DARK: '#1565C0',
    PRIMARY_LIGHT: '#64B5F6',
    
    // 功能色
    SUCCESS: '#4CAF50',
    WARNING: '#FF9800',
    ERROR: '#E53935',
    INFO: '#2196F3',
    
    // 中性色
    WHITE: '#FFFFFF',
    BLACK: '#000000',
    GRAY: {
      50: '#FAFAFA',
      100: '#F5F5F5',
      200: '#EEEEEE',
      300: '#E0E0E0',
      400: '#BDBDBD',
      500: '#9E9E9E',
      600: '#757575',
      700: '#616161',
      800: '#424242',
      900: '#212121'
    },
    
    // 图表颜色
    CHART: {
      SLEEP: '#4CAF50',
      EXERCISE: '#1E88E5',
      MEDICATION: '#FF9800',
      NUTRITION: '#9C27B0'
    }
  },

  // 应急服务相关
  EMERGENCY: {
    // 医院类型
    HOSPITAL_TYPES: {
      GENERAL: 'general',      // 综合医院
      SPECIALIZED: 'specialized', // 专科医院
      COMMUNITY: 'community'   // 社区医院
    },
    
    // 紧急程度
    URGENCY_LEVELS: {
      LOW: 'low',      // 低紧急
      MEDIUM: 'medium', // 中紧急
      HIGH: 'high'     // 高紧急
    },
    
    // 默认医院数据
    DEFAULT_HOSPITALS: [
      {
        id: 1,
        name: '江西省人民医院红谷滩院区',
        type: 'general',
        distance: '2.3公里',
        emergencyDept: '内科、外科、骨科',
        openTime: '24小时',
        phone: '0791-86895550',
        address: '南昌市红谷滩新区丰和北大道266号',
        coordinates: { lat: 28.6896, lng: 115.8575 }
      },
      {
        id: 2,
        name: '南昌大学第二附属医院红角洲院区',
        type: 'general',
        distance: '3.7公里',
        emergencyDept: '全科、急诊科',
        openTime: '24小时',
        phone: '0791-86120120',
        address: '南昌市红谷滩新区学府大道566号',
        coordinates: { lat: 28.6742, lng: 115.8458 }
      },
      {
        id: 3,
        name: '南昌市洪都中医院红谷滩院区',
        type: 'specialized',
        distance: '4.5公里',
        emergencyDept: '骨伤科、急诊科',
        openTime: '24小时',
        phone: '0791-83855120',
        address: '南昌市红谷滩新区绿茵路129号',
        coordinates: { lat: 28.6983, lng: 115.8389 }
      }
    ]
  },

  // 功能配置
  FEATURES: {
    // 健康日志
    HEALTH_LOG: {
      ENABLED: true,
      MAX_NOTES_LENGTH: 500,
      AUTO_SAVE_DELAY: 3000 // 3秒
    },
    
    // 报告分析
    REPORT: {
      ENABLED: true,
      MAX_DATA_POINTS: 365, // 最多显示一年数据
      CHART_ANIMATION_DURATION: 1000 // 图表动画时长
    },
    
    // 提醒功能
    REMINDER: {
      ENABLED: true,
      DEFAULT_INTERVAL: 60, // 默认提醒间隔（分钟）
      MAX_REMINDERS: 10
    }
  },

  // 响应式断点
  BREAKPOINTS: {
    XS: 320,
    SM: 576,
    MD: 768,
    LG: 992,
    XL: 1200,
    XXL: 1400
  },

  // 图标尺寸
  ICON_SIZES: {
    XS: '16px',
    SM: '20px',
    MD: '24px',
    LG: '32px',
    XL: '48px'
  },

  // API 端点
  API_ENDPOINTS: {
    BASE_URL: import.meta.env.VUE_APP_API_BASE_URL || '/api',
    HEALTH: {
      LOGS: '/health/logs',
      REPORTS: '/health/reports',
      REMINDERS: '/health/reminders'
    },
    USER: {
      PROFILE: '/user/profile',
      SETTINGS: '/user/settings'
    },
    EMERGENCY: {
      HOSPITALS: '/emergency/hospitals',
      CONTACTS: '/emergency/contacts'
    }
  },

  // 错误代码
  ERROR_CODES: {
    // 网络错误
    NETWORK_ERROR: 'NETWORK_ERROR',
    TIMEOUT_ERROR: 'TIMEOUT_ERROR',
    
    // 业务错误
    INVALID_DATA: 'INVALID_DATA',
    UNAUTHORIZED: 'UNAUTHORIZED',
    FORBIDDEN: 'FORBIDDEN',
    NOT_FOUND: 'NOT_FOUND',
    
    // 健康数据错误
    HEALTH_LOG_INVALID: 'HEALTH_LOG_INVALID',
    HEALTH_DATA_MISSING: 'HEALTH_DATA_MISSING'
  },

  
  // 验证规则
  VALIDATION: {
    // 邮箱验证
    EMAIL: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
    
    // 手机号验证（中国）
    PHONE: /^1[3-9]\d{9}$/,
    
    // 密码强度
    PASSWORD: {
      MIN_LENGTH: 6,
      MAX_LENGTH: 20,
      STRENGTH_REGEX: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{6,20}$/
    },
    
    // 健康数据验证
    HEALTH_DATA: {
      SLEEP_HOURS: {
        MIN: 0,
        MAX: 24
      },
      EXERCISE_MINUTES: {
        MIN: 0,
        MAX: 480 // 8小时
      }
    }
  }
}