// stores/approvalFormStore.js
import { defineStore } from 'pinia';
import { ref } from 'vue';
import { CreateApprovalDTO } from '@/dto/approval/approval/createApprovalDTO';

export const useApprovalFormStore = defineStore('approvalForm', () => {
    const form = ref(new CreateApprovalDTO());

    const resetForm = () => {
        form.value = new CreateApprovalDTO();
    };

    return { form, resetForm };
});
