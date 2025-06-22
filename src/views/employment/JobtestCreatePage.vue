<template>
    <v-container>
        <v-row>
            <v-col cols="12">
                <h2 class="text-h6 font-weight-bold mb-4">실무 테스트 등록</h2>
            </v-col>
        </v-row>
        <v-text-field v-model="jobtestTitle" label="실무 테스트 이름을 입력해주세요." class="mb-0" variant="outlined" />

        <v-card class="pa-6 mb-6" elevation="2">
            <v-row class="align-center" dense>
                <v-col cols="12" md="3" class="mb-4 mb-md-0">
                    <div class="form-label">시험 시작 일정</div>
                    <v-text-field v-model="startedAt" type="datetime-local" variant="outlined" density="comfortable"
                        hide-details />
                </v-col>
                <v-col cols="12" md="3" class="mb-4 mb-md-0">
                    <div class="form-label">시험 종료 일정</div>
                    <v-text-field v-model="endedAt" type="datetime-local" variant="outlined" density="comfortable"
                        hide-details />
                </v-col>
                <v-col cols="12" md="2" class="mb-4 mb-md-0">
                    <div class="form-label">난이도</div>
                    <v-select v-model="difficulty" :items="difficultyOptions" item-title="title" item-value="value"
                        variant="outlined" density="comfortable" hide-details />
                </v-col>
                <v-col cols="12" md="2">
                    <div class="form-label">시험 시간 (분)</div>
                    <v-text-field v-model="testTime" type="number" variant="outlined" density="comfortable"
                        hide-details />
                </v-col>
            </v-row>
        </v-card>
        <div class="table-header-actions">
            <v-row class="align-center" no-gutters>
                <v-col cols="12" md="6" class="d-flex align-center">
                    <h6 class="text-h6 font-weight-bold mb-4 ml-8 mr-4">문제 선택하기</h6>
                    <Search v-model="search" placeholder="문제 검색" @clear="clearSearch" @search="handleSearch"
                        class="search-bar" />
                </v-col>
                <v-col cols="12" md="6" class="d-flex justify-end">
                    <v-btn color="primary" variant="tonal" prepend-icon="mdi-plus" @click="dialog = true">
                        문제 등록하기
                    </v-btn>
                </v-col>
            </v-row>
        </div>

        <!-- 문제 리스트 v-data-table로 교체 -->
        <v-data-table :headers="questionTableHeaders" :items="filteredQuestions" item-key="id" class="elevation-1 mb-4"
            :items-per-page="10" return-object @click:row="handleQuestionRowClick">
            <!-- 선택 체크박스 헤더 -->
            <template #header.select>
                <v-checkbox :model-value="isAllSelected" :indeterminate="isIndeterminate"
                    @update:model-value="toggleSelectAll" hide-details density="compact" />
            </template>
            <!-- 체크박스 컬럼 -->
            <template #item.select="{ item }">
                <v-checkbox :model-value="isSelected(item)" @update:model-value="() => toggle(item)" hide-details
                    density="compact" @click.stop />
            </template>
            <!-- 문제 유형 -->
            <template #item.type="{ item }">
                <v-chip size="small">{{ item.type }}</v-chip>
            </template>
            <!-- 난이도 -->
            <template #item.difficulty="{ item }">
                <v-chip size="small" :color="getDifficultyColor(item.difficulty)" dark>
                    {{ item.difficulty }}
                </v-chip>
            </template>
            <!-- 점수 입력 -->
            <template #item.score="{ item }">
                <v-text-field :model-value="item.score" @update:model-value="val => item.score = Number(val)"
                    type="number" variant="underlined" density="compact" hide-details style="width: 80px;"
                    @click.stop />
            </template>
            <!-- 문제 내용 -->
            <template #item.content="{ item }">
                <span class="truncate-text">{{ item.content }}</span>
            </template>
        </v-data-table>

        <div class="text-right mt-2">
            <span class="text-caption">
                선택된 문제 총점: {{selectedQuestions.reduce((sum, q) => sum + Number(q.score ?? 0), 0)}}점
            </span>
        </div>

        <div class="d-flex justify-end mt-4">
            <v-btn variant="tonal" color="grey" class="mr-2" @click="cancel">취소하기</v-btn>
            <v-btn color="primary" @click="register">{{ isEdit ? '수정하기' : '등록하기' }}</v-btn>
        </div>

        <v-dialog v-model="dialog" max-width="800">
            <v-card>
                <QuestionCreateModal @close="dialog = false" @saved="handleNewQuestion" />
            </v-card>
        </v-dialog>

        <SuccessModal v-if="showSuccessModal" message="실무 테스트가 등록되었습니다." @confirm="handleSuccessConfirm"
            @cancel="showSuccessModal = false" />

        <Modal v-if="showCancelModal" message="정말 취소하시겠습니까?<br>입력한 내용이 모두 사라집니다." @confirm="handleCancelConfirm"
            @cancel="handleCancelClose" />

        <JobtestQuestionDetailModal v-model="detailDialogVisible" :question="selectedQuestionDetail" :showDelete="false"
            :showEdit="false" />
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

const getDifficultyColor = level => {
    switch (level) {
        case '쉬움': return 'success'
        case '보통': return 'primary'
        case '어려움': return 'error'
        default: return 'grey'
    }
}

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

onMounted(async () => {
    await memberStore.getMyInfo();
})
</script>

<style scoped>
.table-header-actions {
    display: flex;
    justify-content: flex-end;
    align-items: center;
    margin-top: 50px;
    margin-bottom: 8px;
    gap: 8px;
}

.form-label {
    font-size: 0.95rem;
    font-weight: 500;
    margin-bottom: 4px;
    color: #555;
}

.truncate-text {
    max-width: 500px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    display: inline-block;
    vertical-align: middle;
}

@media (max-width: 960px) {
    .form-label {
        margin-bottom: 2px;
    }
}

@media (max-width: 600px) {
    .table-header-actions {
        flex-direction: column;
        align-items: stretch;
        gap: 4px;
    }
}

.search-bar {
    margin-bottom: 0;
    margin-left: 16px;
}
</style>
