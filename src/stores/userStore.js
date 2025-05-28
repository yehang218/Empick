// stores/userStore.js
import { defineStore } from 'pinia';
import { fetchUserWithFormat } from '@/services/userService';

export const useUserStore = defineStore('user', {
    state: () => ({
        user: null,
    }),
    actions: {
        async loadUser(id) {
            this.user = await fetchUserWithFormat(id);
        },
    },
});
