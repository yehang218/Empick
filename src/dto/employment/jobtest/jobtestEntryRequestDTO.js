export class JobtestEntryRequestDTO {
    constructor(entryCode) {
        this.entryCode = entryCode;
    }

    toJSON() {
        return {
            entryCode: this.entryCode,
        };
    }

    static fromJSON(json) {
        return new JobtestEntryRequestDTO(
            json.entryCode
        );
    }
}
