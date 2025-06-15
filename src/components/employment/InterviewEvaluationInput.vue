<template>
    <div class="evaluation-form">
        <!-- 전체 항목 묶는 큰 박스 -->
        <div class="all-criteria-wrapper">
            <div class="evaluation-box">
                <div v-for="(item, index) in localCriteria" :key="index" class="criteria-group">
                    <!-- 상단 정보 블록 -->
                    <div class="criteria-info">
                        <div class="left">
                            <h3>{{ index + 1 }}. {{ item.title }}</h3>
                            <p class="question">{{ item.content }}</p>
                        </div>
                        <div class="right">
                            <span class="weight">가중치 <strong>{{ item.weight }}%</strong></span>
                        </div>
                    </div>

                    <!-- 입력 블록 -->
                    <div class="criteria-input">
                        <div class="textarea-wrapper">
                            <textarea v-model="item.comment" placeholder="제시된 평가 기준을 바탕으로 지원자를 평가해 주세요."></textarea>
                            <div class="score-overlay">
                                <input type="number" v-model.number="item.score" min="0" max="100" />
                                <span>/100</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue';

// ✅ props로 받기
const props = defineProps({
    criteria: {
        type: Array,
        required: true
    }
})

const localCriteria = ref([])

watch(
    () => props.criteria,
    (newVal) => {
        if (newVal && Array.isArray(newVal)) {
            localCriteria.value = newVal.map(item => ({ ...item }))
        } else {
            console.warn('🚨 criteria가 아직 설정되지 않았습니다.')
        }
    },
    { immediate: true }
)

const emit = defineEmits(['update:criteria']);

watch(localCriteria, (updated) => {
    emit('update:criteria', updated);
}, { deep: true });

</script>

<style scoped>
.evaluation-form {
    max-width: 800px;
    margin: 0 auto;
    padding: 1.5rem;
    font-size: 0.95rem;
}

.all-criteria-wrapper {
    border: 1px solid #ddd;
    border-radius: 8px;
    background-color: #fff;
    padding: 1rem;
    margin-bottom: 2rem;
}

.criteria-group {
    margin-bottom: 2rem;
}

.criteria-info {
    background-color: #f5f5f5;
    border-radius: 8px;
    border: 1px solid #e0e0e0;
    padding: 1rem;
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
}

.criteria-info .left h3 {
    margin: 0;
    font-weight: bold;
}

.criteria-info .left .question {
    margin-top: 4px;
    font-size: 0.9rem;
    color: #444;
}

.criteria-info .right .weight {
    font-size: 0.9rem;
    color: #1a8917;
    background: #eef6ee;
    padding: 4px 8px;
    border-radius: 12px;
}

.criteria-input {
    margin-top: 0.5rem;
    background-color: #fff;
}

.textarea-wrapper {
    position: relative;
}

textarea {
    width: 100%;
    height: 70px;
    padding: 0.5rem;
    padding-right: 90px;
    resize: none;
    border: 1px solid #ccc;
    border-radius: 6px;
    font-size: 0.9rem;
    box-sizing: border-box;
}

.score-overlay {
    position: absolute;
    top: 50%;
    right: 10px;
    transform: translateY(-50%);
    display: flex;
    align-items: center;
    gap: 4px;
    font-size: 0.9rem;
}

.score-overlay input {
    width: 35px;
    height: 24px;
    padding: 2px;
    text-align: center;
    border: 1px solid #ccc;
    border-radius: 4px;
    font-size: 0.9rem;
}

.total-score {
    background-color: #fff;
    border-radius: 6px;
    padding: 1rem;
    border: 1px solid #ddd;
}

.total-header {
    display: flex;
    justify-content: space-between;
    margin-bottom: 0.5rem;
}

.total-body {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
}

.total-body .score-box.total-align-right {
    display: flex;
    justify-content: flex-end;
    align-items: center;
    gap: 4px;
}

.total-body .score-box.total-align-right input {
    width: 35px;
    height: 24px;
    padding: 2px;
    text-align: center;
    border: 1px solid #ccc;
    border-radius: 4px;
    font-size: 0.9rem;
}

/* number input 스핀 제거 */
input[type="number"]::-webkit-inner-spin-button,
input[type="number"]::-webkit-outer-spin-button {
    -webkit-appearance: none;
    margin: 0;
}

input[type="number"] {
    -moz-appearance: textfield;
}
</style>
