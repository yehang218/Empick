<template>
  <v-container>
    <v-btn @click="goBack" color="primary" class="mb-4">뒤로가기</v-btn>
    <evaluation-criteria-list :items="criteriaList" />
  </v-container>
</template>

<script setup>
import { useRouter } from 'vue-router'
import EvaluationCriteriaList from '@/components/EvaluationCriteriaList.vue'

const router = useRouter()
const goBack = () => {
  router.push('/test/mj')
}

const criteriaList = [
  { title: '네트워크 개념 정확성', description: '네트워크 핵심 개념 정확히 설명했는가?', weight: 40 },
  { title: '용어의 정확성 및 표현력', description: '기술 용어를 적절히 사용했는가?', weight: 30 },
  { title: '논리 구성 및 용어의 정확성', description: '문장 흐름이 자연스럽고, CS 용어 사용이 적절한가?', weight: 20 },
  { title: '핵심 개념 파악 여부', description: '반드시 알아야 할 핵심 개념을 언급했는가?', weight: 10 },
  { title: '문제 해결 접근 방식', description: '문제를 체계적으로 분석하고 접근했는가?', weight: 20 },
  { title: '예시 활용 적절성', description: '적절한 예시를 들어 이해를 도왔는가?', weight: 15 },
  { title: '간결성 및 명확성', description: '불필요한 설명 없이 명확하게 작성했는가?', weight: 15 },
  { title: '기술 깊이와 폭', description: '단순 나열이 아닌 깊이 있게 설명했는가?', weight: 20 },
]
</script>