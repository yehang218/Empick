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
                <span v-if="selectedTemplateName" class="mr-4 text-subtitle-1 text-grey-darken-1">
                    선택된 템플릿: <strong>{{ selectedTemplateName }}</strong>
                </span>
                <v-btn color="secondary" @click="selectIntroduceTemplate">
                    자기소개서 템플릿 선택
                </v-btn>
            </div>
        </v-row>

        <v-row>
            <!-- 미리보기 영역 -->
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

            <!-- 항목 카테고리 선택 -->
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

        <v-btn color="primary" class="mt-6" @click="submit">
            {{ isEditMode ? '수정 완료' : '공고 등록' }}
        </v-btn>
    </v-container>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter, useRoute, onBeforeRouteLeave } from 'vue-router'
import { useRecruitmentStore } from '@/stores/recruitmentStore'
import { getInputTypeLabel } from '@/constants/employment/inputTypes'
import { useMemberStore } from '@/stores/memberStore'
import IntroduceTemplateSelectModal from '@/components/employment/IntroduceTemplateSelectModal.vue'
import { useToast } from 'vue-toastification'

const router = useRouter()
const route = useRoute()
const store = useRecruitmentStore()
const memberStore = useMemberStore()
const toast = useToast()
const isEditMode = computed(() => route.query.from === 'edit')
const recruitmentIdToUpdate = computed(() => Number(route.query.id))

const requestId = route.query.requestId
const showTemplateModal = ref(false)
const selectedTemplateName = ref('')

// 선택된 항목 ID와 필수 여부
const selectedIds = ref([])
const requiredIds = ref([])

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
    showTemplateModal.value = true
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

    // 수정 모드일 때 스토어에 저장된 값으로 초기화
    if (store.selectedApplicationItemIds.length > 0) {
        selectedIds.value = [...store.selectedApplicationItemIds]
    }
    if (store.requiredApplicationItemIds.length > 0) {
        requiredIds.value = [...store.requiredApplicationItemIds]
    }
    if (store.draftRecruitment?.introduceTemplateTitle) {
        selectedTemplateName.value = store.draftRecruitment.introduceTemplateTitle;
    }
})

const onTemplateSelected = (selection) => {
    if (store.draftRecruitment) {
        store.draftRecruitment.introduceTemplateId = selection.id
        store.draftRecruitment.introduceTemplateTitle = selection.title
        selectedTemplateName.value = selection.title
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

    // ID가 있는 프로세스와 없는 프로세스가 섞이는 문제를 해결하기 위해,
    // 전송 직전에 ID를 모두 제거하여 '전체 교체' 방식으로 전송합니다.
    if (formData.recruitmentProcesses) {
        formData.recruitmentProcesses = formData.recruitmentProcesses.map(({ stepType, displayOrder }) => ({
            stepType,
            displayOrder
        }));
    }

    isSubmitting = true; // 제출 시작 플래그

    try {
        if (isEditMode.value) {
            // 수정 모드
            await store.updateExistingRecruitment(recruitmentIdToUpdate.value, formData)
            toast.success('공고가 성공적으로 수정되었습니다.');
            router.push(`/employment/recruitments/${recruitmentIdToUpdate.value}`)
        } else {
            // 등록 모드
            await store.submitRecruitment(formData)
            toast.success('공고가 성공적으로 등록되었습니다.');
            router.push('/employment/recruitments')
        }

        // 성공 후 초기화
        store.clearAllDrafts();

    } catch (error) {
        console.error('처리 중 오류 발생:', error)
        toast.error(`오류 발생: ${error.message}`);
    } finally {
        isSubmitting = false; // 제출 완료 플래그
    }
}

// selectedIds가 변경되면, 더 이상 선택되지 않은 항목을 requiredIds에서 제거
watch(selectedIds, (newSelected) => {
    requiredIds.value = requiredIds.value.filter(id => newSelected.includes(id))
}, { deep: true })

let isSubmitting = false;

onBeforeRouteLeave((to) => {
    // 제출 중이거나 이전 단계로 돌아가는 것이 아니라면 상태 초기화
    if (isSubmitting || to.name === 'RecruitmentCreate' || to.name === 'RecruitmentUpdate') {
        return;
    }
    store.clearAllDrafts();
});
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
