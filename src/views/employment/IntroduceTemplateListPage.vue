<template>
  <div class="template-list-container">
    <div class="content-wrapper">
      <div class="page-header">
        <h1 class="page-title">자기소개서 템플릿 목록</h1>
        <p class="page-subtitle">등록된 템플릿들을 관리합니다</p>
      </div>

      <div class="action-section">
        <v-btn color="primary" @click="goCreate" size="large" class="create-btn">
          <v-icon left>mdi-plus</v-icon>
          새 템플릿 생성
        </v-btn>
      </div>

      <div class="list-section">
        <div v-if="templates && templates.length > 0" class="templates-list">
          <div v-for="(template, index) in templates" :key="template.id" class="template-card">
            <div class="template-content">
              <div class="template-info">
                <div class="template-number">{{ index + 1 }}</div>
                <div class="template-title">{{ template.title }}</div>
              </div>
              <div class="template-actions">
                <v-btn 
                  size="small" 
                  color="primary" 
                  variant="outlined" 
                  @click="goDetail(template.id)"
                  class="detail-btn"
                >
                  <v-icon left>mdi-eye</v-icon>
                  상세보기
                </v-btn>
                <v-btn 
                  icon 
                  size="small" 
                  color="error" 
                  variant="text" 
                  @click="showDeleteModal(template.id, template.title)"
                  class="delete-btn"
                >
                  <v-icon>mdi-delete</v-icon>
                </v-btn>
              </div>
            </div>
          </div>
        </div>
        <div v-else class="empty-state">
          <v-icon size="large" color="grey">mdi-file-document</v-icon>
          <p>등록된 템플릿이 없습니다.</p>
          <v-btn color="primary" @click="goCreate" size="large" class="create-first-btn">
            <v-icon left>mdi-plus</v-icon>
            첫 번째 템플릿 생성하기
          </v-btn>
        </div>
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
import { useIntroduceTemplateStore } from '@/stores/introduceTemplateStore'
import { useRouter } from 'vue-router'
import { useToast } from 'vue-toastification'
import AlertModal from '@/components/common/AlertModal.vue'

const router = useRouter()
const toast = useToast()
const introduceTemplateStore = useIntroduceTemplateStore()
const templates = computed(() => introduceTemplateStore.templates)

// Modal 상태
const showModal = ref(false)
const modalTitle = ref('')
const modalMessage = ref('')
const modalConfirmText = ref('확인')
const modalCancelText = ref('취소')
const selectedTemplateId = ref(null)

onMounted(async () => {
  try {
    await introduceTemplateStore.loadTemplates()
  } catch (error) {
    console.error('템플릿 목록 로드 실패:', error)
    toast.error('템플릿 목록을 불러오는 데 실패했습니다.')
  }
})

const goCreate = () => router.push('/employment/introduce-templates/create')
const goDetail = (id) => router.push(`/employment/introduce-templates/${id}`)

const showDeleteModal = (id, title) => {
  selectedTemplateId.value = id
  modalTitle.value = '템플릿 삭제'
  modalMessage.value = `정말로 "${title}" 템플릿을 삭제하시겠습니까? 관련 항목도 함께 삭제됩니다.`
  modalConfirmText.value = '삭제'
  modalCancelText.value = '취소'
  showModal.value = true
}

const handleConfirm = async () => {
  showModal.value = false
  await removeTemplate(selectedTemplateId.value)
}

const handleCancel = () => {
  showModal.value = false
  selectedTemplateId.value = null
}

const removeTemplate = async (id) => {
  try {
    await introduceTemplateStore.removeTemplate(id)
    toast.success('템플릿이 성공적으로 삭제되었습니다.')
  } catch (error) {
    console.error('템플릿 삭제 실패:', error)
    console.log('에러 상세:', {
      status: error.response?.status,
      data: error.response?.data,
      message: error.message
    })
    
    // 500 에러이거나 FK 제약 조건 관련 에러인 경우
    if (error.response?.status === 500 || error.response?.status === 503) {
      toast.error('이미 사용 중인 템플릿은 삭제할 수 없습니다.\n\n연관된 항목들이 있는 템플릿입니다.')
    } else {
      toast.error('템플릿 삭제에 실패했습니다. 서버 오류일 수 있습니다.')
    }
  } finally {
    selectedTemplateId.value = null
  }
}
</script>

<style scoped>
.template-list-container {
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

.action-section {
  display: flex;
  justify-content: center;
  margin-bottom: 40px;
  padding: 24px;
  background: #f8fafc;
  border-radius: 12px;
  border: 1px solid #e2e8f0;
}

.create-btn {
  background: linear-gradient(135deg, #3b82f6, #8b5cf6) !important;
  border-radius: 12px !important;
  font-weight: 700 !important;
  height: 48px !important;
  min-width: 160px !important;
  text-transform: none !important;
  box-shadow: 0 4px 14px 0 rgba(59, 130, 246, 0.4) !important;
  transition: all 0.3s ease !important;
}

.create-btn:hover {
  transform: translateY(-2px) !important;
  box-shadow: 0 8px 25px 0 rgba(59, 130, 246, 0.5) !important;
}

.list-section {
  background: #f8fafc;
  border-radius: 12px;
  padding: 24px;
  border: 1px solid #e2e8f0;
}

.templates-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.template-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  border: 1px solid #e2e8f0;
  transition: all 0.3s ease;
}

.template-card:hover {
  border-color: #3b82f6;
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.1);
  transform: translateY(-2px);
}

.template-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
}

.template-info {
  display: flex;
  align-items: center;
  gap: 16px;
  flex: 1;
}

.template-number {
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

.template-title {
  font-size: 16px;
  font-weight: 600;
  color: #1e293b;
  line-height: 1.5;
}

.template-actions {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-shrink: 0;
}

.detail-btn {
  border-radius: 8px;
  font-weight: 600;
  height: 36px;
}

.delete-btn {
  border-radius: 8px;
}

.empty-state {
  text-align: center;
  padding: 60px 24px;
  color: #64748b;
}

.empty-state p {
  margin: 16px 0 24px 0;
  font-size: 16px;
  font-weight: 500;
}

.create-first-btn {
  background: linear-gradient(135deg, #3b82f6, #8b5cf6) !important;
  border-radius: 12px !important;
  font-weight: 700 !important;
  height: 48px !important;
  min-width: 200px !important;
  text-transform: none !important;
  box-shadow: 0 4px 14px 0 rgba(59, 130, 246, 0.4) !important;
  transition: all 0.3s ease !important;
}

.create-first-btn:hover {
  transform: translateY(-2px) !important;
  box-shadow: 0 8px 25px 0 rgba(59, 130, 246, 0.5) !important;
}

/* 반응형 디자인 */
@media (max-width: 768px) {
  .template-list-container {
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
  
  .action-section {
    padding: 20px;
  }
  
  .list-section {
    padding: 20px;
  }
  
  .template-content {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
  
  .template-actions {
    width: 100%;
    justify-content: flex-end;
  }
}

@media (max-width: 480px) {
  .template-list-container {
    padding: 24px 0;
  }
  
  .content-wrapper {
    margin: 0;
    padding: 24px 20px;
  }
  
  .page-title {
    font-size: 24px;
  }
  
  .action-section,
  .list-section {
    padding: 16px;
  }
  
  .template-card {
    padding: 16px;
  }
  
  .empty-state {
    padding: 40px 16px;
  }
}
</style> 