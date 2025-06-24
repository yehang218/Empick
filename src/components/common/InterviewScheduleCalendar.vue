<template>
  <v-container class="calendar-container" fluid>
    <!-- 상단 연도/월 표시 및 월 이동 -->
    <v-row justify="center" align="center" class="mb-4">
      <v-btn icon @click="prevMonth">
        <v-icon>mdi-chevron-left</v-icon>
      </v-btn>

      <div class="text-center mx-4">
        <div class="text-h5 font-weight-bold">{{ year }}</div>
        <div class="d-flex justify-center align-center">
          <div class="display-2 font-weight-bold mr-2">{{ monthNameKR }}</div>
          <div class="text-subtitle-1">{{ monthNameEN }}</div>
        </div>
      </div>

      <v-btn icon @click="nextMonth">
        <v-icon>mdi-chevron-right</v-icon>
      </v-btn>
    </v-row>

    <!-- 달력 -->
    <v-row class="calendar-content" justify="center" align="start" no-gutters>
      <v-col cols="12">
        <v-sheet class="pa-4" elevation="2">
          <!-- 요일 -->
          <v-row dense class="weekday-row">
            <v-col
              v-for="(day, index) in daysOfWeek"
              :key="index"
              class="text-center font-weight-bold"
              :style="{ color: index === 0 ? 'red' : index === 6 ? 'blue' : 'black' }"
              style="flex-basis: 14.28%; max-width: 14.28%;"
            >
              {{ day }}
            </v-col>
          </v-row>

          <!-- 날짜 -->
          <v-row dense v-for="(week, wIdx) in weeks" :key="wIdx" class="week-row">
            <v-col
              v-for="(day, dIdx) in week"
              :key="dIdx"
              class="text-center pa-0"
              style="flex-basis: 14.28%; max-width: 14.28%;"
            >
              <div
                v-if="day"
                :class="[
                  'calendar-day-card',
                  isToday(day) ? 'today' : '',
                  isSelected(day) ? 'selected' : '',
                  dIdx === 0 ? 'sunday' : dIdx === 6 ? 'saturday' : '',
                ]"
                @click="selectDay(day)"
              >
                <div class="day-number">{{ day }}</div>
                <!-- interview 일정 badge: 숫자 -->
                <div v-if="getEventCount(day) > 0" class="event-badge">{{ getEventCount(day) }}</div>
              </div>
            </v-col>
          </v-row>
        </v-sheet>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { ref, computed, watch, defineEmits, defineProps } from 'vue'
import dayjs from 'dayjs'
import 'dayjs/locale/ko'
import 'dayjs/locale/en'

dayjs.locale('en')

const props = defineProps({
  customDays: {
    type: Object,
    default: () => ({})
  }
})

const emit = defineEmits(['date-selected'])

const current = dayjs()
const year = ref(current.year())
const month = ref(current.month() + 1)
const selectedDay = ref(current.date())

watch(selectedDay, () => {
  emit('date-selected', selectedDateFull.value)
})

const selectedDateFull = computed(() => {
  return dayjs(`${year.value}-${pad(month.value)}-${pad(selectedDay.value)}`).format('YYYY-MM-DD')
})

const pad = (n) => n.toString().padStart(2, '0')

const daysOfWeek = ['일', '월', '화', '수', '목', '금', '토']

const monthNameKR = computed(() =>
  dayjs(`${year.value}-${pad(month.value)}-01`).locale('ko').format('M월')
)

const monthNameEN = computed(() =>
  dayjs(`${year.value}-${pad(month.value)}-01`).locale('en').format('MMMM')
)

const daysInMonth = computed(() =>
  dayjs(`${year.value}-${month.value}`).daysInMonth()
)

const blankDays = computed(() =>
  dayjs(`${year.value}-${month.value}-01`).day()
)

const calendarDays = computed(() => {
  const blanks = Array(blankDays.value).fill(null)
  const days = Array.from({ length: daysInMonth.value }, (_, i) => i + 1)
  return [...blanks, ...days]
})

const weeks = computed(() => {
  const chunks = []
  for (let i = 0; i < calendarDays.value.length; i += 7) {
    chunks.push(calendarDays.value.slice(i, i + 7))
  }
  return chunks
})

const selectDay = (day) => {
  selectedDay.value = day
}

const updateSelectedToFirstDay = () => {
  selectedDay.value = 1
  emit('date-selected', selectedDateFull.value)
}

const prevMonth = () => {
  const prev = dayjs(`${year.value}-${month.value}-01`).subtract(1, 'month')
  year.value = prev.year()
  month.value = prev.month() + 1
  updateSelectedToFirstDay()
}

const nextMonth = () => {
  const next = dayjs(`${year.value}-${month.value}-01`).add(1, 'month')
  year.value = next.year()
  month.value = next.month() + 1
  updateSelectedToFirstDay()
}

// 오늘 날짜인지 확인
const isToday = (day) => {
  return (
    year.value === dayjs().year() &&
    month.value === dayjs().month() + 1 &&
    day === dayjs().date()
  )
}

// 해당 날짜에 interview가 있는지 확인 (props.customDays)
const getEventCount = (day) => {
  const dateStr = dayjs(`${year.value}-${pad(month.value)}-${pad(day)}`).format('YYYY-MM-DD')
  return props.customDays[dateStr]?.length || 0
}

// 선택된 날짜인지 확인
const isSelected = (day) => {
  return selectedDay.value === day
}
</script>

<style scoped>
.calendar-container {
  max-width: 900px;
  margin: 0 auto;
  padding: 20px 10px;
  height: 700px;
  display: flex;
  flex-direction: column;
}

.calendar-content {
  display: flex;
  flex-grow: 1;
  height: auto;
}

.calendar-day-card {
  min-width: 56px;
  height: 70px;
  margin: 4px auto;
  border-radius: 12px;
  background: #fff;
  border: 1.5px solid #e0e0e0;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  cursor: pointer;
  position: relative;
  transition: border 0.18s, background 0.18s, box-shadow 0.18s;
  box-shadow: 0 2px 8px 0 rgba(80, 120, 200, 0.06);
}

.calendar-day-card:hover {
  background: #e3f2fd;
  border-color: #64b5f6;
  box-shadow: 0 4px 16px 0 rgba(100, 181, 246, 0.13);
}

.calendar-day-card.selected {
  background: #e1f5fe;
  border: 2.5px solid #039be5;
  box-shadow: 0 4px 16px 0 rgba(3, 155, 229, 0.13);
}

.calendar-day-card.today {
  border: 2.5px solid #1976d2;
}

.calendar-day-card.selected.today {
  background: #e1f5fe;
  border: 2.5px dashed #1976d2;
}

.calendar-day-card.sunday {
  color: #e53935;
}

.calendar-day-card.saturday {
  color: #1e88e5;
}

.day-number {
  font-size: 1.15rem;
  font-weight: 600;
  margin-top: 10px;
  letter-spacing: 0.01em;
}

.event-badge {
  margin-top: 7px;
  background: #1976d2;
  color: #fff;
  border-radius: 12px;
  font-size: 0.85rem;
  padding: 2px 8px;
  min-width: 22px;
  min-height: 22px;
  text-align: center;
  font-weight: 500;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 1px 4px 0 rgba(25, 118, 210, 0.10);
}

@media (max-width: 600px) {
  .calendar-day-card {
    min-width: 38px;
    height: 48px;
    border-radius: 8px;
  }
  .day-number {
    font-size: 0.95rem;
    margin-top: 4px;
  }
  .event-badge {
    font-size: 0.7rem;
    min-width: 14px;
    min-height: 14px;
    padding: 1px 4px;
    margin-top: 3px;
  }
}

.weekday-row,
.week-row {
  margin: 0;
}

.weekday-row > .v-col,
.week-row > .v-col {
  padding-left: 0 !important;
  padding-right: 0 !important;
}

.v-btn.default {
  color: black;
  background-color: #f0f0f0;
  
}
</style>
