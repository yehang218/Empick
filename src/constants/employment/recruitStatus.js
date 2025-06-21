export const RECRUIT_STATUS_MAP = {
    WAITING: '대기',
    PUBLISHED: '게시',
    CLOSED: '종료'
}

export const getRecruitStatusLabel = (status) => {
    return RECRUIT_STATUS_MAP[status] || status
}
