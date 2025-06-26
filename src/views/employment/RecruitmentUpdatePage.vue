<template>
    <v-container fluid class="pa-6">
        <!-- 상단 헤더 -->
        <v-row justify="space-between" align="center" class="mb-6">
            <h2 class="text-h5 font-weight-bold">채용 공고 수정</h2>
            <div>
                <v-btn variant="tonal" class="mr-2" @click="cancel">취소하기</v-btn>
                <v-btn color="primary" variant="elevated" @click="goToApplicationItem">지원서 항목 수정</v-btn>
            </div>
        </v-row>

        <!-- 로딩 상태 -->
        <v-skeleton-loader v-if="loading" type="article" />

        <!-- 수정 폼 -->
        <div v-else>
            <v-card v-if="requestDetail" class="mb-6 pa-6" elevation="1" color="#f8faf9">
                <h3 class="text-subtitle-1 font-weight-bold d-flex align-center mb-4">
                    <v-icon class="mr-2" color="success">mdi-clipboard-text</v-icon>
                    채용 요청서 정보
                </h3>
                <v-row dense>
                    <v-col cols="12">
                        <div class="text-caption text-grey">포지션명</div>
                        <div class="text-body-1 font-weight-medium">{{ requestDetail.jobName }}</div>
                    </v-col>
                    <v-col cols="12">
                        <div class="text-caption text-grey">부서명</div>
                        <div class="text-body-1 font-weight-medium">{{ requestDetail.departmentName }}</div>
                    </v-col>
                    <v-col cols="12">
                        <div class="text-caption text-grey">모집 인원</div>
                        <div class="text-body-1 font-weight-medium">{{ requestDetail.headcount }}명</div>
                    </v-col>
                    <v-col cols="12">
                        <div class="text-caption text-grey">고용 형태</div>
                        <div class="text-body-1 font-weight-medium">{{ requestDetail.employmentType }}</div>
                    </v-col>
                    <v-col cols="12">
                        <div class="text-caption text-grey">근무 지역</div>
                        <div class="text-body-1 font-weight-medium">{{ requestDetail.workLocation }}</div>
                    </v-col>
                    <v-col cols="12">
                        <div class="text-caption text-grey">주요 업무</div>
                        <div class="text-body-1 font-weight-medium" style="white-space: pre-line;">
                            {{ requestDetail.responsibility }}
                        </div>
                    </v-col>
                    <v-col cols="12">
                        <div class="text-caption text-grey">자격 요건</div>
                        <div class="text-body-1 font-weight-medium" style="white-space: pre-line;">
                            {{ requestDetail.qualification }}
                        </div>
                    </v-col>
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
                    <v-col cols="12">
                        <v-text-field v-model="form.title" label="채용 공고 제목" :rules="[rules.required]" />
                    </v-col>
                    <v-select v-model="form.recruitType" :items="recruitTypeOptions" item-title="label" item-value="value"
                        label="채용 유형" />
                    <v-col cols="12" md="6">
                        <v-text-field v-model="form.imageUrl" label="이미지 URL (선택)" />
                    </v-col>
                    <v-col cols="12" md="6">
                        <v-text-field v-model="form.startedAt" label="모집 시작일" type="datetime-local"
                            :rules="[rules.required]" />
                    </v-col>
                    <v-col cols="12" md="6">
                        <v-text-field v-model="form.endedAt" label="모집 마감일" type="datetime-local"
                            :rules="[rules.required]" />
                    </v-col>
                    <v-col cols="12">
                        <label class="font-weight-medium mb-1 d-block">채용 공고 상세 내용</label>
                        <div class="editor-wrapper">
                            <QuillEditor v-model:content="form.content" contentType="html" theme="snow"
                                placeholder="채용 공고 내용을 입력하세요" class="editor" />
                        </div>
                    </v-col>
                    <v-col cols="8">
                        <label class="font-weight-medium mb-1 d-block">채용 프로세스</label>
                        <v-row align="center" dense>
                            <v-col cols="12" md="6">
                                <v-select v-model="newStep.stepType" :items="stepTypeOptions" label="전형 단계"
                                    item-title="label" item-value="value" />
                            </v-col>
                            <v-col cols="12" md="3">
                                <v-btn @click="addStep" color="primary" :disabled="!newStep.stepType">
                                    단계 추가
                                </v-btn>
                            </v-col>
                        </v-row>
                        <div class="process-chip-list mt-2 mb-4">
                            <template v-for="(step, idx) in form.recruitmentProcesses" :key="idx">
                                <v-chip
                                    :color="stepColor(step.stepType)"
                                    class="mr-2 mb-2"
                                    size="small"
                                    label
                                >
                                    {{ getStepTypeLabel(step.stepType) }}
                                    <v-btn icon size="x-small" variant="text" @click.stop="removeStep(idx)">
                                        <v-icon size="x-small">mdi-close</v-icon>
                                    </v-btn>
                                </v-chip>
                                <span v-if="idx < form.recruitmentProcesses.length - 1" class="mx-1" style="font-size:1.2em;">&gt;</span>
                            </template>
                        </div>
                    </v-col>
                </v-row>
            </v-form>
        </div>
    </v-container>
    <ConfirmModal v-if="showCancelConfirm" message="수정을 취소하시겠습니까?" @confirm="confirmCancel"
        @cancel="showCancelConfirm = false" />
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter, useRoute, onBeforeRouteLeave } from 'vue-router';
import { useToast } from 'vue-toastification';
import { QuillEditor } from '@vueup/vue-quill';
import { useRecruitmentStore } from '@/stores/recruitmentStore';
import { useRecruitmentRequestStore } from '@/stores/recruitmentRequestStore';
import { useRecruitmentProcessStore } from '@/stores/recruitmentProcessStore';
import { useApplicationItemStore } from '@/stores/applicationItemStore';
import { recruitTypeOptions } from '@/constants/employment/recruitTypes';
import { stepTypeOptions, getStepTypeLabel } from '@/constants/employment/stepType';
import ConfirmModal from '@/components/common/Modal.vue';

const router = useRouter();
const route = useRoute();
const toast = useToast();
const store = useRecruitmentStore();
const recruitmentRequestStore = useRecruitmentRequestStore();
const processStore = useRecruitmentProcessStore();
const applicationItemStore = useApplicationItemStore();

const formRef = ref(null);
const isValid = ref(true);
const showCancelConfirm = ref(false);
const requestDetail = ref(null);
const loading = ref(false);

const recruitmentId = computed(() => Number(route.params.id));

const form = ref({
    title: '',
    content: '',
    recruitType: null,
    imageUrl: '',
    startedAt: '',
    endedAt: '',
    recruitmentProcesses: [],
    recruitmentRequestId: null,
    introduceTemplateId: null,
});

const newStep = ref({ stepType: '' });

const formatDateForInput = (dateString) => {
    if (!dateString) return '';
    const date = new Date(dateString);
    // YYYY-MM-DDTHH:mm format
    return new Date(date.getTime() - (date.getTimezoneOffset() * 60000)).toISOString().slice(0, 16);
};

onMounted(async () => {
    try {
        loading.value = true;

        // 1. 모든 지원서 항목 카테고리 미리 로드
        await store.loadApplicationItemCategories();
        const allCategories = store.applicationItemCategoryList;

        // 2. 채용 공고 상세 정보 로드
        await store.loadRecruitmentDetail(recruitmentId.value);
        const data = store.detail;

        if (data && data.recruitment) {
            const rec = data.recruitment;
            
            // 폼 데이터 설정
            form.value = {
                title: rec.title || '',
                content: rec.content || '',
                recruitType: rec.recruitType || null,
                imageUrl: rec.imageUrl || '',
                startedAt: formatDateForInput(rec.startedAt),
                endedAt: formatDateForInput(rec.endedAt),
                recruitmentProcesses: [],
                recruitmentRequestId: rec.recruitmentRequestId,
                introduceTemplateId: rec.introduceTemplateId,
            };

            // 채용 프로세스 로드
            await processStore.loadProcesses(recruitmentId.value);
            if (processStore.processList && processStore.processList.length > 0) {
                form.value.recruitmentProcesses = processStore.processList
                    .map(p => ({
                        stepType: p.stepType,
                        displayOrder: p.displayOrder,
                        recruitmentProcessId: p.recruitmentProcessId
                    }))
                    .sort((a, b) => a.displayOrder - b.displayOrder);
            }

            // 3. 현재 공고에 선택된 지원서 항목들 로드 (ApplicationItemDTO)
            await applicationItemStore.loadApplicationItems(recruitmentId.value);
            const selectedItems = applicationItemStore.items;

            // 4. 이름으로 매칭하여 올바른 카테고리 ID 찾기
            if (selectedItems && selectedItems.length > 0 && allCategories && allCategories.length > 0) {
                const getCategoryId = (item) => {
                    const category = allCategories.find(cat => cat.name === item.categoryName);
                    return category ? category.id : null;
                };
                
                const selectedCategoryIds = selectedItems.map(getCategoryId).filter(id => id !== null);
                const requiredCategoryIds = selectedItems.filter(item => item.required).map(getCategoryId).filter(id => id !== null);

                // 5. 올바른 ID 목록을 스토어에 저장
                store.setDraftApplicationItems(selectedCategoryIds, requiredCategoryIds);
            }

            // 채용 요청서 정보 로드
            if (rec.recruitmentRequestId) {
                await recruitmentRequestStore.loadRecruitmentRequestDetail(rec.recruitmentRequestId);
                requestDetail.value = recruitmentRequestStore.recruitmentRequestDetail;
            }
        }
    } catch (error) {
        console.error('❌ 수정 페이지 데이터 로드 실패:', error);
        toast.error('기존 공고 정보를 불러오는데 실패했습니다.');
    } finally {
        loading.value = false;
    }
});

const goToApplicationItem = () => {
    store.setDraftRecruitment(form.value);
    router.push({
        path: '/employment/application-items/select',
        query: { from: 'edit', id: recruitmentId.value }
    });
};

const addStep = () => {
    if (!newStep.value.stepType) return;
    const nextOrder = form.value.recruitmentProcesses.length + 1;
    form.value.recruitmentProcesses.push({
        stepType: newStep.value.stepType,
        displayOrder: nextOrder
    });
    newStep.value = { stepType: '' };
};

const removeStep = (index) => {
    form.value.recruitmentProcesses.splice(index, 1);
};

const cancel = () => {
    showCancelConfirm.value = true;
};

const confirmCancel = () => {
    store.clearAllDrafts();
    router.push(`/employment/recruitments/${recruitmentId.value}`);
};

const rules = {
    required: v => !!v || '필수 입력 항목입니다.',
};

const stepColor = (stepType) => {
    switch (stepType) {
        case 'DOCUMENT': return 'primary';
        case 'PRACTICAL': return 'success';
        case 'INTERVIEW': return 'purple';
        default: return 'grey-lighten-2';
    }
}

onBeforeRouteLeave((to) => {
    if (to.name !== 'ApplicationItemSelectPage') {
        store.clearAllDrafts();
    }
});
</script>

<style scoped>
.editor-wrapper {
    border: 1px solid #ccc;
    border-radius: 4px;
    overflow: hidden;
}

.editor {
    height: 300px;
}

.process-chip-list {
    display: flex;
    align-items: center;
    flex-wrap: wrap;
}
</style> 