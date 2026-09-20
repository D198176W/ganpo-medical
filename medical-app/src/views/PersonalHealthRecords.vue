<template>
  <div class="personal-health-records">
    <NavBar title="健康档案" :show-user="false" />
    <div class="content">
      <h1>健康档案</h1>
      <p>这里是健康档案页面</p>
    </div>
  </div>
</template>

<script setup>
import NavBar from '@/components/common/NavBar.vue'
</script>

<style scoped>
.personal-health-records {
  padding-top: 50px;
  min-height: 100vh;
  background-color: #f5f5f5;
}

.content {
  padding: 20px;
}

h1 {
  font-size: 24px;
  color: #333;
  margin-bottom: 20px;
}

p {
  font-size: 16px;
  color: #666;
}
</style>