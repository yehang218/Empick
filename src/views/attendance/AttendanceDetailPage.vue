<template>
    <v-container class="pa-6">
        <!-- 헤더 -->
        <div class="d-flex justify-space-between align-center mb-6">
            <div class="d-flex align-center">
                <v-btn icon="mdi-arrow-left" variant="text" @click="goBack" class="mr-2" />
                <h2 class="text-h5 font-weight-bold">사원 정보 확인</h2>
                <v-chip color="primary" variant="outlined" class="ml-3">수정요청서 확인</v-chip>
            </div>
        </div>

        <v-row>
            <!-- 개인 정보 섹션 -->
            <v-col cols="12">
                <v-card class="mb-6" elevation="2">
                    <v-card-title class="bg-grey-lighten-4 py-3">
                        <h3 class="text-h6 font-weight-bold">개인 정보</h3>
                    </v-card-title>
                    <v-card-text class="pa-6">
                        <v-row>
                            <!-- 프로필 이미지 -->
                            <v-col cols="12" md="2" class="d-flex justify-center">
                                <v-avatar size="120" class="profile-avatar">
                                    <v-img v-if="memberData?.profileImageUrl || memberData?.pictureUrl"
                                        :src="memberData.profileImageUrl || memberData.pictureUrl"
                                        :alt="memberData?.name" @error="handleImageError" @load="handleImageLoad" />
                                    <v-icon v-else icon="mdi-account-circle" size="120" color="grey-lighten-1" />
                                </v-avatar>
                            </v-col>

                            <!-- 개인 정보 필드들 -->
                            <v-col cols="12" md="10">
                                <v-row>
                                    <v-col cols="12" md="6">
                                        <div class="info-field">
                                            <label class="field-label required">이름</label>
                                            <v-text-field v-model="memberData.name" variant="outlined" density="compact"
                                                readonly hide-details />
                                        </div>
                                    </v-col>
                                    <v-col cols="12" md="6">
                                        <div class="info-field">
                                            <label class="field-label">생년월일</label>
                                            <v-text-field :model-value="formattedBirthDate" variant="outlined"
                                                density="compact" readonly hide-details />
                                        </div>
                                    </v-col>
                                    <v-col cols="12" md="6">
                                        <div class="info-field">
                                            <label class="field-label">연락처</label>
                                            <v-text-field v-model="memberData.phone" variant="outlined"
                                                density="compact" readonly hide-details />
                                        </div>
                                    </v-col>
                                    <v-col cols="12" md="6">
                                        <div class="info-field">
                                            <label class="field-label">이메일</label>
                                            <v-text-field v-model="memberData.email" variant="outlined"
                                                density="compact" readonly hide-details />
                                        </div>
                                    </v-col>
                                    <v-col cols="12">
                                        <div class="info-field">
                                            <label class="field-label">주소</label>
                                            <v-text-field v-model="memberData.address" variant="outlined"
                                                density="compact" readonly hide-details />
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
                <v-card class="mb-6" elevation="2">
                    <v-card-title class="bg-grey-lighten-4 py-3">
                        <h3 class="text-h6 font-weight-bold">소속 정보</h3>
                    </v-card-title>
                    <v-card-text class="pa-6">
                        <v-row>
                            <v-col cols="12" md="6">
                                <div class="info-field">
                                    <label class="field-label required">부서</label>
                                    <v-text-field v-model="memberData.departmentName" variant="outlined"
                                        density="compact" readonly hide-details />
                                </div>
                            </v-col>
                            <v-col cols="12" md="6">
                                <div class="info-field">
                                    <label class="field-label required">직책</label>
                                    <v-text-field v-model="memberData.rankName" variant="outlined" density="compact"
                                        readonly hide-details />
                                </div>
                            </v-col>
                            <v-col cols="12" md="6">
                                <div class="info-field">
                                    <label class="field-label required">직무</label>
                                    <v-text-field v-model="memberData.jobName" variant="outlined" density="compact"
                                        readonly hide-details />
                                </div>
                            </v-col>
                            <v-col cols="12" md="6">
                                <div class="info-field">
                                    <label class="field-label">직급</label>
                                    <v-text-field v-model="memberData.grade" variant="outlined" density="compact"
                                        readonly hide-details />
                                </div>
                            </v-col>
                        </v-row>
                    </v-card-text>
                </v-card>
            </v-col>

            <!-- 근태 기록 섹션 -->
            <v-col cols="12">
                <v-card elevation="2">
                    <v-card-title class="bg-grey-lighten-4 py-3 d-flex justify-space-between align-center">
                        <h3 class="text-h6 font-weight-bold">근태 기록</h3>
                        <div class="d-flex align-center">
                            <label class="field-label mr-2">조회 기간:</label>
                            <v-text-field v-model="selectedMonth" type="month" variant="outlined" density="compact"
                                hide-details style="width: 150px;" @update:modelValue="loadAttendanceData" />
                        </div>
                    </v-card-title>
                    <v-card-text class="pa-6">
                        <!-- 근태 통계 원형 차트 -->
                        <div class="mb-6">
                            <v-row class="text-center">
                                <v-col cols="12" md="2" v-for="stat in attendanceStats" :key="stat.label">
                                    <div class="stat-circle">
                                        <div class="circle-chart" :class="stat.color">
                                            <span class="circle-number">{{ stat.count }}</span>
                                        </div>
                                        <div class="stat-label mt-2">{{ stat.label }}</div>
                                    </div>
                                </v-col>
                            </v-row>
                        </div>

                        <!-- 근태 기록 테이블 -->
                        <v-data-table :headers="attendanceHeaders" :items="attendanceRecords"
                            :loading="attendanceLoading" item-key="id" class="attendance-table" :items-per-page="10">
                            <template #item.workDate="{ item }">
                                {{ formatDate(item.workDate) }}
                            </template>
                            <template #item.checkInTime="{ item }">
                                {{ formatTime(item.checkInTime) }}
                            </template>
                            <template #item.checkOutTime="{ item }">
                                {{ formatTime(item.checkOutTime) }}
                            </template>
                            <template #item.status="{ item }">
                                <v-chip :color="getAttendanceStatusColor(item.status)" variant="flat" size="small">
                                    {{ getAttendanceStatusLabel(item.status) }}
                                </v-chip>
                            </template>
                            <template #item.workHours="{ item }">
                                {{ formatWorkHours(item.workHours) }}
                            </template>
                            <template #item.note="{ item }">
                                {{ item.note || '-' }}
                            </template>
                        </v-data-table>
                    </v-card-text>
                </v-card>
            </v-col>
        </v-row>

        <!-- 하단 버튼 -->
        <div class="text-center mt-6">
            <v-btn color="primary" size="large" min-width="120" @click="handleEdit">
                수정
            </v-btn>
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
    handleEdit
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
watch(selectedMonth, () => {
    loadAttendanceData()
})
</script>

<style scoped>
.profile-avatar {
    border: 3px solid #e0e0e0;
}

.info-field {
    margin-bottom: 16px;
}

.field-label {
    display: block;
    font-size: 14px;
    font-weight: 500;
    color: #424242;
    margin-bottom: 8px;
}

.field-label.required::after {
    content: ' *';
    color: #f44336;
}

.stat-circle {
    display: flex;
    flex-direction: column;
    align-items: center;
}

.circle-chart {
    width: 80px;
    height: 80px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    color: white;
    font-weight: bold;
    font-size: 24px;
}

.circle-chart.success {
    background-color: #4caf50;
}

.circle-chart.warning {
    background-color: #ff9800;
}

.circle-chart.info {
    background-color: #2196f3;
}

.circle-chart.error {
    background-color: #f44336;
}

.circle-chart.secondary {
    background-color: #9c27b0;
}

.stat-label {
    font-size: 14px;
    font-weight: 500;
    color: #666;
}

.attendance-table :deep(.v-data-table__wrapper) {
    border-radius: 8px;
}

.attendance-table :deep(.v-data-table-header th) {
    background-color: #f5f5f5;
    font-weight: 600;
}
</style>