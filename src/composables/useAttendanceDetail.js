import { ref, computed } from 'vue'
import { useMemberStore } from '@/stores/memberStore'
import { useAttendanceStore } from '@/stores/attendanceStore'
import { useToast } from '@/composables/useToast'
import { WORK_TIME_CONFIG, BUSINESS_RULES } from '@/config/attendance'
import dayjs from 'dayjs'

export const useAttendanceDetail = (memberId) => {
    const memberStore = useMemberStore()
    const attendanceStore = useAttendanceStore()
    const { showToast } = useToast()

    // 근태 상태 결정 함수
    const determineAttendanceStatus = (workDate, checkIn, checkOut) => {
        const date = dayjs(workDate)
        const dayOfWeek = date.day()

        // 주말인지 확인 (일요일: 0, 토요일: 6)
        const isWeekend = WORK_TIME_CONFIG.WEEKEND_DAYS.includes(dayOfWeek)

        // 평일인데 출근 기록이 없으면 결근
        if (!isWeekend && !checkIn) {
            return 'absent'
        }

        // 출근 기록이 있으면 시간 기준으로 판단 (주말/평일 관계없이)
        if (checkIn) {
            // 출근 시간 확인
            const checkInTime = dayjs(checkIn.time, 'HH:mm:ss')
            const workStartTime = dayjs(WORK_TIME_CONFIG.WORK_START_TIME, 'HH:mm:ss')
            const lateThreshold = workStartTime.add(BUSINESS_RULES.TARDINESS_RULES.LATE_THRESHOLD_MINUTES, 'minute')

            // 지각 확인 (09:00 + 1분 = 09:01 이후)
            if (checkInTime.isAfter(lateThreshold)) {
                return 'late'
            }

            // 퇴근 기록이 있으면 조퇴 확인
            if (checkOut) {
                const checkOutTime = dayjs(checkOut.time, 'HH:mm:ss')
                const workEndTime = dayjs(WORK_TIME_CONFIG.WORK_END_TIME, 'HH:mm:ss')
                const earlyLeaveThreshold = workEndTime.subtract(BUSINESS_RULES.TARDINESS_RULES.EARLY_LEAVE_THRESHOLD_MINUTES, 'minute')

                // 조퇴 확인 (18:00 - 1분 = 17:59 이전)
                if (checkOutTime.isBefore(earlyLeaveThreshold)) {
                    return 'early'
                }
            }

            // 정상 출근 (주말 출근도 포함)
            return 'present'
        }

        // 주말이면서 출근 기록이 없으면 표시하지 않음 (이 경우는 workDays에 포함되지 않아야 함)
        return 'weekend'
    }

    // 월별 전체 근무일 생성 함수 (오늘까지만, 주말 제외하되 출근 기록이 있으면 포함)
    const generateWorkDaysForMonth = (year, month, apiRecords = []) => {
        const startDate = dayjs(`${year}-${month}-01`)
        const endDate = startDate.endOf('month')
        const today = dayjs().startOf('day') // 오늘 00:00:00

        // 오늘과 월말 중 더 빠른 날짜까지만 처리
        const actualEndDate = today.isBefore(endDate) ? today : endDate

        console.log('📅 날짜 범위:', {
            startDate: startDate.format('YYYY-MM-DD'),
            endDate: endDate.format('YYYY-MM-DD'),
            today: today.format('YYYY-MM-DD'),
            actualEndDate: actualEndDate.format('YYYY-MM-DD')
        })

        const workDays = []

        // 주말에 출근 기록이 있는 날짜들 찾기
        const weekendWorkDays = new Set()
        if (apiRecords && apiRecords.length > 0) {
            apiRecords.forEach(record => {
                const recordDate = dayjs(record.recordTime || record.createdAt)
                if (recordDate.isValid()) {
                    const recordDateStr = recordDate.format('YYYY-MM-DD')
                    const dayOfWeek = recordDate.day()

                    // 주말이지만 출근 기록이 있는 경우
                    if (WORK_TIME_CONFIG.WEEKEND_DAYS.includes(dayOfWeek)) {
                        weekendWorkDays.add(recordDateStr)
                    }
                }
            })
        }

        let currentDate = startDate
        while (currentDate.diff(actualEndDate, 'day') <= 0) { // 오늘까지 포함
            const currentDateStr = currentDate.format('YYYY-MM-DD')
            const dayOfWeek = currentDate.day()

            console.log('📅 처리 중인 날짜:', currentDateStr, '요일:', dayOfWeek, 'diff:', currentDate.diff(actualEndDate, 'day'))

            // 평일이거나, 주말이지만 출근 기록이 있는 경우 포함
            if (!WORK_TIME_CONFIG.WEEKEND_DAYS.includes(dayOfWeek) || weekendWorkDays.has(currentDateStr)) {
                workDays.push(currentDateStr)
                console.log('📅 근무일 추가:', currentDateStr)
            }

            currentDate = currentDate.add(1, 'day')
        }

        console.log('📅 생성된 근무일:', workDays.length, '일 (오늘까지, 주말 출근 포함)')
        console.log('📅 마지막 근무일:', workDays[workDays.length - 1])
        return workDays
    }

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

            // 해당 월의 모든 근무일 생성 (오늘까지만, 주말 출근 기록 있으면 포함)
            const workDays = generateWorkDaysForMonth(year, month, apiRecords || [])
            console.log('📅 해당 월 전체 근무일:', workDays.length, '일')

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

                // 모든 근무일에 대해 근태 데이터 생성
                attendanceRecords.value = workDays.map(workDate => {
                    const dayData = dailyData[workDate]
                    const { checkIn, checkOut } = dayData || {}

                    // 출근/퇴근 시간
                    const checkInTime = checkIn ? `${workDate}T${checkIn.time}` : null
                    const checkOutTime = checkOut ? `${workDate}T${checkOut.time}` : null

                    // 근무 시간 계산
                    let workHours = 0
                    if (checkIn && checkOut) {
                        const workMinutes = attendanceStore.calculateTimeDifferenceInMinutes(checkIn.time, checkOut.time)
                        workHours = Math.round((workMinutes / 60) * 10) / 10
                    }

                    // 정확한 근태 상태 결정
                    const status = determineAttendanceStatus(workDate, checkIn, checkOut)

                    console.log(`📊 ${workDate} 근태 상태:`, {
                        checkInTime: checkInTime ? dayjs(checkInTime).format('HH:mm:ss') : null,
                        checkOutTime: checkOutTime ? dayjs(checkOutTime).format('HH:mm:ss') : null,
                        workHours,
                        status,
                        isWeekend: WORK_TIME_CONFIG.WEEKEND_DAYS.includes(dayjs(workDate).day())
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
                })
                    .filter(record => {
                        const isWeekend = WORK_TIME_CONFIG.WEEKEND_DAYS.includes(dayjs(record.workDate).day())
                        // 주말은 출근 기록이 있는 경우만 포함, 평일은 모두 포함
                        return !isWeekend || (isWeekend && record.checkInTime)
                    })
                    .sort((a, b) => dayjs(b.workDate).diff(dayjs(a.workDate))) // 최신순 정렬

                console.log('📊 최종 처리된 근태 데이터:', attendanceRecords.value.length, '건')
            } else {
                console.log('📊 API 데이터가 없음')

                // 데이터가 없어도 근무일 기준으로 결근 처리 (오늘까지만, 주말 제외)
                attendanceRecords.value = workDays.map(workDate => {
                    const status = determineAttendanceStatus(workDate, null, null)

                    return {
                        id: `${workDate}_${memberId.value}`,
                        workDate,
                        checkInTime: null,
                        checkOutTime: null,
                        status,
                        workHours: 0,
                        note: ''
                    }
                })
                    .filter(record => {
                        const isWeekend = WORK_TIME_CONFIG.WEEKEND_DAYS.includes(dayjs(record.workDate).day())
                        // 주말은 표시하지 않음 (데이터가 없으므로 출근 기록도 없음)
                        return !isWeekend
                    })
                    .sort((a, b) => dayjs(b.workDate).diff(dayjs(a.workDate))) // 최신순 정렬
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