<template>
    <v-container fluid class="pa-6">
        <!-- 상단 헤더 -->
        <v-row justify="space-between" align="center" class="mb-6">
            <h2 class="text-h5 font-weight-bold">채용 공고 작성</h2>
            <div>
                <v-btn variant="tonal" class="mr-2" @click="cancel">취소하기</v-btn>
                <v-btn color="success" variant="elevated" @click="goToApplicationItem">지원서 항목 선택</v-btn>
            </div>
        </v-row>

        <v-card v-if="requestDetail" class="mb-6 pa-6" elevation="1" color="#f8faf9">
            <h3 class="text-subtitle-1 font-weight-bold d-flex align-center mb-4">
                <v-icon class="mr-2" color="success">mdi-clipboard-text</v-icon>
                채용 요청서 정보
            </h3>

            <v-row dense>
                <!-- 포지션 / 부서 -->
                <v-col cols="12">
                    <div class="text-caption text-grey">포지션명</div>
                    <div class="text-body-1 font-weight-medium">{{ requestDetail.jobName }}</div>
                </v-col>
                <v-col cols="12">
                    <div class="text-caption text-grey">부서명</div>
                    <div class="text-body-1 font-weight-medium">{{ requestDetail.departmentName }}</div>
                </v-col>

                <!-- 모집 인원 / 고용 형태 -->
                <v-col cols="12">
                    <div class="text-caption text-grey">모집 인원</div>
                    <div class="text-body-1 font-weight-medium">{{ requestDetail.headcount }}명</div>
                </v-col>
                <v-col cols="12">
                    <div class="text-caption text-grey">고용 형태</div>
                    <div class="text-body-1 font-weight-medium">{{ requestDetail.employmentType }}</div>
                </v-col>

                <!-- 근무 지역 -->
                <v-col cols="12">
                    <div class="text-caption text-grey">근무 지역</div>
                    <div class="text-body-1 font-weight-medium">{{ requestDetail.workLocation }}</div>
                </v-col>

                <!-- 주요 업무 -->
                <v-col cols="12">
                    <div class="text-caption text-grey">주요 업무</div>
                    <div class="text-body-1 font-weight-medium" style="white-space: pre-line;">
                        {{ requestDetail.responsibility }}
                    </div>
                </v-col>

                <!-- 자격 요건 -->
                <v-col cols="12">
                    <div class="text-caption text-grey">자격 요건</div>
                    <div class="text-body-1 font-weight-medium" style="white-space: pre-line;">
                        {{ requestDetail.qualification }}
                    </div>
                </v-col>

                <!-- 우대 사항 -->
                <v-col cols="12">
                    <div class="text-caption text-grey">우대 사항</div>
                    <div class="text-body-1 font-weight-medium" style="white-space: pre-line;">
                        {{ requestDetail.preference }}
                    </div>
                </v-col>
            </v-row>
        </v-card>



        <!-- 입력 폼 -->
        <v-form ref="formRef" v-model="isValid">
            <v-row dense>
                <!-- 제목 -->
                <v-col cols="12">
                    <v-text-field v-model="form.title" label="채용 공고 제목" :rules="[rules.required]" />
                </v-col>

                <!-- 유형 선택 -->
                <v-select v-model="form.recruitType" :items="recruitTypeOptions" item-title="label" item-value="value"
                    label="채용 유형" />

                <!-- 이미지 URL -->
                <v-col cols="12" md="6">
                    <v-text-field v-model="form.imageUrl" label="이미지 URL (선택)" />
                </v-col>

                <!-- 모집 시작일 -->
                <v-col cols="12" md="6">
                    <v-text-field v-model="form.startedAt" label="모집 시작일" type="datetime-local"
                        :rules="[rules.required]" />
                </v-col>

                <!-- 모집 마감일 -->
                <v-col cols="12" md="6">
                    <v-text-field v-model="form.endedAt" label="모집 마감일" type="datetime-local"
                        :rules="[rules.required]" />
                </v-col>

                <!-- 상세 내용 에디터 -->
                <v-col cols="12">
                    <label class="font-weight-medium mb-1 d-block">채용 공고 상세 내용</label>
                    <div class="editor-wrapper">
                        <QuillEditor v-model:content="form.content" contentType="html" theme="snow"
                            placeholder="채용 공고 내용을 입력하세요" class="editor" />
                    </div>
                </v-col>

                <!-- 채용 프로세스 -->
                <v-col cols="12">
                    <label class="font-weight-medium mb-1 d-block">채용 프로세스</label>

                    <!-- 입력 필드 -->
                    <v-row align="center" dense>
                        <v-col cols="12" md="6">
                            <v-select v-model="newStep.stepType" :items="stepTypeOptions" label="전형 단계"
                                item-title="label" item-value="value" />
                        </v-col>
                        <v-col cols="12" md="3">
                            <v-text-field v-model="newStep.displayOrder" type="number" label="표시 순서" />
                        </v-col>
                        <v-col cols="12" md="3">
                            <v-btn @click="addStep" color="primary"
                                :disabled="!newStep.stepType || newStep.displayOrder === null">
                                단계 추가
                            </v-btn>
                        </v-col>
                    </v-row>


                    <!-- 등록된 프로세스 리스트 -->
                    <v-list two-line>
                        <v-col cols="12" v-for="(step, index) in form.recruitmentProcesses" :key="index">
                            <v-row justify="space-between" align="center">
                                <div>
                                    {{ getStepTypeLabel(step.stepType) }} / 순서: {{ step.displayOrder }}
                                </div>
                                <v-btn icon @click="removeStep(index)" size="small" variant="text">
                                    <v-icon size="small">mdi-delete</v-icon>
                                </v-btn>
                            </v-row>
                        </v-col>
                    </v-list>
                </v-col>

            </v-row>
        </v-form>
    </v-container>
    <ConfirmModal v-if="showCancelConfirm" message="작성을 취소하시겠습니까?" @confirm="confirmCancel"
        @cancel="showCancelConfirm = false" />
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { recruitTypeOptions } from '@/constants/employment/recruitTypes'
import { QuillEditor } from '@vueup/vue-quill'
import { stepTypeOptions, getStepTypeLabel } from '@/constants/employment/stepType'
import { useRecruitmentStore } from '@/stores/recruitmentStore'
import ConfirmModal from '@/components/common/Modal.vue'
import { useRecruitmentRequestStore } from '@/stores/recruitmentRequestStore'

const router = useRouter()
const route = useRoute()
const requestId = route.query.requestId;

const store = useRecruitmentStore()
const recruitmentRequestStore = useRecruitmentRequestStore()
const isValid = ref(true)
const formRef = ref()
const showCancelConfirm = ref(false)

const requestDetail = ref(null)

const newStep = ref({
    stepType: '',
    displayOrder: null
})

const form = ref({
    title: '',
    description: '',
    content: '',
    recruitType: null,
    imageUrl: '',
    startedAt: '',
    endedAt: '',
    recruitmentProcesses: [],
    recruitmentRequestId: route.query.id || null
})

// 폼 값이 바뀔 때마다 draftRecruitment에 저장
watch(form, (val) => {
    store.setDraftRecruitment(val)
}, { deep: true })

onMounted(async () => {
    if (store.draftRecruitment) {
        Object.assign(form.value, store.draftRecruitment)
    }

    const requestId = route.query.id

    if (requestId) {
        await recruitmentRequestStore.loadRecruitmentRequestDetail(requestId)
        requestDetail.value = recruitmentRequestStore.recruitmentRequestDetail
        if (requestDetail.value?.startedAt) {
            form.value.startedAt = requestDetail.value.startedAt.slice(0, 16)
        }
        if (requestDetail.value?.endedAt) {
            form.value.endedAt = requestDetail.value.endedAt.slice(0, 16)
        }
    }
})

const cancel = () => {
    showCancelConfirm.value = true
}

const confirmCancel = () => {
    store.clearDraftRecruitment()
    store.clearDraftApplicationItems()
    router.push('/employment/recruitments')
}

const rules = {
    required: v => !!v || '필수 입력 항목입니다.'
}

const goToApplicationItem = () => {

    store.setDraftRecruitment(form.value)

    router.push({
        path: '/employment/application-items/select',
        query: { requestId: route.query.id }
    })
}

// 채용 프로세스 추가
const addStep = () => {
    if (!newStep.value.stepType || !newStep.value.displayOrder) return

    form.value.recruitmentProcesses.push({ ...newStep.value })
    newStep.value = { stepType: '', displayOrder: null }
}

// 채용 프로세스 제거
const removeStep = (index) => {
    form.value.recruitmentProcesses.splice(index, 1)
}
</script>

<style scoped>
.editor-wrapper {
    border: 1px solid #ccc;
    border-radius: 6px;
    overflow: hidden;
}

::v-deep(.ql-container) {
    min-height: 500px;
    padding: 16px;
}
</style>