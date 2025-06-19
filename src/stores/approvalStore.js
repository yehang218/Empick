import { defineStore } from 'pinia';
import { getReceivedApprovals, getApprovalsByWriterId, getRequestedApprovals } from '@/services/approvalService';

export const useApprovalStore = defineStore('approval', {
    state: () => ({
        receivedList: [],
        loadingReceived: false,
        errorReceived: null,

        sentList: [],
        loadingSent: false,
        errorSent: null,

        // 요청한 결재 목록
        requestedList: [],
        loadingRequested: false,
        errorRequested: null,
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
        },

        async loadRequestedApprovals(memberId) {
            this.loadingRequested = true;
            this.errorRequested = null;
            try {
                this.requestedList = await getRequestedApprovals(memberId);
            } catch (e) {
                this.errorRequested = e;
            } finally {
                this.loadingRequested = false;
            }
        }
    }
});