<template>
    <v-container v-if="loading" class="d-flex justify-center align-center" style="height: 300px;">
        <v-progress-circular indeterminate color="primary" size="60" />
    </v-container>

    <v-container v-else-if="jobtest" fluid class="py-12" max-width="800px">
        <v-card>
            <v-card-text>
                <h2 class="text-h5 font-weight-bold mb-6">입장 코드 입력</h2>
                <p class="mb-6">시험 준비가 완료되었습니다.</p>

                <!-- 시험 정보 -->
                <v-card class="pa-6 mb-6" variant="tonal">
                    <v-row class="mb-1">
                        <v-col cols="4"><strong>시험 일정</strong></v-col>
                        <v-col>{{ formatDateTime(jobtest.startedAt) }} ~ {{ formatDateTime(jobtest.endedAt) }}</v-col>
                    </v-row>
                    <v-row class="mb-1">
                        <v-col cols="4"><strong>시험 시간</strong></v-col>
                        <v-col>{{ jobtest.testTime }}분</v-col>
                    </v-row>
                    <v-row class="mb-1">
                        <v-col cols="4"><strong>시험 문항</strong></v-col>
                        <v-col>총 {{ jobtest.questions.length }}문제</v-col>
                    </v-row>
                </v-card>

                <!-- 안내사항 -->
                <div class="mb-6 ml-5">
                    <h2 class="text-h7 font-weight-bold mb-2">다음 안내를 확인해주세요.</h2>
                    <ul>
                        <li>다른 문제로 이동하더라도 선택했던 답은 그대로 유지됩니다.</li>
                        <li>시험 도중 창이 닫히더라도 다시 접속해 이어서 응시할 수 있습니다.</li>
                        <li>각 문제마다 답안을 여러 번 선택할 수 있으며, 그 중 가장 마지막으로 선택한 답안이 최종 답안이 됩니다. 미 선택 시 점수를 매길 수 없으니 시험 종료 전까지 꼭
                            답안을 선택해주세요.</li>
                    </ul>
                </div>

                <!-- 입장 코드 입력 -->
                <v-row class="mt-6">
                    <v-col cols="12" sm="8">
                        <v-text-field v-model="entryCodeInput" label="입장 코드를 입력해주세요." outlined clearable />
                    </v-col>
                    <v-col cols="12" sm="4">
                        <v-btn color="success" block :loading="isSubmitting" :disabled="isSubmitting"
                            @click="handleEntry">
                            시험 시작
                        </v-btn>
                    </v-col>
                </v-row>

                <!-- 에러 메시지 -->
                <v-alert v-if="errorMessage" type="error" class="mt-4">
                    {{ errorMessage }}
                </v-alert>
            </v-card-text>
        </v-card>
    </v-container>

    <AlertModal v-if="showModal" :message="modalMessage" @cancel="showModal = false" @confirm="showModal = false" />
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useJobtestDetailStore } from '@/stores/jobtestDetailStore'
import { useJobtestEntryStore } from '@/stores/jobtestEntryStore';
import AlertModal from '@/components/common/AlertModal.vue'

const router = useRouter()

const props = defineProps(['jobtestId'])
const jobtestDetailStore = useJobtestDetailStore()
const jobtestEntryStore = useJobtestEntryStore()

const entryCodeInput = ref('')
const errorMessage = ref('')
const loading = ref(true)

const showModal = ref(false)
const modalMessage = ref('')

const isSubmitting = ref(false)

onMounted(async () => {
    await jobtestDetailStore.fetchJobtestDetail(Number(props.jobtestId))
    loading.value = false
})

const jobtest = computed(() => jobtestDetailStore.jobtest)

const handleEntry = async () => {
    isSubmitting.value = true
    try {
        const resultMessage = await jobtestEntryStore.verifyEntryCode(Number(props.jobtestId), entryCodeInput.value);
        modalMessage.value = resultMessage
        // router.push({ name: 'JobtestStart' });
        window.location.href = 'https://school.programmers.co.kr/learn/courses/30/lessons/12932';
    } catch (err) {

    } finally {
        isSubmitting.value = false
    }
};

function formatDateTime(datetimeStr) {
    if (!datetimeStr) return '-'
    const date = new Date(datetimeStr)
    const y = date.getFullYear()
    const m = (date.getMonth() + 1).toString().padStart(2, '0')
    const d = date.getDate().toString().padStart(2, '0')
    const h = date.getHours().toString().padStart(2, '0')
    const min = date.getMinutes().toString().padStart(2, '0')
    return `${y}-${m}-${d} ${h}:${min}`
}
</script>
