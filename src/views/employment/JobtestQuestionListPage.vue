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
                                :disabled="!jobtestStore.hasSelectedQuestions" @click="handleDelete">
                                삭제하기
                            </v-btn>
                            <v-btn color="success" @click="handleCreate">
                                문제 등록하기
                            </v-btn>
                        </div>
                    </v-col>
                </v-row>
                <!-- 로딩 상태 -->
                <v-progress-circular v-if="jobtestStore.loading" indeterminate color="primary"
                    class="d-flex mx-auto my-4"></v-progress-circular>

                <!-- 에러 메시지 -->
                <v-alert v-if="jobtestStore.error" type="error" class="mb-4" closable
                    @click:close="jobtestStore.error = null">
                    {{ jobtestStore.error }}
                </v-alert>

                <!-- 문제 리스트 -->

                <ListView :headers="headers" :data="jobtestStore.questions" :showCheckbox="true"
                    @item-click="handleItemClick" />

                <QuestionDetailModal v-model="detailDialogVisible" :question="selectedQuestionDetail" />
            </v-container>
        </v-main>
    </v-app>

    <QuestionDetailModal v-model="showModal" :question="selectedQuestion" @close="showModal = false" />
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import ListView from '@/components/common/ListView.vue'
import { useJobtestStore } from '@/stores/jobtestQuestionStore'

import { getQuestionDetailService } from '@/services/jobtestQuestionService'
import QuestionQueryDTO from '@/dto/employment/jobtest/questionDetailResponseDTO'
import QuestionDetailModal from '@/components/employment/JobtestQuestionDetailModal.vue'

const router = useRouter()
const jobtestStore = useJobtestStore()
const detailDialogVisible = ref(false)
const selectedQuestionDetail = ref(null)

const headers = [
    { label: '문제 제목', key: 'content' },
    { label: '유형', key: 'type' },
    { label: '난이도', key: 'difficulty' },
    { label: '출제자', key: 'createdMemberName' },
    { label: '수정자', key: 'updatedMemberName' },
]

const handleItemClick = async (item) => {
    try {
        const detail = await getQuestionDetailService(item.id)
        selectedQuestionDetail.value = detail
        detailDialogVisible.value = true
    } catch (err) {
        console.error('문제 상세 조회 실패:', err)
    }
}
// 삭제 처리
const handleDelete = () => {
    const selectedIds = jobtestStore.getSelectedQuestionIds()
    console.log('Selected questions:', selectedIds)
    // TODO: 선택된 문제 삭제 로직 구현
}

// 문제 등록
const handleCreate = () => {
    router.push({ name: 'JobtestQuestionCreate' });
}

// 컴포넌트 마운트 시 문제 목록 조회
onMounted(async () => {
    try {
        await jobtestStore.fetchQuestions()
    } catch (error) {
        console.error('Failed to fetch questions:', error)
    }
})

onMounted(() => jobtestStore.fetchQuestions())
</script>

<style scoped></style>