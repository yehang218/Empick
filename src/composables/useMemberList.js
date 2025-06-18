import { ref, computed } from 'vue'
import { useMemberStore } from '@/stores/memberStore'
import { useDepartmentStore } from '@/stores/departmentStore'
import { useToast } from '@/composables/useToast'

export const useMemberList = () => {
    const memberStore = useMemberStore()
    const departmentStore = useDepartmentStore()
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

            members.value = membersWithAttendance
            await departmentStore.loadDepartmentList()

            showToast(`${membersWithAttendance.length}명의 사원 정보를 불러왔습니다.`, 'success')
            return membersWithAttendance
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
        // TODO: 실제 근태 API 연동 시 이 로직을 수정
        return memberList.map((member) => ({
            ...member,
            status: Math.random() > 0.3 ? 1 : 0
        }))
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
        return members.filter(member => member.status === status)
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

        // 유틸리티
        filterBySearch,
        filterByDepartment,
        filterByStatus
    }
} 