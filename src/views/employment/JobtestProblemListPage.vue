<template>
    <v-app>
        <v-main>
            <v-container class="pa-6">
                <h2 class="text-h6 font-weight-bold mb-4">실무 테스트 문제 리스트</h2>

                <!-- 로딩 상태 -->
                <v-progress-circular v-if="jobtestStore.loading" indeterminate color="primary"
                    class="d-flex mx-auto my-4"></v-progress-circular>

                <!-- 에러 메시지 -->
                <v-alert v-if="jobtestStore.error" type="error" class="mb-4" closable
                    @click:close="jobtestStore.error = null">
                    {{ jobtestStore.error }}
                </v-alert>

                <!-- 문제 리스트 -->
                <JobtestListView v-if="!jobtestStore.loading && !jobtestStore.error" :headers="headers"
                    :items="jobtestStore.questions" :showCheckbox="true" :showArrow="true"
                    @item-click="handleItemClick" />

                <div class="d-flex justify-end mt-4">
                    <v-btn color="error" variant="outlined" class="mr-2" :disabled="!jobtestStore.hasSelectedQuestions"
                        @click="handleDelete">
                        삭제하기
                    </v-btn>
                    <v-btn color="success" @click="handleCreate">
                        문제 등록하기
                    </v-btn>
                </div>
            </v-container>
        </v-main>
    </v-app>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import JobtestListView from '@/components/employment/JobtestListView.vue'
import { useJobtestStore } from '@/stores/jobtestStore'

const router = useRouter()
const jobtestStore = useJobtestStore()

const headers = [
    { label: '문제 제목', key: 'content' },
    { label: '유형', key: 'type' },
    { label: '난이도', key: 'difficulty' },
    { label: '출제자', key: 'createdMemberId' },
    { label: '수정자', key: 'updatedMemberId' },
]

// 문제 클릭 처리
const handleItemClick = (item) => {
    jobtestStore.toggleQuestionSelection(item.id)
}

// 삭제 처리
const handleDelete = () => {
    const selectedIds = jobtestStore.getSelectedQuestionIds()
    console.log('Selected questions:', selectedIds)
    // TODO: 선택된 문제 삭제 로직 구현
}

// 문제 등록
const handleCreate = () => {
    // TODO: 문제 등록 페이지로 이동
    console.log('Create new question')
}

// 컴포넌트 마운트 시 문제 목록 조회
onMounted(async () => {
    try {
        await jobtestStore.fetchQuestions()
    } catch (error) {
        console.error('Failed to fetch questions:', error)
    }
})
</script>

<style scoped></style>