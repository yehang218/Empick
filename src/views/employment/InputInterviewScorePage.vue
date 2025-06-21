<template>
    <div>
        <!-- 평가 입력 폼 직접 인라인 -->
        <div class="evaluation-form">
            <div class="all-criteria-wrapper" style="max-height: 500px; overflow-y: auto;">
                <div class="evaluation-box">
                    <div v-for="(item, index) in criteriaItems" :key="index" class="criteria-group">
                        <div class="criteria-info">
                            <div class="left">
                                <h3>{{ index + 1 }}. {{ item.title }}</h3>
                                <p class="question">{{ item.content }}</p>
                            </div>
                            <div class="right">
                                <span class="weight">가중치 <strong>{{ item.weight * 100 }}%</strong></span>
                            </div>
                        </div>
                        <div class="criteria-input">
                            <div class="textarea-wrapper">
                                <textarea v-model="item.review" placeholder="제시된 평가 기준을 바탕으로 지원자를 평가해 주세요."></textarea>
                                <div class="score-overlay">
                                    <input type="number" v-model.number="item.score" min="0" max="100" />
                                    <span>/100</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="total-review-section mt-4">
                <h4>면접 총평</h4>
                <textarea v-model="totalReview" placeholder="면접 전체에 대한 총평을 작성해 주세요." rows="5" class="total-review-textarea"></textarea>
            </div>
            <v-btn class="mt-4" color="primary" @click="handleEvaluationSubmit" :disabled="!isFormValid">
                💾 평가 저장
            </v-btn>
        </div>

        <!-- 뒤로 가기 버튼 -->
        <v-btn color="primary" class="mt-2" @click="goToInterviewDetailPage">
            뒤로 가기
        </v-btn>
    </div>
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
        alert('면접 정보가 없습니다.');
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
        alert('평가 데이터를 불러오는 데 실패했습니다.')
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
            alert('총평을 입력해 주세요.');
            return;
        }
        alert('평가 저장이 완료되었습니다!')
    } catch (err) {
        console.error('총평 저장 실패:', err)
    }
}
</script>

<style scoped>
.evaluation-form {
    max-width: 800px;
    margin: 0 auto;
    padding: 1.5rem;
    font-size: 0.95rem;
}

.all-criteria-wrapper {
    border: 1px solid #ddd;
    border-radius: 8px;
    background-color: #fff;
    padding: 1rem;
    margin-bottom: 2rem;
}

.criteria-group {
    margin-bottom: 2rem;
}

.criteria-info {
    background-color: #f5f5f5;
    border-radius: 8px;
    border: 1px solid #e0e0e0;
    padding: 1rem;
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
}

.criteria-info .left h3 {
    margin: 0;
    font-weight: bold;
}

.criteria-info .left .question {
    margin-top: 4px;
    font-size: 0.9rem;
    color: #444;
}

.criteria-info .right .weight {
    font-size: 0.9rem;
    color: #1a8917;
    background: #eef6ee;
    padding: 4px 8px;
    border-radius: 12px;
}

.criteria-input {
    margin-top: 0.5rem;
    background-color: #fff;
}

.textarea-wrapper {
    position: relative;
}

textarea {
    width: 100%;
    height: 70px;
    padding: 0.5rem;
    padding-right: 90px;
    resize: none;
    border: 1px solid #ccc;
    border-radius: 6px;
    font-size: 0.9rem;
    box-sizing: border-box;
}

.score-overlay {
    position: absolute;
    top: 50%;
    right: 10px;
    transform: translateY(-50%);
    display: flex;
    align-items: center;
    gap: 4px;
    font-size: 0.9rem;
}

.score-overlay input {
    width: 35px;
    height: 24px;
    padding: 2px;
    text-align: center;
    border: 1px solid #ccc;
    border-radius: 4px;
    font-size: 0.9rem;
}

.total-score {
    background-color: #fff;
    border-radius: 6px;
    padding: 1rem;
    border: 1px solid #ddd;
}

.total-header {
    display: flex;
    justify-content: space-between;
    margin-bottom: 0.5rem;
}

.total-body {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
}

.total-body .score-box.total-align-right {
    display: flex;
    justify-content: flex-end;
    align-items: center;
    gap: 4px;
}

.total-body .score-box.total-align-right input {
    width: 35px;
    height: 24px;
    padding: 2px;
    text-align: center;
    border: 1px solid #ccc;
    border-radius: 4px;
    font-size: 0.9rem;
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
</style>
