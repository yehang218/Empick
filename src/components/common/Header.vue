<template>
    <v-app>
        <!-- ✅ 메뉴 + 하위메뉴 전체를 감싸는 wrapper -->
        <div class="menu-wrapper">
            <!-- 상단 고정 AppBar -->
            <v-app-bar app height="70" flat
                style="position: fixed; top: 0; left: 0; right: 0; z-index: 1000; background-color: #5F8D4E;">
                <div class="d-flex align-center pl-16">
                    <img :src="logo" alt="로고" style="height: 32px;" class="mr-6" />
                    <div class="d-flex align-center menu-bar">
                        <div v-for="menu in filteredMenu" :key="menu" class="menu-item"
                            @mouseenter="selectedMenu = menu" :class="{ active: selectedMenu === menu }">
                            {{ menu }}
                        </div>
                    </div>
                </div>

                <v-spacer />

                <!-- 검색창 + 유저 정보 유지 -->
                <v-autocomplete v-model="selectedPath" :items="searchInput.length > 0 ? filteredSearchableItems : []"
                    no-data-text="" item-title="label" item-value="path" placeholder="Search" hide-details flat
                    variant="solo" density="compact" prepend-inner-icon="mdi-magnify" class="search-box mr-4"
                    style="max-width: 200px; background-color: white; border-radius: 20px;"
                    @update:modelValue="onSearchSelect" @update:search="searchInput = $event" />


                <!-- TODO: 로그인 후 받아온 프로필 이미지로 교체 예정 -->
                <v-avatar size="36" class="mr-2" color="grey-lighten-2">
                    <v-icon color="grey">mdi-account</v-icon>
                </v-avatar>

                <!-- TODO: 로그인 후 받아온 사원이름, 부서로 교체 예정 -->
                <div class="text-white text-caption mr-4">
                    <div>박우석 사원</div>
                    <div class="text-subtitle-2">인사팀</div>
                </div>

                <v-btn icon variant="text">
                    <v-icon color="white">mdi-cog</v-icon>
                </v-btn>
            </v-app-bar>

            <!-- ✅ 고정형 2단 메뉴 패널 -->
            <teleport to="body">
                <v-container v-if="selectedMenu" class="menu-panel" fluid @mouseleave="selectedMenu = ''">
                    <div class="menu-columns">
                        <div v-for="section in filteredMenuObject[selectedMenu]" :key="section.label"
                            class="menu-section">
                            <h3 class="menu-title">{{ section.label }}</h3>
                            <ul v-if="section.children.length" class="submenu-list">
                                <li v-for="child in section.children" :key="child.label" class="submenu-item"
                                    @click="goTo(child.path)">
                                    {{ child.label }}
                                </li>
                            </ul>
                        </div>
                    </div>
                </v-container>
            </teleport>
        </div>
    </v-app>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useAuthStore } from '@/stores/authStore'
import { useRouter } from 'vue-router'
import logo from '@/assets/logo.png'
import { fullMenu } from '@/constants/common/fullMenu.js'
import { filterMenuByRoles } from '@/utils/menuAccess'

const router = useRouter()
const selectedMenu = ref('')
const searchInput = ref('')
const selectedPath = ref('')

const authStore = useAuthStore()
const userRoles = computed(() => authStore.userInfo?.roles || [])

const filteredMenuObject = computed(() => filterMenuByRoles(fullMenu, userRoles.value))
const filteredMenu = computed(() => Object.keys(filteredMenuObject.value))

console.log('userInfo:', authStore.userInfo);
console.log('userRoles:', userRoles.value);

const searchableItems = computed(() => {
    const items = []
    const filtered = filterMenuByRoles(fullMenu, userRoles.value)
    for (const sections of Object.values(filtered)) {
        for (const section of sections) {
            for (const child of section.children || []) {
                items.push({ label: child.label, path: child.path })
            }
        }
    }
    return items
})

const filteredSearchableItems = computed(() => {
    const query = searchInput.value?.toLowerCase().trim()
    return searchableItems.value.filter(item =>
        item.label.toLowerCase().includes(query || '')
    )
})

function onSearchSelect(path) {
    if (path) router.push(path)
}

function goTo(path) {
    if (path) router.push(path)
}
</script>

<style scoped>
.menu-bar {
    gap: 30px;
}

.menu-item {
    font-weight: 500;
    cursor: pointer;
    padding: 4px 30px;
    color: white;
    border-radius: 8px;
    transition: all 0.2s;
}

.menu-item.active {
    background-color: white;
    color: #5F8D4E;
}

.menu-wrapper {
    position: relative;
    z-index: 100;
}

.menu-panel {
    position: fixed;
    /* 🔥 전체 뷰포트 기준 */
    top: 70px;
    /* 헤더 height 맞추기 */
    left: 0;
    width: 100%;
    background-color: white;
    border-top: 1px solid #eee;
    border-bottom: 1px solid #eee;
    padding: 24px 48px 36px;
    z-index: 9999;
    /* 🔥 Vuetify Drawer보다 높게 */
}

.menu-columns {
    display: flex;
    gap: 60px;
    flex-wrap: wrap;
    max-width: 1200px;
    margin: 0 auto;
}

.menu-section {
    min-width: 160px;
    flex: 1;
}

.menu-title {
    font-size: 16px;
    font-weight: bold;
    margin-bottom: 8px;
    color: #333;
}

.submenu-list {
    list-style: none;
    padding: 0;
    margin: 0;
}

.submenu-item {
    font-size: 14px;
    padding: 4px 0;
    color: #5F8D4E;
    cursor: pointer;
}

.submenu-item:hover {
    text-decoration: underline;
}

.search-box input {
    padding: 0 10px !important;
}

.main-content {
    background-color: white;
    min-height: 100vh;
}
</style>
