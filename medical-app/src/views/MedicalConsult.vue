<template>
  <div class="medical-consult">
    <!-- 动态背景景物 -->
    <div class="scenery">
      <div class="cloud cloud1"></div>
      <div class="cloud cloud2"></div>
      <div class="tree tree1">
        <div class="tree-leaves"></div>
        <div class="tree-trunk"></div>
      </div>
      <div class="tree tree2">
        <div class="tree-leaves"></div>
        <div class="tree-trunk"></div>
      </div>
    </div>

    <div class="container">
      <!-- 侧边栏 -->
      <aside class="sidebar">
        <div class="sidebar-header">
          <h2>🤖 医疗智能助手</h2>
        </div>
        <div class="history-section">
          <div class="history-header">
            <h3>历史记录</h3>
            <button class="clear-history-btn" @click="clearHistory" title="清除所有记录">🗑️</button>
          </div>
          <ul class="history-list">
            <li 
              v-for="(item, index) in chatHistory" 
              :key="index"
              class="history-item"
              @click="loadHistory(item)"
            >
              {{ item.question }}
            </li>
          </ul>
        </div>
        <div class="quick-actions">
          <h3>快速提问</h3>
          <ul>
            <li 
              v-for="action in quickActions" 
              :key="action.question"
              @click="useQuickAction(action.question)"
            >
              {{ action.text }}
            </li>
          </ul>
        </div>
      </aside>

      <!-- 聊天主窗口 -->
      <main class="chat-window">
        <header class="chat-header">
          南昌本地智能医疗咨询助手
        </header>
        
        <div class="chat-messages" ref="messagesContainer">
          <!-- 欢迎界面 -->
          <div class="welcome-screen" v-if="showWelcome">
            <h3>👋 您好！我是您的智能医疗咨询助手</h3>
            <p>为了给您提供更精准的建议，请尽量详细描述您的症状，例如：</p>
            <div class="tips">
              <h4>高效描述小贴士：</h4>
              <ul>
                <li>具体部位：是"头疼"还是"左侧太阳穴疼"？</li>
                <li>持续时间：症状持续了多久？几分钟还是几天？</li>
                <li>伴随感觉：是否伴有发烧、咳嗽、乏力等？</li>
                <li>严重程度：如果10分是满分，您会给疼痛打几分？</li>
              </ul>
            </div>
          </div>
          
          <!-- 消息列表 -->
          <div 
            v-for="message in messages" 
            :key="message.id"
            class="message"
            :class="message.sender"
          >
            <div class="avatar">
              <img 
                :src="message.sender === 'user' ? patientAvatar : doctorAvatar" 
                :alt="message.sender === 'user' ? '用户头像' : '医生头像'"
              >
            </div>
            <div class="content">
              <!-- 生成式 UI：bot 消息按内容结构动态渲染（表格/警告卡片/分节） -->
              <BotMessage v-if="message.sender === 'bot'" :text="message.text" />
              <template v-else>{{ message.text }}</template>
            </div>
          </div>
        </div>
        
        <div class="chat-input-area">
          <input 
            type="text" 
            class="chat-input" 
            v-model="userInput"
            placeholder="请输入您的问题或症状..."
            @keyup.enter="sendMessage"
            ref="inputElement"
          >
          <button 
            class="voice-btn" 
            :class="{ 'recording': isRecording }"
            @click="toggleVoiceRecognition"
            title="语音输入"
          >
            {{ isRecording ? '🔴' : '🎤' }}
          </button>
          <button class="send-btn" @click="sendMessage" title="发送消息">
            ➤
          </button>
        </div>
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick } from 'vue'
import axios from 'axios'  // 引入axios
import BotMessage from '@/components/BotMessage.vue'

// 导入头像图片
import patientAvatar from '@/assets/images/patient.png'
import doctorAvatar from '@/assets/images/doctor.png'

// 响应式数据
const messages = ref([])
const chatHistory = ref([])
const userInput = ref('')
const isRecording = ref(false)
const messagesContainer = ref(null)
const inputElement = ref(null)
const memoryId = ref(null)  // 对话唯一标识
const API_BASE_URL = import.meta.env.VITE_CONSULT_API_BASE || '/xiaozhi'
const HISTORY_KEY = 'consultChatHistory'

// 计算属性
const showWelcome = computed(() => messages.value.length === 0)

// 快捷操作
const quickActions = ref([
  { text: '失眠怎么办？', question: '我最近总是失眠，怎么办？' },
  { text: '如何预防感冒？', question: '如何预防感冒？' },
  { text: '头疼的原因？', question: '头疼可能是什么原因？' }
])

// 语音识别
let recognition = null
if ('webkitSpeechRecognition' in window || 'SpeechRecognition' in window) {
  const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition
  recognition = new SpeechRecognition()
  recognition.lang = 'zh-CN'
  recognition.continuous = false
  recognition.interimResults = false

  recognition.onstart = () => {
    isRecording.value = true
  }

  recognition.onresult = (event) => {
    const transcript = event.results[0][0].transcript
    userInput.value = transcript
  }

  recognition.onerror = (event) => {
    console.error('语音识别错误:', event.error)
    alert('语音识别失败，请重试或使用文字输入。')
  }

  recognition.onend = () => {
    isRecording.value = false
  }
}

// 初始化：生成新的对话ID
onMounted(() => {
  if (!memoryId.value) {
    memoryId.value = Date.now()  // 用时间戳作为唯一标识
  }
  try {
    const cached = JSON.parse(localStorage.getItem(HISTORY_KEY) || '[]')
    chatHistory.value = Array.isArray(cached) ? cached : []
  } catch (error) {
    console.warn('读取聊天历史失败，已重置', error)
    chatHistory.value = []
  }
  if (inputElement.value) {
    inputElement.value.focus()
  }
})

// // 发送消息到后端（对接真实接口）
// const sendMessage = async () => {
//   const message = userInput.value.trim()
//   if (!message) return

//   // 添加用户消息到前端列表
//   messages.value.push({
//     id: Date.now(),
//     sender: 'user',
//     text: message,
//     timestamp: new Date()
//   })

//   // 保存到本地历史记录
//   chatHistory.value.unshift({
//     question: message,
//     timestamp: new Date(),
//     memoryId: memoryId.value  // 关联对话ID
//   })

//   // 清空输入框
//   userInput.value = ''
//   scrollToBottom()

  

//   try {
//     // 调用后端聊天接口（流式响应）
//     const response = await axios.post(
//   `${API_BASE_URL}/chat`,
//   { memoryId: memoryId.value, message: message },
//   { 
//     responseType: 'text',  // 浏览器支持的text类型
//     headers: {
//       'Content-Type': 'application/json'  // 显式指定请求体格式
//     }
//   }
//  )

//     // 处理流式返回的AI回复
//     let aiReply = ''
//     const reader = response.data.getReader()
//     const decoder = new TextDecoder()

//     while (true) {
//       const { done, value } = await reader.read()
//       if (done) break
      
//       // 累加流式数据
//       aiReply += decoder.decode(value, { stream: true })
//       // 更新前端AI消息
//       const lastMessage = messages.value[messages.value.length - 1]
//       if (lastMessage?.sender === 'bot') {
//         lastMessage.text = aiReply
//       } else {
//         messages.value.push({
//           id: Date.now() + 1,
//           sender: 'bot',
//           text: aiReply,
//           timestamp: new Date()
//         })
//       }
//       scrollToBottom()
//     }
//   } catch (error) {
//     console.error('发送消息失败：', error)
//     messages.value.push({
//       id: Date.now() + 2,
//       sender: 'bot',
//       text: '抱歉，消息发送失败，请重试',
//       timestamp: new Date()
//     })
//   }
// }

const sendMessage = async () => {
  const message = userInput.value.trim();
  if (!message) return;

  messages.value.push({
    id: Date.now(),
    sender: 'user',
    text: message
  });
  userInput.value = '';
  chatHistory.value.unshift({
    question: message,
    timestamp: new Date().toISOString(),
    memoryId: memoryId.value
  })
  try {
    localStorage.setItem(HISTORY_KEY, JSON.stringify(chatHistory.value.slice(0, 20)))
  } catch (error) {
    console.warn('保存聊天历史失败:', error)
  }
  scrollToBottom();

  // 先占位一条 bot 消息，流式追加
  const botMessage = {
    id: Date.now() + 1,
    sender: 'bot',
    text: ''
  };
  messages.value.push(botMessage);
  scrollToBottom();

  try {
    // fetch + ReadableStream 实现真流式渲染（逐 token 更新，生成式 UI 同步重组）
    const response = await fetch(`${API_BASE_URL}/chat`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ memoryId: memoryId.value, message: message })
    });

    if (!response.ok || !response.body) {
      throw new Error('请求失败: ' + response.status);
    }

    const reader = response.body.getReader();
    const decoder = new TextDecoder('utf-8');
    while (true) {
      const { done, value } = await reader.read();
      if (done) break;
      botMessage.text += decoder.decode(value, { stream: true });
      scrollToBottom();
    }
    botMessage.text += decoder.decode();

  } catch (error) {
    console.error('发送消息失败：', error);
    botMessage.text = botMessage.text || '抱歉，消息发送失败，请重试';
  }
};
// 快速提问
const useQuickAction = (question) => {
  userInput.value = question
  sendMessage()
}

// 加载历史记录（对接后端接口）
const loadHistory = async (historyItem) => {
  try {
    const res = await axios.get(`${API_BASE_URL}/history/${historyItem.memoryId}`)
    // 解析历史记录并更新前端消息列表
    messages.value = res.data.map(item => ({
      id: item.id,
      sender: item.sender,
      text: item.text,
      timestamp: item.timestamp
    }))
    memoryId.value = historyItem.memoryId  // 切换当前对话ID
  } catch (error) {
    console.error('加载历史记录失败：', error)
    alert('加载历史记录失败，请重试')
  }
}

// 清除历史记录（对接后端接口）
const clearHistory = async () => {
  if (confirm('确定要清除所有聊天记录吗？')) {
    try {
      if (memoryId.value) {
        await axios.delete(`${API_BASE_URL}/history/${memoryId.value}`)
      }
      messages.value = []
      chatHistory.value = []
      try {
        localStorage.removeItem(HISTORY_KEY)
      } catch (error) {
        console.warn('清除聊天历史存储失败:', error)
      }
    } catch (error) {
      console.error('清除历史记录失败：', error)
      alert('清除历史记录失败，请重试')
    }
  }
}

// 语音识别切换
const toggleVoiceRecognition = () => {
  if (recognition) {
    if (isRecording.value) {
      recognition.stop()
    } else {
      recognition.start()
    }
  } else {
    alert('您的浏览器不支持语音识别功能')
  }
}

// 滚动到底部
const scrollToBottom = () => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  })
}
</script>

<style scoped>
/* --- 全局与变量 --- */
:root {
  --primary-color: #4a90e2;
  --secondary-color: #7fba00;
  --bg-color: #e6f7ff;
  --card-bg: rgba(255, 255, 255, 0.85);
  --text-color: #333;
  --shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.15);
}

.medical-consult {
  height: 100vh;
  overflow: hidden;
  position: relative;
  background-color: var(--bg-color);
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', sans-serif;
}

/* --- 动态背景景物 --- */
.scenery {
  position: absolute;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 1;
}

.cloud {
  position: absolute;
  background: white;
  border-radius: 100px;
  opacity: 0.7;
}

.cloud::before,
.cloud::after {
  content: '';
  position: absolute;
  background: white;
  border-radius: 100px;
}

.cloud1 {
  width: 100px;
  height: 40px;
  top: 20%;
  animation: float 25s infinite;
}

.cloud1::before {
  width: 50px;
  height: 50px;
  top: -25px;
  left: 10px;
}

.cloud1::after {
  width: 60px;
  height: 40px;
  top: -15px;
  right: 10px;
}

.cloud2 {
  width: 80px;
  height: 35px;
  top: 40%;
  animation: float 35s infinite;
  animation-delay: -5s;
}

.cloud2::before {
  width: 40px;
  height: 40px;
  top: -20px;
  left: 15px;
}

.cloud2::after {
  width: 50px;
  height: 35px;
  top: -10px;
  right: 15px;
}

@keyframes float {
  from {
    transform: translateX(-150px);
  }
  to {
    transform: translateX(calc(100vw + 150px));
  }
}

.tree {
  position: absolute;
  bottom: 0;
  width: 150px;
  height: 200px;
  z-index: 2;
}

.tree-trunk {
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 30px;
  height: 80px;
  background: #8B4513;
  border-radius: 0 0 5px 5px;
}

.tree-leaves {
  position: absolute;
  bottom: 60px;
  left: 50%;
  transform: translateX(-50%);
  width: 0;
  height: 0;
  border-left: 60px solid transparent;
  border-right: 60px solid transparent;
  border-bottom: 120px solid #228B22;
  animation: sway 4s ease-in-out infinite;
  transform-origin: bottom center;
}

@keyframes sway {
  0%, 100% { transform: translateX(-50%) rotate(-2deg); }
  50% { transform: translateX(-50%) rotate(2deg); }
}

.tree1 { left: -50px; }
.tree2 { right: -50px; animation-delay: -2s; }
.tree2 .tree-leaves { animation-delay: -2s; }

/* --- 主容器布局 --- */
.container {
  display: flex;
  height: 100vh;
  position: relative;
  z-index: 10;
}

/* --- 侧边栏 --- */
.sidebar {
  width: 280px;
  background: var(--card-bg);
  backdrop-filter: blur(10px);
  border-right: 1px solid rgba(255, 255, 255, 0.3);
  display: flex;
  flex-direction: column;
  box-shadow: var(--shadow);
}

.sidebar-header {
  padding: 20px;
  text-align: center;
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
}

.sidebar-header h2 {
  font-size: 1.2rem;
  color: var(--primary-color);
}

.history-section {
  flex-grow: 1;
  padding: 15px;
  overflow-y: auto;
}

.history-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.history-header h3 {
  font-size: 0.9rem;
  color: #666;
}

.clear-history-btn {
  background: none;
  border: none;
  color: #999;
  cursor: pointer;
  font-size: 1.2rem;
  padding: 5px;
  border-radius: 50%;
  transition: background-color 0.2s, color 0.2s;
}

.clear-history-btn:hover {
  background-color: #f0f0f0;
  color: #ff4d4f;
}

.history-list {
  list-style: none;
}

.history-item {
  padding: 10px;
  margin-bottom: 8px;
  background: rgba(0, 0, 0, 0.03);
  border-radius: 8px;
  cursor: pointer;
  font-size: 0.9rem;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  transition: background-color 0.2s;
}

.history-item:hover {
  background-color: rgba(74, 144, 226, 0.1);
}

.quick-actions {
  padding: 15px;
  border-top: 1px solid rgba(0, 0, 0, 0.05);
}

.quick-actions h3 {
  font-size: 0.9rem;
  margin-bottom: 10px;
  color: #2e7d32; /* 改为深绿色 */
}

.quick-actions ul {
  list-style: none;
}

.quick-actions li {
  padding: 10px;
  margin-bottom: 8px;
  background: #4caf50; /* 改为绿色背景 */
  color: white;
  text-align: center;
  border-radius: 8px;
  cursor: pointer;
  font-size: 0.9rem;
  transition: transform 0.2s, box-shadow 0.2s;
}

.quick-actions li:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(76, 175, 80, 0.3); /* 改为绿色阴影 */
  background: #43a047; /* 悬停时加深绿色 */
}

/* --- 聊天主窗口 --- */
.chat-window {
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  background: var(--card-bg);
  backdrop-filter: blur(10px);
  margin: 20px;
  border-radius: 15px;
  box-shadow: var(--shadow);
  overflow: hidden;
   border-bottom: 1px solid #e0e0e0; /* 添加底部边框 */
}

.chat-header {
  padding: 20px;
 background: white; /* 改为白色背景 */
  color: #4a90e2; /* 改为蓝色文字 */
  text-align: center;
  font-size: 1.2rem;
  font-weight: bold;
}

.chat-messages {
  flex-grow: 1;
  padding: 20px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 15px;
}

/* --- 欢迎界面 --- */
.welcome-screen {
  text-align: center;
  padding: 40px 20px;
  color: #666;
}

.welcome-screen h3 {
  font-size: 1.5rem;
  color: var(--primary-color);
  margin-bottom: 15px;
}

.welcome-screen p {
  font-size: 1rem;
  line-height: 1.6;
  margin-bottom: 20px;
}

.welcome-screen .tips {
  background: rgba(74, 144, 226, 0.1);
  border-left: 4px solid var(--primary-color);
  padding: 15px;
  text-align: left;
  border-radius: 5px;
}

.welcome-screen .tips h4 {
  margin-bottom: 10px;
}

.welcome-screen .tips ul {
  list-style-position: inside;
  line-height: 1.8;
}

/* --- 消息样式 --- */
.message {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  animation: fadeIn 0.4s ease-out;
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

.message.user {
  flex-direction: row-reverse;
}

.message .avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  flex-shrink: 0;
  overflow: hidden;
  border: 2px solid #fff;
  box-shadow: 0 2px 5px rgba(0,0,0,0.1);
}

.message .avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.message .content {
  max-width: 70%;
  padding: 12px 16px;
  border-radius: 18px;
  line-height: 1.5;
  position: relative;
}

.message.user .content {
  background-color: var(--primary-color);
  color: rgb(26, 139, 204);
  border-bottom-right-radius: 5px;
}

.message.bot .content {
  background-color: lab(97.36% -0.78 -1.91);
  color: var(--text-color);
  border-bottom-left-radius: 5px;
}

/* --- 输入区域 --- */
.chat-input-area {
  display: flex;
  align-items: center;
  padding: 15px;
  border-top: 1px solid rgba(0, 0, 0, 0.05);
  gap: 10px;
}

.chat-input {
  flex-grow: 1;
  border: 1px solid #ccc;
  border-radius: 25px;
  padding: 12px 20px;
  font-size: 1rem;
  outline: none;
  transition: border-color 0.3s;
}

.chat-input:focus {
  border-color: var(--primary-color);
}

.voice-btn, .send-btn {
  background: none;
  border: none;
  width: 45px;
  height: 45px;
  border-radius: 50%;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.2rem;
  color: white;
  transition: background-color 0.3s, transform 0.2s;
}

.voice-btn {
  background-color: #f0f0f0;
  color: #666;
}

.voice-btn.recording {
  background-color: var(--secondary-color);
  color: white;
  animation: pulse 1.5s infinite;
}

@keyframes pulse {
  0% { box-shadow: 0 0 0 0 rgba(127, 186, 0, 0.7); }
  70% { box-shadow: 0 0 0 10px rgba(127, 186, 0, 0); }
  100% { box-shadow: 0 0 0 0 rgba(127, 186, 0, 0); }
}

.send-btn {
  background-color: var(--primary-color);
}

.send-btn:hover {
  transform: scale(1.05);
}

.send-btn:active {
  transform: scale(0.95);
}

/* --- 响应式设计 --- */
@media (max-width: 768px) {
  .sidebar {
    position: absolute;
    top: 0;
    left: 0;
    height: 100%;
    transform: translateX(-100%);
    z-index: 20;
  }
  
  .sidebar.open {
    transform: translateX(0);
  }
  
  .chat-window {
    margin: 10px;
  }
  
  .message .content {
    max-width: 85%;
  }
}
</style>