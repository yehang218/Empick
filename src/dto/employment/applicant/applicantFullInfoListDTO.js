class ApplicantFullInfoListDTO {
    constructor(
        applicantId,
        name,
        phone,
        email,
        profileUrl,
        birth,
        address,
        recruitmentId,
        introduceRatingResultId,
        jobId,
        jobName,
        createdAt,
        status,
        updatedAt,
        updatedBy
    ) {
        this.applicantId = applicantId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.profileUrl = profileUrl;
        this.birth = birth;
        this.address = address;
        this.recruitmentId = recruitmentId;
        this.introduceRatingResultId = introduceRatingResultId;
        this.jobId = jobId;
        this.jobName = jobName;
        this.createdAt = createdAt;
        this.status = status;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

    static fromJSON(json) {
        return new ApplicantFullInfoListDTO(
            json.applicantId,
            json.name,
            json.phone,
            json.email,
            json.profileUrl,
            json.birth,
            json.address,
            json.recruitmentId,
            json.introduceRatingResultId,
            json.jobId,
            json.jobName,
            json.createdAt,
            json.status,
            json.updatedAt,
            json.updatedBy
        );
    }
}

export default ApplicantFullInfoListDTO;