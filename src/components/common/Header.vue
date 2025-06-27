<template>
    <v-app>
        <!-- ✅ 메뉴 + 하위메뉴 전체를 감싸는 wrapper -->
        <div class="menu-wrapper">
            <!-- 상단 고정 AppBar -->
            <v-app-bar app height="70" flat
                style="position: fixed; top: 0; left: 0; right: 0; z-index: 1000; background-color: #5F8D4E;">
                <div class="d-flex align-center pl-16">
                    <img :src="logo" alt="로고" style="height: 32px; cursor: pointer;" class="mr-6" @click="goToMain" />
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

                <!-- 프로필 사진 -->
                <v-avatar size="36" class="mr-2" color="grey-lighten-2">
                    <template v-if="memberStore.form.pictureUrl">
                        <img :src="memberStore.form.pictureUrl" alt="프로필"
                            style="width: 100%; height: 100%; object-fit: cover;" />
                    </template>
                </v-avatar>

                <div class="text-white text-caption mr-4">
                    <div>{{ memberStore.form.name || '이름 없음' }}</div>
                    <div class="text-subtitle-2">{{ memberStore.form.departmentName || '부서 없음' }}</div>
                </div>

                <v-btn icon variant="text" @click="goToProfile">
                    <v-icon color="white">mdi-cog</v-icon>
                </v-btn>
            </v-app-bar>

            <teleport to="body">
                <v-container v-if="selectedMenu" class="menu-panel" fluid @mouseleave="selectedMenu = ''">
                    <div class="menu-columns">
                        <div v-for="section in filteredMenuObject[selectedMenu]" :key="section.label"
                            class="menu-section">
                            <h3 class="menu-title" @click="goToFirstChild(section)" style="cursor:pointer">
                                {{ section.label }}
                            </h3>
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
import { useMemberStore } from '@/stores/memberStore'

const router = useRouter()
const selectedMenu = ref('')
const searchInput = ref('')
const selectedPath = ref('')

const authStore = useAuthStore()
const userRoles = computed(() => authStore.userInfo?.roles || [])

const memberStore = useMemberStore()

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

function goToMain() {
    router.push('/');
}

function goToFirstChild(section) {
    if (section.children && section.children.length > 0) {
        const first = section.children.find(child => child.path);
        if (first?.path) goTo(first.path);
    } else if (section.path) {
        goTo(section.path);
    }
}

function goToProfile() {
    router.push('/orgstructure/profile');
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
    top: 70px;
    left: 0;
    width: 100%;
    background-color: white;
    border-top: 1px solid #eee;
    border-bottom: 1px solid #eee;
    padding: 24px 48px 36px;
    z-index: 9999;
}

.menu-columns {
    display: flex;
    gap: 30px;
    flex-wrap: wrap;
    max-width: 1200px;
    margin-left: 115px;
    margin-right: 0;
    justify-content: flex-start;
}

.menu-section {
    min-width: 140px;
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: flex-start;
}

.menu-title {
    font-size: 16px;
    font-weight: bold;
    margin-bottom: 8px;
    color: #333;
    text-align: left;
    width: 100%;
}

.submenu-list {
    list-style: none;
    padding: 0;
    margin: 0;
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    text-align: left;
}

.submenu-item {
    font-size: 14px;
    padding: 4px 0;
    color: #5F8D4E;
    cursor: pointer;
    text-align: left;
    width: 100%;
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
