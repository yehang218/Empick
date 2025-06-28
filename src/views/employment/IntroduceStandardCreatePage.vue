<template>
  <div class="standard-create-container">
    <div class="content-wrapper">
      <div class="page-header">
        <h1 class="page-title">자기소개서 기준표 생성</h1>
        <p class="page-subtitle">채용에 사용할 자기소개서 기준표를 생성합니다</p>
      </div>

      <div class="form-sections">
        <div class="form-section">
          <h2 class="section-title">
            <v-icon class="section-icon">mdi-format-title</v-icon>
            기준표 제목
          </h2>
          <v-text-field
            v-model="title"
            label="기준표 제목을 입력하세요"
            variant="outlined"
            class="form-field"
            prepend-inner-icon="mdi-format-title"
          />
        </div>

        <div class="form-section">
          <h2 class="section-title">
            <v-icon class="section-icon">mdi-format-list-bulleted</v-icon>
            기준표 항목 선택
            <span class="selection-count" v-if="selectedItemIds.length > 0">
              ({{ selectedItemIds.length }}개 선택됨)
            </span>
          </h2>
          <div class="items-container">
            <div v-if="items.length > 0" class="items-list">
              <div v-for="(item, index) in items" :key="item.id || index" class="item-card">
                <div class="item-content">
                  <v-checkbox 
                    v-model="selectedItemIds" 
                    :value="item.id" 
                    hide-details 
                    class="item-checkbox"
                  />
                  <div class="item-text">{{ item.content }}</div>
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
        <v-btn color="grey darken-1" variant="outlined" @click="goToManage" size="large" class="manage-btn">
          <v-icon left>mdi-cog</v-icon>
          기준표 항목 관리
        </v-btn>
        <v-btn color="primary" @click="submit" size="large" class="submit-btn">
          <v-icon left>mdi-check</v-icon>
          기준표 등록
        </v-btn>
      </div>
    </div>

    <!-- Alert Modal -->
    <AlertModal
      v-if="showModal"
      :title="modalTitle"
      :message="modalMessage"
      :confirm-text="modalConfirmText"
      :cancel-text="modalCancelText"
      @confirm="handleConfirm"
      @cancel="handleCancel"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useToast } from 'vue-toastification'
import { useIntroduceStandardItemStore } from '@/stores/introduceStandardItemStore'
import { useIntroduceStandardStore } from '@/stores/introduceStandardStore'
import { useMemberStore } from '@/stores/memberStore'
import AlertModal from '@/components/common/AlertModal.vue'

const router = useRouter()
const toast = useToast()
const standardItemStore = useIntroduceStandardItemStore()
const standardStore = useIntroduceStandardStore()
const memberStore = useMemberStore()

const title = ref('')
const items = computed(() => standardItemStore.items)
const selectedItemIds = ref([])

// Modal 상태 (간단한 방식)
const showModal = ref(false)
const modalTitle = ref('')
const modalMessage = ref('')
const modalConfirmText = ref('확인')
const modalCancelText = ref('취소')

function goToManage() {
  router.push('/employment/introduce-standard-items/manage')
}

function goToCriteriaList() {
  router.push('/employment/introduce-standard/list')
}

onMounted(async () => {
  await Promise.all([
    memberStore.getMyInfo(),
    standardItemStore.fetchItems()
  ])
})

const showConfirmModal = () => {
  modalTitle.value = '기준표 등록'
  modalMessage.value = '입력하신 내용으로 기준표를 등록하시겠습니까?'
  modalConfirmText.value = '등록'
  modalCancelText.value = '취소'
  showModal.value = true
}

const handleConfirm = async () => {
  showModal.value = false
  await performSubmit()
}

const handleCancel = () => {
  showModal.value = false
}

const performSubmit = async () => {
  try {
    await standardStore.addStandard(title.value, selectedItemIds.value)
    toast.success('기준표가 성공적으로 등록되었습니다.')
    router.push('/employment/introduce-standard/list')
  } catch (error) {
    console.error('기준표 등록 실패:', error)
    toast.error('기준표 등록에 실패했습니다. 서버 오류일 수 있습니다.')
  }
}

const submit = async () => {
  if (!title.value.trim()) {
    toast.error('기준표 제목을 입력해주세요.')
    return
  }
  if (selectedItemIds.value.length === 0) {
    toast.error('하나 이상의 기준표 항목을 선택해주세요.')
    return
  }
  
  showConfirmModal()
}
</script>

<style scoped>
.standard-create-container {
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

.selection-count {
  font-size: 14px;
  font-weight: 500;
  color: #059669;
  background: #d1fae5;
  padding: 4px 8px;
  border-radius: 12px;
  margin-left: 8px;
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

.items-container {
  background: #f8fafc;
  border-radius: 12px;
  padding: 24px;
  border: 1px solid #e2e8f0;
  max-height: 400px;
  overflow-y: auto;
  overflow-x: hidden;
}

.items-container::-webkit-scrollbar {
  width: 8px;
}

.items-container::-webkit-scrollbar-track {
  background: #f1f5f9;
  border-radius: 4px;
}

.items-container::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 4px;
}

.items-container::-webkit-scrollbar-thumb:hover {
  background: #94a3b8;
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

.item-checkbox {
  flex-shrink: 0;
}

.item-text {
  font-size: 15px;
  font-weight: 500;
  color: #374151;
  line-height: 1.5;
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
  gap: 16px;
  padding-top: 32px;
  border-top: 1px solid #e2e8f0;
}

.manage-btn {
  border-radius: 12px;
  font-weight: 600;
  height: 48px;
  min-width: 180px;
}

.submit-btn {
  background: linear-gradient(135deg, #3b82f6, #8b5cf6) !important;
  border-radius: 12px !important;
  font-weight: 700 !important;
  height: 48px !important;
  min-width: 140px !important;
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
  .standard-create-container {
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
  
  .items-container {
    padding: 20px;
    max-height: 300px;
  }
  
  .action-buttons {
    flex-direction: column;
    align-items: center;
  }
  
  .manage-btn,
  .submit-btn {
    width: 100%;
    max-width: 300px;
  }
}

@media (max-width: 480px) {
  .standard-create-container {
    padding: 24px 0;
  }
  
  .content-wrapper {
    margin: 0;
    padding: 24px 20px;
  }
  
  .page-title {
    font-size: 24px;
  }
  
  .items-container {
    padding: 16px;
    max-height: 250px;
  }
  
  .item-card {
    padding: 12px;
  }
}
</style> 