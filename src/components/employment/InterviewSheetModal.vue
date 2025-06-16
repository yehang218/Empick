<template>
    <v-dialog v-model="internalVisible" persistent max-width="900">
        <v-card>
            <v-card-title>
                평가표 선택
                <v-spacer />
                <v-btn icon @click="$emit('close')"><v-icon>mdi-close</v-icon></v-btn>
            </v-card-title>
            <v-card-text>
                <v-row>
                    <v-col cols="6">
                        <OneColumnList title="평가표" :items="sheetNames" @update:selectedItem="onSelectSheet" />
                    </v-col>
                    <v-col cols="6">
                        <OneColumnList title="평가 기준" :items="criteriaLabels" @update:selectedItem="onSelectCriteria" />
                    </v-col>
                </v-row>
                <v-divider class="my-2" />
                <div>
                    <strong>상세 내용:</strong>
                    <div class="mt-1">{{ selectedCriteria?.content || '선택된 항목이 없습니다.' }}</div>
                </div>
            </v-card-text>
            <v-card-actions>
                <v-spacer />
                <v-btn color="primary" @click="confirmSelection">선택</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import OneColumnList from '@/components/OneColumnList.vue'
import { useInterviewSheetStore } from '@/stores/interviewSheetStore'
import { useInterviewCriteriaStore } from '@/stores/interviewCriteriaStore'

const props = defineProps({ modelValue: Boolean })
const emit = defineEmits(['close', 'select-sheet'])

const internalVisible = computed({
    get: () => props.modelValue,
    set: (val) => {
        if (!val) emit('close')  // 닫힐 때만 close 이벤트 emit
    },
})

const sheetStore = useInterviewSheetStore()
const criteriaStore = useInterviewCriteriaStore()

const sheets = computed(() => sheetStore.sheetList)
const sheetNames = computed(() => sheets.value.map(s => s.name))
const criteriaList = computed(() => criteriaStore.criteriaList)
const criteriaLabels = computed(() =>
    criteriaList.value.map(item => `${item.title} (${Math.round(item.weight * 100)}%)`)
)
const selectedSheet = ref(null)
const selectedCriteria = computed(() => criteriaStore.selectedCriteria)

const onSelectSheet = async (sheetName) => {
    const sheet = sheets.value.find(s => s.name === sheetName)
    if (sheet) {
        selectedSheet.value = sheet
        await criteriaStore.fetchCriteriaBySheetId(sheet.id)
    }
}

const onSelectCriteria = (label) => {
    const index = criteriaLabels.value.findIndex(l => l === label)
    if (index !== -1) {
        const criteria = criteriaList.value[index]
        criteriaStore.selectedCriteria = criteria
    }
}

const confirmSelection = () => {
    if (!selectedSheet.value) return
    emit('select-sheet', selectedSheet.value)
}
onMounted(() => sheetStore.fetchAllSheets())
</script>
