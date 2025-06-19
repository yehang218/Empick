import { ref, computed, watch } from 'vue'

export function useApplicantManager() {
    // 선택된 지원자 관련 상태
    const selectedApplicants = ref([])
    const currentApplicantIndex = ref(0)

    // 다중 등록 관련 상태
    const selectAllForRegistration = ref(false)
    const selectedForRegistration = ref([])

    // 지원자별 폼 데이터 저장소
    const applicantFormData = ref(new Map())

    // 현재 편집중인 지원자
    const currentApplicant = computed(() => {
        return selectedApplicants.value[currentApplicantIndex.value] || null
    })

    // 체크박스 indeterminate 상태
    const isIndeterminate = computed(() => {
        const selectedCount = selectedForRegistration.value.length
        const totalCount = selectedApplicants.value.length
        return selectedCount > 0 && selectedCount < totalCount
    })

    // 전체 선택 상태 업데이트
    watch(selectedForRegistration, (newValue) => {
        const totalCount = selectedApplicants.value.length
        selectAllForRegistration.value = newValue.length === totalCount && totalCount > 0
    }, { deep: true })

    // 지원자 초기화
    const initializeApplicants = (applicantsData) => {
        selectedApplicants.value = applicantsData
        currentApplicantIndex.value = 0

        // 다중 선택 시 기본적으로 모든 지원자를 등록 대상으로 선택
        if (applicantsData.length > 1) {
            selectedForRegistration.value = [...applicantsData]
            selectAllForRegistration.value = true
        }
    }

    // 현재 폼 데이터 저장
    const saveCurrentFormData = (formData) => {
        if (currentApplicant.value) {
            applicantFormData.value.set(currentApplicant.value.applicantId, { ...formData })
            console.log('💾 폼 데이터 저장됨:', currentApplicant.value.name, formData)
        }
    }

    // 저장된 폼 데이터 가져오기
    const getSavedFormData = (applicantId) => {
        return applicantFormData.value.get(applicantId)
    }

    // 저장된 폼 데이터 삭제
    const deleteSavedFormData = (applicantId) => {
        applicantFormData.value.delete(applicantId)
    }

    // 지원자 선택
    const selectCurrentApplicant = (index) => {
        console.log('👆 지원자 선택:', index)
        currentApplicantIndex.value = index
    }

    // 이전 지원자
    const previousApplicant = () => {
        if (currentApplicantIndex.value > 0) {
            currentApplicantIndex.value--
        }
    }

    // 다음 지원자
    const nextApplicant = () => {
        if (currentApplicantIndex.value < selectedApplicants.value.length - 1) {
            currentApplicantIndex.value++
        }
    }

    // 전체 등록 선택/해제
    const toggleSelectAllForRegistration = (selectAll) => {
        console.log('🔄 전체 등록 선택 토글:', selectAll)
        if (selectAll) {
            selectedForRegistration.value = [...selectedApplicants.value]
        } else {
            selectedForRegistration.value = []
        }
    }

    // 개별 등록 선택/해제
    const toggleRegistrationSelection = (applicant) => {
        console.log('✅ 등록 대상 토글:', applicant.name)
        const index = selectedForRegistration.value.findIndex(a => a.applicantId === applicant.applicantId)

        if (index > -1) {
            selectedForRegistration.value.splice(index, 1)
            console.log('❌ 등록 대상에서 제외됨')
        } else {
            selectedForRegistration.value.push(applicant)
            console.log('✅ 등록 대상에 추가됨')
        }
    }

    return {
        // 상태
        selectedApplicants,
        currentApplicantIndex,
        selectAllForRegistration,
        selectedForRegistration,
        currentApplicant,
        isIndeterminate,

        // 메서드
        initializeApplicants,
        saveCurrentFormData,
        getSavedFormData,
        deleteSavedFormData,
        selectCurrentApplicant,
        previousApplicant,
        nextApplicant,
        toggleSelectAllForRegistration,
        toggleRegistrationSelection
    }
} 