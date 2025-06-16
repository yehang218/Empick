<template>
    <v-container class="pa-6">
        <h2 class="text-h5 font-weight-bold mb-6">받은 결재 목록</h2>
        <v-alert v-if="error" type="error" class="mb-4">{{ error }}</v-alert>
        <v-progress-circular v-if="loading" indeterminate color="primary" class="mb-4" />
        <ListView v-else :headers="headers" :data="pagedList" @item-click="goToDetail" />
        <Pagination v-model="page" :length="totalPages" />
    </v-container>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import { useApprovalStore } from '@/stores/approvalStore';
import { useMemberStore } from '@/stores/memberStore';
import ListView from '@/components/common/ListView.vue';
import Pagination from '@/components/common/Pagination.vue';
import dayjs from 'dayjs';

const approvalStore = useApprovalStore();
const memberStore = useMemberStore();
const router = useRouter();

const receivedList = computed(() => approvalStore.receivedList);
const loading = computed(() => approvalStore.loadingReceived);
const error = computed(() => approvalStore.errorReceived);

const headers = [
    { key: 'approvalId', label: '문서번호' },
    { key: 'categoryName', label: '카테고리' },
    { key: 'writerName', label: '작성자' },
    { key: 'status', label: '상태' },
    { key: 'myApprovalStep', label: '결재순서' },
    { key: 'isMyTurn', label: '결재차례' },
    { key: 'createdAt', label: '작성일' },
];

const itemsPerPage = ref(10);
const page = ref(1);

const pagedList = computed(() => {
    const start = (page.value - 1) * itemsPerPage.value;
    const end = start + itemsPerPage.value;
    return receivedList.value.slice(start, end).map(item => ({
        ...item,
        isMyTurn: item.isMyTurn === 1 ? '내 차례' : '-',
        createdAt: item.createdAt ? dayjs(item.createdAt).format('YYYY-MM-DD HH:mm') : '',
    }));
});

const totalPages = computed(() => Math.ceil(receivedList.value.length / itemsPerPage.value));

function goToDetail(item) {
    if (item && item.approvalId) {
        router.push(`/approval/${item.approvalId}`);
    }
}

onMounted(async () => {
    // 내 정보가 없으면 먼저 불러오기
    if (!memberStore.form.id) {
        await memberStore.getMyInfo();
    }
    if (memberStore.form.id) {
        approvalStore.loadReceivedApprovals(memberStore.form.id);
    }
});
</script>