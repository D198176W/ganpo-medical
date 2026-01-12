import { ref } from 'vue'

export function useToast() {
  const showToast = ref(false)
  const toastMessage = ref('')

  const showToastMessage = (message) => {
    toastMessage.value = message
    showToast.value = true
    setTimeout(() => {
      showToast.value = false
    }, 2000)
  }

  return {
    showToast,
    toastMessage,
    showToastMessage
  }
}