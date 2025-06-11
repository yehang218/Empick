import { defineStore } from 'pinia'
import MemberSignUpRequestDTO from '@/dto/member/memberSignUpRequestDTO'
import { useMemberStore } from '@/stores/memberStore'
import { useFileStore } from '@/stores/fileStore'
import MailRequestDTO from '@/dto/mail/mailRequestDTO'
import { useMailStore } from '@/stores/mailStore'

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
            status: 0,
            departmentId: null,
            positionId: null,
            jobId: null,
            rankId: null,
        },
        employeeNumber: '',
        profileImageFile: null,
        profileImageUrl: '',
        alertMessage: '',
        alertVisible: false,
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
            if (!file.type.startsWith('image/')) {
                this.showAlert('이미지 파일만 업로드할 수 있습니다.')
                return
            }
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
                status: 0,
                departmentId: null,
                positionId: null,
                jobId: null,
                rankId: null,
            }
            this.employeeNumber = ''
            this.profileImageFile = null
            this.profileImageUrl = ''
        },
        showAlert(msg) {
            this.alertMessage = msg
            this.alertVisible = true
            setTimeout(() => {
                this.alertVisible = false
                this.alertMessage = ''
            }, 2000)
        },
        async registerMemberWithImage() {
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
                this.showAlert('다음 항목을 입력해 주세요: ' + missing.map(key => fieldLabels[key] || key).join(', '))
                return false
            }
            if (!this.employeeNumber) this.generateRandomEmployeeNumber()
            this.form.pictureUrl = `profiles/${this.employeeNumber}.png`
            const body = new MemberSignUpRequestDTO({
                ...this.form,
                hireAt: this.form.hireAt ? new Date(this.form.hireAt).toISOString() : '',
                birth: this.form.birth,
            })
            const memberStore = useMemberStore()
            try {
                const registerResult = await memberStore.registerMember(body)
                this.showAlert('사원 등록이 완료되었습니다!')
                if (this.profileImageFile) {
                    try {
                        const fileStore = useFileStore()
                        const prefix = 'profiles/'
                        const fileName = `${this.employeeNumber}.png`
                        await fileStore.uploadProfileImage(this.profileImageFile, prefix, fileName)
                        this.showAlert('프로필 이미지 업로드가 완료되었습니다!')
                    } catch (e) {
                        this.showAlert('프로필 이미지 업로드에 실패했습니다.')
                    }
                }
                this.resetForm()
                // TODO: 등록 후 이동/초기화 등 처리
                const employeeNumber = registerResult?.data?.employeeNumber
                const email = this.form.email
                if (employeeNumber && email) {
                    const mailStore = useMailStore()
                    const mailDto = new MailRequestDTO({
                        email,
                        title: '사번 및 임시 비밀번호 안내',
                        content: `사번: ${employeeNumber}\n임시 비밀번호: ${employeeNumber}\n로그인 후 비밀번호를 꼭 변경하세요.`
                    })
                    await mailStore.createMail(mailDto)
                }
                return true
            } catch (err) {
                this.showAlert('사원 등록에 실패했습니다.')
                return false
            }
        }
    }
}) 