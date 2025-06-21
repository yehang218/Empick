<template>
    <v-container class="pa-6" max-width="600px">
        <h2 class="text-h5 font-weight-bold mb-6">📨 안내 메일 발송</h2>

        <v-form v-model="isValid" ref="formRef">
            <!-- 수신자 이메일 -->
            <v-text-field v-model="email" label="받는 사람 이메일" :rules="[rules.required, rules.email]"
                prepend-icon="mdi-email" clearable />

            <!-- 제목 -->
            <v-text-field v-model="title" label="제목" :rules="[rules.required]" prepend-icon="mdi-format-title"
                clearable />

            <!-- 본문 -->
            <v-textarea v-model="content" label="본문 내용" :rules="[rules.required]" prepend-icon="mdi-text" auto-grow
                rows="5" clearable />

            <!-- 발송 버튼 -->
            <v-btn :loading="sending" color="primary" class="mt-4" block @click="sendMail">
                📤 메일 발송
            </v-btn>
        </v-form>

        <!-- 성공 메시지 -->
        <v-snackbar v-model="snackbar" color="success" timeout="3000">
            메일이 성공적으로 발송되었습니다!
        </v-snackbar>

        <!-- 에러 메시지 -->
        <v-alert v-if="errorMessage" type="error" class="mt-4" dense>
            {{ errorMessage }}
        </v-alert>
    </v-container>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useMailStore } from '@/stores/mailStore'
import { useMemberStore } from '@/stores/memberStore'

const email = ref('')
const title = ref('')
const content = ref('')
const isValid = ref(false)
const formRef = ref(null)

const sending = ref(false)
const snackbar = ref(false)
const errorMessage = ref('')

const mailStore = useMailStore()
const memberStore = useMemberStore()

const rules = {
    required: value => !!value || '필수 입력 항목입니다.',
    email: value => /.+@.+\..+/.test(value) || '유효한 이메일 주소를 입력하세요.',
}

const sendMail = async () => {
    if (!(await formRef.value.validate())) return

    sending.value = true
    errorMessage.value = ''

    const dto = {
        id: null, // 서버에서 자동 생성
        applicantId: null, // 안내 메일이므로 특정 지원자 ID는 null
        email: email.value,
        title: title.value,
        content: content.value,
        senderId: memberStore.form.id ?? 1, // memberStore에서 현재 사용자 ID 가져오기
        sendedAt: new Date().toISOString(),
    }

    try {
        await mailStore.sendMail(dto)
        snackbar.value = true

        // 입력 초기화
        email.value = ''
        title.value = ''
        content.value = ''
        formRef.value.resetValidation()
    } catch (e) {
        errorMessage.value = e.message || '메일 발송에 실패했습니다.'
    } finally {
        sending.value = false
    }
}

// 컴포넌트 마운트 시 사용자 정보 로드
onMounted(async () => {
    try {
        // memberStore에 사용자 정보가 없으면 로드
        if (!memberStore.form.id) {
            await memberStore.getMyInfo()
        }
    } catch (error) {
        console.error('사용자 정보 로드 실패:', error)
    }
})
</script>