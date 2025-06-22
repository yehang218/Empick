<template>
  <div>
    <CareerHeader />
    <v-container fluid class="pa-6">
      <v-card class="pa-5">
        <v-card-title class="text-h5">지원자 인적사항 등록</v-card-title>
        <v-card-text>
          <v-form ref="form" v-model="valid" lazy-validation>
            <v-row>
              <v-col cols="12" md="6">
                <v-text-field
                  v-model="applicant.name"
                  :rules="nameRules"
                  label="이름"
                  required
                ></v-text-field>
              </v-col>
              <v-col cols="12" md="6">
                <v-text-field
                  v-model="applicant.phone"
                  :rules="phoneRules"
                  label="연락처 (예: 010-1234-5678)"
                  required
                ></v-text-field>
              </v-col>
              <v-col cols="12" md="6">
                <v-text-field
                  v-model="applicant.email"
                  :rules="emailRules"
                  label="이메일"
                  required
                ></v-text-field>
              </v-col>
              <v-col cols="12" md="6">
                <v-text-field
                  v-model="applicant.birthDate"
                  :rules="birthDateRules"
                  label="생년월일 (예: 1990-01-01)"
                  required
                ></v-text-field>
              </v-col>
              <v-col cols="12" md="6">
                <v-text-field
                  v-model="applicant.address"
                  label="주소"
                ></v-text-field>
              </v-col>
              <!-- 프로필 이미지 업로드는 v3에서 구현 예정 -->
              <!-- <v-col cols="12">
                <v-file-input
                  v-model="applicant.profileImage"
                  label="프로필 사진"
                  accept="image/*"
                  prepend-icon="mdi-camera"
                  show-size
                ></v-file-input>
              </v-col> -->
            </v-row>
          </v-form>
        </v-card-text>
        <v-card-actions class="pa-4">
          <v-spacer></v-spacer>
          <v-btn color="primary" @click="saveApplicant" :loading="isLoading">
            저장
          </v-btn>
          <v-btn color="grey darken-1" text @click="resetForm">
            초기화
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-container>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import CareerHeader from '@/components/career/CareerHeader.vue'
import { useApplicantStore } from '@/stores/applicantStore'
import { useApplicationStore } from '@/stores/applicationStore'
import { useIntroduceStore } from '@/stores/introduceStore'

const route = useRoute()
const router = useRouter()
const recruitmentId = route.params.id

// ===== Store 사용 (MVVM - Model) =====
const applicantStore = useApplicantStore()
const applicationStore = useApplicationStore()
const introduceStore = useIntroduceStore()

// ===== View State =====
const valid = ref(true)
const form = ref(null)
const applicant = ref({
  name: '',
  phone: '',
  email: '',
  birthDate: '',
  address: '',
  profileImage: null,
})

// ===== ViewModel (Computed) =====
const isLoading = computed(() => applicantStore.loading || applicationStore.loading)
const applicantPayload = computed(() => ({
  name: applicant.value.name,
  phone: applicant.value.phone,
  email: applicant.value.email,
  birth: applicant.value.birthDate,
  address: applicant.value.address,
  profileImageUrl: null, // 일단 null로 설정 (v3 파일 업로드 구현 전까지)
}))

// ===== Validation Rules =====
const nameRules = [v => !!v || '이름은 필수입니다.']
const phoneRules = [
  v => !!v || '연락처는 필수입니다.',
  v => /^[0-9]{2,3}-[0-9]{3,4}-[0-9]{4}$/.test(v) || '유효한 연락처 형식이 아닙니다. (예: 010-1234-5678)',
]
const emailRules = [
  v => !!v || '이메일은 필수입니다.',
  v => /.+@.+\..+/.test(v) || '유효한 이메일 형식이 아닙니다.',
]
const birthDateRules = [
  v => !!v || '생년월일은 필수입니다.',
  v => /^[0-9]{4}-[0-9]{2}-[0-9]{2}$/.test(v) || '유효한 생년월일 형식이 아닙니다. (예: 1990-01-01)',
]

// ===== Actions =====
const saveApplicant = async () => {
  if (!form.value.validate()) return

  try {
    console.log('🔄 지원자 등록 시작:', applicantPayload.value)
    
    // 1. 지원자 등록
    const applicantResponse = await applicantStore.createApplicant(applicantPayload.value)
    console.log('✅ 지원자 등록 성공:', applicantResponse)
    
    if (!applicantResponse?.id) {
      alert('지원자 등록에 실패했습니다.')
      return
    }
    
    // 2. 지원서 자동 생성 (백엔드 스펙에 맞는 최소 필드만)
    console.log('🔄 지원서 생성 시작')
    const applicationPayload = {
      applicantId: applicantResponse.id,
      recruitmentId: parseInt(recruitmentId)
    }
    
    console.log('📝 지원서 생성 요청 데이터:', applicationPayload)
    
    try {
      const applicationResponse = await applicationStore.createApplication(applicationPayload)
      console.log('✅ 지원서 생성 성공:', applicationResponse)
      
      if (!applicationResponse?.id) {
        // 백엔드에서 다른 형태로 응답할 수 있으므로 전체 응답 확인
        console.log('⚠️ applicationResponse 전체 데이터:', applicationResponse)
        const actualId = applicationResponse?.data?.id || applicationResponse?.id
        if (!actualId) {
          alert('지원서 생성에 실패했습니다.')
          return
        }
        applicationResponse.id = actualId
      }
      
      // 3. 성공 처리 (introduce는 이력서 페이지에서 생성)
      localStorage.setItem('currentApplicantId', applicantResponse.id)
      localStorage.setItem('currentApplicationId', applicationResponse.id)
      
      alert('지원자 정보와 지원서가 성공적으로 등록되었습니다!')
      resetForm()
      
      // 4. 이력서 작성 페이지로 이동
      router.push(`/career/recruitments/resume/${recruitmentId}?applicantId=${applicantResponse.id}&applicationId=${applicationResponse.id}`)
      
    } catch (appError) {
      console.error('❌ 지원서 생성 실패:', appError)
      
      // 백엔드 응답 상세 확인
      if (appError.response?.data) {
        console.error('📋 백엔드 에러 응답:', appError.response.data)
        alert(`지원서 생성 실패: ${appError.response.data.message || '알 수 없는 오류'}`)
      } else {
        alert(`지원서 생성 실패: ${appError.message}`)
      }
      return
    }
  } catch (error) {
    console.error('❌ 등록 중 오류:', error)
    alert(`등록 중 오류가 발생했습니다: ${error.message}`)
  }
}

const resetForm = () => {
  form.value?.reset()
  form.value?.resetValidation()
  applicant.value = {
    name: '',
    phone: '',
    email: '',
    birthDate: '',
    address: '',
    profileImage: null,
  }
}
</script>

<style scoped>
/* 필요한 경우 스타일 추가 */
</style> 