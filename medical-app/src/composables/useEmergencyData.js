import { ref } from 'vue'

export function useEmergencyData() {
  const emergencyWarnings = ref([
    '持续高热（体温超过39℃）超过24小时不退',
    '剧烈腹痛、呕吐不止或严重腹泻',
    '头部受到撞击后出现头痛、呕吐、意识模糊等症状',
    '运动损伤导致明显肿胀、畸形或无法活动'
  ])

  const emergencyContacts = ref([
    {
      name: '急救中心',
      description: '全国统一急救电话',
      number: '120',
      bgColor: 'bg-red-50',
      textColor: 'text-danger'
    },
    {
      name: '校医院急诊',
      description: '校园内紧急医疗',
      number: '0791-88776699',
      bgColor: 'bg-blue-50',
      textColor: 'text-primary'
    },
    {
      name: '校保卫处',
      description: '校园安全紧急情况',
      number: '0791-88776110',
      bgColor: 'bg-gray-50',
      textColor: 'text-gray-800'
    }
  ])

  return {
    emergencyWarnings,
    emergencyContacts
  }
}