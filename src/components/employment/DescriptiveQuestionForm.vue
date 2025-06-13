<template>
    <div>
        <!-- 문제 입력 -->
        <v-textarea label="문제를 입력해주세요." v-model="form.content" auto-grow :error-messages="errors.content" />

        <!-- 채점 기준 -->
        <div class="mt-4">
            <div class="text-subtitle-1 mb-2">채점 기준</div>

            <div v-for="(criteria, i) in form.gradingCriteria" :key="i" class="mb-4 pa-4 border rounded">
                <v-text-field v-model="criteria.content" label="채점 기준" class="mb-2"
                    :error-messages="Array.isArray(errors.gradingCriteria) ? errors.gradingCriteria[i]?.content : errors.gradingCriteria" />

                <v-textarea v-model="criteria.detailContent" label="상세 설명" auto-grow class="mb-2" />

                <v-slider v-model="criteria.scoreWeight" label="점수 가중치" min="0" max="1" step="0.1" thumb-label
                    :error-messages="Array.isArray(errors.gradingCriteria) ? errors.gradingCriteria[i]?.scoreWeight : errors.gradingCriteria" />

                <div class="d-flex justify-end">
                    <v-btn icon="mdi-delete" variant="text" color="error" @click="removeCriteria(i)"
                        :disabled="form.gradingCriteria.length <= 1" />
                </div>
            </div>

            <v-btn variant="outlined" @click="addCriteria">
                채점 기준 추가하기
            </v-btn>
        </div>
    </div>
</template>

<script setup>
const props = defineProps({
    form: Object,
    errors: {
        type: Object,
        default: () => ({})
    }
});

const emit = defineEmits(['update:form']);

function addCriteria() {
    const updated = JSON.parse(JSON.stringify(props.form)); // ✅ 안전한 깊은 복사
    updated.gradingCriteria.push({
        content: '',
        detailContent: '',
        scoreWeight: 0
    });
    emit('update:form', updated);
}

function removeCriteria(index) {
    if (props.form.gradingCriteria.length <= 1) return;

    const updated = JSON.parse(JSON.stringify(props.form)); // ✅ 동일하게 처리
    updated.gradingCriteria.splice(index, 1);
    emit('update:form', updated);
}
</script>