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
            <v-btn color="primary" variant="tonal" prepend-icon="mdi-pencil" @click="goEditJobtest">
                수정하기
            </v-btn>
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
                <!-- <template #item.evaluationStatus="{ item }">
                    <span class="status-tag" :style="getStatusStyle(item.evaluationStatus)">
                        {{ getStatusLabel(item.evaluationStatus) }}
                    </span>
                </template> -->

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
import { useApplicationStore } from '@/stores/applicationStore'

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
const applicationStore = useApplicationStore()

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
    { title: '채점 점수', key: 'gradingScore', align: 'center', width: '100px' },
    { title: '채점 상태', key: 'gradingStatus', align: 'center', width: '120px' },
    // { title: '채점자', key: 'gradingMemberName', align: 'start', width: '120px' },
    // { title: '평가 상태', key: 'evaluationStatus', align: 'center', width: '120px' },
    // { title: '평가 점수', key: 'evaluationScore', align: 'center', width: '100px' },
    // { title: '평가자', key: 'evaluationMemberName', align: 'start', width: '120px' }
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
    // 지원자 정보 store에 저장
    if (applicantData) {
        applicationStore.setSelectedJobtestInfo({
            applicantName: applicantData.applicantName,
            recruitmentTitle: applicantData.recruitmentTitle,
            applicantId: applicantData.applicantId,
            applicationId: applicantData.applicationId,
            jobtestTitle: jobtest.value?.title || '실무 테스트',
            submittedAt: applicantData.submittedAt || null
        });
    }
    router.push({ 
        name: 'JobtestAnswerDetail',
        params: { applicationJobtestId }
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
        // console.error('문제 상세 정보를 불러오는 중 오류가 발생했습니다:', err)
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
    min-height: 100vh;
}

.header-actions {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 24px;
}

.list-btn {
    transition: all 0.2s ease-in-out;
    border-radius: 8px;
    font-weight: 500;
}

.list-btn:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.summary-card,
.detail-card {
    background: white;
    border-radius: 16px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
    transition: all 0.3s ease;
    margin-bottom: 24px;
    border: 1px solid rgba(0, 0, 0, 0.05);
    overflow: hidden;
}

.summary-card:hover,
.detail-card:hover {
    transform: translateY(-4px);
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.detail-card {
    position: relative;
}

.detail-card::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 4px;
    background: linear-gradient(90deg, #1976d2, #42a5f5);
    border-radius: 16px 16px 0 0;
}

.truncate-text {
    max-width: 300px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    display: inline-block;
    vertical-align: middle;
    line-height: 1.4;
}

.custom-data-table {
    cursor: pointer;
}

.custom-data-table tbody tr:hover,
.custom-data-table .v-data-table__tr:hover,
.custom-data-table .v-data-table__tr:hover .v-data-table__td {
    background-color: #f8f9fa !important;
    transition: background-color 0.2s ease;
}

.custom-data-table .v-data-table__td:hover {
    background-color: #f8f9fa !important;
}

/* Vuetify 3의 새로운 클래스명 대응 */
.custom-data-table .v-data-table__tr:hover .v-data-table__td {
    background-color: #f8f9fa !important;
}

/* 더 강력한 선택자 */
.custom-data-table :deep(.v-data-table__tr:hover) {
    background-color: #f8f9fa !important;
}

.custom-data-table :deep(.v-data-table__tr:hover .v-data-table__td) {
    background-color: #f8f9fa !important;
}

.question-type-tag,
.difficulty-tag,
.status-tag {
    display: inline-flex;
    align-items: center;
    padding: 6px 12px;
    border-radius: 20px;
    font-size: 0.75rem;
    font-weight: 600;
    text-transform: uppercase;
    letter-spacing: 0.5px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    transition: all 0.2s ease;
    border: 1px solid rgba(255, 255, 255, 0.2);
}

.question-type-tag:hover,
.difficulty-tag:hover,
.status-tag:hover {
    transform: translateY(-1px);
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

/* 테이블 헤더 스타일링 */
.custom-data-table :deep(.v-data-table__thead) {
    background-color: #f8f9fa;
}

.custom-data-table :deep(.v-data-table__thead th) {
    font-weight: 600;
    color: #495057;
    border-bottom: 2px solid #e9ecef;
    padding: 16px 12px;
}

/* 테이블 셀 스타일링 */
.custom-data-table :deep(.v-data-table__td) {
    padding: 16px 12px;
    border-bottom: 1px solid #f1f3f4;
    vertical-align: middle;
}

/* 링크 스타일링 */
a.text-decoration-none {
    color: #1976d2;
    font-weight: 500;
    transition: color 0.2s ease;
}

a.text-decoration-none:hover {
    color: #1565c0;
    text-decoration: underline !important;
}

/* 카드 내부 패딩 조정 */
.detail-card :deep(.v-card-text) {
    padding: 20px 24px;
}

.detail-card :deep(.v-card-title) {
    padding: 20px 24px 16px;
    font-weight: 600;
    color: #1a237e;
    border-bottom: 1px solid #e0e0e0;
    background-color: #fafbfc;
}

/* 페이지네이션 스타일링 */
.detail-card :deep(.v-data-table-footer) {
    padding: 16px 24px;
    border-top: 1px solid #e0e0e0;
    background-color: #fafbfc;
}

/* 반응형 디자인 */
@media (max-width: 960px) {
    .jobtest-detail-page {
        padding: 16px;
    }
    
    .header-actions {
        flex-direction: column;
        gap: 16px;
        align-items: stretch;
    }
    
    .list-btn {
        align-self: flex-start;
    }
    
    .summary-card,
    .detail-card {
        margin-bottom: 16px;
        border-radius: 12px;
    }
    
    .truncate-text {
        max-width: 200px;
    }
    
    .custom-data-table :deep(.v-data-table__thead th),
    .custom-data-table :deep(.v-data-table__td) {
        padding: 12px 8px;
    }
    
    .question-type-tag,
    .difficulty-tag,
    .status-tag {
        padding: 4px 8px;
        font-size: 0.7rem;
    }
}

@media (max-width: 600px) {
    .jobtest-detail-page {
        padding: 12px;
    }
    
    .summary-card,
    .detail-card {
        border-radius: 8px;
    }
    
    .truncate-text {
        max-width: 150px;
    }
    
    .detail-card :deep(.v-card-text) {
        padding: 16px;
    }
    
    .detail-card :deep(.v-card-title) {
        padding: 16px;
        font-size: 1.1rem;
    }
}
</style>
