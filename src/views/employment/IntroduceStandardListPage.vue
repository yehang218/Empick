<template>
  <v-container class="standard-list-container">
    <h2 class="page-title">자기소개서 기준표 목록</h2>

    <v-card class="mb-6 pa-4">
      <v-btn color="primary" @click="goToCreate">새 기준표 생성</v-btn>
    </v-card>

    <v-card>
      <v-list lines="two">
        <template v-for="(standard, index) in standards" :key="standard.id">
          <v-list-item>
            <v-list-item-title class="item-title-text">
              {{ standard.title }}
              <span class="text-caption text-grey"> (항목 {{ standard.items.length }}개)</span>
            </v-list-item-title>
            <v-list-item-subtitle class="text-caption">{{ standard.description }}</v-list-item-subtitle>
            <template v-slot:append>
              <v-btn icon size="small" color="info" variant="text" @click="viewDetail(standard.id)">
                <v-icon>mdi-eye</v-icon>
              </v-btn>
              <v-btn icon size="small" color="red-darken-2" variant="text" @click="removeStandard(standard.id)">
                <v-icon>mdi-delete</v-icon>
              </v-btn>
            </template>
          </v-list-item>
          <v-divider v-if="index < standards.length - 1" inset></v-divider>
        </template>
        <v-list-item v-if="standards.length === 0">
          <v-list-item-title>등록된 기준표가 없습니다.</v-list-item-title>
        </v-list-item>
      </v-list>
    </v-card>
  </v-container>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const standards = ref([
  {
    id: 1,
    title: '기본 자기소개서 기준표',
    description: '신입 지원자용 기본 기준표',
    items: [
      { id: 1, title: '지원동기' },
      { id: 2, title: '성장과정' }
    ]
  },
  {
    id: 2,
    title: '경력직 기준표',
    description: '경력직 지원자 평가용',
    items: [
      { id: 1, title: '지원동기' },
      { id: 3, title: '장단점' },
      { id: 4, title: '입사 후 포부' }
    ]
  }
])

function goToCreate() {
  router.push('/employment/introduce-standard/create')
}

function viewDetail(id) {
  // 상세 페이지로 이동 (추후 구현)
  alert('상세보기: ' + id)
}

function removeStandard(id) {
  if (confirm('정말로 이 기준표를 삭제하시겠습니까?')) {
    standards.value = standards.value.filter(s => s.id !== id)
  }
}
</script>

<style scoped>
.standard-list-container {
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