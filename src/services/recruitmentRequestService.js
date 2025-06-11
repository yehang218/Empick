import api from '@/apis/apiClient';
import { API } from '@/apis/routes';

export const fetchRecruitmentRequests = async () => {
    const response = await api.get(API.RECRUITMENT_REQUEST.LIST);
    return response.data.result;
};

export const fetchRecruitmentRequestDetail = async (id) => {
    const response = await api.get(API.RECRUITMENT_REQUEST.DETAIL(id));
    return response.data.result;
};
