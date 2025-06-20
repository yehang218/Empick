import { defineStore } from 'pinia';
import { ref } from 'vue';
import { getReceivedApprovals, getApprovalsByWriterId, getRequestedApprovals, getReceivedApprovalDetail, getRequestedApprovalDetail } from '@/services/approvalService';
import { useMemberStore } from '@/stores/memberStore';

export const useApprovalStore = defineStore('approval', () => {
    // State
    const receivedList = ref([]);
    const loadingReceived = ref(false);
    const errorReceived = ref(null);

    const sentList = ref([]);
    const loadingSent = ref(false);
    const errorSent = ref(null);

    const requestedList = ref([]);
    const loadingRequested = ref(false);
    const errorRequested = ref(null);

    const approvalDetail = ref(null);

    const loading = ref(false);
    const error = ref(null);

    // Actions
    const loadReceivedApprovals = async (memberId) => {
        loadingReceived.value = true;
        errorReceived.value = null;
        try {
            const response = await getReceivedApprovals(memberId);
            receivedList.value = response || [];
        } catch (e) {
            // 404 Not Found는 에러가 아닌 빈 목록으로 처리
            if (e.response && e.response.status === 404) {
                receivedList.value = [];
            } else {
                console.error('받은 결재 목록 조회 실패:', e);
                errorReceived.value = e;
            }
        } finally {
            loadingReceived.value = false;
        }
    };

    const loadSentApprovals = async (writerId) => {
        loadingSent.value = true;
        errorSent.value = null;
        try {
            const response = await getApprovalsByWriterId(writerId);
            sentList.value = response;
        } catch (e) {
            errorSent.value = e;
        } finally {
            loadingSent.value = false;
        }
    };

    const loadRequestedApprovals = async (memberId) => {
        loadingRequested.value = true;
        errorRequested.value = null;
        try {
            const response = await getRequestedApprovals(memberId);
            requestedList.value = response;
        } catch (e) {
            errorRequested.value = e;
        } finally {
            loadingRequested.value = false;
        }
    };

    const fetchReceivedApprovalDetail = async (approvalId) => {
        loading.value = true;
        error.value = null;
        try {
            const memberStore = useMemberStore();
            const memberId = memberStore.form.id;
            approvalDetail.value = await getReceivedApprovalDetail(approvalId, memberId);
        } catch (err) {
            error.value = err;
            console.error('요청받은 결재문서 상세 조회 실패:', err);
        } finally {
            loading.value = false;
        }
    };

    const fetchRequestedApprovalDetail = async (approvalId) => {
        loading.value = true;
        error.value = null;
        try {
            approvalDetail.value = await getRequestedApprovalDetail(approvalId);
        } catch (err) {
            error.value = err;
            console.error('요청한 결재문서 상세 조회 실패:', err);
        } finally {
            loading.value = false;
        }
    };

    const clearApprovalDetail = () => {
        approvalDetail.value = null;
    };

    // 데이터 초기화
    const reset = () => {
        receivedList.value = [];
        loadingReceived.value = false;
        errorReceived.value = null;
        
        sentList.value = [];
        loadingSent.value = false;
        errorSent.value = null;
        
        requestedList.value = [];
        loadingRequested.value = false;
        errorRequested.value = null;
        localStorage.removeItem('approval-store');
    };

    return {
        // List state
        receivedList,
        sentList,
        requestedList,
        
        // Detail state
        approvalDetail,
        
        // Common state
        loading,
        error,

        // List actions
        loadReceivedApprovals,
        loadSentApprovals,
        loadRequestedApprovals,

        // Detail actions
        fetchReceivedApprovalDetail,
        fetchRequestedApprovalDetail,
        clearApprovalDetail,

        // General actions
        reset
    };
}, {
    persist: {
        key: 'approval-store',
        storage: localStorage,
        paths: ['receivedList', 'sentList', 'requestedList']
    }
});