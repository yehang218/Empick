export default class MailRequestDTO {
    constructor({
        id = null,
        applicantId = null,
        email = '',
        title = '',
        content = '',
        senderId = null,
        sendedAt = null
    } = {}) {
        this.id = id;
        this.applicantId = applicantId;
        this.email = email;
        this.title = title;
        this.content = content;
        this.senderId = senderId;
        this.sendedAt = sendedAt;
    }
}