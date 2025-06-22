<template>
  <v-container class="template-item-manage-container">
    <h2 class="page-title">자기소개서 템플릿 항목 관리</h2>

    <v-card class="mb-6 pa-4">
      <v-text-field
        v-model="newContent"
        label="새 항목 내용"
        variant="outlined"
        density="compact"
        hide-details
        class="mb-3"
      />
      <v-btn color="primary" @click="addItem" block>항목 추가</v-btn>
    </v-card>

    <v-card>
      <v-list lines="two">
        <template v-for="(item, index) in items" :key="item.id">
          <v-list-item>
            <v-list-item-title class="item-title-text">{{ item.title }}</v-list-item-title>
            <template v-slot:append>
              <v-btn icon @click="removeItem(item.id)" size="small" color="red-darken-2" variant="text">
                <v-icon>mdi-delete</v-icon>
              </v-btn>
            </template>
          </v-list-item>
          <v-divider v-if="index < items.length - 1" inset></v-divider>
        </template>
        <v-list-item v-if="items.length === 0">
          <v-list-item-title>등록된 자기소개서 항목이 없습니다.</v-list-item-title>
        </v-list-item>
      </v-list>
    </v-card>

    <div class="d-flex justify-end mt-6">
      <v-btn color="secondary" @click="goToCreateTemplate">템플릿 생성 페이지로 이동</v-btn>
    </div>
  </v-container>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useIntroduceItemStore } from '@/stores/introduceItemStore'

const introduceItemStore = useIntroduceItemStore()

const items = computed(() => introduceItemStore.items)
const newContent = ref('')
const router = useRouter()

const loadItems = async () => {
  try {
    await introduceItemStore.loadItems()
    console.log('Fetched items:', items.value)
  } catch (error) {
    console.error('항목 로드 실패:', error)
  }
}
onMounted(loadItems)

const addItem = async () => {
  if (!newContent.value.trim()) return
  try {
    await introduceItemStore.addItem({
      title: newContent.value,
      memberId: 1, // 실제 로그인 유저 ID로 교체 필요
      introduceTemplateId: null // 필요시 실제 템플릿 ID로 교체
    })
    newContent.value = ''
    await loadItems()
  } catch (error) {
    console.error('항목 추가 실패:', error)
    alert('항목 추가에 실패했습니다. 이미 존재하는 항목이거나 서버 오류일 수 있습니다.')
  }
}

const goToCreateTemplate = () => {
  router.push('/employment/introduce-templates/create')
}

const removeItem = async (id) => {
  if (confirm('정말로 이 항목을 삭제하시겠습니까?')) {
    try {
      await introduceItemStore.removeItem(id)
      await loadItems()
    } catch (error) {
      console.error('항목 삭제 실패:', error)
      alert('항목 삭제에 실패했습니다. 서버 오류일 수 있습니다.')
    }
  }
}
</script>

<style scoped>
.template-item-manage-container {
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