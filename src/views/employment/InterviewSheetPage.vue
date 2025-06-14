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
                    <v-btn color="secondary" class="mr-2" size="small">✏️ 평가표 수정</v-btn>
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
                    <div>{{ selectedCriteria.content || '선택된 항목이 없습니다.' }}</div>
                </v-card>
            </v-col>
        </v-row>

    </v-container>

    <Modal v-if="showDeleteModal" message="정말 삭제하시겠습니까?" @confirm="confirmDelete" @cancel="closeDeleteModal" />
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import OneColumnList from '@/components/OneColumnList.vue'
import Modal from '@/components/common/Modal.vue'
import { getAllSheetsService, deleteSheetService } from '@/services/interviewSheetService'
import { getCriteriaBySheetIdService } from '@/services/interviewCriteriaService'

const sheets = ref([])
const selectedSheet = ref(null)
const criteriaList = ref([])
const selectedCriteria = ref({})
const router = useRouter()

const goToCreatePage = () => {
    router.push('/employment/interview-criteria/create')
}

// 평가표 전체 조회
const fetchSheets = async () => {
    sheets.value = await getAllSheetsService()
}

// 평가표 선택 시 평가 기준 목록 불러오기
const onSelectSheet = async (selectedSheetName) => {
    const sheet = sheets.value.find(sheet => sheet.name === selectedSheetName)
    if (!sheet) {
        console.warn('선택된 이름에 해당하는 평가표를 찾을 수 없습니다:', selectedSheetName)
        return
    }

    selectedSheet.value = sheet

    try {
        const loadedCriteria = await getCriteriaBySheetIdService(sheet.id)
        criteriaList.value = loadedCriteria.map(dto => ({
            id: dto.id,
            title: dto.title,       // 평가 기준 제목
            content: dto.content,   // 평가 기준 설명
            weight: dto.weight
        }))
        selectedCriteria.value = {} // 선택 초기화
    } catch (error) {
        console.error('평가 기준 조회 실패:', error)
        alert('평가 기준을 불러오는 데 실패했습니다.')
    }
}

// 평가 기준 선택
const onSelectItemByTitle = (label) => {
    const titleOnly = label.replace(/\s*\(\d+%?\)\s*$/, '') // "제목 (70%)" → "제목"
    const selected = criteriaList.value.find(c => c.title === titleOnly)
    selectedCriteria.value = selected || {}
}

// 초기 로딩
onMounted(fetchSheets)

const showDeleteModal = ref(false)

const openDeleteModal = () => {
    showDeleteModal.value = true
}
const closeDeleteModal = () => {
    showDeleteModal.value = false
}

const confirmDelete = async () => {
    try {
        await deleteSheetService(selectedSheet.value.id)
        closeDeleteModal()
        fetchSheets()
    } catch (err) {
        console.error(err)
        alert('삭제 중 오류가 발생했습니다.')
    }
}
</script>
