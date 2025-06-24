<template>
  <div class="login-container">
    <div class="login-background">
      <div class="background-pattern"></div>
    </div>
    
    <div class="login-box">
      <div class="logo-area">
        <img src="@/assets/logo.png" alt="Logo" class="logo" />
      </div>

      <div class="welcome-section">
        <h1 class="main-title">EMPICK</h1>
        <h2 class="subtitle">채용부터 근태, 결재까지 한 번에 관리하는<br>올인원 인사관리 시스템</h2>
        <p class="description">효율적인 인사 관리로 조직의 성장을 이끌어보세요</p>
      </div>

      <div class="login-form-container">
        <h3 class="login-title">로그인</h3>
        
        <v-form ref="form" v-model="isFormValid" @submit.prevent="handleLogin">
          <div class="form-field">
            <v-text-field 
              v-model="formData.employeeNumber" 
              label="사원번호" 
              :rules="[rules.required, rules.employeeNumber]"
              :error-messages="errors.employeeNumber" 
              @input="clearError('employeeNumber')" 
              outlined 
              dense
              prepend-inner-icon="mdi-account"
              class="custom-field"
            ></v-text-field>
          </div>

          <div class="form-field">
            <v-text-field 
              v-model="formData.password" 
              label="비밀번호" 
              :type="showPassword ? 'text' : 'password'"
              :rules="[rules.required, rules.password]" 
              :error-messages="errors.password" 
              @input="clearError('password')"
              :append-inner-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'" 
              @click:append-inner="togglePassword" 
              outlined
              dense
              prepend-inner-icon="mdi-lock"
              class="custom-field"
            ></v-text-field>
          </div>

          <div class="forgot-password">
            <router-link to="/forgot-password" class="forgot-link">
              비밀번호를 잊으셨나요?
            </router-link>
          </div>

          <v-btn 
            type="submit" 
            color="primary" 
            :loading="authStore.loading" 
            :disabled="!isFormValid || authStore.loading"
            block 
            class="login-button"
            size="large"
            elevation="2"
          >
            <v-icon left>mdi-login</v-icon>
            로그인
          </v-btn>

          <v-alert 
            v-if="authStore.error" 
            type="error" 
            density="compact" 
            class="mt-3 error-alert" 
            closable
            @click:close="clearError()"
          >
            {{ authStore.error }}
          </v-alert>
        </v-form>
      </div>

      <div class="footer-text">
        <p>© 2024 EMPICK. All rights reserved.</p>
      </div>
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
  password: v => v.length >= 6 || '비밀번호는 6자 이상이어야 합니다.'
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

    // 로그인 성공 시 메인 페이지 & 그전 페이지로 이동
    const redirectPath = route.query.redirect || '/';
    router.push(redirectPath);
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
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: fixed;
  top: 0;
  left: 0;
  overflow: hidden;
}

.login-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
}

.background-pattern {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: 
    radial-gradient(circle at 25% 25%, rgba(255, 255, 255, 0.1) 0%, transparent 50%),
    radial-gradient(circle at 75% 75%, rgba(255, 255, 255, 0.1) 0%, transparent 50%);
  animation: float 20s ease-in-out infinite;
}

@keyframes float {
  0%, 100% { transform: translateY(0px) rotate(0deg); }
  50% { transform: translateY(-20px) rotate(180deg); }
}

.login-box {
  width: 100%;
  max-width: 450px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  padding: 3rem 2.5rem;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  border-radius: 20px;
  margin: 1rem;
  position: relative;
  z-index: 2;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.logo-area {
  text-align: center;
  margin-bottom: 2rem;
}

.logo {
  height: 70px;
  margin-bottom: 1rem;
  filter: drop-shadow(0 4px 8px rgba(0, 0, 0, 0.1));
}

.welcome-section {
  text-align: center;
  margin-bottom: 2.5rem;
}

.main-title {
  color: #2c3e50;
  margin-bottom: 1rem;
  font-size: 2.5rem;
  font-weight: 700;
  letter-spacing: -1px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.subtitle {
  color: #34495e;
  margin-bottom: 0.8rem;
  font-size: 1.1rem;
  font-weight: 500;
  line-height: 1.6;
}

.description {
  color: #7f8c8d;
  font-size: 0.95rem;
  font-weight: 400;
}

.login-form-container {
  margin-bottom: 2rem;
}

.login-title {
  text-align: center;
  color: #2c3e50;
  margin-bottom: 2rem;
  font-size: 1.4rem;
  font-weight: 600;
}

.form-field {
  margin-bottom: 1.5rem;
}

.custom-field {
  border-radius: 12px;
  overflow: hidden;
}

.custom-field :deep(.v-field) {
  border-radius: 12px;
  background: rgba(255, 255, 255, 0.8);
  border: 1px solid rgba(102, 126, 234, 0.2);
  transition: all 0.3s ease;
}

.custom-field :deep(.v-field:hover) {
  border-color: rgba(102, 126, 234, 0.4);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.1);
}

.custom-field :deep(.v-field--focused) {
  border-color: #667eea;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.2);
}

.custom-field :deep(.v-field__input) {
  padding-top: 16px;
  padding-bottom: 16px;
  font-size: 1rem;
}

.custom-field :deep(.v-field__prepend-inner) {
  color: #667eea;
}

.login-button {
  margin-top: 2rem;
  height: 56px;
  font-size: 1.1rem;
  font-weight: 600;
  border-radius: 12px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  transition: all 0.3s ease;
  text-transform: none;
  letter-spacing: 0.5px;
}

.login-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.3);
}

.login-button:active {
  transform: translateY(0);
}

.forgot-password {
  text-align: right;
  margin-top: 0.5rem;
  margin-bottom: 1rem;
}

.forgot-link {
  color: #667eea;
  text-decoration: none;
  font-size: 0.9rem;
  font-weight: 500;
  transition: all 0.3s ease;
}

.forgot-link:hover {
  color: #764ba2;
  text-decoration: underline;
}

.error-alert {
  border-radius: 12px;
  border: none;
  background: rgba(231, 76, 60, 0.1);
  color: #c0392b;
}

.footer-text {
  text-align: center;
  color: #95a5a6;
  font-size: 0.85rem;
  font-weight: 400;
}

/* 반응형 디자인 */
@media (max-width: 480px) {
  .login-box {
    padding: 2rem 1.5rem;
    margin: 0.5rem;
  }
  
  .main-title {
    font-size: 2rem;
  }
  
  .subtitle {
    font-size: 1rem;
  }
  
  .description {
    font-size: 0.9rem;
  }
}

/* 다크 모드 지원 */
@media (prefers-color-scheme: dark) {
  .login-box {
    background: rgba(44, 62, 80, 0.95);
    border: 1px solid rgba(255, 255, 255, 0.1);
  }
  
  .main-title {
    color: #ecf0f1;
  }
  
  .subtitle {
    color: #bdc3c7;
  }
  
  .description {
    color: #95a5a6;
  }
  
  .login-title {
    color: #ecf0f1;
  }
  
  .custom-field :deep(.v-field) {
    background: rgba(44, 62, 80, 0.8);
    border: 1px solid rgba(102, 126, 234, 0.3);
  }
}
</style>
