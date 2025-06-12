<template>
    <v-container>
        <h2 class="text-h6 font-weight-bold mb-4">문제 등록</h2>

        <v-tabs v-model="activeTab">
            <v-tab value="MULTIPLE">선택형</v-tab>
            <v-tab value="SUBJECTIVE">단답형</v-tab>
            <v-tab value="DESCRIPTIVE">서술형</v-tab>
        </v-tabs>
        <div class="mt-4">
            <v-select v-model="form.difficulty" :items="difficultyOptions" label="난이도" variant="outlined"
                density="comfortable" class="mb-4" clearable />
        </div>
        <component :is="currentComponent" v-model:form="form" />

        <div class="d-flex justify-end mt-4">
            <v-btn variant="tonal" @click="resetForm">취소하기</v-btn>
            <v-btn color="primary" class="ml-2" @click="handleSubmit">등록하기</v-btn>
        </div>
    </v-container>
</template>

<script setup>
import { ref, computed } from 'vue';
import MultipleQuestionForm from '@/components/employment/MultipleQuestionForm.vue';
import SubjectiveForm from '@/components/employment/SubjectiveForm.vue';
import DescriptiveQuestionForm from '@/components/employment/DescriptiveQuestionForm.vue';
import { createQuestionService } from '@/services/jobtestQuestionService';
import CreateQuestionRequestDTO from '@/dto/employment/jobtest/createQuestionRequestDTO';

const activeTab = ref('MULTIPLE');
const difficultyOptions = [
    { title: '쉬움', value: 'EASY' },
    { title: '보통', value: 'MEDIUM' },
    { title: '어려움', value: 'HARD' }
];

const form = ref({
    type: 'MULTIPLE',
    content: '',
    detailContent: '',
    difficulty: 'EASY',
    answer: '',
    createdMemberId: 1,
    questionOptions: [],
    gradingCriteria: []
});

const errors = ref({});

const currentComponent = computed(() => {
    switch (activeTab.value) {
        case 'MULTIPLE': return MultipleQuestionForm;
        case 'SUBJECTIVE': return SubjectiveForm;
        default: return DescriptiveQuestionForm;
    }
});

function resetForm() {
    form.value = {
        type: activeTab.value,
        content: '',
        detailContent: '',
        difficulty: 'EASY',
        answer: '',
        createdMemberId: 1,
        questionOptions: [],
        gradingCriteria: []
    };
}

function validateForm() {
    errors.value = {};

    if (!form.value.content) {
        errors.value.content = '문제 내용을 입력해주세요.';
    }

    if (!form.value.answer) {
        errors.value.answer = '정답을 입력해주세요.';
    }

    if (form.value.type === 'MULTIPLE') {
        if (!form.value.questionOptions.length) {
            errors.value.questionOptions = '최소 1개 이상의 선택지를 입력해주세요.';
        } else if (form.value.questionOptions.some(option => !option.content)) {
            errors.value.questionOptions = '모든 선택지의 내용을 입력해주세요.';
        }
    }

    if (form.value.type === 'DESCRIPTIVE') {
        if (!form.value.gradingCriteria.length) {
            errors.value.gradingCriteria = '최소 1개 이상의 채점 기준을 입력해주세요.';
        } else {
            const totalWeight = form.value.gradingCriteria.reduce((sum, criteria) => sum + criteria.scoreWeight, 0);
            if (Math.abs(totalWeight - 1) > 0.001) { // 부동소수점 오차 고려
                errors.value.gradingCriteria = '채점 기준의 점수 가중치 합이 100%가 되어야 합니다.';
            }
        }
    }

    return Object.keys(errors.value).length === 0;
}

async function handleSubmit() {
    if (!validateForm()) {
        alert('입력값을 확인해주세요.');
        return;
    }

    try {
        // 정답은 선택된 보기의 content로 설정
        form.value.answer = form.value.questionOptions.find(opt => opt.isAnswer)?.content || '';
        const dto = CreateQuestionRequestDTO.fromForm(form.value);
        await createQuestionService(dto);
        alert('문제 등록이 완료되었습니다.');
        resetForm();
    } catch (error) {
        console.error('문제 등록 실패:', error);
        alert(error.response?.data?.message || '문제 등록에 실패했습니다.');
    }
}

function addOption() {
    const updated = {
        ...form.value,
        questionOptions: [...form.value.questionOptions, { content: '', isAnswer: false }]
    };
    form.value = updated;
}

function removeOption(index) {
    const updated = {
        ...form.value,
        questionOptions: form.value.questionOptions.filter((_, i) => i !== index)
    };
    form.value = updated;
}

function updateOption(index, value) {
    const updatedOptions = form.value.questionOptions.map((opt, i) =>
        i === index ? { ...opt, content: value } : opt
    );
    const updated = {
        ...form.value,
        questionOptions: updatedOptions
    };
    form.value = updated;
}
</script>

<style scoped></style>