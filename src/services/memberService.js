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

export const getMemberRoleService = async (employeeNumber) => {
    try {
        const response = await api.get(API.MEMBER.ROLE(employeeNumber));
        return response.data;
    } catch (error) {
        console.error('회원 역할 조회 API 오류:', error)
        throw error;
    }
};

export const getMyRoleService = async () => {
    try {
        const response = await api.get(API.MEMBER.MY_ROLE);
        return response.data;
    } catch (error) {
        console.error('내 역할 조회 API 오류:', error)
        throw error;
    }
};

export const findMembersService = async (employeeNumber) => {
    try {
        console.log('🌐 findMembersService 호출:', { employeeNumber });
        console.log('🔗 API URL:', API.MEMBER.FIND_MEMBERS);

        const params = employeeNumber ? { employeeNumber } : {};
        console.log('📋 요청 파라미터:', params);

        const response = await api.get(API.MEMBER.FIND_MEMBERS, { params });

        console.log('✅ API 응답 상태:', response.status);
        console.log('📄 API 응답 헤더:', response.headers);
        console.log('📦 API 응답 데이터:', response.data);

        return response.data;
    } catch (error) {
        console.error('❌ 회원 조회 API 오류:', error);
        console.error('📊 오류 상세 정보:', {
            message: error.message,
            status: error.response?.status,
            statusText: error.response?.statusText,
            data: error.response?.data,
            config: error.config
        });

        throw error;
    }
};

// 페이지네이션된 사원 목록 조회 (서버사이드) - 향후 구현 예정
export const findMembersPaginatedService = async (page = 0, size = 10, sortBy = 'name', sortDir = 'asc', filters = {}) => {
    try {
        const params = {
            page: page,
            size: size,
            sortBy: sortBy,
            sortDir: sortDir,
            ...filters // search, department, status 등
        };

        const response = await api.get(API.MEMBER.PAGINATED || API.MEMBER.FIND_MEMBERS, { params });

        // 서버에서 페이지네이션 지원 시 이런 형태로 응답이 와야 함:
        // {
        //   content: [...],     // 현재 페이지 데이터
        //   totalElements: 100, // 전체 항목 수
        //   totalPages: 10,     // 전체 페이지 수
        //   number: 0,          // 현재 페이지 번호
        //   size: 10,           // 페이지 크기
        //   first: true,        // 첫 페이지 여부
        //   last: false         // 마지막 페이지 여부
        // }

        return response.data;
    } catch (error) {
        console.error('페이지네이션 조회 API 오류:', error);
        throw error;
    }
};