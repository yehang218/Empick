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
        <v-card variant="outlined" class="detail-card pa-4">
            <h3 class="text-subtitle-1 font-weight-bold mb-4">문제 목록</h3>
            <v-data-table :headers="questionHeaders" :items="validQuestions" :items-per-page="10" item-key="id"
                class="elevation-1 custom-data-table" hover @click:row="handleQuestionRowClick">
                <!-- 번호 -->
                <template #item.index="{ index }">
                    {{ index + 1 }}
                </template>

                <!-- 내용 (긴 내용 처리) -->
                <template #item.content="{ item }">
                    <span class="truncate-text">{{ item.content }}</span>
                </template>

                <!-- 유형 -->
                <template #item.type="{ item }">
                    <span class="question-type-tag" :style="getQuestionTypeStyle(item.type)">
                        {{ getQuestionTypeLabel(item.type) }}
                    </span>
                </template>

                <!-- 난이도 -->
                <template #item.difficulty="{ item }">
                    <span class="difficulty-tag" :style="getDifficultyStyle(item.difficulty)">
                        {{ getDifficultyLabel(item.difficulty) }}
                    </span>
                </template>

                <template #footer="{ page, pageCount, setPage }">
                    <Pagination :model-value="page" :length="pageCount" :total-visible="5"
                        @update:modelValue="setPage" />
                </template>
            </v-data-table>
        </v-card>

        <!-- 지원자 테이블 -->
        <v-card variant="outlined" class="detail-card pa-4">
            <h3 class="text-subtitle-1 font-weight-bold mb-4">지원자 현황</h3>
            <v-data-table :headers="applicantHeaders" :items="jobtest.applications" :items-per-page="10"
                item-key="applicationJobtestId" class="elevation-1 custom-data-table" hover
                @click:row="handleApplicantRowClick">
                <!-- 번호 -->
                <template #item.index="{ index }">
                    {{ index + 1 }}
                </template>

                <!-- 지원자 (클릭 가능) -->
                <template #item.applicantName="{ item }">
                    <a href="#" @click.prevent.stop="handleApplicantClick(item)"
                        class="text-decoration-none text-blue-darken-1 font-weight-medium">
                        {{ item.applicantName }}
                    </a>
                </template>

                <!-- 채용 공고 (긴 제목 처리) -->
                <template #item.recruitmentTitle="{ item }">
                    <span class="truncate-text">{{ item.recruitmentTitle }}</span>
                </template>

                <!-- 채점 상태 -->
                <template #item.gradingStatus="{ item }">
                    <span class="status-tag" :style="getStatusStyle(item.gradingStatus)">
                        {{ getStatusLabel(item.gradingStatus) }}
                    </span>
                </template>

                <!-- 평가 상태 -->
                <template #item.evaluationStatus="{ item }">
                    <span class="status-tag" :style="getStatusStyle(item.evaluationStatus)">
                        {{ getStatusLabel(item.evaluationStatus) }}
                    </span>
                </template>

                <template #footer="{ page, pageCount, setPage }">
                    <Pagination :model-value="page" :length="pageCount" :total-visible="5"
                        @update:modelValue="setPage" />
                </template>
            </v-data-table>
        </v-card>

        <!-- 문제 상세 보기 모달 -->
        <QuestionDetailModal v-model="detailDialogVisible" :question="selectedQuestionDetail" :show-delete="false"
            :show-edit="false" @update:modelValue="(val) => {
                detailDialogVisible = val;
            }" />
    </v-container>

    <!-- 에러 발생 시 -->
    <v-container v-else class="text-center py-10">
        <v-alert type="error" title="오류 발생" prominent>
            실무 테스트 정보를 불러오는 중 문제가 발생했습니다.
        </v-alert>
    </v-container>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useJobtestDetailStore } from '@/stores/jobtestDetailStore'
import { useJobtestQuestionStore } from '@/stores/jobtestQuestionStore'

import JobtestSummaryCard from '@/components/employment/JobtestSummaryCard.vue'
import QuestionDetailModal from '@/components/employment/JobtestQuestionDetailModal.vue'
import Pagination from '@/components/common/Pagination.vue'
import { getDifficultyLabel, getDifficultyColors } from '@/constants/employment/difficulty.js'
import { getQuestionTypeLabel, getQuestionTypeColors } from '@/constants/employment/questionTypes.js'
import { getStatusLabel } from '@/constants/employment/jobtestStatus.js'

const router = useRouter()
const props = defineProps(['jobtestId'])

const jobtestDetailStore = useJobtestDetailStore()
const jobtestQuestionStore = useJobtestQuestionStore()

// 모달 관련 상태
const detailDialogVisible = ref(false)
const selectedQuestionDetail = ref(null)

onMounted(() => {
    jobtestDetailStore.fetchJobtestDetail(Number(props.jobtestId))
})

const jobtest = computed(() => jobtestDetailStore.jobtest)
const loading = computed(() => jobtestDetailStore.loading)
const error = computed(() => jobtestDetailStore.error)

// 문제 목록 테이블 헤더
const questionHeaders = [
    { title: '번호', key: 'index', sortable: false, width: '80px' },
    { title: '내용', key: 'content', align: 'start' },
    { title: '배점', key: 'score', align: 'center', width: '100px' },
    { title: '유형', key: 'type', align: 'center', width: '120px' },
    { title: '난이도', key: 'difficulty', align: 'center', width: '120px' }
]

// 지원자 테이블 헤더
const applicantHeaders = [
    { title: 'No', key: 'index', sortable: false, width: '60px' },
    { title: '지원자', key: 'applicantName', align: 'start' },
    { title: '채용 공고', key: 'recruitmentTitle', align: 'start' },
    { title: '채점 상태', key: 'gradingStatus', align: 'center', width: '120px' },
    { title: '채점 점수', key: 'gradingScore', align: 'center', width: '100px' },
    { title: '채점자', key: 'gradingMemberName', align: 'start', width: '120px' },
    { title: '평가 상태', key: 'evaluationStatus', align: 'center', width: '120px' },
    { title: '평가 점수', key: 'evaluationScore', align: 'center', width: '100px' },
    { title: '평가자', key: 'evaluationMemberName', align: 'start', width: '120px' }
]

// 유효한 문제들만 필터링
const validQuestions = computed(() => {
    return (jobtest.value?.questions || []).filter(q => q);
})

const goJobtestList = () => {
    router.push({ name: 'JobtestList' });
}

const goEditJobtest = () => {
    router.push({ name: 'JobtestCreate', query: { jobtestId: jobtest.value.id } });
}

const goToJobtestAnswers = (applicationJobtestId, applicantData = null) => {
    const query = {};
    
    // 지원자 정보가 있으면 쿼리 파라미터로 전달
    if (applicantData) {
        query.applicantName = applicantData.applicantName;
        query.recruitmentTitle = applicantData.recruitmentTitle;
        query.applicantId = applicantData.applicantId;
        query.applicationId = applicantData.applicationId;
    }
    
    // 실무테스트 제목도 함께 전달
    if (jobtest.value?.title) {
        query.jobtestTitle = jobtest.value.title;
    }
    
    router.push({ 
        name: 'JobtestAnswerDetail',
        params: { applicationJobtestId },
        query
    });
}

// 문제 행 클릭 핸들러
const handleQuestionRowClick = async (event, { item }) => {
    try {
        console.log('item', item)
        await jobtestQuestionStore.loadQuestion(item.questionId)
        selectedQuestionDetail.value = { ...jobtestQuestionStore.form }
        detailDialogVisible.value = true
    } catch (err) {
        console.error('문제 상세 정보를 불러오는 중 오류가 발생했습니다:', err)
    }
}

// 지원자 행 클릭 핸들러
const handleApplicantRowClick = (event, { item }) => {
    goToJobtestAnswers(item.applicationJobtestId, item);
}

// 지원자 이름 클릭 핸들러
const handleApplicantClick = (item) => {
    goToJobtestAnswers(item.applicationJobtestId, item);
}

// 스타일 함수들
const getQuestionTypeStyle = (type) => {
    const colors = getQuestionTypeColors(type);
    return {
        backgroundColor: colors.background,
        color: colors.text,
    };
};

const getDifficultyStyle = (difficulty) => {
    const colors = getDifficultyColors(difficulty);
    return {
        backgroundColor: colors.background,
        color: colors.text,
    };
};

const getStatusStyle = (status) => {
    // 상태에 따른 색상 매핑
    const statusColors = {
        'PENDING': { background: '#fff3cd', text: '#856404' },
        'IN_PROGRESS': { background: '#cce5ff', text: '#004085' },
        'COMPLETED': { background: '#d4edda', text: '#155724' },
        'FAILED': { background: '#f8d7da', text: '#721c24' }
    };

    const colors = statusColors[status] || { background: '#f8f9fa', text: '#6c757d' };
    return {
        backgroundColor: colors.background,
        color: colors.text,
    };
};
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
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
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

.truncate-text {
    max-width: 300px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    display: inline-block;
    vertical-align: middle;
}

.v-data-table {
    cursor: pointer;
}

.custom-data-table {
    cursor: pointer;
}

.custom-data-table tbody tr:hover,
.custom-data-table .v-data-table__tr:hover,
.custom-data-table .v-data-table__tr:hover .v-data-table__td {
    background-color: #f5f5f5 !important;
    transition: background-color 0.2s ease;
}

.custom-data-table .v-data-table__td:hover {
    background-color: #f5f5f5 !important;
}

/* Vuetify 3의 새로운 클래스명 대응 */
.custom-data-table .v-data-table__tr:hover .v-data-table__td {
    background-color: #f5f5f5 !important;
}

/* 더 강력한 선택자 */
.custom-data-table :deep(.v-data-table__tr:hover) {
    background-color: #f5f5f5 !important;
}

.custom-data-table :deep(.v-data-table__tr:hover .v-data-table__td) {
    background-color: #f5f5f5 !important;
}

.question-type-tag,
.difficulty-tag,
.status-tag {
    display: inline-flex;
    align-items: center;
    padding: 4px 8px;
    border-radius: 12px;
    font-size: 0.75rem;
    font-weight: 600;
    text-transform: uppercase;
    letter-spacing: 0.3px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
    transition: all 0.2s ease;
}

@media (max-width: 960px) {
    .v-col-md-6 {
        flex-basis: 100%;
        max-width: 100%;
    }
}
</style>
