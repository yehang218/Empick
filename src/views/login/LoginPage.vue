<template>
  <div class="login-container">
    <div class="login-box">
      <h2>Welcome to the HR Portal!</h2>
      <h3>Login</h3>

      <v-text-field v-model="username" label="ID" outlined></v-text-field>

      <v-text-field v-model="password" label="Password" outlined type="password"
        :append-inner-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
        @click:append-inner="togglePassword"></v-text-field>

      <div class="forgot-password">
        <a href="#">Forget Password?</a>
      </div>

      <v-btn color="primary" :loading="authStore.loading" @click="handleLogin" class="login-button">
        Login
      </v-btn>

      <v-alert v-if="authStore.error" type="error" dense class="mt-2">
        {{ authStore.error }}
      </v-alert>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useAuthStore } from '@/stores/authStore';
import LoginRequestDTO from '@/dto/LoginRequestDTO';

const authStore = useAuthStore();
const username = ref('');
const password = ref('');
const showPassword = ref(false);

const togglePassword = () => {
  showPassword.value = !showPassword.value;
};

const handleLogin = async () => {

  const loginRequest = new LoginRequestDTO(username.value, password.value);

  await authStore.login(loginRequest);

  if (authStore.isAuthenticated) {
    // 페이지 이동 (예: router.push('/dashboard'))
    alert('로그인 성공!');
  }
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}

.login-box {
  width: 400px;
  background-color: white;
  padding: 2rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
  border-radius: 8px;
}

.login-button {
  width: 100%;
  margin-top: 1rem;
}

.forgot-password {
  text-align: right;
  margin-top: 0.5rem;
}
</style>
