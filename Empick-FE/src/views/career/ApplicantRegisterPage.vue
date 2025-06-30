<template>
  <div>
    <CareerHeader />
    <div class="register-container">
      <div class="content-wrapper">
        <div class="page-header">
          <h1 class="page-title">지원자 인적사항 등록</h1>
          <p class="page-subtitle">EMPICK과 함께 성장할 준비가 되셨나요? 지원을 위해 기본 정보를 입력해주세요.</p>
        </div>

        <div class="form-card">
          <v-form ref="form" v-model="valid" lazy-validation>
            <div class="form-section">
              <h2 class="section-title">
                <v-icon class="section-icon">mdi-account</v-icon>
                기본 정보
              </h2>
              <v-row>
                <v-col cols="12" md="6">
                  <v-text-field v-model="applicant.name" :rules="nameRules" label="이름" required variant="outlined"
                    class="form-field" prepend-inner-icon="mdi-account"></v-text-field>
                </v-col>
                <v-col cols="12" md="6">
                  <v-text-field v-model="applicant.phone" :rules="phoneRules" label="연락처 (예: 010-1234-5678)" required
                    variant="outlined" class="form-field" prepend-inner-icon="mdi-phone"></v-text-field>
                </v-col>
                <v-col cols="12" md="6">
                  <v-text-field v-model="applicant.email" :rules="emailRules" label="이메일" required variant="outlined"
                    class="form-field" prepend-inner-icon="mdi-email"></v-text-field>
                </v-col>
                <v-col cols="12" md="6">
                  <v-text-field v-model="applicant.birthDate" :rules="birthDateRules" label="생년월일 (예: 1990-01-01)"
                    required variant="outlined" class="form-field" prepend-inner-icon="mdi-calendar"></v-text-field>
                </v-col>
                <v-col cols="12">
                  <v-text-field v-model="applicant.address" label="주소" variant="outlined" class="form-field"
                    prepend-inner-icon="mdi-map-marker"></v-text-field>
                </v-col>
              </v-row>
            </div>

            <!-- <div class="form-section"> -->
              <!-- <h2 class="section-title">
                <v-icon class="section-icon">mdi-camera</v-icon>
                프로필 사진
              </h2>
              <div class="profile-upload-section">
                <v-file-input v-model="applicant.profileImage" label="프로필 사진 (선택사항)" accept="image/*"
                  prepend-icon="mdi-camera" show-size @update:model-value="handleFileChange" :loading="uploadLoading"
                  clearable variant="outlined" class="file-input"></v-file-input> -->

                <!-- 업로드 진행 상태 -->
                <!-- <v-progress-linear v-if="uploadLoading" indeterminate color="primary"
                  class="upload-progress"></v-progress-linear> -->

                <!-- 업로드된 이미지 미리보기 -->
                <!-- <div v-if="profileImageUrl" class="image-preview">
                  <div class="preview-card">
                    <h3 class="preview-title">프로필 사진 미리보기</h3>
                    <div class="preview-content">
                      <v-img :src="profileImageUrl" max-width="200" max-height="200" class="preview-image"></v-img>
                      <div class="preview-actions">
                        <v-btn size="small" color="error" variant="outlined" @click="removeProfileImage"
                          prepend-icon="mdi-delete">
                          사진 제거
                        </v-btn>
                      </div>
                    </div>
                  </div>
                </div> -->

                <!-- 업로드 안내 메시지 -->
                <!-- <v-alert v-if="!profileImageUrl && !uploadLoading" type="info" variant="tonal" class="upload-info">
                  <v-icon>mdi-information</v-icon>
                  프로필 사진은 5MB 이하의 이미지 파일만 업로드 가능합니다.
                </v-alert> -->
              <!-- </div>
            </div> -->
          </v-form>
        </div>

        <div class="action-buttons">
          <v-btn color="grey darken-1" variant="outlined" @click="handleReset" size="large" class="reset-btn">
            <v-icon left>mdi-refresh</v-icon>
            초기화
          </v-btn>
          <v-btn color="primary" @click="saveApplicant" :loading="isLoading" :disabled="!valid" size="large"
            class="submit-btn">
            <v-icon left>mdi-check</v-icon>
            저장
          </v-btn>
        </div>
      </div>
    </div>

    <!-- 초기화 확인 Modal -->
    <AlertModal v-if="showResetModal" title="폼 초기화 확인" message="입력한 모든 정보가 사라집니다. 정말 초기화하시겠습니까?" confirm-text="초기화하기"
      cancel-text="취소" @confirm="handleFinalReset" @cancel="handleCancelReset" />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import CareerHeader from '@/components/career/CareerHeader.vue'
import AlertModal from '@/components/common/AlertModal.vue'
import { useApplicantStore } from '@/stores/applicantStore'
import { useApplicationStore } from '@/stores/applicationStore'
import { useIntroduceStore } from '@/stores/introduceStore'
import { useToast } from 'vue-toastification'
import { useFileStore } from '@/stores/fileStore'

const route = useRoute()
const router = useRouter()
const toast = useToast()
const recruitmentId = route.params.id

// ===== Store 사용 (MVVM - Model) =====
const applicantStore = useApplicantStore()
const applicationStore = useApplicationStore()
const introduceStore = useIntroduceStore()
const fileStore = useFileStore()

// ===== View State (Vue 3 Composition API) =====
const valid = ref(true)
const form = ref(null)
const uploadLoading = ref(false)
const profileImageUrl = ref('')
// 초기화 확인 모달 상태
const showResetModal = ref(false)

// 지원자 정보 (반응형 객체)
const applicant = ref({
  name: '',
  phone: '',
  email: '',
  birthDate: '',
  address: '',
  profileImage: null,
  profileImageKey: null, // S3 key 저장용
})

// ===== ViewModel (Computed Properties) =====
const isLoading = computed(() =>
  applicantStore.loading || applicationStore.loading || uploadLoading.value
)

const applicantPayload = computed(() => ({
  name: applicant.value.name,
  phone: applicant.value.phone,
  email: applicant.value.email,
  birth: applicant.value.birthDate,
  address: applicant.value.address,
  profileUrl: applicant.value.profileImageKey || null, // DTO 필드명에 맞춤 (profileUrl)
}))

// ===== Validation Rules =====
const nameRules = [
  v => !!v || '이름은 필수입니다.',
  v => (v && v.length >= 2) || '이름은 2글자 이상이어야 합니다.'
]

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

// ===== S3 파일 업로드 Functions =====
const handleFileChange = async (files) => {
  console.log('🔍 handleFileChange 호출됨:', { files, type: typeof files, isArray: Array.isArray(files) })

  // 파일이 없거나 제거된 경우 처리
  if (!files) {
    console.log('📁 파일이 null/undefined입니다.')
    profileImageUrl.value = ''
    return
  }

  // v-file-input에서 단일 파일인 경우 배열이 아닐 수 있음
  let file = null
  if (Array.isArray(files)) {
    if (files.length === 0) {
      console.log('📁 파일 배열이 비어있습니다.')
      profileImageUrl.value = ''
      return
    }
    file = files[0]
  } else {
    // 단일 파일 객체인 경우
    file = files
  }

  // 파일 객체 유효성 검사
  if (!file || typeof file !== 'object' || !file.name) {
    console.log('📁 유효하지 않은 파일 객체:', file)
    profileImageUrl.value = ''
    return
  }

  console.log('📤 선택된 파일:', {
    name: file.name,
    size: file.size,
    type: file.type,
    lastModified: file.lastModified
  })

  // 파일 타입 검증 (기존 멤버 등록과 동일)
  const allowedTypes = ['image/jpeg', 'image/jpg', 'image/png', 'image/webp']
  if (!allowedTypes.includes(file.type)) {
    toast.error('JPG, PNG, WEBP 형식의 이미지만 업로드 가능합니다.')
    applicant.value.profileImage = null
    return
  }

  // 파일 크기 검증 (5MB 제한)
  const maxSize = 5 * 1024 * 1024 // 5MB
  if (file.size > maxSize) {
    toast.error('프로필 이미지는 5MB 이하만 업로드 가능합니다.')
    applicant.value.profileImage = null
    return
  }

  try {
    uploadLoading.value = true
    toast.info('프로필 사진을 S3에 업로드 중입니다...')

    console.log('📤 S3 파일 업로드 시작:', {
      fileName: file.name,
      fileSize: file.size,
      fileType: file.type
    })

    // 파일명 안전하게 처리
    const safeFileName = file.name ? file.name.replace(/[^a-zA-Z0-9.-]/g, '_') : 'profile.jpg'

    // 기존 멤버 등록과 동일한 방식: fileStore.uploadProfileImage 사용
    // 파일명: applicant_{timestamp}.png 형식
    const timestamp = Date.now()
    const fileName = `applicant_${timestamp}.png`

    const uploadResult = await fileStore.uploadProfileImage(file, 'profiles/', fileName)

    console.log('✅ S3 업로드 성공:', uploadResult)

    // S3 key와 전체 URL 생성
    const s3Key = uploadResult.key || `profiles/${fileName}`
    const s3FullUrl = `https://empick-bucket.s3.ap-northeast-2.amazonaws.com/${s3Key}`

    // 미리보기용 URL 생성 (FileReader 사용 - 기존 memberRegisterStore 패턴)
    const reader = new FileReader()
    reader.onload = (e) => {
      profileImageUrl.value = e.target.result
      console.log('🖼️ 프로필 이미지 미리보기 설정 완료')
    }
    reader.readAsDataURL(file)

    // applicant 객체에 S3 전체 URL 저장 (DB 저장용)
    applicant.value.profileImageKey = s3FullUrl

    console.log('✅ 프로필 이미지 업로드 완료:', { s3Key, s3FullUrl, hasPreview: !!profileImageUrl.value })
    toast.success('프로필 사진이 성공적으로 업로드되었습니다!')

  } catch (error) {
    console.error('❌ S3 업로드 실패:', {
      error: error,
      message: error.message,
      stack: error.stack
    })
    // toast.error(`파일 업로드에 실패했습니다: ${error.message}`)
    applicant.value.profileImage = null
    applicant.value.profileImageKey = null
    profileImageUrl.value = ''
  } finally {
    uploadLoading.value = false
  }
}

const removeProfileImage = () => {
  applicant.value.profileImage = null
  applicant.value.profileImageKey = null
  profileImageUrl.value = ''
  console.log('🗑️ 프로필 이미지 제거됨')
  toast.info('프로필 사진이 제거되었습니다.')
}

// ===== Main Actions =====
const saveApplicant = async () => {
  // 폼 유효성 검사
  const isValid = await form.value.validate()
  if (!isValid.valid) {
    toast.error('입력 정보를 확인해주세요.')
    return
  }

  try {
    console.log('🔄 지원자 등록 시작:', applicantPayload.value)
    // toast.info('지원자 정보를 등록 중입니다...')

    // 1. 지원자 등록 (프로필 이미지 URL 포함)
    const applicantResponse = await applicantStore.createApplicant(applicantPayload.value)
    console.log('✅ 지원자 등록 성공:', applicantResponse)

    if (!applicantResponse?.id) {
      throw new Error('지원자 등록에 실패했습니다.')
    }

    // 2. 지원서 자동 생성
    console.log('🔄 지원서 생성 시작')
    const applicationPayload = {
      applicantId: applicantResponse.id,
      recruitmentId: parseInt(recruitmentId)
    }

    console.log('📝 지원서 생성 요청 데이터:', applicationPayload)

    const applicationResponse = await applicationStore.createApplication(applicationPayload)
    console.log('✅ 지원서 생성 성공:', applicationResponse)

    const applicationId = applicationResponse?.id || applicationResponse?.data?.id
    if (!applicationId) {
      throw new Error('지원서 생성에 실패했습니다.')
    }

    toast.info('지원자 정보를 등록 중입니다...')
    // 3. 성공 처리
    localStorage.setItem('currentApplicantId', applicantResponse.id)
    localStorage.setItem('currentApplicationId', applicationId)

    // toast.success('지원자 정보와 지원서가 성공적으로 등록되었습니다!')

    // 4. 이력서 작성 페이지로 이동
    setTimeout(() => {
      router.push(`/career/recruitments/resume/${recruitmentId}?applicantId=${applicantResponse.id}&applicationId=${applicationId}`)
      toast.success('지원자 정보와 지원서가 성공적으로 등록되었습니다!')
    }, 1000)

  } catch (error) {
    console.error('❌ 등록 중 오류:', error)
    // toast.error(`등록 중 오류가 발생했습니다: ${error.message}`)
  }
}

// 초기화 버튼 클릭 핸들러
const handleReset = () => {
  showResetModal.value = true
}

// 초기화 모달 닫기
const handleCancelReset = () => {
  showResetModal.value = false
}

// 최종 초기화 처리
const handleFinalReset = () => {
  showResetModal.value = false
  
  // 폼 초기화
  form.value?.reset()
  form.value?.resetValidation()

  // 프로필 이미지 초기화
  profileImageUrl.value = ''

  // 지원자 정보 초기화
  applicant.value = {
    name: '',
    phone: '',
    email: '',
    birthDate: '',
    address: '',
    profileImage: null,
    profileImageKey: null,
  }

  console.log('🔄 폼이 초기화되었습니다.')
  toast.info('폼이 초기화되었습니다.')
}

const resetForm = () => {
  // 폼 초기화
  form.value?.reset()
  form.value?.resetValidation()

  // 프로필 이미지 초기화
  profileImageUrl.value = ''

  // 지원자 정보 초기화
  applicant.value = {
    name: '',
    phone: '',
    email: '',
    birthDate: '',
    address: '',
    profileImage: null,
    profileImageKey: null,
  }

  console.log('🔄 폼이 초기화되었습니다.')
  toast.info('폼이 초기화되었습니다.')
}
</script>

<style scoped>
.register-container {
  max-width: none;
  margin: 0;
  padding: 60px 0;
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  min-height: 100vh;
}

.content-wrapper {
  background: white;
  border-radius: 16px;
  padding: 40px;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
  margin: 0 24px;
  max-width: 800px;
  margin-left: auto;
  margin-right: auto;
}

.page-header {
  text-align: center;
  margin-bottom: 40px;
  padding-bottom: 32px;
  border-bottom: 2px solid #f1f5f9;
}

.page-title {
  font-size: 32px;
  font-weight: 800;
  color: #1e293b;
  margin-bottom: 16px;
  letter-spacing: -0.025em;
}

.page-subtitle {
  font-size: 16px;
  color: #64748b;
  line-height: 1.6;
  max-width: 500px;
  margin: 0 auto;
}

.form-card {
  margin-bottom: 32px;
}

.form-section {
  margin-bottom: 40px;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 20px;
  font-weight: 700;
  color: #1e293b;
  margin-bottom: 24px;
  padding-bottom: 12px;
  border-bottom: 1px solid #e2e8f0;
}

.section-icon {
  color: #3b82f6;
}

.form-field {
  background: #f8fafc;
  border-radius: 8px;
}

.form-field :deep(.v-field) {
  border-radius: 8px;
}

.form-field :deep(.v-field__outline) {
  border-color: #e2e8f0;
}

.form-field :deep(.v-field--focused .v-field__outline) {
  border-color: #3b82f6;
}

.profile-upload-section {
  background: #f8fafc;
  border-radius: 12px;
  padding: 24px;
  border: 1px solid #e2e8f0;
}

.file-input {
  background: white;
  border-radius: 8px;
}

.upload-progress {
  margin-top: 16px;
  border-radius: 4px;
}

.image-preview {
  margin-top: 24px;
}

.preview-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  border: 1px solid #e2e8f0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.preview-title {
  font-size: 16px;
  font-weight: 600;
  color: #1e293b;
  margin-bottom: 16px;
}

.preview-content {
  text-align: center;
}

.preview-image {
  border-radius: 8px;
  border: 2px solid #e2e8f0;
  margin-bottom: 16px;
}

.preview-actions {
  display: flex;
  justify-content: center;
}

.upload-info {
  margin-top: 16px;
  border-radius: 8px;
}

.action-buttons {
  display: flex;
  justify-content: center;
  gap: 16px;
  padding-top: 32px;
  border-top: 1px solid #e2e8f0;
}

.reset-btn {
  border-radius: 12px;
  font-weight: 600;
  height: 48px;
  min-width: 120px;
}

.submit-btn {
  background: linear-gradient(135deg, #3b82f6, #8b5cf6) !important;
  border-radius: 12px !important;
  font-weight: 700 !important;
  height: 48px !important;
  min-width: 120px !important;
  text-transform: none !important;
  box-shadow: 0 4px 14px 0 rgba(59, 130, 246, 0.4) !important;
  transition: all 0.3s ease !important;
}

.submit-btn:hover {
  transform: translateY(-2px) !important;
  box-shadow: 0 8px 25px 0 rgba(59, 130, 246, 0.5) !important;
}

.submit-btn:disabled {
  transform: none !important;
  box-shadow: 0 4px 14px 0 rgba(59, 130, 246, 0.4) !important;
}

/* 반응형 디자인 */
@media (max-width: 768px) {
  .register-container {
    padding: 40px 0;
  }

  .content-wrapper {
    margin: 0 16px;
    padding: 24px 20px;
  }

  .page-title {
    font-size: 28px;
  }

  .page-subtitle {
    font-size: 15px;
  }

  .section-title {
    font-size: 18px;
  }

  .profile-upload-section {
    padding: 20px;
  }

  .action-buttons {
    flex-direction: column;
    align-items: center;
  }

  .reset-btn,
  .submit-btn {
    width: 100%;
    max-width: 300px;
  }
}

@media (max-width: 480px) {
  .register-container {
    padding: 32px 0;
  }

  .content-wrapper {
    margin: 0 12px;
    padding: 20px 16px;
  }

  .page-title {
    font-size: 24px;
  }

  .profile-upload-section {
    padding: 16px;
  }

  .preview-card {
    padding: 20px;
  }
}
</style>