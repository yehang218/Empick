<template>
    <v-container fluid class="d-flex interview-schedule-root">
        <!-- 왼쪽: 채용공고 목록 + 달력 (더 크게) -->
        <div class="calendar-side">
            <!-- 채용공고 체크박스 표 -->
            <v-card class="mb-4 pa-4" elevation="2">
                <h4 class="font-weight-bold mb-3">채용공고별 필터</h4>
                <v-simple-table dense>
                    <tbody>
                        <tr v-for="recruitment in recruitmentList" :key="recruitment.id">
                            <td>
                                <v-checkbox
                                    v-model="selectedRecruitmentIds"
                                    :value="recruitment.id"
                                    hide-details
                                    dense
                                    class="ma-0 pa-0"
                                />
                            </td>
                            <td>{{ recruitment.title }}</td>
                        </tr>
                    </tbody>
                </v-simple-table>
            </v-card>
            <!-- 달력 -->
            <div class="calendar-wrapper">
                <InterviewScheduleCalendar
                    @date-selected="onDateSelected"
                    class="big-calendar"
                    :custom-days="calendarDayMap"
                />
            </div>
        </div>

        <!-- 오른쪽 일정 목록 (더 좁게, 카드 스타일) -->
        <v-sheet class="schedule-list-side pa-4" elevation="2">
            <h3 class="text-h6 font-weight-bold mb-4">{{ selectedDate }} 면접 일정</h3>
            <v-btn color="primary" class="mb-4" @click="goToCreateInterview">면접 등록하기</v-btn>

            <v-alert v-if="interviews.length === 0" type="info" border="start" colored-border>
                등록된 면접 일정이 없습니다.
            </v-alert>

            <div v-else>
                <div v-for="(item, index) in interviews" :key="index" class="interview-card mb-4" @click="goToInterviewDetail(item.applicationId)">
                    <div class="d-flex align-center mb-2">
                        <v-icon color="primary" class="mr-2">mdi-account</v-icon>
                        <span class="font-weight-bold interview-name">{{ item.applicantName }}</span>
                    </div>
                    <div class="mb-1">
                        <v-icon size="18" class="mr-1" color="grey">mdi-clock-outline</v-icon>
                        <span class="interview-time">{{ formatTime(item.datetime) }}</span>
                    </div>
                    <div class="mb-1">
                        <v-icon size="18" class="mr-1" color="grey">mdi-phone</v-icon>
                        <span>{{ item.phone || '연락처 없음' }}</span>
                    </div>
                    <div class="mb-1">
                        <v-icon size="18" class="mr-1" color="grey">mdi-email</v-icon>
                        <span>{{ item.email || '이메일 없음' }}</span>
                    </div>
                    <div class="mt-2 d-flex align-center">
                        <v-icon size="18" class="mr-1" color="grey">mdi-star-circle</v-icon>
                        <span :class="scoreColorClass(item.interviewScore)">
                            {{ scoreText(item.interviewScore) }}
                        </span>
                    </div>
                </div>
            </div>
        </v-sheet>
    </v-container>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import InterviewScheduleCalendar from '@/components/common/InterviewScheduleCalendar.vue'
import { useInterviewStore } from '@/stores/interviewStore'
import { useApplicationStore } from '@/stores/applicationStore'
import { useApplicantStore } from '@/stores/applicantStore'
import { useRecruitmentStore } from '@/stores/recruitmentStore'
import { useRouter } from 'vue-router'
import { useToast } from 'vue-toastification'
const router = useRouter()
const toast = useToast()


const interviewStore = useInterviewStore()
const applicationStore = useApplicationStore()
const applicantStore = useApplicantStore()
const recruitmentStore = useRecruitmentStore()

const selectedInterview = ref(null)
const selectedApplication = ref(null)
const selectedApplicant = ref(null)
const selectedDate = ref('')
const interviews = ref([])

// 채용공고 목록 및 체크박스 상태
const recruitmentList = ref([])
const selectedRecruitmentIds = ref([])

// applicationId → application → recruitmentId 매핑용
const applicationMap = ref({}) // { applicationId: { ...application, recruitmentId, ... } }

// 달력에 표시할 날짜별 면접 정보 (리본용)
const calendarDayMap = ref({}) // { 'YYYY-MM-DD': [ { applicantName, recruitmentTitle, ... } ] }

// 채용공고 목록 불러오기
onMounted(async () => {
    await recruitmentStore.loadRecruitmentList()
    recruitmentList.value = recruitmentStore.list
    // 기본 전체 선택
    selectedRecruitmentIds.value = recruitmentList.value.map(r => r.id)

    // 모든 application 미리 로딩 (면접-application-recruitment 매핑용)
    await applicationStore.fetchAllApplications()
    applicationStore.applicationList.forEach(app => {
        applicationMap.value[app.id] = app
    })

    // 달력에 표시할 면접 일정 미리 세팅
    await loadCalendarDayMap()
})

// 달력에 표시할 면접 일정(리본) 세팅
const loadCalendarDayMap = async () => {
    // 모든 면접 일정 불러오기
    await interviewStore.fetchAllInterviews() // 전체 인터뷰 조회
    const allInterviews = interviewStore.interviewList
    const map = {}
    for (const interview of allInterviews) {
        const app = applicationMap.value[interview.applicationId]
        if (!app) continue
        // 필터링: 체크된 채용공고만
        if (!selectedRecruitmentIds.value.includes(app.recruitmentId)) continue
        const dateStr = interview.datetime?.slice(0, 10)
        if (!dateStr) continue
        if (!map[dateStr]) map[dateStr] = []
        map[dateStr].push({
            applicantName: app.name,
            recruitmentTitle: recruitmentList.value.find(r => r.id === app.recruitmentId)?.title || '',
            interviewId: interview.id,
            applicationId: interview.applicationId
        })
    }
    calendarDayMap.value = map
}

// 채용공고 체크박스 변경 시 달력/오른쪽 목록 리렌더
watch(selectedRecruitmentIds, () => {
    loadCalendarDayMap();
    if (selectedDate.value) {
        onDateSelected(selectedDate.value);
    }
});

const goToInterviewDetail = (applicationId) => {
    router.push(`/employment/interviews/detail/${applicationId}`)
}

const goToCreateInterview = () => {
    if (!selectedDate.value) {
        toast.error('날짜를 먼저 선택해주세요!')
        return
    }
    router.push({
        name: 'CreateInterviewPage',
        query: { date: selectedDate.value }
    })
}

const onDateSelected = async (date) => {
    selectedDate.value = date
    await interviewStore.fetchInterviewsByDate(date)
    const rawInterviews = interviewStore.interviewList
    interviews.value.splice(0)
    for (const interview of rawInterviews) {
        try {
            const applicationId = interview.applicationId
            const app = applicationMap.value[applicationId]
            if (!app) continue
            // 필터링: 체크된 채용공고만
            if (!selectedRecruitmentIds.value.includes(app.recruitmentId)) continue
            await applicantStore.fetchApplicantById(app.applicantId)
            const applicant = applicantStore.selectedApplicant
            let applicantName = applicant ? applicant.name : '이름 없음'
            // 면접 총점(score) 등 정보 추가
            if (!interviews.value.some(i => i.applicationId === interview.applicationId)) {
                interviews.value.push({
                    ...interview,
                    applicantName,
                    phone: applicant?.phone,
                    email: applicant?.email,
                    interviewScore: interview.score // interview.score가 면접 총점
                })
            }
        } catch (err) {
            console.error('Error fetching applicant name:', err)
            if (!interviews.value.some(i => i.applicationId === interview.applicationId)) {
                interviews.value.push({
                    ...interview,
                    applicantName: '오류',
                    phone: '',
                    email: '',
                    interviewScore: null
                })
            }
        }
    }
}

const formatTime = (datetimeStr) => {
    if (!datetimeStr) return '시간 없음';
    const date = new Date(datetimeStr);
    if (isNaN(date)) return '시간 없음';
    const hour = date.getHours().toString().padStart(2, '0');
    const minute = date.getMinutes().toString().padStart(2, '0');
    return `${hour}시 ${minute}분`;
};

// 점수 색상 클래스
const scoreColorClass = (score) => {
    if (score === null || score === undefined || score === '' || isNaN(score)) return 'score-not-evaluated';
    if (score >= 80) return 'score-high';
    if (score >= 60) return 'score-mid';
    return 'score-low';
}
// 점수 텍스트
const scoreText = (score) => {
    if (score === null || score === undefined || score === '' || isNaN(score)) return '평가 전';
    return `${score}점`;
}

// 날짜를 YYYY-MM-DD로 변환하는 함수
const dateString = (date) => {
    if (!date) return '';
    const d = new Date(date);
    const year = d.getFullYear();
    const month = String(d.getMonth() + 1).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
}

// 오늘 날짜인지
const isToday = (date) => {
    const now = new Date();
    return (
        date.getFullYear() === now.getFullYear() &&
        date.getMonth() === now.getMonth() &&
        date.getDate() === now.getDate()
    );
};

// 주말인지
const isWeekend = (date) => {
    const day = date.getDay();
    return day === 0 || day === 6;
};
</script>

<style scoped>
.interview-schedule-root {
    min-height: 90vh;
    background: linear-gradient(135deg, #e8f5e9 0%, #f1f8f4 100%);
}
.calendar-side {
    width: 68%;
    min-width: 340px;
    max-width: 900px;
    padding-right: 2.5rem;
    display: flex;
    flex-direction: column;
}
.calendar-wrapper {
    background: #fff;
    border-radius: 32px;
    box-shadow: 0 4px 32px #81c78433;
    padding: 2.5rem 2.5rem 2rem 2.5rem;
    margin-bottom: 1.5rem;
    min-height: 520px;
    display: flex;
    align-items: center;
    justify-content: center;
}
.big-calendar {
    width: 100%;
    min-width: 320px;
    font-size: 1.25rem;
}
.schedule-list-side {
    width: 32%;
    min-width: 260px;
    max-width: 420px;
    background: #f8fafc;
    border-radius: 24px;
    box-shadow: 0 2px 12px #388e3c22;
    margin-left: 0.5rem;
    overflow-y: auto;
}
.interview-card {
    background: #fff;
    border-radius: 18px;
    box-shadow: 0 2px 8px #81c78422;
    padding: 1.2rem 1.5rem 1.2rem 1.2rem;
    cursor: pointer;
    transition: box-shadow 0.18s, transform 0.18s;
    border: 1.5px solid #a5d6a7;
}
.interview-card:hover {
    box-shadow: 0 4px 18px #388e3c33;
    transform: scale(1.03);
}
.interview-name {
    font-size: 1.15rem;
    color: #204d28;
}
.interview-time {
    font-weight: 500;
    color: #388e3c;
}
.score-high {
    color: #43a047;
    font-weight: bold;
    font-size: 1.1rem;
}
.score-mid {
    color: #ff9800;
    font-weight: bold;
    font-size: 1.1rem;
}
.score-low {
    color: #e53935;
    font-weight: bold;
    font-size: 1.1rem;
}
.score-not-evaluated {
    color: #bdbdbd;
    font-weight: 500;
    font-size: 1.1rem;
}
@media (max-width: 900px) {
    .interview-schedule-root {
        flex-direction: column;
    }
    .calendar-side {
        width: 100%;
        padding-right: 0;
        max-width: 100vw;
    }
    .calendar-wrapper {
        padding: 1rem 0.5rem;
        min-height: 320px;
    }
    .schedule-list-side {
        width: 100%;
        margin-left: 0;
        margin-top: 1.5rem;
        min-width: 0;
        max-width: 100vw;
    }
}
</style>
