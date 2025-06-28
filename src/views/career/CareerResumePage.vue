<template>
  <div>
    <CareerHeader />
    <div class="resume-container">
      <div class="content-wrapper">
        <div class="page-header">
          <h1 class="page-title">지원서 작성하기</h1>
          <p class="page-subtitle">EMPICK과 함께 성장할 준비를 마치기 위한 마지막 단계입니다.</p>
        </div>

        <div class="form-sections">
          <!-- 이력서(지원서) 항목 동적 렌더링 -->
          <div class="form-section">
            <h2 class="section-title">
              <v-icon class="section-icon">mdi-file-document</v-icon>
              이력서
            </h2>
            <div v-if="applicationItems.length > 0" class="items-container">
              <div v-for="item in applicationItems" :key="item.id" class="form-item">
                <div class="item-header">
                  <h3 class="item-title">{{ item.categoryName }}</h3>
                  <div class="item-meta">
                    <span class="input-type">{{ item.inputType === 0 ? '텍스트' : '기타' }}</span>
                    <v-chip v-if="item.required" size="x-small" color="error" variant="elevated" class="required-badge">
                      필수
                    </v-chip>
                  </div>
                </div>
                <v-textarea v-model="applicationAnswers[item.id]"
                  :label="item.categoryName + (item.required ? ' *' : '')" :required="item.required" variant="outlined"
                  rows="4" auto-grow class="form-textarea" />
              </div>
            </div>
            <div v-else class="empty-state">
              <v-icon size="large" color="grey">mdi-file-document-outline</v-icon>
              <p>지원서 항목이 없습니다.</p>
            </div>
          </div>

          <!-- 자기소개서 -->
          <div class="form-section">
            <h2 class="section-title">
              <v-icon class="section-icon">mdi-account-edit</v-icon>
              자기소개서
            </h2>

            <template v-if="template">
              <div v-if="templateItems.length > 0" class="items-container">
                <div v-for="item in templateItems" :key="item.id" class="form-item">
                  <div class="item-header">
                    <h3 class="item-title">{{ item.title }}</h3>
                  </div>
                  <v-textarea :label="item.title" variant="outlined" rows="6" auto-grow v-model="itemAnswers[item.id]"
                    class="form-textarea" />
                </div>
              </div>
              <div v-else class="empty-state">
                <v-icon size="large" color="grey">mdi-account-edit-outline</v-icon>
                <p>연결된 자기소개서 항목이 없습니다.</p>
              </div>
            </template>
            <template v-else>
              <div class="empty-state">
                <v-icon size="large" color="grey">mdi-account-edit-outline</v-icon>
                <p>연결된 자기소개서 템플릿이 없습니다.</p>
              </div>
            </template>
          </div>
        </div>

        <div class="action-buttons">
          <v-btn variant="outlined" color="grey darken-1" @click="handleCancel" size="large" class="cancel-btn">
            <v-icon left>mdi-arrow-left</v-icon>
            취소하기
          </v-btn>
          <v-btn color="primary" @click="handleSubmit" size="large" class="submit-btn">
            <v-icon left>mdi-send</v-icon>
            등록
          </v-btn>
        </div>
      </div>
    </div>

    <!-- 제출 확인 Modal -->
    <AlertModal v-if="showSubmitModal" title="지원서 제출 확인" message="제출한 지원서는 수정할 수 없습니다. 정말 제출하시겠습니까?" confirm-text="제출하기"
      cancel-text="취소" @confirm="handleFinalSubmit" @cancel="handleCancelSubmit" />

    <!-- 취소 확인 Modal -->
    <AlertModal v-if="showCancelModal" title="작성 취소 확인" message="작성 중인 내용이 모두 사라집니다. 정말 취소하시겠습니까?" confirm-text="취소하기"
      cancel-text="계속 작성" @confirm="handleFinalCancel" @cancel="handleCancelModal" />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useToast } from 'vue-toastification'
import CareerHeader from '@/components/career/CareerHeader.vue'
import AlertModal from '@/components/common/AlertModal.vue'
import { useRecruitmentStore } from '@/stores/recruitmentStore'
import { useIntroduceTemplateStore } from '@/stores/introduceTemplateStore'
import { useIntroduceStore } from '@/stores/introduceStore'
import { useApplicationItemStore } from '@/stores/applicationItemStore'
import { useApplicationStore } from '@/stores/applicationStore'
// import api from '@/apis/apiClient' // 아키텍처 규칙 준수를 위해 제거

const route = useRoute()
const router = useRouter()
const toast = useToast()
const id = Number(route.params.id)

// 제출 확인 모달 상태
const showSubmitModal = ref(false)
// 취소 확인 모달 상태
const showCancelModal = ref(false)

// URL 파라미터 또는 로컬 스토리지에서 ID 가져오기
console.log('🔍 ID 소스 확인:', {
  'route.query.applicantId': route.query.applicantId,
  'route.query.applicationId': route.query.applicationId,
  'localStorage.currentApplicantId': localStorage.getItem('currentApplicantId'),
  'localStorage.currentApplicationId': localStorage.getItem('currentApplicationId')
})

const applicantId = ref(Number(route.query.applicantId) || Number(localStorage.getItem('currentApplicantId')) || null)
const applicationId = ref(Number(route.query.applicationId) || Number(localStorage.getItem('currentApplicationId')) || null)

// 상세 로깅 추가
console.log('🔍 변환된 ID 값들:', {
  'applicantId.value': applicantId.value,
  'applicationId.value': applicationId.value,
  'typeof applicantId.value': typeof applicantId.value,
  'typeof applicationId.value': typeof applicationId.value,
  'isNaN(applicantId.value)': isNaN(applicantId.value),
  'isNaN(applicationId.value)': isNaN(applicationId.value)
})

console.log('🔍 Resume Page - IDs:', { applicantId: applicantId.value, applicationId: applicationId.value, recruitmentId: id })

// ID 유효성 즉시 확인
if (!applicantId.value || applicantId.value <= 0) {
  console.error('❌ 유효하지 않은 applicantId:', applicantId.value)
}
if (!applicationId.value || applicationId.value <= 0) {
  console.error('❌ 유효하지 않은 applicationId:', applicationId.value)
}

const recruitmentStore = useRecruitmentStore()
const introduceTemplateStore = useIntroduceTemplateStore()
const introduceStore = useIntroduceStore()
const applicationItemStore = useApplicationItemStore()
const applicationStore = useApplicationStore()

const template = computed(() => introduceTemplateStore.selectedTemplate)
const templateItems = computed(() => template.value?.items || [])
const applicationItems = computed(() => applicationItemStore.items)
const applicationAnswers = ref({})

// 항목별 입력값 관리
const itemAnswers = ref({})

onMounted(async () => {
  await recruitmentStore.loadRecruitmentDetail(id)
  // 지원서 항목(application_item) 로딩
  await applicationItemStore.loadApplicationItems(id)
  const introduceTemplateId = recruitmentStore.detail?.recruitment?.introduceTemplateId
  if (introduceTemplateId) {
    await introduceTemplateStore.loadTemplateDetail(introduceTemplateId)
  }
})

const handleGoBack = () => {
  router.back()
}

// 취소 버튼 클릭 핸들러
const handleCancel = () => {
  showCancelModal.value = true
}

// 취소 모달 닫기
const handleCancelModal = () => {
  showCancelModal.value = false
}

// 최종 취소 처리
const handleFinalCancel = () => {
  showCancelModal.value = false
  router.back()
}

// 제출 확인 모달 핸들러
const handleSubmit = () => {
  // 필수 항목 유효성 검사
  const requiredItems = applicationItems.value.filter(item => item.required === true)
  const missingRequiredItems = []
  
  for (const item of requiredItems) {
    const answer = applicationAnswers.value[item.id]
    if (!answer || answer.trim() === '') {
      missingRequiredItems.push(item.categoryName)
    }
  }
  
  if (missingRequiredItems.length > 0) {
    const missingItemsText = missingRequiredItems.join(', ')
    toast.error(`다음 필수 항목을 입력해주세요: ${missingItemsText}`)
    return
  }

  // 유효성 검사 통과 시 확인 모달 표시
  showSubmitModal.value = true
}

const handleCancelSubmit = () => {
  showSubmitModal.value = false
}

const processApplicationResponses = async (finalApplicationId) => {
  console.log('🔄 이력서 등록 시작')

  for (const [itemId, content] of Object.entries(applicationAnswers.value)) {
    const applicationResponsePayload = {
      applicationId: finalApplicationId,
      applicationItemId: Number(itemId),
      content: content || ''
    }

    console.log('📝 이력서 항목 등록:', applicationResponsePayload)

    try {
      await applicationStore.createApplicationResponse(applicationResponsePayload)
      console.log(`✅ 이력서 항목 ${itemId} 등록 성공`)
    } catch (error) {
      console.error(`❌ 이력서 항목 ${itemId} 등록 실패:`, error.message)
      throw new Error(`이력서 항목 등록 실패: ${error.message}`)
    }
  }

  console.log('✅ 이력서 등록 완료')
}

// 최종 제출 처리 함수
const handleFinalSubmit = async () => {
  showSubmitModal.value = false
  try {
    // ID 유효성 검사
    if (!applicantId.value || !applicationId.value) {
      throw new Error('지원자 ID 또는 지원서 ID가 없습니다. 인적사항부터 다시 등록해주세요.')
    }

    console.log('🔄 이력서/자기소개서 등록 시작')

    // 1. introduce 테이블에 레코드 생성 (템플릿 ID와 함께)
    const introduceTemplateId = recruitmentStore.detail?.recruitment?.introduceTemplateId

    console.log('🔄 자기소개서 생성 시작:', { applicantId: applicantId.value, applicationId: applicationId.value, introduceTemplateId })

    // ID 값 검증 및 임시 fallback
    let finalApplicantId = applicantId.value
    let finalApplicationId = applicationId.value

    if (!finalApplicantId || finalApplicantId <= 0) {
      console.error('❌ applicantId가 유효하지 않습니다:', finalApplicantId)
      throw new Error('지원자 ID가 유효하지 않습니다. 인적사항부터 다시 등록해주세요.')
    }
    if (!finalApplicationId || finalApplicationId <= 0) {
      console.error('❌ applicationId가 유효하지 않습니다:', finalApplicationId)
      throw new Error('지원서 ID가 유효하지 않습니다. 인적사항부터 다시 등록해주세요.')
    }
    if (!introduceTemplateId || introduceTemplateId <= 0) {
      throw new Error(`유효하지 않은 자기소개서 템플릿 ID: ${introduceTemplateId}`)
    }

    console.log('🔧 최종 사용할 ID들:', { finalApplicantId, finalApplicationId, introduceTemplateId })

    // 1. 새로운 자기소개서 생성
    const introducePayload = {
      applicantId: finalApplicantId,
      applicationId: finalApplicationId,
      introduceTemplateId,
      content: '' // 템플릿 기반이므로 content는 비워둠
    }

    console.log('📤 자기소개서 생성 최종 페이로드:', introducePayload)

    const newIntroduce = await introduceStore.createIntroduce(introducePayload)
    const introduceId = newIntroduce.id || newIntroduce

    if (!introduceId) throw new Error('자기소개서 생성 실패')
    console.log('✅ 자기소개서 생성 성공:', introduceId)

    // 2. introduce_template_item_response에 항목별 응답 등록
    console.log('🔄 자기소개서 항목별 응답 등록 시작')
    for (const item of templateItems.value) {
      const itemContent = itemAnswers.value[item.id] || ''
      console.log('📝 항목 응답 등록:', { introduceId, itemId: item.id, content: itemContent })

      await introduceStore.createTemplateItemResponse({
        introduceId,
        introduceTemplateItemId: item.id,
        content: itemContent
      })
    }
    console.log('✅ 자기소개서 항목별 응답 등록 완료')

    // 3. application_response(이력서) 등록
    await processApplicationResponses(finalApplicationId)

    toast.success('자기소개서와 이력서가 성공적으로 등록되었습니다.')

    // 완료 후 채용공고 목록 페이지로 이동
    router.push('/career/recruitments/')

  } catch (e) {
    console.error('❌ 등록 실패:', e)
    toast.error('등록 실패: ' + e.message)
  }
}
</script>

<style scoped>
.resume-container {
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
  max-width: 1000px;
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

.form-sections {
  margin-bottom: 40px;
}

.form-section {
  margin-bottom: 48px;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 24px;
  font-weight: 700;
  color: #1e293b;
  margin-bottom: 24px;
  padding-bottom: 12px;
  border-bottom: 1px solid #e2e8f0;
}

.section-icon {
  color: #3b82f6;
}

.items-container {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.form-item {
  background: #f8fafc;
  border-radius: 12px;
  padding: 24px;
  border: 1px solid #e2e8f0;
  transition: all 0.3s ease;
}

.form-item:hover {
  border-color: #3b82f6;
  box-shadow: 0 2px 8px rgba(59, 130, 246, 0.1);
}

.item-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.item-title {
  font-size: 18px;
  font-weight: 600;
  color: #1e293b;
  margin: 0;
}

.item-meta {
  display: flex;
  align-items: center;
  gap: 8px;
}

.input-type {
  font-size: 12px;
  color: #64748b;
  background: #e2e8f0;
  padding: 4px 8px;
  border-radius: 12px;
  font-weight: 500;
}

.required-badge {
  font-size: 10px;
  font-weight: 600;
}

.form-textarea {
  background: white;
  border-radius: 8px;
}

.form-textarea :deep(.v-field) {
  border-radius: 8px;
}

.form-textarea :deep(.v-field__outline) {
  border-color: #e2e8f0;
}

.form-textarea :deep(.v-field--focused .v-field__outline) {
  border-color: #3b82f6;
}

.empty-state {
  text-align: center;
  padding: 48px 24px;
  background: #f8fafc;
  border-radius: 12px;
  border: 2px dashed #cbd5e1;
  color: #64748b;
}

.empty-state p {
  margin-top: 16px;
  font-size: 16px;
  font-weight: 500;
}

.action-buttons {
  display: flex;
  justify-content: center;
  gap: 16px;
  padding-top: 32px;
  border-top: 1px solid #e2e8f0;
}

.cancel-btn {
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

/* 반응형 디자인 */
@media (max-width: 768px) {
  .resume-container {
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
    font-size: 20px;
  }

  .form-item {
    padding: 20px;
  }

  .item-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }

  .action-buttons {
    flex-direction: column;
    align-items: center;
  }

  .cancel-btn,
  .submit-btn {
    width: 100%;
    max-width: 300px;
  }
}

@media (max-width: 480px) {
  .resume-container {
    padding: 32px 0;
  }

  .content-wrapper {
    margin: 0 12px;
    padding: 20px 16px;
  }

  .page-title {
    font-size: 24px;
  }

  .form-item {
    padding: 16px;
  }

  .empty-state {
    padding: 32px 16px;
  }
}
</style>
