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
        <template v-for="(item, index) in criteriaList" :key="item.id">
          <v-list-item>
            <v-list-item-title class="item-title-text">{{ item.title }}</v-list-item-title>
            <template v-slot:append>
              <v-btn icon @click="removeCriteria(item.id)" size="small" color="red-darken-2" variant="text">
                <v-icon>mdi-close</v-icon>
              </v-btn>
            </template>
          </v-list-item>
          <v-divider v-if="index < criteriaList.length - 1" inset></v-divider>
        </template>
        <v-list-item v-if="criteriaList.length === 0">
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
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
// 실제로는 기준표 항목용 store를 만들어야 함. 임시로 itemStore 구조 복사
// import { useIntroduceCriteriaStore } from '@/stores/introduceCriteriaStore'

// 임시 데이터 및 함수
const criteriaList = ref([])
const newCriteria = ref('')
const router = useRouter()

const loadCriteria = async () => {
  // 실제로는 store에서 불러와야 함
  // await introduceCriteriaStore.loadCriteria()
  // criteriaList.value = introduceCriteriaStore.items
}
onMounted(loadCriteria)

const addCriteria = async () => {
  if (!newCriteria.value.trim()) return
  // 실제로는 store에 추가
  // await introduceCriteriaStore.addCriteria(newCriteria.value, 1)
  criteriaList.value.push({ id: Date.now(), title: newCriteria.value })
  newCriteria.value = ''
}

const goToCreateStandard = () => {
  router.push('/employment/introduce-standard/create')
}

const removeCriteria = async (id) => {
  // 실제로는 store에서 삭제
  criteriaList.value = criteriaList.value.filter(item => item.id !== id)
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