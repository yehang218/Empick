<template>
    <v-container class="pa-6">
        <h2 class="text-h5 font-weight-bold mb-6">요청한 결재 목록</h2>
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

const sentList = computed(() => approvalStore.sentList);
const loading = computed(() => approvalStore.loadingSent);
const error = computed(() => approvalStore.errorSent);

const headers = [
    { key: 'id', label: '문서번호' },
    { key: 'categoryId', label: '카테고리ID' },
    { key: 'writerId', label: '작성자ID' },
    { key: 'status', label: '상태' },
    { key: 'createdAt', label: '작성일' },
];

const itemsPerPage = ref(10);
const page = ref(1);

const pagedList = computed(() => {
    const start = (page.value - 1) * itemsPerPage.value;
    const end = start + itemsPerPage.value;
    // 로그인한 회원의 id와 일치하는 데이터만 필터링
    const filtered = sentList.value.filter(item => item.writerId === memberStore.form.id);
    return filtered.slice(start, end).map(item => ({
        ...item,
        createdAt: item.createdAt ? dayjs(item.createdAt).format('YYYY-MM-DD HH:mm') : '',
    }));
});

const totalPages = computed(() => {
    const filtered = sentList.value.filter(item => item.writerId === memberStore.form.id);
    return Math.ceil(filtered.length / itemsPerPage.value);
});

function goToDetail(item) {
    if (item && item.id) {
        router.push(`/approval/${item.id}`);
    }
}

onMounted(async () => {
    if (!memberStore.form.id) {
        await memberStore.getMyInfo();
    }
    if (memberStore.form.id) {
        await approvalStore.loadSentApprovals(memberStore.form.id);
    }
});
</script>