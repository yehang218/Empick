export default class JobtestListResponseDTO {
    constructor(id, title, difficulty, createdId, createdName, updatedId, updatedName
    ) {
        this.id = id;
        this.title = title;
        this.difficulty = difficulty;
        this.createdId = createdId;
        this.createdName = createdName;
        this.updatedId = updatedId;
        this.updatedName = updatedName;
    }

    static fromJSON(json) {
        return new JobtestListResponseDTO(
            json.id,
            json.title,
            json.difficulty,
            json.createdId,
            json.createdName,
            json.updatedId,
            json.updatedName
        );
    }

    toJSON() {
        return {
            id: this.id,
            title: this.title,
            difficulty: this.difficulty,
            createdId: this.createdId,
            createdName: this.createdName,
            updatedId: this.updatedId,
            updatedName: this.updatedName
        };
    }
}