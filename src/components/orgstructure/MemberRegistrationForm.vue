<template>
    <v-row class="form-main-row">
        <!-- 프로필 이미지 업로드 영역 -->
        <v-col cols="12" md="3" class="profile-column">
            <div class="profile-image-container">
                <div class="profile-image-wrapper" @click="triggerFileInput">
                    <template v-if="profileImageUrl">
                        <v-img :src="profileImageUrl" class="profile-image" cover />
                    </template>
                    <template v-else>
                        <div class="profile-placeholder">
                            <v-icon size="48" class="placeholder-icon">mdi-camera-plus</v-icon>
                            <p class="placeholder-text">사진 업로드</p>
                        </div>
                    </template>
                    <div class="image-overlay">
                        <v-icon color="white" size="24">mdi-camera-plus</v-icon>
                    </div>
                    <input ref="fileInputRef" type="file" accept="image/jpeg,image/png,image/webp"
                        style="display: none;" @change="onProfileImageChange" />
                </div>
                <v-btn class="upload-btn" :variant="profileImageFile ? 'tonal' : 'outlined'"
                    :color="profileImageFile ? 'success' : 'primary'" @click="triggerFileInput" size="small">
                    <v-icon size="16" class="mr-1">{{ profileImageFile ? 'mdi-check' : 'mdi-upload' }}</v-icon>
                    {{ photoButtonText }}
                </v-btn>

                <!-- 등록 버튼 (사진 업로드 바로 아래) -->
                <div class="register-section">
                    <v-btn class="register-btn" color="success" size="large" variant="flat" @click="onRegister">
                        <v-icon size="18" class="mr-2">mdi-check-circle</v-icon>
                        {{ currentApplicant?.name || '지원자' }} 등록
                    </v-btn>

                    <div v-if="selectedApplicants.length > 1" class="progress-info">
                        <div class="progress-text">
                            <v-icon size="14" class="mr-1">mdi-information-outline</v-icon>
                            현재 편집 중 ({{ currentApplicantIndex + 1 }}/{{ selectedApplicants.length }})
                        </div>
                    </div>
                </div>
            </div>
        </v-col>

        <!-- 입력 폼 -->
        <v-col cols="12" md="9">
            <!-- 기본 정보 섹션 -->
            <div class="form-section">
                <div class="section-header">
                    <div class="section-icon-wrapper">
                        <v-icon class="section-icon">mdi-account</v-icon>
                    </div>
                    <div class="section-title">
                        <h4 class="section-main">기본 정보</h4>
                        <p class="section-sub">사원의 기본적인 개인 정보를 입력하세요</p>
                    </div>
                </div>
                <v-row class="form-fields">
                    <v-col cols="12" sm="6">
                        <v-text-field label="이름" v-model="form.name" required prepend-inner-icon="mdi-account-outline"
                            variant="outlined" />
                    </v-col>
                    <v-col cols="12" sm="6">
                        <v-text-field label="생년월일" v-model="form.birth" type="date" required
                            prepend-inner-icon="mdi-calendar" variant="outlined" />
                    </v-col>
                </v-row>
            </div>

            <!-- 연락처 정보 섹션 -->
            <div class="form-section">
                <div class="section-header">
                    <div class="section-icon-wrapper">
                        <v-icon class="section-icon">mdi-phone</v-icon>
                    </div>
                    <div class="section-title">
                        <h4 class="section-main">연락처 정보</h4>
                        <p class="section-sub">연락 가능한 정보를 입력하세요</p>
                    </div>
                </div>
                <v-row class="form-fields">
                    <v-col cols="12" sm="6">
                        <v-text-field label="연락처" v-model="form.phone" required placeholder="010-1234-5678"
                            prepend-inner-icon="mdi-phone-outline" variant="outlined" />
                    </v-col>
                    <v-col cols="12" sm="6">
                        <v-text-field label="이메일" v-model="form.email" required prepend-inner-icon="mdi-email-outline"
                            variant="outlined" />
                    </v-col>
                    <v-col cols="12">
                        <v-text-field label="주소" v-model="form.address" prepend-inner-icon="mdi-map-marker-outline"
                            variant="outlined" />
                    </v-col>
                </v-row>
            </div>

            <!-- 조직 정보 섹션 -->
            <div class="form-section">
                <div class="section-header">
                    <div class="section-icon-wrapper">
                        <v-icon class="section-icon">mdi-office-building</v-icon>
                    </div>
                    <div class="section-title">
                        <h4 class="section-main">조직 정보</h4>
                        <p class="section-sub">소속 부서와 직책 정보를 선택하세요</p>
                    </div>
                </div>
                <v-row class="form-fields">
                    <v-col cols="12" sm="6">
                        <v-select label="부서" :items="departments" v-model="form.departmentId" item-title="label"
                            item-value="value" required prepend-inner-icon="mdi-domain" variant="outlined" />
                    </v-col>
                    <v-col cols="12" sm="6">
                        <v-select label="직책" :items="positions" v-model="form.positionId" item-title="label"
                            item-value="value" required prepend-inner-icon="mdi-account-tie" variant="outlined" />
                    </v-col>
                    <v-col cols="12" sm="6">
                        <v-select label="직무" :items="jobs" v-model="form.jobId" item-title="label" item-value="value"
                            required prepend-inner-icon="mdi-briefcase-outline" variant="outlined" />
                    </v-col>
                    <v-col cols="12" sm="6">
                        <v-select label="직급" :items="ranks" v-model="form.rankId" item-title="label" item-value="value"
                            required prepend-inner-icon="mdi-star-outline" variant="outlined" />
                    </v-col>
                </v-row>
            </div>
        </v-col>
    </v-row>
</template>

<script setup>
import { ref } from 'vue'

// Props
defineProps({
    form: {
        type: Object,
        required: true
    },
    profileImageUrl: {
        type: String,
        default: ''
    },
    profileImageFile: {
        type: File,
        default: null
    },
    photoButtonText: {
        type: String,
        default: '사진 업로드'
    },
    departments: {
        type: Array,
        default: () => []
    },
    positions: {
        type: Array,
        default: () => []
    },
    jobs: {
        type: Array,
        default: () => []
    },
    ranks: {
        type: Array,
        default: () => []
    },
    selectedApplicants: {
        type: Array,
        default: () => []
    },
    currentApplicantIndex: {
        type: Number,
        default: 0
    },
    currentApplicant: {
        type: Object,
        default: null
    }
})

// Emits
const emit = defineEmits([
    'update:form',
    'profileImageChange',
    'register'
])

// Refs
const fileInputRef = ref(null)

// Methods
const triggerFileInput = () => {
    fileInputRef.value?.click()
}

const onProfileImageChange = (event) => {
    emit('profileImageChange', event)
}

const onRegister = () => {
    emit('register')
}
</script>

<style scoped>
/* 레이아웃 정렬 */
.form-main-row {
    align-items: flex-start;
    margin: 0;
}

.profile-column {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 1rem;
}

/* 프로필 이미지 컨테이너 */
.profile-image-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 1rem;
    width: 100%;
}

.profile-image-wrapper {
    position: relative;
    width: 180px;
    height: 180px;
    border-radius: 20px;
    overflow: hidden;
    cursor: pointer;
    background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
    border: 3px solid rgba(255, 255, 255, 0.8);
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.12);
    transition: all 0.3s ease;
}

.profile-image-wrapper:hover {
    transform: translateY(-4px);
    box-shadow: 0 12px 40px rgba(0, 0, 0, 0.18);
}

.profile-image-wrapper:hover .image-overlay {
    opacity: 1;
}

.profile-image {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.profile-placeholder {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    width: 100%;
    height: 100%;
    background: linear-gradient(135deg, #f1f5f9 0%, #e2e8f0 100%);
}

.placeholder-icon {
    color: #94a3b8;
    margin-bottom: 0.5rem;
}

.placeholder-text {
    color: #64748b;
    font-size: 0.875rem;
    font-weight: 500;
    margin: 0;
}

.image-overlay {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    align-items: center;
    justify-content: center;
    opacity: 0;
    transition: opacity 0.3s ease;
    backdrop-filter: blur(2px);
}

.upload-btn {
    min-width: 140px;
    border-radius: 12px !important;
    font-weight: 600 !important;
    text-transform: none !important;
}

/* 폼 섹션 */
.form-section {
    margin-bottom: 1.5rem;
    background: rgba(255, 255, 255, 0.7);
    border-radius: 16px;
    padding: 1.5rem;
    border: 1px solid rgba(226, 232, 240, 0.3);
    backdrop-filter: blur(10px);
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.05);
    height: fit-content;
}

.section-header {
    display: flex;
    align-items: center;
    gap: 1rem;
    margin-bottom: 1.5rem;
    padding-bottom: 1rem;
    border-bottom: 1px solid rgba(226, 232, 240, 0.3);
}

.section-icon-wrapper {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 40px;
    height: 40px;
    background: linear-gradient(135deg, #f1f5f9 0%, #e2e8f0 100%);
    border-radius: 10px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
    border: 1px solid rgba(226, 232, 240, 0.3);
}

.section-icon {
    color: #64748b;
    font-size: 20px;
}

.section-title {
    flex: 1;
}

.section-main {
    font-size: 1.125rem;
    font-weight: 600;
    color: #334155;
    margin: 0 0 0.25rem 0;
    line-height: 1.3;
}

.section-sub {
    font-size: 0.875rem;
    color: #64748b;
    margin: 0;
    font-weight: 400;
}

.form-fields {
    margin: 0;
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

:deep(.v-field--variant-outlined .v-field__outline) {
    display: none !important;
}

:deep(.v-label) {
    color: #64748b !important;
    font-weight: 500 !important;
}

:deep(.v-field__prepend-inner .v-icon) {
    color: #94a3b8 !important;
    opacity: 1 !important;
}

/* 셀렉트 메뉴 스타일 */
:deep(.v-overlay__content) {
    border-radius: 16px !important;
    box-shadow: 0 16px 48px rgba(0, 0, 0, 0.15) !important;
    border: 1px solid rgba(255, 255, 255, 0.2) !important;
    backdrop-filter: blur(20px) !important;
}

:deep(.v-list-item) {
    border-radius: 8px !important;
    margin: 4px 8px !important;
    transition: all 0.2s ease !important;
}

:deep(.v-list-item:hover) {
    background: rgba(248, 250, 252, 0.8) !important;
    transform: translateX(4px) !important;
}

/* 등록 섹션 (사진 업로드 바로 아래) */
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
@media (max-width: 960px) {
    .profile-image-wrapper {
        width: 140px;
        height: 140px;
    }

    .form-section {
        padding: 1rem;
    }

    .section-header {
        flex-direction: column;
        align-items: flex-start;
        text-align: left;
    }

    .register-btn {
        width: 160px;
        height: 40px;
        font-size: 0.875rem !important;
    }
}

@media (max-width: 600px) {
    .profile-image-wrapper {
        width: 120px;
        height: 120px;
    }

    .upload-btn {
        min-width: 120px;
        font-size: 0.875rem;
    }

    .register-btn {
        width: 140px;
        font-size: 0.8rem !important;
    }
}
</style>