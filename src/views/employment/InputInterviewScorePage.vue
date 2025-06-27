<template>
    <v-container class="input-interview-score-page" fluid>
        <!-- 헤더 섹션 -->
        <div class="page-header mb-6">
            <div class="header-content">
                <h1 class="page-title">
                    <v-icon class="mr-3" color="primary" size="32">mdi-clipboard-check</v-icon>
                    면접 평가 입력
                </h1>
                <p class="page-subtitle">지원자의 면접 성과를 평가하고 점수를 입력하세요</p>
            </div>
        </div>

        <div class="evaluation-container">
            <!-- 평가 입력 폼 -->
            <v-card class="evaluation-form-card" elevation="0">
                <div class="form-header">
                    <v-icon color="primary" class="mr-2">mdi-criteria</v-icon>
                    <h2 class="form-title">평가 기준별 점수 입력</h2>
                </div>

                <div class="all-criteria-wrapper">
                    <div class="evaluation-box">
                        <div v-for="(item, index) in criteriaItems" :key="index" class="criteria-group">
                            <div class="criteria-info">
                                <div class="left">
                                    <div class="criteria-number">
                                        <span class="number-badge">{{ index + 1 }}</span>
                                    </div>
                                    <div class="criteria-content">
                                        <h3 class="criteria-title">{{ item.title }}</h3>
                                        <p class="criteria-description">{{ item.content }}</p>
                                    </div>
                                </div>
                                <div class="right">
                                    <div class="weight-badge">
                                        <v-icon size="16" class="mr-1">mdi-percent</v-icon>
                                        <span>{{ item.weight * 100 }}%</span>
                                    </div>
                                </div>
                            </div>
                            <div class="criteria-input">
                                <div class="textarea-wrapper">
                                    <textarea 
                                        v-model="item.review" 
                                        placeholder="제시된 평가 기준을 바탕으로 지원자를 평가해 주세요."
                                        class="evaluation-textarea"
                                    ></textarea>
                                    <div class="score-overlay">
                                        <input 
                                            type="number" 
                                            v-model.number="item.score" 
                                            min="0" 
                                            max="100" 
                                            class="score-input"
                                        />
                                        <span class="score-label">/100</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </v-card>

            <!-- 면접 총평 섹션 -->
            <v-card class="total-review-card" elevation="0">
                <div class="form-header">
                    <v-icon color="primary" class="mr-2">mdi-text-box</v-icon>
                    <h2 class="form-title">면접 총평</h2>
                </div>
                <div class="total-review-section">
                    <textarea 
                        v-model="totalReview" 
                        placeholder="면접 전체에 대한 총평을 작성해 주세요." 
                        rows="5" 
                        class="total-review-textarea"
                    ></textarea>
                </div>
            </v-card>

            <!-- 액션 버튼 섹션 -->
            <div class="action-section">
                <v-row>
                    <v-col cols="12" class="d-flex justify-space-between align-center">
                        <v-btn 
                            color="secondary" 
                            variant="outlined"
                            prepend-icon="mdi-arrow-left"
                            @click="goToInterviewDetailPage"
                            class="back-btn"
                        >
                            뒤로 가기
                        </v-btn>
                        
                        <v-btn 
                            color="success" 
                            size="large"
                            @click="handleEvaluationSubmit" 
                            :disabled="!isFormValid"
                            prepend-icon="mdi-content-save"
                            class="submit-btn"
                        >
                            평가 저장
                        </v-btn>
                    </v-col>
                </v-row>
            </div>
        </div>
    </v-container>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'

import { useInterviewStore } from '@/stores/interviewStore'
import { useInterviewCriteriaStore } from '@/stores/interviewCriteriaStore'
import { useInterviewScoreStore } from '@/stores/interviewScoreStore'
import { useInterviewerStore } from '@/stores/interviewerStore'
import { useAuthStore } from '@/stores/authStore'
import { useMemberStore } from '@/stores/memberStore'
import { useToast } from 'vue-toastification'

const toast = useToast()
const router = useRouter()
const route = useRoute()

const interviewId = Number(route.params.interviewId)

const interviewStore = useInterviewStore()
const criteriaStore = useInterviewCriteriaStore()
const scoreStore = useInterviewScoreStore()
const interviewerStore = useInterviewerStore()
const authStore = useAuthStore()
const memberStore = useMemberStore()

const myId = computed(() => memberStore.form.id)

const selectedInterview = ref(null)
const criteriaItems = ref([])
const totalReview = ref('')

const goToInterviewDetailPage = () => {
    const applicationId = selectedInterview.value?.applicationId;
    if (!interviewId) {
        toast.error('면접 정보가 없습니다.');
        return;
    }
    router.push({ name: 'InterviewDetailPage', params: { applicationId } });
};

const fetchAll = async () => {
    try {
        const interview = await interviewStore.fetchInterviewById(interviewId)
        selectedInterview.value = interviewStore.selectedInterview
        const sheetId = interview.sheetId

        await criteriaStore.fetchCriteriaBySheetId(sheetId)
        const criteriaList = criteriaStore.criteriaList

        // 내 id와 interviewId로 interviewer 불러오기, 없으면 생성
        let interviewer = await interviewerStore.fetchInterviewerByInterviewIdAndMemberId(interviewId, myId.value)
        if (!interviewer) {
            await interviewerStore.createInterviewer({ interviewId, memberId: myId.value })
            interviewer = await interviewerStore.fetchInterviewerByInterviewIdAndMemberId(interviewId, myId.value)
        }
        // totalReview 값이 다를 때만 할당
        const newTotalReview = interviewer?.review || ''
        if (totalReview.value !== newTotalReview) {
            totalReview.value = newTotalReview
        }

        // interviewer의 PK(id)로 점수 조회
        await scoreStore.fetchScoresByInterviewerId(interviewer.id)
        const scoreList = scoreStore.scoreList

        // criteriaItems 값이 다를 때만 할당
        const newCriteriaItems = criteriaList.map(c => {
            const matchedScore = scoreList.find(s => s.criteriaId === c.id)
            return {
                ...c,
                score: matchedScore?.score ?? null,
                review: matchedScore?.review ?? '',
                existingScoreId: matchedScore?.id ?? null
            }
        })
        if (JSON.stringify(criteriaItems.value) !== JSON.stringify(newCriteriaItems)) {
            criteriaItems.value = newCriteriaItems
        }
    } catch (err) {
        console.error('로딩 중 오류:', err)
        toast.error('평가 데이터를 불러오는 데 실패했습니다.')
    }
}

onMounted(fetchAll)

const isFormValid = computed(() =>
    criteriaItems.value.every(item => item.score != null && item.review.trim() !== '') &&
    totalReview.value.trim() !== ''
)

const handleEvaluationSubmit = async () => {
    // interviewer의 PK(id)를 사용
    let interviewer = await interviewerStore.fetchInterviewerByInterviewIdAndMemberId(interviewId, myId.value)
    if (!interviewer) {
        await interviewerStore.createInterviewer({ interviewId, memberId: myId.value })
        interviewer = await interviewerStore.fetchInterviewerByInterviewIdAndMemberId(interviewId, myId.value)
    }
    const interviewerPk = interviewer.id
    for (const item of criteriaItems.value) {
        const dto = {
            interviewId,
            criteriaId: item.id,
            interviewerId: interviewerPk,
            score: item.score,
            review: item.review
        }
        try {
            if (item.existingScoreId) {
                await scoreStore.updateScore(item.existingScoreId, dto)
            } else {
                await scoreStore.createScore(dto)
            }
        } catch (err) {
            console.error('점수 저장 실패:', err)
        }
    }
    // 면접 총평 저장
    try {
        await interviewerStore.updateInterviewerReview(interviewerPk, totalReview.value);
        if (!totalReview.value || totalReview.value.trim() === '') {
            toast.error ('총평을 입력해 주세요.');
            return;
        }
        toast.success('평가 저장이 완료되었습니다!')
        router.push({ name: 'InterviewDetailPage', params: { applicationId: selectedInterview.value.applicationId } })
    } catch (err) {
        console.error('총평 저장 실패:', err)
    }
}
</script>

<style scoped>
.input-interview-score-page {
    background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
    min-height: 100vh;
    padding: 2rem 0;
}

.page-header {
    text-align: center;
    margin-bottom: 3rem;
}

.header-content {
    background: rgba(255, 255, 255, 0.95);
    backdrop-filter: blur(10px);
    border-radius: 20px;
    padding: 2rem;
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
    border: 1px solid rgba(255, 255, 255, 0.2);
}

.page-title {
    font-size: 2.5rem;
    font-weight: 700;
    color: #2c3e50;
    margin-bottom: 0.5rem;
    display: flex;
    align-items: center;
    justify-content: center;
}

.page-subtitle {
    font-size: 1.1rem;
    color: #7f8c8d;
    margin: 0;
}

.evaluation-container {
    max-width: 1000px;
    margin: 0 auto;
}

.evaluation-form-card,
.total-review-card {
    background: rgba(255, 255, 255, 0.95);
    backdrop-filter: blur(10px);
    border-radius: 16px;
    padding: 2rem;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
    border: 1px solid rgba(255, 255, 255, 0.2);
    margin-bottom: 2rem;
    transition: all 0.3s ease;
}

.evaluation-form-card:hover,
.total-review-card:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
}

.form-header {
    display: flex;
    align-items: center;
    margin-bottom: 1.5rem;
    padding-bottom: 1rem;
    border-bottom: 2px solid #e8f5e8;
}

.form-title {
    font-size: 1.4rem;
    font-weight: 600;
    color: #2c3e50;
    margin: 0;
}

.all-criteria-wrapper {
    max-height: 600px;
    overflow-y: auto;
    padding-right: 0.5rem;
}

.criteria-group {
    background: white;
    border-radius: 16px;
    padding: 1.5rem;
    margin-bottom: 1.5rem;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
    border: 1px solid rgba(255, 255, 255, 0.2);
    transition: all 0.3s ease;
    position: relative;
    overflow: hidden;
}

.criteria-group::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 4px;
    height: 100%;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.criteria-group:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.criteria-info {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: 1rem;
}

.left {
    display: flex;
    align-items: flex-start;
    gap: 1rem;
    flex: 1;
}

.criteria-number {
    flex-shrink: 0;
}

.number-badge {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    width: 32px;
    height: 32px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    border-radius: 50%;
    font-weight: 700;
    font-size: 0.9rem;
    box-shadow: 0 2px 8px rgba(102, 126, 234, 0.3);
}

.criteria-content {
    flex: 1;
    min-width: 0;
}

.criteria-title {
    font-size: 1.1rem;
    font-weight: 600;
    color: #2c3e50;
    margin: 0 0 0.5rem 0;
    line-height: 1.4;
}

.criteria-description {
    font-size: 0.95rem;
    color: #6c757d;
    line-height: 1.5;
    margin: 0;
}

.right {
    flex-shrink: 0;
}

.weight-badge {
    display: inline-flex;
    align-items: center;
    background: linear-gradient(135deg, #d4edda 0%, #c3e6cb 100%);
    color: #155724;
    padding: 0.5rem 1rem;
    border-radius: 20px;
    font-size: 0.9rem;
    font-weight: 600;
    border: 1px solid #c3e6cb;
}

.criteria-input {
    margin-left: 3rem;
}

.textarea-wrapper {
    position: relative;
}

.evaluation-textarea {
    width: 100%;
    height: 80px;
    padding: 1rem;
    padding-right: 100px;
    resize: none;
    border: 2px solid #e9ecef;
    border-radius: 12px;
    font-size: 0.95rem;
    box-sizing: border-box;
    background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
    transition: all 0.3s ease;
    font-family: inherit;
}

.evaluation-textarea:focus {
    outline: none;
    border-color: #667eea;
    box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
    background: white;
}

.score-overlay {
    position: absolute;
    top: 50%;
    right: 15px;
    transform: translateY(-50%);
    display: flex;
    align-items: center;
    gap: 6px;
    background: white;
    padding: 0.5rem 0.75rem;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.score-input {
    width: 50px;
    height: 32px;
    padding: 0.25rem;
    text-align: center;
    border: 2px solid #e9ecef;
    border-radius: 6px;
    font-size: 0.9rem;
    font-weight: 600;
    color: #2c3e50;
    transition: all 0.3s ease;
}

.score-input:focus {
    outline: none;
    border-color: #667eea;
    box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.1);
}

.score-label {
    font-size: 0.9rem;
    font-weight: 600;
    color: #6c757d;
}

.total-review-section {
    padding: 1rem 0;
}

.total-review-textarea {
    width: 100%;
    padding: 1.5rem;
    border: 2px solid #e9ecef;
    border-radius: 12px;
    font-size: 1rem;
    line-height: 1.6;
    resize: vertical;
    min-height: 120px;
    background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
    transition: all 0.3s ease;
    font-family: inherit;
}

.total-review-textarea:focus {
    outline: none;
    border-color: #667eea;
    box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
    background: white;
}

.action-section {
    background: rgba(255, 255, 255, 0.95);
    backdrop-filter: blur(10px);
    border-radius: 16px;
    padding: 2rem;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
    border: 1px solid rgba(255, 255, 255, 0.2);
    margin-top: 2rem;
}

.back-btn {
    background: linear-gradient(135deg, #424242 0%, #212121 100%) !important;
    color: white !important;
    border: none;
    padding: 1rem 2rem;
    border-radius: 12px;
    font-weight: 600;
    transition: all 0.3s ease;
    display: flex;
    align-items: center;
    justify-content: center;
    line-height: 1;
}

.back-btn:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 25px rgba(66, 66, 66, 0.4);
    color: white !important;
}

.submit-btn {
    background: linear-gradient(135deg, #2e7d32 0%, #388e3c 100%) !important;
    color: white !important;
    border: none;
    padding: 1rem 3rem;
    border-radius: 12px;
    font-weight: 600;
    font-size: 1.1rem;
    transition: all 0.3s ease;
    display: flex;
    align-items: center;
    justify-content: center;
    line-height: 1;
}

.submit-btn:hover:not(:disabled) {
    transform: translateY(-2px);
    box-shadow: 0 8px 25px rgba(46, 125, 50, 0.4);
    color: white !important;
}

.submit-btn:disabled {
    background: linear-gradient(135deg, #6c757d 0%, #495057 100%) !important;
    opacity: 0.6;
    cursor: not-allowed;
    color: white !important;
}

/* number input 스핀 제거 */
input[type="number"]::-webkit-inner-spin-button,
input[type="number"]::-webkit-outer-spin-button {
    -webkit-appearance: none;
    margin: 0;
}

input[type="number"] {
    -moz-appearance: textfield;
}

/* 스크롤바 스타일링 */
.all-criteria-wrapper::-webkit-scrollbar {
    width: 8px;
}

.all-criteria-wrapper::-webkit-scrollbar-track {
    background: #f1f1f1;
    border-radius: 4px;
}

.all-criteria-wrapper::-webkit-scrollbar-thumb {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border-radius: 4px;
}

.all-criteria-wrapper::-webkit-scrollbar-thumb:hover {
    background: linear-gradient(135deg, #5a6fd8 0%, #6a4190 100%);
}

/* 반응형 디자인 */
@media (max-width: 768px) {
    .page-title {
        font-size: 2rem;
        flex-direction: column;
        gap: 1rem;
    }
    
    .criteria-info {
        flex-direction: column;
        gap: 1rem;
    }
    
    .left {
        flex-direction: column;
        gap: 0.75rem;
    }
    
    .criteria-input {
        margin-left: 0;
    }
    
    .action-section .d-flex {
        flex-direction: column;
        gap: 1rem;
    }
    
    .back-btn, .submit-btn {
        width: 100%;
    }
    
    .evaluation-form-card,
    .total-review-card {
        padding: 1.5rem;
    }
}

/* 애니메이션 효과 */
@keyframes fadeInUp {
    from {
        opacity: 0;
        transform: translateY(30px);
    }
    to {
        opacity: 1;
        transform: translateY(0);
    }
}

.evaluation-form-card,
.total-review-card,
.action-section {
    animation: fadeInUp 0.6s ease-out;
}

.evaluation-form-card { animation-delay: 0.1s; }
.total-review-card { animation-delay: 0.2s; }
.action-section { animation-delay: 0.3s; }

.criteria-group {
    animation: fadeInUp 0.5s ease-out;
}

.criteria-group:nth-child(1) { animation-delay: 0.4s; }
.criteria-group:nth-child(2) { animation-delay: 0.5s; }
.criteria-group:nth-child(3) { animation-delay: 0.6s; }
.criteria-group:nth-child(4) { animation-delay: 0.7s; }
.criteria-group:nth-child(5) { animation-delay: 0.8s; }
</style>
