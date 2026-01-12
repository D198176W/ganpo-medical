<template>
  <div class="chat-window">
    <!-- 聊天头部 -->
    <header class="chat-header">
      <div class="header-content">
        <div class="header-left">
          <button v-if="showBackButton" class="back-btn" @click="$emit('back')">
            <svg viewBox="0 0 24 24">
              <path d="M15.41 7.41L14 6l-6 6 6 6 1.41-1.41L10.83 12z"/>
            </svg>
          </button>
          <div class="header-info">
            <h2 class="chat-title">{{ title }}</h2>
            <p class="chat-subtitle" v-if="subtitle">{{ subtitle }}</p>
          </div>
        </div>
        <div class="header-actions">
          <button class="action-btn" @click="toggleSidebar" v-if="showSidebarToggle">
            <svg viewBox="0 0 24 24">
              <path d="M3 18h18v-2H3v2zm0-5h18v-2H3v2zm0-7v2h18V6H3z"/>
            </svg>
          </button>
          <button class="action-btn" @click="clearMessages">
            <svg viewBox="0 0 24 24">
              <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"/>
            </svg>
          </button>
        </div>
      </div>
    </header>

    <!-- 欢迎界面 -->
    <div v-if="showWelcome && messages.length === 0" class="welcome-screen">
      <div class="welcome-content">
        <div class="welcome-icon">👋</div>
        <h3 class="welcome-title">{{ welcomeTitle }}</h3>
        <p class="welcome-description">{{ welcomeDescription }}</p>
        
        <div v-if="welcomeTips && welcomeTips.length" class="welcome-tips">
          <h4 class="tips-title">{{ tipsTitle }}</h4>
          <ul class="tips-list">
            <li v-for="(tip, index) in welcomeTips" :key="index" class="tip-item">
              {{ tip }}
            </li>
          </ul>
        </div>

        <div v-if="quickQuestions && quickQuestions.length" class="quick-questions">
          <h4 class="quick-questions-title">快捷提问</h4>
          <div class="questions-grid">
            <button 
              v-for="(question, index) in quickQuestions" 
              :key="index"
              class="question-chip"
              @click="$emit('quick-question', question)"
            >
              {{ question }}
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 消息列表 -->
    <div class="chat-messages" ref="messagesContainer">
      <div 
        v-for="message in messages" 
        :key="message.id"
        class="message"
        :class="[
          message.sender,
          message.type,
          { 'has-avatar': showAvatars }
        ]"
      >
        <!-- 头像 -->
        <div v-if="showAvatars" class="message-avatar">
          <img 
            :src="getAvatar(message.sender)" 
            :alt="message.sender"
            class="avatar-img"
          />
        </div>

        <!-- 消息内容 -->
        <div class="message-content">
          <!-- 发送者名称 -->
          <div v-if="showSenderNames && message.senderName" class="sender-name">
            {{ message.senderName }}
          </div>

          <!-- 消息气泡 -->
          <div class="message-bubble">
            <!-- 文本内容 -->
            <div v-if="message.type === 'text'" class="text-content">
              {{ message.content }}
            </div>

            <!-- 图片内容 -->
            <div v-if="message.type === 'image'" class="image-content">
              <img 
                :src="message.content" 
                :alt="message.alt || '图片消息'"
                class="message-image"
                @click="$emit('image-preview', message.content)"
              />
            </div>

            <!-- 系统消息 -->
            <div v-if="message.type === 'system'" class="system-content">
              <span class="system-icon">ℹ️</span>
              <span>{{ message.content }}</span>
            </div>

            <!-- 消息时间 -->
            <div v-if="showTimestamps && message.timestamp" class="message-time">
              {{ formatTime(message.timestamp) }}
            </div>

            <!-- 消息状态 -->
            <div v-if="message.status" class="message-status" :class="message.status">
              <span v-if="message.status === 'sending'">🕐</span>
              <span v-else-if="message.status === 'sent'">✓</span>
              <span v-else-if="message.status === 'delivered'">✓✓</span>
              <span v-else-if="message.status === 'read'">👁️</span>
              <span v-else-if="message.status === 'error'">❌</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 输入中状态 -->
      <div v-if="isTyping" class="typing-indicator">
        <div class="typing-avatar" v-if="showAvatars">
          <img :src="typingAvatar" alt="正在输入" class="avatar-img" />
        </div>
        <div class="typing-content">
          <div class="typing-bubble">
            <div class="typing-dots">
              <span></span>
              <span></span>
              <span></span>
            </div>
            <span class="typing-text">正在输入...</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 输入区域 -->
    <div class="chat-input-area">
      <div class="input-container">
        <!-- 附件按钮 -->
        <button 
          v-if="showAttachmentButton"
          class="input-action-btn attachment-btn"
          @click="$emit('attachment')"
          title="添加附件"
        >
          <svg viewBox="0 0 24 24">
            <path d="M16.5 6v11.5c0 2.21-1.79 4-4 4s-4-1.79-4-4V5c0-1.38 1.12-2.5 2.5-2.5s2.5 1.12 2.5 2.5v10.5c0 .55-.45 1-1 1s-1-.45-1-1V6H10v9.5c0 1.38 1.12 2.5 2.5 2.5s2.5-1.12 2.5-2.5V5c0-2.21-1.79-4-4-4S7 2.79 7 5v12.5c0 3.04 2.46 5.5 5.5 5.5s5.5-2.46 5.5-5.5V6h-1.5z"/>
          </svg>
        </button>

        <!-- 文本输入框 -->
        <div class="text-input-wrapper">
          <textarea
            ref="textInput"
            v-model="inputText"
            :placeholder="inputPlaceholder"
            class="text-input"
            rows="1"
            @keydown="handleKeydown"
            @input="adjustTextareaHeight"
          ></textarea>
        </div>

        <!-- 语音输入按钮 -->
        <button 
          v-if="showVoiceButton"
          class="input-action-btn voice-btn"
          :class="{ recording: isRecording }"
          @click="toggleVoiceRecording"
          :title="isRecording ? '停止录音' : '语音输入'"
        >
          <span v-if="isRecording">🔴</span>
          <span v-else>🎤</span>
        </button>

        <!-- 发送按钮 -->
        <button 
          class="send-btn"
          :disabled="!canSend"
          @click="sendMessage"
          :title="canSend ? '发送消息' : '请输入消息'"
        >
          <svg viewBox="0 0 24 24">
            <path d="M2.01 21L23 12 2.01 3 2 10l15 2-15 2z"/>
          </svg>
        </button>
      </div>

      <!-- 输入提示 -->
      <div v-if="inputHint" class="input-hint">
        {{ inputHint }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, nextTick, watch, onMounted, onUnmounted } from 'vue'

// Props
const props = defineProps({
  // 基础配置
  title: {
    type: String,
    default: '智能医疗助手'
  },
  subtitle: {
    type: String,
    default: ''
  },
  
  // 消息相关
  messages: {
    type: Array,
    default: () => []
  },
  isTyping: {
    type: Boolean,
    default: false
  },
  
  // 欢迎界面配置
  showWelcome: {
    type: Boolean,
    default: true
  },
  welcomeTitle: {
    type: String,
    default: '您好！我是您的智能医疗助手'
  },
  welcomeDescription: {
    type: String,
    default: '请描述您的症状或健康问题，我会尽力为您提供帮助'
  },
  welcomeTips: {
    type: Array,
    default: () => [
      '具体部位：是"头疼"还是"左侧太阳穴疼"？',
      '持续时间：症状持续了多久？几分钟还是几天？',
      '伴随感觉：是否伴有发烧、咳嗽、乏力等？',
      '严重程度：如果10分是满分，您会给疼痛打几分？'
    ]
  },
  tipsTitle: {
    type: String,
    default: '高效描述小贴士：'
  },
  
  // 快捷问题
  quickQuestions: {
    type: Array,
    default: () => [
      '我最近总是失眠，怎么办？',
      '如何预防感冒？',
      '头疼可能是什么原因？',
      '胃不舒服应该注意什么？'
    ]
  },
  
  // UI 配置
  showBackButton: {
    type: Boolean,
    default: false
  },
  showSidebarToggle: {
    type: Boolean,
    default: false
  },
  showAvatars: {
    type: Boolean,
    default: true
  },
  showSenderNames: {
    type: Boolean,
    default: false
  },
  showTimestamps: {
    type: Boolean,
    default: true
  },
  
  // 输入配置
  inputPlaceholder: {
    type: String,
    default: '请输入您的问题或症状描述...'
  },
  inputHint: {
    type: String,
    default: ''
  },
  showAttachmentButton: {
    type: Boolean,
    default: true
  },
  showVoiceButton: {
    type: Boolean,
    default: true
  },
  
  // 头像配置
  userAvatar: {
    type: String,
    default: '@/assets/images/patient.png'
  },
  botAvatar: {
    type: String,
    default: '@/assets/images/doctor.png'
  },
  typingAvatar: {
    type: String,
    default: '@/assets/images/doctor.png'
  }
})

// Emits
const emit = defineEmits([
  'send-message',
  'quick-question',
  'clear-messages',
  'back',
  'attachment',
  'voice-start',
  'voice-stop',
  'image-preview'
])

// Refs
const inputText = ref('')
const isRecording = ref(false)
const messagesContainer = ref(null)
const textInput = ref(null)

// Computed
const canSend = computed(() => {
  return inputText.value.trim().length > 0
})

// Methods
const getAvatar = (sender) => {
  return sender === 'user' ? props.userAvatar : props.botAvatar
}

const formatTime = (timestamp) => {
  if (!timestamp) return ''
  
  const date = new Date(timestamp)
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
}

const sendMessage = () => {
  if (!canSend.value) return
  
  const message = inputText.value.trim()
  emit('send-message', message)
  inputText.value = ''
  
  // 重置文本框高度
  nextTick(() => {
    adjustTextareaHeight()
  })
}

const clearMessages = () => {
  if (confirm('确定要清空所有聊天记录吗？')) {
    emit('clear-messages')
  }
}

const toggleVoiceRecording = () => {
  isRecording.value = !isRecording.value
  
  if (isRecording.value) {
    emit('voice-start')
    // 模拟3秒后停止录音
    setTimeout(() => {
      isRecording.value = false
      emit('voice-stop', '这是模拟的语音输入文本')
    }, 3000)
  } else {
    emit('voice-stop')
  }
}

const handleKeydown = (event) => {
  if (event.key === 'Enter' && !event.shiftKey) {
    event.preventDefault()
    sendMessage()
  }
}

const adjustTextareaHeight = () => {
  if (!textInput.value) return
  
  textInput.value.style.height = 'auto'
  const newHeight = Math.min(textInput.value.scrollHeight, 120)
  textInput.value.style.height = newHeight + 'px'
}

const scrollToBottom = () => {
  if (!messagesContainer.value) return
  
  nextTick(() => {
    messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
  })
}

const toggleSidebar = () => {
  // 这里可以触发侧边栏的显示/隐藏
  console.log('Toggle sidebar')
}

// Watchers
watch(() => props.messages, () => {
  scrollToBottom()
}, { deep: true })

// Lifecycle
onMounted(() => {
  scrollToBottom()
})

onUnmounted(() => {
  // 清理工作
  if (isRecording.value) {
    emit('voice-stop')
  }
})
</script>

<style scoped>
.chat-window {
  display: flex;
  flex-direction: column;
  height: 100%;
  background: #ffffff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

/* 头部样式 */
.chat-header {
  background: linear-gradient(135deg, #1E88E5, #1565C0);
  color: white;
  padding: 16px 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.back-btn {
  background: rgba(255, 255, 255, 0.2);
  border: none;
  border-radius: 50%;
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: background 0.2s;
}

.back-btn:hover {
  background: rgba(255, 255, 255, 0.3);
}

.back-btn svg {
  width: 20px;
  height: 20px;
  fill: white;
}

.header-info {
  display: flex;
  flex-direction: column;
}

.chat-title {
  font-size: 18px;
  font-weight: 600;
  margin: 0;
  line-height: 1.2;
}

.chat-subtitle {
  font-size: 12px;
  opacity: 0.9;
  margin: 2px 0 0 0;
}

.header-actions {
  display: flex;
  gap: 8px;
}

.action-btn {
  background: rgba(255, 255, 255, 0.2);
  border: none;
  border-radius: 6px;
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: background 0.2s;
}

.action-btn:hover {
  background: rgba(255, 255, 255, 0.3);
}

.action-btn svg {
  width: 18px;
  height: 18px;
  fill: white;
}

/* 欢迎界面样式 */
.welcome-screen {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
  background: #f8f9fa;
}

.welcome-content {
  text-align: center;
  max-width: 500px;
}

.welcome-icon {
  font-size: 64px;
  margin-bottom: 20px;
}

.welcome-title {
  font-size: 24px;
  font-weight: 600;
  color: #333;
  margin-bottom: 12px;
}

.welcome-description {
  font-size: 16px;
  color: #666;
  line-height: 1.5;
  margin-bottom: 30px;
}

.welcome-tips {
  background: rgba(30, 136, 229, 0.05);
  border-left: 4px solid #1E88E5;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 30px;
  text-align: left;
}

.tips-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 12px;
}

.tips-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.tip-item {
  font-size: 14px;
  color: #555;
  line-height: 1.6;
  margin-bottom: 8px;
  padding-left: 16px;
  position: relative;
}

.tip-item:before {
  content: "•";
  color: #1E88E5;
  position: absolute;
  left: 0;
}

.quick-questions {
  margin-top: 30px;
}

.quick-questions-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 16px;
}

.questions-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  justify-content: center;
}

.question-chip {
  background: white;
  border: 1px solid #e0e0e0;
  border-radius: 20px;
  padding: 8px 16px;
  font-size: 14px;
  color: #333;
  cursor: pointer;
  transition: all 0.2s;
}

.question-chip:hover {
  background: #1E88E5;
  color: white;
  border-color: #1E88E5;
  transform: translateY(-1px);
}

/* 消息区域样式 */
.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  background: #f8f9fa;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

/* 消息通用样式 */
.message {
  display: flex;
  gap: 12px;
  animation: fadeIn 0.3s ease;
}

.message.user {
  flex-direction: row-reverse;
}

.message.has-avatar .message-content {
  max-width: 70%;
}

.message:not(.has-avatar) .message-content {
  max-width: 85%;
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
}

.avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* 消息内容样式 */
.message-content {
  display: flex;
  flex-direction: column;
}

.sender-name {
  font-size: 12px;
  color: #666;
  margin-bottom: 4px;
  padding: 0 8px;
}

.message.user .sender-name {
  text-align: right;
}

/* 消息气泡样式 */
.message-bubble {
  position: relative;
  padding: 12px 16px;
  border-radius: 18px;
  line-height: 1.4;
  word-wrap: break-word;
}

.message.user .message-bubble {
  background: linear-gradient(135deg, #1E88E5, #1565C0);
  color: white;
  border-bottom-right-radius: 6px;
}

.message.bot .message-bubble {
  background: white;
  color: #333;
  border: 1px solid #e0e0e0;
  border-bottom-left-radius: 6px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.message.system .message-bubble {
  background: #fff3cd;
  color: #856404;
  border-radius: 8px;
  text-align: center;
  margin: 0 auto;
  max-width: 80%;
}

.system-content {
  display: flex;
  align-items: center;
  gap: 8px;
  justify-content: center;
}

.system-icon {
  font-size: 14px;
}

/* 图片消息样式 */
.image-content {
  max-width: 300px;
}

.message-image {
  max-width: 100%;
  border-radius: 8px;
  cursor: pointer;
  transition: transform 0.2s;
}

.message-image:hover {
  transform: scale(1.02);
}

/* 消息时间和状态 */
.message-time {
  font-size: 11px;
  opacity: 0.7;
  margin-top: 4px;
}

.message.user .message-time {
  text-align: right;
}

.message-status {
  position: absolute;
  bottom: -16px;
  font-size: 10px;
  opacity: 0.7;
}

.message.user .message-status {
  right: 8px;
}

.message.bot .message-status {
  left: 8px;
}

/* 输入中指示器 */
.typing-indicator {
  display: flex;
  gap: 12px;
  align-items: flex-end;
}

.typing-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  overflow: hidden;
  flex-shrink: 0;
}

.typing-content {
  display: flex;
  flex-direction: column;
}

.typing-bubble {
  background: white;
  border: 1px solid #e0e0e0;
  border-radius: 18px;
  border-bottom-left-radius: 6px;
  padding: 12px 16px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.typing-dots {
  display: flex;
  gap: 4px;
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

.typing-text {
  font-size: 14px;
  color: #666;
}

/* 输入区域样式 */
.chat-input-area {
  border-top: 1px solid #e0e0e0;
  background: white;
  padding: 16px 20px;
}

.input-container {
  display: flex;
  align-items: flex-end;
  gap: 12px;
}

.input-action-btn {
  background: none;
  border: none;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: background 0.2s;
  flex-shrink: 0;
}

.input-action-btn:hover {
  background: #f5f5f5;
}

.input-action-btn svg {
  width: 20px;
  height: 20px;
  fill: #666;
}

.text-input-wrapper {
  flex: 1;
  position: relative;
}

.text-input {
  width: 100%;
  border: 1px solid #e0e0e0;
  border-radius: 20px;
  padding: 12px 16px;
  font-size: 14px;
  font-family: inherit;
  resize: none;
  outline: none;
  transition: border-color 0.2s;
  max-height: 120px;
  overflow-y: auto;
}

.text-input:focus {
  border-color: #1E88E5;
}

.voice-btn {
  background: #f0f0f0;
}

.voice-btn.recording {
  background: #ff4444;
  animation: pulse 1.5s infinite;
}

.voice-btn.recording span {
  color: white;
}

@keyframes pulse {
  0% {
    box-shadow: 0 0 0 0 rgba(255, 68, 68, 0.7);
  }
  70% {
    box-shadow: 0 0 0 10px rgba(255, 68, 68, 0);
  }
  100% {
    box-shadow: 0 0 0 0 rgba(255, 68, 68, 0);
  }
}

.send-btn {
  background: #1E88E5;
  border: none;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s;
  flex-shrink: 0;
}

.send-btn:hover:not(:disabled) {
  background: #1565C0;
  transform: scale(1.05);
}

.send-btn:disabled {
  background: #ccc;
  cursor: not-allowed;
}

.send-btn svg {
  width: 20px;
  height: 20px;
  fill: white;
}

.input-hint {
  font-size: 12px;
  color: #666;
  margin-top: 8px;
  text-align: center;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .chat-header {
    padding: 12px 16px;
  }
  
  .chat-title {
    font-size: 16px;
  }
  
  .chat-messages {
    padding: 16px;
  }
  
  .message.has-avatar .message-content {
    max-width: 75%;
  }
  
  .chat-input-area {
    padding: 12px 16px;
  }
  
  .input-container {
    gap: 8px;
  }
  
  .input-action-btn,
  .send-btn {
    width: 36px;
    height: 36px;
  }
}

/* 滚动条样式 */
.chat-messages::-webkit-scrollbar {
  width: 6px;
}

.chat-messages::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.chat-messages::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.chat-messages::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>