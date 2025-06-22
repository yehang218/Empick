<template>
  <v-container class="criteria-create-container">
    <div class="d-flex justify-space-between align-center mb-6">
      <h2 class="page-title">자기소개서 기준표 생성</h2>
      <v-btn color="primary" @click="goToManage">기준표 항목 관리로 이동</v-btn>
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
      <v-btn color="grey" variant="outlined" class="mr-3" @click="router.back()">취소</v-btn>
      <v-btn color="info" variant="outlined" class="mr-3" @click="goToCriteriaList">기준표 목록으로 이동</v-btn>
      <v-btn color="success" @click="submit">기준표 등록</v-btn>
    </div>
  </v-container>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useIntroduceStandardItemStore } from '@/stores/introduceStandardItemStore'
import { useIntroduceStandardStore } from '@/stores/introduceStandardStore'
import { useMemberStore } from '@/stores/memberStore'

const router = useRouter()
const standardItemStore = useIntroduceStandardItemStore()
const standardStore = useIntroduceStandardStore()
const memberStore = useMemberStore()

const title = ref('')
const items = computed(() => standardItemStore.items)
const selectedItemIds = ref([])

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

const submit = async () => {
  if (!title.value.trim()) {
    alert('기준표 제목을 입력해주세요.')
    return
  }
  if (selectedItemIds.value.length === 0) {
    alert('하나 이상의 기준표 항목을 선택해주세요.')
    return
  }
  try {
    await standardStore.addStandard(title.value, selectedItemIds.value)
    alert('기준표가 성공적으로 등록되었습니다.')
    router.push('/employment/introduce-standard/list')
  } catch (error) {
    console.error('기준표 등록 실패:', error)
    alert('기준표 등록에 실패했습니다. 서버 오류일 수 있습니다.')
  }
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