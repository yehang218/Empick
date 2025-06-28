import * as XLSX from 'xlsx'

/**
 * 시간 문자열을 분 단위로 변환
 * @param {string} timeStr - "HH:MM:SS" 형식의 시간 문자열
 * @returns {number} 분 단위 시간
 */
const timeToMinutes = (timeStr) => {
    const [hours, minutes] = timeStr.split(':').map(Number)
    return hours * 60 + minutes
}

/**
 * 분을 시간 문자열로 변환
 * @param {number} minutes - 분 단위 시간
 * @returns {string} "Xh Ym" 형식의 문자열
 */
const minutesToTimeString = (minutes) => {
    const hours = Math.floor(minutes / 60)
    const mins = minutes % 60
    return `${hours}h ${mins}m`
}

/**
 * 근무시간 계산
 * @param {string} checkIn - 출근시간 "HH:MM:SS"
 * @param {string} checkOut - 퇴근시간 "HH:MM:SS"
 * @returns {object} 근무시간 정보
 */
const calculateWorkHours = (checkIn, checkOut) => {
    const checkInMinutes = timeToMinutes(checkIn)
    const checkOutMinutes = timeToMinutes(checkOut)

    let totalMinutes = checkOutMinutes - checkInMinutes
    if (totalMinutes < 0) {
        totalMinutes += 24 * 60 // 다음날로 넘어간 경우
    }

    // 휴게시간 계산 (8시간 이상 근무시 1시간 휴게)
    const breakMinutes = totalMinutes >= 8 * 60 ? 60 : 0
    const workMinutes = totalMinutes - breakMinutes

    // 기본 근무시간 (8시간 또는 실제 근무시간 중 작은 값)
    const regularMinutes = Math.min(workMinutes, 8 * 60)

    // 연장 근무시간 (8시간 초과분)
    const overtimeMinutes = Math.max(0, workMinutes - 8 * 60)

    // 야간 근무시간 계산 (22:00-06:00)
    const nightStart = 22 * 60 // 22:00
    const nightEnd = 6 * 60 // 06:00
    let nightMinutes = 0

    // 야간 근무시간 계산 로직 (단순화)
    if (checkInMinutes >= nightStart || checkOutMinutes <= nightEnd) {
        nightMinutes = Math.min(workMinutes, 8 * 60) // 최대 8시간
    }

    return {
        total: minutesToTimeString(totalMinutes),
        regular: minutesToTimeString(regularMinutes),
        overtime: minutesToTimeString(overtimeMinutes),
        night: minutesToTimeString(nightMinutes),
        breakTime: minutesToTimeString(breakMinutes)
    }
}

/**
 * 날짜별 근무 데이터 그룹핑
 * @param {Array} records - 근태 기록 배열
 * @returns {object} 날짜별 그룹핑된 데이터
 */
const groupRecordsByDate = (records) => {
    const dailyData = {}

    records.forEach(record => {
        if (!record.recordTime) return

        const date = record.recordTime.split('T')[0]
        const time = record.recordTime.split('T')[1].substring(0, 8)

        if (!dailyData[date]) {
            dailyData[date] = { date, checkIn: null, checkOut: null }
        }

        if (record.attendanceCategoryId === 1) { // 출근
            dailyData[date].checkIn = time
        } else if (record.attendanceCategoryId === 2) { // 퇴근
            dailyData[date].checkOut = time
        }
    })

    return dailyData
}

/**
 * 일별 근태 시트 데이터 생성
 * @param {object} dailyData - 날짜별 그룹핑된 데이터
 * @param {number} year - 연도
 * @param {number} month - 월
 * @returns {Array} 시트 데이터 배열
 */
const createDailySheetData = (dailyData, year, month) => {
    const headers = [
        '날짜',
        '출근시간',
        '퇴근시간',
        '총 근무시간',
        '기본 근무시간',
        '연장 근무시간',
        '야간 근무시간',
        '휴게시간'
    ]

    const rows = [headers]
    const daysInMonth = new Date(year, month, 0).getDate()

    for (let day = 1; day <= daysInMonth; day++) {
        const dateStr = `${year}-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}`
        const dayData = dailyData[dateStr]

        if (dayData && dayData.checkIn && dayData.checkOut) {
            const workHours = calculateWorkHours(dayData.checkIn, dayData.checkOut)

            const row = [
                `${month}/${day}`,
                dayData.checkIn,
                dayData.checkOut,
                workHours.total,
                workHours.regular,
                workHours.overtime,
                workHours.night,
                workHours.breakTime
            ]
            rows.push(row)
        } else {
            const row = [
                `${month}/${day}`,
                '-',
                '-',
                '0h 0m',
                '0h 0m',
                '0h 0m',
                '0h 0m',
                '0h 0m'
            ]
            rows.push(row)
        }
    }

    return rows
}

/**
 * 월간 요약 시트 데이터 생성
 * @param {object} summary - 요약 정보
 * @param {object} dailyData - 날짜별 데이터
 * @param {number} year - 연도
 * @param {number} month - 월
 * @returns {Array} 시트 데이터 배열
 */
const createSummarySheetData = (summary, dailyData, year, month) => {
    const totalMinutes = summary.workingHours.hours * 60 + summary.workingHours.minutes
    const targetMinutes = summary.targetHours.hours * 60 + summary.targetHours.minutes
    const progressRate = targetMinutes > 0 ? (totalMinutes / targetMinutes * 100).toFixed(1) : 0
    const avgDailyHours = Object.keys(dailyData).length > 0
        ? Math.round(totalMinutes / Object.keys(dailyData).length / 60 * 10) / 10
        : 0

    return [
        ['항목', '값'],
        ['', ''],
        ['=== 기본 정보 ===', ''],
        ['대상 월', `${year}년 ${month}월`],
        ['총 근무일수', `${Object.keys(dailyData).length}일`],
        ['', ''],
        ['=== 근무시간 현황 ===', ''],
        ['총 근무시간', `${summary.workingHours.hours}h ${summary.workingHours.minutes}m`],
        ['할당 근무시간', `${summary.targetHours.hours}h ${summary.targetHours.minutes}m`],
        ['연장 근무시간', `${summary.overtimeHours}h`],
        ['야간 근무시간', `${summary.nightHours}h`],
        ['총 휴게시간', `${Math.floor(summary.totalBreakMinutes / 60)}h ${summary.totalBreakMinutes % 60}m`],
        ['', ''],
        ['=== 추가 정보 ===', ''],
        ['진행률', `${progressRate}%`],
        ['평균 일일 근무시간', `${avgDailyHours}h`]
    ]
}

/**
 * 엑셀 워크시트 생성
 * @param {Array} data - 시트 데이터
 * @param {Array} columnWidths - 컬럼 너비 배열
 * @returns {object} 엑셀 워크시트
 */
const createWorksheet = (data, columnWidths) => {
    const worksheet = XLSX.utils.aoa_to_sheet(data)

    if (columnWidths) {
        worksheet['!cols'] = columnWidths.map(width => ({ width }))
    }

    return worksheet
}

/**
 * 근태 데이터 엑셀 다운로드
 * @param {Array} records - 근태 기록 배열
 * @param {object} summary - 요약 정보
 * @param {number} year - 연도
 * @param {number} month - 월
 * @returns {Promise<boolean>} 성공 여부
 */
export const downloadAttendanceExcel = async (records, summary, year, month) => {
    try {
        // 데이터 유효성 검사
        if (!records || records.length === 0) {
            throw new Error('다운로드할 근태 데이터가 없습니다.')
        }

        // 워크북 생성
        const workbook = XLSX.utils.book_new()

        // 날짜별 데이터 그룹핑
        const dailyData = groupRecordsByDate(records)

        // 일별 근태 시트 생성
        const dailySheetData = createDailySheetData(dailyData, year, month)
        const dailyWorksheet = createWorksheet(dailySheetData, [
            10, // 날짜
            12, // 출근시간
            12, // 퇴근시간
            15, // 총 근무시간
            15, // 기본 근무시간
            15, // 연장 근무시간
            15, // 야간 근무시간
            12  // 휴게시간
        ])

        XLSX.utils.book_append_sheet(workbook, dailyWorksheet, '일별근태현황')

        // 월간 요약 시트 생성
        const summarySheetData = createSummarySheetData(summary, dailyData, year, month)
        const summaryWorksheet = createWorksheet(summarySheetData, [25, 30])

        XLSX.utils.book_append_sheet(workbook, summaryWorksheet, '월간요약')

        // 엑셀 파일 다운로드
        const fileName = `근태현황_${year}년_${month}월.xlsx`
        XLSX.writeFile(workbook, fileName)

        console.log('엑셀 파일 다운로드 완료:', fileName)
        return true

    } catch (error) {
        console.error('엑셀 파일 생성 실패:', error)
        alert(error.message || '엑셀 파일 생성에 실패했습니다. 다시 시도해주세요.')
        return false
    }
} 