// ✅ 파일: src/dto/employment/applicant/applicantListResponseDTO.js

export default class ApplicantListResponseDTO {
  constructor(id, name, email, phone, birth, address, status, profileUrl, recruitmentTitle) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.phone = phone;
    this.birth = birth;
    this.address = address;
    this.status = status;
    this.profileUrl = profileUrl;
    this.recruitmentTitle = recruitmentTitle;
  }

  static fromJSON(json) {
    return new ApplicantListResponseDTO(
      json.id,
      json.name,
      json.email,
      json.phone,
      json.birth,
      json.address,
      json.status,
      json.profileUrl,
      json.recruitmentTitle
    );
  }
}
