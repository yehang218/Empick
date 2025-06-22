<template>
    <v-container>
        <h2 class="text-h6 font-weight-bold mb-4">{{ isEdit ? '문제 수정' : '문제 등록' }}</h2>

        <v-tabs v-model="activeTab" :disabled="isEdit">
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

        <SuccessModal 
            v-if="showSuccessModal" 
            :message="isEdit ? '문제 수정이 완료되었습니다.' : '문제 등록이 완료되었습니다.'"
            @confirm="handleSuccessConfirm" 
        />

        <Modal 
            v-if="showCancelModal && !showSuccessModal" 
            message="정말 취소하시겠습니까?<br>입력한 내용이 모두 사라집니다." 
            @confirm="handleCancelConfirm" 
            @cancel="handleCancelClose" 
        />
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
import Modal from '@/components/common/Modal.vue'

import { DIFFICULTY_MAP } from '@/constants/employment/difficulty'

import { useMemberStore } from '@/stores/memberStore'
import { useJobtestQuestionStore } from '@/stores/jobtestQuestionStore'

const route = useRoute()
const router = useRouter()
const toast = useToast()

const memberStore = useMemberStore()
const jobtestQuestionStore = useJobtestQuestionStore()

const questionId = Number(route.params.id)
const showSuccessModal = ref(false)
const showCancelModal = ref(false)

// ❗ form과 isEdit은 store에서 가져옴
const form = computed(() => jobtestQuestionStore.form)
const isEdit = computed(() => jobtestQuestionStore.isEdit)

const activeTab = ref(form.value.type || 'MULTIPLE')

const difficultyOptions = Object.entries(DIFFICULTY_MAP).map(([value, label]) => ({
    value,
    label
}))

const currentComponent = computed(() => {
    switch (activeTab.value) {
        case 'MULTIPLE': return MultipleQuestionForm
        case 'SUBJECTIVE': return SubjectiveForm
        default: return DescriptiveQuestionForm
    }
})

// activeTab이 변경될 때 폼 타입도 함께 업데이트
watch(activeTab, (newType) => {
    if (!isEdit.value) { // 수정 모드가 아닐 때만 타입 변경 허용
        form.value.type = newType
    }
})

function goBack() {
    // 입력된 내용이 있는지 확인
    const hasContent = form.value.content || form.value.answer || 
                      (form.value.questionOptions && form.value.questionOptions.length > 0) ||
                      (form.value.gradingCriteria && form.value.gradingCriteria.length > 0)
    
    if (hasContent) {
        showCancelModal.value = true
    } else {
        // 내용이 없으면 바로 목록으로 이동
        router.push({ name: 'JobtestQuestionList' })
    }
}

function handleSuccessConfirm() {
    showSuccessModal.value = false
    router.push({ name: 'JobtestQuestionList' })
}

async function handleSubmit() {
    try {
        const userId = memberStore.form.id
        if (!userId) {
            toast.error('사용자 정보를 불러오지 못했습니다.')
            return
        }

        await jobtestQuestionStore.submitQuestion(userId)
        showSuccessModal.value = true
    } catch (error) {
        console.error('문제 저장 실패:', error)
        toast.error(jobtestQuestionStore.error || '저장 중 오류가 발생했습니다.')
    }
}

function handleCancelConfirm() {
    showCancelModal.value = false
    jobtestQuestionStore.resetForm()
    router.push({ name: 'JobtestQuestionList' })
}

function handleCancelClose() {
    showCancelModal.value = false
    // 취소 모달을 닫으면 현재 페이지에 머무름
}

onMounted(async () => {
    try {
        await memberStore.getMyInfo()
        const memberId = memberStore.form.id
        if (!memberId) {
            toast.error('사용자 정보를 불러오는 중입니다. 잠시 후 다시 시도해주세요.')
            router.push({ name: 'JobtestQuestionList' })
            return
        }

        if (questionId) {
            await jobtestQuestionStore.loadQuestion(questionId, memberId)
        } else {
            jobtestQuestionStore.resetForm()
        }

        activeTab.value = form.value.type || 'MULTIPLE'
    } catch (error) {
        console.error('페이지 초기화 실패:', error)
        toast.error('페이지 로딩 중 오류가 발생했습니다.')
        router.push({ name: 'JobtestQuestionList' })
    }
})
</script>