import { ref, computed, watch } from 'vue'
import { useMemberStore } from '@/stores/memberStore'
import { useAttendanceStore } from '@/stores/attendanceStore'
import { useToast } from '@/composables/useToast'

export const useMemberList = () => {
    const memberStore = useMemberStore()
    const attendanceStore = useAttendanceStore()
    const { showToast } = useToast()

    // 상태
    const members = ref([])
    const loading = ref(false)
    const loadingMessage = ref('')
    const error = ref(null)

    // 클라이언트 사이드 페이지네이션 상태
    const currentPage = ref(1)
    const itemsPerPage = ref(10)

    // 검색 및 필터 상태
    const searchQuery = ref('')
    const selectedDepartment = ref(null)
    const selectedStatus = ref('전체')

    // UI 상태
    const expanded = ref([])

    // 비즈니스 로직: 사원별 실제 근태 상태 조회 (배치 처리)
    const enrichMembersWithAttendance = async (memberList) => {
        console.log('근태 정보 로드 시작:', memberList.length, '명')

        // 배치 크기 설정 (동시에 처리할 사원 수)
        const batchSize = 5
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

    // 전체 사원 목록 로드 (클라이언트 사이드 페이지네이션용)
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

            showToast(`${membersWithAttendance.length}명의 사원 정보를 불러왔습니다.`, 'success')

        } catch (error) {
            console.error('사원 데이터 로드 실패:', error)
            members.value = []
            showToast('사원 목록을 불러오는데 실패했습니다.', 'error')
        } finally {
            loading.value = false
            loadingMessage.value = ''
        }
    }

    // 새로고침
    const refreshCurrentPage = () => {
        loadAllMembers()
    }

    // 개별 사원의 프로필 이미지 로드 (필요시 호출)
    const loadSingleProfileImage = async (memberId) => {
        try {
            await memberStore.fetchProfileImage(memberId)
            return memberStore.profileImageUrl
        } catch (error) {
            console.warn(`프로필 이미지 로드 실패 (사원 ID: ${memberId}):`, error)
            return ''
        }
    }

    // 검색 및 필터링 로직
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

    // 부서 옵션
    const createDepartmentOptions = () => {
        return computed(() => {
            const uniqueDepartments = [...new Set(members.value.map(m => m.departmentName).filter(Boolean))]
            return uniqueDepartments.map(dept => ({ title: dept, value: dept }))
        })
    }

    // 이벤트 핸들러
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

    const handleItemsPerPageChange = (newSize) => {
        console.log('페이지 크기 변경됨:', newSize)
        itemsPerPage.value = newSize
        // 페이지 크기 변경 시 첫 페이지로 리셋
        currentPage.value = 1
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

    // 검색어 변경 감지 (디바운싱)
    const setupSearchWatcher = () => {
        return watch(searchQuery, () => {
            handleSearch()
        }, { debounce: 300 })
    }

    return {
        // 상태
        members,
        loading,
        loadingMessage,
        error,
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
        filteredMembers,
        paginatedMembers,

        // 비즈니스 로직
        loadAllMembers,
        refreshCurrentPage,
        enrichMembersWithAttendance,
        loadSingleProfileImage,
        createDepartmentOptions,

        // 이벤트 핸들러
        handleSearch,
        handleDepartmentFilter,
        handleStatusFilter,
        handleItemsPerPageChange,
        handleImageError,

        // 유틸리티
        setupSearchWatcher
    }
} 