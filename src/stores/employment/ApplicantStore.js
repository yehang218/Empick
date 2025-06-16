import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { useToast } from 'vue-toastification'
import { useApplicantService } from '@/services/employment/ApplicantService'

export const useApplicantStore = defineStore('applicant', () => {
    const toast = useToast()
    const applicantService = useApplicantService()
    const applicantFullinfoListDTO = ref([])
    const searchQuery = ref('')
    const isLoading = ref(false)

    const applicantFullinfoList = computed(() => {
        if (!searchQuery.value) return applicantFullinfoListDTO.value

        const query = searchQuery.value.toLowerCase()
        return applicantFullinfoListDTO.value.filter(applicant => {
            return (
                applicant.name?.toLowerCase().includes(query) ||
                applicant.email?.toLowerCase().includes(query) ||
                applicant.phone?.toLowerCase().includes(query) ||
                applicant.jobTitle?.toLowerCase().includes(query)
            )
        })
    })

    const fetchApplicantFullInfoList = async () => {
        try {
            isLoading.value = true
            const response = await applicantService.getApplicantFullInfoList()
            if (response.data) {
                applicantFullinfoListDTO.value = response.data
            }
        } catch (error) {
            console.error('지원자 목록 조회 실패:', error)
            toast.error('지원자 목록을 불러오는데 실패했습니다.')
        } finally {
            isLoading.value = false
        }
    }

    const setSearchQuery = (query) => {
        searchQuery.value = query
    }

    return {
        applicantFullinfoList,
        searchQuery,
        isLoading,
        fetchApplicantFullInfoList,
        setSearchQuery
    }
}) 