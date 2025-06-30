<template>
    <div class="dashboard-container">
        <h1>대시보드</h1>
        <div class="dashboard-content">
            <v-row>
                <v-col cols="12" md="6" lg="4">
                    <v-card class="dashboard-card">
                        <v-card-title>근태 현황</v-card-title>
                        <v-card-text>
                            <div class="text-h4 mb-2">{{ attendanceStatus }}</div>
                            <div class="text-subtitle-1">{{ attendanceMessage }}</div>
                        </v-card-text>
                    </v-card>
                </v-col>

                <v-col cols="12" md="6" lg="4">
                    <v-card class="dashboard-card">
                        <v-card-title>오늘의 일정</v-card-title>
                        <v-card-text>
                            <v-list>
                                <v-list-item v-for="(schedule, index) in todaySchedules" :key="index">
                                    <v-list-item-title>{{ schedule.title }}</v-list-item-title>
                                    <v-list-item-subtitle>{{ schedule.time }}</v-list-item-subtitle>
                                </v-list-item>
                            </v-list>
                        </v-card-text>
                    </v-card>
                </v-col>

                <v-col cols="12" md="6" lg="4">
                    <v-card class="dashboard-card">
                        <v-card-title>결재 대기</v-card-title>
                        <v-card-text>
                            <v-list>
                                <v-list-item v-for="(approval, index) in pendingApprovals" :key="index">
                                    <v-list-item-title>{{ approval.title }}</v-list-item-title>
                                    <v-list-item-subtitle>{{ approval.department }}</v-list-item-subtitle>
                                </v-list-item>
                            </v-list>
                        </v-card-text>
                    </v-card>
                </v-col>
            </v-row>
        </div>
    </div>
    <div>
        <h2>페이지 링크 목록</h2>
        <v-expansion-panels>
            <v-expansion-panel v-for="(routes, domain) in groupedRoutes" :key="domain">
                <v-expansion-panel-title>{{ domain }}</v-expansion-panel-title>
                <v-expansion-panel-text>
                    <v-list>
                        <v-list-item v-for="route in routes" :key="route.path" :to="route.path" link>
                            <v-list-item-title>{{ route.path }}</v-list-item-title>
                        </v-list-item>
                    </v-list>
                </v-expansion-panel-text>
            </v-expansion-panel>
        </v-expansion-panels>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';

// 임시 데이터
const attendanceStatus = ref('정상 출근');
const attendanceMessage = ref('오늘도 좋은 하루 되세요!');
const todaySchedules = ref([
    { title: '팀 미팅', time: '10:00 - 11:00' },
    { title: '점심 약속', time: '12:00 - 13:00' },
    { title: '프로젝트 리뷰', time: '15:00 - 16:00' }
]);
const pendingApprovals = ref([
    { title: '휴가 신청서', department: '인사팀' },
    { title: '출장 보고서', department: '영업팀' }
]);

onMounted(() => {
    // TODO: 실제 데이터 로드
    console.log('Dashboard mounted');
});

import { computed } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

const groupedRoutes = computed(() => {
    const routes = router.getRoutes()
        .filter(route => !route.path.includes(':')) // 동적 라우트 제외
        .sort((a, b) => a.path.localeCompare(b.path));

    const groups = {
        'main': [], // 메인 페이지와 대시보드를 위한 그룹
    };

    routes.forEach(route => {
        const path = route.path;
        if (path === '/' || path === '/dashboard') {
            groups['main'].push(route);
        } else {
            const domain = path.split('/')[1];
            if (!groups[domain]) {
                groups[domain] = [];
            }
            groups[domain].push(route);
        }
    });

    // 빈 그룹 제거
    return Object.fromEntries(
        Object.entries(groups).filter(([_, routes]) => routes.length > 0)
    );
});

</script>

<style scoped>
.dashboard-container {
    padding: 2rem;
}

.dashboard-content {
    margin-top: 2rem;
}

.dashboard-card {
    height: 100%;
    transition: transform 0.2s;
}

.dashboard-card:hover {
    transform: translateY(-5px);
}

.v-card-title {
    font-size: 1.2rem;
    font-weight: 500;
    color: #333;
    border-bottom: 1px solid #eee;
    padding-bottom: 0.5rem;
    margin-bottom: 1rem;
}

.v-card-text {
    color: #666;
}
</style>