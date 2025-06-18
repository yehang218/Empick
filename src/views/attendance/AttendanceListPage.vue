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
            <!-- 제목 -->
            <h2 class="text-h5 font-weight-bold mb-6">사원 목록</h2>

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

            <!-- 사원 목록 테이블 -->
            <v-card class="mb-4 member-list-card" elevation="0">
                <v-data-table :headers="tableHeaders" :items="filteredMembers" :items-per-page="8" :loading="loading"
                    item-key="id" class="member-table" @click:row="handleRowClick" @update:sort-by="handleSort">

                    <!-- 아바타 + 이름 컬럼 -->
                    <template #item.name="{ item }">
                        <div class="d-flex align-center py-2">
                            <v-avatar size="40" class="mr-3">
                                <v-img v-if="item.pictureUrl" :src="item.pictureUrl" :alt="item.name" />
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

                    <!-- 액션 컬럼 -->
                    <template #item.actions="{ item }">
                        <v-btn icon="mdi-chevron-right" variant="text" size="small"
                            @click.stop="goToMemberDetail(item)" />
                    </template>

                    <!-- 로딩 상태 -->
                    <template #loading>
                        <v-skeleton-loader type="table-row@8" />
                    </template>

                    <!-- 데이터 없음 -->
                    <template #no-data>
                        <div class="text-center py-8">
                            <v-icon size="64" color="grey-lighten-2">mdi-account-group-outline</v-icon>
                            <div class="text-h6 mt-2 text-grey-darken-1">검색된 사원이 없습니다</div>
                            <div class="text-body-2 text-grey-darken-1">검색 조건을 변경해보세요</div>
                        </div>
                    </template>
                </v-data-table>
            </v-card>
        </template>
    </v-container>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useDepartmentStore } from '@/stores/departmentStore'
import { useAuthStore } from '@/stores/authStore'
import { RoleCode } from '@/constants/common/RoleCode'
import dayjs from 'dayjs'

const router = useRouter()
const departmentStore = useDepartmentStore()
const authStore = useAuthStore()

// 🛡 권한 체크
const hasHRAccess = computed(() =>
    authStore.userInfo?.roles?.includes(RoleCode.HR_ACCESS)
)

// 반응형 데이터
const searchQuery = ref('')
const selectedDepartment = ref(null)
const selectedStatus = ref('전체')
const loading = ref(false)
const members = ref([])

// 테이블 헤더 정의
const tableHeaders = [
    { title: '이름', key: 'name', sortable: true, width: '200px' },
    { title: '사번', key: 'employeeNumber', sortable: true, width: '120px' },
    { title: '이메일', key: 'email', sortable: true, width: '200px' },
    { title: '연락처', key: 'phone', sortable: true, width: '150px' },
    { title: '부서', key: 'departmentName', sortable: true, width: '150px' },
    { title: '상태', key: 'status', sortable: true, width: '100px' },
    { title: '입사일시', key: 'hireAt', sortable: true, width: '120px' },
    { title: '', key: 'actions', sortable: false, width: '60px' }
]

// 상태 옵션
const statusOptions = [
    { title: '전체', value: '전체' },
    { title: '출근', value: 1 },
    { title: '미출근', value: 0 }
]

// 부서 옵션 (computed)
const departmentOptions = computed(() => {
    const uniqueDepartments = [...new Set(members.value.map(m => m.departmentName).filter(Boolean))]
    return uniqueDepartments.map(dept => ({ title: dept, value: dept }))
})

// 필터링된 사원 목록 (v-data-table이 내장 정렬과 페이징 처리)
const filteredMembers = computed(() => {
    let result = [...members.value]

    // 검색 필터
    if (searchQuery.value) {
        const query = searchQuery.value.toLowerCase()
        result = result.filter(member =>
            member.name?.toLowerCase().includes(query) ||
            member.employeeNumber?.toString().includes(query) ||
            member.email?.toLowerCase().includes(query)
        )
    }

    // 부서 필터
    if (selectedDepartment.value) {
        result = result.filter(member => member.departmentName === selectedDepartment.value)
    }

    // 상태 필터
    if (selectedStatus.value && selectedStatus.value !== '전체') {
        result = result.filter(member => member.status === selectedStatus.value)
    }

    return result
})

// 메서드들
const loadMembers = async () => {
    loading.value = true
    try {
        // 실제로는 memberStore에서 전체 사원 목록을 가져오는 메서드가 필요합니다
        // 현재는 예시 데이터로 대체
        members.value = await getMockMembers()
        await departmentStore.loadDepartmentList()
    } catch (error) {
        console.error('사원 목록 로딩 실패:', error)
    } finally {
        loading.value = false
    }
}

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

const handleRowClick = (event, { item }) => {
    goToMemberDetail(item)
}

const goToMemberDetail = (member) => {
    // 사원 상세 페이지로 이동 (향후 구현)
    router.push(`/orgstructure/members/${member.employeeNumber}`)
}

const getStatusClass = (status) => {
    switch (status) {
        case 1: return 'status-present'
        case 0: return 'status-absent'
        default: return 'status-unknown'
    }
}

const getStatusLabel = (status) => {
    switch (status) {
        case 1: return '출근'
        case 0: return '미출근'
        default: return '알 수 없음'
    }
}

const formatDate = (dateString) => {
    if (!dateString) return '-'
    return dayjs(dateString).format('YYYY-MM-DD')
}

// Mock 데이터 함수 (실제로는 API 호출로 대체)
const getMockMembers = async () => {
    return [
        {
            id: 1,
            name: 'Brooklyn Simmons',
            employeeNumber: '87364523',
            email: 'brooklyns@mail.com',
            phone: '(603) 555-0123',
            departmentName: '인사',
            jobName: '인사관리',
            rankName: '대리',
            status: 1,
            hireAt: '2022-03-01',
            pictureUrl: null
        },
        {
            id: 2,
            name: 'Kristin Watson',
            employeeNumber: '93874563',
            email: 'kristinw@mail.com',
            phone: '(219) 555-0114',
            departmentName: '백엔드/개발',
            jobName: 'PM',
            rankName: '사원',
            status: 1,
            hireAt: '2023-05-15',
            pictureUrl: null
        },
        {
            id: 3,
            name: 'Jacob Jones',
            employeeNumber: '23847569',
            email: 'jacbj@mail.com',
            phone: '(319) 555-0115',
            departmentName: '회계',
            jobName: '대사장',
            rankName: '사원',
            status: 0,
            hireAt: '2021-08-10',
            pictureUrl: null
        },
        {
            id: 4,
            name: 'Cody Fisher',
            employeeNumber: '39485632',
            email: 'codyf@mail.com',
            phone: '(229) 555-0109',
            departmentName: '인사',
            jobName: '대사장',
            rankName: '차장',
            status: 1,
            hireAt: '2020-12-01',
            pictureUrl: null
        },
        {
            id: 5,
            name: 'Alice Johnson',
            employeeNumber: '12345678',
            email: 'alice@mail.com',
            phone: '(555) 123-4567',
            departmentName: '영업',
            jobName: '영업관리',
            rankName: '대리',
            status: 1,
            hireAt: '2024-01-15',
            pictureUrl: null
        },
        {
            id: 6,
            name: 'David Lee',
            employeeNumber: '98765432',
            email: 'david@mail.com',
            phone: '(555) 987-6543',
            departmentName: '마케팅',
            jobName: '마케팅기획',
            rankName: '과장',
            status: 1,
            hireAt: '2019-06-20',
            pictureUrl: null
        },
        {
            id: 7,
            name: 'Emma Wilson',
            employeeNumber: '45678901',
            email: 'emma@mail.com',
            phone: '(555) 456-7890',
            departmentName: '백엔드/개발',
            jobName: '개발자',
            rankName: '사원',
            status: 0,
            hireAt: '2023-02-28',
            pictureUrl: null
        }
    ]
}

// 라이프사이클
onMounted(() => {
    loadMembers()
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
</style>