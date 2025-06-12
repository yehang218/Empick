<template>
    <div>
        <h2>페이지 링크 목록</h2>
        <v-expansion-panels>
            <v-expansion-panel v-for="domain in domains" :key="domain.name">
                <v-expansion-panel-title>{{ domain.name }}</v-expansion-panel-title>
                <v-expansion-panel-text>
                    <v-list>
                        <v-list-item v-for="route in getRoutesForDomain(domain.prefix)" :key="route.path"
                            :to="route.path" link>
                            <v-list-item-title>{{ route.path }}</v-list-item-title>
                        </v-list-item>
                    </v-list>
                </v-expansion-panel-text>
            </v-expansion-panel>
        </v-expansion-panels>
    </div>
</template>

<script setup>
import { computed } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

// 여기에 새로운 도메인을 추가하면 됩니다
const domains = [
    { name: 'Main', prefix: '/' },
    { name: 'Auth', prefix: '/login' },
    { name: 'Employment', prefix: '/employment' },
    { name: 'OrgStructure', prefix: '/orgstructure' },
    { name: 'Test', prefix: '/test' }
];

const allRoutes = computed(() => {
    return router.getRoutes()
        .filter(route => !route.path.includes(':')) // 동적 라우트 제외
        .sort((a, b) => a.path.localeCompare(b.path));
});

const getRoutesForDomain = (prefix) => {
    return allRoutes.value.filter(route => {
        if (prefix === '/') return route.path === '/';
        return route.path.startsWith(prefix);
    });
};
</script>

<style scoped>
.main-page {
    padding: 2rem;
    max-width: 1200px;
    margin: 0 auto;
}

.domain-card {
    height: 100%;
    transition: transform 0.2s ease-in-out;
}

.domain-card:hover {
    transform: translateY(-5px);
}

.v-card-title {
    font-size: 1.25rem;
    font-weight: 600;
    margin-bottom: 0.5rem;
}

.v-card-subtitle {
    color: rgba(0, 0, 0, 0.6);
}
</style>