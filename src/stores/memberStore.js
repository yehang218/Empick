import { defineStore } from 'pinia';
import {
    getMyInfoService,
    updateMyInfoService,
    profileImageFetchService,
    profileImageUploadService,
    getMyRoleService,
    getMemberRoleService,
    findMembersService
} from '@/services/memberService';
import MemberRoleDTO from '@/dto/member/memberRoleDTO';
import { MemberResponseDTO } from '@/dto/member/memberResponseDTO';

/**
 * 개별 사원 관리 Store
 * - 개인 프로필 관리 (내 정보 조회/수정)
 * - 사원 등록
 * - 프로필 이미지 관리
 * - 권한 관리
 * 
 * ⚠️ 사원 목록 관리는 useMemberList 컴포저블 사용
 */
export const useMemberStore = defineStore('member', {
    state: () => ({
        // 🧑‍💼 개인 프로필 관리
        form: {
            id: null,
            employeeNumber: null,
            name: '',
            email: '',
            phone: '',
            departmentName: '',
            positionName: '',
            jobName: '',
            rankName: '',
            pictureUrl: '',
            status: 0,
            birth: null,
            address: '',
            hireAt: null,
            resignAt: null
        },

        // 📝 사원 등록 관리
        registerResult: null,
        registerError: null,

        // 🔄 상태 관리
        loading: false,
        error: '',

        // 🖼️ 프로필 이미지 관리
        profileImageUrl: '',
        defaultProfileImageUrl: '/images/default-profile.png',
        profileImageCache: new Map(), // 프로필 이미지 캐시

        // 📋 기본 사원 목록 (캐싱용 - 단순 조회만)
        membersCache: [],
        membersCacheTimestamp: null,
        cacheExpiryTime: 5 * 60 * 1000, // 5분
    }),

    getters: {
        // 캐시가 유효한지 확인
        isCacheValid: (state) => {
            if (!state.membersCacheTimestamp || state.membersCache.length === 0) {
                return false
            }
            const now = Date.now()
            return (now - state.membersCacheTimestamp) < state.cacheExpiryTime
        },

        // 캐시된 사원 목록 반환 (단순 조회용)
        cachedMembers: (state) => state.membersCache,
    },

    actions: {
        // 🔄 상태 초기화
        reset() {
            this.form = {
                id: null,
                employeeNumber: null,
                name: '',
                email: '',
                phone: '',
                departmentName: '',
                positionName: '',
                jobName: '',
                rankName: '',
                pictureUrl: '',
                status: 0,
                birth: null,
                address: '',
                hireAt: null,
                resignAt: null
            };
            this.registerResult = null;
            this.registerError = null;
            this.loading = false;
            this.error = '';
            this.profileImageUrl = '';

            localStorage.removeItem('member-store');
        },

        // 🧑‍💼 내 정보 조회
        async getMyInfo() {
            this.loading = true;
            this.error = '';
            try {
                const response = await getMyInfoService();
                console.log('API 응답 데이터:', response);

                if (response && response.data && response.data.data) {
                    const result = response.data.data;
                    console.log('실제 데이터:', result);

                    this.form = {
                        id: result.id || null,
                        employeeNumber: result.employeeNumber || null,
                        name: result.name || '',
                        email: result.email || '',
                        phone: result.phone || '',
                        departmentName: result.departmentName || '',
                        positionName: result.positionName || '',
                        jobName: result.jobName || '',
                        rankName: result.rankName || '',
                        pictureUrl: result.pictureUrl || '',
                        status: result.status || 0,
                        birth: result.birth || null,
                        address: result.address || '',
                        hireAt: result.hireAt || null,
                        resignAt: result.resignAt || null
                    };
                    console.log('form 업데이트 결과:', this.form);
                }
                return response;
            } catch (err) {
                console.error('내 정보 조회 중 오류:', err);
                this.error = err.message || '내 정보 조회 중 오류가 발생했습니다.';
                throw err;
            } finally {
                this.loading = false;
            }
        },

        // ✏️ 내 정보 수정
        async updateMyInfo() {
            this.loading = true;
            this.error = '';
            try {
                // 입력값 검증
                if (!this.form.name?.trim()) {
                    throw new Error('이름을 입력해주세요.');
                }
                if (!/^010-\d{4}-\d{4}$/.test(this.form.phone)) {
                    throw new Error('올바른 연락처 형식이 아닙니다.');
                }
                if (!/.+@.+\..+/.test(this.form.email)) {
                    throw new Error('올바른 이메일 형식이 아닙니다.');
                }

                const result = await updateMyInfoService(this.form);
                if (result) {
                    Object.assign(this.form, result);
                }
                return result;
            } catch (err) {
                this.error = err.message || '수정 요청 중 오류가 발생했습니다.';
                throw err;
            } finally {
                this.loading = false;
            }
        },

        // 🖼️ 프로필 이미지 조회
        async fetchProfileImage(memberId) {
            try {
                // 캐시에서 먼저 확인
                const cacheKey = `profile_${memberId}`
                if (this.profileImageCache.has(cacheKey)) {
                    const cachedData = this.profileImageCache.get(cacheKey)
                    // 캐시가 1시간 이내인 경우 사용
                    if (Date.now() - cachedData.timestamp < 60 * 60 * 1000) {
                        console.log('캐시된 프로필 이미지 사용:', memberId)
                        this.profileImageUrl = cachedData.url
                        return cachedData.url
                    } else {
                        // 만료된 캐시 제거
                        URL.revokeObjectURL(cachedData.url)
                        this.profileImageCache.delete(cacheKey)
                    }
                }

                // API에서 이미지 가져오기
                console.log('API에서 프로필 이미지 조회:', memberId)
                const imageBlob = await profileImageFetchService(memberId)
                const imageUrl = URL.createObjectURL(imageBlob)

                // 캐시에 저장
                this.profileImageCache.set(cacheKey, {
                    url: imageUrl,
                    timestamp: Date.now()
                })

                this.profileImageUrl = imageUrl
                return imageUrl
            } catch (err) {
                console.warn('프로필 이미지 로드 실패:', err)
                this.profileImageUrl = this.defaultProfileImageUrl
                return this.defaultProfileImageUrl
            }
        },

        // 🗑️ 프로필 이미지 캐시 정리
        clearProfileImageCache() {
            for (const [, value] of this.profileImageCache.entries()) {
                URL.revokeObjectURL(value.url)
            }
            this.profileImageCache.clear()
        },

        // 📤 프로필 이미지 업로드
        async uploadProfileImage(memberId, formData) {
            try {
                const result = await profileImageUploadService(memberId, formData)

                // 업로드 성공 시 캐시 무효화
                const cacheKey = `profile_${memberId}`
                if (this.profileImageCache.has(cacheKey)) {
                    const cachedData = this.profileImageCache.get(cacheKey)
                    URL.revokeObjectURL(cachedData.url)
                    this.profileImageCache.delete(cacheKey)
                }

                return result
            } catch (err) {
                console.error('프로필 이미지 업로드 실패:', err)
                throw err
            }
        },

        // 🎭 권한 관리
        async getMyRole() {
            const response = await getMyRoleService();
            return response.map(role => MemberRoleDTO.fromJSON(role));
        },

        async getMemberRole(employeeNumber) {
            const response = await getMemberRoleService(employeeNumber);
            return response.map(role => MemberRoleDTO.fromJSON(role));
        },

        // 📋 기본 사원 목록 조회 (캐싱 지원)
        async findMembers(employeeNumber = null, forceRefresh = false) {
            console.log('findMembers 호출:', { employeeNumber, forceRefresh });

            // 캐시 확인 (forceRefresh가 false이고 캐시가 유효한 경우)
            if (!forceRefresh && this.isCacheValid && !employeeNumber) {
                console.log('캐시된 사원 목록 반환:', this.membersCache.length, '명');
                return this.membersCache;
            }

            // API에서 데이터 가져오기
            const members = await this.fetchMembersFromAPI(employeeNumber);

            // 전체 목록인 경우에만 캐시 저장
            if (!employeeNumber) {
                this.membersCache = members;
                this.membersCacheTimestamp = Date.now();
                console.log('사원 목록 캐시 저장:', members.length, '명');
            }

            return members;
        },

        // 🌐 API에서 사원 목록 가져오기
        async fetchMembersFromAPI(employeeNumber = null) {
            try {
                console.log('API에서 사원 목록 조회:', { employeeNumber });
                const response = await findMembersService(employeeNumber);
                console.log('API 응답 전체:', response);

                let members = [];

                // API 응답 구조 분석 및 처리
                if (response) {
                    let rawData = null;

                    // 다양한 응답 구조 처리
                    if (response.data) {
                        // Case 1: { success: true, data: [...] }
                        if (response.data.data) {
                            rawData = response.data.data;
                        }
                        // Case 2: { data: [...] } 
                        else if (Array.isArray(response.data)) {
                            rawData = response.data;
                        }
                        // Case 3: response.data가 객체인 경우
                        else if (typeof response.data === 'object') {
                            rawData = response.data;
                        }
                    }
                    // Case 4: response 자체가 배열인 경우
                    else if (Array.isArray(response)) {
                        rawData = response;
                    }

                    console.log('추출된 rawData:', rawData);

                    if (rawData) {
                        // 배열로 변환
                        const rawMembers = Array.isArray(rawData) ? rawData : [rawData];
                        members = rawMembers.map(member => {
                            console.log('변환할 사원 데이터:', member);
                            return MemberResponseDTO.fromJSON(member);
                        });
                    }
                }

                console.log('변환된 사원 목록:', members.length, '명');
                return members;
            } catch (error) {
                console.error('사원 목록 조회 실패:', error);
                console.error('에러 상세:', error.response?.data || error.message);
                throw error;
            }
        },

        // 🗑️ 캐시 무효화
        invalidateMembersCache() {
            console.log('사원 목록 캐시 무효화');
            this.membersCache = [];
            this.membersCacheTimestamp = null;
        }
    },
}, {
    persist: {
        key: 'member-store',
        storage: localStorage,
        paths: ['form']
    }
});
