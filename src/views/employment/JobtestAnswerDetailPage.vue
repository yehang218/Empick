<template>
  <v-container class="answer-detail-page">
    <!-- 헤더 섹션 -->
    <div class="header-section">
      <div class="header-actions">
        <v-btn prepend-icon="mdi-arrow-left" variant="tonal" @click="goBack" class="back-btn">
          뒤로가기
        </v-btn>
      </div>
      <div class="header-content">
        <div class="title-section">
          <h2 class="text-h5 font-weight-bold">{{ applicantInfo?.jobtestTitle || '실무 테스트' }}</h2>
          <p class="text-body-2 text-medium-emphasis mt-1">{{ applicantInfo?.applicantName || '지원자' }} - 실무 테스트 제출 답안</p>
          
          <!-- 지원자 정보 -->
          <div v-if="applicantInfo" class="applicant-info">
            <div class="applicant-card">
              <div class="applicant-header">
                <v-icon class="applicant-icon">mdi-account</v-icon>
                <span class="applicant-name">{{ applicantInfo.applicantName || '지원자' }}</span>
              </div>
            </div>
          </div>
        </div>
        <div class="score-section">
          <div class="total-score-card">
            <span class="score-label">총점</span>
            <span class="score-value">{{ totalScore }}점</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 답안 목록 -->
    <div class="answers-section">
      <v-row dense>
        <v-col v-for="(answer, idx) in filteredAnswers" :key="answer.id" cols="12" md="12">
          <v-card class="answer-card mb-4" :class="{
            'answer-wrong': answer.isCorrect === 'WRONG',
            'answer-correct': answer.isCorrect === 'CORRECT',
            'answer-partial': answer.isCorrect === 'PARTIAL'
          }">
            <v-card-title class="answer-header">
              <div class="question-info">
                <span class="question-number">{{ idx + 1 }}.</span>
                <div class="question-details">
                  <span class="question-content">{{ answer.question.content }}</span>
                  <div class="question-meta">
                    <span class="question-type-tag" :style="getQuestionTypeStyle(answer.question.type)">
                      {{ getQuestionTypeLabel(answer.question.type) }}
                    </span>
                    <span class="question-difficulty-tag" :style="getDifficultyStyle(answer.question.difficulty)">
                      {{ getDifficultyLabel(answer.question.difficulty) }}
                    </span>
                  </div>
                </div>
              </div>
              <div class="score-display">
                <span class="score-text" :class="{
                  'score-wrong': answer.isCorrect === 'WRONG',
                  'score-correct': answer.isCorrect === 'CORRECT',
                  'score-partial': answer.isCorrect === 'PARTIAL'
                }">
                  {{ answer.score }}/{{ answer.maxScore || 3 }}
                </span>
              </div>
            </v-card-title>

            <v-card-text class="answer-content">
              <!-- 선택지가 있는 경우: 객관식 -->
              <template v-if="hasOptions(answer)">
                <div class="options-section">
                  <div v-for="opt in answer.question.options" :key="opt.id" 
                       class="option-item" 
                       :class="{
                         'option-selected': isSelectedOption(answer, opt),
                         'option-correct': isCorrectOption(answer, opt)
                       }">
                    <span class="option-number">{{ opt.optionNumber }}.</span>
                    <span class="option-content">{{ opt.content }}</span>
                    <div class="option-badges">
                      <v-chip v-if="isCorrectOption(answer, opt)" color="success" size="x-small" class="ml-2">
                        정답
                      </v-chip>
                      <v-chip v-else-if="isSelectedOption(answer, opt)" color="primary" size="x-small" class="ml-2">
                        선택
                      </v-chip>
                    </div>
                  </div>
                </div>
                
                <!-- 오답일 때 정답 강조 -->
                <div v-if="answer.isCorrect === 'WRONG'" class="correct-answer-section">
                  <v-divider class="my-4" />
                  <div class="correct-answer-display">
                    <span class="correct-label">정답:</span>
                    <span class="correct-content">
                      {{ getAnswerContent(answer.question) }}
                    </span>
                  </div>
                </div>
              </template>

              <!-- 선택지 없는 경우: 주관식 -->
              <template v-else>
                <div class="subjective-answer-section">
                  <div class="answer-row">
                    <span class="answer-label">정답:</span>
                    <span class="answer-text">{{ answer.question.answer }}</span>
                  </div>
                  <div class="answer-row">
                    <span class="answer-label">답변:</span>
                    <span class="answer-text">{{ answer.content }}</span>
                  </div>
                </div>
              </template>

              <!-- 공통 정보 -->
              <div class="answer-footer">
                <div class="answer-meta">
                  <v-chip :color="getColor(answer.isCorrect)" class="result-chip" size="small">
                    {{ getResultLabel(answer.isCorrect) }}
                  </v-chip>
                  <span class="meta-info">점수: <strong>{{ answer.score }}</strong></span>
                  <span class="meta-info">시도: <strong>{{ answer.attempt }}</strong>회</span>
                </div>
              </div>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>
    </div>
  </v-container>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useAnswerStore } from '@/stores/answerStore';
import { getDifficultyLabel, getDifficultyColors } from '@/constants/employment/difficulty.js';
import { getQuestionTypeLabel, getQuestionTypeColors } from '@/constants/employment/questionTypes.js';
import { getApplicationJobtestDetailService } from '@/services/jobtestService';
import { getApplicationByIdService } from '@/services/applicationService';
import { getApplicantByIdService } from '@/services/applicantService';
import { useApplicationStore } from '@/stores/applicationStore'

const router = useRouter();
const route = useRoute();
const props = defineProps(['applicationJobtestId']);
const answerStore = useAnswerStore();
const applicationStore = useApplicationStore();

// 지원자 정보 상태
const applicantInfo = ref(null);
const applicationInfo = ref(null);
const loadingApplicantInfo = ref(false);

const answers = computed(() => answerStore.answers);

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

const getQuestionTypeStyle = (type) => {
  const colors = getQuestionTypeColors(type);
  return {
    backgroundColor: colors.background,
    color: colors.text,
  };
};

const getDifficultyStyle = (difficulty) => {
  const colors = getDifficultyColors(difficulty);
  return {
    backgroundColor: colors.background,
    color: colors.text,
  };
};

const goBack = () => {
  applicationStore.clearSelectedJobtestInfo();
  router.go(-1);
};

// 지원자 정보 가져오기 (쿼리 사용 X, 무조건 service)
const fetchApplicantInfo = async () => {
  loadingApplicantInfo.value = true;
  try {
    const applicationJobtestData = await getApplicationJobtestDetailService(Number(props.applicationJobtestId));
    if (!applicationJobtestData) {
      console.error('applicationJobtestData is null or undefined');
      return;
    }
    // 2. applicationId를 통해 지원서 정보 가져오기
    if (applicationJobtestData.applicationId) {
      const applicationData = await getApplicationByIdService(applicationJobtestData.applicationId);
      applicationInfo.value = applicationData;
      // 3. applicantId를 통해 지원자 정보 가져오기
      if (applicationData.applicantId) {
        const applicantData = await getApplicantByIdService(applicationData.applicantId);
        // 4. 모든 정보를 합쳐서 applicantInfo에 저장
        applicantInfo.value = {
          ...applicationJobtestData,
          applicantName: applicantData.name,
          applicantEmail: applicantData.email,
          applicantPhone: applicantData.phone,
          recruitmentTitle: applicationJobtestData.recruitmentTitle || '채용 공고 정보 없음',
          jobtestTitle: applicationJobtestData.jobtestTitle || '실무 테스트',
          submittedAt: applicationJobtestData.submittedAt,
          applicationId: applicationJobtestData.applicationId,
          applicantId: applicationData.applicantId
        };
      }
    }
  } catch (error) {
    console.error('Failed to fetch applicant info:', error);
  } finally {
    loadingApplicantInfo.value = false;
  }
};

onMounted(async () => {
  try {
    window.scrollTo({ top: 0, behavior: 'smooth' });
    await Promise.all([
      answerStore.fetchAnswers(Number(props.applicationJobtestId)),
      fetchApplicantInfo()
    ]);
  } catch (error) {
    console.error('Failed to fetch data:', error);
  }
});
</script>

<style scoped>
.answer-detail-page {
  padding: 24px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.header-section {
  margin-bottom: 32px;
}

.header-actions {
  margin-bottom: 16px;
}

.back-btn {
  transition: all 0.2s ease-in-out;
}

.back-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 24px;
}

.title-section {
  flex: 1;
}

.title-section h2 {
  color: #1a237e;
  margin-bottom: 4px;
}

.applicant-info {
  margin-top: 16px;
}

.applicant-card {
  background: white;
  border-radius: 12px;
  padding: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  border: 1px solid #e0e0e0;
}

.applicant-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
}

.applicant-icon {
  color: #1976d2;
  font-size: 1.2rem;
}

.applicant-name {
  font-weight: 600;
  font-size: 1.1rem;
  color: #333;
}

.score-section {
  flex-shrink: 0;
}

.total-score-card {
  background: linear-gradient(135deg, #4caf50, #66bb6a);
  color: white;
  padding: 16px 24px;
  border-radius: 12px;
  text-align: center;
  box-shadow: 0 4px 12px rgba(76, 175, 80, 0.3);
  min-width: 120px;
}

.score-label {
  display: block;
  font-size: 0.875rem;
  font-weight: 500;
  opacity: 0.9;
  margin-bottom: 4px;
}

.score-value {
  display: block;
  font-size: 1.75rem;
  font-weight: 700;
}

.answers-section {
  margin-top: 24px;
}

.answer-card {
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  border: 2px solid transparent;
}

.answer-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.12);
}

.answer-wrong {
  border-color: #ffebee;
  background-color: #fff5f5;
}

.answer-correct {
  border-color: #e8f5e8;
  background-color: #f8fff8;
}

.answer-partial {
  border-color: #fff8e1;
  background-color: #fffef7;
}

.answer-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding: 20px 24px 16px;
  border-bottom: 1px solid #e0e0e0;
}

.question-info {
  display: flex;
  align-items: flex-start;
  gap: 8px;
  flex: 1;
}

.question-number {
  font-weight: 700;
  color: #1976d2;
  font-size: 1.1rem;
  flex-shrink: 0;
}

.question-details {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.question-content {
  font-weight: 500;
  line-height: 1.5;
  color: #333;
}

.question-meta {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.question-type-tag,
.question-difficulty-tag {
  display: inline-flex;
  align-items: center;
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 0.75rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.3px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  transition: all 0.2s ease;
}

.score-display {
  flex-shrink: 0;
  margin-left: 16px;
}

.score-text {
  font-size: 1.25rem;
  font-weight: 700;
  padding: 8px 12px;
  border-radius: 8px;
  background-color: #f5f5f5;
}

.score-correct {
  color: #2e7d32;
  background-color: #e8f5e8;
}

.score-wrong {
  color: #c62828;
  background-color: #ffebee;
}

.score-partial {
  color: #f57c00;
  background-color: #fff8e1;
}

.answer-content {
  padding: 20px 24px;
}

.options-section {
  margin-bottom: 16px;
}

.option-item {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  margin-bottom: 8px;
  border-radius: 8px;
  background-color: #fafafa;
  border: 2px solid transparent;
  transition: all 0.2s ease;
}

.option-selected {
  background-color: #e3f2fd;
  border-color: #2196f3;
}

.option-correct {
  background-color: #e8f5e8;
  border-color: #4caf50;
}

.option-number {
  font-weight: 600;
  color: #666;
  margin-right: 12px;
  min-width: 24px;
}

.option-content {
  flex: 1;
  font-weight: 500;
}

.option-badges {
  display: flex;
  gap: 4px;
}

.correct-answer-section {
  margin-top: 16px;
}

.correct-answer-display {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 16px;
  background-color: #e8f5e8;
  border-radius: 8px;
  border-left: 4px solid #4caf50;
}

.correct-label {
  font-weight: 600;
  color: #2e7d32;
}

.correct-content {
  font-weight: 500;
  color: #1b5e20;
}

.subjective-answer-section {
  margin-bottom: 16px;
}

.answer-row {
  display: flex;
  align-items: flex-start;
  margin-bottom: 12px;
  gap: 12px;
}

.answer-label {
  font-weight: 600;
  color: #666;
  min-width: 60px;
  flex-shrink: 0;
}

.answer-text {
  font-weight: 500;
  color: #333;
  line-height: 1.5;
  flex: 1;
}

.answer-footer {
  margin-top: 20px;
  padding-top: 16px;
  border-top: 1px solid #e0e0e0;
}

.answer-meta {
  display: flex;
  align-items: center;
  gap: 16px;
  flex-wrap: wrap;
}

.result-chip {
  font-weight: 600;
}

.meta-info {
  color: #666;
  font-size: 0.875rem;
}

.meta-info strong {
  color: #333;
}

@media (max-width: 768px) {
  .answer-detail-page {
    padding: 16px;
  }
  
  .header-content {
    flex-direction: column;
    gap: 16px;
  }
  
  .total-score-card {
    align-self: flex-end;
  }
  
  .answer-header {
    flex-direction: column;
    gap: 12px;
  }
  
  .score-display {
    margin-left: 0;
    align-self: flex-end;
  }
  
  .answer-meta {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }
}
</style>
