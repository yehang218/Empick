<template>
    <v-container class="py-8" style="max-width: 1100px;">
        <v-row>
            <v-col cols="12">
                <div class="d-flex align-center mb-4">
                    <h2 class="text-h5 font-weight-bold mr-4">기본정보</h2>
                    <v-btn color="primary" variant="outlined" @click="goToResult" class="ml-2">수정요청결과 확인</v-btn>
                </div>
            </v-col>
        </v-row>
        <v-row>
            <!-- 프로필 사진 영역 -->
            <v-col cols="2" class="d-flex flex-column align-center">
                <v-sheet width="160" height="160" elevation="1" rounded class="d-flex align-center justify-center mb-4"
                    color="#D3D3D3" style="position: relative; overflow: hidden;">
                    <v-img :src="profileImageSrc" width="160" height="160" cover />
                </v-sheet>
                <input ref="fileInputRef" type="file" accept="image/*" style="display: none;"
                    @change="onProfileImageChange" />
                <v-btn color="success" @click="triggerFileInput" style="width: 100px;">수정</v-btn>
            </v-col>
            <!-- 인적 정보/소속 정보 -->
            <v-col cols="10">
                <v-row>
                    <v-col cols="12" class="mb-2">
                        <h3 class="text-subtitle-1 font-weight-bold">인적 정보</h3>
                    </v-col>
                    <v-col cols="6">
                        <v-text-field label="이름" v-model="memberStore.form.name" required :disabled="!isEditing" />
                    </v-col>
                    <v-col cols="6">
                        <v-text-field label="사번" v-model="memberStore.form.employeeNumber" disabled />
                    </v-col>
                    <v-col cols="6">
                        <v-text-field label="연락처" v-model="memberStore.form.phone" required placeholder="010-1234-5678"
                            :disabled="!isEditing" />
                    </v-col>
                    <v-col cols="6">
                        <v-text-field label="이메일" v-model="memberStore.form.email" required :disabled="!isEditing" />
                    </v-col>
                    <v-col cols="12" class="mt-6 mb-2">
                        <h3 class="text-subtitle-1 font-weight-bold">소속 정보</h3>
                    </v-col>
                    <v-col cols="6">
                        <v-text-field label="부서" v-model="memberStore.form.departmentName" disabled />
                    </v-col>
                    <v-col cols="6">
                        <v-text-field label="직책" v-model="memberStore.form.positionName" disabled />
                    </v-col>
                    <v-col cols="6">
                        <v-text-field label="입사일" v-model="memberStore.form.hireAt" type="date" disabled />
                    </v-col>
                    <v-col cols="6">
                        <v-text-field label="직무" v-model="memberStore.form.jobName" disabled />
                    </v-col>
                    <v-col cols="6">
                        <v-text-field label="직급" v-model="memberStore.form.rankName" disabled />
                    </v-col>
                </v-row>
            </v-col>
        </v-row>
        <v-row class="mt-8">
            <v-col cols="12" class="d-flex flex-row align-center justify-center gap-4">
                <v-btn v-if="!isEditing" color="primary" style="min-width:120px;" @click="startEditing">수정</v-btn>
                <template v-else>
                    <v-btn color="error" style="min-width:120px;" @click="cancelEditing">취소</v-btn>
                    <v-btn color="success" style="min-width:120px;" @click="saveChanges"
                        :loading="memberStore.loading">저장</v-btn>
                </template>
            </v-col>
        </v-row>
        <v-row>
            <v-col cols="12">
                <v-alert v-if="memberStore.error" type="warning" class="mb-4" border="start" variant="tonal"
                    style="position:fixed;top:24px;right:32px;left:auto;transform:none;z-index:2000;min-width:320px;max-width:90vw;">
                    {{ memberStore.error }}
                </v-alert>
            </v-col>
        </v-row>
    </v-container>
</template>

<script setup>
import { ref, onMounted, watch, computed } from 'vue'
import { useMemberStore } from '@/stores/memberStore'
import { useRouter } from 'vue-router'

const memberStore = useMemberStore()
const router = useRouter()
const fileInputRef = ref(null)
const isEditing = ref(false)
const originalForm = ref(null)

// 프로필 이미지 바인딩: blob url이 있으면 그걸, 없으면 기본 이미지
const profileImageSrc = computed(() => {
    return memberStore.profileImageUrl || require('@/assets/default-profile.png')
})

// form 데이터 변경 감시
watch(() => memberStore.form, (newVal) => {
    console.log('form 데이터 변경:', newVal)
    console.log('form 데이터 타입:', typeof newVal)
    console.log('form 데이터 키:', Object.keys(newVal))
}, { deep: true, immediate: true })

const startEditing = () => {
    originalForm.value = JSON.parse(JSON.stringify(memberStore.form))
    isEditing.value = true
}

const cancelEditing = () => {
    memberStore.form = JSON.parse(JSON.stringify(originalForm.value))
    isEditing.value = false
}

const saveChanges = async () => {
    try {
        await memberStore.updateMyInfo()
        isEditing.value = false
    } catch (error) {
        console.error('정보 수정 실패:', error)
    }
}

const triggerFileInput = () => {
    if (fileInputRef.value) fileInputRef.value.click()
}

const onProfileImageChange = (event) => {
    const file = event.target.files && event.target.files[0]
    if (file) memberStore.setProfileImage(file)
}

const goToResult = () => {
    router.push('/orgstructure/profile/result')
}

onMounted(async () => {
    await memberStore.getMyInfo();
    console.log('form.id:', memberStore.form.id);
    if (memberStore.form.id) {
        console.log('fetchProfileImage 호출, memberId:', memberStore.form.id);
        await memberStore.fetchProfileImage(memberStore.form.id);
    } else {
        console.warn('form.id가 없음! 프로필 이미지 요청 안함');
    }
});
</script>