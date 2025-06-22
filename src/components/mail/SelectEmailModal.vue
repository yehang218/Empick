<template>
    <v-dialog v-model="dialogModel" max-width="500px">
        <v-card>
            <v-card-title class="text-h6 font-weight-bold d-flex align-center">
                <v-icon class="mr-2">mdi-email</v-icon>
                이메일 타입 선택
            </v-card-title>
            <v-card-text class="pt-4">
                <p class="text-body-2 text-grey-darken-1 mb-4">
                    선택된 {{ selectedCount }}명의 지원자에게 발송할 이메일 타입을 선택해주세요.
                </p>

                <v-radio-group v-model="selectedType" class="mt-4">
                    <v-radio value="jobtest" class="mb-3">
                        <template #label>
                            <div class="d-flex align-center">
                                <v-icon color="primary" class="mr-2">mdi-file-document</v-icon>
                                <div>
                                    <div class="font-weight-medium">실무테스트 안내 메일</div>
                                    <div class="text-caption text-grey">실무테스트 링크와 안내사항을 포함한 메일</div>
                                </div>
                            </div>
                        </template>
                    </v-radio>

                    <v-radio value="interview" class="mb-3">
                        <template #label>
                            <div class="d-flex align-center">
                                <v-icon color="primary" class="mr-2">mdi-account-group</v-icon>
                                <div>
                                    <div class="font-weight-medium">면접 일정 안내 메일</div>
                                    <div class="text-caption text-grey">면접 일정과 장소 안내를 포함한 메일</div>
                                </div>
                            </div>
                        </template>
                    </v-radio>
                </v-radio-group>
            </v-card-text>
            <v-card-actions class="pa-4">
                <v-spacer />
                <v-btn variant="outlined" @click="handleCancel">
                    취소
                </v-btn>
                <v-btn color="primary" @click="handleSelect" :disabled="!selectedType">
                    선택
                </v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script setup>
import { computed, ref, watch } from 'vue'

const props = defineProps({
    modelValue: {
        type: Boolean,
        default: false
    },
    selectedCount: {
        type: Number,
        default: 0
    }
})

const emit = defineEmits(['update:modelValue', 'select', 'cancel'])

const dialogModel = computed({
    get: () => props.modelValue,
    set: (value) => emit('update:modelValue', value)
})

const selectedType = ref('')

const handleSelect = () => {
    if (!selectedType.value) return

    emit('select', selectedType.value)
    selectedType.value = ''
    dialogModel.value = false
}

const handleCancel = () => {
    selectedType.value = ''
    emit('cancel')
    dialogModel.value = false
}

// 모달이 열릴 때마다 선택 초기화
watch(() => props.modelValue, (newValue) => {
    if (newValue) {
        selectedType.value = ''
    }
})
</script>