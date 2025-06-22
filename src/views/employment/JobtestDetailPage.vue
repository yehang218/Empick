<template>
    <!-- 로딩 중일 때 -->
    <v-container v-if="loading" class="d-flex justify-center align-center" style="height: 300px;">
        <v-progress-circular indeterminate color="primary" size="60" />
    </v-container>

    <!-- 로딩 완료 후 jobtest가 있을 때 -->
    <v-container v-else-if="jobtest" class="jobtest-detail-page">
        <div class="header-actions">
            <v-btn prepend-icon="mdi-arrow-left" variant="tonal" @click="goJobtestList" class="list-btn">
                목록으로
            </v-btn>
            <!-- <v-btn color="primary" variant="tonal" prepend-icon="mdi-pencil" @click="goEditJobtest">
                수정하기
            </v-btn> -->
        </div>

        <!-- 요약 카드 -->
        <jobtest-summary-card :jobtest="jobtest" class="summary-card" />

        <!-- 문제 목록 -->
        <jobtest-question-list :questions="jobtest.questions" class="detail-card" />

        <!-- 지원자 테이블 -->
        <jobtest-applicant-table
            :applications="jobtest.applications"
            @select="goToJobtestAnswers"
            class="detail-card"
        />
    </v-container>

    <!-- 에러 발생 시 -->
    <v-container v-else class="text-center py-10">
        <v-alert type="error" title="오류 발생" prominent>
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

const goEditJobtest = () => {
    router.push({ name: 'JobtestCreate', query: { jobtestId: jobtest.value.id } });
}

const goToJobtestAnswers = (applicationJobtestId) => {
    router.push({ 
        name: 'JobtestAnswerDetail',
        params: { applicationJobtestId }
    });
}
</script>

<style scoped>
.jobtest-detail-page {
    padding: 24px;
    background-color: #f5f7fa;
}

.header-actions {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 24px;
}

.list-btn {
    transition: all 0.2s ease-in-out;
}

.list-btn:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 10px rgba(0,0,0,0.1);
}

.summary-card,
.detail-card {
    background: white;
    border-radius: 12px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
    transition: all 0.3s ease;
    margin-bottom: 24px;
}

.summary-card:hover,
.detail-card:hover {
    transform: translateY(-4px);
    box-shadow: 0 8px 20px rgba(0, 0, 0, 0.12);
}

@media (max-width: 960px) {
    .v-col-md-6 {
        flex-basis: 100%;
        max-width: 100%;
    }
}
</style>
