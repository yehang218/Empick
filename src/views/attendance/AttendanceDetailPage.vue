<template>
    <v-container class="pa-6">
        <!-- 헤더 -->
        <div class="d-flex justify-space-between align-center mb-6">
            <div class="d-flex align-center">
                <v-btn icon="mdi-arrow-left" variant="text" @click="goBack" class="mr-2" size="large" />
                <div>
                    <h2 class="text-h5 font-weight-bold text-grey-darken-2">사원 정보 확인</h2>
                    <p class="text-body-2 text-grey-darken-1 ma-0">상세 정보 및 근태 기록을 확인하세요</p>
                </div>
            </div>
            <!-- <v-chip color="primary" variant="outlined" size="large" prepend-icon="mdi-file-document-edit-outline">
                수정요청서 확인
            </v-chip> -->
        </div>

        <v-row>
            <!-- 개인 정보 섹션 -->
            <v-col cols="12">
                <v-card class="mb-6 profile-card" elevation="0">
                    <v-card-title class="profile-header">
                        <v-icon icon="mdi-account" class="mr-2" />
                        <h3 class="text-h6 font-weight-bold">개인 정보</h3>
                    </v-card-title>
                    <v-card-text class="pa-8">
                        <v-row>
                            <!-- 프로필 이미지 -->
                            <v-col cols="12" md="3" class="d-flex justify-center align-center">
                                <div class="profile-section">
                                    <v-avatar size="140" class="profile-avatar mb-4">
                                        <v-img v-if="memberData?.profileImageUrl || memberData?.pictureUrl"
                                            :src="memberData.profileImageUrl || memberData.pictureUrl"
                                            :alt="memberData?.name" @error="handleImageError" @load="handleImageLoad" />
                                        <v-icon v-else icon="mdi-account-circle" size="140" color="grey-lighten-1" />
                                    </v-avatar>
                                    <div class="text-center">
                                        <h4 class="text-h6 font-weight-bold mb-1">{{ memberData?.name }}</h4>
                                        <p class="text-body-2 text-grey-darken-1">{{ memberData?.departmentName }} · {{
                                            memberData?.rankName }}</p>
                                    </div>
                                </div>
                            </v-col>

                            <!-- 개인 정보 필드들 -->
                            <v-col cols="12" md="9">
                                <v-row>
                                    <v-col cols="12" md="6">
                                        <div class="info-field">
                                            <label class="field-label required">
                                                <v-icon icon="mdi-account" size="small" class="mr-1" />
                                                이름
                                            </label>
                                            <v-text-field v-model="memberData.name" variant="outlined" density="compact"
                                                readonly hide-details class="readonly-field" />
                                        </div>
                                    </v-col>
                                    <v-col cols="12" md="6">
                                        <div class="info-field">
                                            <label class="field-label">
                                                <v-icon icon="mdi-calendar" size="small" class="mr-1" />
                                                생년월일
                                            </label>
                                            <v-text-field :model-value="formattedBirthDate" variant="outlined"
                                                density="compact" readonly hide-details class="readonly-field" />
                                        </div>
                                    </v-col>
                                    <v-col cols="12" md="6">
                                        <div class="info-field">
                                            <label class="field-label">
                                                <v-icon icon="mdi-phone" size="small" class="mr-1" />
                                                연락처
                                            </label>
                                            <v-text-field v-model="memberData.phone" variant="outlined"
                                                density="compact" readonly hide-details class="readonly-field" />
                                        </div>
                                    </v-col>
                                    <v-col cols="12" md="6">
                                        <div class="info-field">
                                            <label class="field-label">
                                                <v-icon icon="mdi-email" size="small" class="mr-1" />
                                                이메일
                                            </label>
                                            <v-text-field v-model="memberData.email" variant="outlined"
                                                density="compact" readonly hide-details class="readonly-field" />
                                        </div>
                                    </v-col>
                                    <v-col cols="12">
                                        <div class="info-field">
                                            <label class="field-label">
                                                <v-icon icon="mdi-map-marker" size="small" class="mr-1" />
                                                주소
                                            </label>
                                            <v-text-field v-model="memberData.address" variant="outlined"
                                                density="compact" readonly hide-details class="readonly-field" />
                                        </div>
                                    </v-col>
                                </v-row>
                            </v-col>
                        </v-row>
                    </v-card-text>
                </v-card>
            </v-col>

            <!-- 소속 정보 섹션 -->
            <v-col cols="12">
                <v-card class="mb-6 organization-card" elevation="0">
                    <v-card-title class="organization-header">
                        <v-icon icon="mdi-office-building" class="mr-2" />
                        <h3 class="text-h6 font-weight-bold">소속 정보</h3>
                    </v-card-title>
                    <v-card-text class="pa-8">
                        <v-row>
                            <v-col cols="12" md="6">
                                <div class="info-field">
                                    <label class="field-label required">
                                        <v-icon icon="mdi-domain" size="small" class="mr-1" />
                                        부서
                                    </label>
                                    <v-text-field v-model="memberData.departmentName" variant="outlined"
                                        density="compact" readonly hide-details class="readonly-field" />
                                </div>
                            </v-col>
                            <v-col cols="12" md="6">
                                <div class="info-field">
                                    <label class="field-label required">
                                        <v-icon icon="mdi-account-tie" size="small" class="mr-1" />
                                        직책
                                    </label>
                                    <v-text-field v-model="memberData.rankName" variant="outlined" density="compact"
                                        readonly hide-details class="readonly-field" />
                                </div>
                            </v-col>
                            <v-col cols="12" md="6">
                                <div class="info-field">
                                    <label class="field-label required">
                                        <v-icon icon="mdi-briefcase" size="small" class="mr-1" />
                                        직무
                                    </label>
                                    <v-text-field v-model="memberData.jobName" variant="outlined" density="compact"
                                        readonly hide-details class="readonly-field" />
                                </div>
                            </v-col>
                            <v-col cols="12" md="6">
                                <div class="info-field">
                                    <label class="field-label">
                                        <v-icon icon="mdi-star" size="small" class="mr-1" />
                                        직급
                                    </label>
                                    <v-text-field v-model="memberData.grade" variant="outlined" density="compact"
                                        readonly hide-details class="readonly-field" />
                                </div>
                            </v-col>
                        </v-row>
                    </v-card-text>
                </v-card>
            </v-col>

            <!-- 근태 기록 섹션 -->
            <v-col cols="12">
                <v-card class="attendance-card" elevation="0">
                    <v-card-title class="attendance-header d-flex justify-space-between align-center">
                        <div class="d-flex align-center">
                            <v-icon icon="mdi-clock-outline" class="mr-2" />
                            <h3 class="text-h6 font-weight-bold">근태 기록</h3>
                        </div>
                        <div class="d-flex align-center">
                            <label class="field-label mr-3">
                                <v-icon icon="mdi-calendar-month" size="small" class="mr-1" />
                                조회 기간
                            </label>
                            <v-text-field v-model="selectedMonth" type="month" variant="outlined" density="compact"
                                hide-details style="width: 180px;" @update:modelValue="loadAttendanceData" />
                        </div>
                    </v-card-title>
                    <v-card-text class="pa-8">
                        <!-- 근태 통계 -->
                        <div class="mb-8">
                            <h4 class="text-subtitle-1 font-weight-bold mb-4 text-grey-darken-2">
                                <v-icon icon="mdi-chart-donut" size="small" class="mr-1" />
                                월간 근태 요약
                            </h4>
                            <div class="stats-container">
                                <div class="stats-grid">
                                    <div v-for="stat in attendanceStats" :key="stat.label" class="stat-card">
                                        <div class="stat-circle" :class="stat.color">
                                            <span class="stat-number">{{ stat.count }}</span>
                                        </div>
                                        <div class="stat-label">{{ stat.label }}</div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- 근태 기록 테이블 -->
                        <div>
                            <h4 class="text-subtitle-1 font-weight-bold mb-4 text-grey-darken-2">
                                <v-icon icon="mdi-table" size="small" class="mr-1" />
                                상세 근태 기록
                            </h4>
                            <v-data-table :headers="attendanceHeaders" :items="attendanceRecords"
                                :loading="attendanceLoading" item-key="id" class="attendance-table" :items-per-page="15"
                                :sort-by="[{ key: 'workDate', order: 'desc' }]">

                                <template #item.workDate="{ item }">
                                    <div class="d-flex align-center">
                                        <v-icon icon="mdi-calendar" size="small" class="mr-2 text-grey-darken-1" />
                                        {{ formatDate(item.workDate) }}
                                    </div>
                                </template>

                                <template #item.checkInTime="{ item }">
                                    <div class="d-flex align-center">
                                        <v-icon icon="mdi-login" size="small" class="mr-2 text-green-darken-1" />
                                        {{ formatTime(item.checkInTime) }}
                                    </div>
                                </template>

                                <template #item.checkOutTime="{ item }">
                                    <div class="d-flex align-center">
                                        <v-icon icon="mdi-logout" size="small" class="mr-2 text-red-darken-1" />
                                        {{ formatTime(item.checkOutTime) }}
                                    </div>
                                </template>

                                <template #item.status="{ item }">
                                    <v-chip :color="getAttendanceStatusColor(item.status)" variant="flat" size="small"
                                        class="font-weight-medium">
                                        {{ getAttendanceStatusLabel(item.status) }}
                                    </v-chip>
                                </template>

                                <template #item.workHours="{ item }">
                                    <div class="d-flex align-center">
                                        <v-icon icon="mdi-clock" size="small" class="mr-2 text-blue-darken-1" />
                                        <span class="font-weight-medium">{{ formatWorkHours(item.workHours) }}</span>
                                    </div>
                                </template>

                                <template #item.note="{ item }">
                                    <span class="text-grey-darken-1">{{ item.note || '-' }}</span>
                                </template>

                                <!-- 로딩 상태 -->
                                <template #loading>
                                    <v-skeleton-loader type="table-row@5" />
                                </template>

                                <!-- 데이터 없음 -->
                                <template #no-data>
                                    <div class="text-center py-8">
                                        <v-icon size="64" color="grey-lighten-2">mdi-clock-outline</v-icon>
                                        <div class="text-h6 mt-2 text-grey-darken-1">근태 기록이 없습니다</div>
                                        <div class="text-body-2 text-grey-darken-1">선택한 기간에 등록된 근태 기록이 없습니다</div>
                                    </div>
                                </template>
                            </v-data-table>
                        </div>
                    </v-card-text>
                </v-card>
            </v-col>
        </v-row>

        <!-- 하단 액션 버튼 -->
        <div class="d-flex justify-center mt-8">
            <!-- <v-btn color="primary" size="large" min-width="150" prepend-icon="mdi-pencil" @click="handleEdit"
                class="action-button">
                정보 수정
            </v-btn> -->
        </div>
    </v-container>
</template>

<script setup>
import { computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAttendanceDetail } from '@/composables/useAttendanceDetail'
import {
    ATTENDANCE_HEADERS,
    formatDate,
    formatTime,
    formatWorkHours,
    getAttendanceStatusColor,
    getAttendanceStatusLabel
} from '@/utils/attendanceUtils'

// 라우터 관련
const route = useRoute()
const router = useRouter()

// 사원 ID (라우트 파라미터에서 가져옴)
const memberId = computed(() => route.params.id)

// 비즈니스 로직 (Composable)
const {
    // 상태
    memberData,
    attendanceRecords,
    attendanceLoading,
    selectedMonth,

    // 계산된 값
    formattedBirthDate,
    attendanceStats,

    // 메서드
    loadMemberData,
    loadAttendanceData,
    handleImageError,
    handleImageLoad,
} = useAttendanceDetail(memberId)

// 유틸리티 상수
const attendanceHeaders = ATTENDANCE_HEADERS

// 네비게이션
const goBack = () => {
    router.back()
}

// 라이프사이클
onMounted(() => {
    loadMemberData()
    loadAttendanceData()
})

// 월 변경 감지
watch(selectedMonth, (newMonth) => {
    console.log('📅 월 변경 감지:', newMonth)
    loadAttendanceData()
})
</script>

<style scoped>
/* 카드 스타일 */
.profile-card,
.organization-card,
.attendance-card {
    border: 1px solid #e8eaed;
    border-radius: 16px;
    overflow: hidden;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
    transition: box-shadow 0.3s ease;
}

.profile-card:hover,
.organization-card:hover,
.attendance-card:hover {
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.12);
}

/* 헤더 스타일 */
.profile-header,
.organization-header,
.attendance-header {
    background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
    border-bottom: 1px solid #e8eaed;
    padding: 20px 32px;
    color: #495057;
}

/* 프로필 섹션 */
.profile-section {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
}

.profile-avatar {
    border: 4px solid #ffffff;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
    transition: transform 0.3s ease;
}

.profile-avatar:hover {
    transform: scale(1.05);
}

/* 입력 필드 스타일 */
.info-field {
    margin-bottom: 20px;
}

.field-label {
    display: flex;
    align-items: center;
    font-size: 14px;
    font-weight: 600;
    color: #495057;
    margin-bottom: 8px;
}

.field-label.required::after {
    content: ' *';
    color: #dc3545;
    margin-left: 4px;
}

.readonly-field :deep(.v-field__input) {
    background-color: #f8f9fa;
}

.readonly-field :deep(.v-field__outline) {
    --v-field-border-opacity: 0.3;
}

/* 통계 카드 스타일 */
.stats-container {
    background: linear-gradient(135deg, #f8f9fa 0%, #ffffff 100%);
    border-radius: 12px;
    padding: 24px;
    border: 1px solid #e9ecef;
}

.stats-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
    gap: 20px;
    justify-items: center;
    align-items: center;
    max-width: 800px;
    margin: 0 auto;
}

.stat-card {
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;
    padding: 16px;
    width: 100%;
    max-width: 140px;
}

.stat-circle {
    width: 80px;
    height: 80px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    color: white;
    font-weight: bold;
    font-size: 24px;
    margin-bottom: 12px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.stat-circle:hover {
    transform: translateY(-2px);
    box-shadow: 0 6px 16px rgba(0, 0, 0, 0.2);
}

.stat-circle.success {
    background: linear-gradient(135deg, #28a745 0%, #20c997 100%);
}

.stat-circle.warning {
    background: linear-gradient(135deg, #ffc107 0%, #fd7e14 100%);
}

.stat-circle.info {
    background: linear-gradient(135deg, #007bff 0%, #6f42c1 100%);
}

.stat-circle.error {
    background: linear-gradient(135deg, #dc3545 0%, #e83e8c 100%);
}

.stat-circle.secondary {
    background: linear-gradient(135deg, #6c757d 0%, #495057 100%);
}

.stat-number {
    font-size: 28px;
    font-weight: 700;
}

.stat-label {
    font-size: 13px;
    font-weight: 600;
    color: #495057;
    text-align: center;
    line-height: 1.2;
}

/* 테이블 스타일 */
.attendance-table {
    border-radius: 12px;
    overflow: hidden;
    border: 1px solid #e9ecef;
}

.attendance-table :deep(.v-data-table__wrapper) {
    border-radius: 12px;
}

.attendance-table :deep(.v-data-table-header th) {
    background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
    font-weight: 600;
    color: #495057;
    border-bottom: 1px solid #dee2e6;
    padding: 16px 12px;
}

.attendance-table :deep(tbody tr) {
    transition: background-color 0.2s ease;
}

.attendance-table :deep(tbody tr:hover) {
    background-color: #f8f9fa;
}

.attendance-table :deep(tbody td) {
    padding: 16px 12px;
    vertical-align: middle;
}

/* 액션 버튼 */
.action-button {
    border-radius: 12px;
    font-weight: 600;
    text-transform: none;
    letter-spacing: 0.5px;
    box-shadow: 0 4px 12px rgba(25, 118, 210, 0.3);
    transition: all 0.3s ease;
}

.action-button:hover {
    transform: translateY(-2px);
    box-shadow: 0 6px 16px rgba(25, 118, 210, 0.4);
}

/* 반응형 스타일 */
@media (max-width: 768px) {

    .profile-header,
    .organization-header,
    .attendance-header {
        padding: 16px 20px;
        flex-direction: column;
        gap: 12px;
    }

    .stats-container {
        padding: 16px;
    }

    .stats-grid {
        grid-template-columns: repeat(auto-fit, minmax(100px, 1fr));
        gap: 16px;
        max-width: 600px;
    }

    .stat-card {
        max-width: 120px;
        padding: 12px;
    }

    .stat-circle {
        width: 60px;
        height: 60px;
        font-size: 18px;
    }

    .stat-number {
        font-size: 20px;
    }

    .stat-label {
        font-size: 12px;
    }
}
</style>