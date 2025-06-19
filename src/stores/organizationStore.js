import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useOrganizationStore = defineStore('organization', () => {
    // 상태
    const departments = ref([
        { label: '인사', value: 1 },
        { label: '개발', value: 2 },
        { label: '영업', value: 3 },
        { label: '기타', value: 4 },
    ])

    const positions = ref([
        { label: '미지정', value: 0 },
        { label: '팀장', value: 1 },
        { label: '부장', value: 2 },
    ])

    const jobs = ref([
        { label: '미지정', value: 0 },
        { label: '개발자', value: 1 },
        { label: '디자이너', value: 2 },
    ])

    const ranks = ref([
        { label: '사원', value: 0 },
        { label: '대리', value: 1 },
        { label: '과장', value: 2 },
    ])

    // 액션 (향후 API 연동 시 사용)
    const fetchDepartments = async () => {
        // TODO: API 호출로 부서 목록 가져오기
        console.log('부서 목록 로드')
    }

    const fetchPositions = async () => {
        // TODO: API 호출로 직책 목록 가져오기
        console.log('직책 목록 로드')
    }

    const fetchJobs = async () => {
        // TODO: API 호출로 직무 목록 가져오기
        console.log('직무 목록 로드')
    }

    const fetchRanks = async () => {
        // TODO: API 호출로 직급 목록 가져오기
        console.log('직급 목록 로드')
    }

    // 모든 조직 정보 로드
    const fetchAllOrganizationData = async () => {
        await Promise.all([
            fetchDepartments(),
            fetchPositions(),
            fetchJobs(),
            fetchRanks()
        ])
    }

    return {
        // 상태
        departments,
        positions,
        jobs,
        ranks,

        // 액션
        fetchDepartments,
        fetchPositions,
        fetchJobs,
        fetchRanks,
        fetchAllOrganizationData
    }
}) 