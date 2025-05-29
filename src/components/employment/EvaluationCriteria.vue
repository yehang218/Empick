<template>
    <v-container fluid class="pa-4">
        <!-- 평가 기준 목록 -->
        <v-card v-for="(item, index) in criteriaList" :key="index" class="pa-4 mb-4" elevation="0"
            style="border: 1px solid #eee; border-radius: 12px;">
            <v-row align="center" justify="space-between">
                <v-col cols="10">
                    <strong>{{ index + 1 }}. {{ item.title }}</strong>
                    <div class="mt-2">{{ item.description }}</div>
                </v-col>
                <v-col cols="2" class="text-right">
                    <div class="font-weight-bold text-success">{{ item.weight }}%</div>
                    <v-btn size="small" class="ml-2" variant="outlined" color="grey"
                        @click="removeItem(index)">삭제하기</v-btn>
                    <v-btn size="small" class="ml-2" variant="flat" color="success"
                        @click="editItem(index)">수정하기</v-btn>
                </v-col>
            </v-row>
        </v-card>

        <!-- 새 평가 기준 입력 폼 -->
        <v-card v-if="showForm" class="pa-4 mb-4" style="border: 1px solid #eee; border-radius: 12px;">
            <v-row class="mb-1">
                <v-col cols="10">
                    <strong>{{ editingIndex !== null ? (editingIndex + 1) + '. 평가 기준 수정하기' : criteriaList.length + 1 +
                        '. 평가 기준을 입력하세요.' }}</strong>
                    <v-textarea v-model="form.description" placeholder="평가 기준을 설명해주세요." rows="2" auto-grow hide-details
                        class="mt-2" />
                </v-col>
                <v-col cols="2" class="text-right">
                    <v-text-field v-model="form.weight" placeholder="가중치 %" type="number" hide-details density="compact"
                        variant="outlined" class="mt-2" />
                </v-col>
            </v-row>

            <v-row justify="end" class="mt-2">
                <v-btn variant="outlined" color="grey" @click="cancel">취소하기</v-btn>
                <v-btn variant="flat" color="success" class="ml-2" @click="submit">등록하기</v-btn>
            </v-row>
        </v-card>

        <!-- 평가 기준 추가 버튼 -->
        <v-btn v-if="!showForm" color="primary" @click="openForm">평가기준 추가하기</v-btn>
    </v-container>
</template>

<script setup>
import { ref } from 'vue'

const showForm = ref(false)
const editingIndex = ref(null)

const form = ref({
    title: '',
    description: '',
    weight: ''
})

const criteriaList = ref([
    {
        title: '스레드 기반 프로그램의 장점',
        description: '빠른 생성/문맥 전환, 자원 공유로 인한 효율성 등 2가지 이상 언급',
        weight: 40
    }
])

const openForm = () => {
    showForm.value = true
    resetForm()
    editingIndex.value = null
}

const cancel = () => {
    showForm.value = false
    resetForm()
}

const submit = () => {
    if (!form.value.description || !form.value.weight) return

    const generatedTitle = form.value.description.split('\n')[0].slice(0, 40)

    if (editingIndex.value !== null) {
        // 수정
        criteriaList.value[editingIndex.value] = {
            ...form.value,
            title: generatedTitle
        }
    } else {
        // 신규 추가
        criteriaList.value.push({
            ...form.value,
            title: generatedTitle
        })
    }

    showForm.value = false
    resetForm()
}

const editItem = (index) => {
    form.value = { ...criteriaList.value[index] }
    editingIndex.value = index
    showForm.value = true
}

const removeItem = (index) => {
    criteriaList.value.splice(index, 1)
}

const resetForm = () => {
    form.value = {
        title: '',
        description: '',
        weight: ''
    }
}
</script>

<style scoped>
.font-weight-bold {
    font-weight: 600;
}
</style>
