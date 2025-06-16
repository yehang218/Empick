<template>
    <v-container>
        <v-form @submit.prevent="submitInterview">
            <v-row>
                <!-- 지원서 선택 -->
                <v-col cols="6">
                    <v-select v-model="selectedApplicationId" :items="applicationOptions" item-title="applicantName"
                        item-value="id" label="지원서 선택" />
                </v-col>

                <!-- 평가표 선택 버튼 -->
                <v-col cols="6">
                    <v-btn @click="openSheetModal" color="primary">평가표 선택</v-btn>
                    <div v-if="selectedSheet">
                        선택한 평가표: {{ selectedSheet.name }}
                    </div>
                </v-col>
            </v-row>

            <!-- 시간 선택 -->
            <v-row>
                <v-col cols="3">
                    <v-select v-model="selectedHour" :items="hours" label="시 선택" />
                </v-col>
                <v-col cols="3">
                    <v-select v-model="selectedMinute" :items="minutes" label="분 선택" />
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
                    <v-btn type="submit" color="success" :disabled="isDatetimeAvailable !== true">면접 등록</v-btn>
                </v-col>
            </v-row>
        </v-form>

        <!-- 평가표 모달 -->
        <InterviewSheetModal v-model="showSheetModal" @select-sheet="onSheetSelected" @close="closeSheetModal" />
    </v-container>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { useApplicationStore } from '@/stores/applicationStore'
import { useInterviewStore } from '@/stores/interviewStore'
import { useApplicantStore } from '@/stores/applicantStore'

import InterviewSheetModal from '@/components/employment/InterviewSheetModal.vue'


const route = useRoute()
const selectedDate = route.query.date  // 'YYYY-MM-DD' 형식
const applicationStore = useApplicationStore()
const applicantStore = useApplicantStore()
const interviewStore = useInterviewStore()

const selectedApplicationId = ref(null)
const selectedSheet = ref(null)

const showSheetModal = ref(false)
const openSheetModal = () => {
    console.log('✅ 모달 열기 시도됨')
    showSheetModal.value = true;
}
const closeSheetModal = () => showSheetModal.value = false;

const selectedHour = ref('')
const selectedMinute = ref('')

const hours = Array.from({ length: 10 }, (_, i) => String(i + 9).padStart(2, '0')) // ['09', '10', ..., '18']
const minutes = ['00', '10', '20', '30', '40', '50']

// 시(hour) 변경 시 체크
watch(selectedHour, (val) => {
    console.log('⏰ Hour changed:', val)
    checkAvailability()
})

// 분(minute) 변경 시 체크
watch(selectedMinute, (val) => {
    console.log('🕐 Minute changed:', val)
    checkAvailability()
})

const getTimeString = () => {
    if (!selectedHour.value || !selectedMinute.value) return ''
    return `${selectedHour.value}:${selectedMinute.value}`
}

const address = ref('')
const isDatetimeAvailable = ref(null)
const applicationOptions = ref([])

const checkAvailability = async () => {
    const timeString = getTimeString()
    if (!timeString) {
        console.log('⛔ 시간 문자열이 없음')
        return
    }
    if (!selectedDate) {
        console.log('⛔ 날짜 문자열이 없음')
        return
    }

    const datetime = `${selectedDate}T${timeString}`
    console.log('🧪 체크할 datetime:', datetime)

    try {
        await interviewStore.checkDatetimeAvailability(datetime)
        console.log('✅ 응답 받음:', interviewStore.isDatetimeAvailable)
        isDatetimeAvailable.value = interviewStore.isDatetimeAvailable
        console.log('isDatetimeAvailable : ', isDatetimeAvailable);
        console.log('interviewStore.isDatetimeAvailable : ', interviewStore.isDatetimeAvailable);
    } catch (e) {
        isDatetimeAvailable.value = null;
        console.error('❌ 시간 확인 실패:', e)
    }
}

const onSheetSelected = (sheet) => {
    selectedSheet.value = sheet
    showSheetModal.value = false
}

const submitInterview = async () => {
    const timeString = getTimeString()
    const datetime = `${selectedDate}T${timeString}`
    const dto = {
        applicationId: selectedApplicationId.value,
        sheetId: selectedSheet.value?.id,
        datetime,
        address: address.value,
    }

    try {
        await interviewStore.createInterview(dto)
        alert('면접이 등록되었습니다!')
    } catch (e) {
        alert('등록 실패: ' + e.message)
    }
}

onMounted(async () => {
    const rawList = await applicationStore.fetchAllApplications()
    const withNames = await Promise.all(rawList.map(async app => {
        const applicant = await applicantStore.fetchApplicantById(app.applicantId)
        return {
            ...app,
            applicantName: applicant.name // ✅ 이름 추가
        }
    }))
    applicationOptions.value = withNames
})
</script>
