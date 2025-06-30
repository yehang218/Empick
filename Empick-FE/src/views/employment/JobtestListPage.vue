<template>
    <v-app>
        <v-main>
            <v-container class="pa-6">
                <v-row align="center">
                    <v-col>
                        <h2 class="text-h6 font-weight-bold">실무 테스트 리스트</h2>
                    </v-col>
                    <v-spacer />
                    <v-col class="d-flex justify-end" style="gap: 8px;">
                        <v-btn color="error" variant="outlined" :disabled="selectedJobtests.length === 0"
                            @click="handleDelete">
                            삭제하기 ({{ selectedJobtests.length }}개)
                        </v-btn>
                        <v-btn color="primary" variant="tonal" @click="handleCreate">
                            실무 테스트 등록하기
                        </v-btn>
                    </v-col>
                </v-row>

                <!-- 필터 섹션 -->
                <v-row class="mt-4">
                    <v-col cols="12" md="4">
                        <v-select
                            v-model="assignmentFilter"
                            :items="assignmentFilterOptions"
                            label="할당 상태"
                            variant="outlined"
                            density="compact"
                            clearable
                        ></v-select>
                    </v-col>
                </v-row>

                <v-overlay :model-value="store.loading" class="align-center justify-center">
                    <v-progress-circular indeterminate size="64" color="primary"></v-progress-circular>
                </v-overlay>

                <v-alert v-if="store.error" type="error" class="my-4" closable @click:close="store.error = null">
                    {{ store.error }}
                </v-alert>

                <v-data-table :headers="tableHeaders" :items="filteredJobtests" :items-per-page="10" item-key="id"
                    class="elevation-1 mt-4" return-object @click:row="handleRowClick">

                    <!-- 전체 선택 체크박스 헤더 -->
                    <template #header.select>
                        <v-checkbox :model-value="isAllSelected" :indeterminate="isIndeterminate"
                            @update:model-value="toggleSelectAll" hide-details density="compact" />
                    </template>

                    <!-- 커스텀 체크박스 컬럼 -->
                    <template #item.select="{ item }">
                        <v-checkbox :model-value="isSelected(item)" @update:model-value="toggleSelection(item)"
                            hide-details density="compact" @click.stop />
                    </template>

                    <!-- 제목 (클릭 시 상세, 긴 제목 처리) -->
                    <template #item.title="{ item }">
                        <a href="#" @click.prevent.stop="handleItemClick(item)"
                            class="text-decoration-none text-blue-darken-1 font-weight-medium truncate-text">
                            {{ item.title }}
                        </a>
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

                <v-card-text v-if="selectedJobtests.length > 0" class="text-caption pa-2">
                    <v-chip color="blue" variant="tonal" size="small">
                        {{ selectedJobtests.length }}개 테스트 선택됨
                    </v-chip>
                </v-card-text>

            </v-container>
        </v-main>
    </v-app>
</template>

<script setup>
import { onMounted, ref, computed, watch } from 'vue';
import { useRouter } from 'vue-router';
import { useToast } from 'vue-toastification';
import { useJobtestListStore } from '@/stores/jobtestListStore';
import Pagination from '@/components/common/Pagination.vue'
import { getDifficultyLabel, getDifficultyColors } from '@/constants/employment/difficulty.js'

const router = useRouter();
const store = useJobtestListStore();
const toast = useToast();
const selectedJobtests = ref([]);

// 필터 관련
const assignmentFilter = ref(null);
const assignmentFilterOptions = [
    { title: '할당됨', value: 'assigned' },
    { title: '할당안됨', value: 'unassigned' }
];

// 필터링된 실무테스트 목록
const filteredJobtests = computed(() => {
    let filtered = store.jobtests;
    
    if (assignmentFilter.value) {
        filtered = filtered.filter(jobtest => {
            if (assignmentFilter.value === 'assigned') {
                return jobtest.isAssigned; // 할당됨
            } else if (assignmentFilter.value === 'unassigned') {
                return !jobtest.isAssigned; // 할당안됨
            }
            return true;
        });
    }
    
    return filtered;
});

const tableHeaders = [
    { title: '', key: 'select', sortable: false, width: '50px' },
    { title: '제목', key: 'title', align: 'start' },
    { title: '난이도', key: 'difficulty', align: 'center' },
    { title: '출제자', key: 'createdName', align: 'start' },
    { title: '수정자', key: 'updatedName', align: 'start' }
];

const handleRowClick = (event, { item }) => {
    handleItemClick(item);
};

const handleItemClick = (item) => {
    router.push({ name: 'JobtestDetail', params: { jobtestId: item.id } });
};

const handleCreate = () => {
    router.push({ name: 'JobtestCreate' });
};

const handleDelete = async () => {
    if (selectedJobtests.value.length === 0) {
        toast.warning('삭제할 테스트를 선택해주세요.');
        return;
    }
    if (!confirm(`선택된 ${selectedJobtests.value.length}개의 테스트를 삭제하시겠습니까?`)) return;

    try {
        const idsToDelete = selectedJobtests.value.map(j => j.id);
        await store.deleteJobtestsByIds(idsToDelete);
        toast.success('선택된 테스트가 성공적으로 삭제되었습니다.');
        selectedJobtests.value = []; // 선택 초기화
    } catch (error) {
    }
};

const refreshList = async () => {
    await store.fetchJobtests();
    selectedJobtests.value = [];
};

onMounted(refreshList);

// 필터 변경시 선택 초기화
watch(assignmentFilter, () => {
    selectedJobtests.value = [];
});

// --- v-data-table 선택 관련 로직 ---

const isSelected = (item) => {
    return selectedJobtests.value.some(selected => selected.id === item.id);
}

const toggleSelection = (item) => {
    const isCurrentlySelected = isSelected(item);
    if (isCurrentlySelected) {
        selectedJobtests.value = selectedJobtests.value.filter(selected => selected.id !== item.id);
    } else {
        selectedJobtests.value.push(item);
    }
}

const isAllSelected = computed(() => {
    const totalVisibleItems = filteredJobtests.value.length;
    return totalVisibleItems > 0 && selectedJobtests.value.length === totalVisibleItems;
});

const isIndeterminate = computed(() => {
    return selectedJobtests.value.length > 0 && !isAllSelected.value;
});

const toggleSelectAll = (selectAll) => {
    if (selectAll) {
        selectedJobtests.value = [...filteredJobtests.value];
    } else {
        selectedJobtests.value = [];
    }
}

const getDifficultyStyle = (difficulty) => {
    const colors = getDifficultyColors(difficulty);
    return {
        backgroundColor: colors.background,
        color: colors.text,
    };
};
</script>

<style scoped>
.truncate-text {
    max-width: 400px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    display: inline-block;
    vertical-align: middle;
}

.v-data-table {
    cursor: pointer;
}

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
