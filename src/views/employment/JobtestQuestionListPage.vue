<template>
    <v-app>
        <v-main>
            <v-container class="pa-6">
                <v-row align="center">
                    <v-col>
                        <h2 class="text-h6 font-weight-bold">실무 테스트 문제 리스트</h2>
                    </v-col>
                    <v-spacer />
                    <v-col class="d-flex justify-end" style="gap: 8px;">
                        <v-btn color="error" variant="outlined" :disabled="selectedQuestions.length === 0"
                            @click="handleDelete">
                            삭제하기 ({{ selectedQuestions.length }}개)
                        </v-btn>
                        <v-btn color="primary" variant="tonal" @click="handleCreate">
                            문제 등록하기
                        </v-btn>
                    </v-col>
                </v-row>

                <!-- 로딩 상태 -->
                <v-overlay :model-value="questionStore.loading" class="align-center justify-center">
                    <v-progress-circular indeterminate size="64" color="primary"></v-progress-circular>
                </v-overlay>


                <!-- 에러 메시지 -->
                <v-alert v-if="questionStore.error" type="error" class="my-4" closable
                    @click:close="questionStore.error = null">
                    {{ questionStore.error }}
                </v-alert>

                <!-- 문제 리스트 -->
                <v-data-table :headers="tableHeaders" :items="questionStore.questions" :items-per-page="10"
                    item-key="id" class="elevation-1 mt-4" return-object>

                    <!-- 전체 선택 체크박스 헤더 -->
                    <template #header.select>
                        <v-checkbox :model-value="isAllSelected" :indeterminate="isIndeterminate"
                            @update:model-value="toggleSelectAll" hide-details density="compact" />
                    </template>

                    <!-- 커스텀 체크박스 컬럼 -->
                    <template #item.select="{ item }">
                        <v-checkbox :model-value="isSelected(item)" @update:model-value="toggleSelection(item)"
                            hide-details density="compact" />
                    </template>

                    <!-- 문제 제목 (클릭 시 상세) -->
                    <template #item.content="{ item }">
                        <a href="#" @click.prevent="handleItemClick(item)"
                            class="text-decoration-none text-blue-darken-1 font-weight-medium truncate-text">
                            {{ item.content }}
                        </a>
                    </template>

                    <!-- 유형 -->
                    <template #item.type="{ item }">
                        <span class="type-tag" :style="getQuestionTypeStyle(item.type)">
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
                        <Pagination :model-value="page" :length="pageCount" :total-visible="5" @update:modelValue="setPage" />
                    </template>

                </v-data-table>

                <!-- 선택된 항목 정보 표시 -->
                <v-card-text v-if="selectedQuestions.length > 0" class="text-caption pa-2">
                    <v-chip color="blue" variant="tonal" size="small">
                        {{ selectedQuestions.length }}개 문제 선택됨
                    </v-chip>
                </v-card-text>


                <!-- 문제 상세 보기 -->
                <QuestionDetailModal v-model="detailDialogVisible" :question="selectedQuestionDetail"
                    @update:modelValue="(val) => {
                        detailDialogVisible = val;
                        if (!val) refreshList();
                    }" />

                <Modal v-if="showDeleteModal" message="정말 삭제하시겠습니까?<br>선택한 문제는 복구할 수 없습니다." @confirm="confirmDelete" @cancel="cancelDelete" />
            </v-container>
        </v-main>
    </v-app>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useToast } from 'vue-toastification'

import QuestionDetailModal from '@/components/employment/JobtestQuestionDetailModal.vue'
import { useJobtestQuestionStore } from '@/stores/jobtestQuestionStore'
import Pagination from '@/components/common/Pagination.vue'
import Modal from '@/components/common/Modal.vue'
import { getDifficultyLabel, getDifficultyColors } from '@/constants/employment/difficulty.js'
import { getQuestionTypeLabel, getQuestionTypeColors } from '@/constants/employment/questionTypes.js'

const router = useRouter()
const toast = useToast()
const questionStore = useJobtestQuestionStore()

const detailDialogVisible = ref(false)
const selectedQuestionDetail = ref(null)
const selectedQuestions = ref([])
const showDeleteModal = ref(false)
let pendingDelete = []

const tableHeaders = [
    { title: '', key: 'select', sortable: false, align: 'center', width: '50px' },
    { title: '문제 제목', key: 'content', align: 'start' },
    { title: '유형', key: 'type', align: 'center' },
    { title: '난이도', key: 'difficulty', align: 'center' },
    { title: '출제자', key: 'createdMemberName', align: 'start' },
    { title: '수정자', key: 'updatedMemberName', align: 'start' }
]

const handleItemClick = async (item) => {
    try {
        await questionStore.loadQuestion(item.id)
        selectedQuestionDetail.value = { ...questionStore.form }
        detailDialogVisible.value = true
    } catch (err) {
        // toast.error('문제 상세 정보를 불러오는 중 오류가 발생했습니다.')
    }
}

const handleDelete = () => {
    if (selectedQuestions.value.length === 0) {
        toast.warning('삭제할 문제를 선택해주세요.');
        return;
    }
    pendingDelete = [...selectedQuestions.value]
    showDeleteModal.value = true
}

const confirmDelete = async () => {
    try {
        const questionIds = pendingDelete.map(q => q.id);
        const questionTypes = pendingDelete.map(q => q.type);
        await questionStore.deleteQuestionsByIds(questionIds, questionTypes);
        toast.success('선택된 문제가 성공적으로 삭제되었습니다.');
        selectedQuestions.value = [] // 선택 초기화
    } catch (err) {
        // toast.error('문제 삭제 중 오류가 발생했습니다.');
    } finally {
        showDeleteModal.value = false
        pendingDelete = []
    }
}

const cancelDelete = () => {
    showDeleteModal.value = false
    pendingDelete = []
}

const handleCreate = () => {
    // JobtestQuestionFormPage로 라우팅
    router.push({ name: 'JobtestQuestionCreate' })
}

const refreshList = async () => {
    try {
        await questionStore.fetchQuestions()
        selectedQuestions.value = []
    } catch (error) {
        // toast.error('문제 목록을 새로고침하는 데 실패했습니다.');
    }
}

onMounted(refreshList)

// --- v-data-table 선택 관련 로직 ---

const isSelected = (item) => {
    return selectedQuestions.value.some(selected => selected.id === item.id);
}

const toggleSelection = (item) => {
    const index = selectedQuestions.value.findIndex(selected => selected.id === item.id);
    if (index > -1) {
        selectedQuestions.value.splice(index, 1);
    } else {
        selectedQuestions.value.push(item);
    }
}

const isAllSelected = computed(() => {
    const totalVisibleItems = questionStore.questions.length;
    return totalVisibleItems > 0 && selectedQuestions.value.length === totalVisibleItems;
});

const isIndeterminate = computed(() => {
    return selectedQuestions.value.length > 0 && !isAllSelected.value;
});

const toggleSelectAll = () => {
    if (isAllSelected.value) {
        selectedQuestions.value = [];
    } else {
        selectedQuestions.value = [...questionStore.questions];
    }
}

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
</script>

<style scoped>
.v-data-table {
    margin-top: 16px;
}

.truncate-text {
    max-width: 350px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    display: inline-block;
    vertical-align: top;
    /* 수직 정렬을 위해 추가 */
}

.type-tag,
.difficulty-tag {
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
</style>
