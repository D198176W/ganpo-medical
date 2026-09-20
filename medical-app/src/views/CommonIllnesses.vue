<template>
  <div class="common-illness-guide">
    <!-- 背景渐变 -->
    <div class="bg-gradient"></div>
    
    <!-- 顶部导航栏 -->
    <div class="nav-bar">
      <div class="nav-left">
        <div class="back-btn" @click="goBack">
          <i class="fas fa-arrow-left"></i>
        </div>
        <div class="title">校园常见病指南</div>
      </div>
      <div class="nav-right">
        <button 
          @click="focusSearch" 
          class="search-toggle-btn"
          aria-label="搜索"
        >
          <i class="fas fa-search"></i>
        </button>
      </div>
    </div>

    <!-- 主内容区 -->
    <main class="main-content">
      <!-- 搜索框 -->
      <div class="search-section">
        <div class="search-wrapper">
          <i class="fas fa-search search-icon"></i>
          <input 
            type="text" 
            v-model="searchTerm"
            placeholder="搜索疾病名称或症状..." 
            class="search-input"
            @focus="onSearchFocus"
            @blur="onSearchBlur"
          >
          <button 
            v-if="searchTerm" 
            @click="searchTerm = ''" 
            class="clear-search-btn"
            aria-label="清除搜索"
          >
            <i class="fas fa-times"></i>
          </button>
        </div>
        <div class="search-hint" :class="{ focused: searchFocused }">
          输入疾病名称或症状关键词，如"感冒"、"发烧"、"腹泻"等
        </div>
      </div>

      <!-- 分类导航 -->
      <div class="category-section">
        <h3 class="section-subtitle">
          <i class="fas fa-filter"></i> 按类别筛选
        </h3>
        <div class="category-grid">
          <button 
            v-for="category in categories" 
            :key="category.id"
            :class="['category-card', { 'active': activeCategory === category.id }]"
            @click="setActiveCategory(category.id)"
          >
            <div class="category-icon-wrapper">
              <i class="fas" :class="getCategoryIcon(category.id)"></i>
            </div>
            <span class="category-name">{{ category.name }}</span>
            <span class="category-count" v-if="category.id !== 'all'">
              {{ getCategoryCount(category.id) }}种
            </span>
          </button>
        </div>
      </div>

      <!-- 常见疾病列表 -->
      <h2 class="section-title">
        <i class="fa fa-stethoscope section-icon"></i> 常见疾病
      </h2>
      <div class="illness-list">
        <div 
          v-for="illness in filteredIllnesses" 
          :key="illness.id"
          class="illness-card"
          :data-category="illness.category"
          @click="showIllnessDetails(illness)"
        >
          <div class="card-header">
            <div class="card-left">
              <div :class="`icon-container ${illness.iconBgColor}`">
                <i :class="`fa ${illness.icon} ${illness.iconColor}`"></i>
              </div>
              <div class="card-info">
                <h3 class="illness-name">{{ illness.title }}</h3>
                <p class="illness-category">{{ illness.subtitle }}</p>
              </div>
            </div>
            <span :class="`status-tag ${illness.tagBgColor} ${illness.tagColor}`">
              {{ illness.tag }}
            </span>
          </div>
          <div class="symptoms-container">
            <div class="symptoms-label">典型症状：</div>
            <div class="symptoms-tags">
              <span 
                v-for="symptom in illness.symptoms" 
                :key="symptom"
                class="symptom-tag"
              >
                {{ symptom }}
              </span>
            </div>
          </div>
          <button 
            class="detail-btn"
            @click.stop="showIllnessDetails(illness)"
          >
            <i class="fa fa-info-circle btn-icon"></i> 查看详情
          </button>
        </div>
      </div>

      <!-- 紧急就医指引 -->
      <div class="emergency-guide">
        <h2 class="section-title">
          <i class="fa fa-ambulance emergency-icon"></i> 紧急就医指引
        </h2>
        <p class="emergency-desc">如出现以下情况，请立即前往校医院或附近医院就诊：</p>
        <ul class="emergency-list">
          <li 
            v-for="(warning, index) in emergencyWarnings" 
            :key="index"
            class="emergency-item"
          >
            <i class="fa fa-exclamation-circle warning-icon"></i>
            <span>{{ warning }}</span>
          </li>
        </ul>
        <div class="emergency-buttons">
          <button 
            @click="openHospitalModal" 
            class="hospital-btn"
          >
            <i class="fa fa-hospital-o btn-icon"></i> 校医院信息
          </button>
          <button 
            @click="openEmergencyModal" 
            class="emergency-call-btn"
          >
            <i class="fa fa-phone btn-icon"></i> 紧急电话
          </button>
        </div>
      </div>
    </main>

    <!-- 疾病详情弹窗 -->
    <transition name="modal">
      <div 
        v-if="showDetailModal" 
        class="modal-overlay" 
        @click="closeDetailModal"
      >
        <div class="modal-content detail-modal" @click.stop>
          <div class="modal-header">
            <h3 class="modal-title">{{ selectedIllness.title }}</h3>
            <button 
              @click="closeDetailModal" 
              class="close-btn" 
              aria-label="关闭弹窗"
            >
              <i class="fa fa-times"></i>
            </button>
          </div>
          <div class="modal-body">
            <!-- 疾病概述 -->
            <div class="info-section overview-section">
              <div class="section-header">
                <i class="fa fa-file-text-o section-icon"></i>
                <h4 class="info-title">疾病概述</h4>
              </div>
              <p class="info-text overview-text">{{ selectedIllness.overview }}</p>
            </div>
            
            <!-- 典型症状 -->
            <div class="info-section symptoms-section">
              <div class="section-header">
                <i class="fa fa-exclamation-circle section-icon symptom-icon"></i>
                <h4 class="info-title">典型症状</h4>
              </div>
              <ul class="info-list symptom-list">
                <li 
                  v-for="symptom in selectedIllness.symptomsList" 
                  :key="symptom"
                  class="info-item symptom-item"
                >
                  <i class="fa fa-circle bullet-icon"></i>
                  <span class="symptom-text">{{ symptom }}</span>
                </li>
              </ul>
            </div>
            
            <!-- 处理建议 -->
            <div class="info-section treatment-section">
              <div class="section-header">
                <i class="fa fa-heartbeat section-icon treatment-icon"></i>
                <h4 class="info-title">处理建议</h4>
              </div>
              <ul class="info-list treatment-list">
                <li 
                  v-for="tip in selectedIllness.treatment" 
                  :key="tip"
                  class="info-item treatment-item"
                >
                  <i class="fa fa-check-circle bullet-icon"></i>
                  <span class="treatment-text">{{ tip }}</span>
                </li>
              </ul>
            </div>
            
            <!-- 预防措施 -->
            <div class="info-section prevention-section">
              <div class="section-header">
                <i class="fa fa-shield section-icon prevention-icon"></i>
                <h4 class="info-title">预防措施</h4>
              </div>
              <ul class="info-list prevention-list">
                <li 
                  v-for="prevention in selectedIllness.prevention" 
                  :key="prevention"
                  class="info-item prevention-item"
                >
                  <i class="fa fa-shield-alt bullet-icon"></i>
                  <span class="prevention-text">{{ prevention }}</span>
                </li>
              </ul>
            </div>
            
            <!-- 校园小贴士 -->
            <div class="info-section campus-tips-section">
              <div class="section-header">
                <i class="fa fa-graduation-cap section-icon tips-icon"></i>
                <h4 class="info-title">校园小贴士</h4>
              </div>
              <p class="tips-text">{{ selectedIllness.campusTips }}</p>
            </div>
          </div>
          <div class="modal-footer">
            <button 
              @click="closeDetailModal" 
              class="confirm-btn"
            >
              <i class="fa fa-check btn-icon"></i> 我知道了
            </button>
          </div>
        </div>
      </div>
    </transition>

    <!-- 校医院信息弹窗 -->
    <transition name="modal">
      <div 
        v-if="showHospitalModal" 
        class="modal-overlay" 
        @click="closeHospitalModal"
      >
        <div class="modal-content-sm" @click.stop>
          <div class="modal-header">
            <h3 class="modal-title">校医院信息</h3>
            <button 
              @click="closeHospitalModal" 
              class="close-btn" 
              aria-label="关闭弹窗"
            >
              <i class="fa fa-times"></i>
            </button>
          </div>
          <div class="hospital-info">
            <div class="info-row">
              <h4 class="info-label">地址</h4>
              <p class="info-text">校园内东区，靠近第三食堂</p>
            </div>
            <div class="info-row">
              <h4 class="info-label">门诊时间</h4>
              <p class="info-text">周一至周五: 8:00-18:00</p>
              <p class="info-text">周六至周日: 9:00-16:00</p>
            </div>
            <div class="info-row">
              <h4 class="info-label">联系电话</h4>
              <p class="info-text">0791-88776655</p>
            </div>
            <div class="info-row">
              <h4 class="info-label">急诊电话</h4>
              <p class="info-text">0791-88776699</p>
            </div>
          </div>
          <div class="modal-footer">
            <button 
              @click="navigateToHospital" 
              class="confirm-btn"
            >
              <i class="fa fa-map-marker btn-icon"></i> 导航前往
            </button>
          </div>
        </div>
      </div>
    </transition>

    <!-- 紧急电话弹窗 -->
    <transition name="modal">
      <div 
        v-if="showEmergencyModal" 
        class="modal-overlay" 
        @click="closeEmergencyModal"
      >
        <div class="modal-content-sm" @click.stop>
          <div class="modal-header">
            <h3 class="modal-title">紧急联系电话</h3>
            <button 
              @click="closeEmergencyModal" 
              class="close-btn" 
              aria-label="关闭弹窗"
            >
              <i class="fa fa-times"></i>
            </button>
          </div>
          <div class="emergency-contacts">
            <div 
              v-for="contact in emergencyContacts" 
              :key="contact.name"
              :class="`contact-item ${contact.bgColor}`"
            >
              <div class="contact-info">
                <h4 class="contact-name">{{ contact.name }}</h4>
                <p class="contact-desc">{{ contact.description }}</p>
              </div>
              <button 
                @click="makeCall(contact.number)" 
                :class="`contact-number ${contact.textColor}`"
              >
                {{ contact.number }}
              </button>
            </div>
          </div>
          <div class="modal-footer">
            <button 
              @click="closeEmergencyModal" 
              class="secondary-btn"
            >
              <i class="fa fa-times btn-icon"></i> 关闭
            </button>
          </div>
        </div>
      </div>
    </transition>

    <!-- 提示消息 -->
    <transition name="toast">
      <div v-if="showToast" class="toast">
        {{ toastMessage }}
      </div>
    </transition>
  </div>
</template>

<script>
export default {
  name: 'CommonIllnessGuide',
  data() {
    return {
      searchTerm: '',
      activeCategory: 'all',
      showDetailModal: false,
      showHospitalModal: false,
      showEmergencyModal: false,
      showToast: false,
      toastMessage: '',
      searchFocused: false,
      selectedIllness: {},
      categories: [
        { id: 'all', name: '全部疾病' },
        { id: 'respiratory', name: '呼吸系统' },
        { id: 'digestive', name: '消化系统' },
        { id: 'infectious', name: '传染性疾病' },
        { id: 'mental', name: '心理疾病' },
        { id: 'injury', name: '意外伤害' }
      ],
      illnesses: [
        {
          id: 1,
          title: "普通感冒",
          subtitle: "常见呼吸系统疾病",
          category: "respiratory",
          icon: "fa-medkit",
          iconBgColor: "icon-bg-blue",
          iconColor: "icon-blue",
          tag: "高发",
          tagBgColor: "tag-bg-blue",
          tagColor: "tag-blue",
          symptoms: ["咳嗽", "流涕", "喉咙痛"],
          overview: "普通感冒是一种常见的上呼吸道感染性疾病，多由病毒引起，校园内由于人员密集，容易相互传染。",
          symptomsList: [
            "鼻塞、流涕",
            "喉咙疼痛或不适",
            "轻微咳嗽",
            "低热（37.3℃-38℃）",
            "乏力、轻微头痛"
          ],
          treatment: [
            "多休息，保证充足睡眠",
            "多喝温开水，保持身体水分",
            "发热时可服用对乙酰氨基酚或布洛芬退烧",
            "鼻塞严重可使用生理盐水洗鼻",
            "普通感冒一般无需抗生素，症状严重时需就医"
          ],
          prevention: [
            "勤洗手，保持手部卫生",
            "避免与感冒患者密切接触",
            "保持室内通风，定期开窗换气",
            "增强体质，规律作息，均衡饮食",
            "流感季节可佩戴口罩减少感染风险"
          ],
          campusTips: "学生宿舍应经常开窗通风，感冒期间尽量避免去人群密集的教室和食堂，必要时佩戴口罩。"
        },
        {
          id: 2,
          title: "急性肠胃炎",
          subtitle: "常见消化系统疾病",
          category: "digestive",
          icon: "fa-cutlery",
          iconBgColor: "icon-bg-green",
          iconColor: "icon-green",
          tag: "注意饮食",
          tagBgColor: "tag-bg-yellow",
          tagColor: "tag-yellow",
          symptoms: ["腹痛", "腹泻", "恶心呕吐"],
          overview: "急性肠胃炎是胃肠黏膜的急性炎症，校园内多因饮食不洁、暴饮暴食或食用生冷食物引起，尤其在夏秋季节高发。",
          symptomsList: [
            "腹痛、腹泻（水样便）",
            "恶心、呕吐",
            "可能伴有发热",
            "严重时出现脱水症状（口干、尿少、头晕）"
          ],
          treatment: [
            "轻度症状可禁食1-2餐，让肠胃休息",
            "补充水分，可饮用口服补液盐或淡盐水防止脱水",
            "腹泻严重可服用蒙脱石散",
            "避免油腻、辛辣和乳制品",
            "严重脱水或症状持续超过2天应及时就医"
          ],
          prevention: [
            "注意饮食卫生，不吃过期或变质食物",
            "饭前便后洗手，保持良好卫生习惯",
            "食堂就餐选择新鲜食物，不吃生冷食物",
            "不暴饮暴食，饮食规律",
            "夏季注意食物保存，避免蚊虫污染"
          ],
          campusTips: "校园食堂就餐时，尽量选择当日制作的新鲜食物，不食用隔顿或隔夜的凉菜和剩菜。"
        },
        {
          id: 3,
          title: "流行性感冒",
          subtitle: "传染性呼吸道疾病",
          category: "infectious",
          icon: "fa-bug",
          iconBgColor: "icon-bg-red",
          iconColor: "icon-red",
          tag: "传染性强",
          tagBgColor: "tag-bg-red",
          tagColor: "tag-red",
          symptoms: ["高热", "全身酸痛", "乏力"],
          overview: "流行性感冒（简称流感）是由流感病毒引起的急性呼吸道传染病，传染性强，校园内容易暴发流行。",
          symptomsList: [
            "突发高热（38.5℃以上）",
            "头痛、全身肌肉酸痛",
            "乏力、食欲下降",
            "干咳、咽喉痛",
            "部分患者伴有呕吐、腹泻"
          ],
          treatment: [
            "尽早（发病48小时内）服用抗病毒药物（如奥司他韦）",
            "高热时服用退烧药，多饮温开水",
            "充分休息，避免劳累",
            "保持室内通风，佩戴口罩避免传染他人",
            "症状严重或高危人群应及时就医"
          ],
          prevention: [
            "每年接种流感疫苗是最有效的预防措施",
            "勤洗手，避免用手触摸口鼻",
            "流感流行期间，避免去人群密集场所",
            "咳嗽或打喷嚏时用纸巾遮住口鼻",
            "宿舍和教室定期消毒，保持通风"
          ],
          campusTips: "出现流感症状应及时就医并请假休息，待症状完全消失后再返校上课，避免传染同学。"
        },
        {
          id: 4,
          title: "神经衰弱",
          subtitle: "常见心理疾病",
          category: "mental",
          icon: "fa-brain",
          iconBgColor: "icon-bg-purple",
          iconColor: "icon-purple",
          tag: "需重视",
          tagBgColor: "tag-bg-purple",
          tagColor: "tag-purple",
          symptoms: ["失眠", "注意力不集中", "易疲劳"],
          overview: "神经衰弱是由于长期精神紧张和压力过大引起的神经系统功能紊乱，在学生群体中较为常见，尤其在考试期间高发。",
          symptomsList: [
            "失眠、入睡困难或早醒",
            "注意力不集中，记忆力下降",
            "易疲劳，精神萎靡",
            "情绪波动大，易烦躁、焦虑",
            "头痛、头晕、肌肉紧张"
          ],
          treatment: [
            "调整作息，建立规律的睡眠习惯",
            "适当运动，如散步、慢跑、瑜伽等",
            "学习压力管理技巧，避免过度劳累",
            "必要时寻求心理咨询帮助",
            "严重时可在医生指导下短期使用镇静催眠药物"
          ],
          prevention: [
            "合理安排学习和休息时间，避免熬夜",
            "培养兴趣爱好，适当放松心情",
            "学习间隙适当运动，缓解压力",
            "建立良好的人际关系，及时倾诉烦恼",
            "避免长时间使用电子产品，尤其睡前"
          ],
          campusTips: "学校心理咨询中心提供免费咨询服务，学生可预约咨询，学习压力大时及时寻求帮助。"
        },
        {
          id: 5,
          title: "运动损伤",
          subtitle: "常见意外伤害",
          category: "injury",
          icon: "fa-heartbeat",
          iconBgColor: "icon-bg-orange",
          iconColor: "icon-orange",
          tag: "紧急处理",
          tagBgColor: "tag-bg-orange",
          tagColor: "tag-orange",
          symptoms: ["疼痛", "肿胀", "活动受限"],
          overview: "运动损伤是学生在体育活动或运动过程中常见的意外伤害，包括扭伤、拉伤、挫伤等，严重时可能导致骨折。",
          symptomsList: [
            "受伤部位疼痛、肿胀",
            "皮肤淤青或擦伤",
            "活动受限",
            "严重时出现畸形或无法承重",
            "局部压痛明显"
          ],
          treatment: [
            "急性损伤立即停止活动，采用RICE原则处理：休息(Rest)、冰敷(Ice)、加压包扎(Compression)、抬高患肢(Elevation)",
            "24-48小时内冷敷减轻肿胀，之后可热敷促进恢复",
            "疼痛明显可服用非甾体抗炎药（如布洛芬）",
            "严重损伤（如骨折、韧带撕裂）需立即就医固定",
            "恢复期适当进行功能锻炼，避免肌肉萎缩"
          ],
          prevention: [
            "运动前充分热身，运动后适当拉伸",
            "掌握正确的运动姿势和技巧",
            "选择合适的运动装备，尤其是运动鞋",
            "避免过度运动，循序渐进增加运动量",
            "身体不适时避免剧烈运动"
          ],
          campusTips: "校体育馆设有简易医疗点，运动中受伤可先去处理，严重时立即前往校医院或附近医院。"
        },
        {
          id: 6,
          title: "过敏性鼻炎",
          subtitle: "呼吸道过敏性疾病",
          category: "respiratory",
          icon: "fa-sneeze",
          iconBgColor: "icon-bg-teal",
          iconColor: "icon-teal",
          tag: "季节多发",
          tagBgColor: "tag-bg-teal",
          tagColor: "tag-teal",
          symptoms: ["打喷嚏", "鼻塞", "鼻痒"],
          overview: "过敏性鼻炎是鼻黏膜对过敏原的反应性疾病，校园内常见过敏原包括花粉、尘螨、动物皮屑等，春秋季多发。",
          symptomsList: [
            "阵发性喷嚏（每次多个）",
            "清水样鼻涕",
            "鼻痒、鼻塞",
            "部分患者伴有眼痒、流泪",
            "嗅觉减退"
          ],
          treatment: [
            "避免接触已知过敏原",
            "鼻腔冲洗可清除过敏原，缓解症状",
            "使用抗组胺药（如氯雷他定）缓解鼻痒、喷嚏",
            "鼻用糖皮质激素喷剂（如布地奈德）减轻炎症",
            "严重病例可考虑脱敏治疗"
          ],
          prevention: [
            "春秋花粉季节减少户外活动，外出佩戴口罩",
            "保持室内清洁，定期除尘除螨",
            "勤洗床上用品，阳光暴晒",
            "避免接触宠物等可能的过敏原",
            "过敏性鼻炎患者提前了解自身过敏原"
          ],
          campusTips: "宿舍应定期打扫，避免灰尘堆积，花粉季节关闭窗户，使用空气净化器有助于减少症状。"
        },
        {
          id: 7,
          title: "便秘",
          subtitle: "常见消化系统疾病",
          category: "digestive",
          icon: "fa-toilet",
          iconBgColor: "icon-bg-green",
          iconColor: "icon-green",
          tag: "生活习惯",
          tagBgColor: "tag-bg-yellow",
          tagColor: "tag-yellow",
          symptoms: ["排便困难", "大便干结", "排便次数减少"],
          overview: "便秘是指排便次数减少、粪便干结、排便困难的症状，校园学生由于学习压力大、饮食不规律、缺乏运动等原因容易发生。",
          symptomsList: [
            "每周排便少于3次",
            "粪便干结、硬便",
            "排便困难、费力",
            "排便不尽感",
            "可能伴有腹胀、腹痛"
          ],
          treatment: [
            "增加膳食纤维摄入，多吃蔬菜、水果、全谷物",
            "多喝水，每天至少1500-2000ml",
            "适当运动，促进肠道蠕动",
            "养成定时排便习惯",
            "必要时使用开塞露或口服通便药"
          ],
          prevention: [
            "均衡饮食，增加膳食纤维摄入",
            "每天定时饮水，保持充足水分",
            "适量运动，避免久坐",
            "建立良好的排便习惯，每天固定时间排便",
            "减少辛辣、油腻食物摄入"
          ],
          campusTips: "学生宿舍可备一些水果，如香蕉、苹果等，有助于预防便秘。学习间隙适当活动，避免长时间久坐。"
        },
        {
          id: 8,
          title: "咽炎",
          subtitle: "常见呼吸系统疾病",
          category: "respiratory",
          icon: "fa-throat",
          iconBgColor: "icon-bg-blue",
          iconColor: "icon-blue",
          tag: "高发",
          tagBgColor: "tag-bg-blue",
          tagColor: "tag-blue",
          symptoms: ["喉咙痛", "咽干", "异物感"],
          overview: "咽炎是咽部黏膜的炎症，校园内由于气候变化、过度用嗓、空气污染等因素容易发生，尤其是在季节交替时。",
          symptomsList: [
            "喉咙疼痛、干燥",
            "咽部异物感、瘙痒",
            "声音嘶哑",
            "咳嗽、咳痰",
            "可能伴有轻度发热"
          ],
          treatment: [
            "多休息，避免过度用嗓",
            "多喝温水，保持咽喉湿润",
            "含服润喉片或喉糖",
            "避免辛辣、刺激性食物",
            "严重时可使用抗生素或抗病毒药物"
          ],
          prevention: [
            "保持室内空气流通，避免空气污染",
            "多喝水，保持咽喉湿润",
            "避免过度用嗓，如长时间说话、唱歌",
            "注意保暖，避免受凉",
            "增强体质，规律作息"
          ],
          campusTips: "学生在课堂发言或参加社团活动时，注意保护嗓子，避免大声喊叫。宿舍应定期开窗通风，保持空气清新。"
        },
        {
          id: 9,
          title: "水痘",
          subtitle: "传染性疾病",
          category: "infectious",
          icon: "fa-virus",
          iconBgColor: "icon-bg-red",
          iconColor: "icon-red",
          tag: "传染性强",
          tagBgColor: "tag-bg-red",
          tagColor: "tag-red",
          symptoms: ["发热", "皮疹", "水疱"],
          overview: "水痘是由水痘-带状疱疹病毒引起的急性传染病，校园内由于人员密集，容易发生暴发流行，主要通过飞沫和直接接触传播。",
          symptomsList: [
            "发热、头痛、乏力",
            "皮疹先出现在躯干，后向四肢蔓延",
            "皮疹发展为水疱，伴有瘙痒",
            "水疱破溃后形成结痂",
            "可能伴有食欲不振"
          ],
          treatment: [
            "隔离治疗，避免传染他人",
            "保持皮肤清洁，避免搔抓",
            "发热时可服用退烧药",
            "可使用抗病毒药物（如阿昔洛韦）",
            "皮肤瘙痒可使用炉甘石洗剂"
          ],
          prevention: [
            "接种水痘疫苗是最有效的预防措施",
            "避免与水痘患者密切接触",
            "保持室内通风，定期消毒",
            "勤洗手，保持个人卫生",
            "患者应隔离至全部皮疹结痂"
          ],
          campusTips: "出现水痘症状应立即请假休息，避免去教室和食堂等人群密集场所，待全部皮疹结痂后再返校。"
        },
        {
          id: 10,
          title: "焦虑症",
          subtitle: "常见心理疾病",
          category: "mental",
          icon: "fa-brain",
          iconBgColor: "icon-bg-purple",
          iconColor: "icon-purple",
          tag: "需重视",
          tagBgColor: "tag-bg-purple",
          tagColor: "tag-purple",
          symptoms: ["紧张不安", "心跳加快", "失眠"],
          overview: "焦虑症是一种常见的心理障碍，校园学生由于学习压力、人际关系、考试焦虑等因素容易发生，严重影响学习和生活。",
          symptomsList: [
            "持续的紧张、不安感",
            "心跳加快、呼吸急促",
            "出汗、手抖",
            "失眠、多梦",
            "注意力不集中、记忆力下降"
          ],
          treatment: [
            "学习放松技巧，如深呼吸、冥想",
            "适当运动，释放压力",
            "建立良好的时间管理，避免过度压力",
            "寻求心理咨询帮助",
            "严重时可在医生指导下使用抗焦虑药物"
          ],
          prevention: [
            "合理安排学习和休息时间",
            "培养兴趣爱好，丰富课余生活",
            "建立良好的人际关系，及时倾诉烦恼",
            "学习压力管理技巧",
            "保持规律的作息和饮食习惯"
          ],
          campusTips: "学校心理咨询中心提供免费咨询服务，学生可预约咨询，学习压力大时及时寻求帮助。"
        },
        {
          id: 11,
          title: "擦伤",
          subtitle: "常见意外伤害",
          category: "injury",
          icon: "fa-band-aid",
          iconBgColor: "icon-bg-orange",
          iconColor: "icon-orange",
          tag: "紧急处理",
          tagBgColor: "tag-bg-orange",
          tagColor: "tag-orange",
          symptoms: ["皮肤破损", "出血", "疼痛"],
          overview: "擦伤是皮肤表面被粗糙物体摩擦导致的损伤，校园内常见于运动、玩耍、上下楼梯等活动中，尤其是在体育课和课外活动时。",
          symptomsList: [
            "皮肤表面破损、出血",
            "局部疼痛、红肿",
            "可能有少量渗液",
            "严重时可能有异物残留",
            "局部压痛"
          ],
          treatment: [
            "立即用清水冲洗伤口，去除异物",
            "用碘伏或酒精消毒伤口",
            "涂抹抗生素软膏",
            "用无菌纱布或创可贴覆盖",
            "严重或污染严重的伤口应及时就医"
          ],
          prevention: [
            "运动时穿戴合适的防护装备",
            "注意行走安全，避免在湿滑地面奔跑",
            "上下楼梯时扶好扶手",
            "保持校园环境整洁，避免杂物绊倒",
            "学习基本的急救知识"
          ],
          campusTips: "校医务室备有急救用品，轻微擦伤可前往处理，严重擦伤应及时就医。"
        },
        {
          id: 12,
          title: "口腔溃疡",
          subtitle: "常见口腔疾病",
          category: "digestive",
          icon: "fa-tooth",
          iconBgColor: "icon-bg-green",
          iconColor: "icon-green",
          tag: "注意饮食",
          tagBgColor: "tag-bg-yellow",
          tagColor: "tag-yellow",
          symptoms: ["口腔疼痛", "溃疡面", "进食困难"],
          overview: "口腔溃疡是口腔黏膜的溃疡性损伤，校园学生由于压力大、饮食不均衡、维生素缺乏等原因容易发生，影响进食和说话。",
          symptomsList: [
            "口腔内出现圆形或椭圆形溃疡",
            "溃疡表面覆盖白色或黄色假膜",
            "局部疼痛，进食时加重",
            "可能伴有口臭",
            "严重时可能影响说话和进食"
          ],
          treatment: [
            "保持口腔清洁，使用淡盐水漱口",
            "使用口腔溃疡贴或喷雾剂",
            "避免辛辣、刺激性食物",
            "补充维生素B2和维生素C",
            "严重或反复发作应就医"
          ],
          prevention: [
            "保持口腔卫生，早晚刷牙，饭后漱口",
            "均衡饮食，多吃蔬菜水果",
            "避免过度疲劳，保证充足睡眠",
            "减少辛辣、烫食的摄入",
            "保持心情舒畅，避免压力过大"
          ],
          campusTips: "学生食堂应注意饮食均衡，提供富含维生素的食物。宿舍可备一些口腔溃疡贴，以备不时之需。"
        },
        {
          id: 13,
          title: "扁桃体炎",
          subtitle: "常见呼吸系统疾病",
          category: "respiratory",
          icon: "fa-lungs",
          iconBgColor: "icon-bg-blue",
          iconColor: "icon-blue",
          tag: "高发",
          tagBgColor: "tag-bg-blue",
          tagColor: "tag-blue",
          symptoms: ["喉咙痛", "发热", "扁桃体肿大"],
          overview: "扁桃体炎是扁桃体的炎症，校园内由于气候变化、过度疲劳、免疫力下降等原因容易发生，春秋季节高发。",
          symptomsList: [
            "喉咙剧烈疼痛，吞咽时加重",
            "发热、寒战",
            "扁桃体红肿、化脓",
            "颈部淋巴结肿大、压痛",
            "可能伴有咳嗽、咳痰"
          ],
          treatment: [
            "充分休息，多喝水",
            "发热时服用退烧药",
            "细菌感染时使用抗生素",
            "含服润喉片缓解疼痛",
            "严重时可能需要扁桃体切除术"
          ],
          prevention: [
            "增强体质，规律作息",
            "注意保暖，避免受凉",
            "勤洗手，保持个人卫生",
            "避免与上呼吸道感染患者密切接触",
            "均衡饮食，增强免疫力"
          ],
          campusTips: "学生在季节交替时注意增减衣物，避免受凉。出现扁桃体炎症状应及时就医，避免病情加重。"
        },
        {
          id: 14,
          title: "抑郁症",
          subtitle: "常见心理疾病",
          category: "mental",
          icon: "fa-cloud",
          iconBgColor: "icon-bg-purple",
          iconColor: "icon-purple",
          tag: "需重视",
          tagBgColor: "tag-bg-purple",
          tagColor: "tag-purple",
          symptoms: ["情绪低落", "兴趣减退", "睡眠障碍"],
          overview: "抑郁症是一种常见的心理障碍，校园学生由于学习压力、人际关系、适应问题等因素容易发生，严重影响学习和生活质量。",
          symptomsList: [
            "持续的情绪低落、悲伤",
            "对平时感兴趣的事物失去兴趣",
            "睡眠障碍（失眠或过度睡眠）",
            "食欲减退或增加",
            "注意力不集中、记忆力下降",
            "可能有自杀念头"
          ],
          treatment: [
            "寻求专业心理咨询和治疗",
            "在医生指导下使用抗抑郁药物",
            "建立支持系统，与亲友交流",
            "适当运动，保持规律作息",
            "学习情绪管理技巧"
          ],
          prevention: [
            "建立良好的人际关系，及时倾诉烦恼",
            "保持规律的作息和饮食习惯",
            "适当运动，释放压力",
            "学习压力管理技巧",
            "关注自身情绪变化，及时寻求帮助"
          ],
          campusTips: "学校心理咨询中心提供专业的心理支持服务，学生如出现持续情绪低落等症状，应及时预约咨询。"
        },
        {
          id: 15,
          title: "烧烫伤",
          subtitle: "常见意外伤害",
          category: "injury",
          icon: "fa-fire",
          iconBgColor: "icon-bg-orange",
          iconColor: "icon-orange",
          tag: "紧急处理",
          tagBgColor: "tag-bg-orange",
          tagColor: "tag-orange",
          symptoms: ["皮肤红肿", "水疱", "疼痛"],
          overview: "烧烫伤是由高温、火焰、热水等引起的皮肤损伤，校园内常见于食堂、实验室、宿舍等场所，如热水烫伤、酒精灯烧伤等。",
          symptomsList: [
            "皮肤红肿、疼痛",
            "水疱形成",
            "严重时皮肤溃烂",
            "可能伴有发热",
            "大面积烧伤可能出现休克症状"
          ],
          treatment: [
            "立即用冷水冲洗伤口15-30分钟",
            "轻轻脱去受伤部位的衣物，避免撕脱皮肤",
            "小面积烫伤可涂抹烫伤膏",
            "水疱不要自行挑破，以免感染",
            "严重烧烫伤应立即就医"
          ],
          prevention: [
            "使用热水、酒精灯等时注意安全",
            "食堂打饭时小心热汤热水",
            "实验室操作时遵守安全规范",
            "宿舍使用电器时注意防火",
            "学习基本的烧烫伤急救知识"
          ],
          campusTips: "校园内如发生烧烫伤，应立即用冷水冲洗，然后前往校医务室或附近医院处理。"
        }
      ],
      emergencyWarnings: [
        "持续高热（体温超过39℃）超过24小时不退",
        "剧烈腹痛、呕吐不止或严重腹泻",
        "头部受到撞击后出现头痛、呕吐、意识模糊等症状",
        "运动损伤导致明显肿胀、畸形或无法活动"
      ],
      emergencyContacts: [
        {
          name: "急救中心",
          description: "全国统一急救电话",
          number: "120",
          bgColor: "contact-bg-red",
          textColor: "contact-red"
        },
        {
          name: "校医院急诊",
          description: "校园内紧急医疗",
          number: "0791-88776699",
          bgColor: "contact-bg-blue",
          textColor: "contact-blue"
        },
        {
          name: "校保卫处",
          description: "校园安全紧急情况",
          number: "0791-88776110",
          bgColor: "contact-bg-gray",
          textColor: "contact-gray"
        }
      ]
    }
  },
  computed: {
    filteredIllnesses() {
      let filtered = this.illnesses;
      
      // 按分类筛选
      if (this.activeCategory !== 'all') {
        filtered = filtered.filter(illness => illness.category === this.activeCategory);
      }
      
      // 按搜索词筛选
      if (this.searchTerm) {
        const term = this.searchTerm.toLowerCase();
        filtered = filtered.filter(illness => {
          return illness.title.toLowerCase().includes(term) || 
                 illness.symptoms.some(symptom => symptom.toLowerCase().includes(term));
        });
      }
      
      return filtered;
    }
  },
  methods: {
    goBack() {
      this.showToastMessage('返回上一页');
      setTimeout(() => {
        history.back();
      }, 500);
    },
    focusSearch() {
      const input = document.querySelector('.search-input');
      if (input) input.focus();
    },
    setActiveCategory(category) {
      this.activeCategory = category;
      const categoryName = this.categories.find(c => c.id === category).name;
      this.showToastMessage(`已筛选出 ${this.filteredIllnesses.length} 种${category !== 'all' ? '相关' : ''}疾病`);
    },
    showIllnessDetails(illness) {
      this.selectedIllness = illness;
      this.showDetailModal = true;
    },
    closeDetailModal() {
      this.showDetailModal = false;
    },
    openHospitalModal() {
      this.showHospitalModal = true;
    },
    closeHospitalModal() {
      this.showHospitalModal = false;
    },
    navigateToHospital() {
      this.closeHospitalModal();
      this.showToastMessage('正在导航至校医院');
    },
    openEmergencyModal() {
      this.showEmergencyModal = true;
    },
    closeEmergencyModal() {
      this.showEmergencyModal = false;
    },
    makeCall(number) {
      this.showToastMessage(`正在拨打 ${number}`);
      // 实际应用中可调用拨号功能
    },
    showToastMessage(message) {
      this.toastMessage = message;
      this.showToast = true;
      setTimeout(() => {
        this.showToast = false;
      }, 2000);
    },
    onSearchFocus() {
      this.searchFocused = true;
    },
    onSearchBlur() {
      this.searchFocused = false;
    },
    getCategoryIcon(categoryId) {
      const icons = {
        'all': 'fa-list',
        'respiratory': 'fa-lungs',
        'digestive': 'fa-utensils',
        'infectious': 'fa-virus',
        'mental': 'fa-brain',
        'injury': 'fa-band-aid'
      };
      return icons[categoryId] || 'fa-question-circle';
    },
    getCategoryCount(categoryId) {
      return this.illnesses.filter(illness => illness.category === categoryId).length;
    }
  }
}
</script>

<style scoped>
/* 颜色变量 - 与 PathologyService.vue 保持一致 */
:root {
  --primary-blue: #1E88E5;
  --blue-light: #E3F2FD;
  --blue-dark: #1565C0;
  --secondary-green: #4CAF50;
  --accent-yellow: #FFB300;
  --danger-red: #F44336;
  --purple: #8E24AA;
  --orange: #FF9800;
  --teal: #00ACC1;
  --gray-50: #FAFAFA;
  --gray-100: #F5F5F5;
  --gray-200: #EEEEEE;
  --gray-300: #E0E0E0;
  --gray-500: #9E9E9E;
  --gray-700: #616161;
  --gray-900: #212121;
  --white: #FFFFFF;
}

/* 基础样式 */
.common-illness-guide {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'PingFang SC', 'Microsoft YaHei', sans-serif;
  background-color: #f5f7fa;
  min-height: 100vh;
  color: var(--gray-900);
  line-height: 1.6;
}

/* 背景渐变 */
.bg-gradient {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, var(--blue-light) 0%, var(--gray-100) 100%);
  z-index: -1;
}

/* 顶部导航栏 - 与 PathologyService.vue 一致 */
.nav-bar {
  height: 56px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 16px;
  background: linear-gradient(135deg, var(--primary-blue), var(--blue-dark));
  color: var(--white);
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

.title {
  font-size: 18px;
  font-weight: 700;
}

.nav-right {
  display: flex;
  align-items: center;
}

.search-toggle-btn {
  background: none;
  border: none;
  color: var(--white);
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: background-color 0.2s;
}

.search-toggle-btn:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

/* 主内容区 */
.main-content {
  padding: 72px 16px 80px;
  max-width: 100%;
  margin: 0 auto;
}

/* 搜索框 */
.search-section {
  margin-bottom: 24px;
}

.search-wrapper {
  position: relative;
  display: flex;
  align-items: center;
  background: var(--white);
  border-radius: 12px;
  padding: 0 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: box-shadow 0.3s ease;
}

.search-wrapper:focus-within {
  box-shadow: 0 4px 16px rgba(30, 136, 229, 0.2);
}

.search-icon {
  color: var(--gray-500);
  font-size: 16px;
  margin-right: 12px;
}

.search-input {
  flex: 1;
  height: 52px;
  border: none;
  background: transparent;
  font-size: 16px;
  color: var(--gray-900);
  outline: none;
}

.search-input::placeholder {
  color: var(--gray-500);
}

.clear-search-btn {
  background: none;
  border: none;
  color: var(--gray-500);
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  border-radius: 50%;
  transition: background-color 0.2s;
}

.clear-search-btn:hover {
  background-color: var(--gray-100);
}

.search-hint {
  margin-top: 8px;
  font-size: 14px;
  color: var(--gray-500);
  opacity: 0;
  transition: opacity 0.3s ease;
  padding: 0 16px;
}

.search-hint.focused {
  opacity: 1;
}

/* 分类导航 */
.category-section {
  margin-bottom: 32px;
}

.section-subtitle {
  font-size: 16px;
  font-weight: 600;
  margin: 0 0 16px;
  display: flex;
  align-items: center;
  gap: 8px;
  color: var(--gray-700);
}

.category-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
}

.category-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 16px 8px;
  background: var(--white);
  border-radius: 12px;
  border: 2px solid var(--gray-200);
  cursor: pointer;
  transition: all 0.3s ease;
  text-align: center;
}

.category-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  border-color: var(--primary-blue);
}

.category-card.active {
  background: linear-gradient(135deg, var(--primary-blue), var(--blue-dark));
  border-color: transparent;
  color: var(--white);
}

.category-card.active .category-icon-wrapper {
  background: rgba(255, 255, 255, 0.2);
  color: var(--white);
}

.category-card.active .category-name,
.category-card.active .category-count {
  color: var(--white);
}

.category-icon-wrapper {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--gray-100);
  color: var(--primary-blue);
  margin-bottom: 8px;
  font-size: 20px;
  transition: all 0.3s ease;
}

.category-name {
  font-size: 14px;
  font-weight: 600;
  margin-bottom: 4px;
  color: var(--gray-900);
}

.category-count {
  font-size: 12px;
  color: var(--gray-500);
}

/* 区块标题 */
.section-title {
  font-size: 20px;
  font-weight: 700;
  margin: 0 0 20px;
  display: flex;
  align-items: center;
  gap: 12px;
  color: var(--gray-900);
}

.section-icon {
  color: var(--primary-blue);
  font-size: 20px;
}

.emergency-icon {
  color: var(--danger-red);
}

/* 疾病列表 */
.illness-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
  margin-bottom: 32px;
}

/* 疾病卡片 */
.illness-card {
  background-color: var(--white);
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  cursor: pointer;
  border: 1px solid var(--gray-200);
}

.illness-card:hover {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
  transform: translateY(-4px);
  border-color: var(--primary-blue);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 16px;
}

.card-left {
  display: flex;
  align-items: flex-start;
  gap: 16px;
}

.icon-container {
  width: 56px;
  height: 56px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.icon-bg-blue { background-color: var(--blue-light); }
.icon-bg-green { background-color: #E8F5E9; }
.icon-bg-red { background-color: #FFEBEE; }
.icon-bg-purple { background-color: #F3E5F5; }
.icon-bg-orange { background-color: #FFF3E0; }
.icon-bg-teal { background-color: #E0F2F1; }

.icon-blue { color: var(--primary-blue); }
.icon-green { color: var(--secondary-green); }
.icon-red { color: var(--danger-red); }
.icon-purple { color: var(--purple); }
.icon-orange { color: var(--orange); }
.icon-teal { color: var(--teal); }

.card-info {
  flex: 1;
}

.illness-name {
  font-size: 18px;
  font-weight: 700;
  margin: 0 0 6px;
  color: var(--gray-900);
}

.illness-category {
  font-size: 14px;
  color: var(--gray-500);
  margin: 0;
}

.status-tag {
  font-size: 12px;
  padding: 6px 12px;
  border-radius: 16px;
  font-weight: 600;
  white-space: nowrap;
}

.tag-bg-blue { background-color: var(--blue-light); }
.tag-bg-yellow { background-color: #FFF3CD; }
.tag-bg-red { background-color: #FFEBEE; }
.tag-bg-purple { background-color: #F3E5F5; }
.tag-bg-orange { background-color: #FFF3E0; }
.tag-bg-teal { background-color: #E0F2F1; }

.tag-blue { color: var(--primary-blue); }
.tag-yellow { color: var(--accent-yellow); }
.tag-red { color: var(--danger-red); }
.tag-purple { color: var(--purple); }
.tag-orange { color: var(--orange); }
.tag-teal { color: var(--teal); }

/* 症状区域 */
.symptoms-container {
  margin-bottom: 20px;
}

.symptoms-label {
  font-size: 14px;
  color: var(--gray-700);
  margin-bottom: 12px;
  font-weight: 600;
}

.symptoms-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.symptom-tag {
  display: inline-block;
  padding: 8px 16px;
  background-color: var(--blue-light);
  color: var(--primary-blue);
  font-size: 14px;
  border-radius: 20px;
  font-weight: 500;
  transition: all 0.2s ease;
}

.symptom-tag:hover {
  background-color: var(--primary-blue);
  color: var(--white);
}

/* 按钮样式 */
.detail-btn {
  width: 100%;
  padding: 14px 16px;
  background: linear-gradient(135deg, var(--primary-blue), var(--blue-dark));
  color: var(--white);
  border: none;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  box-shadow: 0 4px 12px rgba(30, 136, 229, 0.3);
}

.detail-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(30, 136, 229, 0.4);
}

.detail-btn:active {
  transform: translateY(0);
}

.btn-icon {
  font-size: 18px;
}

/* 紧急就医指引 */
.emergency-guide {
  background-color: var(--white);
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  border-left: 6px solid var(--danger-red);
  margin-bottom: 32px;
}

.emergency-desc {
  font-size: 15px;
  color: var(--gray-700);
  margin: 0 0 20px;
  line-height: 1.6;
}

.emergency-list {
  margin: 0 0 24px;
  padding: 0;
  list-style: none;
}

.emergency-item {
  display: flex;
  align-items: flex-start;
  margin-bottom: 16px;
  padding: 12px;
  background: var(--gray-50);
  border-radius: 8px;
  transition: background-color 0.2s;
}

.emergency-item:hover {
  background-color: var(--gray-100);
}

.warning-icon {
  color: var(--danger-red);
  margin-right: 12px;
  margin-top: 2px;
  flex-shrink: 0;
  font-size: 18px;
}

.emergency-buttons {
  display: flex;
  gap: 16px;
}

.hospital-btn,
.emergency-call-btn {
  flex: 1;
  padding: 16px;
  border: none;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.hospital-btn {
  background: linear-gradient(135deg, var(--primary-blue), var(--blue-dark));
  color: var(--white);
  box-shadow: 0 4px 12px rgba(30, 136, 229, 0.3);
}

.hospital-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(30, 136, 229, 0.4);
}

.emergency-call-btn {
  background: linear-gradient(135deg, var(--danger-red), #D32F2F);
  color: var(--white);
  box-shadow: 0 4px 12px rgba(244, 67, 54, 0.3);
}

.emergency-call-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(244, 67, 54, 0.4);
}

/* 弹窗样式 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  padding: 20px;
  backdrop-filter: blur(4px);
}

.modal-content {
  background-color: var(--white);
  border-radius: 20px;
  width: 100%;
  max-width: 500px;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.25);
  animation: modalAppear 0.3s ease;
}

.modal-content-sm {
  background-color: var(--white);
  border-radius: 20px;
  width: 100%;
  max-width: 360px;
  padding: 24px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.25);
  animation: modalAppear 0.3s ease;
}

@keyframes modalAppear {
  from {
    opacity: 0;
    transform: translateY(20px) scale(0.95);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24px 24px 16px;
  border-bottom: 1px solid var(--gray-200);
  margin-bottom: 0;
  position: sticky;
  top: 0;
  background: var(--white);
  z-index: 10;
  border-radius: 20px 20px 0 0;
}

.modal-title {
  font-size: 24px;
  font-weight: 700;
  margin: 0;
  color: var(--gray-900);
  line-height: 1.3;
}

.close-btn {
  background: none;
  border: none;
  color: var(--gray-500);
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: background-color 0.2s;
  font-size: 20px;
}

.close-btn:hover {
  background-color: var(--gray-100);
}

.modal-body {
  padding: 0 24px;
  margin-top: 16px;
}

.modal-footer {
  padding: 24px;
  border-top: 1px solid var(--gray-200);
  margin-top: 0;
}

/* 详情弹窗专用样式 */
.detail-modal {
  max-width: 90%;
  max-height: 85vh;
}

/* 信息区块样式优化 */
.info-section {
  margin-bottom: 24px;
  padding: 24px;
  border-radius: 16px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  transition: transform 0.3s, box-shadow 0.3s;
  border-left: 6px solid;
}

.info-section:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.section-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  gap: 12px;
}

.section-icon {
  font-size: 24px;
  flex-shrink: 0;
}

.info-title {
  font-size: 20px;
  font-weight: 700;
  margin: 0;
  color: var(--gray-900);
  line-height: 1.4;
}

/* 不同区块的颜色区分 */
.overview-section {
  background: linear-gradient(135deg, #E3F2FD 0%, #BBDEFB 100%);
  border-left-color: var(--primary-blue);
}

.symptoms-section {
  background: linear-gradient(135deg, #FFEBEE 0%, #FFCDD2 100%);
  border-left-color: var(--danger-red);
}

.treatment-section {
  background: linear-gradient(135deg, #E8F5E9 0%, #C8E6C9 100%);
  border-left-color: var(--secondary-green);
}

.prevention-section {
  background: linear-gradient(135deg, #FFF3E0 0%, #FFE0B2 100%);
  border-left-color: var(--accent-yellow);
}

.campus-tips-section {
  background: linear-gradient(135deg, #F3E5F5 0%, #E1BEE7 100%);
  border-left-color: var(--purple);
}

/* 概述文本样式 */
.overview-text {
  font-size: 16px;
  line-height: 1.8;
  color: var(--gray-800);
  margin: 0;
  font-weight: 500;
}

/* 列表样式优化 */
.info-list {
  margin: 0;
  padding: 0;
  list-style: none;
}

.info-item {
  display: flex;
  align-items: flex-start;
  margin-bottom: 16px;
  padding: 16px;
  border-radius: 12px;
  transition: background-color 0.2s;
  background-color: rgba(255, 255, 255, 0.7);
  border-left: 4px solid;
}

.info-item:hover {
  background-color: rgba(255, 255, 255, 0.9);
}

.info-item:last-child {
  margin-bottom: 0;
}

/* 症状列表样式 */
.symptom-list .info-item {
  border-left-color: var(--danger-red);
}

.symptom-icon {
  color: var(--danger-red);
}

.symptom-text {
  font-size: 16px;
  line-height: 1.8;
  color: var(--gray-800);
  font-weight: 500;
}

/* 处理建议列表样式 */
.treatment-list .info-item {
  border-left-color: var(--secondary-green);
}

.treatment-icon {
  color: var(--secondary-green);
}

.treatment-text {
  font-size: 16px;
  line-height: 1.8;
  color: var(--gray-800);
  font-weight: 500;
}

/* 预防措施列表样式 */
.prevention-list .info-item {
  border-left-color: var(--accent-yellow);
}

.prevention-icon {
  color: var(--accent-yellow);
}

.prevention-text {
  font-size: 16px;
  line-height: 1.8;
  color: var(--gray-800);
  font-weight: 500;
}

/* 图标样式 */
.bullet-icon {
  margin-right: 16px;
  margin-top: 4px;
  flex-shrink: 0;
  font-size: 16px;
  color: inherit;
}

/* 校园小贴士样式优化 */
.tips-text {
  font-size: 16px;
  line-height: 1.8;
  color: var(--gray-800);
  margin: 0;
  font-weight: 500;
}

.tips-icon {
  color: var(--purple);
}

/* 确认按钮 */
.confirm-btn {
  width: 100%;
  padding: 18px 24px;
  background: linear-gradient(135deg, var(--primary-blue), var(--blue-dark));
  color: var(--white);
  border: none;
  border-radius: 16px;
  font-size: 18px;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  box-shadow: 0 6px 20px rgba(30, 136, 229, 0.4);
}

.confirm-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(30, 136, 229, 0.5);
}

.confirm-btn:active {
  transform: translateY(0);
}

/* 校医院信息 */
.hospital-info {
  margin-bottom: 24px;
}

.info-row {
  margin-bottom: 20px;
}

.info-label {
  font-size: 16px;
  font-weight: 700;
  margin: 0 0 8px;
  color: var(--gray-900);
}

.info-text {
  font-size: 15px;
  color: var(--gray-700);
  margin: 0 0 4px;
}

/* 紧急联系人 */
.emergency-contacts {
  margin-bottom: 24px;
}

.contact-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px;
  border-radius: 16px;
  margin-bottom: 16px;
  transition: transform 0.3s;
}

.contact-item:hover {
  transform: translateY(-2px);
}

.contact-bg-red { 
  background: linear-gradient(135deg, #FFEBEE, #FFCDD2);
  border-left: 6px solid var(--danger-red);
}
.contact-bg-blue { 
  background: linear-gradient(135deg, #E3F2FD, #BBDEFB);
  border-left: 6px solid var(--primary-blue);
}
.contact-bg-gray { 
  background: linear-gradient(135deg, #F5F5F5, #EEEEEE);
  border-left: 6px solid var(--gray-500);
}

.contact-info {
  flex: 1;
}

.contact-name {
  font-size: 18px;
  font-weight: 700;
  margin: 0 0 6px;
  color: var(--gray-900);
}

.contact-desc {
  font-size: 14px;
  color: var(--gray-700);
  margin: 0;
}

.contact-number {
  background: var(--white);
  border: 2px solid;
  font-size: 20px;
  font-weight: 800;
  cursor: pointer;
  padding: 12px 20px;
  border-radius: 12px;
  transition: all 0.3s ease;
  min-width: 120px;
  text-align: center;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.contact-red { 
  color: var(--danger-red);
  border-color: var(--danger-red);
}
.contact-blue { 
  color: var(--primary-blue);
  border-color: var(--primary-blue);
}
.contact-gray { 
  color: var(--gray-700);
  border-color: var(--gray-700);
}

.contact-number:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.15);
}

/* 提示消息 */
.toast {
  position: fixed;
  bottom: 32px;
  left: 50%;
  transform: translateX(-50%);
  background: linear-gradient(135deg, var(--gray-900), #000);
  color: var(--white);
  padding: 16px 28px;
  border-radius: 24px;
  z-index: 1100;
  font-size: 15px;
  text-align: center;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.3);
  animation: toastAppear 0.3s ease;
}

@keyframes toastAppear {
  from {
    opacity: 0;
    transform: translateX(-50%) translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateX(-50%) translateY(0);
  }
}

/* 动画效果 */
.modal-enter-active, .modal-leave-active {
  transition: opacity 0.4s;
}
.modal-enter, .modal-leave-to {
  opacity: 0;
}

.modal-enter-active .modal-content,
.modal-enter-active .modal-content-sm,
.modal-leave-active .modal-content,
.modal-leave-active .modal-content-sm {
  transition: transform 0.4s, opacity 0.4s;
}
.modal-enter .modal-content,
.modal-enter .modal-content-sm,
.modal-leave-to .modal-content,
.modal-leave-to .modal-content-sm {
  transform: translateY(20px) scale(0.95);
  opacity: 0;
}

.toast-enter-active, .toast-leave-active {
  transition: all 0.4s;
}
.toast-enter, .toast-leave-to {
  opacity: 0;
  transform: translateX(-50%) translateY(20px);
}

/* 滚动条样式优化 */
.modal-content::-webkit-scrollbar {
  width: 8px;
}

.modal-content::-webkit-scrollbar-track {
  background: var(--gray-100);
  border-radius: 4px;
  margin: 8px;
}

.modal-content::-webkit-scrollbar-thumb {
  background: var(--gray-300);
  border-radius: 4px;
}

.modal-content::-webkit-scrollbar-thumb:hover {
  background: var(--gray-400);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .category-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .emergency-buttons {
    flex-direction: column;
  }
  
  .modal-content {
    max-width: 95%;
  }
}

@media (max-width: 480px) {
  .main-content {
    padding: 72px 12px 80px;
  }
  
  .category-grid {
    grid-template-columns: 1fr;
  }
  
  .section-title {
    font-size: 18px;
  }
  
  .illness-card {
    padding: 16px;
  }
  
  .card-left {
    gap: 12px;
  }
  
  .icon-container {
    width: 48px;
    height: 48px;
  }
  
  .illness-name {
    font-size: 16px;
  }
  
  .symptom-tag {
    padding: 6px 12px;
    font-size: 13px;
  }
  
  .emergency-guide {
    padding: 20px;
  }
  
  .modal-header {
    padding: 20px 20px 16px;
  }
  
  .modal-title {
    font-size: 20px;
  }
  
  .modal-body {
    padding: 0 20px;
  }
  
  .info-section {
    padding: 20px;
  }
  
  .info-title {
    font-size: 18px;
  }
  
  .overview-text,
  .symptom-text,
  .treatment-text,
  .prevention-text,
  .tips-text {
    font-size: 15px;
    line-height: 1.7;
  }
  
  .confirm-btn {
    padding: 16px 20px;
    font-size: 16px;
  }
  
  .contact-item {
    flex-direction: column;
    align-items: stretch;
    gap: 16px;
  }
  
  .contact-number {
    width: 100%;
  }
}
</style>