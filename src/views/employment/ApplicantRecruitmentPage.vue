<script setup>
import { ref, onMounted, onUnmounted, watch, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import Search from '@/components/common/Search.vue'
import { useToast } from 'vue-toastification';
import { useApplicantStore } from '@/stores/applicantStore';
import { debounce } from 'lodash'
import { useJobtestListStore } from '@/stores/jobtestListStore';
import { useApplicationJobtestStore } from '@/stores/applicationJobtestStore';
import ApplicationJobtestDTO from '@/dto/employment/jobtest/applicationJobtestDTO';
import JobtestSelectModal from '@/components/employment/JobtestSelectModal.vue';

const selectedApplicants = ref([]);
const jobtestModal = ref(false);
const toast = useToast();
const jobtestListStore = useJobtestListStore();
const applicationJobtestStore = useApplicationJobtestStore();
const applicantStore = useApplicantStore();
const route = useRoute();
const router = useRouter();
const search = ref('')

const recruitmentId = computed(() => route.params.recruitmentId);
const filteredApplicants = computed(() => {
    // 전체 목록에서 recruitmentId가 일치하는 지원자만 필터링
    return applicantStore.filteredAndSortedApplicants.filter(
        item => String(item.recruitmentId) === String(recruitmentId.value)
    );
});

const tableHeaders = [
    { title: '', key: 'select', sortable: false, align: 'center', width: '50px' },
    { title: '이름', key: 'name', sortable: true, align: 'start' },
    { title: '이메일', key: 'email', sortable: true, align: 'start' },
    { title: '생년월일', key: 'birth', sortable: true, align: 'start' },
    { title: '전화번호', key: 'phone', sortable: true, align: 'start' },
    { title: '지원서', key: 'actions', sortable: false, align: 'center' },
    { title: '처리 상태', key: 'status', sortable: true, align: 'center' },
    { title: '지원공고', key: 'recruitmentTitle', sortable: true, align: 'start' }
];

const getApplicantCount = (applicantId) => {
    return filteredApplicants.value.filter(
        item => item.applicantId === applicantId
    ).length;
};
const getApplicantApplicationNumber = (currentItem) => {
    const sameApplicantApplications = filteredApplicants.value
        .filter(item => item.applicantId === currentItem.applicantId)
        .sort((a, b) => new Date(a.createdAt) - new Date(b.createdAt));
    return sameApplicantApplications.findIndex(item => item.uniqueKey === currentItem.uniqueKey) + 1;
};
watch(selectedApplicants, (newValue) => {
    if (newValue.length > 0) {
        // 디버깅용
    }
}, { deep: true });
const getSelectedApplicantNames = () => {
    if (!selectedApplicants.value || selectedApplicants.value.length === 0) return [];
    const selectedNames = selectedApplicants.value.map(selectedItem => selectedItem.name);
    return [...new Set(selectedNames)];
};
const getUniqueApplicantCount = () => {
    const uniqueApplicantIds = new Set(
        filteredApplicants.value.map(item => item.applicantId)
    );
    return uniqueApplicantIds.size;
};
const getStatusColor = (status) => {
    switch (status) {
        case 'PASSED_FINAL': return 'success'
        case 'FAILED': return 'error'
        case 'PASSED_DOCS': return 'info'
        case 'PASSED_INTERVIEW_1': return 'teal'
        case 'PASSED_INTERVIEW_2': return 'blue'
        case 'PASSED_PRACTICAL': return 'purple'
        case 'WAITING': return 'grey'
        default: return 'grey'
    }
}
const getStatusText = (status) => {
    switch (status) {
        case 'PASSED_FINAL': return '최종합격'
        case 'FAILED': return '불합격'
        case 'PASSED_DOCS': return '서류합격'
        case 'PASSED_INTERVIEW_1': return '1차합격'
        case 'PASSED_INTERVIEW_2': return '2차합격'
        case 'PASSED_PRACTICAL': return '실무합격'
        case 'WAITING': return '대기중'
        default: return '알 수 없음'
    }
}
const handleSearch = debounce((value) => {
    applicantStore.setSearchQuery(value)
}, 300)
const handleSort = (options) => {
    if (options.sortBy && options.sortBy.length > 0) {
        applicantStore.setSort({
            sortBy: options.sortBy,
            sortDesc: options.sortDesc || [false]
        });
    } else {
        applicantStore.setSort({ sortBy: [], sortDesc: [] });
    }
}
const viewDetail = (item) => {
    const [applicantData] = applicantStore.getSelectedApplicantsData([item]);
    router.push({
        path: `/employment/applications/${item.applicationId}`,
        query: {
            ...applicantData
        }
    })
}
const handleAssignClick = async () => {
    if (!selectedApplicants.value || selectedApplicants.value.length === 0) {
        toast.warning('선택된 지원자가 없습니다.');
        return;
    }
    try {
        await jobtestListStore.fetchJobtests();
        jobtestModal.value = true;
    } catch (error) {
        toast.error('실무 테스트 목록을 불러오는 데 실패했습니다.');
    }
};
const handleJobtestSelected = async (jobtest) => {
    jobtestModal.value = false;
    const dtoList = selectedApplicants.value.map(selectedItem => {
        return new ApplicationJobtestDTO(selectedItem.applicationId, jobtest.id);
    });
    try {
        await applicationJobtestStore.assignJobtest(dtoList);
        toast.success(`선택한 ${selectedApplicants.value.length}개 지원서에 실무테스트를 성공적으로 할당했습니다.`);
        selectedApplicants.value = [];
    } catch (error) {
        toast.error(applicationJobtestStore.errorMessage);
    }
};
const clearSearch = () => {
    search.value = ''
    applicantStore.setSearchQuery('')
}
onMounted(async () => {
    await applicantStore.fetchApplicantFullInfoList();
    // 최초 진입 시에도 recruitmentId로 필터링된 지원자만 보여줌
    selectedApplicants.value = [];
})
onUnmounted(() => {
    applicantStore.resetState()
    handleSearch.cancel()
})
const handleRegisterClick = () => {
    if (!selectedApplicants.value || selectedApplicants.value.length === 0) {
        toast.warning('선택된 지원자가 없습니다.')
        return
    }
    const selectedApplicantsData = selectedApplicants.value.map(applicant => ({
        applicantId: applicant.applicantId,
        applicationId: applicant.applicationId,
        name: applicant.name,
        email: applicant.email,
        phone: applicant.phone,
        birth: applicant.birth,
        address: applicant.address,
    }));
    router.push({
        path: '/orgstructure/member-register',
        query: {
            applicants: JSON.stringify(selectedApplicantsData)
        }
    })
}
const isSelected = (item) => {
    return selectedApplicants.value.some(selected => selected.uniqueKey === item.uniqueKey);
}
const toggleSelection = (item) => {
    const isCurrentlySelected = isSelected(item);
    if (isCurrentlySelected) {
        selectedApplicants.value = selectedApplicants.value.filter(
            selected => selected.uniqueKey !== item.uniqueKey
        );
    } else {
        selectedApplicants.value.push(item);
    }
}
const isAllSelected = computed(() => {
    const totalItems = filteredApplicants.value.length;
    return totalItems > 0 && selectedApplicants.value.length === totalItems;
});
const isIndeterminate = computed(() => {
    const selectedCount = selectedApplicants.value.length;
    const totalItems = filteredApplicants.value.length;
    return selectedCount > 0 && selectedCount < totalItems;
});
const toggleSelectAll = (selectAll) => {
    if (selectAll) {
        selectedApplicants.value = [...filteredApplicants.value];
    } else {
        selectedApplicants.value = [];
    }
}
const goBack = () => {
    router.push(`/employment/recruitments/${recruitmentId.value}`);
}
</script>

<template>
    <v-container fluid style="margin-top: 70px;">
        <v-card>
            <!-- 상단 타이틀 + 검색/버튼 영역 -->
            <v-card-title class="d-flex justify-between align-center flex-wrap">
                <v-btn icon variant="text" @click="goBack" class="mr-3">
                    <v-icon>mdi-arrow-left</v-icon>
                </v-btn>
                <span class="text-h6 font-weight-bold">채용공고별 지원자 목록</span>

                <v-spacer />

                <div class="d-flex align-center flex-wrap" style="gap: 8px;">
                    <Search v-model="search" placeholder="이름, 이메일, 전화번호, 직무로 검색" @clear="clearSearch"
                        @search="handleSearch" />
                    <v-btn icon @click="refreshList" :loading="applicantStore.loading" aria-label="새로고침">
                        <v-icon>mdi-refresh</v-icon>
                    </v-btn>

                    <v-btn color="primary" variant="tonal" size="small" style="min-width: 90px"
                        @click="handleRegisterClick" :disabled="!selectedApplicants.length">
                        사원 등록 ({{ selectedApplicants.length }}개 선택)
                    </v-btn>

                    <v-btn color="secondary" variant="tonal" size="small" style="min-width: 90px"
                        @click="handleAssignClick" :disabled="!selectedApplicants.length">
                        실무테스트 할당 ({{ selectedApplicants.length }}개 선택)
                    </v-btn>

                    <v-btn color="success" variant="outlined" size="small" prepend-icon="mdi-email"
                        style="min-width: 110px">
                        이메일 전송
                    </v-btn>
                </div>
            </v-card-title>

            <v-card-text v-if="search" class="text-caption text-grey">
                <span v-if="getUniqueApplicantCount() === 1">
                    검색어 "{{ search }}"에 대한 검색 결과:
                    지원자 {{ getUniqueApplicantCount() }}명
                    <span v-if="filteredApplicants.length > 1">
                        (지원서 {{ filteredApplicants.length }}건)
                    </span>
                </span>
                <span v-else>
                    검색어 "{{ search }}"에 대한 검색 결과:
                    지원자 {{ getUniqueApplicantCount() }}명, 지원서 {{ filteredApplicants.length }}건
                </span>
            </v-card-text>

            <v-data-table :headers="tableHeaders" :items="filteredApplicants" :items-per-page="8" item-key="uniqueKey"
                class="elevation-1" @update:options="handleSort" return-object>

                <template #header.select>
                    <v-checkbox :model-value="isAllSelected" :indeterminate="isIndeterminate"
                        @update:model-value="toggleSelectAll" hide-details density="compact" />
                </template>

                <template #item.select="{ item }">
                    <v-checkbox :model-value="isSelected(item)" @update:model-value="toggleSelection(item)" hide-details
                        density="compact" />
                </template>

                <template #item.name="{ item }">
                    <div>
                        <div class="font-weight-medium">{{ item.name || '-' }}</div>
                        <div class="text-caption text-grey" v-if="getApplicantCount(item.applicantId) > 1">
                            {{ getApplicantApplicationNumber(item) }}번째 지원
                        </div>
                    </div>
                </template>

                <template #item.email="{ item }">
                    {{ item.email || '-' }}
                </template>
                <template #item.birth="{ item }">
                    {{ item.birth ? new Date(item.birth).toLocaleDateString() : '-' }}
                </template>
                <template #item.phone="{ item }">
                    {{ item.phone || '-' }}
                </template>
                <template #item.status="{ item }">
                    <v-chip :color="getStatusColor(item.status)" variant="tonal" size="small">
                        {{ getStatusText(item.status) }}
                    </v-chip>
                </template>
                <template #item.jobName="{ item }">
                    {{ item.jobName || '미지정' }}
                </template>
                <template #item.actions="{ item }">
                    <v-btn color="primary" variant="text" size="small" @click="viewDetail(item)">
                        지원서 확인
                    </v-btn>
                </template>
            </v-data-table>

            <v-card-text v-if="selectedApplicants.length > 0" class="text-caption">
                <v-chip color="primary" variant="tonal" size="small">
                    {{ selectedApplicants.length }}개 지원서 선택됨
                </v-chip>
                <span class="ml-2 text-grey">
                    선택된 지원자: {{ getSelectedApplicantNames().join(', ') }}
                </span>
            </v-card-text>

            <v-overlay :model-value="applicantStore.loading" class="align-center justify-center">
                <v-progress-circular indeterminate size="64"></v-progress-circular>
            </v-overlay>

            <v-snackbar :model-value="!!applicantStore.error" color="error">
                {{ applicantStore.error }}
                <template v-slot:actions>
                    <v-btn variant="text" @click="applicantStore.error = null">
                        닫기
                    </v-btn>
                </template>
            </v-snackbar>
        </v-card>

        <JobtestSelectModal v-model="jobtestModal" :jobtests="jobtestListStore.jobtests"
            @select="handleJobtestSelected" />
    </v-container>
</template>

<style scoped>
.v-data-table {
    margin-top: 20px;
}
</style>