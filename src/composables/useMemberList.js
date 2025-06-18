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

    // 비즈니스 로직: 사원 목록 로드
    const loadMembers = async () => {
        loading.value = true
        error.value = null

        try {
            const memberList = await memberStore.findMembers()
            const membersWithAttendance = await enrichMembersWithAttendance(memberList)
            const membersWithImages = await loadMemberProfileImages(membersWithAttendance)

            members.value = membersWithImages
            await departmentStore.loadDepartmentList()

            showToast(`${membersWithImages.length}명의 사원 정보를 불러왔습니다.`, 'success')
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

    // 비즈니스 로직: 출근 상태 계산
    const enrichMembersWithAttendance = async (memberList) => {
        // 실제 근태 API를 통해 각 사원의 오늘 출근 상태 조회
        const membersWithAttendance = await Promise.all(
            memberList.map(async (member) => {
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

        return membersWithAttendance
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

    return {
        // 상태
        members,
        loading,
        error,

        // 메서드
        loadMembers,
        createMemberFilter,
        createDepartmentOptions,
        loadSingleProfileImage,

        // 유틸리티
        filterBySearch,
        filterByDepartment,
        filterByStatus
    }
} 