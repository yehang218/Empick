<template>
    <v-container class="create-interview-page" fluid>
        <!-- 헤더 섹션 -->
        <div class="page-header mb-6">
            <div class="header-content">
                <h1 class="page-title">
                    <v-icon class="mr-3" color="primary" size="32">mdi-calendar-plus</v-icon>
                    면접 일정 등록
                </h1>
                <p class="page-subtitle">새로운 면접 일정을 등록하고 관리하세요</p>
            </div>
        </div>

        <v-form @submit.prevent="submitInterview" class="interview-form">
            <!-- 지원서 선택 섹션 -->
            <v-card class="form-section mb-6" elevation="0">
                <div class="section-header">
                    <v-icon color="primary" class="mr-2">mdi-file-document-outline</v-icon>
                    <h2 class="section-title">지원서 선택</h2>
                </div>
                
                <v-row>
                    <v-col cols="12" lg="6">
                        <v-select 
                            v-model="selectedApplicationId" 
                            :items="applicationOptions" 
                            item-title="label"
                            item-value="id" 
                            label="지원서를 선택해주세요" 
                            variant="outlined"
                            prepend-inner-icon="mdi-account-search"
                            class="custom-select"
                        />
                    </v-col>
                </v-row>
            </v-card>

            <!-- 지원서 정보 섹션 -->
            <v-card class="form-section mb-6" elevation="0">
                <div class="section-header">
                    <v-icon color="primary" class="mr-2">mdi-account-details</v-icon>
                    <h2 class="section-title">지원자 정보</h2>
                </div>
                
                <v-row>
                    <v-col cols="12">
                        <div v-if="selectedApplication" class="applicant-info-card">
                            <div class="info-grid">
                                <div class="info-item">
                                    <div class="info-label">
                                        <v-icon size="16" class="mr-1">mdi-briefcase</v-icon>
                                        채용 공고
                                    </div>
                                    <div class="info-value">{{ selectedApplication.recruitment.title }}</div>
                                </div>
                                
                                <div class="info-item">
                                    <div class="info-label">
                                        <v-icon size="16" class="mr-1">mdi-account</v-icon>
                                        지원자 이름
                                    </div>
                                    <div class="info-value">{{ selectedApplication.applicant.name }}</div>
                                </div>
                                
                                <div class="info-item">
                                    <div class="info-label">
                                        <v-icon size="16" class="mr-1">mdi-email</v-icon>
                                        이메일
                                    </div>
                                    <div class="info-value">{{ selectedApplication.applicant.email }}</div>
                                </div>
                                
                                <div class="info-item">
                                    <div class="info-label">
                                        <v-icon size="16" class="mr-1">mdi-phone</v-icon>
                                        전화번호
                                    </div>
                                    <div class="info-value">{{ selectedApplication.applicant.phone }}</div>
                                </div>
                                
                                <div class="info-item">
                                    <div class="info-label">
                                        <v-icon size="16" class="mr-1">mdi-calendar</v-icon>
                                        제출일
                                    </div>
                                    <div class="info-value">{{ selectedApplication.application.createdAt }}</div>
                                </div>
                                
                                <div class="info-item">
                                    <div class="info-label">
                                        <v-icon size="16" class="mr-1">mdi-star</v-icon>
                                        자기소개서 점수
                                    </div>
                                    <div class="info-value score-value">
                                        {{ selectedApplication.application.coverLetterScore ?? '미평가' }}
                                    </div>
                                </div>
                            </div>
                            
                            <div class="action-buttons">
                                <v-btn 
                                    color="primary" 
                                    variant="outlined"
                                    prepend-icon="mdi-eye"
                                    @click="goToApplicationDetail(selectedApplication.id)"
                                    class="detail-btn"
                                >
                                    지원서 상세보기
                                </v-btn>
                            </div>
                        </div>
                        <div v-else class="empty-state">
                            <v-icon size="48" color="grey-lighten-1" class="mb-3">mdi-account-question</v-icon>
                            <p class="empty-text">지원서를 선택하면 지원자 정보가 표시됩니다</p>
                        </div>
                    </v-col>
                </v-row>
            </v-card>

            <!-- 평가표 선택 섹션 -->
            <v-card class="form-section mb-6" elevation="0">
                <div class="section-header">
                    <v-icon color="primary" class="mr-2">mdi-clipboard-list</v-icon>
                    <h2 class="section-title">평가표 선택</h2>
                </div>
                
                <v-row>
                    <v-col cols="12" lg="6">
                        <v-btn 
                            @click="openSheetModal" 
                            color="primary" 
                            variant="outlined"
                            prepend-icon="mdi-clipboard-plus"
                            class="sheet-select-btn"
                        >
                            평가표 선택하기
                        </v-btn>
                        
                        <div v-if="selectedSheet" class="selected-sheet-info">
                            <v-chip color="success" variant="tonal" class="mt-3">
                                <v-icon start>mdi-check-circle</v-icon>
                                선택된 평가표: {{ selectedSheet.name }}
                            </v-chip>
                        </div>
                    </v-col>
                </v-row>
            </v-card>

            <!-- 평가 기준 섹션 -->
            <v-card class="form-section mb-6" elevation="0">
                <div class="section-header">
                    <v-icon color="primary" class="mr-2">mdi-criteria</v-icon>
                    <h2 class="section-title">평가 기준</h2>
                </div>
                
                <div class="criteria-container">
                    <template v-if="criteriaList.length > 0">
                        <InterviewEvaluationCriteria v-model:criteriaList="criteriaList" />
                    </template>
                    <template v-else>
                        <div class="empty-state">
                            <v-icon size="48" color="grey-lighten-1" class="mb-3">mdi-clipboard-text</v-icon>
                            <p class="empty-text">평가표를 선택하면 평가 기준이 표시됩니다</p>
                        </div>
                    </template>
                </div>
            </v-card>

            <!-- 면접 일정 섹션 -->
            <v-card class="form-section mb-6" elevation="0">
                <div class="section-header">
                    <v-icon color="primary" class="mr-2">mdi-clock-outline</v-icon>
                    <h2 class="section-title">면접 일정</h2>
                </div>
                
                <v-row>
                    <v-col cols="12" lg="3">
                        <v-select 
                            v-model="selectedHour" 
                            :items="hours" 
                            label="시 선택" 
                            variant="outlined"
                            prepend-inner-icon="mdi-clock"
                        />
                    </v-col>
                    <v-col cols="12" lg="3">
                        <v-select 
                            v-model="selectedMinute" 
                            :items="minutes" 
                            label="분 선택" 
                            variant="outlined"
                            prepend-inner-icon="mdi-timer"
                        />
                    </v-col>
                    <v-col cols="12" lg="6">
                        <div class="availability-status">
                            <div v-if="isDatetimeAvailable === true" class="status-available">
                                <v-icon color="success" class="mr-2">mdi-check-circle</v-icon>
                                <span>예약 가능한 시간입니다</span>
                            </div>
                            <div v-else-if="isDatetimeAvailable === false" class="status-unavailable">
                                <v-icon color="error" class="mr-2">mdi-close-circle</v-icon>
                                <span>해당 시간대는 예약이 불가능합니다</span>
                            </div>
                            <div v-else class="status-neutral">
                                <v-icon color="grey" class="mr-2">mdi-clock-outline</v-icon>
                                <span>시간을 선택해주세요</span>
                            </div>
                        </div>
                    </v-col>
                </v-row>
            </v-card>

            <!-- 면접 장소 섹션 -->
            <v-card class="form-section mb-6" elevation="0">
                <div class="section-header">
                    <v-icon color="primary" class="mr-2">mdi-map-marker</v-icon>
                    <h2 class="section-title">면접 장소</h2>
                </div>
                
                <v-row>
                    <v-col cols="12" lg="6">
                        <v-text-field 
                            v-model="address" 
                            label="Zoom 주소 또는 면접 장소" 
                            variant="outlined"
                            prepend-inner-icon="mdi-video"
                            placeholder="https://zoom.us/j/..."
                        />
                    </v-col>
                </v-row>
            </v-card>

            <!-- 액션 버튼 섹션 -->
            <div class="action-section">
                <v-row>
                    <v-col cols="12" class="d-flex justify-space-between align-center">
                        <v-btn 
                            color="secondary" 
                            variant="outlined"
                            prepend-icon="mdi-arrow-left"
                            @click="goToInterviewPage"
                            class="back-btn"
                        >
                            뒤로 가기
                        </v-btn>
                        
                        <v-btn 
                            type="submit" 
                            color="success" 
                            size="large"
                            :disabled="isDatetimeAvailable !== true"
                            prepend-icon="mdi-calendar-check"
                            class="submit-btn"
                        >
                            면접 등록하기
                        </v-btn>
                    </v-col>
                </v-row>
            </div>
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
import { useToast } from 'vue-toastification'

const toast = useToast()
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
    toast.success('면접이 등록되었습니다!')
    router.push('/employment/interviews')
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

<style scoped>
.create-interview-page {
    background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
    min-height: 100vh;
    padding: 2rem 0;
}

.page-header {
    text-align: center;
    margin-bottom: 3rem;
}

.header-content {
    background: rgba(255, 255, 255, 0.95);
    backdrop-filter: blur(10px);
    border-radius: 20px;
    padding: 2rem;
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
    border: 1px solid rgba(255, 255, 255, 0.2);
}

.page-title {
    font-size: 2.5rem;
    font-weight: 700;
    color: #2c3e50;
    margin-bottom: 0.5rem;
    display: flex;
    align-items: center;
    justify-content: center;
}

.page-subtitle {
    font-size: 1.1rem;
    color: #7f8c8d;
    margin: 0;
}

.interview-form {
    max-width: 1200px;
    margin: 0 auto;
}

.form-section {
    background: rgba(255, 255, 255, 0.95);
    backdrop-filter: blur(10px);
    border-radius: 16px;
    padding: 2rem;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
    border: 1px solid rgba(255, 255, 255, 0.2);
    transition: all 0.3s ease;
}

.form-section:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
}

.section-header {
    display: flex;
    align-items: center;
    margin-bottom: 1.5rem;
    padding-bottom: 1rem;
    border-bottom: 2px solid #e8f5e8;
}

.section-title {
    font-size: 1.4rem;
    font-weight: 600;
    color: #2c3e50;
    margin: 0;
}

.custom-select {
    border-radius: 12px;
}

.applicant-info-card {
    background: linear-gradient(135deg, #e3f2fd 0%, #bbdefb 100%);
    border-radius: 16px;
    padding: 2rem;
    color: #1565c0;
    position: relative;
    overflow: hidden;
}

.applicant-info-card::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><defs><pattern id="grain" width="100" height="100" patternUnits="userSpaceOnUse"><circle cx="25" cy="25" r="1" fill="white" opacity="0.1"/><circle cx="75" cy="75" r="1" fill="white" opacity="0.1"/><circle cx="50" cy="10" r="0.5" fill="white" opacity="0.1"/><circle cx="10" cy="60" r="0.5" fill="white" opacity="0.1"/><circle cx="90" cy="40" r="0.5" fill="white" opacity="0.1"/></pattern></defs><rect width="100" height="100" fill="url(%23grain)"/></svg>');
    pointer-events: none;
}

.info-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
    gap: 1.5rem;
    margin-bottom: 2rem;
}

.info-item {
    background: rgba(255, 255, 255, 0.3);
    backdrop-filter: blur(10px);
    border-radius: 12px;
    padding: 1.5rem;
    border: 1px solid rgba(255, 255, 255, 0.4);
    transition: all 0.3s ease;
}

.info-item:hover {
    transform: translateY(-2px);
    background: rgba(255, 255, 255, 0.4);
}

.info-label {
    font-size: 0.9rem;
    font-weight: 500;
    color: rgba(21, 101, 192, 0.8);
    margin-bottom: 0.5rem;
    display: flex;
    align-items: center;
}

.info-value {
    font-size: 1.1rem;
    font-weight: 600;
    color: #1565c0;
}

.score-value {
    background: rgba(255, 255, 255, 0.4);
    padding: 0.5rem 1rem;
    border-radius: 20px;
    display: inline-block;
    font-weight: 700;
    color: #1565c0;
}

.action-buttons {
    text-align: center;
}

.detail-btn {
    background: rgba(255, 255, 255, 0.4);
    border: 2px solid rgba(255, 255, 255, 0.5);
    color: #1565c0;
    font-weight: 600;
    padding: 0.75rem 2rem;
    border-radius: 25px;
    transition: all 0.3s ease;
}

.detail-btn:hover {
    background: rgba(255, 255, 255, 0.6);
    transform: translateY(-2px);
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

.empty-state {
    text-align: center;
    padding: 3rem 2rem;
    background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
    border-radius: 16px;
    border: 2px dashed #dee2e6;
}

.empty-text {
    color: #6c757d;
    font-size: 1.1rem;
    margin: 0;
}

.sheet-select-btn {
    background: linear-gradient(135deg, #29b6f6 0%, #0288d1 100%) !important;
    color: white !important;
    border: none;
    padding: 1rem 2rem;
    border-radius: 12px;
    font-weight: 600;
    transition: all 0.3s ease;
    display: flex;
    align-items: center;
    justify-content: center;
    line-height: 1;
}

.sheet-select-btn:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 25px rgba(41, 182, 246, 0.4);
    color: white !important;
}

.selected-sheet-info {
    margin-top: 1rem;
}

.criteria-container {
    background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
    border-radius: 12px;
    padding: 1.5rem;
    min-height: 200px;
}

.availability-status {
    display: flex;
    align-items: center;
    padding: 1rem;
    border-radius: 12px;
    font-weight: 600;
    font-size: 1.1rem;
}

.status-available {
    background: linear-gradient(135deg, #d4edda 0%, #c3e6cb 100%);
    color: #155724;
    border: 2px solid #c3e6cb;
}

.status-unavailable {
    background: linear-gradient(135deg, #f8d7da 0%, #f5c6cb 100%);
    color: #721c24;
    border: 2px solid #f5c6cb;
}

.status-neutral {
    background: linear-gradient(135deg, #e2e3e5 0%, #d6d8db 100%);
    color: #6c757d;
    border: 2px solid #d6d8db;
}

.action-section {
    background: rgba(255, 255, 255, 0.95);
    backdrop-filter: blur(10px);
    border-radius: 16px;
    padding: 2rem;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
    border: 1px solid rgba(255, 255, 255, 0.2);
    margin-top: 2rem;
}

.back-btn {
    background: linear-gradient(135deg, #424242 0%, #212121 100%) !important;
    color: white !important;
    border: none;
    padding: 1rem 2rem;
    border-radius: 12px;
    font-weight: 600;
    transition: all 0.3s ease;
    display: flex;
    align-items: center;
    justify-content: center;
    line-height: 1;
}

.back-btn:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 25px rgba(66, 66, 66, 0.4);
    color: white !important;
}

.submit-btn {
    background: linear-gradient(135deg, #2e7d32 0%, #388e3c 100%) !important;
    color: white !important;
    border: none;
    padding: 1rem 3rem;
    border-radius: 12px;
    font-weight: 600;
    font-size: 1.1rem;
    transition: all 0.3s ease;
    display: flex;
    align-items: center;
    justify-content: center;
    line-height: 1;
}

.submit-btn:hover:not(:disabled) {
    transform: translateY(-2px);
    box-shadow: 0 8px 25px rgba(46, 125, 50, 0.4);
    color: white !important;
}

.submit-btn:disabled {
    background: linear-gradient(135deg, #6c757d 0%, #495057 100%) !important;
    opacity: 0.6;
    cursor: not-allowed;
    color: white !important;
}

/* 반응형 디자인 */
@media (max-width: 768px) {
    .page-title {
        font-size: 2rem;
        flex-direction: column;
        gap: 1rem;
    }
    
    .info-grid {
        grid-template-columns: 1fr;
    }
    
    .action-section .d-flex {
        flex-direction: column;
        gap: 1rem;
    }
    
    .back-btn, .submit-btn {
        width: 100%;
    }
}

/* 애니메이션 효과 */
@keyframes fadeInUp {
    from {
        opacity: 0;
        transform: translateY(30px);
    }
    to {
        opacity: 1;
        transform: translateY(0);
    }
}

.form-section {
    animation: fadeInUp 0.6s ease-out;
}

.form-section:nth-child(2) { animation-delay: 0.1s; }
.form-section:nth-child(3) { animation-delay: 0.2s; }
.form-section:nth-child(4) { animation-delay: 0.3s; }
.form-section:nth-child(5) { animation-delay: 0.4s; }
.form-section:nth-child(6) { animation-delay: 0.5s; }
.form-section:nth-child(7) { animation-delay: 0.6s; }

/* 스크롤바 스타일링 */
::-webkit-scrollbar {
    width: 8px;
}

::-webkit-scrollbar-track {
    background: #f1f1f1;
    border-radius: 4px;
}

::-webkit-scrollbar-thumb {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
    background: linear-gradient(135deg, #5a6fd8 0%, #6a4190 100%);
}
</style>
