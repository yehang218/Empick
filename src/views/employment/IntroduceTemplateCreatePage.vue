<template>
  <v-container class="template-create-container">
    <div class="d-flex justify-space-between align-center mb-6">
      <h2 class="page-title">자기소개서 템플릿 작성</h2>
      <v-btn color="primary" @click="goToManage">항목 관리로 이동</v-btn>
    </div>

    <v-card class="mb-6 pa-4">
      <v-card-title class="card-title">템플릿 제목</v-card-title>
      <v-card-text>
        <v-text-field
          v-model="title"
          label="템플릿 제목을 입력하세요"
          variant="outlined"
          density="compact"
          hide-details
        />
      </v-card-text>
    </v-card>

    <v-card class="mb-6">
      <v-card-title class="card-title">항목 선택</v-card-title>
      <v-card-text class="pa-0">
        <v-list lines="two">
          <template v-for="(item, index) in items" :key="item.id || index">
            <v-list-item>
              <template v-slot:prepend>
                <v-checkbox v-model="selectedItemIds" :value="item.id" hide-details density="compact" />
              </template>
              <v-list-item-title class="item-title-text">{{ item.title }}</v-list-item-title>
            </v-list-item>
            <v-divider v-if="index < items.length - 1" inset></v-divider>
          </template>
          <v-list-item v-if="items.length === 0">
            <v-list-item-title>등록된 자기소개서 항목이 없습니다.</v-list-item-title>
          </v-list-item>
        </v-list>
      </v-card-text>
    </v-card>

    <div class="d-flex justify-end mt-6">
      <v-btn color="grey" variant="outlined" class="mr-3" @click="router.back()">취소</v-btn>
      <v-btn color="info" variant="outlined" class="mr-3" @click="goToTemplateList">템플릿 목록으로 이동</v-btn>
      <v-btn color="success" @click="submit">템플릿 등록</v-btn>
    </div>
  </v-container>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useIntroduceTemplateStore } from '@/stores/introduceTemplateStore'
import { useIntroduceItemStore } from '@/stores/introduceItemStore'

const router = useRouter()
const introduceTemplateStore = useIntroduceTemplateStore()
const introduceItemStore = useIntroduceItemStore()

const title = ref('')
const items = computed(() => introduceItemStore.items)
const selectedItemIds = ref([])

function goToManage() {
  router.push('/employment/introduce-items/manage')
}

function goToTemplateList() {
  router.push('/employment/introduce-templates')
}

onMounted(async () => {
  try {
    await introduceItemStore.loadItems()
    // console.log('Fetched items for create page:', items.value) // 디버깅용
  } catch (error) {
    console.error('항목 로드 실패:', error)
    alert('자기소개서 항목을 불러오는 데 실패했습니다.')
  }
})

const submit = async () => {
  if (!title.value.trim()) {
    alert('템플릿 제목을 입력해주세요.')
    return
  }
  if (selectedItemIds.value.length === 0) {
    alert('하나 이상의 항목을 선택해주세요.')
    return
  }
  try {
    await introduceTemplateStore.addTemplate(title.value, 1, selectedItemIds.value)
    alert('템플릿이 성공적으로 등록되었습니다.')
    router.push('/employment/introduce-templates')
  } catch (error) {
    console.error('템플릿 등록 실패:', error)
    alert('템플릿 등록에 실패했습니다. 서버 오류일 수 있습니다.')
  }
}
</script>

<style scoped>
.template-create-container {
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