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
                <v-card class="mb-6" elevation="2">
                    <v-card-title class="d-flex align-center justify-space-between">
                        <div class="d-flex align-center">
                            <v-icon class="mr-2" color="primary">mdi-account-multiple</v-icon>
                            <span>선택된 지원자 정보 ({{ selectedApplicants.length }}명)</span>
                        </div>
                        <!-- 다중 선택 시 전체 선택/해제 및 일괄 등록 버튼 -->
                        <div v-if="selectedApplicants.length > 1" class="d-flex align-center">
                            <v-checkbox v-model="selectAllForRegistration" :indeterminate="isIndeterminate"
                                label="전체 선택" hide-details density="compact" class="mr-4"
                                @update:model-value="toggleSelectAllForRegistration" />
                            <v-btn color="success" variant="tonal" size="small"
                                :disabled="selectedForRegistration.length === 0" @click="onBulkRegister">
                                일괄 등록 ({{ selectedForRegistration.length }}명)
                            </v-btn>
                        </div>
                    </v-card-title>

                    <!-- 다중 선택 시 스크롤박스 표시 -->
                    <v-card-text v-if="selectedApplicants.length > 1">
                        <div class="selected-applicants-scroll" style="max-height: 200px; overflow-y: auto;">
                            <v-list density="compact">
                                <v-list-item v-for="(applicant, index) in selectedApplicants"
                                    :key="applicant.applicantId" class="mb-2"
                                    :class="{ 'selected-applicant': currentApplicantIndex === index }"
                                    style="cursor: pointer;">
                                    <template v-slot:prepend>
                                        <v-checkbox :model-value="isSelectedForRegistration(applicant)"
                                            @update:model-value="toggleRegistrationSelection(applicant)" hide-details
                                            density="compact" class="mr-2" @click.stop />
                                        <v-avatar size="40" color="primary" @click="selectCurrentApplicant(index)">
                                            <span class="text-white">{{ applicant.name?.charAt(0) || '?' }}</span>
                                        </v-avatar>
                                    </template>
                                    <div @click="selectCurrentApplicant(index)" class="flex-grow-1">
                                        <v-list-item-title>{{ applicant.name || '이름 없음' }}</v-list-item-title>
                                        <v-list-item-subtitle>
                                            {{ applicant.email || '이메일 없음' }} | {{ applicant.phone || '연락처 없음' }}
                                        </v-list-item-subtitle>
                                        <!-- 등록 진행 상황 프로그레스바 -->
                                        <div v-if="registrationProgress[applicant.applicantId]" class="mt-2">
                                            <v-progress-linear
                                                :model-value="registrationProgress[applicant.applicantId].progress"
                                                :color="getProgressColor(registrationProgress[applicant.applicantId].status)"
                                                height="4" rounded />
                                            <div class="text-caption mt-1"
                                                :class="getProgressTextColor(registrationProgress[applicant.applicantId].status)">
                                                {{ registrationProgress[applicant.applicantId].message }}
                                            </div>
                                        </div>
                                    </div>
                                    <template v-slot:append>
                                        <div class="d-flex flex-column align-center">
                                            <v-chip size="small"
                                                :color="currentApplicantIndex === index ? 'primary' : 'grey'"
                                                variant="tonal" class="mb-1">
                                                {{ currentApplicantIndex === index ? '현재 편집중' : '대기' }}
                                            </v-chip>
                                            <v-chip v-if="isSelectedForRegistration(applicant)" size="x-small"
                                                color="success" variant="tonal">
                                                등록 대상
                                            </v-chip>
                                            <!-- 등록 상태 칩 -->
                                            <v-chip v-if="registrationProgress[applicant.applicantId]" size="x-small"
                                                :color="getStatusChipColor(registrationProgress[applicant.applicantId].status)"
                                                variant="tonal" class="mt-1">
                                                {{ getStatusText(registrationProgress[applicant.applicantId].status) }}
                                            </v-chip>
                                        </div>
                                    </template>
                                </v-list-item>
                            </v-list>
                        </div>
                        <v-divider class="my-4"></v-divider>
                        <div class="d-flex align-center justify-space-between">
                            <span class="text-subtitle-2 text-grey">현재 편집중인 지원자:</span>
                            <v-chip color="primary" variant="tonal">
                                {{ currentApplicant?.name || '선택된 지원자 없음' }}
                            </v-chip>
                        </div>
                        <div v-if="selectedForRegistration.length > 0"
                            class="mt-2 d-flex align-center justify-space-between">
                            <span class="text-subtitle-2 text-success">등록 대상 지원자:</span>
                            <v-chip color="success" variant="tonal">
                                {{selectedForRegistration.map(a => a.name).join(', ')}}
                            </v-chip>
                        </div>
                    </v-card-text>

                    <!-- 단일 선택 시 간단한 정보만 표시 -->
                    <v-card-text v-else>
                        <div class="d-flex align-center">
                            <v-avatar size="48" color="primary" class="mr-4">
                                <span class="text-white text-h6">{{ selectedApplicants[0]?.name?.charAt(0) || '?'
                                    }}</span>
                            </v-avatar>
                            <div>
                                <div class="text-h6">{{ selectedApplicants[0]?.name || '이름 없음' }}</div>
                                <div class="text-body-2 text-grey">
                                    {{ selectedApplicants[0]?.email || '이메일 없음' }} | {{ selectedApplicants[0]?.phone ||
                                        '연락처 없음' }}
                                </div>
                            </div>
                        </div>
                    </v-card-text>
                </v-card>
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
        <v-row>
            <!-- 프로필 이미지 업로드 영역 -->
            <v-col cols="2" class="d-flex flex-column align-center">
                <v-sheet width="160" height="160" elevation="1" rounded class="d-flex align-center justify-center mb-4"
                    color="#D3D3D3" style="position: relative; overflow: hidden; cursor: pointer;"
                    @click="triggerFileInput">
                    <template v-if="regStore.profileImageUrl">
                        <v-img :src="regStore.profileImageUrl" width="160" height="160" cover />
                    </template>
                    <template v-else>
                        <v-icon size="48" color="grey darken-2">mdi-camera</v-icon>
                    </template>
                    <input ref="fileInputRef" type="file" accept="image/jpeg,image/png,image/webp"
                        style="display: none;" @change="onProfileImageChange" />
                </v-sheet>
                <v-btn :color="regStore.profileImageFile ? 'success' : 'primary'" @click="triggerFileInput"
                    style="width: 100px;">{{
                        regStore.photoButtonText }}</v-btn>
            </v-col>
            <!-- 입력 폼 -->
            <v-col cols="10">
                <v-row>
                    <v-col cols="6">
                        <v-text-field label="이름" v-model="regStore.form.name" required />
                    </v-col>
                    <v-col cols="6">
                        <v-text-field label="생년월일" v-model="regStore.form.birth" type="date" required />
                    </v-col>
                    <v-col cols="6">
                        <v-text-field label="연락처" v-model="regStore.form.phone" required placeholder="010-1234-5678" />
                    </v-col>
                    <v-col cols="6">
                        <v-text-field label="이메일" v-model="regStore.form.email" required />
                    </v-col>
                    <v-col cols="12">
                        <v-text-field label="주소" v-model="regStore.form.address" />
                    </v-col>
                    <v-col cols="6">
                        <v-select label="부서" :items="departments" v-model="regStore.form.departmentId"
                            item-title="label" item-value="value" required />
                    </v-col>
                    <v-col cols="6">
                        <v-select label="직책" :items="positions" v-model="regStore.form.positionId" item-title="label"
                            item-value="value" required />
                    </v-col>
                    <v-col cols="6">
                        <v-select label="직무" :items="jobs" v-model="regStore.form.jobId" item-title="label"
                            item-value="value" required />
                    </v-col>
                    <v-col cols="6">
                        <v-select label="직급" :items="ranks" v-model="regStore.form.rankId" item-title="label"
                            item-value="value" required />
                    </v-col>
                </v-row>
            </v-col>
        </v-row>

        <!-- 다중 선택 시 네비게이션 버튼 -->
        <v-row v-if="selectedApplicants.length > 1" class="mt-4">
            <v-col cols="12" class="d-flex justify-center align-center">
                <v-btn @click="previousApplicant" :disabled="currentApplicantIndex === 0" variant="outlined"
                    class="mr-4">
                    <v-icon>mdi-chevron-left</v-icon>
                    이전
                </v-btn>
                <span class="mx-4 text-body-1">
                    {{ currentApplicantIndex + 1 }} / {{ selectedApplicants.length }}
                </span>
                <v-btn @click="nextApplicant" :disabled="currentApplicantIndex === selectedApplicants.length - 1"
                    variant="outlined" class="ml-4">
                    다음
                    <v-icon>mdi-chevron-right</v-icon>
                </v-btn>
            </v-col>
        </v-row>

        <v-row class="mt-8">
            <v-col cols="12" class="d-flex flex-column align-center">
                <v-btn color="success" @click="onRegister" size="large">
                    {{ currentApplicant?.name || '지원자' }} 등록
                </v-btn>
                <div v-if="selectedApplicants.length > 1" class="text-caption text-grey mt-2">
                    현재 편집 중인 지원자만 등록됩니다 ({{ currentApplicantIndex + 1 }}/{{ selectedApplicants.length }})
                </div>
            </v-col>
        </v-row>

        <!-- 확인 모달 -->
        <AlertModal v-if="showConfirmDialog" message="입력하신 내용이 모두 삭제됩니다. 정말로 나가시겠습니까?" @confirm="confirmLeave"
            @cancel="cancelLeave" />
    </v-container>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useMemberRegisterStore } from '@/stores/memberRegisterStore'
import { useToast } from 'vue-toastification'
import { useRouter, useRoute, onBeforeRouteLeave } from 'vue-router'
import AlertModal from '@/components/common/AlertModal.vue'

const regStore = useMemberRegisterStore()
const toast = useToast()
const fileInputRef = ref(null)
const router = useRouter()
const route = useRoute()
const showConfirmDialog = ref(false)
const pendingNavigation = ref(null)

// 선택된 지원자 관련 상태
const selectedApplicants = ref([])
const currentApplicantIndex = ref(0)

// 다중 등록 관련 상태
const selectAllForRegistration = ref(false)
const selectedForRegistration = ref([])

// 지원자별 폼 데이터 저장소
const applicantFormData = ref(new Map())

// 등록 진행 상황 관리
const registrationProgress = ref({})

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

// 등록 진행 상황 관련 함수들
const setRegistrationProgress = (applicantId, status, progress, message) => {
    registrationProgress.value[applicantId] = {
        status,
        progress,
        message
    }
}

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

// 현재 폼 데이터 저장 함수
const saveCurrentFormData = () => {
    if (currentApplicant.value) {
        const currentFormData = {
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
        applicantFormData.value.set(currentApplicant.value.applicantId, currentFormData)
        console.log('💾 폼 데이터 저장됨:', currentApplicant.value.name, currentFormData)
    }
}

// 저장된 폼 데이터 복원 함수
const restoreFormData = (applicant) => {
    const savedData = applicantFormData.value.get(applicant.applicantId)

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

const departments = [
    { label: '인사', value: 1 },
    { label: '개발', value: 2 },
    { label: '영업', value: 3 },
    { label: '기타', value: 4 },
]
const positions = [
    { label: '미지정', value: 0 },
    { label: '팀장', value: 1 },
    { label: '부장', value: 2 },
]
const jobs = [
    { label: '미지정', value: 0 },
    { label: '개발자', value: 1 },
    { label: '디자이너', value: 2 },
]
const ranks = [
    { label: '사원', value: 0 },
    { label: '대리', value: 1 },
    { label: '과장', value: 2 },
]

// 컴포넌트 마운트 시 쿼리 파라미터에서 지원자 데이터 로드
onMounted(() => {
    console.log('🚀 MemberRegisterPage 마운트됨')
    console.log('🔍 route.query:', route.query)

    if (route.query.applicants) {
        try {
            const applicantsData = JSON.parse(route.query.applicants)
            console.log('📋 파싱된 지원자 데이터:', applicantsData)

            if (Array.isArray(applicantsData) && applicantsData.length > 0) {
                selectedApplicants.value = applicantsData
                currentApplicantIndex.value = 0

                // 첫 번째 지원자 데이터로 폼 초기화
                loadApplicantToForm(applicantsData[0])

                // 다중 선택 시 기본적으로 모든 지원자를 등록 대상으로 선택
                if (applicantsData.length > 1) {
                    selectedForRegistration.value = [...applicantsData]
                    selectAllForRegistration.value = true
                }

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

// 다중 선택 시 네비게이션 함수들 (수정됨)
const selectCurrentApplicant = (index) => {
    console.log('👆 지원자 선택:', index)

    // 현재 폼 데이터 저장
    saveCurrentFormData()

    // 지원자 변경
    currentApplicantIndex.value = index

    // 새 지원자의 폼 데이터 복원
    restoreFormData(selectedApplicants.value[index])
}

const previousApplicant = () => {
    if (currentApplicantIndex.value > 0) {
        // 현재 폼 데이터 저장
        saveCurrentFormData()

        currentApplicantIndex.value--

        // 이전 지원자의 폼 데이터 복원
        restoreFormData(currentApplicant.value)
    }
}

const nextApplicant = () => {
    if (currentApplicantIndex.value < selectedApplicants.value.length - 1) {
        // 현재 폼 데이터 저장
        saveCurrentFormData()

        currentApplicantIndex.value++

        // 다음 지원자의 폼 데이터 복원
        restoreFormData(currentApplicant.value)
    }
}

// 다중 등록 관련 함수들
const toggleSelectAllForRegistration = (selectAll) => {
    console.log('🔄 전체 등록 선택 토글:', selectAll)
    if (selectAll) {
        selectedForRegistration.value = [...selectedApplicants.value]
    } else {
        selectedForRegistration.value = []
    }
}

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

const isSelectedForRegistration = (applicant) => {
    return selectedForRegistration.value.some(a => a.applicantId === applicant.applicantId)
}

const onBulkRegister = async () => {
    console.log('📝 일괄 등록 시작:', selectedForRegistration.value.length, '명')

    if (selectedForRegistration.value.length === 0) {
        toast.warning('등록할 지원자를 선택해주세요.')
        return
    }

    // 현재 폼 데이터 저장
    saveCurrentFormData()

    // 진행 상황 초기화
    registrationProgress.value = {}

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
            const savedData = applicantFormData.value.get(applicant.applicantId)
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
                applicantFormData.value.delete(applicant.applicantId)
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

// 현재 지원자 변경 감시
watch(currentApplicant, (newApplicant) => {
    if (newApplicant) {
        console.log('🔄 현재 지원자 변경됨:', newApplicant.name)
    }
})

const triggerFileInput = () => {
    if (fileInputRef.value) {
        // 같은 파일명 선택 시에도 onChange 이벤트가 발생하도록 value 초기화
        fileInputRef.value.value = ''
        fileInputRef.value.click()
    }
}

const onProfileImageChange = (event) => {
    const file = event.target.files && event.target.files[0]
    if (file) {
        const allowedTypes = ['image/jpeg', 'image/png', 'image/webp']
        if (!allowedTypes.includes(file.type)) {
            toast.error('JPG, PNG, WEBP 형식의 이미지만 업로드할 수 있습니다.')
            return
        }
        if (file.size > 5 * 1024 * 1024) { // 5MB 제한
            toast.error('파일 크기는 5MB를 초과할 수 없습니다.')
            return
        }

        console.log('📷 프로필 이미지 선택됨:', file.name, 'size:', file.size)
        regStore.setProfileImage(file)

        // 현재 지원자의 이미지 정보를 즉시 저장
        if (currentApplicant.value) {
            saveCurrentFormData()
        }
    } else {
        console.log('📷 프로필 이미지 선택 취소됨')
        regStore.clearProfileImage()

        // 현재 지원자의 이미지 정보를 즉시 저장
        if (currentApplicant.value) {
            saveCurrentFormData()
        }
    }
}

const onRegister = async () => {
    try {
        // 현재 폼 데이터 저장
        saveCurrentFormData()

        const result = await regStore.registerMemberWithImage()
        if (result) {
            const currentName = currentApplicant.value?.name || '지원자'
            toast.success(`${currentName}의 사원 등록이 완료되었습니다!`)

            // 등록 완료된 지원자의 저장된 데이터 삭제
            if (currentApplicant.value) {
                applicantFormData.value.delete(currentApplicant.value.applicantId)
            }

            // 다중 선택 시 다음 지원자로 이동
            if (selectedApplicants.value.length > 1 && currentApplicantIndex.value < selectedApplicants.value.length - 1) {
                nextApplicant()
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