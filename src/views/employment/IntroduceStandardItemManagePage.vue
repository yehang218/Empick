<template>
    <v-container class="criteria-manage-container">
      <h2 class="page-title">자기소개서 기준표 항목 관리</h2>
  
      <v-card class="mb-6 pa-4">
        <v-text-field
          v-model="newCriteria"
          label="새 기준표 항목 내용"
          variant="outlined"
          density="compact"
          hide-details
          class="mb-3"
          @keyup.enter="addCriteria"
        />
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