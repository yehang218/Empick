<template>
    <v-navigation-drawer app permanent width="260" class="sidebar">
        <div class="logo-area">
            <img src="@/assets/empick_logo.png" alt="EMPICK Logo" class="logo" />
        </div>

        <v-list nav dense>
            <v-list-subheader class="menu-title">
                <v-icon start>mdi-account-group</v-icon>
                {{ headerTitle }}
            </v-list-subheader>

            <template v-for="(section, i) in menu[headerTitle]" :key="i">
                <!-- children이 있을 때: 그룹 토글 가능 -->
                <template v-if="section.children && section.children.length > 0">
                    <v-list-group no-action :model-value="isGroupOpen(section)">
                        <template #activator="{ props }">
                            <v-list-item v-bind="props">
                                <v-list-item-title class="menu-group-title"
                                    :class="{ 'highlighted-group': isGroupOpen(section) }">
                                    {{ section.label }}
                                </v-list-item-title>
                            </v-list-item>
                        </template>

                        <v-list-item v-for="(subItem, j) in section.children" :key="j" :title="subItem.label"
                            :to="subItem.path" link :active="isMenuItemActive(subItem)" class="menu-item"
                            active-class="active-item" />
                    </v-list-group>
                </template>

                <!-- children은 없지만 path가 있음: 바로 이동 가능한 단일 항목 -->
                <template v-else-if="section.path">
                    <v-list-item :to="section.path" link :active="route.path === section.path" class="menu-item"
                        active-class="active-item">
                        <v-list-item-title class="menu-group-title">{{ section.label }}</v-list-item-title>
                    </v-list-item>
                </template>

                <!-- children도 없고 path도 없음: 단순 텍스트 항목 -->
                <template v-else>
                    <v-list-item>
                        <v-list-item-title class="menu-group-title">{{ section.label }}</v-list-item-title>
                    </v-list-item>
                </template>
            </template>
        </v-list>

        <div class="bottom-section">
            <!-- <div class="dark-mode-toggle">
                <v-icon start>mdi-weather-night</v-icon>
                Dark Mode
                <v-switch hide-details inset v-model="darkMode" class="ml-auto" />
            </div> -->

            <v-btn block color="#607285" class="logout-btn" prepend-icon="mdi-logout" @click="authStore.logout">
                Logout
            </v-btn>
        </div>
    </v-navigation-drawer>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { fullMenu } from '@/constants/common/fullMenu.js'
import { useAuthStore } from '@/stores/authStore'
import { filterMenuByRoles } from '@/utils/menuAccess'

const route = useRoute()
const authStore = useAuthStore()

const userRoles = computed(() => authStore.userInfo?.roles || [])

// 현재 경로에 따라 헤더 메뉴 결정
const headerTitle = computed(() => {
    console.log('🧭 MainSidebar headerTitle 계산:', route.path)

    if (route.path.startsWith('/myinfo')) {
        console.log('✅ 내정보 메뉴 선택됨')
        return '내정보'
    }
    if (route.path.startsWith('/orgstructure')) {
        console.log('✅ 인사 메뉴 선택됨')
        return '인사'
    }
    if (route.path.startsWith('/attendance')) return '근태'
    if (route.path.startsWith('/approval')) return '결재'
    if (route.path.startsWith('/employment')) return '채용'
    if (route.path.startsWith('/schedule')) return '일정'

    console.log('⚠️ 기본값으로 내정보 메뉴 선택됨')
    return '내정보'
})

// 메뉴 항목 활성화 확인 함수
function isMenuItemActive(item) {
    // 기본 경로 확인
    if (route.path === item.path || route.path.startsWith(item.path)) {
        return true
    }

    // activePaths가 있는 경우 추가 확인
    if (item.activePaths && Array.isArray(item.activePaths)) {
        return item.activePaths.some(activePath =>
            route.path === activePath || route.path.startsWith(activePath)
        )
    }

    return false
}

// 현재 경로에 포함된 하위 항목이 있는 그룹만 열림
function isGroupOpen(section) {
    return section.children.some((item) => isMenuItemActive(item))
}

const menu = computed(() => filterMenuByRoles(fullMenu, userRoles.value))

</script>

<style scoped>
.sidebar {
    width: 260px;
    padding: 0;
    background-color: #fafcfb;
    border-right: 1px solid #e0e0e0;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
}

.v-navigation-drawer {
    z-index: 100 !important;
}

.logo-area {
    padding: 24px;
    text-align: center;
}

.logo {
    width: 120px;
    color: #2f6f3e;
}

.menu-title {
    font-size: 20px !important;
    font-weight: 700;
    color: #2f3e4e;
    display: flex;
    align-items: center;
    gap: 10px;
    padding-left: 16px;
}

.menu-icon {
    font-size: 24px;
    color: #2f6f3e;
}

.menu-group-title {
    font-weight: bold;
    color: #3d4c58;
    font-size: 14px;
    transition: color 0.2s ease;
}

.highlighted-group {
    color: #2f6f3e;
    /* 초록색 강조 */
}

.menu-item {
    margin-left: 8px;
    border-radius: 6px;
    font-size: 14px;
    color: #4b5c66;
}

.menu-item:hover {
    background-color: #eefaf0;
    color: #2f6f3e;
}

.active-item {
    background-color: #e0f3e5 !important;
    color: #2f6f3e !important;
    font-weight: 600;
}

.bottom-section {
    padding: 16px;
    border-top: 1px solid #e0e0e0;
}

.dark-mode-toggle {
    display: flex;
    align-items: center;
    gap: 8px;
    color: #4b5c66;
    font-size: 14px;
    margin-bottom: 16px;
}

.logout-btn {
    font-weight: bold;
    color: white;
    border-radius: 8px;
    text-transform: none;
}
</style>
