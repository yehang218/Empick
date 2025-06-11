export const MemberAPI = {
    REGISTER: '/api/v1/members',
    ME: '/api/v1/members/me',
    PROFILE: (memberId) => `/api/v1/members/${memberId}/profile-image`,
}; 