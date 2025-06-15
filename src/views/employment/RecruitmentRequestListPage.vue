<template>
    <v-container fluid class="pa-4">
        <v-row justify="center">
            <v-col cols="12">
                <v-card class="pa-6" flat>

                    <!-- 상단 타이틀 + 작성 버튼 -->
                    <div class="d-flex justify-space-between align-center mb-4">
                        <h2 class="text-h5 font-weight-bold mb-6">채용 요청서 목록</h2>
                        <v-btn color="success" class="text-white" @click="goToCreate" prepend-icon="mdi-plus">
                            채용 요청서 작성하기
                        </v-btn>
                    </div>

                    <!-- 리스트 클릭 이벤트 감지용 래퍼 div -->
                    <div @click="handleRowClick">
                        <ListView :headers="headers" :data="pagedList" />
                    </div>

                    <Pagination v-model="page" :length="totalPages" />

                </v-card>
            </v-col>
        </v-row>
    </v-container>
</template>

<script setup>
import { onMounted, computed, ref } from 'vue';
import { useRouter } from 'vue-router';
import { useRecruitmentRequestStore } from '@/stores/recruitmentRequestStore';
import ListView from '@/components/common/ListView.vue';
import dayjs from 'dayjs';
import Pagination from '@/components/common/Pagination.vue'

const store = useRecruitmentRequestStore();
const router = useRouter();

const itemsPerPage = ref(10)
const page = ref(1)

onMounted(() => {
    store.loadRecruitmentRequestList();
});

const goToCreate = () => {
    router.push('/employment/recruitment-requests/create');
};

// 행 클릭 시 상세 페이지로 이동
const handleRowClick = (e) => {
    const tr = e.target.closest('tr');
    if (!tr || tr.rowIndex === 0) return; // 헤더 제외

    const index = tr.rowIndex - 1;
    const item = formattedList.value[index];
    if (item?.id) {
        router.push(`/employment/recruitment-requests/${item.id}`);
    }
};

// 데이터 포맷 처리
const formattedList = computed(() =>
    store.recruitmentRequestList.map(item => ({
        ...item,
        headcount: `${item.headcount} 명`,
        createdAt: dayjs(item.createdAt).format('YYYY-MM-DD HH:mm:ss'),
    }))
);

const pagedList = computed(() => {
    const start = (page.value - 1) * itemsPerPage.value
    const end = start + itemsPerPage.value
    return formattedList.value.slice(start, end)
})

const totalPages = computed(() => Math.ceil(formattedList.value.length / itemsPerPage.value))

// 테이블 헤더 정의
const headers = [
    { key: 'jobName', label: '포지션명' },
    { key: 'headcount', label: '인원' },
    { key: 'createdAt', label: '요청일' },
    { key: 'departmentName', label: '부서' },
    { key: 'memberName', label: '작성자' }
];
</script>

<style scoped>
::v-deep tbody tr {
    cursor: pointer;
}
</style>