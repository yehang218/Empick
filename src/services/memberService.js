import api from '@/apis/apiClient';
import { API } from '@/apis/routes';
import { MemberAPI } from '@/apis/routes/member';
import { withErrorHandling } from '@/utils/errorHandler';
import ApiResponseDTO from '@/dto/common/apiResponseDTO';

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
        console.log('내 정보 조회 API 호출 시작')
        const response = await api.get(API.MEMBER.ME);
        console.log('내 정보 조회 API 응답 전체:', response);
        console.log('내 정보 조회 API 응답 데이터:', response.data);
        return response;
    } catch (error) {
        console.error('내 정보 조회 API 오류:', error)
        throw error;
    }
};

export const updateMyInfoService = async (memberData) => {
    try {
        console.log('내 정보 수정 API 호출 시작:', memberData)
        const response = await api.put(API.MEMBER.ME, memberData);
        console.log('내 정보 수정 API 응답:', response.data)
        return response.data;
    } catch (error) {
        console.error('내 정보 수정 API 오류:', error)
        throw error;
    }
};

/**
 * 프로필 이미지 조회 서비스 (반드시 responseType: 'blob'으로 요청!)
 * @returns {Promise<Blob>} 프로필 이미지 blob
 */
export const profileImageFetchService = async (memberId) => {
    try {
        const response = await api.get(API.MEMBER.PROFILE_IMAGE(memberId), {
            responseType: 'blob' // 반드시 blob으로!
        });
        console.log('프로필 이미지 조회 API 응답:', response);
        return response.data; // 이게 Blob 객체여야 함
    } catch (error) {
        console.error('프로필 이미지 조회 API 오류:', error)
        throw error;
    }
};

export const profileImageUploadService = async (memberId, formData) => {
    try {
        const response = await api.post(API.MEMBER.PROFILE_IMAGE(memberId), formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        });
        return response.data;
    } catch (error) {
        console.error('프로필 이미지 업로드 API 오류:', error)
        throw error;
    }
};

export const getMemberList = async (params) => {
    return withErrorHandling(async () => {
        const response = await MemberAPI.getMemberList(params);
        return ApiResponseDTO.fromJSON(response.data);
    });
};

export const getMemberDetail = async (memberId) => {
    return withErrorHandling(async () => {
        const response = await MemberAPI.getMemberDetail(memberId);
        return ApiResponseDTO.fromJSON(response.data);
    });
};

export const updateMember = async (memberId, memberData) => {
    return withErrorHandling(async () => {
        const response = await MemberAPI.updateMember(memberId, memberData);
        return ApiResponseDTO.fromJSON(response.data);
    });
};

export const updateMemberProfile = async (memberId, profileData) => {
    return withErrorHandling(async () => {
        const response = await MemberAPI.updateMemberProfile(memberId, profileData);
        return ApiResponseDTO.fromJSON(response.data);
    });
};

export const deleteMember = async (memberId) => {
    return withErrorHandling(async () => {
        const response = await MemberAPI.deleteMember(memberId);
        return ApiResponseDTO.fromJSON(response.data);
    });
};