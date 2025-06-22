import { defineStore } from 'pinia';
import { ref } from 'vue';
import {
    getApprovalCategories,
    getSubCategories,
    getApprovalCategoryItems,
    createApprovalService,
    getApprovalLine,
    createApprovalContentDTOs,
    createApprovalDTO,
    createApprovalDTOFromJson,
    sanitizeApprovalForm
} from '@/services/approvalService';
import { InputTypeEnum } from '@/constants/common/inputType';

export const useApprovalWriteStore = defineStore('approvalWrite', () => {
    // 상태
    const form = ref(createApprovalDTO());
    const categoryList = ref([]);
    const categoryItems = ref([]);
    const approverList = ref([]);
    const approvalLine = ref([]);
    const loading = ref(false);

    // 폼 리셋
    const resetForm = () => {
        form.value = createApprovalDTO();
        categoryItems.value = [];
        approvalLine.value = [];
    };

    // 외부 JSON으로 폼 세팅
    const setFormFromJson = (json) => {
        form.value = createApprovalDTOFromJson(json);
    };

    // 카테고리 불러오기
    const fetchCategories = async () => {
        loading.value = true;
        try {
            categoryList.value = await getSubCategories();
        } finally {
            loading.value = false;
        }
    };

    // 카테고리별 항목 불러오기
    const fetchCategoryItems = async (categoryId) => {
        loading.value = true;
        try {
            categoryItems.value = await getApprovalCategoryItems(categoryId);

            // inputType이 문자열이면 숫자로 변환
            categoryItems.value = categoryItems.value.map(item => ({
                ...item,
                inputType: typeof item.inputType === 'string' ? InputTypeEnum[item.inputType] : item.inputType
            }));

            // 서비스의 헬퍼 함수를 사용하여 DTO 생성
            form.value.contents = createApprovalContentDTOs(categoryItems.value);
            form.value.categoryId = categoryId;
        } finally {
            loading.value = false;
        }
    };

    // 결재라인 불러오기
    const fetchApprovalLine = async (categoryId, writerId) => {
        loading.value = true;
        try {
            approvalLine.value = await getApprovalLine(categoryId, writerId);
        } finally {
            loading.value = false;
        }
    };

    // 결재 요청
    const submitApproval = async (memberId) => {
        loading.value = true;
        try {
            // writerId 설정
            form.value.writerId = Number(memberId);

            validateForm(form.value);
            const sanitizedForm = sanitizeApprovalForm(form.value, memberId);
            const result = await createApprovalService(sanitizedForm);

            resetForm();
            return result;
        } catch (error) {
            console.error('결재 요청 실패:', error);
            if (error.response?.data?.message) {
                throw new Error(error.response.data.message);
            }
            throw error;
        } finally {
            loading.value = false;
        }
    };


    const validateForm = (form) => {
        if (!form.categoryId) throw new Error('카테고리를 선택해주세요');
        if (!form.writerId) throw new Error('작성자 정보가 없습니다');
        if (!form.approvers?.length) throw new Error('지정된 결재가 없습니다.\n 관리자에게 문의해주세요.');
        if (!form.contents?.length) throw new Error('결재 항목이 없습니다');

        // 결재자 유효성 검사
        // if (!Array.isArray(form.approvers)) throw new Error('결재자 정보가 올바르지 않습니다');
        // if (!form.approvers.every(a => a && a.memberId)) throw new Error('모든 결재자를 선택해주세요');

        // 결재 내용 유효성 검사
        if (!Array.isArray(form.contents)) throw new Error('결재 내용이 올바르지 않습니다');
        if (!form.contents.every(c => c && c.itemId && c.content)) throw new Error('모든 결재 항목을 입력해주세요');

        return true;
    };

    return {
        // 상태
        form, categoryList, categoryItems, approverList, loading, approvalLine,
        // 메서드
        resetForm, setFormFromJson,
        fetchCategories, fetchCategoryItems, submitApproval,
        fetchApprovalLine,
    };
});
