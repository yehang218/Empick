<template>
    <v-card-text class="modal-content">
        <div class="modal-header">
            <h2 class="modal-title">문제 등록</h2>
            <v-icon class="modal-icon">mdi-plus-circle</v-icon>
        </div>

        <!-- 문제 유형 탭 -->
        <div class="tab-section">
            <v-tabs v-model="activeTab" class="question-tabs">
                <v-tab value="MULTIPLE" class="tab-item">
                    <v-icon class="tab-icon">mdi-format-list-bulleted</v-icon>
                    선택형
                </v-tab>
                <v-tab value="SUBJECTIVE" class="tab-item">
                    <v-icon class="tab-icon">mdi-text-box-outline</v-icon>
                    단답형
                </v-tab>
                <!-- <v-tab value="DESCRIPTIVE">서술형</v-tab> -->
            </v-tabs>
        </div>

        <!-- 난이도 선택 -->
        <div class="difficulty-section">
            <div class="form-label">
                <v-icon class="label-icon">mdi-trending-up</v-icon>
                난이도
            </div>
            <v-select 
                v-model="form.difficulty" 
                :items="difficultyOptions" 
                label="난이도를 선택해주세요" 
                variant="outlined" 
                class="difficulty-select"
                prepend-inner-icon="mdi-trending-up"
            />
        </div>

        <!-- 문제 유형별 입력 폼 -->
        <div class="component-section">
            <component :is="currentComponent" v-model:form="form" />
        </div>

        <!-- 액션 버튼 -->
        <div class="action-buttons">
            <v-btn 
                variant="tonal"
                color="grey"
                class="cancel-btn"
                @click="showCancelModal = true"
                prepend-icon="mdi-close"
            >
                취소하기
            </v-btn>
            <v-btn 
                color="primary" 
                class="submit-btn" 
                @click="handleSubmit"
                prepend-icon="mdi-check"
            >
                등록하기
            </v-btn>
        </div>
        <Modal v-if="showCancelModal" message="정말 취소하시겠습니까?<br>입력한 내용이 모두 사라집니다." @confirm="handleCancelConfirm" @cancel="showCancelModal = false" />
    </v-card-text>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import { useToast } from 'vue-toastification'
import MultipleQuestionForm from '@/components/employment/MultipleQuestionForm.vue'
import SubjectiveForm from '@/components/employment/SubjectiveForm.vue'
import Modal from '@/components/common/Modal.vue'

import { useMemberStore } from '@/stores/memberStore'
import { useJobtestQuestionStore } from '@/stores/jobtestQuestionStore'

const emit = defineEmits(['close', 'saved'])
const toast = useToast()

const memberStore = useMemberStore()
const questionStore = useJobtestQuestionStore()
const form = computed(() => questionStore.form)

const activeTab = ref(form.value.type || 'MULTIPLE')

const difficultyOptions = [
    { title: '쉬움', value: 'EASY' },
    { title: '보통', value: 'MEDIUM' },
    { title: '어려움', value: 'HARD' }
]

watch(activeTab, (newType) => {
    form.value.type = newType
})

const currentComponent = computed(() => {
    switch (activeTab.value) {
        case 'MULTIPLE': return MultipleQuestionForm
        case 'SUBJECTIVE': return SubjectiveForm
        default: return MultipleQuestionForm
    }
})

const showCancelModal = ref(false)

const handleCancelConfirm = () => {
    showCancelModal.value = false
    emit('close')
}

const handleSubmit = async () => {
    if (!validateForm()) return

    try {
        await questionStore.submitQuestion(memberStore.form.id)
        toast.success('문제 등록이 완료되었습니다.')
        emit('saved')
    } catch {
        toast.error(questionStore.error || '문제 등록 중 오류가 발생했습니다.')
    }
}

function validateForm() {
    if (!form.value.content) {
        toast.error('문제 내용을 입력해주세요.')
        return false
    }

    if (form.value.type === 'SUBJECTIVE' && !form.value.answer) {
        toast.error('정답을 입력해주세요.')
        return false
    }

    if (form.value.type === 'MULTIPLE') {
        if (!form.value.questionOptions.length) {
            toast.error('선택지를 하나 이상 입력해주세요.')
            return false
        }
        if (form.value.questionOptions.some(opt => !opt.content)) {
            toast.error('선택지 내용을 입력해주세요.')
            return false
        }
        if (!form.value.questionOptions.some(opt => opt.isAnswer)) {
            toast.error('하나 이상의 정답을 지정해주세요.')
            return false
        }
    }

    // if (form.value.type === 'DESCRIPTIVE') {
    //     if (!form.value.gradingCriteria.length) {
    //         toast.error('채점 기준을 입력해주세요.')
    //         return false
    //     }
    //     const total = form.value.gradingCriteria.reduce((sum, c) => sum + c.scoreWeight, 0)
    //     if (Math.abs(total - 1) > 0.001) {
    //         toast.error('채점 기준 가중치의 합이 100%가 되어야 합니다.')
    //         return false
    //     }
    // }

    return true
}

onMounted(async () => {
    await memberStore.getMyInfo()
    const memberId = memberStore.form.id
    if (!memberId) {
        toast.error('등록자 정보를 불러오는 중입니다. 잠시 후 다시 시도해주세요.')
        return
    }

    // ✅ form 초기화 및 생성자 ID 설정
    questionStore.resetForm()
    form.value.createdMemberId = memberId
})
</script>

<style scoped>
.modal-content {
    padding: 24px;
}

/* 모달 헤더 */
.modal-header {
    display: flex;
    align-items: center;
    gap: 12px;
    margin-bottom: 24px;
    padding-bottom: 16px;
    border-bottom: 1px solid #e0e0e0;
}

.modal-title {
    color: #1a237e;
    font-size: 1.25rem;
    font-weight: 600;
    margin: 0;
}

.modal-icon {
    color: #1976d2;
    font-size: 1.5rem;
}

/* 탭 섹션 */
.tab-section {
    margin-bottom: 24px;
}

.question-tabs {
    border-radius: 12px;
    overflow: hidden;
}

.question-tabs :deep(.v-tab) {
    font-weight: 500;
    text-transform: none;
    letter-spacing: normal;
    min-height: 48px;
    transition: all 0.2s ease;
}

.question-tabs :deep(.v-tab:hover) {
    background-color: rgba(25, 118, 210, 0.04);
}

.question-tabs :deep(.v-tab--selected) {
    background-color: rgba(25, 118, 210, 0.08);
    color: #1976d2;
    font-weight: 600;
}

.tab-item {
    display: flex;
    align-items: center;
    gap: 8px;
}

.tab-icon {
    font-size: 1.1rem;
}

/* 난이도 섹션 */
.difficulty-section {
    margin-bottom: 24px;
}

.form-label {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 0.95rem;
    font-weight: 500;
    margin-bottom: 8px;
    color: #555;
}

.label-icon {
    color: #1976d2;
    font-size: 1rem;
}

.difficulty-select {
    max-width: 300px;
}

.difficulty-select :deep(.v-field) {
    border-radius: 8px;
    transition: all 0.2s ease;
}

.difficulty-select :deep(.v-field:hover) {
    box-shadow: 0 2px 8px rgba(25, 118, 210, 0.1);
}

/* 컴포넌트 섹션 */
.component-section {
    margin-bottom: 24px;
}

/* 액션 버튼 */
.action-buttons {
    display: flex;
    justify-content: flex-end;
    gap: 12px;
    padding-top: 16px;
    border-top: 1px solid #e0e0e0;
}

.submit-btn {
    min-width: 100px;
    border-radius: 8px;
    font-weight: 500;
    transition: all 0.2s ease;
}

.submit-btn:hover {
    transform: translateY(-1px);
    box-shadow: 0 4px 12px rgba(25, 118, 210, 0.3);
}

.cancel-btn {
    min-width: 100px;
    border-radius: 8px;
    font-weight: 500;
    transition: all 0.2s ease;
}

.cancel-btn:hover {
    transform: translateY(-1px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

/* 반응형 디자인 */
@media (max-width: 600px) {
    .modal-content {
        padding: 16px;
    }
    
    .modal-title {
        font-size: 1.1rem;
    }
    
    .modal-header {
        flex-direction: column;
        align-items: flex-start;
        gap: 8px;
    }
    
    .action-buttons {
        flex-direction: column;
        align-items: stretch;
        gap: 8px;
    }
    
    .difficulty-select {
        max-width: none;
    }
    
    .question-tabs :deep(.v-tab) {
        min-height: 40px;
        font-size: 0.9rem;
    }
    
    .tab-icon {
        font-size: 1rem;
    }
}
</style>
