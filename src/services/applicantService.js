// src/services/applicantService.js
import api from '@/utils/api'
import { API } from '@/api'

export const fetchApplicantsService = () => {
  return api.get(API.APPLICANT.LIST)
}
