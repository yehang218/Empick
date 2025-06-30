<template>
    <div class="email-bg">
        <v-container class="d-flex justify-center align-center fill-height email-center-container">
            <transition name="fade">
                <div v-if="loadingScreen" class="loading-overlay">
                    <div class="plane-animation">
                        <div class="plane-body">
                            <svg width="80" height="80" viewBox="0 0 80 80">
                                <polygon points="10,40 70,10 55,40 70,70" fill="#42a5f5" stroke="#1976d2"
                                    stroke-width="3" />
                                <polygon points="10,40 55,40 70,70 40,55" fill="#90caf9" stroke="#1976d2"
                                    stroke-width="2" />
                            </svg>
                        </div>
                        <div class="plane-trail"></div>
                        <div class="plane-progress">
                            <v-progress-linear indeterminate color="blue lighten-2" height="8"
                                rounded></v-progress-linear>
                        </div>
                        <div class="plane-text">메일을 발송 중입니다...</div>
                    </div>
                </div>
            </transition>
            <transition name="fade">
                <div v-if="snackbar" class="center-success-modal">
                    <div class="center-success-content">
                        <span class="emoji">🎉</span>
                        <div class="center-success-text">메일이 성공적으로 발송되었습니다!</div>
                    </div>
                </div>
            </transition>
            
            <!-- 제목을 창 밖으로 이동 -->
            <div class="page-title-container">
                <h2 class="email-title text-h4 font-weight-bold animated-title">📨 안내 메일 발송</h2>
            </div>
            
            <v-card class="glass-card pa-12 email-card-big d-flex flex-column align-center justify-center" elevation="12">
                <v-form v-model="isValid" ref="formRef" class="animated-form w-100">
                    <!-- 수신자 추가 섹션 -->
                    <div class="recipient-section mb-6">
                        <v-card class="recipient-card" elevation="4">
                            <v-card-title class="recipient-title">
                                <v-icon class="mr-2">mdi-account-multiple</v-icon>
                                수신자 추가
                            </v-card-title>
                            <v-card-text>
                                <v-row>
                                    <v-col cols="8">
                                        <v-text-field 
                                            v-model="newRecipient.email" 
                                            label="이메일 주소" 
                                            :rules="[rules.required, rules.email]"
                                            prepend-icon="mdi-email"
                                            clearable
                                            class="animated-input"
                                            @keyup.enter="addRecipient"
                                        />
                                    </v-col>
                                    <v-col cols="4">
                                        <v-btn 
                                            color="success" 
                                            @click="addRecipient"
                                            :disabled="!newRecipient.email || !isValidEmail(newRecipient.email)"
                                            class="add-btn"
                                            block
                                        >
                                            <v-icon>mdi-plus</v-icon>
                                            추가
                                        </v-btn>
                                    </v-col>
                                </v-row>
                            </v-card-text>
                        </v-card>
                    </div>

                    <!-- 수신자 목록 -->
                    <div class="recipient-list-section mb-6" v-if="recipients.length > 0">
                        <v-card class="recipient-list-card" elevation="4">
                            <v-card-title class="recipient-title">
                                <v-icon class="mr-2">mdi-format-list-bulleted</v-icon>
                                수신자 목록 ({{ recipients.length }}명)
                            </v-card-title>
                            <v-card-text>
                                <v-list class="recipient-list">
                                    <v-list-item 
                                        v-for="(recipient, index) in recipients" 
                                        :key="index"
                                        class="recipient-item"
                                    >
                                        <v-list-item-content class="recipient-content">
                                            <div class="recipient-email-container">
                                                <span class="recipient-email">{{ recipient.email }}</span>
                                                <span 
                                                    @click="removeRecipient(index)"
                                                    class="remove-x"
                                                >
                                                    ×
                                                </span>
                                            </div>
                                        </v-list-item-content>
                                    </v-list-item>
                                </v-list>
                            </v-card-text>
                        </v-card>
                    </div>

                    <v-text-field v-model="title" label="제목" :rules="[rules.required]" prepend-icon="mdi-format-title"
                        clearable class="animated-input email-input mb-6" />
                    <v-textarea v-model="content" label="본문 내용" :rules="[rules.required]" prepend-icon="mdi-text"
                        auto-grow rows="10" clearable class="animated-input email-textarea mb-8" />
                    <v-btn 
                        :loading="sending" 
                        color="primary" 
                        class="mt-6 animated-btn email-btn" 
                        block 
                        @click="sendMail"
                        :disabled="recipients.length === 0"
                    >
                        <span v-if="!sending">📤 메일 발송 ({{ recipients.length }}명)</span>
                        <span v-else>발송 중...</span>
                    </v-btn>
                </v-form>
                <transition name="fade">
                    <v-alert v-if="errorMessage" type="error" class="mt-6 animated-alert" border="left" colored-border>
                        <span class="emoji">❌</span> {{ errorMessage }}
                    </v-alert>
                </transition>
            </v-card>
        </v-container>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useMailStore } from '@/stores/mailStore'
import { useMemberStore } from '@/stores/memberStore'

const title = ref('')
const content = ref('')
const isValid = ref(false)
const formRef = ref(null)

const sending = ref(false)
const snackbar = ref(false)
const errorMessage = ref('')
const loadingScreen = ref(false)

// 수신자 관련 상태
const newRecipient = ref({ email: '' })
const recipients = ref([])

const mailStore = useMailStore()
const memberStore = useMemberStore()

const rules = {
    required: value => !!value || '필수 입력 항목입니다.',
    email: value => /.+@.+\..+/.test(value) || '유효한 이메일 주소를 입력하세요.',
}

// 이메일 유효성 검사
const isValidEmail = (email) => {
    return /.+@.+\..+/.test(email)
}

// 수신자 추가
const addRecipient = () => {
    if (newRecipient.value.email && isValidEmail(newRecipient.value.email)) {
        // 중복 체크
        const isDuplicate = recipients.value.some(r => r.email === newRecipient.value.email)
        if (isDuplicate) {
            errorMessage.value = '이미 추가된 이메일입니다.'
            setTimeout(() => { errorMessage.value = '' }, 3000)
            return
        }
        
        recipients.value.push({ email: newRecipient.value.email })
        newRecipient.value.email = ''
        errorMessage.value = ''
    }
}

// 수신자 삭제
const removeRecipient = (index) => {
    recipients.value.splice(index, 1)
}

// 메일 발송
const sendMail = async () => {
    if (recipients.value.length === 0) {
        errorMessage.value = '최소 한 명의 수신자를 추가해주세요.'
        return
    }
    
    if (!(await formRef.value.validate())) return
    
    sending.value = true
    errorMessage.value = ''
    loadingScreen.value = true
    
    try {
        await new Promise(res => setTimeout(res, 1200)) // 애니메이션용 딜레이
        
        // 모든 수신자에게 메일 발송
        const sendPromises = recipients.value.map(recipient => {
            const dto = {
                id: null,
                applicantId: null,
                email: recipient.email,
                title: title.value,
                content: content.value,
                senderId: memberStore.form.id ?? 1,
                sendedAt: new Date().toISOString(),
            }
            return mailStore.sendMail(dto)
        })
        
        await Promise.all(sendPromises)
        
        snackbar.value = true
        // 입력 초기화
        title.value = ''
        content.value = ''
        recipients.value = []
        newRecipient.value.email = ''
        formRef.value.resetValidation()
        setTimeout(() => { snackbar.value = false }, 2200)
    } catch (e) {
        errorMessage.value = e.message || '메일 발송에 실패했습니다.'
    } finally {
        sending.value = false
        setTimeout(() => { loadingScreen.value = false }, 900)
    }
}

onMounted(async () => {
    try {
        if (!memberStore.form.id) {
            await memberStore.getMyInfo()
        }
    } catch (error) {
        console.error('사용자 정보 로드 실패:', error)
    }
})
</script>

<style scoped>
.email-bg {
    min-height: 100vh;
    width: 100vw;
    background: linear-gradient(135deg, #e3f0ff 0%, #fbeaff 100%);
    animation: bg-move 12s linear infinite alternate;
    overflow: auto;
}

@keyframes bg-move {
    0% {
        background-position: 0% 50%;
    }

    100% {
        background-position: 100% 50%;
    }
}

.glass-card {
    background: rgba(255, 255, 255, 0.85);
    border-radius: 32px;
    box-shadow: 0 8px 32px 0 rgba(80, 120, 200, 0.18);
    backdrop-filter: blur(8px);
    border: 1.5px solid #e3e3f3;
    transition: box-shadow 0.2s;
    max-width: 700px;
    min-width: 340px;
}

.animated-title {
    letter-spacing: 0.01em;
    background: linear-gradient(90deg, #42a5f5 30%, #ab47bc 100%);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    animation: title-fade 1.2s;
}

@keyframes title-fade {
    0% {
        opacity: 0;
        transform: translateY(-30px);
    }

    100% {
        opacity: 1;
        transform: translateY(0);
    }
}

.animated-form {
    animation: form-fade 1.2s 0.2s both;
}

@keyframes form-fade {
    0% {
        opacity: 0;
        transform: translateY(30px);
    }

    100% {
        opacity: 1;
        transform: translateY(0);
    }
}

.animated-input {
    transition: box-shadow 0.18s, border 0.18s;
    border-radius: 12px;
}

.animated-input input:focus,
.animated-input textarea:focus {
    box-shadow: 0 0 0 2px #42a5f5;
    border-color: #42a5f5;
}

.animated-btn {
    font-size: 1.15rem;
    font-weight: 700;
    border-radius: 16px;
    box-shadow: 0 2px 12px #42a5f522;
    transition: background 0.18s, box-shadow 0.18s;
}

.animated-btn:hover {
    background: linear-gradient(90deg, #42a5f5 30%, #ab47bc 100%);
    color: #fff;
    box-shadow: 0 4px 24px #ab47bc33;
}

.animated-alert {
    font-size: 1.1rem;
    border-radius: 12px;
    animation: alert-pop 0.7s;
}

@keyframes alert-pop {
    0% {
        opacity: 0;
        transform: scale(0.8);
    }

    100% {
        opacity: 1;
        transform: scale(1);
    }
}

.emoji {
    font-size: 1.5rem;
    margin-right: 0.5rem;
}

.fade-enter-active,
.fade-leave-active {
    transition: opacity 0.5s;
}

.fade-enter-from,
.fade-leave-to {
    opacity: 0;
}

.loading-overlay {
    position: fixed;
    z-index: 9999;
    left: 0;
    top: 0;
    right: 0;
    bottom: 0;
    background: rgba(255, 255, 255, 0.92);
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    animation: fadein 0.5s;
}

@keyframes fadein {
    0% {
        opacity: 0;
    }

    100% {
        opacity: 1;
    }
}

.plane-animation {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
}

.plane-body {
    animation: plane-fly 1.2s infinite alternate cubic-bezier(.4, 2, .6, 1);
}

@keyframes plane-fly {
    0% {
        transform: translateY(0) rotate(-8deg);
    }

    100% {
        transform: translateY(-24px) rotate(8deg);
    }
}

.plane-trail {
    width: 80px;
    height: 12px;
    margin-top: -10px;
    background: linear-gradient(90deg, #42a5f5 0%, #fff 100%);
    border-radius: 8px;
    filter: blur(2px);
    opacity: 0.5;
    animation: trail-move 1.2s infinite alternate;
}

@keyframes trail-move {
    0% {
        width: 80px;
        opacity: 0.5;
    }

    100% {
        width: 120px;
        opacity: 0.8;
    }
}

.plane-progress {
    width: 180px;
    margin: 32px 0 8px 0;
}

.plane-text {
    font-size: 1.2rem;
    color: #1976d2;
    font-weight: 600;
    margin-top: 8px;
    letter-spacing: 0.01em;
}

.email-center-container {
    min-height: 100vh;
    display: flex !important;
    align-items: center !important;
    justify-content: center !important;
    padding: 0 !important;
    margin: 0 !important;
    width: 100% !important;
    position: relative;
}

.page-title-container {
    position: absolute;
    top: 40px;
    left: 50%;
    transform: translateX(-50%);
    z-index: 10;
    width: 100%;
    padding-bottom: 20px;
}

.email-title {
    font-size: 2.6rem;
    letter-spacing: 0.01em;
    text-align: center;
    background: linear-gradient(90deg, #42a5f5 30%, #ab47bc 100%);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    animation: title-fade 1.2s;
    margin: 0;
}

.glass-card.email-card-big {
    width: 90vw;
    height: auto;
    max-width: 800px;
    max-height: 90vh;
    min-width: 320px;
    min-height: 400px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    box-sizing: border-box;
    overflow-y: auto;
    margin: 0 auto;
    margin-top: 120px;
}

.animated-form {
    width: 100%;
    max-width: 900px;
    margin: 0 auto;
}

.email-input {
    width: 100%;
    font-size: 1.25rem;
}

.email-textarea {
    width: 100%;
    font-size: 1.15rem;
    min-height: 220px;
    max-height: 220px;
    overflow-y: auto;
    resize: none;
}

.email-btn {
    font-size: 1.3rem;
    height: 60px;
    border-radius: 18px;
}

/* 수신자 관련 스타일 */
.recipient-section {
    width: 100%;
}

.recipient-card {
    background: rgba(255, 255, 255, 0.9);
    border-radius: 16px;
    border: 1px solid #e0e0e0;
}

.recipient-title {
    font-size: 1.1rem;
    font-weight: 600;
    color: #1976d2;
    padding: 16px 20px 8px 20px;
}

.add-btn {
    height: 56px;
    border-radius: 12px;
    font-weight: 600;
}

.recipient-list-section {
    width: 100%;
}

.recipient-list-card {
    background: rgba(255, 255, 255, 0.9);
    border-radius: 16px;
    border: 1px solid #e0e0e0;
}

.recipient-list {
    max-height: 150px;
    overflow-y: auto;
    background: transparent;
}

.recipient-item {
    border-radius: 8px;
    margin-bottom: 4px;
    background: rgba(240, 248, 255, 0.5);
    transition: background 0.2s;
}

.recipient-item:hover {
    background: rgba(240, 248, 255, 0.8);
}

.recipient-content {
    display: flex;
    align-items: center;
    justify-content: space-between;
}

.recipient-email-container {
    display: flex;
    align-items: center;
}

.recipient-email {
    font-size: 1rem;
    font-weight: 500;
    color: #1976d2;
}

.remove-x {
    color: red;
    cursor: pointer;
    font-size: 16px;
    margin-left: 8px;
    transition: transform 0.2s ease;
}

.remove-x:hover {
    transform: scale(1.2);
}

@media (max-width: 900px) {
    .glass-card.email-card-big {
        width: 98vw;
        height: auto;
        min-width: 0;
        min-height: 0;
        padding: 1.5rem !important;
        border-radius: 18px;
    }

    .plane-progress {
        width: 120px;
    }

    .email-title {
        font-size: 1.5rem;
    }

    .email-input, .email-textarea {
        font-size: 1rem;
    }

    .email-btn {
        font-size: 1.1rem;
        height: 48px;
    }
}

.center-success-modal {
    position: fixed;
    z-index: 20000;
    left: 0; top: 0; right: 0; bottom: 0;
    display: flex;
    align-items: center;
    justify-content: center;
    background: rgba(255,255,255,0.55);
    animation: fadein 0.3s;
}
.center-success-content {
    background: rgba(255,255,255,0.98);
    border-radius: 32px;
    box-shadow: 0 8px 32px 0 rgba(80,120,200,0.18);
    padding: 3rem 4rem;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    animation: alert-pop 0.7s;
}
.center-success-text {
    font-size: 1.6rem;
    font-weight: 700;
    color: #1976d2;
    margin-top: 1.2rem;
    text-align: center;
}
@media (max-width: 600px) {
    .center-success-content {
        padding: 1.2rem 1rem;
        border-radius: 18px;
    }
    .center-success-text {
        font-size: 1.1rem;
    }
}
</style>