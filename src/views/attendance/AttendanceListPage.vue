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
                        variant="outlined" density="compact" hide-details clearable @input="handleSearch" />
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

            <!-- 페이징 정보 -->
            <div class="d-flex justify-space-between align-center mb-3">
                <div class="text-body-2 text-grey-darken-1">
                    총 {{ totalMembers }}명 중 {{ ((currentPage - 1) * pageSize) + 1 }}~{{ Math.min(currentPage * pageSize,
                        totalMembers) }}명 표시 ({{ currentPage }}/{{ totalPages }} 페이지)
                </div>
                <div class="d-flex align-center" style="gap: 8px;">
                    <span class="text-body-2">페이지당:</span>
                    <v-select v-model="pageSize" :items="[10, 20, 50, 100]" variant="outlined" density="compact"
                        hide-details style="width: 80px;" @update:modelValue="handlePageSizeChange" />
                </div>
            </div>

            <!-- 사원 목록 테이블 -->
            <v-card class="mb-4 member-list-card" elevation="0">
                <v-data-table :headers="tableHeaders" :items="members" :items-per-page="-1" :loading="loading"
                    item-key="id" class="member-table" show-expand v-model:expanded="expanded" hide-default-footer
                    @update:sort-by="handleSort" @click:row="handleRowClick">

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
            </v-card>

            <!-- 페이징 네비게이션 -->
            <div class="d-flex justify-center mt-4" v-if="totalPages > 1">
                <v-pagination v-model="currentPage" :length="totalPages" :total-visible="7" :disabled="loading"
                    @update:modelValue="handlePageChange" />
            </div>


        </template>
    </v-container>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'
import { useMemberList } from '@/composables/useMemberList'
import { RoleCode } from '@/constants/common/RoleCode'
import { TABLE_HEADERS, STATUS_OPTIONS, getStatusClass, getStatusLabel, formatDate } from '@/utils/memberUtils'
import AttendanceSummaryCard from '@/components/attendance/AttendanceSummaryCard.vue'

const router = useRouter()
const authStore = useAuthStore()

// 🛡 권한 체크
const hasHRAccess = computed(() =>
    authStore.userInfo?.roles?.includes(RoleCode.HR_ACCESS)
)

// 비즈니스 로직 (Composable)
const {
    members,
    loading,
    currentPage,
    pageSize,
    totalMembers,
    totalPages,
    loadMembers,
    goToPage,
    changePageSize,
    refreshCurrentPage,
    createDepartmentOptions,
    loadSingleProfileImage
} = useMemberList()

// UI 상태
const searchQuery = ref('')
const selectedDepartment = ref(null)
const selectedStatus = ref('전체')
const expanded = ref([])

// 상수
const tableHeaders = TABLE_HEADERS
const statusOptions = STATUS_OPTIONS

// 계산된 속성
const departmentOptions = createDepartmentOptions()
// const filteredMembers = createMemberFilter(searchQuery, selectedDepartment, selectedStatus) // 페이징과 충돌하므로 임시 비활성화

// 이벤트 핸들러 (UI 관련만)
const handleSearch = () => {
    // 검색 시 필터만 적용 (v-data-table이 자동으로 처리)
}

const handleDepartmentFilter = () => {
    // 부서 필터 변경 시 (v-data-table이 자동으로 처리)
}

const handleStatusFilter = () => {
    // 상태 필터 변경 시 (v-data-table이 자동으로 처리)
}

const handleSort = (sortBy) => {
    // v-data-table의 내장 정렬 처리
    console.log('정렬 변경:', sortBy)
}

const handlePageChange = (page) => {
    if (!loading.value) {
        if (page !== currentPage.value) {
            goToPage(page)
        } else {
            // 같은 페이지를 클릭한 경우 새로고침
            refreshCurrentPage()
        }
    }
}

const handlePageSizeChange = (newSize) => {
    changePageSize(newSize)
}



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

// 네비게이션 (라우팅 관련)
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

// 프로필 이미지 에러 처리
const handleImageError = async (member) => {
    console.log('프로필 이미지 로드 실패, API로 재시도:', member.name)
    try {
        const imageUrl = await loadSingleProfileImage(member.id)
        if (imageUrl) {
            member.profileImageUrl = imageUrl
        }
    } catch (error) {
        console.warn('프로필 이미지 API 로드도 실패:', error)
    }
}



// 라이프사이클
onMounted(async () => {
    try {
        await loadMembers(1) // 첫 페이지 로드
    } catch (error) {
        console.error('초기 데이터 로드 실패:', error)
    }
})

// 검색어 변경 시 디바운싱
watch(searchQuery, () => {
    handleSearch()
}, { debounce: 300 })
</script>

<style scoped>
.member-list-card {
    border: 1px solid #e0e0e0;
    border-radius: 12px;
    overflow: hidden;
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
</style>