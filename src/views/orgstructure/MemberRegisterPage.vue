<template>
    <v-container class="py-8 modern-container" style="max-width: 1200px;">
        <v-row>
            <v-col cols="12">
                <PageHeader icon="mdi-account-plus-outline" :title="pageTitle" :subtitle="pageSubtitle" />
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
                <RegistrationAlert :visible="regStore.alertVisible" type="warning" :message="regStore.alertMessage" />
            </v-col>
        </v-row>

        <!-- 사원 등록 폼 -->
        <v-row class="main-content-row">
            <!-- 프로필 이미지 및 등록 버튼 -->
            <v-col cols="12" md="3" class="profile-section">
                <ProfileImageUpload :profileImageUrl="regStore.profileImageUrl"
                    :profileImageFile="regStore.profileImageFile" :photoButtonText="regStore.photoButtonText"
                    @fileChange="onProfileImageChange" />

                <!-- 등록 버튼 -->
                <div class="register-section">
                    <v-btn class="register-btn" color="success" size="large" variant="flat" @click="onRegister">
                        <v-icon size="18" class="mr-2">mdi-check-circle</v-icon>
                        {{ registerButtonText }}
                    </v-btn>

                    <div v-if="selectedApplicants.length > 1" class="progress-info">
                        <div class="progress-text">
                            <v-icon size="14" class="mr-1">mdi-information-outline</v-icon>
                            현재 편집 중 ({{ currentApplicantIndex + 1 }}/{{ selectedApplicants.length }})
                        </div>
                    </div>
                </div>
            </v-col>

            <!-- 네비게이션 및 폼 -->
            <v-col cols="12" md="9" class="form-section">
                <!-- 네비게이션과 상태 표시 -->
                <div v-if="selectedApplicants.length > 1" class="navigation-section">
                    <ApplicantNavigation :selectedApplicants="selectedApplicants"
                        :currentApplicantIndex="currentApplicantIndex" :currentApplicant="currentApplicant"
                        @previousApplicant="handlePreviousApplicant" @nextApplicant="handleNextApplicant" />

                    <ApplicantStatusIndicator :currentApplicant="currentApplicant" />
                </div>

                <!-- 폼 섹션들 -->
                <MemberRegistrationForm :form="regStore.form" :profileImageUrl="regStore.profileImageUrl"
                    :profileImageFile="regStore.profileImageFile" :photoButtonText="regStore.photoButtonText"
                    :departments="orgStore.departments" :positions="orgStore.positions" :jobs="orgStore.jobs"
                    :ranks="orgStore.ranks" :selectedApplicants="selectedApplicants"
                    :currentApplicantIndex="currentApplicantIndex" :currentApplicant="currentApplicant"
                    @profileImageChange="onProfileImageChange" @register="onRegister" />
            </v-col>
        </v-row>

        <!-- 확인 모달 -->
        <AlertModal v-if="showConfirmDialog" message="입력하신 내용이 모두 삭제됩니다. 정말로 나가시겠습니까?" @confirm="confirmLeave"
            @cancel="cancelLeave" />

        <!-- 로딩 오버레이 (일괄 등록 중이 아닐 때만 표시) -->
        <CircleLoading :visible="regStore.loading && !isBulkRegistering" :message="loadingMessage"
            :sub-message="loadingSubMessage" color="#2196F3" :size="90" :width="4" />


    </v-container>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import { useMemberRegisterStore } from '@/stores/memberRegisterStore'
import { useOrganizationStore } from '@/stores/organizationStore'
import { useToast } from 'vue-toastification'
import { useRouter, useRoute, onBeforeRouteLeave } from 'vue-router'
import { useApplicantManager } from '@/composables/useApplicantManager'
import { useRegistrationProgress } from '@/composables/useRegistrationProgress'
import { useFileUpload } from '@/composables/useFileUpload'
import AlertModal from '@/components/common/AlertModal.vue'
import PageHeader from '@/components/common/PageHeader.vue'
import RegistrationAlert from '@/components/common/RegistrationAlert.vue'
import CircleLoading from '@/components/common/CircleLoading.vue'
import ApplicantInfoCard from '@/components/orgstructure/ApplicantInfoCard.vue'
import MemberRegistrationForm from '@/components/orgstructure/MemberRegistrationForm.vue'
import ApplicantNavigation from '@/components/orgstructure/ApplicantNavigation.vue'
import ApplicantStatusIndicator from '@/components/orgstructure/ApplicantStatusIndicator.vue'
import ProfileImageUpload from '@/components/orgstructure/ProfileImageUpload.vue'

const regStore = useMemberRegisterStore()
const orgStore = useOrganizationStore()
const toast = useToast()

const router = useRouter()
const route = useRoute()
const showConfirmDialog = ref(false)
const pendingNavigation = ref(null)

// 로딩 상태용 메시지
const loadingMessage = ref('')
const loadingSubMessage = ref('')
const isBulkRegistering = ref(false) // 일괄 등록 중인지 확인하는 플래그

// 등록 버튼 텍스트 (진입 방식에 따라 다르게 표시)
const registerButtonText = computed(() => {
    // 지원자 데이터가 있는 경우 (지원자 목록에서 진입)
    if (selectedApplicants.value.length > 0) {
        return `${currentApplicant.value?.name || '지원자'} 등록`
    }
    // 지원자 데이터가 없는 경우 (신규 사원 정보 등록 메뉴에서 직접 진입)
    return '사원 등록'
})

// 페이지 제목 (진입 방식에 따라 다르게 표시)
const pageTitle = computed(() => {
    // 지원자 데이터가 있는 경우 (지원자 목록에서 진입)
    if (selectedApplicants.value.length > 0) {
        return '지원자 → 사원 등록'
    }
    // 지원자 데이터가 없는 경우 (신규 사원 정보 등록 메뉴에서 직접 진입)
    return '신규 사원 정보 등록'
})

// 페이지 부제목 (진입 방식에 따라 다르게 표시)
const pageSubtitle = computed(() => {
    // 지원자 데이터가 있는 경우 (지원자 목록에서 진입)
    if (selectedApplicants.value.length > 0) {
        return '지원자 정보를 기반으로 새로운 사원을 등록합니다'
    }
    // 지원자 데이터가 없는 경우 (신규 사원 정보 등록 메뉴에서 직접 진입)
    return '새로운 사원의 정보를 입력하여 등록합니다'
})

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

// ProfileImageUpload 컴포넌트로 이동됨

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
        console.log('📷 이미지 상태 복원:', {
            hasFile: !!savedData.profileImageFile,
            fileName: savedData.profileImageFile?.name,
            hasUrl: !!savedData.profileImageUrl
        })

        // pictureUrl 설정: 이미지 파일이 있으면 임시 경로, 없으면 빈 문자열
        if (savedData.profileImageFile) {
            regStore.form.pictureUrl = 'profiles/temp.png'
            console.log('📷 저장된 이미지 파일 사용:', savedData.profileImageFile.name)
        } else {
            regStore.form.pictureUrl = ''
            console.log('📷 저장된 이미지 없음')
        }
    } else {
        // 저장된 데이터가 없으면 기본값으로 로드 + 이미지 상태 초기화
        console.log('📝 기본 데이터로 폼 로드:', applicant.name)
        loadApplicantToForm(applicant)
        // 이미지 상태 명시적 초기화
        regStore.clearProfileImage()
        console.log('📷 이미지 상태 초기화됨')
    }
}

// 컴포넌트 마운트 시 쿼리 파라미터에서 지원자 데이터 로드
onMounted(() => {
    console.log('🚀 MemberRegisterPage 마운트됨')
    console.log('🔍 route.query:', route.query)

    if (route.query.applicants) {
        // 지원자 목록에서 진입한 경우
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
    } else {
        // 신규 사원 정보 등록 메뉴에서 직접 진입한 경우
        console.log('📝 신규 사원 등록 모드로 초기화')

        // 폼을 기본값으로 초기화
        regStore.resetForm()

        // 기본값 설정
        regStore.form.departmentId = 1 // 인사 부서
        regStore.form.positionId = 0   // 미지정
        regStore.form.jobId = 0        // 미지정  
        regStore.form.rankId = 0       // 사원

        console.log('✅ 신규 사원 등록 폼 초기화 완료')
    }
})

// 라우트 변경 감지 (헤더 메뉴에서 같은 페이지로 이동할 때 처리)
watch(() => route.query, (newQuery, oldQuery) => {
    console.log('🔄 라우트 쿼리 변경 감지:', { newQuery, oldQuery })

    // 지원자 데이터가 있던 상태에서 없어진 경우 (헤더 메뉴에서 신규 사원 등록 클릭)
    if (oldQuery?.applicants && !newQuery?.applicants) {
        console.log('📝 지원자 모드 → 신규 사원 모드로 전환')

        // 폼 초기화 및 기본값 설정
        regStore.resetForm()
        regStore.form.departmentId = 1 // 인사 부서
        regStore.form.positionId = 0   // 미지정
        regStore.form.jobId = 0        // 미지정  
        regStore.form.rankId = 0       // 사원

        // 지원자 목록 초기화
        selectedApplicants.value = []

        console.log('✅ 신규 사원 등록 모드로 전환 완료')
    }
    // 지원자 데이터가 없던 상태에서 생긴 경우 (다른 페이지에서 지원자 모드로 진입)
    else if (!oldQuery?.applicants && newQuery?.applicants) {
        console.log('📋 신규 사원 모드 → 지원자 모드로 전환')

        try {
            const applicantsData = JSON.parse(newQuery.applicants)
            console.log('📋 새로운 지원자 데이터:', applicantsData)

            if (Array.isArray(applicantsData) && applicantsData.length > 0) {
                initializeApplicants(applicantsData)
                loadApplicantToForm(applicantsData[0])
                console.log('✅ 지원자 모드로 전환 완료:', selectedApplicants.value.length, '명')
            }
        } catch (error) {
            console.error('❌ 지원자 데이터 파싱 실패:', error)
            toast.error('지원자 데이터를 불러오는데 실패했습니다.')
        }
    }
}, { deep: true })

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

    // 일괄 등록 모드 활성화 (로딩 UI 비활성화)
    isBulkRegistering.value = true

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
                // 이미지 상태 명시적 초기화
                regStore.clearProfileImage()
            }

            // 진행 상황 업데이트: 사원 등록 중
            setRegistrationProgress(applicant.applicantId, 'processing', 50, '사원 등록 중...')

            // 사원 등록 실행
            const result = await regStore.registerMember(regStore.form, regStore.profileImageFile)

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

    // 일괄 등록 완료 후 정리
    isBulkRegistering.value = false
    loadingMessage.value = ''
    loadingSubMessage.value = ''

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
            console.log('📷 프로필 이미지 설정:', currentApplicant.value?.name, file.name)
            regStore.setProfileImage(file)
            // 현재 지원자의 이미지 정보를 즉시 저장
            if (currentApplicant.value) {
                saveCurrentFormData(getCurrentFormData())
                console.log('💾 이미지 설정 후 폼 데이터 저장됨')
            }
        },
        () => {
            console.log('📷 프로필 이미지 제거:', currentApplicant.value?.name)
            regStore.clearProfileImage()
            // 현재 지원자의 이미지 정보를 즉시 저장
            if (currentApplicant.value) {
                saveCurrentFormData(getCurrentFormData())
                console.log('💾 이미지 제거 후 폼 데이터 저장됨')
            }
        }
    )
}

// ProfileImageUpload 컴포넌트로 이동됨

const onRegister = async () => {
    try {
        // 로딩 메시지 설정
        const currentName = currentApplicant.value?.name || regStore.form.name || '사원'
        loadingMessage.value = '사원 등록 중...'
        loadingSubMessage.value = selectedApplicants.value.length > 0
            ? `${currentName}님의 정보를 등록하고 있습니다.`
            : '새로운 사원 정보를 등록하고 있습니다.'

        // 지원자 모드일 때만 현재 폼 데이터 저장
        if (selectedApplicants.value.length > 0) {
            saveCurrentFormData(getCurrentFormData())
        }

        const result = await regStore.registerMember(regStore.form, regStore.profileImageFile)
        if (result) {
            const successName = currentApplicant.value?.name || regStore.form.name || '사원'

            if (selectedApplicants.value.length > 0) {
                // 지원자 → 사원 등록 모드
                toast.success(`${successName}의 사원 등록이 완료되었습니다!`)

                // 등록 완료된 지원자의 저장된 데이터 삭제
                if (currentApplicant.value) {
                    deleteSavedFormData(currentApplicant.value.applicantId)
                }

                // 다중 선택 시 다음 지원자로 이동
                if (selectedApplicants.value.length > 1 && currentApplicantIndex.value < selectedApplicants.value.length - 1) {
                    handleNextApplicant()
                    regStore.resetForm() // 폼 초기화 (이미지 상태 포함)
                    restoreFormData(currentApplicant.value)
                } else {
                    // 모든 지원자 등록 완료 또는 단일 선택 시
                    regStore.resetForm()
                    if (selectedApplicants.value.length > 1) {
                        toast.success('모든 지원자의 사원 등록이 완료되었습니다!')
                    }
                    // 3초 후 지원자 목록으로 이동
                    globalThis.setTimeout(() => {
                        router.push('/employment/applicants')
                    }, 3000)
                }
            } else {
                // 신규 사원 정보 등록 모드
                toast.success(`${successName}의 사원 등록이 완료되었습니다!`)

                // 폼 초기화 후 기본값 재설정
                regStore.resetForm()
                regStore.form.departmentId = 1 // 인사 부서
                regStore.form.positionId = 0   // 미지정
                regStore.form.jobId = 0        // 미지정  
                regStore.form.rankId = 0       // 사원

                // 3초 후 사원 목록 또는 대시보드로 이동
                globalThis.setTimeout(() => {
                    router.push('/')
                }, 3000)
            }
        }
    } catch (error) {
        toast.error(error.message || '사원 등록에 실패했습니다.')
    } finally {
        // 로딩 메시지 초기화
        loadingMessage.value = ''
        loadingSubMessage.value = ''
    }
}

// 페이지를 나가기 전에 확인
onBeforeRouteLeave((to, from, next) => {
    // 실제로 사용자가 입력한 값만 체크 (기본값 제외)
    const userInputFields = [
        'name', 'birth', 'phone', 'email', 'address', 'password', 'hireAt'
    ]

    const hasUserInput = userInputFields.some(field => {
        const value = regStore.form[field]
        return value !== null && value !== '' && value !== undefined
    })

    // 선택 필드 체크 (기본값이 아닌 값이 선택됨)
    // 기본값: departmentId=1(인사부서), positionId=0(미지정), jobId=0(미지정), rankId=0(사원)
    const hasSelections = (regStore.form.departmentId !== null && regStore.form.departmentId !== 1) ||
        (regStore.form.positionId !== null && regStore.form.positionId !== 0) ||
        (regStore.form.jobId !== null && regStore.form.jobId !== 0) ||
        (regStore.form.rankId !== null && regStore.form.rankId !== 0)

    // 프로필 이미지 업로드 여부 체크
    const hasProfileImage = regStore.profileImageFile !== null

    // 디버깅을 위한 로그
    console.log('🔍 페이지 나가기 체크:', {
        hasUserInput,
        hasSelections,
        hasProfileImage,
        formValues: {
            name: regStore.form.name,
            email: regStore.form.email,
            departmentId: regStore.form.departmentId,
            positionId: regStore.form.positionId,
            jobId: regStore.form.jobId,
            rankId: regStore.form.rankId
        }
    })

    // 위 조건 중 하나라도 해당되면 확인 모달 표시
    if (hasUserInput || hasSelections || hasProfileImage) {
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
/* 모던 컨테이너 */
.modern-container {
    background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
    min-height: 100vh;
    padding: 2rem;
}

/* 페이지 헤더 관련 스타일은 PageHeader 컴포넌트로 이동됨 */

/* 모던 알림 */
.modern-alert {
    background: rgba(255, 255, 255, 0.9) !important;
    color: #64748b !important;
    border: 1px solid rgba(255, 255, 255, 0.2) !important;
}

/* 스크롤 영역 개선 */
.selected-applicants-scroll {
    border: 1px solid rgba(226, 232, 240, 0.5);
    border-radius: 16px;
    background: linear-gradient(135deg, #ffffff 0%, #f8fafc 100%);
    box-shadow: inset 0 2px 8px rgba(0, 0, 0, 0.05);
    backdrop-filter: blur(10px);
}

.selected-applicant {
    background: linear-gradient(135deg, #f1f5f9 0%, #e2e8f0 100%) !important;
    border-left: 4px solid #94a3b8;
    border-radius: 12px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
    margin: 8px;
}

.selected-applicant:hover {
    background: linear-gradient(135deg, #e2e8f0 0%, #cbd5e1 100%) !important;
    transform: translateY(-1px);
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;
}

.v-list-item {
    border-radius: 12px;
    margin-bottom: 8px;
    transition: all 0.3s ease;
    background: rgba(255, 255, 255, 0.7);
    backdrop-filter: blur(5px);
}

.v-list-item:hover {
    background: rgba(248, 250, 252, 0.9);
    transform: translateY(-1px);
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
}

/* 글로벌 카드 스타일 개선 - 입체감 줄임 */
:deep(.v-card) {
    border-radius: 16px !important;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.06) !important;
    border: 1px solid rgba(255, 255, 255, 0.2) !important;
    background: linear-gradient(135deg, #ffffff 0%, #f8fafc 100%) !important;
    backdrop-filter: blur(10px) !important;
    overflow: hidden !important;
}

:deep(.v-card-title) {
    background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%) !important;
    border-bottom: 1px solid rgba(226, 232, 240, 0.3) !important;
    font-weight: 600 !important;
    color: #334155 !important;
    padding: 1.5rem !important;
}

:deep(.v-card-text) {
    padding: 1.5rem !important;
}

/* 버튼 스타일 개선 - 입체감 줄임 */
:deep(.v-btn) {
    border-radius: 10px !important;
    text-transform: none !important;
    font-weight: 500 !important;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08) !important;
    transition: all 0.3s ease !important;
}

:deep(.v-btn:hover) {
    transform: translateY(-1px) !important;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12) !important;
}

:deep(.v-btn--variant-tonal) {
    background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%) !important;
    color: #64748b !important;
    border: 1px solid rgba(226, 232, 240, 0.5) !important;
}

:deep(.v-btn--variant-outlined) {
    background: rgba(255, 255, 255, 0.8) !important;
    border: 1px solid rgba(226, 232, 240, 0.5) !important;
    backdrop-filter: blur(10px) !important;
}

/* 칩 스타일 개선 */
:deep(.v-chip) {
    border-radius: 8px !important;
    background: linear-gradient(135deg, #f1f5f9 0%, #e2e8f0 100%) !important;
    color: #64748b !important;
    border: 1px solid rgba(226, 232, 240, 0.3) !important;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05) !important;
}

:deep(.v-chip--variant-tonal) {
    background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%) !important;
}

/* 입력 필드 스타일 개선 */
:deep(.v-field) {
    border-radius: 12px !important;
    background: rgba(255, 255, 255, 0.9) !important;
    backdrop-filter: blur(10px) !important;
    border: 1px solid rgba(226, 232, 240, 0.5) !important;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05) !important;
    transition: all 0.3s ease !important;
}

:deep(.v-field:hover) {
    border-color: rgba(148, 163, 184, 0.5) !important;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08) !important;
}

:deep(.v-field--focused) {
    border-color: #94a3b8 !important;
    box-shadow: 0 0 0 3px rgba(148, 163, 184, 0.1) !important;
}

/* 아바타 스타일 개선 */
:deep(.v-avatar) {
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1) !important;
    border: 2px solid rgba(255, 255, 255, 0.8) !important;
}

/* 진행 바 스타일 개선 */
:deep(.v-progress-linear) {
    border-radius: 8px !important;
    overflow: hidden !important;
    background: rgba(226, 232, 240, 0.3) !important;
}

/* 구분선 스타일 개선 */
:deep(.v-divider) {
    border-color: rgba(226, 232, 240, 0.3) !important;
}

/* 리스트 스타일 개선 */
:deep(.v-list) {
    background: transparent !important;
}

/* 체크박스 스타일 개선 */
:deep(.v-checkbox .v-selection-control__wrapper) {
    border-radius: 6px !important;
}

/* 셀렉트 메뉴 스타일 개선 */
:deep(.v-overlay__content) {
    border-radius: 16px !important;
    box-shadow: 0 16px 48px rgba(0, 0, 0, 0.15) !important;
    border: 1px solid rgba(255, 255, 255, 0.2) !important;
    backdrop-filter: blur(20px) !important;
}

/* 애니메이션 추가 */
@keyframes fadeInUp {
    from {
        opacity: 0;
        transform: translateY(20px);
    }

    to {
        opacity: 1;
        transform: translateY(0);
    }
}

.v-card {
    animation: fadeInUp 0.6s ease-out;
}

/* 메인 콘텐츠 레이아웃 */
.main-content-row {
    align-items: flex-start;
    margin: 0;
}

.profile-section {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 1rem;
}

/* 네비게이션 섹션 */
.navigation-section {
    display: flex;
    align-items: stretch;
    gap: 0.75rem;
    margin-bottom: 1rem;
    width: 100%;
}

/* 프로필 이미지 관련 스타일은 ProfileImageUpload 컴포넌트로 이동됨 */

/* 등록 섹션 */
.register-section {
    margin-top: 1.5rem;
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 0.75rem;
}

.register-btn {
    width: 180px;
    height: 44px;
    border-radius: 12px !important;
    background: linear-gradient(135deg, #10b981 0%, #059669 100%) !important;
    color: white !important;
    font-weight: 600 !important;
    font-size: 0.9rem !important;
    text-transform: none !important;
    box-shadow: 0 4px 16px rgba(16, 185, 129, 0.25) !important;
    border: none !important;
    transition: all 0.3s ease !important;
}

.register-btn:hover {
    background: linear-gradient(135deg, #059669 0%, #047857 100%) !important;
    transform: translateY(-1px) !important;
    box-shadow: 0 6px 20px rgba(16, 185, 129, 0.35) !important;
}

.progress-info {
    padding: 0.5rem 0.75rem;
    background: rgba(248, 250, 252, 0.8);
    border-radius: 8px;
    border: 1px solid rgba(226, 232, 240, 0.3);
    text-align: center;
}

.progress-text {
    display: flex;
    align-items: center;
    justify-content: center;
    color: #64748b;
    font-size: 0.75rem;
    font-weight: 500;
}

/* 반응형 디자인 */
@media (max-width: 768px) {
    .modern-container {
        padding: 1rem;
    }

    .main-content-row {
        flex-direction: column;
    }

    .profile-section {
        order: 2;
        margin-top: 1rem;
    }

    .navigation-section {
        flex-direction: column;
        gap: 1.5rem;
    }

    .register-btn {
        width: 160px;
        height: 40px;
        font-size: 0.875rem !important;
    }
}

@media (max-width: 480px) {
    .register-btn {
        width: 140px;
        font-size: 0.8rem !important;
    }
}
</style>