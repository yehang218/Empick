<template>
  <v-container class="pa-6">
    <h2 class="text-h6 font-weight-bold mb-4">문제 상세 정보</h2>

    <!-- 문제 기본 정보 -->
    <v-card class="mb-4" variant="outlined">
      <v-card-text>
        <div><strong>문제 내용:</strong> {{ question.content }}</div>
        <div v-if="question.detailContent"><strong>상세 설명:</strong> {{ question.detailContent }}</div>
        <div>
          <strong>유형:</strong> {{ getQuestionTypeLabel(question.type) }}
          &nbsp;&nbsp;
          <strong>난이도:</strong> {{ getDifficultyLabel(question.difficulty) }}
        </div>
        <div v-if="question.answer"><strong>정답:</strong> {{ question.answer }}</div>
      </v-card-text>
    </v-card>

    <!-- 선지 -->
    <v-card v-if="question.options && question.options.length" class="mb-4" variant="tonal">
      <v-card-title>선택지</v-card-title>
      <v-list>
        <v-list-item v-for="(opt, i) in question.options" :key="i">
          <v-list-item-content>
            <v-list-item-title>
              {{ opt.optionNumber ? `${opt.optionNumber}. ` : '' }}{{ opt.content }}
            </v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-card>

    <!-- 채점 기준 -->
    <v-card v-if="question.gradingCriteria && question.gradingCriteria.length" class="mb-4" variant="tonal">
      <v-card-title>채점 기준</v-card-title>
      <v-simple-table>
        <thead>
          <tr>
            <th>기준 내용</th>
            <th>가중치</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(criteria, i) in question.gradingCriteria" :key="i">
            <td>{{ criteria.content }}</td>
            <td>{{ criteria.scoreWeight }}</td>
          </tr>
        </tbody>
      </v-simple-table>
    </v-card>

    <!-- 사용중인 실무테스트 -->
    <v-card v-if="question.usedJobTests && question.usedJobTests.length" class="mb-4" variant="tonal">
      <v-card-title>이 문제를 사용하는 실무 테스트</v-card-title>
      <v-list>
        <v-list-item v-for="(test, i) in question.usedJobTests" :key="i">
          <v-list-item-content>
            <v-list-item-title>
              {{ test.title }} (ID: {{ test.id }})
            </v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-card>

    <!-- 생성/수정 정보 -->
    <div class="mt-4">
      <v-row>
        <v-col cols="6"><strong>생성자:</strong> {{ question.createdMemberName }} (ID: {{ question.createdMemberId }})</v-col>
        <v-col cols="6"><strong>생성일:</strong> {{ formatDate(question.createdAt) }}</v-col>
      </v-row>
      <v-row>
        <v-col cols="6" v-if="question.updatedMemberId"><strong>최종 수정자:</strong> {{ question.updatedMemberName }} (ID: {{ question.updatedMemberId }})</v-col>
        <v-col cols="6" v-if="question.updatedAt"><strong>최종 수정일:</strong> {{ formatDate(question.updatedAt) }}</v-col>
      </v-row>
    </div>
  </v-container>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import QuestionQueryDTO from '@/dto/QuestionQueryDTO'; // 한 파일에 모두 정의된 구조

// 실제로는 API에서 받아온 데이터를 사용
const mockApiResponse = {
  id: 1,
  content: 'Big-O 표기법에 대한 설명으로 옳은것은?',
  detailContent: '',
  type: 'MULTIPLE',
  difficulty: 'EASY',
  answer: 'O(1)은 입력 크기에 영향을 받는다.',
  createdMemberId: 1,
  createdMemberName: '김현식',
  updatedMemberId: 2,
  updatedMemberName: '박수정',
  createdAt: '2025-05-22T05:23:00',
  updatedAt: '2025-05-26T12:44:00',
  options: [
    { optionNumber: 1, content: 'O(1)은 입력 크기에 영향을 받는다.' },
    { optionNumber: 2, content: 'O(n)은 입력 크기가 증가하면 시간은 제곱으로 증가한다.' },
    { optionNumber: 3, content: 'O(log n)은 입력 크기가 늘어나도 시간은 선형적으로 증가한다.' },
    { optionNumber: 4, content: 'O(n²)는 최악의 경우에만 적용된다.' }
  ],
  usedJobTests: [
    { id: 101, title: '2024 하반기 현장실습인턴 - IT 직무' },
    { id: 102, title: '2025 하반기 현장실습인턴 - IT 직무_A그룹' }
  ],
  gradingCriteria: [
    { content: '정확하게 O(1)의 정의를 서술', scoreWeight: 1 }
  ]
};

const question = ref(null);

onMounted(() => {
  // 실제로는 API 호출 후 받아온 데이터에서 변환
  question.value = QuestionQueryDTO.fromJSON(mockApiResponse);
});

// 라벨 변환 함수들 (프로젝트별 상수/유틸 활용 가능)
function getQuestionTypeLabel(type) {
  switch (type) {
    case 'MULTIPLE': return '선택형';
    case 'SUBJECTIVE': return '단답형';
    case 'DESCRIPTIVE': return '서술형';
    default: return type;
  }
}
function getDifficultyLabel(diff) {
  switch (diff) {
    case 'EASY': return '쉬움';
    case 'MEDIUM': return '보통';
    case 'HARD': return '어려움';
    default: return diff;
  }
}
function formatDate(dateStr) {
  if (!dateStr) return '';
  const d = new Date(dateStr);
  return `${d.getFullYear()}-${(d.getMonth()+1).toString().padStart(2,'0')}-${d.getDate().toString().padStart(2,'0')} ${d.getHours().toString().padStart(2,'0')}:${d.getMinutes().toString().padStart(2,'0')}`;
}
</script>

<style scoped>
.v-card {
  border-radius: 16px;
}
</style>
