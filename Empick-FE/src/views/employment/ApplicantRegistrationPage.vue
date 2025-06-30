<template>
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
              <v-col cols="12">
                <v-file-input
                  v-model="applicant.profileImage"
                  label="프로필 사진"
                  accept="image/*"
                  prepend-icon="mdi-camera"
                  show-size
                  @update:model-value="watchProfileImage"
                ></v-file-input>
              </v-col>
            </v-row>
          </v-form>
        </v-card-text>
        <v-card-actions class="pa-4">
          <v-spacer></v-spacer>
          <v-btn color="primary" @click="saveApplicant">
            저장
          </v-btn>
          <v-btn color="grey darken-1" text @click="resetForm">
            초기화
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-container>
  </template>
  
<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useApplicantStore } from '@/stores/applicantStore'

const router = useRouter()
const applicantStore = useApplicantStore()

// 폼 상태
const form = ref()
const valid = ref(true)
const applicant = ref({
  name: '',
  phone: '',
  email: '',
  birthDate: '',
  address: '',
  profileImage: null,
})

// 유효성 검사 규칙
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

// 저장 메서드
const saveApplicant = async () => {
  if (form.value.validate()) {
    try {
      // DTO 구조에 맞게 데이터 준비
      const payload = {
        name: applicant.value.name,
        phone: applicant.value.phone,
        email: applicant.value.email,
        birth: applicant.value.birthDate,
        address: applicant.value.address,
        // profileImage는 백엔드에서 MultipartFile로 받는다면 별도 FormData 처리 필요
        // 현재는 문자열 URL로만 받는다고 가정하고, 파일 자체는 보내지 않습니다.
        profileImageUrl: applicant.value.profileImage 
          ? `/uploads/${applicant.value.profileImage.name}` 
          : '/assets/empick_logo.png', // 기본 로고 이미지 경로
      }
      
      console.log('전송 페이로드:', payload)
      console.log('profileImage 상태:', applicant.value.profileImage)
      console.log('profileImageUrl 전송 값:', payload.profileImageUrl)

      // ✅ Store를 통해 지원자 생성
      const response = await applicantStore.createApplicant(payload)
      
      if (response) {
        alert('지원자 정보가 성공적으로 등록되었습니다!')
        resetForm()
        router.push('/employment/applicant') // 등록 후 지원자 목록 페이지로 이동
      } else {
        alert('지원자 등록에 실패했습니다. 상세 오류는 콘솔을 확인해주세요.')
      }
    } catch (error) {
      console.error('지원자 등록 중 오류 발생:', error)
      alert('지원자 등록 중 오류가 발생했습니다. 다시 시도해주세요.')
    }
  }
}

// 폼 초기화
const resetForm = () => {
  form.value.reset()
  form.value.resetValidation()
  applicant.value = {
    name: '',
    phone: '',
    email: '',
    birthDate: '',
    address: '',
    profileImage: null,
  }
}

// 프로필 이미지 감시
const watchProfileImage = (newValue) => {
  console.log('profileImage watch - newValue:', newValue)
  if (newValue) {
    console.log('profileImage watch - newValue.name:', newValue.name)
  } else {
    console.log('profileImage watch - newValue is null or undefined')
  }
}
  </script>
  
  <style scoped>
  /* 필요한 경우 스타일 추가 */
  </style> 