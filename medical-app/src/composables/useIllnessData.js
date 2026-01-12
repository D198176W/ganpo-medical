import { ref } from 'vue'

export function useIllnessData() {
  const illnessesData = ref([
    {
      id: 1,
      title: '普通感冒',
      subtitle: '常见呼吸系统疾病',
      category: 'respiratory',
      icon: 'fa fa-medkit',
      iconBg: 'bg-blue-100',
      iconColor: 'text-primary',
      tag: '高发',
      tagBg: 'bg-blue-100',
      tagColor: 'text-primary',
      symptoms: ['咳嗽', '流涕', '喉咙痛'],
      overview: "普通感冒是一种常见的上呼吸道感染性疾病，多由病毒引起，校园内由于人员密集，容易相互传染。",
      symptomsDetail: ["鼻塞、流涕", "喉咙疼痛或不适", "轻微咳嗽", "低热（37.3℃-38℃）", "乏力、轻微头痛"],
      treatment: ["多休息，保证充足睡眠", "多喝温开水，保持身体水分", "发热时可服用对乙酰氨基酚或布洛芬退烧", "鼻塞严重可使用生理盐水洗鼻", "普通感冒一般无需抗生素，症状严重时需就医"],
      prevention: ["勤洗手，保持手部卫生", "避免与感冒患者密切接触", "保持室内通风，定期开窗换气", "增强体质，规律作息，均衡饮食", "流感季节可佩戴口罩减少感染风险"],
      campusTips: "学生宿舍应经常开窗通风，感冒期间尽量避免去人群密集的教室和食堂，必要时佩戴口罩。"
    },
    // ... 其他疾病数据（与原始代码相同）
  ])

  return {
    illnessesData
  }
}