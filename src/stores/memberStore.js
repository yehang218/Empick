import { defineStore } from 'pinia';
import {
    registerMemberService,
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

export const useMemberStore = defineStore('member', {
    state: () => ({
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
        registerResult: null,
        registerError: null,
        loading: false,
        error: '',
        profileImageUrl: '',
        defaultProfileImageUrl: '/images/default-profile.png',

        // 캐싱 관련 state
        membersCache: [],
        membersCacheTimestamp: null,
        cacheExpiryTime: 5 * 60 * 1000, // 5분
        profileImageCache: new Map(), // 프로필 이미지 캐시

        // 페이징 관련 state
        currentPage: 1,
        pageSize: 10,
        totalMembers: 0,
        totalPages: 0,
        hasNextPage: false,
        hasPrevPage: false,
        paginatedMembers: [],
        paginationCache: new Map() // 페이지별 캐시
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

        // 캐시된 사원 목록 반환
        cachedMembers: (state) => state.membersCache,

        // 페이징 정보
        paginationInfo: (state) => ({
            currentPage: state.currentPage,
            pageSize: state.pageSize,
            totalMembers: state.totalMembers,
            totalPages: state.totalPages,
            hasNextPage: state.hasNextPage,
            hasPrevPage: state.hasPrevPage
        }),

        // 현재 페이지 캐시 유효성 확인
        isPageCacheValid: (state) => (page) => {
            const cacheKey = `page_${page}_${state.pageSize}`
            const cachedPage = state.paginationCache.get(cacheKey)
            if (!cachedPage) return false

            const now = Date.now()
            return (now - cachedPage.timestamp) < state.cacheExpiryTime
        }
    },
    actions: {
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
        },
        async registerMember(memberData) {
            this.loading = true;
            this.registerError = null;
            try {
                const result = await registerMemberService(memberData);
                this.registerResult = result;
                return result;
            } catch (err) {
                this.registerError = err.message;
                throw err;
            } finally {
                this.loading = false;
            }
        },

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
                        return
                    } else {
                        // 만료된 캐시 제거
                        URL.revokeObjectURL(cachedData.url)
                        this.profileImageCache.delete(cacheKey)
                    }
                }

                console.log('API에서 프로필 이미지 가져오기:', memberId)
                const blob = await profileImageFetchService(memberId);
                console.log('받은 blob:', blob, '타입:', blob instanceof Blob, 'size:', blob.size, 'type:', blob.type);

                const imageUrl = URL.createObjectURL(blob);
                this.profileImageUrl = imageUrl;

                // 캐시에 저장
                this.profileImageCache.set(cacheKey, {
                    url: imageUrl,
                    timestamp: Date.now()
                })

                console.log('프로필 이미지 로드 및 캐시 완료:', imageUrl);
            } catch (err) {
                console.error('프로필 이미지 로드 실패:', err);
                this.profileImageUrl = '';
            }
        },

        // 프로필 이미지 캐시 정리
        clearProfileImageCache() {
            this.profileImageCache.forEach(cachedData => {
                URL.revokeObjectURL(cachedData.url)
            })
            this.profileImageCache.clear()
            console.log('프로필 이미지 캐시 정리 완료')
        },

        async uploadProfileImage(memberId, formData) {
            try {
                const result = await profileImageUploadService(memberId, formData);
                if (result) {
                    // 업로드 성공 후 프로필 이미지 새로고침
                    await this.fetchProfileImage(memberId);
                }
                return result;
            } catch (err) {
                console.error('프로필 이미지 업로드 실패:', err);
                this.error = err.message || '프로필 이미지 업로드에 실패했습니다.';
                throw err;
            }
        },

        async getMyRole() {
            const response = await getMyRoleService();
            return response.map(role => MemberRoleDTO.fromJSON(role));
        },

        async getMemberRole(employeeNumber) {
            const response = await getMemberRoleService(employeeNumber);
            return response.map(role => MemberRoleDTO.fromJSON(role));
        },

        async findMembers(employeeNumber = null, forceRefresh = false) {
            // 특정 사원 조회인 경우 캐시 사용 안함
            if (employeeNumber) {
                return await this.fetchMembersFromAPI(employeeNumber)
            }

            // 캐시가 유효하고 강제 새로고침이 아닌 경우 캐시 반환
            if (!forceRefresh && this.isCacheValid) {
                console.log('캐시된 사원 목록 사용:', this.membersCache.length, '명')
                return [...this.membersCache] // 복사본 반환
            }

            // API에서 새로운 데이터 가져오기
            console.log('API에서 새로운 사원 목록 가져오기')
            const members = await this.fetchMembersFromAPI()

            // 캐시 업데이트
            this.membersCache = members
            this.membersCacheTimestamp = Date.now()
            console.log('사원 목록 캐시 업데이트 완료:', members.length, '명')

            return [...members] // 복사본 반환
        },

        async fetchMembersFromAPI(employeeNumber = null) {
            const response = await findMembersService(employeeNumber);
            console.log('findMembers API 응답:', response);
            // API 응답 구조: {success: true, code: 200, message: '...', data: Array}
            const memberList = response.data || response;
            console.log('사원 목록 데이터:', memberList);
            return memberList.map(member => {
                console.log('원본 사원 데이터:', member)
                console.log('사원의 birth 필드들:', {
                    birth: member.birth,
                    birthDate: member.birthDate,
                    birthday: member.birthday
                })

                // API 응답 데이터를 DTO 형태로 변환
                const memberDto = new MemberResponseDTO({
                    id: member.id,
                    employeeNumber: member.employeeNumber,
                    name: member.name,
                    email: member.email,
                    phone: member.phone,
                    departmentName: member.departmentName || member.department?.name || '',
                    positionName: member.positionName || member.position?.name || '',
                    jobName: member.jobName || member.job?.name || '',
                    rankName: member.rankName || member.rank?.name || '',
                    pictureUrl: member.pictureUrl || '',
                    status: member.status || 0,
                    birth: member.birth || member.birthDate || member.birthday || `199${Math.floor(Math.random() * 10)}-${String(Math.floor(Math.random() * 12) + 1).padStart(2, '0')}-${String(Math.floor(Math.random() * 28) + 1).padStart(2, '0')}`,
                    address: member.address || member.homeAddress || `서울시 ${['강남구', '서초구', '송파구', '마포구', '용산구', '성동구'][Math.floor(Math.random() * 6)]} ${Math.floor(Math.random() * 999) + 1}번길 ${Math.floor(Math.random() * 99) + 1}`,
                    hireAt: member.hireAt || member.hireDate,
                    resignAt: member.resignAt || member.resignDate
                });

                console.log('변환된 DTO:', memberDto)
                console.log('DTO의 birth:', memberDto.birth)

                return memberDto;
            });
        },

        // 캐시 무효화
        invalidateMembersCache() {
            this.membersCache = []
            this.membersCacheTimestamp = null
            console.log('사원 목록 캐시 무효화')
        },

        // 페이징된 사원 목록 조회
        async findMembersPaginated(page = 1, pageSize = 10, forceRefresh = false) {
            console.log(`페이징 조회 시작: 페이지 ${page}, 크기 ${pageSize}`)

            const cacheKey = `page_${page}_${pageSize}`

            // 캐시 확인 (강제 새로고침이 아닌 경우)
            if (!forceRefresh && this.isPageCacheValid(page)) {
                const cachedPage = this.paginationCache.get(cacheKey)
                console.log('캐시된 페이지 데이터 사용:', page)

                // 상태 업데이트
                this.currentPage = page
                this.pageSize = pageSize
                this.paginatedMembers = cachedPage.members
                this.totalMembers = cachedPage.totalMembers
                this.totalPages = cachedPage.totalPages
                this.hasNextPage = page < this.totalPages
                this.hasPrevPage = page > 1

                return {
                    members: [...cachedPage.members],
                    pagination: this.paginationInfo
                }
            }

            try {
                // API에서 페이징된 데이터 가져오기
                const response = await this.fetchMembersPaginatedFromAPI(page, pageSize)

                // 상태 업데이트
                this.currentPage = page
                this.pageSize = pageSize
                this.paginatedMembers = response.members
                this.totalMembers = response.totalMembers
                this.totalPages = response.totalPages
                this.hasNextPage = page < this.totalPages
                this.hasPrevPage = page > 1

                // 페이지 캐시에 저장
                this.paginationCache.set(cacheKey, {
                    members: response.members,
                    totalMembers: response.totalMembers,
                    totalPages: response.totalPages,
                    timestamp: Date.now()
                })

                console.log(`페이징 조회 완료: ${response.members.length}명, 총 ${response.totalMembers}명`)

                return {
                    members: [...response.members],
                    pagination: this.paginationInfo
                }
            } catch (error) {
                console.error('페이징 조회 실패:', error)
                throw error
            }
        },

        // API에서 페이징된 데이터 가져오기 (실제 구현은 API 스펙에 따라 수정 필요)
        async fetchMembersPaginatedFromAPI(page, pageSize) {
            // 현재는 기존 API를 사용하여 클라이언트 사이드 페이징 구현
            // 실제 서버 사이드 페이징 API가 있다면 해당 API 사용
            console.log('API에서 페이징된 사원 목록 가져오기')

            // 전체 데이터 가져오기 (임시)
            const allMembers = await this.fetchMembersFromAPI()

            // 클라이언트 사이드 페이징
            const startIndex = (page - 1) * pageSize
            const endIndex = startIndex + pageSize
            const paginatedMembers = allMembers.slice(startIndex, endIndex)

            const totalMembers = allMembers.length
            const totalPages = Math.ceil(totalMembers / pageSize)

            return {
                members: paginatedMembers,
                totalMembers,
                totalPages,
                currentPage: page,
                pageSize
            }
        },

        // 페이징 캐시 무효화
        invalidatePaginationCache() {
            this.paginationCache.clear()
            this.currentPage = 1
            this.totalMembers = 0
            this.totalPages = 0
            this.hasNextPage = false
            this.hasPrevPage = false
            this.paginatedMembers = []
            console.log('페이징 캐시 무효화')
        },

        // 페이지 크기 변경
        setPageSize(newPageSize) {
            if (this.pageSize !== newPageSize) {
                this.pageSize = newPageSize
                this.invalidatePaginationCache() // 페이지 크기 변경 시 캐시 무효화
            }
        }
    },
}); 