import { ref } from 'vue'
import { useMemberStore } from '@/stores/memberStore'

/**
 * 프로필 폼 관리 Composable
 * 폼 상태, 유효성 검사, 편집 모드 관리
 */
export function useProfileForm() {
    const memberStore = useMemberStore()

    // 폼 상태
    const formRef = ref(null)
    const isEditing = ref(false)
    const originalForm = ref(null)
    const isFormValid = ref(false)

    // 유효성 검사 규칙
    const validationRules = {
        name: [
            v => !!v || '이름을 입력해주세요',
            v => (v && v.length >= 2) || '이름은 2글자 이상이어야 합니다',
            v => (v && v.length <= 20) || '이름은 20글자 이하여야 합니다'
        ],
        phone: [
            v => !!v || '연락처를 입력해주세요',
            v => /^010-\d{4}-\d{4}$/.test(v) || '올바른 연락처 형식이 아닙니다 (010-1234-5678)'
        ],
        email: [
            v => !!v || '이메일을 입력해주세요',
            v => /.+@.+\..+/.test(v) || '올바른 이메일 형식이 아닙니다'
        ]
    }

    // 편집 모드 시작
    const startEditing = () => {
        originalForm.value = JSON.parse(JSON.stringify(memberStore.form))
        isEditing.value = true
    }

    // 편집 취소
    const cancelEditing = () => {
        if (originalForm.value) {
            memberStore.form = JSON.parse(JSON.stringify(originalForm.value))
        }
        isEditing.value = false
        if (formRef.value) {
            formRef.value.resetValidation()
        }
    }

    // 폼 유효성 검사
    const validateForm = async () => {
        if (!formRef.value) return false
        const { valid } = await formRef.value.validate()
        return valid
    }

    // 변경사항 저장
    const saveChanges = async () => {
        const isValid = await validateForm()
        if (!isValid) {
            throw new Error('입력된 정보를 확인해주세요')
        }

        await memberStore.updateMyInfo()
        isEditing.value = false
    }

    return {
        // 상태
        formRef,
        isEditing,
        isFormValid,
        validationRules,

        // 메서드
        startEditing,
        cancelEditing,
        validateForm,
        saveChanges
    }
} 