export default class LoginRequestDTO {
    constructor(employeeNumber, password) {
        if (!employeeNumber || !password) {
            throw new Error('사원번호와 비밀번호는 필수 입력 항목입니다.');
        }

        this.employeeNumber = employeeNumber;
        this.password = password;
    }

    toJSON() {
        return {
            employeeNumber: this.employeeNumber,
            password: this.password
        };
    }
}