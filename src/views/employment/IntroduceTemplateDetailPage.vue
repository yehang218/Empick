<template>
  <v-container class="template-detail-container">
    <div class="header-area">
      <h2 class="page-title">자기소개서 템플릿 상세 조회</h2>
      <!-- <v-btn color="success" class="edit-btn" @click="goToEdit">
        <v-icon left>mdi-pencil</v-icon> 수정하기
      </v-btn> -->
    </div>

    <div class="content-card">
      <div class="field-group">
        <label class="field-label">템플릿 제목</label>
        <v-text-field
          :model-value="template?.title"
          variant="outlined"
          readonly
          hide-details
          class="field-value-input"
        ></v-text-field>
      </div>

      <template v-if="template && template.items && template.items.length > 0">
        <div class="field-group">
          <label class="field-label">항목 목록</label>
          <div class="item-list-display">
            <div v-for="(item, index) in template.items" :key="item.id" class="item-display">
              <v-text-field
                :model-value="item.title"
                variant="outlined"
                readonly
                hide-details
                class="field-value-input"
              ></v-text-field>
            </div>
          </div>
        </div>
      </template>
      <template v-else>
        <div class="field-group">
          <label class="field-label">항목 목록</label>
          <v-textarea
            model-value="연결된 자기소개서 항목이 없습니다."
            variant="outlined"
            readonly
            hide-details
            class="field-value-input no-items-textarea"
            rows="3"
          ></v-textarea>
        </div>
      </template>

      <!-- 다른 필드들 (회사 소개, 부서 소개, 모집 인원 등)은 백엔드 데이터에 따라 추가 예정 -->

    </div>

    <div class="d-flex justify-end mt-8">
      <v-btn color="grey-darken-1" variant="outlined" @click="goList">목록으로</v-btn>
    </div>
  </v-container>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useIntroduceTemplateStore } from '@/stores/introduceTemplateStore'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()
const introduceTemplateStore = useIntroduceTemplateStore()
const template = computed(() => introduceTemplateStore.selectedTemplate)

onMounted(async () => {
  try {
    await introduceTemplateStore.loadTemplateDetail(route.params.id)
    // console.log('Fetched template detail:', introduceTemplateStore.selectedTemplate)
  } catch (error) {
    console.error('템플릿 상세 로드 실패:', error)
    alert('템플릿 상세 정보를 불러오는 데 실패했습니다.')
    router.push('/employment/introduce-templates')
  }
})

const goList = () => router.push('/employment/introduce-templates')

// const goToEdit = () => {
//   alert('수정 페이지로 이동 (기능 미구현)');
//   // router.push(`/employment/recruitments/introduce-templates/edit/${route.params.id}`);
// }
</script>

<style scoped>
.template-detail-container {
  max-width: 900px;
  margin: 32px auto;
  padding: 0 24px;
}

.header-area {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32px;
  padding-bottom: 16px;
  border-bottom: 1px solid #eee;
}

.page-title {
  font-size: 2rem;
  font-weight: bold;
  color: #333;
  margin: 0;
}

.edit-btn {
  font-weight: bold;
  padding: 0 16px;
  height: 36px;
}

.content-card {
  background-color: #fff;
  border-radius: 12px;
  padding: 32px 40px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.06);
}

.field-group {
  margin-bottom: 24px;
}

.field-label {
  font-size: 1.1rem;
  font-weight: bold;
  color: #555;
  margin-bottom: 8px;
  display: block;
}

.field-value-input.v-text-field .v-input__control,
.field-value-input.v-textarea .v-input__control {
  background-color: #f8f8f8;
  border-radius: 8px;
  box-shadow: none !important;
}

.field-value-input .v-field__outline {
  border-color: #e0e0e0 !important;
}

.field-value-input.v-text-field.v-input--density-compact .v-field--variant-outlined,
.field-value-input.v-textarea.v-input--density-compact .v-field--variant-outlined {
  padding: 8px 12px;
}

.item-list-display .item-display {
  margin-bottom: 16px; /* 각 항목 사이 간격 */
}
.item-list-display .item-display:last-child {
  margin-bottom: 0;
}

.no-items-textarea.v-textarea .v-input__control textarea {
  color: #888; /* 텍스트 색상 연하게 */
  font-style: italic;
}
</style>
