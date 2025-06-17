<template>
    <v-container>
        <h2 class="text-h6 font-weight-bold mb-4">{{ isEdit ? '문제 수정' : '문제 등록' }}</h2>

        <v-tabs v-model="activeTab">
            <v-tab value="MULTIPLE">선택형</v-tab>
            <v-tab value="SUBJECTIVE">단답형</v-tab>
            <!-- <v-tab value="DESCRIPTIVE">서술형</v-tab> -->
        </v-tabs>

        <!-- 난이도 -->
        <v-select v-model="form.difficulty" :items="difficultyOptions" label="난이도" item-title="label" item-value="value"
            variant="outlined" class="mt-4 mb-4" />

        <!-- 문제 유형별 입력 폼 -->
        <component :is="currentComponent" v-model:form="form" />

        <div class="d-flex justify-end mt-4">
            <v-btn variant="tonal" @click="goBack">취소</v-btn>
            <v-btn color="primary" class="ml-2" @click="handleSubmit">
                {{ isEdit ? '수정하기' : '등록하기' }}
            </v-btn>
        </div>

        <SuccessModal v-if="showSuccessModal" :message="isEdit ? '문제 수정이 완료되었습니다.' : '문제 등록이 완료되었습니다.'"
            @confirm="handleSuccessConfirm" />
    </v-container>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useToast } from 'vue-toastification'

import MultipleQuestionForm from '@/components/employment/MultipleQuestionForm.vue'
import SubjectiveForm from '@/components/employment/SubjectiveForm.vue'
import DescriptiveQuestionForm from '@/components/employment/DescriptiveQuestionForm.vue'
import SuccessModal from '@/components/common/AlertModal.vue'

import { DIFFICULTY_MAP } from '@/constants/employment/difficulty'

import { createQuestionService, updateQuestionService, getQuestionDetailService } from '@/services/jobtestQuestionService'
import { CreateQuestionRequestDTO, UpdateQuestionRequestDTO } from '@/dto/employment/jobtest/questionRequestDTO'

import { useMemberStore } from '@/stores/memberStore'

const route = useRoute()
const router = useRouter()
const toast = useToast()
const memberStore = useMemberStore()

const questionId = Number(route.params.id)
const isEdit = !!questionId // id가 있으면 수정 모드

const activeTab = ref('MULTIPLE')
const showSuccessModal = ref(false)

const difficultyOptions = Object.entries(DIFFICULTY_MAP).map(([value, label]) => ({ value, label }))

const form = ref({
    content: '',
    detailContent: '',
    type: 'MULTIPLE',
    difficulty: 'EASY',
    answer: '',
    createdMemberId: null,
    updatedMemberId: null,
    questionOptions: [],
    gradingCriteria: []
})

const currentComponent = computed(() => {
    switch (activeTab.value) {
        case 'MULTIPLE': return MultipleQuestionForm
        case 'SUBJECTIVE': return SubjectiveForm
        default: return DescriptiveQuestionForm
    }
})

function goBack() {
    router.push({ name: 'JobtestQuestionList' })
}

function handleSuccessConfirm() {
    showSuccessModal.value = false
    goBack()
}

async function handleSubmit() {
    try {
        const userId = memberStore.form.id
        if (!userId) {
            toast.error('사용자 정보를 불러오지 못했습니다.')
            return
        }

        if (form.value.type === 'MULTIPLE') {
            form.value.answer = form.value.questionOptions.find(opt => opt.isAnswer)?.content || ''
        }

        if (isEdit) {
            form.value.updatedMemberId = userId
            const dto = new UpdateQuestionRequestDTO(
                form.value.content,
                form.value.detailContent,
                form.value.type,
                form.value.difficulty,
                form.value.answer,
                form.value.updatedMemberId
            )
            await updateQuestionService(questionId, dto)
        } else {
            form.value.createdMemberId = userId
            const dto = CreateQuestionRequestDTO.fromForm(form.value)
            await createQuestionService(dto)
        }

        showSuccessModal.value = true
    } catch (err) {
        toast.error('저장 중 오류가 발생했습니다.')
    }
}

async function loadQuestionIfEditing() {
    if (!isEdit) return
    try {
        const data = await getQuestionDetailService(questionId)
        Object.assign(form.value, {
            ...data,
            updatedMemberId: memberStore.form.id
        })
        activeTab.value = data.type
    } catch (e) {
        toast.error('문제 정보를 불러오는 중 오류가 발생했습니다.')
    }
}


onMounted(async () => {
    await memberStore.getMyInfo()
    if (!memberStore.form.id) {
        toast.error('사용자 정보를 불러오는 중입니다. 잠시 후 다시 시도해주세요.')
        return
    }

    await loadQuestionIfEditing()
})

watch(activeTab, (newType) => {
    form.value.type = newType;

    // 타입에 따라 관련 필드 초기화
    if (newType === 'MULTIPLE') {
        form.value.gradingCriteria = [];
    } else if (newType === 'SUBJECTIVE') {
        form.value.questionOptions = [];
        form.value.gradingCriteria = [];
    } else if (newType === 'DESCRIPTIVE') {
        form.value.answer = '';
        form.value.questionOptions = [];
    }
});
</script>