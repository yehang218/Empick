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
        <v-alert v-if="error" type="error" class="mb-4">{{ error.message || error }}</v-alert>
        <v-progress-circular v-if="loading" indeterminate color="primary" class="mb-4" />
        
        <div v-if="!loading && receivedList.length === 0 && !error" class="text-center pa-8" style="background-color: #f9f9f9; border-radius: 8px;">
            <p class="text-h6">받은 결재 문서가 없습니다.</p>
        </div>
        
        <div v-else>
            <v-data-table
                :headers="headers"
                :items="pagedList"
                @click:row="handleRowClick"
                class="elevation-1 list-item-hover"
                item-value="approvalId"
                hide-default-footer
            >
                <template #item.myApprovalStatus="{ item }">
                    <v-chip :color="getMyApprovalStatusColor(item.myApprovalStatus)" text-color="white" small
                        class="font-weight-bold" outlined>
                        {{ item.myApprovalStatus }}
                    </v-chip>
                </template>
                <template #item.status="{ item }">
                    <v-chip :color="getStatusColor(item.status)" text-color="white" small class="font-weight-bold"
                        outlined>
                        {{ item.status }}
                    </v-chip>
                </template>
            </v-data-table>
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
        </div>
    </v-container>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import { useApprovalStore } from '@/stores/approvalStore';
import { useMemberStore } from '@/stores/memberStore';
import { useAuthStore } from '@/stores/authStore';
import Pagination from '@/components/common/Pagination.vue';
import dayjs from 'dayjs';
import { getApprovalStatusLabel } from '@/constants/approval/approvalStatus.js';
import { storeToRefs } from 'pinia';

const approvalStore = useApprovalStore();
const memberStore = useMemberStore();
const authStore = useAuthStore();
const router = useRouter();

const { receivedList, loadingReceived: loading, errorReceived: error } = storeToRefs(approvalStore);

const headers = [
    { title: '문서번호', key: 'approvalId' },
    { title: '카테고리', key: 'categoryName' },
    { title: '작성자', key: 'writerName' },
    { title: '상태', key: 'status', align: 'center' },
    { title: '결재순서', key: 'myApprovalStep', align: 'center' },
    { title: '내 결재 상태', key: 'myApprovalStatus', align: 'center' },
    { title: '작성일', key: 'createdAt', align: 'center' },
];

const itemsPerPage = ref(10);
const page = ref(1);
const selectedStatus = ref('ALL');
const selectedMyStatus = ref('ALL');

const statusSummary = computed(() => {
    const all = receivedList.value.length;
    const canceled = receivedList.value.filter(i => i.status === 'CANCELED').length;
    const rejected = receivedList.value.filter(i => i.status === 'REJECTED').length;
    const inProgress = receivedList.value.filter(i => i.status === 'IN_PROGRESS').length;
    const approved = receivedList.value.filter(i => i.status === 'APPROVED').length;
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
    if (selectedStatus.value === 'ALL') return receivedList.value;
    return receivedList.value.filter(item => item.status === selectedStatus.value);
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

        return {
            ...item,
            myApprovalStatus,
            canApproveChip: item.status === 'IN_PROGRESS' && isMyTurn,
            createdAt: item.createdAt ? dayjs(item.createdAt).format('YYYY-MM-DD HH:mm') : '',
            status: getApprovalStatusLabel(item.status)
        };
    });
});

const totalPages = computed(() => Math.ceil(filteredList.value.length / itemsPerPage.value));

const pageText = computed(() => {
    const totalItems = filteredList.value.length;
    if (totalItems === 0) return '0-0 of 0';
    const start = (page.value - 1) * itemsPerPage.value + 1;
    const end = Math.min(page.value * itemsPerPage.value, totalItems);
    return `${start}-${end} of ${totalItems}`;
});

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

const handleRowClick = (event, { item }) => {
    if (item?.approvalId) {
        router.push(`/approval/inbox/${item.approvalId}`);
    }
};

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

function getStatusColor(status) {
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
    try {
        // 인증 상태 확인
        if (!authStore.isAuthenticated) {
            console.log('인증되지 않은 상태, 로그인 페이지로 이동');
            router.push('/login');
            return;
        }

        // 내 정보가 없으면 먼저 불러오기
        if (!memberStore.form.id) {
            await memberStore.getMyInfo();
        }
        if (memberStore.form.id) {
            await approvalStore.loadReceivedApprovals(memberStore.form.id);
        } else {
            throw new Error('멤버 정보 조회 실패(아이디 없음)');
        }
    } catch (err) {
        console.error('결재 목록 불러오기 실패:', err);
        approvalStore.errorReceived = '결재 목록을 불러오지 못했습니다.';
    }

    setTimeout(() => {
        console.log('pagedList:', pagedList.value);
    }, 2000);
});
</script>
