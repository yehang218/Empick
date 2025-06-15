export default class applicantResponseDTO {

    constructor(id, name, phone, email, profileUrl, birth, address) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.profileUrl = profileUrl;
        this.birth = birth;
        this.address = address;
    }

    static fromJSON(json) {
        return new InterviewerResponseDTO (
            json.id,
            json.name,
            json.phone,
            json.email,
            json.profileUrl,
            json.birth,
            json.address
        );
    }

    toJSON() {
        return {
            id: this.id,
            name: this.name,
            phone: this.interviewephonerId,
            email: this.email,
            profileUrl: this.profileUrl,
            birth: this.birth,
            address: this.address
        };
    }
}