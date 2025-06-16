import { getApplicantFullInfoList as fetchApplicantFullInfoList } from '@/api/employment/ApplicantApi'

export const useApplicantService = () => {
    const getApplicantFullInfoList = async () => {
        return await fetchApplicantFullInfoList()
    }

    return {
        getApplicantFullInfoList
    }
} 