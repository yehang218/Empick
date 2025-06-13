<template>
  <v-container>
    <h2 class="text-h6 font-weight-bold mb-4">객관식/단답형 답안 목록</h2>
    <v-row dense>
      <v-col
        v-for="answer in filteredAnswers"
        :key="answer.id"
        cols="12"
        md="6"
      >
        <v-card class="mb-4">
          <v-card-title>
            <span class="font-weight-bold">문제:</span>
            <span class="ml-2">{{ answer.question.content }}</span>
          </v-card-title>
          <v-card-text>
            <!-- 선택지가 있는 경우 -->
            <div v-if="hasOptions(answer)">
              <div class="mb-2 font-weight-bold">선택지</div>
              <v-list dense>
                <v-list-item
                  v-for="opt in answer.question.options"
                  :key="opt.id"
                  :class="{
                    'bg-green-lighten-4': isCorrectOption(answer, opt),
                    'bg-grey-lighten-3': isSelectedOption(answer, opt) && !isCorrectOption(answer, opt)
                  }"
                >
                  <v-list-item-content>
                    <v-list-item-title>
                      {{ opt.optionNumber }}. {{ opt.content }}
                      <v-chip v-if="isCorrectOption(answer, opt)" color="success" size="x-small" class="ml-2">정답</v-chip>
                      <v-chip v-else-if="isSelectedOption(answer, opt)" color="primary" size="x-small" class="ml-2">선택</v-chip>
                    </v-list-item-title>
                  </v-list-item-content>
                </v-list-item>
              </v-list>
            </div>
            <!-- 선택지가 없는 경우: 주관식 -->
            <div v-else>
              <span class="font-weight-bold">정답:</span>
              <span class="ml-2">{{ answer.question.answer }}</span> <br>
              <span class="font-weight-bold">답변:</span>
              <span class="ml-2">{{ answer.content }}</span>
            </div>
            <!-- 공통 정보 -->
            <div class="mt-3">
              <span class="font-weight-bold">채점 결과:</span>
              <v-chip :color="getColor(answer.isCorrect)" class="ml-2" size="small">
                {{ getResultLabel(answer.isCorrect) }}
              </v-chip>
              <span class="ml-4">점수: <strong>{{ answer.score }}</strong></span>
              <span class="ml-4">시도: <strong>{{ answer.attempt }}</strong>회</span>
            </div>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>


<script setup>
import { computed, onMounted } from 'vue';
import  AnswerResponseDTO  from '@/dto/employment/jobtest/answerResponseDTO';
import { useAnswerStore } from '@/stores/answerStore';

const props = defineProps(['applicationJobtestId']);
// 1. Pinia에서 가져오기
const answerStore = useAnswerStore();

// 2. DTO 변환 (만약 아직 변환 전 원본 JSON 상태라면)
const answers = computed(() =>
  answerStore.answers.map(AnswerResponseDTO.fromJSON)
);

// 3. DESCRIPTIVE 아닌 것만
const filteredAnswers = computed(() =>
  answers.value.filter(a => a.question.type !== 'DESCRIPTIVE')
);

console.log(filteredAnswers.value);

// 선택지가 있는지 체크
function hasOptions(answer) {
  return answer.question.options && answer.question.options.length > 0;
}

// 내 답변(숫자 또는 문자열) == 옵션번호 or 옵션 내용 일치 여부
function isSelectedOption(answer, option) {
  // 답변이 옵션 번호(숫자)로 제출되었는지, 옵션 내용(문자)인지 상황에 맞게 비교
  // 예: answer.content === "2" 또는 answer.content === "의존성 명확화"
  return (
    answer.content == option.optionNumber ||
    answer.content === option.content
  );
}

// 정답 옵션
function isCorrectOption(answer, option) {
  return (
    answer.question.answer == option.optionNumber ||
    answer.question.answer === option.content
  );
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
        console.error('Failed to fetch answer:', error)
    }
})
</script>