<template>
    <v-container>
        <v-card class="pa-6" elevation="3">
            <v-card-title class="text-h6 font-weight-bold mb-4">평가표 등록</v-card-title>

            <!-- 평가표 이름 입력 -->
            <v-text-field v-model="sheetName" label="평가표 이름을 입력해주세요." outlined dense class="mb-6" />

            <!-- 평가 기준 목록 -->
            <v-card outlined class="pa-4 mb-4">
                <div class="text-h6 mb-4">평가 기준</div>

                <div v-for="(criterion, index) in criteria" :key="index" class="mb-6" style="position: relative;">
                    <!-- 🔻 X 버튼: 제목/가중치 밑에, 텍스트에어리어 위에 위치 -->
                    <div class="d-flex justify-end mb-2">
                        <v-btn icon size="very-small" color="red" @click="removeCriterion(index)">
                            <v-icon size="18">mdi-close</v-icon>
                        </v-btn>
                    </div>
                    <v-row dense>
                        <v-col cols="12" md="8">
                            <v-text-field v-model="criterion.title" :label="`${index + 1}. 제목`" outlined dense
                                required />
                        </v-col>
                        <v-col cols="12" md="4">
                            <v-text-field v-model.number="criterion.weight" label="가중치 (%)" type="number" min="0"
                                max="100" suffix="%" outlined dense required />
                        </v-col>
                    </v-row>



                    <v-textarea v-model="criterion.content" label="내용을 입력해주세요." outlined rows="3" auto-grow dense
                        required />

                    <v-divider class="mt-4" />
                </div>


                <!-- 평가 기준 추가 버튼 -->
                <v-btn color="primary" variant="outlined" @click="addCriterion">
                    + 평가 기준 추가하기
                </v-btn>
            </v-card>

            <!-- 하단 버튼 -->
            <v-row justify="end">
                <v-btn class="mr-2" variant="outlined" color="grey" @click="goBackToCriteriaPage">취소하기</v-btn>
                <v-btn color="success" @click="submitSheet">등록하기</v-btn>
            </v-row>
        </v-card>
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
.v-card {
    max-width: 900px;
    margin: 0 auto;
}
</style>
