<template>
<v-dialog :model-value="modelValue" @update:modelValue="emit('update:modelValue', $event)" max-width="1200">
        <v-card>
            <v-card-title>실무 테스트 선택</v-card-title>
            <v-card-text>
                <v-list>
                    <v-list-item v-for="jobtest in jobtests" :key="jobtest.id" @click="select(jobtest)">
                        <v-list-item-title>{{ jobtest.title }}</v-list-item-title>
                    </v-list-item>
                </v-list>
            </v-card-text>
        </v-card>
    </v-dialog>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue'

const props = defineProps({
    modelValue: Boolean,
    jobtests: Array
});
const emit = defineEmits(['update:modelValue', 'select']);

const select = (jobtest) => {
    emit('select', jobtest);
    emit('update:modelValue', false);
};
</script>
