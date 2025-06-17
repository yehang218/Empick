export const MemberAPI = {
    REGISTER: '/api/v1/members',
    ME: '/api/v1/members/me',
    PROFILE_IMAGE: (memberId) => `/api/v1/members/${memberId}/profile-image`,
    ROLE: (employeeNumber) => `/api/v1/members/roles?employeeNumber=${employeeNumber}`,
    MY_ROLE: '/api/v1/members/my-roles',
}; 