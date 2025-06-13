<template>
  <v-container>
    <v-row>
      <v-col cols="4">
        <h2 class="text-h6 font-weight-bold mb-4">객관식/단답형 답안 목록</h2>
      </v-col>
      <v-spacer />
      <v-col cols="2">
        <div class="mb-6">
          <span class="font-weight-bold text-lg">총점:</span>
          <span class="ml-2 text-xl" style="color: #388e3c;">{{ totalScore }}점</span>
        </div>
      </v-col>
    </v-row>
    <v-row dense>
      <v-col v-for="(answer, idx) in filteredAnswers" :key="answer.id" cols="12" md="12">
        <v-card class="mb-6" :class="{
          'bg-red-lighten-5': answer.isCorrect === 'WRONG',
          'bg-green-lighten-5': answer.isCorrect === 'CORRECT'
        }">
          <v-card-title class="d-flex justify-space-between align-center pb-0">
            <span class="font-weight-bold">{{ idx + 1 }}. {{ answer.question.content }}</span>
            <span class="display-1" :class="{
              'text-grey': answer.isCorrect === 'WRONG',
              'text-success': answer.isCorrect === 'CORRECT'
            }">
              {{ answer.score }}/{{ answer.maxScore || 3 }}
            </span>
          </v-card-title>

          <v-card-text>
            <!-- 선택지가 있는 경우: 객관식 -->
            <template v-if="hasOptions(answer)">
              <div class="mb-3">
                <div v-for="opt in answer.question.options" :key="opt.id" class="mb-1" :style="{
                  color: isSelectedOption(answer, opt) ? '#222' : '#bbb',
                  fontWeight: isSelectedOption(answer, opt) ? 'bold' : 'normal',
                  fontSize: isSelectedOption(answer, opt) ? '1.1em' : '1em'
                }">
                  {{ opt.optionNumber }}. {{ opt.content }}
                  <v-chip v-if="isCorrectOption(answer, opt)" color="success" size="x-small" class="ml-2">정답</v-chip>
                  <v-chip v-else-if="isSelectedOption(answer, opt)" color="primary" size="x-small"
                    class="ml-2">선택</v-chip>
                </div>
              </div>
              <v-divider class="my-6" />
              <!-- 오답일 때 정답 강조 -->
              <div v-if="answer.isCorrect === 'WRONG'" class="mt-6">
                <span class="font-weight-bold">정답 :</span>
                <span class="text-error font-weight-bold text-lg ml-2">
                  {{ getAnswerContent(answer.question) }}
                </span>
              </div>
            </template>

            <!-- 선택지 없는 경우: 주관식 -->
            <template v-else>
              <div class="mb-2">
                <span class="font-weight-bold">정답:</span>
                <span class="ml-2">{{ answer.question.answer }}</span>
              </div>
              <div>
                <span class="font-weight-bold">답변:</span>
                <span class="ml-2">{{ answer.content }}</span>
              </div>
            </template>

            <!-- 공통 정보 -->
            <div class="mt-3 d-flex align-center justify-space-between">
              <div>
                <span class="font-weight-bold">채점 결과:</span>
                <v-chip :color="getColor(answer.isCorrect)" class="ml-2" size="small">
                  {{ getResultLabel(answer.isCorrect) }}
                </v-chip>
                <span class="ml-4">점수: <strong>{{ answer.score }}</strong></span>
                <span class="ml-4">시도: <strong>{{ answer.attempt }}</strong>회</span>
              </div>
              <!-- <v-btn color="success" variant="tonal" size="large">정정하기</v-btn> -->
            </div>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { computed, onMounted } from 'vue';
import AnswerResponseDTO from '@/dto/employment/jobtest/answerResponseDTO';
import { useAnswerStore } from '@/stores/answerStore';

const props = defineProps(['applicationJobtestId']);
const answerStore = useAnswerStore();

const answers = computed(() =>
  answerStore.answers.map(AnswerResponseDTO.fromJSON)
);

// DESCRIPTIVE가 아닌 것만
const filteredAnswers = computed(() =>
  answers.value.filter(a => a.question.type !== 'DESCRIPTIVE')
);

const totalScore = computed(() =>
  filteredAnswers.value.reduce((sum, a) => sum + (a.score || 0), 0)
);

function hasOptions(answer) {
  return answer.question.options && answer.question.options.length > 0;
}

function isSelectedOption(answer, option) {
  return (
    answer.content == option.optionNumber ||
    answer.content === option.content
  );
}

function isCorrectOption(answer, option) {
  return (
    answer.question.answer == option.optionNumber ||
    answer.question.answer === option.content
  );
}

// 정답 옵션을 옵션번호가 아닌 옵션 내용으로 변환
function getAnswerContent(question) {
  if (question.options && question.options.length) {
    const found = question.options.find(
      (opt) =>
        question.answer == opt.optionNumber ||
        question.answer === opt.content
    );
    return found ? `${found.optionNumber}. ${found.content}` : question.answer;
  }
  return question.answer;
}

function getColor(status) {
  switch (status) {
    case 'CORRECT':
      return 'success';
    case 'PARTIAL':
      return 'warning';
    case 'WRONG':
      return 'error';
    default:
      return 'default';
  }
}
function getResultLabel(status) {
  switch (status) {
    case 'CORRECT':
      return '정답';
    case 'PARTIAL':
      return '부분정답';
    case 'WRONG':
      return '오답';
    default:
      return '미채점';
  }
}

onMounted(async () => {
  try {
    await answerStore.fetchAnswers(Number(props.applicationJobtestId));
  } catch (error) {
    console.error('Failed to fetch answer:', error);
  }
});
</script>
