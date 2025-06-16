<template>
    <v-container>
        <v-form @submit.prevent="submitInterview">
            <v-row>
                <!-- 지원서 선택 -->
                <v-col cols="6">
                    <v-select v-model="selectedApplicationId" :items="applicationOptions" label="지원서 선택"
                        item-title="applicantName" item-value="id" />
                </v-col>

                <!-- 평가표 선택 버튼 -->
                <v-col cols="6">
                    <v-btn @click="showSheetModal = true" color="primary">평가표 선택</v-btn>
                    <div v-if="selectedSheet">
                        선택한 평가표: {{ selectedSheet.name }}
                    </div>
                </v-col>
            </v-row>

            <!-- 시간 선택 -->
            <v-row>
                <v-col cols="6">
                    <v-text-field v-model="time" label="면접 시간 (예: 14:30)" placeholder="HH:mm"
                        @blur="checkAvailability" />
                </v-col>
                <v-col cols="6">
                    <div v-if="isDatetimeAvailable === true" class="text-success">예약 가능</div>
                    <div v-else-if="isDatetimeAvailable === false" class="text-error">해당 시간대는 예약 불가</div>
                </v-col>
            </v-row>

            <!-- 줌 주소 입력 -->
            <v-row>
                <v-col cols="12">
                    <v-text-field v-model="address" label="Zoom 주소" />
                </v-col>
            </v-row>

            <!-- 등록 버튼 -->
            <v-row>
                <v-col cols="12">
                    <v-btn type="submit" color="success" :disabled="!isDatetimeAvailable">면접 등록</v-btn>
                </v-col>
            </v-row>
        </v-form>

        <!-- 평가표 모달 -->
        <InterviewSheetModal v-if="showSheetModal" @close="showSheetModal = false" @select-sheet="onSheetSelected" />
    </v-container>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useApplicationStore } from '@/stores/applicationStore'
import { useInterviewStore } from '@/stores/interviewStore'

import InterviewSheetModal from '@/components/employment/InterviewSheetModal.vue'

const route = useRoute()
const selectedDate = route.query.date  // 'YYYY-MM-DD' 형식
const applicationStore = useApplicationStore()
const interviewStore = useInterviewStore()

const selectedApplicationId = ref(null)
const selectedSheet = ref(null)
const showSheetModal = ref(false)

const time = ref('')
const address = ref('')
const isDatetimeAvailable = ref(null)
const applicationOptions = ref([])

const checkAvailability = async () => {
    if (!time.value || !selectedDate) return

    const datetime = `${selectedDate}T${time.value}`
    try {
        isDatetimeAvailable.value = await interviewStore.checkDatetimeAvailability(datetime)
    } catch (e) {
        console.error('시간 확인 실패', e)
    }
}

const onSheetSelected = (sheet) => {
    selectedSheet.value = sheet
    showSheetModal.value = false
}

const submitInterview = async () => {
    const datetime = `${selectedDate}T${time.value}`
    const dto = {
        applicationId: selectedApplicationId.value,
        sheetId: selectedSheet.value.id,
        datetime,
        address,
    }

    try {
        await interviewStore.createInterview(dto)
        alert('면접이 등록되었습니다!')
    } catch (e) {
        alert('등록 실패: ' + e.message)
    }
}

onMounted(async () => {
    applicationOptions.value = await applicationStore.fetchAllApplications()
})
</script>
