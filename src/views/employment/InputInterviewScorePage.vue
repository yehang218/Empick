<template>
    <div>
        <!-- 평가 입력 컴포넌트 -->
        <InterviewEvaluationInput v-if="criteriaItems.length > 0" v-model:criteria="criteriaItems" />

        <!-- 저장 버튼 -->
        <v-btn color="primary" class="mt-4" @click="saveAll" :disabled="!isFormValid">
            💾 평가 저장
        </v-btn>
    </div>
</template>


<script setup>
import { watch, onMounted, ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'

import InterviewEvaluationInput from '@/components/employment/InterviewEvaluationInput.vue'
import { useInterviewStore } from '@/stores/interviewStore'
import { useInterviewCriteriaStore } from '@/stores/interviewCriteriaStore'
import { useInterviewScoreStore } from '@/stores/interviewScoreStore'
import { useAuthStore } from '@/stores/authStore'

const interviewId = Number(route.params.interviewId)

const router = useRouter()
const route = useRoute()

const interviewStore = useInterviewStore()
const criteriaStore = useInterviewCriteriaStore()
const scoreStore = useInterviewScoreStore()
const authStore = useAuthStore()

const criteriaItems = ref([])

const isFormValid = computed(() =>
    criteriaItems.value.every(item =>
        item.score != null && item.comment.trim() !== ''
    )
)

const fetchAll = async () => {
    try {
        const interview = await interviewStore.fetchInterviewById(interviewId)
        const sheetId = interview.sheetId

        await criteriaStore.fetchCriteriaBySheetId(sheetId)
        const criteriaList = criteriaStore.criteriaList

        const interviewerId = authStore.user?.id || 2000
        await scoreStore.fetchScoresByInterviewerId(interviewerId)
        const scoreList = scoreStore.scoreList

        criteriaItems.value = criteriaList.map(c => {
            const matchedScore = scoreList.find(s => s.criteriaId === c.id)
            return {
                ...c,
                score: matchedScore?.score ?? null,
                comment: matchedScore?.review ?? '',
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

const saveAll = async () => {
    const interviewerId = authStore.user?.id || 2000
    const invalidItems = []

    for (const item of criteriaItems.value) {
        if (item.score == null || item.comment.trim() === '') {
            invalidItems.push(item.title)
            continue
        }

        const dto = {
            interviewId,
            criteriaId: item.id,
            interviewerId,
            score: item.score,
            review: item.comment
        }

        try {
            if (item.existingScoreId) {
                await scoreStore.updateScore(item.existingScoreId, dto)
            } else {
                await scoreStore.createScore(dto)
            }
        } catch (err) {
            console.error('저장 실패:', err)
        }
    }

    if (invalidItems.length > 0) {
        alert(`다음 항목은 점수와 리뷰를 입력해야 저장됩니다:\n- ${invalidItems.join('\n- ')}`)
        return
    }

    alert("점수 등록이 완료되었습니다!")
}
</script>
