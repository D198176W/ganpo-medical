<template>
  <div class="pathology-container">
    <!-- 背景图片 -->
    <div class="bg-container">
      <!-- <img src="@/assets/images/camera.png" alt="病理服务背景" class="bg-img"> -->
    </div>

    <!-- 顶部导航栏 -->
    <div class="nav-bar">
      <div class="nav-left">
        <div class="back-btn" @click="navigateBack">
          <i class="fas fa-arrow-left"></i>
        </div>
        <div class="title">病理知识服务</div>
      </div>
      <!-- 医生认证 -->
      <div class="auth-status" :class="{ verified: isVerifiedDoctor }" @click.stop="toggleAuthMenu">
        <span>👨‍⚕️</span>
        <span>{{ authText }}</span>
        <div class="auth-menu" v-if="showAuthMenu">
          <div class="auth-menu-item" @click="handleAuth">认证管理</div>
          <div class="auth-menu-item" @click="handleLogout" v-if="isVerifiedDoctor">退出认证</div>
        </div>
      </div>
    </div>

    <!-- 内容区域 -->
    <div class="content">
      <!-- 病理问答选项卡 -->
      <div class="tab-content" :class="{ active: currentTab === 'qaTab' }">
        <div class="function-card pathology-qa">
          <div class="card-title">
            <span class="card-title-icon">❓</span>
            <span>病理问答</span>
          </div>
          
          <!-- 清空聊天记录按钮 -->
          <div class="chat-actions">
            <button class="clear-chat-btn" @click="clearChatHistory">清空聊天记录</button>
          </div>
          
          <!-- 问答历史记录 -->
          <div class="chat-container" id="chatContainer">
            <div class="empty-state" v-if="chatHistory.length === 0">
              <div class="empty-icon">💬</div>
              <div>暂无对话记录</div>
              <div class="empty-state-hint">开始提问，获取专业病理解答</div>
            </div>
            <div class="message-item" v-for="item in chatHistory" :key="item.id">
              <div class="user-msg" v-if="item.type === 'user'">
                <div class="user-bubble">
                  <span>{{ item.content }}</span>
                  <span class="time">{{ item.time }}</span>
                </div>
              </div>
              <div class="ai-msg" v-else>
                <div class="ai-bubble">
                  <span v-html="formatMessageContent(item.content)"></span>
                  <span class="source" v-if="item.source" @click="openSource(item.source)">{{ item.sourceLabel || ('参考来源：' + item.source) }}</span>
                  <span class="time">{{ item.time }}</span>
                </div>
              </div>
            </div>
          </div>
          
          <!-- 提问输入区 -->
          <div class="question-input-area">
            <input 
              class="question-input" 
              placeholder="请输入您的病理问题（如：鳞状细胞癌组织学特征）" 
              v-model="currentQuestion"
              @keyup.enter="submitQuestion"
            />
            <button class="send-btn" @click="submitQuestion" :disabled="!currentQuestion || isLoading">
              {{ isLoading ? '发送中...' : '发送' }}
            </button>
          </div>
          
          <!-- 历史记录区域 -->
          <div class="history-section">
            <div class="history-title">
              <span>📚</span>
              <span>历史记录与推荐问题</span>
            </div>
            <div class="history-list">
              <div class="history-item" v-for="(question, index) in allQuestions" :key="index"
                   @click="selectQuestion(question)">{{ question }}</div>
            </div>
          </div>
        </div>
      </div>

      <!-- 图片分析选项卡 -->
      <div class="tab-content" :class="{ active: currentTab === 'imageTab' }">
        <div class="function-card image-analysis">
          <div class="card-title">
            <span class="card-title-icon">📷</span>
            <span>病理图片分析</span>
          </div>
          
          <!-- 图片上传区 -->
          <div class="upload-container">
            <div class="upload-area" @click="chooseImage">
              <div class="upload-icon">📁</div>
              <div class="upload-text">点击上传病理切片图片</div>
              <div class="upload-hint">支持拍照或从相册选择（请上传清晰切片图）</div>
            </div>
            
            <div class="auth-badge">
          <div class="badge" @click="handleAuthBadgeClick">
            <span>{{ isVerifiedDoctor ? '🔓' : '🔒' }}</span>
            <span>{{ isVerifiedDoctor ? '已认证 · 可批量上传' : '点击进行医生认证' }}</span>
          </div>
        </div>
        
        <!-- 测试按钮：直接显示鳞状细胞癌分析结果 -->
        <!-- <div class="test-analysis-btn-container" style="margin-top: 12px; text-align: center;">
          <button class="test-analysis-btn" @click="showSquamousCellCarcinomaResult" style="background: linear-gradient(135deg, #ff9800, #f57c00); color: white; border: none; padding: 10px 20px; border-radius: 20px; font-size: 14px; cursor: pointer; transition: all 0.3s ease;">
            <span>🔬</span> 测试鳞状细胞癌分析结果
          </button>
          <div style="font-size: 12px; color: #7f8c8d; margin-top: 4px;">点击查看预设的鳞状细胞癌AI诊断结果</div>
        </div> -->
          </div>
          
          <!-- 上传提示 -->
          <div class="uploading-tip" :class="{ show: showUploadTip }">
            <div class="loading-icon">⏳</div>
            <span>正在分析图片，请稍候...</span>
          </div>
          
          <!-- 图片预览区域 -->
          <div class="image-preview-section" :class="{ show: uploadedImages.length > 0 }">
            <div class="preview-header">
              <div class="preview-title">已上传图片</div>
              <button class="toggle-preview" @click="togglePreview">
                <span>{{ isPreviewExpanded ? '收起' : '展开' }}</span>
                <span>{{ isPreviewExpanded ? '▲' : '▼' }}</span>
              </button>
            </div>
            <div class="preview-container" :class="{ collapsed: !isPreviewExpanded }">
              <div class="preview-item" v-for="image in uploadedImages" :key="image.id">
                <img :src="image.url" :alt="image.name" class="preview-image">
                <div class="preview-actions">
                  <button class="preview-btn view-btn" @click.stop="viewImage(image)">👁️ 查看</button>
                  <button class="preview-btn delete-btn" @click.stop="deleteImage(image.id)">🗑️ 删除</button>
                </div>
              </div>
            </div>
          </div>
          
          <!-- 分析结果侧边栏 -->
          <div class="analysis-sidebar" :class="{ show: showAnalysisResult }" :style="{ height: sidebarHeight + 'px' }">
            <div class="sidebar-header" @mousedown="startResize" @touchstart="startResize">
              <div class="result-title">分析结果</div>
              <div class="auxiliary-tag">AI辅助诊断</div>
              <div class="drag-handle">拖拽调整</div>
            </div>
            
            <div class="sidebar-content" :style="{ height: (sidebarHeight - 60) + 'px' }">
              <div class="result-section conclusion">
                <span class="result-subtitle">初步结论：</span>
                <span>{{ analysisResult.conclusion }}</span>
              </div>
              
              <div class="result-section features" v-if="analysisResult.features && analysisResult.features.length">
                <span class="result-subtitle">细胞特征：</span>
                <div>
                  <div class="feature-item" v-for="(feature, index) in analysisResult.features" :key="index">
                    <span class="bullet">•</span><span>{{ feature }}</span>
                  </div>
                </div>
              </div>
              
              <div class="result-section reference" v-if="analysisResult.referenceCases && analysisResult.referenceCases.length">
                <span class="result-subtitle">参考案例：</span>
                <div>
                  <div class="reference-item" v-for="(caseItem, index) in analysisResult.referenceCases" :key="index">
                    <span>{{ caseItem }}</span>
                  </div>
                </div>
              </div>
              
              <div class="result-section confidence" v-if="analysisResult.confidence">
                <span class="result-subtitle">可信度：</span>
                <span>{{ analysisResult.confidence }}</span>
              </div>
              
              <div class="result-section suggestion" v-if="analysisResult.suggestion">
                <span class="result-subtitle">建议：</span>
                <span>{{ analysisResult.suggestion }}</span>
              </div>
            </div>
          </div>
          
          <!-- 隐藏的文件输入 -->
          <input type="file" class="file-input" ref="fileInput" accept="image/*" :multiple="isVerifiedDoctor" @change="handleFileUpload">

          <!-- 隐藏的证书上传输入 -->
          <input type="file" ref="certInput" accept="image/*" style="display:none" @change="handleCertFileChange" />
        </div>
      </div>

      <!-- 知识库选项卡 -->
      <div class="tab-content" :class="{ active: currentTab === 'knowledgeTab' }">
        <div class="function-card knowledge-base">
          <div class="card-title">
            <span class="card-title-icon">📖</span>
            <span>病理知识库</span>
          </div>
          
          <div class="knowledge-categories">
            <div class="category-item" v-for="category in knowledgeCategories" :key="category.id"
                 @click="viewKnowledgeCategory(category.id, category.name)">
              <div class="category-icon">{{ category.icon }}</div>
              <div class="category-name">{{ category.name }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 底部导航 -->
    <div class="bottom-nav">
      <div class="nav-item" :class="{ active: currentTab === 'qaTab' }" @click="switchTab('qaTab')">
        <div class="nav-icon">❓</div>
        <div class="nav-text">病理问答</div>
      </div>
      <div class="nav-item" :class="{ active: currentTab === 'imageTab' }" @click="switchTab('imageTab')">
        <div class="nav-icon">📷</div>
        <div class="nav-text">图片分析</div>
      </div>
      <div class="nav-item" :class="{ active: currentTab === 'knowledgeTab' }" @click="switchTab('knowledgeTab')">
        <div class="nav-icon">📖</div>
        <div class="nav-text">知识库</div>
      </div>
    </div>
    
    <!-- 知识库详情模态框 -->
    <div class="knowledge-modal-mask" v-if="showKnowledgeModal" style="position: fixed; top: 0; left: 0; right: 0; bottom: 0; background-color: rgba(0, 0, 0, 0.5); display: flex; align-items: center; justify-content: center; z-index: 9999; overflow: hidden;">
      <div class="knowledge-modal" style="width: 90%; max-width: 600px; max-height: 80vh; background-color: #fff; border-radius: 16px; box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2); overflow: hidden;">
        <div class="knowledge-modal-header" style="padding: 20px; background: linear-gradient(135deg, #1E88E5, #1565C0); color: white; display: flex; justify-content: space-between; align-items: center;">
          <h2 class="knowledge-modal-title" style="font-size: 18px; font-weight: 700; margin: 0;">{{ currentKnowledgeTitle }}</h2>
          <button class="close-knowledge-modal" @click="closeKnowledgeModal" style="background: none; border: none; color: white; font-size: 24px; cursor: pointer; padding: 0; width: 30px; height: 30px; display: flex; align-items: center; justify-content: center; border-radius: 50%; transition: background-color 0.2s;">×</button>
        </div>
        <div class="knowledge-content" v-html="currentKnowledgeContent" style="padding: 20px; max-height: 60vh; overflow-y: auto; line-height: 1.6;"></div>
      </div>
    </div>
    
    <!-- 图片查看模态框 -->
    <div class="image-viewer" v-if="showImageViewer" @click="closeImageViewer">
      <div class="image-container" @click.stop>
        <img :src="currentImageUrl" :alt="currentImageName" class="view-image">
        <button @click="closeImageViewer">×</button>
      </div>
    </div>
    
    <!-- 认证模态框 -->
    <div class="auth-modal-mask" :class="{ show: showAuthModal }" v-if="showAuthModal">
      <div class="auth-modal">
        <div class="modal-title">医疗从业者身份验证</div>
        <div class="modal-content">
          <div class="form-item">
            <span class="label">医师资格证号</span>
            <input type="text" placeholder="请输入医师资格证号" class="input" v-model="authForm.certId" />
          </div>
          <div class="form-item">
            <span class="label">所在医疗机构</span>
            <input type="text" placeholder="请输入所在医疗机构" class="input" v-model="authForm.hospital" />
          </div>
          <div class="form-item">
            <span class="label">上传资格证照片</span>
            <div class="upload-cert-area" @click="chooseCertImage">
              <div class="upload-icon">📁</div>
              <span class="upload-text">{{ certFile ? certFile.name : '点击上传资格证照片' }}</span>
            </div>
          </div>
        </div>
        <div class="modal-btns">
          <button class="cancel-btn" @click="closeAuthDialog">取消</button>
          <button class="confirm-btn" @click="submitAuth" :disabled="authSubmitting">
            {{ authSubmitting ? '提交中...' : '提交验证' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      currentTab: 'qaTab',
      showAuthMenu: false,
      isVerifiedDoctor: false,
      doctorName: "",

      // 用户信息与会话
      userId: 1, // 默认用户ID
      sessionId: null,
      
      // 聊天相关
      chatHistory: [],
      currentQuestion: '',
      isLoading: false,
      
      // 图片分析相关
      uploadedImages: [],
      showUploadTip: false,
      showAnalysisResult: false,
      isPreviewExpanded: true,
      sidebarHeight: 300,
      isResizing: false,
      
      // 历史记录和推荐问题
      historyQuestions: [
        "鳞状细胞癌的组织学特征有哪些？",
        "如何区分良恶性肿瘤的病理表现？",
        "乳腺癌的免疫组化标记物有哪些？",
        "胃肠道间质瘤的病理诊断标准",
        "甲状腺乳头状癌的细胞学特征"
      ],
      
      recommendedQuestions: [
        "肺癌的病理分型及特点",
        "前列腺癌的Gleason评分系统",
        "淋巴瘤的WHO分类标准",
        "黑色素瘤的病理诊断要点"
      ],
      
      // 分析结果
      analysisResult: {
        conclusion: '',
        features: [],
        referenceCases: [],
        confidence: '',
        suggestion: ''
      },
      
      // 知识库
      knowledgeCategories: [
        { id: 'tumor', name: '肿瘤病理', icon: '🔬' },
        { id: 'tissue', name: '组织病理', icon: '🧫' },
        { id: 'cell', name: '细胞病理', icon: '🔍' },
        { id: 'molecular', name: '分子病理', icon: '🧬' },
        { id: 'infectious', name: '感染性病理', icon: '🦠' },
        { id: 'dermatopathology', name: '皮肤病理', icon: '👨‍⚕️' },
        { id: 'neuropathology', name: '神经病理', icon: '🧠' },
        { id: 'hematopathology', name: '血液病理', icon: '🩸' }
      ],
      
      showKnowledgeModal: false,
      currentKnowledgeTitle: '',
      currentKnowledgeContent: '',
      
      showImageViewer: false,
      currentImageUrl: '',
      currentImageName: '',
      
      showAuthModal: false,
      authForm: {
        certId: '',
        hospital: ''
      },

      certFile: null,
      authSubmitting: false
    };
  },
  computed: {
    allQuestions() {
      return [
        ...this.historyQuestions.slice(0, 3),
        ...this.recommendedQuestions.slice(0, 2)
      ];
    },
    authText() {
      return this.isVerifiedDoctor ? `已认证 · ${this.doctorName}` : '医生认证';
    }
  },
  methods: {
    readLocalStorageJson(key, fallback) {
      try {
        const raw = localStorage.getItem(key);
        if (!raw) return fallback;
        const parsed = JSON.parse(raw);
        return parsed ?? fallback;
      } catch (error) {
        console.warn(`读取本地存储失败: ${key}`, error);
        return fallback;
      }
    },
    navigateBack() {
      if (this.$router && this.$router.go) {
        this.$router.go(-1);
      }
    },
    toggleAuthMenu() {
      this.showAuthMenu = !this.showAuthMenu;
    },
    handleAuth() {
      this.showAuthModal = true;
      this.showAuthMenu = false;
    },
    handleAuthBadgeClick() {
      // 点击认证徽章时的处理
      console.log('点击认证徽章，当前认证状态:', this.isVerifiedDoctor);
      if (!this.isVerifiedDoctor) {
        // 未认证用户，直接弹出认证模态框
        console.log('未认证，显示认证模态框');
        this.showAuthModal = true;
      } else {
        // 已认证用户，显示菜单
        console.log('已认证，显示菜单');
        this.showAuthMenu = !this.showAuthMenu;
      }
    },
    async handleLogout() {
      if (confirm('确定要退出医生认证吗？')) {
        // 可调用后端登出接口（如果有）
        this.isVerifiedDoctor = false;
        this.doctorName = '';
        this.showAuthMenu = false;
        // 更新本地 userInfo
        try {
          const userInfo = this.readLocalStorageJson('userInfo', {});
          userInfo.userTag = 'user';
          userInfo.nickName = '';
          localStorage.setItem('userInfo', JSON.stringify(userInfo));
        } catch (e) { /* ignore */ }
        alert('已退出医生认证');
      }
    },
    switchTab(tabId) {
      this.currentTab = tabId;
    },

    // ---------- 聊天：调用后端 /api/chat ----------
    async submitQuestion() {
      const question = this.currentQuestion.trim();
      if (!question) return;

      // 添加到历史问题（本地列表）
      if (!this.historyQuestions.includes(question)) {
        this.historyQuestions.unshift(question);
        if (this.historyQuestions.length > 10) {
          this.historyQuestions.pop();
        }
      }

      // 添加用户消息到本地 UI
      const userMsg = {
        id: Date.now(),
        type: 'user',
        content: question,
        time: new Date().toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'})
      };
      this.chatHistory.push(userMsg);
      this.currentQuestion = '';
      this.isLoading = true;
      this.scrollToBottom();

      // 准备请求 payload（与后端 ChatRequestDTO 对齐：userId, role, query, sessionId）

      // 确保 userId 和 sessionId 已初始化
      if (!this.userId) {
        const userInfo = this.readLocalStorageJson('userInfo', {});
        this.userId = userInfo.userId || userInfo.id || Date.now();
      }
      if (!this.sessionId) {
        this.sessionId = this.generateSessionId();
        try {
          localStorage.setItem('pathologySessionId', this.sessionId);
        } catch (error) {
          console.warn('保存sessionId失败:', error);
        }
      }

      const payload = {
        userId: this.userId,
        role: this.isVerifiedDoctor ? 'doctor' : 'user',
        query: question,
        sessionId: this.sessionId
      };

      try {
        // 发送流式请求
        const controller = new AbortController();
        // 设置超时
        const timeoutId = setTimeout(() => controller.abort(), 30000); // 30秒超时
        
        const response = await fetch('/api/chat/stream', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(payload),
          signal: controller.signal
        });
        
        clearTimeout(timeoutId);

        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }

        // 处理流式响应
        const reader = response.body.getReader();
        const decoder = new TextDecoder();
        let answer = '';
        const aiMsgId = Date.now() + 1;
        
        // 先添加一个空的AI消息，后续会更新内容
        const aiMsg = {
          id: aiMsgId,
          type: 'ai',
          content: '',
          source: 'AI 分析',
          time: new Date().toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'})
        };
        this.chatHistory.push(aiMsg);
        this.scrollToBottom();

        // 流式读取响应
        while (true) {
          const { done, value } = await reader.read();
          if (done) {
            break;
          }
          
          const chunk = decoder.decode(value, { stream: true });
          answer += chunk;
          
          // 更新AI消息的内容
          const msgIndex = this.chatHistory.findIndex(msg => msg.id === aiMsgId);
          if (msgIndex !== -1) {
            this.chatHistory[msgIndex].content = answer;
            // 触发视图更新
            this.$forceUpdate();
            this.scrollToBottom();
          }
        }
      } catch (err) {
        console.error('聊天接口出错：', err);
        // 移除最后添加的空AI消息（如果存在）
        const lastMsg = this.chatHistory[this.chatHistory.length - 1];
        if (lastMsg && lastMsg.type === 'ai' && !lastMsg.content) {
          this.chatHistory.pop();
        }
        alert('提交问题失败，请稍后重试');
      } finally {
        this.isLoading = false;
        this.scrollToBottom();
      }
    },

    scrollToBottom() {
      this.$nextTick(() => {
        const chatContainer = document.getElementById('chatContainer');
        if (chatContainer) {
          chatContainer.scrollTop = chatContainer.scrollHeight;
        }
      });
    },

    clearChatHistory() {
      if (confirm('确定要清空所有聊天记录吗？此操作不可撤销。')) {
        this.chatHistory = [];
        try {
          localStorage.removeItem('pathologyChatHistory');
        } catch (error) {
          console.warn('清空聊天记录存储失败:', error);
        }
        alert('聊天记录已清空');
      }
    },

    // ---------- 图片上传与分析 ----------
    chooseImage() {
      // 非认证用户只能传一张
      if (!this.isVerifiedDoctor && this.uploadedImages.length >= 1) {
        alert('未认证用户只能上传一张图片，请先完成医生认证');
        return;
      }
      this.$refs.fileInput.click();
    },

    async handleFileUpload(e) {
      const files = e.target.files;
      if (files && files.length) {
        let fileArray = Array.from(files);

        // 检查文件数量限制（未认证用户只允许一张）
        if (!this.isVerifiedDoctor && fileArray.length > 1) {
          alert('未认证用户只能单张上传图片');
          fileArray = fileArray.slice(0, 1);
        }

        for (let i = 0; i < fileArray.length; i++) {
          const file = fileArray[i];

          // 基本校验
          if (!file.type.startsWith('image/')) {
            alert(`文件 "${file.name}" 不是图片格式，请选择图片文件`);
            continue;
          }
          if (file.size > 10 * 1024 * 1024) {
            alert(`文件 "${file.name}" 大小超过10MB限制`);
            continue;
          }

          try {
            // 先显示本地预览
            const reader = new FileReader();
            const localUrl = await new Promise((res) => {
              reader.onload = (ev) => res(ev.target.result);
              reader.readAsDataURL(file);
            });

            // 将本地预览先放到列表（id 为临时 id，上传成功后可替换）
            const tempId = 'temp-' + Date.now() + '-' + i;
            const newImageTemp = {
              id: tempId,
              name: file.name,
              url: localUrl,
              uploading: true
            };
            this.uploadedImages.push(newImageTemp);

            this.showUploadTip = true;
            
            // 模拟图片上传成功和分析结果
            setTimeout(() => {
              // 构建图片对象
              const finalImage = {
                id: 'img-' + Date.now(),
                name: file.name,
                url: localUrl
              };

              // 替换临时项（使用展开运算符创建新数组确保响应式更新）
              const idx = this.uploadedImages.findIndex(it => it.id === tempId);
              if (idx !== -1) {
                this.uploadedImages = [...this.uploadedImages.slice(0, idx), finalImage, ...this.uploadedImages.slice(idx + 1)];
              } else {
                this.uploadedImages.push(finalImage);
              }

              // 检测文件名是否包含"鳞状细胞癌"关键词，提供预设分析结果
              const fileNameLower = file.name.toLowerCase();
              const isSquamousCellCarcinoma = fileNameLower.includes('鳞状') || 
                                               fileNameLower.includes('scc') ||
                                               fileNameLower.includes('carcinoma') ||
                                               fileNameLower.includes('癌') ||
                                               fileNameLower.includes('肿瘤');
              
              console.log('文件分析：文件名="' + file.name + '", 包含关键词=' + isSquamousCellCarcinoma);
              
              if (isSquamousCellCarcinoma) {
                // 鳞状细胞癌预设分析结果
                this.analysisResult = {
                  conclusion: '鳞状细胞癌（Squamous Cell Carcinoma）',
                  features: [
                    '角化珠形成：可见典型角化珠（癌珠），为同心圆排列的角化细胞',
                    '细胞间桥：肿瘤细胞间可见细胞间桥连接',
                    '细胞异型性：细胞核大、深染，核浆比例增高',
                    '核分裂象：可见病理性核分裂象',
                    '浸润性生长：癌组织呈浸润性生长，边界不清',
                    '细胞巢状排列：癌细胞呈巢状、团块状排列'
                  ],
                  referenceCases: [
                    '皮肤鳞状细胞癌 - 高分化型',
                    '食管鳞状细胞癌 - 中分化型',
                    '宫颈鳞状细胞癌 - 低分化型'
                  ],
                  confidence: '92%',
                  suggestion: '建议结合免疫组化检查（如CK5/6、p40阳性）进一步确诊，并评估肿瘤分期以制定治疗方案。高分化鳞状细胞癌预后相对较好，但需密切随访。'
                };
              } else {
                // 默认分析结果
                this.analysisResult = {
                  conclusion: '病理切片分析结果',
                  features: [
                    '细胞形态正常',
                    '组织结构完整',
                    '无明显异常细胞'
                  ],
                  referenceCases: [
                    '正常组织切片参考'
                  ],
                  confidence: '95%',
                  suggestion: '未见明显异常，建议定期复查'
                };
              }
              
              // 增加侧边栏高度，确保分析结果能够完全显示
              this.sidebarHeight = 450;
              console.log('设置分析结果显示状态为 true');
              console.log('分析结果数据:', this.analysisResult);
              this.showAnalysisResult = true;
              this.showUploadTip = false;
            }, 1000);
          } catch (err) {
            console.error('文件上传失败：', err);
            alert(`上传 "${file.name}" 失败，请重试`);
            this.showUploadTip = false;
          }
        }
      }
      // 重置 input 值，以便能重复选择同一文件
      e.target.value = '';
    },

    // 显示鳞状细胞癌预设分析结果
    showSquamousCellCarcinomaResult() {
      // 切换到图片分析选项卡
      this.currentTab = 'imageTab';
      
      // 设置预设的鳞状细胞癌分析结果
      this.analysisResult = {
        conclusion: '鳞状细胞癌（Squamous Cell Carcinoma）',
        features: [
          '角化珠形成：可见典型角化珠（癌珠），为同心圆排列的角化细胞',
          '细胞间桥：肿瘤细胞间可见细胞间桥连接',
          '细胞异型性：细胞核大、深染，核浆比例增高',
          '核分裂象：可见病理性核分裂象',
          '浸润性生长：癌组织呈浸润性生长，边界不清',
          '细胞巢状排列：癌细胞呈巢状、团块状排列'
        ],
        referenceCases: [
          '皮肤鳞状细胞癌 - 高分化型',
          '食管鳞状细胞癌 - 中分化型',
          '宫颈鳞状细胞癌 - 低分化型'
        ],
        confidence: '92%',
        suggestion: '建议结合免疫组化检查（如CK5/6、p40阳性）进一步确诊，并评估肿瘤分期以制定治疗方案。高分化鳞状细胞癌预后相对较好，但需密切随访。'
      };
      
      // 增加侧边栏高度，确保分析结果能够完全显示
      this.sidebarHeight = 450;
      console.log('显示鳞状细胞癌分析结果:', this.analysisResult);
      this.showAnalysisResult = true;
      
      // 如果有上传的图片，确保预览区域展开
      if (this.uploadedImages.length === 0) {
        // 如果没有图片，添加一个虚拟图片用于显示
        this.uploadedImages = [{
          id: 'test-squamous-cell-carcinoma',
          name: '鳞状细胞癌测试图片',
          url: 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjAwIiBoZWlnaHQ9IjIwMCIgdmlld0JveD0iMCAwIDIwMCAyMDAiIGZpbGw9Im5vbmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+CjxyZWN0IHdpZHRoPSIyMDAiIGhlaWdodD0iMjAwIiBmaWxsPSIjZjBmNWZhIi8+Cjx0ZXh0IHg9IjUwJSIgeT0iNTAlIiBkb21pbmFudC1iYXNlbGluZT0ibWlkZGxlIiB0ZXh0LWFuY2hvcj0ibWlkZGxlIiBmaWxsPSIjMzQ5OGRiIiBmb250LWZhbWlseT0iQXJpYWwiIGZvbnQtc2l6ZT0iMTQiPlNxdWFtb3VzIENlbGwgQ2FyY2lub21hPC90ZXh0Pgo8dGV4dCB4PSI1MCUiIHk9IjY1JSIgZG9taW5hbnQtYmFzZWxpbmU9Im1pZGRsZSIgdGV4dC1hbmNob3I9Im1pZGRsZSIgZmlsbD0iIzc0NzQ3NCIgZm9udC1mYW1pbHk9IkFyaWFsIiBmb250LXNpemU9IjEyIj5UZXN0IEluYWdlIGZvciBBSSBBbmFseXNpczwvdGV4dD4KPC9zdmc+'
        }];
      }
      
      this.isPreviewExpanded = true;
      alert('已显示预设的鳞状细胞癌分析结果，请查看右侧分析结果侧边栏。');
    },

    togglePreview() {
      this.isPreviewExpanded = !this.isPreviewExpanded;
    },
    viewImage(image) {
      this.currentImageUrl = image.url;
      this.currentImageName = image.name;
      this.showImageViewer = true;
    },
    closeImageViewer() {
      this.showImageViewer = false;
    },
    async deleteImage(imageId) {
      if (confirm('确定要删除这张图片吗？')) {
        // 可选：调用后端删除接口 /api/images/{id}
        try {
          // 尝试调用删除接口（如果后端实现），如果不实现，则该请求会失败，但本地仍然删除
          await axios.delete(`/api/images/${imageId}`);
        } catch (err) {
          // 忽略错误（后端可能没有实现）
        } finally {
          this.uploadedImages = this.uploadedImages.filter(img => img.id !== imageId);
          if (this.uploadedImages.length === 0) {
            this.showAnalysisResult = false;
            this.analysisResult = {
              conclusion: '',
              features: [],
              referenceCases: [],
              confidence: '',
              suggestion: ''
            };
          }
        }
      }
    },

    selectQuestion(question) {
      this.currentQuestion = question;
      // 可直接提交也可让用户编辑后提交
    },

    // ---------- 知识库：调用后端接口获取分类内容 ----------
    async viewKnowledgeCategory(id, name) {
      console.log('viewKnowledgeCategory called with id:', id, 'name:', name);
      console.log('knowledgeData:', this.knowledgeData);
      console.log('knowledgeData[id]:', this.knowledgeData[id]);
      
      try {
        // 直接使用本地静态数据，避免后端数据库连接问题
        if (this.knowledgeData && this.knowledgeData[id]) {
          console.log('Using local data for:', id);
          this.currentKnowledgeTitle = this.knowledgeData[id].title;
          this.currentKnowledgeContent = this.knowledgeData[id].content;
          console.log('Setting showKnowledgeModal to true');
          
          // 使用$nextTick确保DOM更新
          this.$nextTick(() => {
            this.showKnowledgeModal = true;
            console.log('showKnowledgeModal after setting:', this.showKnowledgeModal);
          });
        } else {
          console.log('No local data for:', id, ', trying backend');
          // 尝试从后端获取数据
          const resp = await axios.get(`/api/knowledge/category/${encodeURIComponent(id)}`);
          const data = resp && resp.data ? resp.data : null;
          if (data) {
            // 处理后端返回的列表数据
            if (Array.isArray(data) && data.length > 0) {
              // 取第一个文章作为显示内容
              const firstArticle = data[0];
              this.currentKnowledgeTitle = firstArticle.title || name || this.knowledgeData?.[id]?.title || name;
              this.currentKnowledgeContent = firstArticle.content || firstArticle.body || '<p>暂无内容</p>';
              this.showKnowledgeModal = true;
            } else {
              alert('该分类暂无内容');
            }
          } else {
            alert('未能获取知识库内容');
          }
        }
      } catch (err) {
        console.error('获取知识库分类出错：', err);
        // fallback: 本地静态数据（如果存在）
        if (this.knowledgeData && this.knowledgeData[id]) {
          console.log('Using fallback local data for:', id);
          this.currentKnowledgeTitle = this.knowledgeData[id].title;
          this.currentKnowledgeContent = this.knowledgeData[id].content;
          this.showKnowledgeModal = true;
        } else {
          alert('获取知识库内容失败，请稍后重试');
        }
      }
    },
    closeKnowledgeModal() {
      this.showKnowledgeModal = false;
    },

    openSource(url) {
      if (!url) return;
      // 如果后端返回的是数组或对象，调整为正确的链接
      const link = typeof url === 'string' ? url : (url.link || url.url || '');
      if (link) {
        window.open(link, '_blank');
      } else {
        alert('无效参考来源');
      }
    },

    // ---------- 认证流程：提交到后端 /api/auth/verify（需后端支持） ----------
    closeAuthDialog() {
      this.showAuthModal = false;
    },

    chooseCertImage() {
      // 点击上传认证证书触发隐藏的文件输入
      this.$refs.certInput.click();
    },
    handleCertFileChange(e) {
      const files = e.target.files;
      if (files && files.length > 0) {
        this.certFile = files[0];
      } else {
        this.certFile = null;
      }
    },

    async submitAuth() {
      const certId = (this.authForm.certId || '').trim();
      const hospital = (this.authForm.hospital || '').trim();

      if (!certId || !hospital) {
        alert('请填写完整信息');
        return;
      }

      // 后端可能需要上传图片，调用 /api/auth/verify（或后端给定的认证接口）
      const form = new FormData();
      form.append('certId', certId);
      form.append('hospital', hospital);
      form.append('userId', this.userId || '');
      if (this.certFile) {
        form.append('certImage', this.certFile);
      }

      this.authSubmitting = true;
      try {
        const resp = await axios.post('/api/auth/verify', form, {
          headers: { 'Content-Type': 'multipart/form-data' }
        });
        const data = resp && resp.data ? resp.data : {};
        // 期望返回 { success: true, doctorName: '张医生', userTag: 'doctor' } 等
        if (data && (data.success === true || data.verified === true)) {
          this.isVerifiedDoctor = true;
          this.doctorName = data.doctorName || '医生';
          // 更新 localStorage userInfo（以保持登录态）
          try {
            const userInfo = this.readLocalStorageJson('userInfo', {});
            userInfo.userTag = 'doctor';
            userInfo.nickName = this.doctorName;
            localStorage.setItem('userInfo', JSON.stringify(userInfo));
          } catch (e) {}
          this.closeAuthDialog();
          alert('医生认证成功！');
        } else {
          const msg = data.message || '认证未通过，请检查信息或稍后重试';
          alert(msg);
        }
      } catch (err) {
        console.error('认证提交出错：', err);
        alert('认证提交失败，请稍后重试');
      } finally {
        this.authSubmitting = false;
      }
    },

    // ---------- 侧边栏拖拽调整 ----------
    startResize(e) {
      this.isResizing = true;
      document.addEventListener('mousemove', this.handleResize);
      document.addEventListener('mouseup', this.stopResize);
      document.addEventListener('touchmove', this.handleResize);
      document.addEventListener('touchend', this.stopResize);
      e.preventDefault();
    },
    handleResize(e) {
      if (!this.isResizing) return;

      const clientY = e.clientY || (e.touches && e.touches[0] && e.touches[0].clientY);
      if (!clientY) return;

      const newHeight = window.innerHeight - clientY - 20; // 20px 底部边距

      // 限制最小和最大高度
      if (newHeight >= 200 && newHeight <= 600) {
        this.sidebarHeight = newHeight;
      }
    },
    stopResize() {
      this.isResizing = false;
      document.removeEventListener('mousemove', this.handleResize);
      document.removeEventListener('mouseup', this.stopResize);
      document.removeEventListener('touchmove', this.handleResize);
      document.removeEventListener('touchend', this.stopResize);
    },

    // ---------- 辅助函数 ----------
    formatMessageContent(content) {
      // 简单支持换行转 <br> 渲染
      if (!content) return '';
      return content.toString().replace(/\n/g, '<br/>').replace(/\r/g, '');
    },

    // 生成 sessionId（简单 UUID）
    generateSessionId() {
      return 's-' + ([1e7]+-1e3+-4e3+-8e3+-1e11).replace(/[018]/g, c =>
        (c ^ crypto.getRandomValues(new Uint8Array(1))[0] & 15 >> c / 4).toString(16)
      );
    }
  },
  mounted() {
    // 初始化 user/session 信息
    const userInfo = this.readLocalStorageJson('userInfo', {});
    this.isVerifiedDoctor = userInfo.userTag === 'doctor';
    this.doctorName = userInfo.nickName || '';
    this.userId = userInfo.userId || userInfo.id || Date.now();

    try {
      const savedSession = localStorage.getItem('pathologySessionId');
      if (savedSession) {
        this.sessionId = savedSession;
      } else {
        this.sessionId = this.generateSessionId();
        localStorage.setItem('pathologySessionId', this.sessionId);
      }
    } catch (error) {
      console.warn('读取sessionId失败:', error);
      this.sessionId = this.generateSessionId();
    }

    // 加载聊天历史
    const history = this.readLocalStorageJson('pathologyChatHistory', []);
    this.chatHistory = Array.isArray(history) ? history : [];

    // 点击页面其他地方关闭认证菜单（使用命名处理器以便移除）
    this._onDocClick = (ev) => {
      // 如果点击在 auth-status 或其子元素上则不关闭（事件冒泡已被 @click.stop 阻断，但保留该逻辑更稳健）
      const authNode = document.querySelector('.auth-status');
      if (authNode && authNode.contains(ev.target)) return;
      this.showAuthMenu = false;
    };
    document.addEventListener('click', this._onDocClick);
  },
  beforeUnmount() {
    // 保存聊天历史
    try {
      localStorage.setItem('pathologyChatHistory', JSON.stringify(this.chatHistory));
    } catch (e) {
      console.warn('保存聊天历史失败:', e);
    }

    // 移除事件监听器
    document.removeEventListener('mousemove', this.handleResize);
    document.removeEventListener('mouseup', this.stopResize);
    document.removeEventListener('touchmove', this.handleResize);
    document.removeEventListener('touchend', this.stopResize);
    if (this._onDocClick) {
      document.removeEventListener('click', this._onDocClick);
    }
  },

  // 兼容性：将 knowledgeData 保留在实例上，便于在接口失败时回退
  created() {
    this.knowledgeData = {
      tumor: {
        title: "肿瘤病理",
        content: `
          <div class="knowledge-section">
            <h3 class="knowledge-subtitle">肿瘤病理学概述</h3>
            <p>肿瘤病理学是研究肿瘤的形态学特征、生物学行为、发生发展机制及其与临床关系的学科。它是肿瘤诊断的金标准，为肿瘤的治疗和预后评估提供重要依据。</p>
          </div>
          <div class="knowledge-section">
            <h3 class="knowledge-subtitle">肿瘤分类</h3>
            <p>根据组织来源和生物学行为，肿瘤可分为以下几类：</p>
            <ul class="knowledge-list">
              <li><strong>上皮组织肿瘤</strong>：包括鳞状细胞癌、腺癌、移行细胞癌、基底细胞癌等</li>
              <li><strong>间叶组织肿瘤</strong>：包括纤维肉瘤、脂肪肉瘤、骨肉瘤、平滑肌肉瘤、血管肉瘤等</li>
              <li><strong>淋巴造血组织肿瘤</strong>：包括淋巴瘤、白血病、多发性骨髓瘤等</li>
              <li><strong>神经组织肿瘤</strong>：包括胶质瘤、神经鞘瘤、脑膜瘤、髓母细胞瘤等</li>
              <li><strong>其他肿瘤</strong>：包括黑色素瘤、生殖细胞肿瘤、胚胎性肿瘤等</li>
            </ul>
          </div>
          <div class="knowledge-section">
            <h3 class="knowledge-subtitle">肿瘤分级与分期</h3>
            <p>肿瘤的分级和分期是评估肿瘤恶性程度和扩散范围的重要指标：</p>
            <ul class="knowledge-list">
              <li><strong>分级（Grade）</strong>：根据肿瘤细胞的分化程度、异型性和核分裂象数量分为：
                <ul>
                  <li>G1：高分化，恶性程度低</li>
                  <li>G2：中分化，恶性程度中等</li>
                  <li>G3：低分化，恶性程度高</li>
                  <li>G4：未分化，恶性程度极高</li>
                </ul>
              </li>
              <li><strong>分期（Stage）</strong>：根据TNM系统评估：
                <ul>
                  <li>T：原发肿瘤大小和浸润深度</li>
                  <li>N：区域淋巴结转移情况</li>
                  <li>M：远处转移情况</li>
                </ul>
              </li>
            </ul>
          </div>
          <div class="knowledge-section">
            <h3 class="knowledge-subtitle">肿瘤病理诊断方法</h3>
            <ul class="knowledge-list">
              <li><strong>常规组织病理学</strong>：HE染色，光镜观察</li>
              <li><strong>免疫组织化学</strong>：检测肿瘤标志物，辅助诊断和分型</li>
              <li><strong>分子病理学</strong>：检测基因突变，指导靶向治疗</li>
              <li><strong>电镜检查</strong>：观察超微结构，辅助诊断特殊肿瘤</li>
            </ul>
          </div>
          <div class="knowledge-section">
            <h3 class="knowledge-subtitle">常见肿瘤的病理特征</h3>
            <ul class="knowledge-list">
              <li><strong>鳞状细胞癌</strong>：角化珠形成、细胞间桥、异型性明显</li>
              <li><strong>腺癌</strong>：腺样结构、分泌现象、黏液产生</li>
              <li><strong>肝细胞癌</strong>：肝细胞样形态、胆汁分泌、假腺样结构</li>
              <li><strong>肺癌</strong>：多种组织学类型，包括腺癌、鳞状细胞癌、小细胞癌等</li>
            </ul>
          </div>
        `
      },
      tissue: {
        title: "组织病理",
        content: `
          <div class="knowledge-section">
            <h3 class="knowledge-subtitle">组织病理学概述</h3>
            <p>组织病理学是研究疾病状态下组织结构和细胞变化的学科。通过对组织样本的处理和观察，为疾病的诊断、治疗和预后评估提供重要依据。</p>
          </div>
          <div class="knowledge-section">
            <h3 class="knowledge-subtitle">组织样本处理流程</h3>
            <ul class="knowledge-list">
              <li><strong>固定</strong>：使用福尔马林等固定剂保存组织结构</li>
              <li><strong>脱水</strong>：使用酒精梯度脱水</li>
              <li><strong>透明</strong>：使用二甲苯等透明剂</li>
              <li><strong>包埋</strong>：使用石蜡包埋组织</li>
              <li><strong>切片</strong>：使用切片机制作薄切片</li>
              <li><strong>染色</strong>：使用HE染色等方法</li>
              <li><strong>封片</strong>：使用中性树胶封片</li>
            </ul>
          </div>
          <div class="knowledge-section">
            <h3 class="knowledge-subtitle">常见组织病变</h3>
            <ul class="knowledge-list">
              <li><strong>炎症</strong>：急性炎症、慢性炎症、肉芽肿性炎症</li>
              <li><strong>变性</strong>：细胞水肿、脂肪变性、玻璃样变性</li>
              <li><strong>坏死</strong>：凝固性坏死、液化性坏死、干酪样坏死</li>
              <li><strong>增生</strong>：生理性增生、病理性增生</li>
              <li><strong>化生</strong>：鳞状上皮化生、肠上皮化生</li>
            </ul>
          </div>
          <div class="knowledge-section">
            <h3 class="knowledge-subtitle">各系统组织病理特点</h3>
            <ul class="knowledge-list">
              <li><strong>消化系统</strong>：胃肠道黏膜病变、肝脏病变、胰腺病变</li>
              <li><strong>呼吸系统</strong>：肺部炎症、肺气肿、肺纤维化</li>
              <li><strong>心血管系统</strong>：动脉粥样硬化、心肌梗死、心肌炎</li>
              <li><strong>泌尿系统</strong>：肾小球肾炎、肾小管病变、肾盂肾炎</li>
              <li><strong>生殖系统</strong>：宫颈炎、子宫内膜病变、卵巢病变</li>
            </ul>
          </div>
        `
      },
      cell: {
        title: "细胞病理",
        content: `
          <div class="knowledge-section">
            <h3 class="knowledge-subtitle">细胞病理学概述</h3>
            <p>细胞病理学是通过检查单个细胞或细胞群来诊断疾病的学科。它是一种非侵入性或微创性的诊断方法，广泛应用于肿瘤的早期筛查和诊断。</p>
          </div>
          <div class="knowledge-section">
            <h3 class="knowledge-subtitle">细胞病理学技术</h3>
            <ul class="knowledge-list">
              <li><strong>脱落细胞学</strong>：收集自然脱落的细胞，如宫颈涂片、痰涂片、尿液细胞学</li>
              <li><strong>细针穿刺细胞学</strong>：使用细针吸取病变组织的细胞，如甲状腺、乳腺、淋巴结等</li>
              <li><strong>刷取细胞学</strong>：使用刷子刷取表面细胞，如支气管刷片、食管刷片</li>
              <li><strong>灌洗细胞学</strong>：使用液体灌洗收集细胞，如支气管肺泡灌洗、腹腔灌洗</li>
            </ul>
          </div>
          <div class="knowledge-section">
            <h3 class="knowledge-subtitle">细胞病理学诊断要点</h3>
            <ul class="knowledge-list">
              <li><strong>细胞形态</strong>：大小、形状、核质比</li>
              <li><strong>核特征</strong>：核大小、核形态、染色质分布、核仁</li>
              <li><strong>细胞质</strong>：量、颜色、颗粒、空泡</li>
              <li><strong>细胞排列</strong>：单个细胞、细胞团、组织结构</li>
              <li><strong>背景</strong>：炎症细胞、坏死物、血液</li>
            </ul>
          </div>
          <div class="knowledge-section">
            <h3 class="knowledge-subtitle">常见疾病的细胞病理特征</h3>
            <ul class="knowledge-list">
              <li><strong>宫颈癌</strong>：鳞状上皮细胞异常，如ASC-US、LSIL、HSIL、鳞状细胞癌</li>
              <li><strong>肺癌</strong>：腺癌、鳞状细胞癌、小细胞癌的细胞学特征</li>
              <li><strong>甲状腺癌</strong>：乳头状癌的核特征，如核沟、核内假包涵体</li>
              <li><strong>乳腺癌</strong>：癌细胞的形态特征，如核异型性、细胞质特征</li>
            </ul>
          </div>
          <div class="knowledge-section">
            <h3 class="knowledge-subtitle">细胞病理学的优势与局限性</h3>
            <ul class="knowledge-list">
              <li><strong>优势</strong>：非侵入性、操作简单、成本低、可重复</li>
              <li><strong>局限性</strong>：细胞数量有限、缺乏组织结构、诊断准确性依赖病理学家经验</li>
            </ul>
          </div>
        `
      },
      molecular: {
        title: "分子病理",
        content: `
          <div class="knowledge-section">
            <h3 class="knowledge-subtitle">分子病理学概述</h3>
            <p>分子病理学是从分子水平研究疾病发生、发展机制的学科。通过检测基因、蛋白质等分子标志物，为疾病的诊断、预后评估和靶向治疗提供依据。</p>
          </div>
          <div class="knowledge-section">
            <h3 class="knowledge-subtitle">分子病理学技术</h3>
            <ul class="knowledge-list">
              <li><strong>聚合酶链反应（PCR）</strong>：扩增特定DNA片段，用于基因突变检测</li>
              <li><strong>荧光原位杂交（FISH）</strong>：检测染色体异常和基因扩增</li>
              <li><strong>免疫组织化学（IHC）</strong>：检测蛋白质表达水平</li>
              <li><strong>下一代测序（NGS）</strong>：高通量测序，检测多个基因突变</li>
              <li><strong>基因芯片</strong>：同时检测多个基因的表达水平</li>
              <li><strong>数字PCR</strong>：定量检测基因突变</li>
            </ul>
          </div>
          <div class="knowledge-section">
            <h3 class="knowledge-subtitle">分子标志物在肿瘤中的应用</h3>
            <ul class="knowledge-list">
              <li><strong>诊断标志物</strong>：帮助明确肿瘤类型和起源</li>
              <li><strong>预后标志物</strong>：预测肿瘤的生物学行为和患者预后</li>
              <li><strong>预测标志物</strong>：预测药物治疗的疗效</li>
              <li><strong>监测标志物</strong>：监测肿瘤复发和治疗反应</li>
            </ul>
          </div>
          <div class="knowledge-section">
            <h3 class="knowledge-subtitle">常见肿瘤的分子标志物</h3>
            <ul class="knowledge-list">
              <li><strong>肺癌</strong>：EGFR突变、ALK融合、ROS1融合、PD-L1表达</li>
              <li><strong>乳腺癌</strong>：ER、PR、HER2、Ki-67</li>
              <li><strong>结直肠癌</strong>：KRAS突变、NRAS突变、BRAF突变、MSI/MMR状态</li>
              <li><strong>黑色素瘤</strong>：BRAF突变、NRAS突变、CKIT突变</li>
              <li><strong>胶质瘤</strong>：IDH1/2突变、1p/19q共缺失、MGMT启动子甲基化</li>
            </ul>
          </div>
          <div class="knowledge-section">
            <h3 class="knowledge-subtitle">分子病理学的临床意义</h3>
            <ul class="knowledge-list">
              <li>指导靶向治疗药物的选择</li>
              <li>预测免疫治疗的疗效</li>
              <li>早期诊断和筛查</li>
              <li>肿瘤异质性分析</li>
              <li>个体化治疗方案制定</li>
            </ul>
          </div>
        `
      },
      infectious: {
        title: "感染性病理",
        content: `
          <div class="knowledge-section">
            <h3 class="knowledge-subtitle">感染性疾病病理学概述</h3>
            <p>感染性病理主要研究由病原体感染引起的组织和细胞变化。它是感染性疾病诊断的重要依据，帮助确定病原体类型和感染程度。</p>
          </div>
          <div class="knowledge-section">
            <h3 class="knowledge-subtitle">常见病原体感染的病理特征</h3>
            <ul class="knowledge-list">
              <li><strong>细菌感染</strong>：
                <ul>
                  <li>化脓性炎症：中性粒细胞浸润、脓肿形成</li>
                  <li>肉芽肿性炎症：结核分枝杆菌感染</li>
                  <li>假膜性炎症：白喉杆菌感染</li>
                </ul>
              </li>
              <li><strong>病毒感染</strong>：
                <ul>
                  <li>细胞融合：巨细胞病毒感染</li>
                  <li>包涵体形成：单纯疱疹病毒感染</li>
                  <li>细胞凋亡：HIV感染</li>
                </ul>
              </li>
              <li><strong>真菌感染</strong>：
                <ul>
                  <li>菌丝和孢子：念珠菌感染</li>
                  <li>肉芽肿形成：隐球菌感染</li>
                  <li>坏死性炎症：曲霉感染</li>
                </ul>
              </li>
              <li><strong>寄生虫感染</strong>：
                <ul>
                  <li>虫体或虫卵：蛔虫、血吸虫感染</li>
                  <li>肉芽肿形成：血吸虫感染</li>
                  <li>组织破坏：阿米巴感染</li>
                </ul>
              </li>
            </ul>
          </div>
          <div class="knowledge-section">
            <h3 class="knowledge-subtitle">各系统感染的病理特点</h3>
            <ul class="knowledge-list">
              <li><strong>呼吸系统感染</strong>：肺炎、肺结核、支气管炎</li>
              <li><strong>消化系统感染</strong>：胃肠炎、肝炎、胆囊炎</li>
              <li><strong>中枢神经系统感染</strong>：脑膜炎、脑炎、脑脓肿</li>
              <li><strong>泌尿系统感染</strong>：肾盂肾炎、膀胱炎、尿道炎</li>
              <li><strong>皮肤感染</strong>：蜂窝织炎、疖、痈、丹毒</li>
            </ul>
          </div>
          <div class="knowledge-section">
            <h3 class="knowledge-subtitle">感染性疾病的诊断方法</h3>
            <ul class="knowledge-list">
              <li><strong>组织病理学检查</strong>：观察病原体和炎症反应</li>
              <li><strong>微生物培养</strong>：分离和鉴定病原体</li>
              <li><strong>血清学检查</strong>：检测抗体水平</li>
              <li><strong>分子生物学检查</strong>：检测病原体核酸</li>
              <li><strong>免疫学检查</strong>：检测病原体抗原</li>
            </ul>
          </div>
          <div class="knowledge-section">
            <h3 class="knowledge-subtitle">感染性疾病的病理诊断要点</h3>
            <ul class="knowledge-list">
              <li>识别病原体的形态特征</li>
              <li>观察炎症反应的类型和程度</li>
              <li>评估组织损伤的范围和程度</li>
              <li>结合临床症状和实验室检查</li>
            </ul>
          </div>
        `
      },
      dermatopathology: {
        title: "皮肤病理",
        content: `
          <div class="knowledge-section">
            <h3 class="knowledge-subtitle">皮肤病理学概述</h3>
            <p>皮肤病理学是研究皮肤疾病的病理变化的学科。通过对皮肤组织的病理检查，为皮肤病的诊断和治疗提供依据。</p>
          </div>
          <div class="knowledge-section">
            <h3 class="knowledge-subtitle">皮肤的基本结构</h3>
            <ul class="knowledge-list">
              <li><strong>表皮</strong>：角质层、颗粒层、棘层、基底层</li>
              <li><strong>真皮</strong>：乳头层、网状层</li>
              <li><strong>皮下组织</strong>：脂肪组织</li>
              <li><strong>皮肤附属器</strong>：汗腺、皮脂腺、毛囊、指甲</li>
            </ul>
          </div>
          <div class="knowledge-section">
            <h3 class="knowledge-subtitle">常见皮肤病的病理特征</h3>
            <ul class="knowledge-list">
              <li><strong>炎症性皮肤病</strong>：
                <ul>
                  <li>湿疹：海绵形成、淋巴细胞浸润</li>
                  <li>银屑病：表皮增生、角化过度、中性粒细胞浸润</li>
                  <li>扁平苔藓：界面皮炎、淋巴细胞浸润</li>
                </ul>
              </li>
              <li><strong>感染性皮肤病</strong>：
                <ul>
                  <li>带状疱疹：气球样变性、多核巨细胞</li>
                  <li>真菌感染：菌丝和孢子</li>
                  <li>麻风：肉芽肿形成、抗酸杆菌</li>
                </ul>
              </li>
              <li><strong>肿瘤性皮肤病</strong>：
                <ul>
                  <li>基底细胞癌：基底样细胞增生、周围栅栏状排列</li>
                  <li>鳞状细胞癌：鳞状细胞异型性、角化珠</li>
                  <li>黑色素瘤：黑素细胞异型性、色素沉着</li>
                </ul>
              </li>
              <li><strong>自身免疫性皮肤病</strong>：
                <ul>
                  <li>天疱疮：棘层松解、水疱形成</li>
                  <li>类天疱疮：表皮下水疱、嗜酸性粒细胞浸润</li>
                </ul>
              </li>
            </ul>
          </div>
          <div class="knowledge-section">
            <h3 class="knowledge-subtitle">皮肤病理检查方法</h3>
            <ul class="knowledge-list">
              <li><strong>皮肤活检</strong>： punch活检、 excision活检、 shave活检</li>
              <li><strong>特殊染色</strong>：
                <ul>
                  <li>PAS染色：检测真菌</li>
                  <li>抗酸染色：检测结核分枝杆菌</li>
                  <li>免疫组化：检测特定蛋白质表达</li>
                </ul>
              </li>
              <li><strong>电子显微镜</strong>：观察超微结构</li>
            </ul>
          </div>
          <div class="knowledge-section">
            <h3 class="knowledge-subtitle">皮肤病理诊断的注意事项</h3>
            <ul class="knowledge-list">
              <li>结合临床信息和皮损表现</li>
              <li>注意取材部位和方法</li>
              <li>考虑皮肤病的病程和治疗史</li>
              <li>必要时进行免疫组化和分子检测</li>
            </ul>
          </div>
        `
      },
      neuropathology: {
        title: "神经病理",
        content: `
          <div class="knowledge-section">
            <h3 class="knowledge-subtitle">神经病理学概述</h3>
            <p>神经病理学是研究神经系统疾病的病理变化的学科。包括脑血管疾病、 neurodegenerative 疾病、脑肿瘤等的病理特征和诊断方法。</p>
          </div>
          <div class="knowledge-section">
            <h3 class="knowledge-subtitle">神经系统的基本结构</h3>
            <ul class="knowledge-list">
              <li><strong>大脑</strong>：大脑皮层、基底节、丘脑、下丘脑</li>
              <li><strong>小脑</strong>：小脑皮层、小脑核</li>
              <li><strong>脑干</strong>：中脑、脑桥、延髓</li>
              <li><strong>脊髓</strong>：灰质、白质</li>
              <li><strong>周围神经</strong>：神经纤维、神经节</li>
              <li><strong>肌肉</strong>：骨骼肌、平滑肌</li>
            </ul>
          </div>
          <div class="knowledge-section">
            <h3 class="knowledge-subtitle">常见神经系统疾病的病理特征</h3>
            <ul class="knowledge-list">
              <li><strong>脑血管疾病</strong>：
                <ul>
                  <li>脑梗死：缺血性坏死、神经元丢失</li>
                  <li>脑出血：出血性坏死、红细胞浸润</li>
                  <li>蛛网膜下腔出血：蛛网膜下腔积血</li>
                </ul>
              </li>
              <li><strong> neurodegenerative 疾病</strong>：
                <ul>
                  <li>阿尔茨海默病：老年斑、神经原纤维缠结</li>
                  <li>帕金森病：路易小体、黑质多巴胺能神经元丢失</li>
                  <li>肌萎缩侧索硬化：运动神经元丢失、胶质增生</li>
                </ul>
              </li>
              <li><strong>脑肿瘤</strong>：
                <ul>
                  <li>胶质瘤：星形细胞瘤、少突胶质细胞瘤、室管膜瘤</li>
                  <li>脑膜瘤：蛛网膜细胞增生、砂粒体</li>
                  <li>神经鞘瘤：Antoni A区和Antoni B区</li>
                </ul>
              </li>
              <li><strong>感染性神经系统疾病</strong>：
                <ul>
                  <li>病毒性脑炎：淋巴细胞浸润、神经元坏死</li>
                  <li>细菌性脑膜炎：中性粒细胞浸润、脓性渗出</li>
                  <li>寄生虫感染：虫体或虫卵、肉芽肿形成</li>
                </ul>
              </li>
              <li><strong>脱髓鞘疾病</strong>：
                <ul>
                  <li>多发性硬化：脱髓鞘斑块、胶质增生</li>
                  <li>急性播散性脑脊髓炎：广泛脱髓鞘</li>
                </ul>
              </li>
            </ul>
          </div>
          <div class="knowledge-section">
            <h3 class="knowledge-subtitle">神经病理检查方法</h3>
            <ul class="knowledge-list">
              <li><strong>组织病理学</strong>：HE染色、特殊染色</li>
              <li><strong>免疫组织化学</strong>：检测特定蛋白质表达</li>
              <li><strong>分子病理学</strong>：检测基因突变</li>
              <li><strong>电子显微镜</strong>：观察超微结构</li>
              <li><strong>神经影像学</strong>：CT、MRI、PET</li>
            </ul>
          </div>
          <div class="knowledge-section">
            <h3 class="knowledge-subtitle">神经病理诊断的临床意义</h3>
            <ul class="knowledge-list">
              <li>明确神经系统疾病的诊断</li>
              <li>指导治疗方案的制定</li>
              <li>评估疾病的预后</li>
              <li>研究神经系统疾病的发病机制</li>
            </ul>
          </div>
        `
      },
      hematopathology: {
        title: "血液病理",
        content: `
          <div class="knowledge-section">
            <h3 class="knowledge-subtitle">血液病理学概述</h3>
            <p>血液病理学是研究血液系统疾病的病理变化的学科。包括贫血、白血病、淋巴瘤等血液系统疾病的病理特征和诊断方法。</p>
          </div>
          <div class="knowledge-section">
            <h3 class="knowledge-subtitle">血液系统的基本组成</h3>
            <ul class="knowledge-list">
              <li><strong>红细胞</strong>：运输氧气和二氧化碳</li>
              <li><strong>白细胞</strong>：包括粒细胞、淋巴细胞、单核细胞</li>
              <li><strong>血小板</strong>：参与凝血过程</li>
              <li><strong>骨髓</strong>：造血组织，产生血细胞</li>
              <li><strong> lymphoid组织</strong>：淋巴结、脾脏、胸腺</li>
            </ul>
          </div>
          <div class="knowledge-section">
            <h3 class="knowledge-subtitle">常见血液系统疾病的病理特征</h3>
            <ul class="knowledge-list">
              <li><strong>贫血</strong>：
                <ul>
                  <li>缺铁性贫血：小细胞低色素性贫血</li>
                  <li>巨幼细胞性贫血：大细胞性贫血、核幼浆老</li>
                  <li>溶血性贫血：红细胞破坏增加</li>
                </ul>
              </li>
              <li><strong>白血病</strong>：
                <ul>
                  <li>急性淋巴细胞白血病：原始淋巴细胞增生</li>
                  <li>急性髓系白血病：原始髓系细胞增生</li>
                  <li>慢性粒细胞白血病：粒细胞增生、Ph染色体阳性</li>
                  <li>慢性淋巴细胞白血病：成熟淋巴细胞增生</li>
                </ul>
              </li>
              <li><strong>淋巴瘤</strong>：
                <ul>
                  <li>霍奇金淋巴瘤：Reed-Sternberg细胞</li>
                  <li>非霍奇金淋巴瘤：B细胞淋巴瘤、T细胞淋巴瘤</li>
                </ul>
              </li>
              <li><strong>多发性骨髓瘤</strong>：浆细胞增生、骨破坏</li>
              <li><strong>骨髓增生异常综合征</strong>：血细胞发育异常、无效造血</li>
            </ul>
          </div>
          <div class="knowledge-section">
            <h3 class="knowledge-subtitle">血液病理检查方法</h3>
            <ul class="knowledge-list">
              <li><strong>血常规</strong>：检测血细胞数量和形态</li>
              <li><strong>骨髓穿刺和活检</strong>：评估骨髓造血功能</li>
              <li><strong>流式细胞术</strong>：分析细胞表面标志物</li>
              <li><strong>细胞遗传学</strong>：检测染色体异常</li>
              <li><strong>分子生物学</strong>：检测基因突变</li>
              <li><strong>免疫组织化学</strong>：检测特定蛋白质表达</li>
            </ul>
          </div>
          <div class="knowledge-section">
            <h3 class="knowledge-subtitle">血液病理诊断的临床意义</h3>
            <ul class="knowledge-list">
              <li>明确血液系统疾病的诊断</li>
              <li>指导治疗方案的制定</li>
              <li>评估疾病的预后</li>
              <li>监测治疗反应和疾病复发</li>
            </ul>
          </div>
        `
      }
    };
  }
};
</script>

<style scoped>
/* 原有的所有样式保持不变，这里只添加补充样式 */

 /* 保留您原有的所有CSS样式 */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;
        }
        
        body {
            background-color: #f5f7fa;
            color: #333;
            line-height: 1.6;
            height: 100vh;
            overflow: hidden;
        }
        
        /* 顶部导航栏 */
        .nav-bar {
            height: 56px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 0 16px;
            background: linear-gradient(135deg, #1E88E5, #1565C0);
            color: #fff;
            position: sticky;
            top: 0;
            z-index: 100;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }
        
        .nav-left {
            display: flex;
            align-items: center;
            gap: 12px;
        }
        
        .back-btn {
            width: 40px;
            height: 40px;
            display: flex;
            align-items: center;
            justify-content: center;
            cursor: pointer;
            border-radius: 50%;
            transition: background-color 0.2s;
        }
        
        .back-btn:hover {
            background-color: rgba(255, 255, 255, 0.1);
        }
        
        .back-btn img {
            width: 20px;
            height: 20px;
            filter: brightness(0) invert(1);
        }
        
        .title {
            font-size: 18px;
            font-weight: 700;
        }
        
        .auth-status {
            display: flex;
            align-items: center;
            gap: 8px;
            background: rgba(255, 255, 255, 0.2);
            padding: 6px 12px;
            border-radius: 16px;
            font-size: 14px;
            cursor: pointer;
            transition: all 0.3s ease;
            position: relative;
        }
        
        .auth-status:hover {
            background: rgba(255, 255, 255, 0.3);
        }
        
        .auth-status.verified {
            background: rgba(76, 175, 80, 0.8);
        }
        
        .auth-menu {
            position: absolute;
            top: 100%;
            right: 0;
            background: white;
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
            padding: 8px 0;
            min-width: 150px;
            z-index: 101;
        }
        
        .auth-menu-item {
            padding: 10px 16px;
            font-size: 14px;
            color: #333;
            cursor: pointer;
            transition: background-color 0.2s;
        }
        
        .auth-menu-item:hover {
            background-color: #f5f7fa;
        }
        
        /* 内容区域 - 全屏布局 */
        .content {
            height: calc(100vh - 56px - 60px);
            overflow: hidden;
            position: relative;
        }
        
        .tab-content {
            display: none;
            height: 100%;
            overflow-y: auto;
            padding: 16px;
            padding-bottom: 20px;
        }
        
        .tab-content.active {
            display: flex;
            flex-direction: column;
        }
        
        /* 功能卡片 - 全屏布局 */
        .function-card {
            background: white;
            border-radius: 12px;
            padding: 20px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
            flex: 1;
            display: flex;
            flex-direction: column;
            overflow: hidden;
        }
        
        /* 图片分析卡片特殊样式 - 允许分析结果侧边栏完全显示 */
        .function-card.image-analysis {
            overflow: visible;
        }
        
        .card-title {
            font-size: 18px;
            font-weight: 600;
            color: #2c3e50;
            margin-bottom: 16px;
            display: flex;
            align-items: center;
            gap: 8px;
            padding-bottom: 12px;
            border-bottom: 1px solid #f1f2f6;
        }
        
        .card-title-icon {
            font-size: 20px;
        }
        
        /* 病理问答样式 - 全屏 */
        .chat-container {
            flex: 1;
            overflow-y: auto;
            margin-bottom: 16px;
            padding: 12px;
            background: #f8f9fa;
            border-radius: 12px;
            min-height: 200px;
        }
        
        .message-item {
            margin-bottom: 16px;
            max-width: 100%;
        }
        
        .user-msg {
            display: flex;
            justify-content: flex-end;
        }
        
        .user-bubble {
            background: linear-gradient(135deg, #1E88E5, #1565C0);
            color: #fff;
            padding: 12px 16px;
            border-radius: 18px 18px 4px 18px;
            max-width: 80%;
            font-size: 14px;
            position: relative;
            box-shadow: 0 2px 8px rgba(30, 136, 229, 0.3);
        }
        
        .ai-msg {
            display: flex;
            justify-content: flex-start;
        }
        
        .ai-bubble {
            background: linear-gradient(135deg, #4CAF50, #388E3C);
            color: #fff;
            padding: 12px 16px;
            border-radius: 18px 18px 18px 4px;
            max-width: 80%;
            font-size: 14px;
            position: relative;
            box-shadow: 0 2px 8px rgba(76, 175, 80, 0.3);
        }
        
        .time {
            display: block;
            font-size: 11px;
            margin-top: 6px;
            opacity: 0.8;
            text-align: right;
        }
        
        .source {
            display: block;
            font-size: 11px;
            margin-top: 6px;
            opacity: 0.8;
            text-decoration: underline;
            cursor: pointer;
        }
        
        .question-input-area {
            display: flex;
            align-items: center;
            margin-bottom: 16px;
            gap: 12px;
            flex-shrink: 0;
        }
        
        .question-input {
            flex: 1;
            height: 48px;
            padding: 0 16px;
            border: 1px solid #e1e8ed;
            border-radius: 24px;
            font-size: 14px;
            transition: all 0.3s ease;
        }
        
        .question-input:focus {
            border-color: #3498db;
            outline: none;
            box-shadow: 0 0 0 2px rgba(52, 152, 219, 0.1);
        }
        
        .send-btn {
            width: 80px;
            height: 48px;
            background: linear-gradient(135deg, #3498db, #2980b9);
            color: #fff;
            border-radius: 24px;
            font-size: 14px;
            font-weight: 500;
            border: none;
            cursor: pointer;
            transition: all 0.3s ease;
        }
        
        .send-btn:hover:not(:disabled) {
            box-shadow: 0 4px 12px rgba(52, 152, 219, 0.3);
            transform: translateY(-1px);
        }
        
        .send-btn:disabled {
            background: #BDBDBD;
            cursor: not-allowed;
            transform: none;
            box-shadow: none;
        }
        
        /* 清空聊天记录按钮 */
        .chat-actions {
            display: flex;
            justify-content: flex-end;
            margin-bottom: 12px;
        }
        
        .clear-chat-btn {
            background: #f44336;
            color: white;
            border: none;
            padding: 8px 16px;
            border-radius: 20px;
            font-size: 14px;
            cursor: pointer;
            transition: all 0.3s ease;
        }
        
        .clear-chat-btn:hover {
            background: #d32f2f;
            box-shadow: 0 2px 8px rgba(244, 67, 54, 0.3);
        }
        
        /* 历史记录区域 */
        .history-section {
            margin-top: 20px;
            border-top: 1px solid #f1f2f6;
            padding-top: 16px;
            flex-shrink: 0;
        }
        
        .history-title {
            font-size: 16px;
            font-weight: 600;
            color: #2c3e50;
            margin-bottom: 12px;
            display: flex;
            align-items: center;
            gap: 8px;
        }
        
        .history-list {
            display: flex;
            flex-direction: column;
            gap: 10px;
            max-height: 150px;
            overflow-y: auto;
        }
        
        .history-item {
            padding: 10px 12px;
            background: #f8f9fa;
            border-radius: 8px;
            font-size: 14px;
            color: #555;
            cursor: pointer;
            transition: all 0.2s ease;
            border-left: 3px solid transparent;
        }
        
        .history-item:hover {
            background: #e9ecef;
            border-left-color: #3498db;
        }
        
        /* 图片分析样式 */
        .upload-container {
            margin-bottom: 16px;
            cursor: pointer;
        }
        
        .upload-area {
            border: 2px dashed #dce6f0;
            border-radius: 12px;
            padding: 40px 20px;
            text-align: center;
            cursor: pointer;
            transition: all 0.3s ease;
            background: #fafbfc;
        }
        
        .upload-area:hover {
            border-color: #3498db;
            background: #f8fafc;
            transform: translateY(-2px);
        }
        
        .upload-icon {
            font-size: 48px;
            color: #bdc3c7;
            margin-bottom: 12px;
        }
        
        .upload-text {
            font-size: 16px;
            font-weight: 500;
            color: #34495e;
            margin-bottom: 8px;
        }
        
        .upload-hint {
            font-size: 14px;
            color: #7f8c8d;
        }
        
        .auth-badge {
            display: flex;
            justify-content: center;
            margin-top: 12px;
        }
        
        .badge {
          background: #fff3cd;
          color: #856404;
          padding: 6px 12px;
          border-radius: 16px;
          font-size: 12px;
          display: flex;
          align-items: center;
          gap: 6px;
          cursor: pointer;
          transition: all 0.3s ease;
        }
        
        .badge:hover {
          background: #ffeaa7;
          transform: translateY(-1px);
        }
        
        /* 图片预览区域 */
        .image-preview-section {
            margin-top: 20px;
            border-top: 1px solid #f1f2f6;
            padding-top: 16px;
        }
        
        .preview-header {
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin-bottom: 12px;
        }
        
        .preview-title {
            font-size: 16px;
            font-weight: 600;
            color: #2c3e50;
        }
        
        .toggle-preview {
            background: none;
            border: none;
            color: #3498db;
            font-size: 14px;
            cursor: pointer;
            display: flex;
            align-items: center;
            gap: 4px;
            padding: 4px 8px;
            border-radius: 6px;
            transition: background 0.2s;
        }
        
        .toggle-preview:hover {
            background: #f1f2f6;
        }
        
        .preview-container {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(140px, 1fr));
            gap: 12px;
            margin-bottom: 16px;
            transition: all 0.3s ease;
        }
        
        .preview-container.collapsed {
            max-height: 160px;
            overflow: hidden;
        }
        
        .preview-item {
            position: relative;
            border-radius: 12px;
            overflow: hidden;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
            transition: transform 0.2s ease;
        }
        
        .preview-item:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        }
        
        .preview-image {
            width: 100%;
            height: 120px;
            object-fit: cover;
            display: block;
        }
        
        .preview-actions {
            position: absolute;
            bottom: 0;
            left: 0;
            right: 0;
            background: rgba(0, 0, 0, 0.7);
            display: flex;
            justify-content: space-between;
            padding: 8px 10px;
        }
        
        .preview-btn {
            background: none;
            border: none;
            color: white;
            font-size: 12px;
            cursor: pointer;
            display: flex;
            align-items: center;
            gap: 4px;
            padding: 4px 8px;
            border-radius: 4px;
            transition: background 0.2s;
        }
        
        .preview-btn:hover {
            background: rgba(255, 255, 255, 0.1);
        }
        
        /* 新增：可拖拽侧边栏样式 */
        .analysis-sidebar {
            position: relative;
            background: linear-gradient(135deg, #f8f9fa, #e9ecef);
            border-radius: 12px;
            margin-top: 16px;
            border-left: 4px solid #4CAF50;
            overflow: hidden;
            transition: all 0.3s ease;
            display: none;
        }
        
        .analysis-sidebar.show {
            display: block;
            animation: fadeIn 0.5s ease;
            z-index: 10;
            position: relative;
        }
        
        .sidebar-header {
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 16px 20px;
            background: #f1f2f6;
            border-bottom: 1px solid #e1e8ed;
            cursor: ns-resize;
            .upload-cert-area {
                -webkit-user-select: none; /* 兼容 Safari 和 iOS Safari */
                user-select: none;
            }
        }
        
        .sidebar-header:hover {
            background: #e9ecef;
        }
        
        .result-title {
            font-size: 16px;
            font-weight: 700;
            color: #2c3e50;
        }
        
        .auxiliary-tag {
            padding: 4px 10px;
            background: #fff3e0;
            color: #e65100;
            font-size: 12px;
            border-radius: 12px;
            font-weight: 500;
        }
        
        .drag-handle {
            display: flex;
            align-items: center;
            color: #7f8c8d;
            font-size: 14px;
        }
        
        .drag-handle::after {
            content: "⋮⋮";
            font-size: 16px;
            transform: rotate(90deg);
            margin-left: 8px;
        }
        
        .sidebar-content {
            overflow-y: auto;
            transition: height 0.3s ease;
        }
        
        .result-section {
            margin-bottom: 16px;
            line-height: 1.6;
            padding: 0 20px;
        }
        
        .result-section:first-of-type {
            padding-top: 16px;
        }
        
        .result-section:last-of-type {
            padding-bottom: 16px;
        }
        
        .result-subtitle {
            font-weight: 600;
            color: #2c3e50;
            margin-right: 8px;
            display: inline-block;
            margin-bottom: 4px;
        }
        
        .feature-item, .reference-item {
            display: flex;
            align-items: flex-start;
            margin-bottom: 8px;
            line-height: 1.5;
        }
        
        .bullet {
            color: #4CAF50;
            margin-right: 8px;
            line-height: 1.5;
            font-weight: bold;
        }
        
        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(10px); }
            to { opacity: 1; transform: translateY(0); }
        }
        
        /* 知识库样式 */
        .knowledge-categories {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 16px;
            height: 100%;
            overflow-y: auto;
            padding: 8px 4px;
        }
        
        .category-item {
            display: flex;
            flex-direction: column;
            align-items: center;
            padding: 20px 12px;
            background: white;
            border-radius: 12px;
            text-align: center;
            cursor: pointer;
            transition: all 0.3s ease;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
            border: 1px solid #f1f2f6;
            height: 150px;
            justify-content: center;
        }
        
        .category-item:hover {
            transform: translateY(-3px);
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            border-color: #3498db;
        }
        
        .category-icon {
            width: 48px;
            height: 48px;
            margin-bottom: 12px;
            border-radius: 12px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 24px;
            background: #f8f9fa;
        }
        
        .category-name {
            font-size: 14px;
            font-weight: 500;
            color: #2c3e50;
        }
        
        /* 底部导航栏 */
        .bottom-nav {
            position: fixed;
            bottom: 0;
            left: 0;
            right: 0;
            height: 60px;
            background: white;
            display: flex;
            justify-content: space-around;
            align-items: center;
            box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.1);
            z-index: 100;
        }
        
        .nav-item {
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            flex: 1;
            height: 100%;
            cursor: pointer;
            transition: all 0.2s ease;
            color: #7f8c8d;
        }
        
        .nav-item.active {
            color: #1E88E5;
        }
        
        .nav-item:hover {
            background: #f8f9fa;
        }
        
        .nav-icon {
            font-size: 20px;
            margin-bottom: 4px;
        }
        
        .nav-text {
            font-size: 12px;
        }
        
        /* 知识库模态框 */
        .knowledge-modal-mask {
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background-color: rgba(0, 0, 0, 0.5);
            display: flex;
            align-items: center;
            justify-content: center;
            z-index: 9999;
            overflow: hidden;
        }
        
        .knowledge-modal {
            width: 90%;
            max-width: 600px;
            max-height: 80vh;
            background-color: #fff;
            border-radius: 16px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
            overflow: hidden;
        }
        
        .knowledge-modal-header {
            padding: 20px;
            background: linear-gradient(135deg, #1E88E5, #1565C0);
            color: white;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        
        .knowledge-modal-title {
            font-size: 18px;
            font-weight: 700;
            margin: 0;
        }
        
        .close-knowledge-modal {
            background: none;
            border: none;
            color: white;
            font-size: 24px;
            cursor: pointer;
            padding: 0;
            width: 30px;
            height: 30px;
            display: flex;
            align-items: center;
            justify-content: center;
            border-radius: 50%;
            transition: background-color 0.2s;
        }
        
        .close-knowledge-modal:hover {
            background-color: rgba(255, 255, 255, 0.2);
        }
        
        .knowledge-content {
            padding: 20px;
            max-height: 60vh;
            overflow-y: auto;
            line-height: 1.6;
        }
        
        .knowledge-content h3 {
            color: #2c3e50;
            margin-top: 20px;
            margin-bottom: 10px;
        }
        
        .knowledge-content p {
            color: #555;
            margin-bottom: 15px;
        }
        
        .knowledge-content ul {
            margin-left: 20px;
            margin-bottom: 15px;
        }
        
        .knowledge-content li {
            color: #555;
            margin-bottom: 5px;
        }
        
        /* 认证弹窗 */
        .auth-modal-mask {
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background-color: rgba(0, 0, 0, 0.5);
            display: none;
            align-items: center;
            justify-content: center;
            z-index: 1000;
        }
        
        .auth-modal-mask.show {
            display: flex;
        }
        
        .auth-modal {
            width: 90%;
            max-width: 400px;
            background-color: #fff;
            border-radius: 16px;
            padding: 24px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
        }
        
        .modal-title {
            font-size: 18px;
            font-weight: 700;
            margin-bottom: 20px;
            text-align: center;
            color: #2c3e50;
        }
        
        .form-item {
            margin-bottom: 20px;
        }
        
        .label {
            display: block;
            font-size: 14px;
            margin-bottom: 8px;
            color: #333;
            font-weight: 500;
        }
        
        .input {
            width: 100%;
            height: 44px;
            padding: 0 16px;
            border: 1px solid #e1e8ed;
            border-radius: 8px;
            font-size: 14px;
            box-sizing: border-box;
            transition: border-color 0.3s;
        }
        
        .input:focus {
            border-color: #3498db;
            outline: none;
            box-shadow: 0 0 0 2px rgba(52, 152, 219, 0.1);
        }
        
        .upload-cert-area {
            width: 100%;
            height: 100px;
            border: 2px dashed #ddd;
            border-radius: 8px;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            cursor: pointer;
            transition: all 0.3s;
        }
        
        .upload-cert-area:hover {
            border-color: #3498db;
            background: #f8fafc;
        }
        
        .modal-btns {
            display: flex;
            justify-content: space-between;
            margin-top: 24px;
            gap: 12px;
        }
        
        .cancel-btn, .confirm-btn {
            flex: 1;
            height: 44px;
            border-radius: 8px;
            font-size: 16px;
            font-weight: 500;
            border: none;
            cursor: pointer;
            transition: all 0.3s;
        }
        
        .cancel-btn {
            background-color: #f5f5f5;
            color: #666;
        }
        
        .cancel-btn:hover {
            background-color: #e9ecef;
        }
        
        .confirm-btn {
            background: linear-gradient(135deg, #3498db, #2980b9);
            color: #fff;
        }
        
        .confirm-btn:hover {
            box-shadow: 0 4px 12px rgba(52, 152, 219, 0.3);
        }
        
        /* 加载状态 */
        .uploading-tip {
            display: none;
            align-items: center;
            justify-content: center;
            padding: 20px 0;
            flex-direction: column;
            gap: 12px;
        }
        
        .uploading-tip.show {
            display: flex;
        }
        
        .loading-icon {
            width: 32px;
            height: 32px;
            animation: rotate 1.5s linear infinite;
        }
        
        @keyframes rotate {
            from { transform: rotate(0deg); }
            to { transform: rotate(360deg); }
        }
        
        /* 隐藏的文件输入 */
        .file-input {
            display: none;
        }
        
        /* 空状态样式 */
        .empty-state {
            text-align: center;
            padding: 30px 20px;
            color: #7f8c8d;
        }
        
        .empty-icon {
            font-size: 48px;
            margin-bottom: 12px;
        }
        
        /* 知识库详情模态框 */
        .knowledge-modal-mask {
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background-color: rgba(0, 0, 0, 0.5);
            display: none;
            align-items: center;
            justify-content: center;
            z-index: 1000;
        }
        
        .knowledge-modal-mask.show {
            display: flex;
        }
        
        .knowledge-modal {
            width: 90%;
            max-width: 600px;
            max-height: 80vh;
            background-color: #fff;
            border-radius: 16px;
            padding: 24px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
            overflow-y: auto;
        }
        
        .knowledge-modal-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
            padding-bottom: 16px;
            border-bottom: 1px solid #f1f2f6;
        }
        
        .knowledge-modal-title {
            font-size: 20px;
            font-weight: 700;
            color: #2c3e50;
        }
        
        .close-knowledge-modal {
            background: none;
            border: none;
            font-size: 24px;
            cursor: pointer;
            color: #7f8c8d;
            width: 32px;
            height: 32px;
            display: flex;
            align-items: center;
            justify-content: center;
            border-radius: 50%;
            transition: background 0.2s;
        }
        
        .close-knowledge-modal:hover {
            background: #f1f2f6;
        }
        
        .knowledge-content {
            line-height: 1.6;
        }
        
        .knowledge-section {
            margin-bottom: 20px;
        }
        
        .knowledge-subtitle {
            font-size: 16px;
            font-weight: 600;
            color: #2c3e50;
            margin-bottom: 10px;
            padding-bottom: 8px;
            border-bottom: 1px solid #f1f2f6;
        }
        
        .knowledge-list {
            padding-left: 20px;
        }
        
        .knowledge-list li {
            margin-bottom: 8px;
        }
        
        /* 响应式调整 */
        @media (min-width: 768px) {
            .knowledge-categories {
                grid-template-columns: repeat(3, 1fr);
            }
        }

        .empty-state-hint {
            font-size: 14px;
            margin-top: 8px;
            color: #7f8c8d;
        }
        
        .image-preview-section {
            display: none;
        }
        
        .image-preview-section.show {
            display: block;
        }  
        .hidden {
    display: none;
}             

/* 字体图标支持 */
.fas {
  font-size: 16px;
}

/* 认证状态样式 */
.auth-status.verified {
  background: rgba(76, 175, 80, 0.8);
}

/* 空状态提示 */
.empty-state-hint {
  font-size: 14px;
  margin-top: 8px;
  color: #7f8c8d;
}

/* 辅助标签 */
.auxiliary-tag {
  padding: 4px 10px;
  background: #fff3e0;
  color: #e65100;
  font-size: 12px;
  border-radius: 12px;
  font-weight: 500;
}

/* 拖拽手柄 */
.drag-handle {
  display: flex;
  align-items: center;
  color: #7f8c8d;
  font-size: 14px;
}

.drag-handle::after {
  content: "⋮⋮";
  font-size: 16px;
  transform: rotate(90deg);
  margin-left: 8px;
}

/* 上传提示 */
.uploading-tip {
  display: none;
  align-items: center;
  justify-content: center;
  padding: 20px 0;
  flex-direction: column;
  gap: 12px;
}

.uploading-tip.show {
  display: flex;
}

.loading-icon {
  width: 32px;
  height: 32px;
  animation: rotate 1.5s linear infinite;
}

@keyframes rotate {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

/* 认证徽章 */
.auth-badge {
  display: flex;
  justify-content: center;
  margin-top: 12px;
}

.badge {
  background: #fff3cd;
  color: #856404;
  padding: 6px 12px;
  border-radius: 16px;
  font-size: 12px;
  display: flex;
  align-items: center;
  gap: 6px;
}

/* 知识库分类图标 */
.category-icon {
  width: 48px;
  height: 48px;
  margin-bottom: 12px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  background: #f8f9fa;
}

/* 响应式调整 */
@media (min-width: 768px) {
  .knowledge-categories {
    grid-template-columns: repeat(3, 1fr);
  }
}
</style>

<style>
/* 全局样式，用于知识库模态框的内容 */
.knowledge-list {
  padding-left: 20px;
}

.knowledge-list li {
  margin-bottom: 8px;
}
</style>