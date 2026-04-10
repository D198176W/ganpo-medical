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
              <div class="badge">
                <span>🔒</span>
                <span>已认证用户可批量上传</span>
              </div>
            </div>
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
          <div class="analysis-sidebar" :class="{ show: showAnalysisResult }" v-if="showAnalysisResult" :style="{ height: sidebarHeight + 'px' }">
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
    <div class="knowledge-modal-mask" v-if="showKnowledgeModal">
      <div class="knowledge-modal">
        <div class="knowledge-modal-header">
          <h2 class="knowledge-modal-title">{{ currentKnowledgeTitle }}</h2>
          <button class="close-knowledge-modal" @click="closeKnowledgeModal">×</button>
        </div>
        <div class="knowledge-content" v-html="currentKnowledgeContent"></div>
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
    <div class="auth-modal-mask" v-if="showAuthModal">
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
      userId: null,
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
    async handleLogout() {
      if (confirm('确定要退出医生认证吗？')) {
        // 可调用后端登出接口（如果有）
        this.isVerifiedDoctor = false;
        this.doctorName = '';
        this.showAuthMenu = false;
        // 更新本地 userInfo
        try {
          const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}');
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
        const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}');
        this.userId = userInfo.userId || userInfo.id || ('u-' + Date.now());
      }
      if (!this.sessionId) {
        this.sessionId = localStorage.getItem('pathologySessionId') || this.generateSessionId();
      }

      const payload = {
        userId: this.userId,
        role: this.isVerifiedDoctor ? 'doctor' : 'user',
        query: question,
        sessionId: this.sessionId
      };

      try {
        const resp = await axios.post('/api/chat', payload);
        // 期望后端返回结构示例：{ answer: '...', sources: ['http...'], id: '...', time: '...' }
        const data = resp && resp.data ? resp.data : {};
        const answer = data.answer || data.content || data.reply || '未返回结果';
        // sources 可能是数组或字符串
        let source = '';
        if (Array.isArray(data.sources) && data.sources.length > 0) {
          source = data.sources[0];
        } else if (data.source) {
          source = data.source;
        } else if (typeof data.sources === 'string') {
          source = data.sources;
        }

        const aiMsg = {
          id: Date.now() + 1,
          type: 'ai',
          content: answer,
          source: source,
          sourceLabel: data.sourceLabel,
          time: new Date().toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'})
        };
        this.chatHistory.push(aiMsg);
        // 可选：如果后端返回新的 sessionId，更新本地 sessionId
        if (data.sessionId) {
          this.sessionId = data.sessionId;
          localStorage.setItem('pathologySessionId', this.sessionId);
        }
      } catch (err) {
        console.error('聊天接口出错：', err);
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
        localStorage.removeItem('pathologyChatHistory');
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
            // 先显示本地预览（可在上传成功后替换为后端返回的 url）
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

            // 开始上传到后端 /api/images/upload（返回 ImageEntity）
            const form = new FormData();
            form.append('file', file);
            form.append('userId', this.userId || '');
            // 如果后端需要 sessionId 可以追加
            form.append('sessionId', this.sessionId || '');

            this.showUploadTip = true;
            const uploadResp = await axios.post('/api/images/upload', form, {
              headers: { 'Content-Type': 'multipart/form-data' }
            });

            const uploadedEntity = uploadResp && uploadResp.data ? uploadResp.data : null;
            // 期望 uploadedEntity 包含 id, url
            const finalImage = {
              id: (uploadedEntity && (uploadedEntity.id || uploadedEntity.imageId)) || ('img-' + Date.now()),
              name: file.name,
              url: (uploadedEntity && (uploadedEntity.url || uploadedEntity.path)) || localUrl
            };

            // 替换临时项（以 id 为 key）
            const idx = this.uploadedImages.findIndex(it => it.id === tempId);
            if (idx !== -1) {
              this.$set(this.uploadedImages, idx, finalImage);
            } else {
              this.uploadedImages.push(finalImage);
            }

            // 上传成功后请求分析接口：GET /api/images/{id}/analysis
            try {
              const imageId = finalImage.id;
              // 后端可能需要不同的路径或额外参数：请按后端实际接口调整
              const analysisResp = await axios.get(`/api/images/${imageId}/analysis`, {
                params: { userId: this.userId }
              });
              const analysisData = analysisResp && analysisResp.data ? analysisResp.data : null;

              // 映射到前端 analysisResult（兼容多种返回字段）
              if (analysisData) {
                this.analysisResult = {
                  conclusion: analysisData.conclusion || analysisData.result || '',
                  features: analysisData.features || analysisData.featureList || [],
                  referenceCases: analysisData.referenceCases || analysisData.references || [],
                  confidence: analysisData.confidence || analysisData.confidenceScore || '',
                  suggestion: analysisData.suggestion || analysisData.recommendation || ''
                };
                this.showAnalysisResult = true;
              }
            } catch (analysisErr) {
              console.warn('图片分析接口出错：', analysisErr);
              // 即使分析失败，也保留已上传图片
            }
          } catch (err) {
            console.error('文件上传失败：', err);
            alert(`上传 "${file.name}" 失败，请重试`);
          } finally {
            this.showUploadTip = false;
          }
        }
      }
      // 重置 input 值，以便能重复选择同一文件
      e.target.value = '';
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
      try {
        const resp = await axios.get(`/api/knowledge/category/${encodeURIComponent(id)}`);
        const data = resp && resp.data ? resp.data : null;
        if (data) {
          // 根据后端返回的结构调整
          this.currentKnowledgeTitle = data.title || name || this.knowledgeData?.[id]?.title || name;
          this.currentKnowledgeContent = data.content || data.body || '<p>暂无内容</p>';
          this.showKnowledgeModal = true;
        } else {
          // fallback to local knowledgeData if available
          if (this.knowledgeData && this.knowledgeData[id]) {
            this.currentKnowledgeTitle = this.knowledgeData[id].title;
            this.currentKnowledgeContent = this.knowledgeData[id].content;
            this.showKnowledgeModal = true;
          } else {
            alert('未能获取知识库内容');
          }
        }
      } catch (err) {
        console.error('获取知识库分类出错：', err);
        // fallback: 本地静态数据（如果存在）
        if (this.knowledgeData && this.knowledgeData[id]) {
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
            const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}');
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
    try {
      const userInfo = JSON.parse(localStorage.getItem('userInfo')) || {};
      this.isVerifiedDoctor = userInfo.userTag === 'doctor';
      this.doctorName = userInfo.nickName || '';
      this.userId = userInfo.userId || userInfo.id || ('u-' + Date.now());
    } catch (e) {
      this.userId = 'u-' + Date.now();
    }

    const savedSession = localStorage.getItem('pathologySessionId');
    if (savedSession) {
      this.sessionId = savedSession;
    } else {
      this.sessionId = this.generateSessionId();
      localStorage.setItem('pathologySessionId', this.sessionId);
    }

    // 加载聊天历史
    try {
      const history = JSON.parse(localStorage.getItem('pathologyChatHistory')) || [];
      this.chatHistory = history;
    } catch (e) {
      this.chatHistory = [];
    }

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
    } catch (e) { /* ignore */ }

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
            <h3 class="knowledge-subtitle">肿瘤分类</h3>
            <p>肿瘤病理学主要研究肿瘤的形态学特征、生物学行为及其与临床的关系。根据组织来源和生物学行为，肿瘤可分为以下几类：</p>
            <ul class="knowledge-list">
              <li><strong>上皮组织肿瘤</strong>：包括鳞状细胞癌、腺癌、移行细胞癌等</li>
              <li><strong>间叶组织肿瘤</strong>：包括纤维肉瘤、脂肪肉瘤、骨肉瘤等</li>
              <li><strong>淋巴造血组织肿瘤</strong>：包括淋巴瘤、白血病等</li>
              <li><strong>神经组织肿瘤</strong>：包括胶质瘤、神经鞘瘤等</li>
              <li><strong>其他肿瘤</strong>：包括黑色素瘤、生殖细胞肿瘤等</li>
            </ul>
          </div>
          <div class="knowledge-section">
            <h3 class="knowledge-subtitle">肿瘤分级与分期</h3>
            <p>肿瘤的分级和分期是评估肿瘤恶性程度和扩散范围的重要指标：</p>
            <ul class="knowledge-list">
              <li><strong>分级（Grade）</strong>：根据肿瘤细胞的分化程度、异型性和核分裂象数量分为G1-G4级</li>
              <li><strong>分期（Stage）</strong>：根据TNM系统评估原发肿瘤大小（T）、淋巴结转移（N）和远处转移（M）</li>
            </ul>
          </div>
        `
      },
      tissue: {
        title: "组织病理",
        content: `<div class="knowledge-section"><h3 class="knowledge-subtitle">组织病理学基础</h3><p>组织病理学是研究疾病状态下组织结构和细胞变化的学科。</p></div>`
      },
      cell: {
        title: "细胞病理",
        content: `<div class="knowledge-section"><h3 class="knowledge-subtitle">细胞病理学概述</h3><p>细胞病理学是通过检查单个细胞或细胞群来诊断疾病的学科。</p></div>`
      }
      // 其他分类可按需补齐
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
            display: none;
            z-index: 101;
        }
        
        .auth-menu.show {
            display: block;
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
        }
        
        .analysis-sidebar.show {
            display: block;
            animation: fadeIn 0.5s ease;
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