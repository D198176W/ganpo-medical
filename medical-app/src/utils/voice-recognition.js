export const voiceRecognition = {
  // 语音识别状态
  STATUS: {
    IDLE: 'idle',
    LISTENING: 'listening',
    PROCESSING: 'processing',
    ERROR: 'error',
    UNAVAILABLE: 'unavailable'
  },

  // 语音命令
  COMMANDS: {
    // 健康日志相关
    HEALTH: {
      START_RECORDING: '开始记录',
      STOP_RECORDING: '停止记录',
      SAVE_LOG: '保存记录',
      
      // 作息命令
      SLEEP: {
        EARLY: '早睡',
        NORMAL: '正常作息',
        LATE: '熬夜',
        INSOMNIA: '失眠'
      },
      
      // 运动命令
      EXERCISE: {
        WALK: '散步',
        RUN: '跑步',
        GYM: '健身',
        YOGA: '瑜伽',
        NONE: '无运动'
      },
      
      // 用药命令
      MEDICATION: {
        TAKEN: '按时服药',
        MISSED: '忘记服药',
        NOT_NEEDED: '无需服药'
      }
    },
    
    // 导航命令
    NAVIGATION: {
      BACK: '返回',
      HOME: '首页',
      HEALTH_LOG: '健康日志',
      HEALTH_REPORT: '健康报告',
      EMERGENCY: '应急指引'
    },
    
    // 系统命令
    SYSTEM: {
      HELP: '帮助',
      CANCEL: '取消',
      CONFIRM: '确认',
      DELETE: '删除'
    }
  },

  // 语音识别配置
  CONFIG: {
    // 基础配置
    LANGUAGE: 'zh-CN',
    CONTINUOUS: false,
    INTERIM_RESULTS: true,
    MAX_ALTERNATIVES: 1,
    
    // 语音检测配置
    SPEECH_DETECTION: {
      SILENCE_TIMEOUT: 3000, // 3秒静默超时
      MIN_VOLUME: 0.1,       // 最小音量阈值
      MAX_DURATION: 60000    // 最大录音时长（60秒）
    },
    
    // 音频配置
    AUDIO: {
      SAMPLE_RATE: 16000,
      CHANNELS: 1,
      BIT_DEPTH: 16
    },
    
    // 识别配置
    RECOGNITION: {
      CONFIDENCE_THRESHOLD: 0.7, // 置信度阈值
      MAX_RESULTS: 5,            // 最大结果数
      TIMEOUT: 10000             // 识别超时（10秒）
    }
  },

  // 错误信息
  ERROR_MESSAGES: {
    // 权限错误
    PERMISSION_DENIED: '麦克风权限被拒绝，请在浏览器设置中启用麦克风访问权限',
    PERMISSION_DISMISSED: '麦克风权限请求被取消，请重新授权',
    
    // 网络错误
    NETWORK_ERROR: '网络连接失败，请检查网络设置',
    NETWORK_TIMEOUT: '网络请求超时，请重试',
    
    // 音频错误
    AUDIO_CAPTURE_ERROR: '音频捕获失败，请检查麦克风设备',
    AUDIO_QUALITY_ERROR: '音频质量不佳，请确保在安静环境中使用',
    
    // 识别错误
    RECOGNITION_ERROR: '语音识别失败，请重试',
    NO_SPEECH_DETECTED: '未检测到语音输入，请说话',
    SPEECH_TIMEOUT: '语音输入超时，请重新开始',
    
    // 浏览器支持
    BROWSER_NOT_SUPPORTED: '您的浏览器不支持语音识别功能，请使用 Chrome、Edge 或 Safari 浏览器'
  },

  // 语音反馈
  FEEDBACK: {
    // 开始提示
    START_LISTENING: '正在聆听，请说话...',
    START_RECORDING: '开始录音，请说话...',
    
    // 处理提示
    PROCESSING: '正在处理您的语音...',
    ANALYZING: '正在分析语音内容...',
    
    // 成功提示
    RECOGNITION_SUCCESS: '识别成功',
    COMMAND_RECOGNIZED: '指令识别成功',
    
    // 错误提示
    TRY_AGAIN: '请重试',
    SPEAK_LOUDER: '请大声一点',
    SPEAK_CLEARER: '请说清楚一点',
    
    // 帮助提示
    HELP_MESSAGE: '您可以说：开始记录、保存记录、早睡、散步、按时服药等指令',
    COMMAND_LIST: '可用指令包括：健康日志操作、导航命令和系统命令'
  },

  // 语音合成配置
  SPEECH_SYNTHESIS: {
    // 语音参数
    VOICE: {
      RATE: 1.0,    // 语速 (0.1 - 10)
      PITCH: 1.0,   // 音调 (0 - 2)
      VOLUME: 0.8   // 音量 (0 - 1)
    },
    
    // 语音类型
    VOICES: {
      FEMALE: 'Ting-Ting', // 中文女声
      MALE: 'Sin-Ji'       // 中文男声
    },
    
    // 消息类型
    MESSAGES: {
      WELCOME: '欢迎使用健康管理系统语音助手',
      READY: '语音助手已就绪',
      GOODBYE: '再见，祝您健康'
    }
  },

  // 语音分析
  ANALYSIS: {
    // 关键词匹配
    KEYWORDS: {
      // 肯定词
      POSITIVE: ['是', '好的', '确认', '同意', '确定', '没错', '对的'],
      
      // 否定词
      NEGATIVE: ['不', '不是', '取消', '拒绝', '否定', '错了', '不对'],
      
      // 健康相关关键词
      HEALTH: ['健康', '身体', '睡眠', '睡觉', '运动', '锻炼', '用药', '吃药', '记录', '日志'],
      
      // 时间关键词
      TIME: ['今天', '明天', '昨天', '本周', '本月', '早上', '晚上', '下午', '中午']
    },
    
    // 意图识别
    INTENTS: {
      // 健康记录意图
      LOG_HEALTH: {
        TRIGGERS: ['记录', '记一下', '添加', '输入'],
        ENTITIES: ['sleep', 'exercise', 'medication', 'notes']
      },
      
      // 查询意图
      QUERY: {
        TRIGGERS: ['查询', '查看', '显示', '打开'],
        ENTITIES: ['health_log', 'report', 'emergency', 'hospital']
      },
      
      // 导航意图
      NAVIGATE: {
        TRIGGERS: ['去', '打开', '切换到', '进入'],
        ENTITIES: ['home', 'log', 'report', 'emergency']
      }
    },
    
    // 上下文管理
    CONTEXT: {
      MAX_HISTORY: 10,      // 最大上下文历史
      TIMEOUT: 30000,       // 上下文超时（30秒）
      CONFIDENCE: 0.6       // 上下文匹配置信度
    }
  },

  // 性能指标
  PERFORMANCE: {
    // 响应时间阈值（毫秒）
    RESPONSE_TIME: {
      EXCELLENT: 1000,
      GOOD: 2000,
      ACCEPTABLE: 3000,
      POOR: 5000
    },
    
    // 准确率指标
    ACCURACY: {
      EXCELLENT: 0.95,
      GOOD: 0.85,
      ACCEPTABLE: 0.75,
      POOR: 0.6
    },
    
    // 资源使用
    RESOURCE_USAGE: {
      MAX_CPU: 0.3,    // 最大CPU使用率
      MAX_MEMORY: 50,  // 最大内存使用（MB）
      MAX_STORAGE: 100 // 最大存储使用（MB）
    }
  },

  // 兼容性信息
  COMPATIBILITY: {
    // 支持的浏览器
    SUPPORTED_BROWSERS: {
      CHROME: {
        MIN_VERSION: 25,
        FEATURES: ['speech_recognition', 'speech_synthesis']
      },
      EDGE: {
        MIN_VERSION: 79,
        FEATURES: ['speech_recognition', 'speech_synthesis']
      },
      SAFARI: {
        MIN_VERSION: 14.1,
        FEATURES: ['speech_recognition', 'speech_synthesis']
      },
      FIREFOX: {
        MIN_VERSION: 70,
        FEATURES: ['speech_synthesis'] // Firefox 不支持语音识别
      }
    },
    
    // 必需的API
    REQUIRED_APIS: [
      'SpeechRecognition',
      'webkitSpeechRecognition',
      'speechSynthesis'
    ],
    
    // 功能检测
    FEATURE_DETECTION: {
      SPEECH_RECOGNITION: 'SpeechRecognition' in window || 'webkitSpeechRecognition' in window,
      SPEECH_SYNTHESIS: 'speechSynthesis' in window,
      MEDIA_DEVICES: 'mediaDevices' in navigator,
      GET_USER_MEDIA: 'getUserMedia' in navigator.mediaDevices
    }
  },

  // 数据格式
  DATA_FORMATS: {
    // 语音识别结果
    RECOGNITION_RESULT: {
      TRANSCRIPT: 'transcript',
      CONFIDENCE: 'confidence',
      IS_FINAL: 'isFinal',
      ALTERNATIVES: 'alternatives'
    },
    
    // 语音命令
    VOICE_COMMAND: {
      TEXT: 'text',
      INTENT: 'intent',
      ENTITIES: 'entities',
      CONFIDENCE: 'confidence',
      TIMESTAMP: 'timestamp'
    },
    
    // 音频数据
    AUDIO_DATA: {
      BLOB: 'blob',
      DURATION: 'duration',
      SAMPLE_RATE: 'sampleRate',
      SIZE: 'size'
    }
  },

  // 本地存储键名
  STORAGE_KEYS: {
    VOICE_SETTINGS: 'voice_settings',
    COMMAND_HISTORY: 'voice_command_history',
    USER_PREFERENCES: 'voice_user_preferences',
    TRAINING_DATA: 'voice_training_data'
  },

  // 调试配置
  DEBUG: {
    // 日志级别
    LOG_LEVEL: {
      ERROR: 0,
      WARN: 1,
      INFO: 2,
      DEBUG: 3,
      VERBOSE: 4
    },
    
    // 调试功能
    FEATURES: {
      LOG_RECOGNITION: true,    // 记录识别过程
      LOG_PERFORMANCE: true,    // 记录性能数据
      SIMULATE_RECOGNITION: false, // 模拟识别结果
      TEST_MODE: false          // 测试模式
    },
    
    // 性能监控
    PERFORMANCE_MONITORING: {
      ENABLED: true,
      SAMPLE_RATE: 0.1, // 10%的请求采样
      METRICS: ['response_time', 'accuracy', 'resource_usage']
    }
  }
}