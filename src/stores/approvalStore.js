import { defineStore } from 'pinia';
import { getReceivedApprovals } from '@/services/approvalService';

export const useApprovalStore = defineStore('approval', {
    state: () => ({
        receivedList: [],
        loadingReceived: false,
        errorReceived: null,
    }),
    actions: {
        async loadReceivedApprovals(memberId) {
            this.loadingReceived = true;
            this.errorReceived = null;
            try {
                this.receivedList = await getReceivedApprovals(memberId);
            } catch (e) {
                this.errorReceived = e;
            } finally {
                this.loadingReceived = false;
            }
        }
    }
});