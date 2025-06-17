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
                <v-btn class="mr-2" variant="outlined" color="success">채용 달력보기</v-btn>
                <v-btn color="success" dark @click="goToCreate">+ 채용 공고 작성하기</v-btn>
            </v-col>
        </v-row>

        <!-- 채용 공고 목록 테이블 -->
        <div @click="handleRowClick">
            <ListView :headers="headers" :data="pagedRecruitments" />
            <Pagination v-model="page" :length="totalPages" />
        </div>
    </v-container>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useToast } from 'vue-toastification'
import ListView from '@/components/common/ListView.vue'
import { useRecruitmentStore } from '@/stores/recruitmentStore'
import { getRecruitTypeLabel } from '@/constants/employment/recruitTypes'
import { getRecruitStatusLabel } from '@/constants/employment/recruitStatus'
import Pagination from '@/components/common/Pagination.vue'

const router = useRouter()
const route = useRoute()
const toast = useToast()
const itemsPerPage = ref(10)
const page = ref(1)
const store = useRecruitmentStore()

const selectedStatus = ref('ALL')

function goToCreate() {
    router.push('/employment/recruitments/create')
}

onMounted(() => {
    store.loadRecruitmentList()
    if (route.query.toast === 'deleted') {
        toast.success('채용공고가 삭제되었습니다.')
        router.replace({ query: { ...route.query, toast: undefined } })
    }
})

const handleRowClick = (e) => {
    const tr = e.target.closest('tr');
    if (!tr || !tr.parentElement || tr.parentElement.tagName !== 'TBODY') return;

    const index = tr.rowIndex - 1;

    const item = pagedRecruitments.value[index];
    if (item?.id) {
        router.push(`/employment/recruitments/${item.id}`);
    }
};

const headers = [
    { key: 'title', label: '제목' },
    { key: 'recruitType', label: '유형' },
    { key: 'startedAt', label: '시작일' },
    { key: 'endedAt', label: '마감일' },
    { key: 'departmentName', label: '부서' },
    { key: 'status', label: '상태' },
    { key: 'memberName', label: '작성자' }
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

const pagedRecruitments = computed(() => {
    const start = (page.value - 1) * itemsPerPage.value
    const end = start + itemsPerPage.value
    return recruitmentsForDisplay.value.slice(start, end)
})

const totalPages = computed(() => Math.ceil(recruitmentsForDisplay.value.length / itemsPerPage.value))
</script>

<style scoped>
::v-deep tbody tr {
    cursor: pointer;
}
</style>