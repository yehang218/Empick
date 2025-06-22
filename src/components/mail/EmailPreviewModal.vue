<template>
    <v-dialog v-model="dialogModel" max-width="800px">
        <v-card>
            <v-card-title class="text-h6 font-weight-bold d-flex align-center">
                <v-icon class="mr-2">mdi-eye</v-icon>
                이메일 미리보기
            </v-card-title>
            <v-card-text class="pt-4">
                <div class="mb-4">
                    <v-chip color="primary" variant="tonal" class="mb-2">
                        {{ emailType === 'jobtest' ? '실무테스트 안내 메일' : '면접 일정 안내 메일' }}
                    </v-chip>
                    <p class="text-body-2 text-grey-darken-1">
                        선택된 {{ selectedCount }}명의 지원자에게 발송됩니다.
                    </p>
                </div>

                <v-divider class="my-4"></v-divider>

                <div class="mb-4">
                    <h4 class="text-subtitle-1 font-weight-bold mb-2">받는 사람</h4>
                    <div class="d-flex flex-wrap" style="gap: 8px;">
                        <v-chip v-for="applicant in applicants" :key="applicant.uniqueKey" variant="outlined"
                            size="small">
                            {{ applicant.name }} ({{ applicant.email }})
                        </v-chip>
                    </div>
                </div>

                <div class="mb-4">
                    <h4 class="text-subtitle-1 font-weight-bold mb-2">메일 내용 미리보기</h4>
                    <v-sheet class="pa-4" color="grey-lighten-5" rounded border>
                        <div v-if="emailType === 'jobtest'" class="text-body-1">
                            <p><strong>제목:</strong> [Empick] 실무 테스트 안내드립니다</p>
                            <p><strong>내용:</strong></p>
                            <div style="white-space: pre-line; line-height: 1.6; font-family: 'Segoe UI', sans-serif;">
안녕하세요, <strong>#{이름}</strong> 님.

지원해주신 <strong>#{채용공고명}</strong> 포지션에 대한 서류 전형에 합격하셨음을 알려드립니다. 축하드립니다!

다음 단계인 실무 테스트가 아래와 같이 진행될 예정입니다.

• <strong>시작 시간:</strong> #{시작시간}
• <strong>종료 시간:</strong> #{종료시간}
• <strong>문항 수:</strong> #{문항수}
• <strong>입장 코드:</strong> #{입장코드}

테스트 관련 자세한 정보는 아래 링크를 통해 확인해주세요.
[테스트 상세 안내 보기]

본 메일은 발신 전용입니다. 문의사항은 채용 담당자에게 연락해 주세요.
                            </div>
                        </div>
                        <div v-else class="text-body-1">
                            <p><strong>제목:</strong> [Empick] 면접 일정 안내드립니다</p>
                            <p><strong>내용:</strong></p>
                            <div style="white-space: pre-line; line-height: 1.6; font-family: 'Segoe UI', sans-serif;">
안녕하세요, <strong>#{이름}</strong> 님.

귀하께서 지원하신 <strong>#{채용공고명}</strong> 채용 공고의 실무 테스트 전형에 합격하셨습니다.

다음 단계인 <strong>면접</strong>은 아래 일시에 <strong>ZOOM</strong>을 통해 비대면으로 진행됩니다.

• <strong>면접 일시:</strong> #{면접일시}

아래 링크를 클릭하여 면접 시간에 맞춰 입장해 주세요.
[ZOOM 면접 입장하기]

본 메일은 발신 전용입니다.
면접 관련 문의는 채용 담당자에게 연락 부탁드립니다.
                            </div>
                        </div>
                    </v-sheet>
                </div>
            </v-card-text>
            <v-card-actions class="pa-4">
                <v-spacer />
                <v-btn variant="outlined" @click="handleCancel">
                    취소
                </v-btn>
                <v-btn color="success" @click="handleSend" :loading="loading">
                    <v-icon class="mr-1">mdi-send</v-icon>
                    메일 발송
                </v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
    modelValue: {
        type: Boolean,
        default: false
    },
    emailType: {
        type: String,
        required: true
    },
    selectedCount: {
        type: Number,
        default: 0
    },
    applicants: {
        type: Array,
        default: () => []
    },
    loading: {
        type: Boolean,
        default: false
    }
})

const emit = defineEmits(['update:modelValue', 'send', 'cancel'])

const dialogModel = computed({
    get: () => props.modelValue,
    set: (value) => emit('update:modelValue', value)
})

const handleSend = () => {
    emit('send')
}

const handleCancel = () => {
    emit('cancel')
    dialogModel.value = false
}
</script>