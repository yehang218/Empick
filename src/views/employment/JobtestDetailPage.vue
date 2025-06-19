<template>
    <!-- 로딩 중일 때 -->
    <v-container v-if="loading" class="d-flex justify-center align-center" style="height: 300px;">
        <v-progress-circular indeterminate color="primary" size="60" />
    </v-container>

    <!-- 로딩 완료 후 jobtest가 있을 때 -->
    <v-container v-else-if="jobtest" fluid>
        <v-btn prepend-icon="mdi-arrow-left" variant="tonal" class="mb-4" @click="goJobtestList">
            목록으로
        </v-btn>

        <!-- 요약 카드 -->
        <jobtest-summary-card :jobtest="jobtest" class="mb-6" />

        <v-row>
            <v-col cols="6">
                <jobtest-question-list :questions="jobtest.questions" />
            </v-col>
            <!-- <v-col cols="6">
        <jobtest-evaluation-criteria :criteria="jobtest.evaluationCriteria" />
      </v-col> -->
        </v-row>

        <!-- 지원자 테이블 -->
        <jobtest-applicant-table
            :applications="jobtest.applications"
            @select="goToJobtestAnswers"
        />
    </v-container>

    <!-- 에러 발생 시 -->
    <v-container v-else class="text-center py-10">
        <v-alert type="error" title="오류 발생">
            실무 테스트 정보를 불러오는 중 문제가 발생했습니다.
        </v-alert>
    </v-container>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useJobtestDetailStore } from '@/stores/jobtestDetailStore'

import JobtestSummaryCard from '@/components/employment/JobtestSummaryCard.vue'
import JobtestQuestionList from '@/components/employment/JobtestQuestionList.vue'
// import JobtestEvaluationCriteria from '@/components/common/EvaluationCriteriaList.vue'
import JobtestApplicantTable from '@/components/employment/JobtestApplicantTable.vue'

const router = useRouter()
const props = defineProps(['jobtestId'])

const jobtestDetailStore = useJobtestDetailStore()

onMounted(() => {
    jobtestDetailStore.fetchJobtestDetail(Number(props.jobtestId))
})

const jobtest = computed(() => jobtestDetailStore.jobtest)
const loading = computed(() => jobtestDetailStore.loading)
const error = computed(() => jobtestDetailStore.error)

const goJobtestList = () => {
    router.push({ name: 'JobtestList' });
}

const goToJobtestAnswers = (applicationJobtestId) => {
    router.push({ 
        name: 'JobtestAnswerDetail',
        params: { applicationJobtestId }
    });
}
</script>
