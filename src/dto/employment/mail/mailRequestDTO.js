export default class MailRequestDTO {
    constructor(email, title, content, applicantId = null) {
        if (!email || !title || !content) {
            throw new Error('이메일, 제목, 내용은 필수 입력 항목입니다.');
        }
        this.email = email;
        this.title = title;
        this.content = content;
        this.applicantId = applicantId;
        this.sendedAt = new Date().toISOString();
    }

    toJSON() {
        return {
            email: this.email,
            title: this.title,
            content: this.content,
            applicantId: this.applicantId,
            sendedAt: this.sendedAt
        };
    }
}