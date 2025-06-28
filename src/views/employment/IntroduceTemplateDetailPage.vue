<template>
  <div class="template-detail-container">
    <div class="content-wrapper">
      <div class="page-header">
        <h1 class="page-title">자기소개서 템플릿 상세 조회</h1>
        <p class="page-subtitle">템플릿의 상세 정보를 확인합니다</p>
      </div>

      <div class="detail-sections">
        <div class="detail-section">
          <h2 class="section-title">
            <v-icon class="section-icon">mdi-format-title</v-icon>
            템플릿 제목
          </h2>
          <v-text-field
            :model-value="template?.title"
            variant="outlined"
            readonly
            hide-details
            class="detail-field"
          ></v-text-field>
        </div>

        <template v-if="template && template.items && template.items.length > 0">
          <div class="detail-section">
            <h2 class="section-title">
              <v-icon class="section-icon">mdi-format-list-bulleted</v-icon>
              항목 목록 
              <span class="item-count">({{ template.items.length }}개)</span>
            </h2>
            <div class="items-display">
              <div v-for="(item, index) in template.items" :key="item.id" class="item-display-card">
                <div class="item-number">{{ index + 1 }}</div>
                <v-text-field
                  :model-value="item.title"
                  variant="outlined"
                  readonly
                  hide-details
                  class="item-field"
                ></v-text-field>
              </div>
            </div>
          </div>
        </template>
        <template v-else>
          <div class="detail-section">
            <h2 class="section-title">
              <v-icon class="section-icon">mdi-format-list-bulleted</v-icon>
              항목 목록
            </h2>
            <div class="empty-state">
              <v-icon size="large" color="grey">mdi-format-list-bulleted</v-icon>
              <p>연결된 자기소개서 항목이 없습니다.</p>
            </div>
          </div>
        </template>
      </div>

      <div class="action-buttons">
        <v-btn color="grey darken-1" variant="outlined" @click="goList" size="large" class="back-btn">
          <v-icon left>mdi-arrow-left</v-icon>
          목록으로
        </v-btn>
      </div>
    </div>
  </div>
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
  .template-detail-container {
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
  .template-detail-container {
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
