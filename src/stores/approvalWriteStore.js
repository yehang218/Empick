// stores/approvalWriteStore.js
import { defineStore } from 'pinia';
import { ref } from 'vue';
import { CreateApprovalDTO, ApproverDTO, ApprovalContentDTO } from '@/dto/approval/approval/createApprovalDTO';
import {
    getApprovalCategories,
    getApprovalCategoryItems,
    createApprovalService
} from '@/services/approvalService';

export const useApprovalWriteStore = defineStore('approvalWrite', () => {
    // 상태
    const form = ref(new CreateApprovalDTO());
    const categoryList = ref([]);
    const categoryItems = ref([]);
    const approverList = ref([]);
    const loading = ref(false);

    // 폼 리셋
    const resetForm = () => {
        form.value = new CreateApprovalDTO();
    };

    // 외부 JSON으로 폼 세팅
    const setFormFromJson = (json) => {
        form.value = CreateApprovalDTO.fromJSON(json);
    };

    // 액션: 카테고리 불러오기
    const fetchCategories = async () => {
        loading.value = true;
        try {
            categoryList.value = await getApprovalCategories();
        } finally {
            loading.value = false;
        }
    };

    // 액션: 카테고리별 항목 불러오기
    const fetchCategoryItems = async (categoryId) => {
        loading.value = true;
        try {
            categoryItems.value = await getApprovalCategoryItems(categoryId);
            form.value.contents = categoryItems.value.map(
                item => new ApprovalContentDTO({ itemId: item.id, content: '' })
            );
            form.value.categoryId = categoryId;
        } finally {
            loading.value = false;
        }
    };

    // 액션: 결재 요청 생성
    const submitApproval = async () => {
        loading.value = true;
        try {
            // 예: writerId를 로그인 사용자로 지정 (여기선 1로 예시)
            form.value.writerId = 1;
            await createApprovalService(form.value); // 서비스 함수로 POST 요청
            resetForm();
        } finally {
            loading.value = false;
        }
    };

    return {
        // 상태
        form, categoryList, categoryItems, approverList, loading,
        // 메서드
        resetForm, setFormFromJson,
        fetchCategories, fetchCategoryItems, submitApproval
    };
});
