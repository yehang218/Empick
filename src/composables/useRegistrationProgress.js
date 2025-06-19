import { ref } from 'vue'

export function useRegistrationProgress() {
    // 등록 진행 상황 관리
    const registrationProgress = ref({})

    // 진행 상황 설정
    const setRegistrationProgress = (applicantId, status, progress, message) => {
        registrationProgress.value[applicantId] = {
            status,
            progress,
            message
        }
    }

    // 진행 상황 초기화
    const clearRegistrationProgress = () => {
        registrationProgress.value = {}
    }

    // 특정 지원자의 진행 상황 삭제
    const removeRegistrationProgress = (applicantId) => {
        delete registrationProgress.value[applicantId]
    }

    // 상태별 색상 헬퍼 함수들
    const getProgressColor = (status) => {
        switch (status) {
            case 'processing': return 'primary'
            case 'success': return 'success'
            case 'error': return 'error'
            default: return 'grey'
        }
    }

    const getProgressTextColor = (status) => {
        switch (status) {
            case 'processing': return 'text-primary'
            case 'success': return 'text-success'
            case 'error': return 'text-error'
            default: return 'text-grey'
        }
    }

    const getStatusChipColor = (status) => {
        switch (status) {
            case 'processing': return 'primary'
            case 'success': return 'success'
            case 'error': return 'error'
            default: return 'grey'
        }
    }

    const getStatusText = (status) => {
        switch (status) {
            case 'processing': return '처리중'
            case 'success': return '완료'
            case 'error': return '실패'
            default: return '대기'
        }
    }

    return {
        // 상태
        registrationProgress,

        // 메서드
        setRegistrationProgress,
        clearRegistrationProgress,
        removeRegistrationProgress,

        // 헬퍼 함수
        getProgressColor,
        getProgressTextColor,
        getStatusChipColor,
        getStatusText
    }
} 