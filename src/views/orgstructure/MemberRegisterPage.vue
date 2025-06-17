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
                    <v-card-title class="d-flex align-center">
                        <v-icon class="mr-2" color="primary">mdi-account-multiple</v-icon>
                        <span>선택된 지원자 정보 ({{ selectedApplicants.length }}명)</span>
                    </v-card-title>

                    <!-- 다중 선택 시 스크롤박스 표시 -->
                    <v-card-text v-if="selectedApplicants.length > 1">
                        <div class="selected-applicants-scroll" style="max-height: 200px; overflow-y: auto;">
                            <v-list density="compact">
                                <v-list-item v-for="(applicant, index) in selectedApplicants"
                                    :key="applicant.applicantId" class="mb-2" @click="selectCurrentApplicant(index)"
                                    :class="{ 'selected-applicant': currentApplicantIndex === index }"
                                    style="cursor: pointer;">
                                    <template v-slot:prepend>
                                        <v-avatar size="40" color="primary">
                                            <span class="text-white">{{ applicant.name?.charAt(0) || '?' }}</span>
                                        </v-avatar>
                                    </template>
                                    <v-list-item-title>{{ applicant.name || '이름 없음' }}</v-list-item-title>
                                    <v-list-item-subtitle>
                                        {{ applicant.email || '이메일 없음' }} | {{ applicant.phone || '연락처 없음' }}
                                    </v-list-item-subtitle>
                                    <template v-slot:append>
                                        <v-chip size="small"
                                            :color="currentApplicantIndex === index ? 'primary' : 'grey'"
                                            variant="tonal">
                                            {{ currentApplicantIndex === index ? '현재 편집중' : '대기' }}
                                        </v-chip>
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
                    {{ selectedApplicants.length > 1 ? `등록 (${currentApplicantIndex + 1}/${selectedApplicants.length})`
                        : '등록'
                    }}
                </v-btn>
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

// 현재 편집중인 지원자
const currentApplicant = computed(() => {
    return selectedApplicants.value[currentApplicantIndex.value] || null
})

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

// 다중 선택 시 네비게이션 함수들
const selectCurrentApplicant = (index) => {
    console.log('👆 지원자 선택:', index)
    currentApplicantIndex.value = index
    loadApplicantToForm(selectedApplicants.value[index])
}

const previousApplicant = () => {
    if (currentApplicantIndex.value > 0) {
        currentApplicantIndex.value--
        loadApplicantToForm(currentApplicant.value)
    }
}

const nextApplicant = () => {
    if (currentApplicantIndex.value < selectedApplicants.value.length - 1) {
        currentApplicantIndex.value++
        loadApplicantToForm(currentApplicant.value)
    }
}

// 현재 지원자 변경 감시
watch(currentApplicant, (newApplicant) => {
    if (newApplicant) {
        console.log('🔄 현재 지원자 변경됨:', newApplicant.name)
    }
})

const triggerFileInput = () => {
    if (fileInputRef.value) fileInputRef.value.click()
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
        regStore.setProfileImage(file)
    } else {
        regStore.clearProfileImage()
    }
}

const onRegister = async () => {
    try {
        const result = await regStore.registerMemberWithImage()
        if (result) {
            const currentName = currentApplicant.value?.name || '지원자'
            toast.success(`${currentName}의 사원 등록이 완료되었습니다!`)

            // 다중 선택 시 다음 지원자로 이동
            if (selectedApplicants.value.length > 1 && currentApplicantIndex.value < selectedApplicants.value.length - 1) {
                nextApplicant()
                regStore.resetForm() // 폼 초기화 후 다음 지원자 데이터 로드
                loadApplicantToForm(currentApplicant.value)
            } else {
                // 모든 지원자 등록 완료 또는 단일 선택 시
                regStore.resetForm()
                if (selectedApplicants.value.length > 1) {
                    toast.success('모든 지원자의 사원 등록이 완료되었습니다!')
                    router.push('/employment/applicants') // 지원자 목록으로 돌아가기
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