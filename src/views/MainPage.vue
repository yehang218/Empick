<template>
    <div>
        <h2>Available Routes by Domain</h2>
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
import { computed } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

const groupedRoutes = computed(() => {
    const routes = router.getRoutes()
        .filter(route => !route.path.includes(':')) // 동적 라우트 제외
        .sort((a, b) => a.path.localeCompare(b.path));

    const groups = {
        'Main': [],
        'Auth': [],
        'Employment': [],
        'OrgStructure': [],
        'Test': []
    };

    routes.forEach(route => {
        const path = route.path;
        if (path === '/') {
            groups['Main'].push(route);
        } else if (path.startsWith('/login')) {
            groups['Auth'].push(route);
        } else if (path.startsWith('/employment')) {
            groups['Employment'].push(route);
        } else if (path.startsWith('/orgstructure')) {
            groups['OrgStructure'].push(route);
        } else if (path.startsWith('/test')) {
            groups['Test'].push(route);
        }
    });

    // 빈 그룹 제거
    return Object.fromEntries(
        Object.entries(groups).filter(([_, routes]) => routes.length > 0)
    );
});
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