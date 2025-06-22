<template>
  <v-container class="standard-detail-container">
    <div class="header-area">
      <h2 class="page-title">자기소개서 기준표 상세 조회</h2>
    </div>
    
    <!-- 로딩 상태 -->
    <template v-if="loading">
      <div class="text-center py-8">
        <v-progress-circular indeterminate color="primary"></v-progress-circular>
        <p class="mt-4">기준표 정보를 불러오는 중...</p>
      </div>
    </template>
    
    <!-- 에러 상태 -->
    <template v-else-if="error">
      <v-alert type="error" class="mb-4">
        데이터를 불러오는 중 오류가 발생했습니다: {{ error.message }}
      </v-alert>
    </template>
    
    <!-- 메인 컨텐츠 -->
    <template v-else>
      <div class="content-card">
        <div class="field-group">
          <label class="field-label">기준표 제목</label>
          <v-text-field
            :model-value="standard?.content || '제목 없음'"
            variant="outlined"
            readonly
            hide-details
            class="field-value-input"
          ></v-text-field>
        </div>
        
        <div class="field-group">
          <label class="field-label">
            항목 목록 
            <span class="item-count">({{ items.length }}개)</span>
          </label>
          
          <template v-if="items.length > 0">
            <div class="item-list-display">
              <div v-for="(item, index) in items" :key="item.id" class="item-display">
                <v-text-field
                  :model-value="`${index + 1}. ${item.title || item.content || '항목 내용 없음'}`"
                  variant="outlined"
                  readonly
                  hide-details
                  class="field-value-input"
                ></v-text-field>
              </div>
            </div>
          </template>
          
          <template v-else>
            <v-textarea
              model-value="이 기준표에 연결된 자기소개서 항목이 없습니다."
              variant="outlined"
              readonly
              hide-details
              class="field-value-input no-items-textarea"
              rows="3"
            ></v-textarea>
          </template>
        </div>
      </div>
      
      <div class="d-flex justify-end mt-8">
        <v-btn color="grey-darken-1" variant="outlined" @click="goList">목록으로</v-btn>
      </div>
    </template>
  </v-container>
</template>

<script setup>
import { onMounted, computed, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useIntroduceStandardStore } from '@/stores/introduceStandardStore'
import { useIntroduceStandardItemStore } from '@/stores/introduceStandardItemStore'

const route = useRoute()
const router = useRouter()
const standardStore = useIntroduceStandardStore()
const standardItemStore = useIntroduceStandardItemStore()

const loading = ref(false)
const error = ref(null)

const standard = computed(() => {
  // 1. API로 받아온 상세가 있으면 우선 사용
  if (standardStore.standardDetail) return standardStore.standardDetail
  // 2. 없으면, standards 배열에서 id가 일치하는 임시 객체 fallback
  const id = Number(route.params.id)
  return standardStore.standards.find(s => s.id === id)
})

// 해당 기준표에 연결된 항목들만 표시
const items = computed(() => standardItemStore.items)

onMounted(async () => {
  try {
    loading.value = true
    error.value = null
    
    const standardId = route.params.id
    console.log('🔍 기준표 ID:', standardId)
    
    // 기준표 상세 정보 조회
    await standardStore.fetchStandardDetail(standardId)
    console.log('✅ 기준표 상세 조회 완료:', standardStore.standardDetail)
    
    // 해당 기준표의 항목들만 조회
    await standardItemStore.fetchItemsByStandardId(standardId)
    console.log('✅ 기준표별 항목 조회 완료. 항목 수:', standardItemStore.items.length)
    
  } catch (e) {
    console.error('❌ 페이지 초기화 실패:', e)
    error.value = e
  } finally {
    loading.value = false
  }
})

const goList = () => router.push('/employment/introduce-standard/list')
</script>

<style scoped>
.standard-detail-container {
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
.item-count {
  font-size: 0.9rem;
  font-weight: normal;
  color: #888;
  margin-left: 8px;
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
  margin-bottom: 16px;
}
.item-list-display .item-display:last-child {
  margin-bottom: 0;
}
.no-items-textarea.v-textarea .v-input__control textarea {
  color: #888;
  font-style: italic;
}
</style> 