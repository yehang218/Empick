<template>
    <div class="week-summary-card">
        <!-- 테이블 헤더 -->
        <div class="table-header">
            <div class="header-cell date-cell">일자</div>
            <div class="header-cell time-cell">업무시작</div>
            <div class="header-cell time-cell">업무종료</div>
            <div class="header-cell duration-cell">근무시간</div>
            <div class="header-cell status-cell">근무상태</div>
            <!-- <div class="header-cell status-cell">승인요청</div> -->
        </div>

        <!-- 테이블 데이터 -->
        <div class="table-body">
            <div v-for="(day, index) in weekData" :key="index" class="table-row" :class="{ 'selected': day.selected }">
                <div class="data-cell date-cell">
                    <div class="date-info">
                        <span class="date-number">{{ day.date }}</span>
                        <span class="date-label">일</span>
                    </div>
                </div>

                <div class="data-cell time-cell">
                    <span class="time-text">
                        {{ day.startTime }}
                    </span>
                </div>

                <div class="data-cell time-cell">
                    <span class="time-text">
                        {{ day.endTime }}
                    </span>
                </div>

                <div class="data-cell duration-cell">
                    <div class="duration-main">{{ day.totalDuration }}</div>
                    <div class="duration-detail">
                        기본 {{ day.regularHours }} / 연장 {{ day.overtimeHours }} / 야간 {{ day.nightHours }}
                    </div>
                </div>

                <!-- 🔥 NEW: 근무상태 컬럼 -->
                <div class="data-cell status-cell">
                    <div class="work-status-badges">
                        <v-chip v-if="getDailyValidation(day).exceedsLimit" color="error" size="small" variant="flat">
                            <v-icon start size="small">mdi-alert-circle</v-icon>
                            한도초과
                        </v-chip>
                        <v-chip
                            v-else-if="getDailyValidation(day).hasOvertime && getDailyValidation(day).overtimeHours > 0"
                            color="info" size="small" variant="flat">
                            <v-icon start size="small">mdi-clock-plus-outline</v-icon>
                            연장근무 {{ getDailyValidation(day).overtimeHours }}h
                        </v-chip>
                        <v-chip v-else-if="getDailyValidation(day).isFullDay" color="success" size="small"
                            variant="flat">
                            <v-icon start size="small">mdi-check-circle</v-icon>
                            적정근무
                        </v-chip>
                        <v-chip v-else-if="getDailyValidation(day).isInsufficient" color="warning" size="small"
                            variant="flat">
                            <v-icon start size="small">mdi-clock-minus-outline</v-icon>
                            시간부족
                        </v-chip>
                        <v-chip v-else color="grey" size="small" variant="flat">
                            <v-icon start size="small">mdi-minus</v-icon>
                            데이터없음
                        </v-chip>
                    </div>
                </div>

                <!-- <div class="data-cell status-cell">
                    <v-btn v-if="day.needsApproval" size="small" color="primary" @click="requestApproval(day)">
                        승인요청
                    </v-btn>
                </div> -->
            </div>
        </div>

        <!-- 타임라인 차트 -->
        <div class="timeline-chart">
            <div class="time-scale">
                <div v-for="hour in 24" :key="hour - 1" class="time-marker">
                    {{ (hour - 1).toString().padStart(2, '0') }}
                </div>
            </div>

            <div class="timeline-content" @mousemove="onTimelineMouseMove" @mouseleave="hideTimelineTooltip">
                <!-- 모눈종이 배경 -->
                <div class="grid-background">
                    <!-- 세로선 (시간 구분선) -->
                    <div v-for="hour in 24" :key="`vertical-${hour - 1}`" class="grid-line vertical"
                        :style="{ left: `${((hour - 1) / 24) * 100}%` }"></div>
                    <!-- 가로선 -->
                    <div class="grid-line horizontal" style="top: 25%"></div>
                    <div class="grid-line horizontal" style="top: 50%"></div>
                    <div class="grid-line horizontal" style="top: 75%"></div>
                </div>

                <!-- 호버 시 빨간 세로선 -->
                <div v-if="hoverTime.show" class="hover-line" :style="{ left: hoverTime.linePosition + '%' }"></div>

                <!-- 주차의 마지막 날(최신일)만 표시 -->
                <div v-if="latestDay" class="timeline-row">
                    <!-- 날짜 표시 -->
                    <div class="timeline-date-label">
                        {{ latestDay.date }}일 (최신)
                    </div>

                    <!-- 근무시간 바 (출근과 퇴근이 모두 있는 경우) -->
                    <div v-if="latestDay.startTime !== '-' && latestDay.endTime !== '-'" class="work-bar"
                        :style="getWorkBarStyle(latestDay)" @mouseenter="showWorkBarTooltip(latestDay, $event)"
                        @mouseleave="hideWorkBarTooltip">
                    </div>
                    <!-- 출근만 있는 경우 현재 시간까지 진행 바로 표시 -->
                    <div v-else-if="latestDay.startTime !== '-'" class="work-bar ongoing"
                        :style="getOngoingWorkBarStyle(latestDay)" @mouseenter="showWorkBarTooltip(latestDay, $event)"
                        @mouseleave="hideWorkBarTooltip">
                    </div>
                </div>
                <!-- 데이터가 없는 경우 빈 타임라인 표시 -->
                <div v-else class="timeline-row">
                    <div class="no-data-message">근무 데이터가 없습니다</div>
                </div>

            </div>
        </div>
    </div>

    <!-- 타임라인 호버 툴팁 (body에 직접 렌더링) -->
    <Teleport to="body">
        <div v-if="hoverTime.show" class="timeline-tooltip-global"
            :style="{ left: hoverTime.x + 'px', top: hoverTime.y + 'px' }">
            {{ hoverTime.time }}
        </div>
    </Teleport>

    <!-- 근무시간 바 툴팁 (body에 직접 렌더링) -->
    <Teleport to="body">
        <div v-if="workBarTooltip.show" class="work-bar-tooltip-global"
            :style="{ left: workBarTooltip.x + 'px', top: workBarTooltip.y + 'px' }">
            <div class="tooltip-content">
                <div class="tooltip-item">
                    <span class="tooltip-label">출근:</span>
                    <span class="tooltip-value">{{ workBarTooltip.startTime }}</span>
                </div>
                <div v-if="workBarTooltip.endTime !== '-'" class="tooltip-item">
                    <span class="tooltip-label">퇴근:</span>
                    <span class="tooltip-value">{{ workBarTooltip.endTime }}</span>
                </div>
                <div class="tooltip-item">
                    <span class="tooltip-label">근무시간:</span>
                    <span class="tooltip-value">{{ workBarTooltip.duration }}</span>
                </div>
            </div>
        </div>
    </Teleport>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useWorkTimeValidation } from '@/composables/useWorkTimeValidation'

// Props
const props = defineProps({
    weekData: {
        type: Array,
        default: () => [
            {
                date: '05',
                startTime: '07:45:00',
                endTime: '17:50:00',
                totalDuration: '9h 5m 0s',
                regularHours: '9h 5m 0s',
                overtimeHours: '0h 0m 0s',
                nightHours: '0h 0m 0s',
                needsApproval: false,
                selected: true,
                breakTime: true
            }
        ]
    }
})

// 주차의 마지막 날(최신일) 계산
const latestDay = computed(() => {
    if (!props.weekData || props.weekData.length === 0) {
        return null
    }

    // 날짜 기준으로 정렬하여 마지막 날 찾기
    const sortedDays = [...props.weekData].sort((a, b) => {
        const dateA = parseInt(a.date)
        const dateB = parseInt(b.date)
        return dateB - dateA // 내림차순 정렬 (최신일이 먼저)
    })

    return sortedDays[0] // 가장 최신일 반환
})

// Composables
const { validateDailyWorkTime } = useWorkTimeValidation()

// 🔥 REFACTORED: 일별 근무시간 검증 함수 (휴게시간 포함)
const getDailyValidation = (day) => {
    return validateDailyWorkTime(day)
}

// 현재 시간을 주기적으로 업데이트하기 위한 reactive 변수
const currentTime = ref(new Date())
let timeUpdateInterval = null

// 호버 상태 관리
const hoverTime = ref({
    show: false,
    x: 0,
    y: 0,
    linePosition: 0,
    time: ''
})

const workBarTooltip = ref({
    show: false,
    x: 0,
    y: 0,
    startTime: '',
    endTime: '',
    duration: ''
})

// 1분마다 현재 시간 업데이트
onMounted(() => {
    const updateTime = () => {
        currentTime.value = new Date()
    }

    // eslint-disable-next-line no-undef
    timeUpdateInterval = setInterval(updateTime, 60000) // 1분마다 업데이트
})

onUnmounted(() => {
    if (timeUpdateInterval) {
        // eslint-disable-next-line no-undef
        clearInterval(timeUpdateInterval)
    }
})

// 근무시간 바 스타일 계산
const getWorkBarStyle = (day) => {
    // 시간 형식이 올바른지 확인
    if (!day.startTime || !day.endTime || day.startTime === '-' || day.endTime === '-') {
        return { left: '0%', width: '0%' }
    }

    const startHour = parseFloat(day.startTime.split(':')[0]) + parseFloat(day.startTime.split(':')[1]) / 60
    const endHour = parseFloat(day.endTime.split(':')[0]) + parseFloat(day.endTime.split(':')[1]) / 60

    const left = (startHour / 24) * 100
    const width = ((endHour - startHour) / 24) * 100

    return {
        left: `${left}%`,
        width: `${width}%`
    }
}

// 출근만 있는 경우 현재 시간까지 진행 바 스타일 계산
const getOngoingWorkBarStyle = (day) => {
    if (!day.startTime || day.startTime === '-') {
        return { left: '0%', width: '0%' }
    }

    const startHour = parseFloat(day.startTime.split(':')[0]) + parseFloat(day.startTime.split(':')[1]) / 60

    // 현재 시간 계산 (reactive 변수 사용)
    const now = currentTime.value
    const currentHour = now.getHours() + now.getMinutes() / 60

    // 오늘 날짜인지 확인 (진행중인 근무는 오늘만 해당)
    const today = new Date()
    const dayNumber = parseInt(day.date)
    const isToday = today.getDate() === dayNumber

    let endHour = currentHour

    // 오늘이 아니거나 출근 시간이 현재 시간보다 늦으면 출근 시간만 표시
    if (!isToday || startHour > currentHour) {
        endHour = startHour + 0.1 // 최소한의 너비로 점처럼 표시
    }

    const left = (startHour / 24) * 100
    const width = ((endHour - startHour) / 24) * 100

    return {
        left: `${left}%`,
        width: `${Math.max(width, 1)}%` // 최소 1% 너비 보장
    }
}

// 타임라인 마우스 이동 처리 (배경 - 실시간 시간 표시)
const onTimelineMouseMove = (event) => {
    const rect = event.currentTarget.getBoundingClientRect()
    const x = event.clientX - rect.left
    const percentage = Math.max(0, Math.min(100, (x / rect.width) * 100))

    // 24시간 기준으로 시간 계산 (실시간)
    const totalMinutes = (percentage / 100) * 24 * 60
    const hours = Math.floor(totalMinutes / 60)
    const minutes = Math.floor(totalMinutes % 60)

    // 마우스 위치를 화면 좌표로 저장
    hoverTime.value = {
        show: true,
        x: event.clientX,
        y: event.clientY - 50, // 마우스 위쪽에 표시
        linePosition: percentage,
        time: `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}`
    }
}

// 타임라인 툴팁 숨기기
const hideTimelineTooltip = () => {
    hoverTime.value.show = false
}

// 근무시간 바 툴팁 표시
const showWorkBarTooltip = (day, event) => {
    const rect = event.currentTarget.getBoundingClientRect()

    // 타임라인 배경 툴팁 숨기기
    hoverTime.value.show = false

    workBarTooltip.value = {
        show: true,
        x: rect.left + rect.width / 2,
        y: rect.top - 10,
        startTime: day.startTime ? day.startTime.substring(0, 5) : '-',
        endTime: day.endTime && day.endTime !== '-' ? day.endTime.substring(0, 5) : '-',
        duration: day.totalDuration
    }
}

// 근무시간 바 툴팁 숨기기
const hideWorkBarTooltip = () => {
    workBarTooltip.value.show = false
}

// 승인 요청 처리 (주석 처리)
// const requestApproval = (day) => {
//     console.log('승인 요청:', day)
// }

// Emits
defineEmits([/* 'requestApproval', */ 'editTime'])
</script>

<style lang="scss" scoped>
.week-summary-card {
    background: white;
    border: 1px solid #e0e0e0;
    border-radius: 8px;
    overflow: visible;
}

.table-header {
    display: flex;
    background: #f5f5f5;
    border-bottom: 1px solid #e0e0e0;

    .header-cell {
        padding: 12px 16px;
        font-weight: 600;
        color: #333;
        text-align: center;
        border-right: 1px solid #e0e0e0;

        &:last-child {
            border-right: none;
        }

        &.date-cell {
            width: 80px;
            min-width: 80px;
        }

        &.time-cell {
            width: 120px;
            min-width: 120px;
        }

        &.duration-cell {
            flex: 1;
            text-align: left;
        }

        &.status-cell {
            width: 120px;
            min-width: 120px;
        }
    }
}

.table-body {
    .table-row {
        display: flex;
        border-bottom: 1px solid #f0f0f0;

        &.selected {
            background: #e3f2fd;
        }

        &:hover {
            background: #f9f9f9;
        }

        .data-cell {
            padding: 12px 16px;
            border-right: 1px solid #f0f0f0;
            display: flex;
            align-items: center;

            &:last-child {
                border-right: none;
            }

            &.date-cell {
                width: 80px;
                min-width: 80px;
                justify-content: center;
                flex-direction: column;
                gap: 4px;

                .date-info {
                    display: flex;
                    align-items: center;
                    justify-content: center;
                }

                .date-number {
                    font-size: 16px;
                    font-weight: 600;
                    color: #333;
                }

                .date-label {
                    font-size: 14px;
                    color: #666;
                    margin-left: 2px;
                }
            }

            &.time-cell {
                width: 120px;
                min-width: 120px;
                justify-content: center;

                .time-text {
                    color: #1976d2;
                    font-weight: 500;
                }
            }

            &.duration-cell {
                flex: 1;
                flex-direction: column;
                align-items: flex-start;

                .duration-main {
                    font-weight: 600;
                    color: #333;
                    margin-bottom: 4px;
                }

                .duration-detail {
                    font-size: 12px;
                    color: #666;
                }
            }

            &.status-cell {
                width: 120px;
                min-width: 120px;
                justify-content: center;

                // 🔥 NEW: 근무상태 배지 스타일
                .work-status-badges {
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    width: 100%;
                }
            }
        }
    }
}

.timeline-chart {
    border-top: 1px solid #e0e0e0;
    background: #fafafa;
    overflow: visible;

    .time-scale {
        display: flex;
        height: 30px;
        border-bottom: 1px solid #e0e0e0;

        .time-marker {
            flex: 1;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 11px;
            color: #666;
            border-right: 1px solid #f0f0f0;

            &:last-child {
                border-right: none;
            }
        }
    }

    .timeline-content {
        position: relative;
        height: 60px;
        background: #ffffff;
        overflow: visible;

        // 모눈종이 배경
        .grid-background {
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            pointer-events: none;
            z-index: 1;

            .grid-line {
                position: absolute;

                &.vertical {
                    width: 1px;
                    height: 100%;
                    background: #f0f0f0;

                    &:nth-child(4n+1) {
                        // 4시간마다 진한 선
                        background: #d0d0d0;
                    }
                }

                &.horizontal {
                    width: 100%;
                    height: 1px;
                    background: #f5f5f5;
                }
            }
        }

        // 호버 시 빨간 세로선
        .hover-line {
            position: absolute;
            top: 0;
            bottom: 0;
            width: 2px;
            background: #ff4444;
            pointer-events: none;
            z-index: 20;
            box-shadow: 0 0 4px rgba(255, 68, 68, 0.3);
        }

        .timeline-row {
            position: relative;
            height: 100%;

            .work-bar {
                position: absolute;
                top: 15px;
                height: 30px;
                background: #4caf50;
                border-radius: 6px;
                border: 2px solid #388e3c;
                transition: all 0.2s ease;
                z-index: 15;
                box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

                &:hover {
                    transform: translateY(-2px);
                    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
                    border-color: #2e7d32;
                }

                &.ongoing {
                    background: linear-gradient(90deg, #ff9800 0%, #ffcc80 100%);
                    border-color: #f57c00;
                    animation: pulse 2s ease-in-out infinite alternate;

                    &:hover {
                        border-color: #ef6c00;
                    }
                }
            }

            // 타임라인 호버 툴팁 (벌룬 스타일)
            .timeline-tooltip {
                position: absolute;
                top: -50px;
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
                padding: 8px 12px;
                border-radius: 12px;
                font-size: 13px;
                font-weight: 600;
                white-space: nowrap;
                pointer-events: none;
                z-index: 50;
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
                border: 2px solid rgba(255, 255, 255, 0.2);
                backdrop-filter: blur(10px);
                animation: tooltipFadeIn 0.2s ease-out;

                &::after {
                    content: '';
                    position: absolute;
                    top: 100%;
                    left: 50%;
                    transform: translateX(-50%);
                    border: 8px solid transparent;
                    border-top-color: #667eea;
                }

                &::before {
                    content: '';
                    position: absolute;
                    top: calc(100% + 2px);
                    left: 50%;
                    transform: translateX(-50%);
                    border: 6px solid transparent;
                    border-top-color: rgba(255, 255, 255, 0.2);
                }

                // Fixed position 버전
                &.timeline-tooltip-fixed {
                    position: fixed;
                    top: auto;
                    transform: translate(-50%, -100%);
                    margin-top: -8px;
                    z-index: 9999;

                    &::after {
                        top: 100%;
                        border-top-color: #667eea;
                    }

                    &::before {
                        top: calc(100% + 2px);
                        border-top-color: rgba(255, 255, 255, 0.2);
                    }
                }
            }

            // 근무시간 바 툴팁
            .work-bar-tooltip {
                position: fixed;
                background: white;
                border: 1px solid #ddd;
                border-radius: 8px;
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
                padding: 0;
                font-size: 13px;
                z-index: 40;
                transform: translate(-50%, -100%);
                margin-top: -8px;

                .tooltip-content {
                    padding: 12px;
                }

                .tooltip-item {
                    display: flex;
                    justify-content: space-between;
                    align-items: center;
                    margin-bottom: 6px;

                    &:last-child {
                        margin-bottom: 0;
                    }

                    .tooltip-label {
                        color: #666;
                        margin-right: 12px;
                        font-weight: 500;
                    }

                    .tooltip-value {
                        color: #333;
                        font-weight: 600;
                    }
                }

                &::after {
                    content: '';
                    position: absolute;
                    top: 100%;
                    left: 50%;
                    transform: translateX(-50%);
                    border: 6px solid transparent;
                    border-top-color: white;
                }
            }

            .no-data-message {
                position: absolute;
                top: 50%;
                left: 50%;
                transform: translate(-50%, -50%);
                color: #999;
                font-size: 14px;
                font-style: italic;
            }

            .timeline-date-label {
                position: absolute;
                top: -20px;
                left: 8px;
                font-size: 12px;
                font-weight: 600;
                color: #1976d2;
                background: white;
                padding: 2px 6px;
                border-radius: 4px;
                border: 1px solid #e0e0e0;
                z-index: 10;
            }
        }


    }
}

@keyframes pulse {
    0% {
        opacity: 0.8;
        transform: scaleY(1);
    }

    100% {
        opacity: 1;
        transform: scaleY(1.05);
    }
}

@keyframes tooltipFadeIn {
    0% {
        opacity: 0;
        transform: translateY(5px) scale(0.9);
    }

    100% {
        opacity: 1;
        transform: translateY(0) scale(1);
    }
}

// Global 툴팁 스타일 (body에 직접 렌더링)
.timeline-tooltip-global {
    position: fixed;
    transform: translate(-50%, -100%);
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    padding: 8px 12px;
    border-radius: 12px;
    font-size: 13px;
    font-weight: 600;
    white-space: nowrap;
    pointer-events: none;
    z-index: 99999;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    border: 2px solid rgba(255, 255, 255, 0.2);
    backdrop-filter: blur(10px);
    animation: tooltipFadeIn 0.2s ease-out;
    margin-top: -8px;

    &::after {
        content: '';
        position: absolute;
        top: 100%;
        left: 50%;
        transform: translateX(-50%);
        border: 8px solid transparent;
        border-top-color: #667eea;
    }

    &::before {
        content: '';
        position: absolute;
        top: calc(100% + 2px);
        left: 50%;
        transform: translateX(-50%);
        border: 6px solid transparent;
        border-top-color: rgba(255, 255, 255, 0.2);
    }
}

// Global 근무시간 바 툴팁 스타일
.work-bar-tooltip-global {
    position: fixed;
    background: white;
    border: 1px solid #ddd;
    border-radius: 8px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    padding: 0;
    font-size: 13px;
    z-index: 99999;
    transform: translate(-50%, -100%);
    margin-top: -8px;
    pointer-events: none;

    .tooltip-content {
        padding: 12px;
    }

    .tooltip-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 6px;

        &:last-child {
            margin-bottom: 0;
        }

        .tooltip-label {
            color: #666;
            margin-right: 12px;
            font-weight: 500;
        }

        .tooltip-value {
            color: #333;
            font-weight: 600;
        }
    }

    &::after {
        content: '';
        position: absolute;
        top: 100%;
        left: 50%;
        transform: translateX(-50%);
        border: 6px solid transparent;
        border-top-color: white;
    }
}
</style>