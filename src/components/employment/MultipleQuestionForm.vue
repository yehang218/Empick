<template>
    <div>
        <!-- 문제 본문 입력 -->
        <v-textarea label="문제를 입력해주세요." v-model="form.content" auto-grow :error-messages="errors.content" />


        <!-- 보기 입력 + 정답 선택 -->
        <div class="mt-4">
            <div class="text-subtitle-1 mb-2">보기</div>
            <div v-for="(option, idx) in form.questionOptions" :key="idx" class="d-flex align-center mb-2">

                <!-- 정답 선택 버튼 -->
                <v-btn icon :color="option.isAnswer ? 'success' : 'grey'" variant="tonal" class="mr-2"
                    @click="selectAnswer(idx)">
                    <v-icon>{{ option.isAnswer ? 'mdi-check-circle' : 'mdi-circle-outline' }}</v-icon>
                </v-btn>

                <!-- 보기 내용 입력 -->
                <v-text-field v-model="option.content" :label="`보기 ${idx + 1}`" class="flex-grow-1 mr-2"
                    :error-messages="errors.questionOptions" />

                <!-- 보기 삭제 버튼 -->
                <v-btn icon="mdi-close" variant="text" color="error" @click="removeOption(idx)"
                    :disabled="form.questionOptions.length <= 1" />
            </div>

            <!-- 보기 추가 버튼 -->
            <v-btn variant="outlined" class="mt-2" @click="addOption" :disabled="form.questionOptions.length >= 5">
                + 항목 추가하기
            </v-btn>
            <div v-if="form.questionOptions.length >= 5" class="text-caption text-error">
                최대 5개의 보기만 추가할 수 있습니다.
            </div>
        </div>
    </div>
</template>

<script setup>
import { onMounted, watch } from 'vue'

const props = defineProps({
    form: Object,
    errors: {
        type: Object,
        default: () => ({})
    }
});

// 보기 추가
function addOption() {
    if (props.form.questionOptions.length >= 5) return;
    props.form.questionOptions.push({ content: '', isAnswer: false });
}

// 보기 제거
function removeOption(index) {
    if (props.form.questionOptions.length <= 1) return;
    props.form.questionOptions.splice(index, 1);
}

// 정답 선택
function selectAnswer(selectedIdx) {
    props.form.questionOptions.forEach((opt, idx) => {
        opt.isAnswer = idx === selectedIdx;
    });
    // 자동으로 form.answer도 설정
    props.form.answer = props.form.questionOptions[selectedIdx].content;
}

function initializeCorrectOption() {
    if (
        !props.form ||
        !props.form.answer ||
        !props.form.questionOptions ||
        props.form.questionOptions.length === 0
    ) return;

    props.form.questionOptions.forEach(option => {
        option.isAnswer = option.content === props.form.answer;
    });
}

onMounted(() => {
    initializeCorrectOption()
})

watch(() => props.form.answer, () => {
    initializeCorrectOption()
})
</script>