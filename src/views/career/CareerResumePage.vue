<template>
  <div>
    <CareerHeader />
    <v-container class="custom-container">
      <h2 class="page-title">지원서 작성하기</h2>

      <v-row>
        <!-- 이력서(지원서) 항목 동적 렌더링 -->
        <v-col cols="12" md="6">
          <div>
            <h3 class="section-title">이력서</h3>
            <div v-if="applicationItems.length > 0">
              <v-card v-for="item in applicationItems" :key="item.id" class="mb-4 pa-4" elevation="2">
                <div class="font-weight-medium mb-2">
                  {{ item.categoryName }}
                  <span class="text-caption text-grey-darken-1">
                    ({{ item.inputType === 0 ? '텍스트' : '기타' }})
                  </span>
                  <v-chip 
                    v-if="item.required" 
                    size="x-small" 
                    color="red" 
                    variant="elevated"
                    class="ml-2"
                  >
                    필수
                  </v-chip>
                </div>
                <v-textarea
                  v-model="applicationAnswers[item.id]"
                  :label="item.categoryName + (item.required ? ' *' : '')"
                  :required="item.required"
                  variant="outlined"
                  rows="4"
                  auto-grow
                />
              </v-card>
            </div>
            <div v-else class="text-grey">지원서 항목이 없습니다.</div>
          </div>
        </v-col>

        <!-- 자지소개서 -->
        <v-col cols="12" md="6">
          <h3 class="section-title">자기소개서</h3>

          <template v-if="template">
            <div v-if="templateItems.length > 0">
              <v-textarea
                v-for="item in templateItems"
                :key="item.id"
                :label="item.title"
                variant="outlined"
                class="mb-3"
                rows="6"
                auto-grow
                v-model="itemAnswers[item.id]"
              />
            </div>
            <div v-else class="text-grey">연결된 자기소개서 항목이 없습니다.</div>
          </template>
          <template v-else>
            <div class="text-grey">연결된 자기소개서 템플릿이 없습니다.</div>
          </template>

          <div class="button-group mt-4">
            <v-btn variant="outlined" color="success" class="mr-2" @click="handleGoBack">취소</v-btn>
            <v-btn color="success" class="submit-btn" @click="handleSubmit">등록</v-btn>
          </div>
        </v-col>
      </v-row>
    </v-container>


  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useToast } from 'vue-toastification'
import CareerHeader from '@/components/career/CareerHeader.vue'
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

// 등록 버튼 클릭 시 introduce 테이블에 먼저 insert 후 introduceId로 항목별 응답 등록
const handleSubmit = async () => {
  try {
    // ID 유효성 검사
    if (!applicantId.value || !applicationId.value) {
      throw new Error('지원자 ID 또는 지원서 ID가 없습니다. 인적사항부터 다시 등록해주세요.')
    }

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
.custom-container {
  max-width: 1240px;
  padding-top: 40px;
}
.page-title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 32px;
}
.resume-box {
  width: 100%;
  height: 450px;
  background-color: #e0e0e0;
  display: flex;
  align-items: center;
  justify-content: center;
}
.resume-placeholder {
  text-align: center;
  color: #333;
  font-size: 14px;
}
.section-title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 16px;
}
.button-group {
  display: flex;
  justify-content: flex-end;
}
.submit-btn {
  font-weight: bold;
  color: white;
}
.text-grey {
  color: #888;
  font-style: italic;
}
</style>
