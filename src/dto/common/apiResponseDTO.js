export default class ApiResponseDTO {
    constructor(success, code, message, data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    static fromJSON(json) {
        return new ApiResponseDTO(
            json.success,
            json.code,
            json.message,
            json.data
        );
    }

    toJSON() {
        return {
            success: this.success,
            code: this.code,
            message: this.message,
            data: this.data
        };
    }
}