<template>
    <div class="subjective-form">
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
        
        <div class="form-section">
            <div class="form-label">
                <v-icon class="label-icon">mdi-check-circle</v-icon>
                정답
            </div>
            <v-text-field 
                label="정답을 입력해주세요." 
                v-model="form.answer" 
                :error-messages="errors.answer"
                variant="outlined"
                class="answer-input"
                prepend-inner-icon="mdi-lightbulb-outline"
            />
        </div>
    </div>
</template>

<script setup>
defineProps({
    form: Object,
    errors: {
        type: Object,
        default: () => ({})
    }
});
</script>

<style scoped>
.subjective-form {
    display: flex;
    flex-direction: column;
    gap: 24px;
}

.form-section {
    display: flex;
    flex-direction: column;
    gap: 8px;
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

.answer-input {
    border-radius: 8px;
}

.answer-input :deep(.v-field) {
    border-radius: 8px;
    transition: all 0.2s ease;
}

.answer-input :deep(.v-field:hover) {
    box-shadow: 0 2px 8px rgba(25, 118, 210, 0.1);
}

/* 반응형 디자인 */
@media (max-width: 600px) {
    .subjective-form {
        gap: 20px;
    }
    
    .form-label {
        font-size: 0.9rem;
    }
    
    .label-icon {
        font-size: 0.9rem;
    }
}
</style>