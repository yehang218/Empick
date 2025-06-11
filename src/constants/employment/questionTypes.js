/**
 * 실무테스트 문제 유형 매핑
 */
export const QUESTION_TYPE_MAP = {
    'MULTIPLE': '선택형',
    'SUBJECTIVE': '단답형',
    'DESCRIPTIVE': '서술형'
};

/**
 * 실무테스트 문제 유형 한글화
 * @param {keyof typeof QUESTION_TYPE_MAP} type - 문제 유형
 * @returns {string} 한글화된 문제 유형
 */
export const getQuestionTypeLabel = (type) => {
    return QUESTION_TYPE_MAP[type] || type;
}; 