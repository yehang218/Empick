<template>
    <v-container fluid class="pa-4">
        <v-row justify="center">
            <v-col cols="12">
                <v-card class="pa-6" flat>

                    <!-- 상단 타이틀 + 작성 버튼 -->
                    <div class="d-flex justify-space-between align-center mb-4">
                        <h2 class="text-h5 font-weight-bold mb-6">채용 요청서 목록</h2>
                        <!-- <v-btn color="success" class="text-white" @click="goToCreate" prepend-icon="mdi-plus">
                            채용 요청서 작성하기
                        </v-btn> -->
                    </div>

                    <v-data-table
                        :headers="headers"
                        :items="pagedList"
                        @click:row="handleRowClick"
                        class="elevation-1 list-item-hover"
                        item-value="id"
                        hide-default-footer
                    />
                    <div class="d-flex justify-end align-center mt-4 pa-2">
                        <span class="text-caption mr-4">Items per page:</span>
                        <div style="width: 80px;">
                            <v-select
                                v-model="itemsPerPage"
                                :items="[10, 25, 50, 100]"
                                dense
                                hide-details
                                variant="underlined"
                            ></v-select>
                        </div>

                        <span class="text-caption mx-4">{{ pageText }}</span>

                        <Pagination
                            :model-value="page"
                            @update:model-value="page = $event"
                            :length="totalPages"
                        />
                    </div>
                </v-card>
            </v-col>
        </v-row>
    </v-container>
</template>

<script setup>
import { onMounted, computed, ref, watch } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useRecruitmentRequestStore } from '@/stores/recruitmentRequestStore';
import Pagination from '@/components/common/Pagination.vue';
import dayjs from 'dayjs';

const store = useRecruitmentRequestStore();
const router = useRouter();
const route = useRoute();

const itemsPerPage = ref(10)
const page = ref(Number(route.query.page) || 1)

onMounted(() => {
    store.loadRecruitmentRequestList();
});

watch(
    () => route.query.page,
    (val) => {
        page.value = Number(val) || 1;
    }
);

watch(page, (val) => {
    if (Number(route.query.page) !== val) {
        router.replace({ query: { ...route.query, page: val } });
    }
});

const goToCreate = () => {
    router.push('/employment/recruitment-requests/create');
};

// 행 클릭 시 상세 페이지로 이동
const handleRowClick = (event, { item }) => {
    if (item?.id) {
        router.push({
            path: `/employment/recruitment-requests/${item.id}`,
            query: { page: page.value }
        });
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

const totalPages = computed(() => {
    return Math.ceil(formattedList.value.length / itemsPerPage.value)
})

const pagedList = computed(() => {
    const start = (page.value - 1) * itemsPerPage.value
    const end = start + itemsPerPage.value
    return formattedList.value.slice(start, end)
})

const pageText = computed(() => {
    const totalItems = formattedList.value.length;
    if (totalItems === 0) return '0-0 of 0';
    const start = (page.value - 1) * itemsPerPage.value + 1;
    const end = Math.min(page.value * itemsPerPage.value, totalItems);
    return `${start}-${end} of ${totalItems}`;
})


// 테이블 헤더 정의
const headers = [
    { title: '포지션명', key: 'jobName', sortable: true },
    { title: '인원', key: 'headcount', sortable: true },
    { title: '요청일', key: 'createdAt', sortable: true },
    { title: '부서', key: 'departmentName', sortable: true },
    { title: '작성자', key: 'memberName', sortable: true }
];
</script>

<style scoped>
.list-item-hover :deep(tbody tr:hover) {
    cursor: pointer;
}
</style>