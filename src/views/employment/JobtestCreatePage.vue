<template>
    <v-container>
        <v-row>
            <v-col cols="4">
                <h2 class="text-h6 font-weight-bold mb-4">실무 테스트 등록</h2>
            </v-col>
            <v-spacer />
            <v-col cols="2">
                <v-text-field v-model="startedAt" label="시험 시작 일정" type="datetime-local" variant="outlined" />
            </v-col>
            <v-col cols="2">
                <v-text-field v-model="endedAt" label="시험 종료 일정" type="datetime-local" variant="outlined" />
            </v-col>
            <v-col cols="1">
                <v-select v-model="difficulty" :items="difficultyOptions" label="난이도" item-title="title"
                    item-value="value" />
            </v-col>
            <v-col cols="1">
                <v-text-field v-model="testTime" label="시험 시간 (분)" type="number" />
            </v-col>
        </v-row>

        <v-text-field v-model="jobtestTitle" label="실무 테스트 이름을 입력해주세요." class="mb-4" variant="outlined" />

        <v-data-table :headers="headers" :items="localQuestions" item-value="id" class="mb-4">
            <!-- 선택 버튼 -->
            <template #item.actions="{ item }">
                <v-btn size="small" :color="selectedIds.includes(item.id) ? 'primary' : 'grey-lighten-2'" icon
                    variant="tonal" @click="toggle(item.id)" class="mr-2">
                    <v-icon>
                        {{ selectedIds.includes(item.id) ? 'mdi-checkbox-marked-circle' :
                            'mdi-checkbox-blank-circle-outline' }}
                    </v-icon>
                </v-btn>
            </template>
            <!-- 문제 유형 -->
            <template #item.type="{ item }">
                <v-chip size="small">{{ item.type }}</v-chip>
            </template>
            <!-- 난이도 -->
            <template #item.difficulty="{ item }">
                <v-chip size="small" :color="getDifficultyColor(item.difficulty)" dark>
                    {{ item.difficulty }}
                </v-chip>
            </template>
            <template #item.score="{ item }">
                <v-text-field :model-value="item.score" @update:model-value="val => item.score = Number(val)"
                    type="number" variant="underlined" density="compact" hide-details style="width: 80px;" />
            </template>
        </v-data-table>

        <div class="text-right mt-2">
            <span class="text-caption">
                선택된 문제 총점: {{selectedQuestions.reduce((sum, q) => sum + Number(q.score ?? 0), 0)}}점
            </span>
        </div>

        <div class="d-flex justify-end mt-4">
            <v-btn variant="tonal" color="grey" class="mr-2" @click="cancel">취소하기</v-btn>
            <v-btn color="primary" @click="register">등록하기</v-btn>
        </div>

        <v-dialog v-model="dialog" max-width="800">
            <v-card>
                <QuestionCreateModal @close="dialog = false" @saved="handleNewQuestion" />
            </v-card>
        </v-dialog>

        <SuccessModal v-if="showSuccessModal" message="실무 테스트가 등록되었습니다." @confirm="handleSuccessConfirm"
            @cancel="showSuccessModal = false" />
    </v-container>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useToast } from 'vue-toastification'
import { useJobtestStore } from '@/stores/jobtestQuestionStore'
import { createJobtestService } from '@/services/jobtestService'
import CreateJobtestRequestDTO from '@/dto/employment/jobtest/createJobtestRequestDTO'
import QuestionCreateModal from '@/components/employment/QuestionCreateModal.vue'
import SuccessModal from '@/components/common/AlertModal.vue'

const localQuestions = ref([])
const toast = useToast()
const jobtestTitle = ref('')
const testTime = ref(0)
const difficulty = ref('')
const dialog = ref(false)
const selectedIds = ref([])
const startedAt = ref('');
const endedAt = ref('');

const showSuccessModal = ref(false)

const router = useRouter();

const jobtestStore = useJobtestStore()

const headers = [
    { title: '', key: 'actions', sortable: false, width: 48 },
    { title: '문제', key: 'content', sortable: false },
    { title: '유형', key: 'type', sortable: false },
    { title: '난이도', key: 'difficulty', sortable: false },
    { title: '점수', key: 'score', sortable: false }
]

const difficultyOptions = [
    { title: '쉬움', value: 'EASY' },
    { title: '보통', value: 'MEDIUM' },
    { title: '어려움', value: 'HARD' }
];

onMounted(async () => {
    await jobtestStore.fetchQuestions()
    localQuestions.value = jobtestStore.questions.map(q => ({
        ...q,
        id: Number(q.id)
    }))
})

// 선택 토글 함수
const toggle = id => {
    if (selectedIds.value.includes(id)) {
        selectedIds.value = selectedIds.value.filter(x => x !== id)
    } else {
        selectedIds.value = [...selectedIds.value, id]
    }
}

// 선택된 문제 목록
const selectedQuestions = computed(() =>
    localQuestions.value.filter(q => selectedIds.value.includes(q.id))
)

const getDifficultyColor = level => {
    switch (level) {
        case '쉬움': return 'success'
        case '보통': return 'primary'
        case '어려움': return 'error'
        default: return 'grey'
    }
}

const register = async () => {
    if (!jobtestTitle.value) {
        toast.error('실무 테스트 이름을 입력해주세요.')
        return
    }
    if (!testTime.value || testTime.value <= 0) {
        toast.error('시험 시간을 입력해주세요.')
        return
    }
    if (!difficulty.value) {
        toast.error('난이도를 선택해주세요.')
        return
    }
    const selected = localQuestions.value.filter(q => selectedIds.value.includes(q.id))
    const sum = selected.reduce((acc, q) => acc + Number(q.score ?? 0), 0)
    if (sum !== 100) {
        toast.error(`선택된 문제의 총점이 ${sum}점입니다. 정확히 100점이 되도록 입력해주세요.`)
        return
    }
    const invalid = selected.find(q => q.score < 0)
    if (invalid) {
        toast.error(`"${invalid.content}" 문제의 점수가 0보다 작습니다.`)
        return
    }
    if (!startedAt.value) {
        toast.error('시험 시작 일정을 입력해주세요.');
        return;
    }
    if (!endedAt.value) {
        toast.error('시험 종료 일정을 입력해주세요.');
        return;
    }
    const dto = new CreateJobtestRequestDTO(
        jobtestTitle.value,
        difficulty.value,
        testTime.value,
        new Date(startedAt.value),
        new Date(endedAt.value),
        1,
        selected.map(q => ({
            questionId: q.id,
            score: Number(q.score ?? 0)
        }))
    );
    try {
        await createJobtestService(dto)
        showSuccessModal.value = true
    } catch (e) {
        toast.error('등록 중 오류가 발생했습니다.')
    }
}

function handleSuccessConfirm() {
    showSuccessModal.value = false
    router.push({ name: 'JobtestList' });
}

const cancel = () => {
    jobtestTitle.value = ''
    testTime.value = 30
    difficulty.value = null
    selectedIds.value = []
    toast.info('입력을 초기화했습니다.')
}

const handleNewQuestion = async () => {
    dialog.value = false
    await jobtestStore.fetchQuestions()
}
</script>

<style scoped></style>
