<template>
    <v-container class="pa-6">
        <h2 class="text-h5 font-weight-bold mb-6">요청한 결재 목록</h2>
        <!-- 상태별 필터: 숫자 강조 -->
        <v-row class="mb-4" align="center">
            <v-col v-for="option in statusOptions" :key="option.key" cols="12" md="2" class="text-center"
                @click="selectedStatus = option.key" style="cursor:pointer">
                <div class="text-h6 font-weight-bold" :class="{ 'text-success': selectedStatus === option.key }"
                    style="font-size: 1.5rem;">
                    {{ option.count }}
                </div>
                <div class="text-caption">{{ option.label }}</div>
            </v-col>
        </v-row>
        <v-alert v-if="error" type="error" class="mb-4">{{ error }}</v-alert>
        <v-progress-circular v-if="loading" indeterminate color="primary" class="mb-4" />
        <ListView v-else :headers="headers" :data="pagedList" @item-click="goToDetail">
            <template #item.status="{ item }">
                <v-chip :color="getApprovalStatusColor(item.status)" text-color="white" small
                    class="font-weight-bold" outlined>
                    {{ item.status }}
                </v-chip>
            </template>
        </ListView>
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
import { getApprovalStatusLabel } from '@/constants/approval/approvalStatus.js';

const approvalStore = useApprovalStore();
const memberStore = useMemberStore();
const router = useRouter();

// 요청한 결재 목록
const requestedList = computed(() => approvalStore.requestedList);
const loading = computed(() => approvalStore.loadingRequested);
const error = computed(() => approvalStore.errorRequested);

const headers = [
    { key: 'approvalId', label: '문서번호' },
    { key: 'categoryName', label: '카테고리명' },
    { key: 'status', label: '상태' },
    { key: 'createdAt', label: '작성일' },
];

const itemsPerPage = ref(10);
const page = ref(1);
const selectedStatus = ref('ALL');

const statusSummary = computed(() => {
    const all = requestedList.value.length;
    const canceled = requestedList.value.filter(i => i.status === 'CANCELED').length;
    const rejected = requestedList.value.filter(i => i.status === 'REJECTED').length;
    const inProgress = requestedList.value.filter(i => i.status === 'IN_PROGRESS').length;
    const approved = requestedList.value.filter(i => i.status === 'APPROVED').length;
    return {
        all,
        canceled,
        rejected,
        inProgress,
        approved
    };
});

const statusOptions = computed(() => [
    { key: 'ALL', label: `전체 결재`, count: statusSummary.value.all },
    { key: 'CANCELED', label: `결재 취소`, count: statusSummary.value.canceled },
    { key: 'REJECTED', label: `반려`, count: statusSummary.value.rejected },
    { key: 'IN_PROGRESS', label: `결재 진행중`, count: statusSummary.value.inProgress },
    { key: 'APPROVED', label: `결재 완료`, count: statusSummary.value.approved }
]);

const filteredList = computed(() => {
    if (selectedStatus.value === 'ALL') return requestedList.value;
    return requestedList.value.filter(item => item.status === selectedStatus.value);
});

const pagedList = computed(() => {
    const start = (page.value - 1) * itemsPerPage.value;
    const end = start + itemsPerPage.value;
    return filteredList.value.slice(start, end).map(item => ({
        ...item,
        status: getApprovalStatusLabel(item.status),
        createdAt: item.createdAt ? dayjs(item.createdAt).format('YYYY-MM-DD HH:mm') : '',
    }));
});

const totalPages = computed(() => {
    return Math.ceil(filteredList.value.length / itemsPerPage.value);
});

function goToDetail(item) {
    if (item && item.approvalId) {
        router.push(`/approval/${item.approvalId}`);
    }
}

function getApprovalStatusColor(status) {
    switch (status) {
        case '결재 취소':
            return 'grey'
        case '반려':
            return 'error'
        case '결재 진행중':
            return 'warning'
        case '결재 완료':
            return 'success'
        default:
            return 'grey'
    }
}

onMounted(async () => {
    if (!memberStore.form.id) {
        await memberStore.getMyInfo();
    }
    if (memberStore.form.id) {
        await approvalStore.loadRequestedApprovals(memberStore.form.id);
    }
});
</script>