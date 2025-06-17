<template>
    <v-pagination v-model="modelValueProxy" :length="length" :total-visible="totalVisible"
        class="custom-pagination mt-4" />
</template>

<script setup>
import { computed } from 'vue'
const props = defineProps({
    modelValue: { type: Number, required: true },
    length: { type: Number, required: true },
    totalVisible: { type: Number, default: 5 }
})
const emit = defineEmits(['update:modelValue'])
const modelValueProxy = computed({
    get: () => props.modelValue,
    set: (val) => emit('update:modelValue', val)
})
</script>

<style scoped>
.custom-pagination {
    justify-content: center;
}

.custom-pagination :deep(.v-pagination__item) {
    background: #fff;
    color: #374151;
    border-radius: 8px;
    min-width: 40px;
    min-height: 40px;
    font-size: 1.15rem;
    font-weight: 500;
    margin: 0 4px;
    transition: background 0.2s;
}

.custom-pagination :deep(.v-pagination__item--is-active) {
    background: #5e8d5a !important;
    color: #fff !important;
    box-shadow: 0 2px 8px rgba(94, 141, 90, 0.08);
}

.custom-pagination :deep(.v-pagination__item):hover {
    background: #e6f4ea;
}

.custom-pagination :deep(.v-pagination__more) {
    color: #222;
    font-size: 1.3rem;
    background: transparent;
    box-shadow: none;
}

.custom-pagination :deep(.v-pagination__prev),
.custom-pagination :deep(.v-pagination__next) {
    background: #fff;
    color: #374151;
    border-radius: 8px;
    min-width: 40px;
    min-height: 40px;
    font-size: 1.2rem;
    margin: 0 4px;
    transition: background 0.2s;
}

.custom-pagination :deep(.v-pagination__prev):hover,
.custom-pagination :deep(.v-pagination__next):hover {
    background: #e6f4ea;
}
</style>