<template>
  <div v-if="show" class="modal" @click.self="$emit('close')">
    <div class="modal-content">
      <div class="modal-header">
        <h3 class="modal-title">编辑个人信息</h3>
        <button class="modal-close" @click="$emit('close')">&times;</button>
      </div>
      <div class="modal-body">
        <form @submit.prevent="handleSave">
          <div class="form-group">
            <label class="form-label">姓名</label>
            <input 
              type="text" 
              class="form-input" 
              v-model="formData.name"
            >
          </div>
          <div class="form-group">
            <label class="form-label">性别</label>
            <select class="form-select" v-model="formData.gender">
              <option value="男">男</option>
              <option value="女">女</option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-label">出生日期</label>
            <input 
              type="date" 
              class="form-input" 
              v-model="formData.birthday"
            >
          </div>
          <div class="form-group">
            <label class="form-label">联系电话</label>
            <input 
              type="tel" 
              class="form-input" 
              v-model="formData.phone"
            >
          </div>
          <div class="form-group">
            <label class="form-label">血型</label>
            <select class="form-select" v-model="formData.bloodType">
              <option value="A型">A型</option>
              <option value="B型">B型</option>
              <option value="AB型">AB型</option>
              <option value="O型">O型</option>
            </select>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button class="action-btn btn-outline" @click="$emit('close')">取消</button>
        <button class="action-btn btn-primary" @click="handleSave">保存</button>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, watch } from 'vue'

export default {
  name: 'EditProfileModal',
  props: {
    show: {
      type: Boolean,
      default: false
    },
    profile: {
      type: Object,
      required: true
    }
  },
  emits: ['close', 'save'],
  setup(props, { emit }) {
    const formData = ref({
      name: '',
      gender: '',
      birthday: '',
      phone: '',
      bloodType: ''
    })

    // 监听profile变化，更新表单数据
    watch(() => props.profile, (newProfile) => {
      if (newProfile) {
        formData.value = { ...newProfile }
        // 转换生日格式为YYYY-MM-DD
        if (formData.value.birthday.includes('年')) {
          const [year, month, day] = formData.value.birthday
            .replace('年', '-')
            .replace('月', '-')
            .replace('日', '')
            .split('-')
          formData.value.birthday = `${year}-${month.padStart(2, '0')}-${day.padStart(2, '0')}`
        }
      }
    }, { immediate: true })

    const handleSave = () => {
      // 转换生日格式
      const birthdayDate = new Date(formData.value.birthday)
      const formattedBirthday = `${birthdayDate.getFullYear()}年${birthdayDate.getMonth() + 1}月${birthdayDate.getDate()}日`
      
      const updatedProfile = {
        ...formData.value,
        birthday: formattedBirthday,
        age: `${new Date().getFullYear() - birthdayDate.getFullYear()}岁`
      }
      
      emit('save', updatedProfile)
    }

    return {
      formData,
      handleSave
    }
  }
}
</script>

<style scoped>
.modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  border-radius: 12px;
  padding: 25px;
  width: 90%;
  max-width: 500px;
  max-height: 80vh;
  overflow-y: auto;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.modal-title {
  font-size: 18px;
  font-weight: 600;
}

.modal-close {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: var(--gray);
}

.modal-body {
  line-height: 1.6;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
}

.action-btn {
  padding: 6px 12px;
  border-radius: 6px;
  font-size: 12px;
  cursor: pointer;
  transition: all 0.3s;
  border: none;
  display: flex;
  align-items: center;
  gap: 5px;
}

.btn-primary {
  background: var(--primary);
  color: white;
}

.btn-primary:hover {
  background: var(--primary-dark);
}

.btn-outline {
  background: transparent;
  border: 1px solid var(--primary);
  color: var(--primary);
}

.btn-outline:hover {
  background: var(--primary);
  color: white;
}

.form-group {
  margin-bottom: 15px;
}

.form-label {
  display: block;
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 5px;
}

.form-input {
  width: 100%;
  height: 40px;
  padding: 0 12px;
  border: 1px solid var(--border);
  border-radius: 6px;
  font-size: 14px;
}

.form-select {
  width: 100%;
  height: 40px;
  padding: 0 12px;
  border: 1px solid var(--border);
  border-radius: 6px;
  font-size: 14px;
  background: white;
}
</style>