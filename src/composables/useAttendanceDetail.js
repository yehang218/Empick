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

    console.log('🗓️ 초기 선택된 월:', selectedMonth.value)

    // 생년월일 포맷팅
    const formattedBirthDate = computed(() => {
        if (!memberData.value.birth) return '정보 없음'
        return dayjs(memberData.value.birth).format('YYYY-MM-DD')
    })

    // 근태 통계
    const attendanceStats = computed(() => {
        const records = attendanceRecords.value
        console.log('📊 통계 계산용 근태 기록:', records.length, '건')

        const presentRecords = records.filter(r => r.status === 'present')
        const lateRecords = records.filter(r => r.status === 'late')
        const earlyRecords = records.filter(r => r.status === 'early')
        const absentRecords = records.filter(r => r.status === 'absent')
        const vacationRecords = records.filter(r => r.status === 'vacation')

        console.log('📊 상태별 기록 개수:', {
            present: presentRecords.length,
            late: lateRecords.length,
            early: earlyRecords.length,
            absent: absentRecords.length,
            vacation: vacationRecords.length
        })

        return [
            { label: '정상출근', count: presentRecords.length, color: 'success' },
            { label: '지각', count: lateRecords.length, color: 'warning' },
            { label: '조퇴', count: earlyRecords.length, color: 'info' },
            { label: '결근', count: absentRecords.length, color: 'error' },
            { label: '휴가', count: vacationRecords.length, color: 'secondary' }
        ]
    })

    // 비즈니스 로직: 사원 데이터 로드
    const loadMemberData = async () => {
        try {
            let routerMemberData = null

            // 1. sessionStorage에서 데이터 확인
            try {
                // eslint-disable-next-line no-undef
                const storedData = sessionStorage.getItem('memberDetailData')
                if (storedData) {
                    routerMemberData = JSON.parse(storedData)
                    console.log('sessionStorage에서 데이터 발견:', routerMemberData)
                    // eslint-disable-next-line no-undef
                    sessionStorage.removeItem('memberDetailData')
                }
            } catch (error) {
                console.warn('sessionStorage 읽기 실패:', error)
            }

            // 2. 전역 변수에서 확인
            if (!routerMemberData && globalThis.memberDetailData) {
                routerMemberData = globalThis.memberDetailData
                console.log('전역 변수에서 데이터 발견:', routerMemberData)
                delete globalThis.memberDetailData
            }

            if (routerMemberData) {
                memberData.value = { ...routerMemberData }
                console.log('라우터에서 전달받은 사원 데이터:', memberData.value)
            } else {
                // 전체 사원 목록에서 찾기
                const members = await memberStore.findMembers()
                const member = members.find(m => m.id == memberId.value)

                if (!member) {
                    throw new Error(`ID ${memberId.value}에 해당하는 사원을 찾을 수 없습니다.`)
                }

                memberData.value = member
            }

            // 프로필 이미지 처리
            await loadProfileImage()

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

            console.log('📅 근태 데이터 조회:', { memberId: memberId.value, startDate, endDate })

            // 해당 사원의 근태 기록 조회
            const apiRecords = await attendanceStore.fetchMemberAttendanceRecords(memberId.value)
            console.log('📊 API에서 받은 원본 데이터:', apiRecords?.length || 0, '건')

            if (apiRecords && apiRecords.length > 0) {
                console.log('📊 API 데이터 샘플:', apiRecords.slice(0, 3))

                // 선택된 월에 해당하는 데이터 필터링
                const filteredRecords = apiRecords.filter(record => {
                    const recordDate = dayjs(record.recordTime || record.createdAt)
                    const selectedMonthStart = dayjs(startDate)
                    const selectedMonthEnd = dayjs(endDate)

                    return recordDate.isValid() &&
                        (recordDate.isAfter(selectedMonthStart, 'day') || recordDate.isSame(selectedMonthStart, 'day')) &&
                        (recordDate.isBefore(selectedMonthEnd, 'day') || recordDate.isSame(selectedMonthEnd, 'day'))
                })

                console.log('📊 필터링된 데이터:', filteredRecords.length, '건')

                // 기존 attendanceCalculator 함수 사용
                const dailyData = attendanceStore.groupAttendanceByDate(filteredRecords)
                console.log('📊 날짜별 그룹핑 결과:', Object.keys(dailyData).length, '일')

                // 테이블용 데이터 변환
                attendanceRecords.value = Object.values(dailyData).map(dayData => {
                    const { checkIn, checkOut } = dayData
                    const workDate = dayData.date

                    // 출근/퇴근 시간
                    const checkInTime = checkIn ? `${workDate}T${checkIn.time}` : null
                    const checkOutTime = checkOut ? `${workDate}T${checkOut.time}` : null

                    // 근무 시간 계산 (기존 함수 사용)
                    let workHours = 0
                    if (checkIn && checkOut) {
                        const workMinutes = attendanceStore.calculateTimeDifferenceInMinutes(checkIn.time, checkOut.time)
                        workHours = Math.round((workMinutes / 60) * 10) / 10
                    }

                    // 근태 상태 결정
                    let status = 'absent'
                    if (checkIn) {
                        const checkInHour = dayjs(checkInTime).hour()
                        const checkInMinute = dayjs(checkInTime).minute()
                        const checkInTotalMinutes = checkInHour * 60 + checkInMinute

                        // 10시 이후 출근 → 지각
                        if (checkInTotalMinutes > 600) { // 10시 = 600분
                            status = 'late'
                        } else if (checkOut) {
                            const checkOutHour = dayjs(checkOutTime).hour()
                            // 17시 이전 퇴근 → 조퇴
                            if (checkOutHour < 17) {
                                status = 'early'
                            } else {
                                status = 'present' // 정상출근
                            }
                        } else {
                            status = 'present' // 출근만 있는 경우
                        }
                    }

                    console.log(`📊 ${workDate} 근태 상태:`, {
                        checkInTime: checkInTime ? dayjs(checkInTime).format('HH:mm:ss') : null,
                        checkOutTime: checkOutTime ? dayjs(checkOutTime).format('HH:mm:ss') : null,
                        workHours,
                        status
                    })

                    return {
                        id: `${workDate}_${memberId.value}`,
                        workDate,
                        checkInTime,
                        checkOutTime,
                        status,
                        workHours,
                        note: ''
                    }
                }).sort((a, b) => dayjs(b.workDate).diff(dayjs(a.workDate))) // 최신순 정렬

                console.log('📊 최종 처리된 근태 데이터:', attendanceRecords.value.length, '건')
            } else {
                console.log('📊 API 데이터가 없음')
                attendanceRecords.value = []
            }

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
        if (memberData.value.profileImageUrl || memberData.value.pictureUrl) {
            console.log('기존 프로필 이미지 사용:', memberData.value.profileImageUrl || memberData.value.pictureUrl)
            return
        }

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

        // 최종 실패 시 빈 문자열로 설정
        memberData.value.profileImageUrl = ''
        memberData.value.pictureUrl = ''
    }

    // 이미지 로드 성공 처리
    const handleImageLoad = (event) => {
        if (event && event.target && event.target.src) {
            console.log('프로필 이미지 로드 성공:', event.target.src)
        }
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
        handleImageLoad
    }
} 