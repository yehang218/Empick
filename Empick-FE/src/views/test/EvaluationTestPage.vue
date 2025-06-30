<template>
    <div>
        <h2>평가 기준 테스트</h2>

        <div v-for="(item, index) in criteriaList" :key="index" class="summary-box">
            {{ index + 1 }}. {{ item.title }} ({{ item.weight }}%)<br>
            <small>{{ item.description }}</small>
        </div>

        <!-- 평가 기준 입력 컴포넌트 -->
        <EvaluationCriteria :criteriaList="criteriaList" @update:criteriaList="updateCriteriaList" />
    </div>
</template>

<script setup>
import { ref } from 'vue'
import EvaluationCriteria from '@/components/employment/EvaluationCriteria.vue'

const criteriaList = ref([])

const updateCriteriaList = (newList) => {
    criteriaList.value = newList
}
</script>

<style scoped>
.summary-box {
    padding: 12px;
    border: 1px solid #ccc;
    margin-bottom: 10px;
    border-radius: 6px;
}
</style>