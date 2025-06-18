<template>
  <v-container class="template-list-container">
    <div class="d-flex justify-space-between align-center mb-6">
      <h2 class="page-title">자기소개서 템플릿 목록</h2>
      <v-btn color="primary" @click="goCreate">템플릿 작성</v-btn>
    </div>

    <v-card>
      <v-list lines="two">
        <template v-for="(template, index) in templates" :key="template.id">
          <v-list-item
            @click="goDetail(template.id)"
            style="cursor:pointer"
          >
            <v-list-item-title class="template-title-text">{{ template.title }}</v-list-item-title>
            <template v-slot:append>
              <v-btn icon @click.stop="removeTemplate(template.id)" size="small" color="red-darken-2" variant="text">
                <v-icon>mdi-delete</v-icon>
              </v-btn>
            </template>
          </v-list-item>
          <v-divider v-if="index < templates.length - 1" inset></v-divider>
        </template>
        <v-list-item v-if="templates.length === 0">
          <v-list-item-title>등록된 자기소개서 템플릿이 없습니다.</v-list-item-title>
        </v-list-item>
      </v-list>
    </v-card>
  </v-container>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useIntroduceTemplateStore } from '@/stores/introduceTemplateStore'
import { useRouter } from 'vue-router'

const router = useRouter()
const introduceTemplateStore = useIntroduceTemplateStore()
const templates = introduceTemplateStore.templates

onMounted(async () => {
  try {
    await introduceTemplateStore.loadTemplates()
    // console.log('Fetched templates:', introduceTemplateStore.templates)
  } catch (error) {
    console.error('템플릿 목록 로드 실패:', error)
    alert('템플릿 목록을 불러오는 데 실패했습니다.')
  }
})

const goCreate = () => router.push('/employment/recruitments/introduce-templates/create')
const goDetail = (id) => router.push(`/employment/recruitments/introduce-templates/${id}`)

const removeTemplate = async (id) => {
  if (confirm('정말로 이 템플릿을 삭제하시겠습니까? 관련 항목도 함께 삭제됩니다.')) {
    try {
      await introduceTemplateStore.removeTemplate(id)
      alert('템플릿이 성공적으로 삭제되었습니다.')
      // await introduceTemplateStore.loadTemplates() // store에서 이미 반영됨
    } catch (error) {
      console.error('템플릿 삭제 실패:', error)
      alert('템플릿 삭제에 실패했습니다. 서버 오류일 수 있습니다.')
    }
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
}

.template-title-text {
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