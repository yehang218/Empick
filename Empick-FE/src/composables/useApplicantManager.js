import { ref, computed, watch } from 'vue'
import { debounce } from 'lodash'

/**
 * 지원자 관리 Composable - MVVM 패턴의 ViewModel 로직
 * @param {Object} applicantStore - 지원자 스토어 (Model)
 * @param {Object} router - Vue Router 인스턴스
 * @param {Object} toast - Toast 알림 인스턴스
 */
export function useApplicantManager(applicantStore, router, toast) {
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

    // ===== 계산된 속성 =====
    const getApplicantCount = (applicantId) => {
        return applicantStore.filteredAndSortedApplicants.filter(
            item => item.applicantId === applicantId
        ).length
    }

    const getApplicantApplicationNumber = (currentItem) => {
        const sameApplicantApplications = applicantStore.filteredAndSortedApplicants
            .filter(item => item.applicantId === currentItem.applicantId)
            .sort((a, b) => new Date(a.createdAt) - new Date(b.createdAt))

        return sameApplicantApplications.findIndex(item =>
            item.uniqueKey === currentItem.uniqueKey
        ) + 1
    }

    const getSelectedApplicantNames = () => {
        if (!selectedApplicants.value || selectedApplicants.value.length === 0) return []
        const selectedNames = selectedApplicants.value.map(selectedItem => selectedItem.name)
        return [...new Set(selectedNames)] // 중복 제거
    }

    const getUniqueApplicantCount = () => {
        const uniqueApplicantIds = new Set(
            applicantStore.filteredAndSortedApplicants.map(item => item.applicantId)
        )
        return uniqueApplicantIds.size
    }

    // ===== 이벤트 핸들러 =====
    const handleSearch = debounce((value) => {
        applicantStore.setSearchQuery(value)
    }, 300)

    const handleSort = (options) => {
        console.log('🔄 handleSort 호출됨:', options)
        console.log('🔍 정렬 키:', options.sortBy)
        console.log('🔍 정렬 방향:', options.sortDesc)
        
        if (options.sortBy && options.sortBy.length > 0) {
            const isJobtestSort = options.sortBy[0] === 'jobtestStatus';
            console.log('🎯 실무테스트 정렬인가?', isJobtestSort);
            
            applicantStore.setSort({
                sortBy: options.sortBy,
                sortDesc: options.sortDesc || [false]
            })
        } else {
            console.log('🔄 정렬 초기화');
            applicantStore.setSort({
                sortBy: [],
                sortDesc: []
            })
        }
    }

    const viewApplicantDetail = (item, options = {}) => {
        console.log('🔍 상세보기 클릭:', item)
        
        // applicationId 유효성 검증 및 결정
        let useId = null
        
        if (item.applicationId && !isNaN(Number(item.applicationId)) && Number(item.applicationId) > 0) {
            useId = item.applicationId
            console.log('✅ applicationId 사용:', useId)
        } 
        else if (item.applicantId && !isNaN(Number(item.applicantId)) && Number(item.applicantId) > 0) {
            useId = item.applicantId
            console.log('✅ applicantId를 applicationId 대신 사용:', useId)
        } 
        else if (item.id && !isNaN(Number(item.id)) && Number(item.id) > 0) {
            useId = item.id
            console.log('✅ id를 applicationId 대신 사용:', useId)
        }
        
        if (!useId) {
            console.error('❌ 사용 가능한 ID가 없음:', { 
                applicationId: item.applicationId, 
                id: item.id, 
                applicantId: item.applicantId 
            })
            toast.error('지원서 ID를 찾을 수 없습니다. 관리자에게 문의하세요.')
            throw new Error('Invalid ID')
        }
        
        console.log('✅ 최종 사용할 ID:', useId)
        
        // 상세 페이지로 이동
        router.push({
            path: `/employment/applications/${useId}`,
            query: {
                // 기본 지원자 정보
                applicantId: item.applicantId,
                applicationId: useId,
                name: item.name,
                phone: item.phone,
                email: item.email,
                profileUrl: item.profileUrl,
                birth: item.birth,
                address: item.address,
                recruitmentId: item.recruitmentId,
                introduceRatingResultId: item.introduceRatingResultId,
                jobId: item.jobId,
                jobName: item.jobName,
                createdAt: item.createdAt,
                status: item.status,
                updatedAt: item.updatedAt,
                updatedBy: item.updatedBy,

                // 추가 필드들
                introduceEvaluationContent: item.introduceEvaluationContent,
                from: options.from,
                page: options.page
            }
        })
    }

    // ===== 선택 관리 로직 =====
    const isSelected = (item) => {
        return selectedApplicants.value.some(selected => selected.uniqueKey === item.uniqueKey)
    }

    const toggleSelection = (item) => {
        const isCurrentlySelected = isSelected(item)
        if (isCurrentlySelected) {
            selectedApplicants.value = selectedApplicants.value.filter(
                selected => selected.uniqueKey !== item.uniqueKey
            )
        } else {
            selectedApplicants.value.push(item)
        }
    }

    const selectAll = (items) => {
        selectedApplicants.value = [...items]
    }

    const clearSelection = () => {
        selectedApplicants.value = []
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
        toggleRegistrationSelection,

        // 계산된 값
        getApplicantCount,
        getApplicantApplicationNumber,
        getSelectedApplicantNames,
        getUniqueApplicantCount,

        // 검색 및 정렬
        handleSearch,
        handleSort,

        // 네비게이션
        viewApplicantDetail,

        // 선택 관리
        isSelected,
        toggleSelection,
        selectAll,
        clearSelection
    }
} 