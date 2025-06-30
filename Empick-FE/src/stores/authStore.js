import { defineStore } from 'pinia';
import { ref, nextTick } from 'vue';
import { loginService, logoutService } from '@/services/authService';
import { useRouter } from 'vue-router';
import { useMemberStore } from '@/stores/memberStore'
import { useApprovalStore } from '@/stores/approvalStore'
import { useAttendanceStore } from '@/stores/attendanceStore'
import { setLoggingOut } from '@/utils/errorHandler';

import { jwtDecode } from 'jwt-decode';

export const useAuthStore = defineStore('auth', () => {
    const router = useRouter();
    const memberStore = useMemberStore();

    // 상태 정의
    const isAuthenticated = ref(false);
    const accessToken = ref('');
    const refreshToken = ref('');
    const userInfo = ref(null);
    const error = ref(null);
    const loading = ref(false);

    // 공통 상태 초기화 함수
    const clearAuthState = async () => {
        console.log('인증 상태 초기화 시작');

        // Vue의 반응성 시스템과의 충돌을 방지하기 위해 순차적으로 초기화
        isAuthenticated.value = false;
        await nextTick();

        accessToken.value = '';
        refreshToken.value = '';
        userInfo.value = null;

        // 로컬 스토리지 정리
        localStorage.removeItem('auth_tokens');
        localStorage.removeItem('auth-store');
        localStorage.removeItem('empick-linked-approvals');

        // nextTick 후에 다른 스토어들 초기화
        await nextTick();

        try {
            // 스토어 초기화 - 오류가 발생해도 계속 진행
            useMemberStore().reset();
        } catch (error) {
            console.warn('MemberStore 초기화 실패:', error);
        }

        try {
            useApprovalStore().reset();
        } catch (error) {
            console.warn('ApprovalStore 초기화 실패:', error);
        }

        try {
            useAttendanceStore().resetAllData();
        } catch (error) {
            console.warn('AttendanceStore 초기화 실패:', error);
        }

        console.log('인증 상태 초기화 완료');
    };

    // 로그인 액션
    const login = async (loginRequest) => {
        console.log('authStore.login 시작:', loginRequest);
        loading.value = true;
        error.value = null;

        try {
            console.log('loginService 호출');
            const response = await loginService(loginRequest);
            console.log('loginService 응답:', response);

            accessToken.value = response.accessToken;
            refreshToken.value = response.refreshToken;
            isAuthenticated.value = true;

            // 토큰을 로컬 스토리지에 저장
            localStorage.setItem('auth_tokens', JSON.stringify({
                accessToken: response.accessToken,
                refreshToken: response.refreshToken
            }));

            // 토큰에서 roles 추출
            const decoded = jwtDecode(response.accessToken);
            let roles = decoded.roles || [];
            if (typeof roles === 'string') roles = [roles];

            console.log("roles : " + roles);
            console.log('response.user', response.user);
            console.log('decoded.roles', decoded.roles);
            console.log('userInfo.value (최종)', userInfo.value);

            if (response.user) {
                userInfo.value = {
                    ...response.user,
                    roles
                };
                memberStore.setUser(response.user);
            } else {
                userInfo.value = { roles };
                memberStore.setUser(null);
            }

            // 멤버 정보 및 결재문서 자동 로딩
            await memberStore.getMyInfo();
            const approvalStore = useApprovalStore();
            if (memberStore.form.id) {
                await approvalStore.loadReceivedApprovals(memberStore.form.id);
                await approvalStore.loadRequestedApprovals(memberStore.form.id);
            }

            console.log('로그인 성공, 토큰 저장 완료');

            // 새 사용자 로그인 시 이전 데이터 초기화
            useAttendanceStore().resetAllData();

            // 로그인 성공 후 대시보드로 이동
            router.push({ name: 'MainPage' });
            setLoggingOut(false);
        } catch (err) {
            console.error('로그인 에러:', err);
            error.value = err?.response?.data?.message || '로그인 실패';
            isAuthenticated.value = false;
            accessToken.value = '';
            refreshToken.value = '';
            userInfo.value = null;
            memberStore.setUser(null);
        } finally {
            loading.value = false;
        }
    };

    // 로컬 로그아웃 (API 호출 없이 상태만 정리)
    const logoutLocal = async () => {
        console.log('로컬 로그아웃 처리 시작');
        setLoggingOut(true);

        await clearAuthState();

        // 로그인 페이지로 이동
        router.push({ name: 'LoginPage' });

        // nextTick 후 플래그 해제
        await nextTick();
        Promise.resolve().then(() => {
            setLoggingOut(false);
            localStorage.removeItem('isLoggingOut');
        });
    };

    // 완전 로그아웃 (API 호출 포함)
    const logout = async () => {
        // 이미 로그아웃 중이면 중복 실행 방지
        const isCurrentlyLoggingOut = localStorage.getItem('isLoggingOut');
        if (isCurrentlyLoggingOut === 'true') {
            console.log('이미 로그아웃 진행 중입니다.');
            return;
        }

        // 로그아웃 시작 플래그 설정
        setLoggingOut(true);
        localStorage.setItem('isLoggingOut', 'true');

        loading.value = true;
        error.value = null;

        try {
            // 서버에 로그아웃 요청
            await logoutService();
            console.log('서버 로그아웃 성공');
        } catch (err) {
            console.warn('서버 로그아웃 실패, 로컬 상태만 정리:', err);
            // 서버 로그아웃이 실패해도 로컬 상태는 정리
        } finally {
            // 상태 초기화
            await clearAuthState();

            // 로그인 페이지로 이동
            router.push({ name: 'LoginPage' });

            loading.value = false;
            localStorage.removeItem('isLoggingOut');
            setLoggingOut(false);
        }
    };

    // 토큰으로 인증 헤더 생성
    const getAuthHeaders = () => {
        return {
            Authorization: `Bearer ${accessToken.value}`,
        };
    };

    // 앱 시작 시 토큰 복원
    const restoreAuth = () => {
        const tokens = localStorage.getItem('auth_tokens');
        if (tokens) {
            const { accessToken: storedAccessToken, refreshToken: storedRefreshToken } = JSON.parse(tokens);
            accessToken.value = storedAccessToken;
            refreshToken.value = storedRefreshToken;
            isAuthenticated.value = true;

            // roles 복원
            const decoded = jwtDecode(storedAccessToken);
            const roles = decoded.roles || [];

            userInfo.value = {
                ...userInfo.value,
                roles
            };
        }
    };

    return {
        isAuthenticated,
        accessToken,
        refreshToken,
        userInfo,
        error,
        loading,
        login,
        logout,
        logoutLocal,
        getAuthHeaders,
        restoreAuth
    };
});
