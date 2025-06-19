import { defineStore } from 'pinia';
import { ref } from 'vue';
import { verifyJobtestEntryService } from '@/services/jobtestService';
import { JobtestEntryRequestDTO } from '@/dto/employment/jobtest/jobtestEntryRequestDTO';
import { postAnswer } from '@/services/answerService'

function mapApiQuestions(apiQuestions) {
    return apiQuestions.map(q => ({
        ...q,
        type: mapApiTypeToTypeName(q.type),
        options: (q.options || []).map(opt => opt.content)
    }));
}

export const useJobtestExamStore = defineStore('jobtestExam', () => {
    const errorMessage = ref('');
    const examData = ref(null);

    /**
     * 입장 코드 검증
     */
    const verifyEntryCode = async (jobtestId, entryCode) => {
        errorMessage.value = '';

        try {
            const dto = new JobtestEntryRequestDTO(entryCode);
            const data = await verifyJobtestEntryService(jobtestId, dto, { redirect: false });
            return data;
        } catch (err) {
            errorMessage.value = err?.message || '입장 코드 검증 중 오류가 발생했습니다.';
            throw err;
        }
    };

    async function fetchExamData(apiResponse) {
        // apiResponse.data.questions를 가공해서 저장
        examData.value = {
            ...apiResponse.data,
            questions: mapApiQuestions(apiResponse.data.questions)
        };
    }

    async function saveAnswer({ content, applicationJobTestId, questionId }) {
        return await postAnswer({ content, applicationJobTestId, questionId })
    }

    return {
        errorMessage,
        verifyEntryCode,
        examData,
        fetchExamData,
        saveAnswer,
    };
}, {
    persist: {
        enabled: true,
        strategies: [
            { storage: localStorage, paths: ['examData'] }
        ]
    }
});