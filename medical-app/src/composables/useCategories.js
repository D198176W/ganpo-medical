import { ref } from 'vue'

export function useCategories() {
  const categories = ref([
    { key: 'all', name: '全部疾病' },
    { key: 'respiratory', name: '呼吸系统' },
    { key: 'digestive', name: '消化系统' },
    { key: 'infectious', name: '传染性疾病' },
    { key: 'mental', name: '心理疾病' },
    { key: 'injury', name: '意外伤害' }
  ])

  return {
    categories
  }
}