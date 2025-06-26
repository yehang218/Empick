<template>
  <v-container class="criteria-create-container">
    <div class="d-flex justify-space-between align-center mb-6">
      <h2 class="page-title">자기소개서 기준표 생성</h2>
      <v-btn color="success" @click="submit">기준표 등록</v-btn>
    </div>

    <v-card class="mb-6 pa-4">
      <v-card-title class="card-title">기준표 제목</v-card-title>
      <v-card-text>
        <v-text-field
          v-model="title"
          label="기준표 제목을 입력하세요"
          variant="outlined"
          density="compact"
          hide-details
        />
      </v-card-text>
    </v-card>

    <v-card class="mb-6">
      <v-card-title class="card-title">기준표 항목 선택</v-card-title>
      <v-card-text class="pa-0">
        <v-list lines="two">
          <template v-for="(item, index) in items" :key="item.id || index">
            <v-list-item>
              <template v-slot:prepend>
                <v-checkbox v-model="selectedItemIds" :value="item.id" hide-details density="compact" />
              </template>
              <v-list-item-title class="item-title-text">{{ item.content }}</v-list-item-title>
            </v-list-item>
            <v-divider v-if="index < items.length - 1" inset></v-divider>
          </template>
          <v-list-item v-if="items.length === 0">
            <v-list-item-title>등록된 기준표 항목이 없습니다.</v-list-item-title>
          </v-list-item>
        </v-list>
      </v-card-text>
    </v-card>

    <div class="d-flex justify-end mt-6">
      <v-btn color="primary" class="mr-3" @click="goToManage">기준표 항목 관리로 이동</v-btn>
      <v-btn color="info" variant="outlined" @click="goToCriteriaList">기준표 목록으로 이동</v-btn>
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
  </v-container>
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
.criteria-create-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 24px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.page-title {
  font-size: 1.8rem;
  font-weight: bold;
  color: #333;
}

.card-title {
  font-size: 1.3rem;
  font-weight: bold;
  color: #444;
  padding-bottom: 0;
}

.item-title-text {
  font-size: 1rem;
  font-weight: 500;
  color: #555;
}

.v-list-item {
  padding: 8px 16px;
}

.v-card {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}
</style> 