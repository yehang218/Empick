<template>
    <v-container class="py-12" max-width="800px">
        <v-card>
            <v-card-text>
                <h2 class="text-h5 font-weight-bold mb-6">실무 테스트 입장</h2>

                <!-- 시험 정보 -->
                <v-row class="mb-2">
                    <v-col cols="4"><strong>시험 제목</strong></v-col>
                    <v-col>{{ jobtest.title }}</v-col>
                </v-row>
                <v-row class="mb-2">
                    <v-col cols="4"><strong>제한 시간</strong></v-col>
                    <v-col>{{ jobtest.testTime }}분</v-col>
                </v-row>
                <v-row class="mb-2">
                    <v-col cols="4"><strong>난이도</strong></v-col>
                    <v-col>{{ getDifficultyLabel(jobtest.difficulty) }}</v-col>
                </v-row>

                <!-- 입장 코드 입력 -->
                <v-row class="mt-6">
                    <v-col cols="12" sm="8">
                        <v-text-field v-model="entryCodeInput" label="입장 코드" placeholder="지정된 입장 코드를 입력하세요" outlined
                            clearable />
                    </v-col>
                    <v-col cols="12" sm="4">
                        <v-btn color="primary" class="mt-1" block :disabled="entryCodeInput !== jobtest.entryCode"
                            @click="startTest">
                            입장하기
                        </v-btn>
                    </v-col>
                </v-row>

                <v-alert v-if="entryCodeInput && entryCodeInput !== jobtest.entryCode" type="error" class="mt-4">
                    입장 코드가 올바르지 않습니다.
                </v-alert>
            </v-card-text>
        </v-card>
    </v-container>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useJobtestDetailStore } from '@/stores/jobtestDetailStore'
import { getDifficultyLabel } from '@/constants/employment/difficulty'

const route = useRoute()
const router = useRouter()
const jobtestStore = useJobtestDetailStore()
const props = defineProps(['jobtestId'])

const jobtest = jobtestStore.jobtest
const entryCodeInput = ref('')

onMounted(() => {
    jobtestStore.fetchJobtestDetail(Number(props.jobtestId)) // 상세정보 로드
})

const startTest = () => {
    router.push(`/employment/jobtests/${jobtest.value.id}/solve`)
}
</script>
