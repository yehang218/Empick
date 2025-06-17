<template>
    <v-container fluid class="pa-6">
        <v-row justify="space-between" align="center" class="mb-4">
            <div class="d-flex align-center">
                <v-btn variant="text" size="small" icon @click="goBack">
                    <v-icon>mdi-arrow-left</v-icon>
                </v-btn>
                <h2 class="text-h5 font-weight-bold ml-2">지원서 항목 선택</h2>
            </div>

            <v-btn color="secondary" @click="selectIntroduceTemplate">
                자기소개서 템플릿 선택 (임시)
            </v-btn>
        </v-row>

        <v-row>
            <!-- 중앙: 미리보기 영역 -->
            <v-col cols="8" class="preview-pane">
                <h3 class="text-h6 font-weight-bold mb-3">미리보기</h3>

                <template v-for="(group, index) in groupedCategories" :key="index">
                    <template v-if="selectedItemsByGroup(group.children).length > 0">
                        <v-divider class="my-4" />
                        <h4 class="text-subtitle-1 font-weight-bold mb-3">{{ group.parent.name }}</h4>
                        <v-card class="pa-4 mb-4" v-for="item in selectedItemsByGroup(group.children)" :key="item.id"
                            elevation="2">
                            <div class="font-weight-medium mb-2">
                                {{ item.name }}
                                <span class="text-caption text-grey-darken-1">
                                    ({{ getInputTypeLabel(item.inputType) }})
                                </span>
                            </div>
                            <component v-if="getInputComponent(item.inputType) !== 'date-picker-wrapper'"
                                :is="getInputComponent(item.inputType)" :label="item.name"
                                v-bind="getInputComponentProps(item.inputType)"
                                :readonly="inputTypeIsReadonly(item.inputType)" density="compact" />
                            <template v-else>
                                <v-menu v-model="menuStates[item.id]" :close-on-content-click="false" max-width="290px"
                                    min-width="auto">
                                    <template #activator="{ props }">
                                        <v-text-field v-bind="props" :label="item.name" v-model="dateValues[item.id]"
                                            readonly density="compact" />
                                    </template>
                                    <v-date-picker v-model="dateValues[item.id]" color="primary" hide-header />
                                </v-menu>
                            </template>
                        </v-card>
                    </template>
                </template>
            </v-col>

            <!-- 우측: 항목 카테고리 선택 -->
            <v-col cols="4" class="scroll-pane">
                <h3 class="text-h6 font-weight-bold mb-3">항목 선택</h3>
                <v-list density="compact">
                    <template v-for="(group, index) in groupedCategories" :key="index">
                        <v-list-group v-model="openGroup" :value="index">
                            <template #activator="{ props }">
                                <v-list-item v-bind="props" class="font-weight-bold">
                                    {{ group.parent.name }}
                                </v-list-item>
                            </template>

                            <v-list-item v-for="child in group.children" :key="child.id">
                                <div class="d-flex align-center justify-space-between w-100">
                                    <div class="d-flex align-center">
                                        <v-checkbox v-model="selectedIds" :value="child.id" class="ma-0 pa-0 mr-2"
                                            density="compact" hide-details />
                                        <span>{{ child.name }}</span>
                                    </div>
                                    <v-checkbox v-if="selectedIds.includes(child.id)" v-model="requiredIds"
                                        :value="child.id" class="ma-0 pa-0" label="필수" hide-details density="compact" />
                                </div>
                            </v-list-item>
                        </v-list-group>
                    </template>
                </v-list>
            </v-col>
        </v-row>

        <v-btn color="primary" class="mt-6" @click="submit">공고 등록</v-btn>
    </v-container>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useRecruitmentStore } from '@/stores/recruitmentStore'
import recruitmentCreateDTO from '@/dto/employment/recruitment/recruitmentCreateDTO'
import { getInputTypeLabel } from '@/constants/employment/inputTypes'
import { useMemberStore } from '@/stores/memberStore'

const router = useRouter()
const route = useRoute()
const store = useRecruitmentStore()
const memberStore = useMemberStore()
const requestId = route.query.requestId

// 선택된 항목 ID와 필수 여부
const selectedIds = computed({
    get: () => store.selectedApplicationItemIds,
    set: (val) => store.selectedApplicationItemIds = val
})
const requiredIds = computed({
    get: () => store.requiredApplicationItemIds,
    set: (val) => store.requiredApplicationItemIds = val
})

// 지원서 항목 카테고리
const categoryList = computed(() => store.applicationItemCategoryList || [])

// 그룹핑된 카테고리 (부모-자식 구조)
const groupedCategories = computed(() => {
    const parents = categoryList.value.filter(c => c.applicationItemCategoryId === null)
    return parents.map(parent => ({
        parent,
        children: categoryList.value.filter(c => c.applicationItemCategoryId === parent.id)
    }))
})

const dateValues = ref({})
const menuStates = ref({})
const openGroup = ref(null)

const goBack = () => {
    router.back()
}

const selectIntroduceTemplate = () => {
    if (store.draftRecruitment) {
        store.draftRecruitment.introduceTemplateId = 1
        alert('자기소개서 템플릿이 임시로 선택되었습니다.')
    }
}

const selectedItemsByGroup = (children) => {
    return children.filter(item => selectedIds.value.includes(item.id))
}

// 📦 지원서 항목 카테고리 목록 불러오기 (스토어 함수 사용)
onMounted(async () => {
    await memberStore.getMyInfo()

    if (!store.applicationItemCategoryList.length) {
        await store.loadApplicationItemCategories()
        console.log('불러온 카테고리:', store.applicationItemCategoryList)
    } else {
        console.log('이미 있는 카테고리:', store.applicationItemCategoryList)
    }
})

const getInputComponent = (inputType) => {
    switch (inputType) {
        case 0: return 'v-text-field'
        case 1: return 'v-textarea'
        case 2: return 'v-file-input'
        case 3: return 'v-text-field'
        case 4: return 'date-picker-wrapper'
        case 5: return 'v-text-field'
        case 6: return 'v-radio-group'
        case 7: return 'v-checkbox'
        default: return 'v-text-field'
    }
}

const getInputComponentProps = (inputType) => {
    switch (inputType) {
        case 3: return { type: 'url' }
        case 5: return { type: 'number' }
        default: return {}
    }
}

const inputTypeIsReadonly = (inputType) => {
    return ![6, 7].includes(inputType)
}

const submit = async () => {
    store.setDraftApplicationItems(selectedIds.value, requiredIds.value)

    const draft = store.draftRecruitment
    if (!draft) return

    const applicationItems = selectedIds.value.map(id => ({
        applicationItemCategoryId: id,
        isRequired: requiredIds.value.includes(id)
    }))

    const dto = recruitmentCreateDTO.fromForm({
        ...draft,
        recruitmentRequestId: draft.recruitmentRequestId,
        recruitType: draft.recruitType,
        applicationItems,
        introduceTemplateId: draft.introduceTemplateId || 1,
        memberId: memberStore.form.id
    })

    console.log('📦 전송 DTO:', JSON.stringify(dto, null, 2))

    await store.submitRecruitment(dto)
    store.clearDraftRecruitment()
    store.clearDraftApplicationItems()
    store.clearApplicationItemCategoryList()

    router.push('/employment/recruitments')
}
</script>

<style scoped>
.preview-pane {
    max-height: 85vh;
    overflow-y: auto;
}

.scroll-pane {
    max-height: 85vh;
    overflow-y: auto;
    border-left: 1px solid #eee;
    padding-left: 16px;
}
</style>
