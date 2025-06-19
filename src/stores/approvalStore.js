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
            receivedList.value = await getReceivedApprovals(memberId);
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
            sentList.value = await getApprovalsByWriterId(writerId);
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
            requestedList.value = await getRequestedApprovals(memberId);
        } catch (e) {
            errorRequested.value = e;
        } finally {
            loadingRequested.value = false;
        }
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
        loadRequestedApprovals
    };
});