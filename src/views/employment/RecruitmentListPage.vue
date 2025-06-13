<template>
    <v-container fluid class="pa-6">
        <!-- 제목 -->
        <h2 class="text-h5 font-weight-bold mb-6">채용 공고 목록</h2>

        <!-- 공고 현황 요약 -->
        <v-row class="mb-4" align="center">
            <v-col cols="12" md="2" class="text-center">
                <div class="text-h6 font-weight-bold">{{ summary.waiting }}</div>
                <div class="text-caption">대기 중 공고</div>
            </v-col>
            <v-col cols="12" md="2" class="text-center">
                <div class="text-h6 font-weight-bold">{{ summary.ongoing }}</div>
                <div class="text-caption">게시 중 공고</div>
            </v-col>
            <v-col cols="12" md="2" class="text-center">
                <div class="text-h6 font-weight-bold">{{ summary.totalApplicants }}</div>
                <div class="text-caption">전체 지원자 수</div>
            </v-col>
            <v-col cols="12" md="6" class="d-flex justify-end">
                <v-btn class="mr-2" variant="outlined" color="primary">채용 달력보기</v-btn>
                <v-btn color="green-darken-2" dark @click="goToCreate">+ 채용 공고 작성하기</v-btn>
            </v-col>
        </v-row>

        <!-- 채용 공고 목록 테이블 -->
        <ListView :headers="headers" :data="recruitmentsForDisplay" @click:row="goToDetail" />

        <!-- 페이지네이션 -->
        <div class="d-flex justify-center mt-4">
            <v-pagination v-model="page" :length="totalPages" />
        </div>
    </v-container>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import ListView from '@/components/common/ListView.vue'
import { useRecruitmentStore } from '@/stores/recruitmentStore'
import { getRecruitTypeLabel } from '@/constants/employment/recruitTypes'
import { getRecruitStatusLabel } from '@/constants/employment/recruitStatus'

const router = useRouter()
const page = ref(1)
const totalPages = ref(1)
const store = useRecruitmentStore()

function goToCreate() {
    router.push('/employment/recruitments/create')
}

function goToDetail(item) {
    router.push(`/employment/recruitments/${item.id}`)
}

onMounted(() => {
    store.loadRecruitmentList()
})

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
    waiting: store.list.filter(r => r.status === 'WAITING').length,
    ongoing: store.list.filter(r => r.status === 'PUBLISHED').length,
    totalApplicants: 150 // TODO: 실제 API 연결 시 수정
}))

const recruitmentsForDisplay = computed(() =>
    store.list.map(r => ({
        ...r,
        recruitType: getRecruitTypeLabel(r.recruitType),
        status: getRecruitStatusLabel(r.status)
    }))
)
</script>