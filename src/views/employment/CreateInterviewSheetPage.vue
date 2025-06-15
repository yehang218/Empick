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
                <v-btn class="mr-2" variant="outlined" color="grey" @click="resetForm">취소하기</v-btn>
                <v-btn color="success" @click="submitSheet">등록하기</v-btn>
            </v-row>
        </v-card>
    </v-container>
</template>

<script setup>
import { ref } from 'vue'
import { createSheetService } from '@/services/interviewSheetService'
import { createCriteriaService } from '@/services/interviewCriteriaService'


const removeCriterion = (index) => {
    if (criteria.value.length === 1) {
        alert("최소 하나의 평가 기준은 필요합니다.");
        return;
    }
    criteria.value.splice(index, 1);
};

const sheetName = ref('')
const criteria = ref([
    { title: '', content: '', weight: 0 }
])

const memberId = 1

const addCriterion = () => {
    criteria.value.push({ title: '', content: '', weight: 0 })
}

const resetForm = () => {
    sheetName.value = ''
    criteria.value = [{ title: '', content: '', weight: 0 }]
}

const submitSheet = async () => {
    if (!sheetName.value.trim()) {
        alert('평가표 이름을 입력해주세요.');
        return;
    }

    if (criteria.length === 0) {
        alert('최소 하나 이상의 평가 기준을 추가해주세요.');
        return;
    }

    // ✅ 가중치 총합 검증
    const totalWeight = criteria.value.reduce((sum, criterion) => sum + criterion.weight, 0);
    if (totalWeight > 100) {
        alert(`가중치 총합이 100%를 초과했습니다. 현재 총합: ${totalWeight}%`);
        return;
    }

    if (totalWeight < 100) {
        const confirmProceed = confirm(`가중치 총합이 100%가 아닙니다. (${totalWeight}%) 그래도 등록하시겠습니까?`);
        if (!confirmProceed) {
            return;
        }
    }

    const timestamp = new Date().toISOString();

    // 1. 평가표 등록
    const sheetDTO = {
        id: null,
        name: sheetName.value,
        isDeleted: false,
        memberId: 1,
        updatedAt: timestamp
    };

    try {
        const sheetResponse = await createSheetService(sheetDTO);
        console.log(sheetResponse);
        const sheetId = sheetResponse.id;


        // 2. 각 평가 기준 등록
        for (const criterion of criteria.value) {
            const criteriaDTO = {
                id: null,
                sheetId: sheetId,
                title: criterion.title,
                content: criterion.content,
                weight: criterion.weight / 100,
                isDeleted: 'N',
                memberId: 1,
                updatedAt: timestamp
            };

            await createCriteriaService(criteriaDTO);
        }

        alert('평가표와 기준들이 성공적으로 등록되었습니다!');
        resetForm();
    } catch (error) {
        console.error('등록 실패:', error);
        alert('등록 중 오류가 발생했습니다.');
    }
};

</script>

<style scoped>
.v-card {
    max-width: 900px;
    margin: 0 auto;
}
</style>
