<template>
    <v-container>
        <v-card class="pa-8" max-width="900px" style="margin:auto;">
            <v-card-title class="justify-center text-h6 font-weight-bold">
                결재 작성하기
            </v-card-title>
            <v-card-text>
                <v-form @submit.prevent="handleSubmit">
                    <!-- 결재유형(카테고리) 선택 -->
                    <v-row>
                        <v-col cols="3">결재 유형</v-col>
                        <v-col cols="9">
                            <v-select v-model="form.categoryId" :items="categoryList" item-title="name" item-value="id"
                                label="결재 유형을 선택하세요" @update:model-value="onCategoryChange" clearable required />
                        </v-col>
                    </v-row>

                    <!-- 동적으로 결재 항목 표시 -->
                    <v-row v-for="(item, idx) in categoryItems" :key="item.id" class="mb-2">
                        <v-col cols="3">{{ item.name }}</v-col>
                        <v-col cols="9">
                            <v-text-field v-model="form.contents[idx].content" :label="item.name" required />
                        </v-col>
                    </v-row>

                    <!-- 결재자 선택 -->
                    <v-row>
                        <v-col cols="3">결재자</v-col>
                        <v-col cols="9">
                            <div v-for="(approver, idx) in form.approvers" :key="idx" class="mb-2 d-flex align-center">
                                <v-select v-model="form.approvers[idx].memberId" :items="approverList" item-title="name"
                                    item-value="id" :label="`${idx + 1}차 결재자 선택`" clearable required class="mr-2" />
                                <v-btn icon="mdi-minus" size="x-small" color="error" @click="removeApprover(idx)"
                                    v-if="form.approvers.length > 1" />
                            </div>
                            <!-- 결재자 추가 버튼 -->
                            <v-btn size="x-small" variant="outlined" @click="addApprover"
                                :disabled="form.approvers.length >= 4" class="mt-1">+ 추가</v-btn>
                        </v-col>
                    </v-row>

                    <v-row>
                        <v-col cols="12" class="d-flex justify-end">
                            <v-btn color="primary" type="submit">제출</v-btn>
                        </v-col>
                    </v-row>
                </v-form>
            </v-card-text>
        </v-card>
    </v-container>
</template>

<script setup>
import { onMounted } from 'vue';
import { storeToRefs } from 'pinia';
import { useApprovalWriteStore } from '@/stores/approvalWriteStore';
import { ApproverDTO } from '@/dto/approval/approval/createApprovalDTO';

const approvalStore = useApprovalWriteStore();
const { form, categoryList, categoryItems, approverList, loading } = storeToRefs(approvalStore);

onMounted(() => {
    approvalStore.fetchCategories();
    if (!form.value.approvers.length) {
        form.value.approvers = [new ApproverDTO({ order: 1, memberId: null })];
    }
    // 결재자 목록도 실제 API로 불러오는게 실무 방식!
    approverList.value = [
        { id: 11, name: '이철수' },
        { id: 12, name: '박우석' },
        { id: 13, name: '홍길동' }
    ];
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

const handleSubmit = async () => {
    await approvalStore.submitApproval();
    alert('결재 요청이 등록되었습니다.');
    // 라우터 이동 등 후처리
};
</script>
