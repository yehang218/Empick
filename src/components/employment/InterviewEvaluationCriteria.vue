<template>
    <v-container fluid class="pa-4">
        <div class="section-title">평가 기준</div>

        <v-sheet class="criteria-bg" elevation="0">
            <v-card v-for="(item, index) in localList" :key="index" class="criteria-card" flat>
                <div class="card-content">
                    <div class="text-section">
                        <div class="title-row d-flex justify-space-between align-center">
                            <span class="criteria-title mb-1 mt-3"><strong>{{ index + 1 }}. {{ item.title
                                    }}</strong></span>
                            <div class="plain-weight mr-3 d-flex justify-end">{{ item.weight * 100 }}%</div>
                        </div>
                        <div class="criteria-desc">{{ item.content }}</div>
                    </div>
                </div>
            </v-card>
        </v-sheet>
    </v-container>
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
.criteria-bg {
    background-color: #F8F8F8;
    padding: 24px;
    border-radius: 12px;
}

.criteria-card {
    background-color: #fff;
    border: 1px solid #EBEAED;
    border-radius: 12px;
    padding: 20px;
    margin-bottom: 16px;
}

.card-content {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    gap: 16px;
}

.text-section {
    flex: 1;
    min-width: 0;
}

.text-section.full-width {
    width: 100%;
}

.title-row {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 4px;
}

.criteria-number {
    font-weight: 700;
}

.criteria-title {
    font-size: 16px;
}

.criteria-desc {
    font-size: 14px;
    color: #666;
    margin-top: 8px;
}

.input-title {
    font-size: 16px;
    flex: 1;
    padding: 0 !important;
    margin: 0;
}

.input-desc {
    font-size: 14px;
    padding: 0 !important;
    margin-top: 4px;
}

.right-section {
    width: 160px;
    flex-shrink: 0;
    display: flex;
    flex-direction: column;
    align-items: flex-end;
    gap: 12px;
}

.plain-weight {
    font-size: 14px;
    font-weight: 700;
    color: #2e7d32;
    margin-top: 2px;
    text-align: right;

}

.weight-input-group {
    display: flex;
    align-items: center;
    gap: 10px;
    background-color: #f4f4f4;
    padding: 4px 10px;
    border-radius: 999px;
}

.weight-input {
    width: 100px;
    max-width: 40px;
    font-size: 10px;
    text-align: right;
    padding: 0;
    margin: 0;
}

.weight-label {
    font-size: 15px;
    color: #285430;
    font-weight: 700;
    white-space: nowrap;
}

.action-buttons {
    display: flex;
    gap: 8px;
}

.add-btn-wrap {
    display: flex;
    justify-content: flex-end;
    margin-top: 12px;
}

.add-criteria-btn {
    background-color: #fff;
    border: 1px solid #E7E7E7;
    color: #81BF6A;
    font-weight: 500;
    border-radius: 8px;
    padding: 6px 12px;
}
</style>
