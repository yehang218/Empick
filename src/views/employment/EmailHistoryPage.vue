<template>
    <v-container class="pa-4">
        <h2 class="text-h5 font-weight-bold mb-4">📧 이메일 발송 이력</h2>

        <v-data-table :headers="headers" :items="mails" :loading="loading" loading-text="이메일 발송 이력을 불러오는 중..."
            class="elevation-1" item-value="id">
            <template #item.sendedAt="{ item }">
                {{ formatDate(item.sendedAt) }}
            </template>
            <template #item.senderName="{ item }">
                {{ getSenderName(item.senderId) }}
            </template>
            <template #item.actions="{ item }">
                <v-btn size="small" color="primary" variant="outlined" @click="viewMailDetails(item)">
                    상세보기
                </v-btn>
            </template>
        </v-data-table>

        <!-- 상세보기 다이얼로그 -->
        <v-dialog v-model="dialog" max-width="700px">
            <v-card>
                <v-card-title class="text-h6 font-weight-bold d-flex align-center">
                    <v-icon class="mr-2">mdi-email</v-icon>
                    이메일 상세 정보
                </v-card-title>
                <v-card-text v-if="selectedMail" class="pt-4">
                    <v-row>
                        <v-col cols="12" sm="6">
                            <v-list-item class="px-0">
                                <template #prepend>
                                    <v-icon color="primary">mdi-format-title</v-icon>
                                </template>
                                <v-list-item-title class="text-subtitle-2 text-grey-darken-1">제목</v-list-item-title>
                                <v-list-item-subtitle class="text-body-1 font-weight-medium">{{ selectedMail.title }}</v-list-item-subtitle>
                            </v-list-item>
                        </v-col>
                        <v-col cols="12" sm="6">
                            <v-list-item class="px-0">
                                <template #prepend>
                                    <v-icon color="primary">mdi-email-outline</v-icon>
                                </template>
                                <v-list-item-title class="text-subtitle-2 text-grey-darken-1">받는 이메일일</v-list-item-title>
                                <v-list-item-subtitle class="text-body-1 font-weight-medium">{{ selectedMail.email }}</v-list-item-subtitle>
                            </v-list-item>
                        </v-col>
                        <v-col cols="12" sm="6">
                            <v-list-item class="px-0">
                                <template #prepend>
                                    <v-icon color="primary">mdi-account</v-icon>
                                </template>
                                <v-list-item-title class="text-subtitle-2 text-grey-darken-1">보낸 사람</v-list-item-title>
                                <v-list-item-subtitle class="text-body-1 font-weight-medium">{{ getSenderName(selectedMail.senderId) }}</v-list-item-subtitle>
                            </v-list-item>
                        </v-col>
                        <v-col cols="12" sm="6">
                            <v-list-item class="px-0">
                                <template #prepend>
                                    <v-icon color="primary">mdi-clock-outline</v-icon>
                                </template>
                                <v-list-item-title class="text-subtitle-2 text-grey-darken-1">보낸 시각</v-list-item-title>
                                <v-list-item-subtitle class="text-body-1 font-weight-medium">{{ formatDate(selectedMail.sendedAt) }}</v-list-item-subtitle>
                            </v-list-item>
                        </v-col>
                    </v-row>
                    
                    <v-divider class="my-4"></v-divider>
                    
                    <div class="mt-4">
                        <div class="d-flex align-center mb-3">
                            <v-icon color="primary" class="mr-2">mdi-text</v-icon>
                            <span class="text-subtitle-1 font-weight-bold">이메일 내용</span>
                        </div>
                        <v-sheet class="pa-4" color="grey-lighten-5" rounded border>
                            <div v-if="selectedMail.content" class="text-body-1" style="white-space: pre-line; line-height: 1.6;">
                                {{ selectedMail.content }}
                            </div>
                            <div v-else class="text-grey-darken-1 text-center py-4">
                                <v-icon size="large" color="grey">mdi-text-off</v-icon>
                                <p class="mt-2">내용이 없습니다.</p>
                            </div>
                        </v-sheet>
                    </div>
                </v-card-text>
                <v-card-actions class="pa-4">
                    <v-spacer />
                    <v-btn color="primary" variant="outlined" @click="dialog = false">
                        <v-icon class="mr-1">mdi-close</v-icon>
                        닫기
                    </v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>
    </v-container>
</template>

<script setup>
import { onMounted, ref, computed } from 'vue'
import { useMailStore } from '@/stores/mailStore'
import { useMemberStore } from '@/stores/memberStore'
import { storeToRefs } from 'pinia'
import dayjs from 'dayjs'

const mailStore = useMailStore()
const memberStore = useMemberStore()
const { mails, loading, error } = storeToRefs(mailStore)
const selectedMail = ref(null)
const dialog = ref(false)

const headers = [
    { title: '이메일', value: 'email' },
    { title: '제목', value: 'title' },
    { title: '보낸 사람', value: 'senderName' },
    { title: '보낸 시각', value: 'sendedAt' },
    { title: '작업', value: 'actions', sortable: false, width: 120 }
]

// 보낸 사람 이름을 가져오는 함수
const getSenderName = (senderId) => {
    // 현재 로그인한 사용자가 보낸 경우
    if (memberStore.form.id === senderId) {
        return memberStore.form.name || '알 수 없음'
    }
    
    // 다른 사용자가 보낸 경우 (향후 확장 가능)
    // 여기서는 일단 ID로 표시하거나, 사용자 목록에서 조회할 수 있음
    return `사용자 ${senderId}` || '알 수 없음'
}

const fetchAll = async () => {
    try {
        await mailStore.fetchAllMails()
    } catch (e) {
        console.error('이메일 로딩 실패:', e.message)
    }
}

const viewMailDetails = (mail) => {
    selectedMail.value = mail
    dialog.value = true
}

const formatDate = (datetime) => {
    if (!datetime) return '날짜 없음'
    return dayjs(datetime).format('YYYY-MM-DD HH:mm:ss')
}

onMounted(async () => {
    try {
        // 사용자 정보가 없으면 로드
        if (!memberStore.form.id) {
            await memberStore.getMyInfo()
        }
        await fetchAll()
    } catch (error) {
        console.error('초기화 실패:', error)
    }
})
</script>