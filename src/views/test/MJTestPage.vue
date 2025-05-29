<template>
  <v-container fluid>
    <v-row>
      <!-- 달력 (왼쪽 영역) -->
      <v-col cols="7">
        <Calendar @date-selected="handleDateSelected" />
      </v-col>

      <!-- 선택된 날짜 데이터 (오른쪽 영역) -->
      <v-col cols="5">
        <v-sheet elevation="2" class="pa-4">
          <h3 class="mb-4">선택된 날짜: {{ selectedDate }}</h3>

          <div v-if="Array.isArray(dataForSelectedDate)">
            <v-list>
            <v-list-item
                v-for="item in dataForSelectedDate"
                :key="item.id"
            >
                <v-list-item-title>{{ item.면접자 }}</v-list-item-title>
                <v-list-item-subtitle>{{ item.시간 }}</v-list-item-subtitle>
            </v-list-item>
            </v-list>
          </div>

          <div v-else class="text-grey">
            {{ dataForSelectedDate }}
          </div>
        </v-sheet>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { ref } from 'vue'
import Calendar from '@/components/Calendar.vue'

const selectedDate = ref('')
const dataForSelectedDate = ref(null)

const handleDateSelected = (date) => {
  selectedDate.value = date

  const exampleData = {
    '2025-05-29': [
      { id: 1, 면접자: '김민수', 시간: '10:00' },
      { id: 2, 면접자: '이지은', 시간: '11:30' },
      { id: 3, 면접자: '박지훈', 시간: '14:00' },
    ],
    '2025-05-30': [
      { id: 4, 면접자: '최수영', 시간: '09:00' },
      { id: 5, 면접자: '정해인', 시간: '13:00' },
      { id: 6, 면접자: '한지민', 시간: '15:30' },
    ],
  }

  dataForSelectedDate.value = exampleData[date] || '해당 날짜에 대한 정보가 없습니다.'
}
</script>

<style scoped>

</style>