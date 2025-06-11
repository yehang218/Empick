<template>
  <div class="login-container">
    <div class="login-box">
      <div class="logo-area">
        <img src="@/assets/logo.png" alt="Logo" class="logo" />
      </div>

      <h2 class="welcome-text">Welcome to the HR Portal!</h2>
      <h3 class="login-title">Login</h3>

      <v-form ref="form" v-model="isFormValid" @submit.prevent="handleLogin">
        <v-text-field v-model="formData.employeeNumber" label="사원번호" :rules="[rules.required, rules.employeeNumber]"
          :error-messages="errors.employeeNumber" @input="clearError('employeeNumber')" outlined dense></v-text-field>

        <v-text-field v-model="formData.password" label="비밀번호" :type="showPassword ? 'text' : 'password'"
          :rules="[rules.required, rules.password]" :error-messages="errors.password" @input="clearError('password')"
          :append-inner-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'" @click:append-inner="togglePassword" outlined
          dense></v-text-field>

        <div class="forgot-password">
          <router-link to="/forgot-password" class="forgot-link">
            비밀번호를 잊으셨나요?
          </router-link>
        </div>

        <v-btn type="submit" color="primary" :loading="authStore.loading" :disabled="!isFormValid || authStore.loading"
          block class="login-button">
          로그인
        </v-btn>

        <v-alert v-if="authStore.error" type="error" density="compact" class="mt-2" closable
          @click:close="clearError()">
          {{ authStore.error }}
        </v-alert>
      </v-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue';
import { useAuthStore } from '@/stores/authStore';
import { useRouter } from 'vue-router';

const router = useRouter();
const authStore = useAuthStore();
const form = ref(null);
const isFormValid = ref(false);
const showPassword = ref(false);

const formData = reactive({
  employeeNumber: '',
  password: ''
});

const errors = reactive({
  employeeNumber: '',
  password: ''
});

const rules = {
  required: v => !!v || '필수 입력 항목입니다.',
  employeeNumber: v => /^\d{6}$/.test(v) || '6자리 사원번호를 입력해주세요.',
  password: v => v.length >= 8 || '비밀번호는 8자 이상이어야 합니다.'
};

const clearError = (field) => {
  if (field) {
    errors[field] = '';
  } else {
    authStore.error = null;
  }
};

const togglePassword = () => {
  showPassword.value = !showPassword.value;
};

const validateForm = async () => {
  const { valid } = await form.value.validate();
  return valid;
};

const handleLogin = async () => {
  console.log('로그인 시도:', formData);

  if (!await validateForm()) {
    console.log('폼 유효성 검사 실패');
    return;
  }

  try {
    console.log('authStore.login 호출');
    await authStore.login({
      employeeNumber: formData.employeeNumber,
      password: formData.password
    });
    console.log('로그인 성공');

    // 로그인 성공 시 메인 페이지로 이동
    router.push('/');
  } catch (error) {
    console.error('로그인 실패:', error);
    // 에러는 authStore에서 처리됨
  }
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  width: 100vw;
  background-color: #f5f5f5;
  position: fixed;
  top: 0;
  left: 0;
}

.login-box {
  width: 100%;
  max-width: 400px;
  background-color: white;
  padding: 2rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  margin: 1rem;
}

.logo-area {
  text-align: center;
  margin-bottom: 2rem;
}

.logo {
  height: 60px;
  margin-bottom: 1rem;
}

.welcome-text {
  text-align: center;
  color: #333;
  margin-bottom: 0.5rem;
  font-size: 1.5rem;
}

.login-title {
  text-align: center;
  color: #666;
  margin-bottom: 2rem;
  font-size: 1.2rem;
}

.login-button {
  margin-top: 1.5rem;
  height: 48px;
  font-size: 1.1rem;
}

.forgot-password {
  text-align: right;
  margin-top: 0.5rem;
  margin-bottom: 1rem;
}

.forgot-link {
  color: #666;
  text-decoration: none;
  font-size: 0.9rem;
}

.forgot-link:hover {
  text-decoration: underline;
}

:deep(.v-field) {
  border-radius: 4px;
}

:deep(.v-field__input) {
  padding-top: 12px;
  padding-bottom: 12px;
}
</style>
