<template>
    <v-container class="jobtest-create-page">
        <!-- 페이지 헤더 -->
        <div class="page-header">
            <div class="header-actions">
                <v-btn prepend-icon="mdi-arrow-left" variant="tonal" @click="goToList" class="list-btn">
                    목록으로
                </v-btn>
            </div>
            <h2 class="page-title">{{ isEdit ? '실무 테스트 수정' : '실무 테스트 등록' }}</h2>
            <p class="page-description">실무 테스트 정보를 입력하고 문제를 선택해주세요.</p>
        </div>

        <!-- 기본 정보 입력 -->
        <v-card class="info-card pa-6 mb-6" elevation="2">
            <div class="card-header">
                <h3 class="card-title">기본 정보</h3>
                <v-icon class="card-icon">mdi-information-outline</v-icon>
            </div>
            
            <v-text-field 
                v-model="jobtestTitle" 
                label="실무 테스트 이름" 
                placeholder="실무 테스트 이름을 입력해주세요."
                class="mb-4" 
                variant="outlined" 
                prepend-inner-icon="mdi-format-title"
            />

            <v-row class="align-center" dense>
                <v-col cols="12" md="3" class="mb-4 mb-md-0">
                    <div class="form-label">
                        <v-icon class="label-icon">mdi-calendar-start</v-icon>
                        시험 시작 일정
                    </div>
                    <v-text-field 
                        v-model="startedAt" 
                        type="datetime-local" 
                        variant="outlined" 
                        density="comfortable"
                        hide-details 
                    />
                </v-col>
                <v-col cols="12" md="3" class="mb-4 mb-md-0">
                    <div class="form-label">
                        <v-icon class="label-icon">mdi-calendar-end</v-icon>
                        시험 종료 일정
                    </div>
                    <v-text-field 
                        v-model="endedAt" 
                        type="datetime-local" 
                        variant="outlined" 
                        density="comfortable"
                        hide-details 
                    />
                </v-col>
                <v-col cols="12" md="2" class="mb-4 mb-md-0">
                    <div class="form-label">
                        <v-icon class="label-icon">mdi-trending-up</v-icon>
                        난이도
                    </div>
                    <v-select 
                        v-model="difficulty" 
                        :items="difficultyOptions" 
                        item-title="title" 
                        item-value="value"
                        variant="outlined" 
                        density="comfortable" 
                        hide-details 
                    />
                </v-col>
                <v-col cols="12" md="2">
                    <div class="form-label">
                        <v-icon class="label-icon">mdi-clock-outline</v-icon>
                        시험 시간 (분)
                    </div>
                    <v-text-field 
                        v-model="testTime" 
                        type="number" 
                        variant="outlined" 
                        density="comfortable"
                        hide-details 
                    />
                </v-col>
            </v-row>
        </v-card>

        <!-- 문제 선택 섹션 -->
        <v-card class="question-card pa-6 mb-6" elevation="2">
            <div class="card-header">
                <h3 class="card-title">문제 선택</h3>
                <v-icon class="card-icon">mdi-format-list-bulleted</v-icon>
            </div>

            <div class="table-header-actions">
                <v-row class="align-center" no-gutters>
                    <v-col cols="12" md="6" class="d-flex align-center">
                        <Search 
                            v-model="search" 
                            placeholder="문제 검색" 
                            @clear="clearSearch" 
                            @search="handleSearch"
                            class="search-bar" 
                        />
                    </v-col>
                    <v-col cols="12" md="6" class="d-flex justify-end">
                        <v-btn 
                            color="primary" 
                            variant="tonal" 
                            prepend-icon="mdi-plus" 
                            @click="dialog = true"
                            class="add-question-btn"
                        >
                            문제 등록하기
                        </v-btn>
                    </v-col>
                </v-row>
            </div>

            <!-- 문제 리스트 테이블 -->
            <v-data-table 
                :headers="questionTableHeaders" 
                :items="filteredQuestions" 
                item-key="id" 
                class="question-table elevation-1 mb-4"
                :items-per-page="10" 
                return-object 
                @click:row="handleQuestionRowClick"
                hover
            >
                <!-- 선택 체크박스 헤더 -->
                <template #header.select>
                    <v-checkbox 
                        :model-value="isAllSelected" 
                        :indeterminate="isIndeterminate"
                        @update:model-value="toggleSelectAll" 
                        hide-details 
                        density="compact" 
                    />
                </template>
                <!-- 체크박스 컬럼 -->
                <template #item.select="{ item }">
                    <v-checkbox 
                        :model-value="isSelected(item)" 
                        @update:model-value="() => toggle(item)" 
                        hide-details
                        density="compact" 
                        @click.stop 
                    />
                </template>
                <!-- 문제 유형 -->
                <template #item.type="{ item }">
                    <span class="tag-style" :style="getQuestionTypeStyle(item.type)">
                        {{ getQuestionTypeLabel(item.type) }}
                    </span>
                </template>
                <!-- 난이도 -->
                <template #item.difficulty="{ item }">
                    <span class="tag-style" :style="getDifficultyStyle(item.difficulty)">
                        {{ getDifficultyLabel(item.difficulty) }}
                    </span>
                </template>
                <!-- 점수 입력 -->
                <template #item.score="{ item }">
                    <v-text-field 
                        :model-value="item.score" 
                        @update:model-value="val => item.score = Number(val)"
                        type="number" 
                        variant="underlined" 
                        density="compact" 
                        hide-details 
                        style="width: 80px;"
                        @click.stop 
                    />
                </template>
                <!-- 문제 내용 -->
                <template #item.content="{ item }">
                    <span class="truncate-text">{{ item.content }}</span>
                </template>
            </v-data-table>

            <!-- 총점 표시 -->
            <div class="total-score-section">
                <div class="total-score-card">
                    <v-icon class="score-icon">mdi-calculator</v-icon>
                    <span class="score-label">선택된 문제 총점:</span>
                    <span class="score-value">{{selectedQuestions.reduce((sum, q) => sum + Number(q.score ?? 0), 0)}}점</span>
                </div>
            </div>
        </v-card>

        <!-- 액션 버튼 -->
        <div class="action-buttons">
            <v-btn 
                variant="tonal" 
                color="grey" 
                class="cancel-btn" 
                @click="cancel"
                prepend-icon="mdi-close"
            >
                취소하기
            </v-btn>
            <v-btn 
                color="primary" 
                class="submit-btn" 
                @click="register"
                prepend-icon="mdi-check"
            >
                {{ isEdit ? '수정하기' : '등록하기' }}
            </v-btn>
        </div>

        <!-- 모달들 -->
        <v-dialog v-model="dialog" max-width="800">
            <v-card>
                <QuestionCreateModal @close="dialog = false" @saved="handleNewQuestion" />
            </v-card>
        </v-dialog>

        <SuccessModal 
            v-if="showSuccessModal" 
            message="실무 테스트가 등록되었습니다." 
            @confirm="handleSuccessConfirm"
            @cancel="showSuccessModal = false" 
        />

        <Modal 
            v-if="showCancelModal" 
            message="정말 취소하시겠습니까?<br>입력한 내용이 모두 사라집니다." 
            @confirm="handleCancelConfirm"
            @cancel="handleCancelClose" 
        />

        <JobtestQuestionDetailModal 
            v-model="detailDialogVisible" 
            :question="selectedQuestionDetail" 
            :showDelete="false"
            :showEdit="false" 
        />
    </v-container>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useToast } from 'vue-toastification'
import { useJobtestQuestionStore } from '@/stores/jobtestQuestionStore'
import { useJobtestDetailStore } from '@/stores/jobtestDetailStore'
import { useMemberStore } from '@/stores/memberStore'
import { CreateJobtestRequestDTO, UpdateJobtestRequestDTO } from '@/dto/employment/jobtest/jobtestRequestDTO'
import QuestionCreateModal from '@/components/employment/QuestionCreateModal.vue'
import SuccessModal from '@/components/common/AlertModal.vue'
import Modal from '@/components/common/Modal.vue'
import JobtestQuestionDetailModal from '@/components/employment/JobtestQuestionDetailModal.vue'
import Search from '@/components/common/Search.vue'
import { getDifficultyLabel, getDifficultyColors } from '@/constants/employment/difficulty.js'
import { getQuestionTypeLabel, getQuestionTypeColors } from '@/constants/employment/questionTypes.js'

const router = useRouter();
const route = useRoute()
const jobtestQuestionStore = useJobtestQuestionStore()
const jobtestDetailStore = useJobtestDetailStore()
const memberStore = useMemberStore()

const showSuccessModal = ref(false)
const showCancelModal = ref(false)

const localQuestions = ref([])
const toast = useToast()
const jobtestTitle = ref('')
const testTime = ref(0)
const difficulty = ref('')
const dialog = ref(false)
const selectedIds = ref([])
const startedAt = ref('');
const endedAt = ref('');
const detailDialogVisible = ref(false)
const selectedQuestionDetail = ref(null)
const search = ref('')
const isEdit = ref(false)
const editingJobtestId = ref(null)

// v-data-table 헤더 정의
const questionTableHeaders = [
    { title: '', key: 'select', sortable: false, width: '50px' },
    { title: '문제', key: 'content', sortable: true },
    { title: '유형', key: 'type', sortable: true },
    { title: '난이도', key: 'difficulty', sortable: true },
    { title: '점수', key: 'score', sortable: true }
];

const difficultyOptions = [
    { title: '쉬움', value: 'EASY' },
    { title: '보통', value: 'MEDIUM' },
    { title: '어려움', value: 'HARD' }
];

onMounted(async () => {
    await jobtestQuestionStore.fetchQuestions()
    localQuestions.value = jobtestQuestionStore.questions.map(q => ({
        ...q,
        id: Number(q.id)
    }))

    // Edit mode: pre-fill if jobtestId is present
    const jobtestId = route.query.jobtestId
    if (jobtestId) {
        isEdit.value = true
        editingJobtestId.value = Number(jobtestId)
        try {
            await jobtestDetailStore.fetchJobtestDetail(editingJobtestId.value)
            const jobtest = jobtestDetailStore.jobtest
            jobtestTitle.value = jobtest.title
            testTime.value = jobtest.testTime
            difficulty.value = jobtest.difficulty
            startedAt.value = jobtest.startedAt ? new Date(jobtest.startedAt).toISOString().slice(0, 16) : ''
            endedAt.value = jobtest.endedAt ? new Date(jobtest.endedAt).toISOString().slice(0, 16) : ''
            // Set selected questions and scores
            selectedIds.value = jobtest.questions.map(q => q.questionId)
            // Set score for each question in localQuestions
            localQuestions.value = localQuestions.value.map(q => {
                const found = jobtest.questions.find(jq => jq.questionId === q.id)
                return found ? { ...q, score: found.score } : q
            })
        } catch (e) {
            toast.error("데이터를 불러오는데 실패했습니다.")
        }
    }
})

// v-data-table 선택 관련 로직
const isSelected = (item) => selectedIds.value.includes(item.id);
const isAllSelected = computed(() => localQuestions.value.length > 0 && selectedIds.value.length === localQuestions.value.length);
const isIndeterminate = computed(() => selectedIds.value.length > 0 && !isAllSelected.value);
const toggleSelectAll = (selectAll) => {
    if (selectAll) {
        selectedIds.value = localQuestions.value.map(q => q.id);
    } else {
        selectedIds.value = [];
    }
};

// 선택된 문제 목록
const selectedQuestions = computed(() =>
    localQuestions.value.filter(q => selectedIds.value.includes(q.id))
)

const getDifficultyStyle = (difficulty) => {
    const colors = getDifficultyColors(difficulty);
    return {
        backgroundColor: colors.background,
        color: colors.text
    };
};

const getQuestionTypeStyle = (type) => {
    const colors = getQuestionTypeColors(type);
    return {
        backgroundColor: colors.background,
        color: colors.text
    };
};

const toggle = (item) => {
    if (selectedIds.value.includes(item.id)) {
        selectedIds.value = selectedIds.value.filter(x => x !== item.id);
    } else {
        selectedIds.value = [...selectedIds.value, item.id];
    }
};

const register = async () => {
    if (!memberStore.form.id) {
        toast.error('등록자 정보가 없습니다. 다시 로그인해주세요.');
        return;
    }
    if (!jobtestTitle.value) {
        toast.error('실무 테스트 이름을 입력해주세요.')
        return
    }
    if (!testTime.value || testTime.value <= 0) {
        toast.error('시험 시간을 입력해주세요.')
        return
    }
    if (!difficulty.value) {
        toast.error('난이도를 선택해주세요.')
        return
    }
    const selected = localQuestions.value.filter(q => selectedIds.value.includes(q.id))
    const sum = selected.reduce((acc, q) => acc + Number(q.score ?? 0), 0)
    if (sum !== 100) {
        toast.error(`선택된 문제의 총점이 ${sum}점입니다. 정확히 100점이 되도록 입력해주세요.`)
        return
    }
    const invalid = selected.find(q => q.score < 0)
    if (invalid) {
        toast.error(`"${invalid.content}" 문제의 점수가 0보다 작습니다.`)
        return
    }
    if (!startedAt.value) {
        toast.error('시험 시작 일정을 입력해주세요.');
        return;
    }
    if (!endedAt.value) {
        toast.error('시험 종료 일정을 입력해주세요.');
        return;
    }

    try {
        if (isEdit.value && editingJobtestId.value) {
            const updateDto = new UpdateJobtestRequestDTO(
                jobtestTitle.value,
                difficulty.value,
                testTime.value,
                new Date(startedAt.value),
                new Date(endedAt.value),
                memberStore.form.id,
                selected.map(q => ({
                    questionId: q.id,
                    score: Number(q.score ?? 0)
                }))
            )
            await jobtestDetailStore.updateJobtest(editingJobtestId.value, updateDto)
            toast.success('실무 테스트가 수정되었습니다.')
            router.push({ name: 'JobtestList' });
        } else {
            const dto = new CreateJobtestRequestDTO(
                jobtestTitle.value,
                difficulty.value,
                testTime.value,
                new Date(startedAt.value),
                new Date(endedAt.value),
                memberStore.form.id,
                selected.map(q => ({
                    questionId: q.id,
                    score: Number(q.score ?? 0)
                }))
            );
            await jobtestDetailStore.createJobtest(dto)
            showSuccessModal.value = true
        }
    } catch (e) {
        toast.error('등록 중 오류가 발생했습니다.')
    }
}

function handleSuccessConfirm() {
    showSuccessModal.value = false
    router.push({ name: 'JobtestList' });
}

const cancel = () => {
    showCancelModal.value = true
}

function handleCancelConfirm() {
    // 입력 초기화 및 목록 이동
    jobtestTitle.value = ''
    testTime.value = 30
    difficulty.value = null
    selectedIds.value = []
    showCancelModal.value = false
    router.push({ name: 'JobtestList' });
}

function handleCancelClose() {
    showCancelModal.value = false
}

const handleNewQuestion = async () => {
    dialog.value = false;
    await jobtestQuestionStore.fetchQuestions();
    localQuestions.value = jobtestQuestionStore.questions.map(q => ({
        ...q,
        id: Number(q.id)
    }));
};

const handleQuestionRowClick = async (event, { item }) => {
    try {
        await jobtestQuestionStore.loadQuestion(item.id)
        selectedQuestionDetail.value = { ...jobtestQuestionStore.form }
        detailDialogVisible.value = true
    } catch (err) {
        toast.error('문제 상세 정보를 불러오는 중 오류가 발생했습니다.')
    }
}

const filteredQuestions = computed(() => {
    if (!search.value) return localQuestions.value
    const keyword = search.value.toLowerCase()
    return localQuestions.value.filter(q =>
        (q.content && q.content.toLowerCase().includes(keyword)) ||
        (q.type && q.type.toLowerCase().includes(keyword)) ||
        (q.difficulty && q.difficulty.toLowerCase().includes(keyword))
    )
})

const clearSearch = () => {
    search.value = ''
}
const handleSearch = (value) => {
    search.value = value
}

const goToList = () => {
    router.push({ name: 'JobtestList' });
}

onMounted(async () => {
    await memberStore.getMyInfo();
})
</script>

<style scoped>
.jobtest-create-page {
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

.list-btn {
    transition: all 0.2s ease-in-out;
    border-radius: 8px;
    font-weight: 500;
}

.list-btn:hover {
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

/* 카드 공통 스타일 */
.info-card,
.question-card {
    background: white;
    border-radius: 16px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
    transition: all 0.3s ease;
    border: 1px solid rgba(0, 0, 0, 0.05);
    overflow: hidden;
    position: relative;
}

.info-card::before,
.question-card::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 4px;
    background: linear-gradient(90deg, #1976d2, #42a5f5);
    border-radius: 16px 16px 0 0;
}

.info-card:hover,
.question-card:hover {
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

/* 폼 라벨 */
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

/* 테이블 헤더 액션 */
.table-header-actions {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    gap: 16px;
}

.search-bar {
    flex: 1;
    max-width: 400px;
}

.add-question-btn {
    transition: all 0.2s ease;
    border-radius: 8px;
}

.add-question-btn:hover {
    transform: translateY(-1px);
    box-shadow: 0 4px 12px rgba(25, 118, 210, 0.3);
}

/* 문제 테이블 */
.question-table {
    border-radius: 12px;
    overflow: hidden;
}

.question-table :deep(.v-data-table__thead) {
    background-color: #f8f9fa;
}

.question-table :deep(.v-data-table__thead th) {
    font-weight: 600;
    color: #495057;
    border-bottom: 2px solid #e9ecef;
    padding: 16px 12px;
}

.question-table :deep(.v-data-table__td) {
    padding: 16px 12px;
    border-bottom: 1px solid #f1f3f4;
    vertical-align: middle;
}

.question-table :deep(.v-data-table__tr:hover) {
    background-color: #f8f9fa !important;
}

/* 총점 섹션 */
.total-score-section {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
}

.total-score-card {
    display: flex;
    align-items: center;
    gap: 8px;
    background: linear-gradient(135deg, #4caf50, #66bb6a);
    color: white;
    padding: 12px 20px;
    border-radius: 20px;
    font-weight: 600;
    box-shadow: 0 2px 8px rgba(76, 175, 80, 0.3);
    transition: all 0.2s ease;
}

.total-score-card:hover {
    transform: translateY(-1px);
    box-shadow: 0 4px 12px rgba(76, 175, 80, 0.4);
}

.score-icon {
    font-size: 1.2rem;
}

.score-label {
    font-size: 0.9rem;
}

.score-value {
    font-size: 1.1rem;
    font-weight: 700;
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

/* 태그 스타일 */
.tag-style {
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

.tag-style:hover {
    transform: translateY(-1px);
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

/* 텍스트 자르기 */
.truncate-text {
    max-width: 400px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    display: inline-block;
    vertical-align: middle;
    line-height: 1.4;
}

/* 반응형 디자인 */
@media (max-width: 960px) {
    .jobtest-create-page {
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
    
    .table-header-actions {
        flex-direction: column;
        align-items: stretch;
        gap: 12px;
    }
    
    .search-bar {
        max-width: none;
    }
    
    .add-question-btn {
        align-self: flex-end;
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
    
    .truncate-text {
        max-width: 250px;
    }
}

@media (max-width: 600px) {
    .jobtest-create-page {
        padding: 12px;
    }
    
    .page-title {
        font-size: 1.3rem;
    }
    
    .page-description {
        font-size: 0.9rem;
    }
    
    .info-card,
    .question-card {
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
    
    .total-score-card {
        padding: 10px 16px;
        font-size: 0.9rem;
    }
    
    .truncate-text {
        max-width: 200px;
    }
    
    .action-buttons {
        padding: 16px;
        border-radius: 12px;
    }
}
</style>
