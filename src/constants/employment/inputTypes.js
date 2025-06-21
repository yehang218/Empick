export const INPUT_TYPE_MAP = {
    TEXT: '텍스트',
    TEXTAREA: '긴 텍스트',
    FILE: '파일 업로드',
    URL: 'URL',
    DATE: '날짜',
    NUMBER: '숫자',
    RADIO: '라디오 버튼',
    CHECKBOX: '체크박스'
}

export const getInputTypeLabel = (type) => {
    return INPUT_TYPE_MAP[type] || '알 수 없음'
}