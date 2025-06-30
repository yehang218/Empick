import { ref, computed } from 'vue'
import { useMemberStore } from '@/stores/memberStore'

/**
 * 프로필 이미지 관리 Composable
 * 이미지 업로드, 유효성 검사, 표시 로직
 */
export function useProfileImage() {
    const memberStore = useMemberStore()
    const fileInputRef = ref(null)

    // 이미지 소스 계산
    const profileImageSrc = computed(() => {
        return memberStore.profileImageUrl || memberStore.defaultProfileImageUrl
    })

    // 파일 유효성 검사
    const validateImageFile = (file) => {
        const allowedTypes = ['image/jpeg', 'image/png', 'image/webp']
        const maxSize = 5 * 1024 * 1024 // 5MB

        if (!allowedTypes.includes(file.type)) {
            throw new Error('JPG, PNG, WEBP 형식의 이미지만 업로드할 수 있습니다')
        }

        if (file.size > maxSize) {
            throw new Error('파일 크기는 5MB를 초과할 수 없습니다')
        }

        return true
    }

    // 파일 입력 트리거
    const triggerFileInput = () => {
        if (fileInputRef.value) {
            fileInputRef.value.click()
        }
    }

    // 프로필 이미지 업로드
    const uploadProfileImage = async (file, employeeNumber, memberId) => {
        validateImageFile(file)

        const formData = new FormData()
        const fileName = `${employeeNumber}.png`
        const newFile = new File([file], fileName, { type: file.type })

        formData.append('file', newFile)
        // fileName 매개변수 제거 (백엔드에서 UUID로 자동 생성)

        await memberStore.uploadProfileImage(memberId, formData)

        // 업로드 성공 후 프로필 이미지 다시 로드
        await loadProfileImage(memberId)

        // 파일 입력 초기화
        if (fileInputRef.value) {
            fileInputRef.value.value = ''
        }
    }

    // 프로필 이미지 로드
    const loadProfileImage = async (memberId) => {
        if (memberId) {
            await memberStore.fetchProfileImage(memberId)
        }
    }

    return {
        // 상태
        fileInputRef,
        profileImageSrc,

        // 메서드
        triggerFileInput,
        uploadProfileImage,
        loadProfileImage,
        validateImageFile
    }
} 