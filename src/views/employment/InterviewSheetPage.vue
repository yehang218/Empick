<template>
    <v-container fluid class="pa-5 grass-bg" style="min-height: 100vh;">
        <!-- 헤더 -->
        <v-row align="center" class="mb-4">
            <v-col cols="12" md="6">
                <h2 class="font-weight-bold grass-title">
                    <v-icon left color="grass-green">mdi-clipboard-list-outline</v-icon>
                    면접 평가표
                </h2>
            </v-col>
            <v-col cols="12" md="6" class="d-flex justify-end align-center">
                <v-btn class="grass-btn mr-2" size="small" @click="goToCreatePage" elevation="2">
                    <v-icon left>mdi-plus</v-icon> 평가표 추가
                </v-btn>
                <v-btn class="grass-btn grass-btn--danger" size="small" @click="openDeleteModal" elevation="2">
                    <v-icon left>mdi-trash-can-outline</v-icon> 평가표 삭제
                </v-btn>
            </v-col>
        </v-row>

        <!-- 평가표/기준 목록 -->
        <v-row no-gutters class="mb-4">
            <v-col cols="12" md="6" class="d-flex">
                <v-card class="pa-4 flex-grow-1 w-100 mr-2 grass-card" elevation="2" rounded="xl">
                    <OneColumnList
                        title="평가표"
                        :items="sheets.map(item => item.name)"
                        @update:selectedItem="onSelectSheet"
                    />
                </v-card>
            </v-col>
            <v-col cols="12" md="6" class="d-flex">
                <v-card class="pa-4 flex-grow-1 w-100 ml-2 grass-card" elevation="2" rounded="xl">
                    <OneColumnList
                        title="평가 기준"
                        :items="criteriaList.map(item => `${item.title} (${Math.round(item.weight * 100)}%)`)"
                        @update:selectedItem="onSelectItemByTitle"
                    />
                </v-card>
            </v-col>
        </v-row>

        <!-- 상세 내용 -->
        <v-row>
            <v-col cols="12">
                <v-card outlined class="pa-6 grass-card" elevation="1" rounded="xl" style="min-height: 180px;">
                    <div class="d-flex align-center mb-2">
                        <v-icon color="grass-green" class="mr-2">mdi-information-outline</v-icon>
                        <span class="text-h6 font-weight-bold grass-title">상세 내용</span>
                    </div>
                    <div class="text-body-1" style="min-height: 60px;">
                        {{ selectedCriteria?.content || '선택된 항목이 없습니다.' }}
                    </div>
                </v-card>
            </v-col>
        </v-row>

        <Modal v-if="showDeleteModal" message="정말 삭제하시겠습니까?" @confirm="confirmDelete" @cancel="closeDeleteModal" />
    </v-container>
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

const criteriaList = computed(() => criteriaStore.criteriaList)  // 이건 OK
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
    console.log('✅ 불러온 기준 목록:', criteriaList.value)
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

<style scoped>
.grass-bg {
    background: #e8f5e9;
}
.grass-title {
    color: #204d28;
    font-family: 'Spoqa Han Sans Neo', 'sans-serif';
    letter-spacing: -1px;
}
.grass-card {
    background: #f1f8f4;
    border-radius: 24px !important;
    border: 1.5px solid #81c784;
}
.grass-btn {
    background: #388e3c !important;
    color: #fff !important;
    border-radius: 20px !important;
    font-weight: bold;
    font-family: 'Spoqa Han Sans Neo', 'sans-serif';
    transition: box-shadow 0.2s;
}
.grass-btn:hover {
    box-shadow: 0 2px 8px #388e3c55;
}
.grass-btn--danger {
    background: #204d28 !important;
    color: #fff !important;
}
/* Vuetify 커스텀 컬러 */
.v-icon[color="grass-green"] {
    color: #388e3c !important;
}
</style>
