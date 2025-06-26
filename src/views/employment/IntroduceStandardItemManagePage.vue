<template>
    <v-container class="criteria-manage-container">
      <h2 class="page-title">자기소개서 기준표 항목 관리</h2>
  
      <v-card class="mb-6 pa-4">
        <div class="d-flex align-center" style="gap: 24px;">
          <v-text-field
            v-model="newCriteria"
            label="새 기준표 항목 내용"
            variant="outlined"
            density="compact"
            hide-details
            class="flex-grow-1"
            placeholder="예: 지원동기, 성장과정, 성격의 장단점 등"
          />
          <v-btn 
            color="primary" 
            variant="elevated"
            prepend-icon="mdi-plus"
            @click="addCriteria"
            :disabled="!newCriteria.trim() || addingItem"
            :loading="addingItem"
            class="px-6"
          >
            추가하기
          </v-btn>
        </div>
      </v-card>
  
      <v-card>
        <v-list lines="two">
          <template v-for="(item, index) in items || []" :key="item.id">
            <v-list-item>
              <v-list-item-title class="item-title-text">{{ item.content }}</v-list-item-title>
              <template v-slot:append>
                <v-btn icon size="small" color="red-darken-2" variant="text" @click="removeItem(item.id)">
                  <v-icon>mdi-delete</v-icon>
                </v-btn>
              </template>
            </v-list-item>
            <v-divider v-if="index < items.length - 1" inset></v-divider>
          </template>
          <v-list-item v-if="items.length === 0">
            <v-list-item-title>등록된 기준표 항목이 없습니다.</v-list-item-title>
          </v-list-item>
        </v-list>
      </v-card>
  
      <div class="d-flex justify-end mt-6">
        <v-btn color="secondary" @click="goToCreateStandard">기준표 생성 페이지로 이동</v-btn>
      </div>
    </v-container>
  </template>
  
  <script setup>
  import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useIntroduceStandardItemStore } from '@/stores/introduceStandardItemStore'
import { useMemberStore } from '@/stores/memberStore'
import { useToast } from 'vue-toastification'
  
  const store = useIntroduceStandardItemStore()
const memberStore = useMemberStore()
const toast = useToast()
const newCriteria = ref('')
const router = useRouter()
const addingItem = ref(false)
  
  onMounted(async () => {
    await Promise.all([
      memberStore.getMyInfo(),
      store.fetchItems()
    ])
  })
  
  const items = computed(() => store.items)
  
  const addCriteria = async () => {
  if (!newCriteria.value.trim()) {
    toast.warning('항목 내용을 입력해주세요.')
    return
  }
  
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
  if (confirm('정말로 이 항목을 삭제하시겠습니까?')) {
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
    }
  }
}
  </script>
  
  <style scoped>
  .criteria-manage-container {
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