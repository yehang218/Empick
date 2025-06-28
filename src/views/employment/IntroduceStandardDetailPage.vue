<template>
  <div class="standard-detail-container">
    <div class="content-wrapper">
      <div class="page-header">
        <h1 class="page-title">자기소개서 기준표 상세 조회</h1>
        <p class="page-subtitle">기준표의 상세 정보를 확인합니다</p>
      </div>
      
      <!-- 로딩 상태 -->
      <template v-if="loading">
        <div class="loading-state">
          <v-progress-circular indeterminate color="primary" size="48"></v-progress-circular>
          <p>기준표 정보를 불러오는 중...</p>
        </div>
      </template>
      
      <!-- 에러 상태 -->
      <template v-else-if="error">
        <div class="error-state">
          <v-alert type="error" class="error-alert">
            <v-icon left>mdi-alert-circle</v-icon>
            데이터를 불러오는 중 오류가 발생했습니다: {{ error.message }}
          </v-alert>
        </div>
      </template>
      
      <!-- 메인 컨텐츠 -->
      <template v-else>
        <div class="detail-sections">
          <div class="detail-section">
            <h2 class="section-title">
              <v-icon class="section-icon">mdi-format-title</v-icon>
              기준표 제목
            </h2>
            <v-text-field
              :model-value="standard?.content || '제목 없음'"
              variant="outlined"
              readonly
              hide-details
              class="detail-field"
            ></v-text-field>
          </div>
          
          <div class="detail-section">
            <h2 class="section-title">
              <v-icon class="section-icon">mdi-format-list-bulleted</v-icon>
              항목 목록 
              <span class="item-count">({{ items.length }}개)</span>
            </h2>
            
            <template v-if="items.length > 0">
              <div class="items-display">
                <div v-for="(item, index) in items" :key="item.id" class="item-display-card">
                  <div class="item-number">{{ index + 1 }}</div>
                  <v-text-field
                    :model-value="item.title || item.content || '항목 내용 없음'"
                    variant="outlined"
                    readonly
                    hide-details
                    class="item-field"
                  ></v-text-field>
                </div>
              </div>
            </template>
            
            <template v-else>
              <div class="empty-state">
                <v-icon size="large" color="grey">mdi-format-list-bulleted</v-icon>
                <p>이 기준표에 연결된 자기소개서 항목이 없습니다.</p>
              </div>
            </template>
          </div>
        </div>
        
        <div class="action-buttons">
          <v-btn color="grey darken-1" variant="outlined" @click="goList" size="large" class="back-btn">
            <v-icon left>mdi-arrow-left</v-icon>
            목록으로
          </v-btn>
        </div>
      </template>
    </div>
  </div>
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
  max-width: none;
  margin: 0;
  padding: 40px 0;
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  min-height: 100vh;
}

.content-wrapper {
  background: white;
  border-radius: 0;
  padding: 40px 60px;
  box-shadow: none;
  margin: 0;
  max-width: none;
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

.loading-state {
  text-align: center;
  padding: 60px 24px;
  color: #64748b;
}

.loading-state p {
  margin-top: 24px;
  font-size: 16px;
  font-weight: 500;
}

.error-state {
  padding: 24px;
}

.error-alert {
  border-radius: 12px;
}

.detail-sections {
  margin-bottom: 40px;
}

.detail-section {
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

.item-count {
  font-size: 14px;
  font-weight: 500;
  color: #64748b;
  background: #f1f5f9;
  padding: 4px 8px;
  border-radius: 12px;
  margin-left: 8px;
}

.detail-field {
  background: #f8fafc;
  border-radius: 8px;
}

.detail-field :deep(.v-field) {
  border-radius: 8px;
}

.detail-field :deep(.v-field__outline) {
  border-color: #e2e8f0;
}

.items-display {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.item-display-card {
  display: flex;
  align-items: center;
  gap: 16px;
  background: #f8fafc;
  border-radius: 12px;
  padding: 16px;
  border: 1px solid #e2e8f0;
}

.item-number {
  width: 32px;
  height: 32px;
  background: linear-gradient(135deg, #3b82f6, #8b5cf6);
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 14px;
  flex-shrink: 0;
}

.item-field {
  background: white;
  border-radius: 8px;
  flex: 1;
}

.item-field :deep(.v-field) {
  border-radius: 8px;
}

.item-field :deep(.v-field__outline) {
  border-color: #e2e8f0;
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
  padding-top: 32px;
  border-top: 1px solid #e2e8f0;
}

.back-btn {
  border-radius: 12px;
  font-weight: 600;
  height: 48px;
  min-width: 140px;
}

/* 반응형 디자인 */
@media (max-width: 768px) {
  .standard-detail-container {
    padding: 32px 0;
  }
  
  .content-wrapper {
    margin: 0;
    padding: 32px 40px;
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
  
  .item-display-card {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
  
  .item-field {
    width: 100%;
  }
}

@media (max-width: 480px) {
  .standard-detail-container {
    padding: 24px 0;
  }
  
  .content-wrapper {
    margin: 0;
    padding: 24px 20px;
  }
  
  .page-title {
    font-size: 24px;
  }
  
  .item-display-card {
    padding: 12px;
  }
  
  .empty-state {
    padding: 32px 16px;
  }
}
</style> 