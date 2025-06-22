import { ref, computed } from 'vue'
import { useMemberStore } from '@/stores/memberStore'
import { useAttendanceStore } from '@/stores/attendanceStore'
import { useToast } from '@/composables/useToast'
import dayjs from 'dayjs'

export const useAttendanceDetail = (memberId) => {
    const memberStore = useMemberStore()
    const attendanceStore = useAttendanceStore()
    const { showToast } = useToast()

    // 상태
    const memberData = ref({})
    const attendanceRecords = ref([])
    const attendanceLoading = ref(false)
    const selectedMonth = ref(dayjs().format('YYYY-MM'))

    // 생년월일 포맷팅
    const formattedBirthDate = computed(() => {
        console.log('memberData.value:', memberData.value)
        console.log('memberData.value.birth:', memberData.value.birth)
        if (!memberData.value.birth) return '정보 없음'
        return dayjs(memberData.value.birth).format('YYYY-MM-DD')
    })

    // 근태 통계
    const attendanceStats = computed(() => [
        { label: '출근', count: attendanceRecords.value.filter(r => r.status === 'present').length, color: 'success' },  // 멤버 출근 상태
        { label: '지각', count: attendanceRecords.value.filter(r => r.status === 'late').length, color: 'warning' },
        { label: '조퇴', count: attendanceRecords.value.filter(r => r.status === 'early').length, color: 'info' },
        { label: '결근', count: attendanceRecords.value.filter(r => r.status === 'absent').length, color: 'error' },
        { label: '휴가', count: attendanceRecords.value.filter(r => r.status === 'vacation').length, color: 'secondary' }
    ])

    // 비즈니스 로직: 사원 데이터 로드
    const loadMemberData = async () => {
        try {
            let routerMemberData = null

            // 1. sessionStorage에서 데이터 확인 (새로고침 시에도 유지됨)
            try {
                // eslint-disable-next-line no-undef
                const storedData = sessionStorage.getItem('memberDetailData')
                if (storedData) {
                    routerMemberData = JSON.parse(storedData)
                    console.log('sessionStorage에서 데이터 발견:', routerMemberData)
                    // 사용 후 정리
                    // eslint-disable-next-line no-undef
                    sessionStorage.removeItem('memberDetailData')
                }
            } catch (error) {
                console.warn('sessionStorage 읽기 실패:', error)
            }

            // 2. sessionStorage에 없으면 전역 변수에서 확인 (fallback)
            if (!routerMemberData && globalThis.memberDetailData) {
                routerMemberData = globalThis.memberDetailData
                console.log('전역 변수에서 데이터 발견:', routerMemberData)
                // 사용 후 정리
                delete globalThis.memberDetailData
            }

            console.log('최종 routerMemberData:', routerMemberData)

            if (routerMemberData) {
                // 라우터에서 전달받은 데이터 사용
                memberData.value = { ...routerMemberData }
                console.log('라우터에서 전달받은 사원 데이터:', memberData.value)
                console.log('프로필 이미지 URL 확인:', {
                    profileImageUrl: memberData.value.profileImageUrl,
                    pictureUrl: memberData.value.pictureUrl
                })
            } else {
                // 라우터 데이터가 없으면 전체 사원 목록에서 찾기
                const members = await memberStore.findMembers()
                const member = members.find(m => m.id == memberId.value)

                if (!member) {
                    throw new Error(`ID ${memberId.value}에 해당하는 사원을 찾을 수 없습니다.`)
                }

                memberData.value = member
            }

            // 프로필 이미지 처리
            await loadProfileImage()

            console.log('최종 프로필 이미지:', {
                profileImageUrl: memberData.value.profileImageUrl,
                pictureUrl: memberData.value.pictureUrl
            })
        } catch (error) {
            console.error('사원 정보 로드 실패:', error)
            showToast('사원 정보를 불러오는데 실패했습니다.', 'error')
            throw error
        }
    }

    // 비즈니스 로직: 근태 데이터 로드
    const loadAttendanceData = async () => {
        if (!memberId.value || !selectedMonth.value) return

        attendanceLoading.value = true
        try {
            const [year, month] = selectedMonth.value.split('-')
            const startDate = dayjs(`${year}-${month}-01`).format('YYYY-MM-DD')
            const endDate = dayjs(`${year}-${month}-01`).endOf('month').format('YYYY-MM-DD')

            // 해당 사원의 근태 기록 조회
            const records = await attendanceStore.fetchMemberAttendanceRecords(memberId.value)

            // 선택된 월의 데이터만 필터링
            const filteredRecords = records.filter(record => {
                const recordDate = dayjs(record.workDate || record.createdAt)
                return recordDate.isBetween(startDate, endDate, 'day', '[]')
            })

            // 테이블 데이터 변환
            attendanceRecords.value = filteredRecords.map(record => ({
                id: record.id,
                departmentName: memberData.value.departmentName || '백엔드',
                checkInTime: record.checkInTime || record.createdAt,
                checkOutTime: record.checkOutTime || null,
                position: memberData.value.grade || '시원',
                role: memberData.value.rankName || '미지정',
                job: memberData.value.jobName || '백엔드 개발'
            }))

        } catch (error) {
            console.error('근태 데이터 로드 실패:', error)
            showToast('근태 데이터를 불러오는데 실패했습니다.', 'error')
            attendanceRecords.value = []
        } finally {
            attendanceLoading.value = false
        }
    }

    // 프로필 이미지 로드
    const loadProfileImage = async () => {
        // 이미 프로필 이미지가 있으면 그대로 사용
        if (memberData.value.profileImageUrl || memberData.value.pictureUrl) {
            console.log('기존 프로필 이미지 사용:', memberData.value.profileImageUrl || memberData.value.pictureUrl)
            return
        }

        // 프로필 이미지가 없으면 API를 통해 로드 시도
        try {
            console.log(`사원 ID ${memberData.value.id}의 프로필 이미지 API 로드 시도`)
            await memberStore.fetchProfileImage(memberData.value.id)
            if (memberStore.profileImageUrl) {
                memberData.value.profileImageUrl = memberStore.profileImageUrl
                console.log('API를 통해 프로필 이미지 로드 성공:', memberStore.profileImageUrl)
            }
        } catch (error) {
            console.warn('프로필 이미지 API 로드 실패:', error)
        }
    }

    // 이미지 에러 처리
    const handleImageError = async (event) => {
        console.error('프로필 이미지 로드 에러:', event)
        console.log('현재 이미지 URL:', memberData.value.profileImageUrl || memberData.value.pictureUrl)

        // API를 통해 다시 시도
        try {
            console.log('이미지 에러 발생, API를 통해 재시도')
            await memberStore.fetchProfileImage(memberData.value.id)
            if (memberStore.profileImageUrl) {
                memberData.value.profileImageUrl = memberStore.profileImageUrl
                console.log('API 재시도로 프로필 이미지 로드 성공')
                return
            }
        } catch (error) {
            console.warn('프로필 이미지 API 재시도 실패:', error)
        }

        // 최종적으로 실패하면 기본 아이콘으로 표시
        memberData.value.profileImageUrl = ''
        memberData.value.pictureUrl = ''
    }

    // 이미지 로드 성공 처리
    const handleImageLoad = (event) => {
        if (event && event.target && event.target.src) {
            console.log('프로필 이미지 로드 성공:', event.target.src)
        } else {
            console.log('프로필 이미지 로드 성공 (이벤트 정보 없음)')
        }
    }

    // 수정 처리
    const handleEdit = () => {
        showToast('수정 기능은 준비 중입니다.', 'info')
    }

    return {
        // 상태
        memberData,
        attendanceRecords,
        attendanceLoading,
        selectedMonth,

        // 계산된 값
        formattedBirthDate,
        attendanceStats,

        // 메서드
        loadMemberData,
        loadAttendanceData,
        loadProfileImage,
        handleImageError,
        handleImageLoad,
        handleEdit
    }
} 