import api from '@/apis/apiClient';
import { API } from '@/apis/routes';
import MemberSignUpRequestDTO from '@/dto/member/memberSignUpRequestDTO'; // DTO import 추가

/**
 * 신규 사원 등록 서비스 (프로필 이미지 포함)
 * @param {Object} memberData - 사원 등록 데이터
 * @param {File} profileImage - 프로필 이미지 파일
 * @returns {Promise<Object>} 등록 결과
 */
export const registerMemberService = async (memberData, profileImage) => {
    try {
        // 프로필 이미지 필수 검증
        if (!profileImage) {
            throw new Error('프로필 이미지는 필수입니다.');
        }

        // 파일 타입 검증
        const allowedTypes = ['image/jpeg', 'image/jpg', 'image/png', 'image/webp'];
        if (!allowedTypes.includes(profileImage.type)) {
            throw new Error('JPG, PNG, WEBP 형식의 이미지만 업로드 가능합니다.');
        }

        // 파일 크기 검증 (5MB 이하)
        const maxSize = 5 * 1024 * 1024; // 5MB
        if (profileImage.size > maxSize) {
            throw new Error('프로필 이미지는 5MB 이하만 업로드 가능합니다.');
        }

        // DTO 객체 생성 및 데이터 정제
        const memberDto = new MemberSignUpRequestDTO(memberData);
        const cleanedData = memberDto.toFormData();

        // FormData 생성
        const formData = new FormData();

        // 정제된 사원 정보 추가
        Object.keys(cleanedData).forEach(key => {
            const value = cleanedData[key];
            if (value !== null && value !== undefined && value !== '') {
                formData.append(key, value);
            }
        });

        // 프로필 이미지 추가
        formData.append('profileImage', profileImage);

        console.log('사원 등록 API 호출:', {
            originalData: memberData,
            cleanedData: cleanedData,
            profileImage: profileImage?.name,
            formDataEntries: Array.from(formData.entries())
        });

        const response = await api.post(API.MEMBER.REGISTER, formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            },
            timeout: 60000, // 파일 업로드를 위해 60초 타임아웃 설정
            onUploadProgress: (progressEvent) => {
                if (progressEvent.total) {
                    const percentCompleted = Math.round((progressEvent.loaded * 100) / progressEvent.total);
                    console.log(`업로드 진행률: ${percentCompleted}%`);
                }
            }
        });

        console.log('사원 등록 API 응답:', response.data);
        return response.data;
    } catch (error) {
        console.error('사원 등록 API 오류:', error);

        // 타임아웃 에러 처리
        if (error.code === 'ECONNABORTED' && error.message.includes('timeout')) {
            throw new Error('네트워크 연결이 불안정하거나 서버 응답이 지연되고 있습니다. 잠시 후 다시 시도해주세요.');
        }

        // 서버 응답 에러
        if (error.response) {
            const status = error.response.status;
            const message = error.response.data?.message;

            if (status >= 500) {
                throw new Error(message || '서버 내부 오류가 발생했습니다. 관리자에게 문의해주세요.');
            } else if (status >= 400) {
                throw new Error(message || '요청 데이터에 문제가 있습니다. 입력 정보를 확인해주세요.');
            }
        }

        // 네트워크 에러
        if (error.request) {
            throw new Error('서버와 연결할 수 없습니다. 네트워크 상태를 확인해주세요.');
        }

        throw new Error(error.message || '사원 등록 중 알 수 없는 오류가 발생했습니다.');
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

// 사원 정보 수정 요청 생성 (승인 시스템)
export const createMemberEditProposalService = async (proposalData) => {
    try {
        console.log('🔄 사원 정보 수정 요청 생성 API 호출 시작:', {
            memberId: proposalData.memberId,
            targetField: proposalData.targetField,
            originalValue: proposalData.originalValue,
            requestedValue: proposalData.requestedValue,
            reason: proposalData.reason
        })

        const response = await api.post(API.MEMBER.EDIT_PROPOSALS, proposalData);

        console.log('✅ 사원 정보 수정 요청 생성 API 응답:', response.data)
        return response.data;
    } catch (error) {
        console.error('❌ 사원 정보 수정 요청 생성 API 오류:', {
            status: error.response?.status,
            statusText: error.response?.statusText,
            data: error.response?.data,
            targetField: proposalData.targetField
        })

        // 409 에러(중복 요청) 처리
        if (error.response && error.response.status === 409) {
            const errorMessage = error.response.data?.message || '같은 필드에 대해 이미 대기중인 수정 요청이 존재합니다.';
            const fieldName = {
                'NAME': '이름',
                'PHONE': '연락처',
                'EMAIL': '이메일',
                'ADDRESS': '주소'
            }[proposalData.targetField] || proposalData.targetField;

            throw new Error(`${fieldName} 수정 요청 실패: ${errorMessage}`);
        }

        throw error;
    }
};

// 대기중인 사원 정보 수정 요청 조회
export const getMemberEditProposalsService = async (memberId) => {
    try {
        console.log('사원 정보 수정 요청 조회 API 호출 시작:', memberId)
        const response = await api.get(API.MEMBER.EDIT_PROPOSALS, {
            params: { memberId }
        });
        console.log('사원 정보 수정 요청 조회 API 응답:', response.data)
        return response.data;
    } catch (error) {
        console.error('사원 정보 수정 요청 조회 API 오류:', error)
        throw error;
    }
};

// 내 정보 직접 수정 (승인 시스템 우회)
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

export const profileImageUploadService = async (memberId, formDataOrFile) => {
    try {
        let formData;
        let fileInfo = {};

        // FormData 또는 File 객체 처리
        if (formDataOrFile instanceof FormData) {
            formData = formDataOrFile;
            // FormData에서 파일 정보 추출 (로깅용)
            const file = formData.get('file');
            if (file && file instanceof File) {
                fileInfo = {
                    fileName: file.name,
                    size: file.size,
                    type: file.type
                };
            }
        } else if (formDataOrFile instanceof File) {
            // File 객체인 경우 FormData 생성
            const file = formDataOrFile;

            // 파일 타입 검증
            const allowedTypes = ['image/jpeg', 'image/jpg', 'image/png', 'image/webp'];
            if (!allowedTypes.includes(file.type)) {
                throw new Error('JPG, PNG, WEBP 형식의 이미지만 업로드 가능합니다.');
            }

            // 파일 크기 검증 (5MB 이하)
            const maxSize = 5 * 1024 * 1024; // 5MB
            if (file.size > maxSize) {
                throw new Error('프로필 이미지는 5MB 이하만 업로드 가능합니다.');
            }

            formData = new FormData();
            formData.append('file', file);

            fileInfo = {
                fileName: file.name,
                size: file.size,
                type: file.type
            };
        } else {
            throw new Error('프로필 이미지는 필수입니다.');
        }

        console.log('프로필 이미지 업로드 API 호출:', {
            memberId,
            ...fileInfo
        });

        const response = await api.put(API.MEMBER.PROFILE_IMAGE(memberId), formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            },
            timeout: 60000 // 파일 업로드를 위해 60초 타임아웃 설정
        });

        console.log('프로필 이미지 업로드 성공:', response.data);
        return response.data;
    } catch (error) {
        console.error('프로필 이미지 업로드 API 오류:', error);
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

// TODO: 프로필 이미지 업데이트 service 필요

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

export const findMemberByIdService = async (id) => {
    try {
        const response = await api.get(API.MEMBER.FIND_MEMBER_BY_ID(id));
        return response.data;
    } catch (error) {
        console.error('회원 조회 API 오류:', error)
        throw error;
    }
};