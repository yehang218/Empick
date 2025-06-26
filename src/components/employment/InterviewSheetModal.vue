<template>
    <v-dialog v-model="internalVisible" persistent max-width="1000">
        <v-card class="sheet-modal-card">
            <div class="modal-header">
                <div class="header-content">
                    <v-icon color="primary" size="28" class="mr-3">mdi-clipboard-list</v-icon>
                    <h2 class="modal-title">평가표 선택</h2>
                </div>
                <v-btn 
                    icon 
                    @click="$emit('close')" 
                    class="close-btn"
                    variant="text"
                >
                    <v-icon>mdi-close</v-icon>
                </v-btn>
            </div>
            
            <v-card-text class="modal-content">
                <v-row>
                    <v-col cols="12" lg="6">
                        <div class="list-section">
                            <div class="section-header">
                                <v-icon color="primary" class="mr-2">mdi-file-document</v-icon>
                                <h3 class="section-title">평가표 목록</h3>
                            </div>
                            <div class="list-container">
                                <OneColumnList 
                                    title="평가표" 
                                    :items="sheetNames" 
                                    @update:selectedItem="onSelectSheet" 
                                />
                            </div>
                        </div>
                    </v-col>
                    
                    <v-col cols="12" lg="6">
                        <div class="list-section">
                            <div class="section-header">
                                <v-icon color="primary" class="mr-2">mdi-criteria</v-icon>
                                <h3 class="section-title">평가 기준</h3>
                            </div>
                            <div class="list-container">
                                <OneColumnList 
                                    title="평가 기준" 
                                    :items="criteriaLabels" 
                                    @update:selectedItem="onSelectCriteria" 
                                />
                            </div>
                        </div>
                    </v-col>
                </v-row>
                
                <v-divider class="my-4" />
                
                <div class="detail-section">
                    <div class="section-header">
                        <v-icon color="primary" class="mr-2">mdi-information</v-icon>
                        <h3 class="section-title">상세 내용</h3>
                    </div>
                    <div class="detail-content">
                        <div v-if="selectedCriteria?.content" class="content-text">
                            {{ selectedCriteria.content }}
                        </div>
                        <div v-else class="empty-content">
                            <v-icon size="32" color="grey-lighten-1" class="mb-2">mdi-text-box-outline</v-icon>
                            <p>선택된 항목이 없습니다.</p>
                        </div>
                    </div>
                </div>
            </v-card-text>
            
            <v-card-actions class="modal-actions">
                <v-spacer />
                <v-btn 
                    color="primary" 
                    @click="confirmSelection"
                    class="confirm-btn"
                    :disabled="!selectedSheet"
                    prepend-icon="mdi-check"
                >
                    선택 완료
                </v-btn>
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

<style scoped>
.sheet-modal-card {
    border-radius: 20px;
    overflow: hidden;
    box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
}

.modal-header {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    padding: 1.5rem 2rem;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.header-content {
    display: flex;
    align-items: center;
}

.modal-title {
    font-size: 1.5rem;
    font-weight: 600;
    margin: 0;
}

.close-btn {
    color: white !important;
    background: rgba(255, 255, 255, 0.1);
    border-radius: 50%;
    transition: all 0.3s ease;
}

.close-btn:hover {
    background: rgba(255, 255, 255, 0.2);
    transform: scale(1.1);
}

.modal-content {
    padding: 2rem;
    background: #f8f9fa;
}

.list-section {
    background: white;
    border-radius: 16px;
    padding: 1.5rem;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
    border: 1px solid rgba(255, 255, 255, 0.2);
    height: 100%;
    transition: all 0.3s ease;
}

.list-section:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
}

.section-header {
    display: flex;
    align-items: center;
    margin-bottom: 1rem;
    padding-bottom: 0.75rem;
    border-bottom: 2px solid #e8f5e8;
}

.section-title {
    font-size: 1.1rem;
    font-weight: 600;
    color: #2c3e50;
    margin: 0;
}

.list-container {
    min-height: 200px;
}

.detail-section {
    background: white;
    border-radius: 16px;
    padding: 1.5rem;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
    border: 1px solid rgba(255, 255, 255, 0.2);
}

.detail-content {
    min-height: 120px;
    display: flex;
    align-items: center;
    justify-content: center;
}

.content-text {
    background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
    padding: 1.5rem;
    border-radius: 12px;
    border-left: 4px solid #667eea;
    font-size: 1rem;
    line-height: 1.6;
    color: #2c3e50;
    width: 100%;
}

.empty-content {
    text-align: center;
    color: #6c757d;
    padding: 2rem;
}

.empty-content p {
    margin: 0;
    font-size: 1rem;
}

.modal-actions {
    background: #f8f9fa;
    padding: 1.5rem 2rem;
    border-top: 1px solid #e9ecef;
}

.confirm-btn {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%) !important;
    color: white !important;
    border: none;
    padding: 0.75rem 2rem;
    border-radius: 12px;
    font-weight: 600;
    transition: all 0.3s ease;
    display: flex;
    align-items: center;
    justify-content: center;
    line-height: 1;
}

.confirm-btn:hover:not(:disabled) {
    transform: translateY(-2px);
    box-shadow: 0 8px 25px rgba(102, 126, 234, 0.4);
    color: white !important;
}

.confirm-btn:disabled {
    background: linear-gradient(135deg, #6c757d 0%, #495057 100%) !important;
    opacity: 0.6;
    cursor: not-allowed;
    color: white !important;
}

/* 반응형 디자인 */
@media (max-width: 768px) {
    .modal-header {
        padding: 1rem 1.5rem;
    }
    
    .modal-title {
        font-size: 1.3rem;
    }
    
    .modal-content {
        padding: 1.5rem;
    }
    
    .list-section {
        margin-bottom: 1rem;
    }
}
</style>
