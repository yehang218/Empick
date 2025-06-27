<template>
    <v-container class="question-form-page">
        <!-- 페이지 헤더 -->
        <div class="page-header">
            <div class="header-actions">
                <v-btn prepend-icon="mdi-arrow-left" variant="tonal" @click="goBack" class="back-btn">
                    목록으로
                </v-btn>
            </div>
            <h2 class="page-title">{{ isEdit ? '문제 수정' : '문제 등록' }}</h2>
            <p class="page-description">문제 유형을 선택하고 내용을 입력해주세요.</p>
        </div>

        <!-- 메인 폼 카드 -->
        <v-card class="form-card pa-6 mb-6" elevation="2">
            <div class="card-header">
                <h3 class="card-title">문제 정보</h3>
                <v-icon class="card-icon">mdi-help-circle-outline</v-icon>
            </div>

            <!-- 문제 유형 탭 -->
            <div class="tab-section">
                <v-tabs v-model="activeTab" :disabled="isEdit" class="question-tabs">
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
                    item-title="label" 
                    item-value="value"
                    variant="outlined" 
                    class="difficulty-select"
                    prepend-inner-icon="mdi-trending-up"
                />
            </div>

            <!-- 문제 유형별 입력 폼 -->
            <div class="component-section">
                <component :is="currentComponent" v-model:form="form" />
            </div>
        </v-card>

        <!-- 액션 버튼 -->
        <div class="action-buttons">
            <v-btn 
                variant="tonal" 
                color="grey" 
                class="cancel-btn" 
                @click="showCancelModal = true"
                prepend-icon="mdi-close"
            >
                취소
            </v-btn>
            <v-btn 
                color="primary" 
                class="submit-btn" 
                @click="handleSubmit"
                prepend-icon="mdi-check"
            >
                {{ isEdit ? '수정하기' : '등록하기' }}
            </v-btn>
        </div>

        <!-- 모달들 -->
        <SuccessModal 
            v-if="showSuccessModal" 
            :message="isEdit ? '문제 수정이 완료되었습니다.' : '문제 등록이 완료되었습니다.'"
            @confirm="handleSuccessConfirm"
            :showCancel="false"
        />

        <Modal 
            v-if="showCancelModal && !showSuccessModal" 
            message="정말 취소하시겠습니까?<br>입력한 내용이 모두 사라집니다." 
            @confirm="handleCancelConfirm" 
            @cancel="handleCancelClose" 
        />
    </v-container>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useToast } from 'vue-toastification'

import MultipleQuestionForm from '@/components/employment/MultipleQuestionForm.vue'
import SubjectiveForm from '@/components/employment/SubjectiveForm.vue'
import DescriptiveQuestionForm from '@/components/employment/DescriptiveQuestionForm.vue'
import SuccessModal from '@/components/common/AlertModal.vue'
import Modal from '@/components/common/Modal.vue'

import { DIFFICULTY_MAP } from '@/constants/employment/difficulty'

import { useMemberStore } from '@/stores/memberStore'
import { useJobtestQuestionStore } from '@/stores/jobtestQuestionStore'

const route = useRoute()
const router = useRouter()
const toast = useToast()

const memberStore = useMemberStore()
const jobtestQuestionStore = useJobtestQuestionStore()

const questionId = Number(route.params.id)
const showSuccessModal = ref(false)
const showCancelModal = ref(false)

// ❗ form과 isEdit은 store에서 가져옴
const form = computed(() => jobtestQuestionStore.form)
const isEdit = computed(() => jobtestQuestionStore.isEdit)

const activeTab = ref(form.value.type || 'MULTIPLE')

const difficultyOptions = Object.entries(DIFFICULTY_MAP).map(([value, label]) => ({
    value,
    label
}))

const currentComponent = computed(() => {
    switch (activeTab.value) {
        case 'MULTIPLE': return MultipleQuestionForm
        case 'SUBJECTIVE': return SubjectiveForm
        default: return DescriptiveQuestionForm
    }
})

// activeTab이 변경될 때 폼 타입도 함께 업데이트
watch(activeTab, (newType) => {
    if (!isEdit.value) { // 수정 모드가 아닐 때만 타입 변경 허용
        form.value.type = newType
    }
})

function goBack() {
    // 입력된 내용이 있는지 확인
    const hasContent = form.value.content || form.value.answer || 
                      (form.value.questionOptions && form.value.questionOptions.length > 0) ||
                      (form.value.gradingCriteria && form.value.gradingCriteria.length > 0)
    
    if (hasContent) {
        showCancelModal.value = true
    } else {
        // 내용이 없으면 바로 목록으로 이동
        router.push({ name: 'JobtestQuestionList' })
    }
}

function handleSuccessConfirm() {
    showSuccessModal.value = false
    router.push({ name: 'JobtestQuestionList' })
}

async function handleSubmit() {
    if (!validateForm()) return;
    try {
        const userId = memberStore.form.id
        if (!userId) {
            toast.error('사용자 정보를 불러오지 못했습니다.')
            return
        }

        await jobtestQuestionStore.submitQuestion(userId)
        showSuccessModal.value = true
    } catch (error) {
        console.error('문제 저장 실패:', error)
        // toast.error(jobtestQuestionStore.error || '저장 중 오류가 발생했습니다.')
    }
}

function validateForm() {
    if (!form.value.content) {
        toast.error('문제 내용을 입력해주세요.')
        return false
    }
    if (form.value.type === 'MULTIPLE') {
        if (!form.value.questionOptions || form.value.questionOptions.length === 0) {
            toast.error('선택지를 하나 이상 입력해주세요.')
            return false
        }
        if (!form.value.questionOptions.some(opt => opt.isAnswer)) {
            toast.error('하나 이상의 정답을 지정해주세요.')
            return false
        }
    }
    if (form.value.type === 'SUBJECTIVE' && !form.value.answer) {
        toast.error('정답을 입력해주세요.')
        return false
    }
    return true
}

function handleCancelConfirm() {
    showCancelModal.value = false
    jobtestQuestionStore.resetForm()
    router.push({ name: 'JobtestQuestionList' })
}

function handleCancelClose() {
    showCancelModal.value = false
    // 취소 모달을 닫으면 현재 페이지에 머무름
}

onMounted(async () => {
    try {
        await memberStore.getMyInfo()
        const memberId = memberStore.form.id
        if (!memberId) {
            toast.error('사용자 정보를 불러오는 중입니다. 잠시 후 다시 시도해주세요.')
            router.push({ name: 'JobtestQuestionList' })
            return
        }

        if (questionId) {
            await jobtestQuestionStore.loadQuestion(questionId, memberId)
        } else {
            jobtestQuestionStore.resetForm()
        }

        activeTab.value = form.value.type || 'MULTIPLE'
    } catch (error) {
        console.error('페이지 초기화 실패:', error)
        // toast.error('페이지 로딩 중 오류가 발생했습니다.')
        router.push({ name: 'JobtestQuestionList' })
    }
})
</script>

<style scoped>
.question-form-page {
    padding: 24px;
    background-color: #f5f7fa;
    min-height: 100vh;
}

/* 페이지 헤더 */
.page-header {
    margin-bottom: 32px;
    text-align: center;
    position: relative;
}

.header-actions {
    position: absolute;
    top: 0;
    left: 0;
    z-index: 1;
}

.back-btn {
    transition: all 0.2s ease-in-out;
    border-radius: 8px;
    font-weight: 500;
}

.back-btn:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.page-title {
    color: #1a237e;
    font-size: 2rem;
    font-weight: 700;
    margin-bottom: 8px;
    line-height: 1.3;
}

.page-description {
    color: #666;
    font-size: 1rem;
    margin: 0;
}

/* 폼 카드 */
.form-card {
    background: white;
    border-radius: 16px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
    transition: all 0.3s ease;
    border: 1px solid rgba(0, 0, 0, 0.05);
    overflow: hidden;
    position: relative;
}

.form-card::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 4px;
    background: linear-gradient(90deg, #1976d2, #42a5f5);
    border-radius: 16px 16px 0 0;
}

.form-card:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

/* 카드 헤더 */
.card-header {
    display: flex;
    align-items: center;
    gap: 12px;
    margin-bottom: 24px;
    padding-bottom: 16px;
    border-bottom: 1px solid #e0e0e0;
}

.card-title {
    color: #1a237e;
    font-size: 1.25rem;
    font-weight: 600;
    margin: 0;
}

.card-icon {
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

/* 컴포넌트 섹션 */
.component-section {
    margin-top: 24px;
}

/* 액션 버튼 */
.action-buttons {
    display: flex;
    justify-content: center;
    gap: 16px;
    margin-top: 32px;
    padding: 24px;
    background: white;
    border-radius: 16px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.cancel-btn,
.submit-btn {
    min-width: 120px;
    border-radius: 8px;
    font-weight: 500;
    transition: all 0.2s ease;
}

.cancel-btn:hover {
    transform: translateY(-1px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.submit-btn:hover {
    transform: translateY(-1px);
    box-shadow: 0 4px 12px rgba(25, 118, 210, 0.3);
}

/* 반응형 디자인 */
@media (max-width: 960px) {
    .question-form-page {
        padding: 16px;
    }
    
    .page-title {
        font-size: 1.5rem;
    }
    
    .card-header {
        flex-direction: column;
        align-items: flex-start;
        gap: 8px;
    }
    
    .action-buttons {
        flex-direction: column;
        align-items: stretch;
        gap: 12px;
    }
    
    .cancel-btn,
    .submit-btn {
        min-width: auto;
    }
    
    .difficulty-select {
        max-width: none;
    }
}

@media (max-width: 600px) {
    .question-form-page {
        padding: 12px;
    }
    
    .page-title {
        font-size: 1.3rem;
    }
    
    .page-description {
        font-size: 0.9rem;
    }
    
    .form-card {
        border-radius: 12px;
        padding: 20px;
    }
    
    .form-label {
        font-size: 0.9rem;
        margin-bottom: 6px;
    }
    
    .card-title {
        font-size: 1.1rem;
    }
    
    .action-buttons {
        padding: 16px;
        border-radius: 12px;
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