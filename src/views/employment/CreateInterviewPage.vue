<template>
    <v-container>
        <v-form @submit.prevent="submitInterview">
            <!-- 지원서 선택 + 정보 -->
            <v-row>
                <!-- 지원서 선택 -->
                <v-col cols="6">
                    <v-select v-model="selectedApplicationId" :items="applicationOptions" item-title="label"
                        item-value="id" label="지원서 선택" />
                </v-col>

                <!-- 고정 박스: 지원서 정보 -->
                <v-col cols="6">
                    <v-card elevation="2" class="pa-4" style="height: 220px; overflow-y: auto;">
                        <template v-if="selectedApplication">
                            <h3>선택된 지원서 정보</h3>
                            <p><strong>채용 공고 제목:</strong> {{ selectedApplication.recruitment.title }}</p>
                            <p><strong>제출일:</strong> {{ selectedApplication.application.createdAt }}</p>
                            <p><strong>지원자 이름:</strong> {{ selectedApplication.applicant.name }}</p>
                            <p><strong>이메일:</strong> {{ selectedApplication.applicant.email }}</p>
                            <p><strong>전화번호:</strong> {{ selectedApplication.applicant.phone }}</p>
                            <p><strong>자기소개서 평가 점수:</strong> {{ selectedApplication.application.coverLetterScore ??
                                '미평가' }}</p>

                            <v-btn color="secondary" @click="goToApplicationDetail(selectedApplication.id)">
                                지원서 상세보기
                            </v-btn>
                        </template>
                        <template v-else>
                            <p class="text-grey">선택된 지원서가 없습니다.</p>
                        </template>
                    </v-card>
                </v-col>
            </v-row>

            <!-- 평가 기준 목록 표시 고정 박스 -->
            <v-row class="mt-4">
                <v-col cols="12">
                    <v-card elevation="2" class="pa-4" style="height: 300px; overflow-y: auto;">
                        <template v-if="criteriaList.length > 0">
                            <InterviewEvaluationCriteria v-model:criteriaList="criteriaList" />
                        </template>
                        <template v-else>
                            <p class="text-grey">선택된 평가표가 없습니다. 평가표를 먼저 선택해주세요.</p>
                        </template>
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
            <v-btn color="primary" class="mt-4" @click="goToInterviewPage">
                뒤로 가기
            </v-btn>
        </v-form>

        <!-- 평가표 모달 -->
        <InterviewSheetModal v-model="showSheetModal" @select-sheet="onSheetSelected" @close="closeSheetModal" />
    </v-container>
</template>

<script setup>
import { ref, onMounted, watch, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useApplicationStore } from '@/stores/applicationStore'
import { useInterviewStore } from '@/stores/interviewStore'
import { useApplicantStore } from '@/stores/applicantStore'
import { useRecruitmentStore } from '@/stores/recruitmentStore'
import { useInterviewCriteriaStore } from '@/stores/interviewCriteriaStore'

import InterviewSheetModal from '@/components/employment/InterviewSheetModal.vue'
import InterviewEvaluationCriteria from '@/components/employment/InterviewEvaluationCriteria.vue'



const route = useRoute()
const router = useRouter()
const selectedDate = route.query.date  // 'YYYY-MM-DD' 형식
const applicationStore = useApplicationStore()
const applicantStore = useApplicantStore()
const interviewStore = useInterviewStore()
const recruitmentStore = useRecruitmentStore()
const interviewCriteriaStore = useInterviewCriteriaStore()

const goToInterviewPage = () => router.push('/employment/interviews')

const goToApplicationDetail = (applicationId) => {
    const selected = selectedApplication.value;
    if (!selected) return;
    
    router.push({
        path: `/employment/applications/${applicationId}`,
        query: {
            // 기본 지원자 정보
            applicantId: selected.applicantId,
            applicationId: selected.id,
            name: selected.applicant?.name,
            phone: selected.applicant?.phone,
            email: selected.applicant?.email,
            profileUrl: selected.applicant?.profileUrl,
            birth: selected.applicant?.birth,
            address: selected.applicant?.address,
            recruitmentId: selected.recruitmentId,
            introduceRatingResultId: selected.introduceRatingResultId,
            jobId: selected.jobId,
            jobName: selected.jobName,
            createdAt: selected.createdAt,
            status: selected.status,
            updatedAt: selected.updatedAt,
            updatedBy: selected.updatedBy,

            // 추가된 필드들
            introduceEvaluationContent: selected.introduceEvaluationContent,
            introduceScore: selected.introduceScore,
            introduceStatus: selected.introduceStatus,
            motivation: selected.motivation,
            experience: selected.experience,
            skills: selected.skills,
            education: selected.education,
            portfolioUrl: selected.portfolioUrl,
            coverLetter: selected.coverLetter,
            jobtestTotalScore: selected.jobtestTotalScore,
            jobtestEvaluationScore: selected.jobtestEvaluationScore,
            jobtestStatus: selected.jobtestStatus,
            interviewScore: selected.interviewScore,
            interviewAddress: selected.interviewAddress,
            interviewDatetime: selected.interviewDatetime
        }
    })
}

const selectedHour = ref('')
const selectedMinute = ref('')

const applicationList = ref([])
const selectedApplication = ref(null)
const selectedApplicationId = ref(null);

const selectedApplicant = ref(null)
const selectedRecruitment = ref(null)

const sheetList = ref([])
const selectedSheet = ref(null)

const criteriaList = ref([])
const selectedCriteria = ref(null)

const address = ref('')
const isDatetimeAvailable = ref(null)
const applicationOptions = ref([])

const hours = Array.from({ length: 10 }, (_, i) => String(i + 9).padStart(2, '0')) // ['09', '10', ..., '18']
const minutes = ['00', '10', '20', '30', '40', '50']

watch(selectedApplicationId, async (newId) => {
    const selected = applicationOptions.value.find(app => String(app.id) === String(newId));
    if (!selected) {
        selectedApplication.value = null;
        return;
    }

    // 필요한 정보 다시 fetch (안 해도 되지만 보장용)
    try {
        // 지원자 정보
        await applicantStore.fetchApplicantById(selected.applicant.id);
        selected.applicant = applicantStore.selectedApplicant;

        // 채용공고 정보
        await recruitmentStore.loadRecruitmentDetail(selected.recruitment.id);
        selected.recruitment = recruitmentStore.detail.recruitment;

        selected.application = selected;

        selectedApplication.value = selected;

        console.log('🎯 선택된 지원서:', selectedApplication.value);
    } catch (e) {
        console.warn('❌ 선택된 지원서 정보 불러오기 실패', e);
        selectedApplication.value = null;
    }
});

watch(selectedSheet, async (newSheet) => {
    if (!newSheet?.id) return;
    const sheetId = newSheet.id
    try {
        await interviewCriteriaStore.fetchCriteriaBySheetId(sheetId)
        criteriaList.value = interviewCriteriaStore.criteriaList
        console.log('✅ 불러온 평가 기준:', criteriaList.value);
    } catch (e) {
        console.error('❌ 평가 기준 불러오기 실패:', e);
    }
});

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
    return `${selectedHour.value}:${selectedMinute.value}:00`
}



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
    console.log('selectedDate : ', selectedDate)
    console.log('timeString : ', timeString)
    const datetime = `${selectedDate}T${timeString}`
    console.log('datetime : ', datetime)
    const dto = {
        applicationId: selectedApplication.value.id,
        sheetId: selectedSheet.value?.id,
        datetime,
        address: address.value,
    }
    console.log('dto : ', dto)

    await interviewStore.createInterview(dto)
    alert('면접이 등록되었습니다!')
    // try {
    //     await interviewStore.createInterview(dto)
    //     alert('면접이 등록되었습니다!')
    //     router.push('/employment/interviews')
    // } catch (e) {
    //     alert('등록 실패: ' + e.message)
    // }
}

onMounted(async () => {
    await applicationStore.fetchAllApplications()
    const applications = applicationStore.applicationList
    console.log('applications :', applications)
    if (!Array.isArray(applications)) {
        console.error("applicationList is not an array:", applications)
        return
    }
    applicationList.value = applicationStore.applicationList

    const withDetails = await Promise.all(
        applicationList.value.map(async (app) => {
            try {
                // 지원자 정보 가져오기
                // 지원자 정보
                await applicantStore.fetchApplicantById(app.applicantId);
                const applicant = applicantStore.selectedApplicant;
                if (!applicant) return null;

                // 채용 공고 정보
                await recruitmentStore.loadRecruitmentDetail(app.recruitmentId);
                const recruitment = recruitmentStore.detail.recruitment;
                if (!recruitment) return null;

                // 면접 정보
                await interviewStore.fetchInterviewByApplicationId(app.id);
                const interview = interviewStore.selectedInterview;
                const exist = !!interview; // true or false

                return {
                    ...app,
                    application: app,
                    applicant,
                    recruitment,
                    interview,
                    label: `${applicant.name} - ${recruitment.title} - ${exist ? '✅ 면접 있음' : '❌ 없음'}`,
                    interviewExist: exist
                };
            } catch (error) {
                console.error('❌ 에러 발생!');
                console.log('➡️ message:', error.message);
                console.log('➡️ name:', error.name);
                console.log('➡️ stack:', error.stack);
                console.log('➡️ response:', error.response);
                console.log('➡️ config:', error.config);
                console.log('➡️ code:', error.code);
                console.error('에러 발생:', {
                    message: error.response?.data?.message ?? error.message,
                    code: error.response?.data?.code,
                    status: error.response?.status,
                    path: error.config?.url
                });
            }
        })
    )
    applicationOptions.value = withDetails.filter(Boolean)  // null 제거
})

const showSheetModal = ref(false)
const openSheetModal = () => {
    console.log('✅ 모달 열기 시도됨')
    showSheetModal.value = true;
}
const closeSheetModal = () => showSheetModal.value = false;

</script>
