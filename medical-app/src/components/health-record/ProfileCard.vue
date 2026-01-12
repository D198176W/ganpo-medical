<template>
  <div class="profile-card">
    <div class="profile-header">
      <div class="profile-avatar">{{ profile.avatar }}</div>
      <div class="profile-info">
        <h2>{{ profile.name }}</h2>
        <p>{{ profile.school }}</p>
      </div>
    </div>
    <div class="profile-details">
      <div class="detail-item">
        <div class="detail-icon">
          <i class="fas fa-user"></i>
        </div>
        <div class="detail-text">
          性别/年龄
          <strong>{{ profile.gender }} / {{ profile.age }}</strong>
        </div>
      </div>
      <div class="detail-item">
        <div class="detail-icon">
          <i class="fas fa-birthday-cake"></i>
        </div>
        <div class="detail-text">
          出生日期
          <strong>{{ profile.birthday }}</strong>
        </div>
      </div>
      <div class="detail-item">
        <div class="detail-icon">
          <i class="fas fa-phone"></i>
        </div>
        <div class="detail-text">
          联系电话
          <strong>{{ profile.phone }}</strong>
        </div>
      </div>
      <div class="detail-item">
        <div class="detail-icon">
          <i class="fas fa-tint"></i>
        </div>
        <div class="detail-text">
          血型
          <strong>{{ profile.bloodType }}</strong>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ProfileCard',
  props: {
    profile: {
      type: Object,
      required: true
    }
  }
}
</script>

<style scoped>
.profile-card {
  background: var(--card);
  border-radius: 12px;
  padding: 20px;
  margin: 0 20px 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.profile-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.profile-avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--primary), var(--secondary));
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 28px;
  font-weight: bold;
  margin-right: 20px;
}

.profile-info h2 {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 5px;
}

.profile-info p {
  font-size: 14px;
  color: var(--gray);
}

.profile-details {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 15px;
}

.detail-item {
  display: flex;
  align-items: center;
}

.detail-icon {
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--light);
  border-radius: 50%;
  margin-right: 12px;
  color: var(--primary);
}

.detail-text {
  font-size: 14px;
  color: var(--gray);
}

.detail-text strong {
  color: var(--dark);
  display: block;
  font-size: 15px;
}

@media (max-width: 600px) {
  .profile-details {
    grid-template-columns: 1fr;
  }
}
</style>