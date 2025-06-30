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
        <div v-if="!loading && requestedList.length === 0 && !error" class="text-center pa-8" style="background-color: #f9f9f9; border-radius: 8px;">
            <p class="text-h6">요청한 결재 문서가 없습니다.</p>
        </div>
        <div v-else-if="!loading && requestedList.length > 0">
            <v-data-table
                :headers="headers"
                :items="pagedList"
                @click:row="goToDetail"
                class="elevation-1 list-item-hover"
                item-value="approvalId"
                hide-default-footer
            >
                <template #item.status="{ item }">
                    <v-chip :color="getApprovalStatusColor(item.status)" text-color="white" small class="font-weight-bold"
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
import { storeToRefs } from 'pinia';
import { useApprovalStore } from '@/stores/approvalStore';
import { useMemberStore } from '@/stores/memberStore';
import { useAuthStore } from '@/stores/authStore';
import Pagination from '@/components/common/Pagination.vue';
import dayjs from 'dayjs';
import { getApprovalStatusLabel } from '@/constants/approval/approvalStatus.js';

const approvalStore = useApprovalStore();
const memberStore = useMemberStore();
const authStore = useAuthStore();
const router = useRouter();

const { requestedList, loadingRequested: loading, errorRequested: error } = storeToRefs(approvalStore);

const headers = [
    { title: '문서번호', key: 'approvalId' },
    { title: '카테고리명', key: 'categoryName' },
    { title: '상태', key: 'status', align: 'center' },
    { title: '작성일', key: 'createdAt', align: 'center' },
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

const pageText = computed(() => {
    const totalItems = filteredList.value.length;
    if (totalItems === 0) return '0-0 of 0';
    const start = (page.value - 1) * itemsPerPage.value + 1;
    const end = Math.min(page.value * itemsPerPage.value, totalItems);
    return `${start}-${end} of ${totalItems}`;
});

function goToDetail(event, { item }) {
    if (item && item.approvalId) {
        router.push(`/approval/sent/${item.approvalId}`);
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
    try {
        // 인증 상태 확인
        if (!authStore.isAuthenticated) {
            console.log('인증되지 않은 상태, 로그인 페이지로 이동');
            router.push('/login');
            return;
        }

        if (!memberStore.form.id) {
            await memberStore.getMyInfo();
        }
        if (memberStore.form.id) {
            await approvalStore.loadRequestedApprovals(memberStore.form.id);
        } else {
            throw new Error('멤버 정보 조회 실패(아이디 없음)');
        }
    } catch (err) {
        console.error('결재 목록 불러오기 실패:', err);
        approvalStore.errorRequested = '결재 목록을 불러오지 못했습니다.';
    }
});
</script>