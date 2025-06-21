<template>
    <div class="detail-root">
        <h2 class="mb-4 font-weight-bold">면접 상세 정보</h2>

        <!-- 면접 총점(평균) 상단 고정 -->
        <div class="mb-4 text-h5 font-weight-bold text-center">
            면접 총점(평균): {{ selectedInterview?.score ?? '-' }}
        </div>

        <v-alert v-if="loading" type="info">로딩 중...</v-alert>
        <v-alert v-else-if="!selectedInterview" type="warning">면접 정보가 없습니다. 면접을 배정해주세요.</v-alert>

        <!-- 상단 정보 카드 -->
        <v-row dense class="mb-4" align="stretch">
            <!-- 지원자 정보 -->
            <v-col cols="12" md="6">
                <v-card class="pa-4 mb-2 info-card h-100 d-flex flex-column" elevation="2" rounded="lg">
                    <h3 class="font-weight-bold mb-3">지원자 정보</h3>
                    <v-row>
                        <v-col cols="12" md="3">
                            <!-- <v-img :src="selectedApplicant.profileUrl" aspect-ratio="1" class="rounded" contain /> -->
                        </v-col>
                        <v-col cols="12" md="9">
                            <p><strong>이름:</strong> {{ selectedApplicant?.name }}</p>
                            <p><strong>연락처:</strong> {{ selectedApplicant?.phone }}</p>
                            <p><strong>이메일:</strong> {{ selectedApplicant?.email }}</p>
                            <p><strong>주소:</strong> {{ selectedApplicant?.address }}</p>
                            <p><strong>생년월일:</strong> {{ formatDate(selectedApplicant?.birth, 'date') }}</p>
                        </v-col>
                    </v-row>
                </v-card>
            </v-col>

            <!-- 면접 정보 -->
            <v-col cols="12" md="6">
                <v-card class="pa-4 mb-2 info-card h-100 d-flex flex-column" elevation="2" rounded="lg">
                    <v-card-title class="font-weight-bold mb-2">면접 정보</v-card-title>
                    <v-card-text>
                        <div><strong>면접 ID:</strong> {{ selectedInterview?.id }}</div>
                        <div><strong>지원서 ID:</strong> {{ selectedInterview?.applicationId }}</div>
                        <div><strong>평가표 ID:</strong> {{ selectedInterview?.sheetId }}</div>
                        <div><strong>면접 일시:</strong> {{ formatDate(selectedInterview?.datetime) }}</div>
                        <div><strong>면접 줌 링크:</strong>
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
                        <div><strong>점수:</strong> {{ selectedInterview?.score }}</div>
                    </v-card-text>
                    <v-card-actions class="justify-end mt-auto">
                        <v-btn color="primary" variant="outlined" @click="startEditing" class="edit-btn">
                            <v-icon left>mdi-pencil</v-icon> 면접 정보 수정
                        </v-btn>
                    </v-card-actions>
                </v-card>
            </v-col>
        </v-row>

        <!-- 평가 기준 목록 -->
        <v-card class="pa-4 mb-4 criteria-card" outlined>
            <v-card-title class="font-weight-bold">평가 기준 목록</v-card-title>
            <v-divider />
            <v-list>
                <v-list-item v-for="(criteria, index) in criteriaList" :key="criteria.id"
                    :title="`${index + 1}. ${criteria.title} (가중치 : ${criteria.weight * 100}%)`"
                    :subtitle="criteria.content" class="mb-2" />
            </v-list>
        </v-card>

        <!-- 면접관 평가 점수 카드 -->
        <v-card class="pa-4 mb-6 score-card" outlined>
            <v-card-title class="d-flex justify-space-between align-center font-weight-bold">
                <span>면접관 평가 보기</span>
                <span class="text-primary font-weight-bold">
                    합산 점수: {{ currentInterviewerScore }}
                </span>
                <div>
                    <v-btn icon @click="prevInterviewer" :disabled="currentIndex === 0">
                        <v-icon>mdi-chevron-left</v-icon>
                    </v-btn>
                    <span class="mx-4">익명 {{ currentIndex + 1 }}</span>
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
                                <span class="text-body-1 font-weight-bold">
                                    {{ item.score }}/100
                                    <span class="text-caption grey--text ml-2">({{ item.weight }}%)</span>
                                </span>
                            </div>
                            <p class="mb-1 grey--text text--darken-1">{{ item.criteria }}</p>
                            <v-card class="pa-3 mt-2" outlined>
                                <p class="mb-0">{{ item.evaluation }}</p>
                            </v-card>
                        </v-col>
                        <v-divider v-if="index < evaluationItems.length - 1"></v-divider>
                    </v-row>
                </template>
            </v-container>
        </v-card>

        <!-- 하단 버튼: 평가 입력/뒤로 가기 -->
        <v-row class="mt-6 mb-2">
            <v-col cols="12" md="6" class="d-flex justify-start">
                <v-btn color="primary" class="action-btn" @click="goToInputInterviewScorePage">
                    <v-icon left>mdi-pencil-box-outline</v-icon> 평가 입력하기
                </v-btn>
            </v-col>
            <v-col cols="12" md="6" class="d-flex justify-end">
                <v-btn color="grey lighten-1" class="action-btn" @click="goToInterviewPage">
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

        <v-btn v-if="!selectedInterview && !loading" color="primary" class="mt-4">
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
import { useRouter, useRoute } from 'vue-router'; // useRoute 추가

const interviewStore = useInterviewStore();
const criteriaStore = useInterviewCriteriaStore();
const applicantStore = useApplicantStore();
const applicationStore = useApplicationStore();
const interviewSheetStore = useInterviewSheetStore();
const interviewerStore = useInterviewerStore();
const interviewScoreStore = useInterviewScoreStore();
const memberStore = useMemberStore();

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

        await interviewerStore.fetchInterviewersByInterviewId(selectedInterview.value.id)
        InterviewerList.value = interviewerStore.interviewerList

        // 면접관별 점수 가져오기
        const scorePromises = InterviewerList.value.map(async (interviewer) => {
            await interviewScoreStore.fetchScoresByInterviewerId(interviewer.id)
            await memberStore.fetchM
            return {
                interviewerId: interviewer.id,
                name: interviewer.name,
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

</script>

<style scoped>
.detail-root {
    max-width: 1100px;
    margin: 0 auto;
    padding: 32px 0 64px 0;
}
.info-card {
    background: #f8fafc;
    border-radius: 18px;
    border: 1.5px solid #e0e0e0;
    min-height: 260px;
    display: flex;
    flex-direction: column;
}
.criteria-card {
    background: #f6f8f7;
    border-radius: 18px;
    border: 1.5px solid #e0e0e0;
}
.score-card {
    background: #f6f8f7;
    border-radius: 18px;
    border: 1.5px solid #e0e0e0;
    min-height: 220px;
}
.text-grey {
    color: #888 !important;
}
.action-btn {
    min-width: 160px;
    border-radius: 24px;
    font-weight: bold;
    font-size: 1.05rem;
    letter-spacing: -0.5px;
    box-shadow: 0 2px 8px #e0e0e033;
}
.edit-btn {
    min-width: 140px;
    border-radius: 20px;
    font-weight: 500;
    font-size: 0.98rem;
}
/* 모달 관련 스타일 */
.edit-modal-card {
    padding: 8px 0 16px 0;
    border-radius: 18px;
}
.rounded-picker {
    border-radius: 16px;
    overflow: hidden;
    box-shadow: 0 2px 8px #e0e0e022;
}
.rounded-select {
    border-radius: 16px !important;
}
.rounded-input {
    border-radius: 16px !important;
}
.modal-btn-cancel {
    min-width: 80px;
    font-weight: 500;
    color: #888 !important;
}
.modal-btn-confirm {
    min-width: 80px;
    font-weight: bold;
    border-radius: 20px;
}
</style>
