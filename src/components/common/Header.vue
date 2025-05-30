<template>
    <v-app>
        <!-- ✅ 메뉴 + 하위메뉴 전체를 감싸는 wrapper -->
        <div class="menu-wrapper">
            <!-- 상단 고정 AppBar -->
            <v-app-bar flat height="70" style="background-color: #5F8D4E;" app clipped-left>
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
            <v-container v-if="selectedMenu" class="menu-panel" fluid @mouseleave="selectedMenu = ''">
                <div class="menu-columns">
                    <div v-for="section in fullMenu[selectedMenu]" :key="section.label" class="menu-section">
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
        </div>

        <!-- 본문 -->
        <v-main class="main-content"></v-main>
    </v-app>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import logo from '@/assets/logo.png'
import { fullMenu } from '@/json/fullMenu.js'

const router = useRouter()
const selectedMenu = ref('')
const userRole = ref('인사팀') // 현재는 인사팀으로 설정
const searchInput = ref('')

const filteredMenu = computed(() => {
    const visibleMenus = []
    for (const [key, sections] of Object.entries(fullMenu)) {
        const hasVisibleSection = sections.some(section =>
            !section.role || section.role.includes(userRole.value)
        )
        if (hasVisibleSection) visibleMenus.push(key)
    }
    return visibleMenus
})

const searchableItems = computed(() => {
    const items = []
    for (const sections of Object.values(fullMenu)) {
        for (const section of sections) {
            if (!section.role || section.role.includes(userRole.value)) {
                for (const child of section.children || []) {
                    items.push({ label: child.label, path: child.path })
                }
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
    background-color: white;
    border-top: 1px solid #eee;
    border-bottom: 1px solid #eee;
    padding: 24px 48px 36px;
    margin-top: 70px;
    position: absolute;
    width: 100%;
    left: 0;
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
    padding-top: 140px;
}
</style>
