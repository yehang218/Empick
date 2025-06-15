<!-- InterviewDetailPage.vue -->
<template>
    <div>
        <h2>면접 상세 정보</h2>

        <v-alert v-if="loading" type="info">로딩 중...</v-alert>
        <v-alert v-else-if="!interview" type="warning">면접 정보가 없습니다. 면접을 배정해주세요.</v-alert>
        <v-card v-if="applicant && applicant.name" class="pa-4" elevation="1">
            <h3>지원자 정보</h3>
            <v-row>
                <v-col cols="12" md="3">
                    <v-img :src="applicant.profileUrl" aspect-ratio="1" class="rounded" contain />
                </v-col>
                <v-col cols="12" md="9">
                    <p><strong>이름:</strong> {{ applicant.name }}</p>
                    <p><strong>연락처:</strong> {{ applicant.phone }}</p>
                    <p><strong>이메일:</strong> {{ applicant.email }}</p>
                    <p><strong>주소:</strong> {{ applicant.address }}</p>
                    <p><strong>생년월일:</strong> {{ formatDate(applicant.birth, 'date') }}</p>
                </v-col>
            </v-row>
        </v-card>
        <v-card v-if="interview" class="pa-4 mt-4" outlined>
            <v-card-title>
                면접 ID: {{ interview.id }}
                <v-spacer />
                <v-btn icon @click="startEditing">
                    <v-icon>mdi-pencil</v-icon>
                </v-btn>
            </v-card-title>
            <v-card-text>
                <div><strong>지원서 ID:</strong> {{ interview.applicationId }}</div>
                <div><strong>평가표 ID:</strong> {{ interview.sheetId }}</div>
                <div><strong>면접 일시:</strong> {{ formatDate(interview.datetime) }}</div>
                <div><strong>면접 장소:</strong> {{ interview.address }}</div>
                <div><strong>점수:</strong> {{ interview.score }}</div>
            </v-card-text>
        </v-card>

        <v-dialog v-model="dialog" max-width="500px">
            <v-card>
                <v-card-title>면접 정보 수정</v-card-title>
                <v-card-text>
                    <v-date-picker v-model="editDatetime" label="날짜 선택" />

                    <v-row>
                        <v-col cols="6">
                            <v-select v-model="selectedHour" :items="hourOptions" label="시간" dense />
                        </v-col>
                        <v-col cols="6">
                            <v-select v-model="selectedMinute" :items="minuteOptions" label="분" dense />
                        </v-col>
                    </v-row>

                    <v-text-field v-model="editAddress" label="면접 장소" />
                </v-card-text>
                <v-card-actions>
                    <v-spacer />
                    <v-btn text @click="dialog = false">취소</v-btn>
                    <v-btn color="primary" @click="saveChanges">수정</v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>

        <v-btn v-if="!interview && !loading" color="primary" class="mt-4" @click="assignInterview">
            면접 배정하기
        </v-btn>
        <v-btn color="primary" class="mt-4" @click="goToInterviewPage">
            뒤로 가기
        </v-btn>
    </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';

import { useInterviewStore } from '@/stores/interviewStore';
import { useInterviewCriteriaStore } from '@/stores/interviewCriteriaStore';
import { useApplicantStore } from '@/stores/applicantStore';
import { useApplicationStore } from '@/stores/applicationStore';
import { useRouter, useRoute } from 'vue-router'; // useRoute 추가

const interviewStore = useInterviewStore();
const criteriaStore = useInterviewCriteriaStore();
const applicantStore = useApplicantStore();
const applicationStore = useApplicationStore();

const interview = computed(() => interviewStore.selectedInterview);
const selectedCriteria = computed(() => criteriaStore.selectedCriteria);

const router = useRouter(); // 페이지 이동용
const route = useRoute();   // 현재 라우트 정보용

const applicationId = route.params.applicationId; // ✅ 이렇게 해야 params 접근 가능

const applicant = ref(null);
const criteriaList = ref([]);
const loading = computed(() => interviewStore.loading || criteriaStore.loading);
const error = computed(() => interviewStore.error || criteriaStore.error);

const fetchApplicant = async () => {
    try {
        await interviewStore.fetchInterviewByApplicationId(applicationId);
        const interviewResult = interview.value;
        console.log('💬 interviewResult:', interviewResult);

        // ✅ applicationStore 통해 application 조회
        await applicationStore.fetchApplicationById(interviewResult.applicationId);
        const application = applicationStore.selectedApplication;
        const applicantId = application?.applicantId;

        console.log('🆔 applicantId from store:', applicantId);

        // ✅ applicantStore 통해 applicant 조회
        if (applicantId) {
            await applicantStore.fetchApplicantById(applicantId);
            applicant.value = applicantStore.selectedApplicant;
        } else {
            applicant.value = null;
        }

        console.log('🎯 최종 applicant:', applicant.value);
    } catch (err) {
        console.warn('지원자 정보 없음 or 에러 발생', err);
        applicant.value = null;
    } finally {
        loading.value = false;
    }
};

const fetchCriteria = async (sheetId) => {
    loading.value = true;
    try {
        await criteriaStore.fetchCriteriaById(sheetId);
        criteriaList.value = [selectedCriteria.value]; // 이건 UI에 맞게 조절
    } catch (err) {
        console.warn('평가 기준 조회 실패', err);
        criteriaList.value = [];
    } finally {
        loading.value = false;
    }
};

const assignInterview = () => {
    // 나중에 면접 배정 모달 or 페이지 이동 구현
    alert(`지원서 ${applicationId}에 대해 면접을 배정합니다.`);
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
    await fetchApplicant(); // 내부에서 interview와 applicant 모두 처리
    console.log('📌 applicant:', applicant.value); // 👈 이게 실제 객체로 잘 나오는지
    if (interview.value?.sheetId) {
        await fetchCriteria(interview.value.sheetId);
    }
});

const isEditing = ref(false);
const editDatetime = ref(null);
const editTime = ref('');
const editAddress = ref('');
const dialog = ref(false);

const selectedHour = ref('09');   // 초기값 예시
const selectedMinute = ref('00');

const hourOptions = Array.from({ length: 24 }, (_, i) => String(i).padStart(2, '0'));
const minuteOptions = ['00', '10', '20', '30', '40', '50'];

const startEditing = () => {
    if (!interview.value) return;

    const original = new Date(interview.value.datetime);
    editDatetime.value = original.toISOString().substr(0, 10); // YYYY-MM-DD

    selectedHour.value = String(original.getHours()).padStart(2, '0');
    selectedMinute.value = String(Math.floor(original.getMinutes() / 10) * 10).padStart(2, '0');

    editAddress.value = interview.value.address;

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

        await interviewStore.updateInterviewDatetime(interview.value.id, datetimeStr);
        await interviewStore.updateInterviewAddress(interview.value.id, editAddress.value);

        await interviewStore.fetchInterviewById(interview.value.id);

        alert('면접 정보가 성공적으로 수정되었습니다.');
        dialog.value = false;
    } catch (err) {
        console.error('면접 정보 수정 실패:', err);
        alert('면접 정보 수정에 실패했습니다.');
    }
};

</script>
