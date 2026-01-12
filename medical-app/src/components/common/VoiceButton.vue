<template>
  <button
    class="voice-button"
    :class="[
      `voice-button--${size}`,
      `voice-button--${variant}`,
      `voice-button--${position}`,
      {
        'voice-button--disabled': disabled,
        'voice-button--loading': loading,
        'voice-button--round': round,
        'voice-button--shadow': shadow,
        'voice-button--floating': floating,
        'voice-button--recording': isRecording,
        'voice-button--pulsing': isRecording && pulse
      }
    ]"
    :disabled="disabled || loading"
    :aria-label="computedAriaLabel"
    :title="tooltip"
    @click="handleClick"
    @mousedown="handleMouseDown"
    @mouseup="handleMouseUp"
    @touchstart="handleTouchStart"
    @touchend="handleTouchEnd"
    @mouseenter="handleMouseEnter"
    @mouseleave="handleMouseLeave"
    @focus="handleFocus"
    @blur="handleBlur"
  >
    <!-- 加载状态 -->
    <div v-if="loading" class="voice-button__loading">
      <div class="voice-button__spinner"></div>
      <span v-if="showLoadingText" class="voice-button__loading-text">
        {{ loadingText }}
      </span>
    </div>

    <!-- 正常状态 -->
    <div v-else class="voice-button__content">
      <!-- 图标 -->
      <div class="voice-button__icon">
        <slot name="icon">
          <!-- 默认语音图标 -->
          <svg 
            v-if="!customIcon" 
            class="voice-button__default-icon"
            :class="{
              'voice-button__default-icon--recording': isRecording
            }"
            viewBox="0 0 24 24"
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
          >
            <!-- 麦克风图标 -->
            <path 
              v-if="!isRecording"
              d="M12 14c1.66 0 3-1.34 3-3V5c0-1.66-1.34-3-3-3S9 3.34 9 5v6c0 1.66 1.34 3 3 3z"
              :fill="iconColor"
            />
            <path 
              v-if="!isRecording"
              d="M17 11c0 2.76-2.24 5-5 5s-5-2.24-5-5H5c0 3.53 2.61 6.43 6 6.92V21h2v-3.08c3.39-.49 6-3.39 6-6.92h-2z"
              :fill="iconColor"
            />
            
            <!-- 录音状态图标 -->
            <rect 
              v-else
              x="9" 
              y="3" 
              width="6" 
              height="12" 
              rx="3" 
              :fill="recordingColor"
            />
            <rect 
              v-else
              x="4" 
              y="11" 
              width="16" 
              height="2" 
              :fill="recordingColor"
            />
          </svg>
          
          <!-- 自定义图标插槽 -->
          <slot v-else name="custom-icon"></slot>
        </slot>
      </div>

      <!-- 文本 -->
      <span 
        v-if="showText || $slots.default" 
        class="voice-button__text"
        :class="{
          'voice-button__text--hidden': hideTextOnMobile && isMobile
        }"
      >
        <slot>
          {{ isRecording ? recordingText : text }}
        </slot>
      </span>

      <!-- 录音时长 -->
      <div v-if="showDuration && isRecording" class="voice-button__duration">
        {{ formattedDuration }}
      </div>

      <!-- 音量指示器 -->
      <div v-if="showVolumeIndicator && isRecording" class="voice-button__volume">
        <div 
          class="voice-button__volume-bar" 
          :style="{ transform: `scaleY(${volumeLevel})` }"
        ></div>
      </div>

      <!-- 徽章 -->
      <div v-if="badge" class="voice-button__badge" :class="`voice-button__badge--${badgeType}`">
        {{ badge }}
      </div>
    </div>

    <!-- 波纹效果 -->
    <div v-if="ripple" class="voice-button__ripple" :style="rippleStyle"></div>

    <!-- 录音波纹动画 -->
    <div v-if="isRecording && waveAnimation" class="voice-button__waves">
      <div class="voice-button__wave voice-button__wave--1"></div>
      <div class="voice-button__wave voice-button__wave--2"></div>
      <div class="voice-button__wave voice-button__wave--3"></div>
    </div>

    <!-- 工具提示 -->
    <div 
      v-if="showTooltip && tooltip" 
      class="voice-button__tooltip"
      :class="`voice-button__tooltip--${tooltipPosition}`"
    >
      {{ tooltip }}
    </div>
  </button>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'

// Props
const props = defineProps({
  // 基础配置
  text: {
    type: String,
    default: '语音输入'
  },
  recordingText: {
    type: String,
    default: '录音中...'
  },
  showText: {
    type: Boolean,
    default: true
  },
  hideTextOnMobile: {
    type: Boolean,
    default: false
  },
  
  // 样式配置
  size: {
    type: String,
    default: 'medium',
    validator: (value) => ['small', 'medium', 'large'].includes(value)
  },
  variant: {
    type: String,
    default: 'primary',
    validator: (value) => ['primary', 'secondary', 'text', 'outline', 'ghost'].includes(value)
  },
  position: {
    type: String,
    default: 'static',
    validator: (value) => ['static', 'fixed', 'absolute', 'sticky'].includes(value)
  },
  
  // 图标配置
  customIcon: {
    type: Boolean,
    default: false
  },
  iconColor: {
    type: String,
    default: 'currentColor'
  },
  recordingColor: {
    type: String,
    default: '#f44336'
  },
  
  // 状态配置
  disabled: {
    type: Boolean,
    default: false
  },
  loading: {
    type: Boolean,
    default: false
  },
  loadingText: {
    type: String,
    default: '加载中...'
  },
  showLoadingText: {
    type: Boolean,
    default: false
  },
  
  // 交互配置
  ripple: {
    type: Boolean,
    default: true
  },
  round: {
    type: Boolean,
    default: true
  },
  shadow: {
    type: Boolean,
    default: true
  },
  floating: {
    type: Boolean,
    default: false
  },
  pulse: {
    type: Boolean,
    default: true
  },
  waveAnimation: {
    type: Boolean,
    default: true
  },
  
  // 辅助功能
  ariaLabel: {
    type: String,
    default: '语音按钮'
  },
  tooltip: {
    type: String,
    default: ''
  },
  tooltipPosition: {
    type: String,
    default: 'top',
    validator: (value) => ['top', 'bottom', 'left', 'right'].includes(value)
  },
  
  // 徽章
  badge: {
    type: [String, Number],
    default: null
  },
  badgeType: {
    type: String,
    default: 'default',
    validator: (value) => ['default', 'primary', 'success', 'warning', 'error'].includes(value)
  },
  
  // 录音功能配置
  showDuration: {
    type: Boolean,
    default: true
  },
  showVolumeIndicator: {
    type: Boolean,
    default: true
  },
  maxDuration: {
    type: Number,
    default: 60000 // 60秒
  },
  holdToRecord: {
    type: Boolean,
    default: true
  }
})

// Emits
const emit = defineEmits([
  'click',
  'start',
  'stop',
  'data',
  'error',
  'mouseenter',
  'mouseleave',
  'focus',
  'blur'
])

// Refs
const isMobile = ref(false)
const showTooltip = ref(false)
const rippleStyle = ref({})
const isHovered = ref(false)
const isFocused = ref(false)
const isRecording = ref(false)
const recordingStartTime = ref(0)
const recordingDuration = ref(0)
const volumeLevel = ref(0)
const durationInterval = ref(null)
const volumeInterval = ref(null)

// Computed
const computedAriaLabel = computed(() => {
  return props.ariaLabel || props.text || '语音按钮'
})

const formattedDuration = computed(() => {
  const seconds = Math.floor(recordingDuration.value / 1000)
  const minutes = Math.floor(seconds / 60)
  const remainingSeconds = seconds % 60
  return `${minutes}:${remainingSeconds.toString().padStart(2, '0')}`
})

// Methods
const handleClick = (event) => {
  if (props.preventDefault) {
    event.preventDefault()
  }
  
  if (props.stopPropagation) {
    event.stopPropagation()
  }
  
  if (props.loading || props.disabled) {
    return
  }
  
  // 触发点击事件
  emit('click', event)
  
  // 添加波纹效果
  if (props.ripple) {
    createRippleEffect(event)
  }
  
  // 如果不是长按模式，则切换录音状态
  if (!props.holdToRecord) {
    toggleRecording()
  }
}

const handleMouseDown = (event) => {
  if (props.holdToRecord && !props.loading && !props.disabled) {
    startRecording()
  }
}

const handleMouseUp = (event) => {
  if (props.holdToRecord && isRecording.value) {
    stopRecording()
  }
}

const handleTouchStart = (event) => {
  if (props.holdToRecord && !props.loading && !props.disabled) {
    startRecording()
    event.preventDefault() // 防止触摸时滚动
  }
}

const handleTouchEnd = (event) => {
  if (props.holdToRecord && isRecording.value) {
    stopRecording()
    event.preventDefault()
  }
}

const handleMouseEnter = (event) => {
  isHovered.value = true
  showTooltip.value = true
  emit('mouseenter', event)
}

const handleMouseLeave = (event) => {
  isHovered.value = false
  showTooltip.value = false
  emit('mouseleave', event)
}

const handleFocus = (event) => {
  isFocused.value = true
  emit('focus', event)
}

const handleBlur = (event) => {
  isFocused.value = false
  emit('blur', event)
}

const toggleRecording = () => {
  if (isRecording.value) {
    stopRecording()
  } else {
    startRecording()
  }
}

const startRecording = () => {
  if (isRecording.value) return
  
  isRecording.value = true
  recordingStartTime.value = Date.now()
  recordingDuration.value = 0
  
  // 开始计时
  durationInterval.value = setInterval(() => {
    recordingDuration.value = Date.now() - recordingStartTime.value
    
    // 检查最大时长
    if (recordingDuration.value >= props.maxDuration) {
      stopRecording()
    }
  }, 100)
  
  // 模拟音量变化
  volumeInterval.value = setInterval(() => {
    volumeLevel.value = Math.random() * 0.8 + 0.2 // 0.2 - 1.0
  }, 200)
  
  emit('start')
  
  // 这里可以集成实际的语音识别 API
  // 例如：navigator.mediaDevices.getUserMedia({ audio: true })
}

const stopRecording = () => {
  if (!isRecording.value) return
  
  isRecording.value = false
  
  // 清除定时器
  if (durationInterval.value) {
    clearInterval(durationInterval.value)
    durationInterval.value = null
  }
  
  if (volumeInterval.value) {
    clearInterval(volumeInterval.value)
    volumeInterval.value = null
  }
  
  const duration = recordingDuration.value
  recordingDuration.value = 0
  volumeLevel.value = 0
  
  emit('stop', { duration })
  
  // 这里可以处理录音数据的发送
}

const createRippleEffect = (event) => {
  const button = event.currentTarget
  const diameter = Math.max(button.clientWidth, button.clientHeight)
  const radius = diameter / 2
  
  const rect = button.getBoundingClientRect()
  const x = event.clientX - rect.left - radius
  const y = event.clientY - rect.top - radius
  
  rippleStyle.value = {
    width: `${diameter}px`,
    height: `${diameter}px`,
    left: `${x}px`,
    top: `${y}px`
  }
  
  // 清除之前的波纹
  const existingRipple = button.querySelector('.voice-button__ripple')
  if (existingRipple) {
    existingRipple.remove()
  }
  
  // 添加新的波纹
  const ripple = document.createElement('div')
  ripple.className = 'voice-button__ripple'
  ripple.style.cssText = `
    width: ${diameter}px;
    height: ${diameter}px;
    left: ${x}px;
    top: ${y}px;
  `
  
  button.appendChild(ripple)
  
  // 动画结束后移除波纹
  setTimeout(() => {
    if (ripple.parentNode === button) {
      button.removeChild(ripple)
    }
  }, 600)
}

const checkMobile = () => {
  isMobile.value = window.innerWidth <= 768
}

const handleResize = () => {
  checkMobile()
}

// Lifecycle
onMounted(() => {
  if (typeof window !== 'undefined') {
    checkMobile()
    window.addEventListener('resize', handleResize)
  }
})

onUnmounted(() => {
  if (typeof window !== 'undefined') {
    window.removeEventListener('resize', handleResize)
  }
  
  // 清理定时器
  if (durationInterval.value) {
    clearInterval(durationInterval.value)
  }
  if (volumeInterval.value) {
    clearInterval(volumeInterval.value)
  }
})
</script>

<style scoped>
.voice-button {
  position: relative;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  border: none;
  outline: none;
  cursor: pointer;
  font-family: inherit;
  font-weight: 500;
  text-decoration: none;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  user-select: none;
  overflow: hidden;
  background: transparent;
  color: inherit;
}

/* 尺寸变体 */
.voice-button--small {
  padding: 6px 12px;
  font-size: 12px;
  min-height: 32px;
}

.voice-button--medium {
  padding: 8px 16px;
  font-size: 14px;
  min-height: 40px;
}

.voice-button--large {
  padding: 12px 20px;
  font-size: 16px;
  min-height: 48px;
}

/* 样式变体 */
.voice-button--primary {
  background: linear-gradient(135deg, #1E88E5, #1565C0);
  color: white;
}

.voice-button--secondary {
  background: #6c757d;
  color: white;
}

.voice-button--text {
  background: transparent;
  color: #1E88E5;
}

.voice-button--outline {
  background: transparent;
  color: #1E88E5;
  border: 1px solid #1E88E5;
}

.voice-button--ghost {
  background: transparent;
  color: #666;
  border: 1px solid transparent;
}

.voice-button--ghost:hover {
  background: rgba(0, 0, 0, 0.05);
}

/* 录音状态 */
.voice-button--recording {
  background: #f44336 !important;
  color: white !important;
}

.voice-button--recording.voice-button--outline {
  background: transparent !important;
  color: #f44336 !important;
  border-color: #f44336 !important;
}

.voice-button--pulsing {
  animation: voice-button-pulse 1.5s ease-in-out infinite;
}

@keyframes voice-button-pulse {
  0% {
    transform: scale(1);
    box-shadow: 0 0 0 0 rgba(244, 67, 54, 0.7);
  }
  50% {
    transform: scale(1.05);
  }
  70% {
    box-shadow: 0 0 0 10px rgba(244, 67, 54, 0);
  }
  100% {
    transform: scale(1);
    box-shadow: 0 0 0 0 rgba(244, 67, 54, 0);
  }
}

/* 位置变体 */
.voice-button--static {
  position: static;
}

.voice-button--fixed {
  position: fixed;
  z-index: 1000;
}

.voice-button--absolute {
  position: absolute;
  z-index: 100;
}

.voice-button--sticky {
  position: sticky;
  z-index: 100;
}

/* 状态样式 */
.voice-button--disabled {
  opacity: 0.6;
  cursor: not-allowed;
  pointer-events: none;
}

.voice-button--loading {
  cursor: wait;
}

.voice-button--round {
  border-radius: 50px;
}

.voice-button--round:not(.voice-button--text) {
  border-radius: 50px;
}

.voice-button--shadow {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.voice-button--shadow:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);
}

.voice-button--floating {
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.25);
}

.voice-button--floating:hover {
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.3);
  transform: translateY(-2px);
}

/* 内容区域 */
.voice-button__content {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  position: relative;
  z-index: 2;
}

/* 图标样式 */
.voice-button__icon {
  display: flex;
  align-items: center;
  justify-content: center;
  transition: transform 0.2s ease;
}

.voice-button:hover .voice-button__icon {
  transform: scale(1.1);
}

.voice-button__default-icon {
  width: 20px;
  height: 20px;
  transition: all 0.3s ease;
}

.voice-button--small .voice-button__default-icon {
  width: 16px;
  height: 16px;
}

.voice-button--large .voice-button__default-icon {
  width: 24px;
  height: 24px;
}

.voice-button__default-icon--recording {
  animation: voice-button-icon-pulse 0.8s ease-in-out infinite;
}

@keyframes voice-button-icon-pulse {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: 0.6;
  }
}

/* 文本样式 */
.voice-button__text {
  white-space: nowrap;
  transition: opacity 0.3s ease;
}

.voice-button__text--hidden {
  opacity: 0;
  width: 0;
  overflow: hidden;
}

/* 录音时长 */
.voice-button__duration {
  font-size: 0.75em;
  font-weight: 600;
  background: rgba(0, 0, 0, 0.2);
  padding: 2px 6px;
  border-radius: 10px;
  min-width: 40px;
  text-align: center;
}

/* 音量指示器 */
.voice-button__volume {
  width: 4px;
  height: 16px;
  background: rgba(255, 255, 255, 0.3);
  border-radius: 2px;
  overflow: hidden;
  position: relative;
}

.voice-button__volume-bar {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: white;
  border-radius: 2px;
  transform-origin: bottom;
  transition: transform 0.1s ease;
}

/* 加载状态 */
.voice-button__loading {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.voice-button__spinner {
  width: 16px;
  height: 16px;
  border: 2px solid transparent;
  border-top: 2px solid currentColor;
  border-radius: 50%;
  animation: voice-button-spin 1s linear infinite;
}

@keyframes voice-button-spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.voice-button__loading-text {
  font-size: 0.875em;
  opacity: 0.8;
}

/* 徽章样式 */
.voice-button__badge {
  position: absolute;
  top: -4px;
  right: -4px;
  min-width: 16px;
  height: 16px;
  padding: 0 4px;
  border-radius: 8px;
  font-size: 10px;
  font-weight: 600;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  z-index: 3;
}

.voice-button__badge--default {
  background: #6c757d;
}

.voice-button__badge--primary {
  background: #1E88E5;
}

.voice-button__badge--success {
  background: #4CAF50;
}

.voice-button__badge--warning {
  background: #FF9800;
}

.voice-button__badge--error {
  background: #f44336;
}

/* 波纹效果 */
.voice-button__ripple {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.6);
  transform: scale(0);
  animation: voice-button-ripple 0.6s linear;
  pointer-events: none;
  z-index: 1;
}

@keyframes voice-button-ripple {
  to {
    transform: scale(4);
    opacity: 0;
  }
}

/* 录音波纹动画 */
.voice-button__waves {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  pointer-events: none;
}

.voice-button__wave {
  position: absolute;
  top: 50%;
  left: 50%;
  width: 100%;
  height: 100%;
  border: 2px solid rgba(244, 67, 54, 0.6);
  border-radius: 50%;
  transform: translate(-50%, -50%) scale(0);
  animation: voice-button-wave 2s linear infinite;
}

.voice-button__wave--1 {
  animation-delay: 0s;
}

.voice-button__wave--2 {
  animation-delay: 0.66s;
}

.voice-button__wave--3 {
  animation-delay: 1.33s;
}

@keyframes voice-button-wave {
  0% {
    transform: translate(-50%, -50%) scale(0);
    opacity: 1;
  }
  100% {
    transform: translate(-50%, -50%) scale(1.5);
    opacity: 0;
  }
}

/* 工具提示 */
.voice-button__tooltip {
  position: absolute;
  background: rgba(0, 0, 0, 0.8);
  color: white;
  padding: 6px 12px;
  border-radius: 4px;
  font-size: 12px;
  white-space: nowrap;
  pointer-events: none;
  z-index: 1000;
  opacity: 0;
  transform: translateY(4px);
  transition: all 0.2s ease;
}

.voice-button:hover .voice-button__tooltip {
  opacity: 1;
  transform: translateY(0);
}

.voice-button__tooltip--top {
  bottom: 100%;
  left: 50%;
  transform: translateX(-50%) translateY(-8px);
}

.voice-button:hover .voice-button__tooltip--top {
  transform: translateX(-50%) translateY(-4px);
}

.voice-button__tooltip--bottom {
  top: 100%;
  left: 50%;
  transform: translateX(-50%) translateY(8px);
}

.voice-button:hover .voice-button__tooltip--bottom {
  transform: translateX(-50%) translateY(4px);
}

.voice-button__tooltip--left {
  right: 100%;
  top: 50%;
  transform: translateX(8px) translateY(-50%);
}

.voice-button:hover .voice-button__tooltip--left {
  transform: translateX(4px) translateY(-50%);
}

.voice-button__tooltip--right {
  left: 100%;
  top: 50%;
  transform: translateX(-8px) translateY(-50%);
}

.voice-button:hover .voice-button__tooltip--right {
  transform: translateX(-4px) translateY(-50%);
}

/* 悬停和焦点状态 */
.voice-button:not(.voice-button--disabled):not(.voice-button--loading):hover {
  transform: translateY(-1px);
}

.voice-button:not(.voice-button--disabled):not(.voice-button--loading):active {
  transform: translateY(0);
}

.voice-button:focus-visible {
  outline: 2px solid #1E88E5;
  outline-offset: 2px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .voice-button--medium {
    padding: 6px 12px;
    min-height: 36px;
  }
  
  .voice-button--large {
    padding: 8px 16px;
    min-height: 42px;
  }
  
  .voice-button__text--hidden {
    display: none;
  }
}

@media (max-width: 480px) {
  .voice-button--small {
    padding: 4px 8px;
    min-height: 28px;
  }
  
  .voice-button--medium {
    padding: 4px 10px;
    min-height: 32px;
  }
  
  .voice-button--large {
    padding: 6px 12px;
    min-height: 36px;
  }
}

/* 深色模式支持 */
@media (prefers-color-scheme: dark) {
  .voice-button--text {
    color: #64b5f6;
  }
  
  .voice-button--outline {
    color: #64b5f6;
    border-color: #64b5f6;
  }
  
  .voice-button--ghost {
    color: #ccc;
  }
  
  .voice-button--ghost:hover {
    background: rgba(255, 255, 255, 0.1);
  }
}

/* 打印样式 */
@media print {
  .voice-button {
    display: none;
  }
}
</style>