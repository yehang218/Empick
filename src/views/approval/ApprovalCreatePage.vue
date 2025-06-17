<template>
    <v-container class="py-10" style="background:#fff;">
        <v-row justify="center">
            <v-col cols="12" md="10" lg="8">
                <div class="approval-card pa-8">
                    <div class="text-h5 font-weight-bold mb-8 text-center" style="color:#4B6B3A;">
                        결재 문서 작성
                    </div>

                    <!-- 결재 유형, 구분, 작성일 등 상단 입력 -->
                    <v-row class="mb-6" align="center">
                        <v-col cols="2" class="approval-label">결재 유형</v-col>
                        <v-col cols="4">
                            <v-select v-model="form.categoryId" :items="categoryList" item-title="name" item-value="id"
                                label="유형 선택" variant="solo" density="comfortable" class="approval-input" hide-details @update:model-value="onCategoryChange" clearable required />
                        </v-col>
                        <v-col cols="2" class="approval-label">작성일</v-col>
                        <v-col cols="4">
                            <v-text-field v-model="form.createdAt" label="작성일" variant="solo" density="comfortable" class="approval-input" hide-details readonly />
                        </v-col>
                    </v-row>

                    <!-- 결재선 -->
                    <div class="approval-label mb-2">결재선</div>
                    <div class="d-flex mb-6 flex-wrap">
                        <div v-for="(approver, idx) in form.approvers" :key="idx" class="approver-box mr-4 mb-2" style="position:relative;">
                            <div class="approver-name">{{ approverList.find(a => a.id === approver.memberId)?.name || '결재자 선택' }}</div>
                            <div v-if="approver.memberId" class="approver-info">
                                {{ approverList.find(a => a.id === approver.memberId)?.departmentName }} · 
                                {{ approverList.find(a => a.id === approver.memberId)?.positionName }}
                            </div>
                            <v-select v-model="form.approvers[idx].memberId" 
                                :items="getAvailableApprovers(idx)" 
                                item-title="displayName" 
                                item-value="id"
                                label="결재자" 
                                variant="solo" 
                                density="comfortable" 
                                class="approval-input mt-1" 
                                hide-details 
                                clearable 
                                required />
                            <v-btn v-if="form.approvers.length > 1" icon size="x-small" class="remove-approver-btn" @click="removeApprover(idx)" style="position:absolute; top:6px; right:6px; z-index:2;">
                                <v-icon size="16">mdi-close</v-icon>
                            </v-btn>
                        </div>
                        <v-btn icon="mdi-plus" size="small" class="add-approver-btn" @click="addApprover" :disabled="form.approvers.length >= 4" />
                    </div>

                    <!-- 동적 결재 항목 -->
                    <div v-for="(item, idx) in categoryItems" :key="item.id" class="mb-4">
                        <v-row align="center">
                            <v-col cols="2" class="approval-label">{{ item.name }}</v-col>
                            <v-col cols="10">
                                <v-text-field v-if="item.inputType === InputTypeEnum.TEXT"
                                    v-model="form.contents[idx].content" :label="item.name" variant="solo" density="comfortable" class="approval-input" hide-details required />
                                <v-textarea v-else-if="item.inputType === InputTypeEnum.TEXTAREA"
                                    v-model="form.contents[idx].content" :label="item.name" variant="solo" density="comfortable" class="approval-input" hide-details required />
                                <v-file-input v-else-if="item.inputType === InputTypeEnum.FILE"
                                    v-model="form.contents[idx].content" :label="item.name" variant="solo" density="comfortable" class="approval-input" hide-details required />
                                <v-text-field v-else-if="item.inputType === InputTypeEnum.URL"
                                    v-model="form.contents[idx].content" :label="item.name" type="url" variant="solo" density="comfortable" class="approval-input" hide-details required />
                                <v-text-field v-else-if="item.inputType === InputTypeEnum.DATE"
                                    v-model="form.contents[idx].content" :label="item.name" type="date" variant="solo" density="comfortable" class="approval-input" hide-details required />
                                <v-text-field v-else-if="item.inputType === InputTypeEnum.NUMBER"
                                    v-model="form.contents[idx].content" :label="item.name" type="number" variant="solo" density="comfortable" class="approval-input" hide-details required />
                                <v-radio-group v-else-if="item.inputType === InputTypeEnum.RADIO"
                                    v-model="form.contents[idx].content" :label="item.name" class="approval-input" hide-details required>
                                    <v-radio v-for="option in item.options || []" :key="option" :label="option" :value="option" />
                                </v-radio-group>
                                <v-checkbox-group v-else-if="item.inputType === InputTypeEnum.CHECKBOX"
                                    v-model="form.contents[idx].content" :label="item.name" class="approval-input" hide-details required>
                                    <v-checkbox v-for="option in item.options || []" :key="option" :label="option" :value="option" />
                                </v-checkbox-group>
                                <v-text-field v-else v-model="form.contents[idx].content" :label="item.name" variant="solo" density="comfortable" class="approval-input" hide-details required />
                            </v-col>
                        </v-row>
                    </div>

                    <!-- 첨부 문서 안내 -->
                    <div class="approval-label mt-8 mb-2">첨부 문서</div>
                    <div class="mb-6" style="color:#888;">첨부된 문서가 없습니다.</div>

                    <!-- 안내문구 -->
                    <div class="approval-guide mt-8 mb-4">
                        ※ 신청서 제출 후에는 수정이 불가능하니, 내용을 정확히 입력해 주세요.<br />
                        ※ 모든 결재자가 승인해야 휴가가 최종 확정되며, 확정 이후 근태에 반영됩니다.<br />
                        ※ 신청한 휴가 기간과 사유는 인사팀 및 관리자에게 전달됩니다.
                    </div>

                    <!-- 제출 버튼 -->
                    <div class="d-flex justify-end mt-6">
                        <v-btn color="success" class="approval-btn" @click="handleSubmit">등록</v-btn>
                    </div>
                </div>
            </v-col>
        </v-row>
    </v-container>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { storeToRefs } from 'pinia';
import { useApprovalWriteStore } from '@/stores/approvalWriteStore';
import { ApproverDTO } from '@/dto/approval/approval/createApprovalDTO';
import { InputTypeEnum } from '@/constants/common/inputType.js';
import { useMemberStore } from '@/stores/memberStore'
import { useRouter } from 'vue-router';

const approvalStore = useApprovalWriteStore();
const memberStore = useMemberStore();
const router = useRouter();

const memberId = ref(null);

const { form, categoryList, categoryItems, loading } = storeToRefs(approvalStore);
// const { memberList } = storeToRefs(memberStore);

// 결재자 목록
// const approverList = computed(() => 
//     memberList.value.map(member => ({
//         ...member,
//         displayName: `${member.name} (${member.departmentName} · ${member.positionName})`
//     }))
// );

const approverList = [
    { id: 1, name: '이철수', departmentName: '개발팀', positionName: '과장', displayName: '이철수 (개발팀 · 과장)' },
    { id: 10, name: '박우석', departmentName: '기획팀', positionName: '대리', displayName: '박우석 (기획팀 · 대리)' },
    { id: 57, name: '홍길동', departmentName: '인사팀', positionName: '사원', displayName: '홍길동 (인사팀 · 사원)' },
    { id: 62, name: '김민지', departmentName: '디자인팀', positionName: '팀장', displayName: '김민지 (디자인팀 · 팀장)' },
];

onMounted(async () => {
    approvalStore.fetchCategories();
    if (!form.value.approvers.length) {
        form.value.approvers = [new ApproverDTO({ order: 1, memberId: null })];
    }

    // await memberStore.getMemberList();
    await memberStore.getMyInfo();
    memberId.value = memberStore.form.id;

    // 작성일 자동으로
    if (!form.value.createdAt) {
        form.value.createdAt = new Date().toISOString().slice(0, 10);
    }
});

const onCategoryChange = async (categoryId) => {
    await approvalStore.fetchCategoryItems(categoryId);
};

const addApprover = () => {
    if (form.value.approvers.length < 4) {
        form.value.approvers.push(new ApproverDTO({ order: form.value.approvers.length + 1, memberId: null }));
    }
};

const removeApprover = (idx) => {
    if (form.value.approvers.length > 1) {
        form.value.approvers.splice(idx, 1);
        form.value.approvers.forEach((a, i) => a.order = i + 1);
    }
};

const getAvailableApprovers = (currentIndex) => {
    // 현재 선택된 모든 결재자 ID 목록
    const selectedApproverIds = form.value.approvers
        .map((approver, idx) => idx !== currentIndex ? approver.memberId : null)
        .filter(id => id !== null);
    
    // 이미 선택된 결재자를 제외한 목록 반환
    return approverList.filter(approver => !selectedApproverIds.includes(approver.id));
};

const handleSubmit = async () => {
    try {
        // 기본 유효성 검사
        if (!form.value.categoryId) {
            alert('결재 유형을 선택해주세요.');
            return;
        }
        if (!form.value.approvers.length) {
            alert('최소 한 명의 결재자를 지정해주세요.');
            return;
        }
        if (!form.value.approvers.every(a => a.memberId)) {
            alert('모든 결재자를 선택해주세요.');
            return;
        }
        if (!form.value.contents.every(c => c.content)) {
            alert('모든 결재 항목을 입력해주세요.');
            return;
        }

        await approvalStore.submitApproval(memberId.value);
        alert('결재 요청이 등록되었습니다.');
        // 목록 페이지로 이동
        router.push('/approval/sent');
    } catch (error) {
        console.error('결재 요청 실패:', error);
        alert(error.message || '결재 요청 중 오류가 발생했습니다.');
    }
};
</script>

<style scoped>
.approval-card {
    border: 1.5px solid #7BAE6C;
    border-radius: 18px;
    background: #fff;
    box-shadow: 0 2px 8px 0 #e6f2e6;
}
.approval-label {
    font-weight: bold;
    color: #4B6B3A;
    min-width: 80px;
    font-size: 1.05rem;
    display: flex;
    align-items: center;
}
.approval-input .v-input__control {
    border-radius: 8px !important;
    border: 1.5px solid #7BAE6C !important;
    background: #f8fff8 !important;
}
.approver-box {
    border: 1.5px solid #7BAE6C;
    border-radius: 12px;
    padding: 12px 18px 8px 18px;
    min-width: 140px;
    background: #f8fff8;
    display: flex;
    flex-direction: column;
    align-items: center;
}
.approver-name {
    font-weight: bold;
    color: #4B6B3A;
    margin-bottom: 2px;
    font-size: 1.02rem;
}
.approver-info {
    font-size: 0.9rem;
    color: #666;
    margin-bottom: 4px;
}
.add-approver-btn {
    border: 1.5px solid #7BAE6C !important;
    background: #eaf7e6 !important;
    color: #4B6B3A !important;
    border-radius: 8px !important;
    margin-top: 8px;
}
.approval-btn {
    border-radius: 8px !important;
    min-width: 120px;
    font-weight: bold;
    border: 1.5px solid #7BAE6C !important;
    color: #fff !important;
    background: #7BAE6C !important;
}
.approval-guide {
    color: #4B6B3A;
    font-size: 0.98rem;
    background: #f8fff8;
    border-radius: 8px;
    padding: 16px 20px;
    border: 1px solid #eaf7e6;
}
.remove-approver-btn {
    position: absolute;
    top: 6px;
    right: 6px;
    background: #fff !important;
    color: #7BAE6C !important;
    border-radius: 50% !important;
    box-shadow: 0 1px 4px #eaf7e6;
    min-width: 24px !important;
    min-height: 24px !important;
    padding: 0 !important;
}
</style>
