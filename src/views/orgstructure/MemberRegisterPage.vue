<template>
    <v-container class="py-8" style="max-width: 1100px;">
        <v-row>
            <v-col cols="12">
                <h2 class="text-h5 font-weight-bold mb-6">신규 사원 정보 등록</h2>
            </v-col>
        </v-row>

        <!-- 선택된 지원자 정보 표시 섹션 -->
        <v-row v-if="selectedApplicants.length > 0">
            <v-col cols="12">
                <ApplicantInfoCard :selectedApplicants="selectedApplicants"
                    :currentApplicantIndex="currentApplicantIndex" :selectedForRegistration="selectedForRegistration"
                    :selectAllForRegistration="selectAllForRegistration" :isIndeterminate="isIndeterminate"
                    :registrationProgress="registrationProgress" :currentApplicant="currentApplicant"
                    @toggleSelectAllForRegistration="toggleSelectAllForRegistration" @bulkRegister="onBulkRegister"
                    @toggleRegistrationSelection="toggleRegistrationSelection"
                    @selectCurrentApplicant="handleSelectCurrentApplicant" />
            </v-col>
        </v-row>

        <v-row>
            <v-col cols="12">
                <v-alert v-if="regStore.alertVisible" type="warning" class="mb-4" border="start" variant="tonal"
                    style="position:fixed;top:24px;right:32px;left:auto;transform:none;z-index:2000;min-width:320px;max-width:90vw;">
                    {{ regStore.alertMessage }}
                </v-alert>
            </v-col>
        </v-row>

        <!-- 사원 등록 폼 -->
        <MemberRegistrationForm :form="regStore.form" :profileImageUrl="regStore.profileImageUrl"
            :profileImageFile="regStore.profileImageFile" :photoButtonText="regStore.photoButtonText"
            :departments="orgStore.departments" :positions="orgStore.positions" :jobs="orgStore.jobs"
            :ranks="orgStore.ranks" @profileImageChange="onProfileImageChange" />

        <!-- 네비게이션 -->
        <ApplicantNavigation :selectedApplicants="selectedApplicants" :currentApplicantIndex="currentApplicantIndex"
            @previousApplicant="handlePreviousApplicant" @nextApplicant="handleNextApplicant" />

        <!-- 등록 액션 -->
        <MemberRegisterActions :selectedApplicants="selectedApplicants" :currentApplicantIndex="currentApplicantIndex"
            :currentApplicant="currentApplicant" @register="onRegister" />

        <!-- 확인 모달 -->
        <AlertModal v-if="showConfirmDialog" message="입력하신 내용이 모두 삭제됩니다. 정말로 나가시겠습니까?" @confirm="confirmLeave"
            @cancel="cancelLeave" />
    </v-container>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useMemberRegisterStore } from '@/stores/memberRegisterStore'
import { useOrganizationStore } from '@/stores/organizationStore'
import { useToast } from 'vue-toastification'
import { useRouter, useRoute, onBeforeRouteLeave } from 'vue-router'
import { useApplicantManager } from '@/composables/useApplicantManager'
import { useRegistrationProgress } from '@/composables/useRegistrationProgress'
import { useFileUpload } from '@/composables/useFileUpload'
import AlertModal from '@/components/common/AlertModal.vue'
import ApplicantInfoCard from '@/components/orgstructure/ApplicantInfoCard.vue'
import MemberRegistrationForm from '@/components/orgstructure/MemberRegistrationForm.vue'
import ApplicantNavigation from '@/components/orgstructure/ApplicantNavigation.vue'
import MemberRegisterActions from '@/components/orgstructure/MemberRegisterActions.vue'

const regStore = useMemberRegisterStore()
const orgStore = useOrganizationStore()
const toast = useToast()

const router = useRouter()
const route = useRoute()
const showConfirmDialog = ref(false)
const pendingNavigation = ref(null)

// Composables 사용
const {
    selectedApplicants,
    currentApplicantIndex,
    selectAllForRegistration,
    selectedForRegistration,
    currentApplicant,
    isIndeterminate,
    initializeApplicants,
    saveCurrentFormData,
    getSavedFormData,
    deleteSavedFormData,
    selectCurrentApplicant,
    previousApplicant,
    nextApplicant,
    toggleSelectAllForRegistration,
    toggleRegistrationSelection
} = useApplicantManager()

const {
    registrationProgress,
    setRegistrationProgress,
    clearRegistrationProgress
} = useRegistrationProgress()

const { handleImageUpload } = useFileUpload()

// 현재 폼 데이터를 composable 형태로 변환하는 헬퍼 함수
const getCurrentFormData = () => {
    return {
        name: regStore.form.name,
        email: regStore.form.email,
        phone: regStore.form.phone,
        birth: regStore.form.birth,
        address: regStore.form.address,
        departmentId: regStore.form.departmentId,
        positionId: regStore.form.positionId,
        jobId: regStore.form.jobId,
        rankId: regStore.form.rankId,
        profileImageFile: regStore.profileImageFile,
        profileImageUrl: regStore.profileImageUrl
    }
}

// 저장된 폼 데이터 복원 함수
const restoreFormData = (applicant) => {
    const savedData = getSavedFormData(applicant.applicantId)

    if (savedData) {
        // 저장된 데이터가 있으면 복원
        console.log('📂 저장된 폼 데이터 복원:', applicant.name, savedData)
        regStore.form.name = savedData.name
        regStore.form.email = savedData.email
        regStore.form.phone = savedData.phone
        regStore.form.birth = savedData.birth
        regStore.form.address = savedData.address
        regStore.form.departmentId = savedData.departmentId
        regStore.form.positionId = savedData.positionId
        regStore.form.jobId = savedData.jobId
        regStore.form.rankId = savedData.rankId

        // 이미지 정보 복원
        regStore.profileImageFile = savedData.profileImageFile
        regStore.profileImageUrl = savedData.profileImageUrl

        // pictureUrl 설정: 이미지 파일이 있으면 임시 경로, 없으면 빈 문자열
        if (savedData.profileImageFile) {
            regStore.form.pictureUrl = 'profiles/temp.png'
            console.log('📷 저장된 이미지 파일 사용:', savedData.profileImageFile.name)
        } else {
            regStore.form.pictureUrl = ''
            console.log('📷 저장된 이미지 없음')
        }
    } else {
        // 저장된 데이터가 없으면 기본값으로 로드
        console.log('📝 기본 데이터로 폼 로드:', applicant.name)
        loadApplicantToForm(applicant)
    }
}

// 컴포넌트 마운트 시 쿼리 파라미터에서 지원자 데이터 로드
onMounted(() => {
    console.log('🚀 MemberRegisterPage 마운트됨')
    console.log('🔍 route.query:', route.query)

    if (route.query.applicants) {
        try {
            const applicantsData = JSON.parse(route.query.applicants)
            console.log('📋 파싱된 지원자 데이터:', applicantsData)

            if (Array.isArray(applicantsData) && applicantsData.length > 0) {
                initializeApplicants(applicantsData)

                // 첫 번째 지원자 데이터로 폼 초기화
                loadApplicantToForm(applicantsData[0])

                console.log('✅ 지원자 데이터 로드 완료:', selectedApplicants.value.length, '명')
            }
        } catch (error) {
            console.error('❌ 지원자 데이터 파싱 실패:', error)
            toast.error('지원자 데이터를 불러오는데 실패했습니다.')
        }
    }
})

// 지원자 데이터를 폼에 로드하는 함수
const loadApplicantToForm = (applicant) => {
    console.log('📝 폼에 지원자 데이터 로드:', applicant.name)

    regStore.form.name = applicant.name || ''
    regStore.form.email = applicant.email || ''
    regStore.form.phone = applicant.phone || ''
    regStore.form.birth = applicant.birth ? new Date(applicant.birth).toISOString().split('T')[0] : ''
    regStore.form.address = applicant.address || ''

    // 기본값으로 설정 (필요에 따라 수정)
    regStore.form.departmentId = regStore.form.departmentId || 1
    regStore.form.positionId = regStore.form.positionId || 0
    regStore.form.jobId = regStore.form.jobId || 0
    regStore.form.rankId = regStore.form.rankId || 0
}

// 지원자 선택 핸들러
const handleSelectCurrentApplicant = (index) => {
    // 현재 폼 데이터 저장
    saveCurrentFormData(getCurrentFormData())

    // 지원자 변경
    selectCurrentApplicant(index)

    // 새 지원자의 폼 데이터 복원
    restoreFormData(selectedApplicants.value[index])
}

// 이전 지원자 핸들러
const handlePreviousApplicant = () => {
    // 현재 폼 데이터 저장
    saveCurrentFormData(getCurrentFormData())

    previousApplicant()

    // 이전 지원자의 폼 데이터 복원
    restoreFormData(currentApplicant.value)
}

// 다음 지원자 핸들러
const handleNextApplicant = () => {
    // 현재 폼 데이터 저장
    saveCurrentFormData(getCurrentFormData())

    nextApplicant()

    // 다음 지원자의 폼 데이터 복원
    restoreFormData(currentApplicant.value)
}

const onBulkRegister = async () => {
    console.log('📝 일괄 등록 시작:', selectedForRegistration.value.length, '명')

    if (selectedForRegistration.value.length === 0) {
        toast.warning('등록할 지원자를 선택해주세요.')
        return
    }

    // 현재 폼 데이터 저장
    saveCurrentFormData(getCurrentFormData())

    // 진행 상황 초기화
    clearRegistrationProgress()

    let successCount = 0
    let failCount = 0
    const failedApplicants = []

    // 선택된 지원자들을 순차적으로 처리
    for (let i = 0; i < selectedForRegistration.value.length; i++) {
        const applicant = selectedForRegistration.value[i]

        try {
            console.log(`📝 등록 중 (${i + 1}/${selectedForRegistration.value.length}):`, applicant.name)

            // 진행 상황 업데이트: 처리 시작
            setRegistrationProgress(applicant.applicantId, 'processing', 10, '등록 준비 중...')

            // 저장된 폼 데이터가 있으면 사용, 없으면 기본 데이터 사용
            const savedData = getSavedFormData(applicant.applicantId)
            if (savedData) {
                console.log('📂 저장된 데이터로 등록:', applicant.name)
                // 저장된 데이터를 폼에 적용
                regStore.form.name = savedData.name
                regStore.form.email = savedData.email
                regStore.form.phone = savedData.phone
                regStore.form.birth = savedData.birth
                regStore.form.address = savedData.address
                regStore.form.departmentId = savedData.departmentId
                regStore.form.positionId = savedData.positionId
                regStore.form.jobId = savedData.jobId
                regStore.form.rankId = savedData.rankId
                regStore.profileImageFile = savedData.profileImageFile
                regStore.profileImageUrl = savedData.profileImageUrl

                // pictureUrl 설정: 이미지 파일이 있으면 임시 경로, 없으면 빈 문자열
                if (savedData.profileImageFile) {
                    regStore.form.pictureUrl = 'profiles/temp.png'
                    console.log('📷 저장된 이미지 파일 사용:', savedData.profileImageFile.name)
                } else {
                    regStore.form.pictureUrl = ''
                    console.log('📷 저장된 이미지 없음')
                }
            } else {
                console.log('📝 기본 데이터로 등록:', applicant.name)
                // 기본 지원자 데이터로 폼 설정
                loadApplicantToForm(applicant)
            }

            // 진행 상황 업데이트: 사원 등록 중
            setRegistrationProgress(applicant.applicantId, 'processing', 50, '사원 등록 중...')

            // 사원 등록 실행
            const result = await regStore.registerMemberWithImage()

            if (result) {
                successCount++
                console.log('✅ 등록 성공:', applicant.name)

                // 진행 상황 업데이트: 성공
                setRegistrationProgress(applicant.applicantId, 'success', 100, '등록 완료')

                // 등록 성공한 지원자의 저장된 데이터 삭제
                deleteSavedFormData(applicant.applicantId)
            } else {
                failCount++
                failedApplicants.push(applicant.name)
                console.log('❌ 등록 실패:', applicant.name)

                // 진행 상황 업데이트: 실패
                setRegistrationProgress(applicant.applicantId, 'error', 100, '등록 실패')
            }

            // 폼 초기화 (다음 지원자를 위해)
            regStore.resetForm()

        } catch (error) {
            failCount++
            failedApplicants.push(applicant.name)
            console.error('❌ 등록 중 오류:', applicant.name, error)

            // 진행 상황 업데이트: 오류
            setRegistrationProgress(applicant.applicantId, 'error', 100, `오류: ${error.message || '알 수 없는 오류'}`)

            // 폼 초기화
            regStore.resetForm()
        }

        // 각 등록 사이에 약간의 지연 (UI 업데이트를 위해)
        await new Promise(resolve => globalThis.setTimeout(resolve, 100))
    }

    // 결과 알림
    if (successCount > 0 && failCount === 0) {
        toast.success(`${successCount}명의 사원 등록이 모두 완료되었습니다!`)
    } else if (successCount > 0 && failCount > 0) {
        toast.warning(`${successCount}명 등록 성공, ${failCount}명 등록 실패\n실패: ${failedApplicants.join(', ')}`)
    } else {
        toast.error(`모든 등록이 실패했습니다.\n실패: ${failedApplicants.join(', ')}`)
    }

    // 성공한 경우 지원자 목록으로 이동
    if (successCount > 0) {
        // 3초 후 자동 이동 (사용자가 결과를 확인할 수 있도록)
        globalThis.setTimeout(() => {
            router.push('/employment/applicants')
        }, 3000)
    }
}

const onProfileImageChange = (event) => {
    handleImageUpload(
        event,
        (file) => {
            regStore.setProfileImage(file)
            // 현재 지원자의 이미지 정보를 즉시 저장
            if (currentApplicant.value) {
                saveCurrentFormData(getCurrentFormData())
            }
        },
        () => {
            regStore.clearProfileImage()
            // 현재 지원자의 이미지 정보를 즉시 저장
            if (currentApplicant.value) {
                saveCurrentFormData(getCurrentFormData())
            }
        }
    )
}

const onRegister = async () => {
    try {
        // 현재 폼 데이터 저장
        saveCurrentFormData(getCurrentFormData())

        const result = await regStore.registerMemberWithImage()
        if (result) {
            const currentName = currentApplicant.value?.name || '지원자'
            toast.success(`${currentName}의 사원 등록이 완료되었습니다!`)

            // 등록 완료된 지원자의 저장된 데이터 삭제
            if (currentApplicant.value) {
                deleteSavedFormData(currentApplicant.value.applicantId)
            }

            // 다중 선택 시 다음 지원자로 이동
            if (selectedApplicants.value.length > 1 && currentApplicantIndex.value < selectedApplicants.value.length - 1) {
                handleNextApplicant()
                regStore.resetForm() // 폼 초기화 후 다음 지원자 데이터 로드
                restoreFormData(currentApplicant.value)
            } else {
                // 모든 지원자 등록 완료 또는 단일 선택 시
                regStore.resetForm()
                if (selectedApplicants.value.length > 1) {
                    toast.success('모든 지원자의 사원 등록이 완료되었습니다!')
                    router.push('/employment/applicants')
                }
            }
        }
    } catch (error) {
        toast.error(error.message || '사원 등록에 실패했습니다.')
    }
}

// 페이지를 나가기 전에 확인
onBeforeRouteLeave((to, from, next) => {
    // 입력값이 있는지 확인
    const hasInput = Object.values(regStore.form).some(value => value !== null && value !== '') || regStore.profileImageFile

    if (hasInput) {
        pendingNavigation.value = next
        showConfirmDialog.value = true
    } else {
        next()
    }
})

const confirmLeave = () => {
    regStore.resetForm()
    showConfirmDialog.value = false
    if (pendingNavigation.value) {
        pendingNavigation.value()
        pendingNavigation.value = null
    }
}

const cancelLeave = () => {
    showConfirmDialog.value = false
    if (pendingNavigation.value) {
        pendingNavigation.value(false)
        pendingNavigation.value = null
    }
}
</script>

<style scoped>
.selected-applicants-scroll {
    border: 1px solid #e0e0e0;
    border-radius: 8px;
    background-color: #fafafa;
}

.selected-applicant {
    background-color: #e3f2fd !important;
    border-left: 4px solid #1976d2;
}

.selected-applicant:hover {
    background-color: #bbdefb !important;
}

.v-list-item {
    border-radius: 6px;
    margin-bottom: 4px;
}

.v-list-item:hover {
    background-color: #f5f5f5;
}
</style>