<template>
    <v-container>
        <v-row class="my-4" justify="space-between" align="center">
            <v-col>
                <h2>📦 카운터</h2>
                <v-alert v-if="counterStore.error" type="error" dense>
                    {{ counterStore.error }}
                </v-alert>
                <v-btn color="primary" @click="counterStore.fetchCount" :loading="counterStore.loading">
                    불러오기
                </v-btn>
                <v-btn color="success" @click="counterStore.increment" :loading="counterStore.loading">
                    증가
                </v-btn>
                <v-btn color="error" @click="counterStore.decrement" :loading="counterStore.loading">
                    감소
                </v-btn>
                <h3 class="mt-4">현재 값: {{ counterStore.count }}</h3>
            </v-col>

            <v-col>
                <h2>🧮 계산기 결과</h2>
                <v-alert v-if="calculatorStore.error" type="error" dense>
                    {{ calculatorStore.error }}
                </v-alert>
                <v-btn color="info" @click="openCalc('add')">더하기</v-btn>
                <v-btn color="warning" @click="openCalc('sub')">빼기</v-btn>
                <h3 class="mt-4">결과: {{ calculatorStore.result }}</h3>
            </v-col>
        </v-row>

        <!-- 계산기 모달 -->
        <CalcModal v-model:show="showModal" :type="calcType" />
    </v-container>
</template>

<script setup>
import { ref } from 'vue';
import { useCounterStore } from '@/stores/counterStore';
import { useCalculatorStore } from '@/stores/calculatorStore';
import CalcModal from '@/components/calculator/CalcModal.vue';

const counterStore = useCounterStore();
const calculatorStore = useCalculatorStore();

const showModal = ref(false);
const calcType = ref('add');

const openCalc = (type) => {
    calcType.value = type;
    showModal.value = true;
};
</script>
