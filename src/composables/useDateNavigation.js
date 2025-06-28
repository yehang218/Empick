import { ref, computed } from 'vue'

/**
 * 날짜 네비게이션 관련 로직을 담은 composable
 */
export const useDateNavigation = () => {

    // 현재 날짜 상태
    const currentDate = ref(new Date())

    // 계산된 속성들
    const currentYear = computed(() => currentDate.value.getFullYear())
    const currentMonth = computed(() => currentDate.value.getMonth() + 1)

    /**
     * 현재 달인지 확인하는 computed
     */
    const isCurrentMonth = computed(() => {
        const today = new Date()
        return currentYear.value === today.getFullYear() &&
            currentMonth.value === (today.getMonth() + 1)
    })

    /**
     * 이전 월로 이동
     */
    const previousMonth = () => {
        const newDate = new Date(currentDate.value)
        newDate.setMonth(newDate.getMonth() - 1)
        currentDate.value = newDate
    }

    /**
     * 다음 월로 이동
     */
    const nextMonth = () => {
        const newDate = new Date(currentDate.value)
        newDate.setMonth(newDate.getMonth() + 1)
        currentDate.value = newDate
    }

    /**
     * 오늘로 이동
     */
    const goToToday = () => {
        currentDate.value = new Date()
    }

    /**
     * 특정 연월로 이동
     * @param {number} year - 연도
     * @param {number} month - 월 (1-12)
     */
    const goToYearMonth = (year, month) => {
        const newDate = new Date(year, month - 1, 1)
        currentDate.value = newDate
    }

    /**
     * 날짜 포맷팅 유틸리티들
     */
    const formatters = {
        /**
         * YYYY.MM 형식으로 포맷
         */
        yearMonthDot: computed(() =>
            `${currentYear.value}.${currentMonth.value.toString().padStart(2, '0')}`
        ),

        /**
         * YYYY-MM 형식으로 포맷
         */
        yearMonthHyphen: computed(() =>
            `${currentYear.value}-${currentMonth.value.toString().padStart(2, '0')}`
        ),

        /**
         * MM월 형식으로 포맷
         */
        monthText: computed(() =>
            `${currentMonth.value.toString().padStart(2, '0')}월`
        ),

        /**
         * YYYY년 MM월 형식으로 포맷
         */
        fullText: computed(() =>
            `${currentYear.value}년 ${currentMonth.value.toString().padStart(2, '0')}월`
        )
    }

    /**
     * 날짜 관련 유틸리티 함수들
     */
    const utils = {
        /**
         * 해당 월의 첫 번째 날짜
         */
        getFirstDayOfCurrentMonth: () => new Date(currentYear.value, currentMonth.value - 1, 1),

        /**
         * 해당 월의 마지막 날짜
         */
        getLastDayOfCurrentMonth: () => new Date(currentYear.value, currentMonth.value, 0),

        /**
         * 해당 월의 총 일수
         */
        getDaysInCurrentMonth: () => new Date(currentYear.value, currentMonth.value, 0).getDate(),

        /**
         * 현재 월이 특정 월인지 확인
         * @param {number} year - 연도
         * @param {number} month - 월
         * @returns {boolean}
         */
        isSpecificMonth: (year, month) =>
            currentYear.value === year && currentMonth.value === month,

        /**
         * 현재 날짜가 선택된 월 범위 내인지 확인
         * @returns {boolean}
         */
        isTodayInSelectedMonth: () => {
            const today = new Date()
            return today.getFullYear() === currentYear.value &&
                (today.getMonth() + 1) === currentMonth.value
        },

        /**
         * 오늘 날짜 문자열 생성 (YYYY-MM-DD)
         * @returns {string}
         */
        getTodayString: () => {
            const today = new Date()
            return `${today.getFullYear()}-${String(today.getMonth() + 1).padStart(2, '0')}-${String(today.getDate()).padStart(2, '0')}`
        },

        /**
         * 현재 선택된 월의 시작일/종료일 범위
         * @returns {Object} {startDate, endDate}
         */
        getCurrentMonthRange: () => {
            const startDate = new Date(currentYear.value, currentMonth.value - 1, 1)
            const endDate = new Date(currentYear.value, currentMonth.value, 0)
            return { startDate, endDate }
        }
    }

    /**
     * 날짜 네비게이션 상태 정보
     */
    const navigationInfo = computed(() => ({
        year: currentYear.value,
        month: currentMonth.value,
        isCurrentMonth: isCurrentMonth.value,
        canGoBack: true, // 항상 이전으로 갈 수 있음
        canGoForward: true, // 항상 다음으로 갈 수 있음
        displayText: formatters.yearMonthDot.value
    }))

    return {
        // 상태
        currentDate,
        currentYear,
        currentMonth,
        isCurrentMonth,

        // 네비게이션 메서드
        previousMonth,
        nextMonth,
        goToToday,
        goToYearMonth,

        // 포맷터들
        formatters,

        // 유틸리티들
        utils,

        // 네비게이션 정보
        navigationInfo
    }
} 