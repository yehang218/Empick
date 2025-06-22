<template>
  <v-dialog :model-value="modelValue" @update:modelValue="emit('update:modelValue', $event)" max-width="1000">
    <v-card class="question-detail-modal">
      <!-- 헤더 -->
      <v-card-title class="modal-header">
        <div class="header-content">
          <h2 class="modal-title">문제 상세 정보</h2>
          <v-btn icon @click="closeModal" class="close-btn">
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </div>
      </v-card-title>

      <v-card-text class="modal-content">
        <!-- 문제 정보 -->
        <div class="question-info-section">
          <div class="question-content-item">
            <span class="question-content-label">문제 내용</span>
            <div class="question-content-text">{{ question.content }}</div>
          </div>
          
          <div v-if="question.detailContent" class="info-item">
            <span class="info-label">상세 설명:</span>
            <span class="info-content">{{ question.detailContent }}</span>
          </div>
          
          <div class="info-row">
            <div class="info-item">
              <span class="info-label">유형:</span>
              <span class="info-tag" :style="getQuestionTypeStyle(question.type)">{{ getQuestionTypeLabel(question.type) }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">난이도:</span>
              <span class="info-tag" :style="getDifficultyStyle(question.difficulty)">{{ getDifficultyLabel(question.difficulty) }}</span>
            </div>
          </div>
          
          <div v-if="question.answer" class="info-item">
            <span class="info-label">정답:</span>
            <span class="info-content answer-content">{{ question.answer }}</span>
          </div>
        </div>

        <!-- 선택지 -->
        <div v-if="question.type === 'MULTIPLE' && question.questionOptions?.length" class="options-section">
          <h3 class="section-title">선택지</h3>
          <div class="options-list">
            <div v-for="(opt, i) in question.questionOptions" :key="i" 
                 class="option-item"
                 :class="{ 'correct-option': question.answer?.trim() === opt.content?.trim() }">
              <span class="option-number">{{ opt.optionNumber ? `${opt.optionNumber}.` : `${i + 1}.` }}</span>
              <span class="option-content">{{ opt.content }}</span>
              <v-icon v-if="question.answer?.trim() === opt.content?.trim()" 
                      color="white" 
                      size="small" 
                      class="correct-icon">
                mdi-check-circle
              </v-icon>
            </div>
          </div>
        </div>

        <!-- 채점 기준 -->
        <div v-if="question.gradingCriteria?.length" class="grading-section">
          <h3 class="section-title">채점 기준</h3>
          <v-simple-table class="grading-table">
            <thead>
              <tr>
                <th>기준 내용</th>
                <th>가중치</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(c, i) in question.gradingCriteria" :key="i">
                <td>{{ c.content }}</td>
                <td>{{ c.scoreWeight }}</td>
              </tr>
            </tbody>
          </v-simple-table>
        </div>

        <!-- 사용중인 실무테스트 -->
        <div v-if="question.usedJobTests?.length" class="used-tests-section">
          <h3 class="section-title">이 문제를 사용하는 실무 테스트</h3>
          <div class="tests-list">
            <div v-for="(test, i) in question.usedJobTests" :key="i" class="test-item">
              {{ test.title }}
            </div>
          </div>
        </div>

        <!-- 메타 정보 테이블 -->
        <div class="meta-info-section">
          <h3 class="meta-section-title">생성/수정 정보</h3>
          <v-simple-table class="meta-table">
            <thead>
              <tr>
                <th>생성자</th>
                <th>생성일</th>
                <th>최종 수정자</th>
                <th>최종 수정일</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>{{ question.createdMemberName || '-' }}</td>
                <td>{{ formatDate(question.createdAt) }}</td>
                <td>{{ question.updatedMemberName || '-' }}</td>
                <td>{{ formatDate(question.updatedAt) }}</td>
              </tr>
            </tbody>
          </v-simple-table>
        </div>
      </v-card-text>

      <!-- 액션 버튼 -->
      <v-card-actions class="modal-actions">
        <v-btn variant="text" @click="closeModal" class="cancel-btn">
          닫기
        </v-btn>
        <v-spacer />
        <v-btn v-if="props.showDelete" 
               color="error" 
               variant="outlined" 
               prepend-icon="mdi-delete" 
               @click="handleDeleteConfirm"
               class="delete-btn">
          삭제하기
        </v-btn>
        <v-btn v-if="props.showEdit" 
               color="primary" 
               variant="elevated" 
               @click="goEditPage" 
               prepend-icon="mdi-pencil"
               class="edit-btn">
          수정하기
        </v-btn>
      </v-card-actions>
    </v-card>
    
    <AlertModal v-if="showDeleteConfirm" 
                :message="'정말 이 문제를 삭제하시겠습니까? 복구할 수 없습니다.'" 
                @confirm="confirmDelete"
                @cancel="showDeleteConfirm = false" />
  </v-dialog>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { ref, defineProps, defineEmits } from 'vue'
import { useToast } from 'vue-toastification'

import { useJobtestQuestionStore } from '@/stores/jobtestQuestionStore'

import { getQuestionTypeLabel, getQuestionTypeColors } from '@/constants/employment/questionTypes.js'
import { getDifficultyLabel, getDifficultyColors } from '@/constants/employment/difficulty.js'
import AlertModal from '@/components/common/AlertModal.vue'

const jobtestQuestionStore = useJobtestQuestionStore()

const toast = useToast()
const showDeleteConfirm = ref(false)

const props = defineProps({
  modelValue: Boolean,
  question: Object,
  showDelete: {
    type: Boolean,
    default: true
  },
  showEdit: {
    type: Boolean,
    default: true
  }
})
const router = useRouter()
const emit = defineEmits(['update:modelValue'])

function closeModal() {
  emit('update:modelValue', false);
}

function handleDeleteConfirm() {
  showDeleteConfirm.value = true
}

async function confirmDelete() {
  try {
    if (!props.question?.id) return;

    await jobtestQuestionStore.deleteQuestion(props.question.id, props.question.type);

    toast.success('문제가 성공적으로 삭제되었습니다.');
    closeModal();
  } catch (err) {

  } finally {
    showDeleteConfirm.value = false;
  }
}

function goEditPage() {
  if (props.question?.id) {
    emit('update:modelValue', false);
    router.push({ name: 'JobtestQuestionEdit', params: { id: props.question.id } })
  }
}

function formatDate(dateStr) {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  const yyyy = date.getFullYear()
  const MM = String(date.getMonth() + 1).padStart(2, '0')
  const dd = String(date.getDate()).padStart(2, '0')
  const HH = String(date.getHours()).padStart(2, '0')
  const mm = String(date.getMinutes()).padStart(2, '0')
  return `${yyyy}-${MM}-${dd} ${HH}:${mm}`
}

// 난이도 및 유형 색상 정보
const getDifficultyStyle = (difficulty) => {
  const colors = getDifficultyColors(difficulty);
  return {
    backgroundColor: colors.background,
    color: colors.text
  };
};

const getQuestionTypeStyle = (type) => {
  const colors = getQuestionTypeColors(type);
  return {
    backgroundColor: colors.background,
    color: colors.text
  };
};

</script>

<style scoped>
/* 모달 전체 스타일 */
.question-detail-modal {
  border-radius: 20px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
  overflow: hidden;
}

/* 헤더 스타일 */
.modal-header {
  background: linear-gradient(135deg, #4A7C59 0%, #7EDC92 100%);
  color: white;
  padding: 24px 32px;
  border-bottom: none;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.modal-title {
  font-size: 1.5rem;
  font-weight: 700;
  margin: 0;
  color: white;
}

.close-btn {
  color: white !important;
  background: rgba(255, 255, 255, 0.1) !important;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  transition: all 0.3s ease;
}

.close-btn:hover {
  background: rgba(255, 255, 255, 0.2) !important;
  transform: scale(1.1);
}

/* 컨텐츠 영역 */
.modal-content {
  padding: 32px;
  max-height: 70vh;
  overflow-y: auto;
}

/* 메타 정보 테이블 */
.meta-info-section {
  margin-top: 40px;
  padding-top: 24px;
  border-top: 1px solid #e9ecef;
}

.meta-section-title {
  font-size: 1rem;
  font-weight: 600;
  color: #6c757d;
  margin-bottom: 12px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.meta-table {
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  background: #f8f9fa;
}

.meta-table thead th {
  background: #e9ecef;
  color: #6c757d;
  font-weight: 500;
  font-size: 0.8rem;
  padding: 12px 8px;
  border-bottom: 1px solid #dee2e6;
  text-align: center;
}

.meta-table tbody td {
  padding: 10px 8px;
  font-weight: 400;
  color: #6c757d;
  text-align: center;
  border-bottom: 1px solid #f1f3f4;
  font-size: 0.85rem;
}

.meta-table tbody tr:hover {
  background: #f1f3f4;
}

/* 문제 정보 섹션 */
.question-info-section {
  margin-bottom: 40px;
  padding: 24px;
  background: linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%);
  border-radius: 16px;
  border: 1px solid #e9ecef;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
}

.question-content-item {
  margin-bottom: 32px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 16px;
}

.question-content-label {
  font-weight: 700;
  color: #212529;
  font-size: 1.1rem;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.question-content-text {
  color: #212529;
  font-weight: 500;
  line-height: 1.8;
  font-size: 1.2rem;
  background: #f8f9fa;
  padding: 20px;
  border-radius: 12px;
  border-left: 4px solid #4A7C59;
  width: 100%;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.info-item {
  margin-bottom: 20px;
  display: flex;
  align-items: flex-start;
  gap: 12px;
}

.info-row {
  display: flex;
  gap: 32px;
  margin-bottom: 20px;
}

.info-row .info-item {
  flex: 1;
  margin-bottom: 0;
}

.info-label {
  font-weight: 600;
  color: #495057;
  min-width: 80px;
  font-size: 0.9rem;
}

.info-content {
  color: #212529;
  font-weight: 500;
  line-height: 1.6;
  flex: 1;
}

.answer-content {
  background: #e8f5e8;
  padding: 8px 12px;
  border-radius: 8px;
  border-left: 4px solid #4CAF50;
  font-weight: 600;
  color: #2e7d32;
}

/* 태그 스타일 */
.info-tag {
  display: inline-flex;
  align-items: center;
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.info-tag:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

/* 섹션 제목 */
.section-title {
  font-size: 1.25rem;
  font-weight: 700;
  color: #212529;
  margin-bottom: 16px;
  padding-bottom: 8px;
  border-bottom: 2px solid #e9ecef;
}

/* 선택지 섹션 */
.options-section {
  margin-bottom: 32px;
}

.options-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.option-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  background: #f8f9fa;
  border-radius: 12px;
  border: 2px solid transparent;
  transition: all 0.3s ease;
}

.option-item:hover {
  background: #e9ecef;
  border-color: #dee2e6;
}

.correct-option {
  background: linear-gradient(135deg, #4CAF50 0%, #45a049 100%);
  border-color: #4CAF50;
  color: white;
  box-shadow: 0 4px 12px rgba(76, 175, 80, 0.3);
}

.correct-option:hover {
  background: linear-gradient(135deg, #45a049 0%, #3d8b40 100%);
  border-color: #45a049;
  transform: translateY(-1px);
  box-shadow: 0 6px 16px rgba(76, 175, 80, 0.4);
}

.correct-option .option-number {
  color: white;
  font-weight: 700;
}

.correct-option .option-content {
  color: white;
  font-weight: 600;
}

.option-number {
  font-weight: 600;
  color: #495057;
  min-width: 30px;
  font-size: 0.9rem;
}

.option-content {
  flex: 1;
  color: #212529;
  font-weight: 500;
  line-height: 1.5;
}

.correct-icon {
  color: #4CAF50 !important;
  margin-left: auto;
}

/* 채점 기준 테이블 */
.grading-section {
  margin-bottom: 32px;
}

.grading-table {
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.grading-table thead th {
  background: #f8f9fa;
  color: #495057;
  font-weight: 600;
  font-size: 0.875rem;
  padding: 16px 12px;
  border-bottom: 2px solid #e9ecef;
}

.grading-table tbody td {
  padding: 16px 12px;
  font-weight: 500;
  color: #212529;
  border-bottom: 1px solid #f1f3f4;
}

.grading-table tbody tr:hover {
  background: #f8f9fa;
}

/* 사용중인 테스트 섹션 */
.used-tests-section {
  margin-bottom: 32px;
}

.tests-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.test-item {
  padding: 12px 16px;
  background: #e3f2fd;
  border-radius: 8px;
  color: #1976d2;
  font-weight: 500;
  border-left: 4px solid #2196f3;
}

/* 액션 버튼 */
.modal-actions {
  padding: 24px 32px;
  background: #f8f9fa;
  border-top: 1px solid #e9ecef;
  gap: 12px;
}

.cancel-btn {
  color: #6c757d !important;
  font-weight: 500;
  text-transform: none;
  padding: 8px 16px;
}

.cancel-btn:hover {
  background: #e9ecef !important;
}

.delete-btn {
  font-weight: 500;
  text-transform: none;
  padding: 8px 20px;
  border-radius: 8px;
}

.edit-btn {
  font-weight: 500;
  text-transform: none;
  padding: 8px 20px;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(74, 124, 89, 0.3);
}

.edit-btn:hover {
  box-shadow: 0 6px 16px rgba(74, 124, 89, 0.4);
}

/* 반응형 디자인 */
@media (max-width: 768px) {
  .modal-content {
    padding: 20px;
  }
  
  .modal-header {
    padding: 20px;
  }
  
  .modal-actions {
    padding: 20px;
    flex-direction: column;
  }
  
  .info-row {
    flex-direction: column;
    gap: 16px;
  }
  
  .info-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }
  
  .info-label {
    min-width: auto;
  }
  
  .info-tag {
    font-size: 0.75rem;
    padding: 4px 10px;
  }
}

/* 스크롤바 스타일링 */
.modal-content::-webkit-scrollbar {
  width: 8px;
}

.modal-content::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

.modal-content::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 4px;
}

.modal-content::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>
