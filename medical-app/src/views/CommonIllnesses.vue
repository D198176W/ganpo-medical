<template>
  <div class="common-illness-guide">
    <!-- 顶部导航栏 -->
    <header class="app-header">
      <div class="header-content">
        <div class="header-left">
          <button 
            @click="goBack" 
            class="header-btn" 
            aria-label="返回上一页"
          >
            <i class="fa fa-arrow-left"></i>
          </button>
          <h1 class="app-title">校园常见病指南</h1>
        </div>
        <button 
          @click="focusSearch" 
          class="header-btn" 
          aria-label="搜索"
        >
          <i class="fa fa-search"></i>
        </button>
      </div>
    </header>

    <!-- 主内容区 -->
    <main class="main-content">
      <!-- 搜索框 -->
      <div class="search-container">
        <input 
          type="text" 
          v-model="searchTerm"
          placeholder="搜索疾病名称或症状..." 
          class="search-input"
        >
        <i class="fa fa-search search-icon"></i>
      </div>

      <!-- 分类导航 -->
      <div class="category-container">
        <div class="category-scroll">
          <button 
            v-for="category in categories" 
            :key="category.id"
            :class="['category-btn', { 'active': activeCategory === category.id }]"
            @click="setActiveCategory(category.id)"
          >
            {{ category.name }}
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
            @click="showHospitalModal" 
            class="hospital-btn"
          >
            <i class="fa fa-hospital-o btn-icon"></i> 校医院信息
          </button>
          <button 
            @click="showEmergencyModal" 
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
      document.querySelector('.search-input').focus();
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
    showHospitalModal() {
      this.showHospitalModal = true;
    },
    closeHospitalModal() {
      this.showHospitalModal = false;
    },
    navigateToHospital() {
      this.closeHospitalModal();
      this.showToastMessage('正在导航至校医院');
    },
    showEmergencyModal() {
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
    }
  }
}
</script>

<style scoped>
/* 颜色变量 */
:root {
  --primary-blue: #3B82F6;
  --blue-light: #DBEAFE;
  --blue-dark: #1E40AF;
  --secondary-green: #10B981;
  --accent-yellow: #F59E0B;
  --danger-red: #EF4444;
  --purple: #8B5CF6;
  --orange: #F97316;
  --teal: #14B8A6;
  --gray-50: #F9FAFB;
  --gray-100: #F3F4F6;
  --gray-200: #E5E7EB;
  --gray-500: #6B7280;
  --gray-700: #374151;
  --gray-900: #111827;
  --white: #FFFFFF;
}

/* 基础样式 */
.common-illness-guide {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  background-color: var(--gray-50);
  min-height: 100vh;
  color: var(--gray-900);
}

/* 顶部导航栏 */
.app-header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  background-color: var(--primary-blue);
  color: var(--white);
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  z-index: 100;
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  max-width: 100%;
  margin: 0 auto;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.app-title {
  font-size: 18px;
  font-weight: 600;
  margin: 0;
}

.header-btn {
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

.header-btn:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

/* 主内容区 */
.main-content {
  padding: 72px 16px 80px;
  max-width: 100%;
  margin: 0 auto;
}

/* 搜索框 */
.search-container {
  position: relative;
  margin-bottom: 20px;
}

.search-input {
  width: 100%;
  padding: 12px 16px 12px 44px;
  border: 1px solid var(--gray-200);
  border-radius: 8px;
  background-color: var(--white);
  font-size: 16px;
  transition: border-color 0.2s, box-shadow 0.2s;
}

.search-input:focus {
  outline: none;
  border-color: var(--primary-blue);
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.search-icon {
  position: absolute;
  left: 16px;
  top: 50%;
  transform: translateY(-50%);
  color: var(--gray-500);
}

/* 分类导航 */
.category-container {
  margin-bottom: 24px;
  overflow-x: auto;
}

.category-scroll {
  display: flex;
  gap: 8px;
  padding-bottom: 8px;
  min-width: max-content;
}

.category-btn {
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 500;
  border: none;
  cursor: pointer;
  transition: all 0.2s;
  white-space: nowrap;
  background-color: var(--gray-100);
  color: var(--gray-700);
}

.category-btn.active {
  background-color: var(--primary-blue);
  color: var(--white);
}

.category-btn:not(.active):hover {
  background-color: var(--gray-200);
}

/* 区块标题 */
.section-title {
  font-size: 18px;
  font-weight: 600;
  margin: 0 0 16px;
  display: flex;
  align-items: center;
}

.section-icon {
  margin-right: 8px;
  color: var(--primary-blue);
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
  border-radius: 12px;
  padding: 16px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  transition: box-shadow 0.2s, transform 0.2s;
  cursor: pointer;
}

.illness-card:hover {
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 12px;
}

.card-left {
  display: flex;
  align-items: flex-start;
  gap: 12px;
}

.icon-container {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.icon-bg-blue { background-color: var(--blue-light); }
.icon-bg-green { background-color: #D1FAE5; }
.icon-bg-red { background-color: #FEE2E2; }
.icon-bg-purple { background-color: #EDE9FE; }
.icon-bg-orange { background-color: #FFEDD5; }
.icon-bg-teal { background-color: #CCFBF1; }

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
  font-size: 16px;
  font-weight: 600;
  margin: 0 0 4px;
}

.illness-category {
  font-size: 14px;
  color: var(--gray-500);
  margin: 0;
}

.status-tag {
  font-size: 12px;
  padding: 4px 8px;
  border-radius: 12px;
  font-weight: 500;
}

.tag-bg-blue { background-color: var(--blue-light); }
.tag-bg-yellow { background-color: #FEF3C7; }
.tag-bg-red { background-color: #FEE2E2; }
.tag-bg-purple { background-color: #EDE9FE; }
.tag-bg-orange { background-color: #FFEDD5; }
.tag-bg-teal { background-color: #CCFBF1; }

.tag-blue { color: var(--primary-blue); }
.tag-yellow { color: var(--accent-yellow); }
.tag-red { color: var(--danger-red); }
.tag-purple { color: var(--purple); }
.tag-orange { color: var(--orange); }
.tag-teal { color: var(--teal); }

/* 症状区域 */
.symptoms-container {
  margin-bottom: 16px;
}

.symptoms-label {
  font-size: 14px;
  color: var(--gray-500);
  margin-bottom: 8px;
}

.symptoms-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.symptom-tag {
  display: inline-block;
  padding: 6px 12px;
  background-color: var(--blue-light);
  color: var(--primary-blue);
  font-size: 14px;
  border-radius: 16px;
}

/* 按钮样式 */
.detail-btn, .hospital-btn, .emergency-call-btn, .confirm-btn, .secondary-btn {
  width: 100%;
  padding: 12px 16px;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
}

.detail-btn, .confirm-btn {
  background-color: var(--primary-blue);
  color: var(--white);
}

.detail-btn:hover, .confirm-btn:hover {
  background-color: var(--blue-dark);
}

.hospital-btn {
  background-color: var(--primary-blue);
  color: var(--white);
}

.hospital-btn:hover {
  background-color: var(--blue-dark);
}

.emergency-call-btn {
  background-color: var(--danger-red);
  color: var(--white);
}

.emergency-call-btn:hover {
  background-color: #DC2626;
}

.secondary-btn {
  background-color: var(--gray-200);
  color: var(--gray-700);
}

.secondary-btn:hover {
  background-color: var(--gray-300);
}

.btn-icon {
  margin-right: 8px;
}

/* 紧急就医指引 */
.emergency-guide {
  background-color: var(--white);
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  border-left: 4px solid var(--danger-red);
}

.emergency-desc {
  font-size: 14px;
  color: var(--gray-500);
  margin: 0 0 16px;
}

.emergency-list {
  margin: 0 0 20px;
  padding: 0;
  list-style: none;
}

.emergency-item {
  display: flex;
  align-items: flex-start;
  margin-bottom: 12px;
}

.warning-icon {
  color: var(--danger-red);
  margin-right: 8px;
  margin-top: 2px;
  flex-shrink: 0;
}

.emergency-buttons {
  display: flex;
  gap: 12px;
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
  padding: 16px;
}

.modal-content {
  background-color: var(--white);
  border-radius: 12px;
  width: 100%;
  max-width: 500px;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.15);
}

.modal-content-sm {
  background-color: var(--white);
  border-radius: 12px;
  width: 100%;
  max-width: 320px;
  padding: 20px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.15);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 20px 0;
  margin-bottom: 20px;
}

.modal-title {
  font-size: 20px;
  font-weight: 600;
  margin: 0;
}

.close-btn {
  background: none;
  border: none;
  color: var(--gray-500);
  width: 32px;
  height: 32px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: background-color 0.2s;
}

.close-btn:hover {
  background-color: var(--gray-100);
}

.modal-body {
  padding: 0 20px;
}

.modal-footer {
  padding: 20px;
  border-top: 1px solid var(--gray-200);
}

/* 详情弹窗专用样式 */
.detail-modal {
  max-width: 90%;
  max-height: 85vh;
}

.modal-header {
  position: sticky;
  top: 0;
  background: var(--white);
  z-index: 10;
  padding: 20px 24px 16px;
  border-bottom: 1px solid var(--gray-200);
  margin-bottom: 0;
}

.modal-title {
  font-size: 22px;
  font-weight: 700;
  color: var(--gray-900);
  margin: 0;
  line-height: 1.3;
}

.modal-body {
  padding: 0 24px;
  margin-top: 16px;
}

/* 信息区块样式优化 */
.info-section {
  margin-bottom: 24px;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  transition: transform 0.2s, box-shadow 0.2s;
}

.info-section:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.section-header {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
}

.section-icon {
  margin-right: 10px;
  font-size: 18px;
}

.info-title {
  font-size: 18px;
  font-weight: 700;
  margin: 0;
  color: var(--gray-900);
  line-height: 1.4;
}

/* 不同区块的颜色区分 */
.overview-section {
  background: linear-gradient(135deg, #E0F2FE 0%, #B3E5FC 100%);
  border-left: 4px solid var(--primary-blue);
}

.symptoms-section {
  background: linear-gradient(135deg, #FFEBEE 0%, #FFCDD2 100%);
  border-left: 4px solid var(--danger-red);
}

.treatment-section {
  background: linear-gradient(135deg, #E8F5E9 0%, #C8E6C9 100%);
  border-left: 4px solid var(--secondary-green);
}

.prevention-section {
  background: linear-gradient(135deg, #FFFDE7 0%, #FFF9C4 100%);
  border-left: 4px solid var(--accent-yellow);
}

.campus-tips-section {
  background: linear-gradient(135deg, #F3E5F5 0%, #E1BEE7 100%);
  border-left: 4px solid var(--purple);
}

/* 概述文本样式 */
.overview-text {
  font-size: 16px;
  line-height: 1.7;
  color: var(--gray-800);
  margin: 0;
  text-align: justify;
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
  margin-bottom: 14px;
  padding: 12px;
  border-radius: 8px;
  transition: background-color 0.2s;
  background-color: rgba(255, 255, 255, 0.7);
}

.info-item:hover {
  background-color: rgba(255, 255, 255, 0.9);
}

.info-item:last-child {
  margin-bottom: 0;
}

/* 症状列表样式 */
.symptom-list .info-item {
  border-left: 3px solid var(--danger-red);
}

.symptom-icon {
  color: var(--danger-red);
}

.symptom-text {
  font-size: 16px;
  line-height: 1.6;
  color: var(--gray-800);
  font-weight: 500;
}

/* 处理建议列表样式 */
.treatment-list .info-item {
  border-left: 3px solid var(--secondary-green);
}

.treatment-icon {
  color: var(--secondary-green);
}

.treatment-text {
  font-size: 16px;
  line-height: 1.6;
  color: var(--gray-800);
  font-weight: 500;
}

/* 预防措施列表样式 */
.prevention-list .info-item {
  border-left: 3px solid var(--accent-yellow);
}

.prevention-icon {
  color: var(--accent-yellow);
}

.prevention-text {
  font-size: 16px;
  line-height: 1.6;
  color: var(--gray-800);
  font-weight: 500;
}

/* 图标样式 */
.bullet-icon {
  margin-right: 14px;
  margin-top: 4px;
  flex-shrink: 0;
  font-size: 16px;
}

/* 校园小贴士样式优化 */
.tips-text {
  font-size: 16px;
  line-height: 1.7;
  color: var(--gray-800);
  margin: 0;
  font-weight: 500;
}

.tips-icon {
  color: var(--purple);
}

/* 模态框底部 */
.modal-footer {
  position: sticky;
  bottom: 0;
  background: var(--white);
  padding: 20px 24px;
  border-top: 1px solid var(--gray-200);
  margin-top: 0;
}

.confirm-btn {
  background: linear-gradient(135deg, var(--primary-blue) 0%, var(--blue-dark) 100%);
  color: var(--white);
  border: none;
  border-radius: 10px;
  padding: 16px 24px;
  font-size: 18px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.3);
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.confirm-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(59, 130, 246, 0.4);
}

.confirm-btn:active {
  transform: translateY(0);
}

/* 校医院信息 */
.hospital-info {
  margin-bottom: 20px;
}

.info-row {
  margin-bottom: 16px;
}

.info-label {
  font-size: 14px;
  font-weight: 600;
  margin: 0 0 4px;
  color: var(--gray-900);
}

/* 紧急联系人 */
.emergency-contacts {
  margin-bottom: 20px;
}

.contact-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px;
  border-radius: 8px;
  margin-bottom: 12px;
}

.contact-bg-red { background-color: #FEE2E2; }
.contact-bg-blue { background-color: var(--blue-light); }
.contact-bg-gray { background-color: var(--gray-100); }

.contact-info {
  flex: 1;
}

.contact-name {
  font-size: 16px;
  font-weight: 600;
  margin: 0 0 4px;
  color: var(--gray-900);
}

.contact-desc {
  font-size: 14px;
  color: var(--gray-500);
  margin: 0;
}

.contact-number {
  background: none;
  border: none;
  font-size: 18px;
  font-weight: 700;
  cursor: pointer;
  padding: 8px;
  border-radius: 4px;
  transition: background-color 0.2s;
}

.contact-red { color: var(--danger-red); }
.contact-blue { color: var(--primary-blue); }
.contact-gray { color: var(--gray-700); }

.contact-number:hover {
  background-color: rgba(0, 0, 0, 0.05);
}

/* 提示消息 */
.toast {
  position: fixed;
  bottom: 24px;
  left: 50%;
  transform: translateX(-50%);
  background-color: rgba(0, 0, 0, 0.8);
  color: var(--white);
  padding: 12px 20px;
  border-radius: 24px;
  z-index: 1100;
  font-size: 14px;
  text-align: center;
}

/* 动画效果 */
.modal-enter-active, .modal-leave-active {
  transition: opacity 0.3s;
}
.modal-enter, .modal-leave-to {
  opacity: 0;
}

.modal-enter-active .modal-content,
.modal-enter-active .modal-content-sm,
.modal-leave-active .modal-content,
.modal-leave-active .modal-content-sm {
  transition: transform 0.3s, opacity 0.3s;
}
.modal-enter .modal-content,
.modal-enter .modal-content-sm,
.modal-leave-to .modal-content,
.modal-leave-to .modal-content-sm {
  transform: scale(0.95);
  opacity: 0;
}

.toast-enter-active, .toast-leave-active {
  transition: all 0.3s;
}
.toast-enter, .toast-leave-to {
  opacity: 0;
  transform: translateX(-50%) translateY(10px);
}

/* 增加字体清晰度优化 */
.info-text,
.symptom-text,
.treatment-text,
.prevention-text,
.tips-text {
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-rendering: optimizeLegibility;
}

/* 滚动条样式优化 */
.modal-content::-webkit-scrollbar {
  width: 6px;
}

.modal-content::-webkit-scrollbar-track {
  background: var(--gray-100);
  border-radius: 3px;
}

.modal-content::-webkit-scrollbar-thumb {
  background: var(--gray-400);
  border-radius: 3px;
}

.modal-content::-webkit-scrollbar-thumb:hover {
  background: var(--gray-500);
}

/* 响应式设计 */
@media (max-width: 480px) {
  .detail-modal {
    max-width: 95%;
    margin: 20px auto;
  }
  
  .modal-header {
    padding: 16px 20px 12px;
  }
  
  .modal-title {
    font-size: 20px;
  }
  
  .modal-body {
    padding: 0 20px;
  }
  
  .info-section {
    padding: 16px;
    margin-bottom: 20px;
  }
  
  .info-title {
    font-size: 17px;
  }
  
  .overview-text,
  .symptom-text,
  .treatment-text,
  .prevention-text,
  .tips-text {
    font-size: 15px;
  }
  
  .modal-footer {
    padding: 16px 20px;
  }
  
  .confirm-btn {
    padding: 14px 20px;
    font-size: 16px;
  }
}
</style>