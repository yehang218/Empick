<template>
    <v-container class="py-8" style="max-width: 1100px;">
        <v-row>
            <v-col cols="12">
                <h2 class="text-h5 font-weight-bold mb-6">신규 사원 정보 등록</h2>
            </v-col>
        </v-row>
        <v-row>
            <v-col cols="12">
                <v-alert v-if="regStore.alertVisible" type="warning" class="mb-4" border="start" variant="tonal"
                    style="position:fixed;top:24px;right:32px;left:auto;transform:none;z-index:2000;min-width:320px;max-width:90vw;">
                    {{ regStore.alertMessage }}
                </v-alert>
            </v-col>
        </v-row>
        <v-row>
            <!-- 프로필 이미지 업로드 영역 -->
            <v-col cols="2" class="d-flex flex-column align-center">
                <v-sheet width="160" height="160" elevation="1" rounded class="d-flex align-center justify-center mb-4"
                    color="#D3D3D3" style="position: relative; overflow: hidden; cursor: pointer;"
                    @click="triggerFileInput">
                    <template v-if="regStore.profileImageUrl">
                        <v-img :src="regStore.profileImageUrl" width="160" height="160" cover />
                    </template>
                    <template v-else>
                        <v-icon size="48" color="grey darken-2">mdi-camera</v-icon>
                    </template>
                    <input ref="fileInputRef" type="file" accept="image/jpeg,image/png,image/webp"
                        style="display: none;" @change="onProfileImageChange" />
                </v-sheet>
                <v-btn :color="regStore.profileImageFile ? 'success' : 'primary'" @click="triggerFileInput"
                    style="width: 100px;">{{
                        regStore.photoButtonText }}</v-btn>
            </v-col>
            <!-- 입력 폼 -->
            <v-col cols="10">
                <v-row>
                    <v-col cols="6">
                        <v-text-field label="이름" v-model="regStore.form.name" required />
                    </v-col>
                    <v-col cols="6">
                        <v-text-field label="생년월일" v-model="regStore.form.birth" type="date" required />
                    </v-col>
                    <v-col cols="6">
                        <v-text-field label="연락처" v-model="regStore.form.phone" required placeholder="010-1234-5678" />
                    </v-col>
                    <v-col cols="6">
                        <v-text-field label="이메일" v-model="regStore.form.email" required />
                    </v-col>
                    <v-col cols="12">
                        <v-text-field label="주소" v-model="regStore.form.address" />
                    </v-col>
                    <v-col cols="6">
                        <v-select label="부서" :items="departments" v-model="regStore.form.departmentId"
                            item-title="label" item-value="value" required />
                    </v-col>
                    <v-col cols="6">
                        <v-select label="직책" :items="positions" v-model="regStore.form.positionId" item-title="label"
                            item-value="value" required />
                    </v-col>
                    <v-col cols="6">
                        <v-select label="직무" :items="jobs" v-model="regStore.form.jobId" item-title="label"
                            item-value="value" required />
                    </v-col>
                    <v-col cols="6">
                        <v-select label="직급" :items="ranks" v-model="regStore.form.rankId" item-title="label"
                            item-value="value" required />
                    </v-col>
                </v-row>
            </v-col>
        </v-row>
        <v-row class="mt-8">
            <v-col cols="12" class="d-flex flex-column align-center">
                <v-btn color="success" @click="onRegister" size="large">등록</v-btn>
            </v-col>
        </v-row>

        <!-- 확인 모달 -->
        <AlertModal v-if="showConfirmDialog" message="입력하신 내용이 모두 삭제됩니다. 정말로 나가시겠습니까?" @confirm="confirmLeave"
            @cancel="cancelLeave" />
    </v-container>
</template>

<script setup>
import { ref } from 'vue'
import { useMemberRegisterStore } from '@/stores/memberRegisterStore'
import { useToast } from 'vue-toastification'
import { useRouter, onBeforeRouteLeave } from 'vue-router'
import AlertModal from '@/components/common/AlertModal.vue'

const regStore = useMemberRegisterStore()
const toast = useToast()
const fileInputRef = ref(null)
const router = useRouter()
const showConfirmDialog = ref(false)
const pendingNavigation = ref(null)

const departments = [
    { label: '인사', value: 1 },
    { label: '개발', value: 2 },
    { label: '영업', value: 3 },
    { label: '기타', value: 4 },
]
const positions = [
    { label: '미지정', value: 0 },
    { label: '팀장', value: 1 },
    { label: '부장', value: 2 },
]
const jobs = [
    { label: '미지정', value: 0 },
    { label: '개발자', value: 1 },
    { label: '디자이너', value: 2 },
]
const ranks = [
    { label: '사원', value: 0 },
    { label: '대리', value: 1 },
    { label: '과장', value: 2 },
]

const triggerFileInput = () => {
    if (fileInputRef.value) fileInputRef.value.click()
}

const onProfileImageChange = (event) => {
    const file = event.target.files && event.target.files[0]
    if (file) {
        const allowedTypes = ['image/jpeg', 'image/png', 'image/webp']
        if (!allowedTypes.includes(file.type)) {
            toast.error('JPG, PNG, WEBP 형식의 이미지만 업로드할 수 있습니다.')
            return
        }
        if (file.size > 5 * 1024 * 1024) { // 5MB 제한
            toast.error('파일 크기는 5MB를 초과할 수 없습니다.')
            return
        }
        regStore.setProfileImage(file)
    } else {
        regStore.clearProfileImage()
    }
}

const onRegister = async () => {
    try {
        const result = await regStore.registerMemberWithImage()
        if (result) {
            toast.success('사원 등록이 완료되었습니다!')
            // 등록 성공 후 폼 초기화
            regStore.resetForm()
        }
    } catch (error) {
        toast.error(error.message || '사원 등록에 실패했습니다.')
    }
}

// 페이지를 나가기 전에 확인
onBeforeRouteLeave((to, from, next) => {
    // 입력값이 있는지 확인
    const hasInput = Object.values(regStore.form).some(value => value !== null && value !== '') || regStore.profileImageFile

    if (hasInput) {
        pendingNavigation.value = next
        showConfirmDialog.value = true
    } else {
        next()
    }
})

const confirmLeave = () => {
    regStore.resetForm()
    showConfirmDialog.value = false
    if (pendingNavigation.value) {
        pendingNavigation.value()
        pendingNavigation.value = null
    }
}

const cancelLeave = () => {
    showConfirmDialog.value = false
    if (pendingNavigation.value) {
        pendingNavigation.value(false)
        pendingNavigation.value = null
    }
}
</script>