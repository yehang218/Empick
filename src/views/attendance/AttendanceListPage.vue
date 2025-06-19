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
                    v-model:expanded="expanded" @update:sort-by="handleSort" @click:row="handleRowClick"
                    :items-per-page="-1" hide-default-footer>

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
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'
import { useMemberStore } from '@/stores/memberStore'
import { useAttendanceStore } from '@/stores/attendanceStore'
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

// 클라이언트 사이드 페이지네이션을 위한 간단한 데이터 로딩
const memberStore = useMemberStore()
const attendanceStore = useAttendanceStore()
const members = ref([])
const loading = ref(false)

// 사원별 실제 근태 상태 조회
const enrichMembersWithAttendance = async (memberList) => {
    console.log('근태 정보 로드 시작:', memberList.length, '명')

    // 배치 크기 설정 (동시에 처리할 사원 수)
    const batchSize = 5 // 서버 부하를 고려해서 5명씩 처리
    const batches = []

    for (let i = 0; i < memberList.length; i += batchSize) {
        batches.push(memberList.slice(i, i + batchSize))
    }

    console.log('배치 처리:', batches.length, '개 배치')

    // 배치별로 순차 처리 (서버 부하 방지)
    const allResults = []
    for (let batchIndex = 0; batchIndex < batches.length; batchIndex++) {
        const batch = batches[batchIndex]
        loadingMessage.value = `근태 정보 조회 중... (${batchIndex + 1}/${batches.length})`
        console.log(`배치 ${batchIndex + 1}/${batches.length} 처리 중...`)

        const batchResults = await Promise.all(
            batch.map(async (member) => {
                try {
                    // 각 사원의 근태 기록 조회
                    const attendanceRecords = await attendanceStore.fetchMemberAttendanceRecords(member.id, {
                        silent: true // 에러 시 토스트 표시 안함
                    })

                    let status = -1 // 기본값: 기록없음

                    if (attendanceRecords && attendanceRecords.length > 0) {
                        // 오늘 날짜 기준으로 출근 기록 확인
                        const today = new Date().toISOString().split('T')[0]
                        const todayRecord = attendanceRecords.find(record => {
                            const recordDate = new Date(record.checkInTime || record.createdAt).toISOString().split('T')[0]
                            return recordDate === today
                        })

                        if (todayRecord) {
                            // 출근 기록이 있으면 출근 상태
                            status = 1
                        } else {
                            // 기록은 있지만 오늘 출근 기록이 없으면 미출근
                            status = 0
                        }
                    }

                    console.log(`사원 ${member.name}: 상태 ${status} (${status === 1 ? '출근' : status === 0 ? '미출근' : '기록없음'})`)

                    return {
                        ...member,
                        status: status
                    }
                } catch (error) {
                    console.warn(`사원 ${member.name}의 근태 정보 조회 실패:`, error)
                    // API 실패 시 기본값으로 설정 (기록없음)
                    return {
                        ...member,
                        status: -1
                    }
                }
            })
        )

        allResults.push(...batchResults)

        // 배치 간 약간의 지연 (서버 부하 방지)
        if (batchIndex < batches.length - 1) {
            // eslint-disable-next-line no-undef
            await new Promise(resolve => setTimeout(resolve, 200))
        }
    }

    console.log('근태 정보 로드 완료:', allResults.length, '명')
    return allResults
}

// 전체 사원 목록 로드
const loadAllMembers = async () => {
    loading.value = true
    loadingMessage.value = '사원 데이터를 불러오는 중...'

    try {
        // 전체 사원 데이터 로드
        console.log('사원 데이터 로딩 시작...')
        const allMembers = await memberStore.findMembers()
        console.log('기본 사원 데이터 로드 완료:', allMembers.length, '명')

        // 실제 근태 정보와 함께 사원 데이터 보강
        loadingMessage.value = '근태 정보를 조회하는 중...'
        console.log('근태 정보 조회 시작...')
        const membersWithAttendance = await enrichMembersWithAttendance(allMembers)

        members.value = membersWithAttendance
        console.log('전체 데이터 로드 완료:', membersWithAttendance.length, '명')
        console.log('첫 번째 사원 샘플:', membersWithAttendance[0])

        // 상태별 통계
        const stats = {
            출근: membersWithAttendance.filter(m => m.status === 1).length,
            미출근: membersWithAttendance.filter(m => m.status === 0).length,
            기록없음: membersWithAttendance.filter(m => m.status === -1).length
        }
        console.log('사원 상태 통계:', stats)

    } catch (error) {
        console.error('사원 데이터 로드 실패:', error)
        members.value = []
    } finally {
        loading.value = false
        loadingMessage.value = ''
    }
}

const refreshCurrentPage = () => {
    loadAllMembers()
}

// 기존 composable 함수들
const {
    createDepartmentOptions,
    loadSingleProfileImage
} = useMemberList()

// UI 상태 - 클라이언트 사이드 페이지네이션
const searchQuery = ref('')
const selectedDepartment = ref(null)
const selectedStatus = ref('전체')
const expanded = ref([])
const currentPage = ref(1)
const itemsPerPage = ref(10)
const loadingMessage = ref('')

// 상수
const tableHeaders = TABLE_HEADERS
const statusOptions = STATUS_OPTIONS

// 계산된 속성
const departmentOptions = createDepartmentOptions()

// 직접 필터링 로직 구현
const filteredMembers = computed(() => {
    let result = [...members.value]

    console.log('필터링 시작:', {
        전체사원수: result.length,
        검색어: searchQuery.value,
        선택부서: selectedDepartment.value,
        선택상태: selectedStatus.value
    })

    // 검색 필터
    if (searchQuery.value && searchQuery.value.trim()) {
        const searchTerm = searchQuery.value.toLowerCase().trim()
        result = result.filter(member =>
            member.name?.toLowerCase().includes(searchTerm) ||
            member.employeeNumber?.toString().includes(searchTerm) ||
            member.email?.toLowerCase().includes(searchTerm)
        )
        console.log('검색 필터 후:', result.length, '명')
    }

    // 부서 필터
    if (selectedDepartment.value) {
        result = result.filter(member => member.departmentName === selectedDepartment.value)
        console.log('부서 필터 후:', result.length, '명')
    }

    // 상태 필터
    if (selectedStatus.value && selectedStatus.value !== '전체') {
        const filterStatus = selectedStatus.value
        console.log('상태 필터 조건:', { filterStatus, 타입: typeof filterStatus })

        result = result.filter(member => {
            const memberStatus = member.status
            console.log('사원 상태 비교:', {
                사원명: member.name,
                사원상태: memberStatus,
                사원상태타입: typeof memberStatus,
                필터상태: filterStatus,
                필터상태타입: typeof filterStatus,
                비교결과: memberStatus == filterStatus
            })
            return memberStatus == filterStatus // == 사용 (느슨한 비교)
        })
        console.log('상태 필터 후:', result.length, '명')
    }

    console.log('최종 필터링 결과:', result.length, '명')
    return result
})

// 클라이언트 사이드 페이지네이션 계산
const totalFilteredMembers = computed(() => filteredMembers.value.length)
const totalPages = computed(() => Math.ceil(totalFilteredMembers.value / itemsPerPage.value))

const startIndex = computed(() => (currentPage.value - 1) * itemsPerPage.value)
const endIndex = computed(() => Math.min(startIndex.value + itemsPerPage.value, totalFilteredMembers.value))

const paginatedMembers = computed(() => {
    const start = startIndex.value
    const end = endIndex.value
    return filteredMembers.value.slice(start, end)
})

// 이벤트 핸들러 (UI 관련만)
const handleSearch = () => {
    console.log('검색어 변경:', searchQuery.value)
    // 검색 시 첫 페이지로 리셋
    currentPage.value = 1
}

const handleDepartmentFilter = () => {
    console.log('부서 필터 변경:', selectedDepartment.value)
    // 필터 변경 시 첫 페이지로 리셋
    currentPage.value = 1
}

const handleStatusFilter = () => {
    console.log('상태 필터 변경:', selectedStatus.value)
    // 필터 변경 시 첫 페이지로 리셋
    currentPage.value = 1
}

const handleSort = (sortBy) => {
    // v-data-table의 내장 정렬 처리
    console.log('정렬 변경:', sortBy)
}

// 클라이언트 사이드 페이지네이션 핸들러
const handleItemsPerPageChange = (newSize) => {
    console.log('페이지 크기 변경됨:', newSize)
    itemsPerPage.value = newSize
    // 페이지 크기 변경 시 첫 페이지로 리셋
    currentPage.value = 1
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
        // 클라이언트 사이드 페이지네이션을 위해 전체 데이터 로드
        await loadAllMembers() // 전체 데이터 로드
        console.log('=== 데이터 로드 완료 ===')
        console.log('전체 사원 수:', members.value.length)
        console.log('필터링된 사원 수:', filteredMembers.value.length)
        console.log('현재 페이지:', currentPage.value)
        console.log('페이지당 항목 수:', itemsPerPage.value)
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