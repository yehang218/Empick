<template>
    <div class="evaluation-criteria">
        <div class="criteria-header">
            <v-icon color="primary" class="mr-2">mdi-criteria</v-icon>
            <h3 class="criteria-title">평가 기준 목록</h3>
        </div>

        <div class="criteria-list">
            <div 
                v-for="(item, index) in localList" 
                :key="index" 
                class="criteria-item"
            >
                <div class="criteria-content">
                    <div class="criteria-header-row">
                        <div class="criteria-number">
                            <span class="number-badge">{{ index + 1 }}</span>
                        </div>
                        <div class="criteria-main">
                            <h4 class="criteria-name">{{ item.title }}</h4>
                            <div class="criteria-weight">
                                <v-icon size="16" class="mr-1">mdi-percent</v-icon>
                                <span>{{ Math.round(item.weight * 100) }}%</span>
                            </div>
                        </div>
                    </div>
                    <div class="criteria-description">
                        {{ item.content }}
                    </div>
                </div>
            </div>
        </div>

        <div v-if="localList.length === 0" class="empty-criteria">
            <v-icon size="48" color="grey-lighten-1" class="mb-3">mdi-clipboard-text</v-icon>
            <p>평가 기준이 없습니다.</p>
        </div>
    </div>
</template>

<script setup>
import { ref, defineProps, watch } from 'vue'

const props = defineProps({
    criteriaList: { type: Array, required: true }
})

const localList = ref([...props.criteriaList])

watch(() => props.criteriaList, (val) => {
    localList.value = [...val]
})

</script>

<style scoped>
.evaluation-criteria {
    width: 100%;
}

.criteria-header {
    display: flex;
    align-items: center;
    margin-bottom: 1.5rem;
    padding-bottom: 0.75rem;
    border-bottom: 2px solid #e8f5e8;
}

.criteria-title {
    font-size: 1.2rem;
    font-weight: 600;
    color: #2c3e50;
    margin: 0;
}

.criteria-list {
    display: flex;
    flex-direction: column;
    gap: 1rem;
}

.criteria-item {
    background: white;
    border-radius: 16px;
    padding: 1.5rem;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
    border: 1px solid rgba(255, 255, 255, 0.2);
    transition: all 0.3s ease;
    position: relative;
    overflow: hidden;
}

.criteria-item::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 4px;
    height: 100%;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.criteria-item:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
}

.criteria-content {
    margin-left: 1rem;
}

.criteria-header-row {
    display: flex;
    align-items: flex-start;
    gap: 1rem;
    margin-bottom: 1rem;
}

.criteria-number {
    flex-shrink: 0;
}

.number-badge {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    width: 32px;
    height: 32px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    border-radius: 50%;
    font-weight: 700;
    font-size: 0.9rem;
    box-shadow: 0 2px 8px rgba(102, 126, 234, 0.3);
}

.criteria-main {
    flex: 1;
    min-width: 0;
}

.criteria-name {
    font-size: 1.1rem;
    font-weight: 600;
    color: #2c3e50;
    margin: 0 0 0.5rem 0;
    line-height: 1.4;
}

.criteria-weight {
    display: inline-flex;
    align-items: center;
    background: linear-gradient(135deg, #d4edda 0%, #c3e6cb 100%);
    color: #155724;
    padding: 0.25rem 0.75rem;
    border-radius: 20px;
    font-size: 0.85rem;
    font-weight: 600;
    border: 1px solid #c3e6cb;
}

.criteria-description {
    color: #6c757d;
    font-size: 0.95rem;
    line-height: 1.6;
    background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
    padding: 1rem;
    border-radius: 12px;
    border-left: 3px solid #667eea;
}

.empty-criteria {
    text-align: center;
    padding: 3rem 2rem;
    background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
    border-radius: 16px;
    border: 2px dashed #dee2e6;
    color: #6c757d;
}

.empty-criteria p {
    margin: 0;
    font-size: 1rem;
}

/* 애니메이션 효과 */
@keyframes slideInUp {
    from {
        opacity: 0;
        transform: translateY(20px);
    }
    to {
        opacity: 1;
        transform: translateY(0);
    }
}

.criteria-item {
    animation: slideInUp 0.5s ease-out;
}

.criteria-item:nth-child(1) { animation-delay: 0.1s; }
.criteria-item:nth-child(2) { animation-delay: 0.2s; }
.criteria-item:nth-child(3) { animation-delay: 0.3s; }
.criteria-item:nth-child(4) { animation-delay: 0.4s; }
.criteria-item:nth-child(5) { animation-delay: 0.5s; }

/* 반응형 디자인 */
@media (max-width: 768px) {
    .criteria-header-row {
        flex-direction: column;
        gap: 0.75rem;
    }
    
    .criteria-content {
        margin-left: 0;
    }
    
    .criteria-item {
        padding: 1rem;
    }
    
    .criteria-name {
        font-size: 1rem;
    }
    
    .criteria-description {
        font-size: 0.9rem;
        padding: 0.75rem;
    }
}
</style>
