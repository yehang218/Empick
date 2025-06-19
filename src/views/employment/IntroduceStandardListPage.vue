<template>
  <v-container class="standard-list-container">
    <h2 class="page-title">자기소개서 기준표 목록</h2>

    <v-card class="mb-6 pa-4">
      <v-btn color="primary" @click="goToCreate">새 기준표 생성</v-btn>
    </v-card>

    <v-card>
      <v-list lines="two">
        <template v-for="(standard, index) in standards || []" :key="standard.id">
          <v-list-item>
            <v-list-item-title class="item-title-text">
              {{ standard.content }}
            </v-list-item-title>
            <template v-slot:append>
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
import { onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useIntroduceStandardStore } from '@/stores/introduceStandardStore'
import { deleteIntroduceStandard } from '@/services/introduceStandardService'

const router = useRouter()
const store = useIntroduceStandardStore()

onMounted(() => {
  store.fetchStandards()
})

const standards = computed(() => store.standards)

function goToCreate() {
  router.push('/employment/introduce-standard/create')
}

async function removeStandard(id) {
  if (confirm('정말로 이 기준표를 삭제하시겠습니까?')) {
    try {
      await deleteIntroduceStandard(id)
      await store.fetchStandards()
    } catch (e) {
      alert('삭제에 실패했습니다.')
    }
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