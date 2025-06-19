<template>
    <div>
        <InterviewEvaluationInput v-if="criteriaItems.length > 0" v-model:criteria="criteriaItems"
            v-model:totalReview="totalReview" @submit="handleEvaluationSubmit" />

        <!-- 뒤로 가기 버튼 -->
        <v-btn color="primary" class="mt-2" @click="goToInterviewDetailPage">
            뒤로 가기
        </v-btn>
    </div>
</template>


<script setup>
import { watch, computed, onMounted, ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'

import InterviewEvaluationInput from '@/components/employment/InterviewEvaluationInput.vue'
import { useInterviewStore } from '@/stores/interviewStore'
import { useInterviewCriteriaStore } from '@/stores/interviewCriteriaStore'
import { useInterviewScoreStore } from '@/stores/interviewScoreStore'
import { useInterviewerStore } from '@/stores/interviewerStore'
import { useAuthStore } from '@/stores/authStore'



const router = useRouter()
const route = useRoute()

const interviewId = Number(route.params.interviewId)

const interviewStore = useInterviewStore()
const criteriaStore = useInterviewCriteriaStore()
const scoreStore = useInterviewScoreStore()
const interviewerStore = useInterviewerStore()
const authStore = useAuthStore()


const selectedInterview = ref(null)

const criteriaItems = ref([])

const goToInterviewDetailPage = () => {
    const applicationId = selectedInterview.value?.applicationId;
    if (!interviewId) {
        alert('면접 정보가 없습니다.');
        return;
    }

    router.push({ name: 'InterviewDetailPage', params: { applicationId } });
};

const totalReview = ref('')

const fetchAll = async () => {
    try {
        const interview = await interviewStore.fetchInterviewById(interviewId)
        selectedInterview.value = interviewStore.selectedInterview
        const sheetId = interview.sheetId

        await criteriaStore.fetchCriteriaBySheetId(sheetId)
        const criteriaList = criteriaStore.criteriaList

        const interviewerId = authStore.user?.id || 2001
        console.log('interviewerId', interviewerId)
        await interviewerStore.fetchInterviewerById(interviewerId)
        const interviewer = interviewerStore.selectedInterviewer
        console.log('📌 selectedInterviewer:', interviewer)
        totalReview.value = interviewer?.review || ''

        await scoreStore.fetchScoresByInterviewerId(interviewerId)
        const scoreList = scoreStore.scoreList

        criteriaItems.value = criteriaList.map(c => {
            const matchedScore = scoreList.find(s => s.criteriaId === c.id)
            return {
                ...c,
                score: matchedScore?.score ?? null,
                review: matchedScore?.review ?? '',
                existingScoreId: matchedScore?.id ?? null
            }
        })

    } catch (err) {
        console.error('로딩 중 오류:', err)
        alert('평가 데이터를 불러오는 데 실패했습니다.')
    }
}

// ✅ 첫 진입 및 경로 변경 시 다시 fetch
onMounted(fetchAll)

watch(() => route.fullPath, fetchAll)

const handleEvaluationSubmit = async ({ criteria, totalReview }) => {
    const interviewerId = authStore.user?.id || 2001

    for (const item of criteria) {
        const dto = {
            interviewId,
            criteriaId: item.id,
            interviewerId,
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
        await interviewerStore.updateInterviewerReview(interviewerId, totalReview);
        if (!totalReview || totalReview.trim() === '') {
            alert('총평을 입력해 주세요.');
            return;
        }
        alert('평가 저장이 완료되었습니다!')
    } catch (err) {
        console.error('총평 저장 실패:', err)
    }
}
</script>
