<template>
    <v-dialog :model-value="modelValue" @update:modelValue="emit('update:modelValue', $event)" max-width="1200">
        <v-card class="select-modal-card">
            <div class="modal-header">
                <v-card-title class="modal-title">
                    <v-icon class="title-icon">mdi-format-list-bulleted</v-icon>
                    실무 테스트 선택
                </v-card-title>
                <p class="modal-description">사용할 실무 테스트를 선택해주세요.</p>
            </div>
            
            <v-card-text class="modal-content">
                <div class="jobtest-list">
                    <div 
                        v-for="jobtest in jobtests" 
                        :key="jobtest.id" 
                        class="jobtest-item"
                        @click="select(jobtest)"
                    >
                        <div class="jobtest-info">
                            <div class="jobtest-title">{{ jobtest.title }}</div>
                            <div class="jobtest-meta">
                                <span class="meta-item">
                                    <v-icon class="meta-icon">mdi-clock-outline</v-icon>
                                    {{ jobtest.testTime }}분
                                </span>
                                <span class="meta-item">
                                    <v-icon class="meta-icon">mdi-trending-up</v-icon>
                                    {{ getDifficultyLabel(jobtest.difficulty) }}
                                </span>
                            </div>
                        </div>
                        <v-icon class="select-icon">mdi-chevron-right</v-icon>
                    </div>
                </div>
                
                <div v-if="jobtests.length === 0" class="empty-state">
                    <v-icon class="empty-icon">mdi-inbox-outline</v-icon>
                    <p class="empty-text">등록된 실무 테스트가 없습니다.</p>
                </div>
            </v-card-text>
        </v-card>
    </v-dialog>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue'
import { getDifficultyLabel } from '@/constants/employment/difficulty.js'

const props = defineProps({
    modelValue: Boolean,
    jobtests: Array
});
const emit = defineEmits(['update:modelValue', 'select']);

const select = (jobtest) => {
    emit('select', jobtest);
    emit('update:modelValue', false);
};
</script>

<style scoped>
.select-modal-card {
    border-radius: 16px;
    overflow: hidden;
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.12);
}

/* 모달 헤더 */
.modal-header {
    background: linear-gradient(135deg, #1976d2, #42a5f5);
    color: white;
    padding: 24px;
    text-align: center;
}

.modal-title {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 12px;
    font-size: 1.5rem;
    font-weight: 600;
    margin-bottom: 8px;
}

.title-icon {
    font-size: 1.8rem;
}

.modal-description {
    margin: 0;
    opacity: 0.9;
    font-size: 0.95rem;
}

/* 모달 콘텐츠 */
.modal-content {
    padding: 24px;
    max-height: 60vh;
    overflow-y: auto;
}

.jobtest-list {
    display: flex;
    flex-direction: column;
    gap: 12px;
}

.jobtest-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 16px;
    border: 1px solid #e0e0e0;
    border-radius: 12px;
    background: white;
    cursor: pointer;
    transition: all 0.2s ease;
}

.jobtest-item:hover {
    border-color: #1976d2;
    background: #f8f9fa;
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(25, 118, 210, 0.15);
}

.jobtest-info {
    flex: 1;
}

.jobtest-title {
    font-size: 1.1rem;
    font-weight: 600;
    color: #1a237e;
    margin-bottom: 8px;
    line-height: 1.3;
}

.jobtest-meta {
    display: flex;
    gap: 16px;
}

.meta-item {
    display: flex;
    align-items: center;
    gap: 4px;
    font-size: 0.85rem;
    color: #666;
}

.meta-icon {
    font-size: 1rem;
    color: #1976d2;
}

.select-icon {
    color: #1976d2;
    font-size: 1.2rem;
    transition: transform 0.2s ease;
}

.jobtest-item:hover .select-icon {
    transform: translateX(4px);
}

/* 빈 상태 */
.empty-state {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 48px 24px;
    text-align: center;
    color: #666;
}

.empty-icon {
    font-size: 3rem;
    color: #ccc;
    margin-bottom: 16px;
}

.empty-text {
    font-size: 1rem;
    margin: 0;
}

/* 반응형 디자인 */
@media (max-width: 600px) {
    .modal-header {
        padding: 20px;
    }
    
    .modal-title {
        font-size: 1.3rem;
    }
    
    .title-icon {
        font-size: 1.5rem;
    }
    
    .modal-content {
        padding: 16px;
    }
    
    .jobtest-item {
        padding: 12px;
    }
    
    .jobtest-title {
        font-size: 1rem;
    }
    
    .jobtest-meta {
        flex-direction: column;
        gap: 4px;
    }
    
    .meta-item {
        font-size: 0.8rem;
    }
}
</style>
