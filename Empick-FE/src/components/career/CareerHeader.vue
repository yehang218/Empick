<template>
  <header class="career-header">
    <div class="header-content">
      <img src="@/assets/logo-career.png" class="logo" />
      <nav class="nav-items">
        <router-link to="/career/welfare">복지안내</router-link>
        <router-link to="/career/recruitments">채용공고</router-link>
      </nav>
    </div>
  </header>
</template>

<script setup>
// 특별한 로직 없음
</script>

<style scoped>
.career-header {
  height: 64px;
  padding: 0 24px;
  background: white;
  border-bottom: 1px solid #e0e0e0;
  display: flex;
  align-items: center;
  justify-content: start;
}

.header-content {
  display: flex;
  align-items: center;
  gap: 150px;
}

.logo {
  height: 55px;
}

.nav-items {
  display: flex;
  gap: 100px;
  font-weight: bold;
  font-size: 20px;
}

.nav-items a {
  color: inherit;
  text-decoration: none;
}

.nav-items a:hover {
  text-decoration: underline;
}
</style>
