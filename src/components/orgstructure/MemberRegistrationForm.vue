<template>
    <v-row>
        <!-- 프로필 이미지 업로드 영역 -->
        <v-col cols="2" class="d-flex flex-column align-center">
            <v-sheet width="160" height="160" elevation="1" rounded class="d-flex align-center justify-center mb-4"
                color="#D3D3D3" style="position: relative; overflow: hidden; cursor: pointer;"
                @click="triggerFileInput">
                <template v-if="profileImageUrl">
                    <v-img :src="profileImageUrl" width="160" height="160" cover />
                </template>
                <template v-else>
                    <v-icon size="48" color="grey darken-2">mdi-camera</v-icon>
                </template>
                <input ref="fileInputRef" type="file" accept="image/jpeg,image/png,image/webp" style="display: none;"
                    @change="onProfileImageChange" />
            </v-sheet>
            <v-btn :color="profileImageFile ? 'success' : 'primary'" @click="triggerFileInput" style="width: 100px;">{{
                photoButtonText }}</v-btn>
        </v-col>

        <!-- 입력 폼 -->
        <v-col cols="10">
            <v-row>
                <v-col cols="6">
                    <v-text-field label="이름" v-model="form.name" required />
                </v-col>
                <v-col cols="6">
                    <v-text-field label="생년월일" v-model="form.birth" type="date" required />
                </v-col>
                <v-col cols="6">
                    <v-text-field label="연락처" v-model="form.phone" required placeholder="010-1234-5678" />
                </v-col>
                <v-col cols="6">
                    <v-text-field label="이메일" v-model="form.email" required />
                </v-col>
                <v-col cols="12">
                    <v-text-field label="주소" v-model="form.address" />
                </v-col>
                <v-col cols="6">
                    <v-select label="부서" :items="departments" v-model="form.departmentId" item-title="label"
                        item-value="value" required />
                </v-col>
                <v-col cols="6">
                    <v-select label="직책" :items="positions" v-model="form.positionId" item-title="label"
                        item-value="value" required />
                </v-col>
                <v-col cols="6">
                    <v-select label="직무" :items="jobs" v-model="form.jobId" item-title="label" item-value="value"
                        required />
                </v-col>
                <v-col cols="6">
                    <v-select label="직급" :items="ranks" v-model="form.rankId" item-title="label" item-value="value"
                        required />
                </v-col>
            </v-row>
        </v-col>
    </v-row>
</template>

<script setup>
import { ref } from 'vue'

// Props
defineProps({
    form: {
        type: Object,
        required: true
    },
    profileImageUrl: {
        type: String,
        default: ''
    },
    profileImageFile: {
        type: File,
        default: null
    },
    photoButtonText: {
        type: String,
        default: '사진 업로드'
    },
    departments: {
        type: Array,
        default: () => []
    },
    positions: {
        type: Array,
        default: () => []
    },
    jobs: {
        type: Array,
        default: () => []
    },
    ranks: {
        type: Array,
        default: () => []
    }
})

// Emits
const emit = defineEmits([
    'update:form',
    'profileImageChange'
])

// Refs
const fileInputRef = ref(null)

// Methods
const triggerFileInput = () => {
    fileInputRef.value?.click()
}

const onProfileImageChange = (event) => {
    emit('profileImageChange', event)
}
</script>

<style scoped>
/* 컴포넌트별 스타일이 필요한 경우 여기에 추가 */
</style>