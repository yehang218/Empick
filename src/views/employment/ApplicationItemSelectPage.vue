<template>
    <IntroduceTemplateSelectModal v-model="showTemplateModal" @select="onTemplateSelected" />
    <v-container fluid class="pa-6">
        <v-row justify="center">
            <v-col cols="12" md="11" lg="10">
                <v-row justify="space-between" align="center" class="mb-4">
                    <div class="d-flex align-center">
                        <v-btn variant="text" size="small" icon @click="goBack">
                            <v-icon>mdi-arrow-left</v-icon>
                        </v-btn>
                        <h2 class="text-h5 font-weight-bold ml-2">지원서 항목 선택</h2>
                    </div>

                    <div class="d-flex align-center">
                        <span v-if="store.draftRecruitment?.introduceTemplateTitle" class="mr-4 text-subtitle-1 text-grey-darken-1">
                            선택된 템플릿: <strong>{{ store.draftRecruitment.introduceTemplateTitle }}</strong>
                        </span>
                        <v-btn color="secondary" @click="selectIntroduceTemplate">
                            자기소개서 템플릿 선택
                        </v-btn>
                    </div>
                </v-row>

                <v-row>
                    <!-- 미리보기 영역 -->
                    <v-col cols="7">
                        <v-row>
                            <v-col cols="6" class="preview-pane">
                                <h3 class="text-h6 font-weight-bold mb-3">지원서 항목 미리보기</h3>
                                <v-card class="pa-4 preview-card" flat>
                                    <template v-if="selectedIds.length === 0">
                                        <p class="text-center text-grey-darken-1">오른쪽에서 항목을 선택하면<br>미리보기가 표시됩니다.</p>
                                    </template>
                                    <template v-for="(group, index) in groupedCategories" :key="index">
                                        <template v-if="selectedItemsByGroup(group.children).length > 0">
                                            <v-divider v-if="index > 0 && hasVisibleItemsBefore(index)"
                                                class="my-4" />
                                            <h4 class="text-subtitle-1 font-weight-bold mb-3">{{ group.parent.name }}
                                            </h4>
                                            <div v-for="item in selectedItemsByGroup(group.children)" :key="item.id"
                                                class="mb-4">
                                                <div class="font-weight-medium mb-2">
                                                    {{ item.name }}
                                                    <span v-if="requiredIds.includes(item.id)"
                                                        class="text-red ml-1">*</span>
                                                    <span class="text-caption text-grey-darken-1 ml-2">
                                                        ({{ getInputTypeLabel(item.inputType) }})
                                                    </span>
                                                </div>
                                                <component
                                                    v-if="getInputComponent(item.inputType) !== 'date-picker-wrapper'"
                                                    :is="getInputComponent(item.inputType)" :label="item.name"
                                                    v-bind="getInputComponentProps(item.inputType)" :readonly="true"
                                                    density="compact" />
                                                <template v-else>
                                                    <v-date-picker :label="item.name" readonly
                                                        style="pointer-events: none;" />
                                                </template>
                                            </div>
                                        </template>
                                    </template>
                                </v-card>
                            </v-col>
                            <v-col cols="6" class="preview-pane">
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
                        </v-row>
                    </v-col>

                    <!-- 항목 카테고리 선택 -->
                    <v-col cols="5" class="scroll-pane">
                        <h3 class="text-h6 font-weight-bold mb-3">항목 선택</h3>
                        <v-card class="pa-4" flat>
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
                                                    <v-checkbox v-model="selectedIds" :value="child.id"
                                                        class="ma-0 pa-0 mr-2" density="compact" hide-details />
                                                    <span>{{ child.name }}</span>
                                                </div>
                                                <v-checkbox v-if="selectedIds.includes(child.id)" v-model="requiredIds"
                                                    :value="child.id" class="ma-0 pa-0" label="필수" hide-details
                                                    density="compact" />
                                            </div>
                                        </v-list-item>
                                    </v-list-group>
                                </template>
                            </v-list>
                        </v-card>
                    </v-col>
                </v-row>
                <v-row justify="end">
                    <v-col cols="auto">
                        <v-btn color="primary" class="mt-6" @click="submit">
                            {{ isEditMode ? '수정 완료' : '공고 등록' }}
                        </v-btn>
                    </v-col>
                </v-row>
            </v-col>
        </v-row>
    </v-container>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter, useRoute, onBeforeRouteLeave } from 'vue-router'
import { useRecruitmentStore } from '@/stores/recruitmentStore'
import { getInputTypeLabel } from '@/constants/employment/inputTypes'
import { useMemberStore } from '@/stores/memberStore'
import { useIntroduceTemplateStore } from '@/stores/introduceTemplateStore'
import { storeToRefs } from 'pinia'
import IntroduceTemplateSelectModal from '@/components/employment/IntroduceTemplateSelectModal.vue'
import { useToast } from 'vue-toastification'

const router = useRouter()
const route = useRoute()
const store = useRecruitmentStore()
const memberStore = useMemberStore()
const introduceTemplateStore = useIntroduceTemplateStore()
const toast = useToast()

const isEditMode = computed(() => route.query.from === 'edit')
const recruitmentIdToUpdate = computed(() => Number(route.query.id))
const requestId = route.query.requestId

const showTemplateModal = ref(false)

const {
    selectedTemplate: selectedIntroduceTemplate,
    loadingDetail: loadingIntroduceTemplateDetail,
} = storeToRefs(introduceTemplateStore)

const selectedIds = ref([])
const requiredIds = ref([])

const categoryList = computed(() => store.applicationItemCategoryList || [])

const groupedCategories = computed(() => {
    const parents = categoryList.value.filter(c => c.applicationItemCategoryId === null)
    return parents.map(parent => ({
        parent,
        children: categoryList.value.filter(c => c.applicationItemCategoryId === parent.id)
    }))
})

const openGroup = ref(null)

let isNavigatingBack = false;

const goBack = () => {
    store.setDraftApplicationItems(selectedIds.value, requiredIds.value);
    isNavigatingBack = true;
    router.back();
}

const selectIntroduceTemplate = () => {
    showTemplateModal.value = true
}

const selectedItemsByGroup = (children) => {
    return children.filter(item => selectedIds.value.includes(item.id))
}

const hasVisibleItemsBefore = (currentIndex) => {
    for (let i = 0; i < currentIndex; i++) {
        if (selectedItemsByGroup(groupedCategories.value[i].children).length > 0) {
            return true;
        }
    }
    return false;
}

onMounted(async () => {
    await memberStore.getMyInfo()

    if (store.applicationItemCategoryList.length === 0) {
        await store.loadApplicationItemCategories()
    }
    await introduceTemplateStore.loadTemplates()

    const draft = store.draftRecruitment;
    if (draft) {
        selectedIds.value = store.selectedApplicationItemIds ? [...store.selectedApplicationItemIds] : []
        requiredIds.value = store.requiredApplicationItemIds ? [...store.requiredApplicationItemIds] : []

        if (draft.introduceTemplateId) {
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

const onTemplateSelected = async (selection) => {
    if (selection && selection.id && selection.title) {
        if (store.draftRecruitment) {
            store.draftRecruitment.introduceTemplateId = selection.id;
            store.draftRecruitment.introduceTemplateTitle = selection.title;
        }
        await introduceTemplateStore.loadTemplateDetail(selection.id)
        toast.success(`'${selection.title}' 템플릿이 선택되었습니다.`);
    }
}

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

let isSubmitting = false;

onBeforeRouteLeave((to, from, next) => {
    if (isSubmitting || isNavigatingBack) {
        isSubmitting = false;
        isNavigatingBack = false;
        next();
        return;
    }

    const draft = store.draftRecruitment || {};
    const hasChanges =
        JSON.stringify(selectedIds.value) !== JSON.stringify(store.selectedApplicationItemIds) ||
        JSON.stringify(requiredIds.value) !== JSON.stringify(store.requiredApplicationItemIds) ||
        (selectedIntroduceTemplate.value ? draft.introduceTemplateId !== selectedIntroduceTemplate.value.id : draft.introduceTemplateId !== null);

    if (hasChanges) {
        if (confirm('변경사항이 저장되지 않았습니다. 정말로 페이지를 떠나시겠습니까?')) {
            store.clearApplicationItemDraft();
            store.clearIntroduceTemplateDraft();
            next();
        } else {
            next(false);
        }
    } else {
        next();
    }
});


const submit = async () => {
    store.setDraftApplicationItems(selectedIds.value, requiredIds.value)

    const draft = store.draftRecruitment
    if (!draft) {
        toast.error('등록할 채용 정보가 없습니다.');
        return
    }

    const applicationItems = selectedIds.value.map(id => ({
        applicationItemCategoryId: id,
        isRequired: requiredIds.value.includes(id)
    }))

    const formData = {
        title: draft.title,
        content: draft.content,
        recruitType: draft.recruitType,
        imageUrl: draft.imageUrl,
        startedAt: draft.startedAt,
        endedAt: draft.endedAt,
        memberId: memberStore.form.id,
        recruitmentTemplateId: null,
        introduceTemplateId: draft.introduceTemplateId,
        recruitmentRequestId: draft.recruitmentRequestId,
        applicationItems: applicationItems,
        recruitmentProcesses: draft.recruitmentProcesses || []
    }

    if (isEditMode.value) {
        formData.recruitmentRequestId = null;
    }

    if (formData.recruitmentProcesses) {
        formData.recruitmentProcesses = formData.recruitmentProcesses.map(({ stepType, displayOrder }) => ({
            stepType,
            displayOrder
        }));
    }

    isSubmitting = true;

    try {
        if (isEditMode.value) {
            await store.updateExistingRecruitment(recruitmentIdToUpdate.value, formData)
            toast.success('공고가 성공적으로 수정되었습니다.');
            router.push(`/employment/recruitments/${recruitmentIdToUpdate.value}`)
        } else {
            await store.submitRecruitment(formData)
            toast.success('공고가 성공적으로 등록되었습니다.');
            router.push('/employment/recruitments')
        }
        store.clearAllDrafts();
    } catch (error) {
        toast.error('오류가 발생했습니다: ' + error.message);
        isSubmitting = false;
    }
}

watch(selectedIds, (newSelected) => {
    requiredIds.value = requiredIds.value.filter(id => newSelected.includes(id))
}, { deep: true })
</script>

<style scoped>
.preview-pane {
    height: calc(100vh - 220px);
    overflow-y: auto;
    padding: 0 8px;
}

.scroll-pane {
    height: calc(100vh - 220px);
    overflow-y: auto;
}

.preview-card {
    border: 1px solid #e0e0e0;
}
</style>
