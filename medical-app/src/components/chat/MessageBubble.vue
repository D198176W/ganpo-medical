<template>
  <div 
    class="message-bubble" 
    :class="[
      `message-${message.sender}`,
      `message-${message.type}`,
      { 
        'has-avatar': showAvatar,
        'has-status': showStatus,
        'has-timestamp': showTimestamp,
        'is-typing': isTyping
      }
    ]"
  >
    <!-- 头像 -->
    <div v-if="showAvatar" class="message-avatar" @click="$emit('avatar-click', message)">
      <img 
        :src="avatarUrl" 
        :alt="senderName"
        class="avatar-img"
        :class="{ 'avatar-online': isOnline }"
      />
      <div v-if="showOnlineStatus && isOnline" class="online-indicator"></div>
    </div>

    <!-- 消息内容区域 -->
    <div class="message-content">
      <!-- 发送者名称 -->
      <div v-if="showSenderName && senderName" class="sender-name">
        {{ senderName }}
      </div>

      <!-- 消息主体 -->
      <div class="message-main">
        <!-- 消息气泡 -->
        <div 
          class="bubble" 
          :class="bubbleClass"
          @click="$emit('bubble-click', message)"
          @contextmenu="handleContextMenu"
        >
          <!-- 文本消息 -->
          <div v-if="message.type === 'text'" class="text-message">
            <div class="text-content" v-html="formattedContent"></div>
            
            <!-- 消息引用 -->
            <div v-if="message.quote" class="message-quote" @click.stop="$emit('quote-click', message.quote)">
              <div class="quote-header">
                <span class="quote-author">{{ message.quote.author }}</span>
                <span class="quote-time">{{ formatTime(message.quote.timestamp) }}</span>
              </div>
              <div class="quote-content">{{ message.quote.content }}</div>
            </div>
          </div>

          <!-- 图片消息 -->
          <div v-else-if="message.type === 'image'" class="image-message">
            <div class="image-container">
              <img 
                :src="message.content" 
                :alt="message.alt || '图片消息'"
                class="message-image"
                @load="handleImageLoad"
                @error="handleImageError"
                @click="$emit('image-click', message)"
              />
              <div v-if="message.loading" class="image-loading">
                <div class="loading-spinner"></div>
                <span>加载中...</span>
              </div>
            </div>
            <div v-if="message.caption" class="image-caption">
              {{ message.caption }}
            </div>
          </div>

          <!-- 文件消息 -->
          <div v-else-if="message.type === 'file'" class="file-message">
            <div class="file-icon">
              <span class="file-emoji">📎</span>
            </div>
            <div class="file-info">
              <div class="file-name">{{ message.fileName || '未知文件' }}</div>
              <div class="file-size">{{ formatFileSize(message.fileSize) }}</div>
              <div v-if="message.progress !== undefined" class="file-progress">
                <div class="progress-bar">
                  <div 
                    class="progress-fill" 
                    :style="{ width: `${message.progress}%` }"
                  ></div>
                </div>
                <span class="progress-text">{{ message.progress }}%</span>
              </div>
            </div>
            <button 
              v-if="message.downloadable" 
              class="download-btn"
              @click.stop="$emit('download', message)"
              :title="`下载 ${message.fileName}`"
            >
              <svg viewBox="0 0 24 24">
                <path d="M19 9h-4V3H9v6H5l7 7 7-7zM5 18v2h14v-2H5z"/>
              </svg>
            </button>
          </div>

          <!-- 系统消息 -->
          <div v-else-if="message.type === 'system'" class="system-message">
            <span class="system-icon">{{ systemIcon }}</span>
            <span class="system-text">{{ message.content }}</span>
          </div>

          <!-- 语音消息 -->
          <div v-else-if="message.type === 'voice'" class="voice-message">
            <button 
              class="play-btn"
              :class="{ playing: isPlaying }"
              @click.stop="togglePlay"
            >
              <span class="play-icon">{{ isPlaying ? '⏸️' : '▶️' }}</span>
            </button>
            <div class="voice-waveform">
              <div 
                v-for="(bar, index) in voiceBars" 
                :key="index"
                class="voice-bar"
                :style="{ height: `${bar}%` }"
                :class="{ active: isPlaying && currentVoiceBar === index }"
              ></div>
            </div>
            <div class="voice-duration">
              {{ formatDuration(message.duration || 0) }}
            </div>
          </div>

          <!-- 位置消息 -->
          <div v-else-if="message.type === 'location'" class="location-message">
            <div class="location-icon">📍</div>
            <div class="location-info">
              <div class="location-title">{{ message.location?.title || '位置分享' }}</div>
              <div class="location-address">{{ message.location?.address || '未知地址' }}</div>
            </div>
            <button 
              class="view-map-btn"
              @click.stop="$emit('view-map', message)"
            >
              查看地图
            </button>
          </div>

          <!-- 自定义消息类型 -->
          <div v-else class="custom-message">
            <slot name="custom-message" :message="message">
              <div class="unknown-message">
                <span class="unknown-icon">❓</span>
                <span>未知消息类型</span>
              </div>
            </slot>
          </div>

          <!-- 消息操作按钮 -->
          <div v-if="showActions && !isTyping" class="message-actions">
            <button 
              v-if="canReply"
              class="action-btn reply-btn"
              @click.stop="$emit('reply', message)"
              title="回复"
            >
              <svg viewBox="0 0 24 24">
                <path d="M10 9V5l-7 7 7 7v-4.1c5 0 8.5 1.6 11 5.1-1-5-4-10-11-11z"/>
              </svg>
            </button>
            <button 
              v-if="canCopy && message.type === 'text'"
              class="action-btn copy-btn"
              @click.stop="copyText"
              title="复制"
            >
              <svg viewBox="0 0 24 24">
                <path d="M16 1H4c-1.1 0-2 .9-2 2v14h2V3h12V1zm3 4H8c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h11c1.1 0 2-.9 2-2V7c0-1.1-.9-2-2-2zm0 16H8V7h11v14z"/>
              </svg>
            </button>
            <button 
              v-if="canDelete"
              class="action-btn delete-btn"
              @click.stop="$emit('delete', message)"
              title="删除"
            >
              <svg viewBox="0 0 24 24">
                <path d="M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z"/>
              </svg>
            </button>
          </div>
        </div>

        <!-- 消息状态区域 -->
        <div v-if="showStatus" class="message-status-area">
          <!-- 消息时间 -->
          <div v-if="showTimestamp && message.timestamp" class="message-time">
            {{ formattedTime }}
          </div>

          <!-- 消息状态图标 -->
          <div v-if="message.status" class="message-status" :class="message.status">
            <span class="status-icon">{{ statusIcon }}</span>
            <span v-if="showStatusText" class="status-text">{{ statusText }}</span>
          </div>

          <!-- 已读回执 -->
          <div v-if="showReadReceipt && message.readBy && message.readBy.length > 0" class="read-receipts">
            <div class="read-by">
              <span class="read-text">已读</span>
              <div class="read-avatars">
                <img 
                  v-for="(reader, index) in message.readBy.slice(0, 3)" 
                  :key="reader.id"
                  :src="reader.avatar" 
                  :alt="reader.name"
                  class="read-avatar"
                  :style="{ zIndex: 3 - index }"
                />
                <div v-if="message.readBy.length > 3" class="more-readers">
                  +{{ message.readBy.length - 3 }}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 输入中指示器 -->
    <div v-if="isTyping" class="typing-indicator">
      <div class="typing-dots">
        <span></span>
        <span></span>
        <span></span>
      </div>
    </div>

    <!-- 上下文菜单 -->
    <div 
      v-if="showContextMenu" 
      class="context-menu"
      :style="contextMenuStyle"
      @click.stop
    >
      <div class="context-menu-item" @click="copyText">复制</div>
      <div class="context-menu-item" @click="$emit('reply', message)">回复</div>
      <div class="context-menu-item" @click="$emit('forward', message)">转发</div>
      <div class="context-menu-divider"></div>
      <div class="context-menu-item delete-item" @click="$emit('delete', message)">删除</div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'

// Props
const props = defineProps({
  message: {
    type: Object,
    required: true,
    validator: (value) => {
      return value && typeof value === 'object' && value.sender && value.type
    }
  },
  
  // 显示配置
  showAvatar: {
    type: Boolean,
    default: true
  },
  showSenderName: {
    type: Boolean,
    default: false
  },
  showTimestamp: {
    type: Boolean,
    default: true
  },
  showStatus: {
    type: Boolean,
    default: true
  },
  showStatusText: {
    type: Boolean,
    default: false
  },
  showReadReceipt: {
    type: Boolean,
    default: false
  },
  showOnlineStatus: {
    type: Boolean,
    default: false
  },
  showActions: {
    type: Boolean,
    default: false
  },
  
  // 功能配置
  canReply: {
    type: Boolean,
    default: true
  },
  canCopy: {
    type: Boolean,
    default: true
  },
  canDelete: {
    type: Boolean,
    default: true
  },
  
  // 状态配置
  isTyping: {
    type: Boolean,
    default: false
  },
  isOnline: {
    type: Boolean,
    default: false
  },
  isPlaying: {
    type: Boolean,
    default: false
  },
  
  // 头像配置
  userAvatar: {
    type: String,
    default: '@/assets/images/patient.png'
  },
  botAvatar: {
    type: String,
    default: '@/assets/images/doctor.png'
  }
})

// Emits
const emit = defineEmits([
  'avatar-click',
  'bubble-click',
  'image-click',
  'quote-click',
  'download',
  'view-map',
  'reply',
  'delete',
  'forward',
  'copy',
  'play-voice',
  'pause-voice'
])

// Refs
const showContextMenu = ref(false)
const contextMenuStyle = ref({})
const currentVoiceBar = ref(0)
const voiceInterval = ref(null)

// Computed
const senderName = computed(() => {
  return props.message.senderName || 
         (props.message.sender === 'user' ? '您' : 
          props.message.sender === 'bot' ? '医疗助手' : '未知用户')
})

const avatarUrl = computed(() => {
  if (props.message.avatar) {
    return props.message.avatar
  }
  return props.message.sender === 'user' ? props.userAvatar : props.botAvatar
})

const formattedTime = computed(() => {
  if (!props.message.timestamp) return ''
  
  const date = new Date(props.message.timestamp)
  const now = new Date()
  const isToday = date.toDateString() === now.toDateString()
  
  if (isToday) {
    return date.toLocaleTimeString('zh-CN', { 
      hour: '2-digit', 
      minute: '2-digit' 
    })
  } else {
    return date.toLocaleDateString('zh-CN', { 
      month: 'short', 
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    })
  }
})

const bubbleClass = computed(() => {
  const classes = []
  
  // 根据消息类型添加类名
  if (props.message.type) {
    classes.push(`bubble-${props.message.type}`)
  }
  
  // 根据消息状态添加类名
  if (props.message.status) {
    classes.push(`bubble-${props.message.status}`)
  }
  
  // 如果有引用消息
  if (props.message.quote) {
    classes.push('has-quote')
  }
  
  return classes
})

const statusIcon = computed(() => {
  const statusMap = {
    'sending': '🕐',
    'sent': '✓',
    'delivered': '✓✓',
    'read': '👁️',
    'error': '❌'
  }
  return statusMap[props.message.status] || ''
})

const statusText = computed(() => {
  const textMap = {
    'sending': '发送中',
    'sent': '已发送',
    'delivered': '已送达',
    'read': '已读',
    'error': '发送失败'
  }
  return textMap[props.message.status] || ''
})

const systemIcon = computed(() => {
  const iconMap = {
    'info': 'ℹ️',
    'success': '✅',
    'warning': '⚠️',
    'error': '❌',
    'tip': '💡'
  }
  return iconMap[props.message.systemType] || 'ℹ️'
})

const formattedContent = computed(() => {
  if (props.message.type !== 'text') return props.message.content
  
  // 简单的文本格式化
  let content = props.message.content
  
  // URL 链接检测（简单版本）
  const urlRegex = /(https?:\/\/[^\s]+)/g
  content = content.replace(urlRegex, '<a href="$1" target="_blank" class="message-link">$1</a>')
  
  // 换行处理
  content = content.replace(/\n/g, '<br>')
  
  return content
})

const voiceBars = computed(() => {
  // 生成随机的音频波形条
  return Array.from({ length: 20 }, () => Math.floor(Math.random() * 60) + 20)
})

// Methods
const formatTime = (timestamp) => {
  const date = new Date(timestamp)
  return date.toLocaleTimeString('zh-CN', { 
    hour: '2-digit', 
    minute: '2-digit' 
  })
}

const formatFileSize = (bytes) => {
  if (!bytes) return '未知大小'
  
  const units = ['B', 'KB', 'MB', 'GB']
  let size = bytes
  let unitIndex = 0
  
  while (size >= 1024 && unitIndex < units.length - 1) {
    size /= 1024
    unitIndex++
  }
  
  return `${size.toFixed(1)} ${units[unitIndex]}`
}

const formatDuration = (seconds) => {
  const mins = Math.floor(seconds / 60)
  const secs = Math.floor(seconds % 60)
  return `${mins}:${secs.toString().padStart(2, '0')}`
}

const handleContextMenu = (event) => {
  event.preventDefault()
  
  // 计算菜单位置
  const rect = event.currentTarget.getBoundingClientRect()
  contextMenuStyle.value = {
    top: `${event.clientY}px`,
    left: `${event.clientX}px`
  }
  
  showContextMenu.value = true
}

const copyText = async () => {
  if (props.message.type !== 'text') return
  
  try {
    await navigator.clipboard.writeText(props.message.content)
    emit('copy', props.message)
    
    // 可以在这里添加复制成功的反馈
    console.log('文本已复制到剪贴板')
  } catch (err) {
    console.error('复制失败:', err)
  }
}

const togglePlay = () => {
  if (props.isPlaying) {
    emit('pause-voice', props.message)
    stopVoiceAnimation()
  } else {
    emit('play-voice', props.message)
    startVoiceAnimation()
  }
}

const startVoiceAnimation = () => {
  stopVoiceAnimation()
  currentVoiceBar.value = 0
  voiceInterval.value = setInterval(() => {
    currentVoiceBar.value = (currentVoiceBar.value + 1) % voiceBars.value.length
  }, 100)
}

const stopVoiceAnimation = () => {
  if (voiceInterval.value) {
    clearInterval(voiceInterval.value)
    voiceInterval.value = null
  }
}

const handleImageLoad = () => {
  console.log('图片加载完成')
}

const handleImageError = () => {
  console.error('图片加载失败')
}

// 点击其他地方关闭上下文菜单
const closeContextMenu = () => {
  showContextMenu.value = false
}

// Lifecycle
onMounted(() => {
  document.addEventListener('click', closeContextMenu)
  document.addEventListener('contextmenu', closeContextMenu)
})

onUnmounted(() => {
  document.removeEventListener('click', closeContextMenu)
  document.removeEventListener('contextmenu', closeContextMenu)
  stopVoiceAnimation()
})
</script>

<style scoped>
.message-bubble {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
  position: relative;
  animation: fadeIn 0.3s ease;
}

.message-bubble.message-user {
  flex-direction: row-reverse;
}

.message-bubble.has-avatar .message-content {
  max-width: calc(100% - 52px);
}

.message-bubble:not(.has-avatar) .message-content {
  max-width: 85%;
}

.message-bubble.is-typing {
  opacity: 0.8;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 头像样式 */
.message-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  overflow: hidden;
  flex-shrink: 0;
  position: relative;
  cursor: pointer;
  transition: transform 0.2s ease;
}

.message-avatar:hover {
  transform: scale(1.05);
}

.avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-img.avatar-online {
  border: 2px solid #4CAF50;
}

.online-indicator {
  position: absolute;
  bottom: 2px;
  right: 2px;
  width: 8px;
  height: 8px;
  background: #4CAF50;
  border: 2px solid white;
  border-radius: 50%;
}

/* 消息内容区域 */
.message-content {
  display: flex;
  flex-direction: column;
  flex: 1;
}

.sender-name {
  font-size: 12px;
  color: #666;
  margin-bottom: 4px;
  padding: 0 8px;
}

.message-user .sender-name {
  text-align: right;
}

.message-main {
  display: flex;
  flex-direction: column;
}

.message-user .message-main {
  align-items: flex-end;
}

.message-bot .message-main {
  align-items: flex-start;
}

/* 消息气泡基础样式 */
.bubble {
  position: relative;
  padding: 12px 16px;
  border-radius: 18px;
  line-height: 1.4;
  word-wrap: break-word;
  max-width: 100%;
  transition: all 0.2s ease;
  cursor: pointer;
}

.bubble:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.message-user .bubble {
  background: linear-gradient(135deg, #1E88E5, #1565C0);
  color: white;
  border-bottom-right-radius: 6px;
}

.message-bot .bubble {
  background: white;
  color: #333;
  border: 1px solid #e0e0e0;
  border-bottom-left-radius: 6px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

/* 文本消息样式 */
.text-content {
  white-space: pre-wrap;
}

.message-link {
  color: inherit;
  text-decoration: underline;
  opacity: 0.9;
}

.message-user .message-link {
  color: rgba(255, 255, 255, 0.9);
}

.message-bot .message-link {
  color: #1E88E5;
}

/* 消息引用样式 */
.message-quote {
  margin-top: 8px;
  padding: 8px;
  background: rgba(0, 0, 0, 0.05);
  border-left: 3px solid #1E88E5;
  border-radius: 4px;
  cursor: pointer;
  transition: background 0.2s;
}

.message-quote:hover {
  background: rgba(0, 0, 0, 0.08);
}

.quote-header {
  display: flex;
  justify-content: space-between;
  font-size: 11px;
  opacity: 0.7;
  margin-bottom: 4px;
}

.quote-content {
  font-size: 13px;
  line-height: 1.3;
}

/* 图片消息样式 */
.image-message {
  max-width: 300px;
}

.image-container {
  position: relative;
  border-radius: 8px;
  overflow: hidden;
}

.message-image {
  max-width: 100%;
  display: block;
  transition: transform 0.3s ease;
}

.message-image:hover {
  transform: scale(1.02);
}

.image-loading {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: white;
}

.loading-spinner {
  width: 24px;
  height: 24px;
  border: 2px solid transparent;
  border-top: 2px solid white;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 8px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.image-caption {
  margin-top: 8px;
  font-size: 13px;
  color: #666;
  line-height: 1.3;
}

/* 文件消息样式 */
.file-message {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  background: rgba(0, 0, 0, 0.03);
  border-radius: 8px;
}

.file-icon {
  font-size: 24px;
}

.file-info {
  flex: 1;
  min-width: 0;
}

.file-name {
  font-weight: 500;
  margin-bottom: 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.file-size {
  font-size: 12px;
  color: #666;
}

.file-progress {
  margin-top: 8px;
}

.progress-bar {
  width: 100%;
  height: 4px;
  background: #e0e0e0;
  border-radius: 2px;
  overflow: hidden;
  margin-bottom: 4px;
}

.progress-fill {
  height: 100%;
  background: #4CAF50;
  transition: width 0.3s ease;
}

.progress-text {
  font-size: 11px;
  color: #666;
}

.download-btn {
  background: #1E88E5;
  border: none;
  border-radius: 6px;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: background 0.2s;
}

.download-btn:hover {
  background: #1565C0;
}

.download-btn svg {
  width: 16px;
  height: 16px;
  fill: white;
}

/* 系统消息样式 */
.system-message {
  display: flex;
  align-items: center;
  gap: 8px;
  justify-content: center;
  text-align: center;
}

.system-icon {
  font-size: 14px;
}

.system-text {
  font-size: 13px;
  opacity: 0.8;
}

/* 语音消息样式 */
.voice-message {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 8px 12px;
}

.play-btn {
  background: #1E88E5;
  border: none;
  border-radius: 50%;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s;
}

.play-btn:hover {
  background: #1565C0;
  transform: scale(1.1);
}

.play-btn.playing {
  background: #ff4444;
}

.voice-waveform {
  display: flex;
  align-items: flex-end;
  gap: 2px;
  height: 24px;
  flex: 1;
}

.voice-bar {
  width: 3px;
  background: #ccc;
  border-radius: 1px;
  transition: all 0.3s ease;
}

.voice-bar.active {
  background: #1E88E5;
}

.voice-duration {
  font-size: 12px;
  color: #666;
  min-width: 40px;
}

/* 位置消息样式 */
.location-message {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  background: rgba(0, 0, 0, 0.03);
  border-radius: 8px;
}

.location-icon {
  font-size: 24px;
}

.location-info {
  flex: 1;
}

.location-title {
  font-weight: 500;
  margin-bottom: 4px;
}

.location-address {
  font-size: 12px;
  color: #666;
}

.view-map-btn {
  background: #1E88E5;
  color: white;
  border: none;
  border-radius: 6px;
  padding: 6px 12px;
  font-size: 12px;
  cursor: pointer;
  transition: background 0.2s;
}

.view-map-btn:hover {
  background: #1565C0;
}

/* 自定义消息样式 */
.custom-message {
  padding: 12px;
}

.unknown-message {
  display: flex;
  align-items: center;
  gap: 8px;
  opacity: 0.7;
}

/* 消息操作按钮 */
.message-actions {
  position: absolute;
  top: -28px;
  display: flex;
  gap: 4px;
  opacity: 0;
  transition: opacity 0.2s;
}

.message-user .message-actions {
  right: 8px;
}

.message-bot .message-actions {
  left: 8px;
}

.bubble:hover .message-actions {
  opacity: 1;
}

.action-btn {
  background: white;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s;
}

.action-btn:hover {
  background: #f5f5f5;
  transform: scale(1.1);
}

.action-btn svg {
  width: 14px;
  height: 14px;
  fill: #666;
}

/* 消息状态区域 */
.message-status-area {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-top: 4px;
  font-size: 11px;
  opacity: 0.7;
}

.message-user .message-status-area {
  justify-content: flex-end;
}

.message-time {
  color: #999;
}

.message-status {
  display: flex;
  align-items: center;
  gap: 2px;
}

.status-icon {
  font-size: 10px;
}

.status-text {
  font-size: 10px;
}

/* 已读回执 */
.read-receipts {
  margin-top: 4px;
}

.read-by {
  display: flex;
  align-items: center;
  gap: 6px;
}

.read-text {
  font-size: 10px;
  color: #999;
}

.read-avatars {
  display: flex;
}

.read-avatar {
  width: 16px;
  height: 16px;
  border-radius: 50%;
  border: 1px solid white;
  margin-left: -4px;
}

.more-readers {
  width: 16px;
  height: 16px;
  border-radius: 50%;
  background: #e0e0e0;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 8px;
  color: #666;
  margin-left: -4px;
}

/* 输入中指示器 */
.typing-indicator {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
}

.typing-dots {
  display: flex;
  gap: 3px;
}

.typing-dots span {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: #999;
  animation: typing 1.4s infinite ease-in-out;
}

.typing-dots span:nth-child(1) { animation-delay: -0.32s; }
.typing-dots span:nth-child(2) { animation-delay: -0.16s; }

@keyframes typing {
  0%, 80%, 100% {
    transform: scale(0.8);
    opacity: 0.5;
  }
  40% {
    transform: scale(1);
    opacity: 1;
  }
}

/* 上下文菜单 */
.context-menu {
  position: fixed;
  background: white;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  z-index: 1000;
  min-width: 120px;
  padding: 4px 0;
}

.context-menu-item {
  padding: 8px 16px;
  cursor: pointer;
  transition: background 0.2s;
  font-size: 14px;
}

.context-menu-item:hover {
  background: #f5f5f5;
}

.context-menu-divider {
  height: 1px;
  background: #e0e0e0;
  margin: 4px 0;
}

.context-menu-item.delete-item {
  color: #ff4444;
}

.context-menu-item.delete-item:hover {
  background: #ffeaea;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .message-bubble.has-avatar .message-content {
    max-width: calc(100% - 44px);
  }
  
  .message-avatar {
    width: 36px;
    height: 36px;
  }
  
  .bubble {
    padding: 10px 14px;
    font-size: 14px;
  }
  
  .image-message {
    max-width: 250px;
  }
  
  .message-actions {
    opacity: 1; /* 在移动端始终显示操作按钮 */
  }
}

@media (max-width: 480px) {
  .message-bubble {
    gap: 8px;
    margin-bottom: 12px;
  }
  
  .message-bubble.has-avatar .message-content {
    max-width: calc(100% - 40px);
  }
  
  .message-avatar {
    width: 32px;
    height: 32px;
  }
  
  .bubble {
    padding: 8px 12px;
    font-size: 13px;
  }
  
  .image-message {
    max-width: 200px;
  }

  


}
</style>