<template>
    <v-container>
        <v-form @submit.prevent="submitInterview">
            <v-row>
                <!-- 지원서 선택 -->
                <v-col cols="6">
                    <v-select v-model="selectedApplicationId" :items="applicationOptions" item-title="label"
                        item-value="id" label="지원서 선택" />
                </v-col>

                <v-row v-if="selectedApplication">
                    <v-col cols="12">
                        <v-card class="pa-4 mt-2">
                            <h3>선택된 지원서 정보</h3>
                            <p><strong>채용 공고 제목:</strong> {{ selectedApplication.recruitmentTitle }}</p>
                            <p><strong>제출일:</strong> {{ selectedApplication.createdAt }}</p>
                            <p><strong>지원자 이름:</strong> {{ selectedApplication.applicant.name }}</p>
                            <p><strong>이메일:</strong> {{ selectedApplication.applicant.email }}</p>
                            <p><strong>전화번호:</strong> {{ selectedApplication.applicant.phone }}</p>
                            <p><strong>자기소개서 평가 점수:</strong> {{ selectedApplication.coverLetterScore ?? '미평가' }}</p>

                            <v-btn color="secondary" @click="goToApplicationDetail(selectedApplication.id)">
                                지원서 상세보기
                            </v-btn>
                        </v-card>
                    </v-col>
                </v-row>

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
import { ref, onMounted, watch, computed } from 'vue'
import { useRoute } from 'vue-router'
import { useApplicationStore } from '@/stores/applicationStore'
import { useInterviewStore } from '@/stores/interviewStore'
import { useApplicantStore } from '@/stores/applicantStore'
import { useRecruitmentStore } from '@/stores/recruitmentStore'

import InterviewSheetModal from '@/components/employment/InterviewSheetModal.vue'


const route = useRoute()
const selectedDate = route.query.date  // 'YYYY-MM-DD' 형식
const applicationStore = useApplicationStore()
const applicantStore = useApplicantStore()
const interviewStore = useInterviewStore()
const recruitmentStore = useRecruitmentStore()

const selectedApplication = computed(() => {
    return applicationOptions.value.find(app => app.id === selectedApplicationId.value)
})
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
    await applicationStore.fetchAllApplications()
    const rawList = applicationStore.applicationList

    const withDetails = await Promise.all(
        rawList.map(async app => {
            try {
                // 지원자 정보 가져오기
                await applicantStore.fetchApplicantById(app.applicantId)
                const applicant = applicantStore.selectedApplicant
                if (!applicant) return null

                // 채용 공고 정보 가져오기
                await recruitmentStore.loadRecruitmentDetail(app.recruitmentId)
                const recruitment = recruitmentStore.detail
                if (!recruitment) return null

                return {
                    ...app,
                    applicantName: applicant.name,
                    recruitmentTitle: recruitment.title,
                    label: `${applicant.name} - ${recruitment.title}`,
                    applicant,
                }
            } catch (error) {
                // 커스텀 에러의 경우: 코드 또는 상태로 판단
                if (error.code === 'RECRUITMENT_NOT_FOUND' || error.status === 404) {
                    return null
                }

                // 예상 외 에러는 콘솔에 표시
                console.warn(`❌ 지원서 ${app.id} 처리 중 오류 발생`, error)
                return null
            }
        })
    )

    applicationOptions.value = withDetails.filter(Boolean)  // null 제거
})
</script>
