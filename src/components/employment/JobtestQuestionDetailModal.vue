<template>
  <v-dialog :model-value="modelValue" @update:modelValue="emit('update:modelValue', $event)" max-width="1200">
    <v-card class="mb-4">
      <v-card-text>
        <v-row class="mt-4 px-4">
          <h2 class="text-h6 font-weight-bold mb-4">문제 상세 정보</h2>
          <v-spacer />
          <v-simple-table class="info-table" den>
            <thead>
              <tr>
                <th class="text-left">생성자</th>
                <th class="text-left">생성일</th>
                <th class="text-left">최종 수정자</th>
                <th class="text-left">최종 수정일</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>{{ question.createdMemberName }}</td>
                <td>{{ formatDate(question.createdAt) }}</td>
                <td>{{ question.updatedMemberName }}</td>
                <td>{{ formatDate(question.updatedAt) }}</td>
              </tr>
            </tbody>
          </v-simple-table>
        </v-row>
        <div><strong>문제 내용:</strong> {{ question.content }}</div>
        <div v-if="question.detailContent"><strong>상세 설명:</strong> {{ question.detailContent }}</div>
        <div>
          <strong>유형:</strong> {{ getQuestionTypeLabel(question.type) }}
          &nbsp;&nbsp;
          <strong>난이도:</strong> {{ getDifficultyLabel(question.difficulty) }}
        </div>
        <div v-if="question.answer"><strong>정답:</strong> {{ question.answer }}</div>
      </v-card-text>


      <!-- 선지 -->
      <v-card v-if="question.type === 'MULTIPLE' && question.questionOptions?.length" class="mb-4" variant="tonal">
        <v-card-title>선택지</v-card-title>
        <v-list>
          <v-list-item v-for="(opt, i) in question.questionOptions" :key="i">
            <v-list-item-title>
              {{ opt.optionNumber ? `${opt.optionNumber}. ` : '' }}{{ opt.content }}
              <span v-if="question.answer?.trim() === opt.content?.trim()" class="text-success font-weight-bold">
                <v-icon v-if="question.answer === opt.content" color="green" size="small"
                  class="ml-1">mdi-check-circle</v-icon>
              </span>
            </v-list-item-title>
          </v-list-item>
        </v-list>
      </v-card>

      <!-- 채점 기준 -->
      <v-card v-if="question.gradingCriteria?.length" class="mb-4" variant="tonal">
        <v-card-title>채점 기준</v-card-title>
        <v-simple-table>
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
      </v-card>

      <!-- 사용중인 실무테스트 -->
      <v-card v-if="question.usedJobTests?.length" class="mb-4" variant="tonal">
        <v-card-title>이 문제를 사용하는 실무 테스트</v-card-title>
        <v-list>
          <v-list-item v-for="(test, i) in question.usedJobTests" :key="i">
            <v-list-item-title>{{ test.title }}</v-list-item-title>
          </v-list-item>
        </v-list>
      </v-card>

      <!-- 생성/수정 정보 -->


      <v-card-actions class="justify-end">
        <v-btn text @click="closeModal">닫기</v-btn>
        <v-btn v-if="props.showDelete" color="error" variant="outlined" prepend-icon="mdi-delete" @click="handleDeleteConfirm">
          삭제하기
        </v-btn>
        <v-btn v-if="props.showEdit" color="primary" variant="tonal" @click="goEditPage" prepend-icon="mdi-pencil">
          수정하기
        </v-btn>
      </v-card-actions>
    </v-card>
    <AlertModal v-if="showDeleteConfirm" :message="'정말 이 문제를 삭제하시겠습니까? 복구할 수 없습니다.'" @confirm="confirmDelete"
      @cancel="showDeleteConfirm = false" />
  </v-dialog>

</template>

<script setup>
import { useRouter } from 'vue-router'
import { ref, defineProps, defineEmits } from 'vue'
import { useToast } from 'vue-toastification'

import { useJobtestQuestionStore } from '@/stores/jobtestQuestionStore'

import { getQuestionTypeLabel } from '@/constants/employment/questionTypes'
import { getDifficultyLabel } from '@/constants/employment/difficulty'
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

</script>

<style scoped>
.v-card {
  border-radius: 16px;
}

.info-table {
  font-size: 14px;
  color: #333;
  margin-top: 16px;
}

.info-table thead th {
  color: #666;
  font-weight: 600;
  font-size: 12px;
  border-bottom: 1px solid #ccc;
  padding: 8px;
}

.info-table tbody td {
  padding: 10px 8px;
  font-weight: 500;
  color: #1e1e1e;
}
</style>
