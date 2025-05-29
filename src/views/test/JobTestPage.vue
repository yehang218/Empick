<template>
    <v-container class="criteria-wrapper" fluid>
        <!-- ✅ 회색 박스 바깥 제목 -->
        <div class="section-title">평가 기준</div>

        <v-sheet class="criteria-bg" elevation="0">
            <!-- ✅ 등록된 항목 리스트 -->
            <v-card v-for="(item, index) in criteriaList" :key="index" class="criteria-card" flat>
                <div class="card-content">
                    <div class="text-section">
                        <div class="title-row">
                            <span class="criteria-number">{{ index + 1 }}.</span>
                            <span class="criteria-title"><strong>{{ item.title }}</strong></span>
                        </div>
                        <div class="criteria-desc">{{ item.description }}</div>
                    </div>
                    <div class="right-section">
                        <div class="plain-weight">{{ item.weight }}%</div>
                        <div class="action-buttons">
                            <v-btn size="small" variant="flat" color="grey" @click="removeItem(index)">삭제하기</v-btn>
                            <v-btn size="small" variant="flat" color="success" @click="editItem(index)"
                                :disabled="showForm">수정하기</v-btn>
                        </div>
                    </div>
                </div>
            </v-card>

            <!-- ✅ 입력 폼 -->
            <v-card v-if="showForm" class="criteria-card" flat>
                <div class="card-content">
                    <div class="text-section">
                        <div class="title-row">
                            <span class="criteria-number">{{ editingIndex !== null ? editingIndex + 1 :
                                criteriaList.length + 1 }}.</span>
                            <v-text-field v-model="form.title" variant="plain" placeholder="평가 기준을 입력하세요."
                                class="input-title" hide-details />
                        </div>
                        <v-textarea v-model="form.description" variant="plain" placeholder="평가 기준을 설명해주세요."
                            class="input-desc" auto-grow hide-details />
                    </div>
                    <div class="right-section">
                        <div class="weight-input-group">
                            <span class="weight-label">가중치</span>
                            <v-text-field v-model="form.weight" variant="plain" placeholder="%" class="weight-input"
                                hide-details type="number" />
                        </div>
                        <div class="action-buttons form">
                            <v-btn size="small" variant="flat" color="grey" @click="cancel">취소하기</v-btn>
                            <v-btn size="small" variant="flat" color="success" @click="submit">등록하기</v-btn>
                        </div>
                    </div>
                </div>
            </v-card>

            <!-- ✅ 추가 버튼 -->
            <div class="add-btn-wrap" v-if="!showForm">
                <v-btn variant="outlined" color="success" @click="openForm" class="add-criteria-btn">
                    <v-icon start>mdi-plus</v-icon>
                    평가 기준 추가하기
                </v-btn>

            </div>
        </v-sheet>
    </v-container>
</template>

<script setup>
import { ref } from 'vue'

const showForm = ref(false)
const editingIndex = ref(null)
const form = ref({ title: '', description: '', weight: '' })
const criteriaList = ref([])

const openForm = () => {
    resetForm()
    editingIndex.value = null
    showForm.value = true
}

const cancel = () => {
    resetForm()
    showForm.value = false
}

const submit = () => {
    if (!form.value.title || !form.value.description || !form.value.weight) return
    const item = { ...form.value }
    if (editingIndex.value !== null) {
        criteriaList.value.splice(editingIndex.value, 1, item)
    } else {
        criteriaList.value.push(item)
    }
    resetForm()
    showForm.value = false
    editingIndex.value = null
}

const editItem = (index) => {
    if (showForm.value) return
    form.value = { ...criteriaList.value[index] }
    editingIndex.value = index
    showForm.value = true
}

const removeItem = (index) => {
    criteriaList.value.splice(index, 1)
}

const resetForm = () => {
    form.value = { title: '', description: '', weight: '' }
}
</script>

<style scoped>
.criteria-wrapper {
    padding: 24px;
    max-width: 900px;
    margin: 0 auto;
}

.section-title {
    font-size: 18px;
    font-weight: bold;
    margin-bottom: 12px;
}

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
}

.text-section {
    flex: 1;
}

.title-row {
    display: flex;
    align-items: center;
    gap: 6px;
    font-size: 16px;
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
    width: 300px;
    padding: 0;
    margin: 0;
}

.input-desc {
    font-size: 14px;
    padding: 0;
    padding-right: 10px;
    margin-top: 8px;
}

.right-section {
    display: flex;
    flex-direction: column;
    align-items: flex-end;
    min-width: 140px;
    justify-content: space-between;
    gap: 16px;
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
    gap: 6px;
    padding: 0px 10px;
    border-radius: 999px;
    background-color: #f4f4f4;
}

.weight-label {
    font-size: 13px;
    color: #888;
}

.weight-input {
    width: 50px;
    font-size: 14px;
    text-align: right;
    padding: 0;
    margin: 0;
}

.action-buttons {
    display: flex;
    gap: 8px;
}

.action-buttons.form {
    margin-top: auto;
}

.add-btn-wrap {
    display: flex;
    justify-content: flex-end;
    margin-top: 12px;
    /* 배경색 제거 */
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