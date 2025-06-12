import { defineStore } from 'pinia'
import MemberSignUpRequestDTO from '@/dto/member/memberSignUpRequestDTO'
import MailRequestDTO from '@/dto/employment/mail/mailRequestDTO'
import { useMemberStore } from '@/stores/memberStore'
import { useFileStore } from '@/stores/fileStore'
import { useMailStore } from '@/stores/mailStore'
import { useToast } from 'vue-toastification'

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
        generateRandomEmployeeNumber() {
            this.employeeNumber = Math.floor(10000 + Math.random() * 90000).toString()
        },
        setProfileImage(file) {
            this.profileImageFile = file
            const reader = new FileReader()
            reader.onload = e => {
                this.profileImageUrl = e.target.result
            }
            reader.readAsDataURL(file)
            if (!this.employeeNumber) this.generateRandomEmployeeNumber()
            this.form.pictureUrl = `profiles/${this.employeeNumber}.png`
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

        async registerMemberWithImage() {
            const toast = useToast()
            const requiredFields = [
                'name', 'phone', 'pictureUrl', 'email', 'address'
            ]
            const fieldLabels = {
                name: '이름',
                phone: '연락처',
                pictureUrl: '프로필 이미지',
                email: '이메일',
                address: '주소',
            }
            const missing = requiredFields.filter(key => !this.form[key] || this.form[key].toString().trim() === '')
            if (missing.length > 0) {
                throw new Error('다음 항목을 입력해 주세요: ' + missing.map(key => fieldLabels[key] || key).join(', '))
            }

            if (!this.employeeNumber) this.generateRandomEmployeeNumber()
            this.form.pictureUrl = `profiles/${this.employeeNumber}.png`

            const body = new MemberSignUpRequestDTO({
                ...this.form,
                hireAt: this.form.hireAt ? new Date(this.form.hireAt).toISOString() : '',
                birth: this.form.birth,
            })

            const memberStore = useMemberStore()
            let registerResult = null

            try {
                // 1. 사원 등록
                registerResult = await memberStore.registerMember(body)
                if (!registerResult?.success) throw new Error('사원 등록에 실패했습니다.')

                // 2. 프로필 이미지 업로드 (실패해도 계속 진행)
                if (this.profileImageFile) {
                    const uploadResult = await this.uploadProfileImage(this.employeeNumber)
                    if (!uploadResult?.success && uploadResult?.error) {
                        toast.warning(uploadResult.error)
                    }
                }

                // 3. 이메일 발송 (비동기)
                this.sendWelcomeEmail(this.employeeNumber, this.form.name, this.form.email)
                    .then(result => {
                        if (!result?.success && result?.error) {
                            console.error(result.error)
                        }
                    })

                // 4. 성공 메시지 (한 번만!)
                // toast.success('사원 등록이 완료되었습니다.')
                this.resetForm()
                return true
            } catch (err) {
                // toast.error('사원 등록에 실패했습니다.')
                throw err
            }
        }
    }
}) 