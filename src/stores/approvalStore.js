import { defineStore } from 'pinia';
import { getReceivedApprovals, getApprovalsByWriterId } from '@/services/approvalService';

export const useApprovalStore = defineStore('approval', {
    state: () => ({
        receivedList: [],
        loadingReceived: false,
        errorReceived: null,

        sentList: [],
        loadingSent: false,
        errorSent: null,
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
        },
        
        async loadSentApprovals(writerId) {
            this.loadingSent = true;
            this.errorSent = null;
            try {
                this.sentList = await getApprovalsByWriterId(writerId);
            } catch (e) {
                this.errorSent = e;
            } finally {
                this.loadingSent = false;
            }
        }
    }
});