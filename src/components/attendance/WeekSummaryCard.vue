<template>
    <div class="week-summary-card">
        <!-- 테이블 헤더 -->
        <div class="table-header">
            <div class="header-cell date-cell">일자</div>
            <div class="header-cell time-cell">업무시작</div>
            <div class="header-cell time-cell">업무종료</div>
            <div class="header-cell duration-cell">근무시간</div>
            <div class="header-cell status-cell">승인요청</div>
        </div>

        <!-- 테이블 데이터 -->
        <div class="table-body">
            <div v-for="(day, index) in weekData" :key="index" class="table-row" :class="{ 'selected': day.selected }">
                <div class="data-cell date-cell">
                    <span class="date-number">{{ day.date }}</span>
                    <span class="date-label">일</span>
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

                <div class="data-cell status-cell">
                    <v-btn v-if="day.needsApproval" size="small" color="primary" @click="requestApproval(day)">
                        승인요청
                    </v-btn>
                </div>
            </div>
        </div>

        <!-- 타임라인 차트 -->
        <div class="timeline-chart">
            <div class="time-scale">
                <div v-for="hour in 24" :key="hour - 1" class="time-marker">
                    {{ (hour - 1).toString().padStart(2, '0') }}
                </div>
            </div>

            <div class="timeline-content">
                <div v-for="(day, index) in weekData" :key="index" class="timeline-row">
                    <!-- 근무시간 바 (출근과 퇴근이 모두 있는 경우) -->
                    <div v-if="day.startTime !== '-' && day.endTime !== '-'" class="work-bar"
                        :style="getWorkBarStyle(day)">
                        <span class="work-label start-label">출근</span>
                        <span class="work-label end-label">퇴근</span>
                    </div>
                    <!-- 출근만 있는 경우 현재 시간까지 진행 바로 표시 -->
                    <div v-else-if="day.startTime !== '-'" class="work-bar ongoing"
                        :style="getOngoingWorkBarStyle(day)">
                        <span class="work-label start-label">출근</span>
                    </div>

                    <!-- 점심시간 구분선 -->
                    <!-- <div class="lunch-break-line" :style="{ left: '50%' }"></div>
                    <div class="end-break-line" :style="{ left: '75%' }"></div> -->
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

// Props
defineProps({
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

// 현재 시간을 주기적으로 업데이트하기 위한 reactive 변수
const currentTime = ref(new Date())
let timeUpdateInterval = null

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



// 승인 요청 처리
const requestApproval = (day) => {
    console.log('승인 요청:', day)
}

// Emits
defineEmits(['requestApproval', 'editTime'])
</script>

<style lang="scss" scoped>
.week-summary-card {
    background: white;
    border: 1px solid #e0e0e0;
    border-radius: 8px;
    overflow: hidden;
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
            width: 100px;
            min-width: 100px;
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
                width: 100px;
                min-width: 100px;
                justify-content: center;
            }
        }
    }
}

.timeline-chart {
    border-top: 1px solid #e0e0e0;
    background: #fafafa;

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

        .timeline-row {
            position: relative;
            height: 100%;

            .work-bar {
                position: absolute;
                top: 15px;
                height: 30px;
                background: #4caf50;
                border-radius: 4px;
                display: flex;
                align-items: center;
                justify-content: space-between;
                padding: 0 8px;

                &.ongoing {
                    background: linear-gradient(90deg, #ff9800 0%, #ffcc80 100%);
                    animation: pulse 2s ease-in-out infinite alternate;

                    .ongoing-label {
                        font-size: 11px;
                        color: white;
                        font-weight: 600;
                    }
                }
            }

            .work-point {
                position: absolute;
                top: 20px;
                width: 20px;
                height: 20px;
                background: #ff9800;
                border-radius: 50%;
                display: flex;
                align-items: center;
                justify-content: center;
                transform: translateX(-50%);

                .work-label {
                    position: absolute;
                    top: -25px;
                    left: 50%;
                    transform: translateX(-50%);
                    white-space: nowrap;
                }
            }

            .work-label {
                color: white;
                font-size: 12px;
                font-weight: 500;
                display: flex;
                align-items: center;
                height: 100%;

                &.start-label {
                    justify-content: flex-start;
                }

                &.end-label {
                    justify-content: flex-end;
                }
            }
        }

        .lunch-break-line,
        .end-break-line {
            position: absolute;
            top: 0;
            bottom: 0;
            width: 1px;
            background: #f44336;
            opacity: 0.6;
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
</style>