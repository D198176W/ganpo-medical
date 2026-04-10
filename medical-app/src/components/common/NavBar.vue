<template>
  <div class="nav-bar">
    <div class="nav-title">{{ title }}</div>
    <router-link v-if="showUser" to="/personal" class="user-text">个人中心</router-link>
    <router-link v-if="showUser" to="/personal" class="user-btn">
      <img src="@/assets/images/user-avatar.png" alt="用户头像">
    </router-link>
    <BackButton v-if="showBack" @click="handleBack" />
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import BackButton from './BackButton.vue'

const props = defineProps({
  title: {
    type: String,
    default: ''
  },
  showBack: {
    type: Boolean,
    default: false
  },
  showUser: {
    type: Boolean,
    default: false
  }
})

const router = useRouter()

const handleBack = () => {
  router.back()
}
</script>

<style scoped>
.nav-bar {
  width: 100%;
  height: 50px;
  background-color: #1E88E5;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  box-sizing: border-box;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 100;
}

.nav-title {
  font-size: 18px;
  font-weight: 700;
  color: #FFFFFF;
}

.user-text {
  font-size: 16px;
  font-weight: 500;
  color: #fff;
  margin-right: 10px;
  text-decoration: none;
}

.user-btn {
  background: transparent;
  border: none;
  cursor: pointer;
  padding: 5px;
}

.user-btn img {
  width: 30px;
  height: 30px;
  border-radius: 50%;
}
</style>