<template>
    <v-container fluid class="pa-6">
        <!-- 권한 없음 메시지 -->
        <div v-if="!hasHRAccess" class="text-center py-12">
            <v-icon size="64" color="error">mdi-lock-outline</v-icon>
            <div class="text-h6 mt-4 text-error">접근 권한이 없습니다</div>
            <div class="text-body-2 text-grey-darken-1 mt-2">인사팀 권한이 필요한 페이지입니다</div>
            <v-btn color="primary" class="mt-4" @click="router.push('/')">메인으로 돌아가기</v-btn>
        </div>

        <!-- 권한 있는 경우 메인 콘텐츠 -->
        <template v-else>
            <!-- 제목 및 액션 버튼 -->
            <div class="d-flex justify-space-between align-center mb-6">
                <h2 class="text-h5 font-weight-bold">사원 목록</h2>
                <div class="d-flex" style="gap: 16px;">
                    <v-btn color="grey" variant="outlined" prepend-icon="mdi-refresh" @click="refreshCurrentPage"
                        :loading="loading">
                        새로고침
                    </v-btn>
                    <v-btn color="primary" prepend-icon="mdi-plus"
                        @click="() => router.push('/orgstructure/member-register')">
                        사원 등록
                    </v-btn>
                </div>
            </div>

            <!-- 검색 및 필터 영역 -->
            <v-row class="mb-4" align="center">
                <v-col cols="12" md="6">
                    <v-text-field v-model="searchQuery" placeholder="이름, 사번, 이메일로 검색" prepend-inner-icon="mdi-magnify"
                        variant="outlined" density="compact" hide-details clearable />
                </v-col>
                <v-col cols="12" md="3">
                    <v-select v-model="selectedDepartment" :items="departmentOptions" label="부서" variant="outlined"
                        density="compact" hide-details clearable @update:modelValue="handleDepartmentFilter" />
                </v-col>
                <v-col cols="12" md="3">
                    <v-select v-model="selectedStatus" :items="statusOptions" label="상태" variant="outlined"
                        density="compact" hide-details @update:modelValue="handleStatusFilter" />
                </v-col>
            </v-row>

            <!-- 검색 결과 정보 -->
            <div class="d-flex justify-space-between align-center mb-3">
                <div class="text-body-2 text-grey-darken-1">
                    총 {{ members.length }}명 중 {{ totalFilteredMembers }}명 검색됨
                    <span v-if="totalFilteredMembers !== members.length" class="text-primary font-weight-medium">
                        (필터 적용됨)
                    </span>
                </div>
            </div>

            <!-- 사원 목록 테이블 -->
            <v-card class="mb-4 member-list-card" elevation="0">
                <v-data-table :headers="tableHeaders" :items="paginatedMembers" :loading="loading"
                    :loading-text="loadingMessage || '데이터를 불러오는 중...'" item-key="id" class="member-table" show-expand
                    v-model:expanded="expanded" @click:row="handleRowClick" :items-per-page="-1" hide-default-footer>

                    <!-- 아바타 + 이름 컬럼 -->
                    <template #item.name="{ item }">
                        <div class="d-flex align-center py-2">
                            <v-avatar size="40" class="mr-3">
                                <v-img v-if="item.profileImageUrl || item.pictureUrl"
                                    :src="item.profileImageUrl || item.pictureUrl" :alt="item.name"
                                    @error="handleImageError(item)" />
                                <v-icon v-else icon="mdi-account-circle" size="40" color="grey-lighten-1" />
                            </v-avatar>
                            <div>
                                <div class="font-weight-medium">{{ item.name }}</div>
                                <div class="text-caption text-grey-darken-1">{{ item.rankName || '-' }}</div>
                            </div>
                        </div>
                    </template>

                    <!-- 부서 컬럼 -->
                    <template #item.departmentName="{ item }">
                        <div>
                            <div class="font-weight-medium">{{ item.departmentName || '-' }}</div>
                            <div class="text-caption text-grey-darken-1">{{ item.jobName || '-' }}</div>
                        </div>
                    </template>

                    <!-- 상태 컬럼 -->
                    <template #item.status="{ item }">
                        <div class="status-badge" :class="getStatusClass(item.status)">
                            <div class="status-dot"></div>
                            <span class="status-text">{{ getStatusLabel(item.status) }}</span>
                        </div>
                    </template>

                    <!-- 입사일 컬럼 -->
                    <template #item.hireAt="{ item }">
                        {{ formatDate(item.hireAt) }}
                    </template>

                    <!-- 확장된 행 내용 -->
                    <template #expanded-row="{ item }">
                        <tr>
                            <td :colspan="tableHeaders.length" class="pa-0">
                                <AttendanceSummaryCard :member="item" @view-detail="goToMemberDetail"
                                    @view-attendance="handleViewAttendance" @send-mail="handleSendMail" />
                            </td>
                        </tr>
                    </template>

                    <!-- 로딩 상태 -->
                    <template #loading>
                        <v-skeleton-loader type="table-row@8" />
                    </template>

                    <!-- 데이터 없음 -->
                    <template #no-data>
                        <div class="text-center py-8">
                            <v-icon size="64" color="grey-lighten-2">mdi-account-group-outline</v-icon>
                            <div class="text-h6 mt-2 text-grey-darken-1">
                                {{ members.length === 0 ? '등록된 사원이 없습니다' : '검색된 사원이 없습니다' }}
                            </div>
                            <div class="text-body-2 text-grey-darken-1">
                                {{ members.length === 0 ? '사원을 먼저 등록해주세요' : '검색 조건을 변경해보세요' }}
                            </div>
                        </div>
                    </template>
                </v-data-table>

                <!-- 사용자 정의 페이지네이션 -->
                <div class="d-flex justify-space-between align-center pa-4">
                    <div class="text-body-2 text-grey-darken-1">
                        {{ startIndex + 1 }}-{{ endIndex }}개 (총 {{ totalFilteredMembers }}개)
                    </div>
                    <div class="d-flex align-center">
                        <span class="text-body-2 mr-3">페이지당 항목 수:</span>
                        <v-select v-model="itemsPerPage" :items="[10, 25, 50, 100]" variant="outlined" density="compact"
                            style="width: 80px;" hide-details @update:modelValue="handleItemsPerPageChange" />
                    </div>
                    <Pagination v-model="currentPage" :length="totalPages" :total-visible="7" />
                </div>
            </v-card>
        </template>
    </v-container>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'
import { useMemberList } from '@/composables/useMemberList'
import { RoleCode } from '@/constants/common/RoleCode'
import { TABLE_HEADERS, STATUS_OPTIONS, getStatusClass, getStatusLabel, formatDate } from '@/utils/memberUtils'
import AttendanceSummaryCard from '@/components/attendance/AttendanceSummaryCard.vue'
import Pagination from '@/components/common/Pagination.vue'

const router = useRouter()
const authStore = useAuthStore()

// 🛡 권한 체크
const hasHRAccess = computed(() =>
    authStore.userInfo?.roles?.includes(RoleCode.HR_ACCESS)
)

// 📋 useMemberList 컴포저블 사용
const {
    // 상태
    members,
    loading,
    loadingMessage,
    expanded,

    // 페이지네이션 상태
    currentPage,
    itemsPerPage,
    totalPages,
    totalFilteredMembers,
    startIndex,
    endIndex,

    // 검색 및 필터 상태
    searchQuery,
    selectedDepartment,
    selectedStatus,

    // 계산된 속성
    paginatedMembers,

    // 비즈니스 로직
    loadAllMembers,
    refreshCurrentPage,
    createDepartmentOptions,

    // 이벤트 핸들러
    handleDepartmentFilter,
    handleStatusFilter,
    handleItemsPerPageChange,
    handleImageError,

    // 유틸리티
    setupSearchWatcher
} = useMemberList()

// 🏢 부서 옵션
const departmentOptions = createDepartmentOptions()

// 📊 테이블 설정
const tableHeaders = TABLE_HEADERS
const statusOptions = STATUS_OPTIONS

// 🔄 검색어 감지 설정
setupSearchWatcher()

// 🖱 행 클릭 핸들러
const handleRowClick = (event, { item }) => {
    console.log('클릭한 사원 데이터:', item)
    console.log('사원의 birth 데이터:', item.birth)

    // 사원 상세 페이지로 이동 (sessionStorage 사용)
    const memberDataWithImage = {
        ...item,
        // 현재 표시되고 있는 프로필 이미지 URL 포함
        profileImageUrl: item.profileImageUrl || item.pictureUrl || '',
        // 생년월일 포함
        birth: item.birth,
        // 주소 포함
        address: item.address
    }

    console.log('전달할 데이터:', memberDataWithImage)
    console.log('전달할 birth 데이터:', memberDataWithImage.birth)

    // sessionStorage에 데이터 저장 (새로고침 시에도 유지됨)
    try {
        // eslint-disable-next-line no-undef
        sessionStorage.setItem('memberDetailData', JSON.stringify(memberDataWithImage))
        console.log('sessionStorage에 데이터 저장 완료')
    } catch (error) {
        console.error('sessionStorage 저장 실패:', error)
        // fallback으로 전역 변수 사용
        globalThis.memberDetailData = memberDataWithImage
    }

    router.push({
        name: 'AttendanceDetailPage',
        params: { id: item.id }
    })
}

// 🧭 네비게이션 (라우팅 관련)
const goToMemberDetail = (member) => {
    router.push(`/orgstructure/members/${member.employeeNumber}`)
}

const handleViewAttendance = (member) => {
    console.log('근태 기록 보기:', member)
    // TODO: 근태 기록 페이지로 이동
}

const handleSendMail = (member) => {
    console.log('메일 발송:', member)
    // TODO: 메일 발송 모달 열기
}

// 🚀 라이프사이클
onMounted(async () => {
    try {
        // 클라이언트 사이드 페이지네이션을 위해 전체 데이터 로드
        await loadAllMembers() // 전체 데이터 로드
        console.log('=== 데이터 로드 완료 ===')
        console.log('전체 사원 수:', members.value.length)
        console.log('현재 페이지:', currentPage.value)
        console.log('페이지당 항목 수:', itemsPerPage.value)
    } catch (error) {
        console.error('초기 데이터 로드 실패:', error)
    }
})
</script>

<style scoped>
.member-list-card {
    border: 1px solid #e0e0e0;
    border-radius: 12px;
    overflow: visible;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.member-table :deep(.v-data-table__wrapper) {
    border-radius: 12px;
}

.member-table :deep(.v-data-table-header th) {
    background: linear-gradient(135deg, #f8f9fa 0%, #f1f3f4 100%);
    border-bottom: 1px solid #e8eaed;
    font-weight: 600;
    color: #374151;
    transition: background-color 0.2s ease;
}

.member-table :deep(.v-data-table-header th:hover) {
    background-color: rgba(255, 255, 255, 0.8);
}

.member-table :deep(tbody tr) {
    cursor: pointer;
    transition: background-color 0.2s ease, border-left 0.2s ease;
    background-color: #ffffff;
}

.member-table :deep(tbody tr:hover) {
    background-color: #f8f9fa;
    border-left: 3px solid #1976d2;
}

.member-table :deep(.v-data-table__td) {
    border-bottom: 1px solid #f0f2f5;
}

/* 상태 배지 스타일 */
.status-badge {
    display: inline-flex;
    align-items: center;
    padding: 6px 12px;
    border-radius: 20px;
    font-size: 12px;
    font-weight: 500;
    transition: all 0.2s ease;
}

.status-dot {
    width: 6px;
    height: 6px;
    border-radius: 50%;
    margin-right: 6px;
}

.status-text {
    line-height: 1;
}

.status-present {
    background-color: #e8f5e8;
    color: #2e7d32;
    border: 1px solid #c8e6c9;
}

.status-present .status-dot {
    background-color: #4caf50;
}

.status-absent {
    background-color: #ffebee;
    color: #c62828;
    border: 1px solid #ffcdd2;
}

.status-absent .status-dot {
    background-color: #f44336;
}

.status-no-record {
    background-color: #fff3e0;
    color: #ef6c00;
    border: 1px solid #ffcc02;
}

.status-no-record .status-dot {
    background-color: #ff9800;
}

.status-unknown {
    background-color: #f5f5f5;
    color: #666;
    border: 1px solid #e0e0e0;
}

.status-unknown .status-dot {
    background-color: #9e9e9e;
}

/* 아바타 개선 */
.member-table :deep(.v-avatar) {
    border: 2px solid #f0f2f5;
    transition: border-color 0.2s ease;
}

.member-table :deep(tbody tr:hover .v-avatar) {
    border-color: #1976d2;
}

/* 텍스트 스타일 개선 */
.font-weight-medium {
    color: #1a1a1a;
    font-weight: 500;
}

.text-caption {
    color: #666;
    font-size: 11px;
}

/* 확장된 행 스타일 */
.member-table :deep(.v-data-table__expanded__content) {
    background-color: #f8f9fa;
}

.info-grid {
    display: flex;
    flex-direction: column;
    gap: 8px;
}

.info-item {
    display: flex;
    align-items: center;
    gap: 8px;
}

.info-label {
    font-weight: 500;
    color: #666;
    min-width: 80px;
    font-size: 13px;
}

.info-value {
    color: #333;
    font-weight: 400;
    font-size: 13px;
}

/* 확장 아이콘 스타일 */
.member-table :deep(.v-data-table__expand-icon) {
    color: #1976d2;
    transition: transform 0.2s ease;
}

.member-table :deep(.v-data-table__expand-icon--active) {
    transform: rotate(90deg);
}

/* 페이지네이션 스타일 */
.member-table :deep(.v-data-table-footer) {
    padding: 16px;
    border-top: 1px solid #e0e0e0;
    background-color: #fafafa;
}
</style>