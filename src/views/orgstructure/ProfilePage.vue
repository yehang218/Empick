<template>
    <v-container class="profile-page py-8" style="max-width: 1200px;">
        <!-- 페이지 헤더 -->
        <div class="page-header mb-6">
            <div class="d-flex justify-space-between align-center">
                <div>
                    <h1 class="text-h5 font-weight-bold mb-1">사원 정보 관리</h1>
                    <p class="text-body-2 text-medium-emphasis">기본정보 및 소속정보 조회/수정</p>
                </div>
                <div class="d-flex gap-2">
                    <v-btn color="grey-darken-1" variant="outlined" size="small"
                        prepend-icon="mdi-file-document-outline" @click="goToResult">
                        수정요청 결과
                    </v-btn>
                    <v-btn v-if="!isEditing" color="primary" size="small" prepend-icon="mdi-pencil"
                        @click="startEditing">
                        정보 수정
                    </v-btn>
                </div>
            </div>
        </div>

        <!-- 메인 카드 -->
        <v-card class="profile-card" elevation="2">
            <v-card-text class="pa-8">
                <v-row>
                    <!-- 증명사진 섹션 -->
                    <v-col cols="12" md="3" class="d-flex flex-column align-center">
                        <div class="photo-section">
                            <div class="photo-container mb-3">
                                <div class="id-photo">
                                    <v-img :src="profileImageSrc" :alt="`${memberStore.form.name} 증명사진`" cover
                                        class="id-photo-img">
                                        <template v-slot:placeholder>
                                            <div
                                                class="d-flex align-center justify-center fill-height bg-grey-lighten-4">
                                                <div class="text-center">
                                                    <v-icon size="40" color="grey-lighten-1">mdi-account</v-icon>
                                                    <div class="text-caption text-grey-darken-1 mt-1">증명사진</div>
                                                </div>
                                            </div>
                                        </template>
                                    </v-img>
                                </div>

                                <!-- 사진 변경 버튼 -->
                                <v-btn class="photo-change-btn" variant="outlined" size="x-small" color="grey-darken-2"
                                    @click="triggerFileInput">
                                    <v-icon size="12" class="mr-1">mdi-camera</v-icon>
                                    변경
                                </v-btn>
                            </div>

                            <div class="text-center employee-info">
                                <div class="text-subtitle-2 font-weight-bold mb-1">{{ memberStore.form.name || '사용자명' }}
                                </div>
                                <div class="text-caption text-grey-darken-1 mb-2">사번: {{ memberStore.form.employeeNumber
                                }}
                                </div>
                                <v-chip :color="getStatusColor(memberStore.form.status)" size="x-small" variant="flat"
                                    class="status-chip">
                                    {{ getStatusText(memberStore.form.status) }}
                                </v-chip>
                            </div>
                        </div>

                        <input ref="fileInputRef" type="file" accept="image/jpeg,image/png,image/webp"
                            style="display: none;" @change="handleImageChange" />
                    </v-col>

                    <!-- 정보 입력 섹션 -->
                    <v-col cols="12" md="9">
                        <v-form ref="formRef" v-model="isFormValid">
                            <!-- 인적 정보 -->
                            <div class="info-section mb-6">
                                <div class="section-header mb-4">
                                    <v-icon color="grey-darken-2" class="mr-2">mdi-account-circle</v-icon>
                                    <h3 class="text-subtitle-1 font-weight-bold">인적 정보</h3>
                                </div>

                                <v-row>
                                    <v-col cols="12" sm="6">
                                        <v-text-field label="이름" v-model="memberStore.form.name" :disabled="!isEditing"
                                            :rules="validationRules.name" variant="outlined"
                                            prepend-inner-icon="mdi-account" required />
                                    </v-col>
                                    <v-col cols="12" sm="6">
                                        <v-text-field label="사번" v-model="memberStore.form.employeeNumber"
                                            variant="outlined" prepend-inner-icon="mdi-badge-account" disabled
                                            readonly />
                                    </v-col>
                                    <v-col cols="12" sm="6">
                                        <v-text-field label="연락처" v-model="memberStore.form.phone"
                                            :disabled="!isEditing" :rules="validationRules.phone" variant="outlined"
                                            prepend-inner-icon="mdi-phone" placeholder="010-1234-5678" required />
                                    </v-col>
                                    <v-col cols="12" sm="6">
                                        <v-text-field label="이메일" v-model="memberStore.form.email"
                                            :disabled="!isEditing" :rules="validationRules.email" variant="outlined"
                                            prepend-inner-icon="mdi-email" required />
                                    </v-col>
                                </v-row>
                            </div>

                            <!-- 소속 정보 -->
                            <div class="info-section">
                                <div class="section-header mb-4">
                                    <v-icon color="grey-darken-2" class="mr-2">mdi-office-building</v-icon>
                                    <h3 class="text-subtitle-1 font-weight-bold">소속 정보</h3>
                                </div>

                                <v-row>
                                    <v-col cols="12" sm="6">
                                        <v-text-field label="부서" v-model="memberStore.form.departmentName"
                                            variant="outlined" prepend-inner-icon="mdi-domain" disabled readonly />
                                    </v-col>
                                    <v-col cols="12" sm="6">
                                        <v-text-field label="직책" v-model="memberStore.form.positionName"
                                            variant="outlined" prepend-inner-icon="mdi-account-tie" disabled readonly />
                                    </v-col>
                                    <v-col cols="12" sm="6">
                                        <v-text-field label="직무" v-model="memberStore.form.jobName" variant="outlined"
                                            prepend-inner-icon="mdi-briefcase" disabled readonly />
                                    </v-col>
                                    <v-col cols="12" sm="6">
                                        <v-text-field label="직급" v-model="memberStore.form.rankName" variant="outlined"
                                            prepend-inner-icon="mdi-star" disabled readonly />
                                    </v-col>
                                    <v-col cols="12" sm="6">
                                        <v-text-field label="입사일" v-model="formattedHireDate" variant="outlined"
                                            prepend-inner-icon="mdi-calendar" disabled readonly />
                                    </v-col>
                                </v-row>
                            </div>
                        </v-form>
                    </v-col>
                </v-row>
            </v-card-text>

            <!-- 액션 버튼 -->
            <v-card-actions class="px-8 pb-8">
                <v-spacer />
                <div class="d-flex gap-3">
                    <v-btn v-if="isEditing" color="error" variant="outlined" prepend-icon="mdi-close"
                        @click="cancelEditing" :disabled="memberStore.loading">
                        취소
                    </v-btn>
                    <v-btn v-if="isEditing" color="success" prepend-icon="mdi-content-save" @click="handleSaveChanges"
                        :loading="memberStore.loading" :disabled="!isFormValid">
                        저장
                    </v-btn>
                </div>
            </v-card-actions>
        </v-card>

        <!-- 안내사항 -->
        <v-card class="mt-4" elevation="1" variant="outlined">
            <v-card-title class="d-flex align-center text-subtitle-2 py-3">
                <v-icon color="grey-darken-1" size="small" class="mr-2">mdi-information-outline</v-icon>
                안내사항
            </v-card-title>
            <v-card-text class="pt-0">
                <div class="text-body-2 text-grey-darken-2">
                    <div class="mb-2">
                        <span class="font-weight-medium">수정 가능:</span> 이름, 연락처, 이메일
                    </div>
                    <div class="mb-2">
                        <span class="font-weight-medium">수정 불가:</span> 사번, 부서, 직책, 직무, 직급, 입사일
                    </div>
                    <div>
                        <span class="font-weight-medium">증명사진:</span> JPG, PNG, WEBP 형식 (최대 5MB)
                    </div>
                </div>
            </v-card-text>
        </v-card>

        <!-- 토스트 메시지 -->
        <v-snackbar v-model="showSnackbar" :color="snackbarColor" :timeout="3000" location="top right">
            {{ snackbarMessage }}
            <template v-slot:actions>
                <v-btn variant="text" @click="hideToast">
                    닫기
                </v-btn>
            </template>
        </v-snackbar>
    </v-container>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useMemberStore } from '@/stores/memberStore'
import { useRouter } from 'vue-router'

// Composables
import { useProfileForm } from '@/composables/useProfileForm'
import { useProfileImage } from '@/composables/useProfileImage'
import { useToast } from '@/composables/useToast'

// Utils
import { getStatusColor, getStatusText, formatHireDate } from '@/utils/memberUtils'

const memberStore = useMemberStore()
const router = useRouter()

// Composables 사용
const {
    formRef,
    isEditing,
    isFormValid,
    validationRules,
    startEditing,
    cancelEditing,
    saveChanges: saveFormChanges
} = useProfileForm()

const {
    fileInputRef,
    profileImageSrc,
    triggerFileInput,
    uploadProfileImage,
    loadProfileImage
} = useProfileImage()

const {
    showSnackbar,
    snackbarMessage,
    snackbarColor,
    showSuccess,
    showError,
    hideToast
} = useToast()

// 계산된 속성
const formattedHireDate = computed(() => formatHireDate(memberStore.form.hireAt))

// 이벤트 핸들러
const handleSaveChanges = async () => {
    try {
        await saveFormChanges()
        showSuccess('프로필 정보가 성공적으로 수정되었습니다')
    } catch (error) {
        console.error('프로필 수정 실패:', error)
        showError(error.message || '프로필 정보 수정에 실패했습니다')
    }
}

const handleImageChange = async (event) => {
    const file = event.target.files?.[0]
    if (!file) return

    try {
        await uploadProfileImage(file, memberStore.form.employeeNumber, memberStore.form.id)
        showSuccess('프로필 이미지가 성공적으로 업로드되었습니다')
    } catch (error) {
        console.error('프로필 이미지 업로드 실패:', error)
        showError(error.message || '프로필 이미지 업로드에 실패했습니다')
    }
}

const goToResult = () => {
    router.push('/orgstructure/profile/result')
}

// 생명주기 훅
onMounted(async () => {
    try {
        await memberStore.getMyInfo()
        await loadProfileImage(memberStore.form.id)
    } catch (error) {
        console.error('프로필 정보 로드 실패:', error)
        showError('프로필 정보를 불러오는데 실패했습니다')
    }
})
</script>

<style scoped>
.profile-page {
    background-color: #fafafa;
    min-height: 100vh;
}

.page-header {
    background-color: white;
    color: #424242;
    padding: 1.5rem 2rem;
    border-radius: 4px;
    border: 1px solid #e0e0e0;
    margin-bottom: 1.5rem;
}

.profile-card {
    border-radius: 4px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
    border: 1px solid #e0e0e0;
}

/* 증명사진 스타일 */
.photo-section {
    position: sticky;
    top: 2rem;
}

.photo-container {
    position: relative;
    display: inline-block;
}

.id-photo {
    width: 140px;
    height: 180px;
    border: 2px solid #424242;
    border-radius: 4px;
    overflow: hidden;
    background-color: white;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
    position: relative;
}

.id-photo-img {
    width: 100%;
    height: 100%;
}

.photo-change-btn {
    position: absolute;
    bottom: -12px;
    left: 50%;
    transform: translateX(-50%);
    background-color: white !important;
    border: 1px solid #bdbdbd !important;
    font-size: 10px !important;
    min-width: 50px !important;
    height: 24px !important;
}

.employee-info {
    max-width: 140px;
}

.status-chip {
    font-size: 11px !important;
    height: 20px !important;
    font-weight: 500 !important;
}

.section-header {
    display: flex;
    align-items: center;
    padding-bottom: 0.75rem;
    border-bottom: 1px solid #bdbdbd;
    margin-bottom: 1rem;
}

.info-section {
    background-color: white;
    padding: 1.5rem;
    border-radius: 4px;
    border: 1px solid #e0e0e0;
}

.v-text-field {
    margin-bottom: 0.5rem;
}

.v-text-field--disabled {
    opacity: 0.6;
}

/* 반응형 디자인 */
@media (max-width: 960px) {
    .photo-section {
        position: static;
        margin-bottom: 2rem;
    }

    .page-header {
        padding: 1rem 1.5rem;
    }

    .page-header .d-flex {
        flex-direction: column;
        gap: 1rem;
        align-items: stretch !important;
    }

    .id-photo {
        width: 120px;
        height: 160px;
    }

    .employee-info {
        max-width: 120px;
    }
}

@media (max-width: 600px) {
    .id-photo {
        width: 100px;
        height: 140px;
    }

    .employee-info {
        max-width: 100px;
    }

    .info-section {
        padding: 1rem;
    }

    .v-card-text {
        padding: 1rem !important;
    }

    .v-card-actions {
        padding: 1rem !important;
    }
}

/* 사무적인 느낌의 미니멀한 애니메이션 */
.profile-card {
    transition: box-shadow 0.2s ease;
}

.profile-card:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12);
}

.photo-change-btn {
    transition: all 0.2s ease;
}

.photo-change-btn:hover {
    background-color: #f5f5f5 !important;
    border-color: #9e9e9e !important;
}

.info-section {
    transition: none;
}

/* 폼 필드 스타일 개선 */
.v-text-field .v-field {
    border-radius: 4px;
}

.v-text-field--variant-outlined .v-field {
    background-color: white;
}

.v-text-field--disabled .v-field {
    background-color: #f8f8f8;
}
</style>