<template>
  <div class="standard-item-manage-container">
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
            <div class="input-group">
              <v-text-field v-model="newCriteria" label="새 기준표 항목 내용" variant="outlined" class="form-field"
                placeholder="예: 지원동기, 성장과정, 성격의 장단점 등" prepend-inner-icon="mdi-format-list-bulleted" />
              <v-btn color="primary" variant="elevated" prepend-icon="mdi-plus" @click="showAddModal"
                :disabled="!newCriteria.trim() || addingItem" :loading="addingItem" class="add-btn">
                추가하기
              </v-btn>
            </div>
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
                  <v-btn icon size="small" color="error" variant="text" @click="showDeleteModal(item.id, item.content)"
                    class="delete-btn">
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

    <!-- Alert Modal -->
    <AlertModal v-if="showModal" :title="modalTitle" :message="modalMessage" :confirm-text="modalConfirmText"
      :cancel-text="modalCancelText" @confirm="handleConfirm" @cancel="handleCancel" />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useIntroduceStandardItemStore } from '@/stores/introduceStandardItemStore'
import { useMemberStore } from '@/stores/memberStore'
import { useToast } from 'vue-toastification'
import AlertModal from '@/components/common/AlertModal.vue'

const store = useIntroduceStandardItemStore()
const memberStore = useMemberStore()
const toast = useToast()
const newCriteria = ref('')
const router = useRouter()
const addingItem = ref(false)

// Modal 상태
const showModal = ref(false)
const modalTitle = ref('')
const modalMessage = ref('')
const modalConfirmText = ref('확인')
const modalCancelText = ref('취소')
const modalAction = ref('')
const selectedItemId = ref(null)

onMounted(async () => {
  await Promise.all([
    memberStore.getMyInfo(),
    store.fetchItems()
  ])
})

const items = computed(() => store.items)

const showAddModal = () => {
  if (!newCriteria.value.trim()) {
    toast.error('항목 내용을 입력해주세요.')
    return
  }

  modalTitle.value = '항목 추가'
  modalMessage.value = `"${newCriteria.value.trim()}" 항목을 추가하시겠습니까?`
  modalConfirmText.value = '추가'
  modalCancelText.value = '취소'
  modalAction.value = 'add'
  showModal.value = true
}

const showDeleteModal = (id, content) => {
  selectedItemId.value = id
  modalTitle.value = '항목 삭제'
  modalMessage.value = `정말로 "${content}" 항목을 삭제하시겠습니까?`
  modalConfirmText.value = '삭제'
  modalCancelText.value = '취소'
  modalAction.value = 'delete'
  showModal.value = true
}

const handleConfirm = async () => {
  showModal.value = false

  if (modalAction.value === 'add') {
    await addCriteria()
  } else if (modalAction.value === 'delete') {
    await removeItem(selectedItemId.value)
  }
}

const handleCancel = () => {
  showModal.value = false
  selectedItemId.value = null
}

const addCriteria = async () => {
  try {
    addingItem.value = true
    await store.addItem(newCriteria.value.trim())
    toast.success('기준표 항목이 성공적으로 추가되었습니다.')
    newCriteria.value = ''
  } catch (error) {
    console.error('항목 추가 실패:', error)
    toast.error('항목 추가에 실패했습니다. 다시 시도해주세요.')
  } finally {
    addingItem.value = false
  }
}

const goToCreateStandard = () => {
  router.push('/employment/introduce-standard/create')
}

const removeItem = async (id) => {
  try {
    await store.removeItem(id)
    toast.success('기준표 항목이 삭제되었습니다.')
  } catch (error) {
    console.error('항목 삭제 실패:', error)
    console.log('에러 상세:', {
      status: error.response?.status,
      data: error.response?.data,
      message: error.message
    })

    // 500 에러이거나 FK 제약 조건 관련 에러인 경우
    if (error.response?.status === 500 || error.response?.status === 503) {
      toast.error('이미 사용 중인 항목은 삭제할 수 없습니다.\n\n기준표나 템플릿에서 사용 중인 항목입니다.')
    } else {
      toast.error('항목 삭제에 실패했습니다. 다시 시도해주세요.')
    }
  } finally {
    selectedItemId.value = null
  }
}
</script>

<style scoped>
.standard-item-manage-container {
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

.input-group {
  display: flex;
  align-items: center;
  gap: 16px;
}

.form-field {
  background: white;
  border-radius: 8px;
  flex: 1;
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

.add-btn {
  border-radius: 12px;
  font-weight: 600;
  height: 48px;
  min-width: 120px;
  flex-shrink: 0;
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
  .standard-item-manage-container {
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

  .input-group {
    flex-direction: column;
    align-items: stretch;
    gap: 12px;
  }

  .add-btn {
    width: 100%;
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
  .standard-item-manage-container {
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