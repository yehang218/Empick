import { useToast } from 'vue-toastification'

export function useFileUpload() {
    const toast = useToast()

    // 파일 검증
    const validateImageFile = (file) => {
        const allowedTypes = ['image/jpeg', 'image/png', 'image/webp']
        const maxSize = 5 * 1024 * 1024 // 5MB

        if (!allowedTypes.includes(file.type)) {
            toast.error('JPG, PNG, WEBP 형식의 이미지만 업로드할 수 있습니다.')
            return false
        }

        if (file.size > maxSize) {
            toast.error('파일 크기는 5MB를 초과할 수 없습니다.')
            return false
        }

        return true
    }

    // 이미지 파일 처리
    const handleImageUpload = (event, onSuccess, onClear) => {
        const file = event.target.files && event.target.files[0]

        if (file) {
            if (validateImageFile(file)) {
                console.log('📷 프로필 이미지 선택됨:', file.name, 'size:', file.size)
                onSuccess(file)
            }
        } else {
            console.log('📷 프로필 이미지 선택 취소됨')
            onClear()
        }
    }

    return {
        validateImageFile,
        handleImageUpload
    }
} 