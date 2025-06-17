<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useRecruitmentStore } from '@/stores/recruitmentStore'
import { useApplicationItemStore } from '@/stores/applicationItemStore'
import { useRecruitmentProcessStore } from '@/stores/recruitmentProcessStore'
import { useRecruitmentRequestStore } from '@/stores/recruitmentRequestStore'
import { getRecruitTypeLabel } from '@/constants/employment/recruitTypes'
import { getRecruitStatusLabel } from '@/constants/employment/recruitStatus'
import { getStepTypeLabel } from '@/constants/employment/stepType'
import ConfirmModal from '@/components/common/Modal.vue'
import { useToast } from 'vue-toastification'

const route = useRoute()
const router = useRouter()

const store = useRecruitmentStore()
const applicationItemStore = useApplicationItemStore()

const processStore = useRecruitmentProcessStore()
const requestStore = useRecruitmentRequestStore()

const processList = computed(() => processStore.processList)
const toast = useToast()

const detail = computed(() => store.detail)
const requestDetail = computed(() => requestStore.recruitmentRequestDetail);
const applicationItemDialog = ref(false)
const loading = computed(() => store.loadingDetail)
const error = computed(() => store.detailError)
const deleteDialog = ref(false)

const goToRecruitmentList = () => {
    const currentPage = route.query.page || 1;
    router.push({ path: '/employment/recruitments', query: { page: currentPage } });
};

const getInputComponent = (type) => {
    switch (type) {
        case 'TEXT': return 'v-text-field'
        case 'TEXTAREA': return 'v-textarea'
        case 'FILE': return 'v-file-input'
        case 'URL': return 'v-text-field'
        case 'DATE': return 'v-text-field'
        case 'NUMBER': return 'v-text-field'
        case 'RADIO': return 'v-radio-group'
        case 'CHECKBOX': return 'v-checkbox'
        default: return 'v-text-field'
    }
}

onMounted(async () => {
    const id = Number(route.params.id);
    await store.loadRecruitmentDetail(id);
    console.log('Loaded ID:', id);
    console.log('Detail:', store.value);


    // ✅ 이전 요청서 정보 초기화
    requestStore.recruitmentRequestDetail = null;

    const requestId = detail.value?.recruitment?.recruitmentRequestId;
    if (requestId !== null && requestId !== undefined) {
        await requestStore.loadRecruitmentRequestDetail(requestId);

    }

    await processStore.loadProcesses(id);
    await applicationItemStore.loadApplicationItems(id);
});

function formatDate(date) {
    if (!date) return ''
    return new Date(date).toLocaleString('ko-KR', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
    })
}

const handleDelete = async () => {
    try {
        await store.deleteExistingRecruitment(detail.value.recruitment.id)
        router.push({ path: '/employment/recruitments', query: { toast: 'deleted' } })
    } catch (e) {
        toast.error('삭제 실패: ' + e)
    }
}

const getStatusColor = (status) => {
    switch (status) {
        case 'WAITING': return 'grey'
        case 'PUBLISHED': return 'green'
        case 'CLOSED': return 'red'
        default: return 'grey'
    }
}
</script>

<template>
    <v-container fluid class="pa-6">
        <v-skeleton-loader v-if="loading" type="article" />
        <v-alert v-else-if="error" type="error" class="mb-4">{{ error }}</v-alert>

        <v-card v-else-if="detail" class="pa-6" flat>
            <v-row align="center" justify="space-between" class="mb-6">
                <v-col cols="auto" class="d-flex align-center">
                    <v-icon @click="goToRecruitmentList" class="me-2 cursor-pointer" size="28" color="black">
                        mdi-arrow-left
                    </v-icon>
                    <h2 class="text-h5 font-weight-bold me-3">
                        채용 공고 상세
                    </h2>
                    <v-chip v-if="detail?.recruitment?.status !== undefined"
                        :color="getStatusColor(detail.recruitment.status)" text-color="white" class="ml-2" size="small">
                        {{ getRecruitStatusLabel(detail.recruitment.status) }}
                    </v-chip>
                </v-col>

                <v-col cols="auto" class="d-flex gap-2">
                    <v-btn class="mr-2" variant="outlined" color="success" :to="{
                        path: `/employment/applicant/recruitments/${detail.recruitment.id}`,
                        query: { page: $route.query.page }
                    }">
                        지원자 현황 보기
                    </v-btn>
                    <v-btn class="mr-2" variant="outlined" color="success" @click="applicationItemDialog = true">
                        지원서 항목 보기
                    </v-btn>
                    <v-btn variant="outlined" color="error" @click="deleteDialog = true">
                        삭제
                    </v-btn>
                </v-col>
            </v-row>

            <v-card class="mb-4 pa-4">
                <div class="font-weight-bold mb-2" style="color: #2f6f3e;">제목</div>
                <div>{{ detail.recruitment.title }}</div>
            </v-card>
            <v-card class="mb-4 pa-4">
                <div class="font-weight-bold mb-2" style="color: #2f6f3e;">상세 내용</div>
                <div v-html="detail.recruitment.content"></div>
            </v-card>
            <v-card class="mb-4 pa-4">
                <div class="font-weight-bold mb-2" style="color: #2f6f3e;">유형</div>
                <div>{{ getRecruitTypeLabel(detail.recruitment.recruitType) }}</div>
            </v-card>
            <v-card class="mb-4 pa-4">
                <div class="font-weight-bold mb-2" style="color: #2f6f3e;">모집 기간</div>
                <div>{{ formatDate(detail.recruitment.startedAt) }} ~ {{ formatDate(detail.recruitment.endedAt) }}</div>
            </v-card>

            <v-card v-if="requestDetail" class="mb-4 pa-4">
                <div class="font-weight-bold mb-2" style="color: #2f6f3e;">포지션명</div>
                <div class="white-space-pre-line">{{ requestDetail.jobName }}</div>
            </v-card>
            <v-card v-if="requestDetail" class="mb-4 pa-4">
                <div class="font-weight-bold mb-2" style="color: #2f6f3e;">부서명</div>
                <div class="white-space-pre-line">{{ requestDetail.departmentName }}</div>
            </v-card>
            <v-card v-if="requestDetail" class="mb-4 pa-4">
                <div class="font-weight-bold mb-2" style="color: #2f6f3e;">모집 인원</div>
                <div>{{ requestDetail.headcount }}명</div>
            </v-card>
            <v-card v-if="requestDetail" class="mb-4 pa-4">
                <div class="font-weight-bold mb-2" style="color: #2f6f3e;">고용 형태</div>
                <div class="white-space-pre-line">{{ requestDetail.employmentType }}</div>
            </v-card>
            <v-card v-if="requestDetail" class="mb-4 pa-4">
                <div class="font-weight-bold mb-2" style="color: #2f6f3e;">근무 지역</div>
                <div>{{ requestDetail.workLocation }}</div>
            </v-card>
            <v-card v-if="requestDetail" class="mb-4 pa-4">
                <div class="font-weight-bold mb-2" style="color: #2f6f3e;">주요 업무</div>
                <div class="white-space-pre-line">{{ requestDetail.responsibility }}</div>
            </v-card>
            <v-card v-if="requestDetail" class="mb-4 pa-4">
                <div class="font-weight-bold mb-2" style="color: #2f6f3e;">자격 요건</div>
                <div class="white-space-pre-line">{{ requestDetail.qualification }}</div>
            </v-card>
            <v-card v-if="requestDetail" class="mb-4 pa-4">
                <div class="font-weight-bold mb-2" style="color: #2f6f3e;">우대 사항</div>
                <div class="white-space-pre-line">{{ requestDetail.preference }}</div>
            </v-card>

            <v-card v-if="processList.length" class="mb-4 pa-4">
                <div class="font-weight-bold mb-2" style="color: #2f6f3e;">채용 프로세스</div>
                <div>
                    <span v-for="(p, index) in processList.slice().sort((a, b) => a.displayOrder - b.displayOrder)"
                        :key="p.id">
                        <span>{{ getStepTypeLabel(p.stepType) }}</span>
                        <span v-if="index !== processList.length - 1"> → </span>
                    </span>
                </div>
            </v-card>

            <ConfirmModal v-if="deleteDialog" message="정말 삭제하시겠습니까?" @confirm="handleDelete"
                @cancel="deleteDialog = false" />

        </v-card>

        <v-dialog v-model="applicationItemDialog" max-width="700px">
            <v-card>
                <v-card-title class="text-h6 font-weight-bold">지원서 항목 미리보기</v-card-title>
                <v-divider />
                <v-card-text>
                    <v-card v-for="(item, index) in applicationItemStore.items" :key="index" class="mb-4 pa-4"
                        elevation="2">
                        <div class="font-weight-medium mb-2">
                            {{ item.categoryName }}
                            <span class="text-caption text-grey-darken-1">
                                ({{ item.inputType }} / 필수: {{ item.required ? 'O' : 'X' }})
                            </span>
                        </div>
                        <component :is="getInputComponent(item.inputType)" :label="item.categoryName" :readonly="true"
                            density="compact" />
                    </v-card>
                </v-card-text>
                <v-card-actions>
                    <v-spacer />
                    <v-btn text @click="applicationItemDialog = false">닫기</v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>

    </v-container>
</template>

<style scoped>
.white-space-pre-line {
    white-space: pre-line;
}
</style>
