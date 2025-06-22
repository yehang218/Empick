<template>
  <div class="question-view">
    <div class="question-header">
      <div class="question-info">
        <span class="question-number">{{ questionIndex + 1 }}</span>
        <div class="question-content">
          <h3 class="question-title">{{ question.title }}</h3>
          <div class="question-meta">
            <!-- <span class="question-type-tag" :style="getQuestionTypeStyle(question.questionType || question.type)">
              {{ getQuestionTypeLabel(question.questionType || question.type) }}
            </span> -->
            <span class="question-score">
              <v-icon class="score-icon">mdi-star</v-icon>
              {{ question.score }}점
            </span>
          </div>
        </div>
      </div>
    </div>
    
    <div class="question-body">
      <div v-if="getQuestionTypeKey(question.questionType || question.type) === 'MULTIPLE'">
        <MultipleChoice :options="question.options" :answer="answer" @select="val => $emit('updateAnswer', val)" />
      </div>
      <div v-else-if="getQuestionTypeKey(question.questionType || question.type) === 'SUBJECTIVE'">
        <ShortAnswer :answer="answer" @input="val => { 
          console.log('📝 QuestionView 단답형 답안 입력:', val)
          $emit('updateAnswer', val) 
        }" />
      </div>
    </div>
  </div>
</template>

<script setup>
import MultipleChoice from '@/components/employment/MultipleChoice.vue'
import ShortAnswer from '@/components/employment/ShortAnswer.vue'
import { getQuestionTypeLabel, getQuestionTypeColors } from '@/constants/employment/questionTypes.js'

const props = defineProps({
  question: Object,
  answer: [Number, String, null],
  questionIndex: {
    type: Number,
    default: 0
  }
})

// 디버깅용 로그
console.log('🔍 QuestionView - question:', {
  type: props.question.type,
  questionType: props.question.questionType,
  question: props.question
})

const getQuestionTypeKey = (type) => {
  console.log('🔍 getQuestionTypeKey input:', { type, typeOf: typeof type })
  
  // 문자열 "0", "1", "2" 처리
  if (type === 0 || type === '0') return 'MULTIPLE'
  if (type === 1 || type === '1') return 'SUBJECTIVE'
  if (type === 2 || type === '2') return 'DESCRIPTIVE'
  
  // 문자열 타입 처리
  if (type === 'MULTIPLE') return 'MULTIPLE'
  if (type === 'SUBJECTIVE') return 'SUBJECTIVE'
  if (type === 'DESCRIPTIVE') return 'DESCRIPTIVE'
  
  // 기본값
  console.log('⚠️ Unknown question type:', type, 'defaulting to MULTIPLE')
  return 'MULTIPLE'
}

const getQuestionTypeStyle = (type) => {
  const key = getQuestionTypeKey(type)
  const colors = getQuestionTypeColors(key);
  return {
    backgroundColor: colors.background,
    color: colors.text,
  };
};
</script>

<style scoped>
.question-view {
  width: 100%;
  margin-bottom: 32px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  border: 1px solid rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.question-view:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.question-header {
  background: linear-gradient(135deg, #f8f9fa, #e9ecef);
  padding: 24px;
  border-bottom: 1px solid #e0e0e0;
}

.question-info {
  display: flex;
  align-items: flex-start;
  gap: 16px;
}

.question-number {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, #1976d2, #42a5f5);
  color: white;
  border-radius: 50%;
  font-size: 1.2rem;
  font-weight: 700;
  flex-shrink: 0;
  box-shadow: 0 2px 8px rgba(25, 118, 210, 0.3);
}

.question-content {
  flex: 1;
}

.question-title {
  color: #1a237e;
  font-size: 1.3rem;
  font-weight: 600;
  margin: 0 0 12px 0;
  line-height: 1.4;
}

.question-meta {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.question-type-tag {
  display: inline-flex;
  align-items: center;
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 0.75rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: all 0.2s ease;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.question-type-tag:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.question-score {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #666;
  font-size: 0.9rem;
  font-weight: 500;
}

.score-icon {
  color: #ffc107;
  font-size: 1rem;
}

.question-body {
  padding: 24px;
}

/* 반응형 디자인 */
@media (max-width: 960px) {
  .question-view {
    margin-bottom: 24px;
    border-radius: 12px;
  }
  
  .question-header {
    padding: 20px;
  }
  
  .question-info {
    gap: 12px;
  }
  
  .question-number {
    width: 36px;
    height: 36px;
    font-size: 1.1rem;
  }
  
  .question-title {
    font-size: 1.2rem;
  }
  
  .question-body {
    padding: 20px;
  }
}

@media (max-width: 600px) {
  .question-view {
    margin-bottom: 16px;
    border-radius: 8px;
  }
  
  .question-header {
    padding: 16px;
  }
  
  .question-info {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
  
  .question-number {
    width: 32px;
    height: 32px;
    font-size: 1rem;
  }
  
  .question-title {
    font-size: 1.1rem;
    margin-bottom: 8px;
  }
  
  .question-meta {
    gap: 8px;
  }
  
  .question-type-tag {
    padding: 4px 8px;
    font-size: 0.7rem;
  }
  
  .question-score {
    font-size: 0.85rem;
  }
  
  .question-body {
    padding: 16px;
  }
}
</style>