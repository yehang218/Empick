<template>
    <v-container fluid class="pa-4">
        <div class="section-title">평가 기준</div>

        <v-sheet class="criteria-bg" elevation="0">
            <v-card v-for="(item, index) in localList" :key="index" class="criteria-card" flat>
                <!-- 수정 중인 항목 -->
                <div v-if="editingIndex === index" class="card-content">
                    <div class="text-section">
                        <div class="title-row">
                            <span class="criteria-number">{{ index + 1 }}.</span>
                            <v-text-field v-model="form.title" variant="plain" placeholder="평가 기준을 입력하세요."
                                class="input-title" hide-details density="compact" />
                            <div class="weight-input-group">
                                <span class="weight-label">가중치</span>
                                <v-text-field v-model.number="form.weight" variant="plain" placeholder="%"
                                    class="weight-input" hide-details type="number" density="compact" max="100" />
                            </div>
                        </div>
                        <v-textarea v-model="form.description" variant="plain" placeholder="평가 기준을 설명해주세요."
                            class="input-desc" auto-grow hide-details rows="2" density="compact" />

                        <div class="action-buttons d-flex justify-end">
                            <v-btn size="small" variant="flat" color="grey" @click="cancel">취소하기</v-btn>
                            <v-btn size="small" variant="flat" color="success" @click="submit"
                                :disabled="!form.title || !form.description || !form.weight">등록하기</v-btn>
                        </div>
                    </div>
                </div>
                <!-- 일반 항목 -->
                <div v-else class="card-content">
                    <div class="text-section">
                        <div class="title-row">
                            <span class="criteria-title mb-1 mt-3"><strong>{{ index + 1 }}. {{ item.title
                                    }}</strong></span>
                            <div class="plain-weight ml-3 d-flex justify-end">{{ item.weight }}%</div>
                        </div>
                        <div class="criteria-desc">{{ item.description }}</div>
                    </div>

                </div>
                <div v-if="editingIndex != index" class="action-buttons d-flex justify-end">
                    <v-btn size="small" variant="flat" color="grey" @click="removeItem(index)">삭제하기</v-btn>
                    <v-btn size="small" variant="flat" color="success" @click="editItem(index)"
                        :disabled="showForm">수정하기</v-btn>
                </div>
            </v-card>

            <!-- 추가 항목 -->
            <v-card v-if="showForm && editingIndex === null" class="criteria-card" flat>
                <div class="card-content">
                    <div class="text-section full-width">
                        <div class="title-row">
                            <span class="criteria-number">{{ localList.length + 1 }}.</span>
                            <v-text-field v-model="form.title" variant="plain" placeholder="평가 기준을 입력하세요."
                                class="input-title" hide-details density="compact" style="font-size: 16px;" />
                            <div class="weight-input-group">
                                <v-text-field v-model.number="form.weight" variant="plain" placeholder="가중치"
                                    class="weight-input" hide-details type="number" density="compact" max="100"
                                    style="font-size: 10px;" />
                                <span class="weight-label">%</span>
                            </div>
                        </div>

                        <v-textarea v-model="form.description" variant="plain" placeholder="평가 기준을 설명해주세요."
                            class="input-desc" auto-grow hide-details rows="2" density="compact"
                            style="font-size: 14px;" />
                        <div class="action-buttons d-flex justify-end">
                            <v-btn size="small" variant="flat" color="grey" @click="cancel">취소하기</v-btn>
                            <v-btn size="small" variant="flat" color="success" @click="submit"
                                :disabled="!form.title || !form.description || !form.weight">등록하기</v-btn>
                        </div>
                    </div>
                </div>

            </v-card>

            <!-- 추가 버튼 -->
            <div class="add-btn-wrap" v-if="!showForm && editingIndex === null">
                <v-btn variant="outlined" color="success" @click="openForm" class="add-criteria-btn">
                    <v-icon start>mdi-plus</v-icon>
                    평가 기준 추가하기
                </v-btn>
            </div>
        </v-sheet>
    </v-container>
</template>

<script setup>
import { ref, defineProps, defineEmits, watch } from 'vue'

const props = defineProps({
    criteriaList: { type: Array, required: true }
})
const emit = defineEmits(['update:criteriaList'])

const showForm = ref(false)
const editingIndex = ref(null)
const form = ref({ title: '', description: '', weight: '' })
const localList = ref([...props.criteriaList])

watch(() => props.criteriaList, (val) => {
    localList.value = [...val]
})

const openForm = () => {
    resetForm()
    editingIndex.value = null
    showForm.value = true
}

const cancel = () => {
    resetForm()
    showForm.value = false
    editingIndex.value = null
}

const submit = () => {
    if (!form.value.title || !form.value.description || !form.value.weight) return
    const item = { ...form.value }
    if (editingIndex.value !== null) {
        localList.value.splice(editingIndex.value, 1, item)
    } else {
        localList.value.push(item)
    }
    emit('update:criteriaList', [...localList.value])
    resetForm()
    editingIndex.value = null
    showForm.value = false
}

const editItem = (index) => {
    if (showForm.value) return
    form.value = { ...localList.value[index] }
    editingIndex.value = index
    showForm.value = false
}

const removeItem = (index) => {
    localList.value.splice(index, 1)
    emit('update:criteriaList', [...localList.value])
}

const resetForm = () => {
    form.value = { title: '', description: '', weight: '' }
}
</script>

<style scoped>
.criteria-bg {
    background-color: #F8F8F8;
    padding: 24px;
    border-radius: 12px;
}

.criteria-card {
    background-color: #fff;
    border: 1px solid #EBEAED;
    border-radius: 12px;
    padding: 20px;
    margin-bottom: 16px;
}

.card-content {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    gap: 16px;
}

.text-section {
    flex: 1;
    min-width: 0;
}

.text-section.full-width {
    width: 100%;
}

.title-row {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 4px;
}

.criteria-number {
    font-weight: 700;
}

.criteria-title {
    font-size: 16px;
}

.criteria-desc {
    font-size: 14px;
    color: #666;
    margin-top: 8px;
}

.input-title {
    font-size: 16px;
    flex: 1;
    padding: 0 !important;
    margin: 0;
}

.input-desc {
    font-size: 14px;
    padding: 0 !important;
    margin-top: 4px;
}

.right-section {
    width: 160px;
    flex-shrink: 0;
    display: flex;
    flex-direction: column;
    align-items: flex-end;
    gap: 12px;
}

.plain-weight {
    font-size: 14px;
    font-weight: 700;
    color: #2e7d32;
    margin-top: 2px;
}

.weight-input-group {
    display: flex;
    align-items: center;
    gap: 10px;
    background-color: #f4f4f4;
    padding: 4px 10px;
    border-radius: 999px;
}

.weight-input {
    width: 100px;
    max-width: 40px;
    font-size: 10px;
    text-align: right;
    padding: 0;
    margin: 0;
}

.weight-label {
    font-size: 15px;
    color: #285430;
    font-weight: 700;
    white-space: nowrap;
}

.action-buttons {
    display: flex;
    gap: 8px;
}

.add-btn-wrap {
    display: flex;
    justify-content: flex-end;
    margin-top: 12px;
}

.add-criteria-btn {
    background-color: #fff;
    border: 1px solid #E7E7E7;
    color: #81BF6A;
    font-weight: 500;
    border-radius: 8px;
    padding: 6px 12px;
}
</style>
