class ApplicantFullInfoListDTO {
    constructor(
        applicantId,
        applicationId,
        name,
        phone,
        email,
        profileUrl,
        birth,
        address,
        recruitmentId,
        recruitmentTitle,
        introduceRatingResultId,
        jobId,
        jobName,
        createdAt,
        status,
        updatedAt,
        updatedBy,
        // 추가된 필드들
        introduceEvaluationContent,
        introduceScore,
        introduceStatus,
        motivation,
        experience,
        skills,
        education,
        portfolioUrl,
        coverLetter,
        interviewScore,
        interviewAddress,
        interviewDatetime,
        
        // 실무테스트 관련
        applicationJobtestId,
        applicationJobtestTitle,
        jobtestTotalScore,
        jobtestEvaluationScore,
    ) {
        // 기존 필드들
        this.applicantId = applicantId;
        this.applicationId = applicationId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.profileUrl = profileUrl;
        this.birth = birth;
        this.address = address;
        this.recruitmentId = recruitmentId;
        this.recruitmentTitle = recruitmentTitle;
        this.introduceRatingResultId = introduceRatingResultId;
        this.jobId = jobId;
        this.jobName = jobName;
        this.createdAt = createdAt;
        this.status = status;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;

        // 추가된 필드들
        this.introduceEvaluationContent = introduceEvaluationContent;
        this.introduceScore = introduceScore;
        this.introduceStatus = introduceStatus;
        this.motivation = motivation;
        this.experience = experience;
        this.skills = skills;
        this.education = education;
        this.portfolioUrl = portfolioUrl;
        this.coverLetter = coverLetter;
        this.interviewScore = interviewScore;
        this.interviewAddress = interviewAddress;
        this.interviewDatetime = interviewDatetime;

        this.applicationJobtestId = applicationJobtestId;
        this.jobtestTotalScore = jobtestTotalScore;
        this.jobtestEvaluationScore = jobtestEvaluationScore;
        this.applicationJobtestTitle = applicationJobtestTitle;
    }

    static fromJSON(json) {
        return new ApplicantFullInfoListDTO(
            json.applicantId,
            json.applicationId,
            json.name,
            json.phone,
            json.email,
            json.profileUrl,
            json.birth,
            json.address,
            json.recruitmentId,
            json.recruitmentTitle,
            json.introduceRatingResultId,
            json.jobId,
            json.jobName,
            json.createdAt,
            json.status,
            json.updatedAt,
            json.updatedBy,
            // 추가된 필드들
            json.introduceEvaluationContent,
            json.introduceScore,
            json.introduceStatus,
            json.motivation,
            json.experience,
            json.skills,
            json.education,
            json.portfolioUrl,
            json.coverLetter,
            json.interviewScore,
            json.interviewAddress,
            json.interviewDatetime,
            
            // 실무테스트
            json.applicationJobtestId,
            json.applicationJobtestTitle,
            json.jobtestTotalScore,
            json.jobtestEvaluationScore
        );
    }
}

export default ApplicantFullInfoListDTO;