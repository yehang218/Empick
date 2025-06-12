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
                <template v-if="memberStore.profileImageUrl">
                    <v-img :src="memberStore.profileImageUrl" width="160" height="200" class="mb-4" cover />
                </template>
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
                        <v-text-field label="이름" v-model="memberStore.form.name" required />
                    </v-col>
                    <v-col cols="6">
                        <v-text-field label="생년월일" v-model="memberStore.form.birth" type="date" required />
                    </v-col>
                    <v-col cols="6">
                        <v-text-field label="연락처" v-model="memberStore.form.phone" required
                            placeholder="010-1234-5678" />
                    </v-col>
                    <v-col cols="6">
                        <v-text-field label="이메일" v-model="memberStore.form.email" required />
                    </v-col>
                    <v-col cols="12">
                        <v-text-field label="주소" v-model="memberStore.form.address" />
                    </v-col>
                    <v-col cols="12" class="mt-6 mb-2">
                        <h3 class="text-subtitle-1 font-weight-bold">소속 정보</h3>
                    </v-col>
                    <v-col cols="6">
                        <v-select label="부서" :items="departments" v-model="memberStore.form.departmentId"
                            item-title="label" item-value="value" required />
                    </v-col>
                    <v-col cols="6">
                        <v-select label="직책" :items="positions" v-model="memberStore.form.positionId" item-title="label"
                            item-value="value" required />
                    </v-col>
                    <v-col cols="6">
                        <v-text-field label="입사일" v-model="memberStore.form.hireAt" type="date" required />
                    </v-col>
                    <v-col cols="6">
                        <v-select label="직무" :items="jobs" v-model="memberStore.form.jobId" item-title="label"
                            item-value="value" required />
                    </v-col>
                    <v-col cols="6">
                        <v-select label="직급" :items="ranks" v-model="memberStore.form.rankId" item-title="label"
                            item-value="value" required />
                    </v-col>
                </v-row>
            </v-col>
        </v-row>
        <v-row class="mt-8">
            <v-col cols="12" class="d-flex flex-row align-center justify-center gap-4">
                <v-btn color="grey" style="min-width:120px;" disabled>수정요청</v-btn>
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
import { ref, onMounted } from 'vue'
import { useMemberStore } from '@/stores/memberStore'
import { useRouter } from 'vue-router'

const memberStore = useMemberStore()
const router = useRouter()
const fileInputRef = ref(null)

const departments = [
    { label: '백엔드', value: 1 },
    { label: '프론트엔드', value: 2 },
    { label: '기획', value: 3 },
    { label: '디자인', value: 4 },
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
    if (file) memberStore.setProfileImage(file)
}

const goToResult = () => {
    router.push('/orgstructure/profile/result')
}

onMounted(async () => {
    await memberStore.getMyInfo()
    if (memberStore.form.id) {
        await memberStore.fetchProfileImage(memberStore.form.id)
    }
})
</script>