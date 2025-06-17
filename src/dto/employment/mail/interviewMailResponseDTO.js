export default class InterviewMailResponseDTO {
    constructor(id, recruitmentId, recruitmentTitle, applicantId, applicantName, email, interviewId, interviewDatetime, interviewAddress) {
        this.id = id;
        this.recruitmentId = recruitmentId;
        this.recruitmentTitle = recruitmentTitle;
        this.applicantId = applicantId;
        this.applicantName = applicantName;
        this.email = email;
        this.interviewId = interviewId;
        this.interviewDatetime = interviewDatetime;
        this.interviewAddress = interviewAddress;
    }

    static fromJSON(json) {
        return new InterviewMailResponseDTO(
            json.id,
            json.recruitmentId,
            json.recruitmentTitle,
            json.applicantId,
            json.applicantName,
            json.email,
            json.interviewId,
            json.interviewDatetime,
            json.interviewAddressFFF
        );
    }

    toJSON() {
        return {
            id: this.id,
            recruitmentId: this.recruitmentId,
            recruitmentTitle: this.recruitmentTitle,
            applicantId: this.applicantId,
            applicantName: this.applicantName,
            email: this.email,
            interviewId: this.interviewId,
            interviewDatetime: this.interviewDatetime,
            interviewAddress: this.interviewAddress
        };
    }
}