import { computed } from 'vue'
import { useAuthStore } from '@/stores/authStore'
import { RoleCode } from '@/constants/common/RoleCode'

/**
 * 인증 및 권한 관리 컴포저블
 * 사용자 권한 체크, 인증 상태 관리를 담당
 */
export const useAuth = () => {
    const authStore = useAuthStore()

    // 🔐 기본 인증 상태
    const isAuthenticated = computed(() => authStore.isAuthenticated)
    const userInfo = computed(() => authStore.userInfo)
    const userRoles = computed(() => authStore.userInfo?.roles || [])

    // 🎭 개별 권한 체크 함수들
    const hasRole = (role) => computed(() =>
        userRoles.value.includes(role)
    )

    const hasAnyRole = (roles) => computed(() =>
        roles.some(role => userRoles.value.includes(role))
    )

    const hasAllRoles = (roles) => computed(() =>
        roles.every(role => userRoles.value.includes(role))
    )

    // 🏢 주요 권한별 단축 함수들
    const hasUserAccess = computed(() =>
        // ROLE_USER는 기본 권한으로 간주하여 인증된 사용자라면 항상 허용
        isAuthenticated.value || userRoles.value.includes(RoleCode.USER)
    )

    const hasHRAccess = computed(() =>
        userRoles.value.includes(RoleCode.HR_ACCESS)
    )

    const hasRecruitmentPlanEditor = computed(() =>
        userRoles.value.includes(RoleCode.RECRUITMENT_PLAN_EDITOR)
    )

    const hasApprovalProcessor = computed(() =>
        userRoles.value.includes(RoleCode.APPROVAL_PROCESSOR)
    )

    const hasRecruitmentOperator = computed(() =>
        userRoles.value.includes(RoleCode.RECRUITMENT_OPERATOR)
    )

    // 🚪 로그인/로그아웃 함수들
    const login = async (credentials) => {
        return await authStore.login(credentials)
    }

    const logout = async () => {
        return await authStore.logout()
    }

    // 🔧 유틸리티 함수들
    const getAuthHeaders = () => {
        return authStore.getAuthHeaders()
    }

    const restoreAuth = () => {
        return authStore.restoreAuth()
    }

    return {
        // 기본 상태
        isAuthenticated,
        userInfo,
        userRoles,

        // 권한 체크 함수들
        hasRole,
        hasAnyRole,
        hasAllRoles,

        // 주요 권한 단축키
        hasUserAccess,
        hasHRAccess,
        hasRecruitmentPlanEditor,
        hasApprovalProcessor,
        hasRecruitmentOperator,

        // 인증 관련 함수들
        login,
        logout,
        getAuthHeaders,
        restoreAuth
    }
} 