import { defineStore } from 'pinia';
import { ref } from 'vue';
import { getReceivedApprovals, getApprovalsByWriterId, getRequestedApprovals } from '@/services/approvalService';

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

    // Actions
    const loadReceivedApprovals = async (memberId) => {
        loadingReceived.value = true;
        errorReceived.value = null;
        try {
            const response = await getReceivedApprovals(memberId);
            receivedList.value = response;
        } catch (e) {
            errorReceived.value = e;
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
    };

    return {
        receivedList,
        loadingReceived,
        errorReceived,
        sentList,
        loadingSent,
        errorSent,
        requestedList,
        loadingRequested,
        errorRequested,
        loadReceivedApprovals,
        loadSentApprovals,
        loadRequestedApprovals,
        reset
    };
}, {
    persist: {
        key: 'approval-store',
        storage: localStorage,
        paths: ['receivedList', 'sentList', 'requestedList']
    }
});