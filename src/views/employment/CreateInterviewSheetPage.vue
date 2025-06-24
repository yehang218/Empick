<template>
    <v-container class="create-bg" fluid>
        <!-- 헤더 -->
        <v-row align="center" class="mb-6">
            <v-col cols="12" md="8">
                <div class="sheet-header">
                    <v-icon size="40" class="mr-3 sheet-header__icon">mdi-clipboard-plus-outline</v-icon>
                    <div>
                        <h2 class="font-weight-bold sheet-header__title">평가표 등록</h2>
                        <div class="sheet-header__subtitle">새로운 면접 평가표와 기준을 등록하세요</div>
                    </div>
                </div>
            </v-col>
        </v-row>

        <v-row justify="center">
            <v-col cols="12" md="10" lg="8">
                <v-card class="pa-8 create-card" elevation="4" rounded="xl">
                    <!-- 평가표 이름 입력 -->
                    <div class="mb-6">
                        <v-text-field v-model="sheetName" label="평가표 이름을 입력해주세요." outlined dense class="sheet-input" />
                    </div>

                    <v-divider class="mb-6" />

                    <!-- 평가 기준 목록 -->
                    <div class="mb-4">
                        <div class="text-h6 font-weight-bold mb-4 d-flex align-center">
                            <v-icon class="mr-2" color="grass-green">mdi-format-list-bulleted-square</v-icon>
                            평가 기준
                        </div>

                        <div v-for="(criterion, index) in criteria" :key="index" class="mb-7 sheet-criterion-card">
                            <div class="d-flex justify-end mb-2">
                                <v-btn icon size="small" class="criterion-remove-btn" @click="removeCriterion(index)">
                                    <v-icon size="20">mdi-close</v-icon>
                                </v-btn>
                            </div>
                            <v-row dense>
                                <v-col cols="12" md="8">
                                    <v-text-field v-model="criterion.title" :label="`${index + 1}. 제목`" outlined dense required class="sheet-input" />
                                </v-col>
                                <v-col cols="12" md="4">
                                    <v-text-field v-model.number="criterion.weight" label="가중치 (%)" type="number" min="0" max="100" suffix="%" outlined dense required class="sheet-input" />
                                </v-col>
                            </v-row>
                            <v-textarea v-model="criterion.content" label="내용을 입력해주세요." outlined rows="3" auto-grow dense required class="sheet-input" />
                        </div>

                        <!-- 평가 기준 추가 버튼 -->
                        <v-btn class="sheet-btn sheet-btn--add mb-2" @click="addCriterion" elevation="2">
                            <v-icon left>mdi-plus</v-icon> 평가 기준 추가하기
                        </v-btn>
                    </div>

                    <v-divider class="mb-6" />

                    <!-- 하단 버튼 -->
                    <v-row justify="end" class="mt-2">
                        <v-btn class="mr-2 sheet-btn sheet-btn--cancel" variant="outlined" @click="goBackToCriteriaPage">취소하기</v-btn>
                        <v-btn class="sheet-btn sheet-btn--submit" color="success" @click="submitSheet">등록하기</v-btn>
                    </v-row>
                </v-card>
            </v-col>
        </v-row>
    </v-container>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

import { useInterviewSheetStore } from '@/stores/interviewSheetStore'
import { useInterviewCriteriaStore } from '@/stores/interviewCriteriaStore'
import { useAuthStore } from '@/stores/authStore'

const sheetStore = useInterviewSheetStore()
const criteriaStore = useInterviewCriteriaStore()
const authStore = useAuthStore()
const memberId = authStore.userInfo?.id

const router = useRouter()

const sheetName = ref('')
const criteria = ref([
    { title: '', content: '', weight: 0 }
])

const addCriterion = () => {
    criteria.value.push({ title: '', content: '', weight: 0 })
}

const removeCriterion = (index) => {
    if (criteria.value.length === 1) {
        alert("최소 하나의 평가 기준은 필요합니다.");
        return;
    }
    criteria.value.splice(index, 1);
}

const goBackToCriteriaPage = () => {
    sheetName.value = ''
    criteria.value = [{ title: '', content: '', weight: 0 }]
    router.push('/employment/interview-criteria')
}

const submitSheet = async () => {
    if (!sheetName.value.trim()) {
        alert('평가표 이름을 입력해주세요.');
        return;
    }

    if (criteria.value.length === 0) {
        alert('최소 하나 이상의 평가 기준을 추가해주세요.');
        return;
    }

    const totalWeight = criteria.value.reduce((sum, criterion) => sum + criterion.weight, 0);
    if (totalWeight !== 100) {
        alert(`가중치 총합이 반드시 100%여야 합니다. 현재 총합: ${totalWeight}%`);
        return;
    }

    const timestamp = new Date().toISOString()
    console.log('userInfo:', authStore.userInfo)

    try {
        // ✅ 1. Store 통해 평가표 생성
        const sheetDTO = {
            id: null,
            name: sheetName.value,
            isDeleted: false,
            memberId: memberId,
            updatedAt: timestamp
        }
        const sheetResponse = await sheetStore.createSheet(sheetDTO)
        const sheetId = sheetResponse.id

        // ✅ 2. Store 통해 각 기준 생성
        for (const c of criteria.value) {
            const criteriaDTO = {
                id: null,
                sheetId,
                title: c.title,
                content: c.content,
                weight: c.weight / 100, // 0~1 로 변환
                isDeleted: 'N',
                memberId: memberId,
                updatedAt: timestamp
            }
            await criteriaStore.createCriteria(criteriaDTO)
        }

        alert('평가표와 기준들이 성공적으로 등록되었습니다!')
        goBackToCriteriaPage()
    } catch (error) {
        console.error('등록 실패:', error)
        alert('등록 중 오류가 발생했습니다.')
    }
}

</script>

<style scoped>
.create-bg {
    background: linear-gradient(135deg, #e8f5e9 0%, #f1f8f4 100%);
    min-height: 100vh;
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

.create-card {
    background: #fff;
    border-radius: 24px !important;
    border: 1.5px solid #81c784;
    box-shadow: 0 2px 12px #81c78422;
}
.sheet-input {
    font-size: 1.1rem;
}
.sheet-criterion-card {
    background: #f1f8f4;
    border-radius: 16px;
    border: 1.5px solid #a5d6a7;
    box-shadow: 0 2px 8px #81c78422;
    padding: 1.2rem 1.5rem 1.5rem 1.5rem;
    margin-bottom: 1.5rem;
    transition: box-shadow 0.18s;
}
.sheet-criterion-card:hover {
    box-shadow: 0 4px 18px #388e3c33;
}
.criterion-remove-btn {
    background: #fff !important;
    color: #e53935 !important;
    border-radius: 50% !important;
    box-shadow: 0 2px 8px #e5737322;
    transition: box-shadow 0.18s, transform 0.18s;
}
.criterion-remove-btn:hover {
    background: #e57373 !important;
    color: #fff !important;
    box-shadow: 0 4px 16px #e5737333;
    transform: scale(1.08);
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
.sheet-btn--add {
    background: linear-gradient(90deg, #a5d6a7 0%, #43a047 100%) !important;
    color: #204d28 !important;
    font-weight: bold;
    font-size: 1rem;
    border-radius: 18px !important;
    box-shadow: 0 2px 8px #81c78433;
}
.sheet-btn--add:hover {
    box-shadow: 0 4px 18px #388e3c55;
    transform: translateY(-2px) scale(1.03);
}
.sheet-btn--cancel {
    background: #fff !important;
    color: #388e3c !important;
    border: 1.5px solid #81c784 !important;
    font-weight: bold;
    font-size: 1rem;
    border-radius: 18px !important;
    box-shadow: 0 2px 8px #81c78422;
}
.sheet-btn--submit {
    background: linear-gradient(90deg, #43a047 0%, #388e3c 100%) !important;
    color: #fff !important;
    font-weight: bold;
    font-size: 1rem;
    border-radius: 18px !important;
    box-shadow: 0 2px 8px #388e3c33;
}
.sheet-btn--submit:hover {
    box-shadow: 0 4px 18px #388e3c55;
    transform: translateY(-2px) scale(1.03);
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
    .create-card {
        padding: 1.5rem !important;
    }
}
</style>
