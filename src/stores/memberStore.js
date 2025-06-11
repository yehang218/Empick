import { defineStore } from 'pinia';
import { registerMemberService } from '@/services/memberService';

export const useMemberStore = defineStore('member', {
    state: () => ({
        registerResult: null,
        registerError: null,
        loading: false,
    }),
    actions: {
        async registerMember(memberData) {
            this.loading = true;
            this.registerError = null;
            try {
                const result = await registerMemberService(memberData);
                this.registerResult = result;
                return result;
            } catch (err) {
                this.registerError = err.message;
                throw err;
            } finally {
                this.loading = false;
            }
        },
    },
}); 