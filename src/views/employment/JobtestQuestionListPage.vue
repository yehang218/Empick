<template>
    <v-app>
        <v-main>
            <v-container class="pa-6">
                <v-row>
                    <v-col cols="4">
                        <h2 class="text-h6 font-weight-bold mb-4">실무 테스트 문제 리스트</h2>
                    </v-col>
                    <v-spacer />
                    <v-col cols="4">
                        <div class="d-flex justify-end mt-4">
                            <v-btn color="error" variant="outlined" class="mr-2"
                                :disabled="!questionStore.hasSelectedQuestions" @click="handleDelete">
                                삭제하기
                            </v-btn>
                            <v-btn color="success" @click="handleCreate">
                                문제 등록하기
                            </v-btn>
                        </div>
                    </v-col>
                </v-row>

                <!-- 로딩 상태 -->
                <v-progress-circular v-if="questionStore.loading" indeterminate color="primary"
                    class="d-flex mx-auto my-4"></v-progress-circular>

                <!-- 에러 메시지 -->
                <v-alert v-if="questionStore.error" type="error" class="mb-4" closable
                    @click:close="questionStore.error = null">
                    {{ questionStore.error }}
                </v-alert>

                <!-- 문제 리스트 -->
                <ListView :headers="headers" :data="questionStore.questions" :showCheckbox="true"
                    @item-click="handleItemClick" @toggle-select="questionStore.toggleQuestionSelection" />

                <!-- 문제 상세 보기 -->
                <QuestionDetailModal v-model="detailDialogVisible" :question="selectedQuestionDetail" />
            </v-container>
        </v-main>
    </v-app>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useToast } from 'vue-toastification'

import ListView from '@/components/common/ListView.vue'
import QuestionDetailModal from '@/components/employment/JobtestQuestionDetailModal.vue'

import { useJobtestQuestionStore } from '@/stores/jobtestQuestionStore'

const router = useRouter()
const toast = useToast()
const questionStore = useJobtestQuestionStore()

const detailDialogVisible = ref(false)
const selectedQuestionDetail = ref(null)

const headers = [
    { label: '문제 제목', key: 'content' },
    { label: '유형', key: 'type' },
    { label: '난이도', key: 'difficulty' },
    { label: '출제자', key: 'createdMemberName' },
    { label: '수정자', key: 'updatedMemberName' }
]

// ✅ 문제 상세 클릭 → store 통해 가져오기
const handleItemClick = async (item) => {
    try {
        await questionStore.loadQuestion(item.id)
        selectedQuestionDetail.value = { ...questionStore.form }
        detailDialogVisible.value = true
    } catch (err) {
        toast.error('문제 상세 조회 중 오류가 발생했습니다.')
    }
}

// ✅ 선택된 문제 삭제
const handleDelete = async () => {
    if (!confirm('선택된 문제를 삭제하시겠습니까?')) return

    try {
        await questionStore.deleteSelectedQuestions()
        toast.success('선택된 문제가 삭제되었습니다.')
    } catch (err) {
        toast.error('문제 삭제 중 오류가 발생했습니다.')
    }
}

// ✅ 문제 등록 페이지로 이동
const handleCreate = () => {
    router.push({ name: 'JobtestQuestionCreate' })
}

// ✅ 컴포넌트 마운트 시 문제 목록 조회
onMounted(() => {
    questionStore.fetchQuestions()
})
</script>
