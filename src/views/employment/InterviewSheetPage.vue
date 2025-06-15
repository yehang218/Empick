<template>
    <v-container fluid class="pa-5" style="height: 100vh; display: flex; flex-direction: column;">
        <!-- 헤더 -->
        <v-row align="center" class="mb-4" style="height: 64px; flex-shrink: 0;">
            <v-col cols="6">
                <h2>면접 평가표</h2>
            </v-col>
            <v-col cols="6" class="d-flex justify-end">
                <div>
                    <v-btn color="primary" class="mr-2" size="small" @click="goToCreatePage">+ 평가표 추가</v-btn>
                    <!-- <v-btn color="secondary" class="mr-2" size="small" @click="goToEditPage">✏️ 평가표 수정</v-btn> -->
                    <v-btn color="error" size="small" @click="openDeleteModal">🗑 평가표 삭제</v-btn>
                </div>
            </v-col>
        </v-row>

        <!-- 평가표/기준 목록 -->
        <v-row no-gutters style="height: 40%; flex-shrink: 0; margin-bottom: 16px;">
            <v-col cols="6" class="d-flex">
                <div class="pa-4 flex-grow-1 w-100" style="height: 70%;">
                    <OneColumnList title="평가표" :items="sheets.map(item => item.name)"
                        @update:selectedItem="onSelectSheet" />
                </div>
            </v-col>
            <v-col cols="6" class="d-flex">
                <div class="pa-4 flex-grow-1 w-100" style="height: 70%;">
                    <OneColumnList title="평가 기준"
                        :items="criteriaList.map(item => `${item.title} (${Math.round(item.weight * 100)}%)`)"
                        @update:selectedItem="onSelectItemByTitle" />
                </div>
            </v-col>
        </v-row>


        <!-- 상세 내용 -->
        <v-row style="height: 50%; flex-shrink: 0;">
            <v-col cols="12" class="d-flex">
                <v-card outlined class="pa-4 flex-grow-1 w-100" style="height: 90%; overflow-y: auto;">
                    <div class="text-subtitle-1 font-weight-bold mb-2">상세 내용</div>
                    <div>{{ selectedCriteria?.content || '선택된 항목이 없습니다.' }}</div>
                </v-card>
            </v-col>
        </v-row>

    </v-container>

    <Modal v-if="showDeleteModal" message="정말 삭제하시겠습니까?" @confirm="confirmDelete" @cancel="closeDeleteModal" />
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import OneColumnList from '@/components/OneColumnList.vue'
import Modal from '@/components/common/Modal.vue'

import { useInterviewSheetStore } from '@/stores/interviewSheetStore'
import { useInterviewCriteriaStore } from '@/stores/interviewCriteriaStore'

const sheetStore = useInterviewSheetStore()
const criteriaStore = useInterviewCriteriaStore()

const sheets = computed(() => sheetStore.sheetList)
const selectedSheet = ref(null)

const criteriaList = computed(() => criteriaStore.criteriaList)
const selectedCriteria = computed(() => criteriaStore.selectedCriteria)

const router = useRouter()

// ✅ 평가표 등록 페이지 이동
const goToCreatePage = () => {
    router.push('/employment/interview-criteria/create')
}

// ✅ 초기 로딩
onMounted(async () => {
    await sheetStore.fetchAllSheets()
})


// ✅ 평가표 선택 시 평가 기준 가져오기
const onSelectSheet = async (sheetName) => {
    const targetSheet = sheets.value.find(s => s.name === sheetName)
    if (!targetSheet) return

    selectedSheet.value = targetSheet
    await criteriaStore.fetchCriteriaBySheetId(targetSheet.id)
}

// ✅ 평가 기준 선택
const onSelectItemByTitle = (label) => {
    const title = label.replace(/\s*\(\d+%?\)\s*$/, '')
    const found = criteriaList.value.find(c => c.title === title)
    if (found) criteriaStore.selectedCriteria = found
}

const showDeleteModal = ref(false)

// ✅ 삭제
const openDeleteModal = () => showDeleteModal.value = true
const closeDeleteModal = () => showDeleteModal.value = false

const confirmDelete = async () => {
    try {
        await sheetStore.deleteSheet(selectedSheet.value.id)
        selectedSheet.value = null
        criteriaStore.criteriaList = []
        closeDeleteModal()
    } catch (err) {
        console.error('삭제 오류:', err)
        alert('삭제 중 오류가 발생했습니다.')
    }
}


</script>
