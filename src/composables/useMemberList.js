import { ref, computed } from 'vue'
import { useMemberStore } from '@/stores/memberStore'
import { useDepartmentStore } from '@/stores/departmentStore'
import { useAttendanceStore } from '@/stores/attendanceStore'
import { useToast } from '@/composables/useToast'

export const useMemberList = () => {
    const memberStore = useMemberStore()
    const departmentStore = useDepartmentStore()
    const attendanceStore = useAttendanceStore()
    const { showToast } = useToast()

    // 상태
    const members = ref([])
    const loading = ref(false)
    const error = ref(null)

    // 페이징 상태
    const currentPage = ref(1)
    const pageSize = ref(10)
    const totalMembers = ref(0)
    const totalPages = ref(0)
    const hasNextPage = ref(false)
    const hasPrevPage = ref(false)

    // 비즈니스 로직: 페이징된 사원 목록 로드
    const loadMembers = async (page = 1, forceRefresh = false) => {
        loading.value = true
        error.value = null

        try {
            console.log('페이징된 사원 목록 로드 시작:', { page, pageSize: pageSize.value, forceRefresh })
            // eslint-disable-next-line no-undef
            const startTime = performance.now()

            // 페이징된 사원 목록 가져오기
            const result = await memberStore.findMembersPaginated(page, pageSize.value, forceRefresh)
            // eslint-disable-next-line no-undef
            console.log('페이징 API 호출 완료:', performance.now() - startTime, 'ms')

            // 페이징 상태 업데이트
            currentPage.value = result.pagination.currentPage
            totalMembers.value = result.pagination.totalMembers
            totalPages.value = result.pagination.totalPages
            hasNextPage.value = result.pagination.hasNextPage
            hasPrevPage.value = result.pagination.hasPrevPage

            // 병렬 처리로 성능 개선
            const [membersWithAttendance] = await Promise.all([
                enrichMembersWithAttendance(result.members),
                departmentStore.loadDepartmentList()
            ])
            // eslint-disable-next-line no-undef
            console.log('병렬 처리 완료:', performance.now() - startTime, 'ms')

            const membersWithImages = await loadMemberProfileImages(membersWithAttendance)
            // eslint-disable-next-line no-undef
            console.log('이미지 로드 완료:', performance.now() - startTime, 'ms')

            members.value = membersWithImages

            const loadType = forceRefresh ? '새로고침' : '페이징'
            showToast(`${membersWithImages.length}명의 사원 정보를 불러왔습니다. (${loadType}, ${currentPage.value}/${totalPages.value} 페이지)`, 'success')
            // eslint-disable-next-line no-undef
            console.log('전체 로드 완료:', performance.now() - startTime, 'ms')

            return membersWithImages
        } catch (err) {
            error.value = err
            showToast('사원 목록을 불러오는데 실패했습니다.', 'error')
            members.value = []
            throw err
        } finally {
            loading.value = false
        }
    }

    // 비즈니스 로직: 출근 상태 계산 (배치 처리로 최적화)
    const enrichMembersWithAttendance = async (memberList) => {
        console.log('근태 정보 로드 시작:', memberList.length, '명')

        // 배치 크기 설정 (동시에 처리할 사원 수)
        const batchSize = 10
        const batches = []

        for (let i = 0; i < memberList.length; i += batchSize) {
            batches.push(memberList.slice(i, i + batchSize))
        }

        console.log('배치 처리:', batches.length, '개 배치')

        // 배치별로 순차 처리 (서버 부하 방지)
        const allResults = []
        for (let batchIndex = 0; batchIndex < batches.length; batchIndex++) {
            const batch = batches[batchIndex]
            console.log(`배치 ${batchIndex + 1}/${batches.length} 처리 중...`)

            const batchResults = await Promise.all(
                batch.map(async (member) => {
                    try {
                        // 각 사원의 오늘 출근 기록 조회
                        const todayAttendance = await attendanceStore.fetchMemberAttendanceRecords(member.id, {
                            silent: true // 에러 시 토스트 표시 안함
                        })

                        let status = -1 // 기본값: 기록없음

                        if (todayAttendance && todayAttendance.length > 0) {
                            // 오늘 날짜 기준으로 출근 기록 확인
                            const today = new Date().toISOString().split('T')[0]
                            const todayRecord = todayAttendance.find(record => {
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
                await new Promise(resolve => setTimeout(resolve, 100))
            }
        }

        console.log('근태 정보 로드 완료:', allResults.length, '명')
        return allResults
    }

    // 프로필 이미지 로딩
    const loadMemberProfileImages = async (memberList) => {
        return memberList.map((member) => {
            console.log(`사원 ${member.name}의 pictureUrl:`, member.pictureUrl)

            // pictureUrl이 있으면 그대로 사용, 없으면 API로 이미지 로드
            if (member.pictureUrl) {
                member.profileImageUrl = member.pictureUrl
            } else {
                // 개별 이미지 로드는 필요시에만 (성능 고려)
                member.profileImageUrl = ''
            }
            return member
        })
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
    const createMemberFilter = (searchQuery, selectedDepartment, selectedStatus) => {
        return computed(() => {
            let result = [...members.value]

            // 검색 필터
            if (searchQuery.value) {
                result = filterBySearch(result, searchQuery.value)
            }

            // 부서 필터
            if (selectedDepartment.value) {
                result = filterByDepartment(result, selectedDepartment.value)
            }

            // 상태 필터
            if (selectedStatus.value && selectedStatus.value !== '전체') {
                result = filterByStatus(result, selectedStatus.value)
            }
            return result
        })
    }

    // 검색 필터 로직
    const filterBySearch = (members, query) => {
        const searchTerm = query.toLowerCase()
        return members.filter(member =>
            member.name?.toLowerCase().includes(searchTerm) ||
            member.employeeNumber?.toString().includes(searchTerm) ||
            member.email?.toLowerCase().includes(searchTerm)
        )
    }

    // 부서 필터 로직
    const filterByDepartment = (members, department) => {
        return members.filter(member => member.departmentName === department)
    }

    // 상태 필터 로직
    const filterByStatus = (members, status) => {
        return members.filter(member => {
            // 타입 변환을 통한 안전한 비교
            const memberStatus = Number(member.status)
            const filterStatus = Number(status)
            return memberStatus === filterStatus
        })
    }

    // 부서 옵션 생성
    const createDepartmentOptions = () => {
        return computed(() => {
            // departmentStore에서 부서 목록 가져오기
            if (departmentStore.departmentList.length > 0) {
                return departmentStore.departmentList.map(dept => ({
                    title: dept.name || dept.departmentName,
                    value: dept.name || dept.departmentName
                }))
            }

            // fallback: 현재 사원들의 부서명으로 생성
            const uniqueDepartments = [...new Set(members.value.map(m => m.departmentName).filter(Boolean))]
            return uniqueDepartments.map(dept => ({ title: dept, value: dept }))
        })
    }

    // 페이징 관련 함수들
    const goToPage = async (page) => {
        if (page >= 1 && page <= totalPages.value && page !== currentPage.value) {
            await loadMembers(page)
        }
    }

    const goToNextPage = async () => {
        if (hasNextPage.value) {
            await goToPage(currentPage.value + 1)
        }
    }

    const goToPrevPage = async () => {
        if (hasPrevPage.value) {
            await goToPage(currentPage.value - 1)
        }
    }

    const changePageSize = async (newPageSize) => {
        if (newPageSize !== pageSize.value) {
            pageSize.value = newPageSize
            memberStore.setPageSize(newPageSize)
            // 첫 페이지로 이동
            await loadMembers(1)
        }
    }

    const refreshCurrentPage = async () => {
        await loadMembers(currentPage.value, true)
    }

    return {
        // 상태
        members,
        loading,
        error,

        // 페이징 상태
        currentPage,
        pageSize,
        totalMembers,
        totalPages,
        hasNextPage,
        hasPrevPage,

        // 메서드
        loadMembers,
        goToPage,
        goToNextPage,
        goToPrevPage,
        changePageSize,
        refreshCurrentPage,
        createMemberFilter,
        createDepartmentOptions,
        loadSingleProfileImage,

        // 유틸리티
        filterBySearch,
        filterByDepartment,
        filterByStatus
    }
} 