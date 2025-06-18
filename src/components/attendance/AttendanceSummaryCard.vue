<template>
    <v-card flat class="ma-4" style="border: 1px solid #e0e0e0; border-radius: 8px;">
        <v-card-text class="py-4">
            <div class="d-flex flex-wrap" style="gap: 24px;">
                <!-- 기본 정보 -->
                <div class="flex-1" style="min-width: 200px;">
                    <h4 class="text-subtitle-1 font-weight-bold mb-3 text-primary">기본 정보</h4>
                    <div class="d-flex align-start gap-3">
                        <!-- 프로필 이미지 -->
                        <v-avatar size="60" class="mt-1">
                            <v-img v-if="member.profileImageUrl || member.pictureUrl"
                                :src="member.profileImageUrl || member.pictureUrl" :alt="member.name" />
                            <v-icon v-else icon="mdi-account-circle" size="60" color="grey-lighten-1" />
                        </v-avatar>

                        <!-- 정보 -->
                        <div class="info-grid flex-1">
                            <div class="info-item">
                                <span class="info-label">사원번호:</span>
                                <span class="info-value">{{ member.employeeNumber }}</span>
                            </div>
                            <div class="info-item">
                                <span class="info-label">이름:</span>
                                <span class="info-value">{{ member.name }}</span>
                            </div>
                            <div class="info-item">
                                <span class="info-label">직급:</span>
                                <span class="info-value">{{ member.rankName || '-' }}</span>
                            </div>
                            <div class="info-item">
                                <span class="info-label">입사일:</span>
                                <span class="info-value">{{ formatDate(member.hireAt) }}</span>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- 연락처 정보 -->
                <div class="flex-1" style="min-width: 200px;">
                    <h4 class="text-subtitle-1 font-weight-bold mb-3 text-primary">연락처 정보</h4>
                    <div class="info-grid">
                        <div class="info-item">
                            <span class="info-label">이메일:</span>
                            <span class="info-value">{{ member.email }}</span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">전화번호:</span>
                            <span class="info-value">{{ member.phone }}</span>
                        </div>
                    </div>
                </div>

                <!-- 조직 정보 -->
                <div class="flex-1" style="min-width: 200px;">
                    <h4 class="text-subtitle-1 font-weight-bold mb-3 text-primary">조직 정보</h4>
                    <div class="info-grid">
                        <div class="info-item">
                            <span class="info-label">부서:</span>
                            <span class="info-value">{{ member.departmentName || '-' }}</span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">직무:</span>
                            <span class="info-value">{{ member.jobName || '-' }}</span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">상태:</span>
                            <div class="status-badge" :class="getStatusClass(member.status)">
                                <div class="status-dot"></div>
                                <span class="status-text">{{ getStatusLabel(member.status) }}</span>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- 액션 버튼들 -->
                <div class="flex-0" style="min-width: 150px;">
                    <h4 class="text-subtitle-1 font-weight-bold mb-3 text-primary">액션</h4>
                    <div class="d-flex flex-column" style="gap: 8px;">
                        <v-btn color="primary" variant="outlined" size="small" @click="$emit('view-detail', member)">
                            <v-icon start>mdi-account-details</v-icon>
                            상세 보기
                        </v-btn>
                        <v-btn color="success" variant="outlined" size="small"
                            @click="$emit('view-attendance', member)">
                            <v-icon start>mdi-clock-outline</v-icon>
                            근태 기록
                        </v-btn>
                        <v-btn color="orange" variant="outlined" size="small" @click="$emit('send-mail', member)">
                            <v-icon start>mdi-email-outline</v-icon>
                            메일 발송
                        </v-btn>
                    </div>
                </div>
            </div>
        </v-card-text>
    </v-card>
</template>

<script setup>
import dayjs from 'dayjs'

defineProps({
    member: {
        type: Object,
        required: true
    }
})

defineEmits(['view-detail', 'view-attendance', 'send-mail'])

const getStatusClass = (status) => {
    switch (status) {
        case 1: return 'status-present'
        case 0: return 'status-absent'
        default: return 'status-unknown'
    }
}

const getStatusLabel = (status) => {
    switch (status) {
        case 1: return '출근'
        case 0: return '미출근'
        default: return '알 수 없음'
    }
}

const formatDate = (dateString) => {
    if (!dateString) return '-'
    return dayjs(dateString).format('YYYY-MM-DD')
}
</script>

<style scoped>
.info-grid {
    display: flex;
    flex-direction: column;
    gap: 8px;
}

.info-item {
    display: flex;
    align-items: center;
    gap: 8px;
}

.info-label {
    font-weight: 500;
    color: #666;
    min-width: 80px;
    font-size: 13px;
}

.info-value {
    color: #333;
    font-weight: 400;
    font-size: 13px;
}

/* 상태 배지 스타일 */
.status-badge {
    display: inline-flex;
    align-items: center;
    padding: 6px 12px;
    border-radius: 20px;
    font-size: 12px;
    font-weight: 500;
    transition: all 0.2s ease;
}

.status-dot {
    width: 6px;
    height: 6px;
    border-radius: 50%;
    margin-right: 6px;
}

.status-text {
    line-height: 1;
}

.status-present {
    background-color: #e8f5e8;
    color: #2e7d32;
    border: 1px solid #c8e6c9;
}

.status-present .status-dot {
    background-color: #4caf50;
}

.status-absent {
    background-color: #ffebee;
    color: #c62828;
    border: 1px solid #ffcdd2;
}

.status-absent .status-dot {
    background-color: #f44336;
}

.status-unknown {
    background-color: #f5f5f5;
    color: #666;
    border: 1px solid #e0e0e0;
}

.status-unknown .status-dot {
    background-color: #9e9e9e;
}
</style>