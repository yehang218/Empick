<template>
    <v-container class="pa-6">
        <h2 class="text-h5 font-weight-bold mb-6">받은 결재 목록</h2>
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
        <ListView v-else :headers="headers" :data="filteredList" :itemsPerPage="itemsPerPage" :page="page"
            @update:page="page = $event" @item-click="goToDetail">
            <template #item.myApprovalStatus="{ item }">
                <v-chip :color="getMyApprovalStatusColor(item.myApprovalStatus)" text-color="white" small
                    class="font-weight-bold" outlined>
                    {{ item.myApprovalStatus }}
                </v-chip>
            </template>
            <template #item.status="{ item }">
                <v-chip :color="getStatusColor(item.statusLabel)" text-color="white" small class="font-weight-bold"
                    outlined>
                    {{ item.statusLabel }}
                </v-chip>
            </template>
        </ListView>
    </v-container>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import { useApprovalStore } from '@/stores/approvalStore';
import { useMemberStore } from '@/stores/memberStore';
import ListView from '@/components/common/ListView.vue';
import dayjs from 'dayjs';
import { getApprovalStatusLabel } from '@/constants/approval/approvalStatus.js';

const approvalStore = useApprovalStore();
const memberStore = useMemberStore();
const router = useRouter();

const receivedList = approvalStore.receivedList;
const loading = approvalStore.loadingReceived;
const error = approvalStore.errorReceived;

const headers = [
    { key: 'approvalId', label: '문서번호' },
    { key: 'categoryName', label: '카테고리' },
    { key: 'writerName', label: '작성자' },
    { key: 'status', label: '상태' },
    { key: 'myApprovalStep', label: '결재순서' },
    { key: 'myApprovalStatus', label: '내 결재 상태' }, // 변경!
    { key: 'createdAt', label: '작성일' },
];

const itemsPerPage = ref(10);
const page = ref(1);
const selectedStatus = ref('ALL');
const selectedMyStatus = ref('ALL');

const statusSummary = computed(() => {
    const all = receivedList.length;
    const canceled = receivedList.filter(i => i.status === 'CANCELED').length;
    const rejected = receivedList.filter(i => i.status === 'REJECTED').length;
    const inProgress = receivedList.filter(i => i.status === 'IN_PROGRESS').length;
    const approved = receivedList.filter(i => i.status === 'APPROVED').length;
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
    if (selectedStatus.value === 'ALL') return receivedList;
    return receivedList.filter(item => item.status === selectedStatus.value);
});

const pagedList = computed(() => {
    const start = (page.value - 1) * itemsPerPage.value;
    const end = start + itemsPerPage.value;
    return filteredList.value.slice(start, end).map(item => {
        const isMyTurn = item.isMyTurn == 1 || item.isMyTurn === true;
        let myApprovalStatus = '-';

        if (item.status === 'IN_PROGRESS') {
            myApprovalStatus = isMyTurn ? '결재 가능' : '대기 중';
        } else if (item.status === 'APPROVED') {
            myApprovalStatus = '완료';
        } else if (item.status === 'REJECTED') {
            myApprovalStatus = '진행 불가';
        } else if (item.status === 'CANCELED') {
            myApprovalStatus = '취소됨';
        }
        return Object.assign({}, item, {
            myApprovalStatus,
            canApproveChip: item.status === 'IN_PROGRESS' && isMyTurn, // 핵심!
            createdAt: item.createdAt ? dayjs(item.createdAt).format('YYYY-MM-DD HH:mm') : '',
            statusLabel: getApprovalStatusLabel(item.status)
        });
    });
});

const totalPages = computed(() => Math.ceil(filteredList.value.length / itemsPerPage.value));

const myApprovalStatusSummary = computed(() => {
    const all = pagedList.value.length;
    const possible = pagedList.value.filter(i => i.myApprovalStatus === '결재 가능').length;
    const waiting = pagedList.value.filter(i => i.myApprovalStatus === '대기 중').length;
    const done = pagedList.value.filter(i => i.myApprovalStatus === '완료').length;
    const rejected = pagedList.value.filter(i => i.myApprovalStatus === '진행 불가').length;
    const canceled = pagedList.value.filter(i => i.myApprovalStatus === '취소됨').length;
    return { all, possible, waiting, done, rejected, canceled };
});

const myApprovalStatusOptions = computed(() => [
    { key: 'ALL', label: '전체', count: myApprovalStatusSummary.value.all, color: 'primary' },
    { key: '결재 가능', label: '결재 가능', count: myApprovalStatusSummary.value.possible, color: 'success' },
    { key: '대기 중', label: '대기 중', count: myApprovalStatusSummary.value.waiting, color: 'grey' },
    { key: '완료', label: '완료', count: myApprovalStatusSummary.value.done, color: 'primary' },
    { key: '진행 불가', label: '진행 불가', count: myApprovalStatusSummary.value.rejected, color: 'error' },
    { key: '취소됨', label: '취소됨', count: myApprovalStatusSummary.value.canceled, color: 'grey' }
]);

function goToDetail(item) {
    if (item && item.approvalId) {
        router.push(`/approval/${item.approvalId}`);
    }
}

function getMyApprovalStatusColor(status) {
    switch (status) {
        case '결재 가능':
            return 'success'
        case '대기 중':
            return 'grey lighten-1'
        case '완료':
            return 'primary'
        case '진행 불가':
            return 'error'
        case '취소됨':
            return 'grey'
        default:
            return 'grey'
    }
}

function getStatusColor(statusLabel) {
    switch (statusLabel) {
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
    // 내 정보가 없으면 먼저 불러오기
    if (!memberStore.form.id) {
        await memberStore.getMyInfo();
    }
    if (memberStore.form.id) {
        approvalStore.loadReceivedApprovals(memberStore.form.id);
        // console.log('받은 결재 데이터:', approvalStore.receivedList);
    }

    setTimeout(() => {
        console.log('pagedList:', pagedList.value);
    }, 2000);
});
</script>
