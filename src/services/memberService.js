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

/**
 * 내 정보 조회 서비스
 * @returns {Promise<Object>} 내 정보
 */
export const getMyInfoService = async () => {
    try {
        const response = await api.get(API.MEMBER.ME);
        return response.data;
    } catch (error) {
        throw error;
    }
};

export const updateMyInfoService = async (memberData) => {
    try {
        const response = await api.put(API.MEMBER.ME, memberData);
        return response.data;
    } catch (error) {
        throw error;
    }
};

export const profileImageFetchService = async (memberId) => {
    try {
        const response = await api.get(API.MEMBER.PROFILE_IMAGE(memberId));
        return response.data;
    } catch (error) {
        throw error;
    }
};