import api from '@/apis/apiClient';
import { API } from '@/apis/routes';

export const createMailService = async (mailData) => {
    try {
        const response = await api.post(API.MAIL.CREATE, mailData);
        return response.data;
    } catch (error) {
        throw error;
    }
};