<template>
    <v-container fluid class="pa-5 grass-bg" style="min-height: 100vh;">
        <!-- 헤더 -->
        <v-row align="center" class="mb-6">
            <v-col cols="12" md="7">
                <div class="sheet-header">
                    <v-icon size="40" class="mr-3 sheet-header__icon">mdi-clipboard-list-outline</v-icon>
                    <div>
                        <h2 class="font-weight-bold sheet-header__title">면접 평가표</h2>
                        <div class="sheet-header__subtitle">면접 평가표와 평가 기준을 관리하세요</div>
                    </div>
                </div>
            </v-col>
            <v-col cols="12" md="5" class="d-flex justify-end align-center">
                <v-btn class="grass-btn mr-2 sheet-btn" size="large" @click="goToCreatePage" elevation="3">
                    <v-icon left>mdi-plus</v-icon> 평가표 추가
                </v-btn>
                <v-btn class="grass-btn grass-btn--danger sheet-btn" size="large" @click="openDeleteModal" elevation="3">
                    <v-icon left>mdi-trash-can-outline</v-icon> 평가표 삭제
                </v-btn>
            </v-col>
        </v-row>

        <!-- 평가표/기준 목록 -->
        <v-row no-gutters class="mb-6">
            <v-col cols="12" md="6" class="d-flex">
                <v-card class="pa-4 flex-grow-1 w-100 mr-2 grass-card sheet-list-card" elevation="4" rounded="xl">
                    <OneColumnList
                        title="평가표"
                        :items="sheets.map(item => item.name)"
                        @update:selectedItem="onSelectSheet"
                        class="sheet-list"
                    />
                </v-card>
            </v-col>
            <v-col cols="12" md="6" class="d-flex">
                <v-card class="pa-4 flex-grow-1 w-100 ml-2 grass-card sheet-list-card" elevation="4" rounded="xl">
                    <OneColumnList
                        title="평가 기준"
                        :items="criteriaList.map(item => `${item.title} (${Math.round(item.weight * 100)}%)`)"
                        @update:selectedItem="onSelectItemByTitle"
                        class="sheet-list"
                    />
                </v-card>
            </v-col>
        </v-row>

        <!-- 상세 내용 -->
        <v-row>
            <v-col cols="12">
                <v-card outlined class="pa-8 grass-card sheet-detail-card" elevation="2" rounded="xl" style="min-height: 200px;">
                    <div class="d-flex align-center mb-4">
                        <v-icon size="32" color="grass-green" class="mr-3">mdi-information-outline</v-icon>
                        <span class="text-h5 font-weight-bold grass-title">상세 내용</span>
                    </div>
                    <v-divider class="mb-4" />
                    <div class="text-body-1 sheet-detail-content">
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
    background: linear-gradient(135deg, #e8f5e9 0%, #f1f8f4 100%);
}
.sheet-header {
    display: flex;
    align-items: center;
    background: linear-gradient(90deg, #43a047 0%, #81c784 100%);
    border-radius: 18px;
    padding: 2rem 2.5rem 1.5rem 2rem;
    box-shadow: 0 4px 24px rgba(67, 160, 71, 0.08);
    margin-bottom: 0.5rem;
}
.sheet-header__icon {
    color: #fff !important;
    background: #388e3c;
    border-radius: 50%;
    padding: 0.5rem;
    box-shadow: 0 2px 8px #388e3c33;
}
.sheet-header__title {
    color: #fff;
    font-size: 2.1rem;
    letter-spacing: -1px;
    margin-bottom: 0.2rem;
}
.sheet-header__subtitle {
    color: #e0f2f1;
    font-size: 1.1rem;
    font-weight: 400;
}

.sheet-btn {
    background: linear-gradient(90deg, #43a047 0%, #388e3c 100%) !important;
    color: #fff !important;
    border-radius: 24px !important;
    font-weight: bold;
    font-size: 1.1rem;
    box-shadow: 0 2px 12px #388e3c33;
    transition: box-shadow 0.2s, transform 0.2s;
}
.sheet-btn:hover {
    box-shadow: 0 4px 18px #388e3c55;
    transform: translateY(-2px) scale(1.03);
}
.grass-btn--danger {
    background: linear-gradient(90deg, #204d28 0%, #388e3c 100%) !important;
}

.grass-card {
    background: #f1f8f4;
    border-radius: 24px !important;
    border: 1.5px solid #81c784;
    box-shadow: 0 2px 12px #81c78422;
}
.sheet-list-card {
    min-height: 340px;
    transition: box-shadow 0.2s;
}
.sheet-list-card:hover {
    box-shadow: 0 6px 24px #388e3c33;
}

/* OneColumnList 커스텀 (선택/hover 효과) */
.sheet-list >>> .v-list-item {
    border-radius: 12px;
    margin-bottom: 0.5rem;
    transition: background 0.18s, box-shadow 0.18s;
}
.sheet-list >>> .v-list-item:hover {
    background: #e0f2f1 !important;
    box-shadow: 0 2px 8px #81c78433;
}
.sheet-list >>> .v-list-item--active {
    background: linear-gradient(90deg, #a5d6a7 0%, #e0f2f1 100%) !important;
    color: #204d28 !important;
    font-weight: bold;
    box-shadow: 0 2px 12px #388e3c22;
}

.sheet-detail-card {
    background: #fff;
    border: 1.5px solid #81c784;
    box-shadow: 0 2px 12px #388e3c22;
    min-height: 200px;
}
.sheet-detail-content {
    min-height: 60px;
    font-size: 1.15rem;
    color: #204d28;
    padding: 0.5rem 0.5rem 0.5rem 0.2rem;
}

@media (max-width: 900px) {
    .sheet-header {
        flex-direction: column;
        align-items: flex-start;
        padding: 1.2rem 1rem 1rem 1rem;
    }
    .sheet-header__title {
        font-size: 1.5rem;
    }
    .sheet-header__subtitle {
        font-size: 1rem;
    }
    .sheet-btn {
        font-size: 1rem;
        padding: 0.7rem 1.2rem;
    }
    .sheet-list-card {
        min-height: 220px;
    }
    .sheet-detail-card {
        padding: 1.5rem !important;
    }
}
</style>
