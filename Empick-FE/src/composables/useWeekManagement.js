import { ref, watch } from 'vue'

/**
 * 주차 관리 관련 로직을 담은 composable
 */
export const useWeekManagement = () => {

    /**
     * 현재 주차 번호 계산
     * @param {number} year - 연도
     * @param {number} month - 월
     * @returns {number} 현재 주차 번호 (-1: 현재 월이 아님)
     */
    const getCurrentWeekNumber = (year, month) => {
        const today = new Date()

        // 현재 월이 아니면 -1 반환 (아무 주차도 열지 않음)
        if (today.getFullYear() !== year || today.getMonth() + 1 !== month) {
            return -1
        }

        const currentDate = today.getDate()
        const firstDay = new Date(year, month - 1, 1)
        const firstDayOfWeek = firstDay.getDay()

        // 첫 주의 시작을 월요일로 맞추기
        let weekStart = 1
        if (firstDayOfWeek !== 1) {
            weekStart = 1 - (firstDayOfWeek === 0 ? 6 : firstDayOfWeek - 1)
        }

        // 현재 날짜가 속한 주차 계산
        let weekNumber = 1
        while (weekStart <= currentDate) {
            const weekEnd = weekStart + 6
            if (currentDate >= weekStart && currentDate <= weekEnd) {
                return weekNumber
            }
            weekStart += 7
            weekNumber++
        }

        return 1 // 기본값
    }

    /**
     * 주차 범위 포맷팅
     * @param {Date} startDate - 시작일
     * @param {Date} endDate - 종료일
     * @returns {string} 포맷된 범위 문자열
     */
    const formatWeekRange = (startDate, endDate) => {
        if (!startDate || !endDate) return '-'

        const start = startDate.getDate()
        const end = endDate.getDate()
        return `${start}일 ~ ${end}일`
    }

    /**
     * 주차별 데이터를 관리하는 reactive state와 함수들
     * @param {Object} props - 컴포넌트 props
     * @param {Object} attendanceStore - 출석 store
     * @returns {Object} 주차 관리 관련 상태와 함수들
     */
    const useWeekListState = (props, attendanceStore) => {
        // 아코디언 상태를 관리하는 반응형 데이터
        const weekList = ref([])

        // Store에서 주차별 데이터 가져와서 상태 업데이트
        const updateWeekList = () => {
            if (!props.rawAttendanceRecords.length) {
                weekList.value = []
                return
            }

            const newWeekData = attendanceStore.groupAttendanceByWeek(
                props.year,
                props.month,
                props.rawAttendanceRecords
            )

            const currentWeek = getCurrentWeekNumber(props.year, props.month)

            // 기존 expanded 상태 보존하면서 업데이트
            weekList.value = newWeekData.map((newWeek, index) => {
                const existingWeek = weekList.value[index]

                // 기존 상태가 있으면 보존, 없으면 현재 주차인지 확인
                const shouldExpand = existingWeek
                    ? existingWeek.expanded
                    : newWeek.weekNumber === currentWeek

                return {
                    ...newWeek,
                    expanded: shouldExpand
                }
            })
        }

        // Props 변경 감지
        watch([() => props.year, () => props.month, () => props.rawAttendanceRecords], () => {
            updateWeekList()
        }, { immediate: true, deep: true })

        // 주차 토글
        const toggleWeek = (index) => {
            if (weekList.value[index]) {
                weekList.value[index].expanded = !weekList.value[index].expanded
            }
        }

        return {
            weekList,
            updateWeekList,
            toggleWeek
        }
    }

    /**
     * 날짜 유틸리티 함수들
     */
    const dateUtils = {
        /**
         * 월의 첫 번째 날짜 가져오기
         * @param {number} year - 연도
         * @param {number} month - 월
         * @returns {Date} 첫 번째 날짜
         */
        getFirstDayOfMonth: (year, month) => new Date(year, month - 1, 1),

        /**
         * 월의 마지막 날짜 가져오기
         * @param {number} year - 연도
         * @param {number} month - 월
         * @returns {Date} 마지막 날짜
         */
        getLastDayOfMonth: (year, month) => new Date(year, month, 0),

        /**
         * 해당 월의 총 일수 가져오기
         * @param {number} year - 연도
         * @param {number} month - 월
         * @returns {number} 총 일수
         */
        getDaysInMonth: (year, month) => new Date(year, month, 0).getDate(),

        /**
         * 해당 월의 근무일수 계산 (주말 제외)
         * @param {number} year - 연도
         * @param {number} month - 월
         * @returns {number} 근무일수
         */
        getWorkDaysInMonth: (year, month) => {
            const firstDay = new Date(year, month - 1, 1)
            const lastDay = new Date(year, month, 0)
            let workDays = 0

            for (let date = new Date(firstDay); date <= lastDay; date.setDate(date.getDate() + 1)) {
                const dayOfWeek = date.getDay()
                // 일요일(0)과 토요일(6)이 아닌 경우만 근무일로 계산
                if (dayOfWeek !== 0 && dayOfWeek !== 6) {
                    workDays++
                }
            }

            return workDays
        },

        /**
         * 두 날짜 사이의 일수 계산
         * @param {Date} startDate - 시작일
         * @param {Date} endDate - 종료일
         * @returns {number} 일수
         */
        getDaysBetween: (startDate, endDate) => {
            const timeDiff = endDate.getTime() - startDate.getTime()
            return Math.ceil(timeDiff / (1000 * 3600 * 24))
        },

        /**
         * 날짜가 오늘인지 확인
         * @param {Date} date - 확인할 날짜
         * @returns {boolean} 오늘 여부
         */
        isToday: (date) => {
            const today = new Date()
            return date.getDate() === today.getDate() &&
                date.getMonth() === today.getMonth() &&
                date.getFullYear() === today.getFullYear()
        },

        /**
         * 날짜가 현재 월인지 확인
         * @param {Date} date - 확인할 날짜
         * @param {number} year - 연도
         * @param {number} month - 월
         * @returns {boolean} 현재 월 여부
         */
        isCurrentMonth: (date, year, month) => {
            return date.getFullYear() === year && date.getMonth() === month - 1
        }
    }

    return {
        // 주차 관련 함수들
        getCurrentWeekNumber,
        formatWeekRange,
        useWeekListState,

        // 날짜 유틸리티
        dateUtils
    }
} 