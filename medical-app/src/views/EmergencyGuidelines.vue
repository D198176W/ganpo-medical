<template>
  <div class="emergency-guidelines">
    <!-- 顶部导航栏 -->
    <header class="header">
      <div class="header-container">
        <div class="nav-left">
          <button 
            class="back-btn" 
            aria-label="返回上一页"
            @click="handleBack"
          >
            <i class="fa fa-arrow-left"></i>
          </button>
          <h1 class="title">创业园区应急指引</h1>
        </div>
        <button 
          class="emergency-call-btn" 
          @click="showCallModal = true"
        >
          <i class="fa fa-phone"></i>
          <span>紧急呼叫</span>
        </button>
      </div>
    </header>

    <!-- 主内容区 -->
    <main class="main-content">
      <!-- 紧急联系方式卡片 -->
      <div class="emergency-contact-card">
        <h2 class="section-title">
          <i class="fa fa-exclamation-triangle"></i> 紧急联系电话
        </h2>
        <div class="contact-grid">
          <div class="contact-item">
            <div class="contact-number">119</div>
            <div class="contact-label">消防救援</div>
          </div>
          <div class="contact-item">
            <div class="contact-number">120</div>
            <div class="contact-label">医疗急救</div>
          </div>
          <div class="contact-item">
            <div class="contact-number">110</div>
            <div class="contact-label">报警电话</div>
          </div>
          <div class="contact-item">
            <div class="contact-number">0791-8866999</div>
            <div class="contact-label">园区安全</div>
          </div>
        </div>
      </div>

      <!-- 应急分类 -->
      <h2 class="section-title">
        <i class="fa fa-list-alt"></i> 应急情况分类
      </h2>
      <div class="emergency-grid">
        <!-- 火灾应急 -->
        <div class="emergency-card fire-card" @click="showEmergencyDetail('fire')">
          <div class="card-header">
            <div class="card-icon-container">
              <div class="card-icon fire-icon">
                <i class="fa fa-fire"></i>
              </div>
              <div class="card-text">
                <h3 class="card-title">火灾应急</h3>
                <p class="card-subtitle">火灾逃生与应对措施</p>
              </div>
            </div>
            <i class="fa fa-chevron-right"></i>
          </div>
          <div class="card-description">
            包含火灾报警、初期火灾扑救、安全疏散等应急处理措施
          </div>
        </div>

        <!-- 安全保卫 -->
        <div class="emergency-card security-card" @click="showEmergencyDetail('security')">
          <div class="card-header">
            <div class="card-icon-container">
              <div class="card-icon security-icon">
                <i class="fa fa-shield"></i>
              </div>
              <div class="card-text">
                <h3 class="card-title">安全保卫</h3>
                <p class="card-subtitle">治安事件应对措施</p>
              </div>
            </div>
            <i class="fa fa-chevron-right"></i>
          </div>
          <div class="card-description">
            包含可疑人员处置、盗窃事件、滋扰事件应对方法
          </div>
        </div>

        <!-- 医疗急救 -->
        <div class="emergency-card medical-card" @click="showEmergencyDetail('medical')">
          <div class="card-header">
            <div class="card-icon-container">
              <div class="card-icon medical-icon">
                <i class="fa fa-medkit"></i>
              </div>
              <div class="card-text">
                <h3 class="card-title">医疗急救</h3>
                <p class="card-subtitle">常见伤病应急处理</p>
              </div>
            </div>
            <i class="fa fa-chevron-right"></i>
          </div>
          <div class="card-description">
            包含外伤处理、突发疾病、心肺复苏等急救知识
          </div>
        </div>

        <!-- 设施故障 -->
        <div class="emergency-card facility-card" @click="showEmergencyDetail('facility')">
          <div class="card-header">
            <div class="card-icon-container">
              <div class="card-icon facility-icon">
                <i class="fa fa-wrench"></i>
              </div>
              <div class="card-text">
                <h3 class="card-title">设施故障</h3>
                <p class="card-subtitle">水电设备应急处理</p>
              </div>
            </div>
            <i class="fa fa-chevron-right"></i>
          </div>
          <div class="card-description">
            包含停电、漏水、电梯故障等设施问题处理方式
          </div>
        </div>
      </div>

      <!-- 应急设施分布 -->
      <div class="facility-section">
        <h2 class="section-title">
          <i class="fa fa-map-marker"></i> 应急设施分布
        </h2>
        <div class="map-container">
          <img src="https://picsum.photos/id/1019/800/400" alt="创业园区地图" class="map-image">
          <div class="map-marker" style="top: 30%; left: 60%;" @click="showFacilityDetail('fire-extinguisher-1')">
            <i class="fa fa-fire-extinguisher"></i>
          </div>
          <div class="map-marker" style="top: 60%; left: 70%;" @click="showFacilityDetail('fire-extinguisher-2')">
            <i class="fa fa-fire-extinguisher"></i>
          </div>
          <div class="map-marker" style="top: 45%; left: 50%;" @click="showFacilityDetail('first-aid')">
            <i class="fa fa-medkit"></i>
          </div>
          <div class="map-marker" style="top: 20%; left: 80%;" @click="showFacilityDetail('security-room')">
            <i class="fa fa-shield"></i>
          </div>
          <div class="map-marker" style="top: 70%; left: 30%;" @click="showFacilityDetail('evacuation-exit')">
            <i class="fa fa-sign-out"></i>
          </div>
        </div>
        <div class="facility-legend">
          <div class="legend-item">
            <i class="fa fa-fire-extinguisher"></i>
            <span>灭火器</span>
          </div>
          <div class="legend-item">
            <i class="fa fa-medkit"></i>
            <span>急救点</span>
          </div>
          <div class="legend-item">
            <i class="fa fa-shield"></i>
            <span>保安室</span>
          </div>
          <div class="legend-item">
            <i class="fa fa-sign-out"></i>
            <span>疏散出口</span>
          </div>
          <div class="legend-item">
            <i class="fa fa-phone"></i>
            <span>紧急电话</span>
          </div>
        </div>
      </div>

      <!-- 最近应急点 -->
      <div class="emergency-services">
        <h2 class="section-title">
          <i class="fa fa-ambulance"></i> 最近应急服务点
        </h2>
        <div class="services-list">
          <div class="service-item">
            <div class="service-icon medical-service-icon">
              <i class="fa fa-hospital-o"></i>
            </div>
            <div class="service-details">
              <div class="service-header">
                <h3 class="service-name">园区医务室</h3>
                <span class="service-distance">距离 200米</span>
              </div>
              <p class="service-description">1号楼一层东侧，24小时值班</p>
              <div class="service-actions">
                <button class="action-btn call-btn" @click="makeCall('0791-88669120', '园区医务室')">
                  <i class="fa fa-phone"></i> 呼叫
                </button>
                <button class="action-btn navigate-btn" @click="navigateTo('园区医务室')">
                  <i class="fa fa-map-marker"></i> 导航
                </button>
              </div>
            </div>
          </div>

          <div class="service-item">
            <div class="service-icon security-service-icon">
              <i class="fa fa-shield"></i>
            </div>
            <div class="service-details">
              <div class="service-header">
                <h3 class="service-name">园区保安亭</h3>
                <span class="service-distance">距离 150米</span>
              </div>
              <p class="service-description">园区主入口处，24小时值班</p>
              <div class="service-actions">
                <button class="action-btn call-btn" @click="makeCall('0791-88669999', '园区保安亭')">
                  <i class="fa fa-phone"></i> 呼叫
                </button>
                <button class="action-btn navigate-btn" @click="navigateTo('园区保安亭')">
                  <i class="fa fa-map-marker"></i> 导航
                </button>
              </div>
            </div>
          </div>

          <div class="service-item">
            <div class="service-icon hospital-service-icon">
              <i class="fa fa-ambulance"></i>
            </div>
            <div class="service-details">
              <div class="service-header">
                <h3 class="service-name">附近医院</h3>
                <span class="service-distance">距离 1.2公里</span>
              </div>
              <p class="service-description">南昌市第一人民医院，急诊科24小时开放</p>
              <div class="service-actions">
                <button class="action-btn call-btn" @click="makeCall('0791-88881120', '南昌市第一人民医院')">
                  <i class="fa fa-phone"></i> 呼叫
                </button>
                <button class="action-btn navigate-btn" @click="navigateTo('南昌市第一人民医院')">
                  <i class="fa fa-map-marker"></i> 导航
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>

    <!-- 模态框组件 -->
    <div 
      v-if="showDetailModal" 
      class="modal-overlay"
      @click.self="closeDetailModal"
    >
      <div class="modal-content detail-modal">
        <div class="modal-header">
          <h3 class="modal-title">{{ currentEmergencyDetail.title }}</h3>
          <button 
            @click="closeDetailModal" 
            class="close-btn" 
            aria-label="关闭弹窗"
          >
            <i class="fa fa-times"></i>
          </button>
        </div>
        <div class="modal-body">
          <div class="detail-section">
            <h4 class="detail-section-title">情况概述</h4>
            <p class="detail-text">{{ currentEmergencyDetail.overview }}</p>
          </div>
          
          <div class="detail-section">
            <h4 class="detail-section-title">应急处理步骤</h4>
            <div class="steps-list">
              <div 
                v-for="(step, index) in currentEmergencyDetail.steps" 
                :key="index" 
                class="step-item"
              >
                <div class="step-number">{{ index + 1 }}</div>
                <div class="step-text">{{ step }}</div>
              </div>
            </div>
          </div>
          
          <!-- 火灾应急额外内容 -->
          <div v-if="currentEmergencyType === 'fire'" class="detail-section">
            <h4 class="detail-section-title">预防措施</h4>
            <ul class="detail-list">
              <li 
                v-for="(item, index) in currentEmergencyDetail.precautions" 
                :key="index" 
                class="detail-list-item"
              >
                <i class="fa fa-check-circle"></i>
                <span>{{ item }}</span>
              </li>
            </ul>
          </div>
          
          <div v-if="currentEmergencyType === 'fire'" class="detail-section">
            <h4 class="detail-section-title">消防设施</h4>
            <ul class="detail-list">
              <li 
                v-for="(item, index) in currentEmergencyDetail.equipment" 
                :key="index" 
                class="detail-list-item"
              >
                <i class="fa fa-fire-extinguisher"></i>
                <span>{{ item }}</span>
              </li>
            </ul>
          </div>
          
          <!-- 医疗应急额外内容 -->
          <div v-if="currentEmergencyType === 'medical'" class="detail-section">
            <h4 class="detail-section-title">常见伤病处理</h4>
            <ul class="detail-list medical-list">
              <li 
                v-for="(item, index) in currentEmergencyDetail.common" 
                :key="index" 
                class="detail-list-item"
              >
                <i class="fa fa-medkit"></i>
                <div class="medical-item">
                  <span class="medical-condition">{{ item.split('：')[0] }}：</span>
                  <span class="medical-handling">{{ item.split('：')[1] }}</span>
                </div>
              </li>
            </ul>
          </div>
          
          <div v-if="currentEmergencyType === 'medical'" class="detail-section">
            <h4 class="detail-section-title">医疗设施</h4>
            <ul class="detail-list">
              <li 
                v-for="(item, index) in currentEmergencyDetail.facilities" 
                :key="index" 
                class="detail-list-item"
              >
                <i class="fa fa-hospital-o"></i>
                <span>{{ item }}</span>
              </li>
            </ul>
          </div>
          
          <!-- 安全保卫额外内容 -->
          <div v-if="currentEmergencyType === 'security'" class="detail-section">
            <h4 class="detail-section-title">预防措施</h4>
            <ul class="detail-list">
              <li 
                v-for="(item, index) in currentEmergencyDetail.prevention" 
                :key="index" 
                class="detail-list-item"
              >
                <i class="fa fa-shield"></i>
                <span>{{ item }}</span>
              </li>
            </ul>
          </div>
          
          <div v-if="currentEmergencyType === 'security'" class="detail-section">
            <h4 class="detail-section-title">联系方式</h4>
            <ul class="detail-list">
              <li 
                v-for="(item, index) in currentEmergencyDetail.contact" 
                :key="index" 
                class="detail-list-item"
              >
                <i class="fa fa-phone"></i>
                <span>{{ item }}</span>
              </li>
            </ul>
          </div>
          
          <!-- 设施故障额外内容 -->
          <div v-if="currentEmergencyType === 'facility'" class="detail-section">
            <h4 class="detail-section-title">维修联系方式</h4>
            <ul class="detail-list">
              <li 
                v-for="(item, index) in currentEmergencyDetail.contact" 
                :key="index" 
                class="detail-list-item"
              >
                <i class="fa fa-wrench"></i>
                <span>{{ item }}</span>
              </li>
            </ul>
          </div>
          
          <div v-if="currentEmergencyType === 'facility'" class="detail-section">
            <h4 class="detail-section-title">安全提示</h4>
            <ul class="detail-list">
              <li 
                v-for="(item, index) in currentEmergencyDetail.tips" 
                :key="index" 
                class="detail-list-item"
              >
                <i class="fa fa-lightbulb-o"></i>
                <span>{{ item }}</span>
              </li>
            </ul>
          </div>
        </div>
        <div class="modal-footer">
          <button 
            @click="closeDetailModal" 
            class="confirm-btn"
          >
            <i class="fa fa-check"></i> 我知道了
          </button>
        </div>
      </div>
    </div>

    <!-- 设施详情弹窗 -->
    <div 
      v-if="showFacilityModal" 
      class="modal-overlay"
      @click.self="closeFacilityModal"
    >
      <div class="modal-content facility-modal">
        <div class="modal-header">
          <h3 class="modal-title">{{ currentFacilityDetail.name }}</h3>
          <button 
            @click="closeFacilityModal" 
            class="close-btn" 
            aria-label="关闭弹窗"
          >
            <i class="fa fa-times"></i>
          </button>
        </div>
        <div class="modal-body">
          <p class="facility-info"><i class="fa fa-map-marker"></i> 位置：{{ currentFacilityDetail.location }}</p>
          
          <p v-if="currentFacilityDetail.type" class="facility-info">
            <i class="fa fa-tag"></i> 类型：{{ currentFacilityDetail.type }}
          </p>
          
          <div v-if="currentFacilityDetail.equipment" class="facility-equipment">
            <p class="facility-subtitle">配备设备：</p>
            <ul class="equipment-list">
              <li 
                v-for="(item, index) in currentFacilityDetail.equipment" 
                :key="index" 
                class="equipment-item"
              >
                <i class="fa fa-check"></i>{{ item }}
              </li>
            </ul>
          </div>
          
          <p v-if="currentFacilityDetail.description" class="facility-description">{{ currentFacilityDetail.description }}</p>
          
          <p v-if="currentFacilityDetail.checkDate" class="facility-info">
            <i class="fa fa-calendar"></i> 最近检查：{{ currentFacilityDetail.checkDate }}
          </p>
        </div>
        <div class="modal-footer">
          <button 
            class="navigate-btn" 
            @click="navigateToFacility"
          >
            <i class="fa fa-map-marker"></i> 导航前往
          </button>
        </div>
      </div>
    </div>

    <!-- 紧急呼叫弹窗 -->
    <div 
      v-if="showCallModal" 
      class="modal-overlay"
      @click.self="closeCallModal"
    >
      <div class="modal-content call-modal">
        <div class="call-modal-header">
          <div class="call-icon-container">
            <i class="fa fa-phone"></i>
          </div>
          <h3 class="call-modal-title">紧急呼叫</h3>
          <p class="call-modal-subtitle">请选择需要呼叫的紧急服务</p>
        </div>
        <div class="call-options">
          <button 
            class="call-option fire-call" 
            @click="makeCall('119', '消防救援')"
          >
            <div class="call-option-text">
              <div class="call-service">消防救援</div>
              <div class="call-description">火灾、爆炸等紧急情况</div>
            </div>
            <div class="call-number">119</div>
          </button>
          <button 
            class="call-option medical-call" 
            @click="makeCall('120', '医疗急救')"
          >
            <div class="call-option-text">
              <div class="call-service">医疗急救</div>
              <div class="call-description">重伤、急病等医疗紧急情况</div>
            </div>
            <div class="call-number">120</div>
          </button>
          <button 
            class="call-option police-call" 
            @click="makeCall('110', '报警电话')"
          >
            <div class="call-option-text">
              <div class="call-service">报警电话</div>
              <div class="call-description">盗窃、滋扰等治安事件</div>
            </div>
            <div class="call-number">110</div>
          </button>
          <button 
            class="call-option security-call" 
            @click="makeCall('0791-88669999', '园区安保')"
          >
            <div class="call-option-text">
              <div class="call-service">园区安保</div>
              <div class="call-description">园区内安全问题</div>
            </div>
            <div class="call-number">0791-88669999</div>
          </button>
        </div>
        <button 
          class="cancel-btn" 
          @click="closeCallModal"
        >
          取消
        </button>
      </div>
    </div>

    <!-- 提示消息 -->
    <div 
      class="toast" 
      :class="{ show: showToast }"
    >
      {{ toastMessage }}
    </div>
  </div>
</template>

<script>
export default {
  name: 'EmergencyGuidelines',
  data() {
    return {
      showDetailModal: false,
      showFacilityModal: false,
      showCallModal: false,
      showToast: false,
      toastMessage: '',
      currentEmergencyType: '',
      currentFacilityId: '',
      
      // 应急处理详情数据
      emergencyData: {
        fire: {
          title: "火灾应急处理",
          overview: "火灾是园区内最危险的安全隐患之一，发现火情应立即采取正确措施，确保人员安全并减少财产损失。",
          steps: [
            "保持冷静，立即拨打火警电话119和园区安保电话0791-88669999，清晰说明火灾地点、火势大小和燃烧物质",
            "如果火势较小且自己有能力扑灭，可使用附近的灭火器进行初期灭火（注意：使用灭火器时需站在上风处）",
            "立即组织人员疏散，提醒大家用湿毛巾捂住口鼻，低姿前进，切勿乘坐电梯",
            "疏散时检查周围是否有被困人员，特别是卫生间和办公室内",
            "到达安全集合点后，清点人数并向救援人员报告",
            "切勿贪恋财物返回火场，等待专业消防人员到达"
          ],
          precautions: [
            "熟悉所在楼层的消防疏散通道和安全出口位置",
            "不堵塞消防通道，保持出口畅通",
            "定期检查办公室内的电器设备，避免超负荷用电",
            "不在非吸烟区吸烟，不乱扔烟头",
            "了解灭火器的正确使用方法，定期参加消防演练"
          ],
          equipment: [
            "各楼层走廊均配备干粉灭火器，位于楼梯口附近",
            "每层楼设有消防栓，使用时需连接水带和水枪",
            "走廊顶部安装有烟感报警器和喷淋系统",
            "安全出口指示灯和应急照明灯确保断电时正常工作"
          ]
        },
        medical: {
          title: "医疗急救处理",
          overview: "园区内可能发生各种突发疾病或意外伤害，及时正确的急救处理能有效挽救生命，减轻伤害。",
          steps: [
            "保持镇定，评估现场环境是否安全，避免自身受到伤害",
            "判断伤者意识和生命体征（呼吸、心跳），必要时立即拨打120急救电话",
            "对于外伤出血，立即用干净的纱布或毛巾按压止血，抬高受伤部位",
            "如遇骨折，尽量保持伤处固定，不要随意搬动，等待专业医护人员处理",
            "如遇有人晕倒且无呼吸心跳，立即进行心肺复苏（CPR）",
            "记录伤者情况和发病时间，向医护人员准确描述",
            "派人到园区入口引导救护车快速到达"
          ],
          common: [
            "轻微割伤：用清水冲洗，涂抹碘伏消毒，贴上创可贴",
            "烧烫伤：立即用冷水冲洗15-20分钟，不要涂抹牙膏等物质，严重时就医",
            "扭伤：遵循RICE原则（休息、冰敷、加压、抬高）",
            "中暑：立即移至阴凉处，解开衣物，补充含盐水分，用湿毛巾降温",
            "过敏反应：远离过敏原，服用抗过敏药物，严重时立即就医"
          ],
          facilities: [
            "园区1号楼一层设有医务室，配备基础医疗设备和药品",
            "各楼层茶水间配备简易急救箱，包含创可贴、碘伏、纱布等基础用品",
            "园区保安亭配备AED（自动体外除颤器）",
            "医务室工作时间：周一至周五 8:30-18:00，其他时间可联系值班医生"
          ]
        },
        security: {
          title: "安全保卫处理",
          overview: "园区安全是保障正常工作的基础，遇到治安事件应保持警惕，及时采取正确措施保护自身安全。",
          steps: [
            "遇到可疑人员或异常情况，保持距离，不要惊动对方，立即联系园区安保（0791-88669999）",
            "如遇盗窃或滋扰事件，确保自身安全为先，不要与对方发生正面冲突",
            "尽量记住可疑人员的特征（身高、衣着、相貌等）和行为举止",
            "如事件正在发生，悄悄用手机录像或拍照作为证据（注意隐蔽）",
            "如遇紧急情况，立即拨打110报警，并向园区安保说明情况",
            "配合安保人员和警方调查，提供准确信息"
          ],
          prevention: [
            "离开办公室时锁好门窗，贵重物品妥善保管",
            "不随意透露个人和公司敏感信息给陌生人",
            "为电脑等重要设备设置密码，重要文件及时备份",
            "发现安全隐患（如门锁损坏、监控故障）及时向物业报告",
            "夜间单独离开园区时，可要求安保人员护送"
          ],
          contact: [
            "园区安保24小时热线：0791-88669999",
            "辖区派出所：0791-88661110",
            "园区物业安全部：0791-88669001",
            "紧急报警：110"
          ]
        },
        facility: {
          title: "设施故障处理",
          overview: "园区内的水电、电梯等设施可能发生故障，正确的应急处理能避免事故扩大，保障人员安全。",
          steps: [
            "发生停电时，保持冷静，使用手机照明，不要惊慌拥挤",
            "电梯故障被困时，使用电梯内紧急呼叫按钮求救，保持体力等待救援，不要强行扒门",
            "发现水管破裂漏水时，立即关闭附近阀门，远离积水区域，防止触电",
            "电路短路或电器冒烟时，立即切断电源，不要用水灭火，拨打物业维修电话",
            "空调系统故障导致室温异常时，打开窗户通风，联系物业处理",
            "任何设施故障都应及时向物业报修，说明具体位置和情况"
          ],
          contact: [
            "物业维修部（24小时）：0791-88669002",
            "电力故障紧急处理：0791-88669003",
            "给排水维修：0791-88669004",
            "电梯维保紧急电话：0791-88669005"
          ],
          tips: [
            "熟悉所在楼层的紧急电源开关和水阀位置",
            "定期检查办公室内电器线路，避免老化短路",
            "雷雨天气关闭不必要的电器设备，拔掉电源插头",
            "不要在卫生间、茶水间等潮湿区域使用大功率电器",
            "发现设施异常声响或气味时，立即停止使用并报告"
          ]
        }
      },
      
      // 应急设施详情数据
      facilityData: {
        "fire-extinguisher-1": {
          name: "1号灭火器箱",
          location: "2号楼一层楼梯口",
          type: "ABC干粉灭火器",
          capacity: "4公斤",
          checkDate: "2023年10月15日",
          description: "适用于扑灭固体物质、液体和气体火灾，以及电器火灾。使用时拔掉保险销，握住喷管对准火焰根部，按下压把喷射。"
        },
        "fire-extinguisher-2": {
          name: "2号灭火器箱",
          location: "3号楼二层走廊中部",
          type: "ABC干粉灭火器",
          capacity: "4公斤",
          checkDate: "2023年10月15日",
          description: "适用于扑灭固体物质、液体和气体火灾，以及电器火灾。使用时拔掉保险销，握住喷管对准火焰根部，按下压把喷射。"
        },
        "first-aid": {
          name: "园区急救点",
          location: "1号楼一层大厅",
          equipment: ["急救箱", "AED自动除颤器", "担架", "氧气瓶"],
          service: "提供基础急救处理，可联系值班医护人员",
          hours: "24小时开放"
        },
        "security-room": {
          name: "主保安室",
          location: "园区主入口右侧",
          function: ["24小时安保值班", "监控中心", "访客登记", "应急物资储备"],
          staff: "4名安保人员轮班",
          equipment: ["对讲机", "应急照明", "防爆设备", "消防应急包"]
        },
        "evacuation-exit": {
          name: "主要疏散出口",
          location: "4号楼西侧",
          width: "1.5米",
          capacity: "可同时容纳30人通过",
          direction: "通向园区紧急集合点",
          notes: "出口处设有应急照明和疏散指示标志，保持24小时畅通"
        }
      }
    };
  },
  computed: {
    currentEmergencyDetail() {
      return this.emergencyData[this.currentEmergencyType] || {};
    },
    currentFacilityDetail() {
      return this.facilityData[this.currentFacilityId] || {};
    }
  },
  methods: {
    handleBack() {
      this.showToastMessage('返回上一页');
      setTimeout(() => {
        window.history.back();
      }, 500);
    },
    
    showEmergencyDetail(type) {
      this.currentEmergencyType = type;
      this.showDetailModal = true;
    },
    
    closeDetailModal() {
      this.showDetailModal = false;
    },
    
    showFacilityDetail(facilityId) {
      this.currentFacilityId = facilityId;
      this.showFacilityModal = true;
    },
    
    closeFacilityModal() {
      this.showFacilityModal = false;
    },
    
    closeCallModal() {
      this.showCallModal = false;
    },
    
    makeCall(number, name) {
      this.showToastMessage(`正在拨打 ${name} ${number}`);
      this.showCallModal = false;
      // 实际应用中可调用拨号功能
      // window.location.href = `tel:${number}`;
    },
    
    navigateTo(location) {
      this.showToastMessage(`正在导航至${location}`);
      // 实际应用中可调用地图导航功能
    },
    
    navigateToFacility() {
      const facility = this.currentFacilityDetail;
      this.showToastMessage(`正在导航至${facility.name}`);
      this.closeFacilityModal();
    },
    
    showToastMessage(message) {
      this.toastMessage = message;
      this.showToast = true;
      setTimeout(() => {
        this.showToast = false;
      }, 2000);
    }
  }
};
</script>

<style scoped>
@import url('https://cdn.jsdelivr.net/npm/font-awesome@4.7.0/css/font-awesome.min.css');

/* 基础样式 */
* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

body {
  font-family: 'PingFang SC', 'Helvetica Neue', Arial, sans-serif;
  background-color: #f3f4f6;
  color: #1f2937;
  line-height: 1.5;
}

/* 头部样式 */
.header {
  background-color: #1E40AF;
  color: white;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 50;
  height: 64px;
}

.header-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 16px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
}

.nav-left {
  display: flex;
  align-items: center;
  gap: 8px;
}

.back-btn {
  padding: 8px;
  border-radius: 9999px;
  transition: background-color 0.2s;
  background: none;
  border: none;
  color: white;
  cursor: pointer;
}

.back-btn:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

.title {
  font-size: 20px;
  font-weight: 600;
}

.emergency-call-btn {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 8px 16px;
  border-radius: 8px;
  font-weight: 500;
  transition: all 0.2s;
  background-color: #EF4444;
  color: white;
  border: none;
  cursor: pointer;
}

.emergency-call-btn:hover {
  background-color: rgba(239, 68, 68, 0.9);
}

.emergency-call-btn:active {
  transform: scale(0.95);
}

/* 主内容区样式 */
.main-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 80px 16px 64px;
}

/* 紧急联系方式卡片 */
.emergency-contact-card {
  background: linear-gradient(to right, #EF4444, #f97316);
  border-radius: 12px;
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
  padding: 20px;
  margin-bottom: 24px;
  color: white;
}

.section-title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 16px;
  display: flex;
  align-items: center;
}

.section-title i {
  margin-right: 8px;
}

.contact-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}

@media (min-width: 768px) {
  .contact-grid {
    grid-template-columns: repeat(4, 1fr);
  }
}

.contact-item {
  background-color: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(8px);
  border-radius: 8px;
  padding: 12px;
  text-align: center;
}

.contact-number {
  font-size: 24px;
  font-weight: 700;
  margin-bottom: 4px;
}

.contact-label {
  font-size: 14px;
  opacity: 0.9;
}

/* 应急分类网格 */
.emergency-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 16px;
  margin-bottom: 32px;
}

@media (min-width: 768px) {
  .emergency-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

.emergency-card {
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
  padding: 20px;
  transition: all 0.3s;
  cursor: pointer;
  border-left-width: 4px;
}

.emergency-card:hover {
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
  transform: translateY(-4px);
}

.fire-card {
  border-left-color: #EF4444;
}

.security-card {
  border-left-color: #F59E0B;
}

.medical-card {
  border-left-color: #10B981;
}

.facility-card {
  border-left-color: #1E40AF;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 12px;
}

.card-icon-container {
  display: flex;
  align-items: center;
  gap: 12px;
}

.card-icon {
  width: 48px;
  height: 48px;
  border-radius: 9999px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
}

.fire-icon {
  background-color: rgba(239, 68, 68, 0.1);
  color: #EF4444;
}

.security-icon {
  background-color: rgba(245, 158, 11, 0.1);
  color: #F59E0B;
}

.medical-icon {
  background-color: rgba(16, 185, 129, 0.1);
  color: #10B981;
}

.facility-icon {
  background-color: rgba(30, 64, 175, 0.1);
  color: #1E40AF;
}

.card-text h3 {
  font-size: 18px;
  font-weight: 500;
  margin-bottom: 4px;
}

.card-subtitle {
  font-size: 14px;
  color: #6b7280;
}

.card-header .fa-chevron-right {
  color: #9ca3af;
}

.card-description {
  font-size: 14px;
  color: #6b7280;
}

/* 应急设施分布 */
.facility-section {
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
  padding: 20px;
  margin-bottom: 32px;
}

.map-container {
  position: relative;
  background-color: #f3f4f6;
  border-radius: 8px;
  overflow: hidden;
  height: 256px;
  margin-bottom: 16px;
}

@media (min-width: 768px) {
  .map-container {
    height: 320px;
  }
}

.map-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  opacity: 0.6;
}

.map-marker {
  position: absolute;
  width: 24px;
  height: 24px;
  background-color: #EF4444;
  color: #fff;
  border-radius: 9999px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transform: translateX(-50%) translateY(-50%);
  transition: transform 0.3s;
  font-size: 12px;
}

.map-marker:hover {
  transform: translateX(-50%) translateY(-50%) scale(1.1);
}

.facility-legend {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 8px;
  font-size: 14px;
}

@media (min-width: 768px) {
  .facility-legend {
    grid-template-columns: repeat(5, 1fr);
  }
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

.legend-item i {
  width: 16px;
}

.fa-fire-extinguisher {
  color: #EF4444;
}

.fa-medkit {
  color: #10B981;
}

.fa-shield {
  color: #F59E0B;
}

.fa-sign-out {
  color: #1E40AF;
}

.fa-phone {
  color: #8b5cf6;
}

/* 最近应急服务点 */
.emergency-services {
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.services-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.service-item {
  display: flex;
  align-items: flex-start;
  gap: 16px;
  padding: 12px;
  border-radius: 8px;
  transition: background-color 0.2s;
}

.service-item:hover {
  background-color: #f9fafb;
}

.service-icon {
  width: 40px;
  height: 40px;
  border-radius: 9999px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.medical-service-icon {
  background-color: rgba(239, 68, 68, 0.1);
  color: #EF4444;
}

.security-service-icon {
  background-color: rgba(30, 64, 175, 0.1);
  color: #1E40AF;
}

.hospital-service-icon {
  background-color: rgba(16, 185, 129, 0.1);
  color: #10B981;
}

.service-details {
  flex: 1;
}

.service-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 4px;
}

.service-name {
  font-size: 16px;
  font-weight: 500;
}

.service-distance {
  font-size: 14px;
  color: #6b7280;
}

.service-description {
  font-size: 14px;
  color: #6b7280;
  margin-bottom: 8px;
}

.service-actions {
  display: flex;
  gap: 8px;
}

.action-btn {
  padding: 6px 12px;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.2s;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 4px;
}

.call-btn {
  background-color: #1E40AF;
  color: white;
}

.call-btn:hover {
  background-color: rgba(30, 64, 175, 0.9);
}

.navigate-btn {
  background-color: #f3f4f6;
  color: #1f2937;
}

.navigate-btn:hover {
  background-color: #e5e7eb;
}

.action-btn:active {
  transform: scale(0.95);
}

/* 模态框样式 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 100;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 16px;
  animation: fadeIn 0.3s;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

.modal-content {
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1);
  animation: scaleIn 0.3s;
  max-height: 90vh;
  overflow-y: auto;
}

@keyframes scaleIn {
  from { 
    opacity: 0;
    transform: scale(0.95);
  }
  to { 
    opacity: 1;
    transform: scale(1);
  }
}

.detail-modal {
  width: 100%;
  max-width: 672px;
}

.facility-modal {
  width: 100%;
  max-width: 448px;
  padding: 20px;
}

.call-modal {
  width: 100%;
  max-width: 448px;
  padding: 20px;
}

.modal-header {
  position: sticky;
  top: 0;
  background-color: white;
  z-index: 10;
  padding: 20px;
  border-bottom: 1px solid #e5e7eb;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-title {
  font-size: 20px;
  font-weight: 600;
}

.close-btn {
  color: #9ca3af;
  background: none;
  border: none;
  cursor: pointer;
  font-size: 20px;
  transition: color 0.2s;
}

.close-btn:hover {
  color: #6b7280;
}

.modal-body {
  padding: 20px;
}

.modal-footer {
  position: sticky;
  bottom: 0;
  background-color: white;
  z-index: 10;
  padding: 20px;
  border-top: 1px solid #e5e7eb;
}

/* 详情模态框样式 */
.detail-section {
  margin-bottom: 24px;
}

.detail-section-title {
  font-size: 16px;
  font-weight: 600;
  color: #374151;
  margin-bottom: 8px;
}

.detail-text {
  color: #6b7280;
}

.steps-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.step-item {
  display: flex;
  align-items: flex-start;
  margin-bottom: 16px;
}

.step-item:last-child {
  margin-bottom: 0;
}

.step-number {
  width: 24px;
  height: 24px;
  border-radius: 9999px;
  background-color: #1E40AF;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 700;
  margin-right: 12px;
  margin-top: 2px;
  flex-shrink: 0;
}

.step-text {
  color: #6b7280;
}

.detail-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.detail-list-item {
  display: flex;
  align-items: flex-start;
  gap: 8px;
  color: #6b7280;
}

.medical-list .detail-list-item {
  align-items: flex-start;
}

.medical-item {
  display: flex;
  flex-direction: column;
}

.medical-condition {
  font-weight: 500;
  color: #374151;
}

.medical-handling {
  color: #6b7280;
}

.detail-list-item i {
  margin-top: 2px;
  flex-shrink: 0;
}

.fa-check-circle {
  color: #10B981;
}

.fa-fire-extinguisher {
  color: #EF4444;
}

.fa-medkit {
  color: #10B981;
}

.fa-hospital-o {
  color: #1E40AF;
}

.fa-shield {
  color: #F59E0B;
}

.fa-phone {
  color: #8b5cf6;
}

.fa-wrench {
  color: #1E40AF;
}

.fa-lightbulb-o {
  color: #F59E0B;
}

.confirm-btn {
  width: 100%;
  padding: 12px;
  border-radius: 8px;
  font-weight: 500;
  transition: all 0.2s;
  background-color: #1E40AF;
  color: white;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
}

.confirm-btn:hover {
  background-color: rgba(30, 64, 175, 0.9);
}

.confirm-btn:active {
  transform: scale(0.95);
}

/* 设施详情模态框样式 */
.facility-info {
  color: #6b7280;
  margin-bottom: 12px;
  display: flex;
  align-items: center;
  gap: 4px;
}

.facility-subtitle {
  color: #374151;
  font-weight: 500;
  margin-bottom: 4px;
}

.equipment-list {
  color: #6b7280;
}

.equipment-item {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 4px;
}

.equipment-item i {
  color: #10B981;
}

.facility-description {
  color: #6b7280;
  margin-bottom: 12px;
}

.navigate-btn {
  width: 100%;
  padding: 12px;
  border-radius: 8px;
  font-weight: 500;
  transition: all 0.2s;
  background-color: #f3f4f6;
  color: #1f2937;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
}

.navigate-btn:hover {
  background-color: #e5e7eb;
}

.navigate-btn:active {
  transform: scale(0.95);
}

/* 呼叫模态框样式 */
.call-modal-header {
  text-align: center;
  margin-bottom: 24px;
}

.call-icon-container {
  width: 64px;
  height: 64px;
  background-color: rgba(239, 68, 68, 0.1);
  border-radius: 9999px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #EF4444;
  font-size: 24px;
  margin: 0 auto 16px;
}

.call-modal-title {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 8px;
}

.call-modal-subtitle {
  color: #6b7280;
}

.call-options {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-bottom: 24px;
}

.call-option {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px;
  border-radius: 8px;
  text-align: left;
  border: none;
  cursor: pointer;
  transition: all 0.2s;
}

.fire-call {
  background-color: rgba(239, 68, 68, 0.1);
}

.medical-call {
  background-color: rgba(30, 64, 175, 0.1);
}

.police-call {
  background-color: rgba(156, 163, 175, 0.1);
}

.security-call {
  background-color: rgba(245, 158, 11, 0.1);
}

.call-option:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
}

.call-option-text {
  flex: 1;
}

.call-service {
  font-weight: 500;
  margin-bottom: 4px;
}

.call-description {
  font-size: 14px;
  color: #6b7280;
}

.call-number {
  font-size: 20px;
  font-weight: 700;
}

.fire-call .call-number {
  color: #EF4444;
}

.medical-call .call-number {
  color: #1E40AF;
}

.police-call .call-number {
  color: #1f2937;
}

.security-call .call-number {
  color: #F59E0B;
}

.cancel-btn {
  width: 100%;
  padding: 12px;
  border-radius: 8px;
  font-weight: 500;
  transition: all 0.2s;
  background-color: #f3f4f6;
  color: #1f2937;
  border: none;
  cursor: pointer;
}

.cancel-btn:hover {
  background-color: #e5e7eb;
}

.cancel-btn:active {
  transform: scale(0.95);
}

/* 提示消息样式 */
.toast {
  position: fixed;
  bottom: 32px;
  left: 50%;
  transform: translateX(-50%);
  background-color: #1f2937;
  color: white;
  padding: 8px 16px;
  border-radius: 8px;
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
  opacity: 0;
  transition: opacity 0.3s;
  z-index: 50;
  pointer-events: none;
}

.toast.show {
  opacity: 1;
}
</style>