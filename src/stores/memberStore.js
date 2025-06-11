import { defineStore } from 'pinia';
import { registerMemberService, getMyInfoService, updateMyInfoService } from '@/services/memberService';

export const useMemberStore = defineStore('member', {
    state: () => ({
        form: {
            name: '',
            birth: '',
            phone: '',
            email: '',
            address: '',
            departmentId: '',
            positionId: '',
            jobId: '',
            rankId: '',
            // profileImageUrl: '',
            // profileImageFile: null,
        },
        registerResult: null,
        registerError: null,
        loading: false,
        error: '',
        profileImageUrl: '', // Blob URL 저장
    }),
    actions: {
        async registerMember(memberData) {
            this.loading = true;
            this.registerError = null;
            try {
                const result = await registerMemberService(memberData);
                this.registerResult = result;
                return result;
            } catch (err) {
                this.registerError = err.message;
                throw err;
            } finally {
                this.loading = false;
            }
        },

        async getMyInfo() {
            this.loading = true;
            this.error = '';
            try {
                const result = await getMyInfoService();
                if (result) {
                    Object.assign(this.form, result);
                }
                return result;
            } catch (err) {
                this.error = err.message || '내 정보 조회 중 오류가 발생했습니다.';
                throw err;
            } finally {
                this.loading = false;
            }
        },

        async updateMyInfo() {
            this.loading = true;
            this.error = '';
            try {
                // updateMyInfoService는 put/patch /api/v1/members/me로 구현되어 있다고 가정
                const result = await updateMyInfoService({ ...this.form });
                // 성공 시 최신 정보로 갱신
                Object.assign(this.form, result);
                return result;
            } catch (err) {
                this.error = err.message || '수정 요청 중 오류가 발생했습니다.';
                throw err;
            } finally {
                this.loading = false;
            }
        },

        async profileImageFetch() {
            this.loading = true;
            this.error = '';
            try {
                const result = await profileImageFetchService(this.form.id);
            } catch (err) {
                this.error = err.message || '프로필 이미지 조회 중 오류가 발생했습니다.';
                throw err;
            } finally {
                this.loading = false;
            }
        },

        async fetchProfileImage(memberId) {
            try {
                const response = await api.get(`/api/v1/members/${memberId}/profile-image`, {
                    responseType: 'blob'
                });
                const blob = response.data;
                this.profileImageUrl = URL.createObjectURL(blob);
            } catch (err) {
                this.profileImageUrl = '';
            }
        },
    },
}); 