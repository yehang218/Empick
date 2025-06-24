<template>
    <v-container fluid class="pa-6">
        <!-- 제목 -->
        <h2 class="text-h5 font-weight-bold mb-6">채용 공고 목록</h2>

        <!-- 공고 현황 요약 -->
        <v-row class="mb-4" align="center">
            <v-col cols="12" md="2" class="text-center" @click="selectedStatus = 'ALL'" style="cursor:pointer">
                <div class="text-h6 font-weight-bold" :class="{ 'text-success': selectedStatus === 'ALL' }">{{
                    summary.total }}</div>
                <div class="text-caption">전체 공고</div>
            </v-col>
            <v-col cols="12" md="2" class="text-center" @click="selectedStatus = 'WAITING'" style="cursor:pointer">
                <div class="text-h6 font-weight-bold" :class="{ 'text-success': selectedStatus === 'WAITING' }">{{
                    summary.waiting }}</div>
                <div class="text-caption">대기 중 공고</div>
            </v-col>
            <v-col cols="12" md="2" class="text-center" @click="selectedStatus = 'PUBLISHED'" style="cursor:pointer">
                <div class="text-h6 font-weight-bold" :class="{ 'text-success': selectedStatus === 'PUBLISHED' }">{{
                    summary.ongoing }}</div>
                <div class="text-caption">게시 중 공고</div>
            </v-col>
            <v-col cols="12" md="2" class="text-center" @click="selectedStatus = 'CLOSED'" style="cursor:pointer">
                <div class="text-h6 font-weight-bold" :class="{ 'text-success': selectedStatus === 'CLOSED' }">{{
                    summary.closed }}</div>
                <div class="text-caption">종료 된 공고</div>
            </v-col>
            <v-col cols="12" md="4" class="d-flex justify-end">
                <v-btn color="success" dark @click="goToCreate">+ 채용 공고 작성하기</v-btn>
            </v-col>
        </v-row>

        <!-- 채용 공고 목록 -->
        <v-data-table :headers="headers" :items="pagedRecruitments" :loading="store.loadingList"
            loading-text="채용공고를 불러오는 중입니다..." @click:row="handleRowClick" class="elevation-1 list-item-hover"
            item-value="id" hide-default-footer>
            <template v-slot:item.status="{ item }">
                <v-chip size="small">{{ item.status }}</v-chip>
            </template>
        </v-data-table>
        <div class="d-flex justify-end align-center mt-4 pa-2">
            <span class="text-caption mr-4">Items per page:</span>
            <div style="width: 80px;">
                <v-select v-model="itemsPerPage" :items="[10, 25, 50, 100]" dense hide-details
                    variant="underlined"></v-select>
            </div>

            <span class="text-caption mx-4">{{ pageText }}</span>

            <Pagination :model-value="page" @update:model-value="page = $event" :length="totalPages" />
        </div>
    </v-container>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useToast } from 'vue-toastification'
import { useRecruitmentStore } from '@/stores/recruitmentStore'
import { getRecruitTypeLabel } from '@/constants/employment/recruitTypes'
import { getRecruitStatusLabel } from '@/constants/employment/recruitStatus'
import Pagination from '@/components/common/Pagination.vue';

const router = useRouter()
const route = useRoute()
const toast = useToast()
const itemsPerPage = ref(10)
const page = ref(Number(route.query.page) || 1)
const store = useRecruitmentStore()

const selectedStatus = ref('ALL')

function goToCreate() {
    router.push('/employment/recruitments/create')
}

watch(page, (newPage) => {
    router.replace({ query: { ...route.query, page: newPage } });
});

onMounted(async () => {
    page.value = Number(route.query.page) || 1;

    try {
        await store.loadRecruitmentList();
    } catch (e) {
        toast.error('채용공고 목록 로딩 중 오류가 발생했습니다.');
    }

    if (route.query.toast === 'deleted') {
        toast.success('채용공고가 삭제되었습니다.');
        router.replace({ query: { ...route.query, toast: undefined } });
    }
});

const handleRowClick = (event, { item }) => {
    if (item?.id) {
        router.push({
            path: `/employment/recruitments/${item.id}`,
            query: { page: page.value }
        });
    }
};

const headers = [
    { title: '제목', key: 'title', sortable: true },
    { title: '유형', key: 'recruitType', sortable: true },
    { title: '시작일', key: 'startedAt', sortable: true },
    { title: '마감일', key: 'endedAt', sortable: true },
    { title: '부서', key: 'departmentName', sortable: true },
    { title: '상태', key: 'status', sortable: true },
    { title: '작성자', key: 'memberName', sortable: true }
]

const summary = computed(() => ({
    total: store.list.length,
    waiting: store.list.filter(r => r.status === 'WAITING').length,
    ongoing: store.list.filter(r => r.status === 'PUBLISHED').length,
    closed: store.list.filter(r => r.status === 'CLOSED').length,
}))

const recruitmentsForDisplay = computed(() =>
    store.list
        .filter(r => selectedStatus.value === 'ALL' ? true : r.status === selectedStatus.value)
        .map(r => ({
            ...r,
            recruitType: getRecruitTypeLabel(r.recruitType),
            status: getRecruitStatusLabel(r.status)
        }))
)

const totalPages = computed(() => {
    if (itemsPerPage.value === -1 || recruitmentsForDisplay.value.length === 0) {
        return 1
    }
    return Math.ceil(recruitmentsForDisplay.value.length / itemsPerPage.value)
})

const pagedRecruitments = computed(() => {
    if (itemsPerPage.value === -1) {
        return recruitmentsForDisplay.value;
    }
    const start = (page.value - 1) * itemsPerPage.value
    const end = start + itemsPerPage.value
    return recruitmentsForDisplay.value.slice(start, end)
})

const pageText = computed(() => {
    const totalItems = recruitmentsForDisplay.value.length;
    if (totalItems === 0) return '0-0 of 0';
    const start = (page.value - 1) * itemsPerPage.value + 1;
    const end = Math.min(page.value * itemsPerPage.value, totalItems);
    return `${start}-${end} of ${totalItems}`;
})
</script>

<style scoped>
.list-item-hover :deep(tbody tr:hover) {
    cursor: pointer;
}
</style>