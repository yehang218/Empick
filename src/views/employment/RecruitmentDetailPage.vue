<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { useRecruitmentStore } from '@/stores/recruitmentStore'
import { fetchRecruitmentProcesses } from '@/services/recruitmentProcessService'
import { fetchRecruitmentRequestDetail } from '@/services/recruitmentRequestService'
import { getRecruitTypeLabel } from '@/constants/employment/recruitTypes'
import { getRecruitStatusLabel } from '@/constants/employment/recruitStatus'
import { getStepTypeLabel } from '@/constants/employment/stepType'

const route = useRoute()
const store = useRecruitmentStore()
const processList = ref([])

// 로딩 상태와 에러 상태 추가
const loading = computed(() => store.loadingDetail)
const error = computed(() => store.detailError)

// 상세 데이터
const detail = computed(() => store.detail)

const requestDetail = ref(null)

onMounted(async () => {
    const id = route.params.id
    try {
        await store.loadRecruitmentDetail(id)
        // 연관된 요청서 정보 불러오기
        if (detail.value.recruitment.recruitmentRequestId) {
            requestDetail.value = await fetchRecruitmentRequestDetail(detail.value.recruitment.recruitmentRequestId)
        }

        processList.value = await fetchRecruitmentProcesses(id)
        console.log('📌 processList:', processList.value)

    } catch (err) {
        console.error('채용 공고 상세 로딩 실패:', err)
    }
})

// 날짜 포맷 함수
function formatDate(date) {
    if (!date) return ''
    return new Date(date).toLocaleString('ko-KR', { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' })
}
</script>

<template>
    <v-container fluid class="pa-6">
        <!-- 로딩 중 -->
        <v-skeleton-loader v-if="loading" type="article" />

        <!-- 에러 발생 -->
        <v-alert v-else-if="error" type="error" class="mb-4">
            {{ error }}
        </v-alert>

        <!-- 데이터 표시 -->
        <v-card v-else-if="detail" class="pa-6" flat>

            <v-row align="center" justify="space-between" class="mb-6">
                <v-col cols="auto" class="d-flex align-center">
                    <v-icon @click="$router.back()" class="me-2 cursor-pointer" size="28" color="black">
                        mdi-arrow-left
                    </v-icon>
                    <h2 class="text-h5 font-weight-bold">채용 공고 상세</h2>
                </v-col>

                <v-col cols="auto" class="d-flex gap-2">
                    <v-btn class="mr-2" variant="outlined" color="success"
                        :to="`/employment/applicants?recruitmentId=${detail.recruitment.id}`">
                        지원자 현황 보기
                    </v-btn>
                    <v-btn variant="outlined" color="success"
                        :to="`/employment/application-items?recruitmentId=${detail.recruitment.id}`">
                        지원서 항목 보기
                    </v-btn>
                </v-col>
            </v-row>


            <v-card class="mb-4 pa-4">
                <div class="font-weight-bold mb-2" style="color: #2f6f3e;">제목</div>
                <div>{{ detail.recruitment.title }}</div>
            </v-card>

            <v-card class="mb-4 pa-4">
                <div class="font-weight-bold mb-2" style="color: #2f6f3e;">내용</div>
                <div>{{ detail.recruitment.content }}</div>
            </v-card>

            <v-card class="mb-4 pa-4">
                <div class="font-weight-bold mb-2" style="color: #2f6f3e;">유형</div>
                <div>{{ getRecruitTypeLabel(detail.recruitment.recruitType) }}</div>
            </v-card>

            <v-card class="mb-4 pa-4">
                <div class="font-weight-bold mb-2" style="color: #2f6f3e;">상태</div>
                <div>{{ getRecruitStatusLabel(detail.recruitment.status) }}</div>
            </v-card>

            <v-card class="mb-4 pa-4">
                <div class="font-weight-bold mb-2" style="color: #2f6f3e;">모집 기간</div>
                <div>{{ formatDate(detail.recruitment.startedAt) }} ~ {{ formatDate(detail.recruitment.endedAt) }}</div>
            </v-card>

            <!-- 채용 요청서 내용 불러오기 -->
            <v-card v-if="requestDetail" class="mb-4 pa-4">
                <div class="font-weight-bold mb-2" style="color: #2f6f3e;">포지션명</div>
                <div class="white-space-pre-line">{{ requestDetail.jobName }}</div>
            </v-card>

            <v-card v-if="requestDetail" class="mb-4 pa-4">
                <div class="font-weight-bold mb-2" style="color: #2f6f3e;">고용 형태</div>
                <div class="white-space-pre-line">{{ requestDetail.employmentType }}</div>
            </v-card>

            <v-card v-if="requestDetail" class="mb-4 pa-4">
                <div class="font-weight-bold mb-2" style="color: #2f6f3e;">주요 업무</div>
                <div class="white-space-pre-line">{{ requestDetail.responsibility }}</div>
            </v-card>

            <v-card v-if="requestDetail" class="mb-4 pa-4">
                <div class="font-weight-bold mb-2" style="color: #2f6f3e;">모집 인원</div>
                <div>{{ requestDetail.headcount }}명</div>
            </v-card>

            <v-card v-if="requestDetail" class="mb-4 pa-4">
                <div class="font-weight-bold mb-2" style="color: #2f6f3e;">근무 지역</div>
                <div>{{ requestDetail.workLocation }}</div>
            </v-card>

            <v-card v-if="requestDetail" class="mb-4 pa-4">
                <div class="font-weight-bold mb-2" style="color: #2f6f3e;">자격 요건</div>
                <div class="white-space-pre-line">{{ requestDetail.qualification }}</div>
            </v-card>

            <v-card v-if="requestDetail" class="mb-4 pa-4">
                <div class="font-weight-bold mb-2" style="color: #2f6f3e;">우대 사항</div>
                <div class="white-space-pre-line">{{ requestDetail.preference }}</div>
            </v-card>

            <!-- 채용 프로세스 정보 추가 -->
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
        </v-card>

        <!-- 데이터 없음 -->
        <v-alert v-else type="info" title="데이터가 없습니다." />
    </v-container>
</template>

<style scoped>
.white-space-pre-line {
    white-space: pre-line;
}
</style>
