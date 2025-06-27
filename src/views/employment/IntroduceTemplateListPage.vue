<template>
  <v-container class="template-list-container">
    <h2 class="page-title">자기소개서 템플릿 목록</h2>

    <v-card class="mb-6 pa-4">
      <v-btn color="primary" @click="goCreate">새 템플릿 생성</v-btn>
    </v-card>

    <v-card>
      <v-list lines="two">
        <template v-for="(template, index) in templates" :key="template.id">
          <v-list-item>
            <v-list-item-title class="item-title-text">
              {{ template.title }}
            </v-list-item-title>
            <template v-slot:append>
              <v-btn icon size="small" color="red-darken-2" variant="text" @click="showDeleteModal(template.id, template.title)">
                <v-icon>mdi-delete</v-icon>
              </v-btn>
              <v-btn size="small" color="primary" variant="text" @click="goDetail(template.id)">
                상세보기
              </v-btn>
            </template>
          </v-list-item>
          <v-divider v-if="index < templates.length - 1" inset></v-divider>
        </template>
        <v-list-item v-if="templates.length === 0">
          <v-list-item-title>등록된 템플릿이 없습니다.</v-list-item-title>
        </v-list-item>
      </v-list>
    </v-card>

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
  </v-container>
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
  max-width: 800px;
  margin: 0 auto;
  padding: 24px;
}

.page-title {
  font-size: 1.8rem;
  font-weight: bold;
  color: #333;
  margin-bottom: 24px;
  text-align: center;
}

.item-title-text {
  font-size: 1.1rem;
  font-weight: 500;
  color: #555;
}

.v-list-item {
  padding: 12px 16px;
}

.v-card {
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}
</style> 