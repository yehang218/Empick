<template>
    <!-- 폼 섹션들만 남김 -->
    <div class="form-content">
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
    </div>
</template>

<script setup>
// Vue 3 Composition API - ref 제거

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

// 더 이상 사용하지 않는 코드 제거
</script>

<style scoped>
/* 폼 콘텐츠 */
.form-content {
    width: 100%;
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

/* 등록 관련 스타일 제거됨 (메인 페이지에서 처리) */

/* 반응형 디자인 */
@media (max-width: 960px) {
    .form-section {
        padding: 1rem;
    }

    .section-header {
        flex-direction: column;
        align-items: flex-start;
        text-align: left;
    }
}
</style>