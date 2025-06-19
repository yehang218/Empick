import { defineStore } from 'pinia'
import MailRequestDTO from '@/dto/employment/mail/mailRequestDTO'
import { useFileStore } from '@/stores/fileStore'
import { useMailStore } from '@/stores/mailStore'
import { registerMemberService } from '@/services/memberService'

export const useMemberRegisterStore = defineStore('memberRegister', {
    state: () => ({
        form: {
            password: '',
            name: '',
            birth: '',
            phone: '',
            pictureUrl: '',
            email: '',
            address: '',
            vacationCount: 0,
            hireAt: '',
            resignAt: '',
            deletedMemberId: 0,
            updatedMemberId: 0,
            lastLoginAt: '',
            status: 1,
            departmentId: null,
            positionId: null,
            jobId: null,
            rankId: null,
        },
        employeeNumber: '',
        profileImageFile: null,
        profileImageUrl: '',
    }),
    getters: {
        isFormValid(state) {
            const requiredFields = [
                'name', 'phone', 'pictureUrl', 'email', 'address'
            ]
            return requiredFields.every(key => state.form[key] && state.form[key].toString().trim() !== '')
        },
        photoButtonText(state) {
            return state.profileImageFile ? '수정' : '등록'
        }
    },
    actions: {
        setProfileImage(file) {
            this.profileImageFile = file
            const reader = new FileReader()
            reader.onload = e => {
                this.profileImageUrl = e.target.result
            }
            reader.readAsDataURL(file)
            this.form.pictureUrl = this.employeeNumber ? `profiles/${this.employeeNumber}.png` : 'profiles/temp.png'
        },
        clearProfileImage() {
            this.profileImageFile = null
            this.profileImageUrl = ''
            this.form.pictureUrl = ''
        },
        resetForm() {
            this.form = {
                password: '',
                name: '',
                birth: '',
                phone: '',
                pictureUrl: '',
                email: '',
                address: '',
                vacationCount: 0,
                hireAt: '',
                resignAt: '',
                deletedMemberId: 0,
                updatedMemberId: 0,
                lastLoginAt: '',
                status: 1,
                departmentId: null,
                positionId: null,
                jobId: null,
                rankId: null,
            }
            this.employeeNumber = ''
            this.profileImageFile = null
            this.profileImageUrl = ''
        },

        // 📝 사원 등록 (프로필 이미지 포함)
        async registerMember(memberData, profileImage) {
            console.log('📝 사원 등록 시작:', { memberData, profileImageName: profileImage?.name });

            this.loading = true;
            this.registerError = null;
            this.registerResult = null;

            try {
                // 입력 데이터 검증
                if (!memberData) {
                    throw new Error('사원 정보가 필요합니다.');
                }

                if (!profileImage) {
                    throw new Error('프로필 이미지는 필수입니다.');
                }

                // 파일 타입 검증
                const allowedTypes = ['image/jpeg', 'image/jpg', 'image/png', 'image/webp'];
                if (!allowedTypes.includes(profileImage.type)) {
                    throw new Error('JPG, PNG, WEBP 형식의 이미지만 업로드 가능합니다.');
                }

                // 파일 크기 검증 (5MB 이하)
                const maxSize = 5 * 1024 * 1024; // 5MB
                if (profileImage.size > maxSize) {
                    throw new Error('프로필 이미지는 5MB 이하만 업로드 가능합니다.');
                }

                console.log('✅ 입력 검증 완료');

                // 서비스 호출 (DTO 처리는 Service에서 수행)
                const result = await registerMemberService(memberData, profileImage);

                this.registerResult = result;

                // 등록 후 캐시 무효화
                // this.invalidateMembersCache();

                console.log('✅ 사원 등록 성공:', result);
                return result;

            } catch (err) {
                console.error('❌ 사원 등록 실패:', err);
                this.registerError = err.message || '사원 등록 중 오류가 발생했습니다.';
                throw err;
            } finally {
                this.loading = false;
                console.log('📝 사원 등록 프로세스 완료');
            }
        },

        async sendWelcomeEmail(employeeNumber, name, email) {
            const mailStore = useMailStore()

            if (!email) {
                return { success: false, error: '이메일 주소가 없습니다.' }
            }

            try {
                const mailDto = new MailRequestDTO(
                    email,
                    '사번 및 임시 비밀번호 안내',
                    `안녕하세요, ${name}님!\n\n` +
                    `사원 등록이 완료되었습니다.\n\n` +
                    `사번: ${employeeNumber}\n` +
                    `임시 비밀번호: ${employeeNumber}\n\n` +
                    `보안을 위해 로그인 후 반드시 비밀번호를 변경해주세요.\n\n` +
                    `감사합니다.`
                )

                await mailStore.sendMail(mailDto)
                return { success: true }
            } catch (e) {
                console.error('이메일 발송 중 오류 발생:', e)
                return { success: false, error: e?.message || '이메일 발송에 실패했습니다.' }
            }
        },

        async uploadProfileImage(employeeNumber) {
            if (!this.profileImageFile) return { success: true }

            try {
                const fileStore = useFileStore()
                const prefix = 'profiles/'
                const fileName = `${employeeNumber}.png`
                await fileStore.uploadProfileImage(this.profileImageFile, prefix, fileName)
                return { success: true }
            } catch (e) {
                console.error('프로필 이미지 업로드 중 오류 발생:', e)
                return { success: false, error: e?.message || '프로필 이미지 업로드에 실패했습니다.' }
            }
        },
    }
}) 