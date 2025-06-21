<template>
  <div>
    <CareerHeader />
    <v-container class="custom-container">
      <h2 class="page-title">지원서 작성하기</h2>

      <v-row>
        <!-- 건드 이러서 -->
        <v-col cols="12" md="6">
          <div class="resume-box">
            <p class="resume-placeholder">이력서 자리<br />(최신)</p>
          </div>
        </v-col>

        <!-- 자지소개서 -->
        <v-col cols="12" md="6">
          <h3 class="section-title">자기소개서</h3>

          <template v-if="template">
            <v-text-field
              label="자기소개서 제목"
              :model-value="template.title"
              variant="outlined"
              density="compact"
              class="mb-4"
              readonly
            />
            <div v-if="templateItems.length > 0">
              <v-text-field
                v-for="item in templateItems"
                :key="item.id"
                :label="item.title"
                variant="outlined"
                density="compact"
                class="mb-3"
                v-model="itemAnswers[item.id]"
              />
            </div>
            <div v-else class="text-grey">연결된 자기소개서 항목이 없습니다.</div>
          </template>
          <template v-else>
            <div class="text-grey">연결된 자기소개서 템플릿이 없습니다.</div>
          </template>

          <div class="button-group mt-4">
            <v-btn variant="outlined" color="success" class="mr-2">취소</v-btn>
            <v-btn color="success" class="submit-btn" @click="handleSubmit">등록</v-btn>
          </div>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import CareerHeader from '@/components/career/CareerHeader.vue'
import { useRecruitmentStore } from '@/stores/recruitmentStore'
import { useIntroduceTemplateStore } from '@/stores/introduceTemplateStore'
import { createIntroduceTemplateItemResponse } from '@/services/introduceService'
import { useIntroduceStore } from '@/stores/introduceStore'

const route = useRoute()
const id = Number(route.params.id)

const recruitmentStore = useRecruitmentStore()
const introduceTemplateStore = useIntroduceTemplateStore()
const introduceStore = useIntroduceStore()

const template = computed(() => introduceTemplateStore.selectedTemplate)
const templateItems = computed(() => template.value?.items || [])

// 항목별 입력값 관리
const itemAnswers = ref({})

onMounted(async () => {
  await recruitmentStore.loadRecruitmentDetail(id)
  const introduceTemplateId = recruitmentStore.detail?.recruitment?.introduceTemplateId
  if (introduceTemplateId) {
    await introduceTemplateStore.loadTemplateDetail(introduceTemplateId)
  }
})

// 등록 버튼 클릭 시 introduce 테이블에 먼저 insert 후 introduceId로 항목별 응답 등록
const handleSubmit = async () => {
  try {
    // 1. introduce 테이블에 insert (로그인 유저 id, 템플릿 id, content)
    // memberId는 실제 로그인 유저 id로 대체 필요 (예시로 1)
    const memberId = 1
    const introduceTemplateId = recruitmentStore.detail?.recruitment?.introduceTemplateId
    const content = '' // 템플릿 기반이므로 content는 비워둠
    const introduceId = await introduceStore.createIntroduce({ memberId, introduceTemplateId, content })
    if (!introduceId) throw new Error('자기소개서 등록 실패: introduceId 없음')

    // 2. introduce_template_item_response에 항목별 응답 등록
    for (const item of templateItems.value) {
      const itemContent = itemAnswers.value[item.id] || ''
      await createIntroduceTemplateItemResponse({
        introduceId,
        introduceTemplateItemId: item.id,
        content: itemContent
      })
    }
    alert('자기소개서가 성공적으로 등록되었습니다.')
  } catch (e) {
    alert('등록 실패: ' + e)
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
