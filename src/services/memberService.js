import api from '@/apis/apiClient';
import { API } from '@/apis/routes';

/**
 * 신규 사원 등록 서비스
 * @param {Object} memberData - 사원 등록 데이터
 * @returns {Promise<Object>} 등록 결과
 */
export const registerMemberService = async (memberData) => {
    try {
        const response = await api.post(API.MEMBER.REGISTER, memberData);
        return response.data;
    } catch (error) {
        if (error.response) {
            throw new Error(error.response.data?.message || '사원 등록 중 오류가 발생했습니다.');
        }
        throw error;
    }
}; 