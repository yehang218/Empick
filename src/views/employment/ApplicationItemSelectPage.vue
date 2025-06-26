<template>
    <IntroduceTemplateSelectModal v-model="showTemplateModal" @select="onTemplateSelected" />
    <v-container fluid class="pa-6">
        <v-row justify="space-between" align="center" class="mb-4">
            <div class="d-flex align-center">
                <v-btn variant="text" size="small" icon @click="goBack">
                    <v-icon>mdi-arrow-left</v-icon>
                </v-btn>
                <h2 class="text-h5 font-weight-bold ml-2">지원서 항목 선택</h2>
            </div>
            <div class="d-flex align-center">
                <span v-if="store.draftRecruitment?.introduceTemplateTitle"
                    class="mr-4 text-subtitle-1 text-grey-darken-1">
                    선택된 템플릿: <strong>{{ store.draftRecruitment.introduceTemplateTitle }}</strong>
                </span>
                <v-btn color="secondary" @click="selectIntroduceTemplate">
                    자기소개서 템플릿 선택
                </v-btn>
            </div>
        </v-row>

        <v-row>
            <!-- (1) 자기소개서 미리보기 (좌측 1/3) -->
            <v-col cols="4" class="preview-pane">
                <h3 class="text-h6 font-weight-bold mb-3">자기소개서 미리보기</h3>
                <v-card class="pa-4 preview-card" flat>
                    <div v-if="loadingIntroduceTemplateDetail">
                        <v-progress-circular indeterminate size="20"></v-progress-circular>
                        <span>템플릿을 불러오는 중...</span>
                    </div>
                    <div v-else-if="selectedIntroduceTemplate && selectedIntroduceTemplate.items">
                        <v-list-item v-for="item in selectedIntroduceTemplate.items" :key="item.id">
                            <v-list-item-title>
                                - {{ item.title }}
                            </v-list-item-title>
                        </v-list-item>
                    </div>
                    <p v-else class="text-center text-grey-darken-1">
                        자기소개서 템플릿을 선택하면<br>미리보기가 표시됩니다.
                    </p>
                </v-card>
            </v-col>

            <!-- (2) 지원서 항목 미리보기 (중앙 1/3) -->
            <v-col cols="4" class="preview-pane">
                <h3 class="text-h6 font-weight-bold mb-3">지원서 항목 미리보기</h3>
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

            <!-- (3) 항목 선택 (오른쪽 1/3) -->
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
import { useRouter, onBeforeRouteLeave } from 'vue-router'
import { useRecruitmentStore } from '@/stores/recruitmentStore'
import recruitmentCreateDTO from '@/dto/employment/recruitment/recruitmentCreateDTO'
import { fetchApplicationItemCategories } from '@/services/applicationItemService'
import { getInputTypeLabel } from '@/constants/employment/inputTypes'
import { useMemberStore } from '@/stores/memberStore'
import { useIntroduceTemplateStore } from '@/stores/introduceTemplateStore'
import { storeToRefs } from 'pinia'
import IntroduceTemplateSelectModal from '@/components/employment/IntroduceTemplateSelectModal.vue'
import { useToast } from 'vue-toastification'

const router = useRouter()
const store = useRecruitmentStore()
const memberStore = useMemberStore()
const introduceTemplateStore = useIntroduceTemplateStore()
const showTemplateModal = ref(false)
const { selectedTemplate: selectedIntroduceTemplate, loadingDetail: loadingIntroduceTemplateDetail } = storeToRefs(introduceTemplateStore)
const toast = useToast()

const categoryList = computed(() => store.applicationItemCategoryList)
const selectedIds = computed({
    get: () => store.selectedApplicationItemIds,
    set: (val) => store.selectedApplicationItemIds = val
})
const requiredIds = computed({
    get: () => store.requiredApplicationItemIds,
    set: (val) => store.requiredApplicationItemIds = val
})
const dateValues = ref({})
const menuStates = ref({})
const openGroup = ref(null)

const goBack = () => {
    router.back()
}

const selectIntroduceTemplate = () => {
    showTemplateModal.value = true
}

const onTemplateSelected = async (selection) => {
    if (selection && selection.id && selection.title) {
        if (store.draftRecruitment) {
            store.draftRecruitment.introduceTemplateId = selection.id;
            store.draftRecruitment.introduceTemplateTitle = selection.title;
        }
        await introduceTemplateStore.loadTemplateDetail(selection.id)
    }
}

const selectedItemsByGroup = (children) => {
    return children.filter(item => selectedIds.value.includes(item.id))
}

const groupedCategories = computed(() => {
    const parents = categoryList.value.filter(c => c.applicationItemCategoryId === null)
    return parents.map(parent => ({
        parent,
        children: categoryList.value.filter(c => c.applicationItemCategoryId === parent.id)
    }))
})

const keepPages = [
    '/employment/recruitments/create',
    '/employment/recruitments/edit',
    '/employment/application-items/select'
]

onBeforeRouteLeave((to) => {
    if (!keepPages.some(path => to.path.startsWith(path))) {
        store.clearAllDrafts()
    }
})

onMounted(async () => {
    await memberStore.getMyInfo()
    // store에 없을 때만 fetch
    if (!store.applicationItemCategoryList.length) {
        const result = await fetchApplicationItemCategories()
        store.setApplicationItemCategoryList(result)
    }
    await introduceTemplateStore.loadTemplates()
    // draft 값이 있으면 불러오기
    const draft = store.draftRecruitment;
    if (draft) {
        selectedIds.value = store.selectedApplicationItemIds ? [...store.selectedApplicationItemIds] : []
        requiredIds.value = store.requiredApplicationItemIds ? [...store.requiredApplicationItemIds] : []
        if (draft.introduceTemplateId) {
            // templates 목록 중 해당 id 찾아서 title 세팅
            const selected = introduceTemplateStore.templates.find(
                t => t.id === draft.introduceTemplateId
            );
            if (selected) {
                draft.introduceTemplateTitle = selected.title;
            }
            await introduceTemplateStore.loadTemplateDetail(draft.introduceTemplateId)
        } else {
            introduceTemplateStore.selectedTemplate = null;
        }
    } else {
        selectedIds.value = []
        requiredIds.value = []
        introduceTemplateStore.selectedTemplate = null;
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

const isEditMode = computed(() => !!router.currentRoute.value.query.id)

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
        recruitType: draft.recruitType,
        applicationItems,
        introduceTemplateId: draft.introduceTemplateId || 1,
        memberId: memberStore.form.id
    })

    try {
        if (isEditMode.value) {
            await store.updateExistingRecruitment(router.currentRoute.value.query.id, dto)
            toast.success('공고가 성공적으로 수정되었습니다.')
        } else {
            await store.submitRecruitment(dto)
            toast.success('공고가 성공적으로 등록되었습니다.')
        }
        store.clearDraftRecruitment()
        store.clearDraftApplicationItems()
        store.clearApplicationItemCategoryList()
        router.push('/employment/recruitments')
    } catch (e) {
        toast.error('공고 저장에 실패했습니다.')
    }
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