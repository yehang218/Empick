export default class MailResponseDTO {
    constructor(id, applicantId, email, title, content, senderId, sendedAt) {
        this.id = id;
        this.applicantId = applicantId;
        this.email = email;
        this.title = title;
        this.content = content;
        this.senderId = senderId;
        this.sendedAt = sendedAt;
    }

    static fromJSON(json) {
        return new MailResponseDTO(
            json.id,
            json.applicantId,
            json.email,
            json.title,
            json.content,
            json.senderId,
            json.sendedAt
        );
    }

    toJSON() {
        return {
            id: this.id,
            applicantId: this.applicantId,
            email: this.email,
            title: this.title,
            content: this.content,
            senderId: this.senderId,
            sendedAt: this.sendedAt
        };
    }
}