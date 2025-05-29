<template>
  <v-dialog :model-value="show" @update:model-value="(val) => emit('update:show', val)" max-width="500px">
    <v-card>
      <v-card-title>
        {{ type === 'add' ? '더하기' : '빼기' }} 계산
      </v-card-title>
      <v-card-text>
        <v-text-field v-model.number="a" label="숫자 A" type="number" />
        <v-text-field v-model.number="b" label="숫자 B" type="number" />
        <v-alert v-if="store.error" type="error" dense>
          {{ store.error }}
        </v-alert>
      </v-card-text>
      <v-card-actions>
        <v-spacer />
        <v-btn color="grey" @click="close">닫기</v-btn>
        <v-btn color="primary" :loading="store.loading" @click="calculate">계산</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref, watch } from 'vue';
import { useCalculatorStore } from '@/stores/calculatorStore';

const props = defineProps({
  type: { type: String, required: true },
  show: { type: Boolean, required: true },
});

const emit = defineEmits(['update:show']);
const store = useCalculatorStore();

const a = ref(0);
const b = ref(0);

const calculate = async () => {
  await store.calculate(props.type, a.value, b.value);
  if (!store.error) close();
};

const close = () => {
  emit('update:show', false);
  store.resetResult();
};

// 모달 열릴 때 초기화
watch(() => props.show, (val) => {
  if (val) {
    a.value = 0;
    b.value = 0;
    store.resetResult();
  }
});
</script>
