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
import { ref, computed, watch } from 'vue';
import MultipleQuestionForm from '@/components/employment/MultipleQuestionForm.vue';
import SubjectiveForm from '@/components/employment/SubjectiveForm.vue';
import DescriptiveQuestionForm from '@/components/employment/DescriptiveQuestionForm.vue';
import { createQuestionService } from '@/services/jobtestQuestionService';
import CreateQuestionRequestDTO from '@/dto/employment/jobtest/createQuestionRequestDTO';
import { useToast } from 'vue-toastification';
import { withErrorHandling } from '@/utils/errorHandler';

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

watch(activeTab, (newType) => {
    form.value.type = newType;
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

const toast = useToast();

// 내용 등록하기 전에 빠진 내용이 있는지 확인
function validateForm() {
    errors.value = {};

    if (!form.value.content) {
        errors.value.content = '문제 내용을 입력해주세요.';
    }

    if (form.value.type === 'SUBJECTIVE' && !form.value.answer) {
        errors.value.answer = '정답을 입력해주세요.';
    }

    if (form.value.type === 'MULTIPLE') {
        if (!form.value.questionOptions.length) {
            errors.value.questionOptions = '최소 1개 이상의 선택지를 입력해주세요.';
        } else if (form.value.questionOptions.some(option => !option.content)) {
            errors.value.questionOptions = '모든 선택지의 내용을 입력해주세요.';
        } else if (!form.value.questionOptions.some(option => option.isAnswer)) {
            errors.value.questionOptions = '하나 이상의 선택지를 정답으로 지정해주세요.';
        }
    }

    if (form.value.type === 'DESCRIPTIVE') {
        if (!form.value.gradingCriteria.length) {
            errors.value.gradingCriteria = '최소 1개 이상의 채점 기준을 입력해주세요.';
        } else {
            const totalWeight = form.value.gradingCriteria.reduce((sum, c) => sum + c.scoreWeight, 0);
            if (Math.abs(totalWeight - 1) > 0.001) {
                errors.value.gradingCriteria = '채점 기준 점수 가중치 합이 100%가 되어야 합니다.';
            }
        }
    }

    // 에러 메시지 정리
    const errorMessages = [];

    for (const key in errors.value) {
        const val = errors.value[key];
        if (Array.isArray(val)) {
            errorMessages.push(...val);
        } else if (typeof val === 'object' && val !== null) {
            errorMessages.push(...Object.values(val));
        } else if (typeof val === 'string') {
            errorMessages.push(val);
        }
    }

    // 차례로 토스트 띄우기
    if (errorMessages.length > 0) {
        errorMessages.forEach(msg => toast.error(msg));
        return false;
    }

    return true;
}


async function handleSubmit() {
    if (!validateForm()) return;

    try {
        if (form.value.type === 'MULTIPLE') {
            form.value.answer = form.value.questionOptions.find(opt => opt.isAnswer)?.content || '';
        }

        const dto = CreateQuestionRequestDTO.fromForm(form.value);

        await withErrorHandling(() => createQuestionService(dto), {
            showToast: true,
            redirect: false
        });

        toast.success('문제 등록이 완료되었습니다.');
        resetForm();
    } catch (e) {
        // 이미 withErrorHandling에서 처리하는 중
    }
}

</script>

<style scoped></style>