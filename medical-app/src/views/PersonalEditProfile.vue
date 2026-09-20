<template>
  <div class="personal-edit-profile">
    <div class="bg-container">
      <img src="/src/assets/images/personal-bg.png" alt="背景" class="bg-img">
    </div>
    <NavBar title="编辑资料" :show-user="false" />
    
    <div class="content">
      <div class="avatar-section">
      <div class="avatar-container">
        <img :src="profile.avatar" alt="用户头像" class="user-avatar">
        <div class="avatar-edit" @click="changeAvatar">
          <img src="/src/assets/images/camera-icon.png" alt="更换头像">
        </div>
      </div>
      <p class="avatar-tip">点击更换头像</p>
    </div>

    <div class="form-section">
      <div class="section-header">
        <span class="section-title">基本信息</span>
      </div>
      
      <div class="form-item">
        <label>昵称</label>
        <input type="text" v-model="profile.name" placeholder="请输入昵称">
      </div>

      <div class="form-item">
        <label>性别</label>
        <div class="gender-select">
          <div 
            v-for="gender in genders" 
            :key="gender.value"
            class="gender-option"
            :class="{ active: profile.gender === gender.value }"
            @click="profile.gender = gender.value"
          >
            <i :class="gender.icon"></i>
            <span>{{ gender.label }}</span>
          </div>
        </div>
      </div>

      <div class="form-item">
        <label>出生日期</label>
        <input type="date" v-model="profile.birthday">
      </div>

      <div class="form-item">
        <label>手机号</label>
        <input type="tel" v-model="profile.phone" placeholder="请输入手机号">
      </div>

      <div class="form-item">
        <label>所在地区</label>
        <select v-model="profile.region">
          <option value="">请选择地区</option>
          <option value="beijing">北京市</option>
          <option value="shanghai">上海市</option>
          <option value="guangzhou">广州市</option>
          <option value="shenzhen">深圳市</option>
          <option value="hangzhou">杭州市</option>
          <option value="nanchang">南昌市</option>
        </select>
      </div>

      <div class="form-item">
        <label>个人简介</label>
        <textarea v-model="profile.bio" placeholder="介绍一下自己吧..." rows="4"></textarea>
      </div>
    </div>

    <div class="health-info-section">
      <div class="section-header">
        <span class="section-title">健康信息</span>
      </div>
      
      <div class="form-item">
        <label>身高</label>
        <div class="unit-input">
          <input type="number" v-model="profile.height" placeholder="请输入身高">
          <span class="unit">cm</span>
        </div>
      </div>

      <div class="form-item">
        <label>体重</label>
        <div class="unit-input">
          <input type="number" v-model="profile.weight" placeholder="请输入体重">
          <span class="unit">kg</span>
        </div>
      </div>

      <div class="form-item">
        <label>血型</label>
        <select v-model="profile.bloodType">
          <option value="">请选择血型</option>
          <option value="A">A型</option>
          <option value="B">B型</option>
          <option value="AB">AB型</option>
          <option value="O">O型</option>
        </select>
      </div>

      <div class="form-item">
        <label>过敏史</label>
        <textarea v-model="profile.allergies" placeholder="如有过敏史请填写..." rows="3"></textarea>
      </div>
    </div>

    <div class="save-section">
      <button class="save-btn" @click="saveProfile">
        <i class="fas fa-check"></i>
        保存修改
      </button>
    </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import NavBar from '@/components/common/NavBar.vue'

const profile = ref({
  avatar: '/src/assets/images/default-avatar.png',
  name: '张同学',
  gender: 'male',
  birthday: '2002-05-15',
  phone: '138****1234',
  region: 'nanchang',
  bio: '热爱运动，关注健康，积极向上的大学生~',
  height: 175,
  weight: 65,
  bloodType: 'A',
  allergies: '无'
})

const genders = ref([
  { value: 'male', label: '男', icon: 'fas fa-mars' },
  { value: 'female', label: '女', icon: 'fas fa-venus' }
])

const changeAvatar = () => {
  console.log('更换头像')
}

const saveProfile = () => {
  console.log('保存资料:', profile.value)
}
</script>

<style scoped>
.personal-edit-profile {
  width: 100%;
  min-height: 100vh;
  background-color: transparent;
  padding-bottom: 20px;
  margin: 0;
  padding-top: 50px;
}

.bg-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: -1;
  overflow: hidden;
}

.bg-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  opacity: 0.3;
}

.content {
  padding: 20px;
}

.avatar-section {
  text-align: center;
  padding: 30px 0;
  margin-bottom: 20px;
}

.avatar-container {
  position: relative;
  display: inline-block;
}

.user-avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
  border: 4px solid white;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.avatar-edit {
  position: absolute;
  bottom: 0;
  right: 0;
  width: 32px;
  height: 32px;
  background: linear-gradient(135deg, #1E88E5, #64B5F6);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 2px 8px rgba(30, 136, 229, 0.4);
  border: 2px solid white;
}

.avatar-edit img {
  width: 16px;
  height: 16px;
}

.avatar-tip {
  margin-top: 12px;
  font-size: 14px;
  color: #666;
}

.form-section,
.health-info-section {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 20px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.section-header {
  margin-bottom: 20px;
}

.section-title {
  font-size: 18px;
  font-weight: 700;
  color: #333333;
  position: relative;
  padding-left: 8px;
}

.section-title::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 4px;
  height: 16px;
  background-color: #1E88E5;
  border-radius: 2px;
}

.form-item {
  margin-bottom: 20px;
}

.form-item:last-child {
  margin-bottom: 0;
}

.form-item label {
  display: block;
  font-size: 14px;
  font-weight: 600;
  color: #333;
  margin-bottom: 8px;
}

.form-item input,
.form-item select,
.form-item textarea {
  width: 100%;
  height: 48px;
  border: 1px solid #e0e0e0;
  border-radius: 12px;
  padding: 0 16px;
  font-size: 16px;
  color: #333;
  background: #fafafa;
  transition: all 0.3s ease;
}

.form-item input:focus,
.form-item select:focus,
.form-item textarea:focus {
  outline: none;
  border-color: #1E88E5;
  background: white;
}

.form-item textarea {
  height: auto;
  padding: 12px 16px;
  resize: none;
  line-height: 1.6;
}

.gender-select {
  display: flex;
  gap: 16px;
}

.gender-option {
  flex: 1;
  height: 64px;
  border: 2px solid #e0e0e0;
  border-radius: 12px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  background: #fafafa;
  transition: all 0.3s ease;
}

.gender-option.active {
  border-color: #1E88E5;
  background: #E3F2FD;
}

.gender-option i {
  font-size: 24px;
  color: #666;
  margin-bottom: 4px;
}

.gender-option.active i {
  color: #1E88E5;
}

.gender-option span {
  font-size: 14px;
  color: #666;
}

.gender-option.active span {
  color: #1E88E5;
  font-weight: 600;
}

.unit-input {
  display: flex;
  align-items: center;
  position: relative;
}

.unit-input input {
  flex: 1;
  padding-right: 50px;
}

.unit-input .unit {
  position: absolute;
  right: 16px;
  font-size: 16px;
  color: #666;
  font-weight: 500;
}

.save-section {
  margin-top: 20px;
}

.save-btn {
  width: 100%;
  height: 52px;
  background: linear-gradient(135deg, #1E88E5, #64B5F6);
  color: white;
  border: none;
  border-radius: 26px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  box-shadow: 0 4px 12px rgba(30, 136, 229, 0.3);
  transition: all 0.3s ease;
}

.save-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(30, 136, 229, 0.4);
}

.save-btn i {
  font-size: 18px;
}
</style>
