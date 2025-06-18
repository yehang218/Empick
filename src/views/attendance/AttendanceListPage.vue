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
            <v-card class="mb-4" elevation="0" style="border: 1px solid #e0e0e0;">
                <v-data-table :headers="headers" :items="paginatedMembers" :loading="loading" hide-default-footer
                    class="member-table" @click:row="handleRowClick">
                    <!-- 아바타 + 이름 컬럼 -->
                    <template #item.profile="{ item }">
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
                    <template #item.department="{ item }">
                        <div>
                            <div class="font-weight-medium">{{ item.departmentName || '-' }}</div>
                            <div class="text-caption text-grey-darken-1">{{ item.jobName || '-' }}</div>
                        </div>
                    </template>

                    <!-- 상태 컬럼 -->
                    <template #item.status="{ item }">
                        <v-chip :color="getStatusColor(item.status)" size="small" variant="flat">
                            {{ getStatusLabel(item.status) }}
                        </v-chip>
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
                        <v-skeleton-loader type="table-row@10" />
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

            <!-- 페이지네이션 -->
            <div class="d-flex justify-center">
                <v-pagination v-model="currentPage" :length="totalPages" :total-visible="7"
                    @update:modelValue="handlePageChange" />
            </div>
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
const currentPage = ref(1)
const itemsPerPage = ref(10)
const loading = ref(false)
const members = ref([])

// 테이블 헤더 정의
const headers = [
    { title: '이름', key: 'profile', sortable: false, width: '200px' },
    { title: '사번', key: 'employeeNumber', sortable: true, width: '120px' },
    { title: '이메일', key: 'email', sortable: false, width: '200px' },
    { title: '연락처', key: 'phone', sortable: false, width: '150px' },
    { title: '부서', key: 'department', sortable: false, width: '150px' },
    { title: '입사일시', key: 'hireAt', sortable: true, width: '120px' },
    { title: '', key: 'actions', sortable: false, width: '60px' }
]

// 상태 옵션
const statusOptions = [
    { title: '전체', value: '전체' },
    { title: '재직', value: 1 },
    { title: '퇴사', value: 0 }
]

// 부서 옵션 (computed)
const departmentOptions = computed(() => {
    const uniqueDepartments = [...new Set(members.value.map(m => m.departmentName).filter(Boolean))]
    return uniqueDepartments.map(dept => ({ title: dept, value: dept }))
})

// 필터링된 사원 목록
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

// 페이지네이션된 사원 목록
const paginatedMembers = computed(() => {
    const start = (currentPage.value - 1) * itemsPerPage.value
    const end = start + itemsPerPage.value
    return filteredMembers.value.slice(start, end)
})

// 총 페이지 수
const totalPages = computed(() =>
    Math.ceil(filteredMembers.value.length / itemsPerPage.value)
)

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
    currentPage.value = 1
}

const handleDepartmentFilter = () => {
    currentPage.value = 1
}

const handleStatusFilter = () => {
    currentPage.value = 1
}

const handlePageChange = (page) => {
    currentPage.value = page
}

const handleRowClick = (event, { item }) => {
    goToMemberDetail(item)
}

const goToMemberDetail = (member) => {
    // 사원 상세 페이지로 이동 (향후 구현)
    router.push(`/orgstructure/members/${member.employeeNumber}`)
}

const getStatusColor = (status) => {
    switch (status) {
        case 1: return 'success'
        case 0: return 'error'
        default: return 'grey'
    }
}

const getStatusLabel = (status) => {
    switch (status) {
        case 1: return '재직'
        case 0: return '퇴사'
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
            hireAt: '2022-03-01',
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
            status: 1,
            hireAt: '2022-03-01',
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
            rankName: '인사',
            status: 1,
            hireAt: '2022-03-01',
            pictureUrl: null
        },
        {
            id: 5,
            name: 'Brooklyn Simmons',
            employeeNumber: '87364523',
            email: 'brooklyns@mail.com',
            phone: '(603) 555-0123',
            departmentName: '영업',
            jobName: '영업관리',
            rankName: '대리',
            status: 1,
            hireAt: '2022-03-01',
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
.member-table :deep(.v-data-table__wrapper) {
    border-radius: 8px;
}

.member-table :deep(tbody tr) {
    cursor: pointer;
    transition: background-color 0.2s ease;
}

.member-table :deep(tbody tr:hover) {
    background-color: #f5f5f5;
}

.member-table :deep(.v-data-table-header th) {
    background-color: #fafafa;
    font-weight: 600;
    border-bottom: 1px solid #e0e0e0;
}

.member-table :deep(.v-data-table__td) {
    border-bottom: 1px solid #f0f0f0;
}
</style>