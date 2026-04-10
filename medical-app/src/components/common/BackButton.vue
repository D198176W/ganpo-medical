<template>
  <button
    class="back-button"
    :class="[
      `back-button--${size}`,
      `back-button--${variant}`,
      `back-button--${position}`,
      {
        'back-button--disabled': disabled,
        'back-button--loading': loading,
        'back-button--round': round,
        'back-button--shadow': shadow,
        'back-button--floating': floating
      }
    ]"
    :disabled="disabled || loading"
    :aria-label="ariaLabel"
    :title="tooltip"
    @click="handleClick"
    @mouseenter="handleMouseEnter"
    @mouseleave="handleMouseLeave"
    @focus="handleFocus"
    @blur="handleBlur"
  >

    <!-- 加载状态 -->
    <div v-if="loading" class="back-button__loading">
      <div class="back-button__spinner"></div>
      <span v-if="showLoadingText" class="back-button__loading-text">
        {{ loadingText }}
      </span>
    </div>

    <!-- 正常状态 -->
    <div v-else class="back-button__content">
      <!-- 图标 -->
      <div class="back-button__icon">
        <slot name="icon">
          <!-- 默认返回图标 -->
          <svg 
            v-if="!customIcon" 
            class="back-button__default-icon"
            :class="`back-button__default-icon--${iconVariant}`"
            viewBox="0 0 24 24"
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
          >
            <!-- 箭头图标 -->
            <path 
              v-if="iconVariant === 'arrow'"
              d="M15.41 7.41L14 6l-6 6 6 6 1.41-1.41L10.83 12z"
              :fill="iconColor"
            />
            <!-- 叉号图标 -->
            <path 
              v-else-if="iconVariant === 'close'"
              d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"
              :fill="iconColor"
            />
            <!-- 菜单图标 -->
            <path 
              v-else-if="iconVariant === 'menu'"
              d="M3 18h18v-2H3v2zm0-5h18v-2H3v2zm0-7v2h18V6H3z"
              :fill="iconColor"
            />
            <!-- 默认箭头图标 -->
            <path 
              v-else
              d="M15.41 7.41L14 6l-6 6 6 6 1.41-1.41L10.83 12z"
              :fill="iconColor"
            />
          </svg>
          
          <!-- 自定义图标插槽 -->
          <slot v-else name="custom-icon"></slot>
        </slot>
      </div>

      <!-- 文本 -->
      <span 
        v-if="showText || $slots.default" 
        class="back-button__text"
        :class="{
          'back-button__text--hidden': hideTextOnMobile && isMobile
        }"
      >
        <slot>
          {{ text }}
        </slot>
      </span>

      <!-- 徽章 -->
      <div v-if="badge" class="back-button__badge" :class="`back-button__badge--${badgeType}`">
        {{ badge }}
      </div>
    </div>

    <!-- 波纹效果 -->
    <div v-if="ripple" class="back-button__ripple" :style="rippleStyle"></div>

    <!-- 工具提示 -->
    <div 
      v-if="showTooltip && tooltip" 
      class="back-button__tooltip"
      :class="`back-button__tooltip--${tooltipPosition}`"
    >
      {{ tooltip }}
    </div>

    <div class="back-btn" @click="$emit('click')">
    <i class="fas fa-arrow-left"></i>
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
    default: '返回'
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
  iconVariant: {
    type: String,
    default: 'arrow',
    validator: (value) => ['arrow', 'close', 'menu'].includes(value)
  },
  customIcon: {
    type: Boolean,
    default: false
  },
  iconColor: {
    type: String,
    default: 'currentColor'
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
  
  // 辅助功能
  ariaLabel: {
    type: String,
    default: '返回按钮'
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
  
  // 路由配置
  to: {
    type: [String, Object],
    default: null
  },
  replace: {
    type: Boolean,
    default: false
  },
  
  // 点击行为
  preventDefault: {
    type: Boolean,
    default: false
  },
  stopPropagation: {
    type: Boolean,
    default: false
  }
})

// Emits
const emit = defineEmits([
  'click',
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

// Computed
const computedAriaLabel = computed(() => {
  return props.ariaLabel || props.text || '返回按钮'
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
  
  // 路由跳转
  if (props.to) {
    handleRouteNavigation()
  } else {
    // 默认返回行为
    handleDefaultBack()
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

const createRippleEffect = (event) => {
  const button = event.currentTarget
  const circle = document.createElement('span')
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
  const existingRipple = button.querySelector('.back-button__ripple')
  if (existingRipple) {
    existingRipple.remove()
  }
  
  // 添加新的波纹
  const ripple = document.createElement('div')
  ripple.className = 'back-button__ripple'
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

const handleRouteNavigation = () => {
  // 如果有路由配置，使用路由跳转
  if (props.to && typeof window !== 'undefined' && window.$router) {
    if (props.replace) {
      window.$router.replace(props.to)
    } else {
      window.$router.push(props.to)
    }
  }
}

const handleDefaultBack = () => {
  // 默认返回上一页
  if (typeof window !== 'undefined' && window.history) {
    window.history.back()
  }
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
})


</script>

<style scoped>
.back-button {
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
.back-button--small {
  padding: 6px 12px;
  font-size: 12px;
  min-height: 32px;
}

.back-button--medium {
  padding: 8px 16px;
  font-size: 14px;
  min-height: 40px;
}

.back-button--large {
  padding: 12px 20px;
  font-size: 16px;
  min-height: 48px;
}

/* 样式变体 */
.back-button--primary {
  background: linear-gradient(135deg, #1E88E5, #1565C0);
  color: white;
}

.back-button--secondary {
  background: #6c757d;
  color: white;
}

.back-button--text {
  background: transparent;
  color: #1E88E5;
}

.back-button--outline {
  background: transparent;
  color: #1E88E5;
  border: 1px solid #1E88E5;
}

.back-button--ghost {
  background: transparent;
  color: #666;
  border: 1px solid transparent;
}

.back-button--ghost:hover {
  background: rgba(0, 0, 0, 0.05);
}

/* 位置变体 */
.back-button--static {
  position: static;
}

.back-button--fixed {
  position: fixed;
  z-index: 1000;
}

.back-button--absolute {
  position: absolute;
  z-index: 100;
}

.back-button--sticky {
  position: sticky;
  z-index: 100;
}

/* 状态样式 */
.back-button--disabled {
  opacity: 0.6;
  cursor: not-allowed;
  pointer-events: none;
}

.back-button--loading {
  cursor: wait;
}

.back-button--round {
  border-radius: 50px;
}

.back-button--round:not(.back-button--text) {
  border-radius: 50px;
}

.back-button--shadow {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.back-button--shadow:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);
}

.back-button--floating {
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.25);
}

.back-button--floating:hover {
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.3);
  transform: translateY(-2px);
}

/* 内容区域 */
.back-button__content {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  position: relative;
  z-index: 2;
}

/* 图标样式 */
.back-button__icon {
  display: flex;
  align-items: center;
  justify-content: center;
  transition: transform 0.2s ease;
}

.back-button:hover .back-button__icon {
  transform: translateX(-2px);
}

.back-button__default-icon {
  width: 20px;
  height: 20px;
  transition: transform 0.2s ease;
}

.back-button--small .back-button__default-icon {
  width: 16px;
  height: 16px;
}

.back-button--large .back-button__default-icon {
  width: 24px;
  height: 24px;
}

/* 文本样式 */
.back-button__text {
  white-space: nowrap;
  transition: opacity 0.3s ease;
}

.back-button__text--hidden {
  opacity: 0;
  width: 0;
  overflow: hidden;
}

/* 加载状态 */
.back-button__loading {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.back-button__spinner {
  width: 16px;
  height: 16px;
  border: 2px solid transparent;
  border-top: 2px solid currentColor;
  border-radius: 50%;
  animation: back-button-spin 1s linear infinite;
}

@keyframes back-button-spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.back-button__loading-text {
  font-size: 0.875em;
  opacity: 0.8;
}

/* 徽章样式 */
.back-button__badge {
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

.back-button__badge--default {
  background: #6c757d;
}

.back-button__badge--primary {
  background: #1E88E5;
}

.back-button__badge--success {
  background: #4CAF50;
}

.back-button__badge--warning {
  background: #FF9800;
}

.back-button__badge--error {
  background: #f44336;
}

/* 波纹效果 */
.back-button__ripple {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.6);
  transform: scale(0);
  animation: back-button-ripple 0.6s linear;
  pointer-events: none;
  z-index: 1;
}

@keyframes back-button-ripple {
  to {
    transform: scale(4);
    opacity: 0;
  }
}

/* 工具提示 */
.back-button__tooltip {
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

.back-button:hover .back-button__tooltip {
  opacity: 1;
  transform: translateY(0);
}

.back-button__tooltip--top {
  bottom: 100%;
  left: 50%;
  transform: translateX(-50%) translateY(-8px);
}

.back-button:hover .back-button__tooltip--top {
  transform: translateX(-50%) translateY(-4px);
}

.back-button__tooltip--bottom {
  top: 100%;
  left: 50%;
  transform: translateX(-50%) translateY(8px);
}

.back-button:hover .back-button__tooltip--bottom {
  transform: translateX(-50%) translateY(4px);
}

.back-button__tooltip--left {
  right: 100%;
  top: 50%;
  transform: translateX(8px) translateY(-50%);
}

.back-button:hover .back-button__tooltip--left {
  transform: translateX(4px) translateY(-50%);
}

.back-button__tooltip--right {
  left: 100%;
  top: 50%;
  transform: translateX(-8px) translateY(-50%);
}

.back-button:hover .back-button__tooltip--right {
  transform: translateX(-4px) translateY(-50%);
}

/* 悬停和焦点状态 */
.back-button:not(.back-button--disabled):not(.back-button--loading):hover {
  transform: translateY(-1px);
}

.back-button:not(.back-button--disabled):not(.back-button--loading):active {
  transform: translateY(0);
}

.back-button:focus-visible {
  outline: 2px solid #1E88E5;
  outline-offset: 2px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .back-button--medium {
    padding: 6px 12px;
    min-height: 36px;
  }
  
  .back-button--large {
    padding: 8px 16px;
    min-height: 42px;
  }
  
  .back-button__text--hidden {
    display: none;
  }
}

@media (max-width: 480px) {
  .back-button--small {
    padding: 4px 8px;
    min-height: 28px;
  }
  
  .back-button--medium {
    padding: 4px 10px;
    min-height: 32px;
  }
  
  .back-button--large {
    padding: 6px 12px;
    min-height: 36px;
  }
}

/* 深色模式支持 */
@media (prefers-color-scheme: dark) {
  .back-button--text {
    color: #64b5f6;
  }
  
  .back-button--outline {
    color: #64b5f6;
    border-color: #64b5f6;
  }
  
  .back-button--ghost {
    color: #ccc;
  }
  
  .back-button--ghost:hover {
    background: rgba(255, 255, 255, 0.1);
  }
}

/* 打印样式 */
@media print {
  .back-button {
    display: none;
  }
}

.back-btn {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  background: var(--primary);
  color: white;
  margin-right: 15px;
  cursor: pointer;
  transition: all 0.3s;
}

.back-btn:hover {
  background: var(--primary-dark);
  transform: translateX(-3px);
}
</style>