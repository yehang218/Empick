<template>
    <div class="multiple-question-form">
        <!-- 문제 본문 입력 -->
        <div class="form-section">
            <div class="form-label">
                <v-icon class="label-icon">mdi-help-circle</v-icon>
                문제 내용
            </div>
            <v-textarea 
                label="문제를 입력해주세요." 
                v-model="form.content" 
                auto-grow 
                :error-messages="errors.content"
                variant="outlined"
                rows="4"
                class="question-textarea"
                prepend-inner-icon="mdi-text-box-outline"
            />
        </div>

        <!-- 보기 입력 + 정답 선택 -->
        <div class="form-section">
            <div class="form-label">
                <v-icon class="label-icon">mdi-format-list-bulleted</v-icon>
                보기
            </div>
            <div class="options-container">
                <div v-for="(option, idx) in form.questionOptions" :key="idx" class="option-item">
                    <!-- 정답 선택 버튼 -->
                    <v-btn 
                        icon 
                        :color="option.isAnswer ? 'success' : 'grey'" 
                        variant="tonal" 
                        class="answer-btn"
                        @click="selectAnswer(idx)"
                    >
                        <v-icon>{{ option.isAnswer ? 'mdi-check-circle' : 'mdi-circle-outline' }}</v-icon>
                    </v-btn>

                    <!-- 보기 내용 입력 -->
                    <v-text-field 
                        v-model="option.content" 
                        :label="`보기 ${idx + 1}`" 
                        class="option-input"
                        :error-messages="errors.questionOptions"
                        variant="outlined"
                        prepend-inner-icon="mdi-format-list-numbered"
                    />

                    <!-- 보기 삭제 버튼 -->
                    <v-btn 
                        icon="mdi-close" 
                        variant="text" 
                        color="error" 
                        class="remove-btn"
                        @click="removeOption(idx)"
                        :disabled="form.questionOptions.length <= 1" 
                    />
                </div>

                <!-- 보기 추가 버튼 -->
                <div class="add-option-section">
                    <v-btn 
                        variant="outlined" 
                        class="add-option-btn" 
                        @click="addOption" 
                        :disabled="form.questionOptions.length >= 5"
                        prepend-icon="mdi-plus"
                    >
                        항목 추가하기
                    </v-btn>
                    <div v-if="form.questionOptions.length >= 5" class="max-options-warning">
                        최대 5개의 보기만 추가할 수 있습니다.
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { onMounted, watch } from 'vue'

const props = defineProps({
    form: Object,
    errors: {
        type: Object,
        default: () => ({})
    }
});

// 보기 추가
function addOption() {
    if (props.form.questionOptions.length >= 5) return;
    props.form.questionOptions.push({ content: '', isAnswer: false });
}

// 보기 제거
function removeOption(index) {
    if (props.form.questionOptions.length <= 1) return;
    props.form.questionOptions.splice(index, 1);
}

// 정답 선택
function selectAnswer(selectedIdx) {
    props.form.questionOptions.forEach((opt, idx) => {
        opt.isAnswer = idx === selectedIdx;
    });
    // 자동으로 form.answer도 설정
    props.form.answer = props.form.questionOptions[selectedIdx].content;
}

function initializeCorrectOption() {
    if (
        !props.form ||
        !props.form.answer ||
        !props.form.questionOptions ||
        props.form.questionOptions.length === 0
    ) return;

    props.form.questionOptions.forEach(option => {
        option.isAnswer = option.content === props.form.answer;
    });
}

onMounted(() => {
    initializeCorrectOption()
})

watch(() => props.form.answer, () => {
    initializeCorrectOption()
})
</script>

<style scoped>
.multiple-question-form {
    display: flex;
    flex-direction: column;
    gap: 24px;
}

.form-section {
    display: flex;
    flex-direction: column;
    gap: 12px;
}

.form-label {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 0.95rem;
    font-weight: 500;
    color: #555;
}

.label-icon {
    color: #1976d2;
    font-size: 1rem;
}

.question-textarea {
    border-radius: 8px;
}

.question-textarea :deep(.v-field) {
    border-radius: 8px;
    transition: all 0.2s ease;
}

.question-textarea :deep(.v-field:hover) {
    box-shadow: 0 2px 8px rgba(25, 118, 210, 0.1);
}

/* 보기 섹션 */
.options-container {
    display: flex;
    flex-direction: column;
    gap: 12px;
}

.option-item {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 12px;
    border: 1px solid #e0e0e0;
    border-radius: 12px;
    background: #fafbfc;
    transition: all 0.2s ease;
}

.option-item:hover {
    border-color: #1976d2;
    background: #f8f9fa;
    box-shadow: 0 2px 8px rgba(25, 118, 210, 0.1);
}

.answer-btn {
    flex-shrink: 0;
    transition: all 0.2s ease;
}

.answer-btn:hover {
    transform: scale(1.1);
}

.option-input {
    flex: 1;
    border-radius: 8px;
}

.option-input :deep(.v-field) {
    border-radius: 8px;
    transition: all 0.2s ease;
}

.option-input :deep(.v-field:hover) {
    box-shadow: 0 2px 8px rgba(25, 118, 210, 0.1);
}

.remove-btn {
    flex-shrink: 0;
    transition: all 0.2s ease;
}

.remove-btn:hover {
    transform: scale(1.1);
}

/* 보기 추가 섹션 */
.add-option-section {
    display: flex;
    flex-direction: column;
    gap: 8px;
    margin-top: 8px;
}

.add-option-btn {
    align-self: flex-start;
    border-radius: 8px;
    transition: all 0.2s ease;
}

.add-option-btn:hover:not(:disabled) {
    transform: translateY(-1px);
    box-shadow: 0 4px 12px rgba(25, 118, 210, 0.2);
}

.max-options-warning {
    color: #f44336;
    font-size: 0.8rem;
    font-weight: 500;
    padding-left: 8px;
}

/* 반응형 디자인 */
@media (max-width: 600px) {
    .multiple-question-form {
        gap: 20px;
    }
    
    .form-label {
        font-size: 0.9rem;
    }
    
    .label-icon {
        font-size: 0.9rem;
    }
    
    .option-item {
        flex-direction: column;
        align-items: stretch;
        gap: 8px;
        padding: 16px;
    }
    
    .answer-btn {
        align-self: flex-start;
    }
    
    .remove-btn {
        align-self: flex-end;
    }
    
    .add-option-btn {
        align-self: stretch;
    }
}
</style>