import { defineStore } from 'pinia';
import { uploadFileService } from '@/services/fileService';

export const useFileStore = defineStore('file', {
    state: () => ({
        uploadResult: null,
        uploadError: null,
        loading: false,
    }),
    actions: {
        async uploadProfileImage(file, prefix = 'profiles/', fileName = 'profile.png') {
            this.loading = true;
            this.uploadError = null;
            try {
                const result = await uploadFileService(file, prefix, fileName);
                this.uploadResult = result;
                return result;
            } catch (err) {
                this.uploadError = err.message;
                throw err;
            } finally {
                this.loading = false;
            }
        },
    },
}); 