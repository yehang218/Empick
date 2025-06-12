import { defineStore } from 'pinia'
import { createMailService } from '@/services/mailService'

export const useMailStore = defineStore('mail', {
    state: () => ({
        loading: false,
    }),
    actions: {
        async createMail(mailData) {
            this.loading = true
            try {
                await createMailService(mailData)
            } finally {
                this.loading = false
            }
        }
    }
});