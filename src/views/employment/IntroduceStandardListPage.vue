<template>
  <div class="standard-list-container">
    <div class="content-wrapper">
      <div class="page-header">
        <h1 class="page-title">자기소개서 기준표 목록</h1>
        <p class="page-subtitle">등록된 기준표들을 관리합니다</p>
      </div>

      <div class="action-section">
        <v-btn color="primary" @click="goToCreate" size="large" class="create-btn">
          <v-icon left>mdi-plus</v-icon>
          새 기준표 생성
        </v-btn>
      </div>

      <div class="list-section">
        <div v-if="standards && standards.length > 0" class="standards-list">
          <div v-for="(standard, index) in standards" :key="standard.id" class="standard-card">
            <div class="standard-content">
              <div class="standard-info">
                <div class="standard-number">{{ index + 1 }}</div>
                <div class="standard-title">{{ standard.content }}</div>
              </div>
              <div class="standard-actions">
                <v-btn size="small" color="primary" variant="outlined" @click="goToDetail(standard.id)"
                  class="detail-btn">
                  <v-icon left>mdi-eye</v-icon>
                  상세보기
                </v-btn>
                <v-btn icon size="small" color="error" variant="text" @click="removeStandard(standard.id)"
                  class="delete-btn">
                  <v-icon>mdi-delete</v-icon>
                </v-btn>
              </div>
            </div>
          </div>
        </div>
        <div v-else class="empty-state">
          <v-icon size="large" color="grey">mdi-format-list-bulleted</v-icon>
          <p>등록된 기준표가 없습니다.</p>
          <v-btn color="primary" @click="goToCreate" size="large" class="create-first-btn">
            <v-icon left>mdi-plus</v-icon>
            첫 번째 기준표 생성하기
          </v-btn>
        </div>
      </div>
    </div>

    <!-- 삭제 확인 모달 -->
    <AlertModal v-if="deleteDialog" :message="`정말로 이 기준표를 삭제하시겠습니까?\n\n${selectedStandardContent}`" confirm-text="삭제"
      cancel-text="취소" @confirm="confirmDelete" @cancel="deleteDialog = false" />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useIntroduceStandardStore } from '@/stores/introduceStandardStore'
import { useToast } from 'vue-toastification'
import AlertModal from '@/components/common/AlertModal.vue'


const router = useRouter()
const store = useIntroduceStandardStore()
const toast = useToast()

const selectedStandardId = ref(null)
const deleteDialog = ref(false)
const selectedStandardContent = ref('')

onMounted(() => {
  store.fetchStandards()
})

const standards = computed(() => store.standards)

function goToCreate() {
  router.push('/employment/introduce-standard/create')
}

function removeStandard(id) {
  // 삭제할 기준표 정보 설정
  selectedStandardId.value = id
  const standard = standards.value.find(s => s.id === id)
  selectedStandardContent.value = standard?.content || '기준표'

  // 삭제 확인 다이얼로그 열기
  deleteDialog.value = true
}

async function confirmDelete() {
  if (!selectedStandardId.value) return

  // 다이얼로그 닫기
  deleteDialog.value = false

  try {
    await store.deleteStandard(selectedStandardId.value)
    toast.success('기준표가 성공적으로 삭제되었습니다.')
  } catch (e) {
    console.error('기준표 삭제 실패:', e)
    console.log('에러 상세:', {
      status: e.response?.status,
      data: e.response?.data,
      message: e.message
    })

    // 500 에러이거나 FK 제약 조건 관련 에러인 경우
    if (e.response?.status === 500 || e.response?.status === 503) {
      toast.error('이미 사용 중인 기준표는 삭제할 수 없습니다. 연관된 항목들이 있는 기준표입니다.')
    } else {
      toast.error('삭제에 실패했습니다.')
    }
  } finally {
    // 상태 초기화
    selectedStandardId.value = null
    selectedStandardContent.value = ''
  }
}

function goToDetail(id) {
  router.push(`/employment/introduce-standard/${id}`)
}
</script>

<style scoped>
.standard-list-container {
  max-width: none;
  margin: 0;
  padding: 40px 0;
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  min-height: 100vh;
}

.content-wrapper {
  background: white;
  border-radius: 0;
  padding: 40px 60px;
  box-shadow: none;
  margin: 0;
  max-width: none;
}

.page-header {
  text-align: center;
  margin-bottom: 40px;
  padding-bottom: 32px;
  border-bottom: 2px solid #f1f5f9;
}

.page-title {
  font-size: 32px;
  font-weight: 800;
  color: #1e293b;
  margin-bottom: 16px;
  letter-spacing: -0.025em;
}

.page-subtitle {
  font-size: 16px;
  color: #64748b;
  line-height: 1.6;
  max-width: 500px;
  margin: 0 auto;
}

.action-section {
  display: flex;
  justify-content: center;
  margin-bottom: 40px;
  padding: 24px;
  background: #f8fafc;
  border-radius: 12px;
  border: 1px solid #e2e8f0;
}

.create-btn {
  background: linear-gradient(135deg, #3b82f6, #8b5cf6) !important;
  border-radius: 12px !important;
  font-weight: 700 !important;
  height: 48px !important;
  min-width: 160px !important;
  text-transform: none !important;
  box-shadow: 0 4px 14px 0 rgba(59, 130, 246, 0.4) !important;
  transition: all 0.3s ease !important;
}

.create-btn:hover {
  transform: translateY(-2px) !important;
  box-shadow: 0 8px 25px 0 rgba(59, 130, 246, 0.5) !important;
}

.list-section {
  background: #f8fafc;
  border-radius: 12px;
  padding: 24px;
  border: 1px solid #e2e8f0;
}

.standards-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.standard-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  border: 1px solid #e2e8f0;
  transition: all 0.3s ease;
}

.standard-card:hover {
  border-color: #3b82f6;
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.1);
  transform: translateY(-2px);
}

.standard-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
}

.standard-info {
  display: flex;
  align-items: center;
  gap: 16px;
  flex: 1;
}

.standard-number {
  width: 32px;
  height: 32px;
  background: linear-gradient(135deg, #3b82f6, #8b5cf6);
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 14px;
  flex-shrink: 0;
}

.standard-title {
  font-size: 16px;
  font-weight: 600;
  color: #1e293b;
  line-height: 1.5;
}

.standard-actions {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-shrink: 0;
}

.detail-btn {
  border-radius: 8px;
  font-weight: 600;
  height: 36px;
}

.delete-btn {
  border-radius: 8px;
}

.empty-state {
  text-align: center;
  padding: 60px 24px;
  color: #64748b;
}

.empty-state p {
  margin: 16px 0 24px 0;
  font-size: 16px;
  font-weight: 500;
}

.create-first-btn {
  background: linear-gradient(135deg, #3b82f6, #8b5cf6) !important;
  border-radius: 12px !important;
  font-weight: 700 !important;
  height: 48px !important;
  min-width: 200px !important;
  text-transform: none !important;
  box-shadow: 0 4px 14px 0 rgba(59, 130, 246, 0.4) !important;
  transition: all 0.3s ease !important;
}

.create-first-btn:hover {
  transform: translateY(-2px) !important;
  box-shadow: 0 8px 25px 0 rgba(59, 130, 246, 0.5) !important;
}

/* 반응형 디자인 */
@media (max-width: 768px) {
  .standard-list-container {
    padding: 32px 0;
  }

  .content-wrapper {
    margin: 0;
    padding: 32px 40px;
  }

  .page-title {
    font-size: 28px;
  }

  .page-subtitle {
    font-size: 15px;
  }

  .action-section {
    padding: 20px;
  }

  .list-section {
    padding: 20px;
  }

  .standard-content {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }

  .standard-actions {
    width: 100%;
    justify-content: flex-end;
  }
}

@media (max-width: 480px) {
  .standard-list-container {
    padding: 24px 0;
  }

  .content-wrapper {
    margin: 0;
    padding: 24px 20px;
  }

  .page-title {
    font-size: 24px;
  }

  .action-section,
  .list-section {
    padding: 16px;
  }

  .standard-card {
    padding: 16px;
  }

  .empty-state {
    padding: 40px 16px;
  }
}
</style>