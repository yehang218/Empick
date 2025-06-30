import { ref } from 'vue'

/**
 * 토스트 메시지 관리 Composable
 * 성공, 에러, 정보 메시지 표시
 */
export function useToast() {
    const showSnackbar = ref(false)
    const snackbarMessage = ref('')
    const snackbarColor = ref('success')

    /**
     * 토스트 메시지 표시
     * @param {string} message - 표시할 메시지
     * @param {string} color - 메시지 색상 (success, error, warning, info)
     */
    const showToast = (message, color = 'success') => {
        snackbarMessage.value = message
        snackbarColor.value = color
        showSnackbar.value = true
    }

    /**
     * 성공 메시지 표시
     * @param {string} message - 성공 메시지
     */
    const showSuccess = (message) => {
        showToast(message, 'success')
    }

    /**
     * 에러 메시지 표시
     * @param {string} message - 에러 메시지
     */
    const showError = (message) => {
        showToast(message, 'error')
    }

    /**
     * 경고 메시지 표시
     * @param {string} message - 경고 메시지
     */
    const showWarning = (message) => {
        showToast(message, 'warning')
    }

    /**
     * 정보 메시지 표시
     * @param {string} message - 정보 메시지
     */
    const showInfo = (message) => {
        showToast(message, 'info')
    }

    /**
     * 토스트 닫기
     */
    const hideToast = () => {
        showSnackbar.value = false
    }

    return {
        // 상태
        showSnackbar,
        snackbarMessage,
        snackbarColor,

        // 메서드
        showToast,
        showSuccess,
        showError,
        showWarning,
        showInfo,
        hideToast
    }
} 