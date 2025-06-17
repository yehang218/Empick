// stores/recruitmentProcessStore.js
import { defineStore } from 'pinia'
import { ref } from 'vue'
import { fetchRecruitmentProcesses } from '@/services/recruitmentProcessService'

export const useRecruitmentProcessStore = defineStore('recruitmentProcess', () => {
    const processList = ref([])

    const loadProcesses = async (recruitmentId) => {
        try {
            const result = await fetchRecruitmentProcesses(recruitmentId)
            processList.value = Array.isArray(result) ? result : []  // ✅ 안전하게 할당
        } catch (err) {
            console.error('채용 프로세스 조회 실패:', err)
            processList.value = [] // ✅ 실패 시도 안전하게 초기화
        }
    }

    return {
        processList,
        loadProcesses  // ✅ 반드시 반환
    }
})
