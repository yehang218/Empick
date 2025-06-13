import { defineStore } from 'pinia';
import { registerMemberService, getMyInfoService, updateMyInfoService, profileImageFetchService, profileImageUploadService } from '@/services/memberService';

export const useMemberStore = defineStore('member', {
    state: () => ({
        form: {
            id: null,
            employeeNumber: null,
            name: '',
            email: '',
            phone: '',
            departmentName: '',
            positionName: '',
            jobName: '',
            rankName: '',
            pictureUrl: '',
            status: 0,
            hireAt: null,
            resignAt: null
        },
        registerResult: null,
        registerError: null,
        loading: false,
        error: '',
        profileImageUrl: '',
        defaultProfileImageUrl: '/images/default-profile.png'
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
                const response = await getMyInfoService();
                console.log('API 응답 데이터:', response);

                if (response && response.data && response.data.data) {
                    const result = response.data.data;
                    console.log('실제 데이터:', result);

                    this.form = {
                        id: result.id || null,
                        employeeNumber: result.employeeNumber || null,
                        name: result.name || '',
                        email: result.email || '',
                        phone: result.phone || '',
                        departmentName: result.departmentName || '',
                        positionName: result.positionName || '',
                        jobName: result.jobName || '',
                        rankName: result.rankName || '',
                        pictureUrl: result.pictureUrl || '',
                        status: result.status || 0,
                        hireAt: result.hireAt || null,
                        resignAt: result.resignAt || null
                    };
                    console.log('form 업데이트 결과:', this.form);
                }
                return response;
            } catch (err) {
                console.error('내 정보 조회 중 오류:', err);
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
                // 입력값 검증
                if (!this.form.name?.trim()) {
                    throw new Error('이름을 입력해주세요.');
                }
                if (!/^010-\d{4}-\d{4}$/.test(this.form.phone)) {
                    throw new Error('올바른 연락처 형식이 아닙니다.');
                }
                if (!/.+@.+\..+/.test(this.form.email)) {
                    throw new Error('올바른 이메일 형식이 아닙니다.');
                }

                const result = await updateMyInfoService(this.form);
                if (result) {
                    Object.assign(this.form, result);
                }
                return result;
            } catch (err) {
                this.error = err.message || '수정 요청 중 오류가 발생했습니다.';
                throw err;
            } finally {
                this.loading = false;
            }
        },

        async fetchProfileImage(memberId) {
            try {
                const blob = await profileImageFetchService(memberId);
                console.log('받은 blob:', blob, '타입:', blob instanceof Blob, 'size:', blob.size, 'type:', blob.type);
                this.profileImageUrl = URL.createObjectURL(blob);
                console.log('profileImageUrl', this.profileImageUrl);
            } catch (err) {
                console.error('프로필 이미지 로드 실패:', err);
                this.profileImageUrl = '';
            }
        },

        async uploadProfileImage(memberId, formData) {
            try {
                const result = await profileImageUploadService(memberId, formData);
                if (result) {
                    // 업로드 성공 후 프로필 이미지 새로고침
                    await this.fetchProfileImage(memberId);
                }
                return result;
            } catch (err) {
                console.error('프로필 이미지 업로드 실패:', err);
                this.error = err.message || '프로필 이미지 업로드에 실패했습니다.';
                throw err;
            }
        }
    },
}); 