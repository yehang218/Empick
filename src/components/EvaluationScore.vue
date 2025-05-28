<template>
  <v-card
    class="pa-6"
    elevation="4"
    max-height="600px"
    style="overflow-y: auto; max-width: 700px; margin: 0 auto;"
  >
    <!-- 상단: 면접관 이름과 총평 -->
    <div class="d-flex justify-space-between align-center mb-6">
      <div>
        <h3 class="text-h6 font-weight-bold">김석희</h3>
        <p class="text-body-2 grey--text">
          Spring Boot 지식에 대해서는 정확하지 않고 얕지만, 네트워크 부문에서는 역할이 부족함.
          전체적으로 기술 이해도 부족이 드러났으며, 단순 암기한 수준에 머문 것으로 보임.
        </p>
      </div>
  
          <!-- 총점 (배경 없음, 색상 동적 적용, 오른쪽 여백) -->
      <div class="ml-8 text-h3 font-weight-bold d-flex align-end">
          <span :style="{ color: scoreColor }">{{ averageScore }}</span>
          <span class="text-h6 grey--text ml-1">/100</span>
      </div>
    </div>

    <!-- 평가 항목 목록 -->
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
import { computed } from 'vue';

const evaluationItems = [
  {
    title: '네트워크 개념 정확성',
    weight: 20,
    criteria: '네트워크 핵심 개념을 정확히 설명했는가?',
    evaluation:
      '네트워크의 기본 구조와 계층에 대한 설명이 부족했습니다. 용어 사용에서 혼동이 있었습니다.',
    score: 90,
  },
  {
    title: '용어의 정확성 및 표현력',
    weight: 15,
    criteria: '기술 용어를 적절히 사용했는가?',
    evaluation:
      '전문 용어를 정확히 사용하는 데 어려움이 있었으며, 의미 전달이 명확하지 않았습니다.',
    score: 70,
  },
  {
    title: 'Spring Boot 이해도',
    weight: 20,
    criteria: 'Spring Boot의 구조 및 동작 방식을 설명할 수 있는가?',
    evaluation:
      'Spring Boot에 대한 기본적인 개념은 알고 있으나 내부 구조나 흐름에 대한 이해가 부족합니다.',
    score: 80,
  },
  {
    title: '문제 해결 접근 방식',
    weight: 15,
    criteria: '문제를 어떻게 분석하고 접근했는가?',
    evaluation:
      '문제 해결 시 로직을 체계적으로 설명하지 못했으며, 단편적인 지식에 의존하는 경향이 있었습니다.',
    score: 90,
  },
  {
    title: '코드 구현 능력',
    weight: 20,
    criteria: '질문에 대해 실제 코드 예시를 들며 설명했는가?',
    evaluation:
      '코드로 설명하는 능력이 부족했고, 개념을 코드로 연결하는 데 어려움을 보였습니다.',
    score: 90,
  },
  {
    title: '전반적인 커뮤니케이션',
    weight: 10,
    criteria: '질문에 대해 논리적이고 명확하게 답했는가?',
    evaluation:
      '답변이 중간에 흐트러지거나 논리 흐름이 약한 경우가 있었으며, 일관된 전달이 부족했습니다.',
    score: 78,
  },
];

// 평균 점수 계산 (가중 평균)
const averageScore = computed(() => {
  const total = evaluationItems.reduce((sum, item) => {
    return sum + (item.score * item.weight) / 100;
  }, 0);
  return total.toFixed(1);
});

// 점수에 따라 색상 설정
const scoreColor = computed(() => {
if (averageScore.value >= 80) return '#4CAF50'; // 초록
if (averageScore.value >= 60) return '#FFC107'; // 주황
return '#F44336'; // 빨강
});
</script>
