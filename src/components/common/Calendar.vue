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
              class="text-center"
              style="flex-basis: 14.28%; max-width: 14.28%;"
            >
              <v-btn
                v-if="day"
                :color="selectedDay === day ? 'primary' : 'default'"
                @click="selectDay(day)"
                class="ma-1"
                variant="outlined"
                size="small"
                :style="{ color: dIdx === 0 ? 'red' : dIdx === 6 ? 'blue' : '' }"
              >
                {{ day }}
              </v-btn>
            </v-col>
          </v-row>
        </v-sheet>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { ref, computed, watch, defineEmits } from 'vue'
import dayjs from 'dayjs'
import 'dayjs/locale/ko'
import 'dayjs/locale/en'

dayjs.locale('en')

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
