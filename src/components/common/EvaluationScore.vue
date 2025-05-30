<!-- components/EvaluationCard.vue -->
<template>
  <v-card
    class="pa-6"
    elevation="4"
    max-height="600px"
    style="overflow-y: auto; max-width: 700px; margin: 0 auto;"
  >
    <!-- 상단: 면접자 이름과 총평 -->
    <div class="d-flex justify-space-between align-center mb-6">
      <div>
        <h3 class="text-h6 font-weight-bold">{{ candidateName }}</h3>
        <p class="text-body-2 grey--text">
          {{ summary }}
        </p>
      </div>

      <div class="ml-8 text-h3 font-weight-bold d-flex align-end">
        <span :style="{ color: scoreColor }">{{ averageScore }}</span>
        <span class="text-h6 grey--text ml-1">/100</span>
      </div>
    </div>

    <v-divider></v-divider>

    <v-container fluid class="pa-0">
      <v-row
        v-for="(item, index) in evaluationItems"
        :key="index"
        class="py-4"
      >
        <v-col cols="12">
          <div class="d-flex justify-space-between align-center mb-1">
            <div>
              <h4 class="text-subtitle-1 font-weight-bold">
                {{ index + 1 }}. {{ item.title }}
              </h4>
            </div>
            <span class="text-body-1 font-weight-bold">
              {{ item.score }}/100
              <span class="text-caption grey--text ml-2">({{ item.weight }}%)</span>
            </span>
          </div>
          <p class="mb-1 grey--text text--darken-1">{{ item.criteria }}</p>
          <v-card class="pa-3 mt-2" outlined>
            <p class="mb-0">{{ item.evaluation }}</p>
          </v-card>
        </v-col>
        <v-divider v-if="index < evaluationItems.length - 1"></v-divider>
      </v-row>
    </v-container>
  </v-card>
</template>

<script setup>
import { computed } from 'vue'


const props = defineProps({
  candidateName: String,
  summary: String,
  evaluationItems: Array,
})


// 평균 점수 계산
const averageScore = computed(() => {
  const total = props.evaluationItems.reduce((sum, item) => {
    return sum + (item.score * item.weight) / 100
  }, 0)
  return total.toFixed(1)
})

// 색상 계산
const scoreColor = computed(() => {
  if (averageScore.value >= 80) return '#4CAF50'
  if (averageScore.value >= 60) return '#FFC107'
  return '#F44336'
})
</script>
