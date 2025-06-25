<template>
    <div class="detail-root fancy-bg">
        <transition name="fade-slide">
        <h2 class="mb-4 font-weight-bold page-title-glow">면접 상세 정보</h2>
        </transition>

        <!-- 면접 총점(평균) 상단 고정 -->
        <transition name="fade-slide">
        <div class="mb-4 d-flex flex-column align-center justify-center text-center score-glass-card">
            <span class="score-label text-h5 font-weight-bold">면접 총점(평균)</span>
            <span :class="['score-animated', getScoreColorClass(selectedInterview?.score), 'text-h5', 'font-weight-bold']">
                {{ formatScore(selectedInterview?.score) }}
            </span>
        </div>
        </transition>

        <v-alert v-if="loading" type="info">로딩 중...</v-alert>
        <v-alert v-else-if="!selectedInterview" type="warning">면접 정보가 없습니다. 면접을 배정해주세요.</v-alert>

        <!-- 상단 정보 카드 -->
        <v-row dense class="mb-4" align="stretch">
            <!-- 지원자 정보 -->
            <v-col cols="12" md="6">
                <transition name="fade-slide">
                <div class="glass-card applicant-card">
                    <div class="card-header">
                        <v-icon class="mr-2" color="primary">mdi-account-circle</v-icon>
                        <span class="font-weight-bold">지원자 정보</span>
                    </div>
                    <v-row>
                        <v-col cols="12" md="3" class="d-flex align-center justify-center">
                            <v-avatar size="80" class="avatar-glow">
                                <v-icon size="80">mdi-account</v-icon>
                            </v-avatar>
                        </v-col>
                        <v-col cols="12" md="9">
                            <div class="info-list">
                                <div class="info-item"><v-icon color="primary" size="18">mdi-account</v-icon> <span>이름:</span> <b>{{ selectedApplicant?.name }}</b></div>
                                <div class="info-item"><v-icon color="primary" size="18">mdi-phone</v-icon> <span>연락처:</span> {{ selectedApplicant?.phone }}</div>
                                <div class="info-item"><v-icon color="primary" size="18">mdi-email</v-icon> <span>이메일:</span> {{ selectedApplicant?.email }}</div>
                                <div class="info-item"><v-icon color="primary" size="18">mdi-map-marker</v-icon> <span>주소:</span> {{ selectedApplicant?.address }}</div>
                                <div class="info-item"><v-icon color="primary" size="18">mdi-cake-variant</v-icon> <span>생년월일:</span> {{ formatDate(selectedApplicant?.birth, 'date') }}</div>
                            </div>
                        </v-col>
                    </v-row>
                </div>
                </transition>
            </v-col>

            <!-- 면접 정보 -->
            <v-col cols="12" md="6">
                <transition name="fade-slide">
                <div class="glass-card interview-card">
                    <div class="card-header">
                        <v-icon class="mr-2" color="deep-purple">mdi-clipboard-text</v-icon>
                        <span class="font-weight-bold">면접 정보</span>
                    </div>
                    <div class="info-list">
                        <div class="info-item"><v-icon color="deep-purple" size="18">mdi-briefcase</v-icon> <span>채용 공고:</span> {{ selectedRecruitment?.recruitment.title || selectedRecruitment?.title || selectedRecruitment?.name || selectedRecruitment?.recruitmentTitle || '채용 공고 정보 없음' }}</div>
                        <div class="info-item"><v-icon color="deep-purple" size="18">mdi-file-document-edit</v-icon> <span>면접 평가표:</span> {{ selectedInterviewSheet?.name || '평가표 정보 없음' }}</div>
                        <div class="info-item"><v-icon color="deep-purple" size="18">mdi-calendar-clock</v-icon> <span>면접 일시:</span> {{ formatDate(selectedInterview?.datetime) }}</div>
                        <div class="info-item"><v-icon color="deep-purple" size="18">mdi-video</v-icon> <span>면접 줌 링크:</span>
                            <template v-if="isZoomUrl(selectedInterview?.address)">
                                <a :href="selectedInterview.address" target="_blank"
                                    class="text-primary text-decoration-underline">
                                    {{ selectedInterview.address }}
                                </a>
                            </template>
                            <template v-else>
                                {{ selectedInterview?.address }}
                            </template>
                        </div>
                    </div>
                    <div class="d-flex justify-end mt-4">
                        <v-btn color="primary" variant="outlined" @click="startEditing" class="edit-btn fancy-btn">
                            <v-icon left>mdi-pencil</v-icon> 면접 정보 수정
                        </v-btn>
                    </div>
                </div>
                </transition>
            </v-col>
        </v-row>

        <!-- 평가 기준 목록 -->
        <transition name="fade-slide">
        <v-card class="pa-4 mb-4 criteria-card glass-card" outlined>
            <v-card-title class="font-weight-bold"><v-icon class="mr-2" color="teal">mdi-format-list-bulleted</v-icon> 평가 기준 목록</v-card-title>
            <v-divider />
            <v-list>
                <v-list-item v-for="(criteria, index) in criteriaList" :key="criteria.id"
                    :title="`${index + 1}. ${criteria.title} (가중치 : ${criteria.weight * 100}%)`"
                    :subtitle="criteria.content" class="mb-2" />
            </v-list>
        </v-card>
        </transition>

        <!-- 면접관 평가 점수 카드 -->
        <transition name="fade-slide">
        <v-card class="pa-4 mb-6 score-card glass-card" outlined>
            <v-card-title class="d-flex justify-space-between align-center font-weight-bold">
                <span><v-icon class="mr-2" color="amber">mdi-account-group</v-icon>면접관 평가 보기</span>
                <span class="text-primary font-weight-bold">
                    합산 점수: <span :class="['score-animated', getScoreColorClass(currentInterviewerScore)]">{{ formatScore(currentInterviewerScore) }}</span>
                </span>
                <div>
                    <v-btn icon @click="prevInterviewer" :disabled="currentIndex === 0">
                        <v-icon>mdi-chevron-left</v-icon>
                    </v-btn>
                    <span class="mx-4">{{ currentInterviewerName }}</span>
                    <v-btn icon @click="nextInterviewer" :disabled="allScores.length === 0 || currentIndex === allScores.length - 1">
                        <v-icon>mdi-chevron-right</v-icon>
                    </v-btn>
                </div>
            </v-card-title>
            <v-divider />

            <v-container fluid class="pa-0">
                <template v-if="allScores.length === 0 || !hasAnyScore">
                    <div class="text-center py-8 text-grey font-weight-bold">입력된 평가 점수가 없습니다.</div>
                </template>
                <template v-else>
                    <v-row v-for="(item, index) in evaluationItems" :key="index" class="py-4">
                        <v-col cols="12">
                            <div class="d-flex justify-space-between align-center mb-1">
                                <div>
                                    <h4 class="text-subtitle-1 font-weight-bold">
                                        {{ index + 1 }}. {{ item.title }}
                                    </h4>
                                </div>
                                <span class="text-body-1 font-weight-bold score-animated" :class="getScoreColorClass(item.score)">
                                    {{ item.score }}/100
                                    <span class="text-caption grey--text ml-2">({{ item.weight }}%)</span>
                                </span>
                            </div>
                            <p class="mb-1 grey--text text--darken-1">{{ item.criteria }}</p>
                            <v-card class="pa-3 mt-2 sub-glass-card" outlined>
                                <p class="mb-0">{{ item.evaluation }}</p>
                            </v-card>
                        </v-col>
                        <v-divider v-if="index < evaluationItems.length - 1"></v-divider>
                    </v-row>

                    <!-- 면접관 총평 섹션 -->
                    <v-divider class="my-4"></v-divider>
                    <v-row class="py-4">
                        <v-col cols="12">
                            <div class="d-flex align-center mb-3">
                                <v-icon class="mr-2" color="primary">mdi-comment-text</v-icon>
                                <h4 class="text-subtitle-1 font-weight-bold mb-0">면접관 총평</h4>
                            </div>
                            <v-card class="pa-4 sub-glass-card" outlined style="background-color: #f8f9fa;">
                                <p class="mb-0 text-body-1" v-if="currentInterviewerReview">
                                    {{ currentInterviewerReview }}
                                </p>
                                <p class="mb-0 text-grey text-body-1" v-else>
                                    입력된 총평이 없습니다.
                                </p>
                            </v-card>
                        </v-col>
                    </v-row>
                </template>
            </v-container>
        </v-card>
        </transition>

        <!-- 하단 버튼: 평가 입력/뒤로 가기 -->
        <v-row class="mt-6 mb-2">
            <v-col cols="12" md="6" class="d-flex justify-start">
                <v-btn color="primary" class="action-btn fancy-btn" @click="goToInputInterviewScorePage">
                    <v-icon left>mdi-pencil-box-outline</v-icon> 평가 입력하기
                </v-btn>
            </v-col>
            <v-col cols="12" md="6" class="d-flex justify-end">
                <v-btn color="grey lighten-1" class="action-btn fancy-btn" @click="goToInterviewPage">
                    <v-icon left>mdi-arrow-left</v-icon> 뒤로 가기
                </v-btn>
            </v-col>
        </v-row>

        <!-- 면접 수정 다이얼로그 -->
        <v-dialog v-model="dialog" max-width="420px">
            <v-card class="edit-modal-card">
                <v-card-title class="font-weight-bold">면접 정보 수정</v-card-title>
                <v-card-text>
                    <v-date-picker v-model="editDatetime" locale="ko" color="primary" header-color="primary" class="mb-4 rounded-picker" />

                    <v-row class="mb-3">
                        <v-col cols="6">
                            <v-select
                                v-model="selectedHour"
                                :items="hourOptions"
                                label="시"
                                dense
                                variant="outlined"
                                class="rounded-select"
                            />
                        </v-col>
                        <v-col cols="6">
                            <v-select
                                v-model="selectedMinute"
                                :items="minuteOptions"
                                label="분"
                                dense
                                variant="outlined"
                                class="rounded-select"
                            />
                        </v-col>
                    </v-row>

                    <v-text-field
                        v-model="editAddress"
                        label="면접 장소"
                        placeholder="Zoom 링크 입력"
                        variant="outlined"
                        class="rounded-input"
                        dense
                    />
                </v-card-text>
                <v-card-actions class="justify-end">
                    <v-btn text class="modal-btn-cancel" @click="dialog = false">취소</v-btn>
                    <v-btn color="primary" class="modal-btn-confirm" @click="saveChanges">수정</v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>

        <v-btn v-if="!selectedInterview && !loading" color="primary" class="mt-4 fancy-btn">
            면접 배정하기
        </v-btn>
    </div>
</template>


<script setup>
import { ref, onMounted, computed } from 'vue';
import dayjs from 'dayjs';
import 'dayjs/locale/ko';
dayjs.locale('ko');

import { useInterviewStore } from '@/stores/interviewStore';
import { useInterviewCriteriaStore } from '@/stores/interviewCriteriaStore';
import { useApplicantStore } from '@/stores/applicantStore';
import { useApplicationStore } from '@/stores/applicationStore';
import { useInterviewerStore } from '@/stores/interviewerStore';
import { useInterviewSheetStore } from '@/stores/interviewSheetStore';
import { useInterviewScoreStore } from '@/stores/interviewScoreStore';
import { useMemberStore } from '@/stores/memberStore';
import { useRecruitmentStore } from '@/stores/recruitmentStore';
import { useRouter, useRoute } from 'vue-router'; // useRoute 추가

const interviewStore = useInterviewStore();
const criteriaStore = useInterviewCriteriaStore();
const applicantStore = useApplicantStore();
const applicationStore = useApplicationStore();
const interviewSheetStore = useInterviewSheetStore();
const interviewerStore = useInterviewerStore();
const interviewScoreStore = useInterviewScoreStore();
const memberStore = useMemberStore();
const recruitmentStore = useRecruitmentStore();

const router = useRouter(); // 페이지 이동용
const route = useRoute();   // 현재 라우트 정보용

const applicationId = route.params.applicationId;
const selectedApplication = ref(null)
const selectedApplicant = ref(null)
const selectedInterview = ref(null)
const selectedInterviewSheet = ref(null)
const selectedInterviewer = ref(null)
const InterviewerList = ref([])
const selectedCriteria = ref(null)
const criteriaList = ref([])
const selectedScore = ref(null)
const scoreList = ref([])
const selectedRecruitment = ref(null)


const currentInterviewer = computed(() => allScores.value[currentIndex.value])
console.log('currentInterviewer', currentInterviewer);

const loading = computed(() => interviewStore.loading || criteriaStore.loading);
const error = computed(() => interviewStore.error || criteriaStore.error);

const goToInputInterviewScorePage = () => {
    const interviewId = selectedInterview.value?.id;
    if (!interviewId) {
        alert('면접 정보가 없습니다.');
        return;
    }

    router.push({ name: 'InputInterviewScorePage', params: { interviewId } });
};

const isZoomUrl = (url) => {
    return typeof url === 'string' && url.startsWith('http');
};

const fetchAll = async () => {
    try {
        await applicationStore.fetchApplicationById(applicationId)
        selectedApplication.value = applicationStore.selectedApplication;
        console.log('selectedApplication : ', selectedApplication)

        const applicantId = selectedApplication.value.applicantId
        console.log('applicantId : ', applicantId)

        await applicantStore.fetchApplicantById(applicantId)
        selectedApplicant.value = applicantStore.selectedApplicant
        console.log('selectedApplicant', selectedApplicant)

        await interviewStore.fetchInterviewByApplicationId(applicationId)
        selectedInterview.value = interviewStore.selectedInterview
        console.log('selectedInterview : ', selectedInterview)

        const interviewSheetId = selectedInterview.value.sheetId
        console.log('interviewSheetId : ', interviewSheetId)

        await interviewSheetStore.fetchSheetById(interviewSheetId)
        selectedInterviewSheet.value = interviewSheetStore.selectedSheet
        console.log('selectedInterviewSheet', selectedInterviewSheet)

        const sheetId = selectedInterviewSheet.value.id
        await criteriaStore.fetchCriteriaBySheetId(sheetId)
        criteriaList.value = criteriaStore.criteriaList
        console.log('criteriaList : ', criteriaList)

        // 채용 공고 정보 가져오기
        const recruitmentId = selectedApplication.value.recruitmentId
        console.log('recruitmentId : ', recruitmentId)
        if (recruitmentId) {
            await recruitmentStore.loadRecruitmentDetail(recruitmentId)
            selectedRecruitment.value = recruitmentStore.detail
            console.log('recruitment detail : ', recruitmentStore.detail)
        }

        await interviewerStore.fetchInterviewersByInterviewId(selectedInterview.value.id)
        InterviewerList.value = interviewerStore.interviewerList

        // 면접관별 점수 가져오기
        const scorePromises = InterviewerList.value.map(async (interviewer) => {
            await interviewScoreStore.fetchScoresByInterviewerId(interviewer.id)
            return {
                interviewerId: interviewer.id,
                memberId: interviewer.memberId,
                name: interviewer.name,
                review: interviewer.review,
                scores: [...interviewScoreStore.scoreList]
            }
        })
        allScores.value = await Promise.all(scorePromises)

    } catch (err) {
        console.warn('지원자 정보 없음 or 에러 발생', err);
    }
};

const goToInterviewPage = () => {
    router.push('/employment/interviews')
}

const formatDate = (dateStr, type = 'datetime') => {
    if (!dateStr) return '';
    const d = new Date(dateStr);
    return type === 'date'
        ? d.toLocaleDateString()      // YYYY-MM-DD
        : d.toLocaleString();         // YYYY-MM-DD HH:mm
};

onMounted(async () => {
    await fetchAll(); // 내부에서 interview와 applicant 모두 처리
});

const editDatetime = ref(null);
const editAddress = ref('');
const dialog = ref(false);

const selectedHour = ref('09');   // 초기값 예시
const selectedMinute = ref('00');

const hourOptions = Array.from({ length: 24 }, (_, i) => String(i).padStart(2, '0'));
const minuteOptions = ['00', '10', '20', '30', '40', '50'];

const startEditing = () => {
    if (!selectedInterview.value) return;

    const original = new Date(selectedInterview.value.datetime);
    editDatetime.value = original.toISOString().substr(0, 10); // YYYY-MM-DD

    selectedHour.value = String(original.getHours()).padStart(2, '0');
    selectedMinute.value = String(Math.floor(original.getMinutes() / 10) * 10).padStart(2, '0');

    editAddress.value = selectedInterview.value.address;

    dialog.value = true;
};

const formatDateLocal = (date) => {
    const d = new Date(date);
    const year = d.getFullYear();
    const month = String(d.getMonth() + 1).padStart(2, '0'); // getMonth()는 0부터 시작
    const day = String(d.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
};

const saveChanges = async () => {
    if (!confirm('면접 정보를 수정하시겠습니까?')) return;

    try {
        const datePart = formatDateLocal(editDatetime.value); // ✅ 로컬 기준 날짜
        const datetimeStr = `${datePart}T${selectedHour.value}:${selectedMinute.value}:00`;

        console.log('[debug] 최종 datetimeStr:', datetimeStr);

        await interviewStore.updateInterviewDatetime(selectedInterview.value.id, datetimeStr);
        await interviewStore.updateInterviewAddress(selectedInterview.value.id, editAddress.value);

        await interviewStore.fetchInterviewById(selectedInterview.value.id);

        alert('면접 정보가 성공적으로 수정되었습니다.');
        dialog.value = false;
    } catch (err) {
        console.error('면접 정보 수정 실패:', err);
    }
};

const allScores = ref([]) // [{interviewerId: 1, scores: [...]}, ...]
const currentIndex = ref(0)

const currentScoreData = computed(() => allScores.value[currentIndex.value])
const evaluationItems = computed(() => {
    return criteriaList.value.map(criteria => {
        const matched = currentScoreData.value?.scores.find(score => score.criteriaId === criteria.id)
        return {
            title: criteria.title,
            weight: criteria.weight * 100,
            criteria: criteria.content,
            score: matched?.score ?? 0,
            evaluation: matched?.review ?? '평가 없음'
        }
    })
})

const prevInterviewer = () => {
    if (currentIndex.value > 0) currentIndex.value--
}
const nextInterviewer = () => {
    if (currentIndex.value < allScores.value.length - 1) currentIndex.value++
}

// 평가 점수 중 하나라도 입력된 게 있는지 체크
const hasAnyScore = computed(() => {
    if (!allScores.value.length) return false;
    // 현재 면접관의 점수 데이터
    const scoreData = allScores.value[currentIndex.value]?.scores || [];
    // 점수/리뷰가 하나라도 있으면 true
    return scoreData.some(s => s.score > 0 || (s.review && s.review !== '평가 없음'));
});

// 면접관별 합산 점수(화살표 이동 시 바뀜)
const currentInterviewerScore = computed(() => {
    const current = allScores.value[currentIndex.value]
    if (!current) return '-'
    // interviewerId 또는 memberId로 interviewerList에서 score 찾기
    const found = interviewerStore.interviewerList.find(
        i => i.id === current.interviewerId || i.memberId === current.memberId
    )
    return found?.score ?? '-'
})

// 현재 면접관의 총평
const currentInterviewerReview = computed(() => {
    const current = allScores.value[currentIndex.value]
    return current?.review ?? null;
})

const currentInterviewerName = computed(() => {
    const current = allScores.value[currentIndex.value]
    return current?.name ?? '면접관 이름 없음';
})

const formatScore = (score) => {
    if (typeof score === 'number') {
        return score.toFixed(1);
    }
    return '-';
};

// 점수 색상 및 애니메이션 클래스 반환
function getScoreColorClass(score) {
    if (score === null || score === undefined || isNaN(score)) return 'score-gray';
    if (score >= 90) return 'score-blue';
    if (score >= 80) return 'score-green';
    if (score >= 70) return 'score-yellow';
    if (score >= 60) return 'score-orange';
    return 'score-red';
}

</script>

<style scoped>
.fancy-bg {
    min-height: 100vh;
    background: linear-gradient(135deg, #e3f0ff 0%, #fbeaff 100%);
    animation: bg-move 12s linear infinite alternate;
}
@keyframes bg-move {
    0% { background-position: 0% 50%; }
    100% { background-position: 100% 50%; }
}
.page-title-glow {
    font-size: 2.3rem;
    letter-spacing: 0.01em;
    text-align: center;
    background: linear-gradient(90deg, #42a5f5 30%, #ab47bc 100%);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    filter: drop-shadow(0 0 16px #ab47bc66);
    animation: fadein 1.2s;
}

.glass-card {
    background: rgba(255,255,255,0.85);
    border-radius: 28px;
    box-shadow: 0 8px 32px 0 rgba(80,120,200,0.18);
    backdrop-filter: blur(8px);
    border: 1.5px solid #e3e3f3;
    transition: box-shadow 0.2s, transform 0.2s;
    margin-bottom: 18px;
    padding: 1.5rem 1.2rem;
    position: relative;
    overflow: hidden;
}
.glass-card:hover {
    box-shadow: 0 12px 36px 0 #ab47bc33;
    transform: scale(1.015);
}
.sub-glass-card {
    background: rgba(255,255,255,0.7);
    border-radius: 18px;
    border: 1px solid #e0e0e0;
    box-shadow: 0 2px 8px #e0e0e022;
}
.score-glass-card {
    background: rgba(255,255,255,0.7);
    border-radius: 22px;
    box-shadow: 0 4px 18px #42a5f522;
    padding: 1.2rem 2.2rem;
    display: inline-block;
    margin: 0 auto 2rem auto;
    animation: fadein 1.2s;
}
.avatar-glow {
    box-shadow: 0 0 24px #42a5f5aa, 0 0 0 4px #fff;
    border-radius: 50%;
    background: #fff;
    animation: avatar-pop 1.2s;
}
@keyframes avatar-pop {
    0% { transform: scale(0.7); opacity: 0; }
    100% { transform: scale(1); opacity: 1; }
}
.card-header {
    display: flex;
    align-items: center;
    font-size: 1.2rem;
    margin-bottom: 1.2rem;
    letter-spacing: 0.01em;
}
.info-list {
    margin-top: 0.5rem;
}
.info-item {
    display: flex;
    align-items: center;
    font-size: 1.08rem;
    margin-bottom: 0.3rem;
    gap: 0.5rem;
}
.score-label {
    font-size: 1.1rem;
    color: #888;
    margin-right: 0.7rem;
}
.score-animated {
    font-size: 2.1rem;
    font-weight: 700;
    margin-left: 0.5rem;
    margin-right: 0.5rem;
    letter-spacing: 0.01em;
    transition: color 0.3s, transform 0.3s;
    animation: popscore 1.1s;
}
@keyframes popscore {
    0% { transform: scale(0.7); opacity: 0; }
    100% { transform: scale(1); opacity: 1; }
}
.score-blue {
    color: #1976d2;
}
.score-green {
    color: #43a047;
}
.score-yellow {
    color: #fbc02d;
}
.score-orange {
    color: #fb8c00;
}
.score-red {
    color: #e53935;
}
.score-gray {
    color: #aaa;
}
.fancy-btn {
    border-radius: 18px;
    font-weight: 600;
    font-size: 1.1rem;
    box-shadow: 0 2px 12px #42a5f522;
    background: linear-gradient(90deg, #42a5f5 30%, #ab47bc 100%);
    color: #fff;
    transition: background 0.18s, box-shadow 0.18s, transform 0.18s;
}
.fancy-btn:hover {
    background: linear-gradient(90deg, #ab47bc 30%, #42a5f5 100%);
    color: #fff;
    box-shadow: 0 4px 24px #ab47bc33;
    transform: scale(1.05);
}
.fade-slide-enter-active, .fade-slide-leave-active {
    transition: all 0.7s cubic-bezier(.4,2,.6,1);
}
.fade-slide-enter-from, .fade-slide-leave-to {
    opacity: 0;
    transform: translateY(30px);
}
.applicant-card, .interview-card {
    max-width: 720px;
    min-width: 0;
    width: 95%;
    margin-left: auto;
    margin-right: auto;
}
.edit-btn.fancy-btn {
    background: linear-gradient(90deg, #7c4dff 30%, #b388ff 100%) !important;
    color: #fff !important;
    border: none !important;
}
.edit-btn.fancy-btn:hover {
    background: linear-gradient(90deg, #b388ff 30%, #7c4dff 100%) !important;
    color: #fff !important;
    box-shadow: 0 4px 24px #7c4dff33;
}
</style>
