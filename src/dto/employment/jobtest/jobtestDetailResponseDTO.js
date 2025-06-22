export default class JobtestDetailResponseDTO {
  constructor(data) {
    this.id = data.id
    this.title = data.title
    this.testTime = data.testTime
    this.difficulty = data.difficulty
    this.createdName = data.createdName
    this.updatedName = data.updatedName
    this.createdAt = data.createdAt
    this.updatedAt = data.updatedAt
    this.startedAt = data.startedAt
    this.endedAt = data.endedAt
    this.questions = (data.questions || []).map(q => ({
      id: q.questionId,
      content: q.content,
      score: q.score,
      type: q.type,
      difficulty: q.difficulty,
    }))
    this.applications = data.applications || []
  }
} 