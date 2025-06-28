<template>
  <div class="standard-item-create-container">
    <div class="content-wrapper">
      <div class="page-header">
        <h1 class="page-title">자기소개서 기준표 항목 관리</h1>
        <p class="page-subtitle">채용에 사용할 기준표 항목을 관리합니다</p>
      </div>

      <div class="form-sections">
        <div class="form-section">
          <h2 class="section-title">
            <v-icon class="section-icon">mdi-plus-circle</v-icon>
            새 항목 추가
          </h2>
          <div class="add-item-container">
            <v-text-field
              v-model="newCriteria"
              label="새 기준표 항목 내용"
              variant="outlined"
              class="form-field"
              prepend-inner-icon="mdi-format-list-bulleted"
              @keyup.enter="addCriteria"
            />
          </div>
        </div>

        <div class="form-section">
          <h2 class="section-title">
            <v-icon class="section-icon">mdi-format-list-bulleted</v-icon>
            등록된 항목 목록
          </h2>
          <div class="items-container">
            <div v-if="items && items.length > 0" class="items-list">
              <div v-for="(item, index) in items" :key="item.id" class="item-card">
                <div class="item-content">
                  <div class="item-number">{{ index + 1 }}</div>
                  <div class="item-text">{{ item.content }}</div>
                  <v-btn 
                    icon 
                    size="small" 
                    color="error" 
                    variant="text" 
                    @click="removeItem(item.id)"
                    class="delete-btn"
                  >
                    <v-icon>mdi-delete</v-icon>
                  </v-btn>
                </div>
              </div>
            </div>
            <div v-else class="empty-state">
              <v-icon size="large" color="grey">mdi-format-list-bulleted</v-icon>
              <p>등록된 기준표 항목이 없습니다.</p>
            </div>
          </div>
        </div>
      </div>

      <div class="action-buttons">
        <v-btn color="primary" @click="goToCreateStandard" size="large" class="create-btn">
          <v-icon left>mdi-plus</v-icon>
          기준표 생성 페이지로 이동
        </v-btn>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useIntroduceStandardItemStore } from '@/stores/introduceStandardItemStore'
import { useMemberStore } from '@/stores/memberStore'

const store = useIntroduceStandardItemStore()
const memberStore = useMemberStore()
const newCriteria = ref('')
const router = useRouter()

onMounted(async () => {
  await Promise.all([
    memberStore.getMyInfo(),
    store.fetchItems()
  ])
})

const items = computed(() => store.items)

const addCriteria = async () => {
  if (!newCriteria.value.trim()) return
  await store.addItem(newCriteria.value)
  newCriteria.value = ''
}

const goToCreateStandard = () => {
  router.push('/employment/introduce-standard/create')
}

const removeItem = async (id) => {
  if (confirm('정말로 이 항목을 삭제하시겠습니까?')) {
    await store.removeItem(id)
  }
}
</script>

<style scoped>
.standard-item-create-container {
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

.form-sections {
  margin-bottom: 40px;
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

.add-item-container {
  background: #f8fafc;
  border-radius: 12px;
  padding: 24px;
  border: 1px solid #e2e8f0;
}

.form-field {
  background: white;
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

.items-container {
  background: #f8fafc;
  border-radius: 12px;
  padding: 24px;
  border: 1px solid #e2e8f0;
}

.items-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.item-card {
  background: white;
  border-radius: 8px;
  padding: 16px;
  border: 1px solid #e2e8f0;
  transition: all 0.3s ease;
}

.item-card:hover {
  border-color: #3b82f6;
  box-shadow: 0 2px 8px rgba(59, 130, 246, 0.1);
}

.item-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.item-number {
  width: 28px;
  height: 28px;
  background: linear-gradient(135deg, #3b82f6, #8b5cf6);
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 12px;
  flex-shrink: 0;
}

.item-text {
  font-size: 15px;
  font-weight: 500;
  color: #374151;
  line-height: 1.5;
  flex: 1;
}

.delete-btn {
  flex-shrink: 0;
}

.empty-state {
  text-align: center;
  padding: 48px 24px;
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

.create-btn {
  background: linear-gradient(135deg, #3b82f6, #8b5cf6) !important;
  border-radius: 12px !important;
  font-weight: 700 !important;
  height: 48px !important;
  min-width: 200px !important;
  text-transform: none !important;
  box-shadow: 0 4px 14px 0 rgba(59, 130, 246, 0.4) !important;
  transition: all 0.3s ease !important;
}

.create-btn:hover {
  transform: translateY(-2px) !important;
  box-shadow: 0 8px 25px 0 rgba(59, 130, 246, 0.5) !important;
}

/* 반응형 디자인 */
@media (max-width: 768px) {
  .standard-item-create-container {
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
  
  .add-item-container,
  .items-container {
    padding: 20px;
  }
  
  .item-content {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
  
  .delete-btn {
    align-self: flex-end;
  }
}

@media (max-width: 480px) {
  .standard-item-create-container {
    padding: 24px 0;
  }
  
  .content-wrapper {
    margin: 0;
    padding: 24px 20px;
  }
  
  .page-title {
    font-size: 24px;
  }
  
  .add-item-container,
  .items-container {
    padding: 16px;
  }
  
  .item-card {
    padding: 12px;
  }
}
</style> 