export const RECRUIT_TYPE_MAP = {
    REGULAR: '상시',
    SEASONAL: '공채'
}

export const getRecruitTypeLabel = (type) => {
    return RECRUIT_TYPE_MAP[type] || type
}
