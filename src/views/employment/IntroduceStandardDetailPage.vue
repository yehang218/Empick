<template>
  <v-container class="standard-detail-container">
    <div class="header-area">
      <h2 class="page-title">자기소개서 기준표 상세 조회</h2>
    </div>
    <div class="content-card">
      <div class="field-group">
        <label class="field-label">기준표 제목</label>
        <v-text-field
          :model-value="standard?.content"
          variant="outlined"
          readonly
          hide-details
          class="field-value-input"
        ></v-text-field>
      </div>
      <template v-if="items.length > 0">
        <div class="field-group">
          <label class="field-label">항목 목록</label>
          <div class="item-list-display">
            <div v-for="(item, index) in items" :key="item.id" class="item-display">
              <v-text-field
                :model-value="item.title || item.content"
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
    </div>
    <div class="d-flex justify-end mt-8">
      <v-btn color="grey-darken-1" variant="outlined" @click="goList">목록으로</v-btn>
    </div>
  </v-container>
</template>

<script setup>
import { onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useIntroduceStandardStore } from '@/stores/introduceStandardStore'
import { useIntroduceStandardItemStore } from '@/stores/introduceStandardItemStore'

const route = useRoute()
const router = useRouter()
const standardStore = useIntroduceStandardStore()
const standardItemStore = useIntroduceStandardItemStore()
const standard = computed(() => {
  // 1. API로 받아온 상세가 있으면 우선 사용
  if (standardStore.standardDetail) return standardStore.standardDetail
  // 2. 없으면, standards 배열에서 id가 일치하는 임시 객체 fallback
  const id = Number(route.params.id)
  return standardStore.standards.find(s => s.id === id)
})

// 프론트에서만 관리되는 itemIds로 항목 리스트 생성
const items = computed(() => {
  if (!standard.value) return []
  if (standard.value.items && standard.value.items.length > 0) return standard.value.items
  if (standard.value.itemIds && Array.isArray(standard.value.itemIds)) {
    return standardItemStore.items.filter(item => standard.value.itemIds.includes(item.id))
  }
  return []
})

onMounted(async () => {
  await standardStore.fetchStandardDetail(route.params.id)
  await standardItemStore.fetchItems()
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