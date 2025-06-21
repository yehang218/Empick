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
              <v-col cols="12">
                <v-file-input
                  v-model="applicant.profileImage"
                  label="프로필 사진"
                  accept="image/*"
                  prepend-icon="mdi-camera"
                  show-size
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
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import CareerHeader from '@/components/career/CareerHeader.vue'
import { createApplicantService } from '@/services/applicantService'
import { useApplicationStore } from '@/stores/applicationStore'

const route = useRoute()
const router = useRouter()
const recruitmentId = route.params.id

const valid = ref(true)
const applicant = ref({
  name: '',
  phone: '',
  email: '',
  birthDate: '',
  address: '',
  profileImage: null,
})

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

const form = ref(null)
const applicationStore = useApplicationStore()

const saveApplicant = async () => {
  if (form.value.validate()) {
    try {
      const payload = {
        name: applicant.value.name,
        phone: applicant.value.phone,
        email: applicant.value.email,
        birth: applicant.value.birthDate,
        address: applicant.value.address,
        profileImageUrl: applicant.value.profileImage
          ? `/uploads/${applicant.value.profileImage.name}`
          : '/assets/empick_logo.png',
      }
      const response = await createApplicantService(payload)
      if (response && response.id) {
        // 지원서(application) 자동 생성
        const applicationRes = await applicationStore.createApplication({
          applicantId: response.id,
          recruitmentId: recruitmentId
        })
        if (applicationRes && applicationRes.id) {
          alert('지원자 정보와 지원서가 성공적으로 등록되었습니다!')
          resetForm()
          router.push(`/career/recruitments/resume/${recruitmentId}`)
        } else {
          alert('지원서 생성에 실패했습니다. 관리자에게 문의하세요.')
        }
      } else {
        alert('지원자 등록에 실패했습니다. 상세 오류는 콘솔을 확인해주세요.')
      }
    } catch (error) {
      console.error('지원자 등록 중 오류 발생:', error)
      alert('지원자 등록 중 오류가 발생했습니다. 다시 시도해주세요.')
    }
  }
}

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
</script>

<style scoped>
/* 필요한 경우 스타일 추가 */
</style> 