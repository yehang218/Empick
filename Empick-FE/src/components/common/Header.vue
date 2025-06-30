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
                <v-autocomplete v-model="selectedPath" :items="filteredSearchableItems"
                    :no-data-text="searchInput.length > 0 ? '검색 결과가 없습니다' : '메뉴를 검색하세요'" item-title="label"
                    item-value="path" placeholder="메뉴 검색" hide-details flat variant="solo" density="compact"
                    prepend-inner-icon="mdi-magnify" class="search-box mr-4"
                    style="max-width: 250px; background-color: white; border-radius: 20px;"
                    @update:modelValue="onSearchSelect" @update:search="onSearchInput" @blur="onSearchBlur" clearable
                    auto-select-first :menu-props="{ maxHeight: '300px', closeOnContentClick: true, zIndex: 10000 }" />

                <!-- 프로필 영역 (클릭 가능) -->
                <div class="d-flex align-center profile-area" @click="goToProfile">
                    <v-avatar size="36" class="mr-2" color="grey-lighten-2">
                        <template v-if="profileImageSrc">
                            <img :src="profileImageSrc" alt="프로필"
                                style="width: 100%; height: 100%; object-fit: cover;" />
                        </template>
                        <template v-else>
                            <v-icon color="grey-lighten-1">mdi-account</v-icon>
                        </template>
                    </v-avatar>

                    <div class="text-white text-caption mr-4">
                        <div>{{ memberStore.form.name || '이름 없음' }}</div>
                        <div class="text-subtitle-2">{{ memberStore.form.departmentName || '부서 없음' }}</div>
                    </div>
                </div>

                <!-- 설정 버튼 (주석처리) -->
                <!-- <v-btn icon variant="text" @click="goToProfile">
                    <v-icon color="white">mdi-cog</v-icon>
                </v-btn> -->
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
import { ref, computed, onMounted, watch, nextTick } from 'vue'
import { useAuthStore } from '@/stores/authStore'
import { useRouter } from 'vue-router'
import logo from '@/assets/logo.png'
import { fullMenu } from '@/constants/common/fullMenu.js'
import { filterMenuByRoles } from '@/utils/menuAccess'
import { useMemberStore } from '@/stores/memberStore'
import { useProfileImage } from '@/composables/useProfileImage'

const router = useRouter()
const selectedMenu = ref('')
const searchInput = ref('')
const selectedPath = ref('')

const authStore = useAuthStore()
const userRoles = computed(() => authStore.userInfo?.roles || [])

const memberStore = useMemberStore()

// 🖼️ ProfilePage와 동일한 방식으로 프로필 이미지 처리
const { profileImageSrc, loadProfileImage } = useProfileImage()

const filteredMenuObject = computed(() => filterMenuByRoles(fullMenu, userRoles.value))
const filteredMenu = computed(() => Object.keys(filteredMenuObject.value))

console.log('userInfo:', authStore.userInfo);
console.log('userRoles:', userRoles.value);

const searchableItems = computed(() => {
    const items = []
    const filtered = filterMenuByRoles(fullMenu, userRoles.value)

    // 상위 메뉴별로 하위 메뉴들을 수집
    for (const [menuName, sections] of Object.entries(filtered)) {
        for (const section of sections) {
            // children이 있는 경우 - 하위 메뉴들을 추가
            if (section.children && section.children.length > 0) {
                for (const child of section.children) {
                    items.push({
                        label: `${menuName} > ${section.label} > ${child.label}`, // 계층 구조 표시
                        path: child.path,
                        menuName,
                        sectionName: section.label,
                        childName: child.label
                    })
                }
            }
            // children이 없거나 비어있는 경우 - 섹션 자체를 추가
            else if (section.path) {
                items.push({
                    label: `${menuName} > ${section.label}`, // 계층 구조 표시
                    path: section.path,
                    menuName,
                    sectionName: section.label,
                    childName: null
                })
            }
        }
    }

    console.log('🔍 검색 가능한 메뉴 항목:', items.length, '개')
    console.log('🔍 검색 가능한 메뉴 목록:', items.map(item => item.label))
    return items
})

const filteredSearchableItems = computed(() => {
    const query = searchInput.value?.toLowerCase().trim()

    console.log('🔍 검색어 입력:', query, '길이:', searchInput.value?.length)

    // 검색어가 없으면 빈 배열 반환 (드롭다운 목록이 너무 길어지는 것을 방지)
    if (!query || query.length === 0) {
        console.log('🔍 검색어 없음 - 빈 배열 반환')
        return []
    }

    const filtered = searchableItems.value.filter(item => {
        const itemLabel = item.label.toLowerCase()
        const childLabel = item.childName?.toLowerCase() || ''
        const sectionLabel = item.sectionName?.toLowerCase() || ''
        const menuName = item.menuName?.toLowerCase() || ''

        // 한글, 영문 모두 지원하는 포함 검색 (전체 라벨, 메뉴명, 섹션명, 하위 메뉴명에서 검색)
        const matches = itemLabel.includes(query) ||
            menuName.includes(query) ||
            sectionLabel.includes(query) ||
            childLabel.includes(query)

        return matches
    })

    console.log('🔍 검색 결과:', filtered.length, '개 항목')
    console.log('🔍 검색 결과 상세:', filtered.map(item => ({ label: item.label, path: item.path })))

    // 최대 10개까지만 표시하여 성능 및 UX 개선
    return filtered.slice(0, 10)
})

function onSearchInput(value) {
    console.log('🔍 검색어 입력:', value)
    searchInput.value = value
}

function onSearchSelect(path) {
    console.log('🔍 검색 메뉴 선택:', path)
    if (path) {
        // 페이지 이동
        router.push(path)

        // 검색창 초기화 (nextTick으로 DOM 업데이트 후 실행)
        nextTick(() => {
            searchInput.value = ''
            selectedPath.value = ''
            console.log('🔍 검색창 초기화 완료')
        })
    }
}

function onSearchBlur() {
    // 검색창이 포커스를 잃을 때 검색어 초기화 (메뉴를 선택하지 않은 경우)
    console.log('🔍 검색창 포커스 잃음, 검색어 초기화')
    nextTick(() => {
        searchInput.value = ''
        selectedPath.value = ''
    })
}

function goTo(path) {
    if (path) {
        // 현재 경로와 동일한 경우 강제로 이동 (쿼리 파라미터가 다른 경우 대응)
        const currentRoute = router.currentRoute.value
        if (currentRoute.path === path && currentRoute.fullPath !== path) {
            console.log('🔄 같은 경로로 강제 이동:', path)
            // 쿼리 파라미터를 제거하고 이동
            router.replace(path)
        } else {
            router.push(path)
        }
    }
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
    router.push('/myinfo/profile');
}

// 🖼️ memberStore 전체를 watch하여 변경사항 감지
watch(() => [memberStore.form?.id, memberStore.profileImageUrl], async (newValues, oldValues) => {
    // 값이 배열이 아니거나 undefined인 경우 처리
    if (!Array.isArray(newValues) || !Array.isArray(oldValues)) {
        console.warn('Header watch: 값이 배열이 아님', { newValues, oldValues })
        return
    }

    const [newId, newProfileUrl] = newValues
    const [oldId, oldProfileUrl] = oldValues || [null, null]

    console.log('🔍 Header watch 실행:', {
        newId,
        oldId,
        newProfileUrl: newProfileUrl ? 'blob URL 있음' : '없음',
        oldProfileUrl: oldProfileUrl ? 'blob URL 있음' : '없음'
    })

    // ID가 있고 이전과 다르거나, 프로필 이미지 URL이 없는 경우
    if (newId && (newId !== oldId || !newProfileUrl)) {
        try {
            console.log('Header: 프로필 이미지 로드 시도, memberId:', newId)
            await loadProfileImage(newId)
            console.log('Header: 프로필 이미지 로드 완료, URL:', memberStore.profileImageUrl)
        } catch (error) {
            console.warn('Header 프로필 이미지 로드 실패:', error)
        }
    }
}, { immediate: true, deep: true })

// 🔍 profileImageSrc 변경 감지 (디버깅용)
watch(() => profileImageSrc.value, (newSrc) => {
    console.log('🖼️ Header profileImageSrc 변경:', newSrc)
}, { immediate: true })

// 🔄 컴포넌트 초기화
onMounted(() => {
    console.log('Header 마운트됨, 현재 memberStore.form.id:', memberStore.form.id)
})
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

.search-box :deep(.v-field__input) {
    font-size: 14px !important;
    padding: 8px 12px !important;
}

.search-box :deep(.v-field__prepend-inner) {
    padding-left: 12px !important;
}

.search-box :deep(.v-autocomplete__menu-icon) {
    display: none !important;
}

.profile-area {
    cursor: pointer;
    border-radius: 8px;
    padding: 4px 8px;
    transition: all 0.2s ease;
}

.profile-area:hover {
    background-color: rgba(255, 255, 255, 0.1);
}

.main-content {
    background-color: white;
    min-height: 100vh;
}
</style>
