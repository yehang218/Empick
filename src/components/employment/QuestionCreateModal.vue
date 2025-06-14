<template>
    <v-card-text>
        <h2 class="text-h6 font-weight-bold mb-4">문제 등록</h2>

        <v-tabs v-model="activeTab">
            <v-tab value="MULTIPLE">선택형</v-tab>
            <v-tab value="SUBJECTIVE">단답형</v-tab>
            <v-tab value="DESCRIPTIVE">서술형</v-tab>
        </v-tabs>

        <v-select v-model="form.difficulty" :items="difficultyOptions" label="난이도" variant="outlined" class="mt-4" />

        <component :is="currentComponent" v-model:form="form" />

        <div class="d-flex justify-end mt-6">
            <v-btn variant="tonal" @click="close">취소하기</v-btn>
            <v-btn color="primary" class="ml-2" @click="handleSubmit">등록하기</v-btn>
        </div>
    </v-card-text>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useToast } from 'vue-toastification'
import MultipleQuestionForm from '@/components/employment/MultipleQuestionForm.vue'
import SubjectiveForm from '@/components/employment/SubjectiveForm.vue'
import DescriptiveQuestionForm from '@/components/employment/DescriptiveQuestionForm.vue'
import { createQuestionService } from '@/services/jobtestQuestionService'
import CreateQuestionRequestDTO from '@/dto/employment/jobtest/createQuestionRequestDTO'

const emit = defineEmits(['close', 'saved'])
const toast = useToast()

const activeTab = ref('MULTIPLE')

const form = ref({
    type: 'MULTIPLE',
    content: '',
    detailContent: '',
    difficulty: 'EASY',
    answer: '',
    createdMemberId: 1,
    questionOptions: [],
    gradingCriteria: []
})

const difficultyOptions = [
    { title: '쉬움', value: 'EASY' },
    { title: '보통', value: 'MEDIUM' },
    { title: '어려움', value: 'HARD' }
]

watch(activeTab, (newType) => {
    form.value.type = newType
})

const currentComponent = computed(() => {
    switch (activeTab.value) {
        case 'MULTIPLE': return MultipleQuestionForm
        case 'SUBJECTIVE': return SubjectiveForm
        default: return DescriptiveQuestionForm
    }
})

const close = () => {
    emit('close')
}

const handleSubmit = async () => {
    if (!validateForm()) return

    try {
        if (form.value.type === 'MULTIPLE') {
            form.value.answer = form.value.questionOptions.find(opt => opt.isAnswer)?.content || ''
        }

        const dto = CreateQuestionRequestDTO.fromForm(form.value)
        await createQuestionService(dto)

        toast.success('문제 등록이 완료되었습니다.')
        emit('saved')
    } catch (e) {
        toast.error('문제 등록 중 오류가 발생했습니다.')
    }
}

function validateForm() {
    if (!form.value.content) {
        toast.error('문제 내용을 입력해주세요.')
        return false
    }

    if (form.value.type === 'SUBJECTIVE' && !form.value.answer) {
        toast.error('정답을 입력해주세요.')
        return false
    }

    if (form.value.type === 'MULTIPLE') {
        if (!form.value.questionOptions.length) {
            toast.error('선택지를 하나 이상 입력해주세요.')
            return false
        }
        if (form.value.questionOptions.some(opt => !opt.content)) {
            toast.error('선택지 내용을 입력해주세요.')
            return false
        }
        if (!form.value.questionOptions.some(opt => opt.isAnswer)) {
            toast.error('하나 이상의 정답을 지정해주세요.')
            return false
        }
    }

    if (form.value.type === 'DESCRIPTIVE') {
        if (!form.value.gradingCriteria.length) {
            toast.error('채점 기준을 입력해주세요.')
            return false
        }
        const total = form.value.gradingCriteria.reduce((sum, c) => sum + c.scoreWeight, 0)
        if (Math.abs(total - 1) > 0.001) {
            toast.error('채점 기준 가중치의 합이 100%가 되어야 합니다.')
            return false
        }
    }

    return true
}
</script>

<style scoped></style>