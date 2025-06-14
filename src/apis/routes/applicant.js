// ✅ 파일: src/apis/applicant.js

export const ApplicantAPI = {
  BASE: '/api/v1/applicants',
  FETCH_ALL: '/api/v1/applicants',
  ASSIGN_TEST: '/api/v1/applicants/assign-test',
  UPDATE_STATUS: (id) => `/api/v1/applicants/${id}/status`,
};
